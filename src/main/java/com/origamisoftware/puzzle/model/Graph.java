package com.origamisoftware.puzzle.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.List;


/**
 * A Graph is a collection of RoomNodes (vertexes) and Edges.
 * An edge is basically the line between two vertexes (or RoomNodes).
 * This class will contain all the RoomNodes and Edges as parsed from the input data.
 * (The map.xml file)
 *
 * This models an undirected graph
 *
 * The Graph class is immutable.
 *
 */
@Immutable
public class Graph {

    int numberOfVertexes;

    /**
     * All the vertexes in the graph
     */
    private final List<RoomNode> vertexes;

    /**
     * All the edges in the graph
     */
    private final List<Edge> edges;

    /**
     * Create a new Graph instance.
     *
     * @param vertexes the number of rooms (aka Nodes, aka vertexes)
     * @param edges the 'lines' or routes contenting the rooms.
     */
    public Graph(List<RoomNode> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
        this.numberOfVertexes = vertexes.size();
    }

    /**
     *
     * @return the list of RoomNode associated with this Graph
     */
    List<RoomNode> getVertexes() {
        return vertexes;
    }

    /**
     *
     * @return the list of Edges associated with this Graph
     */
    List<Edge> getEdges() {
        return edges;
    }



}
