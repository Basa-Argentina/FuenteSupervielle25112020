package com.security.exceptions;
/**
 * 
 * @author Luciano de Asteinza
 *
 */
public class ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException extends BasaException {

	private static final long serialVersionUID = 1123L;

	public ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException() {
		super("El elemento a reposicionar contiene un deposito local distinto del deposito destino");
	}

	public ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException(String message) {
		super(message);
	}

	public ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException(Throwable cause) {
		super("El elemento a reposicionar contiene un deposito local distinto del deposito destino",cause);
	}

}
