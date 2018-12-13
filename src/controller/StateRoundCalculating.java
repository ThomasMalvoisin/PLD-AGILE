package controller;

import model.CityMap;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;

public class StateRoundCalculating extends StateDefault {
	
	Thread calculate;
	Thread display;
	
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
		mainView.setCancelButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(true);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
		mainView.setZoomAutoButtonsEnable(true);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#stopAlgo()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void stopAlgo() {
		calculate.stop();
	}
	
	/* (non-Javadoc)
	 * @see controller.StateDefault#refreshView(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void refreshView(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet) {
		mainView.printCityMap(map);
		mainView.printRoundSet(map, roundSet);
		mainView.printPotentielDeliveries(map, request);
	}
	
	/**
	 * @param calculate
	 * @param display
	 */
	protected void actionCalculate(Thread calculate, Thread display) {
		this.calculate = calculate;
		this.display = display;
		this.calculate.start();
		this.display.start();
	}
}
