package com.origamisoftware.puzzle.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DepthFirstSearch {

    private Set<RoomNode> visited;

    private int count;           // number of vertices connected to s
    private List<String> itemsToFind;
    private Map<String, RoomNode> roomByContents;

    /**
     * Computes the vertices in graph <tt>G</tt> that are
     * connected to the source vertex <tt>s</tt>.
     *
     * @param roomsById    the graph
     * @param startingNode the source vertex
     */
    public DepthFirstSearch(Map<String, RoomNode> roomsById, RoomNode startingNode, List<String> itemsToFind,
                            Map<String, RoomNode> roomByContents) {
        this.visited = new LinkedHashSet<>();
        /* we are going to be remove the items from this list as we find them and don't want to destroy the source
         * since we know we are single threaded we don't need use something like CopyOnWriteArrayList
         */
        this.itemsToFind = new ArrayList<>(itemsToFind);
        this.roomByContents = roomByContents;
        dfs(roomsById, startingNode);
    }

    private void dfs(Map<String, RoomNode> roomsById, RoomNode node) {
        count++;
        visited.add(node);

        System.out.println("In the " + node.getName());

        String item = node.getContents();
        if (itemsToFind.contains(item)) {
            System.out.println("I collect the " + item);
            roomByContents.put(item, node);
            itemsToFind.remove(item);
        }

        List<Edge> edges = node.getEdges(roomsById);

        for (Edge edge : edges) {
            if (itemsToFind.isEmpty()) {
                return;
            }
            RoomNode adjacentNode = edge.getDestination();
            if (!visited.contains(adjacentNode)) {
                System.out.println("I go " + edge.getDirectionFromSource());
                dfs(roomsById, adjacentNode);
            }
        }
    }

    /**
     * Returns the number of vertices connected to the source vertex <tt>s</tt>.
     *
     * @return the number of vertices connected to the source vertex <tt>s</tt>
     */
    public int count() {
        return count;
    }

    /**
     * Unit tests the <tt>DepthFirstSearch</tt> data type.
     */
    public static void main(String[] args) {


    }
}
