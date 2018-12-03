package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonListener implements EventHandler<ActionEvent>{

	Controller controller;

	public ButtonListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void handle(ActionEvent event) {
		if(((Button)event.getTarget()).getId().equals("DELETE")) {
			//controller.delete2();
		}
	}
}
