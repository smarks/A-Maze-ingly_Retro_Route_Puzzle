package com.origamisoftware.puzzle.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a room on the map.
 * A list of Edge objects provide this room's connections to other rooms.
 * If a
 */
public class RoomNode {

    private final String EMPTY = "empty";

    /**
     * Adjacent Nodes
     */
    public Map<String, String> neighbors = new HashMap<>();

    /**
     * Contents of the room
     */
    private String contents;

    /**
     * the display name
     */
    private String name;

    /**
     * The node id
     */
    private String id;

    public boolean visited;


    public RoomNode(String name, String id) {
        this.name = name;
        this.contents = EMPTY;
        this.id = id;
    }

    public RoomNode(String id) {
        this.id = id;
    }

    public void addNeighbor(String roomId, LinkDirections direction) {
        neighbors.put(direction.toString(), roomId);
    }

    public Map<String, String> getNeighbors() {
        return neighbors;
    }

    public String getContents() {
        return contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getId() {
        return id;
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
        return name + ", contents= " + contents + ", \n\t" + neighbors;
    }


}
