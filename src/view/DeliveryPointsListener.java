package view;

import controller.Controller;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import model.Delivery;
import model.Intersection;

public class DeliveryPointsListener implements EventHandler<MouseEvent>{
	
	Controller controller;

	public DeliveryPointsListener(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(MouseEvent event) {
		if(event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			onClick((Delivery)(((Node) event.getSource()).getProperties().get("DELIVERY")));
		}
	}
	
	private void onClick(Delivery d) {
		System.out.println("Suuuuuuu");
		//TODO appel de méthodes du controller
	}
	
}
