package controller;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public interface State {
	
	public void loadMap(MainView mainView, CityMap cityMap);

	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest);

	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan, RoundSet roundSet);

	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet);

	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet, Delivery delivery);

	public void add(MainView mv);

	public void selectIntersection(MainView mainView, Intersection i);

	public void delete(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet);
	
}
