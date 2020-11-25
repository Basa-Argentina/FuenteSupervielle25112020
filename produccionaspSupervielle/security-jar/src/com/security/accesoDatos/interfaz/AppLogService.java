package com.security.accesoDatos.interfaz;

import java.util.Date;
import java.util.List;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.AppLog;
/**
 * 
 * @author Federico Muñoz
 *
 */
public interface AppLogService extends GeneralServiceInterface<AppLog>{

	public List<AppLog> listarTodosAppLogFiltrados(String nivel, Date fecha, String aplicacion, ClienteAsp cliente);
	public void eliminarInsModAppLog(List<AppLog> lista);

}
