package com.origamisoftware.puzzle.util;

import com.origamisoftware.puzzle.model.AdventureMap;
import com.origamisoftware.puzzle.model.CardinalPoint;
import com.origamisoftware.puzzle.model.DepthFirstSearch;
import com.origamisoftware.puzzle.model.DijkstraAlgorithm;
import com.origamisoftware.puzzle.model.Edge;
import com.origamisoftware.puzzle.model.Graph;
import com.origamisoftware.puzzle.model.RoomNode;
import com.origamisoftware.puzzle.model.Vertex;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utilities for creating a map model from the XML data and navigating that map.
 */
public class MapUtils {

    // these XML element attribute names. If the XML changes these would have to change as well.
    final static String XML_TAG_NAME_FOR_ROOM = "room";
    final private static String XML_ROOM_ATTRIBUTE_NAME_FOR_ID = "id";
    final private static String XML_ROOM_ATTRIBUTE_KEY_FOR_ROOM_NAME = "name";
    final private static String XML_ROOM_ATTRIBUTE_KEY_FOR_OBJECT_NAME = "name";

    /**
     * Convert an XML instance to a Map of RoomNodes by Id.
     *
     * @param document the XML document
     * @return a Map where the key is the room id and the value which maps to a RoomNode.
     */
    public static Map<String, RoomNode> buildMapModelFromDocument(Document document) {

        NodeList rooms = document.getElementsByTagName(XML_TAG_NAME_FOR_ROOM);

        int count = rooms.getLength();

        /* The roomNodes will hold the data in XML room elements. Since we know that size
         * we can pre-size our array avoiding any performance penalties  due to dynamic resizing
         */
        Map<String, RoomNode> roomMapById = new HashMap<>(count);

        // assume the first room in the map.xml is the starting point
         node2RoomNode(roomMapById, rooms.item(0));

        // we already have the first room (0) now get every room element in the xml and create a roomNode for them.
        for (int index = 1; index < count; index++) {
            node2RoomNode(roomMapById, rooms.item(index));
        }

        return roomMapById;
    }

    /**
     * Find the shortest path to the room that contains the specified item and returns the list of nodes
     * starting from the specified starting node leading to the room with the item in it.
     *
     * @param adventureMap
     * @param startingPoint
     * @param itemToFind
     * @return a list of rooms, the last room in the list will be the room where the item was found.
     * and empty list means the item was not found.
     */
    public static List<RoomNode> findShortestPath(AdventureMap adventureMap, RoomNode startingPoint,
                                                  String itemToFind) {

        List<RoomNode> rooms = new ArrayList<>();
        RoomNode roomThatContains = adventureMap.getRoomThatContains(itemToFind);
        List<RoomNode> nodes = new ArrayList<>(adventureMap.getRoomsById().values());
        List<Edge> edges = new ArrayList<>();

        for (RoomNode roomNode : nodes) {
            edges.addAll(roomNode.getEdges(adventureMap.getRoomsById()));
        }

        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(startingPoint);
        LinkedList<Vertex> path = dijkstra.getPath(roomThatContains);

        int numberOfRooms = path.size();
        for (int index = 0; index < numberOfRooms; index++) {
            Vertex vertex = path.get(index);
            RoomNode roomNode = adventureMap.getRoomsById().get(vertex.getId());
            rooms.add(roomNode);
            System.out.println("In the " + roomNode.getName());
            if (path.size() != index + 1) {
                vertex = path.get(index + 1);
                RoomNode nextRoom = adventureMap.getRoomsById().get(vertex.getId());
                System.out.println("I go " + roomNode.whichWayIsThisRoom(nextRoom).toString().toLowerCase());
            }
        }

        return rooms;
    }


    private static RoomNode node2RoomNode(Map<String, RoomNode> roomMapById, Node node) {

        NamedNodeMap attributes = node.getAttributes();
        String roomId = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_NAME_FOR_ID).getNodeValue();
        String roomName = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_KEY_FOR_ROOM_NAME).getNodeValue();

        RoomNode roomNode = new RoomNode(roomName, roomId);
        roomMapById.put(roomId, roomNode);

        for (CardinalPoint linkDirection : CardinalPoint.values()) {
            String linkId = getAttributeValueOrNull(attributes, linkDirection.getXmlName());

            // lots of rooms won't have a link for each direction, that's OK
            if (linkId != null) {
                // create an edge between current room and room being pointed to.
                roomNode.addEdge(linkId, linkDirection);
            }
        }

        populateRoomContents(node, roomNode);

        return roomNode;
    }

    private static void populateRoomContents(Node node, RoomNode roomNode) {

        // the XML node will have child nodes if it has Object element which holds the room's contents.
        if (node.hasChildNodes()) {
            int length = node.getChildNodes().getLength();
            NodeList childNodes = node.getChildNodes();
            for (int index = 0; index < length; index++) {

                Node childNode = childNodes.item(index);
                short nodeType = childNode.getNodeType();
                if (nodeType == Node.ELEMENT_NODE) {
                    roomNode.setContents(childNode.getAttributes().getNamedItem(XML_ROOM_ATTRIBUTE_KEY_FOR_OBJECT_NAME)
                            .getNodeValue());
                }
            }
        }
    }

    private static String getAttributeValueOrNull(NamedNodeMap attributes, String attributeName) {
        String returnValue = null;
        Node namedItem = attributes.getNamedItem(attributeName);
        if (namedItem != null) {
            returnValue = namedItem.getNodeValue();
        }
        return returnValue;
    }

    public static void searchRooms(RoomNode startingPoint, Map<String, RoomNode> roomsById, List<String> itemsToFind,
                                   Map<String, RoomNode> roomByContents) {

       new DepthFirstSearch(roomsById,startingPoint,  itemsToFind,  roomByContents);
    }

}
