package nl.adeda.reisplanner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Antonio on 24-3-2017.
 */

public class XMLParser {



    public static ReisData parse(Document doc, ReisData data) {

        // Initialize ArrayList for directions objects
        ArrayList<ReisDirections> directionsList = new ArrayList<>();

        // Check for errors
        Element root = doc.getDocumentElement();
        if (root.getTagName().equals("error")){
            return data;
        }

        // Navigate to relevant nodes
        Node reisMogelijkheid = root.getElementsByTagName("ReisMogelijkheid").item(0);
        Element rmElement = (Element) reisMogelijkheid;

        Node aantalOverstappen = rmElement.getElementsByTagName("AantalOverstappen").item(0);
        Node reisTijd = rmElement.getElementsByTagName("ActueleReisTijd").item(0);

        // General information elements
        Element aoElement = (Element) aantalOverstappen;
        Element rtElement = (Element) reisTijd;

        // Get information from nodes
        String overstappen = aoElement.getTextContent();
        String reistijd = rtElement.getTextContent();

        // Travel route information elements
        NodeList reisDelen = rmElement.getElementsByTagName("ReisDeel");

        for (int i = 0; i < reisDelen.getLength(); i++) {
            Node reisDeel = reisDelen.item(i);
            Element rdElement = (Element) reisDeel;

            NodeList reisStops = rdElement.getElementsByTagName("ReisStop");

            for (int j = 0; j < reisStops.getLength(); j++) {
                Node reisStop = reisStops.item(j);
                Element rsElement = (Element) reisStop;

                // Return number of nodes with type 'Spoor'
                int spoorNodes = rsElement.getElementsByTagName("Spoor").getLength();

                // Navigate to station, time ('tijd') and platform ('spoor')
                Node stationNode = rsElement.getElementsByTagName("Naam").item(0);
                Node tijdNode = rsElement.getElementsByTagName("Tijd").item(0);
                Node spoorNode = null;
                if (spoorNodes > 0) {
                    spoorNode = rsElement.getElementsByTagName("Spoor").item(0);
                }

                // Get strings from nodes
                String station = stationNode.getTextContent();
                String tijd = tijdNode.getTextContent();
                String spoor = "";
                if (spoorNode != null) {
                    spoor = spoorNode.getTextContent();
                }

                // Put 'ReisStop' information into lists
                ReisDirections directions = new ReisDirections();
                directions.setStation(station);
                directions.setPlatform(spoor);

                // Convert timestamp
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                SimpleDateFormat output = new SimpleDateFormat("HH:mm");
                Date parsedDate = null;
                try {
                    parsedDate = input.parse(tijd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String newTijd = output.format(parsedDate);

                directions.setTime(newTijd);

                directionsList.add(directions);

            }
        }

        // Set information to 'ReisData' class
        data.setChanges(overstappen);
        data.setTravelTime(reistijd);
        data.setDirections(directionsList);

        return data;


    }
}
