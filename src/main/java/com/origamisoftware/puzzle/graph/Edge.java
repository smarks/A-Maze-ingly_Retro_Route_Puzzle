package com.origamisoftware.puzzle.graph;

import com.origamisoftware.puzzle.model.AdjacentRoom;
import com.origamisoftware.puzzle.model.Vertex;

/**
 * An Edge models 'the line' between two nodes in a Graph.
 * <p>
 * e.g.
 * <p>
 * A --- B
 * <p>
 * The dashes are the edge
 * <p>
 * This distance can have a weight which could
 * make some edges more costly to traverse than others.
 * This value is used in weighted graphs and is required by
 * some  graph algorithms.
 *
 * @see AdjacentRoom
 */
public interface Edge {

    /**
     * The starting point of the  edge. An Edge is
     *
     * A --- B
     *
     * This is the A value.
     *
     * @return a room node
     */
    Vertex getSource();

    /**
     * The far end of the edge. An Edge is
     *
     * A --- B
     *
     * This is the B value.
     *
     * @return a room node
     */
    Vertex getDestination();

    /**
     * The 'weight' of this edge.
     * (Used in weighted graphs)
     *
     * @return an int value
     */
    int getWeight();

}