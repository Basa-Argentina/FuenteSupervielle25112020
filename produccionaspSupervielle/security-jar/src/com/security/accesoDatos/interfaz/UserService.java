package com.security.accesoDatos.interfaz;

import java.util.List;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.Persona;
import com.security.modelo.seguridad.Group;
import com.security.modelo.seguridad.PasswordHistory;
import com.security.modelo.seguridad.User;
import com.security.modelo.seguridad.UserLogin;
/**
 * 
 * @author Federico Muñoz
 * @Modificado Gabriel Mainero
 *
 */
public interface UserService extends GeneralServiceInterface<User>{
	public User obtenerPorUsername(String username);
	public List<User> listarTodosUserFiltradosByCliente(User user, ClienteAsp cliente);
	public boolean delete(User user, List<UserLogin> log, List<PasswordHistory> history);
	public boolean delete(Long id, List<UserLogin> log, List<PasswordHistory> history);
	public boolean save(User user);
	public boolean update(User user);
	public User obtenerPorUsernameRoles(String username, String roles);
	public User obtenerPorEMail(String eMail);
	public List<User> listarPorEMail(String eMail);
	public User listarPorPersona(Persona persona);
	public List<User> listarPorGrupo(Group group);
	public List<User> listarPopup(String val, ClienteAsp cliente);
	public List<User> listarPopupNoPersonal(String val, ClienteAsp cliente);
	public User obtenerPorIdNoPersonal(Long id);
}
