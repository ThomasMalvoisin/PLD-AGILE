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
	
	/** Create command which delete a delivery from delivery request and roundSet
	 * @param map the current city map
	 * @param dr the delivery request to modify
	 * @param rs the round set to modify
	 * @param d the delivery to delete
	 */
	public ComDelete(CityMap map, DeliveryRequest dr, RoundSet rs, Delivery d) {
		this.map=map;
		this.dr=dr;
		this.rs=rs;
		this.d=d;
		deliveryBefore= rs.getPreviousDelivery(d);
	}
	
	
	
	@Override
	public void doCde() throws ExceptionAlgo {
		rs.deleteDelivery(map, d);
		dr.deleteDelivery(d);
	}
	
	@Override
	public void undoCde() {
		try {
			rs.addDelivery(map, d, deliveryBefore);
			dr.addDelivery(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
