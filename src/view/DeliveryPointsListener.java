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

	/**
	 * Create a listener with the given controller class
	 * @param controller
	 */
	public DeliveryPointsListener(Controller controller) {
		this.controller = controller;
	}

	/* (non-Javadoc)
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			ObservableMap<Object, Object> circleProp = ((Node) event.getSource()).getProperties();
			if (circleProp.get("DELIVERY") != null) {
				onClick((Delivery) circleProp.get("DELIVERY"));
			}
			else if(circleProp.get("WAREHOUSE") != null) {
				onClickWarehouse((Intersection) circleProp.get("WAREHOUSE"));
			}
			 else if (circleProp.get("INTERSECTION") != null) {
				onClick((Intersection) circleProp.get("INTERSECTION"));
			}
		}else if(event.getEventType() == MouseEvent.MOUSE_ENTERED) {
			ObservableMap<Object, Object> circleProp = ((Node) event.getSource()).getProperties();
			if (circleProp.get("INTERSECTION") != null) {
				 onHover((Intersection) circleProp.get("INTERSECTION"));
			}
		}
		else if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
			ObservableMap<Object, Object> circleProp = ((Node) event.getSource()).getProperties();
			if (circleProp.get("INTERSECTION") != null) {
				 onExit((Intersection) circleProp.get("INTERSECTION"));
			}
		}
	}

	/**
	 * @param d
	 */
	private void onClick(Delivery d) {
		controller.selectDelivery(d);
	}

	/**
	 * @param i
	 */
	private void onClick(Intersection i) {
		controller.selectIntersection(i);
	}
	
	/**
	 * @param i
	 */
	private void onClickWarehouse(Intersection i) {
		controller.selectWarehouse(i);
	}
	
	/**
	 * @param i
	 */
	private void onHover(Intersection i) {
		controller.hoverIntersection(i);
	}
	
	/**
	 * @param i
	 */
	private void onExit(Intersection i) {
		controller.exitIntersection(i);
	}
}
