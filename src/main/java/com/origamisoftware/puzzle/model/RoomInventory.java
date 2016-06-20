package com.origamisoftware.puzzle.model;

/**
 * Models the contents of a room.
 * Rooms that have no contents are modeled with EMPTY
 */
public enum RoomInventory {

    EMPTY("empty"),
    PICKAXE("pickaxe"),
    BOOK("book"),
    LAMP("lamp"),
    PINE_CONE("Pine-cone"),
    PLATE("plate"),
    FISHING_ROD("Fishing-rod");

    String description;

    RoomInventory(String description) {
        this.description = description;
    }


}
