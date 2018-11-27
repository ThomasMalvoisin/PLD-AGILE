package algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Journey;
import model.Round;
import model.RoundSet;
import model.Section;

public class Algorithms {
	protected CityMap map;
	protected Map<Long, Map<Long, Journey>> reducedMap;
	protected double bikersSpeed = 0.2399998;	// 15km/h => 4.16667 m/s => 0.2399998 s/m
	
	public Algorithms() {}
	
	public Algorithms(CityMap map) {
		this.map = map;
	}
	
	public void dijkstraDeliveryRequest (DeliveryRequest request) {
		
		this.reducedMap = new HashMap<Long, Map<Long, Journey>>();
		ArrayList<Intersection> intersectionList = new ArrayList<Intersection>();
		
		for (Delivery d : request.getRequestDeliveries()) {
			intersectionList.add(d.getAdress());
		}
		intersectionList.add(request.getWarehouse());
		
		for (Intersection i : intersectionList) {
			System.out.println(i.getId());
			reducedMap.put(i.getId(), dijkstraOneToN(i, intersectionList));
		}
			
	}
	
	public Map<Long, Journey> dijkstraOneToN (Intersection start, ArrayList<Intersection> ends) {
		// Intersections car le warehouse n'est pas une delivery
		// Warehouse DOIT etre dans ends
		
		Map<Long, Journey> shortestJourneys = new HashMap<Long, Journey>();
		Map<Long, Boolean> reached = new HashMap<Long, Boolean>();
		PriorityQueue<Long> pQueue = new PriorityQueue<Long>(1, new Comparator<Long>() {
		    public int compare(Long i1, Long i2) {
		    	Double comp = shortestJourneys.get(i1).getLength() - shortestJourneys.get(i2).getLength();
		    	if (comp < 0) return -1;
		    	else if (comp == 0) return 0;
		    	else return 1;
		    }
		});
		ArrayList<Intersection> unreachedTargetPoints = new ArrayList<Intersection>(ends);
		
		for (Intersection i : map.getIntersections().values()) {
			reached.put(i.getId(), false);
			Journey newJourney = new Journey(start, i, new ArrayList<Section>(), Double.MAX_VALUE);
			shortestJourneys.put(i.getId(), newJourney);
		}

		Long startId = start.getId();
		reached.remove(startId);
		reached.put(startId, true);
		shortestJourneys.get(startId).setLength(0.0);
		pQueue.add(startId);
		
		while (!unreachedTargetPoints.isEmpty()) {
			// On part du principe qu'aucun point n'est pas relié à d'autres, et donc qu'on finira 
			// toujours par trouver les plus courts chemins vers les intersections ends avant de parcourir
			// toute une composante connexe
			Long currStartId = pQueue.poll();
			System.out.println(currStartId);
			Intersection currentIntersection = map.getIntersectionById(currStartId);
			System.out.println(currentIntersection);
			unreachedTargetPoints.remove(currentIntersection);
			
			for (Section s : map.getCityMapSections().get(currentIntersection)) {
				Intersection arrival = s.getDestination();
				Long arrivalId = arrival.getId();
				Double cost = s.getLength();
				
				if (shortestJourneys.get(currStartId).getLength() + cost < shortestJourneys.get(arrivalId).getLength()) {
					
					shortestJourneys.get(arrivalId).setLength(shortestJourneys.get(currStartId).getLength() + cost);
					
					List<Section> newWay = new ArrayList<Section>(shortestJourneys.get(currStartId).getSectionList());
					newWay.add(s);
					shortestJourneys.get(arrivalId).setSectionList(newWay);
					
					pQueue.remove(arrivalId);
					if (!reached.get(arrivalId))
						pQueue.add(arrivalId);
				}
			}
			if (!reached.get(currStartId)) {
				reached.remove(currStartId);
				reached.put(currStartId, true);
			}
		}
		
		Map<Long, Journey> result = new HashMap<Long, Journey>();
		for (Intersection i : ends) 
			result.put(i.getId(), shortestJourneys.get(i.getId()));
		
		return result;
	}
	
	public RoundSet solveTSP (DeliveryRequest request, int nbDeliveryMan) {
		// Pour l'instant, nbDeliveryMan = 1 forcément
		
		ArrayList<Delivery> visited = new ArrayList<Delivery>();
		visited.add(new Delivery(0, new Date(0L), new Date(0L), request.getWarehouse()));
		ArrayList<Delivery> cand = new ArrayList<Delivery> (request.getRequestDeliveries());
		
		Round round = new Round();
		round.setDuration(Double.MAX_VALUE);	// Modifier branchAndBound pour construire un roundSet, et ajouter le numero de Round en cours de remplissage
		
		branchAndBound(visited, cand, 0.0, round, request.getWarehouse());
		
		ArrayList<Round> rounds = new ArrayList<Round>();
		rounds.add(round);
		RoundSet result = new RoundSet (1, rounds, round.getTotalLength());
		
		return result;
	}
	
	public void branchAndBound (ArrayList<Delivery> visited, ArrayList<Delivery> cand, double t, Round bestSolution, Intersection warehouse) {
		if (cand.isEmpty()) {
			t += bikersSpeed * reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(visited.get(0).getAdress().getId()).getLength();
			if (t < bestSolution.getDuration()) {
				bestSolution.setDuration(t);
				bestSolution.setDeliveries(new ArrayList<Delivery>(visited));
				ArrayList<Journey> newWay = new ArrayList<Journey> ();
				for(int i = 0; i < visited.size()-1; i++) {
					newWay.add(reducedMap.get(visited.get(i).getAdress().getId()).get(visited.get(i+1).getAdress().getId()));
				}
				newWay.add(reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(visited.get(0).getAdress().getId()));
				bestSolution.setJourneys(newWay);
			}
		} else if (t + bound(visited.get(visited.size()-1), cand, warehouse) < bestSolution.getDuration()){
			for (int i = 0; i < cand.size(); i++) {
				visited.add(cand.get(i));
				cand.remove(i);
				branchAndBound(visited, cand, t + bikersSpeed * reducedMap.get(visited.get(visited.size()-2).getAdress().getId()).get(visited.get(visited.size()-1).getAdress().getId()).getLength() + visited.get(visited.size()-1).getDuration(), bestSolution, warehouse);
				cand.add(i, visited.get(visited.size()-1));
				visited.remove(visited.size()-1);
			}
		}
	}
	
	public double bound (Delivery currentDelivery, ArrayList<Delivery> cand, Intersection warehouse) {
		System.out.println("Current Delivery : " + currentDelivery.getAdress().getId());
		for (Delivery d : cand) {
			System.out.print(d.getAdress().getId() + " ");
		}
		System.out.println();
		double result = 0.0;
		double minToWarehouse = Double.MAX_VALUE, minToNextCand = Double.MAX_VALUE;
		for (Delivery d : cand) {
			System.out.println(currentDelivery.getAdress().getId());
			System.out.println(d.getAdress().getId());
			double toThisCand = reducedMap.get(currentDelivery.getAdress().getId()).get(d.getAdress().getId()).getLength();
			if (toThisCand < minToNextCand)
				minToNextCand = toThisCand;
			double toWarehouse = reducedMap.get(currentDelivery.getAdress().getId()).get(warehouse.getId()).getLength();
			if (toWarehouse < minToWarehouse)
				minToWarehouse = toWarehouse;
			double minLength = Double.MAX_VALUE;
			for (Delivery e : cand) {
				if (e != d) {
					double currentCost = reducedMap.get(d.getAdress().getId()).get(e.getAdress().getId()).getLength();
					if (currentCost < minLength)
						minLength = currentCost;
				}
			}
			result += bikersSpeed * minLength;
			result += d.getDuration();
		}
		return result + bikersSpeed * (minToWarehouse + minToNextCand);
	}
	
	public CityMap getMap() {
		return map;
	}
	
	public void setMap(CityMap map) {
		this.map = map;
	}

	public Map<Long, Map<Long, Journey>> getReducedMap() {
		return reducedMap;
	}
}
