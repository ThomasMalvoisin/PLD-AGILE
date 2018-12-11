package controller;

import algo.ExceptionAlgo;
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
		try {
			rs.addDelivery(map, d, deliveryBefore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void undoCde() {
		dr.deleteDelivery(d);
		try {
			rs.deleteDelivery(map, d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
