package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import algo.ExceptionAlgo;

public class RoundSet extends Observable {
	protected ArrayList<Round> rounds;
	protected double totalLength;
	protected double duration;
	protected Date departureTime;

	public RoundSet() {
		rounds = new ArrayList<Round>();
		totalLength=0;
	}

	public RoundSet(ArrayList<Round> rounds, double totalLength) {
		super();
		this.rounds = rounds;
		this.totalLength = totalLength;
		totalLength=0;
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

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public void copy(RoundSet roundSet) {
		rounds = new ArrayList<Round>(roundSet.rounds);
		totalLength = roundSet.totalLength;
		duration=-1;
		this.departureTime = roundSet.getDepartureTime();
		this.calculTime();
		setChanged();
		notifyObservers();
	}

	public void deleteDelivery(CityMap map, Delivery d) {
		for (Round r : rounds) {
			if (r.getDeliveries().contains(d)) {
				try {
					r.deleteDelivery(map, d);
				} catch (ExceptionAlgo e) {
					e.printStackTrace();
				}
				
				calculTime();
				setChanged();
				notifyObservers();
			}
		}
	}

	public void addDelivery(CityMap map, Delivery d, Delivery deliveryBefore) {
		for (Round r : rounds) {
			if (r.getDeliveries().contains(deliveryBefore)) {
				try {
					r.addDelivery(map, d, deliveryBefore);
				} catch (ExceptionAlgo e) {
					e.printStackTrace();
				}
				
				calculTime();
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

	public void calculTime() {
		for (Round r : rounds) {
			r.setDepartureTime(this.getDepartureTime());
			r.calculTime();
		}
		
		//this.duration = rounds.get(0).getDuration();
		
		for(Round r : rounds) {
			this.totalLength += r.getTotalLength();
			if(duration==-1 || r.getDuration() > this.duration) {
				this.duration = r.getDuration();
			}
		}
	}
	
	public String toString () {
		String a  ="***** General information about Round Set ***** \n"; 
		a+="\n";
		a+="Number of Delivery Man : "+rounds.size()+" \n";
		a+="Total length : "+Math.round(totalLength)/1000.0+" kilometer(s) \n";
		a+="Maximum duration: "+Math.round(duration/60000)+" minutes \n";
		a+="\n";
		int numeroLivreur =0;
		for(Round r : rounds) {
			numeroLivreur++;
			a+="***** Roadmap of delivery man "+numeroLivreur+" ******\n";
			a+= r.toString();
			a+="***** End of the roadmap of delivery man *****"+numeroLivreur+" \n";
			a+="\n";
			}
		return a;
	}
	
	public void reset() {
		rounds.clear();
		totalLength=0;
		duration=0;
		departureTime=null;
	}
}

