package com.origamisoftware.puzzle.model;

/**
 * A Edge models 'the line' between to nodes in a graph.
 **/
public class Edge {

    /**
     * The staring point
     */
    private final RoomNode source;

    /**
     * The end point
     */
    private final RoomNode destination;

    /**
     * In some graphs (in some use cases) Edges can have a different weights associated with each one.
     * E.g. a weighted graph
     * Our use case does not require varying weight edges. That is, while our graph is technically weighted
     * all of them have a weight of one. We use a weighted graph because Dijkstra's shortest path Algorithm
     * (which we use in this program) uses the weight value of edges.
     * However, for our use, this value never varies
     */
    private final static int weight = 1;

    /**
     * The cardinal direction from the source to the destination.
     */
    private final CardinalPoint directionFromSource;

    public Edge(RoomNode source, RoomNode destination, CardinalPoint directionFromSource) {
        this.source = source;
        this.destination = destination;
        this.directionFromSource = directionFromSource;
    }


    public RoomNode getDestination() {
        return destination;
    }

    public RoomNode getSource() {
        return source;
    }

    int getWeight() {
        return weight;
    }

    public CardinalPoint getDirectionFromSource() {
        return directionFromSource;
    }

    @Override
    public String toString() {
        return "Edge{" + "source=" + source + ", destination=" + destination + ", weight=" + weight +
                ", directionFromSource='" + directionFromSource + '\'' + '}';
    }
}
