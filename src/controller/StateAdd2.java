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
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
		mainView.setComputeButtonEnable(false);
		mainView.setDeleteButtonEnable(false);
		mainView.setMapButtonEnable(false);
		mainView.setDeliveryButtonEnable(false);
		mainView.setCancelButtonEnable(true);
		mainView.setMoveButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
		mainView.setIntersectionSelected(intersectionSelected);
		mainView.printRoundSet(cityMap,roundSet);
	}
	
	@Override
	public void selectDelivery(MainView mv, CityMap map, DeliveryRequest delivReq, RoundSet result, Delivery delivery,  ListCommands listeDeCdes) {
		mv.printMessage("");
		Delivery d = new Delivery(5, intersectionSelected); //TODO: permettre � l'utilisateur de saisir la dur�e
		/*delivReq.addDelivery(d);
		result.addDelivery(map, d, delivery);*/
		listeDeCdes.ajoute(new ComAdd(map,delivReq, result, d, delivery));
		Controller.stateRoundCalculated.setButtonsEnabled(mv);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	@Override
	public void cancel(MainView mainView) {
		mainView.printMessage("");
		mainView.setIntersectionSelected(null);
		this.intersectionSelected = null;
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	protected void actionSelect(Intersection i) {
		this.intersectionSelected = i;
	}
}
