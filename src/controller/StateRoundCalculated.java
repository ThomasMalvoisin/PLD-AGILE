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

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
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
		mainView.setComputeButtonEnable(false);
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
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		// mainView.printDeliveryRequest(cityMap, deliveryRequest);
		mainView.printPotentielDeliveries(cityMap, deliveryRequest);
		mainView.printRoundSet(cityMap, roundSet);
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		// TODO : changement dans l'ihm en appelant des fonctions de mainView : afficher
		// un message Ã  l'utilisateur pour lui dire quoi faire

		mainView.setDeliverySelected(delivery);
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
	public void exportRoundSet(RoundSet roundSet) {
		System.out.println("export");
		try{
			File ff=new File("C:\\Users\\Samuel GOY\\Desktop\\PLD AGILE\\Feuille de Route\\Roadmap.txt"); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			
			ffw.write(roundSet.toString());
			ffw.close();
		} catch (Exception e) {}
	}


	@Override
	public void discardChanges(ListCommands listeDeCdes) {
		listeDeCdes.discard();
	}
}
