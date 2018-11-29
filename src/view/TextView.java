package view;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;

public class TextView {
	
	DeliveryPointsListener dpl;
	
	private TextFlow txtArea;
	
	public TextView(TextFlow txtArea) {
		
		this.txtArea = txtArea;
	}

	public void printDeliveryRequest(DeliveryRequest deliveryRequest) {
		clearDeliveryRequest();
		printWarehouse(deliveryRequest.getWarehouse());
		
		for(Delivery d : deliveryRequest.getRequestDeliveries()) {
			printDeliveryPoint(d);
		}
		
	}

	private void printDeliveryPoint(Delivery d) {
		String dlvP = "Delivery at (" + d.getAdress().getLatitude() + " , " + d.getAdress().getLongitude() + ")\n";
		
		Text t = new Text(dlvP);
		t.setFill(Color.RED);
		t.getProperties().put("DELIVERY", d);
		
		t.addEventHandler(MouseEvent.ANY, dpl);
		txtArea.getChildren().add(t);
		
	}

	private void printWarehouse(Intersection warehouse) {
		String dlvP = "Warehouse at (" + warehouse.getLatitude() + " , " + warehouse.getLongitude() + ")\n";
		
		Text t = new Text(dlvP);
		t.setFill(Color.FORESTGREEN);
		txtArea.getChildren().add(t);
		
	}

	public void clearDeliveryRequest() {
		txtArea.getChildren().clear();
	}
	
	public void setDeliveryPointsListener(DeliveryPointsListener dpl) {
		this.dpl = dpl;
	}
	
}
