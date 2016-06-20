package com.origamisoftware.puzzle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Models a room on the map.
 * A list of Edge objects provide this room's connections to other rooms.
 * If a
 */
public class RoomNode {

    List<Edge> edges;
    RoomInventory contents;
    String name;

    public  RoomNode(String name) {
        this.name = name;
        contents = RoomInventory.EMPTY;
        edges = new ArrayList<>();
    }

    public RoomNode(List<Edge> edges, RoomInventory contents, String name) {
        this.edges = edges;
        this.contents = contents;
        this.name = name;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
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
        return "RoomNode{" + "edges=" + edges + ", contents=" + contents + ", name='" + name + '\'' + '}';
    }


}
