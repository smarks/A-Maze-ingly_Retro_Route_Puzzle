package com.origamisoftware.puzzle.model;

/**
 * Models an Edge in a graph (the line between two nodes)
 */
public class Edge {

    private LinkDirections direction;
    private String fromRoomId;
    private String toRoomId;

    public Edge(String fromRoomId, String toRoomId, LinkDirections direction) {
        this.fromRoomId = fromRoomId;
        this.toRoomId = toRoomId;
        this.direction = direction;
    }

    public LinkDirections getDirection() {
        return direction;
    }

    public String getFromRoomId() {
        return fromRoomId;
    }

    public String getToRoomId() {
        return toRoomId;
    }

    @Override
    public String toString() {
        return "Edge{" + "direction=" + direction + ", from " + fromRoomId + ",to " + toRoomId + '}';
    }
}
