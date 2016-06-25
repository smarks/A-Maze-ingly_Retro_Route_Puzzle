package com.origamisoftware.puzzle.util;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for methods in the XMLUtils class.
 */
public class XMLUtilsTest {


    @Test
    public void testParseDocumentPositive() throws Exception{
        Document document = XMLUtils.parseXML("src/test/test_data/good-map.xml");
        assertNotNull("Verify document was returned",document);
    }

    @Test(expected = SAXParseException.class)
    public void testParseDocumentDataDoesMatchDTD() throws Exception{
      XMLUtils.parseXML("src/test/test_data/invalid-map.xml");
     }
}
