package view;

import java.util.Collection;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import model.CityMap;
import model.Intersection;
import model.Section;

public class MapViewBuilder {

	Canvas canvas;
	
	public void build(Canvas c, CityMap map) {
		this.canvas = c;
		Collection<List<Section>> listSections = map.getSections();

		for(List<Section> secs : listSections) {
			for(Section sec : secs) {
				drawSection(sec);
			}
		}
	}
	
	private void drawSection(Section sec) {
		
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
}
