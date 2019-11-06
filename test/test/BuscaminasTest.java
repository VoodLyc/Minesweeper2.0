package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import modelo.Buscaminas;
import modelo.Casilla;

class BuscaminasTest {
	
	private Buscaminas buscaminas;

	private void setUpStagePrincipiante() {
		
		buscaminas = new Buscaminas(Buscaminas.PRINCIPIANTE);
	}

	private void setUpStageIntermedio(){
		
		buscaminas = new Buscaminas(Buscaminas.INTERMEDIO);
	}

	private void setUpStageExperto(){

		buscaminas = new Buscaminas(Buscaminas.EXPERTO);
	}

	private void setUpStageAbrirCasilla(){

		buscaminas = new Buscaminas(Buscaminas.PRINCIPIANTE);
		Casilla[][] casillas = new Casilla[3][3];
		casillas[2][2] = new Casilla(Casilla.MINA);
		buscaminas.setCasillas(casillas);
		buscaminas.inicializarCasillasLibres();
	}

	private void setUpStageGano(){

		buscaminas = new Buscaminas(Buscaminas.PRINCIPIANTE);
		Casilla[][] casillas = new Casilla[3][3];
		casillas[0][0] = new Casilla(Casilla.MINA);
		casillas[0][1] = new Casilla(Casilla.MINA);
		casillas[0][2] = new Casilla(Casilla.MINA);
		casillas[1][0] = new Casilla(Casilla.MINA);
		casillas[1][1] = new Casilla(Casilla.LIBRE);
		casillas[1][2] = new Casilla(Casilla.MINA);
		casillas[2][0] = new Casilla(Casilla.MINA);
		casillas[2][1] = new Casilla(Casilla.MINA);
		casillas[2][2] = new Casilla(Casilla.MINA);

		buscaminas.setCasillas(casillas);
	}

	private void setUpStageDarPista(){

		buscaminas = new Buscaminas(Buscaminas.PRINCIPIANTE);
		Casilla[][] casilla = new Casilla[3][3];
		casilla[2][2] = new Casilla(Casilla.MINA);
		buscaminas.setCasillas(casilla);
		buscaminas.inicializarCasillasLibres();
	}

	private void setUpStageCantidadMinasAlrededor(){

		buscaminas = new Buscaminas(Buscaminas.PRINCIPIANTE);
		Casilla[][] casilla = new Casilla[3][3];
		casilla[2][2] = new Casilla(Casilla.MINA);
		buscaminas.setCasillas(casilla);
		buscaminas.inicializarCasillasLibres();
	}

	@Test
	void testIllegalValueException() {
		
		buscaminas = new Buscaminas(4);
		buscaminas = new Buscaminas(-1);
	}

	@Test
	void testIllegalCoordinatesException(){

		setUpStagePrincipiante();

		assertFalse(buscaminas.abrirCasilla(223,390));
		assertFalse(buscaminas.abrirCasilla(2,390));
		assertFalse(buscaminas.abrirCasilla(223,1));
		assertFalse(buscaminas.abrirCasilla(-1,-24));
		assertFalse(buscaminas.abrirCasilla(-1,3));
		assertFalse(buscaminas.abrirCasilla(4,-1));
		
		Casilla[][] casillas = buscaminas.darCasillas();

		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){

				if(!casillas[x][y].esMina()){

					assertTrue(buscaminas.abrirCasilla(x,y));
				}
			}
		}
	}

	@Test
	void testCantidadMinasAlrededor(){

		setUpStageCantidadMinasAlrededor();
		Casilla[][] casilla;
		
		assertEquals(buscaminas.cantidadMinasAlrededor(1,1), 1);
		
		casilla = buscaminas.darCasillas();
		casilla[0][0] = new Casilla(Casilla.MINA);
		buscaminas.setCasillas(casilla);

		assertEquals(buscaminas.cantidadMinasAlrededor(1,1), 2);

		casilla = new Casilla[3][3];
		casilla[0][0] = new Casilla(Casilla.MINA);
		casilla[0][1] = new Casilla(Casilla.MINA);
		casilla[0][2] = new Casilla(Casilla.MINA);
		casilla[1][0] = new Casilla(Casilla.MINA);
		casilla[1][2] = new Casilla(Casilla.MINA);
		casilla[2][0] = new Casilla(Casilla.MINA);
		casilla[2][1] = new Casilla(Casilla.MINA);
		casilla[2][2] = new Casilla(Casilla.MINA);

		buscaminas.setCasillas(casilla);
		buscaminas.inicializarCasillasLibres();

		assertEquals(buscaminas.cantidadMinasAlrededor(1,1), 8);
	}

	@Test
	void testMostrarTablero() {

		setUpStagePrincipiante();
		String board;
		board = buscaminas.mostrarTablero();
		buscaminas.resolver();

		assertNotEquals(buscaminas.mostrarTablero(), board);

		buscaminas = new Buscaminas(Buscaminas.INTERMEDIO);
		board = buscaminas.mostrarTablero();
		buscaminas.resolver();

		assertNotEquals(buscaminas.mostrarTablero(), board);
	}

	@Test
	void testDarPista() {

		setUpStageDarPista();
		
		assertEquals(buscaminas.darPista(), "Se abrio la casilla: (2 , 2)");
		assertEquals(buscaminas.darPista(), "Se abrio la casilla: (2 , 3)");
		assertEquals(buscaminas.darPista(), "Se abrio la casilla: (3 , 2)");
		assertEquals(buscaminas.darPista(), "No hay pistas para dar");
	}

	@Test
	void testGano() {

		setUpStageGano();

		assertTrue(buscaminas.gano() == false);
		buscaminas.abrirCasilla(1,1);
		assertTrue(buscaminas.gano() == true);
	}

	@Test
	void testAbrirCasilla() {

		setUpStageAbrirCasilla();
		buscaminas.abrirCasilla(1,1);
		Casilla[][] casillas = buscaminas.darCasillas();

		assertEquals(casillas[1][1].darSeleccionada(), true);
		assertEquals(buscaminas.abrirCasilla(1,1), false);

		buscaminas.abrirCasilla(2,2);
		casillas = buscaminas.darCasillas();

		assertEquals(buscaminas.darPerdio(), true);

	}

	@Test
	void testResolverPrincipiante() {

		setUpStagePrincipiante();
		buscaminas.resolver();
		int uncovers = 0;
		int covers = 0;
		Casilla[][] casillas = buscaminas.darCasillas();
		casillas[3][5] = new Casilla(Casilla.LIBRE);
				
		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){

				if(casillas[x][y].darSeleccionada() == true){
					
					uncovers++;
				}
				else {
					
					covers++;
				}
			}
		}
		
		assertEquals(uncovers, 63);
		assertEquals(covers, 1);
	}
	
	@Test
	void testResolverIntermedio() {

		setUpStageIntermedio();
		buscaminas.resolver();
		int uncovers = 0;
		int covers = 0;
		Casilla[][] casillas = buscaminas.darCasillas();
		casillas[3][5] = new Casilla(Casilla.LIBRE);
				
		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){

				if(casillas[x][y].darSeleccionada() == true){
					
					uncovers++;
				}
				else {
					
					covers++;
				}
			}
		}
		
		assertEquals(uncovers, 255);
		assertEquals(covers, 1);
	}

	@Test
	void testResolverExperto() {

		setUpStageExperto();
		buscaminas.resolver();
		int uncovers = 0;
		int covers = 0;
		Casilla[][] casillas = buscaminas.darCasillas();
		casillas[3][5] = new Casilla(Casilla.LIBRE);
				
		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){

				if(casillas[x][y].darSeleccionada() == true){
					
					uncovers++;
				}
				else {
					
					covers++;
				}
			}
		}
		
		assertEquals(uncovers, 479);
		assertEquals(covers, 1);
	}

	@Test
	void testGenerarMinasPrincipiante() {

		setUpStagePrincipiante();
		Casilla[][] casillas1 = buscaminas.darCasillas();
		Casilla[][] casillas2 = new Casilla[Buscaminas.FILAS_PRINCIPIANTE][Buscaminas.COLUMNAS_PRINCIPIANTE];
		buscaminas.setCasillas(casillas2);
		buscaminas.generarMinas();
		buscaminas.inicializarCasillasLibres();

		assertNotEquals(casillas1, casillas2);
	}

	@Test
	void testGenerarMinasIntermedio() {

		setUpStageIntermedio();
		Casilla[][] casillas1 = buscaminas.darCasillas();
		Casilla[][] casillas2 = new Casilla[Buscaminas.FILAS_INTERMEDIO][Buscaminas.COLUMNAS_INTERMEDIO];
		buscaminas.setCasillas(casillas2);
		buscaminas.generarMinas();
		buscaminas.inicializarCasillasLibres();

		assertNotEquals(casillas1, casillas2);
	}

	@Test
	void testGenerarMinasExperto() {

		setUpStageExperto();
		Casilla[][] casillas1 = buscaminas.darCasillas();
		Casilla[][] casillas2 = new Casilla[Buscaminas.FILAS_EXPERTO][Buscaminas.COLUMNAS_EXPERTO];
		buscaminas.setCasillas(casillas2);
		buscaminas.generarMinas();
		buscaminas.inicializarCasillasLibres();

		assertNotEquals(casillas1, casillas2);
	}

	@Test
	void testInicializarPartidaPrincipiante() {

		setUpStagePrincipiante();
		int minesQuantity = 0;
		int freeBoxes = 0;
		int row = 0;
		int column = 0;
		Casilla[][] casillas = buscaminas.darCasillas();

		row = casillas.length;
		column = casillas[0].length;

		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){
				
				if(casillas[x][y].esMina() == true){

					minesQuantity++;
				}
				else{

					freeBoxes++;
				}
			}
		}

		assertEquals(row, Buscaminas.FILAS_PRINCIPIANTE);	
		assertEquals(column, Buscaminas.COLUMNAS_PRINCIPIANTE);
		assertEquals(minesQuantity, Buscaminas.CANTIDAD_MINAS_PRINCIPANTE);
		assertEquals(freeBoxes, 54);
	}

	@Test
	void testInicializarPartidaIntermedio() {

		setUpStageIntermedio();
		int minesQuantity = 0;
		int freeBoxes = 0;
		int row = 0;
		int column = 0;
		Casilla[][] casillas = buscaminas.darCasillas();

		row = casillas.length;
		column = casillas[0].length;

		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){
				
				if(casillas[x][y].esMina() == true){

					minesQuantity++;
				}
				else{

					freeBoxes++;
				}
			}
		}

		assertEquals(row, Buscaminas.FILAS_INTERMEDIO);	
		assertEquals(column, Buscaminas.COLUMNAS_INTERMEDIO);
		assertEquals(minesQuantity, Buscaminas.CANTIDAD_MINAS_INTERMEDIO);
		assertEquals(freeBoxes, 216);
	}

	@Test
	void testInicializarPartidaExperto() {

		setUpStageExperto();
		int minesQuantity = 0;
		int freeBoxes = 0;
		int row = 0;
		int column = 0;
		Casilla[][] casillas = buscaminas.darCasillas();

		row = casillas.length;
		column = casillas[0].length;

		for(int x = 0; x < casillas.length; x++){
			for(int y = 0; y < casillas[0].length; y++){
				
				if(casillas[x][y].esMina() == true){

					minesQuantity++;
				}
				else{

					freeBoxes++;
				}
			}
		}

		assertEquals(row, Buscaminas.FILAS_EXPERTO);	
		assertEquals(column, Buscaminas.COLUMNAS_EXPERTO);
		assertEquals(minesQuantity, Buscaminas.CANTIDAD_MINAS_EXPERTO);
		assertEquals(freeBoxes, 381);
	}

}
