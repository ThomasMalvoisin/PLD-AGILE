package controller;

import algo.ExceptionAlgo;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
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
		mainView.setDeliveryManEnable(false);
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
		mainView.printMessage("");

		try {
			listeDeCdes.ajoute(new ComMove(map, roundSet, deliveryToMove, delivery, roundSet.getPreviousDelivery(deliveryToMove)));
      mainView.showNotificationCheck("Delivery moved", "The delivery point " + deliveryToMove.getId() + " has been moved after the delivery point " + delivery.getId() + " correctly");
		} catch (ExceptionAlgo e) {
			cancel(mainView, roundSet);	
			mainView.displayMessage(null, "Cannot move this delivery!");
			return;
		}
		/*boolean move = mainView.displayPopUpConfirmation("Are you sure to move this delivery?");
		if(move) {
			listeDeCdes.ajoute(new ComMove(map, roundSet, deliveryToMove, delivery, roundSet.getPreviousDelivery(deliveryToMove)));
		}else {
			cancel(mainView);
			return;
		}*/
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	@Override
	public void selectWarehouse(MainView mv, CityMap map, DeliveryRequest delivReq, RoundSet result, Intersection i,  ListCommands listeDeCdes) {
		mv.printMessage("");
		int resultSetSize = result.getRounds().size();
		int indexRound ;
		Delivery delivery;
		if(resultSetSize>1) {
			 indexRound = mv.displayPopUpWarehouse(resultSetSize, false);
			if( indexRound !=-1 ) {
				indexRound = indexRound-1;
				delivery=result.getRounds().get(indexRound).getDeliveries().get(0);
				try {
					listeDeCdes.ajoute(new ComMove(map, result, deliveryToMove, delivery, result.getPreviousDelivery(deliveryToMove)));
          mv.showNotificationCheck("Delivery moved", "The delivery point " + deliveryToMove.getId() + " has been moved after the warehouse in the round " + indexRound + 1 + " correctly");
				} catch (ExceptionAlgo e) {
					cancel(mv, result);	
					mv.displayMessage(null, "Cannot move this delivery!");
					return;
				}
			}else {
				cancel(mv, result);
				return;
			}
		}
		else {
			boolean move = mv.displayPopUpConfirmation("Are you sure to move this delivery?");
			if(move) {
				delivery=result.getRounds().get(0).getDeliveries().get(0);
				try {
					listeDeCdes.ajoute(new ComMove(map, result, deliveryToMove, delivery, result.getPreviousDelivery(deliveryToMove)));
          mv.showNotificationCheck("Delivery moved", "The delivery point " + deliveryToMove.getId() + " has been moved after the warehouse correctly");
				} catch (ExceptionAlgo e) {
					cancel(mv, result);	
					mv.displayMessage(null, "Cannot move this delivery!");
					return;
				}
			}else {
				cancel(mv, result);
				return;
			}
		}
		Controller.stateRoundCalculated.setButtonsEnabled(mv);
		Controller.setCurrentState(Controller.stateRoundCalculated);
		
	}

	@Override
	public void cancel(MainView mainView, RoundSet roundSet) {
	  mainView.printMessage("");
		this.deliveryToMove = null;
		mainView.setDeliverySelected(null);
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	public void actionDeliveriesSelected(Delivery toMove) {
		this.deliveryToMove = toMove;
	}
}
