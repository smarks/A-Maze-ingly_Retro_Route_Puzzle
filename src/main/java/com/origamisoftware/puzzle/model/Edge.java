package com.origamisoftware.puzzle.model;

/**
 *
 */
public class Edge  {

    private final String id;
    private final RoomNode source;
    private final RoomNode destination;
    private final int weight;
    private final String directionFromSource;

    public Edge(String id, RoomNode source, RoomNode destination, String directionFromSource) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = 1;
        this.directionFromSource = directionFromSource;
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

    public String getDirectionFromSource() {
        return directionFromSource;
    }

    @Override
    public String toString() {
        return "Edge{" + "id='" + id + '\'' + ", source=" + source + ", destination=" + destination + ", weight=" +
                weight + ", directionFromSource='" + directionFromSource + '\'' + '}';
    }
}
