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
import xml.ExceptionXML;
import xml.MapDeserializer;

public class Controller implements Initializable{

	@FXML
	Canvas canvas;

	protected static State currentState;
	protected static final StateInit stateInit = new StateInit();
	protected static final StateMapLoaded stateMapLoaded = new StateMapLoaded();

	protected static void setCurrentState(State state) {
		currentState = state;
	}

	public void loadMap() {
		currentState.loadMap(canvas);
	}

	// Clic sur un bouton -> à relier au fxml
	public void loadDeliveryRequest() {

	}

	public void drawRandomLine() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO A modifier pour stateInit
		this.setCurrentState(stateInit);
	}
}
