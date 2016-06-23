package com.origamisoftware.puzzle.model;

/**
 *
 */

import java.util.List;

public class Graph {

    private final List<RoomNode> vertexes;
    private final List<Edge> edges;

    public Graph(List<RoomNode> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    List<RoomNode> getVertexes() {
        return vertexes;
    }

    List<Edge> getEdges() {
        return edges;
    }


}
