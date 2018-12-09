package controller;

import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class StateAdd1 extends StateDefault {
	
	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
		mainView.setComputeButtonEnable(false);
		mainView.setDeleteButtonEnable(false);
		mainView.setMapButtonEnable(false);
		mainView.setDeliveryButtonEnable(false);
		mainView.setCancelButtonEnable(true);
		mainView.setStopButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap,roundSet);
	}
	
	@Override
	public void selectIntersection(MainView mainView, Intersection i) {
		mainView.printMessage("Please select the delivery you want to put before...");
		mainView.setIntersectionSelected(i);
		Controller.stateAdd2.actionSelect(i);
		Controller.stateAdd2.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateAdd2);
	}

	@Override
	public void cancel(MainView mainView) {
		mainView.printMessage("");
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
}
