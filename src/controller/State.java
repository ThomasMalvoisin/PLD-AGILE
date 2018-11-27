package controller;

import java.io.File;

import javafx.scene.canvas.Canvas;
import model.CityMap;
import model.DeliveryRequest;
import model.RoundSet;
import view.MapViewBuilder;

public interface State {
	
	public CityMap loadMap() throws Exception;
	
	public DeliveryRequest loadDeliveryRequest(CityMap map) throws Exception;
	
	public RoundSet roundsCompute(CityMap map, DeliveryRequest delivReq ) throws Exception;
}