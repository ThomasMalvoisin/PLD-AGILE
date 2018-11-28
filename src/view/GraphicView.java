package view;

import java.util.Collection;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Group;
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

public class GraphicView {
	// Implémentera le design pattern observer pour : deliveryRequest, peut être
	// autre chose ?

	Pane pane;
	CityMap map = null;
	
	Group deliveries = null;

	public GraphicView(Pane pane) {
		this.pane = pane;
		this.deliveries = new Group();
	}

	public void clear() {
//		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		pane.getChildren().clear();
	}

	public void clearDeliveryRequest() {
		deliveries.getChildren().clear();
		pane.getChildren().remove(deliveries);
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
	
	public void drawRoundSet(RoundSet rs) {
		
		for(Round r : rs.getRounds()) {
			for(Journey j : r.getJourneys()) {
				for(Section s : j.getSectionList()) {
					drawRoundSection(s);
				}
			}
		}
		pane.getChildren().remove(deliveries);
		pane.getChildren().add(deliveries);
	}

	public void drawDeliveryRequest(DeliveryRequest deliveryRequest) {
		clearDeliveryRequest();
		drawWarehousePoint(deliveryRequest.getWarehouse());

		for(Delivery d : deliveryRequest.getRequestDeliveries()) {
			drawDeliveryPoint(d.getAdress());
		}
		pane.getChildren().add(deliveries);
	}

	private void drawRoundSection(Section sec) {
		
		drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()),3,Color.BLUEVIOLET);
	}
	
	private void drawSection(Section sec) {
		drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()),1,Color.BLACK);
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

	private void drawLine(double[] departure, double[] arrival,double width, Paint p) {
		// GraphicsContext gc = canvas.getGraphicsContext2D();
		// gc.strokeLine(departure[0], departure[1], arrival[0], arrival[1]);

		Line l = new Line(departure[0], departure[1], arrival[0], arrival[1]);
		l.setStroke(p);
		l.setStrokeWidth(width);

		pane.getChildren().add(l);
	}

	public void drawDeliveryPoint(Intersection i) {
		drawPoint(geoToCoord(i), Color.RED);
	}

	public void drawWarehousePoint(Intersection i) {
		drawPoint(geoToCoord(i), Color.FORESTGREEN);
	}

	public void drawPoint(double[] point, Paint p) {
		Circle c = new Circle(point[0], point[1], 5.0);
		c.setFill(p);
		c.getOnMouseClicked();
		c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			System.out.println("Point cliqu�");
			
		});
		
		deliveries.getChildren().add(c);
	}

}
