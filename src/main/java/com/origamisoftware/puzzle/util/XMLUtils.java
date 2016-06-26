package com.origamisoftware.puzzle.util;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Static methods for dealing with XML documents
 */
public class XMLUtils {

    /**
     * Given a path to an XML instance parse it into a Document.
     *
     * @param pathToMap - the file to parse. If there is DTD associated with the file it will be used to validate the
     *                  instance.
     * @return a org.w3c.dom.Document
     * @throws ParserConfigurationException non recoverable try again with better data.
     * @throws SAXException                 non recoverable try again with better data.
     * @throws IOException                  non recoverable check that file exists
     */
    public static Document parseXML(String pathToMap) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;

            }

            @Override
            public void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });
        return builder.parse(pathToMap);
    }

}