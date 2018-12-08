package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import algo.Algorithms;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;

public class StateDeliveryLoaded extends StateDefault {

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
				mainView.printDeliveryRequest(cityMap, deliveryRequest);
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

	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan,
			RoundSet roundSet) {

		new Thread(() -> {
			Platform.runLater(() -> {
				mainView.setLoader(true);
			});

			roundSet.copy(Algorithms.solveTSP(map,delivReq, nbDeliveryMan));
			
			if (roundSet != null) {
				Platform.runLater(() -> {
					roundSet.calculTime(delivReq);
					mainView.printRoundSet(map,roundSet);
					mainView.printPotentielDeliveries(map, delivReq);
					mainView.setAddButtonEnable(true);
					mainView.setComputeButtonEnable(false);
					mainView.setDeleteButtonEnable(false);
					mainView.setMapButtonEnable(true);
					mainView.setDeliveryButtonEnable(true);
					Controller.setCurrentState(Controller.stateRoundCalculated);
				});
			}

			Platform.runLater(() -> {
				mainView.setLoader(false);
			});
		}).start();
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
			mainView.printCityMap(cityMap);
			mainView.printDeliveryRequest(cityMap, deliveryRequest);
	}
	
	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,Delivery delivery) {
		// TODO : changement dans l'ihm en appelant des fonctions de mainView : afficher
		// un message à l'utilisateur pour lui dire quoi faire

		mainView.setDeliverySelected(delivery);
		Controller.stateModify.actionDeliverySelected(delivery);
	}
}
