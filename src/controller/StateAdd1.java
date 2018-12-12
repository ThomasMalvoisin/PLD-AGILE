package controller;

import java.util.ArrayList;

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
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
		mainView.setZoomAutoButtonsEnable(true);
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap,roundSet);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
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
	public void cancel(MainView mainView, RoundSet roundSet) {
		mainView.printMessage("");
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	@Override 
	public void hoverIntersection(MainView mv, CityMap map, Intersection inter) {
		ArrayList<String> sectionNames = map.getIntersectionSectionNames(inter);
		String dlvP ="Add a new delivery at : ";
		int i = 0;
		for(String name : sectionNames) {
			if(i != 0 ) {
				dlvP += " , ";
			}
			if(name.equals("")){
				name =  "No Name";
			}
			dlvP +=  name;
			i++;
		}
		mv.printMessage(dlvP);
	}
	
	@Override
	public void exitIntersection(MainView mv) {
		mv.printMessage("Please select the point where you want to add a delivery... ");
	}
}
