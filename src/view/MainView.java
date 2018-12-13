package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;

public class MainView implements Initializable {

	@FXML
	Pane pane;

	@FXML
	VBox txtArea;

	@FXML
	VBox loader;

	@FXML
	TextField infoBar;

	@FXML
	VBox deliveryMan;
	@FXML
	ComboBox<Integer> deliveryManCombo;

	@FXML
	VBox mapButton;
	@FXML
	ImageView addMapImage;

	@FXML
	VBox deliveryButton;
	@FXML
	ImageView addDeliveryImage;

	@FXML
	VBox computeButton;
	@FXML
	ImageView computeImage;

	@FXML
	VBox addButton;
	@FXML
	ImageView addImage;

	@FXML
	VBox deleteButton;
	@FXML
	ImageView deleteImage;

	@FXML
	VBox moveButton;
	@FXML
	ImageView moveImage;

	@FXML
	VBox cancelButton;
	@FXML
	ImageView cancelImage;

	@FXML
	VBox stopButton;
	@FXML
	ImageView stopImage;

	@FXML
	VBox undoButton;
	@FXML
	ImageView undoImage;

	@FXML
	VBox redoButton;
	@FXML
	ImageView redoImage;

	@FXML
	VBox discardButton;
	@FXML
	ImageView discardImage;

	@FXML
	VBox exportButton;
	@FXML
	ImageView exportImage;
	
	@FXML
	VBox zoomAutoButton;
	@FXML
	ImageView zoomAutoImage;

	Controller controller;
	ResizeListener rl;
	DeliveryPointsListener dpl;

	GraphicView gv;
	TextView tv;

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@SuppressWarnings("unchecked")
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
		for (int i = 1; i < 11; i++) {
			values.add(i);
		}
		deliveryManCombo.getItems().addAll(values);
		deliveryManCombo.setValue(1);

	}

	/**
	 * This method is called once the fxml file is fully loaded and displayed
	 * This method is used to initialize the key listeners on ctrl+Z, ctrl+ Y and right click
	 * @param scene
	 */
	public void postInitialize(Scene scene) {

		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

			if (event.getCode() == KeyCode.Z && event.isControlDown()) {
				controller.undo();

			}

			if (event.getCode() == KeyCode.Y && event.isControlDown()) {
				controller.redo();

			}
		});
		
		scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
			controller.stopAlgo();
		});

		pane.setOnMouseClicked(event -> {

			if (event.getButton() == MouseButton.SECONDARY) {
				buttonCancel();
			}
		});
	}

	/**
	 * This method is called when the user click on the Load Map Button
	 */
	public void clickLoadMap() {
		controller.loadMap();
	}

	/**
	 * This method is called when the user click on the Delete Button
	 */
	public void buttonDelete() {
		controller.delete();
	}

	/**
	 * This method is called when the user click on the Cancel Button
	 */
	public void buttonCancel() {
		controller.cancel();
	}

	/**
	 * This method is called when the user click on the Delivery Request Loading Button
	 */
	public void clickLoadDeliveryRequest() {
		controller.loadDeliveryRequest();
	}

	/**
	 * This method is called when the user click on the Round Compute Button
	 */
	public void clickRoundsCompute() {
		int nbDeliveryMan = deliveryManCombo.getValue();
		controller.roundsCompute(nbDeliveryMan);
	}

	/**
	 * This method is called when the user click on the Add Delivery Button
	 */
	public void clickAddDelivery() {
		controller.add();
	}

	/**
	 * This method is called when the user click on the Stop Compute Button
	 */
	public void clickStopAlgo() {
		controller.stopAlgo();
	}

	/**
	 * This method is called when the user click on the Undo Button
	 */
	public void undo() {
		controller.undo();
	}

	/**
	 * This method is called when the user click on the Redo Button
	 */
	public void redo() {
		controller.redo();
	}

	/**
	 * This method is called when the user click on the discard Button
	 */
	public void discardChanges() {
		controller.discardChanges();
	}

	/**
	 * This method is called when the user click on the Export Button
	 */
	public void clickExport() {
		controller.exportRoundSet();
	}

	/**
	 * This method is called when the user click on the Move Button
	 */
	public void clickMove() {
		controller.move();
	}

	/**
	 * Display city map on text and graphic view
	 * @param map
	 */
	public void printCityMap(CityMap map) {
		tv.clearTextView();
		gv.drawCityMap(map);
	}

	/**
	 * Display delivery request on graphic and text view
	 * @param map
	 * @param delivReq
	 */
	public void printDeliveryRequest(CityMap map, DeliveryRequest delivReq) {
		gv.drawDeliveryRequest(delivReq);
		tv.printDeliveryRequest(map, delivReq);
	}

	/**
	 * Draw intersections on graphic view that are visible when the user hover the points
	 * @param map
	 * @param deliveryRequest
	 */
	public void printPotentielDeliveries(CityMap map, DeliveryRequest deliveryRequest) {
		gv.drawIntersections(map, deliveryRequest);
	}

	/**
	 * Display the computed round set on the graphic and text view
	 * @param map
	 * @param result
	 */
	public void printRoundSet(CityMap map, RoundSet result) {
		gv.drawRoundSet(result);
		tv.printRoundSet(map, result);
	}

	/**
	 * Display the loader for in progress computing
	 * @param b
	 */
	public void setLoader(boolean b) {
		if(b) {
			loader.setVisible(true);
			loader.toFront();
		}
		else {
			loader.setVisible(false);
			computeButton.toFront();
		}
	}

	/**
	 * Display highlighted delivery on text and graphic view
	 * @param delivery
	 */
	public void setDeliverySelected(Delivery delivery) {
		gv.setDeliverySelected(delivery);
		tv.setDeliverySelected(delivery);
	}

	/**
	 * Display highlighted intersection on text and graphic view
	 * @param intersection
	 */
	public void setIntersectionSelected(Intersection intersection) {
		gv.setIntersectionSelected(intersection);
	}
	
	/**
	 * Display the given round as selected
	 * @param roundSet
	 * @param delivery
	 * @param flag
	 */
	public void setRoundSelected(RoundSet roundSet, Delivery delivery, boolean flag) {
		gv.setRoundSelected(roundSet, delivery, flag);
	}
	
	/**
	 * Display feedback notification
	 * @param title
	 * @param text
	 */
	public void showNotificationCheck(String title, String text) {
		Image img = new Image("/images/checked.png");
		Notifications.create().darkStyle().title(title).text(text).graphic(new ImageView(img)).hideAfter(Duration.seconds(3))
		.position(Pos.TOP_RIGHT).owner(this.pane).show();
	}
	

	/**
	 * Display a message in the bottom info bar
	 * @param string
	 */
	public void printMessage(String string) {
		infoBar.setText(string);
	}

	/**
	 * Create a blocking warning popup to inform user
	 * @param header
	 * @param content
	 */
	public void displayMessage(String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();

	}

	/**
	 * Display popup asking for a roundSet to choose
	 * @param nbRounds
	 * @param next
	 * @return
	 */
	public int displayPopUpWarehouse(int nbRounds, boolean next) {
		List<Integer> choices = new ArrayList<>();
		for (int i = 0; i < nbRounds; i++) {
			choices.add(i + 1);
		}
		ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
		dialog.getDialogPane().getButtonTypes().clear();
		ButtonType buttonOk;
		if (next) {
			buttonOk = new ButtonType("Next", ButtonData.OK_DONE);
		} else {
			buttonOk = new ButtonType("Ok", ButtonData.OK_DONE);
		}
		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(buttonOk, buttonCancel);

		dialog.setTitle("Confirmation");
		dialog.setHeaderText(null);
		dialog.setContentText("Please choose the round id");
		Optional<Integer> result = dialog.showAndWait();

		if (result.isPresent()) {
			return result.get();
		}
		return -1;
	}

	/**
	 * Display popup add asking delivery duration
	 * @param header
	 * @return
	 */
	public int displayPopUpAdd(String header) {
		int duration = -1;
		TextInputDialog dialog = new TextInputDialog("60");
		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().clear();
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, buttonCancel);
		dialog.setTitle("Confirmation");
		dialog.setHeaderText(header);
		dialog.setContentText("Please enter the delivery duration (s)");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			try {
				duration = Integer.parseInt(result.get());
			} catch (Exception e) {
				duration = displayPopUpAdd("Make sure you enter an integer");
			}
		}
		return duration;
	}

	/**
	 * Display popup asking confirmation
	 * @param msg
	 * @return
	 */
	public boolean displayPopUpConfirmation(String msg) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.OK, buttonCancel);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Method called when user click on Auto Zoom Button
	 */
	public void zoomAuto() {
		gv.zoomAuto();
	}

	/**
	 * Enable click on MapButton if b is true else disable click and set the image with gray scale
	 * 
	 * @param b
	 */
	public void setMapButtonEnable(boolean b) {
		mapButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			addMapImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			addMapImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on DeliveryButton if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setDeliveryButtonEnable(boolean b) {
		deliveryButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			addDeliveryImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			addDeliveryImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Compute Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setComputeButtonEnable(boolean b) {
		computeButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			computeImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			computeImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Add Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setAddButtonEnable(boolean b) {
		addButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			addImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			addImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Delete Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setDeleteButtonEnable(boolean b) {
		deleteButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			deleteImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			deleteImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Move Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setMoveButtonEnable(boolean b) {
		moveButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			moveImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			moveImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Cancel Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setCancelButtonEnable(boolean b) {
		cancelButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			cancelImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			cancelImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Stop Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setStopButtonEnable(boolean b) {
		stopButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			stopImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			stopImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Undo Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setUndoButtonEnable(boolean b) {
		undoButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			undoImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			undoImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Redo Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setRedoButtonEnable(boolean b) {
		redoButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			redoImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			redoImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Discard Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setDiscardButtonEnable(boolean b) {
		discardButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			discardImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			discardImage.setEffect(desaturate);
		}
	}
  
	/**
	 * Enable click on Export Button if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setExportButtonEnable(boolean b) {
		exportButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			exportImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			exportImage.setEffect(desaturate);
		}
	}
	
	/**
	 * Enable click on Zoom Auto if b is true else disable click and set the image with gray scale
	 * @param b
	 */
	public void setZoomAutoButtonsEnable(boolean b) {
		zoomAutoButton.setDisable(!b);

		if (b) {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(0);
			zoomAutoImage.setEffect(desaturate);
		} else {
			ColorAdjust desaturate = new ColorAdjust();
			desaturate.setSaturation(-1);
			zoomAutoImage.setEffect(desaturate);
		}
	}

	/**
	 * Enable click on Delivery Man selection combo box if b is true else disable click
	 * @param b
	 */
	public void setDeliveryManEnable(boolean b) {
		deliveryMan.setDisable(!b);

	}

}
