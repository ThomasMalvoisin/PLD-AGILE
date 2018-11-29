package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Observable;

public class DeliveryRequest{

	private Date startTime;
	private ArrayList<Delivery>  requestDeliveries;
	private Intersection warehouse;
	
	public DeliveryRequest() {
		requestDeliveries = new ArrayList<>();
	}
	
	public DeliveryRequest(Date startTime, ArrayList<Delivery> requestDeliveries, Intersection warehouse) {
		super();
		this.startTime= startTime;
		this.requestDeliveries=requestDeliveries;
		this.warehouse=warehouse ;
	}

	public Date getStartTime() {
		return startTime;
	}

	public ArrayList<Delivery>  getRequestDeliveries() {
		return requestDeliveries;
	}

	public Intersection getWarehouse() {
		return warehouse;
	}
	
	public void setWarehouse(Intersection warehouse) {
		this.warehouse = warehouse;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public void addDelivery(Delivery delivery) {
		requestDeliveries.add(delivery);
	}
	
	public void reset() {
		requestDeliveries.clear();
		warehouse = null ;
		startTime = null ;
	}
	
	public void copy(DeliveryRequest deliveryRequest) {
		requestDeliveries = new ArrayList<Delivery>(deliveryRequest.requestDeliveries);
		startTime = new Date(deliveryRequest.startTime.getTime());
		warehouse = new Intersection(deliveryRequest.getWarehouse().getLatitude(),
				deliveryRequest.getWarehouse().getLongitude() ,
				deliveryRequest.getWarehouse().getId());
	}
}
