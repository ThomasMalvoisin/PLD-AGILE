package Tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

import model.CityMap;
import model.DeliveryRequest;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class DeliveryRequestDeserealisationTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
		//node 'livraison' with no existing 'adresse'
		@Test
		public void InValideTest1() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, NumberFormatException, ParseException, URISyntaxException {
			CityMap map = new CityMap();
			File file_map = new File (getClass().getResource("petitPlan.xml").toURI());
			map = MapDeserializer.load (file_map);

			File file_deliveryReq = new File (getClass().getResource("invalide-dl-petit2.xml").toURI());
			thrown.expect(ExceptionXML.class);
			DeliveryRequestDeserializer.Load(map, file_deliveryReq);
			
			
		}
		
		//negative duration
		@Test
		public void InValideTest2() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, NumberFormatException, ParseException, URISyntaxException {
			CityMap map = new CityMap();
			File file_map = new File (getClass().getResource("petitPlan.xml").toURI());
			map = MapDeserializer.load (file_map);
			File file_deliveryReq = new File (getClass().getResource("invalide-dl-petit2.xml").toURI());
			thrown.expect(ExceptionXML.class);
			DeliveryRequestDeserializer.Load(map, file_deliveryReq);
			
			
		}
		
		//file with 2 warehouses
		@Test
		public void InValideTest3() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, NumberFormatException, ParseException, URISyntaxException {
			CityMap map = new CityMap();
			File file_map = new File (getClass().getResource("petitPlan.xml").toURI());
			map = MapDeserializer.load (file_map);
			File file_deliveryReq = new File (getClass().getResource("invalide-dl-petit3.xml").toURI());
			thrown.expect(ExceptionXML.class);
			DeliveryRequestDeserializer.Load(map, file_deliveryReq);
			
			
		}
		
		//Check heureDepart
		@Test
		public void ValideTest4() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, NumberFormatException, ParseException, URISyntaxException {
			CityMap map = new CityMap();
			File file_map = new File (getClass().getResource("petitPlan.xml").toURI());
			map = MapDeserializer.load (file_map);
			File file_deliveryReq = new File (getClass().getResource("dl-petit-3.xml").toURI());
			DeliveryRequest deliverRequest = DeliveryRequestDeserializer.Load(map, file_deliveryReq);
			String string = "08:00:00";
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			Date date = format.parse(string);
			assert(date.equals(deliverRequest.getStartTime()));
			
		}
}
