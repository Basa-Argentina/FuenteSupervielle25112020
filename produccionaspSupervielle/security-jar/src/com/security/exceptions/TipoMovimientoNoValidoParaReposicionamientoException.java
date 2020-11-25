package com.security.exceptions;
/**
 * 
 * @author Luciano de Asteinza
 *
 */
public class TipoMovimientoNoValidoParaReposicionamientoException extends BasaException {

	private static final long serialVersionUID = 1123L;

	public TipoMovimientoNoValidoParaReposicionamientoException() {
		super("El tipo de movimiento no es valido para el proceso de reposicionamiento");
	}

	public TipoMovimientoNoValidoParaReposicionamientoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TipoMovimientoNoValidoParaReposicionamientoException(String message) {
		super(message);
	}

	public TipoMovimientoNoValidoParaReposicionamientoException(Throwable cause) {
		super("El tipo de movimiento no es valido para el proceso de reposicionamiento",cause);
	}

}
