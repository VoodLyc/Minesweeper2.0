package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	
	private Buscaminas minesweeper;
	private GridPane grid;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
	
	public void loadButtons() {
		
		ToolBar toolBar = new ToolBar();
		Button solve = new Button();
		solve.setText("SOLVE THE GAME");
		Button clue = new Button();
		clue.setText("GIVE A CLUE");
		toolBar.getItems().add(clue);
		toolBar.getItems().add(solve);
		pane.setTop(toolBar);
	}
	
	public void loadBoard(int rows, int columns, int difficulty) {
		
		loadButtons();
		
		grid = new GridPane();
		pane.setCenter(grid);
		
		minesweeper = new Buscaminas(difficulty);

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
				square.setText(minesweeper.getSquare(j, i).mostrarValorCasilla());
				int x = j;
				int y = i;
				square.setOnAction(event -> {openSquare(x,y,difficulty); square.setText(minesweeper.getSquare(x,y).mostrarValorCasilla());});
				grid.add(square, i, j);
			}
		}
	}
	
	public void openSquare(int x, int y, int difficulty) {
		
		minesweeper.abrirCasilla(x, y);
		
		if(minesweeper.darPerdio()) {
			
			ButtonType again = new ButtonType("Play again", ButtonBar.ButtonData.OK_DONE);
			ButtonType menu = new ButtonType("Return to menu", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.CONFIRMATION, "You lost! do you want to play again?", again, menu);
			alert.setHeaderText(null);
			alert.setTitle(null);
			
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == again) {
				
				if(difficulty == 1) {
					
					loadBoard(Buscaminas.FILAS_PRINCIPIANTE, Buscaminas.COLUMNAS_PRINCIPIANTE, Buscaminas.PRINCIPIANTE);
				}
				else if(difficulty == 2) {
					
					loadBoard(Buscaminas.FILAS_INTERMEDIO, Buscaminas.COLUMNAS_INTERMEDIO, Buscaminas.INTERMEDIO);
				}
				else {
					
					loadBoard(Buscaminas.FILAS_EXPERTO, Buscaminas.COLUMNAS_EXPERTO, Buscaminas.EXPERTO);
				}
			}
			else {
				
				pane.setCenter(box);
				pane.setTop(null);
				choice.getSelectionModel().select(0);
			}
		}
	}
}
