package com.origamisoftware.puzzle.model;

import com.origamisoftware.puzzle.graph.Edge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RoomNode is a specific implementation of a Vertex or Node which models a room in the adventurer's map.
 * <p>
 * A RoomNode contains a list of adjacent rooms (edges).
 * <p>
 * A RoomNode may have contents (e.g. a plate, if not, it's contents will be labeled as RoomNode.NO_CONTENTS).
 */
public class RoomNode implements Vertex {

    // used to denote a room that has no contents
    final static String NO_CONTENTS = "empty";

    /**
     * Adjacent Rooms
     */
    private final Map<CardinalPoint, RoomNode> neighbors = new HashMap<>();

    /**
     * Contents of the room
     */
    private String contents;

    // room identifier
    private final String id;

    // descriptive name
    private String name;

    /**
     * Constructor a RoomNode using just's its ID and name.
     * @param name a string value as provided in the data set.
     * @param id a  string value as provided in the data set. This value should be unique.
     */
    public RoomNode(String name, String id) {
        this.name = name;
        this.id = id;

        // contents are filled in later - if the room has contents.
        this.contents = NO_CONTENTS;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name specify the name of the room.
     */
    public void setName(String name) {this.name = name;}

    /**
     * Add an adjacent room to this room.
     * @param roomNode the adjacent room
     * @param direction the direction (cardinal point) of the adjacent room from this room
     *                  e.g.
     *                  this room -> LivingRoom
     *                  roomId -> Basement
     *                  direction is the cardinal point from LivingRoom to Basement
     */
    public void addEdge(RoomNode roomNode, CardinalPoint direction) {
        neighbors.put(direction,roomNode);
    }

    /**
     *
     * @return A list of the edges from this room.
     */
    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>(neighbors.size());
        for (CardinalPoint directionLabel : neighbors.keySet()) {
            RoomNode destination = neighbors.get(directionLabel);
            AdjacentRoom edge = new AdjacentRoom(this, destination);
            edges.add(edge);
        }
        return edges;
    }

    /**
     *
     * @return the contents of the room. If the room has no contents, RoomNode.NO_CONTENTS will returned.
     *
     */
    public String getContents() {
        return contents;
    }

    /**
     *
     * @param contents specify the room's contents.
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * A convenience method that returns the RoomNodes specified in as destinations
     * in this room's edges.
     *
     * @return get all the adjacent rooms.
     */
    public Collection<RoomNode> getNeighbors() {
        return neighbors.values();
    }


    /**
     * Get the CardinalPoint for the direction for the given room from this room.
     *
     * e.g.
     *
     * this --> CardinalPoint --> roomNode
     *
     * @param roomNode the end point
     * @return the direction to the provided room, from this room.
     */
    public CardinalPoint whichWayIsThisRoom(RoomNode roomNode) {

        for (CardinalPoint key : neighbors.keySet()) {
            if (neighbors.get(key).equals(roomNode)) {
                return key;
            }
        }
        throw new IllegalStateException("This room: " + roomNode.getName() + " is not adjacent to " + this.getName());
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
        return "RoomNode{" + "contents='" + contents + '\'' + ", id='" + id + '\'' + ", name='" + name + '\'' + '}';
    }
}