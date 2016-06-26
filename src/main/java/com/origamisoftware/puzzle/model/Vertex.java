package com.origamisoftware.puzzle.model;

import com.origamisoftware.puzzle.graph.Edge;

import java.util.List;

/**
 * A basic interface for a Node or Vertex used by graph algorithms.
 * The interface allows us to have specific implementations of Vertex
 * which could contain additional information about the Node which is not
 * required by the algorithm, but useful to other parts of the data model.
 *
 * For example, a RoomNode implements this interface, but provides
 * additional information about the room.
 *
 * @see RoomNode
 */
public interface Vertex {

    /**
     * A unique id for the Vertex
     * @return a String value
     */
    String getId();

    /**
     * A label for the Vertex. It does not have to be unique.
     * @return a String value.
     */
    String getName();

    List<Edge> getEdges();
}