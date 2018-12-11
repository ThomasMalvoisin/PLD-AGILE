package controller;

import algo.ExceptionAlgo;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;

public class ComMove implements Command {

	CityMap cityMap;
	RoundSet roundSet;
	Delivery delivery;
	Delivery newBefore;
	Delivery oldBefore;
	
	public ComMove(CityMap cityMap, RoundSet roundSet, Delivery delivery, Delivery newBefore, Delivery oldBefore) {
		super();
		this.cityMap = cityMap;
		this.roundSet = roundSet;
		this.delivery = delivery;
		this.newBefore = newBefore;
		this.oldBefore = oldBefore;
	}

	@Override
	public void doCde() throws ExceptionAlgo {
			roundSet.deleteDelivery(cityMap, delivery);
			roundSet.addDelivery(cityMap, delivery, newBefore);
			
	}

	@Override
	public void undoCde() {
		try {
			roundSet.deleteDelivery(cityMap, delivery);
			roundSet.addDelivery(cityMap, delivery, oldBefore);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
