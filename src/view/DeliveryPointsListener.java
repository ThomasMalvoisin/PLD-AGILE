package view;

import controller.Controller;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import model.Delivery;
import model.Intersection;

public class DeliveryPointsListener implements EventHandler<MouseEvent> {

	Controller controller;

	public DeliveryPointsListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			ObservableMap<Object, Object> circleProp = ((Node) event.getSource()).getProperties();
			if (circleProp.get("DELIVERY") != null) {
				onClick((Delivery) circleProp.get("DELIVERY"));
			} else if (circleProp.get("INTERSECTION") != null) {
				onClick((Intersection) circleProp.get("INTERSECTION"));
			}
		}
	}

	private void onClick(Delivery d) {
		controller.selectDelivery(d);
	}

	private void onClick(Intersection i) {
		controller.selectIntersection(i);
	}
}
