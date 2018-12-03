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
        	throw new ExceptionXML("Document non conforme");
	}

    private static DeliveryRequest buildFromDOMXML(Element noeudDOMRacine,CityMap map) throws ExceptionXML, NumberFormatException, ParseException{
    	DeliveryRequest deliveryRequest = new DeliveryRequest();
       	NodeList deliveryList = noeudDOMRacine.getElementsByTagName("livraison");
       	for (int i = 0; i < deliveryList.getLength(); i++) {
       		deliveryRequest.addDelivery(createDelivery((Element) deliveryList.item(i),map));
       	}
       	NodeList warehouseListe= noeudDOMRacine.getElementsByTagName("entrepot");
       	if(warehouseListe.getLength() != 1 ) {
       		throw new ExceptionXML("Document non conforme, The file can't contain more than one warehouse!");
       	}
       	deliveryRequest.setWarehouse(getWarehouse((Element) warehouseListe.item(0),map));
       	deliveryRequest.setStartTime(getStartTime((Element) warehouseListe.item(0)));
       	return deliveryRequest;
    }
    
    //verification a faire . conforme ???
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
    
    //check that attributes are numeric ???
    private static Intersection getWarehouse(Element element,CityMap map) throws ExceptionXML {
    	long intersectionId  = Long.parseLong(element.getAttribute("adresse"));
    	Intersection intersection  = map.getIntersectionById(intersectionId);
    	if(intersection == null ) {
    		throw new ExceptionXML("XML load error : Intersection address not found");
    	}
    	return intersection;
    }
    private static Date getStartTime(Element element) throws ParseException {
    	//TODO get the date with the right format !!!
    	SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    	Date startTime  = df.parse(element.getAttribute("heureDepart"));
    	return startTime;
    	
    }
}