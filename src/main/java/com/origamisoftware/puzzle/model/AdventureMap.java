package com.origamisoftware.puzzle.model;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all the rooms on the adventure map along with helpful information about those rooms.
 * e.g.
 * given an item, this class can return a room that contains that item.
 * it also contains the root node (or starting room)
 */
public class AdventureMap {

    RoomNode entryNode;

    Map<String, RoomNode> roomContents = new HashMap<>();
    Map<String, RoomNode> roomsById;

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
                roomContents.put(contents, roomNode);
            }
        }
    }

    /**
     * Get all the room nodes by room id
     *
     * @return a map data structure of all the rooms on the adventure map
     */
    public Map<String, RoomNode> getRoomContents() {
        return roomContents;
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
        return roomContents.get(contents);
    }

    /**
     * @return get all the rooms in the adventure's map in map data structure where the key is the room id.
     */
    public Map<String, RoomNode> getRoomsById() {
        return roomsById;
    }
}
