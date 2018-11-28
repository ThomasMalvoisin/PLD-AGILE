package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import model.Section;
import view.GraphicView;
import view.MapViewBuilder;
import view.TextView;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class Controller implements Initializable{

	@FXML
	Pane pane;
	
	@FXML
	TextFlow txtArea;
	
	@FXML 
	VBox loader;
	
	CityMap map;
	DeliveryRequest delivReq;
	RoundSet result;
	GraphicView gv;
	TextView tv;
	protected static State currentState;
	protected static final StateInit stateInit = new StateInit();
	protected static final StateMapLoaded stateMapLoaded = new StateMapLoaded();
	protected static final StateDeliveryLoaded stateDeliveryLoaded = new StateDeliveryLoaded();
	protected static final StateRoundCalculated stateRoundCalculated = new StateRoundCalculated();

	protected static void setCurrentState(State state) {
		currentState = state;
	}

	public void loadMap() {
		try {
			CityMap mapTemp=currentState.loadMap();
			if(mapTemp!=null) {
				delivReq = null;
				result=null;
				map = mapTemp;
				tv.clearDeliveryRequest();
				gv.drawCityMap(map);
			}
		} catch (Exception e) {
			//TODO : informer l'utilisateur que le fichier n'a pas pu être chargé
			e.printStackTrace();
		}
	}

	public void loadDeliveryRequest() {
		try {
			DeliveryRequest delivReqTemp = currentState.loadDeliveryRequest(map);
			if(delivReqTemp!=null) {
				delivReq=delivReqTemp;
				result=null;
				gv.drawDeliveryRequest(delivReq);
				tv.printDeliveryRequest(delivReq);
			}
			
			//TODO : delivReq.addObserver(gv);
		} catch (Exception e) {
			//TODO : informer l'utilisateur que le fichier n'a pas pu être chargé
			e.printStackTrace();
		}
	}
	
	public void roundsCompute() {
		 new Thread(() -> {
		try {
			//System.out.println(delivReq.getStartTime());
			 Platform.runLater(() -> {
				 loader.toFront();
	            });
			RoundSet tempResult = currentState.roundsCompute(map, delivReq);
			if(tempResult != null) {
				result = tempResult;
				 Platform.runLater(() -> {
					 gv.drawRoundSet(result);
		            });
			}
			Platform.runLater(() -> {
				 loader.toBack();
	            });
			//System.out.println(result.getRounds().get(0).getDuration());

		} catch (Exception e) {
			e.printStackTrace();
		}
		 }).start();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gv = new GraphicView(pane);
		tv = new TextView(txtArea);
		pane.widthProperty().addListener( 
	            (observable, oldvalue, newvalue) -> 
	            {
		            if(map!=null) {
		            	gv.drawCityMap(map);
		            	if(delivReq!=null) {
		            		gv.drawDeliveryRequest(delivReq);
		            		tv.printDeliveryRequest(delivReq);
		            		if(result!=null) {
		            			gv.drawRoundSet(result);
		            		}
		            	}
		            }
	            }
        );
        pane.heightProperty().addListener(   
	            (observable, oldvalue, newvalue) -> 
	            {
	            	if(map!=null) {
		            	gv.drawCityMap(map);
		            	if(delivReq!=null) {
		            		gv.drawDeliveryRequest(delivReq);
		            		tv.printDeliveryRequest(delivReq);
		            		if(result!=null) {
		            			gv.drawRoundSet(result);
		            		}
		            	}
		            }
            	}
	            );
		//TODO : TextView
		currentState=stateInit;
	}
}
