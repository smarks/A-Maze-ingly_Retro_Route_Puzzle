package com.origamisoftware.puzzle;

import com.origamisoftware.puzzle.util.XMLUtils;
import com.origamisoftware.puzzle.util.MapUtils;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Main entry point for the  A-Maze-ingly Retro Route Puzzle
 * <p>
 * Given a Map which contains a list of rooms and what they contain which other rooms they connect to, and another file
 * which contains a list of items to collect, this program will determine a route through the rooms that will result in being
 * to collect all the items.
 *
 * @author <a href="mailto:smarks@origamisoftware.com"></a>
 */
public class FindRoute {


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
            MapUtils.buildMapModelFromDocument(XMLUtils.parseXML(appArgs.map));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("Could not parse " + appArgs.map, e);
        }
    }

}
