package com.security.exceptions;
/**
 * 
 * @author Luciano de Asteinza
 *
 */
public class ClasificacionDocumentalException extends BasaException {

	private static final long serialVersionUID = 1123L;

	public ClasificacionDocumentalException() {
		super("Ha ocurrido un error durante la gestion de clasificacion documental");

	}

	public ClasificacionDocumentalException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClasificacionDocumentalException(String message) {
		super(message);
	}

	public ClasificacionDocumentalException(Throwable cause) {
		super("Ha ocurrido un error durante la gestion de clasificacion documental",cause);
	}

}
