package Tests;
import model.CityMap;
import model.Intersection;
import model.Section;
import xml.ExceptionXML;
import xml.MapDeserializer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

public class DeserialisationTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void ValideTest1() throws URISyntaxException {
		CityMap mapAttendu =new CityMap();
		Intersection inter1 = new Intersection (41, 51.51, 1);
		Intersection inter2 = new Intersection (42, 52.51, 2);
		Intersection inter3 = new Intersection (43, 53.51, 3);
		Intersection inter4 = new Intersection (44, 54.51, 4);
		
		mapAttendu.addIntersection(inter1);
		mapAttendu.addIntersection(inter2);
		mapAttendu.addIntersection(inter3);
		mapAttendu.addIntersection(inter4);
		
		mapAttendu.addSection(new Section (inter1, inter2, "Rue Danton", 69.979805));
		mapAttendu.addSection(new Section (inter2, inter3, "Rue de l'Abondance" , 130));
		mapAttendu.addSection(new Section (inter3, inter2, "Rue de l'Abondance",131));
		mapAttendu.addSection(new Section (inter3, inter4, "Avenue Lacassagne",132));
		mapAttendu.addSection(new Section (inter2, inter4, "Rue Sainte-Anne de Baraban", 133));
		mapAttendu.addSection(new Section (inter4, inter2, "Rue Sainte-Anne de Baraban", 134));
		
		CityMap mapCree = new CityMap();
		

		File file = new File (getClass().getResource("validPlan.xml").toURI());
		try {
			mapCree.copy(MapDeserializer.load (file));
		} catch(Exception e) {};
    	
		assert (mapCree.equals(mapAttendu));

	}

	// xml file first node is 'ville'
	@Test
	public void InValideTest1() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, URISyntaxException {
		File file = new File (getClass().getResource("planInvalid1.xml").toURI());

		thrown.expect(ExceptionXML.class);
		MapDeserializer.load ( file);
		
	}
	
	// Section with no existing intersection
	@Test
	public void InValideTest2() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, URISyntaxException {

		File file = new File (getClass().getResource("planInvalid2.xml").toURI());
		thrown.expect(ExceptionXML.class);
		MapDeserializer.load( file);
		
	}
	
	// Node without attribute id
	@Test
	public void InValideTest3() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, URISyntaxException {
		CityMap mapCree = new CityMap();
		File file = new File (getClass().getResource("planInvalid3.xml").toURI());

		thrown.expect(NumberFormatException.class);
		MapDeserializer.load (file);
		
	}

	// Longueur is negative
	@Test
	public void InValideTest4() throws ParserConfigurationException, SAXException, IOException, ExceptionXML, URISyntaxException {
		CityMap mapCree = new CityMap();
		File file = new File (getClass().getResource("planInvalid4.xml").toURI());
		thrown.expect(ExceptionXML.class);
		MapDeserializer.load (file);
		
	}
}
