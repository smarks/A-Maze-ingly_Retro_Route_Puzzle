package com.origamisoftware.puzzle.model;

/**
 * An enum that models north, south east or west - the direction
 * a room can be adjacent to another room in the map.
 */
public enum CardinalPoint {

    NORTH("north"), SOUTH("south"), EAST("east"), WEST("west");

    private final String name;

    CardinalPoint(String name) {
        this.name = name;
    }

    /**
     * @return the value in the XML document representing the direction
     * of the room.
     */
    public String getName() {
        return name;
    }


}
