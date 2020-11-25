package com.security.exceptions;
/**
 * 
 * @author Luciano de Asteinza
 *
 */
public class EmpleadoClienteEmpDistintoDeClienteEmpUserException extends BasaException {

	private static final long serialVersionUID = 1123L;

	public EmpleadoClienteEmpDistintoDeClienteEmpUserException() {
		super("El clienteEmp del usuario logueado es diferente del clienteEmp del empleado");

	}

	public EmpleadoClienteEmpDistintoDeClienteEmpUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmpleadoClienteEmpDistintoDeClienteEmpUserException(String message) {
		super(message);
	}

	public EmpleadoClienteEmpDistintoDeClienteEmpUserException(Throwable cause) {
		super("El clienteEmp del usuario logueado es diferente del clienteEmp del empleado",cause);
	}

}
