package com.origamisoftware.puzzle.graph;

import com.origamisoftware.puzzle.model.Vertex;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for Dijkstra's Algorithm
 * also cribbed from source: http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 */
public class DijkstraAlgorithmTest {

    private class TestEdge implements Edge {
        private final String id;
        private final Vertex source;
        private final Vertex destination;
        private final int weight;

        TestEdge(String id, Vertex source, Vertex destination, int weight) {
            this.id = id;
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public String getId() {
            return id;
        }

        public Vertex getDestination() {
            return destination;
        }

        public Vertex getSource() {
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

    private class TestVertex implements Vertex {

        final String id;
        private final String name;


        TestVertex(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public List<Edge> getEdges() {
            return new ArrayList<>();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TestVertex other = (TestVertex) obj;
            if (id == null) {
                if (other.id != null) {
                    return false;
                }
            } else if (!id.equals(other.id)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return name;
        }


    }

    private List<Vertex> nodes;
    private List<Edge> edges;

    @Test
    public void testExecute() {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        for (int i = 0; i < 11; i++) {

            Vertex location = new TestVertex("Node_" + i, "Node_" + i);
            nodes.add(location);
        }

        addLane("Edge_0", 0, 1, 85);
        addLane("Edge_1", 0, 2, 217);
        addLane("Edge_2", 0, 4, 173);
        addLane("Edge_3", 2, 6, 186);
        addLane("Edge_4", 2, 7, 103);
        addLane("Edge_5", 3, 7, 183);
        addLane("Edge_6", 5, 8, 250);
        addLane("Edge_7", 8, 9, 84);
        addLane("Edge_8", 7, 9, 167);
        addLane("Edge_9", 4, 9, 502);
        addLane("Edge_10", 9, 10, 40);
        addLane("Edge_11", 1, 10, 600);

        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(0));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));

        assertNotNull(path);
        assertTrue(path.size() > 0);

        String[]  expectedResults = new String[]{ "Node_0","Node_2","Node_7","Node_9", "Node_10"};
        List<String> results = new ArrayList<>(expectedResults.length);
        for (Vertex vertex : path) {
          results.add(vertex.toString());
        }
        for (String expectedResult: expectedResults) {
            results.remove(expectedResult);
        }
        assertTrue("Got expected results ", results.isEmpty());
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
        Edge lane = new TestEdge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
        edges.add(lane);
    }
}

