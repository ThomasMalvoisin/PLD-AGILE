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
import xml.ExceptionXML;
import xml.MapDeserializer;

public class StateDefault implements State {

	@Override
	public CityMap loadMap() throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger un plan");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		CityMap map = null;

		if (file != null) {
			map = MapDeserializer.load(file);
			Controller.setCurrentState(Controller.stateMapLoaded);
		}

		return map;
	}

	@Override
	public DeliveryRequest loadDeliveryRequest(CityMap map) throws Exception{
		return null;
	}
}
