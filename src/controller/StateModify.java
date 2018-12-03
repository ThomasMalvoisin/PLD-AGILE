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
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		mainView.printDeliveryRequest(deliveryRequest);
		mainView.printRoundSet(roundSet);
		mainView.setDeliverySelected(this.deliverySelected);
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet,
			Delivery delivery) {
		// TODO : changement dans l'ihm en appelant des fonctions de mainView : afficher
		// un message à l'utilisateur pour lui dire quoi faire
		mainView.setDeliverySelected(delivery);
		Controller.stateModify.actionDeliverySelected(delivery);
		Controller.setCurrentState(Controller.stateModify);
	}

	@Override
	public void delete(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		// TODO : Recalculer le morceau de tournée qui a été modifié

		System.out.println("Delete " + deliverySelected.getId());
		deliveryRequest.delete(deliverySelected);
		roundSet.deleteDelivery(deliverySelected);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}

	// methode appelee avant d'entrer dans l'etat this, pour definir le delivery
	// selectionne
	protected void actionDeliverySelected(Delivery delivery) {
		this.deliverySelected = delivery;
		System.out.println(delivery.getAdress().getLatitude());
	}

}
