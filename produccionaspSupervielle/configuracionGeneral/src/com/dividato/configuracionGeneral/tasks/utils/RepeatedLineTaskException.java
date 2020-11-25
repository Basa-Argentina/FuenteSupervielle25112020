package com.dividato.configuracionGeneral.tasks.utils;
/**
 * Excepción en el procesamiento de los archivos de entrada.
 * @author Victor Kenis
 *
 */
public class RepeatedLineTaskException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param message
	 * 
	 */
	public RepeatedLineTaskException(String message, Long ... extraData) {
		super(message);
	}

}
