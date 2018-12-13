package controller;

import controller.ListCommands;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class Controller {
	
	private CityMap map;
	private DeliveryRequest request;
	private RoundSet result;
	private ListCommands listCdes;
	private MainView mainView;
	protected static State currentState;
	// Instances associated to each possible state of the controller
	protected static final StateInit stateInit = new StateInit();
	protected static final StateMapLoaded stateMapLoaded = new StateMapLoaded();
	protected static final StateDeliveryLoaded stateDeliveryLoaded = new StateDeliveryLoaded();
	protected static final StateRoundCalculating stateRoundCalculating = new StateRoundCalculating();
	protected static final StateRoundCalculated stateRoundCalculated = new StateRoundCalculated();
	protected static final StateAdd1 stateAdd1 = new StateAdd1();
	protected static final StateAdd2 stateAdd2 = new StateAdd2();
	protected static final StateModify stateModify = new StateModify();
	protected static final StateMove stateMove = new StateMove();

	/**
	 * Controller's constructor 
	 * @param mv 
	 * 		the view 
	 * 
	 */
	public Controller(MainView mv) {
		mainView = mv;
		map = new CityMap();
		request = new DeliveryRequest();
		result = new RoundSet();
		listCdes = new ListCommands();
		
		stateInit.setButtonsEnabled(mv);
		currentState = stateInit;
	}
	
	/**
	 * Defines the current state
	 * @param state
	 * 		state of the controller
	 */
	protected static void setCurrentState(State state) {
		currentState = state;
	}

	/**
	 * Method called when you want load the map
	 */
	public void loadMap() {
		currentState.loadMap(mainView, map, request, result);
	}

	/**
	 * Method called when you want load a delivery request 
	 */
	public void loadDeliveryRequest() {
		currentState.loadDeliveryRequest(mainView, map, request, result);
	}

	/**
	 * Method called when you want compute rounds
	 * @param nbDeliveryMan
	 * 
	 */
	public void roundsCompute(int nbDeliveryMan) {
		currentState.roundsCompute(mainView, map, request, nbDeliveryMan, result, listCdes);
	}

	/**
	 * Method called when you want to stop the algorithms
	 */
	public void stopAlgo() {
		currentState.stopAlgo();
	}
	
	/**
	 * Method called when you want to select the delivery passed in parameter
	 * @param delivery
	 * 		The delivery which will be selected
	 */
	public void selectDelivery(Delivery delivery) {
		currentState.selectDelivery(mainView, map, request, result, delivery, listCdes);
	}
	
	/**
	 * Method called when you want to select the warehouse as a delivery before a new one
	 * @param i
	 * 		Intersection
	 */
	public void selectWarehouse(Intersection i) {
		currentState.selectWarehouse(mainView, map, request, result, i, listCdes);
	}
	
	/**
	 * Method called when you want to select a intersection
	 * @param i
	 * 		Intersection that you want select 
	 */
	public void selectIntersection(Intersection i) {
		currentState.selectIntersection(mainView, i);
	}
	
	/**
	 * Method called when you hover a intersection
	 * @param i
	 * 		Intersection
	 */
	public void hoverIntersection(Intersection i) {
		currentState.hoverIntersection(mainView, map, i);
	}
	
	/**
	 * Method called when you exit a intersection,
	 * @param i
	 * 		Intersection
	 */
	public void exitIntersection(Intersection i) {
		currentState.exitIntersection(mainView);
	}
	
	/**
	 * Method called when you want to add a delivery
	 * 
	 */
	public void add() {
		currentState.add(mainView);
	}

	/**
	 * Method called when you want to delete the delivery which is selected before
	 */
	public void delete() {
		currentState.delete(mainView, map, request, result, listCdes);
	}
	
	/**
	 * Method called when you want to move a delivery in a other round
	 */
	public void move() {
		currentState.move(mainView);
	}
	
	/**
	 * Method called when you want to unselect the intersection
	 */
	public void cancel() {
		currentState.cancel(mainView, result);
	}
	
	/**
	 * Method called when you want to undo the last modification
	 */
	public void undo(){
		currentState.undo(listCdes, mainView);
	}
	
	/**
	 * Method called when you want to redo the modification undone
	 * 
	 */
	public void redo(){
		currentState.redo(listCdes, mainView);
	}
	
	/**
	 * Method called when you want to discard all changes since the last calculation
	 */
	public void discardChanges(){
		currentState.discardChanges(listCdes);
	}

	/**
	 * Method called when you want to export the roadmap in text field
	 */
	public void exportRoundSet() {
		currentState.exportRoundSet(mainView, result);
	}
	
	/**
	 * Method called when you want to refresh the view
	 */
	public void refreshView() {
		currentState.refreshView(mainView, map, request, result);
	}
}
