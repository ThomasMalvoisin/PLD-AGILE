package controller;


import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public interface State {
	
	/**
	 * Define which button is enable in the current state
	 * @param mainView
	 */
	public void setButtonsEnabled(MainView mainView);
	
	
	/**
	 * Load the map
	 * @param mainView
	 * 		View
	 * @param cityMap
	 * 		City Map
	 * @param delivReq
	 * 		Delivery Request
	 * @param result
	 * 		Result
	 */
	public void loadMap(MainView mainView, CityMap cityMap, DeliveryRequest delivReq, RoundSet result);

	/**
	 * Load the delivery request
	 * @param mainView
	 * @param cityMap
	 * @param deliveryRequest
	 * @param result
	 */
	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet result);

	/**
	 * Compute Rounds 
	 * @param mainView
	 * @param map
	 * @param delivReq
	 * @param nbDeliveryMan
	 * @param roundSet
	 * @param listeDeCdes
	 * 		List of Command for undo/Redo
	 */
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan, RoundSet roundSet, ListCommands listeDeCdes);

	
	/**
	 * Refresh the view
	 * @param mainView
	 * @param cityMap
	 * @param deliveryRequest
	 * @param roundSet
	 */
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet);

	/**
	 * Select a delivery 
	 * @param mainView
	 * @param map
	 * @param deliveryRequest
	 * @param roundSet
	 * @param delivery
	 * @param listeDeCdes
	 */
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet, Delivery delivery, ListCommands listeDeCdes);

	/** 
	 * Select Warehouse 
	 * @param mainView
	 * @param map
	 * @param deliveryRequest
	 * @param roundSet
	 * @param i
	 * @param listeDeCdes
	 */
	public void selectWarehouse(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet, Intersection i, ListCommands listeDeCdes);
	
	/**
	 * Add a delivery
	 * @param mv
	 * 		Main Wiew
	 */
	public void add(MainView mv);
	
	/**
	 * Select a Intersection
	 * @param mainView
	 * @param i
	 * 		Intersection
	 */
	public void selectIntersection(MainView mainView, Intersection i);

	/**
	 * Stop the algorithm
	 */
	public void stopAlgo();

	/** 
	 * Delete a delivery
	 * @param mainView
	 * @param cityMap
	 * @param deliveryRequest
	 * @param roundSet
	 * @param listeDeCdes
	 */
	public void delete(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet, ListCommands listeDeCdes);
	
	/** 
	 * Undo modification
	 * @param listeDeCdes
	 * @param mainView
	 */
	public void undo(ListCommands listeDeCdes, MainView mainView);
	
	/**
	 * Redo modification undone
	 * @param listeDeCdes
	 * @param mainView
	 */
	public void redo(ListCommands listeDeCdes, MainView mainView);
	
	/** 
	 * Cancel the modification that it's do
	 * @param mainView
	 * @param roundSet
	 */
	public void cancel(MainView mainView, RoundSet roundSet);
  
	/**
	 * Discard all changes
	 * @param listeDeCdes
	 */
	public void discardChanges(ListCommands listeDeCdes);
	
	/**
	 * Export Road map 
	 * @param mainView
	 * @param roundSet
	 */
	public void exportRoundSet(MainView mainView, RoundSet roundSet);

	/**
	 * Move a delivery in a new round
	 * @param mainView
	 */
	public void move(MainView mainView);
	
	/**
	 * Bring out this intersection
	 * @param mv
	 * @param map
	 * @param i
	 */
	public void hoverIntersection(MainView mv,CityMap map,Intersection i) ;
	
	/** 
	 * Not bring out any intersection on the mainview
	 * @param mv
	 */
	public void exitIntersection(MainView mv) ;
	
}
