package controller;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.scene.canvas.Canvas;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.DeliveryRequest;
import view.MapViewBuilder;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;

public class StateMapLoaded extends StateDefault {

	@Override
	public DeliveryRequest loadDeliveryRequest(CityMap map) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger une demande de livraison");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		DeliveryRequest dr = null;

		if (file != null) {
			dr = DeliveryRequestDeserializer.Load(map, file);
			Controller.setCurrentState(Controller.stateDeliveryLoaded);
		}
		return dr;
	}

}
