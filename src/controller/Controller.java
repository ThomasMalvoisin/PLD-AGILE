package controller;

public class Controller {
	
	//ajouter canvas du fxml
	
	protected static State currentState;
	protected static final StateInit stateInit = new StateInit();
	protected static final StateLoadingMap stateLoadingMap = new StateLoadingMap();
	protected static final StateLoadingMapEnd stateLoadingMapEnd = new StateLoadingMapEnd();
	
	protected static void setCurrentState(State state) {
		currentState = state;
	}
	
	
	//Clic sur un bouton -> à relier au fxml
	public void LoadMap() {
		
	}
	
	//Clic sur un bouton -> à relier au fxml
	public void LoadDeliveryRequest() {
		
	}
	
	//Clic sur un fichier -> à relier au fxml (voir quel paramètre passer)
	public void clicFile( ) {
		
	}
}
