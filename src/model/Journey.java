package model;

import java.util.ArrayList;
import java.util.List;

public class Journey {
	protected Intersection origin;
	protected Intersection destination;
	protected List<Section> sectionList;
	protected double length;
	
	public Journey() {}
	
	public Journey(Intersection origin, Intersection destination, List<Section> sectionList, double length) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.sectionList = sectionList;
		this.length = length;
	}
	
	public Intersection getOrigin() {
		return origin;
	}
	public void setOrigin(Intersection origin) {
		this.origin = origin;
	}
	public Intersection getDestination() {
		return destination;
	}
	public void setDestination(Intersection destination) {
		this.destination = destination;
	}
	public List<Section> getSectionList() {
		return sectionList;
	}
	public void setSectionList(List<Section> sectionList) {
		this.sectionList = sectionList;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	
	
}
