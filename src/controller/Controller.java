package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import model.CityMap;
import model.DeliveryRequest;
import view.MapViewBuilder;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class Controller implements Initializable{

	@FXML
	Canvas canvas;
	MapViewBuilder mvb;
	CityMap map;
	DeliveryRequest delivReq;
	protected static State currentState;
	protected static final StateInit stateInit = new StateInit();
	protected static final StateMapLoaded stateMapLoaded = new StateMapLoaded();
	protected static final StateDeliveryLoaded stateDeliveryLoaded = new StateDeliveryLoaded();

	protected static void setCurrentState(State state) {
		currentState = state;
	}

	public void loadMap() {
		currentState.loadMap(mvb, map);
	}

	// Clic sur un bouton -> à relier au fxml
	public void loadDeliveryRequest() {
		currentState.loadDeliveryRequest(mvb, map, delivReq);
	}

	public void drawRandomLine() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mvb = new MapViewBuilder(canvas);
		map = new CityMap();
		delivReq = new DeliveryRequest();
		currentState=stateInit;
	}
}
