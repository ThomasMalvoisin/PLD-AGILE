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

	/* (non-Javadoc)
	 * @see controller.StateDefault#setButtonsEnabled(view.MainView)
	 */
	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
 		mainView.setComputeButtonEnable(false);
 		mainView.setDeleteButtonEnable(false);
 		mainView.setMapButtonEnable(false);
 		mainView.setDeliveryButtonEnable(false);
		mainView.setCancelButtonEnable(true);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
		mainView.setZoomAutoButtonsEnable(true);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#refreshView(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		mainView.printRoundSet(cityMap, roundSet);
		mainView.setDeliverySelected(this.deliveryToMove);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#selectDelivery(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Delivery, controller.ListCommands)
	 */
	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		mainView.printMessage("");

		try {
			listeDeCdes.ajoute(new ComMove(map, roundSet, deliveryToMove, delivery, roundSet.getPreviousDelivery(deliveryToMove)));
			mainView.printMessage("Delivery moved ! Press Add to add a delivery or Select a delivery to delete or move it. ");
			mainView.showNotificationCheck("Delivery moved", "The delivery point " + deliveryToMove.getId() + " has been moved after the delivery point " + delivery.getId() + " correctly");
		} catch (ExceptionAlgo e) {
			cancel(mainView, roundSet);	
			mainView.displayMessage(null, "Cannot move this delivery!");
			return;
		}
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	/* (non-Javadoc)
	 * @see controller.StateDefault#selectWarehouse(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Intersection, controller.ListCommands)
	 */
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
					mv.printMessage("Delivery moved ! Press Add to add a delivery or Select a delivery to delete or move it. ");
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
					mv.printMessage("Delivery moved ! Press Add to add a delivery or Select a delivery to delete or move it. ");
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

	/* (non-Javadoc)
	 * @see controller.StateDefault#cancel(view.MainView, model.RoundSet)
	 */
	@Override
	public void cancel(MainView mainView, RoundSet roundSet) {
		this.deliveryToMove = null;
		mainView.setDeliverySelected(null);
		mainView.printMessage("Canceled ! Press Add to add a delivery or Select a delivery to delete or move it. ");
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	/**Select a delivery
	 * @param toMove
	 */
	public void actionDeliveriesSelected(Delivery toMove) {
		this.deliveryToMove = toMove;
	}
}
