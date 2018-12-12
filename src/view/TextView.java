package view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.border.TitledBorder;

import org.omg.CORBA.TIMEOUT;

import com.sun.javafx.scene.traversal.WeightedClosestCorner;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Round;
import model.RoundSet;
import model.Section;

public class TextView implements Observer{
	private DeliveryPointsListener dpl;
	private CityMap map;
	private RoundSet roundSet;
	private Color[] colors = { Color.ROYALBLUE, Color.BLACK, Color.ORANGE, Color.BROWN, Color.GREEN, Color.GOLD,
			Color.BLUEVIOLET, Color.YELLOW, Color.AQUAMARINE, Color.CORAL };
	private ArrayList<Text> deliveries;
	private Text selectedDelivery ;
	private VBox txtArea;
	
	private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	public TextView(VBox txtArea) {
		this.txtArea = txtArea;
		deliveries = new ArrayList<Text>();
	}

	public void printDeliveryRequest(CityMap map, DeliveryRequest deliveryRequest) {
		clearTextView();
		this.map = map;
		TitledPane titledPane = new TitledPane();
		titledPane.setCollapsible(false);
		VBox titledPaneContent = new VBox();
		addWarehouse(map, deliveryRequest.getWarehouse(),titledPaneContent);
		titledPaneContent.getChildren().add(new Separator());

		for (Delivery d : deliveryRequest.getRequestDeliveries()) {
			addDeliveryPoint(map, d,titledPaneContent,Color.RED);
			titledPaneContent.getChildren().add(new Separator());
		}
		
		titledPane.setContent(titledPaneContent);
		titledPane.setText("Delivery Request with "+ deliveryRequest.getNbDeliveries() + "  Delivery Points");
		txtArea.getChildren().add(titledPane);
	}
	
	public void printRoundSet(CityMap map, RoundSet roundSet) {
		clearTextView();
		roundSet.addObserver(this);
		this.roundSet=roundSet;
		this.map = map;
		
		ArrayList<Round> rounds = roundSet.getRounds();
		int r =  0;
		for(Round round : rounds) {
			if(r < 10 ) {
				TitledPane titledPane = new TitledPane();
				titledPane.setExpanded(true);
				VBox titledPaneContent = new VBox();
				int i = 0 ;
				for (Delivery d : round.getDeliveries()) {
					if(i != 0) {
						addDeliveryPoint(map, d,titledPaneContent,colors[r]);
						titledPaneContent.getChildren().add(new Separator());
					} else {
						if(r == 0) {
							VBox warehousePane = new VBox();
							Intersection warehouse = d.getAdress();
							addWarehouse(map, warehouse, warehousePane);
							
							String desc = "                      Departure : " + df.format(roundSet.getDepartureTime());
							
							desc +=  "\n                      ";
							desc += "Duration  : " + (int)roundSet.getDuration()/60000 + " min";
							desc +=  "\n                      ";
							desc += "Arrival   : " + df.format(roundSet.getArrivalTime());
							
							Text warehouseDescription = new Text(desc);
							warehouseDescription.setFill(Color.FORESTGREEN);
							warehousePane.getChildren().add(warehouseDescription);
							txtArea.getChildren().add(warehousePane);
						}
					}
					i++;
				}
				titledPane.setContent(titledPaneContent);
				titledPane.setText("Round "+(r+1)+", "+ (round.getDeliveries().size()-1) + "  Delivery Points");
				titledPane.setTextFill(colors[r]);
				txtArea.getChildren().add(titledPane);
			}
			r++;
		}
		
	}
		

	private void addDeliveryPoint(CityMap map, Delivery d,VBox titledPane,Color color) {
		
		ArrayList<String> sectionNames = map.getIntersectionSectionNames(d.getAdress());
		String dlvP = "\n Delivery " + d.getId() + " :\n" ;
		dlvP = dlvP +  "                      ";
		int i = 0;
		for(String name : sectionNames) {
			if(i != 0 ) {
				dlvP += " , ";
			}
			if(name.equals("")){
				name =  "No Name";
			}
			dlvP +=  name;
			i++;
		}
		if(d.getDepartureTime() != null && d.getArrivalTime() != null) {
			dlvP +=  "\n                      ";
			dlvP += "Arrival   : " + df.format(d.getArrivalTime());
			dlvP +=  "\n                      ";
			dlvP += "Duration  : " + d.getDuration()/60 + " min";
			dlvP +=  "\n                      ";
			dlvP += "Departure : " + df.format(d.getDepartureTime());
			
		}else {
			dlvP += "\n			";
			dlvP += "Duration : " + d.getDuration()/60 + " min";
		}
		Text t = new Text(dlvP);
		t.setFill(color);
		t.getProperties().put("DELIVERY", d);
		t.addEventHandler(MouseEvent.ANY, dpl);
		t.getStyleClass().add("delivery-text");
		deliveries.add(t);
		titledPane.getChildren().add(t);

	}

	
	
	private void addWarehouse(CityMap map, Intersection warehouse,VBox titledPane) {
		//String dlvP = "Warehouse :" + warehouse.getLatitude() + " , " + warehouse.getLongitude() + ")\n";
		ArrayList<String> sectionNames = map.getIntersectionSectionNames(warehouse);
		String dlvP = "\n Warehouse " + " :\n" ;
		dlvP = dlvP +  "                      ";
		int i = 0;
		for(String name : sectionNames) {
			if(i != 0 ) {
				dlvP += " , ";
			}
			if(name.equals("")){
				name =  "No Name";
			}
			dlvP +=  name;
			i++;
		}
		Text t = new Text(dlvP);
		//store this one ?
		t.setFill(Color.FORESTGREEN);
		titledPane.getChildren().add(t);
	}

	public void clearTextView() {
		txtArea.getChildren().clear();
		deliveries.clear();
		selectedDelivery = null;
	}
	
	public void setDeliverySelected(Delivery delivery) {
		if(selectedDelivery != null) {
			selectedDelivery.getStyleClass().remove("selected-delivery-text");
		}
		for (Text text : deliveries) {
			Delivery temp = (Delivery) text.getProperties().get("DELIVERY");
			if(temp.equals(delivery)){
				selectedDelivery = text;
				text.getStyleClass().add("selected-delivery-text");
			}
		}
	}
	
	public void setDeliveryPointsListener(DeliveryPointsListener dpl) {
		this.dpl = dpl;
	}

	@Override
	public void update(Observable o, Object a) {
		printRoundSet(map, roundSet);
	}

}
