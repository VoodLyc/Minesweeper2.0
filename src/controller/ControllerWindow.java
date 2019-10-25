package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import modelo.Buscaminas;
import modelo.Casilla;

public class ControllerWindow implements Initializable {
	
	private Buscaminas minesweeper;
	
	@FXML
	private BorderPane pane;
	@FXML
	private VBox box;
	@FXML
	private ChoiceBox<String> choice;
	@FXML
	private Button play;
	
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
			
			loadBoard(Buscaminas.FILAS_PRINCIPIANTE, Buscaminas.COLUMNAS_PRINCIPIANTE, Buscaminas.PRINCIPIANTE);
		}
		else if(choice.getValue().equals("Medium")) {
			
			loadBoard(Buscaminas.FILAS_INTERMEDIO, Buscaminas.COLUMNAS_INTERMEDIO, Buscaminas.INTERMEDIO);
		}
		else {
			
			loadBoard(Buscaminas.FILAS_EXPERTO, Buscaminas.COLUMNAS_EXPERTO, Buscaminas.EXPERTO);
		}
	}
	
	public void clearScene() {
		
		pane.getChildren().remove(box);
		
	}
	
	public void loadButtons() {
		
		ToolBar toolBarClue = new ToolBar();
		Button solve = new Button();
		solve.setText("SOLVE THE GAME");
		Button clue = new Button();
		clue.setText("GIVE A CLUE");
		toolBarClue.getItems().add(clue);
		toolBarClue.getItems().add(solve);
		pane.setTop(toolBarClue);
	}
	
	public void loadBoard(int rows, int columns, int dificcult) {
		
		loadButtons();
		
		GridPane grid = new GridPane();	
		pane.setCenter(grid);
		
		minesweeper = new Buscaminas(dificcult);
		Casilla [][] squares = minesweeper.darCasillas();

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
				
				Button square = new Button();
				square.setMaxWidth(Double.MAX_VALUE);
				square.setMaxHeight(Double.MAX_VALUE);
				square.setText(squares[j][i].mostrarValorCasilla());
				grid.add(square, i, j);	
			}
		}
	}
}
