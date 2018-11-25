package algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Section;

public class Algorithms {
	protected CityMap map;
	protected CityMap reducedMap;
	protected DeliveryRequest request;
	
	public Algorithms() {}
	
	public Algorithms(CityMap map, DeliveryRequest request) {
		super();
		this.map = map;
		this.request = request;
	}
	
	public Map<Long, List<Section>> dijkstraOneToN (Intersection start, ArrayList<Intersection> ends) {
		// Intersections car le warehouse n'est pas une delivery
		// Warehous DOIT etre dans ends
		Map<Long, List<Section>> result = new HashMap<Long, List<Section>>();
		Map<Long, Boolean> reached = new HashMap<Long, Boolean>();
		Map<Long, Double> currentCosts = new HashMap<Long, Double>();
		PriorityQueue<Long> pQueue = new PriorityQueue<Long>(1, new Comparator<Long>() {
		    public int compare(Long i1, Long i2) {
		    	Double comp = currentCosts.get(i1) - currentCosts.get(i2);
		    	if (comp < 0) return -1;
		    	else if (comp == 0) return 0;
		    	else return 1;
		    }
		});
		ArrayList<Intersection> unreachedTargetPoints = new ArrayList<Intersection>(ends);
		
		for (Intersection i : map.getIntersections().values()) {
			reached.put(i.getId(), false);
			currentCosts.put(i.getId(), Double.MAX_VALUE);
			result.put(i.getId(), new ArrayList<Section>());
		}

		Long startId = start.getId();
		reached.remove(startId);
		reached.put(startId, true);
		currentCosts.remove(startId);
		currentCosts.put(startId, 0.0);
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
				
				if (currentCosts.get(currStartId) + cost < currentCosts.get(arrivalId)) {
					
					currentCosts.remove(arrivalId);
					currentCosts.put(arrivalId, currentCosts.get(currStartId) + cost);
					
					result.remove(arrivalId);
					List<Section> newWay = new ArrayList<Section>(result.get(currStartId));
					newWay.add(s);
					result.put(arrivalId, newWay);
					
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

	public DeliveryRequest getRequest() {
		return request;
	}
	public void setRequest(DeliveryRequest request) {
		this.request = request;
	}
	
	
}
