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
	public void doCde() throws ExceptionAlgo {
		rs.addDelivery(map, d, deliveryBefore);
		dr.addDelivery(d);
		
		
	}
	
	@Override
	public void undoCde() {
		
		try {
			rs.deleteDelivery(map, d);
			dr.deleteDelivery(d);
		} catch (ExceptionAlgo e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
}
