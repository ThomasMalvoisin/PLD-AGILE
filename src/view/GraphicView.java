package view;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.Section;

public class GraphicView {
	//Implémentera le design pattern observer pour : deliveryRequest, peut être autre chose ?

	Pane pane;
	CityMap map = null;

	public GraphicView(Pane pane) {
		this.pane = pane;
	}
	
	public void clear() {
//		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		pane.getChildren().clear();	
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

	private void drawSection(Section sec) {
		drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()));
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

}
