package controller;

import algo.ExceptionAlgo;
import model.CityMap;
import model.Delivery;
import model.RoundSet;

public class ComMove implements Command {
	
	CityMap cityMap;
	RoundSet roundSet;
	Delivery delivery;
	Delivery newBefore;
	Delivery oldBefore;
	
	/**
	 * Create command which move a delivery from one round to another
	 * @param cityMap current cityMap
	 * @param roundSet the roundSet to modify
	 * @param delivery the delivery to move
	 * @param newBefore the delivery before d before adding
	 * @param oldBefore the delivery before d after adding
	 */
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
