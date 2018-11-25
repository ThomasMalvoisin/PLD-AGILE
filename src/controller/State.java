package controller;

import java.io.File;

import javafx.scene.canvas.Canvas;
import model.CityMap;
import model.DeliveryRequest;
import view.MapViewBuilder;

public interface State {
	
	public void loadMap(MapViewBuilder mvb, CityMap map);
	
	public void loadDeliveryRequest(MapViewBuilder mvb, CityMap map, DeliveryRequest delivReq);
	
//	public void roundsComputation(Canvas canvas, )
}
