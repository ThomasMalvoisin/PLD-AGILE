package model;

import java.util.ArrayList;
import java.util.Observable;

public class RoundSet extends Observable{
	protected int deliveryManNb;
	protected ArrayList<Round> rounds;
	protected double totalLength;
	protected double duration;
	
	
	public RoundSet() {
		rounds = new ArrayList<Round>();
	}
	public RoundSet(int deliveryManNb, ArrayList<Round> rounds, double totalLength) {
		super();
		this.deliveryManNb = deliveryManNb;
		this.rounds = rounds;
		this.totalLength = totalLength;
	}

	public int getDeliveryManNb() {
		return deliveryManNb;
	}
	
	public void setDeliveryManNb(int deliveryManNb) {
		this.deliveryManNb = deliveryManNb;
	}
	
	public ArrayList<Round> getRounds() {
		return rounds;
	}
	
	public void setRounds(ArrayList<Round> rounds) {
		this.rounds = rounds;
	}

	public double getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(double totalLength) {
		this.totalLength = totalLength;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public void copy(RoundSet roundSet) {
		rounds = new ArrayList<Round>(roundSet.rounds);
		deliveryManNb = roundSet.deliveryManNb;
		duration = roundSet.duration;
		totalLength = roundSet.totalLength;
	}
	
	public void deleteDelivery(Delivery d) {
		int i=0;
		for(Round r : rounds) {
			if(r.getDeliveries().contains(d)) {
				r.deleteDelivery(d);
				setChanged();
				notifyObservers();
			}
		}	
	}
}
