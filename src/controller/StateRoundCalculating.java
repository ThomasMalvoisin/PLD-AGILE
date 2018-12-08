package controller;

import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.RoundSet;
import view.MainView;

public class StateRoundCalculating extends StateDefault {
	Thread calculate;
	Thread display;

	@Override
	public void stopAlgo() {
		calculate.stop();
	}
	
	@Override
	public void refreshView(MainView mainView, CityMap cityMap, DeliveryRequest deliveryRequest, RoundSet roundSet) {
		mainView.printCityMap(cityMap);
		mainView.printDeliveryRequest(cityMap, deliveryRequest);

	}
	
	protected void actionCalculate(Thread calculate, Thread display) {
		this.calculate = calculate;
		this.display = display;
		this.calculate.start();
		this.display.start();
	}
}
