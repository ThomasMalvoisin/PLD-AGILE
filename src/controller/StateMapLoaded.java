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

public class StateMapLoaded extends StateDefault{

	@Override
	public void loadDeliveryRequest(MapViewBuilder mvb, CityMap map, DeliveryRequest delivReq) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger une demande de livraison");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());
		
		if(file != null) {
			try {
				DeliveryRequestDeserializer.Load(delivReq, map, file);
			} catch (ExceptionXML e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Controller.setCurrentState(Controller.stateDeliveryLoaded);
		}
	}

	
}
