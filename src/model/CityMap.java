package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class CityMap {

	private Map<Long,Intersection> intersections;
	private Map<Intersection,List<Section>> sections;

	public CityMap() {
		
		intersections = new HashMap<Long,Intersection>();
		sections = new HashMap<Intersection,List<Section>>();
	}
	
	public Intersection getIntersectionById(long id) {
		
		return intersections.get(id);
	}
	
	public boolean addIntersection(Intersection i) {
		
		if(intersections.containsKey(i.getId())) {
			
			return false;
		}else {
			intersections.put(i.getId(), i);
			return true;
		}		
	}
	
	public boolean addSection(Section s) {
		
		if(sections.containsKey(s.getOrigin())) {
			
			if(!sections.get(s.getOrigin()).contains(s)) {
				sections.get(s.getOrigin()).add(s);
			}else {
				return false;
			}

		}else {
			List<Section> listSection = new LinkedList<Section>();
			listSection.add(s);
			sections.put(s.getOrigin(), listSection);		
		}

		if(sections.containsKey(s.getDestination())) {

			if(!sections.get(s.getDestination()).contains(s)) {
				sections.get(s.getDestination()).add(s);
			}else {
				return false;
			}

		}else {
			List<Section> listSection = new LinkedList<Section>();
			listSection.add(s);
			sections.put(s.getDestination(), listSection);		
		}
		return true;
	}

	public Collection<List<Section>> getSections(){
		
		Collection<List<Section>> listSections = sections.values();
		
		return listSections;
	}
	
}
