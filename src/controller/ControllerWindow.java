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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import modelo.AlreadySelectedException;
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
	
	public void loadButtons(int rows, int columns, int difficulty) {
		
		ToolBar toolBar = new ToolBar();
		
		Button solve = new Button("SOLVE THE GAME");
		solve.setOnAction(event -> solve(rows, columns));
		
		Button clue = new Button("GIVE A CLUE");
		clue.setOnAction(event -> giveClue(rows, columns, difficulty));
		
		Button back = new Button("RETURN TO MENU");
		back.setOnAction(event -> backToMenu());
		
		Button restart = new Button("NEW GAME");
		restart.setOnAction(event -> restartGame(rows, columns, difficulty));
		
		toolBar.getItems().add(clue);
		toolBar.getItems().add(solve);
		toolBar.getItems().add(restart);
		toolBar.getItems().add(back);
		
		pane.setTop(toolBar);
	}
	
	public void loadBoard(int rows, int columns, int difficulty) {
		
		loadButtons(rows, columns, difficulty);
		
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
				
				Button square = new Button(minesweeper.getSquare(j, i).mostrarValorCasilla());
				square.setMaxWidth(Double.MAX_VALUE);
				square.setMaxHeight(Double.MAX_VALUE);
				int x = j;
				int y = i;
				square.setOnMouseClicked(MouseEvent -> onClick(x, y, rows, columns, difficulty, square, MouseEvent));
				grid.add(square, i, j);
			}
		}
	}
	
	public void solve(int rows, int columns) {
		
		ButtonType solve = new ButtonType("Solve", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.WARNING, "The game will end and you lose, are you sure?", solve, cancel);
		alert.setHeaderText(null);
		alert.setTitle(null);
		
		Optional <ButtonType> action = alert.showAndWait();
		
		if(action.get() == solve) {
			
			solveGame(rows, columns);
		}	
	}
	
	public void solveGame(int rows, int columns) {
		
		minesweeper.resolver();
		
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
				
				Button square = new Button(minesweeper.getSquare(j, i).mostrarValorCasilla());
				square.setMaxWidth(Double.MAX_VALUE);
				square.setMaxHeight(Double.MAX_VALUE);
				square.setOnAction(event -> loadMenu());
				grid.add(square, i, j);
			}
		}
		
	}
	
	public void loadMenu() {
		
		pane.setCenter(box);
		pane.setTop(null);
		choice.getSelectionModel().select(0);
	}
	
	public void startNewGame(int difficulty) {
		
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
	
	public void onClick(int x, int y, int rows, int columns, int difficulty, Button square, MouseEvent event) {
		
		MouseButton mouse = event.getButton();
		
		if(mouse == MouseButton.PRIMARY) {
			
			if(square.getText().equals(minesweeper.getSquare(x, y).mostrarValorCasilla())) {
				
				openSquare(x, y, rows, columns, difficulty, square);
			}
		}
		else if(mouse == MouseButton.SECONDARY) {
			
			if(minesweeper.getSquare(x, y).getMarked()) {
				
				minesweeper.unmarkSquare(x, y);
				square.setText(minesweeper.getSquare(x, y).mostrarValorCasilla());
			}
			else {
				
				try {
					
					minesweeper.markSquare(x, y);
					square.setText("?");
					
				}
				catch(AlreadySelectedException e) {
					
					ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
					Alert alert = new Alert(AlertType.INFORMATION,"You can't mark an already open square", ok);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.show();
				}
			}
		}
	}
	
	public void openSquare(int x, int y, int rows, int columns, int difficulty, Button square) {
		
		minesweeper.abrirCasilla(x, y);
		square.setText(minesweeper.getSquare(x, y).mostrarValorCasilla());
		
		if(minesweeper.darPerdio()) {
			
			solveGame(rows, columns);
			
			ButtonType again = new ButtonType("Play again", ButtonBar.ButtonData.OK_DONE);
			ButtonType menu = new ButtonType("Return to menu", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.INFORMATION, "You lost! do you want to play again?", again, menu);
			alert.setHeaderText(null);
			alert.setTitle(null);
			
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == again) {
				
				startNewGame(difficulty);
			}
			else {
				
				loadMenu();
			}
		}
		
		else if(minesweeper.gano()) {
			
			ButtonType again = new ButtonType("Play again", ButtonBar.ButtonData.OK_DONE);
			ButtonType menu = new ButtonType("Return to menu", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.INFORMATION, "You win! do you want to play again?", again, menu);
			alert.setHeaderText(null);
			alert.setTitle(null);
			
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == again) {
				
				startNewGame(difficulty);
			}
			else {
				
				loadMenu();
			}
			
		}
	}
	
	public void restartGame(int rows, int columns, int difficulty) {
		
		if(minesweeper.gano()) {
			
			loadBoard(rows, columns, difficulty);
		}
		else {
			
			ButtonType accept = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
			ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.WARNING, "All current progress will be lost, are you sure?", accept, cancel);
			alert.setTitle(null);
			alert.setHeaderText(null);
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == accept) {
				
				loadBoard(rows, columns, difficulty);
			}	
		}
	}
	
	public void backToMenu() {
		
		if(minesweeper.gano()) {
			
			loadMenu();
		}
		else {
			
			ButtonType accept = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
			ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.WARNING, "All current progress will be lost, are you sure?", accept, cancel);
			alert.setTitle(null);
			alert.setHeaderText(null);
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == accept) {
				
				loadMenu();
			}
		}
	}
	
	public void giveClue(int rows, int columns, int difficulty) {
		
		if(minesweeper.darPista().equals("")) {
			
			ButtonType accept = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.INFORMATION, "There are no clues to give", accept);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.show();
		}
		else {
			
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
					
					Button square = new Button(minesweeper.getSquare(j, i).mostrarValorCasilla());
					
					if(minesweeper.getSquare(j, i).getMarked()) {
						
						square.setText("?");
					}
					
					square.setMaxWidth(Double.MAX_VALUE);
					square.setMaxHeight(Double.MAX_VALUE);
					int x = j;
					int y = i;
					square.setOnMouseClicked(MouseEvent -> onClick(x, y, rows, columns, difficulty, square, MouseEvent));
					grid.add(square, i, j);
				}
			}
		}
	}
}
