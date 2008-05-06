package ru.amse.gomoku.gomokuio;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.*;

import java.io.File;
import java.io.IOException;

import java.util.Hashtable;

/**
 * 
 */
public class XMLReader {

    final String myPath;

    static final String dibs = "dibs";
    static final String dibColorFirst = "dibColorFirst";
    static final String dibColorSecond = "dibColorSecond";

    static final String player = "player";
    static final String playerName = "name";
    static final String playerUrl = "url";

    static final String boardColor = "boardColor";
    static final String red = "red";
    static final String green = "green";
    static final String blue = "blue";

    private File myFile;
    private Color myBoardColor;
    private String myDibFirst = "Dib1";
    private String myDibSecond = "Dib6";
    private Hashtable<String, String> myPlayers = new Hashtable<String, String>();

    public XMLReader(String path) {
        myPath = path;
    }

    public void parse() {
        myFile = new File(myPath);
        try {
            if (!myFile.createNewFile()) {
                parseXML();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document myDocument = null;
        try {
			builder = factory.newDocumentBuilder();
            try {
                myDocument = builder.parse(myFile);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element root = myDocument.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child instanceof Element) {
                    Element childElement = (Element) child;
                    String name = childElement.getNodeName();
                    if (name.equals(boardColor)) {
                        int r = Integer.parseInt(childElement.getAttribute(red).trim());
                        int g = Integer.parseInt(childElement.getAttribute(green).trim());
                        int b = Integer.parseInt(childElement.getAttribute(blue).trim());
                        myBoardColor = new Color(r, g, b);
                    } else if (name.equals(player)) {
                        String className = childElement.getAttribute(playerName).trim();
                        String url = childElement.getAttribute(playerUrl).trim();
                        myPlayers.put(className, url);
                    } else if (name.equals(dibs)) {
                        myDibFirst = childElement.getAttribute(dibColorFirst);
                        myDibSecond = childElement.getAttribute(dibColorSecond);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
    }

    public Color getBoardColor() {
        return myBoardColor;
    }

    public Hashtable<String, String> getPlayers() {
        return myPlayers;
    }

    public String getDibFirst() {
        return myDibFirst;
    }

    public String getDibSecond() {
        return myDibSecond;
    }
}
