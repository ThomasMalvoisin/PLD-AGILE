package model;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Delivery {
	
	public static int currentId = 1;

	private int duration;
	private Date arrivalTime;
	private Date departureTime;
	private Intersection adress; 
	private int id;
	

	/**
	 * @param duration
	 * @param arrivalTime
	 * @param departureTime
	 * @param adress
	 */
	public Delivery(int duration, Date arrivalTime, Date departureTime, Intersection adress) {
		super();
		this.duration = duration;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.adress = adress;
	}
	
	/**
	 * Creates an identified delivery without the same id than another identified delivery
	 * @param duration
	 * @param adress
	 */
	public Delivery(int duration, Intersection adress) {
		super();
		this.duration = duration;
		this.adress = adress;
		this.id = currentId++;
	}
	
	/**
	 * Creates an unidentified delivery with just a particular adress
	 * @param adress
	 */
	public Delivery(Intersection adress) {
		super();
		this.duration = 0;
		this.adress = adress;
		this.id = 0;
	}
	
	/**
	 * @return duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return arrivalTime
	 */
	public Date getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @return departureTime
	 */
	public Date getDepartureTime() {
		return departureTime;
	}
	
	/**
	 * @return adress
	 */
	public Intersection getAdress() {
		return adress;
	}
	
	/**
	 * @param arrivalTime
	 */
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	/**
	 * @param departureTime
	 */
	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Equality test to a given delivery.
	 * @param d
	 * 		The delivery to test
	 * @return true if both deliveries have the same id, false otherwise
	 */
	public boolean equals(Delivery d) {
		if(d != null) {
			return this.id == d.getId();
		}
		
		return false;
	}

	/**
	 * Creates a string describing the delivery.
	 * @param isFirst
	 * 		Define if the delivery is a warehouse or not (true if it is)
	 * @return a string describing the delivery
	 */
	public String toString (boolean isFirst) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		String a = "";
		if (isFirst) {
			a+="Warehouse at ";
		} else {
			a+="Delivery "+id+".\n";
			a+="Duration : "+duration/60.0+" minutes. \n";
			a+="Arrival time : "+sdfDate.format(arrivalTime)+"\n";
			a+="Departure time : "+sdfDate.format(departureTime)+"\n";
		}

		a+=adress.toString();
		return a;
	}
}
