package com.origamisoftware.puzzle.util;

import com.origamisoftware.puzzle.graph.DijkstraAlgorithm;
import com.origamisoftware.puzzle.graph.Edge;
import com.origamisoftware.puzzle.graph.Graph;
import com.origamisoftware.puzzle.model.CardinalPoint;
import com.origamisoftware.puzzle.model.RoomNode;
import com.origamisoftware.puzzle.model.Vertex;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Utilities for creating a map model from the XML data and navigating that map.
 */
public class MapUtils {

    // these XML elements attribute names. If the XML changes, these would have to change as well.
    final static String XML_TAG_NAME_FOR_ROOM = "room";
    final private static String XML_ROOM_ATTRIBUTE_NAME_FOR_ID = "id";
    final private static String XML_ROOM_ATTRIBUTE_KEY_FOR_ROOM_NAME = "name";
    final private static String XML_ROOM_ATTRIBUTE_KEY_FOR_OBJECT_NAME = "name";

    /**
     * Convert an XML instance to a Map of RoomNodes by id.
     *
     * @param document the XML document
     * @return a Map where the key is the room id and the value which maps to a RoomNode.
     */
    public static Map<String, RoomNode> buildMapModelFromDocument(Document document) {

        NodeList rooms = document.getElementsByTagName(XML_TAG_NAME_FOR_ROOM);

        int count = rooms.getLength();

        /* The RoomNodes will hold the data in XML room elements. Since we know that size,
         * we can pre-size our array avoiding any performance penalties  due to dynamic resizing.
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
     * Convert an XML data structure (Node) to our model of a room.
     *
     * @param roomMapById a map of all the rooms by id
     * @param node the XML data structure
     * @return a RoomNode
     */
    private static RoomNode node2RoomNode(Map<String, RoomNode> roomMapById, Node node) {

        NamedNodeMap attributes = node.getAttributes();
        String roomId = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_NAME_FOR_ID).getNodeValue();
        String roomName = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_KEY_FOR_ROOM_NAME).getNodeValue();

        RoomNode roomNode = null;

        if (roomMapById.containsKey(roomId)) {
            roomNode = roomMapById.get(roomId);
            roomNode.setName(roomName);
        } else {
            roomNode = new RoomNode(roomName, roomId);
            roomMapById.put(roomId, roomNode);
        }

        for (CardinalPoint linkDirection : CardinalPoint.values()) {
            String linkId = getAttributeValueOrNull(attributes, linkDirection.getName());

            // lots of rooms won't have a link for each direction, that's OK.
            if (linkId != null) {
                RoomNode toBeCompleted = null;
                if (!roomMapById.containsKey(linkId)) {
                    toBeCompleted = new RoomNode("tbd", linkId);
                    roomMapById.put(linkId, toBeCompleted);
                }
                // create an edge between current room and room being pointed to.
                roomNode.addEdge(roomMapById.get(linkId), linkDirection);
            }
        }

        populateRoomContents(node, roomNode);

        return roomNode;
    }

    /**
     * Using the XML data determine if a room contains an item
     * and if so, put that item in the provided RoomNode
     *
     * @param node     the XML data
     * @param roomNode the model data to store the item (if one is present)
     */
    private static void populateRoomContents(Node node, RoomNode roomNode) {

        // the XML node will have child nodes if it has an Object element which holds the room's contents.
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

    /**
     * Given a list of attributes (from an XML element) and the particular attribute we are concerned with,
     * return it's value. If there is no value, return NULL>
     *
     * @param attributes    the map of attributes
     * @param attributeName the particular attribute whose value will be returned.
     * @return the value that corresponds to the attribute name provided.
     */
    private static String getAttributeValueOrNull(NamedNodeMap attributes, String attributeName) {
        String returnValue = null;
        Node namedItem = attributes.getNamedItem(attributeName);
        if (namedItem != null) {
            returnValue = namedItem.getNodeValue();
        }
        return returnValue;
    }

    /**
     * Find the shortest path to the room that contains the specified item.
     * <p>
     * The list of rooms must all be connected (i.e. the graph must be connected.)
     * The item must be in one of the rooms. If either of these requirements is not true
     * an InvalidGraphSearchParametersException will be thrown.
     *
     * @param roomsByContents a map of the rooms by contents. The key is the item. The value is RoomNode the item is in.
     * @param roomsById       a map of all the RoomNodes by their ID.
     * @param startingPoint   the RoomNode to start looking in.
     * @param itemToFind      the item to find.
     * @return The list of rooms that need to be traversed in order to find the item.
     * The last room in the list will be the room where the item was found.
     * @throws InvalidGraphSearchParametersException if the item is not in any of the rooms, or if the graph is not
     *                                               connected.
     */
    public static List<RoomNode> findShortestPath(Map<String, RoomNode> roomsById,
                                                  Map<String, RoomNode> roomsByContents, RoomNode startingPoint,
                                                  String itemToFind) throws InvalidGraphSearchParametersException {

         if (!roomsByContents.containsKey(itemToFind)) {
            throw new InvalidGraphSearchParametersException("The item " + itemToFind + " is not in any of the rooms");
        }

        List<RoomNode> rooms = new ArrayList<>();
        RoomNode roomThatContains = roomsByContents.get(itemToFind);
        List<Vertex> nodes = new ArrayList<>(roomsById.values());
        List<Edge> edges = new ArrayList<>();

        for (Vertex roomNode : nodes) {
            edges.addAll(roomNode.getEdges());
        }

        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(startingPoint);
        LinkedList<Vertex> path = dijkstra.getPath(roomThatContains);
        if (path == null){
            throw new InvalidGraphSearchParametersException("The graph is not connected.");
        }
        int numberOfRooms = path.size();
        for (int index = 0; index < numberOfRooms; index++) {
            Vertex vertex = path.get(index);
            RoomNode roomNode = roomsById.get(vertex.getId());
            rooms.add(roomNode);
            System.out.println("In the " + roomNode.getName());
            if (path.size() != index + 1) {
                vertex = path.get(index + 1);
                RoomNode nextRoom = roomsById.get(vertex.getId());
                System.out.println("I go " + (roomNode.whichWayIsThisRoom(nextRoom).toString()).toLowerCase());
            }
        }
        return rooms;
    }


    /**
     * This is a modified DSF algorithm. It takes the iterative (using a Stack) approach and not the  recursive one.
     * A real DFS is smart and skips vertex that have been visited before.
     * But our algorithm models how an adventurer would go from room to room at times having to back track
     * and revisit rooms they have been in before.
     * <p>
     * The direction the the adventure travels and what items are collected are written into the provided StringBuffer
     *
     * @param roomsById    a map of RoomNodes (vertex) by their id
     * @param itemsToFind  a list of items to search for
     * @param startingNode a RoomNode (vertex) to start searching from.
     * @param log          an empty list of Strings which is populated by the steps taken to find all the items.
     * @return a map whose key is an item and whose value is the RoomNode the item was found in.
     */
    public static Map<String, RoomNode> findItems(Map<String, RoomNode> roomsById, List<String> itemsToFind,
                                                  RoomNode startingNode, List<String> log) {

        // keeps track of the number of rooms to visit.
        Set<RoomNode> visited = new LinkedHashSet<>(roomsById.size());

        Map<String, RoomNode> roomByContents = new HashMap<>(itemsToFind.size());

        // Each room visited is recorded here. This allows us to back track into previously visited.
        Stack<RoomNode> steps = new Stack<>();

        // Seed the currentNode with the starting Node.
        RoomNode currentNode = startingNode;

        // Keep track of the last room we've been in. Initially that's no room (or null).
        RoomNode lastVisited = currentNode;

        while (currentNode != null) {

            log.add("In the " + currentNode.getName());

            exploreRoom(itemsToFind, currentNode, roomByContents, log);

            if (allItemsFound(itemsToFind, roomByContents, log)) {
                return roomByContents;
            }

            // Get this room's neighbors
            Collection<RoomNode> neighbors = currentNode.getNeighbors();
            Iterator<RoomNode> neighboringRoomIterator = neighbors.iterator();
            while (neighboringRoomIterator.hasNext()) {

                RoomNode next = neighboringRoomIterator.next();
                lastVisited = currentNode;

                // Have we been here before?
                if (!visited.contains(next)) { // no

                    // Mark room as visited
                    visited.add(next);
                    // Move to the next room
                    currentNode = next;
                    // We only want to deal with one room at a time - this makes it easier to back up
                    break;

                } else { // Yes, we have been here before.

                    // If there are no more possibly unvisited rooms from the current room...
                    if (!neighboringRoomIterator.hasNext()) {
                        // We have to back track.
                        steps.pop();
                        // We  have to pop twice because the stack already has the current room and we want the one after that.
                        currentNode = steps.pop();
                    } // Otherwise loop back into the next neighboring room and go from there.

                }
            }

            log.add("I go " + lastVisited.whichWayIsThisRoom(currentNode).toString().toLowerCase());

            steps.push(currentNode);
        }

        throw new IllegalStateException(
                "Not all items could be found. The following items weren't located:" + String.join(",", itemsToFind));
    }

    /**
     * When the number of rooms in the roomByContents map equals the number of items being searched, all items have
     * been found.
     * <p>
     *
     * @param itemsToFind    a list of items to search for
     * @param roomByContents a map of the rooms where items have already been found.
     * @param log            used to record that all the items where found.
     * @return true if all the items have been found and we can stop wondering around or false if the search should
     * continue.
     */
    private static boolean allItemsFound(List<String> itemsToFind, Map<String, RoomNode> roomByContents,
                                         List<String> log) {
        if (roomByContents.size() == itemsToFind.size()) {
            log.add("I found all " + roomByContents.size() + " items!");
            return true;
        }
        return false;
    }

    /**
     * Check the contents of the room for items we are looking for. If an item is found, note it in the console
     * and also place the room in a map whose key is the item. When we are done, we will have a map of what room
     * each item is in.  Finally, remove the item from list of items being searched for.
     * <p>
     *
     * @param itemsToFind a list of items to search for
     * @param currentNode the room to examine (i.e. see if it contains any from our list of items)
     * @param log         used to record that an item was found in the room.
     */
    private static void exploreRoom(List<String> itemsToFind, RoomNode currentNode,
                                    Map<String, RoomNode> roomByContents, List<String> log) {
        String item = currentNode.getContents();
        if (itemsToFind.contains(item)) {

            log.add("I collect the " + item);

            // a map of rooms by their item (to be used by the find the shortest path algorithm)
            roomByContents.put(item, currentNode);

        }
    }


}
