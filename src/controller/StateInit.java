package controller;

import view.MainView;

public class StateInit extends StateDefault{

	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setMapButtonEnable(true);
		mainView.setDeliveryButtonEnable(false);
		mainView.setComputeButtonEnable(false);
		mainView.setAddButtonEnable(false);
		mainView.setDeleteButtonEnable(false);
		mainView.setCancelButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
		mainView.setZoomAutoButtonsEnable(false);
	}
	
}
