package com.origamisoftware.puzzle.model;

import com.origamisoftware.puzzle.graph.Edge;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for the RoomNode class
 */
public class RoomNodeTest {

    private String name = "Cave";
    private String id = "1";
    private RoomNode roomNode;

    @Before
    public void RoomNode() {
        roomNode = new RoomNode(name, id);
    }

    @Test
    public void testGetId() {
        assertEquals("id match", id, roomNode.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("name match", name, roomNode.getName());
    }

    @Test
    public void testEdgesNoEdges() {
        assertTrue("No edges", roomNode.getEdges().isEmpty());
    }

    @Test
    public void testEdgeAddEdges() {
        RoomNode destination = new RoomNode("Well", "wellId");
        CardinalPoint east = CardinalPoint.EAST;
        roomNode.addEdge(destination, east);
        List<Edge> edges = roomNode.getEdges();
        assertTrue("There should be one edge", edges.size() == 1);
        Edge edge = edges.get(0);
        assertTrue("Destination name is correct", edge.getDestination().getName().equals(destination.getName()));
        assertTrue("Destination id is correct", edge.getDestination().getId().equals(destination.getId()));
        assertTrue("Source name is correct", edge.getSource().getName().equals(roomNode.getName()));
        assertTrue("Source id is correct", edge.getSource().getId().equals(roomNode.getId()));
    }

    @Test
    public void testContents() {
        assertEquals("contents match", RoomNode.NO_CONTENTS, roomNode.getContents());
        String item = "Contents";
        roomNode.setContents(item);
        assertEquals("contents match", item, roomNode.getContents());
    }

    @Test
    public void testGetNeighbors() {
        assertTrue("No neighbors to start", roomNode.getNeighbors().isEmpty());
        RoomNode east = new RoomNode("east", "east");
        roomNode.addEdge(east, CardinalPoint.EAST);
        RoomNode west = new RoomNode("west", "west");
        roomNode.addEdge(west, CardinalPoint.WEST);
        Collection<RoomNode> neighbors = roomNode.getNeighbors();
        // these asserts verify we got back the expected results.
        assertTrue("removed a neighbor",  neighbors.remove(east));
        assertTrue("removed a neighbor",  neighbors.remove(west));
        assertTrue("not more neighbors",  neighbors.isEmpty());
     }


    @Test
    public void testWhichWayIsThisRoom() {
        RoomNode east = new RoomNode("east", "east");
        roomNode.addEdge(east, CardinalPoint.EAST);
        assertEquals("verify direction",CardinalPoint.EAST,roomNode.whichWayIsThisRoom(east));
        east.addEdge(roomNode,CardinalPoint.WEST);
        assertEquals("verify direction",CardinalPoint.WEST,east.whichWayIsThisRoom(roomNode));
    }

    @Test
    public void testHashCode() {
        RoomNode east1 = new RoomNode("east", "east");
        RoomNode east2 = new RoomNode("east", "east");
        assertEquals("hashcode should match", east1.hashCode(), east2.hashCode());
        assertFalse("hashcode should not match", new RoomNode("west","12").hashCode() == (east2.hashCode()));
    }

    @Test
    public void testEquals() {
        RoomNode east1 = new RoomNode("east", "east");
        RoomNode east2 = new RoomNode("east", "east");
        assertTrue("equals should match", east1.equals(east2));
        assertFalse("equals should not match", new RoomNode("west","12").equals(east2));
    }
}
