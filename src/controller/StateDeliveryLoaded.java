package controller;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.DeliveryRequest;
import xml.DeliveryRequestDeserializer;

public class StateDeliveryLoaded extends StateDefault{
	
	@Override
	public DeliveryRequest loadDeliveryRequest(CityMap map) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger une demande de livraison");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		DeliveryRequest dr = null;

		if (file != null) {
			//Ajouter des vérifications
			dr = DeliveryRequestDeserializer.Load(map, file);
			Controller.setCurrentState(Controller.stateDeliveryLoaded);
		}
		return dr;
	}
}
