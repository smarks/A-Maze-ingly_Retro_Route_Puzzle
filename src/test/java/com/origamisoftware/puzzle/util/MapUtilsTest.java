package com.origamisoftware.puzzle.util;

import com.origamisoftware.puzzle.model.RoomNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the methods in the MapUtils class.
 */
public class MapUtilsTest {
    private NodeList rooms;
    private Map<String, RoomNode> roomNodeMap;

    @Before
    public void setUp() throws Exception {
        Document document = XMLUtils.parseXML("src/test/test_data/good-map.xml");
        rooms = document.getElementsByTagName(MapUtils.XML_TAG_NAME_FOR_ROOM);
        roomNodeMap = MapUtils.buildMapModelFromDocument(document);
    }

    @Test
    public void testBuildMapModelFromDocument() throws Exception {
        int numberOfRoomsInXML = rooms.getLength();
        int numberOfRoomsInModel = roomNodeMap.size();
        assertEquals("Verify model contains same number of nodes as xml", numberOfRoomsInXML, numberOfRoomsInModel);
    }

    @Test
    public void testFindItem() throws Exception {
        List<String> itemsToFind = new ArrayList<>();
        itemsToFind.add("Pine-cone");
        itemsToFind.add("Pickaxe");
        itemsToFind.add("Book");
        itemsToFind.add("Lamp");
        itemsToFind.add("Fishing-rod");
        itemsToFind.add("Plate");
        RoomNode startingPoint = roomNodeMap.get("scullery");
        List<String> log = new ArrayList<>();
        Map<String, RoomNode> items = MapUtils.findItems(roomNodeMap, itemsToFind, startingPoint, log);
        assertTrue("Found the same number of items", items.size() == itemsToFind.size());
        for (String item : itemsToFind) {
            assertTrue(item + " was found", items.containsKey(item));
        }
    }
}
