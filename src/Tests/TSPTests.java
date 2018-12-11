package Tests;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Journey;
import model.Round;
import model.RoundSet;
import model.Section;
import algo.Algorithms;
import algo.ExceptionAlgo;

import java.lang.Long;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
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
	public void InitiliseClassAlgo() throws ParseException {
		map =new CityMap();
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
		section63=new Section (inter6, inter3, "Rue63", 1);
		
		map.addSection(section12);
		map.addSection(section13);
		map.addSection(section25);
		map.addSection(section35);
		map.addSection(section34);
		map.addSection(section46);
		map.addSection(section56);
		map.addSection(section51);
		map.addSection(section31);
		map.addSection(section63);
		
		
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//TSP with 1 deliveryMan and 1 delivery (warehouse : 1 / deliveries : 5)
	@Test
	public void TSPTest() throws ParseException, ExceptionAlgo {
		
		//deliveryRequest
		ArrayList<Delivery> listDeliveries = new ArrayList<>();
		Delivery del = new Delivery(0, inter5);
		listDeliveries.add(del);
		
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    	Date startTime  = df.parse("8:0:0");
    	
    	DeliveryRequest dr=new DeliveryRequest(startTime,listDeliveries,inter1);
    	
    	
		//journeys
		List<Journey> journeys = new ArrayList<>();
		List<Section> sectionList = new ArrayList<>();
		sectionList.add(section13);
		sectionList.add(section35);
		Journey j1 = new Journey(inter1, inter5, sectionList, 4);
		
		List<Section> sectionList2 = new ArrayList<>();
		sectionList2.add(section51);
		Journey j2 = new Journey(inter5, inter1, sectionList2, 2);
		
		journeys.add(j1);
		journeys.add(j2);
		
		//deliveries
		ArrayList<Delivery> deliveries = new ArrayList<>();
		Delivery d1 = new Delivery(0, inter1);
		Delivery d2 = new Delivery(0, inter5);
		deliveries.add(d1);
		deliveries.add(d2);
		
		//rounds
		ArrayList<Round> listeRound = new ArrayList<>();
		Round r1 = new Round();
		r1.setDeliveries(deliveries);
		r1.setJourneys(journeys);
		r1.setTotalLength(6.0);
		r1.setDuration(6*0.2399998);
		listeRound.add(r1);		
		
		
		//roundSet	
		RoundSet roundSetattendu = new RoundSet();	
		roundSetattendu.setRounds(listeRound);
		
		roundSetattendu.setDuration(6*0.2399998);
		
		roundSetattendu.setTotalLength(6.0);
		
		
		RoundSet rs = new RoundSet();
		Algorithms.solveTSP(rs, map,dr, 1);
		
		compare(rs, roundSetattendu);
		
		return;
	}
	
	//TSP with 1 deliveryMan and 2 delivery (warehouse : 1 / deliveries : 5, 6)
		@Test
		public void TSPTest2() throws ParseException, ExceptionAlgo {
			
			//deliveryRequest
			ArrayList<Delivery> listDeliveries = new ArrayList<>();
			Delivery del = new Delivery(0, inter5);
			Delivery del2 = new Delivery(0, inter6);
			listDeliveries.add(del);
			listDeliveries.add(del2);
			
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	    	Date startTime  = df.parse("8:0:0");
	    	
	    	DeliveryRequest dr=new DeliveryRequest(startTime,listDeliveries,inter1);
	    	
	    	
			
			//journeys
			List<Journey> journeys = new ArrayList<>();
			List<Section> sectionList = new ArrayList<>();
			sectionList.add(section13);
			sectionList.add(section35);
			Journey j1 = new Journey(inter1, inter5, sectionList, 4);
			
			List<Section> sectionList2 = new ArrayList<>();
			sectionList2.add(section56);
			Journey j2 = new Journey(inter5, inter6, sectionList2, 5);
			
			List<Section> sectionList3 = new ArrayList<>();
			sectionList3.add(section63);
			sectionList3.add(section31);
			Journey j3 = new Journey(inter6, inter1, sectionList3, 2);
			
			journeys.add(j1);
			journeys.add(j2);
			journeys.add(j3);
			
			//deliveries
			ArrayList<Delivery> deliveries = new ArrayList<>();
			Delivery d1 = new Delivery(0, inter1);
			Delivery d2 = new Delivery(0, inter5);
			Delivery d3 = new Delivery(0, inter6);
			deliveries.add(d1);
			deliveries.add(d2);
			deliveries.add(d3);
			
			//rounds
			ArrayList<Round> listeRound = new ArrayList<>();
			Round r1 = new Round();
			r1.setDeliveries(deliveries);
			r1.setJourneys(journeys);
			r1.setTotalLength(11.0);
			r1.setDuration(11*0.2399998);
			listeRound.add(r1);		
			
			
			//roundSet	
			RoundSet roundSetattendu = new RoundSet();	
			roundSetattendu.setRounds(listeRound);
			
			roundSetattendu.setDuration(11*0.2399998);
			
			roundSetattendu.setTotalLength(6.0);
			
			
			RoundSet rs = new RoundSet();
			Algorithms.solveTSP(rs, map,dr, 1);
			
			compare(rs, roundSetattendu);
			return;
		
	}
	
	private void compare(RoundSet roundSet, RoundSet roundSet2) {
		assert(roundSet.getDuration()==roundSet2.getDuration());
		//assert(roundSet.getTotalLength()==roundSet2.getTotalLength());
		assert(roundSet.getRounds().size()==roundSet2.getRounds().size());
		for(int i=0 ; i<roundSet.getRounds().size() ; i++) {
			//assert(roundSet.getRounds().get(i).getTotalLength()==roundSet2.getRounds().get(i).getTotalLength());
		   //	assert(roundSet.getRounds().get(i).getDuration()==roundSet2.getRounds().get(i).getDuration());
			assert(roundSet.getRounds().get(i).getDeliveries().size() == roundSet2.getRounds().get(i).getDeliveries().size());
			for(int j=0 ; j<roundSet.getRounds().get(i).getDeliveries().size() ; j++) {
				assert(roundSet.getRounds().get(i).getDeliveries().get(j).getAdress().equals(roundSet2.getRounds().get(i).getDeliveries().get(j).getAdress()));
			}
			
			assert(roundSet.getRounds().get(i).getJourneys().size()==roundSet2.getRounds().get(i).getJourneys().size());
			for(int k=0 ; k<roundSet.getRounds().get(i).getJourneys().size() ; k++) {
				
				assert(roundSet.getRounds().get(i).getJourneys().get(k).getOrigin().equals(roundSet2.getRounds().get(i).getJourneys().get(k).getOrigin()));
				
				assert(roundSet.getRounds().get(i).getJourneys().get(k).getDestination().equals(roundSet2.getRounds().get(i).getJourneys().get(k).getDestination()));
				
				assert(roundSet.getRounds().get(i).getJourneys().get(k).getLength() == roundSet2.getRounds().get(i).getJourneys().get(k).getLength());
				
				assert(roundSet.getRounds().get(i).getJourneys().get(k).getSectionList().size() == roundSet2.getRounds().get(i).getJourneys().get(k).getSectionList().size());
				for(int h=0 ; h<roundSet.getRounds().get(i).getJourneys().get(k).getSectionList().size() ; h++) {
					assert(roundSet.getRounds().get(i).getJourneys().get(k).getSectionList().get(h).equals(roundSet2.getRounds().get(i).getJourneys().get(k).getSectionList().get(h)));
				}
			}
		}
	}
}


