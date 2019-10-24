package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

public class ControllerWindow implements Initializable {
	
	@FXML
	private ChoiceBox<String> choice;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		start();
	}
	
	public void start() {
		
		choice.getItems().addAll("Easy", "Medium", "Hard");
		choice.getSelectionModel().select(0);
	}

}
