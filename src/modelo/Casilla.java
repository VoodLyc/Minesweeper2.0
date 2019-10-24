package modelo;
import modelo.IllegalValueException;

public class Casilla {
	
	
	// -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------
	
    /**
     * Es una constante utilizada para indicar que la casilla es de tipo Mina
     */
	public static final int MINA = 100;
	
	 /**
     * Es una constante utilizada para indicar que la casilla no es de tipo Mina
     */
	public static final int LIBRE = 50;

	// -----------------------------------------------------------------
    // Atributos y relaciones
    // -----------------------------------------------------------------

	/**
	 * Es el tipo de la casilla <Solo puede ser tipo MINA o LIBRE>
	 */
	private int tipo;
	
	/**
	 * Atributo que indica si la casilla ya fue seleccionada
	 */
	private boolean seleccionada;
	
	/**
	 * Atributo que indica la cantidad de minas que tiene alrededor una casilla.
	 */
	private int valor;

	 // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

	/**
	 * Constructor de la clase Casilla
	 * @param tipo - El tipo de la casilla
	 */
	public Casilla(int tipo) {
		
		try{
			this.tipo = tipo;
			
			if(tipo != MINA && tipo != LIBRE){

				throw new IllegalValueException("Ingrese un valor valido");
			}
		}

		catch(IllegalValueException e){

			tipo = LIBRE;
			e.printStackTrace();
		}
		seleccionada = false;
		valor = -1;
	}
	

    // -----------------------------------------------------------------
    // Metodos
    // -----------------------------------------------------------------
    
	/**
	 * Metodo modificar del atributo valor
	 * @param valor - nuevo valor
	 */
	public void modificarValor(int valor){
		this.valor = valor;
	}
	
	/**
	 * Retorna true si una casilla es de tipo Mina, false en caso contrario
	 * @return
	 */
	public boolean esMina(){

		boolean mine = false;

		if(tipo == MINA){

			mine = true;
		}

		return mine;
	}
	
	/**
	 * Genera un String que representa el valor que se debe mostrar de la casilla
	 * @return El String con la representación actual de la casilla
	 */
	public String mostrarValorCasilla(){
		
		String valor = "";
		
		if(!seleccionada){
			valor = "-";
		}else if(esMina()) {
			valor = "*";
		}else {
			valor = this.valor+"";
		}
		
		return valor;
	}
	
	/**
	 * Marca la casilla como que ya fue selecciona
	 */
	public void destapar(){
		seleccionada = true;
	}
	
	/**
	 * Metodo dar del atributo seleccionda
	 * @return el atributo
	 */
	public boolean darSeleccionada(){
		return seleccionada;
	}
	
	/**
	 * Metodo dar del atributo valor
	 * @return el valor
	 */
	public int darValor(){
		return valor;
	}

	/**
	* This method allows getting the attribute tipo
	* @return the tipo
	*/
	
	public int getTipo(){

		return tipo;
	}
	
}
