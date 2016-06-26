package com.origamisoftware.puzzle.model;

import com.origamisoftware.puzzle.graph.Edge;

/**
 * Models adjoining rooms (in more general graph parlance, an edge)
 **/
public class AdjacentRoom implements Edge {

    /**
     * The staring point
     */
    private final RoomNode source;

    /**
     * The end point
     */
    private final RoomNode destination;

    /**
     * In some graphs (in some use cases), Edges can have a different weights associated with each one.
     * E.g. a weighted graph.
     * Our use case does not require varying weight edges. That is, while our graph is technically weighted
     * all of them have a weight of one. We use a weighted graph because Dijkstra's shortest path Algorithm
     * (which we use in this program) requires the weight as input.
     * <p>
     * However, for our use, this value never varies and we just 1.
     */
    final static int WEIGHT = 1;

    /**
     * Create an Edge
     *
     * @param source      the starting node
     * @param destination the ending node
     */
    AdjacentRoom(RoomNode source, RoomNode destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public Vertex getSource() {
        return source;
    }

    @Override
    public Vertex getDestination() {
        return destination;
    }

    /**
     * Always 1 (since the puzzle does not required a weighted graph but
     * Dijkstra Algorithm expects some weight value.
     *
     * @return the weight between the source and destination
     */
    public int getWeight() {
        return WEIGHT;
    }

    @Override
    public String toString() {
        return "Edge{" + "source=" + source + ", destination=" + destination + '}';
    }


}
