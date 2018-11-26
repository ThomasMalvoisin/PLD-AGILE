package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import model.CityMap;
import model.CityMapElement;
import model.DeliveryRequest;
import model.Intersection;
import model.Section;
import model.VisitorElement;
import view.GraphicView;
import view.MapViewBuilder;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class Controller implements Initializable{

	@FXML
	Canvas canvas;
	MapViewBuilder mvb;
	CityMap map;
	DeliveryRequest delivReq;
	GraphicView gv;
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

	public void loadDeliveryRequest() {
		currentState.loadDeliveryRequest(mvb, map, delivReq);
	}

	public void drawRandomLine() {
		
		double[] p = {100,45};
		mvb.drawPoint(p);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mvb = new MapViewBuilder(canvas);
		map = new CityMap();
		delivReq = new DeliveryRequest();
		gv = new GraphicView(mvb, map, delivReq);
		currentState=stateInit;
	}

}
