package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.List;

public class CityMap{

	private Map<Long,Intersection> intersections;
	private Map<Intersection,List<Section>> cityMapSections;

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
		} else {
			
			intersections.put(i.getId(), i);
			cityMapSections.put(i, new LinkedList<Section>());
			return true;
		}		
	}

	public boolean addSection(Section s) {
		
		if(cityMapSections.containsKey(s.getOrigin())) {
			
			if(!cityMapSections.get(s.getOrigin()).contains(s)) {
				cityMapSections.get(s.getOrigin()).add(s);
			} else {
				return false;
			}
		} else {
			
			List<Section> listSection = new LinkedList<Section>();
			listSection.add(s);
			cityMapSections.put(s.getOrigin(), listSection);	
		}

		return true;
	}

	public Map<Long, Intersection> getIntersections() {
		return intersections;
	}

	public Map<Intersection, List<Section>> getCityMapSections() {
		return cityMapSections;
	}
	
	public Collection<List<Section>> getSections(){
		
		Collection<List<Section>> listSections = cityMapSections.values();
		
		return listSections;
	}
	
	public void reset() {
		intersections.clear();
		cityMapSections.clear();
	}
}
