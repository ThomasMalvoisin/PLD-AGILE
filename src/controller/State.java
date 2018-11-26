package controller;

import java.io.File;

import javafx.scene.canvas.Canvas;
import model.CityMap;
import model.DeliveryRequest;
import view.MapViewBuilder;

public interface State {
	
	public CityMap loadMap() throws Exception;
	
	public DeliveryRequest loadDeliveryRequest(CityMap map) throws Exception;
	
//	public void roundsComputation(Canvas canvas, )
}
