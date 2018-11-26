package algo;

import java.util.ArrayList;
import java.util.Comparator;
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
import model.Section;

public class Algorithms {
	protected CityMap map;
	protected CityMap reducedMap;
	
	public Algorithms() {}
	
	public Algorithms(CityMap map) {
		this.map = map;
	}
	
	/*public Map<Long, List<Section>> dijkstraDeliveryRequest (DeliveryRequest request) {
		
		Map<Long, List<Section>> result = new HashMap<Long, List<Section>>();
		ArrayList<Intersection> intersectionList = new ArrayList<Intersection>();
		
		for (Delivery d : request.getRequestDeliveries()) {
			intersectionList.add(d.getAdress());
		}
		intersectionList.add(request.getWarehouse());
		
		for (Intersection i : intersectionList) {
			result.put(i.getId(), new LinkedList<Section>());
		}
		
		return null;
	}*/
	
	public Map<Long, Journey> dijkstraOneToN (Intersection start, ArrayList<Intersection> ends) {
		// Intersections car le warehouse n'est pas une delivery
		// Warehouse DOIT etre dans ends
		
		Map<Long, Journey> result = new HashMap<Long, Journey>();
		Map<Long, Boolean> reached = new HashMap<Long, Boolean>();
		PriorityQueue<Long> pQueue = new PriorityQueue<Long>(1, new Comparator<Long>() {
		    public int compare(Long i1, Long i2) {
		    	Double comp = result.get(i1).getLength() - result.get(i2).getLength();
		    	if (comp < 0) return -1;
		    	else if (comp == 0) return 0;
		    	else return 1;
		    }
		});
		ArrayList<Intersection> unreachedTargetPoints = new ArrayList<Intersection>(ends);
		
		for (Intersection i : map.getIntersections().values()) {
			reached.put(i.getId(), false);
			Journey newJourney = new Journey(start, i, new ArrayList<Section>(), Double.MAX_VALUE);
			result.put(i.getId(), newJourney);
		}

		Long startId = start.getId();
		reached.remove(startId);
		reached.put(startId, true);
		result.get(startId).setLength(0.0);
		pQueue.add(startId);
		
		while (!unreachedTargetPoints.isEmpty()) {
			// On part du principe qu'aucun point n'est pas relié à d'autres, et donc qu'on finira 
			// toujours par trouver les plus courts chemins vers les intersections ends avant de parcourir
			// toute une composante connexe
			Long currStartId = pQueue.poll();
			Intersection currentIntersection = map.getIntersectionById(currStartId);
			unreachedTargetPoints.remove(currentIntersection);
			
			for (Section s : map.getCityMapSections().get(currentIntersection)) {
				
				Intersection arrival = s.getDestination();
				Long arrivalId = arrival.getId();
				Double cost = s.getLength();
				
				if (result.get(currStartId).getLength() + cost < result.get(arrivalId).getLength()) {
					
					result.get(arrivalId).setLength(result.get(currStartId).getLength() + cost);
					
					List<Section> newWay = new ArrayList<Section>(result.get(currStartId).getSectionList());
					newWay.add(s);
					result.get(arrivalId).setSectionList(newWay);
					
					pQueue.remove(arrivalId);
					if (!reached.get(arrivalId))
						pQueue.add(arrivalId);
				}
				
				if (!reached.get(arrivalId)) {
					reached.remove(arrivalId);
					reached.put(arrivalId, true);
				}
			}
		}
		return result;
	}
	
	public CityMap getMap() {
		return map;
	}
	
	public void setMap(CityMap map) {
		this.map = map;
	}
	
	public CityMap getReducedMap() {
		return reducedMap;
	}
}
