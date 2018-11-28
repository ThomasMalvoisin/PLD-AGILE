package view;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ResizeListener implements ChangeListener{
	Controller controller;
	public ResizeListener(Controller controller) {
		this.controller = controller;
	}
	
	@Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		controller.refreshView();
	}
	
}
