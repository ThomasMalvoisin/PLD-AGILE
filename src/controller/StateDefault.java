package controller;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class StateDefault implements State {

	/* (non-Javadoc)
	 * @see controller.State#setButtonsEnabled(view.MainView)
	 */
	@Override
	public void setButtonsEnabled(MainView mainView) {
	}

	/* (non-Javadoc)
	 * @see controller.State#loadMap(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void loadMap(MainView mainView, CityMap map, DeliveryRequest request, RoundSet result){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a map");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(" MAP XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		if (file != null) {
			try {
				map.copy(MapDeserializer.load(file));
				mainView.printCityMap(map);
				request.reset();
				result.reset();
				mainView.printMessage("Map Loaded ! Please load a delivery request or load another map.");
				mainView.showNotificationCheck("Map Loaded", "A map has been loaded successfully !");
				Controller.stateMapLoaded.setButtonsEnabled(mainView);
				Controller.setCurrentState(Controller.stateMapLoaded);
			} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
				mainView.displayMessage("Cannot load this map file","Please select a valid file");
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see controller.State#loadDeliveryRequest(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void loadDeliveryRequest(MainView mainView, CityMap map, DeliveryRequest request, RoundSet result){
	}

	/* (non-Javadoc)
	 * @see controller.State#roundsCompute(view.MainView, model.CityMap, model.DeliveryRequest, int, model.RoundSet, controller.ListCommands)
	 */
	@Override
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest request, int nbDeliveryMan, RoundSet roundSet, ListCommands listeDeCdes) {
	}

	/* (non-Javadoc)
	 * @see controller.State#refreshView(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet)
	 */
	@Override
	public void refreshView(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet) {
	}

	/* (non-Javadoc)
	 * @see controller.State#selectDelivery(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Delivery, controller.ListCommands)
	 */
	@Override
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet, Delivery delivery, ListCommands listeDeCdes) {		
	}
	
	/* (non-Javadoc)
	 * @see controller.State#selectWarehouse(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, model.Intersection, controller.ListCommands)
	 */
	@Override
	public void selectWarehouse(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet, Intersection i, ListCommands listeDeCdes) {
		
	}

	/* (non-Javadoc)
	 * @see controller.State#delete(view.MainView, model.CityMap, model.DeliveryRequest, model.RoundSet, controller.ListCommands)
	 */
	@Override
	public void delete(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet, ListCommands listeDeCdes) {
		
	}

	/* (non-Javadoc)
	 * @see controller.State#selectIntersection(view.MainView, model.Intersection)
	 */
	@Override
	public void selectIntersection(MainView mainView,Intersection i) {
	}

	/* (non-Javadoc)
	 * @see controller.State#add(view.MainView)
	 */
	@Override
	public void add(MainView mainView) {
	}

	/* (non-Javadoc)
	 * @see controller.State#cancel(view.MainView, model.RoundSet)
	 */
	@Override
	public void cancel(MainView mainView, RoundSet roundSet) {
	}
  
	/* (non-Javadoc)
	 * @see controller.State#exportRoundSet(view.MainView, model.RoundSet)
	 */
	@Override
	public void exportRoundSet(MainView mainView, RoundSet roundSet) {	
	}
  
	/* (non-Javadoc)
	 * @see controller.State#undo(controller.ListCommands, view.MainView)
	 */
	@Override
	public void undo(ListCommands listeDeCdes, MainView mainView) {
	}
	
	/* (non-Javadoc)
	 * @see controller.State#redo(controller.ListCommands, view.MainView)
	 */
	@Override		
	public void redo(ListCommands listeDeCdes, MainView mainView) {
	}
	
	/* (non-Javadoc)
	 * @see controller.State#discardChanges(controller.ListCommands)
	 */
	@Override
	public void discardChanges(ListCommands listeDeCdes) {
	}
	
	/* (non-Javadoc)
	 * @see controller.State#stopAlgo()
	 */
	@Override
	public void stopAlgo() {
	}

	/* (non-Javadoc)
	 * @see controller.State#move(view.MainView)
	 */
	@Override
	public void move(MainView mainView) {	
	}
	
	/* (non-Javadoc)
	 * @see controller.State#hoverIntersection(view.MainView, model.CityMap, model.Intersection)
	 */
	@Override
	public void hoverIntersection(MainView mainView, CityMap map, Intersection i) {	
	}
	
	/* (non-Javadoc)
	 * @see controller.State#exitIntersection(view.MainView)
	 */
	@Override
	public void exitIntersection(MainView mainView) {	
	}
}
