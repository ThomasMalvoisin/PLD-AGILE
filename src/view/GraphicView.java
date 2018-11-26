package view;

import java.util.Observable;
import java.util.Observer;

import javafx.fxml.Initializable;
import model.CityMap;
import model.CityMapElement;
import model.DeliveryRequest;
import model.Intersection;
import model.Section;
import model.VisitorElement;

public class GraphicView implements Observer, VisitorElement {
	CityMap map;
	MapViewBuilder mvb;
	DeliveryRequest delivReq;
	
	public GraphicView(MapViewBuilder mvb, CityMap map, DeliveryRequest delivReq)
	{
		this.map = map;
		this.mvb = mvb;
		map.addObserver(this);
		delivReq.addObserver(this);
	}
	
	@Override
	public void visiteElement(Section s) {
		mvb.drawSection(s);
	}

	@Override
	public void visiteElement(Intersection i) {
		mvb.drawDeliveryPoint(i);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg != null) {
			CityMapElement element = (CityMapElement) arg;
			element.printElement(this);
		}
	}

}
