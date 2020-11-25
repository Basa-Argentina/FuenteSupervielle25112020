package com.dividato.configuracionGeneral.tasks.utils;
/**
 * Excepción en el procesamiento de los archivos de entrada.
 * @author Gabriel Mainero
 *
 */
public class ScheduledTaskException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param message
	 * 
	 */
	public ScheduledTaskException(String message, Long ... extraData) {
		super(message);
	}

}
