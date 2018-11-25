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

public class StateDefault implements State{

	@Override
	public void loadMap(MapViewBuilder mvb, CityMap map) {	
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger un plan");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());
		
		if(file != null) {
			mvb.clearCanevas();
			try {
				MapDeserializer.load(map, file);
			} catch (ExceptionXML e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			mvb.build(map);
			
			Controller.setCurrentState(Controller.stateMapLoaded);
		}
	}

	@Override
	public void loadDeliveryRequest(MapViewBuilder mvb, CityMap map, DeliveryRequest delivReq) {
	}
}
