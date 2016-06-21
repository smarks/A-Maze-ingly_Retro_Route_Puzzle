package com.origamisoftware.puzzle;

import com.origamisoftware.puzzle.model.AdventureMap;
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
    public static boolean  findPathTo(AdventureMap adventureMap, String item, List<RoomNode> path) {

        RoomNode node = adventureMap.getEntryNode();
        Map<String, RoomNode> roomsById = adventureMap.getRoomsById();

        Queue<RoomNode> queue = new LinkedList<RoomNode>();
        node.visited = true;
        queue.add(node);

        System.out.println(node.getName());
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
                    //System.out.println(w.value);
                    path.add(v);
                    w.visited = true;
                    queue.add(w);
                }
            }
        }
        return false;
    }

}
