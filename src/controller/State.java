package controller;

import model.CityMap;
import model.DeliveryRequest;
import model.RoundSet;
import view.MainView;

public interface State {
	
	public void loadMap(MainView mainView, CityMap cityMap);
	
	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest);
	
	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, RoundSet roundSet);

	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet);
	
}