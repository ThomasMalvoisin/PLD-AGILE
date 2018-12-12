package view;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ResizeListener implements ChangeListener{
	Controller controller;
	
	/**
	 * Create a Resize listener on Pane
	 * @param controller
	 */
	public ResizeListener(Controller controller) {
		this.controller = controller;
	}
	
	/* (non-Javadoc)
	 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
	 */
	@Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		controller.refreshView();
	}
	
}
