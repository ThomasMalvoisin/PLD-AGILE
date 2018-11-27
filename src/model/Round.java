package model;

import java.util.ArrayList;
import java.util.List;

public class Round {
	protected double duration;
	protected double totalLength;
	protected double departureTime;
	protected double arrivalTime;
	protected ArrayList<Delivery> deliveries;
	protected List<Journey> journeys;
	
	public Round(int duration, int departureTime, int arrivalTime, ArrayList<Delivery> deliveries,
			List<Journey> journeys) {
		super();
		this.duration = duration;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.deliveries = deliveries;
		this.journeys = journeys;
	}
	
	public Round() {
		deliveries = new ArrayList<Delivery>();
	}
	
	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public double getDepartureTime() {
		return departureTime;
	}
	
	public void setDepartureTime(double departureTime) {
		this.departureTime = departureTime;
	}
	
	public double getArrivalTime() {
		return arrivalTime;
	}
	
	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public ArrayList<Delivery> getDeliveries() {
		return deliveries;
	}
	
	public void setDeliveries(ArrayList<Delivery> deliveries) {
		this.deliveries = new ArrayList<Delivery>(deliveries);
	}
	
	public List<Journey> getJourneys() {
		return journeys;
	}
	
	public void setJourneys(List<Journey> journeys) {
		this.journeys = journeys;
	}

	public double getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(double totalLength) {
		this.totalLength = totalLength;
	}
	
	
}
