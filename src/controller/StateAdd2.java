package controller;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class StateAdd2 extends StateDefault {
	Intersection intersectionSelected;

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
		mainView.setIntersectionSelected(intersectionSelected);
		mainView.printRoundSet(cityMap,roundSet);
	}
	
	@Override
	public void selectDelivery(MainView mv, CityMap map, DeliveryRequest delivReq, RoundSet result, Delivery delivery) {
		mv.printMessage("");
		Delivery d = new Delivery(5, intersectionSelected); //TODO: permettre � l'utilisateur de saisir la dur�e
		delivReq.addDelivery(d);
		result.addDelivery(map, d, delivery);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	protected void actionSelect(Intersection i) {
		this.intersectionSelected = i;
	}
}
