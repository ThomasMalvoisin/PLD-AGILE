package controller;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class StateDefault implements State {

	
	@Override
	public void setButtonsEnabled(MainView mainView) {
	}

	@Override
	public void loadMap(MainView mainView, CityMap cityMap){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a map");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(" MAP XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		if (file != null) {
			try {
				cityMap.copy(MapDeserializer.load(file));
				mainView.printCityMap(cityMap);
				Controller.stateMapLoaded.setButtonsEnabled(mainView);
				Controller.setCurrentState(Controller.stateMapLoaded);
			} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
				mainView.displayMessage("Cannot load this map file","Please select a valid file");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest){
	}

	@Override
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan, RoundSet roundSet, ListCommands listeDeCdes) {
	}

	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
	}

	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet, Delivery delivery, ListCommands listeDeCdes) {		
	}
	
	@Override
	public void selectWarehouse(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet, Intersection i, ListCommands listeDeCdes) {
		
	}

	@Override
	public void delete(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet, ListCommands listeDeCdes) {
		
	}

	@Override
	public void selectIntersection(MainView mv,Intersection i) {
	}

	@Override
	public void add(MainView mv) {
	}

	@Override
	public void exportRoundSet(RoundSet roundSet) {	
  }
  
  @Override
	public void cancel(MainView mainView) {
	}
	
	@Override
	public void undo(ListCommands listeDeCdes) {
	}
	
	@Override
	public void redo(ListCommands listeDeCdes) {
	}
	
	@Override
	public void discardChanges(ListCommands listeDeCdes) {
	}
	
	@Override
	public void stopAlgo() {
	}

	@Override
	public void move(MainView mainView) {	
	}
	
}
