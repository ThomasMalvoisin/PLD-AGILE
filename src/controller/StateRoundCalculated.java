package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import algo.Algorithms;
import algo.ExceptionAlgo;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Round;
import model.RoundSet;
import model.Section;
import view.MainView;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;

public class StateRoundCalculated extends StateDefault {

	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(true);
		mainView.setComputeButtonEnable(true);
		mainView.setDeleteButtonEnable(false);
		mainView.setMapButtonEnable(true);
		mainView.setDeliveryButtonEnable(true);
		mainView.setCancelButtonEnable(false);
		mainView.setStopButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setUndoButtonEnable(true);
		mainView.setRedoButtonEnable(true);
		mainView.setDiscardButtonEnable(true);
		mainView.setExportButtonEnable(true);
		mainView.setDeliveryManEnable(true);
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
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan,
			RoundSet roundSet, ListCommands listeDeCdes) {

		listeDeCdes.reset();
		roundSet.reset();

		RoundSet roundsTemp = new RoundSet();
		roundsTemp.setDepartureTime(delivReq.getStartTime());
	
		Thread calculate = new Thread(() -> {
			try {
				Algorithms.solveTSP(roundsTemp, map, delivReq, nbDeliveryMan);
			} catch(ExceptionAlgo e) {
				
			}
		});
		Thread display = new Thread(() -> {
			Platform.runLater(() -> {
				mainView.setLoader(true);
				mainView.printRoundSet(map, roundSet);
				mainView.printPotentielDeliveries(map, delivReq);
			});

			double duration = roundsTemp.getDuration();
			while (calculate.isAlive()) {
				if (roundsTemp.getDuration() < duration || duration == 0.0) {
					System.out.println(duration);
					duration = roundsTemp.getDuration();
					Platform.runLater(() -> {
						roundSet.copy(roundsTemp);
					});
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (roundsTemp.getDuration() < duration || duration == 0.0) {
				duration = roundsTemp.getDuration();
				Platform.runLater(() -> {
					roundSet.copy(roundsTemp);
				});
			}
			Platform.runLater(()-> mainView.setLoader(false));
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
		// mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap, roundSet);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		// TODO : changement dans l'ihm en appelant des fonctions de mainView : afficher
		// un message à l'utilisateur pour lui dire quoi faire

		mainView.setDeliverySelected(delivery);
		mainView.setRoundSelected(roundSet, delivery, true);
		Controller.stateModify.actionDeliverySelected(delivery);
		Controller.stateModify.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateModify);
	}

	@Override
	public void add(MainView mv) {
		mv.printMessage("Please select the point where you want to add a delivery... ");
		Controller.stateAdd1.setButtonsEnabled(mv);
		Controller.setCurrentState(Controller.stateAdd1);
	}

	@Override
	public void undo(ListCommands listeDeCdes) {
		listeDeCdes.undo();
	}

	@Override
	public void redo(ListCommands listeDeCdes) {
		listeDeCdes.redo();
	}

@Override
public void exportRoundSet(MainView mainView, RoundSet roundSet) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export");
		fileChooser.setInitialFileName("RoundsExport.txt");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
		File file = fileChooser.showSaveDialog(new Stage());

		if (file != null) {
			FileWriter fw;
			try {
				fw = new FileWriter(file);
				fw.write(roundSet.toString());
				fw.close();
			} catch (IOException e) {
				mainView.displayMessage("Error occured", "An error occured. Please, try again");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void discardChanges(ListCommands listeDeCdes) {
		listeDeCdes.discard();
	}
	
	
}
