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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
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
	
	@FXML
	VBox mapButton;
	@FXML
	MenuItem mapMenuButton;
	
	@FXML
	VBox deliveryButton;
	@FXML
	MenuItem deliveryMenuButton;
	
	@FXML
	VBox computeButton;
	@FXML
	MenuItem computeMenuButton;
	
	@FXML
	VBox addButton;
	
	@FXML
	VBox deleteButton;
	
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

		setMapButtonEnable(true);
		setDeliveryButtonEnable(false);
		setComputeButtonEnable(false);
		setAddButtonEnable(false);
		setDeleteButtonEnable(false);
	}
	
	public void clickLoadMap(){
		controller.loadMap();
	}
	
	public void buttonDelete(){
		controller.buttonDelete();
	}
	
	public void buttonCancel() {
		controller.buttonCancel();
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
	
	public void clickStopAlgo(){
		controller.stopAlgo();
	}
		
	public void undo(){
		controller.undo();
	}
	
	public void redo(){
		controller.redo();
	}
	
	public void discardChanges(){
		controller.discardChanges();
	}
	
	public void printCityMap(CityMap map) {
		tv.clearTextView();
		gv.drawCityMap(map);
	}

	public void printDeliveryRequest(CityMap map ,DeliveryRequest delivReq) {
		gv.drawDeliveryRequest(delivReq);
		tv.printDeliveryRequest(map, delivReq);
	}
	
	public void printPotentielDeliveries(CityMap map, DeliveryRequest deliveryRequest) {
		gv.drawIntersections(map, deliveryRequest);
	}

	public void printRoundSet(CityMap map ,RoundSet result) {
		gv.drawRoundSet(result);
		tv.printRoundSet(map, result);
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
	
	public void zoomIn() {
		gv.zoomIn();
	}
	
	public void zoomOut() {
		gv.zoomOut();
	}
	
	public void zoomAuto() {
		gv.zoomAuto();
	}
	
	public void setMapButtonEnable(boolean b) {
		mapButton.setDisable(!b);
		mapMenuButton.setDisable(!b);
	}
	
	public void setDeliveryButtonEnable(boolean b) {
		deliveryButton.setDisable(!b);
		deliveryMenuButton.setDisable(!b);
	}
	
	public void setComputeButtonEnable(boolean b) {
		computeButton.setDisable(!b);
		computeMenuButton.setDisable(!b);
	}
	
	public void setAddButtonEnable(boolean b) {
		addButton.setDisable(!b);
	}
	
	public void setDeleteButtonEnable(boolean b) {
		deleteButton.setDisable(!b);
	}
}
