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
import model.RoundSet;
import view.MainView;
import view.MapViewBuilder;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class StateDefault implements State {

	@Override
	public void loadMap(MainView mainView, CityMap cityMap){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger un plan");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(" MAP XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		if (file != null) {
			try {
				cityMap.copy(MapDeserializer.load(file));
				mainView.printCityMap(cityMap);
				Controller.setCurrentState(Controller.stateMapLoaded);
			} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
				//TODO : mv.printMessage("Unable to open the selected file"); pour prévenir l'utilisateur
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest){
	}

	@Override
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, RoundSet roundSet) {
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		
	}
	
}
