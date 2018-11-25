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
		//Intersection car le warehouse n'est pas une delivery
		
		Map<Long, List<Section>> result = new HashMap<Long, List<Section>>();
		Map<Long, Boolean> reached = new HashMap<Long, Boolean>();
		Map<Long, Double> currentCosts = new HashMap<Long, Double>();
		PriorityQueue<Intersection> pQueue = new PriorityQueue<Intersection>(0, new Comparator<Intersection>() {
		    public int compare(Intersection i1, Intersection i2) {
		    	Double comp = currentCosts.get(i1.getId()) - currentCosts.get(i2.getId());
		    	if (comp < 0) return -1;
		    	else if (comp == 0) return 0;
		    	else return 1;
		    }
		});
		ArrayList<Intersection> unreachedTargetPoints = new ArrayList<Intersection>(ends);
		
		for (Intersection d : map.getIntersections().values()) {
			reached.put(d.getId(), false);
			currentCosts.put(d.getId(), Double.MAX_VALUE);
			result.put(d.getId(), new ArrayList<Section>());
		}
		
		Long startId = start.getId();
		reached.remove(startId);
		reached.put(startId, true);
		currentCosts.remove(startId);
		currentCosts.put(startId, 0.0);
		result.remove(startId);
		
		while (!unreachedTargetPoints.isEmpty()) {
			// On part du principe qu'aucun point n'est pas relié à d'autres, et donc qu'on finira 
			// toujours par trouver les plus courts chemins vers les intersections ends avant de parcourir
			// toute une composante connexe
			/*Intersection currentIntersection = pQueue.poll();
			for (Section s : map.getCityMapSections().get(currentIntersection)) {
				Intersection arrival = s.getDestination();
				Double cost = s.getLength();
			}*/
		}
		return null;
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
