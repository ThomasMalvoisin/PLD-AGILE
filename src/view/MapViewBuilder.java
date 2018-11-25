package view;

import java.util.Collection;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.CityMap;
import model.Intersection;
import model.Section;

public class MapViewBuilder {

	Canvas canvas;
	
	public MapViewBuilder(Canvas c) {
		this.canvas = c;
	}
	public void build(CityMap map) {
		Collection<List<Section>> listSections = map.getSections();

		for(List<Section> secs : listSections) {
			for(Section sec : secs) {
				drawSection(sec);
			}
		}
	}
	
	public void drawSection(Section sec) {
		
		drawLine(geoToCoord(sec.getOrigin()),geoToCoord(sec.getDestination()));		
	}
	
	private double[] geoToCoord(Intersection i) {
		double[] result = new double[2];
		Bounds bounds = canvas.getBoundsInLocal();
		result[0] = ((i.getLongitude() - Intersection.longitudeMin) * bounds.getMaxX())/(Intersection.longitudeMax-Intersection.longitudeMin);
		result[1] = ((i.getLatitude() - Intersection.latitudeMin) * bounds.getMaxY())/(Intersection.latitudeMax-Intersection.latitudeMin);
		return result;
	}
	
	private void drawLine(double[] departure,double[] arrival) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.strokeLine(departure[0], departure[1], arrival[0], arrival[1]);
	}
	
	public void clearCanevas () {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void drawDeliveryPoint(Intersection i) {
		
		drawPoint(geoToCoord(i));
	}
	
	public void drawPoint(double[] point) {		
		Circle c = new Circle(point[0],point[1],5.0);
		c.setFill(Color.RED);
		c.getOnMouseClicked();
		c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.out.println("Point cliqu�");
        });
		
		AnchorPane ap = (AnchorPane) canvas.getParent();
		ap.getChildren().add(c);
	}
}
