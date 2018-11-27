package Tests;
import model.CityMap;
import model.Intersection;
import model.Section;
import xml.ExceptionXML;
import xml.MapDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

public class DeserialisationTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void ValideTest1() {
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
		mapAttendu.addSection(new Section (inter4, inter2, "Rue Sainte-Anne de Baraban", 133));
		mapAttendu.addSection(new Section (inter2, inter4, "Rue Sainte-Anne de Baraban", 134));
		
		CityMap mapCree = new CityMap();
		
		/*mapCree.addIntersection(inter3);
		mapCree.addIntersection(inter1);
		mapCree.addIntersection(inter2);
		mapCree.addIntersection(inter4);
		mapCree.addSection(new Section (inter3, inter4, "Avenue Lacassagne",132));
		mapCree.addSection(new Section (inter4, inter2, "Rue Sainte-Anne de Baraban", 133));
		mapCree.addSection(new Section (inter2, inter3, "Rue de l'Abondance" , 130));
		mapCree.addSection(new Section (inter2, inter4, "Rue Sainte-Anne de Baraban", 134));
		mapCree.addSection(new Section (inter1, inter2, "Rue Danton", 69.979805));

		mapCree.addSection(new Section (inter3, inter2, "Rue de l'Abondance",131));
		*/
		
		File file = new File ("C:\\Users\\Samuel GOY\\Documents\\PLD AGILE\\Xml\\TestValide1.xml");
		try {
			mapCree = MapDeserializer.load ( file);
		} catch(Exception e) {};
		assert (mapAttendu.equals(mapCree));

	
	}

	// xml file first node is 'ville'
	@Test
	public void InValideTest1() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		//CityMap mapCree = new CityMap();
		File file = new File ("C:\\Users\\Samuel GOY\\Documents\\PLD AGILE\\Xml\\TestInvalide1.xml");
		thrown.expect(ExceptionXML.class);
		MapDeserializer.load ( file);
		
	}
	
	
	// Section with no existing intersection
	@Test
	public void InValideTest2() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		//CityMap mapCree = new CityMap();
		File file = new File ("C:\\Users\\Samuel GOY\\Documents\\PLD AGILE\\Xml\\TestInvalide2.xml");
		thrown.expect(ExceptionXML.class);
		MapDeserializer.load( file);
		
	}
	
	// Noeud without attribute id
	@Test
	public void InValideTest3() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		CityMap mapCree = new CityMap();
		File file = new File ("C:\\Users\\Samuel GOY\\Documents\\PLD AGILE\\Xml\\TestInvalide3.xml");
		thrown.expect(NumberFormatException.class);
		MapDeserializer.load (file);
		
	}
	
	// Longueur is negative
		@Test
		public void InValideTest4() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
			CityMap mapCree = new CityMap();
			File file = new File ("C:\\Users\\Samuel GOY\\Documents\\PLD AGILE\\Xml\\TestInvalide4.xml");
			thrown.expect(ExceptionXML.class);
			MapDeserializer.load (file);
			
		}

	
	

	
}
