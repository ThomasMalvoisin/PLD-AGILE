package controller;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;

public class StateMove extends StateDefault{
	
	private Delivery deliveryToMove;

	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
 		mainView.setComputeButtonEnable(false);
 		mainView.setDeleteButtonEnable(false);
 		mainView.setMapButtonEnable(true);
 		mainView.setDeliveryButtonEnable(true);
		mainView.setCancelButtonEnable(true);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		mainView.printRoundSet(cityMap, roundSet);
		mainView.setDeliverySelected(this.deliveryToMove);
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		listeDeCdes.ajoute(new ComMove(map, roundSet, deliveryToMove, delivery, roundSet.getPreviousDelivery(deliveryToMove)));
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}

	@Override
	public void cancel(MainView mainView) {
		this.deliveryToMove = null;
		mainView.setDeliverySelected(null);
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	public void actionDeliveriesSelected(Delivery toMove) {
		this.deliveryToMove = toMove;
	}
}
