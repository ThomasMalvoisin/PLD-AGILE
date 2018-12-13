package controller;

import java.util.ArrayList;

import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class StateAdd1 extends StateDefault {
	
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
		mainView.setStopButtonEnable(false);
		mainView.setMoveButtonEnable(false);
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
	}
	
	/* (non-Javadoc)
	 * @see controller.StateDefault#selectIntersection(view.MainView, model.Intersection)
	 */
	@Override
	public void selectIntersection(MainView mainView, Intersection i) {
		mainView.printMessage("Please select the delivery you want to put before...");
		mainView.setIntersectionSelected(i);
		Controller.stateAdd2.actionSelect(i);
		Controller.stateAdd2.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateAdd2);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#cancel(view.MainView, model.RoundSet)
	 */
	@Override
	public void cancel(MainView mainView, RoundSet roundSet) {
		mainView.printMessage("Canceled ! Press Add to add a delivery or Select a delivery to delete or move it. ");
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	/* (non-Javadoc)
	 * @see controller.StateDefault#hoverIntersection(view.MainView, model.CityMap, model.Intersection)
	 */
	@Override 
	public void hoverIntersection(MainView mainView, CityMap map, Intersection inter) {
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
		mainView.printMessage(dlvP);
	}
	
	/* (non-Javadoc)
	 * @see controller.StateDefault#exitIntersection(view.MainView)
	 */
	@Override
	public void exitIntersection(MainView mainView) {
		mainView.printMessage("Please select the point where you want to add a delivery... ");
	}
}
