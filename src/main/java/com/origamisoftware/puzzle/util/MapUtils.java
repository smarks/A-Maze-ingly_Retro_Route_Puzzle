package com.origamisoftware.puzzle.util;

import com.origamisoftware.puzzle.model.AdventureMap;
import com.origamisoftware.puzzle.model.LinkDirections;
import com.origamisoftware.puzzle.model.RoomNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for creating a map model from the XML data.
 */
public class MapUtils {

    /**
     * Each node can have a link in any of the for cardinal directions
     */

    final private static String XML_TAG_NAME_FOR_ROOM = "room";
    final private static String XML_ROOM_ATTRIBUTE_NAME_FOR_ID = "id";
    final private static String XML_ROOM_ATTRIBUTE_KEY_FOR_ROOM_NAME = "name";
    final private static String XML_ROOM_ATTRIBUTE_KEY_FOR_OBJECT_NAME = "name";

    /**
     * Convert an XML instance to a Map of RoomNodes by Id.
     *
     * @param document the XML document
     * @return a Map where the key is the room id and the value which maps to a RoomNode.
     */
    public static AdventureMap buildMapModelFromDocument(Document document) {

        NodeList rooms = document.getElementsByTagName(XML_TAG_NAME_FOR_ROOM);

        int count = rooms.getLength();

        /* The roomNodes will hold the data in XML room elements. Since we know that size
         * we can pre-size our array avoiding any performance penalties  due to dynamic resizing
         */
        Map<String, RoomNode> roomMapById = new HashMap<>(count);

        // assume the first room in the map.xml is the starting point
        RoomNode entryPoint = node2RoomNode(roomMapById, rooms.item(0));

        // we already have the first room (0) now get every room element in the xml and create a roomNode for them.
        for (int index = 1; index < count; index++) {
            node2RoomNode(roomMapById, rooms.item(index));
        }

        return new AdventureMap(roomMapById,entryPoint);
    }

    private static RoomNode node2RoomNode(Map<String, RoomNode> roomMapById, Node node) {

        NamedNodeMap attributes = node.getAttributes();
        String roomId = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_NAME_FOR_ID).getNodeValue();
        String roomName = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_KEY_FOR_ROOM_NAME).getNodeValue();

        RoomNode roomNode = new RoomNode(roomName, roomId);
        roomMapById.put(roomId, roomNode);

        for (LinkDirections linkDirection : LinkDirections.values()) {
            String linkId = getAttributeValueOrNull(attributes, linkDirection.getXmlName());

            // lots of rooms won't have a link for each direction, that's OK
            if (linkId != null) {
                // create an edge between current room and room being pointed to.
                roomNode.addEdge(linkId, linkDirection);
            }
        }

        populateRoomContents(node, roomName, roomNode);

        return roomNode;
    }

    private static void populateRoomContents(Node node, String roomName, RoomNode roomNode) {

        // the XML node will have child nodes if it has Object element which holds the room's contents.
        if (node.hasChildNodes()) {
            int length = node.getChildNodes().getLength();
            NodeList childNodes = node.getChildNodes();
            for (int index = 0; index < length; index++) {

                Node childNode = childNodes.item(index);
                short nodeType = childNode.getNodeType();
                if (nodeType == Node.ELEMENT_NODE) {
                    roomNode.setContents(childNode.getAttributes().getNamedItem(XML_ROOM_ATTRIBUTE_KEY_FOR_OBJECT_NAME)
                            .getNodeValue());
                }
            }
        }
    }

    private static String getAttributeValueOrNull(NamedNodeMap attributes, String attributeName) {
        String returnValue = null;
        Node namedItem = attributes.getNamedItem(attributeName);
        if (namedItem != null) {
            returnValue = namedItem.getNodeValue();
        }
        return returnValue;
    }
}
