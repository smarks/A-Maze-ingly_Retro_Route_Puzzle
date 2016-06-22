package com.origamisoftware.puzzle.model;

/**
 *
 */
public class Edge  {

    private final String id;
    private final RoomNode source;
    private final RoomNode destination;
    private final int weight;

    public Edge(String id, RoomNode source, RoomNode destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = 1;
    }

    public String getId() {
        return id;
    }
    public RoomNode getDestination() {
        return destination;
    }

    public RoomNode getSource() {
        return source;
    }
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }


}
