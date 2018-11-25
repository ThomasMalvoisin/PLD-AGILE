package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class DeliveryRequest {

	private Date startTime;
	private ArrayList<Delivery>  requestDeliveries;
	private Intersection warehouse;
	
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
	
	public void addDelivery(Delivery delivery) {
		requestDeliveries.add(delivery);
	}

}
