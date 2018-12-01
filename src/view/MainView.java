package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
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
	
	@FXML
	ComboBox<Integer> deliveryManCombo;
	
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
		
		Collection<Integer> values = new ArrayList<Integer>(10);
		for(int i = 1 ; i < 11 ; i++) {
			values.add(i);
		}
		deliveryManCombo.getItems().addAll(values);
		deliveryManCombo.setValue(1);
		
	}
	
	public void clickLoadMap(){
		controller.loadMap();
	}
	
	public void clickLoadDeliveryRequest(){
		controller.loadDeliveryRequest();
	}
	
	public void clickRoundsCompute(){
		//send delivery request with certain amount of delivery man
		int nbDeliveryMan = deliveryManCombo.getValue();
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

	public void displayMessage(String header,String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
		
	}
}
