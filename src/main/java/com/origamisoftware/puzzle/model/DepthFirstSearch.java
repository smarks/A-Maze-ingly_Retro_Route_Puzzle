package com.origamisoftware.puzzle.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * This class provide a iterative (not a recursive) appro
 */
public class DepthFirstSearch {

    private final Set<RoomNode> visited;
    private final List<String> itemsToFind;
    private final Map<String, RoomNode> roomByContents;
    private final Map<String, RoomNode> roomsById;

    /**
     * @param roomsById
     * @param startingNode
     * @param itemsToFind
     * @param roomByContents
     */
    public DepthFirstSearch(Map<String, RoomNode> roomsById, RoomNode startingNode, List<String> itemsToFind,
                            Map<String, RoomNode> roomByContents) {

        this.visited = new LinkedHashSet<>();

        /* We are going to be remove the items from this list as we find them and don't want to destroy the source
         * since we know we are single threaded we don't need use something like CopyOnWriteArrayList
         */
        this.itemsToFind = new ArrayList<>(itemsToFind);
        this.roomByContents = roomByContents;
        this.roomsById = roomsById;

        dfs(startingNode);
    }


    /**
     * This is a modified DSF algorithm. A real DFS is smart and skips vertex that have been visited before
     * but our algorithm models how an adventurer would go from room to room at times having to back track
     * and revisit rooms they have been in before.
     *
     * @param startingNode this is room to be gin searching
     */
    private void dfs(RoomNode startingNode) {

        // Each room visited is recorded here. This allows up to back track into previously visited
        Stack<RoomNode> steps = new Stack<>();

        // Seed the currentNode with the starting Node.
        RoomNode currentNode = startingNode;

        // Keep track of the last room we've been in. Initially that's no room (or null).
        RoomNode lastVisited = null;

        while (currentNode != null) {

            System.out.println("In the " + currentNode.getName());

            exploreRoom(currentNode);

            if (allItemsFound()) {
                return;
            }

            // Get this room's neighbors
            List<RoomNode> children = currentNode.getChildren(roomsById);
            Iterator<RoomNode> neighboringRoomIterator = children.iterator();
            while (neighboringRoomIterator.hasNext()) {

                RoomNode next = neighboringRoomIterator.next();
                lastVisited = currentNode;

                // Have we been here before?
                if (!visited.contains(next)) { // no

                    // Mark room as visited
                    visited.add(next);
                    // Move to the next room
                    currentNode = next;
                    // We only want to deal with one room at a time - this makes it easier to back up
                    break;

                } else { // Yes, we have been here before.

                    // If there are no more possibly unvisited rooms from the current room...
                    if (!neighboringRoomIterator.hasNext()) {
                        // We have to back track.
                        steps.pop();
                        // We have to pop twice because the stack already has the current room and we want the one after that.
                        currentNode = steps.pop();
                    } // Otherwise loop back into the next neighboring room and go from there.

                }
            }

            System.out.println("I go " + lastVisited.whichWayIsThisRoom(currentNode));

            steps.push(currentNode);
        }
    }

    /**
     *
     * @return true if all the items have been found and we can stop wondering around, false if the search should
     * continue.
     */
    private boolean allItemsFound() {
        if (itemsToFind.isEmpty()) {
            System.out.println("I found all" + roomByContents.size() + " items!");
            return true;
        }
        return false;
    }

    /**
     * Check the contents of the room for items we are looking for. If an item is found note it in the console
     * and also place the room in a map whose key is the item. When we are done, we will have a map of what room
     * each item is in.  Finally remove the item from list of items being searched for.
     *
     * @param currentNode the room to explain
     */
    private void exploreRoom(RoomNode currentNode) {
        String item = currentNode.getContents();
        if (itemsToFind.contains(item)) {

            System.out.println("I collect the " + item);

            // a map of rooms by their item (to be used by the find the shortest path algorithm)
            roomByContents.put(item, currentNode);

            itemsToFind.remove(item);

        }

    }

}
