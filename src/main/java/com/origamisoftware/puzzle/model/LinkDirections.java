package com.origamisoftware.puzzle.model;

/**
 *
 */
public enum LinkDirections {
    NORTH("north"), SOUTH("south"), EAST("east"), WEST("west");

    private String xmlName;

    LinkDirections(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getXmlName() {
        return xmlName;
    }
}
