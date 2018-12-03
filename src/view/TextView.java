package view;

import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;

public class TextView implements Observer{

	DeliveryPointsListener dpl;
	ButtonListener bl;
	
	DeliveryRequest dr;

	private VBox txtArea;

	public TextView(VBox txtArea) {

		this.txtArea = txtArea;
	}

	public void printDeliveryRequest(DeliveryRequest deliveryRequest) {
		clearDeliveryRequest();
		deliveryRequest.addObserver(this);
		this.dr=deliveryRequest;
		printWarehouse(deliveryRequest.getWarehouse());

		for (Delivery d : deliveryRequest.getRequestDeliveries()) {
			printDeliveryPoint(d);
		}

	}

	private void printDeliveryPoint(Delivery d) {
		Group group = new Group();

		String dlvP = d.getId() + " : Delivery at (" + d.getAdress().getLatitude() + " , "
				+ d.getAdress().getLongitude() + ")\n";

		Text t = new Text(dlvP);
		t.setFill(Color.RED);

		group.getChildren().add(t);
		group.getProperties().put("DELIVERY", d);
		group.addEventHandler(MouseEvent.ANY, dpl);
		txtArea.getChildren().add(group);

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
	
	public void setDeliverySelected(Delivery delivery) {
		for(Node n : txtArea.getChildren()) {
			if(delivery.equals(n.getProperties().get("DELIVERY"))) {
				Group group = (Group) n;
				//Button deleteButton = new Button("Delete");
				//deleteButton.setId("DELETE");
				//deleteButton.addEventHandler(ActionEvent.ACTION, bl);
				//group.getChildren().add(deleteButton);
				((Text) group.getChildren().get(0)).setFill(Color.BLUE);
				
			}else if(n.getProperties().get("DELIVERY")==null){
				((Text) n).setFill(Color.FORESTGREEN);
			}else {
				Group group = (Group) n;
				((Text) group.getChildren().get(0)).setFill(Color.RED);
			}
		}
	}

	public void setDeliveryPointsListener(DeliveryPointsListener dpl) {
		this.dpl = dpl;
	}
	
	public void setButtonListener(ButtonListener bl) {
		this.bl = bl;
	}
	
	@Override
	public void update(Observable o, Object a) {
		printDeliveryRequest(dr);
	}

}
