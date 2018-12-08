package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;

public class StateRoundCalculated extends StateDefault {
	@Override
	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a delivery request");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		if (file != null) {
			int temp = Delivery.currentId;
			try {
				Delivery.currentId = 1;
				deliveryRequest.copy(DeliveryRequestDeserializer.Load(cityMap, file));
				mainView.setAddButtonEnable(false);
				mainView.setComputeButtonEnable(true);
				mainView.setDeleteButtonEnable(false);
				mainView.setMapButtonEnable(true);
				mainView.setDeliveryButtonEnable(true);
				mainView.printDeliveryRequest(cityMap, deliveryRequest);
				Controller.setCurrentState(Controller.stateDeliveryLoaded);
			} catch (NumberFormatException | ParserConfigurationException | SAXException | IOException | ExceptionXML
					| ParseException e) {
				Delivery.currentId = temp;
				mainView.displayMessage("Unable to load delivery request", "Please choose a valid delivery request file. Make sure that all the delivery point's locations are available in the current loaded map !");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		//mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap,roundSet);
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,Delivery delivery) {
		// TODO : changement dans l'ihm en appelant des fonctions de mainView : afficher
		// un message à l'utilisateur pour lui dire quoi faire

		mainView.setDeliverySelected(delivery);
		Controller.stateModify.actionDeliverySelected(delivery);
		mainView.setAddButtonEnable(false);
		mainView.setComputeButtonEnable(false);
		mainView.setDeleteButtonEnable(true);
		mainView.setMapButtonEnable(true);
		mainView.setDeliveryButtonEnable(true);
		Controller.setCurrentState(Controller.stateModify);
	}

	@Override
	public void add(MainView mv) {
		mv.printMessage("Please select the point where you want to add a delivery... ");
		mv.setAddButtonEnable(false);
		mv.setComputeButtonEnable(false);
		mv.setDeleteButtonEnable(false);
		mv.setMapButtonEnable(false);
		mv.setDeliveryButtonEnable(false);
		Controller.setCurrentState(Controller.stateAdd1);
	}
	
}
