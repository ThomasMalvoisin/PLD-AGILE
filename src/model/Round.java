package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	
	public void deleteDelivery(CityMap map,Delivery d) {
		
		Intersection newStart = null;
		Intersection newEnd = null ;
		int insertionIndex  = -1;
		for(int i = 0 ; i < journeys.size() ; i++) {
			if(journeys.get(i).getDestination().equals(d.getAdress())) {
				newStart = journeys.get(i).getOrigin();
				insertionIndex = i;
				journeys.remove(i); 
				break;
			}
		}	
		
		for(int i = 0 ; i < journeys.size() ; i++) {
			if(journeys.get(i).getOrigin().equals(d.getAdress())) {
				newEnd = journeys.get(i).getDestination();
				journeys.remove(i);
				break;
			}
		}
		if(newStart != null && newEnd != null && insertionIndex!= -1) {
			ArrayList<Intersection> ends = new ArrayList<Intersection>();
			ends.add(newEnd);
			ArrayList<Journey> tempJourneys =  map.dijkstraOneToN(newStart, ends).values().stream()
					.collect(Collectors.toCollection(ArrayList::new));
			if(tempJourneys.size() == 1) {
				Journey newJourney = tempJourneys.get(0);
				journeys.add(insertionIndex, newJourney);
				deliveries.remove(d);
			}
			
		}
		
	}
	
	public void addDelivery(CityMap map,Delivery d, Delivery deliveryBefore){
		int deliveryBeforeIndex = deliveries.indexOf(deliveryBefore);
		int journeyBetweenIndex = -1;
		Intersection intersectionAfter = null ;
		for(int i = 0 ; i < journeys.size() ; i++) {
			// && journeys.get(i).getDestination().equals(deliveries.get(deliveryBeforeIndex+1).getAdress())
			if(journeys.get(i).getOrigin().equals(deliveryBefore.getAdress()) ) {
				journeyBetweenIndex = i;
				intersectionAfter = journeys.get(i).getDestination();
				journeys.remove(i);
			}
		}
		if(journeyBetweenIndex != -1) {
			ArrayList<Intersection> ends = new ArrayList<Intersection>();
			ends.add(d.getAdress());
			ArrayList<Journey> tempJourneys =  map.dijkstraOneToN(deliveryBefore.getAdress(), ends).values().stream()
					.collect(Collectors.toCollection(ArrayList::new));
			if(tempJourneys.size() == 1) {
				Journey newJourney = tempJourneys.get(0);
				journeys.add(journeyBetweenIndex, newJourney);
			}
			ends = new ArrayList<Intersection>();
			ends.add(intersectionAfter);
			tempJourneys =  map.dijkstraOneToN(d.getAdress(), ends).values().stream()
					.collect(Collectors.toCollection(ArrayList::new));
			if(tempJourneys.size() == 1) {
				Journey newJourney = tempJourneys.get(0);
				journeys.add(journeyBetweenIndex, newJourney);
			}
		}
		deliveries.add(deliveryBeforeIndex + 1, d);
	}
	
	
}
