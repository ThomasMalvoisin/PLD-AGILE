package Tests;
import model.CityMap;
import model.Intersection;
import model.Journey;
import model.Section;
import algo.Algorithms;
import algo.ExceptionAlgo;

import java.lang.Long;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TSPTests {
	
	Algorithms algo;
	CityMap map;
	Section section12;
	Section section13;
	Section section25;
	Section section35;
	Section section34;
	Section section46;
	Section section56;
	
	Section section51;
	Section section31;
	Section section63;

	Intersection inter1;
	Intersection inter2;
	Intersection inter3;
	Intersection inter4;
	Intersection inter5;
	Intersection inter6;
	Intersection interIsole;
	
	@Before
	public void InitiliseClassAlgo() {
		CityMap map =new CityMap();
		inter1 = new Intersection (41, 51.51, 1);
		inter2 = new Intersection (42, 52.51, 2);
		inter3 = new Intersection (43, 53.51, 3);
		inter4 = new Intersection (44, 54.51, 4);
		inter5 = new Intersection (44, 54.51, 5);
		inter6 = new Intersection (44, 54.51, 6);
		interIsole = new Intersection (44, 57.51, 7);
		map.addIntersection(inter1);
		map.addIntersection(inter2);
		map.addIntersection(inter3);
		map.addIntersection(inter4);
		map.addIntersection(inter5);
		map.addIntersection(inter6);
		map.addIntersection(interIsole);
		section12=new Section (inter1, inter2, "Rue Danton", 1);
		section13=new Section (inter1, inter3, "Rue de l'Abondance" , 2);
		section25=new Section (inter2, inter5, "Rue25",4);
		section35=new Section (inter3, inter5, "Avenue Lacassagne",2);
		section34=new Section (inter3, inter4, "Rue Sainte-Anne de Baraban", 3);
		section46=new Section (inter4, inter6, "Rue46", 3);
		section56=new Section (inter5, inter6, "Rue56", 5);

		section51=new Section (inter5, inter1, "Rue51", 2);
		section31=new Section (inter3, inter1, "Rue31", 1);
		section63=new Section (inter6, inter6, "Rue63", 1);
		
		map.addSection(section12);
		map.addSection(section13);
		map.addSection(section25);
		map.addSection(section35);
		map.addSection(section34);
		map.addSection(section46);
		map.addSection(section13);
		this.map=map;
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void TSPTest() {
		
		
		
		/*if(theorique.size() != dijkstra.size()) {
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
		}*/
		return;
	}
}
