package view;

import java.net.URL;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import model.CityMap;
import model.DeliveryRequest;
import model.RoundSet;


public class MainView implements Initializable{

	@FXML
	Pane pane;
	
	@FXML
	TextFlow txtArea;
	
	@FXML 
	VBox loader;
	
	Controller controller;
	ResizeListener rl;
	DeliveryPointsListener dpl;
	
	GraphicView gv;
	TextView tv;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		controller = new Controller(this);
		gv = new GraphicView(pane);
		tv = new TextView(txtArea);
		
		rl = new ResizeListener(controller);
		dpl = new DeliveryPointsListener(controller);
		
		gv.setDeliveryPointsListener(dpl);
		tv.setDeliveryPointsListener(dpl);
		
		pane.widthProperty().addListener(rl);
		pane.heightProperty().addListener(rl);
		
	}
	
	public void clickLoadMap(){
		controller.loadMap();
	}
	
	public void clickLoadDeliveryRequest(){
		controller.loadDeliveryRequest();
	}
	
	public void clickRoundsCompute(){
		controller.roundsCompute();
	}

	public void printCityMap(CityMap map) {
		gv.drawCityMap(map);
	}

	public void printDeliveryRequest(DeliveryRequest delivReq) {
		gv.drawDeliveryRequest(delivReq);
		tv.printDeliveryRequest(delivReq);
	}

	public void printRoundSet(RoundSet result) {
		gv.drawRoundSet(result);
	}
	
	public void setLoader(boolean toBePrinted) {
		if(toBePrinted) loader.toFront();
		else loader.toBack();
	}
}
