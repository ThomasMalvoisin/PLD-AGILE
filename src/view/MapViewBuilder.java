package view;

import java.util.Collection;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.CityMap;
import model.Intersection;
import model.Section;

public class MapViewBuilder {

	Pane pane;
	CityMap map;
	
	public MapViewBuilder(Pane p,CityMap map) {
		this.pane = p;
		this.map = map;
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
		Bounds bounds = pane.getBoundsInLocal();
		result[0] = ((i.getLongitude() - map.getLongitudeMin()) * bounds.getMaxX())/(map.getLongitudeMax()-map.getLongitudeMin());
		//result[1] = ((i.getLatitude() - map.latitudeMin) * bounds.getMaxY())/(map.latitudeMax-map.latitudeMin);
		result[1] = ((-i.getLatitude() + map.getLatitudeMax()) * bounds.getMaxY())/(map.getLatitudeMax()-map.getLatitudeMin());
		return result;
	}
	
	private void drawLine(double[] departure,double[] arrival) {
		//GraphicsContext gc = canvas.getGraphicsContext2D();
		//gc.strokeLine(departure[0], departure[1], arrival[0], arrival[1]);
		
		Line l = new Line(departure[0],departure[1],arrival[0],arrival[1]);
		l.setFill(Color.BLACK);
		
		pane.getChildren().add(l);
	}
	
	public void clearCanevas () {
		//canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		pane.getChildren().clear();	
	}
	
	public void drawDeliveryPoint(Intersection i) {
		
		drawPoint(geoToCoord(i),Color.RED);
	}
	
	public void drawWarehousePoint(Intersection i) {
		
		drawPoint(geoToCoord(i),Color.FORESTGREEN);
	}
	
	public void drawPoint(double[] point, Paint p) {		
		Circle c = new Circle(point[0],point[1],5.0);
		c.setFill(p);
		c.getOnMouseClicked();
		c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            System.out.println("Point cliqu�");
        });
		
		pane.getChildren().add(c);
	}
	
}
