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
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
		mainView.setComputeButtonEnable(true);
		mainView.setDeleteButtonEnable(false);
		mainView.setMapButtonEnable(true);
		mainView.setDeliveryButtonEnable(true);
		mainView.setCancelButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
	}

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
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan,
			RoundSet roundSet, ListCommands listeDeCdes) {
		
		listeDeCdes.reset();

		RoundSet roundsTemp = new RoundSet();
		Thread calculate = new Thread(() -> {
			Algorithms.solveTSP(roundsTemp, map, delivReq, nbDeliveryMan);
		});
		Thread display = new Thread(() -> {
			Platform.runLater(() -> {
				mainView.printRoundSet(map, roundSet);
				mainView.printPotentielDeliveries(map, delivReq);
			});

			double duration = roundsTemp.getDuration();
			while (calculate.isAlive()) {
				if (roundsTemp.getDuration() < duration || duration == 0.0) {
					System.out.println(duration);
					duration = roundsTemp.getDuration();
					Platform.runLater(() -> {
						roundsTemp.calculTime(delivReq);
						roundSet.copy(roundsTemp);
//						roundSet.calculTime(delivReq);
					});
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (roundsTemp.getDuration() < duration || duration == 0.0) {
				duration = roundsTemp.getDuration();
				Platform.runLater(() -> {
					roundsTemp.calculTime(delivReq);
					roundSet.copy(roundsTemp);
//					roundSet.calculTime(delivReq);
				});
			}

			Controller.stateRoundCalculated.setButtonsEnabled(mainView);
			Controller.setCurrentState(Controller.stateRoundCalculated);
		});
		Controller.stateRoundCalculating.actionCalculate(calculate, display);
		Controller.stateRoundCalculating.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculating);
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		mainView.printDeliveryRequest(cityMap, deliveryRequest);
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		// TODO : changement dans l'ihm en appelant des fonctions de mainView : afficher
		// un message à l'utilisateur pour lui dire quoi faire

		mainView.setDeliverySelected(delivery);
		Controller.stateModify.setButtonsEnabled(mainView);
		Controller.stateModify.actionDeliverySelected(delivery);
	}
}
