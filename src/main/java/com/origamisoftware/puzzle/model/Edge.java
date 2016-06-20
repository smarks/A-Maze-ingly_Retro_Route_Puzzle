package com.origamisoftware.puzzle.model;

/**
 * Models an Edge in a graph (the line between two nodes)
 */
public class Edge {

    private LinkDirections direction;
    private RoomNode a;
    private RoomNode b;

    public Edge(RoomNode a, RoomNode b, LinkDirections direction) {
        this.a = a;
        this.b = b;
    }

    public LinkDirections getDirection() {
        return direction;
    }

    public RoomNode getA() {
        return a;
    }

    public RoomNode getB() {
        return b;
    }
}
