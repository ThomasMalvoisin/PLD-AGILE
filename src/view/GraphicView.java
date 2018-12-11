package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.LinkedList;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Journey;
import model.Round;
import model.RoundSet;
import model.Section;

public class GraphicView implements Observer {

	Pane pane;
	
	CityMap map;
	DeliveryRequest dr;
	RoundSet rs;
	
	Group deliveries;
	Map<Color, Group> rounds;
	Group deliveriesIds;
	Group notDeliveriesIntersections;

	Color[] colors = { Color.ROYALBLUE, Color.BLACK, Color.ORANGE, Color.BROWN, Color.GREEN, Color.GOLD,
			Color.BLUEVIOLET, Color.YELLOW, Color.AQUAMARINE, Color.CORAL };

	DeliveryPointsListener dpl;

	public GraphicView(Pane pane) {
		this.pane = pane;
		this.deliveries = new Group();
		this.rounds = new HashMap<Color, Group>();
		this.deliveriesIds = new Group();
		this.notDeliveriesIntersections = new Group();
		
		pane.setOnScroll(event -> {
			zoom(event.getDeltaY()*0.1,event.getX(),event.getY());
	        
	    });
		
		
		pane.setOnMousePressed(event ->{
			double mouseXPos = event.getX();
			double mouseYPos = event.getY();
			
			initDrag(mouseXPos,mouseYPos);
		});
			
	}

	private void initDrag(double mouseXPos, double mouseYPos) {
		pane.setOnMouseDragged(event -> {

			if(pane.getScaleX() == 1) return;
		    pane.setTranslateX(event.getX() + pane.getTranslateX() - mouseXPos);
		    pane.setTranslateY(event.getY() + pane.getTranslateY() - mouseYPos);
		    event.consume();
		});
	}
	
	public void clear() {
		pane.getChildren().clear();
	}

	public void clearDeliveryRequest() {
		deliveries.getChildren().clear();
		pane.getChildren().remove(deliveries);
		clearRounds();
		deliveriesIds.getChildren().clear();
		pane.getChildren().remove(deliveriesIds);
		notDeliveriesIntersections.getChildren().clear();
		pane.getChildren().remove(notDeliveriesIntersections);
	}

	public void drawCityMap(CityMap cityMap) {
		clear();
		this.map = cityMap;
		for (Section section : cityMap.getSections()) {
			drawSection(section);
		}
	}

	public void drawIntersections(CityMap cityMap, DeliveryRequest dr) {
		Map<Long, Intersection> listPotentialDeliveriesIntersections = cityMap.getNotDeliveriesIntersections(dr);
		for (Entry<Long, Intersection> entry : listPotentialDeliveriesIntersections.entrySet()) {
			drawIntersectionPoint(entry.getValue());
		}
		pane.getChildren().remove(deliveries);
		pane.getChildren().remove(deliveriesIds);
		pane.getChildren().add(notDeliveriesIntersections);
		pane.getChildren().add(deliveries);
		pane.getChildren().add(deliveriesIds);


	}
	
	public Map<Section, List<Color>> convertRoundSetToMap (RoundSet rs){
		Map<Section, List<Color>> listeSection = new HashMap<Section, List<Color>>();
		double arrowOpacity = 1.0;
		int i = 0;
		for (Round r : rs.getRounds()) {
			for (Journey j : r.getJourneys()) {
				int k = 0;
				for (Section s : j.getSectionList()) {
					Section sinv = s.getInverse();
					if (!listeSection.containsKey(s) ){
						boolean foundInv = false ;
						for (Entry<Section, List<Color>> entry : listeSection.entrySet()) {
							
							if (sinv.equals(entry.getKey()))
							{
								if (!entry.getValue().contains(colors[i])){
									entry.getValue().add(colors[i]);
								}
								foundInv = true;
								break;
							}
						}
						if (!foundInv)
						{
							List<Color> c = new LinkedList<Color>();
							c.add(colors[i]);
							listeSection.put(s, c);
						}
						
					}
					else  {
						if (!listeSection.get(s).contains(colors[i])){
							listeSection.get(s).add(colors[i]);
						}
					}
					if(k == j.getSectionList().size()/2 ) {
						Line[] lines = drawArrow( geoToCoord(s.getOrigin()), geoToCoord(s.getDestination()), 2, colors[i], arrowOpacity);
						addLineToRounds(colors[i], lines[0]);
						addLineToRounds(colors[i], lines[1]);
					}
					k++;
				}
			}
			i++;
		}
				
		return listeSection;
	}

	public void drawRoundSet(RoundSet rs) {
		drawDeliveryRequest(dr);
		rs.addObserver(this);
		this.rs = rs;
		Map<Section, List<Color>> listeSection = convertRoundSetToMap(rs);
		drawRoundsFromMap(listeSection);
	}
	void drawRoundsFromMap(Map<Section,List<Color>> listeSection) {
		double x0, y0, x1, y1;
		double size;
		for (Entry<Section, List<Color>> entry : listeSection.entrySet()) {
			x0 = geoToCoord(entry.getKey().getOrigin())[0];
			y0 = geoToCoord(entry.getKey().getOrigin())[1];
			x1 = geoToCoord(entry.getKey().getDestination())[0];
			y1 = geoToCoord(entry.getKey().getDestination())[1];
			size = entry.getValue().size();
			for(int m = 0; m<size; m++) {
				for(int l = 0; l<size; l++){
					Line line = new Line(x0 + l* (x1-x0)/size, y0 + l* (y1-y0)/size, x0 + (l+1)*(x1-x0)/size, y0 + (l+1)*(y1-y0)/size);
					//System.out.println(("(x0,y0) = (" + x0 +", " +  y0 + " )  and (x1,y1) = (" +  + x1 +", " +  y1 + " )  from : (" + (x0 + l* (x1-x0)/size) + ", " + (y0 + l* (y1-y0)/size)) + ")  To (" + (x0 + (l+1)* (x1-x0)/size) + ", " + ( y0 + (l+1)*(y1-y0)/size) + ")");
					line.setStroke(entry.getValue().get(m));
					line.setStrokeWidth(3);
					if (m == l)
					{
						line.setOpacity(1.0);
					}else {
						line.setOpacity(0.0);
						line.getProperties().put("HiddenLine", line);
					}
					addLineToRounds(entry.getValue().get(m),line);
				}
				System.out.println("");
			}
		}
		for(Entry<Color, Group> entry : rounds.entrySet())
		{
			pane.getChildren().add(entry.getValue());
		}
		pane.getChildren().remove(deliveries);
		pane.getChildren().add(deliveries);
		pane.getChildren().remove(deliveriesIds);
		pane.getChildren().add(deliveriesIds);
	}

	public void drawDeliveryRequest(DeliveryRequest deliveryRequest) {
		clearDeliveryRequest();
		deliveryRequest.addObserver(this);
		this.dr = deliveryRequest;
		drawWarehousePoint(deliveryRequest.getWarehouse());

		for (Delivery d : deliveryRequest.getRequestDeliveries()) {
			drawDeliveryPoint(d);
		}
		pane.getChildren().add(deliveries);
		pane.getChildren().add(deliveriesIds);
	}

	
	private void drawSection(Section sec) {
		Line l = drawLine(geoToCoord(sec.getOrigin()), geoToCoord(sec.getDestination()), 1, Color.WHITE, 0, 1);
		pane.getChildren().add(l);
	}

	private double[] geoToCoord(Intersection i) {
		double[] result = new double[2];
		Bounds bounds = pane.getBoundsInLocal();
		result[0] = ((i.getLongitude() - map.getLongitudeMin()) * bounds.getMaxX())
				/ (map.getLongitudeMax() - map.getLongitudeMin());
		// result[1] = ((i.getLatitude() - map.latitudeMin) *
		// bounds.getMaxY())/(map.latitudeMax-map.latitudeMin);
		result[1] = ((-i.getLatitude() + map.getLatitudeMax()) * bounds.getMaxY())
				/ (map.getLatitudeMax() - map.getLatitudeMin());
		return result;
	}

	private Line drawLine(double[] departure, double[] arrival, double width, Paint p, double delta, double opacity) {
		Line l = new Line(departure[0] , departure[1] , arrival[0] , arrival[1] );
		l.setOpacity(opacity);
		l.setStroke(p);
		l.setStrokeWidth(width);
		
		return l;
	}
	private Line[] drawArrow(double[] departure, double[] arrival, double width, Paint p, double opacity) {
		//    D
		//    |\
		//    | \
		//A---C--B------F
		//    | /
		//    |/
		//    E
		double xA, yA, xB, yB, xC, yC, xD, yD, xE, yE, xF, yF, AB;
		double n = 6.0;
		xA = arrival[0];
		yA = arrival[1];
		xF = departure[0];
		yF = departure[1];
		xB = (xA + xF)/2;
		yB = (yA + yF)/2;
		AB = Math.sqrt((xB-xA)*(xB-xA)+(yB-yA)*(yB-yA));
		xC = xB + n * (xB-xA)/AB;
		yC = yB + n * (yB-yA)/AB;
		xD = xC + n * (yA-yB)/AB;
		yD = yC + n * (xB-xA)/AB;
		xE = xC - n * (yA-yB)/AB;
		yE = yC - n * (xB-xA)/AB;
		Line[] l = new Line[2];
		l[0] = new Line(xB, yB, xD, yD);
		l[1] = new Line(xB, yB, xE, yE);
		l[0].setOpacity(opacity);
		l[0].setStroke(p);
		l[0].setStrokeWidth(width);
		l[1].setOpacity(opacity);
		l[1].setStroke(p);
		l[1].setStrokeWidth(width);
		return l;
	}
	
	

	private void drawIntersectionPoint(Intersection i) {
		Circle c = new Circle(geoToCoord(i)[0], geoToCoord(i)[1], 5);
		c.setFill(Color.WHITE);
		c.setOpacity(0);
		c.getStyleClass().add("map-point");
		notDeliveriesIntersections.getChildren().add(c);
		c.getProperties().put("INTERSECTION", i);
		c.addEventHandler(MouseEvent.ANY, dpl);
	}

	public void drawDeliveryPoint(Delivery d) {
//		drawPoint(geoToCoord(d.getAdress()),5, Color.RED, d);
		double[] coord = geoToCoord(d.getAdress());
		Circle c = makePoint(coord, 5, Color.RED);
		Text t = new Text(d.getId() + "");
		t.setX(coord[0] + 5);
		t.setY(coord[1] - 5);
		t.setFill(Color.RED);
		deliveriesIds.getChildren().add(t);
		c.getProperties().put("DELIVERY", d);
		c.addEventHandler(MouseEvent.ANY, dpl);
	}

	public void drawWarehousePoint(Intersection i) {
//		drawPoint(geoToCoord(i),8, Color.FORESTGREEN, null);
		Circle c = makePoint(geoToCoord(i), 7, Color.FORESTGREEN);
		c.getProperties().put("WAREHOUSE", i);
		c.addEventHandler(MouseEvent.ANY, dpl);
	}

	private Circle makePoint(double[] point, double radius, Paint p) {
		Circle c = new Circle(point[0], point[1], radius);
		c.setFill(p);
		c.getStyleClass().add("map-point");
		deliveries.getChildren().add(c);
		return c;
	}

	public void setDeliverySelected(Delivery d) {
		for (Node n : deliveries.getChildren()) {
			if (d != null && d.equals(n.getProperties().get("DELIVERY"))) {
				((Circle) n).setFill(Color.AQUA);
				((Circle) n).setRadius(7);
			} else if (n.getProperties().get("DELIVERY") == null) {
				((Circle) n).setFill(Color.FORESTGREEN);
			} else {
				((Circle) n).setFill(Color.RED);
				((Circle) n).setRadius(5);
			}
		}
	}

	public void setIntersectionSelected(Intersection i) {
		for (Node n : notDeliveriesIntersections.getChildren()) {
			if (i != null && i.equals(n.getProperties().get("INTERSECTION"))) {
				System.out.println("Suu");
				((Circle) n).getStyleClass().add("map-point-selected");
//				((Circle) n).setFill(Color.AQUA);
//				((Circle) n).setOpacity(1);
			} else {
				((Circle) n).setFill(Color.WHITE);
				((Circle) n).setOpacity(0);
				((Circle) n).getStyleClass().add("map-point");
				((Circle) n).getStyleClass().remove("map-point-selected");
			}
		}
	}

	public void addLineToRounds (Color color, Line line)
	{
		if (!rounds.containsKey(color)) {
			Group g = new Group();
			g.getChildren().add(line);
			rounds.put(color, g);
		}else{
			rounds.get(color).getChildren().add(line);
		}
	}
	public void clearRound(Color color)
	{
		rounds.get(color).getChildren().clear();
		pane.getChildren().remove(rounds.get(color));
	}
	public void clearRounds()
	{
		for(Entry<Color, Group> entry : rounds.entrySet())
		{
			entry.getValue().getChildren().clear();
			pane.getChildren().remove(entry.getValue());
		}
	}
	
	public void setRoundSelected(RoundSet roundSet, Delivery delivery, boolean flag) {
		ArrayList<Round> rs = roundSet.getRounds();
		int i = 0;
		for (Round round : rs) {
			if(round.getDeliveries().contains(delivery)) {
				break;
			}
			i++;
		}
		for(Node n : (rounds.get(colors[i]).getChildren()))
		{
			if(flag)
			{
				((Line) n).setStrokeWidth(5.0);
				if(((Line) n).getProperties().containsKey("HiddenLine"))
				{
					((Line) n).setOpacity(1.0);
				}
			}
			else
			{
				((Line) n).setStrokeWidth(3.0);
				if(((Line) n).getProperties().containsKey("HiddenLine"))
				{
					((Line) n).setOpacity(0.0);
				}
			}
		}
		pane.getChildren().remove(rounds.get(colors[i]));
		pane.getChildren().add(rounds.get(colors[i]));
		pane.getChildren().remove(deliveries);
		pane.getChildren().add(deliveries);
	}
	
	public void zoomIn() {
		
		pane.setScaleX(pane.getScaleX() + 0.4);
		pane.setScaleY(pane.getScaleY() + 0.4);
		
	}
	
	public void zoomOut() {
		
		if(pane.getScaleX() - 0.4 < 1) {
			
			return;
		}
		
		
		pane.setScaleX(pane.getScaleX() - 0.4);
		pane.setScaleY(pane.getScaleY() - 0.4);
	}
	
	public void zoomAuto() {
		pane.setScaleX(1);
		pane.setScaleY(1);
		pane.setTranslateX(0);
		pane.setTranslateY(0);
		
	}
	
	public void zoom(double zoomFactor, double xFocus, double yFocus) {
		
		if(pane.getScaleX() + zoomFactor < 1) {
			
			pane.setScaleX(1);
			pane.setScaleY(1);
			pane.setTranslateX(0);
			pane.setTranslateY(0);
			return;
		}
		pane.setScaleX(pane.getScaleX() + zoomFactor);
		pane.setScaleY(pane.getScaleY() + zoomFactor);
	}
	
	
	public void setDeliveryPointsListener(DeliveryPointsListener dpl) {
		this.dpl = dpl;
	}

	@Override
	public void update(Observable o, Object arg) {
		drawDeliveryRequest(dr);
		drawRoundSet(rs);
		drawIntersections(map, dr);
	}

}
