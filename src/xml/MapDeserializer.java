package xml;
import model.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapDeserializer {
	
	public static void charger(CityMap map, File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
           construireAPartirDeDOMXML(racine, map);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}

    private static void construireAPartirDeDOMXML(Element noeudDOMRacine, CityMap map) throws ExceptionXML, NumberFormatException{
       	NodeList intersectionList = noeudDOMRacine.getElementsByTagName("noeud");
       	for (int i = 0; i < intersectionList.getLength(); i++) {
        	map.addIntersection(createInteresection((Element) intersectionList.item(i)));
       	}
       	NodeList sectionListe= noeudDOMRacine.getElementsByTagName("troncon");
       	for (int i = 0; i < sectionListe.getLength(); i++) {
          	map.addSection(createSection((Element) sectionListe.item(i),map));
       	}
    }
    
    //verification a faire . conforme ???
    private static Intersection createInteresection(Element element) {
    	double lat =  Double.parseDouble(element.getAttribute("latitude"));
    	double longitude =  Double.parseDouble(element.getAttribute("longitude"));
    	long id  = Long.parseLong(element.getAttribute("id"));
    	return new Intersection(lat,longitude,id);

    }
    
    //check that attributes are numeric ???
    private static Section createSection(Element element,CityMap map) throws ExceptionXML {
    	long idDest  = Long.parseLong(element.getAttribute("destination"));
    	long idOrigine  = Long.parseLong(element.getAttribute("origine"));
    	double longueur =  Double.parseDouble(element.getAttribute("longueur"));
    	if(longueur < 0 ) {
    		throw new ExceptionXML("XML load error : Negative section length");
    	}
    	String name = element.getAttribute("nomRue");
    	Intersection intersectionDest = map.getIntersectionById(idDest);
    	Intersection intersectionOrigine = map.getIntersectionById(idOrigine);
    	if(intersectionDest == null || intersectionOrigine == null ) {
    		throw new ExceptionXML("XML load error : Missing intersection in XML file ");
    	}
    	return new Section(intersectionOrigine,intersectionDest,name,longueur);
    }
}
