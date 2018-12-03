package model;
import java.util.Date;

//TODO : devra être observable
public class Delivery {
	
	public static int currentId = 1;

	private int duration;
	private Date arrivalTime;
	private Date departureTime;
	private Intersection adress; 
	private int id;
	

	public Delivery(int duration, Date arrivalTime, Date departureTime, Intersection adress) {
		super();
		this.duration = duration;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.adress = adress;
	}
	
	public Delivery(int duration, Intersection adress) {
		super();
		this.duration = duration;
		this.adress = adress;
		this.id = currentId++;
	}

	public int getDuration() {
		return duration;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public Date getDepartureTime() {
		return departureTime;
	}
	
	public Intersection getAdress() {
		return adress;
	}
	
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public int getId() {
		return id;
	}
	
	public boolean equals(Delivery d) {
		if(d != null) {
			return this.id == d.getId();
		}
		
		return false;
	}
	
}
