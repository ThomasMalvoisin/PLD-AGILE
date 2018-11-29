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
			try {
				deliveryRequest.copy(DeliveryRequestDeserializer.Load(cityMap, file));
				mainView.printDeliveryRequest(deliveryRequest);
				Controller.setCurrentState(Controller.stateDeliveryLoaded);
			} catch (NumberFormatException | ParserConfigurationException | SAXException | IOException | ExceptionXML
					| ParseException e) {
				// TODO : mv.printMessage("Unable to open the selected file"); pour prévenir
				// l'utilisateur
				e.printStackTrace();
			}
		}
	}

	@Override
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, RoundSet roundSet) {
		Algorithms algoUtil = new Algorithms(map);
		new Thread(() -> {
			Platform.runLater(() -> {
				mainView.setLoader(true);
			});
			
			algoUtil.dijkstraDeliveryRequest(delivReq);
			// TODO : Constructeur de copie nécessaire
			//TODO : trouver une solution pour modifier la variable roundSet dans ce thread
			roundSet.copy(algoUtil.solveTSP(delivReq, 1));
			
			if (roundSet != null) {
				Platform.runLater(() -> {
					mainView.printRoundSet(roundSet);
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
