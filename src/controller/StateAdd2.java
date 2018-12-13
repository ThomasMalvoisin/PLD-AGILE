package controller;

import algo.ExceptionAlgo;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class StateAdd2 extends StateDefault {
	
	Intersection intersectionSelected;

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
	public void refreshView(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet) {
		mainView.printCityMap(map);
		mainView.printRoundSet(map, roundSet);
		mainView.printPotentielDeliveries(map, request);
		mainView.setIntersectionSelected(intersectionSelected);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#selectDelivery(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Delivery, controller.ListCommands)
	 */
	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest delivReq, RoundSet result, Delivery delivery,
			ListCommands listeDeCdes) {
		mainView.printMessage("");
		int duration = mainView.displayPopUpAdd(null);
		if (duration != -1) {
			Delivery d = new Delivery(duration, intersectionSelected);
			try {
				listeDeCdes.ajoute(new ComAdd(map, delivReq, result, d, delivery));
				mainView.showNotificationCheck("Delivery added", "The new delivery point has been added correctly");
				mainView.printMessage("Delivery added ! Press Add to add another delivery or Select a delivery to delete or move it.");
			} catch (ExceptionAlgo e) {
				e.printStackTrace();
				cancel(mainView, result);
				mainView.displayMessage(null, "Cannot add this delivery, this point is not reachable");
				return;
			}
		} else {
			cancel(mainView, result);
			return;
		}
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#selectWarehouse(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Intersection, controller.ListCommands)
	 */
	@Override
	public void selectWarehouse(MainView mainView, CityMap map, DeliveryRequest delivReq, RoundSet result, Intersection i,
			ListCommands listeDeCdes) {
		mainView.printMessage("");
		int resultSetSize = result.getRounds().size();
		int duration, indexRound;
		Delivery d;
		if (resultSetSize > 1) {
			indexRound = mainView.displayPopUpWarehouse(resultSetSize, true);
			if (indexRound != -1) {
				duration = mainView.displayPopUpAdd(null);
				if (duration != -1) {
					d = new Delivery(duration, intersectionSelected);
					indexRound = indexRound - 1;
					Delivery delBefore = result.getRounds().get(indexRound).getDeliveries().get(0);
					try {
						listeDeCdes.ajoute(new ComAdd(map, delivReq, result, d, delBefore));
						mainView.showNotificationCheck("Delivery added", "The new delivery point has been added correctly");
					} catch (ExceptionAlgo e) {
						e.printStackTrace();
						cancel(mainView, result);
						mainView.displayMessage(null, "Cannot add this delivery, this point is not reachable");
						return;
					}
				} else {
					cancel(mainView, result);
					return;
				}
			} else {
				cancel(mainView, result);
				return;
			}
		} else if (resultSetSize == 1) {
			duration = mainView.displayPopUpAdd(null);
			if (duration != -1) {
				indexRound = 0;
				d = new Delivery(duration, intersectionSelected);
				Delivery delBefore = result.getRounds().get(indexRound).getDeliveries().get(0);
				try {
					listeDeCdes.ajoute(new ComAdd(map, delivReq, result, d, delBefore));
					mainView.showNotificationCheck("Delivery added", "The new delivery point has been added correctly");
				} catch (ExceptionAlgo e) {
					cancel(mainView, result);
					e.printStackTrace();
					mainView.displayMessage(null, "Cannot add this delivery, this point is not reachable");
					return;
				}
			} else {
				cancel(mainView, result);
				return;
			}
		}
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#cancel(view.MainView, model.RoundSet)
	 */
	@Override
	public void cancel(MainView mainView, RoundSet roundSet) {
		mainView.printMessage("Canceled ! Press Add to add a delivery or Select a delivery to delete or move it. ");
		mainView.setIntersectionSelected(null);
		this.intersectionSelected = null;
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}

	/**
	 * @param i
	 * 		Intersection
	 */
	protected void actionSelect(Intersection i) {
		this.intersectionSelected = i;
	}
}
