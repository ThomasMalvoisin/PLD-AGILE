package xml;

import java.io.File;
import java.io.IOException;

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
import model.Section;

public class DeliveryRequestDeserializer {
	
	public static void Load(DeliveryRequest deliveryrequest,CityMap map, File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("demandeDeLivraisons")) {
        	buildFromDOMXML(racine, map, deliveryrequest);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}

    private static void buildFromDOMXML(Element noeudDOMRacine,CityMap map, DeliveryRequest deliveryrequest) throws ExceptionXML, NumberFormatException{
       	NodeList deliveryList = noeudDOMRacine.getElementsByTagName("livraison");
       	for (int i = 0; i < deliveryList.getLength(); i++) {
       		deliveryrequest.addDelivery(createDelivery((Element) deliveryList.item(i),map));
       	}
       	NodeList warehouseListe= noeudDOMRacine.getElementsByTagName("entrepot");
       	if(warehouseListe.getLength() != 1 ) {
       		throw new ExceptionXML("Document non conforme, The file can't contain more than one warehouse!");
       	}
       		deliveryrequest.setWarehouse(getWarehouse((Element) warehouseListe.item(0),map));
    }
    
    //verification a faire . conforme ???
    private static Delivery createDelivery(Element element,CityMap map) throws ExceptionXML {
    	long intersectionId  = Long.parseLong(element.getAttribute("id"));
    	Intersection intersection  = map.getIntersectionById(intersectionId);
    	if(intersection == null ) {
    		throw new ExceptionXML("XML load error : Intersection address not found");
    	}
    	int duree =  Integer.parseInt(element.getAttribute("duree"));
    	return new Delivery(duree,intersection);

    }
    
    //check that attributes are numeric ???
    private static Intersection getWarehouse(Element element,CityMap map) throws ExceptionXML {
    	long intersectionId  = Long.parseLong(element.getAttribute("id"));
    	Intersection intersection  = map.getIntersectionById(intersectionId);
    	if(intersection == null ) {
    		throw new ExceptionXML("XML load error : Intersection address not found");
    	}
    	return intersection;
    }
}