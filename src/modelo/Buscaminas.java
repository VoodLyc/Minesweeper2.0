package modelo;
import modelo.IllegalCoordinatesException;

public class Buscaminas {


	// -----------------------------------------------------------------
	// Constantes
	// -----------------------------------------------------------------

	/**
	 * Es una constante utilizada para indicar la cantidad de filas en el nivel principiante
	 */
	public static final int FILAS_PRINCIPIANTE = 8;

	/**
	 * Es una constante utilizada para indicar la cantidad de filas en el nivel intermedio
	 */
	public static final int FILAS_INTERMEDIO = 16;

	/**
	 * Es una constante utilizada para indicar la cantidad de filas en el nivel experto
	 */
	public static final int FILAS_EXPERTO = 16;

	/**
	 * Es una constante utilizada para indicar la cantidad de columnas en el nivel principiante
	 */
	public static final int COLUMNAS_PRINCIPIANTE = 8;

	/**
	 * Es una constante utilizada para indicar la cantidad de columnas en el nivel intermedio
	 */
	public static final int COLUMNAS_INTERMEDIO = 16;

	/**
	 * Es una constante utilizada para indicar la cantidad de columnas en el nivel experto
	 */
	public static final int COLUMNAS_EXPERTO = 30;

	/**
	 * Es una constante utilizada para saber la dificultad del juego, representa el nivel principiante
	 */
	public static final int PRINCIPIANTE = 1;

	/**
	 * Es una constante utilizada para saber la dificultad del juego, representa el nivel intermedio
	 */
	public static final int INTERMEDIO = 2;

	/**
	 * Es una constante utilizada para saber la dificultad del juego, representa el nivel experto
	 */
	public static final int EXPERTO = 3;

	/**
	 * Es una constante utilizada para saber la cantidad de minas en nivel principiante
	 */
	public static final int CANTIDAD_MINAS_PRINCIPANTE = 10;

	/**
	 * Es una constante utilizada para saber la cantidad de minas en nivel intermedio
	 */
	public static final int CANTIDAD_MINAS_INTERMEDIO = 40;

	/**
	 * Es una constante utilizada para saber la cantidad de minas en nivel experto
	 */
	public static final int CANTIDAD_MINAS_EXPERTO = 99;

	// -----------------------------------------------------------------
	// Atributos y relaciones
	// -----------------------------------------------------------------

	/**
	 * Relacion que tiene la matriz de casillas
	 */
	private Casilla[][] casillas;

	/**
	 * Atributo que representa el nivel del juego <Solo puede tomar valores PRINCIPIANTE, INTERMEDIO, EXPERTO>
	 */
	private int nivel;

	/**
	 * Atributo que tiene la cantidad de minas en el tablero
	 */
	private int cantidadMinas;

	/**
	 * Atributo que representa si el usuario perdio al abrir una mina
	 */
	private boolean perdio;
	
	// -----------------------------------------------------------------
	// Constructores
	// -----------------------------------------------------------------

	/**
	 * Constructo de la clase Buscaminas
	 * @param nivel - el nivel seleccionado por el usuario
	 */
	public Buscaminas(int nivel) {
		
		try{

			this.nivel = nivel;

			if(nivel != 1 && nivel != 2 && nivel != 3){

				throw new IllegalLevelException("Ese nivel de dificultad no existe");
			}
		}
		catch(IllegalLevelException e){

			nivel = 1;
			e.printStackTrace(); 
		}
		perdio = false;
		inicializarPartida();

	}


	// -----------------------------------------------------------------
	// Metodos
	// -----------------------------------------------------------------

	/**
	 * Se encarga de inicializar los atributos y relaciones de la clase buscaminas a partir del nivel elegido por el usuario
	 */
	private void inicializarPartida() {

		if(nivel == PRINCIPIANTE){

			casillas = new Casilla[FILAS_PRINCIPIANTE][COLUMNAS_PRINCIPIANTE];
			cantidadMinas = CANTIDAD_MINAS_PRINCIPANTE;
		}

		else if(nivel == INTERMEDIO){

			casillas = new Casilla[FILAS_INTERMEDIO][COLUMNAS_INTERMEDIO];
			cantidadMinas = CANTIDAD_MINAS_INTERMEDIO;
		}

		else{

			casillas = new Casilla[FILAS_EXPERTO][COLUMNAS_EXPERTO];
			cantidadMinas = CANTIDAD_MINAS_EXPERTO;
		}

		generarMinas();
		inicializarCasillasLibres();
	}


	/**
	 * Metodo que se encarga de inicializar todas las casillas que no son minas
	 */
	public void inicializarCasillasLibres() {

		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){

				if(casillas[x][y] == null){

					casillas[x][y] = new Casilla(Casilla.LIBRE);
				}
			}
		}

		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){

				if(casillas[x][y].esMina() == false){

					casillas[x][y].modificarValor(cantidadMinasAlrededor(x,y));
				}
			}
		}
	}

	/**
	 * Metodo que permite contar la cantidad de minas que tiene alrededor una casillas
	 * @param i - La fila de la matriz
	 * @param j - la columna de la matriz
	 * @return int - La cantidad de minas que tiene alrededor la casilla [i][j]
	 */
	public int cantidadMinasAlrededor(int x, int y) { 
        
        int mines = 0;

        for(int i = (x - 1); i <= (x + 1); i++){
            for(int j = (y - 1); j <= (y + 1); j++){
                if(i >= 0 && i < casillas.length && j >= 0 && j < casillas[0].length){
                    if(casillas[i][j].esMina() == true){
                        
                        mines++;
                    }
                }
            }
        }
        return mines;
    }

	/**
	 * Método que se encarga de generar aleatoriomente las minas
	 */
	public void generarMinas() {

		boolean generating = true;
		int rowLimit, columnLimit, x, y;
		int counter = 0;		

		rowLimit = casillas.length;
		columnLimit = casillas[0].length;

		while(generating){

			x = (int) (Math.random() * rowLimit);
			y = (int) (Math.random() * columnLimit);

			if(casillas[x][y] == null){

				casillas[x][y] = new Casilla(Casilla.MINA);
				counter++;

				if(counter == cantidadMinas){

					generating = false;
				}
			}		
	    }
    }


	/**
	 * Metodo que se encarga de convertir el tablero a un String para poder verlo en pantalla
	 * @return String - El tablero en formato String
	 */
	public String mostrarTablero() {

		String board = "  ";

		for(int i = 1; i < (casillas[0].length + 1); i++){
			
			if( i < 10){

			board += "  " + i;
			
			}
			else {

				board += " " + i;
			}
		}

		board += "\n";

		for(int x = 0; x < casillas.length; x++){
			if(x < 9){

				board += (x + 1) + " ";
			}
			else{

				board += (x + 1);
			}
			for(int y = 0; y < casillas[0].length; y++){

					board += "  " + casillas[x][y].mostrarValorCasilla() ;

			}

			board += "\n";
		}

		return board;
	}


	/**
	 * Metodo que se encarga de marcar todas las casillas como destapadas
	 */
	public void resolver() {

		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){

					casillas[x][y].destapar();
				}
			}
		}

	/**
	 * Metodo dar del atributo casillas
	 * @return la relacion casillas
	 */
	public Casilla[][] darCasillas(){
		return casillas;
	}

	/**
	* This method allows setting the attribute casilla.
	*/
	public void setCasillas(Casilla[][] x){

		casillas = x;
	}

	/**
	 * Este metodo se encarga de abrir una casilla
	 * Si se abre una casilla de tipo Mina, se marca que el jugador perdio el juego.
	 * @param i - la fila donde esta la casilla 
	 * @param j - la columna donde esta la casilla
	 * @return boolean - true si fue posible destaparla, false en caso contrario
	 */
	public boolean abrirCasilla(int i, int j) {

		boolean uncover = false;

		if(i >= 0 && i < casillas.length && j >= 0 && j < casillas[0].length){

			if(casillas[i][j].darSeleccionada() != true){
				if(casillas[i][j].getTipo() == Casilla.LIBRE){
				
					casillas[i][j].destapar();
					uncover = true;
				}
				else{

					perdio = true;
				}
			}

		}

		else{

			try{

				throw new IllegalCoordinatesException("No se puede abrir la casilla porque no existe");
			}
			catch(IllegalCoordinatesException e){

				e.printStackTrace();
			}
		}

		return uncover;
	}


	/**
	 * Metodo que se encarga de revisar si el jugador gano el juego
	 * @return boolean - true si gano el juego, false en caso contrario
	 */
	public boolean gano() {

		boolean won = true;

        for(int x = 0; x < casillas.length && won; x++){
            for(int y = 0; y < casillas[0].length && won; y++){

                if(!casillas[x][y].esMina() && !casillas[x][y].darSeleccionada()){
                    
                    won = false;
                }

            }
        }

        return won;
	}


	/**
	 * Metodo que se encarga de abrir la primera casilla que no sea una Mina y cuyo valor sea Mayor que 0
	 * @return String, Mensaje de la Casilla que marco abierta, En caso de no haber casillas posibles para dar una pista, retorna el mensaje no hay pistas para dar
	 */
	public String darPista() {

		String msg = "";
		boolean running = true;

		for(int x = 0; x < casillas.length && running == true; x++){
			for(int y = 0; y < casillas[0].length && running == true; y++){

				if(casillas[x][y].esMina() != true && casillas[x][y].darSeleccionada() == false && casillas[x][y].darValor() > 0){

					casillas[x][y].destapar();
					unmarkSquare(x,y);
					msg = "Se abrio la casilla (" + (x+1) + ")"+ "(" + (y+1) + ")";
					running = false;
				}
			}
		}

		return msg;
	}
	
	/**
	 * This method allows marking a square.
	 * @param x The column number.
	 * @param y The row number.
	 */
	
	public void markSquare(int x, int y) {
		
		casillas[x][y].setMarked(true);
	}
	
	/**
	 * This method allows unmarking a square.
	 * @param x The column number.
	 * @param y The row number.
	 */
	
	public void unmarkSquare(int x, int y) {
		
		casillas[x][y].setMarked(false);
	}
	
	/***
	 * Metodo dar del atributo perdio
	 * @return boolean el atributo
	 */
	public boolean darPerdio(){
		return perdio;
	}
	
	public Casilla getSquare(int x, int y) {
		
		return casillas[x][y];
	}
}
