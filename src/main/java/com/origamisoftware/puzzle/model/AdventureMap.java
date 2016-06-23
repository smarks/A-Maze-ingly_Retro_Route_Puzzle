package com.origamisoftware.puzzle.model;

import java.util.Map;

/**
 * Stores all the rooms on the adventure map along with helpful information about those rooms.
 *
 */
public class AdventureMap {

    private Map<String, RoomNode> roomsById;
    private Map<String, RoomNode> roomContentsByItem;


    /**
     * Creates a new AdventureMap
     *
     * @param roomsById a map of all the rooms by ID
      */
    public AdventureMap(Map<String, RoomNode> roomsById,Map<String, RoomNode> roomContentsByItem) {
        this.roomsById = roomsById;
        this.roomContentsByItem = roomContentsByItem;
    }

    /**
     * Get all the room nodes by room id
     *
     * @return a map data structure of all the rooms on the adventure map
     */
    public Map<String, RoomNode> getRoomContents() {
        return roomContentsByItem;
    }



    /**
     * Return the room that contains the specified contents or null if no room has the contents.
     *
     * @param contents a string value e.g. book
     * @return a roomNode that contains the requested item or NULL.
     */
    public RoomNode getRoomThatContains(String contents) {
        return roomContentsByItem.get(contents);
    }

    /**
     * @return get all the rooms in the adventure's map in map data structure where the key is the room id.
     */
    public Map<String, RoomNode> getRoomsById() {
        return roomsById;
    }
}
