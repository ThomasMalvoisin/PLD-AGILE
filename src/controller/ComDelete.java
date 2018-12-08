package controller;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;

public class ComDelete implements Command {
	CityMap map;
	DeliveryRequest dr;
	RoundSet rs;
	Delivery d;
	Delivery deliveryBefore;
	
	public ComDelete(CityMap map, DeliveryRequest dr, RoundSet rs, Delivery d) {
		this.map=map;
		this.dr=dr;
		this.rs=rs;
		this.d=d;
		deliveryBefore= rs.getPreviousDelivery(d);
	}
	
	
	
	@Override
	public void doCde() {
		dr.deleteDelivery(d);
		rs.deleteDelivery(map, d);
	}
	
	@Override
	public void undoCde() {
		dr.addDelivery(d);
		rs.addDelivery(map, d, deliveryBefore);
	}

}
