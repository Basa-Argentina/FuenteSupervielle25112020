package com.security.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.security.accesoDatos.interfaz.LicenciaService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.Licencia;
import com.security.modelo.seguridad.Authority;
import com.security.modelo.seguridad.User;
/**
 * Servicio de lectura de usuarios.
 * @author Federico Muñoz
 *
 */
public class UserDetailsService implements org.springframework.security.userdetails.UserDetailsService {	
	private UserService userService;
	private LicenciaService licenciaService;
	private String roles;
	@Autowired
	public void setUserService(UserService userService){
		this.userService=userService;
	}
	
	@Autowired
	public void setLicenciaService(LicenciaService licenciaService) {
		this.licenciaService = licenciaService;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		User user = userService.obtenerPorUsernameRoles(username,roles);
		if(user!=null) {
			List<Authority> authorities = new ArrayList<Authority>();
			//valido la licencia
			boolean bandera = true;
			for(Authority a:user.obtenerAutorizaciones()){
				if("ROLE_ASP_ADMIN".equals(a.getAuthority()))
					bandera = false;
			}
			if(bandera){ //Verifico que poseea una licencia activa si nos es admin_asp				 
				Licencia licencia = licenciaService.obtenerLicenciaPorFechaAccesoAsp(new Date(), user.getCliente());
				if(licencia == null){					
					authorities.add(new Authority("ROLE_NO_LICENSE"));
				}else{					
					authorities.addAll(user.obtenerAutorizaciones());
				}
			}else{
				authorities.addAll(user.obtenerAutorizaciones());
			}
			authorities.add(new Authority("ROLE_ANY_AUTHENTICATED"));
			user.setGrantedAuthorities(authorities.toArray(new GrantedAuthority[authorities.size()]));
			return user;
		}
		throw new UsernameNotFoundException("user not found");
	}
	
	public void setRoles(String roles){
		this.roles = roles;
	}
}
