package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;


public class MainView implements Initializable{

	@FXML
	Pane pane;
	
	@FXML
	VBox txtArea;
	
	@FXML 
	VBox loader;
	
	@FXML
	TextField infoBar;
  
	@FXML
	ComboBox<Integer> deliveryManCombo;
	
	Controller controller;
	ResizeListener rl;
	DeliveryPointsListener dpl;
	ButtonListener bl;
	
	GraphicView gv;
	TextView tv;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		controller = new Controller(this);
		gv = new GraphicView(pane);
		tv = new TextView(txtArea);
		
		rl = new ResizeListener(controller);
		dpl = new DeliveryPointsListener(controller);
		bl = new ButtonListener(controller);
		
		gv.setDeliveryPointsListener(dpl);
		tv.setDeliveryPointsListener(dpl);
		tv.setButtonListener(bl);
		
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
	
	public void buttonDelete(){
		controller.buttonDelete();
	}
	
	public void clickLoadDeliveryRequest(){
		controller.loadDeliveryRequest();
	}
	
	public void clickRoundsCompute(){
		int nbDeliveryMan = deliveryManCombo.getValue();
		controller.roundsCompute(nbDeliveryMan);
	}
	
	public void clickAddDelivery(){
		controller.add();
	}
	
	public void printCityMap(CityMap map) {
		tv.clearDeliveryRequest();
		gv.drawCityMap(map);
	}

	public void printDeliveryRequest(DeliveryRequest delivReq) {
		gv.drawDeliveryRequest(delivReq);
		tv.printDeliveryRequest(delivReq);
	}
	
	public void printPotentielDeliveries(CityMap map, DeliveryRequest deliveryRequest) {
		gv.drawIntersections(map, deliveryRequest);
	}

	public void printRoundSet(RoundSet result) {
		gv.drawRoundSet(result);
	}
	
	public void setLoader(boolean toBePrinted) {
		if(toBePrinted) loader.toFront();
		else loader.toBack();
	}

	public void setDeliverySelected (Delivery delivery) {
		gv.setDeliverySelected(delivery);
		tv.setDeliverySelected(delivery);
	}
	
	public void setIntersectionSelected(Intersection intersection) {
		gv.setIntersectionSelected(intersection);
	}

	public void printMessage(String string) {
		infoBar.setText(string);
	}

	public void displayMessage(String header,String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
		
	}
}
