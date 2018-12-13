package deliveryApp;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.MainView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Sample.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/application.css").toExternalForm());
			MainView mainView = (MainView)loader.getController();
			primaryStage.setScene(scene);
			primaryStage.show();
			mainView.postInitialize(scene);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
