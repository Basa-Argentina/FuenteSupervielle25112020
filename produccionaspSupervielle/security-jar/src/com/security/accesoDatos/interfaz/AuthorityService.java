package com.security.accesoDatos.interfaz;

import java.util.List;

import com.security.modelo.seguridad.Authority;
/**
 * 
 * @author Gabriel Mainero
 *
 */
public interface AuthorityService extends GeneralServiceInterface<Authority>{
	public List<Authority> listarTodosAuthorityFiltrados(Authority authority);
	public List<Authority> listarPorAuthority(String[] authority);
	public boolean eliminar(String authority);
	public Authority obtenerPorAuthority(String authority);
	public List<Authority> listAuthorityExceptAuthority(String authority);		
}
