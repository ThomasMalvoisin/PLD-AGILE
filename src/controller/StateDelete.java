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

public class StateDelete extends StateDefault{
	

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
	}
	
	@Override
	public void selectDelivery(MainView mv, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet, Delivery delivery) {
		System.out.println("Delete " + delivery.getId());
		deliveryRequest.delete(delivery);
		roundSet.deleteDelivery(delivery);
		
		// TODO : Recalculer le morceau de tournée qui a été modifié
		mv.printMessage("");
		Controller.setCurrentState(Controller.stateRoundCalculated);
		
	}
}
