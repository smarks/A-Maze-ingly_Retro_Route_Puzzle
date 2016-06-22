package com.origamisoftware.puzzle.model;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Stores all the rooms on the adventure map along with helpful information about those rooms.
 * e.g.
 * given an item, this class can return a room that contains that item.
 * it also contains the root node (or starting room)
 */
public class AdventureMap {

    private RoomNode entryNode;

    private Map<String, RoomNode> roomContentsFromXML = new HashMap<>();
    private Map<String, RoomNode> roomsById;
    private Map<String, RoomNode> roomContentsByBFS = new HashMap<>();


    /**
     * Creates a new AdventureMap
     *
     * @param roomsById a map of all the rooms by ID
     * @param entryNode the first room, or root node
     */
    public AdventureMap(Map<String, RoomNode> roomsById, RoomNode entryNode) {
        this.entryNode = entryNode;
        this.roomsById = roomsById;
        for (String key : roomsById.keySet()) {
            RoomNode roomNode = roomsById.get(key);
            String contents = roomNode.getContents();
            if (!StringUtils.isEmpty(contents)) {
                roomContentsFromXML.put(contents, roomNode);
            }
        }
        roomContentsByBFS = findItems(this,roomContentsFromXML.keySet());
        System.out.println(roomContentsByBFS.size() == roomContentsFromXML.size());
    }


    /**
     * Given an AdventureMap which contains both the entire map of rooms (or graph) and the starting point (initial node),
     * look in all the rooms (nodes) and if they contain an item in the itemsToFind set, record the roomNode and
     * it's contents in a Map. When all the rooms have been searched (all the nodes visited via BSF) return the
     * Map.
     *
     * @param adventureMap a data structure that contains a graph of the rooms as well as the entry point
     * @param itemsToFind
     * @return
     */
    public static Map<String,RoomNode> findItems(AdventureMap adventureMap, Set<String> itemsToFind) {

        Map<String,RoomNode> foundItems = new HashMap<>();

        RoomNode node = adventureMap.getEntryNode();
        Map<String, RoomNode> roomsById = adventureMap.getRoomsById();

        Queue<RoomNode> queue = new LinkedList<RoomNode>();
        node.visited = true;
        queue.add(node);

           while (!queue.isEmpty()) {
            RoomNode v = queue.poll();

            if (itemsToFind.contains(v.getContents())){
                foundItems.put(v.getContents(),v);
            }
            for (String roomId : v.getNeighbors().values()) {
                RoomNode w = roomsById.get(roomId);
                if (!w.visited) {
                    w.visited = true;
                    queue.add(w);
                }
            }
        }
        return foundItems;
    }


    /**
     * Get all the room nodes by room id
     *
     * @return a map data structure of all the rooms on the adventure map
     */
    public Map<String, RoomNode> getRoomContents() {
        return roomContentsByBFS;
    }

    /**
     * The starting point on the map.
     *
     * @return a roomnode
     */
    public RoomNode getEntryNode() {
        return entryNode;
    }

    /**
     * Return the room that contains the specified contents or null if no room has the contents.
     *
     * @param contents a string value e.g. book
     * @return a roomNode that contains the requested item or NULL.
     */
    public RoomNode getRoomThatContains(String contents) {
        return roomContentsByBFS.get(contents);
    }

    /**
     * @return get all the rooms in the adventure's map in map data structure where the key is the room id.
     */
    public Map<String, RoomNode> getRoomsById() {
        return roomsById;
    }
}
