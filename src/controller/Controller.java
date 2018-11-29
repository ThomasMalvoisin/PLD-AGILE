package controller;

import org.junit.runner.Result;

import javafx.application.Platform;
import model.CityMap;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;

public class Controller{
	
	private CityMap map;
	private DeliveryRequest delivReq;
	private RoundSet result;
	private MainView mv;
	protected static State currentState;
	protected static final StateInit stateInit = new StateInit();
	protected static final StateMapLoaded stateMapLoaded = new StateMapLoaded();
	protected static final StateDeliveryLoaded stateDeliveryLoaded = new StateDeliveryLoaded();
	protected static final StateRoundCalculated stateRoundCalculated = new StateRoundCalculated();

	public Controller(MainView mv) {
		this.mv = mv;
		map = new CityMap();
		delivReq = new DeliveryRequest();
		result  = new RoundSet();
		currentState=stateInit;
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
	
	public void roundsCompute() {
		currentState.roundsCompute(mv, map, delivReq, result);
	}
	
	public void refreshView() {
		currentState.refreshView(mv, map, delivReq, result);
	}
}
