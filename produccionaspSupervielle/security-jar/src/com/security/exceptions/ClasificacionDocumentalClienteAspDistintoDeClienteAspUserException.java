package com.security.exceptions;
/**
 * 
 * @author Luciano de Asteinza
 *
 */
public class ClasificacionDocumentalClienteAspDistintoDeClienteAspUserException extends BasaException {

	private static final long serialVersionUID = 1123L;

	public ClasificacionDocumentalClienteAspDistintoDeClienteAspUserException() {
		super("El clienteAsp del usuario logueado es diferente del clienteAsp del nodo de clasificacion documental");

	}

	public ClasificacionDocumentalClienteAspDistintoDeClienteAspUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClasificacionDocumentalClienteAspDistintoDeClienteAspUserException(String message) {
		super(message);
	}

	public ClasificacionDocumentalClienteAspDistintoDeClienteAspUserException(Throwable cause) {
		super("El clienteAsp del usuario logueado es diferente del clienteAsp del nodo de clasificacion documental",cause);
	}

}
