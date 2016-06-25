package com.origamisoftware.puzzle.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for AdjacentRoom
 */
public class AdjacentRoomTest {

    private RoomNode source;
    private RoomNode destination;
    private AdjacentRoom adjacentRoom;

    @Before
    public void setup() {
        source = new RoomNode("source", "source");
        destination = new RoomNode("destination", "destination");
        adjacentRoom = new AdjacentRoom(source, destination);
    }

    @Test
    public void testGetDestination() {
         assertEquals("destination matches", destination,adjacentRoom.getDestination());
    }

    @Test
    public void testGetSource() {
        assertEquals("source matches", source,adjacentRoom.getSource());
    }

    @Test
    public void testGetWeight() {
        assertEquals("weight is 1", AdjacentRoom.WEIGHT, adjacentRoom.getWeight());
    }
}
