package com.origamisoftware.puzzle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the FindRoute application
 */
public class FindRouteTest {

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void verifyNoArguments() {
        exit.expectSystemExitWithStatus(-1);
        FindRouteApplication.main(new String[]{});
        assertEquals(" -map VAL : Specify the full path to an xml that describes the map\n" +
                " -scenario VAL : Specify the full path to text file that describes a search\n" +
                "                 scenario", systemErrRule.getLog());
    }

    @Test
    public void verifyIncorrectArguments() {
        exit.expectSystemExitWithStatus(-1);
        FindRouteApplication.main(new String[]{"-map","foo","-snap","caller"});
        assertEquals(" -map VAL : Specify the full path to an xml that describes the map\n" +
                " -scenario VAL : Specify the full path to text file that describes a search\n" +
                "                 scenario", systemErrRule.getLog());
    }

    @Test(expected = IllegalStateException.class)
    public void verifyCorrectArgumentsBadPaths() {
        FindRouteApplication.main(new String[]{"-map","map.xml","-scenario","caller"});
    }

    @Test()
    public void verifyCorrectArgumentsGoodPaths() {
        FindRouteApplication.main(new String[]{"-map","./data/map.xml","-scenario","./data/scenario.txt"});
    }
}
