package nl.adeda.reisplanner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Antonio on 24-3-2017.
 */

public class XMLParser {

    public void parse(Document doc) {
        NodeList rootNodes = doc.getElementsByTagName("ReisMogelijkheden");
        Node rootNode = rootNodes.item(0);
        Element rootElement = (Element) rootNode;
        Node reisMogelijkheid = rootElement.getElementsByTagName("ReisMogelijkheid").item(0);
        Element rmElement = (Element) reisMogelijkheid;
        Node aantalOverstappen = rmElement.getElementsByTagName("AantalOverstappen").item(0);
        Element aoElement = (Element) aantalOverstappen;
        String overstappen =  aoElement.getTextContent();

    }
}
