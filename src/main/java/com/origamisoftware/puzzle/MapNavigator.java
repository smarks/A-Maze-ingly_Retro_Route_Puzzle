package com.origamisoftware.puzzle;

import com.origamisoftware.puzzle.model.AdventureMap;
import com.origamisoftware.puzzle.model.RoomNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Used to navigate a series of RoomNode to find the requested objects in the requested order
 */
public class MapNavigator {

    // bsf

    /**
     * Given an AdventureMap which contains both the entire map of rooms (or graph) and the starting point (initial node),
     * look in all the rooms (nodes) and if they contain an item in the itemsToFind list, record the roomNode and
     * it's contents in a Map. When all the rooms have been searched (all the nodes visited via BSF) return the
     * Map.
     *
     * @param adventureMap a data structure that contains a graph of the rooms as well as the room (node) to start from
     * @param itemsToFind  a set of items to find.
     * @return a Map where the key is the item name and the value is the room node it is located in.
     */
    public static Map<String, RoomNode> findItems(AdventureMap adventureMap, List<String> itemsToFind) {

        Map<String, RoomNode> foundItems = new HashMap<>();

        RoomNode entryNode = adventureMap.getEntryNode();
        Map<String, RoomNode> roomsById = adventureMap.getRoomsById();

        Queue<RoomNode> queue = new LinkedList<RoomNode>();
        entryNode.visited = true;
        queue.add(entryNode);

        while (!queue.isEmpty()) {
            RoomNode node = queue.poll();

            if (itemsToFind.contains(node.getContents())) {
                foundItems.put(node.getContents(), node);
            }
            for (String roomId : node.getNeighbors().values()) {
                RoomNode neighboringNode = roomsById.get(roomId);
                if (!neighboringNode.visited) {
                    neighboringNode.visited = true;
                    queue.add(neighboringNode);
                }
            }
        }
        return foundItems;
    }

    Map<String, RoomNode> findShortestPath(AdventureMap adventureMap, RoomNode startingNode, RoomNode destinationNode) {
        Map<String, RoomNode> path = new HashMap<>();

        return path;
    }


}
