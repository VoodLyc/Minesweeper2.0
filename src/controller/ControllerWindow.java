package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import modelo.Buscaminas;

public class ControllerWindow implements Initializable {
	
	@FXML
	private BorderPane pane;
	@FXML
	private VBox box;
	@FXML
	private ChoiceBox<String> choice;
	@FXML
	private Button play;
	
	private GridPane grid;
	private Button square;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		start();
	}
	
	public void start() {
		
		choice.getItems().addAll("Easy", "Medium", "Hard");
		choice.getSelectionModel().select(0);
	}
	
	public void play(ActionEvent event) {
		
		if(choice.getValue().equals("Easy")) {
			
			clearScene();
			loadBoard(Buscaminas.FILAS_PRINCIPIANTE, Buscaminas.COLUMNAS_PRINCIPIANTE);
		}
		else if(choice.getValue().equals("Medium")) {
			
			clearScene();
			loadBoard(Buscaminas.FILAS_INTERMEDIO, Buscaminas.COLUMNAS_INTERMEDIO);
		}
		else {
			
			clearScene();
			loadBoard(Buscaminas.FILAS_EXPERTO, Buscaminas.COLUMNAS_EXPERTO);
		}
	}
	
	public void clearScene() {
		
		pane.getChildren().remove(box);
		
	}
	
	public void loadBoard(int rows, int columns) {
			
		grid = new GridPane();	
		pane.setCenter(grid);

		for(int i = 0; i < columns; i++) {
			
			ColumnConstraints column = new ColumnConstraints();
			column.setHgrow(Priority.ALWAYS);
			grid.getColumnConstraints().add(column);	
		}
		
		for(int i = 0; i < rows; i++) {
			
			RowConstraints row = new RowConstraints();
			row.setVgrow(Priority.ALWAYS);
			grid.getRowConstraints().add(row);
		}
		
		for(int i = 0; i < columns; i++) {
			
			for(int j = 0; j < rows; j++) {
				
				square = new Button();
				square.setMaxWidth(Double.MAX_VALUE);
				square.setMaxHeight(Double.MAX_VALUE);
				grid.add(square, i, j);
			}
		}
	}
}
