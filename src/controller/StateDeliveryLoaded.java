package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import algo.Algorithms;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;
import xml.DeliveryRequestDeserializer;
import xml.ExceptionXML;

public class StateDeliveryLoaded extends StateDefault {

	/* (non-Javadoc)
	 * @see controller.StateDefault#setButtonsEnabled(view.MainView)
	 */
	@Override
	public void setButtonsEnabled(MainView mainView) {
		mainView.setAddButtonEnable(false);
		mainView.setComputeButtonEnable(true);
		mainView.setDeleteButtonEnable(false);
		mainView.setMapButtonEnable(true);
		mainView.setDeliveryButtonEnable(true);
		mainView.setCancelButtonEnable(false);
		mainView.setMoveButtonEnable(false);
		mainView.setStopButtonEnable(false);
		mainView.setUndoButtonEnable(false);
		mainView.setRedoButtonEnable(false);
		mainView.setDiscardButtonEnable(false);
		mainView.setExportButtonEnable(false);
		mainView.setDeliveryManEnable(true);
		mainView.setZoomAutoButtonsEnable(true);
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
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#roundsCompute(view.MainView, model.CityMap, model.DeliveryRequest, int, model.RoundSet, controller.ListCommands)
	 */
	@Override
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest request, int nbDeliveryMan,
			RoundSet roundSet, ListCommands listeDeCdes) {

		listeDeCdes.reset();

		RoundSet roundsTemp = new RoundSet();
		roundsTemp.setDepartureTime(request.getStartTime());
	
		Thread calculate = new Thread(() -> {
			try {
				Algorithms.solveTSP(roundsTemp, map, request, nbDeliveryMan);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		Thread display = new Thread(() -> {
			Platform.runLater(() -> {
				mainView.setLoader(true);
				mainView.printRoundSet(map, roundSet);
				mainView.printPotentielDeliveries(map, request);
			});

			double duration = roundsTemp.getDuration();
			while (calculate.isAlive()) {
				if (roundsTemp.getDuration() < duration || duration == 0.0) {
					duration = roundsTemp.getDuration();
					Platform.runLater(() -> {
						roundSet.copy(roundsTemp);
					});
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (roundsTemp.getDuration() < duration || duration == 0.0) {
				duration = roundsTemp.getDuration();
				Platform.runLater(() -> {
					roundSet.copy(roundsTemp);
				});
			}			
			if(duration==0 ) {
				Platform.runLater(() -> {
					roundSet.reset();
					mainView.setLoader(false);
					mainView.displayMessage("Unable to calculate rounds", "This delivery request contains unreachable delivery point");
					mainView.printMessage("Please load another delivery request and try again..");
				});
				Controller.stateDeliveryLoaded.setButtonsEnabled(mainView);
				Controller.setCurrentState(Controller.stateDeliveryLoaded);
			}
			else {
				Platform.runLater(()->{
					mainView.setLoader(false);
					mainView.printMessage("Press Add to add a delivery or Select a delivery to delete or move it.");
				});
        		Controller.stateRoundCalculated.setButtonsEnabled(mainView);
				Controller.setCurrentState(Controller.stateRoundCalculated);
			}

		});
		Platform.runLater(()->mainView.printMessage("Calculating..."));
		Controller.stateRoundCalculating.actionCalculate(calculate, display);
		Controller.stateRoundCalculating.setButtonsEnabled(mainView);
		Controller.setCurrentState(Controller.stateRoundCalculating);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#refreshView(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void refreshView(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet) {
		mainView.printCityMap(map);
		mainView.printDeliveryRequest(map, request);
	}

	/* (non-Javadoc)
	 * @see controller.StateDefault#selectDelivery(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Delivery, controller.ListCommands)
	 */
	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet,
			Delivery delivery, ListCommands listeDeCdes) {
		mainView.setDeliverySelected(delivery);
	}
	
	
		

}
