package controller;

import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class StateRoundCalculating extends StateDefault {
	Thread calculate;
	Thread display;
	
	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
		mainView.setComputeButtonEnable(false);
		mainView.setDeleteButtonEnable(false);
		mainView.setMapButtonEnable(false);
		mainView.setDeliveryButtonEnable(false);
		mainView.setCancelButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(true);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
	}

	@Override
	public void stopAlgo() {
		calculate.stop();
	}
	
	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap, roundSet);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
	}
	
	protected void actionCalculate(Thread calculate, Thread display) {
		this.calculate = calculate;
		this.display = display;
		this.calculate.start();
		this.display.start();
	}
}
