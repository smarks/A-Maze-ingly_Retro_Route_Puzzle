package com.origamisoftware.puzzle.model;

import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class RouteSegment {

    CardinalPoint cardinalPoint;
    RoomNode roomNode;

    public RouteSegment(CardinalPoint cardinalPoint, RoomNode roomNode) {
        this.cardinalPoint = cardinalPoint;
        this.roomNode = roomNode;
    }

    public CardinalPoint getCardinalPoint() {
        return cardinalPoint;
    }

    public RoomNode getRoomNode() {
        return roomNode;
    }

    @Override
    public String toString() {
        String step = "Go " + cardinalPoint + " to " + roomNode.getName();
        if (! roomNode.getContents().equals(RoomNode.NO_CONTENTS)) {
            step = step + " and pick up the " + roomNode.getContents();
        }
        return step;
    }
}
