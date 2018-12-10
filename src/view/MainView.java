package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
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
	
	@FXML
	VBox moveButton;
	
	@FXML
	VBox cancelButton;
	
	@FXML
	VBox stopButton;
	
	@FXML
	VBox undoButton;
	
	@FXML
	VBox redoButton;
	
	@FXML
	VBox discardButton;
	
	@FXML
	VBox exportButton;
	
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
	
	public void clickExport() {
		controller.export();
	}
	
	public void clickMove() {
		controller.buttonMove();
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
	
	public int displayPopUpWarehouse(int nbRounds, boolean next) {
		List<Integer> choices = new ArrayList<>();
		for(int i=0; i<nbRounds; i++) {
			choices.add(i+1);
		}
			ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
			dialog.getDialogPane().getButtonTypes().clear();
			ButtonType buttonOk ;
			if(next) {
				buttonOk = new ButtonType("Next", ButtonData.OK_DONE);
			}else {
				buttonOk = new ButtonType("Ok", ButtonData.OK_DONE);
			}
			ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			dialog.getDialogPane().getButtonTypes().addAll(buttonOk, buttonCancel);
			
			dialog.setTitle("Confirmation");
			dialog.setHeaderText(null);
			dialog.setContentText("Please choose the round id");
			Optional<Integer> result = dialog.showAndWait();
			
			if (result.isPresent()){
				return result.get();
			}
			return -1;
	}
	
	public int displayPopUpAdd(String header) {
		int duration=-1;
		TextInputDialog dialog = new TextInputDialog("60");
		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().clear();
	    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, buttonCancel);
		dialog.setTitle("Confirmation");
		dialog.setHeaderText(header);
		dialog.setContentText("Please enter the delivery duration (s)");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
				try{
					duration = Integer.parseInt(result.get());
				}catch(Exception e) {
					duration = displayPopUpAdd("Make sure you enter an integer");
				}
		}
		return duration;
	}
	
	public boolean displayPopUpConfirmation(String msg) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().clear();
	    alert.getButtonTypes().addAll(ButtonType.OK, buttonCancel);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    return true;
		} else {
		    return false;
		}
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
	
	public void setMoveButtonEnable(boolean b) {
		moveButton.setDisable(!b);
	}
	
	public void setCancelButtonEnable(boolean b) {
		cancelButton.setDisable(!b);
	}
	
	public void setStopButtonEnable(boolean b) {
		stopButton.setDisable(!b);
	}
	
	public void setUndoButtonEnable(boolean b) {
		undoButton.setDisable(!b);
	}
	
	public void setRedoButtonEnable(boolean b) {
		redoButton.setDisable(!b);
	}
	
	public void setDiscardButtonEnable(boolean b) {
		discardButton.setDisable(!b);
	}
	
	public void setExportButtonEnable(boolean b) {
		exportButton.setDisable(!b);
	}
}
