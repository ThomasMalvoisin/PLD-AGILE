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
		fileChooser.setTitle("Charger une demande de livraison");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		if (file != null) {
			int temp = Delivery.currentId;
			try {
				Delivery.currentId = 1;
				deliveryRequest.copy(DeliveryRequestDeserializer.Load(cityMap, file));
				mainView.printDeliveryRequest(deliveryRequest);
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


	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan, RoundSet roundSet) {

		new Thread(() -> {
			Platform.runLater(() -> {
				mainView.setLoader(true);
			});


			//algoUtil.dijkstraDeliveryRequest(delivReq);
			roundSet.copy(Algorithms.solveTSP(map,delivReq, nbDeliveryMan));

			//TODO : trouver une solution pour modifier la variable roundSet dans ce thread

			
			if (roundSet != null) {
				Platform.runLater(() -> {
					mainView.printRoundSet(roundSet);
					mainView.printPotentielDeliveries(map, delivReq);
					Controller.setCurrentState(Controller.stateRoundCalculated);
				});
			}
			
			Platform.runLater(() -> {
				mainView.setLoader(false);
			});
			// System.out.println(result.getRounds().get(0).getDuration());
		}).start();
	}
	
	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
			mainView.printCityMap(cityMap);
			mainView.printDeliveryRequest(deliveryRequest);

	}
}
