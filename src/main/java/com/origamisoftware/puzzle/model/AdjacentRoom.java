package com.origamisoftware.puzzle.model;

/**
 * Models an Edge in a graph (the line between two nodes)
 */
public class AdjacentRoom {

    /**
     * The direction from one room to another e.g. north, east, west, south
     */
    private LinkDirections direction;

    /**
     * The originating room
     */
    private String fromRoomId;

    /**
     * The adjacent room
     */
    private String toRoomId;

    /**
     * Create a new Edge instance
     *
     * @param fromRoomId the starting room
     * @param toRoomId the adjacent room
     * @param direction the direction from the room to the room
     */
    public AdjacentRoom(String fromRoomId, String toRoomId, LinkDirections direction) {
        this.fromRoomId = fromRoomId;
        this.toRoomId = toRoomId;
        this.direction = direction;
    }

    /**
     *
     * @return get the direction
     */
    public LinkDirections getDirection() {
        return direction;
    }

    /**
     *
     * @return the RoomNode ID for the originating room
     */
    public String getFromRoomId() {
        return fromRoomId;
    }

    /**
     *
     * @return the RoomNode ID for the adjacent room
     */
    public String getToRoomId() {
        return toRoomId;
    }

    @Override
    public String toString() {
        return "Edge{" + "direction=" + direction + ", from " + fromRoomId + ",to " + toRoomId + '}';
    }
}
