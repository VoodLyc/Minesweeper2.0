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
import javafx.scene.control.DialogPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
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
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
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
		AudioClip audio = new AudioClip("file:sounds/selection.wav");
		choice.setOnAction(ActionEvent -> audio.play());
	}
	
	public void play(ActionEvent event) {
		
		Stage stage = (Stage) pane.getScene().getWindow();
		
		if(choice.getValue().equals("Easy")) {
			
			loadBoard(Buscaminas.FILAS_PRINCIPIANTE, Buscaminas.COLUMNAS_PRINCIPIANTE, Buscaminas.PRINCIPIANTE);
			stage.setTitle("Minesweeper - Easy");
			stage.setMaximized(false);
			stage.setHeight(500);
			stage.setWidth(600);
			stage.setMinHeight(500);
			stage.setMinWidth(600);
			stage.centerOnScreen();
		}
		else if(choice.getValue().equals("Medium")) {
			
			loadBoard(Buscaminas.FILAS_INTERMEDIO, Buscaminas.COLUMNAS_INTERMEDIO, Buscaminas.INTERMEDIO);
			stage.setTitle("Minesweeper - Medium");
			stage.setMaximized(false);
			stage.setHeight(750);
			stage.setWidth(970);
			stage.setMinHeight(750);
			stage.setMinWidth(970);
			stage.centerOnScreen();
		}
		else {
			
			loadBoard(Buscaminas.FILAS_EXPERTO, Buscaminas.COLUMNAS_EXPERTO, Buscaminas.EXPERTO);
			stage.setTitle("Minesweeper - Hard");
			stage.setMaximized(true);
			stage.setMinHeight(750);
			stage.setMinWidth(1200);
			stage.centerOnScreen();
		}
		
		AudioClip audio = new AudioClip("file:sounds/selection.wav");
		audio.play();
	}
	
	public void loadButtons(int rows, int columns, int difficulty) {
		
		ToolBar toolBar = new ToolBar();
		
		Button solve = new Button("Solve the game");
		solve.setOnAction(event -> solve(rows, columns));
		solve.getStyleClass().add("menuButton");
		
		Button clue = new Button("Give a clue");
		clue.setOnAction(event -> giveClue(rows, columns, difficulty));
		clue.getStyleClass().add("menuButton");
		
		Button back = new Button("Return to menu");
		back.setOnAction(event -> backToMenu());
		back.getStyleClass().add("menuButton");
		
		Button restart = new Button("New game");
		restart.setOnAction(event -> restartGame(rows, columns, difficulty));
		restart.getStyleClass().add("menuButton");

		toolBar.getItems().addAll(clue, solve, restart, back);
		toolBar.getStyleClass().add("toolbar");
		
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
				square.getStyleClass().add("square");
				grid.add(square, i, j);
			}
		}
	}
	
	public void solve(int rows, int columns) {
		
		AudioClip audio = new AudioClip("file:sounds/selection.wav");
		audio.play();
		
		if(minesweeper.gano()) {
			
			ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.WARNING, "The game is already solved", ok);
			alert.setHeaderText(null);
			alert.setTitle(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/icon.png"));
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("/view/view.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
			alert.show();
		}
		else {
			
			ButtonType solve = new ButtonType("Solve", ButtonBar.ButtonData.OK_DONE);
			ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.WARNING, "The game will end and you lose, are you sure?", solve, cancel);
			alert.setHeaderText(null);
			alert.setTitle(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/icon.png"));
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("/view/view.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
			
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == solve) {
				
				solveGame(rows, columns);
			}	
		}
	}
	
	public void lose(int rows, int columns, int x, int y) {
		
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
				square.getStyleClass().add(setStyle(j, i));
				if(j == x && i == y) {
					square.getStyleClass().add("x");
				}
				grid.add(square, i, j);
			}
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
				square.getStyleClass().add(setStyle(j, i));
				grid.add(square, i, j);
			}
		}
	}
	
	public void loadMenu() {
			
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.setTitle("Minesweeper");
		stage.setMaximized(false);
		stage.setHeight(500);
		stage.setWidth(600);
		stage.setMinHeight(500);
		stage.setMinWidth(600);
		stage.centerOnScreen();
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
			
			if(minesweeper.getSquare(x, y).darSeleccionada()) {
				
				ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
				Alert alert = new Alert(AlertType.INFORMATION, "You can't open an already open square", ok);
				alert.setHeaderText(null);
				alert.setTitle(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/icon.png"));
				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(
				   getClass().getResource("/view/view.css").toExternalForm());
				dialogPane.getStyleClass().add("dialog");
				alert.show();	
				
			}
			else if(!minesweeper.getSquare(x, y).getMarked()) {
				
				openSquare(x, y, rows, columns, difficulty, square);
			}
			else {
				
				ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
				Alert alert = new Alert(AlertType.INFORMATION, "You can't open a marked square", ok);
				alert.setHeaderText(null);
				alert.setTitle(null);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/icon.png"));
				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(
				   getClass().getResource("/view/view.css").toExternalForm());
				dialogPane.getStyleClass().add("dialog");
				alert.show();	
			}
		}
		else if(mouse == MouseButton.SECONDARY) {
			
			if(minesweeper.getSquare(x, y).getMarked()) {
				
				minesweeper.unmarkSquare(x, y);
				square.setText(minesweeper.getSquare(x, y).mostrarValorCasilla());
				square.getStyleClass().remove("flag");
				square.getStyleClass().add(setStyle(x,y));
				AudioClip audio = new AudioClip("file:sounds/mark.wav");
				audio.play();
			}
			else {
				
				try {
					
					minesweeper.markSquare(x, y);
					square.setText("");
					square.getStyleClass().add("flag");
					AudioClip audio = new AudioClip("file:sounds/mark.wav");
					audio.play();
					
				}
				catch(AlreadySelectedException e) {
					
					ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
					Alert alert = new Alert(AlertType.INFORMATION,"You can't mark an already open square", ok);
					alert.setTitle(null);
					alert.setHeaderText(null);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("file:images/icon.png"));
					DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getStylesheets().add(
					   getClass().getResource("/view/view.css").toExternalForm());
					dialogPane.getStyleClass().add("dialog");
					alert.show();
				}
			}
		}
	}
	
	public void openSquare(int x, int y, int rows, int columns, int difficulty, Button square) {
		
		minesweeper.abrirCasilla(x, y);
		square.getStyleClass().add("number" + minesweeper.getSquare(x, y).darValor());
		square.setText(minesweeper.getSquare(x, y).mostrarValorCasilla());
		
		if(minesweeper.darPerdio()) {
			
			lose(rows, columns, x, y);
			
			ButtonType again = new ButtonType("Play again", ButtonBar.ButtonData.OK_DONE);
			ButtonType menu = new ButtonType("Return to menu", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.INFORMATION, "You lose!", again, menu);
			alert.setHeaderText(null);
			alert.setTitle(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/icon.png"));
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("/view/view.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
			
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
			Alert alert = new Alert(AlertType.INFORMATION, "You win!", again, menu);
			alert.setHeaderText(null);
			alert.setTitle(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/icon.png"));
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("/view/view.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
			
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == again) {
				
				startNewGame(difficulty);
			}
			else {
				
				loadMenu();
			}
		}
		else {
			
			AudioClip audio = new AudioClip("file:sounds/open.wav");
			audio.play();
		}
	}
	
	public void restartGame(int rows, int columns, int difficulty) {
		
		AudioClip audio = new AudioClip("file:sounds/selection.wav");
		audio.play();
		
		if(minesweeper.gano()) {
			
			loadBoard(rows, columns, difficulty);
		}
		else {
			
			ButtonType accept = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
			ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.WARNING, "All current progress will be lost, are you sure?", accept, cancel);
			alert.setTitle(null);
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/icon.png"));
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("/view/view.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
			
			Optional <ButtonType> action = alert.showAndWait();
			
			
			if(action.get() == accept) {
				
				loadBoard(rows, columns, difficulty);
			}	
		}
	}
	
	public void backToMenu() {
		
		AudioClip audio = new AudioClip("file:sounds/selection.wav");
		audio.play();
		
		if(minesweeper.gano()) {
			
			loadMenu();
		}
		else {
			
			ButtonType accept = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
			ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.WARNING, "All current progress will be lost, are you sure?", accept, cancel);
			alert.setTitle(null);
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/icon.png"));
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("/view/view.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
			
			Optional <ButtonType> action = alert.showAndWait();
			
			if(action.get() == accept) {
				
				loadMenu();
			}
		}
	}
	
	public void giveClue(int rows, int columns, int difficulty) {
		
		AudioClip audio = new AudioClip("file:sounds/selection.wav");
		audio.play();
		
		if(minesweeper.darPista().equals("")) {
			
			ButtonType accept = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
			Alert alert = new Alert(AlertType.INFORMATION, "There are no clues to give", accept);
			alert.setTitle(null);
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/icon.png"));
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("/view/view.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
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
						
						square.setText("");
					}
					
					square.setMaxWidth(Double.MAX_VALUE);
					square.setMaxHeight(Double.MAX_VALUE);
					int x = j;
					int y = i;
					square.setOnMouseClicked(MouseEvent -> onClick(x, y, rows, columns, difficulty, square, MouseEvent));
					square.getStyleClass().add(setStyle(x,y));
					if(minesweeper.getSquare(x, y).esMina()) {
						square.getStyleClass().remove("mine");
						square.getStyleClass().add("square");
					}
					grid.add(square, i, j);
				}
			}
		}
	}
	
	public String setStyle(int x, int y) {
		
		String style = "square";
		
		if(minesweeper.getSquare(x, y).darSeleccionada()) {
			
			style = "number" + minesweeper.getSquare(x, y).darValor();
		}
		if(minesweeper.getSquare(x, y).esMina() && minesweeper.getSquare(x, y).darSeleccionada()) {
			
			style = "mine";
		}
		if(minesweeper.getSquare(x, y).getMarked()) {
			
			style = "flag";
		}
		
		return style;
	}
}
