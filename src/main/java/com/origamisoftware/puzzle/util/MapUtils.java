package com.origamisoftware.puzzle.util;

import com.origamisoftware.puzzle.model.AdventureMap;
import com.origamisoftware.puzzle.model.CardinalPoint;
import com.origamisoftware.puzzle.model.DijkstraAlgorithm;
import com.origamisoftware.puzzle.model.Edge;
import com.origamisoftware.puzzle.model.Graph;
import com.origamisoftware.puzzle.model.RoomNode;
import com.origamisoftware.puzzle.model.RouteSegment;
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
import java.util.Queue;

/**
 * Utilities for creating a map model from the XML data.
 */
public class MapUtils {

    /**
     * Each node can have a link in any of the for cardinal directions
     */

    final private static String XML_TAG_NAME_FOR_ROOM = "room";
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
        RoomNode entryPoint = node2RoomNode(roomMapById, rooms.item(0));

        // we already have the first room (0) now get every room element in the xml and create a roomNode for them.
        for (int index = 1; index < count; index++) {
            node2RoomNode(roomMapById, rooms.item(index));
        }

        return roomMapById;
    }

    /**
     * Given an AdventureMap which contains both the entire map of rooms (or graph) and the starting point (initial node),
     * look in all the rooms (nodes) and if they contain an item in the itemsToFind set, record the roomNode and
     * it's contents in a Map. When all the rooms have been searched (all the nodes visited via BSF) return the
     * Map.
     *
     * @param roomsById
     * @param startingPoint
     * @param itemsToFind
     * @return
     */
    public static List<RouteSegment> findItems(Map<String, RoomNode> roomsById, RoomNode startingPoint,
                                               List<String> itemsToFind, Map<String, RoomNode> roomsByContents) {

        List<RouteSegment> path = new ArrayList<>();
        Queue<RoomNode> queue = new LinkedList<RoomNode>();

        startingPoint.visited = true;

        queue.add(startingPoint);

        while (!queue.isEmpty()) {

            RoomNode roomNode = queue.poll();

            if (itemsToFind.contains(roomNode.getContents())) {
                roomsByContents.put(roomNode.getContents(), roomNode);
                if (itemsToFind.size() == roomsByContents.size()) {
                    return path;
                }
            }

            Map<CardinalPoint, String> neighbors = roomNode.getNeighbors();

            // visit each adjoining node
            neighbors.forEach((direction, roomId) -> {

                RoomNode neighbor = roomsById.get(roomId);

                if (!neighbor.visited) {
                    neighbor.visited = true;
                    queue.add(neighbor);
                    RouteSegment e = new RouteSegment(direction, neighbor);
                    System.out.println(e.toString());
                    path.add(e);
                }


            });

        }
        return path;
    }


    // bsf
    public static boolean  findPathTo(RoomNode startingNode, Map<String, RoomNode> roomsById, String item, List<RoomNode> path) {

        Queue<RoomNode> queue = new LinkedList<RoomNode>();
        startingNode.visited = true;
        queue.add(startingNode);

        System.out.println(startingNode.getName());


        while (!queue.isEmpty()) {
            RoomNode v = queue.poll();
            if (v.getContents().equals(item)) {
                path.add(v);
                return true;
            }
            for (String roomId : v.neighbors.values()) {
                RoomNode w = roomsById.get(roomId);
                if (!w.visited) {
                    System.out.println(w.getName());
                    path.add(v);
                    w.visited = true;
                    queue.add(w);
                }
                System.out.println(v.getName());

            }
        }
        return false;
    }



    public static RoomNode findShortestPath(AdventureMap adventureMap, RoomNode startingPoint, String itemToFind) {

        RoomNode roomThatContains = adventureMap.getRoomThatContains(itemToFind);
        List<RoomNode> nodes = new ArrayList<>(adventureMap.getRoomsById().values());
        List<Edge> edges = new ArrayList<Edge>();

        for (RoomNode roomNode : nodes) {
            edges.addAll(roomNode.getEdges(adventureMap.getRoomsById()));
        }

        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(startingPoint);
        LinkedList<Vertex> path = dijkstra.getPath(roomThatContains);

        System.out.println("Starting from " + startingPoint.getName() + " the path to the " + itemToFind + " is ");
        for (Vertex vertex : path) {
            RoomNode x = adventureMap.getRoomsById().get(vertex.getId());
            System.out.println(x.getName());
        }
        return (RoomNode) path.getLast();
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

        populateRoomContents(node, roomName, roomNode);

        return roomNode;
    }

    private static void populateRoomContents(Node node, String roomName, RoomNode roomNode) {

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
}
