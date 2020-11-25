package com.security.accesoDatos.interfaz;

import java.util.List;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.Group;

/**
 * 
 * @author Federico Muñoz
 * @Modificado Gabriel Mainero
 * @Modificado Ezequiel Beccaria
 *
 */
public interface GroupService extends GeneralServiceInterface<Group>{
	public List<Group> listarTodosGroupFiltrados(Group group, ClienteAsp cliente);	
	public Boolean save(Group group);
	public Boolean update(Group group);
	public Boolean delete(Group group);
	public List<Group> listarPorId(Long[] ids, ClienteAsp cliente);	
}
