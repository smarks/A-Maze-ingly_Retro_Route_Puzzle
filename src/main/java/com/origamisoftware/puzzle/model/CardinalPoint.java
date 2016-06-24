package com.origamisoftware.puzzle.model;

/**
 *
 */
public enum CardinalPoint {

    NORTH("north"), SOUTH("south"), EAST("east"), WEST("west");

    private final String xmlName;

    CardinalPoint(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public CardinalPoint getOpposite(){
        if (this.equals(NORTH)) {
            return SOUTH;
        }
        if (this.equals(SOUTH)) {
            return NORTH;
        }
        if (this.equals(WEST)) {
            return EAST;
        } if (this.equals(EAST)) {
            return WEST;
        }
        throw new IllegalStateException("Undefined CardinalPoint");
    }


}
