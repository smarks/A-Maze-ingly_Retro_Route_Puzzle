package com.origamisoftware.puzzle.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DepthFirstSearch {

    private final Set<RoomNode> visited;

    private final List<String> itemsToFind;
    private final Map<String, RoomNode> roomByContents;
    private final Map<String, RoomNode> roomsById;

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
        this.roomsById = roomsById;

        dfs2(startingNode);
    }

    private void dfs(RoomNode node) {

        visited.add(node);

        System.out.println("In the " + node.getName());

        String item = node.getContents();
        if (itemsToFind.contains(item)) {
            System.out.println("I collect the " + item);
            roomByContents.put(item, node);
            itemsToFind.remove(item);
            if (item.equals("Fishing-rod")) {
                System.out.println("-");
            }
        }

        List<Edge> edges = node.getEdges(roomsById);

        for (Edge edge : edges) {

            if (itemsToFind.isEmpty()) {
                return;
            }

            // if all rooms visited then go back up
            RoomNode adjacentNode = edge.getDestination();
            if (!visited.contains(adjacentNode)) {
                System.out.println("I go " + edge.getDirectionFromSource());
                dfs(adjacentNode);
            }
        }
    }

    private void dfs2(RoomNode startingNode) {

        Stack<RoomNode> steps = new Stack<>();
        RoomNode currentNode = startingNode;
        RoomNode lastVisited = null;

        while (currentNode != null) {

            String item = currentNode.getContents();

            System.out.println("In the " + currentNode.getName());

            if (itemsToFind.contains(item)) {
                System.out.println("I collect the " + item);
                roomByContents.put(item, currentNode);
                itemsToFind.remove(item);
                if (item.equals("Lamp")) {
                    System.out.println("-");
                }
                if (itemsToFind.isEmpty()) {
                    System.out.println("I found all" + roomByContents.size() +" items!");
                    return;
                }
            }

            List<RoomNode> children = currentNode.getChildren(roomsById);
            Iterator<RoomNode> iterator = children.iterator();
            while (iterator.hasNext()) {
                RoomNode next = iterator.next();
                lastVisited = currentNode;
                if (!visited.contains(next)) {
                    visited.add(next);
                    currentNode = next;
                    break;
                } else {
                    if (!iterator.hasNext()) {
                        // now we have to back track.
                        currentNode = steps.pop();
                        currentNode = steps.pop();
                    }
                }
            }

            System.out.println("I go " + lastVisited.whichWayIsThisRoom(currentNode));


            steps.push(currentNode);
        }
    }

}
