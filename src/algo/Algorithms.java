package algo;

import model.CityMap;
import model.DeliveryRequest;

public class Algorithms {
	protected CityMap map;
	protected DeliveryRequest request;
	
	public Algorithms(CityMap map, DeliveryRequest request) {
		super();
		this.map = map;
		this.request = request;
	}
	
	public CityMap getMap() {
		return map;
	}
	public void setMap(CityMap map) {
		this.map = map;
	}
	public DeliveryRequest getRequest() {
		return request;
	}
	public void setRequest(DeliveryRequest request) {
		this.request = request;
	}
	
	
}
