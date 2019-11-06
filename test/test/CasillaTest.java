package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import modelo.Casilla;

class CasillaTest {
	
	private Casilla casilla;
	
	private void setUpStageCasillaLibre() {
		
		casilla = new Casilla(Casilla.LIBRE);
	}
	
	private void setUpStageMina() {
		
		casilla = new Casilla(Casilla.MINA);
	}
	
	private void setUpStageIllegalValueException(){
		
		casilla = new Casilla(200);
	}

	@Test
	void testIllegalValueException() {

		setUpStageIllegalValueException();
		assertFalse(casilla.esMina());
	}

	@Test
	void testDarValor() {
		
		setUpStageCasillaLibre();
		assertEquals(casilla.darValor(), -1);		
	}

	@Test
	void testModificarValor() {

		setUpStageCasillaLibre();
		casilla.modificarValor(3);
		assertEquals(casilla.darValor(), 3);
	}

	@Test
	void testDestapar() {
		
		setUpStageCasillaLibre();
		casilla.destapar();
		assertEquals(casilla.darSeleccionada(), true);
	}

	@Test
	void testDarSeleccionada() {

		setUpStageCasillaLibre();
		assertEquals(casilla.darSeleccionada(), false);
		casilla.destapar();
		assertEquals(casilla.darSeleccionada(), true);
	}

	@Test
	void testGetTipo() {

		setUpStageCasillaLibre();
		assertEquals(casilla.getTipo(), Casilla.LIBRE);
	}
	
	@Test
	void testEsMina() {

		setUpStageMina();
		assertEquals(casilla.esMina(), true);
	}

	@Test
	void testMostrarValorCasilla() {

		setUpStageCasillaLibre();
		assertEquals(casilla.mostrarValorCasilla(), "-");
		casilla.destapar();
		assertEquals(casilla.mostrarValorCasilla(), casilla.darValor()+"");

		setUpStageMina();
		assertEquals(casilla.mostrarValorCasilla(), "-");
		casilla.destapar();
		assertEquals(casilla.mostrarValorCasilla(), "*");
	}

}
