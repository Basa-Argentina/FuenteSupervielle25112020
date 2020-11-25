package com.security.exceptions;
/**
 * 
 * @author Luciano de Asteinza
 *
 */
public class ClasificacionDocumentalClienteEmpDistintoDeClienteEmpUserException extends BasaException {

	private static final long serialVersionUID = 1123L;

	public ClasificacionDocumentalClienteEmpDistintoDeClienteEmpUserException() {
		super("El clienteEmp del usuario logueado es diferente del clienteEmp del nodo de clasificacion documental");

	}

	public ClasificacionDocumentalClienteEmpDistintoDeClienteEmpUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClasificacionDocumentalClienteEmpDistintoDeClienteEmpUserException(String message) {
		super(message);
	}

	public ClasificacionDocumentalClienteEmpDistintoDeClienteEmpUserException(Throwable cause) {
		super("El clienteEmp del usuario logueado es diferente del clienteEmp del nodo de clasificacion documental",cause);
	}

}
