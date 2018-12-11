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
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap,roundSet);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
		mainView.setIntersectionSelected(intersectionSelected);
	}
	
	@Override
	public void selectDelivery(MainView mv, CityMap map, DeliveryRequest delivReq, RoundSet result, Delivery delivery,  ListCommands listeDeCdes) {
		mv.printMessage("");
		int duration;
		duration = mv.displayPopUpAdd(null);
		if(duration!=-1) {
			Delivery d = new Delivery(duration, intersectionSelected); //TODO: permettre � l'utilisateur de saisir la dur�e
			listeDeCdes.ajoute(new ComAdd(map,delivReq, result, d, delivery));
		}else {
			cancel(mv);
			return;
		}
		Controller.stateRoundCalculated.setButtonsEnabled(mv);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	@Override
	public void selectWarehouse(MainView mv, CityMap map, DeliveryRequest delivReq, RoundSet result, Intersection i,  ListCommands listeDeCdes) {
		mv.printMessage("");
		int resultSetSize = result.getRounds().size();
		int duration;
		Delivery d ;
		int indexRound ;
		if(resultSetSize>1) {
			 indexRound = mv.displayPopUpWarehouse(resultSetSize, true);
			if( indexRound !=-1 ) {
				duration = mv.displayPopUpAdd(null);
				if(duration!=-1) {
					d = new Delivery(duration, intersectionSelected);
					indexRound = indexRound-1;
					Delivery delBefore = result.getRounds().get(indexRound).getDeliveries().get(0);
					listeDeCdes.ajoute(new ComAdd(map,delivReq, result, d, delBefore));
				}else {
					cancel(mv);
					return;
				}
			}else {
				cancel(mv);
				return;
			}
		}
		else if(resultSetSize==1){
			
			duration = mv.displayPopUpAdd(null);
			if(duration!=-1) {
				indexRound=0;
				d = new Delivery(duration, intersectionSelected);
				Delivery delBefore = result.getRounds().get(indexRound).getDeliveries().get(0);
				listeDeCdes.ajoute(new ComAdd(map,delivReq, result, d, delBefore));
			}else {
				cancel(mv);
				return;
			}
			
		}
		
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
