package controller;

import controller.Command;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;

public class ComAdd implements Command {
	CityMap map;
	DeliveryRequest dr;
	RoundSet rs;
	Delivery d;
	Delivery deliveryBefore;
	
	public ComAdd(CityMap map, DeliveryRequest dr, RoundSet rs, Delivery d, Delivery deliveryBefore) {
		this.map=map;
		this.dr=dr;
		this.rs=rs;
		this.d=d;	
		this.deliveryBefore=deliveryBefore;
	}
	
	
	
	@Override
	public void doCde() {
		dr.addDelivery(d);
		rs.addDelivery(map, d, deliveryBefore);
	}
	
	@Override
	public void undoCde() {
		dr.deleteDelivery(d);
		rs.deleteDelivery(map, d);
	}
	
}
