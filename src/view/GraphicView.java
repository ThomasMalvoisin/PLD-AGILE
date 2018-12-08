package view;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Iterator;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Journey;
import model.Round;
import model.RoundSet;
import model.Section;

public class GraphicView implements Observer {

	Pane pane;
	
	CityMap map;
	DeliveryRequest dr;
	RoundSet rs;
	
	Group deliveries;
	Group roundSet;
	Group notDeliveriesIntersections;

	Color[] colors = { Color.ROYALBLUE, Color.BLACK, Color.ORANGE, Color.BROWN, Color.GREEN, Color.GOLD,
			Color.BLUEVIOLET, Color.YELLOW, Color.AQUAMARINE, Color.CORAL };

	DeliveryPointsListener dpl;

	public GraphicView(Pane pane) {
		this.pane = pane;
		this.deliveries = new Group();
		this.roundSet = new Group();
		this.notDeliveriesIntersections = new Group();
		
		pane.setOnScroll(event -> {
			zoom(event.getDeltaY()*0.1,event.getX(),event.getY());
	        
	    });
		
		
		pane.setOnMousePressed(event ->{
			double mouseXPos = event.getX();
			double mouseYPos = event.getY();
			
			initDrag(mouseXPos,mouseYPos);
		});
			
	}

	private void initDrag(double mouseXPos, double mouseYPos) {
		pane.setOnMouseDragged(event -> {

		    pane.setTranslateX(event.getX() + pane.getTranslateX() - mouseXPos);
		    pane.setTranslateY(event.getY() + pane.getTranslateY() - mouseYPos);
		    event.consume();
		});
	}
	
	public void clear() {
		pane.getChildren().clear();
	}

	public void clearDeliveryRequest() {
		deliveries.getChildren().clear();
		pane.getChildren().remove(deliveries);
		roundSet.getChildren().clear();
		pane.getChildren().remove(roundSet);
		notDeliveriesIntersections.getChildren().clear();
		pane.getChildren().remove(notDeliveriesIntersections);
	}

	public void drawCityMap(CityMap cityMap) {
		clear();
		this.map = cityMap;
		for (Section section : cityMap.getSections()) {
			drawSection(section);
		}
	}

	public void drawIntersections(CityMap cityMap, DeliveryRequest dr) {
		Map<Long, Intersection> listPotentialDeliveriesIntersections = cityMap.getNotDeliveriesIntersections(dr);
		for (Entry<Long, Intersection> entry : listPotentialDeliveriesIntersections.entrySet()) {
			drawIntersectionPoint(entry.getValue());
		}
		pane.getChildren().remove(deliveries);
		pane.getChildren().add(notDeliveriesIntersections);
		pane.getChildren().add(deliveries);

	}

	public void drawRoundSet(RoundSet rs) {
		drawDeliveryRequest(dr);
		ArrayList<Section> listeSection = new ArrayList<>();
		rs.addObserver(this);
		this.rs = rs;
		int i = 0;
		double delta = 0.0;
		for (Round r : rs.getRounds()) {
			double opacity = 1.0;
			int nbJourneys = r.getJourneys().size();
			for (Journey j : r.getJourneys()) {

				for (Section s : j.getSectionList()) {
					// delta=10.0/(Collections.frequency(listeSection, s));
					System.out.println(s);
					System.out.println(s.getInverse());
//					delta = 3.5 * (Collections.frequency(listeSection, s)
//							+ Collections.frequency(listeSection, s.getInverse()));
					// delta=3.5*(Collections.frequency(listeSection, s));
					// ATTENTION IL FAUT METTRE UN IF SUR I SINON INDEX OUT OUF BOUNDS SI BCP DE ROUNDS !!!
					drawRoundSection(s, colors[i], delta, opacity);
					listeSection.add(s);
				}
//				opacity = opacity - 0.75 / nbJourneys;
				System.out.println(opacity);
				

			}
			i++;
		}
		pane.getChildren().add(roundSet);
		pane.getChildren().remove(deliveries);
		pane.getChildren().add(deliveries);
	}

	public void drawDeliveryRequest(DeliveryRequest deliveryRequest) {
		clearDeliveryRequest();
		deliveryRequest.addObserver(this);
		this.dr = deliveryRequest;
		drawWarehousePoint(deliveryRequest.getWarehouse());

		for (Delivery d : deliveryRequest.getRequestDeliveries()) {
			drawDeliveryPoint(d);
		}
		pane.getChildren().add(deliveries);
	}

	private void drawRoundSection(Section sec, Color color, double delta, double opacity) {
		Line l = drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()), 3, color, delta, opacity);
		roundSet.getChildren().add(l);
	}

	private void drawSection(Section sec) {
		Line l = drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()), 1, Color.WHITE, 0, 1);
		pane.getChildren().add(l);
	}

	private double[] geoToCoord(Intersection i) {
		double[] result = new double[2];
		Bounds bounds = pane.getBoundsInLocal();
		result[0] = ((i.getLongitude() - map.getLongitudeMin()) * bounds.getMaxX())
				/ (map.getLongitudeMax() - map.getLongitudeMin());
		// result[1] = ((i.getLatitude() - map.latitudeMin) *
		// bounds.getMaxY())/(map.latitudeMax-map.latitudeMin);
		result[1] = ((-i.getLatitude() + map.getLatitudeMax()) * bounds.getMaxY())
				/ (map.getLatitudeMax() - map.getLatitudeMin());
		return result;
	}

	private Line drawLine(double[] departure, double[] arrival, double width, Paint p, double delta, double opacity) {
		// GraphicsContext gc = canvas.getGraphicsContext2D();
		// gc.strokeLine(departure[0], departure[1], arrival[0], arrival[1]);

		double x = 0;
		double y = 0;
		if (delta != 0) {
			if ((arrival[1] - departure[1]) != 0) {
				x = delta / Math.sqrt(1 + ((arrival[0] - departure[0]) / (arrival[1] - departure[1]))
						* ((arrival[0] - departure[0]) / (arrival[1] - departure[1])));
				y = Math.sqrt(delta * delta - x * x);
			} else {
				x = 0;
				y = delta;
			}
		}
		Line l = new Line(departure[0] - x, departure[1] - y, arrival[0] - x, arrival[1] - y);
		l.setOpacity(opacity);
		/*
		 * if(deltaY!=0) { l.getStrokeDashArray().addAll(deltaY); }
		 */

		l.setStroke(p);
		l.setStrokeWidth(width);

		return l;
	}

	private void drawIntersectionPoint(Intersection i) {
		Circle c = new Circle(geoToCoord(i)[0], geoToCoord(i)[1], 5);
		c.setFill(Color.WHITE);
		c.setOpacity(0);
		c.getStyleClass().add("map-point");
		notDeliveriesIntersections.getChildren().add(c);
		c.getProperties().put("INTERSECTION", i);
		c.addEventHandler(MouseEvent.ANY, dpl);
	}

	public void drawDeliveryPoint(Delivery d) {
//		drawPoint(geoToCoord(d.getAdress()),5, Color.RED, d);
		Circle c = makePoint(geoToCoord(d.getAdress()), 5, Color.RED);

		c.getProperties().put("DELIVERY", d);
		c.addEventHandler(MouseEvent.ANY, dpl);
	}

	public void drawWarehousePoint(Intersection i) {
//		drawPoint(geoToCoord(i),8, Color.FORESTGREEN, null);
		Circle c = makePoint(geoToCoord(i), 5, Color.FORESTGREEN);
	}

	private Circle makePoint(double[] point, double radius, Paint p) {
		Circle c = new Circle(point[0], point[1], radius);
		c.setFill(p);
		c.getStyleClass().add("map-point");
		deliveries.getChildren().add(c);
		return c;
	}

//	public void drawPoint(double[] point,double radius, Paint p, Delivery d) {
//		c.getProperties().put("INTERSECTION", i);
//
//		c.addEventHandler(MouseEvent.ANY, dpl);
//
//		deliveries.getChildren().add(c);
//	}

	public void setDeliverySelected(Delivery d) {
		for (Node n : deliveries.getChildren()) {
			if (d != null && d.equals(n.getProperties().get("DELIVERY"))) {
				((Circle) n).setFill(Color.AQUA);
				((Circle) n).setRadius(7);
			} else if (n.getProperties().get("DELIVERY") == null) {
				((Circle) n).setFill(Color.FORESTGREEN);
			} else {
				((Circle) n).setFill(Color.RED);
				((Circle) n).setRadius(5);
			}
		}
	}

	public void setIntersectionSelected(Intersection i) {
		for (Node n : notDeliveriesIntersections.getChildren()) {
			if (i != null && i.equals(n.getProperties().get("INTERSECTION"))) {
				System.out.println("Suu");
				((Circle) n).getStyleClass().add("map-point-selected");
//				((Circle) n).setFill(Color.AQUA);
//				((Circle) n).setOpacity(1);
			} else {
				((Circle) n).setFill(Color.WHITE);
				((Circle) n).setOpacity(0);
				((Circle) n).getStyleClass().add("map-point");
				((Circle) n).getStyleClass().remove("map-point-selected");
			}
		}
	}

	public void zoomIn() {
		
		pane.setScaleX(pane.getScaleX() + 0.4);
		pane.setScaleY(pane.getScaleY() + 0.4);
		
	}
	
	public void zoomOut() {
		
		if(pane.getScaleX() - 0.4 < 1) {
			
			return;
		}
		
		
		pane.setScaleX(pane.getScaleX() - 0.4);
		pane.setScaleY(pane.getScaleY() - 0.4);
	}
	
	public void zoomAuto() {
		pane.setScaleX(1);
		pane.setScaleY(1);
		pane.setTranslateX(0);
		pane.setTranslateY(0);
		
	}
	
	public void zoom(double zoomFactor, double xFocus, double yFocus) {
		
		if(pane.getScaleX() + zoomFactor < 1) {
			
			return;
		}
		pane.setScaleX(pane.getScaleX() + zoomFactor);
		pane.setScaleY(pane.getScaleY() + zoomFactor);
	}
	
	
	public void setDeliveryPointsListener(DeliveryPointsListener dpl) {
		this.dpl = dpl;
	}

	@Override
	public void update(Observable o, Object arg) {
		drawDeliveryRequest(dr);
		drawRoundSet(rs);
		drawIntersections(map, dr);
	}
}
