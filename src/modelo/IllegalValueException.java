package modelo;
import java.lang.Exception;

public class IllegalValueException extends Exception{
	
	public IllegalValueException(String msg){
		super(msg);
	}
}
