package controller;


import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public interface State {
	
	public void setButtonsEnabled(MainView mainView);
	
	public void loadMap(MainView mainView, CityMap cityMap, DeliveryRequest delivReq, RoundSet result);

	public void loadDeliveryRequest(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet result);

	public void roundsCompute(MainView mainView, CityMap map, DeliveryRequest delivReq, int nbDeliveryMan, RoundSet roundSet, ListCommands listeDeCdes);

	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet);

	public void selectDelivery(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet, Delivery delivery, ListCommands listeDeCdes);

	public void selectWarehouse(MainView mainView, CityMap map, DeliveryRequest deliveryRequest, RoundSet roundSet, Intersection i, ListCommands listeDeCdes);
	
	public void add(MainView mv);

	public void selectIntersection(MainView mainView, Intersection i);

	public void stopAlgo();

	public void delete(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet, ListCommands listeDeCdes);
	
	public void undo(ListCommands listeDeCdes);
	
	public void redo(ListCommands listeDeCdes);
	
	public void cancel(MainView mainView, RoundSet roundSet);
  
	public void discardChanges(ListCommands listeDeCdes);
	
	public void exportRoundSet(MainView mainView, RoundSet roundSet);

	public void move(MainView mainView);
	
	public void hoverIntersection(MainView mv,CityMap map,Intersection i) ;
	
	public void exitIntersection(MainView mv) ;
	
}
