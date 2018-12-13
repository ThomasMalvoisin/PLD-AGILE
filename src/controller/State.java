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
	 * @param request
	 * 		Delivery Request
	 * @param roundSet
	 * 		Result round set
	 */
	public void loadMap(MainView mainView, CityMap cityMap, DeliveryRequest request, RoundSet roundSet);

	/**
	 * Load the delivery request
	 * @param mainView
	 * @param cityMap
	 * @param request
	 * @param roundSet
	 */
	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest request, RoundSet roundSet);

	/**
	 * Compute Rounds 
	 * @param mainView
	 * @param map
	 * @param request
	 * @param nbDeliveryMan
	 * @param roundSet
	 * @param listeDeCdes
	 * 		List of Command for undo/Redo
	 */
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest request, int nbDeliveryMan, RoundSet roundSet, ListCommands listeDeCdes);

	/**
	 * Stop the algorithm
	 */
	public void stopAlgo();

	/**
	 * Bring out this intersection
	 * @param mainView
	 * @param map
	 * @param i
	 */
	public void hoverIntersection(MainView mainView,CityMap map,Intersection i) ;
	
	/** 
	 * Not bring out any intersection on the mainview
	 * @param mainView
	 */
	public void exitIntersection(MainView mainView) ;
	
	/**
	 * Select a delivery 
	 * @param mainView
	 * @param map
	 * @param request
	 * @param roundSet
	 * @param delivery
	 * @param listeDeCdes
	 */
	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet, Delivery delivery, ListCommands listeDeCdes);

	/** 
	 * Select Warehouse 
	 * @param mainView
	 * @param map
	 * @param request
	 * @param roundSet
	 * @param i
	 * @param listeDeCdes
	 */
	public void selectWarehouse(MainView mainView, CityMap map, DeliveryRequest request, RoundSet roundSet, Intersection i, ListCommands listeDeCdes);
	
	/**
	 * Select a Intersection
	 * @param mainView
	 * @param i
	 * 		Intersection
	 */
	
	public void selectIntersection(MainView mainView, Intersection i);
	/**
	 * Add a delivery
	 * @param mainView
	 * 		The Main View
	 */
	public void add(MainView mainView);

	/** 
	 * Delete a delivery
	 * @param mainView
	 * @param cityMap
	 * @param request
	 * @param roundSet
	 * @param listeDeCdes
	 */
	public void delete(MainView mainView, CityMap cityMap, DeliveryRequest request, RoundSet roundSet, ListCommands listeDeCdes);
	
	/**
	 * Move a delivery in a new round
	 * @param mainView
	 */
	public void move(MainView mainView);
	
	/** 
	 * Cancel the modification
	 * @param mainView
	 * @param roundSet
	 */
	public void cancel(MainView mainView, RoundSet roundSet);
	
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
	 * Refresh the view
	 * @param mainView
	 * @param cityMap
	 * @param request
	 * @param roundSet
	 */
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest request, RoundSet roundSet);
}
