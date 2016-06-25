package com.origamisoftware.puzzle.util;

import com.origamisoftware.puzzle.model.RoomNode;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the methods in the MapUtils class.
 */
public class MapUtilsTest {

    @Test
    public void testBuildMapModelFromDocument() throws Exception {
        Document document = XMLUtils.parseXML("src/test/test_data/good-map.xml");
        NodeList rooms = document.getElementsByTagName(MapUtils.XML_TAG_NAME_FOR_ROOM);

        Map<String, RoomNode> roomNodeMap = MapUtils.buildMapModelFromDocument(document);

        int numberOfRoomsInXML = rooms.getLength();
        int numberOfRoomsInModel = roomNodeMap.size();
        assertEquals("Verify model contains same number of nodes as xml", numberOfRoomsInXML, numberOfRoomsInModel);

    }
}
