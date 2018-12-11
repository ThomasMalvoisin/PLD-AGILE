package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import algo.Algorithms;
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
 		mainView.setMapButtonEnable(true);
 		mainView.setDeliveryButtonEnable(true);
		mainView.setCancelButtonEnable(true);
		mainView.setMoveButtonEnable(true);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(true);
		mainView.setRedoButtonEnable(true);
		mainView.setDiscardButtonEnable(true);
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
	}

	@Override
	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet result) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a delivery request");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		if (file != null) {
			int temp = Delivery.currentId;
			try {
				Delivery.currentId = 1;
				deliveryRequest.copy(DeliveryRequestDeserializer.Load(cityMap, file));
				mainView.printDeliveryRequest(cityMap, deliveryRequest);
				result.reset();
				Controller.stateDeliveryLoaded.setButtonsEnabled(mainView);
				Controller.setCurrentState(Controller.stateDeliveryLoaded);
			} catch (NumberFormatException | ParserConfigurationException | SAXException | IOException | ExceptionXML
					| ParseException e) {
				Delivery.currentId = temp;
				mainView.displayMessage("Unable to load delivery request",
						"Please choose a valid delivery request file. Make sure that all the delivery point's locations are available in the current loaded map !");
				e.printStackTrace();
			}
		}
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
		Controller.stateModify.actionDeliverySelected(delivery);
		Controller.stateModify.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateModify);
	}

	@Override
	public void delete(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			ListCommands listeDeCdes) {
		System.out.println("Delete " + deliverySelected.getId());
		
		listeDeCdes.ajoute(new ComDelete(map, deliveryRequest, roundSet, deliverySelected));
		
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
	public void cancel(MainView mainView) {
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
