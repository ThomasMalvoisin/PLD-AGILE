package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
}