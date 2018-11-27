package controller;

import java.io.File;

import algo.Algorithms;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CityMap;
import model.DeliveryRequest;
import model.RoundSet;
import xml.DeliveryRequestDeserializer;

public class StateDeliveryLoaded extends StateDefault{
	
	@Override
	public DeliveryRequest loadDeliveryRequest(CityMap map) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger une demande de livraison");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		File file = fileChooser.showOpenDialog(new Stage());

		DeliveryRequest dr = null;

		if (file != null) {
			//Ajouter des vérifications
			dr = DeliveryRequestDeserializer.Load(map, file);
			Controller.setCurrentState(Controller.stateDeliveryLoaded);
		}
		return dr;
	}
	
	@Override
	public RoundSet roundsCompute(CityMap map, DeliveryRequest delivReq) throws Exception{
		Algorithms algoUtil = new Algorithms(map);
		algoUtil.dijkstraDeliveryRequest(delivReq);
		RoundSet result = algoUtil.solveTSP(delivReq, 1);
		Controller.setCurrentState(Controller.stateRoundCalculated);
		return result;
	}
}
