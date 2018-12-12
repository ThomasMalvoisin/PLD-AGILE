package controller;

import controller.ListCommands;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

/**
 * @author Samuel GOY
 *
 */
public class Controller {

	private CityMap map;
	private DeliveryRequest delivReq;
	private RoundSet result;
	
	private ListCommands listCdes;
	private MainView mv;
	protected static State currentState;
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
		this.mv = mv;
		map = new CityMap();
		delivReq = new DeliveryRequest();
		result = new RoundSet();
		listCdes = new ListCommands();
		
		stateInit.setButtonsEnabled(mv);
		currentState = stateInit;
	}

	
	/**
	 * Defined the current state
	 * @param state
	 * 		state of the controller
	 */
	protected static void setCurrentState(State state) {
		currentState = state;
	}

	/**
	 * Method call when you want load the map
	 */
	public void loadMap() {
		currentState.loadMap(mv, map, delivReq, result);
	}

	/**
	 * Method call when you want load a delivery request 
	 */
	public void loadDeliveryRequest() {
		currentState.loadDeliveryRequest(mv, map, delivReq, result);
	}

	
	/**
	 * Method call when you want compute rounds
	 * @param nbDeliveryMan
	 * 
	 */
	public void roundsCompute(int nbDeliveryMan) {
		currentState.roundsCompute(mv, map, delivReq, nbDeliveryMan, result, listCdes);
	}

	/**
	 * Method call when you want to refresh the view
	 */
	public void refreshView() {
		currentState.refreshView(mv, map, delivReq, result);
	}

	
	/**
	 * Method call when you want to add a delivery
	 * 
	 */
	public void add() {
		currentState.add(mv);
	}

	/**
	 * Method call when you want to select the delivery passed in parameter
	 * @param delivery
	 * 		The delivery which will be selected
	 */
	public void selectDelivery(Delivery delivery) {
		currentState.selectDelivery(mv, map, delivReq, result, delivery, listCdes);
	}

	/**
	 * Method call when you want to delete the delivery which is selected before
	 */
	public void buttonDelete() {
		currentState.delete(mv, map, delivReq, result, listCdes);
	}

	/**
	 * Method call when you want to select a intersection
	 * @param i
	 * 		Intersection that you want select 
	 */
	public void selectIntersection(Intersection i) {
		currentState.selectIntersection(mv, i);
	}
	
	/**
	 * Method call when you want to stop the algorithms
	 */
	public void stopAlgo() {
		currentState.stopAlgo();
	}
	
	/**
	 * Method call when you want to undo the last modification
	 */
	public void undo(){
		currentState.undo(listCdes, mv);
	}

	
	/**
	 * Method call when you want to redo the modification undone
	 * 
	 */
	public void redo(){
		currentState.redo(listCdes, mv);
	}
	
	/**
	 * Method call when you want to discard all changes since the last calculation
	 */
	public void discardChanges(){
		currentState.discardChanges(listCdes);
	}
	

	/**
	 * Method call when you want to unselect the intersection
	 */
	public void buttonCancel() {
		currentState.cancel(mv, result);
	}
	
	/**
	 * Method call when you want move a delivery in a other round
	 */
	public void buttonMove() {
		currentState.move(mv);
	}

	
	/**
	 * Method call when you want to select the warehouse as a delivery before a new one
	 * @param i
	 * 		Intersection
	 */
	public void selectWarehouse(Intersection i) {
		currentState.selectWarehouse(mv, map, delivReq, result, i, listCdes);
  }

	/**
	 * Method call when you want export the roadmap in text field
	 */
	public void export() {
		currentState.exportRoundSet(mv, result);
	}
	
	/**
	 * Method call when you over a intersection
	 * @param i
	 * 		Intersection
	 */
	public void hoverIntersection(Intersection i) {
		currentState.hoverIntersection(mv, map, i);
	}
	
	
	/**
	 * Method call when you exit a intersectio,
	 * @param i
	 * 		Intersection
	 */
	public void exitIntersection(Intersection i) {
		currentState.exitIntersection(mv);
	}

}
