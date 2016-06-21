package com.origamisoftware.puzzle.util;

import com.origamisoftware.puzzle.model.LinkDirections;
import com.origamisoftware.puzzle.model.RoomInventory;
import com.origamisoftware.puzzle.model.RoomNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MapUtils {


    /**
     * Each node can have a link in any of the for cardinal directions
     */
    // final private static String[] XML_LINK_ATTRIBUTE_NAMES = new String[]{"north", "east", "south", "west"};

    final private static String XML_TAG_NAME_FOR_ROOM = "room";
    final private static String XML_ROOM_ATTRIBUTE_NAME_FOR_ID = "id";
    final private static String XML_ROOM_ATTRIBUTE_NAME_FOR_NAME = "name";

    public static Map<String, RoomNode> buildMapModelFromDocument(Document document) {

        NodeList rooms = document.getElementsByTagName(XML_TAG_NAME_FOR_ROOM);

        int count = rooms.getLength();

        /* The roomNodes will hold the data in XML room elements. Since we know that size
         * we can pre-size our array avoiding any performance penalties  due to dynamic resizing
         */
        Map<String, RoomNode> roomMapByName = new HashMap<>(count);

        // get every room element in the xml and create a roomNode for them.
        for (int index = 0; index < count; index++) {
            node2RoomNode(roomMapByName, rooms.item(index));
        }

        return roomMapByName;
    }

    private static RoomNode node2RoomNode(Map<String, RoomNode> roomMapById, Node node) {

        NamedNodeMap attributes = node.getAttributes();
        String roomId = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_NAME_FOR_ID).getNodeValue();
        String roomName = attributes.getNamedItem(XML_ROOM_ATTRIBUTE_NAME_FOR_NAME).getNodeValue();

        RoomNode roomNode = new RoomNode(roomName, roomId);
        roomMapById.put(roomName, roomNode);

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
                    String contentsName = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    // dashes are not permitted as identifiers in java so we do this replacement if want to use enums
                    roomNode.setContents(RoomInventory.valueOf(contentsName.toUpperCase().replace("-", "_")));
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
