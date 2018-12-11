package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Observable;

public class DeliveryRequest extends Observable{

	private Date startTime;
	private ArrayList<Delivery>  requestDeliveries;
	private Intersection warehouse;
	
	/**
	 * Constructor of an empty DeliveryRequest.
	 */
	public DeliveryRequest() {
		requestDeliveries = new ArrayList<>();
	}
	
	/**
	 * @param startTime
	 * @param requestDeliveries
	 * @param warehouse
	 */
	public DeliveryRequest(Date startTime, ArrayList<Delivery> requestDeliveries, Intersection warehouse) {
		super();
		this.startTime= startTime;
		this.requestDeliveries=requestDeliveries;
		this.warehouse=warehouse ;
	}

	/**
	 * @return startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return requestDeliveries
	 */
	public ArrayList<Delivery>  getRequestDeliveries() {
		return requestDeliveries;
	}

	/**
	 * @return warehouse
	 */
	public Intersection getWarehouse() {
		return warehouse;
	}
	
	/**
	 * @param warehouse
	 */
	public void setWarehouse(Intersection warehouse) {
		this.warehouse = warehouse;
	}
	
	/**
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * @return the number of deliveries
	 */
	public int getNbDeliveries() {
		return requestDeliveries.size();
	}
	
	/**
	 * Adds a delivery to the DeliveryRequest.
	 * @param delivery
	 * 		The delivery to add
	 */
	public void addDelivery(Delivery delivery) {
		requestDeliveries.add(delivery);
		setChanged();
		notifyObservers();	
	}
	
	/**
	 * Reset all attributes of a DeliveryRequest.
	 */
	public void reset() {
		requestDeliveries.clear();
		warehouse = null ;
		startTime = null ;
	}
	
	/**
	 * Copy the DeliveryRequest (shallow copy).
	 * @param deliveryRequest
	 */
	public void copy(DeliveryRequest deliveryRequest) {
		requestDeliveries = new ArrayList<Delivery>(deliveryRequest.requestDeliveries);
		startTime = new Date(deliveryRequest.startTime.getTime());
		warehouse = new Intersection(deliveryRequest.getWarehouse().getLatitude(),
				deliveryRequest.getWarehouse().getLongitude() ,
				deliveryRequest.getWarehouse().getId());
	}
	
	/**
	 * Deletes a delivery from the DeliveryRequest.
	 * @param d
	 * 		The delivery to delete
	 */
	public void deleteDelivery(Delivery d) {
		requestDeliveries.remove(d);
		setChanged();
		notifyObservers(d);
	}
}
