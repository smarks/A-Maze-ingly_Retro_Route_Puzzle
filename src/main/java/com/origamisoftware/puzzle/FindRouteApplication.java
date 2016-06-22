package com.origamisoftware.puzzle;

import com.origamisoftware.puzzle.model.AdventureMap;
import com.origamisoftware.puzzle.model.DijkstraAlgorithm;
import com.origamisoftware.puzzle.model.Edge;
import com.origamisoftware.puzzle.model.Graph;
import com.origamisoftware.puzzle.model.RoomNode;
import com.origamisoftware.puzzle.model.Vertex;
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
import java.util.LinkedList;
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
public class FindRouteApplication {


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
    public static void exit(int code) {
        System.exit(code);
    }

    private static void printMap(Map<String, RoomNode> map) {
        System.out.println("There are " + map.size() + " rooms");
        for (String roomKeys : map.keySet()) {
            System.out.println(map.get(roomKeys));
        }

    }

    /**
     * Main entry point
     *
     * @param args - a total of 4 arguments are required.
     *             -map [path to map.xml file]  -scenario [path to scenario.txt file]
     */
    public static void main(String[] args) {
        AppArgs appArgs = new AppArgs();

        CmdLineParser parser = new CmdLineParser(appArgs);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            exit(-1);
        }

        try {
            List<String> scenario = Files.readAllLines(Paths.get((appArgs.scenario)));


            AdventureMap adventureMap = MapUtils.buildMapModelFromDocument(XMLUtils.parseXML(appArgs.map));

            /*
            Map<String, RoomNode> roomContents = MapNavigator.findItems(adventureMap, scenario);
            roomContents.keySet().forEach(new Consumer<String>() {
                @Override
                public void accept(String item) {
                    RoomNode roomNode = roomContents.get(item);
                    System.out.println("Room " + roomNode + " contains: " + item);
                }
            });
            if (scenario.size() != roomContents.size()) {
                System.out.println("Cool, we found all the items!");
            }
            */

            RoomNode startingPoint = adventureMap.getRoomsById().get(scenario.get(0));
            List<String> items = scenario.subList(1, scenario.size());

            for (String item : items) {
                startingPoint =  new Test().testExecute(adventureMap, startingPoint, item);
                System.out.println("\tfound " + item + " in the " + startingPoint.getName());
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("Could not parse " + appArgs.map, e);
        }

    }

    private static class Test {
        private List<RoomNode> nodes;
        private List<Edge> edges;

        RoomNode testExecute(AdventureMap adventureMap, RoomNode startingPoint, String itemToFind) {

            RoomNode roomThatContains = adventureMap.getRoomThatContains(itemToFind);
            nodes = new ArrayList<>(adventureMap.getRoomsById().values());
            edges = new ArrayList<Edge>();

            for (RoomNode roomNode : nodes) {
                edges.addAll(roomNode.getEdges(adventureMap.getRoomsById()));
            }

            Graph graph = new Graph(nodes, edges);
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
            dijkstra.execute(startingPoint);
            LinkedList<Vertex> path = dijkstra.getPath(roomThatContains);

            System.out.println("Starting from " + startingPoint.getName() + " the path to the " + itemToFind + " is ");
            for (Vertex vertex : path) {
                RoomNode x = adventureMap.getRoomsById().get(vertex.getId());
                System.out.println(x.getName());
            }
            return (RoomNode) path.getLast();
        }


    }
}

