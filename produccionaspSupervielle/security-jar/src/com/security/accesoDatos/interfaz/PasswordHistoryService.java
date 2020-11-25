package com.security.accesoDatos.interfaz;

import com.security.modelo.seguridad.PasswordHistory;
import com.security.modelo.seguridad.User;

/**
 * 
 * @author Gabriel Mainero
 *
 */
public interface PasswordHistoryService extends GeneralServiceInterface<PasswordHistory>{

	public boolean validarContraseniaAnterior(User user, String contrasenia);
	public Long diasCambioContrasenia(User user);
	
}
