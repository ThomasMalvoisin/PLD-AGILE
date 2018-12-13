package model;

import java.util.List;

public class Journey {
	protected Intersection origin;
	protected Intersection destination;
	protected List<Section> sectionList;
	protected double length;
	
	/**
	 * Create an empty journey
	 */
	public Journey() {}
	
	/**
	 * Create a journey with a selected origin and destination, a list of sections and a specified length
	 * @param origin
	 * @param destination
	 * @param sectionList
	 * @param length
	 */
	public Journey(Intersection origin, Intersection destination, List<Section> sectionList, double length) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.sectionList = sectionList;
		this.length = length;
	}
	
	/**
	 * @return
	 */
	public Intersection getOrigin() {
		return origin;
	}
	/**
	 * @param origin
	 */
	public void setOrigin(Intersection origin) {
		this.origin = origin;
	}
	/**
	 * @return
	 */
	public Intersection getDestination() {
		return destination;
	}
	/**
	 * @param destination
	 */
	public void setDestination(Intersection destination) {
		this.destination = destination;
	}
	/**
	 * @return
	 */
	public List<Section> getSectionList() {
		return sectionList;
	}
	/**
	 * @param sectionList
	 */
	public void setSectionList(List<Section> sectionList) {
		this.sectionList = sectionList;
	}
	/**
	 * @return
	 */
	public double getLength() {
		return length;
	}
	/**
	 * @param length
	 */
	public void setLength(double length) {
		this.length = length;
	}
	
	/**
	 * Print the journey's text description according to the given number 
	 * @param number
	 * @return
	 */
	public String toString(int number) {
		String a = "Lenght of journey "+number+" : "+ Math.round(length)/1000.0 +" kilometer(s).\n \n";
		for (Section s : sectionList) {
			a+=s.toString();
		}
		return a;
	}
	
}
