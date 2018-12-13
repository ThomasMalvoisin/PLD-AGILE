package Tests;
import model.CityMap;
import model.Intersection;
import model.Journey;
import model.Section;
import xml.ExceptionXML;
import algo.Algorithms;
import algo.ExceptionAlgo;

import java.lang.Long;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;

public class AlgoTest {
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void DijkstraOneToNTest() throws ExceptionAlgo {
		CityMap map =new CityMap();
		Intersection inter1 = new Intersection (41, 51.51, 1);
		Intersection inter2 = new Intersection (42, 52.51, 2);
		Intersection inter3 = new Intersection (43, 53.51, 3);
		Intersection inter4 = new Intersection (44, 54.51, 4);
		Intersection inter5 = new Intersection (44, 54.51, 5);
		Intersection inter6 = new Intersection (44, 54.51, 6);
		map.addIntersection(inter1);
		map.addIntersection(inter2);
		map.addIntersection(inter3);
		map.addIntersection(inter4);
		map.addIntersection(inter5);
		map.addIntersection(inter6);
		Section section12=new Section (inter1, inter2, "Rue Danton", 1);
		Section section13=new Section (inter1, inter3, "Rue de l'Abondance" , 2);
		Section section25=new Section (inter2, inter5, "Rue25",4);
		Section section35=new Section (inter3, inter5, "Avenue Lacassagne",2);
		Section section34=new Section (inter3, inter4, "Rue Sainte-Anne de Baraban", 3);
		Section section46=new Section (inter4, inter6, "Rue46", 3);
		Section section56=new Section (inter5, inter6, "Rue56", 5);
		
		map.addSection(section12);
		map.addSection(section13);
		map.addSection(section25);
		map.addSection(section35);
		map.addSection(section34);
		map.addSection(section46);
		map.addSection(section13);
		
		ArrayList<Intersection> intersectionList = new ArrayList<Intersection>();
		intersectionList.add(inter1);
		intersectionList.add(inter5);
		intersectionList.add(inter6);
		Map<Long, Journey> dijkstra = map.dijkstraOneToN (inter1, intersectionList);
		
		List<Section> sectionList1 = new ArrayList<Section> ();
		
		List<Section> sectionList5 = new ArrayList<Section> ();
		sectionList5.add(section13);
		sectionList5.add(section35);
		
		List<Section> sectionList6 = new ArrayList<Section> ();
		sectionList6.add(section13);
		sectionList6.add(section34);
		sectionList6.add(section46);
		
		Journey j1 = new Journey(inter1, inter1, sectionList1, 0);
		Journey j5 = new Journey(inter1, inter5, sectionList5, 4);
		Journey j6 = new Journey(inter1, inter6, sectionList6, 8);
		Map<Long, Journey> theorique = new HashMap<Long, Journey> ();
		
		Long id1 = inter1.getId();
		Long id5 = inter5.getId();
		Long id6 = inter6.getId();

		theorique.put(id1, j1);
		theorique.put(id5, j5);
		theorique.put(id6, j6);
		
		if(theorique.size() != dijkstra.size()) {
			   fail("map de taille différente");
			   
	   }
		Set keysSections = theorique.keySet();
		Iterator it_sec = keysSections.iterator();
		while (it_sec.hasNext()){
		   Object key = it_sec.next(); 
		   if (dijkstra.get(key).getOrigin()!= theorique.get(key).getOrigin() || dijkstra.get(key).getDestination()!= theorique.get(key).getDestination() || dijkstra.get(key).getLength()!= theorique.get(key).getLength()) {
			   fail("mauvaise origine, destination ou longueur");
		   }
		   List<Section> list_sec = theorique.get(key).getSectionList();
		   for(Section s : list_sec) {
			   if(dijkstra.get(key).getSectionList().indexOf(s)==-1) {
				   fail("liste de Section non égale");
				}
		   }
		}
		return;
	}
	
	
	@Test 	//Point isolé dans la liste d'intersection
	public void DijkstraOneToNTestIsole1 () throws ExceptionAlgo {
		CityMap map =new CityMap();
		Intersection inter1 = new Intersection (41, 51.51, 1);
		Intersection inter2 = new Intersection (42, 52.51, 2);
		Intersection inter3 = new Intersection (43, 53.51, 3);
		Intersection inter4 = new Intersection (44, 54.51, 4);
		Intersection inter5 = new Intersection (44, 54.51, 5);
		Intersection inter6 = new Intersection (44, 54.51, 6);
		Intersection interIsole = new Intersection (44, 57.51, 7);
		map.addIntersection(inter1);
		map.addIntersection(inter2);
		map.addIntersection(inter3);
		map.addIntersection(inter4);
		map.addIntersection(inter5);
		map.addIntersection(inter6);
		map.addIntersection(interIsole);
		Section section12=new Section (inter1, inter2, "Rue Danton", 1);
		Section section13=new Section (inter1, inter3, "Rue de l'Abondance" , 2);
		Section section25=new Section (inter2, inter5, "Rue25",4);
		Section section35=new Section (inter3, inter5, "Avenue Lacassagne",2);
		Section section34=new Section (inter3, inter4, "Rue Sainte-Anne de Baraban", 3);
		Section section46=new Section (inter4, inter6, "Rue46", 3);
		Section section56=new Section (inter5, inter6, "Rue56", 5);
		
		map.addSection(section12);
		map.addSection(section13);
		map.addSection(section25);
		map.addSection(section35);
		map.addSection(section34);
		map.addSection(section46);
		map.addSection(section13);
		
		ArrayList<Intersection> intersectionList = new ArrayList<Intersection>();
		intersectionList.add(inter1);
		intersectionList.add(inter5);
		intersectionList.add(interIsole);
		
		thrown.expect(ExceptionAlgo.class);
		Map<Long, Journey> dijkstra = map.dijkstraOneToN (inter1, intersectionList);
	}
	
	
	@Test 	//Point de départ isolé
	public void DijkstraOneToNTestIsole2 () throws ExceptionAlgo {
		CityMap map =new CityMap();
		Intersection inter1 = new Intersection (41, 51.51, 1);
		Intersection inter2 = new Intersection (42, 52.51, 2);
		Intersection inter3 = new Intersection (43, 53.51, 3);
		Intersection inter4 = new Intersection (44, 54.51, 4);
		Intersection inter5 = new Intersection (44, 54.51, 5);
		Intersection inter6 = new Intersection (44, 54.51, 6);
		Intersection interIsole = new Intersection (44, 57.51, 7);
		map.addIntersection(inter1);
		map.addIntersection(inter2);
		map.addIntersection(inter3);
		map.addIntersection(inter4);
		map.addIntersection(inter5);
		map.addIntersection(inter6);
		map.addIntersection(interIsole);
		Section section12=new Section (inter1, inter2, "Rue Danton", 1);
		Section section13=new Section (inter1, inter3, "Rue de l'Abondance" , 2);
		Section section25=new Section (inter2, inter5, "Rue25",4);
		Section section35=new Section (inter3, inter5, "Avenue Lacassagne",2);
		Section section34=new Section (inter3, inter4, "Rue Sainte-Anne de Baraban", 3);
		Section section46=new Section (inter4, inter6, "Rue46", 3);
		Section section56=new Section (inter5, inter6, "Rue56", 5);
		
		map.addSection(section12);
		map.addSection(section13);
		map.addSection(section25);
		map.addSection(section35);
		map.addSection(section34);
		map.addSection(section46);
		map.addSection(section13);
		
		ArrayList<Intersection> intersectionList = new ArrayList<Intersection>();
		intersectionList.add(inter1);
		intersectionList.add(inter5);
		intersectionList.add(inter6);
		
		thrown.expect(ExceptionAlgo.class);
		Map<Long, Journey> dijkstra = map.dijkstraOneToN (interIsole, intersectionList);
	}
	
	
	
	
}
