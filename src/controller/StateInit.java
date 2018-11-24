package controller;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.scene.canvas.Canvas;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import view.MapViewBuilder;
import xml.ExceptionXML;
import xml.MapDeserializer;

public class StateInit extends StateDefault{

	@Override
	public void loadMap(Canvas canvas) {		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger un plan");
		File file = fileChooser.showOpenDialog(new Stage());
		
		if(file != null) {
			CityMap map = new CityMap();
			
			try {
				MapDeserializer.charger(map, file);
			} catch (ExceptionXML e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			MapViewBuilder mvb = new MapViewBuilder();
			mvb.build(canvas, map);
			
			Controller.setCurrentState(Controller.stateMapLoaded);
		}
	}
}
