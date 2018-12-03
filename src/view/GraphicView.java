package view;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Iterator;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Journey;
import model.Round;
import model.RoundSet;
import model.Section;

public class GraphicView implements Observer{
	// Implémentera le design pattern observer pour : deliveryRequest, peut être
	// autre chose ?

	Pane pane;
	CityMap map;
	DeliveryRequest dr;
	RoundSet rs;
	Group deliveries;
	Group roundSet;
	Group notDeliveriesIntersections;

	DeliveryPointsListener dpl;

	public GraphicView(Pane pane) {
		this.pane = pane;
		this.deliveries = new Group();
		this.roundSet = new Group();
		this.notDeliveriesIntersections = new Group();
	}

	public void clear() {
//		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
		Collection<List<Section>> listSections = cityMap.getSections();


		for (List<Section> secs : listSections) {
			for (Section sec : secs) {
				drawSection(sec);
			}
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
		rs.addObserver(this);
		this.rs = rs;
		for (Round r : rs.getRounds()) {
			for (Journey j : r.getJourneys()) {
				for (Section s : j.getSectionList()) {
					drawRoundSection(s);
				}
			}
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

	private void drawRoundSection(Section sec) {

		Line l = drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()), 3, Color.ROYALBLUE);
		roundSet.getChildren().add(l);
	}

	private void drawSection(Section sec) {
		Line l = drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()), 1, Color.WHITE);
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

	private Line drawLine(double[] departure, double[] arrival, double width, Paint p) {
		// GraphicsContext gc = canvas.getGraphicsContext2D();
		// gc.strokeLine(departure[0], departure[1], arrival[0], arrival[1]);

		Line l = new Line(departure[0], departure[1], arrival[0], arrival[1]);
		l.setStroke(p);
		l.setStrokeWidth(width);

		return l;
	}

	private void drawIntersectionPoint (Intersection i)
	{
		Circle c = new Circle(geoToCoord(i)[0],geoToCoord(i)[1], 5);
		c.setFill(Color.WHITE);
		c.setOpacity(0);
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
		for(Node n : deliveries.getChildren()) {
			if(d.equals(n.getProperties().get("DELIVERY"))) {
				((Circle) n).setFill(Color.AQUA);
				((Circle) n).setRadius(7);
			}else if (n.getProperties().get("DELIVERY")==null){
				((Circle) n).setFill(Color.FORESTGREEN);
			}else {
				((Circle) n).setFill(Color.RED);
				((Circle) n).setRadius(5);
			}
		}
	}
	
	public void setIntersectionSelected(Intersection i) {
		for (Node n : notDeliveriesIntersections.getChildren()) {
			if(i.equals(n.getProperties().get("INTERSECTION"))) {
				((Circle) n).setFill(Color.AQUA);
				((Circle) n).setOpacity(1);
			} else {
				((Circle) n).setFill(Color.WHITE);
				((Circle) n).setOpacity(0);
			}
		}
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
