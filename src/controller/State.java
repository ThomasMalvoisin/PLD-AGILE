package controller;

import java.io.File;

import javafx.scene.canvas.Canvas;

public interface State {
	
	public void loadMap(Canvas canvas);
	
	public void loadDeliveryRequest(Canvas canvas);
	
	public void clicFile(Canvas canvas, File file);
	
//	public void roundsComputation(Canvas canvas, )
}
