package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import algo.ExceptionAlgo;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;

public class StateModify extends StateDefault {
	
	private Delivery deliverySelected;

	/* (non-Javadoc)
	 * @see controller.StateDefault#setButtonsEnabled(view.MainView)
	 */
	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
 		mainView.setComputeButtonEnable(false);
 		mainView.setDeleteButtonEnable(true);
 		mainView.setMapButtonEnable(false);
 		mainView.setDeliveryButtonEnable(false);
		mainView.setCancelButtonEnable(true);
		mainView.setMoveButtonEnable(true);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(false);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#loadDeliveryRequest(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void loadDeliveryRequest(MainView mainView, CityMap map, DeliveryRequest request, RoundSet result) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a delivery request");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		if (file != null) {
			int temp = Delivery.currentId;
			try {
				Delivery.currentId = 1;
				request.copy(DeliveryRequestDeserializer.Load(map, file));
				mainView.printDeliveryRequest(map, request);
				result.reset();
				mainView.printMessage("Delivery Request Loaded ! You can now choose the number of delivery men and compute the rounds.");
				mainView.showNotificationCheck("Delivery Request", "A delivery request has been loaded successfully !");
				Controller.stateDeliveryLoaded.setButtonsEnabled(mainView);
				Controller.setCurrentState(Controller.stateDeliveryLoaded);
			} catch (NumberFormatException | ParserConfigurationException | SAXException | IOException | ExceptionXML
					| ParseException e) {
				Delivery.currentId = temp;
				mainView.displayMessage("Unable to load delivery request",
						"Please choose a valid delivery request file. Make sure that all the delivery point's locations are available in the current loaded map !");
				e.printStackTrace();
			}
		}
		mainView.setZoomAutoButtonsEnable(true);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#refreshView(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void refreshView(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet) {
		mainView.printCityMap(map);
		mainView.printRoundSet(map, roundSet);
		mainView.setDeliverySelected(this.deliverySelected);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#selectDelivery(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Delivery, controller.ListCommands)
	 */
	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		mainView.setDeliverySelected(delivery);
		mainView.setRoundSelected(roundSet, deliverySelected, false);
		mainView.setRoundSelected(roundSet, delivery, true);
		Controller.stateModify.actionDeliverySelected(delivery);
		Controller.stateModify.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateModify);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#delete(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, controller.ListCommands)
	 */
	@Override
	public void delete(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet,
			ListCommands listeDeCdes) {
		try {
			listeDeCdes.ajoute(new ComDelete(map, request, roundSet, deliverySelected));
			mainView.printMessage("Delivery deleted ! Press Add to add a delivery or Select a delivery to delete or move it. ");
			mainView.showNotificationCheck("Delivery deleted", "The delivery point number " + deliverySelected.getId() + " has been deleted correctly");
		} catch (ExceptionAlgo e) {
			cancel(mainView, roundSet);	
			mainView.displayMessage(null, "Cannot delete this delivery!");
			return;
		}
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#cancel(view.MainView, model.RoundSet)
	 */
	@Override
	public void cancel(MainView mainView, RoundSet roundSet) {
		mainView.setRoundSelected(roundSet, deliverySelected, false);
		this.deliverySelected = null;
		mainView.setDeliverySelected(null);
		mainView.printMessage("Canceled ! Press Add to add a delivery or Select a delivery to delete or move it. ");
		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculated);
	}
	
	/* (non-Javadoc)
	 * @see controller.StateDefault#move(view.MainView)
	 */
	@Override
	public void move(MainView mainView) {
		mainView.printMessage("Please select the delivery you want to put before...");
		Controller.stateMove.actionDeliveriesSelected(this.deliverySelected);
		Controller.stateMove.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateMove);
	}
	
	/**Select a delivery
	 * @param delivery
	 */
	protected void actionDeliverySelected(Delivery delivery) {
		this.deliverySelected = delivery;
	}

}
