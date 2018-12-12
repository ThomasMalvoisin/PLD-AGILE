package xml;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;

public class DeliveryRequestDeserializer {
	
	
	/**
	 * Deserializes a XML deliveryRequest file into a DeliveryRequest Class
	 * @param map CityMap object of a preloaded map
	 * @param xml XML file path
	 * @return A DeliveryRequest Object containing the data in the  XML file organized in the right attributes
	 * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration requested for reading xml file.   
	 * @throws SAXException if any parse errors occur
	 * @throws IOException if file not found
	 * @throws ExceptionXML  if file does not respect defined structure or has missing or illegal information
	 * @throws NumberFormatException if xml file cointains illegal number types
	 * @throws ParseException if xml file cointains illegal dates
	 */
	public static DeliveryRequest Load(CityMap map, File xml) throws ParserConfigurationException,
	SAXException, IOException, ExceptionXML, NumberFormatException, ParseException{
		if(map == null ) {
			throw new ExceptionXML("Can't Load delivery request. Null map");
		}
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("demandeDeLivraisons")) {
        	return buildFromDOMXML(racine, map);
        }
        else
        	throw new ExceptionXML("Illegal XML File");
	}

    /**
     * Builds a DeliveryRequest Object From a parent XML Element 
     * @param root A XML Element corresponding to the root of XML file, has to respect the format of the root of an XML DeliveryRequest
     * @param map CityMap object of a preloaded map, has to contain the id of the intersections in element
     * @return A DeliveryRequest Object containing the data in the root element
     * @throws ExceptionXML if file does not respect defined structure or has missing or illegal information
     * @throws NumberFormatException if xml file cointains illegal number types
     * @throws ParseException  if xml file cointains illegal dates
     */
    private static DeliveryRequest buildFromDOMXML(Element root,CityMap map) throws ExceptionXML, NumberFormatException, ParseException{
    	DeliveryRequest deliveryRequest = new DeliveryRequest();
       	NodeList deliveryList = root.getElementsByTagName("livraison");
       	for (int i = 0; i < deliveryList.getLength(); i++) {
       		deliveryRequest.addDelivery(createDelivery((Element) deliveryList.item(i),map));
       	}
       	NodeList warehouseListe= root.getElementsByTagName("entrepot");
       	if(warehouseListe.getLength() != 1 ) {
       		throw new ExceptionXML("Illega XML File, The file can't contain more than one warehouse!");
       	}
       	deliveryRequest.setWarehouse(getWarehouse((Element) warehouseListe.item(0),map));
       	deliveryRequest.setStartTime(getStartTime((Element) warehouseListe.item(0)));
       	return deliveryRequest;
    }
    
    
    
    
    /**
     * Builds Delivery object from a XML Element that respects the format for an delivery XMl Element
     * @param element XML Element corresponding to a Delivery
     * @param map CityMap object of a preloaded map, has to contain the intersection in the element 
     * @return A Delivery object containing the data in element
     * @throws ExceptionXML if map doesn't contain intersection in element , or element contains negative duration 
     */
    private static Delivery createDelivery(Element element,CityMap map) throws ExceptionXML {
    	long intersectionId  = Long.parseLong(element.getAttribute("adresse"));
    	Intersection intersection  = map.getIntersectionById(intersectionId);
    	if(intersection == null ) {
    		throw new ExceptionXML("XML load error : Intersection address not found");
    	}
    	int duree =  Integer.parseInt(element.getAttribute("duree"));
    	if(duree < 0 ) {
    		throw new ExceptionXML("XML load error : Negative duration");
    	}
    	return new Delivery(duree,intersection);
    }
    
    
    /**
     * Gets the Intersection Object in map corresponding to the warehouse in element
     * @param XML element of a warehouse 
     * @param map CityMap object of a preloaded map, has to contain the intersection corresponding to the intersection id in element 
     * @return Intersection corresponding to the warehouse
     * @throws ExceptionXML if the intersection id in element isn't in map
     */
    private static Intersection getWarehouse(Element element,CityMap map) throws ExceptionXML {
    	long intersectionId  = Long.parseLong(element.getAttribute("adresse"));
    	Intersection intersection  = map.getIntersectionById(intersectionId);
    	if(intersection == null ) {
    		throw new ExceptionXML("XML load error : Intersection address not found");
    	}
    	return intersection;
    }
    
    
    /**
     * gets the StartTime of the DeliveryRequest
     * @param element XML element of a warehouse 
     * @return Date startDate in the element 
     * @throws ParseException if element contains illegal dates
     */
    private static Date getStartTime(Element element) throws ParseException {
    	SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    	Date startTime  = df.parse(element.getAttribute("heureDepart"));
    	return startTime;
    	
    }
}