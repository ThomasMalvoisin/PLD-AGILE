package controller;

import javafx.application.Platform;
import model.CityMap;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;

public class Controller{
	
	CityMap map;
	DeliveryRequest delivReq;
	RoundSet result;
	MainView mv;
	protected static State currentState;
	protected static final StateInit stateInit = new StateInit();
	protected static final StateMapLoaded stateMapLoaded = new StateMapLoaded();
	protected static final StateDeliveryLoaded stateDeliveryLoaded = new StateDeliveryLoaded();
	protected static final StateRoundCalculated stateRoundCalculated = new StateRoundCalculated();

	public Controller(MainView mv) {
		this.mv = mv;
		currentState=stateInit;
	}
	protected static void setCurrentState(State state) {
		currentState = state;
	}

	public void loadMap() {
		try {
			CityMap mapTemp=currentState.loadMap();
			if(mapTemp!=null) {
				delivReq = null;
				result=null;
				map = mapTemp;
				mv.printCityMap(map);
			}
		} catch (Exception e) {
			//TODO : informer l'utilisateur que le fichier n'a pas pu être chargé
			e.printStackTrace();
		}
	}

	public void loadDeliveryRequest() {
		try {
			DeliveryRequest delivReqTemp = currentState.loadDeliveryRequest(map);
			if(delivReqTemp!=null) {
				delivReq=delivReqTemp;
				result=null;
				mv.printDeliveryRequest(delivReq);
			}
			
			//TODO : delivReq.addObserver(gv);
		} catch (Exception e) {
			//TODO : informer l'utilisateur que le fichier n'a pas pu être chargé
			e.printStackTrace();
		}
	}
	
	public void roundsCompute() {
		 new Thread(() -> {
		try {
			//System.out.println(delivReq.getStartTime());
			 Platform.runLater(() -> {
				 mv.setLoader(true);
	            });
			RoundSet tempResult = currentState.roundsCompute(map, delivReq);
			if(tempResult != null) {
				result = tempResult;
				 Platform.runLater(() -> {
					 mv.printRoundSet(result);
		            });
			}
			Platform.runLater(() -> {
				mv.setLoader(false);
	            });
			//System.out.println(result.getRounds().get(0).getDuration());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 }).start();
	}
	
	public void refreshView() {
		if(map!=null) {
			mv.printCityMap(map);
        	if(delivReq!=null) {
        		mv.printDeliveryRequest(delivReq);
        		if(result!=null) {
        			mv.printRoundSet(result);
        		}
        	}
        }
	}
}
