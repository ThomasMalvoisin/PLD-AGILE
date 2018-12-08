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
	private DeliveryRequest delivReq;
	private RoundSet result;
	
	private ListCommands listCdes;
	private MainView mv;
	protected static State currentState;
	protected static final StateInit stateInit = new StateInit();
	protected static final StateMapLoaded stateMapLoaded = new StateMapLoaded();
	protected static final StateDeliveryLoaded stateDeliveryLoaded = new StateDeliveryLoaded();
	protected static final StateRoundCalculated stateRoundCalculated = new StateRoundCalculated();
	protected static final StateAdd1 stateAdd1 = new StateAdd1();
	protected static final StateAdd2 stateAdd2 = new StateAdd2();
	protected static final StateModify stateModify = new StateModify();

	public Controller(MainView mv) {
		this.mv = mv;
		map = new CityMap();
		delivReq = new DeliveryRequest();
		result = new RoundSet();
		listCdes = new ListCommands();
		currentState = stateInit;
	}

	protected static void setCurrentState(State state) {
		currentState = state;
	}

	public void loadMap() {
		currentState.loadMap(mv, map);
	}

	public void loadDeliveryRequest() {
		currentState.loadDeliveryRequest(mv, map, delivReq);
	}

	public void roundsCompute(int nbDeliveryMan) {
		currentState.roundsCompute(mv, map, delivReq, nbDeliveryMan, result, listCdes);
	}

	public void refreshView() {
		currentState.refreshView(mv, map, delivReq, result);
	}

	public void add() {
		currentState.add(mv);
	}

	public void selectDelivery(Delivery delivery) {
		currentState.selectDelivery(mv, map, delivReq, result, delivery, listCdes);
	}

	public void buttonDelete() {
		currentState.delete(mv, map, delivReq, result, listCdes);
	}

	public void selectIntersection(Intersection i) {
		currentState.selectIntersection(mv, i);
	}
	

	public void undo(){
		currentState.undo(listCdes);
	}

	public void redo(){
		currentState.redo(listCdes);
	}
	
	public void discardChanges(){
		currentState.discardChanges(listCdes);
	}
	

	public void buttonCancel() {
		currentState.cancel(mv);
	}

}
