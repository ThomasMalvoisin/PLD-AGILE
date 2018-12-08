package model;

import java.util.ArrayList;
import java.util.Observable;

public class RoundSet extends Observable {
	protected ArrayList<Round> rounds;
	protected double totalLength;
	protected double duration;

	public RoundSet() {
		rounds = new ArrayList<Round>();
	}

	public RoundSet(ArrayList<Round> rounds, double totalLength) {
		super();
		this.rounds = rounds;
		this.totalLength = totalLength;
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
		duration = roundSet.duration;
		totalLength = roundSet.totalLength;
	}

	public void deleteDelivery(CityMap map, Delivery d) {
		for (Round r : rounds) {
			if (r.getDeliveries().contains(d)) {
				r.deleteDelivery(map, d);
				setChanged();
				notifyObservers();
			}
		}
	}

	public void addDelivery(CityMap map, Delivery d, Delivery deliveryBefore) {
		for (Round r : rounds) {
			if (r.getDeliveries().contains(deliveryBefore)) {
				r.addDelivery(map, d, deliveryBefore);
				setChanged();
				notifyObservers();
			}
		}
	}

	public Delivery getPreviousDelivery(Delivery d) {
		Delivery deliveryBefore;
		for (Round r : rounds) {
			if (r.getDeliveries().contains(d)) {
				int i = r.getDeliveries().indexOf(d);
				deliveryBefore = r.getDeliveries().get(i - 1);
				return deliveryBefore;
			}
		}
		return null;
	}

	public void calculTime(DeliveryRequest dr) {
		for (Round r : rounds) {
			r.setDepartureTime(dr.getStartTime());
			r.calculTime();
		}

		// TODO modifier duration (min des durations des rounds)

	}
}
