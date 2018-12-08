package controller;

import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class StateAdd1 extends StateDefault {
	
	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap,roundSet);
	}
	
	@Override
	public void selectIntersection(MainView mv, Intersection i) {
		mv.printMessage("Please select the delivery you want to put before...");
		mv.setIntersectionSelected(i);
		Controller.stateAdd2.actionSelect(i);
		mv.setAddButtonEnable(false);
		mv.setComputeButtonEnable(false);
		mv.setDeleteButtonEnable(false);
		mv.setMapButtonEnable(false);
		mv.setDeliveryButtonEnable(false);
		Controller.setCurrentState(Controller.stateAdd2);
	}
}
