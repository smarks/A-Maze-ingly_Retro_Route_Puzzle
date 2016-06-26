package com.origamisoftware.puzzle.util;

import com.google.common.collect.Iterables;
import com.origamisoftware.puzzle.model.RoomNode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
    private List<String> itemsToFind;

    @Before
    public void setUp() throws Exception {
        Document document = XMLUtils.parseXML("src/test/test_data/good-map.xml");
        rooms = document.getElementsByTagName(MapUtils.XML_TAG_NAME_FOR_ROOM);
        roomNodeMap = MapUtils.buildMapModelFromDocument(document);
        itemsToFind = new ArrayList<>();
        itemsToFind.add("Pine-cone");
        itemsToFind.add("Pickaxe");
        itemsToFind.add("Book");
        itemsToFind.add("Lamp");
        itemsToFind.add("Fishing-rod");
        itemsToFind.add("Plate");
    }

    @Test
    public void testBuildMapModelFromDocument() throws Exception {
        int numberOfRoomsInXML = rooms.getLength();
        int numberOfRoomsInModel = roomNodeMap.size();
        assertEquals("Verify model contains same number of nodes as xml", numberOfRoomsInXML, numberOfRoomsInModel);
    }

    @Test
    public void testFindItem() throws Exception {
        RoomNode startingPoint = roomNodeMap.get("scullery");
        List<String> log = new ArrayList<>();
        Map<String, RoomNode> itemsByRoom = MapUtils.findItems(roomNodeMap, itemsToFind, startingPoint, log);
        assertTrue("Found the same number of items", itemsByRoom.size() == itemsToFind.size());
        for (String item : itemsToFind) {
            assertTrue(item + " was found", itemsByRoom.containsKey(item));
        }
    }

    @Test
    public void testFindShortestPath() throws Exception {
        RoomNode startingPoint = roomNodeMap.get("scullery");
        List<String> log = new ArrayList<>();
        Map<String, RoomNode> itemsByRoom = MapUtils.findItems(roomNodeMap, itemsToFind, startingPoint, log);
        String itemToFind = itemsToFind.get(0);
        RoomNode expectedRoom = itemsByRoom.get(itemToFind);
        List<RoomNode> path = MapUtils.findShortestPath(roomNodeMap, itemsByRoom, startingPoint, itemToFind);
        RoomNode lastRoom = Iterables.getLast(path);
        assertEquals(itemsByRoom + " should be in the " + expectedRoom, expectedRoom, lastRoom);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFindShortestPathInvalidItem() throws Exception {
        expectedException.expect(InvalidGraphSearchParametersException.class);
        expectedException.expectMessage("The item bogus is not in any of the rooms");
        RoomNode startingPoint = roomNodeMap.get("scullery");
        List<String> log = new ArrayList<>();
        Map<String, RoomNode> itemsByRoom = MapUtils.findItems(roomNodeMap, itemsToFind, startingPoint, log);
        MapUtils.findShortestPath(roomNodeMap, itemsByRoom, startingPoint, "bogus");
    }

    @Test
    public void testFindShortestPathDisconnectedGraph() throws Exception {
        expectedException.expect(InvalidGraphSearchParametersException.class);
        expectedException.expectMessage("The graph is not connected");
        List<String> log = new ArrayList<>();
        RoomNode startingPoint = roomNodeMap.get("scullery");
        Map<String, RoomNode> itemsByRoom = MapUtils.findItems(roomNodeMap, itemsToFind, startingPoint, log);
        startingPoint = new RoomNode("Disconnected", "disconnected");
        MapUtils.findShortestPath(roomNodeMap, itemsByRoom, startingPoint, "Book");
    }
}
