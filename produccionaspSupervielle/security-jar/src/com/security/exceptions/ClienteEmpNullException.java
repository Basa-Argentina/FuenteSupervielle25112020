package com.security.exceptions;
/**
 * 
 * @author Luciano de Asteinza
 *
 */
public class ClienteEmpNullException extends BasaException {

	private static final long serialVersionUID = 1123L;

	public ClienteEmpNullException() {
		super("El clienteEmp es nulo o no existe en la base de datos");

	}

	public ClienteEmpNullException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClienteEmpNullException(String message) {
		super(message);
	}

	public ClienteEmpNullException(Throwable cause) {
		super("El clienteEmp es nulo o no existe en la base de datos",cause);
	}

}
