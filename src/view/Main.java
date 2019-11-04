package view;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/view/Window.fxml"));
			primaryStage.setTitle("Minesweeper");
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:images/icon.png"));
			scene.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
			primaryStage.setHeight(500);
			primaryStage.setWidth(600);
			primaryStage.setMinHeight(500);
			primaryStage.setMinWidth(600);
			primaryStage.centerOnScreen();
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
