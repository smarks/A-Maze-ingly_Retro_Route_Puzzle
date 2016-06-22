package com.origamisoftware.puzzle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models a room on the map.
 * A list of Edge objects provide this room's connections to other rooms.
 * If a
 */
public class RoomNode implements Vertex{

    private final String EMPTY = "empty";

    /**
     * Adjacent Nodes
     */
    private Map<String, String> neighbors = new HashMap<>();

    /**
     * Contents of the room
     */
    private String contents;

    private List<AdjacentRoom> adjacentRooms = new ArrayList<>();

    public boolean visited;

    private String id;
    private String name;

    public RoomNode(String name, String id) {
        this.name = name;
        this.id = id;
        this.contents = EMPTY;
    }

    public void addNeighbor(String roomId, LinkDirections direction) {
        neighbors.put(direction.toString(), roomId);
        adjacentRooms.add(new AdjacentRoom(id, roomId, direction));
    }

    public List<Edge> getEdges(Map<String, RoomNode> roomsById) {
        List<Edge> edges = new ArrayList<>(neighbors.size());

        for (String directionLabel : neighbors.keySet()) {
            RoomNode destination = roomsById.get(neighbors.get(directionLabel));
            edges.add(new Edge(name + ":" + directionLabel, this, destination));
        }
        return edges;
    }

    public Map<String, String> getNeighbors() {
        return neighbors;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 :id.hashCode());
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
        RoomNode other = (RoomNode) obj;
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
        return "id " + id + " name: " + id + ", contents= " + contents + ", \n\t" + neighbors;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
