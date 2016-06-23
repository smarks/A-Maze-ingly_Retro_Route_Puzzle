package com.origamisoftware.puzzle;

import com.origamisoftware.puzzle.model.RoomNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Used to navigate a series of RoomNode to find the requested objects in the requested order
 */
public class MapNavigator {


    // bsf
    public static boolean  findPathTo(RoomNode startingNode, Map<String, RoomNode> roomsById, String item, List<RoomNode> path) {


        Queue<RoomNode> queue = new LinkedList<RoomNode>();
        startingNode.visited = true;
        queue.add(startingNode);

        System.out.println(startingNode.getName());
        System.out.println("----");


        while (!queue.isEmpty()) {
            RoomNode v = queue.poll();
            if (v.getContents().equals(item)) {
                path.add(v);
                return true;
            }
            for (String roomId : v.neighbors.values()) {
                RoomNode w = roomsById.get(roomId);
                if (!w.visited) {
                    System.out.println(w.getName());
                    path.add(v);
                    w.visited = true;
                    queue.add(w);
                }
            }
        }
        return false;
    }

}

