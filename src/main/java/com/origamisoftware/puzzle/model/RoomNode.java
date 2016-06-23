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
public class RoomNode implements Vertex {

    public final static String NO_CONTENTS = "empty";

    /**
     * Adjacent Nodes
     */
    public Map<CardinalPoint, String> neighbors = new HashMap<>();

    /**
     * Contents of the room
     */
    private String contents;

    public boolean visited;

    private String id;
    private String name;

    public RoomNode(String name, String id) {
        this.name = name;
        this.id = id;
        this.contents = NO_CONTENTS;
    }

    public void addEdge(String roomId, CardinalPoint direction) {
        neighbors.put(direction, roomId);
    }

    public List<Edge> getEdges(Map<String, RoomNode> roomsById) {
        List<Edge> edges = new ArrayList<>(neighbors.size());

        for (CardinalPoint directionLabel : neighbors.keySet()) {
            RoomNode destination = roomsById.get(neighbors.get(directionLabel));
            Edge edge = new Edge(name + ":" + destination.getName(), this, destination, directionLabel.getXmlName());
            edges.add(edge);
        }
        return edges;
    }

    public Map<CardinalPoint, String> getNeighbors() {
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

    public CardinalPoint whichWayIsThisRoom(RoomNode roomNode) {

        for (CardinalPoint key : neighbors.keySet()) {
            if (neighbors.get(key).equals(roomNode.getId())) {
                return key;
            }
        }
        throw new IllegalStateException("This room: " + roomNode.getName() + " is not adjacent to " + this.getName());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RoomNode{" + "neighbors=" + neighbors + ", contents='" + contents + '\'' + ", visited=" + visited +
                ", id='" + id + '\'' + ", name='" + name + '\'' + '}';
    }
}