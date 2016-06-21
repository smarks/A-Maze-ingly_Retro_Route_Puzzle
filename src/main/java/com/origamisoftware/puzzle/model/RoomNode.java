package com.origamisoftware.puzzle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Models a room on the map.
 * A list of Edge objects provide this room's connections to other rooms.
 * If a
 */
public class RoomNode {

    /**
     * List of edges from this room to other rooms
     */
    List<Edge> edges;

    /**
     * Contents of the room
     */
    RoomInventory contents;

    /**
     * the display name
     */
    String name;

    /**
     * The node id
     */
    String id;


    public RoomNode(String name, String id) {
        this.name = name;
        this.contents = RoomInventory.EMPTY;
        this.edges = new ArrayList<>();
        this.id = id;
    }

    public RoomNode(String id) {
        this.id = id;
    }

    public void addEdge(String roomId, LinkDirections directions) {
        edges.add(new Edge(this.id, roomId, directions));
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public RoomInventory getContents() {
        return contents;
    }

    public String getName() {
        return name;
    }

    public void setContents(RoomInventory contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return name + ", contents= " + contents + ", \n\t" + edges;
    }


    public void setName(String name) {
        this.name = name;
    }
}
