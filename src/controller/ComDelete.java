package controller;

import algo.ExceptionAlgo;
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
		try {
			rs.deleteDelivery(map, d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void undoCde() {
		dr.addDelivery(d);
		try {
			rs.addDelivery(map, d, deliveryBefore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
