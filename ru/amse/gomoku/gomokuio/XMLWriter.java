package ru.amse.gomoku.gomokuio;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.amse.gomoku.ui.gui.game.GUIGame;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.Hashtable;

/**
 * 
 */
public class XMLWriter {

    private Color myBoardColor;
    private String myDibFirst;
    private String myDibSecond;

    private Hashtable<String, String> myPlayers = new Hashtable<String, String>();

    public void writeXML() {
        File f = new File(GUIGame.ourPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document myDocument = null;
        try {
            builder = factory.newDocumentBuilder();
            myDocument = builder.newDocument();
            Element root = myDocument.createElement("Settings");

            myDocument.appendChild(root);
            Element board = myDocument.createElement(XMLReader.boardColor);
            board.setAttribute(XMLReader.red, Integer.toString(myBoardColor.getRed()));
            board.setAttribute(XMLReader.green, Integer.toString(myBoardColor.getGreen()));
            board.setAttribute(XMLReader.blue, Integer.toString(myBoardColor.getBlue()));
            root.appendChild(board);

            Element dibs = myDocument.createElement(XMLReader.dibs);
            dibs.setAttribute(XMLReader.dibColorFirst, myDibFirst);
            dibs.setAttribute(XMLReader.dibColorSecond, myDibSecond);
            root.appendChild(dibs);

            for (String name : myPlayers.keySet()) {
                Element player = myDocument.createElement(XMLReader.player);
                player.setAttribute(XMLReader.playerName, name);
                player.setAttribute(XMLReader.playerUrl, myPlayers.get(name));
                root.appendChild(player);
            }

        } catch ( ParserConfigurationException e ) {
            e.printStackTrace();
        }
        if (myDocument != null) {
            try {
                Transformer t = TransformerFactory.newInstance().newTransformer();
                try {
                    t.transform(new DOMSource(myDocument), new StreamResult(new FileOutputStream(f)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMyBoardColor(Color myBoardColor) {
        this.myBoardColor = myBoardColor;
    }

    public void setPlayer(String name, String url) {
        myPlayers.put(name, url);
    }

    public void setDibs(String first, String second) {
        myDibFirst = first;
        myDibSecond = second;
    }
}
