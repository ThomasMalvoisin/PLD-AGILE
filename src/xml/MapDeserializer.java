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
	
	
	/**
	 * Deserializes a XML map file into a CityMap Class
	 * @param xml XML file path
	 * @return A CityMap Object containing the data in the  XML file organized in the right attributes
	 * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration requested for reading xml file.
	 * @throws SAXException if any parse errors occur
	 * @throws IOException if file not found
	 * @throws ExceptionXML if file does not respect defined structure or has missing or illegal information
	 */
	public static CityMap load(File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
        	return buildFromDOMXML(racine);
        }
        else
        	throw new ExceptionXML("Illegal XML File");
	}

	
    /**
     * Builds a CityMap Object From a parent XML Element 
     * @param root A XML Element corresponding to the root of XML file, has to respect the format of the root of an XML Map
     * @return A CityMap Object containing the data in the root element
     * @throws ExceptionXML if root element does not respect defined structure or has missing or illegal information
     * @throws NumberFormatException if root contains a string that contains a non parsable number.
     */
    private static CityMap buildFromDOMXML(Element root) throws ExceptionXML, NumberFormatException{
    	CityMap map = new CityMap();
       	NodeList intersectionList = root.getElementsByTagName("noeud");
       	for (int i = 0; i < intersectionList.getLength(); i++) {
        	map.addIntersection(createInteresection((Element) intersectionList.item(i)));
       	}
       	NodeList sectionListe= root.getElementsByTagName("troncon");
       	for (int i = 0; i < sectionListe.getLength(); i++) {
          	map.addSection(createSection((Element) sectionListe.item(i),map));
       	}
       	return map;
    }
    
    
    /**
     * Builds Intersection object from a XML Element that respects the format for an intersection XMl Element
     * @param element XML Element corresponding to a map intersection 
     * @return An Intersecion object containing the data in element
     */
    private static Intersection createInteresection(Element element) {
    	double lat =  Double.parseDouble(element.getAttribute("latitude"));
    	double longitude =  Double.parseDouble(element.getAttribute("longitude"));
    	long id  = Long.parseLong(element.getAttribute("id"));
    	return new Intersection(lat,longitude,id);

    }
    
    /**
     * Builds Section object from a XML Element that respects the format for a Section XML Element
     * @param element XML Element corresponding to a map section
     * @param map CityMap object of a preloaded map, has to contain the id of the intersections in element
     * @return A Section object containing the data in element
     * @throws ExceptionXML if element cointains intersection id's that aren't in map or element cointains negative section length
     */
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
