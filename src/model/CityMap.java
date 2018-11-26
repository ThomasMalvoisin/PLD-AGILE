package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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

	public Collection<List<Section>> getSections(){
		
		Collection<List<Section>> listSections = cityMapSections.values();
		
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
	
}
