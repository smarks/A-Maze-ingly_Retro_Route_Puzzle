package com.origamisoftware.puzzle;

import com.origamisoftware.puzzle.model.AdventureMap;
import com.origamisoftware.puzzle.model.RoomNode;
import com.origamisoftware.puzzle.util.MapUtils;
import com.origamisoftware.puzzle.util.XMLUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main entry point for the  A-Maze-ingly Retro Route Puzzle
 * <p>
 * Given a Map which contains a list of rooms and what they contain which other rooms they connect to, and another file
 * which contains a list of items to collect, this program will determine a route through the rooms that will result in being
 * to collect all the items.
 *
 * @author <a href="mailto:smarks@origamisoftware.com"></a>
 */
class FindRouteApplication {


    /**
     * This inner is used to specify the command line arguments using the Args4j framework.
     */
    private static class AppArgs {
        @Option(required = true, name = "-map", usage = "Specify the full path to an xml that describes the map")
        private String map;

        @Option(required = true, name = "-scenario", usage = "Specify the full path to text file that describes a search scenario")
        private String scenario;
    }


    /**
     * Provide a single exit point for the application.
     *
     * @param code the exit value to pass to the caller. 0 denotes normal termination. Anything else
     *             denotes an error.
     */
    private static void exit(int code) {
        System.exit(code);
    }

    private static void printMap(Map<String, RoomNode> map) {
        System.out.println("There are " + map.size() + " rooms");
        for (String roomKeys : map.keySet()) {
            System.out.println(map.get(roomKeys));
        }

    }

    /**
     * Main entry point of the find the items in the map program.
     *
     * @param args - a total of 4 arguments are required.
     *             -map [path to map.xml file]  -scenario [path to scenario.txt file]
     */
    public static void main(String[] args) {

        // get the paths to the two input files or punt
        AppArgs appArgs = new AppArgs();

        CmdLineParser parser = new CmdLineParser(appArgs);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            exit(-1);
        }

        // the contents of the scenario file
        List<String> scenario;

        /**
         *  Map that will be created by parsing XML document. The key is the room Id as provided in the XML
         *  The value is a RoomNode object which contains all the information provided in the XML Room element.
         */
        Map<String, RoomNode> roomsById;

        try {

            scenario = Files.readAllLines(Paths.get((appArgs.scenario)));

            // parse the XML and return a map where the keys are the room ids the value is a RoomNode (or vertex).
            roomsById = MapUtils.buildMapModelFromDocument(XMLUtils.parseXML(appArgs.map));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("Error reading input files", e);
        }

        // The first line in the scenario is the room to start in. The remaining lines are items to find.
        RoomNode startingPoint = roomsById.get(scenario.get(0));
        List<String> itemsToFind = scenario.subList(1, scenario.size());

        // This map is filled by searchRooms method. The key is the item and the value is the room the item is in.
        Map<String, RoomNode> roomByContents = new HashMap<>();

        // Find all the items in the all the rooms, populating roomByContents
        MapUtils.searchRooms(startingPoint, roomsById, itemsToFind, roomByContents);

        // Find the shortest path to each item, one item at a time from the room the last item was found it.
        System.out.println(
                "\n\n>> Find Each item starting from the starting point or the room where the last item was found.\n\n");

        AdventureMap adventureMap = new AdventureMap(roomsById, roomByContents);
        List<RoomNode> rooms = new ArrayList<>();
        rooms.add(startingPoint);
        for (String item : itemsToFind) {
            rooms = MapUtils.findShortestPath(adventureMap, rooms.get(rooms.size() - 1), item);
            System.out.println("I collect the " + item);
        }
    }
}


