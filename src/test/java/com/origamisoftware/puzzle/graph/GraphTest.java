package com.origamisoftware.puzzle.graph;

import com.origamisoftware.puzzle.model.Vertex;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the Graph class
 */
public class GraphTest {

    private List<Vertex> vertexes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private Graph graph;

    @Before
    public void setup() {
          graph = new Graph(vertexes, edges);
    }

    @Test
    public void testGetVertex() {
        assertEquals("Vertex match", vertexes, graph.getVertexes());
    }

    @Test
    public void testGetEdges() {
        assertEquals("Edges match", edges, graph.getEdges());
    }

}
