package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import algo.Algorithms;

import java.util.Map.Entry;
import java.util.Iterator;

import java.util.List;

public class CityMap{

	private Map<Long,Intersection> intersections;
	private Map<Intersection,List<Section>> cityMapSections;
	private double latitudeMin = Double.MAX_VALUE;
	private double longitudeMin = Double.MAX_VALUE;
	private double latitudeMax = Double.MIN_VALUE;
	private double longitudeMax = Double.MIN_VALUE;

	public double getLatitudeMin() {
		return latitudeMin;
	}

	public double getLongitudeMin() {
		return longitudeMin;
	}

	public double getLatitudeMax() {
		return latitudeMax;
	}

	public double getLongitudeMax() {
		return longitudeMax;
	}

	public CityMap() {
		intersections = new HashMap<Long,Intersection>();
		cityMapSections = new HashMap<Intersection,List<Section>>();
	}
	
	public Intersection getIntersectionById(long id) {
		
		return intersections.get(id);
	}
	
	public boolean addIntersection(Intersection i) {
		
		if(intersections.containsKey(i.getId())) {
			return false;
		}else {
			intersections.put(i.getId(), i);
			cityMapSections.put(i,  new LinkedList<Section>());
			if(i.getLatitude() > latitudeMax) {
				latitudeMax = i.getLatitude();
			}
			if(i.getLatitude() < latitudeMin) {
				latitudeMin = i.getLatitude();
			}
			if(i.getLongitude() > longitudeMax) {
				longitudeMax = i.getLongitude();
			}
			if(i.getLongitude() < longitudeMin) {
				longitudeMin = i.getLongitude();
			}
			return true;
		}	
	}
	
	public boolean addSection(Section s) {
		
		if(cityMapSections.containsKey(s.getOrigin())) {
			
			if(!cityMapSections.get(s.getOrigin()).contains(s)) {
				cityMapSections.get(s.getOrigin()).add(s);
			}else {
				return false;
			}

		}else {
			List<Section> listSection = new LinkedList<Section>();
			listSection.add(s);
			cityMapSections.put(s.getOrigin(), listSection);	
		}

		return true;
	}

	public ArrayList<Section> getSections(){
		
		ArrayList<Section> listSections = cityMapSections.values().stream()
				.flatMap(Collection::stream)
				.distinct()
				.collect(Collectors.toCollection(ArrayList::new));
		
		return listSections;
	}
	
	public void reset() {
		//reset size 
		latitudeMin = Double.MAX_VALUE;
		longitudeMin = Double.MAX_VALUE;
		latitudeMax = Double.MIN_VALUE;
		longitudeMax = Double.MIN_VALUE;

		intersections.clear();
		cityMapSections.clear();
	}

	public Map<Long, Intersection> getIntersections() {
		return intersections;
	}

	public Map<Intersection, List<Section>> getCityMapSections() {
		return cityMapSections;
	}

	@Override
	public boolean equals(Object obj) {
		
		CityMap cityMap = (CityMap)obj;
		if (intersections.size() != cityMap.intersections.size() ||  cityMap.cityMapSections.size() != cityMapSections.size())
		{
			return false;
		}
		
		Set keysIntersections = intersections.keySet();
		Iterator it_inter = keysIntersections.iterator();
		while (it_inter.hasNext()){
		   Object key = it_inter.next(); 
		   if(!intersections.get(key).equals(cityMap.intersections.get(key))) {
			   return false;
		   }
		}
		
		Set keysSections = cityMapSections.keySet();
		Iterator it_sec = keysSections.iterator();
		while (it_sec.hasNext()){
		   Object key = it_sec.next(); 
		   if(cityMapSections.get(key).size() != cityMap.cityMapSections.get(key).size()) {
			   return false;
		   }
		   List<Section> list_sec = cityMapSections.get(key);
		   for(Section s : list_sec) {
			   if(cityMap.cityMapSections.get(key).indexOf(s)==-1) {
				   return false;
			   }
		   }
		}
		
		
		return true;
	}
	
	public void copy(CityMap map) {
		intersections = new HashMap<Long,Intersection>(map.intersections);
		cityMapSections = new HashMap<Intersection,List<Section>>(map.cityMapSections);
		latitudeMin = map.latitudeMin;
		longitudeMin = map.longitudeMin;
		latitudeMax = map.latitudeMax;
		longitudeMax = map.longitudeMax;
	}
	
	public Map<Long, Intersection> getNotDeliveriesIntersections(DeliveryRequest dr)
	{
		Map<Long, Intersection> result = new HashMap<Long,Intersection>();
		boolean found;
		for (Entry<Long, Intersection> entry : intersections.entrySet()) {
			found = false;
			if (entry.getValue().equals(dr.getWarehouse())){
				found = true;
			}
			for (Delivery d : dr.getRequestDeliveries())
			{
				if (entry.getValue().equals(d.getAdress()))
				{
					found = true;
					break;
				}
			}
			if(!found)
			{
				result.put(entry.getValue().getId(), entry.getValue());
			}
		}
		return result;
	}
	
	public Map<Long, Map<Long, Journey>> GetShortestJourneys (DeliveryRequest request) {
			//Map<origin,Map<destination,path>>
		  	Map<Long, Map<Long, Journey>> reducedMap = new HashMap<Long, Map<Long, Journey>>();
			ArrayList<Intersection> intersectionList = new ArrayList<Intersection>();
			for (Delivery d : request.getRequestDeliveries()) {
				intersectionList.add(d.getAdress());
			}
			intersectionList.add(request.getWarehouse());
	
			for (Intersection i : intersectionList) {
				//System.out.println(i.getId());
				reducedMap.put(i.getId(), dijkstraOneToN(i, intersectionList));
			}
			return reducedMap;
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
		
		for (Intersection i : getIntersections().values()) {
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

			//System.out.println(currStartId);
			Intersection currentIntersection = getIntersectionById(currStartId);
			//System.out.println(currentIntersection);
			unreachedTargetPoints.remove(currentIntersection);
			
			for (Section s : getCityMapSections().get(currentIntersection)) {
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
	
	public ArrayList<String> getIntersectionSectionNames(Intersection intersection){
		return cityMapSections.values().stream()
			.flatMap(Collection::stream)
			.filter(section -> section.getOrigin().equals(intersection) || section.getDestination().equals(intersection))
			.map(section -> section.getName())
			.distinct( )
			.collect(Collectors.toCollection(ArrayList::new));
	}
	
}