package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import model.CityMap;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class SampleController {

	@FXML
	Canvas canvas;
	
	public void loadMap() {
		URL url = getClass().getResource("/xml/petitPlan.xml");
		File xml = new File(url.getPath());
		CityMap map = new CityMap();
		
			try {
				MapDeserializer.charger(map, xml);
			} catch (ExceptionXML e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MapViewBuilder mvb = new MapViewBuilder();
			mvb.build(canvas, map);
		
	}
	
	public void drawRandomLine() {
		Bounds bounds = canvas.getBoundsInLocal();
		double xD = Math.random() * (bounds.getMaxX());
		double xA = Math.random() * (bounds.getMaxX());
		double yD = Math.random() * (bounds.getMaxY());
		double yA = Math.random() * (bounds.getMaxY());
		drawLine(xD,yD,xA,yA);
		
	}
	
	public void drawLine(double xD, double yD,double xA,double yA) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.strokeLine(xD, yD, xA, yA);
	}
	
}
