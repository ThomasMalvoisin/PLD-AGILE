package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import algo.Algorithms;
import algo.ExceptionAlgo;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;

public class StateModify extends StateDefault {

	private Delivery deliverySelected;

	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
 		mainView.setComputeButtonEnable(false);
 		mainView.setDeleteButtonEnable(true);
 		mainView.setMapButtonEnable(false);
 		mainView.setDeliveryButtonEnable(false);
		mainView.setCancelButtonEnable(true);
		mainView.setMoveButtonEnable(true);
		mainView.setStopButtonEnable(false);
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
		// mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap, roundSet);
		mainView.setDeliverySelected(this.deliverySelected);
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		// TODO : changement dans l'ihm en appelant des fonctions de mainView : afficher
		// un message à l'utilisateur pour lui dire quoi faire
		mainView.setDeliverySelected(delivery);
		mainView.setRoundSelected(roundSet, deliverySelected, false);
		mainView.setRoundSelected(roundSet, delivery, true);
		Controller.stateModify.actionDeliverySelected(delivery);
		Controller.stateModify.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateModify);
	}

	@Override
	public void delete(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			ListCommands listeDeCdes) {
		System.out.println("Delete " + deliverySelected.getId());
				try {
			listeDeCdes.ajoute(new ComDelete(map, deliveryRequest, roundSet, deliverySelected));
      mainView.showNotificationCheck("Delivery deleted", "The delivery point number " + deliverySelected.getId() + " has been deleted correctly");
		} catch (ExceptionAlgo e) {
			cancel(mainView, roundSet);	
			mainView.displayMessage(null, "Cannot delete this delivery!");
			return;
		}
		/*boolean delete = mainView.displayPopUpConfirmation("Are you sure to delete this delivery?");
		if(delete) {
			listeDeCdes.ajoute(new ComDelete(map, deliveryRequest, roundSet, deliverySelected));
		}else {
			cancel(mainView);
			return;
		}*/
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}

	@Override
	public void cancel(MainView mainView, RoundSet roundSet) {
		mainView.setRoundSelected(roundSet, deliverySelected, false);
		this.deliverySelected = null;
		mainView.setDeliverySelected(null);
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	

	@Override
	public void move(MainView mainView) {
		mainView.printMessage("Please select the delivery you want to put before...");
		Controller.stateMove.actionDeliveriesSelected(this.deliverySelected);
		Controller.stateMove.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateMove);
	}

	// methode appelee avant d'entrer dans l'etat this, pour definir le delivery
	// selectionne
	protected void actionDeliverySelected(Delivery delivery) {
		this.deliverySelected = delivery;
		System.out.println(delivery.getAdress().getLatitude());
	}

}
