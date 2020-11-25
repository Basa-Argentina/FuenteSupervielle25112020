package com.dividato.security.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.security.validadores.UserBusquedaValidator;
import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.interfaz.PasswordHistoryService;
import com.security.accesoDatos.interfaz.UserLoginService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.seguridad.PasswordHistory;
import com.security.modelo.seguridad.User;
import com.security.modelo.seguridad.UserLogin;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarUser.html",
				"/mostrarUser.html",
				"/eliminarUser.html",
				"/filtrarUser.html",
				"/habilitarUser.html",
				"/desHabilitarUser.html"
			}
		)
public class ListaUserController {
	
	private UserService userService;
	private UserLoginService userLoginService;
	private PasswordHistoryService passwordHistoryService;
	private UserBusquedaValidator validator;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setUserLoginService(UserLoginService userLoginService) {
		this.userLoginService = userLoginService;
	}

	@Autowired
	public void setPasswordHistoryService(
			PasswordHistoryService passwordHistoryService) {
		this.passwordHistoryService = passwordHistoryService;
	}

	@Autowired
	public void setValidator(UserBusquedaValidator validator) {
		this.validator = validator;
	}

	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarUser.html",
			method = RequestMethod.GET
		)
	public String iniciar(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("userBusqueda");
		return "redirect:mostrarUser.html";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de User y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarUser.html",
			method = RequestMethod.GET
		)
	public String mostrar(HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<User> users = null;
		User user = (User) session.getAttribute("userBusqueda");
		users =(List<User>) userService.listarTodosUserFiltradosByCliente(user,obtenerClienteAspUser());	
		atributos.put("users", users);
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
		//hacemos el forward
		return "consultaUser";
	}
	
	@RequestMapping(
			value="/filtrarUser.html",
			method = RequestMethod.POST
		)
	public String filtrar(@ModelAttribute("userBusqueda") User user, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(user, result);
		if(!result.hasErrors()){
			session.setAttribute("userBusqueda", user);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(session,atributos);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar User.
	 * 
	 * @param idUser el id de user a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarUser.html",
			method = RequestMethod.GET
		)
	public String eliminar(HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//Busco el usuario
		User user = userService.obtenerPorId(id);
		if(user.getAdmin() != null && !user.getAdmin() && !(user instanceof Empleado)){
			List<UserLogin> logUser = userLoginService.listarPorUser(user);
			List<PasswordHistory> passHist = 
				passwordHistoryService.listarTodosFiltradoPorLista(new CampoComparacion("user", user));
			//eliminamos user
			commit = userService.delete(id, logUser, passHist);
			ScreenMessage mensaje;
			if(commit){
				mensaje = new ScreenMessageImp("notif.user.eliminado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.commitDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
		}else{
			//Notifico que el usuario no puede ser eliminado
			avisos.add(new ScreenMessageImp("notif.user.deleteAdminUser", null));
			hayAvisosNeg = true;
		}
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(session,atributos);
	}
	
	@RequestMapping(
			value="/habilitarUser.html",
			method = RequestMethod.GET
		)
	public String habilitarUser(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
	
		User user = userService.obtenerPorId(Long.valueOf(id));
		user.setEnable(true);
		userService.actualizar(user);
		return "redirect:mostrarUser.html";
	}
	
	@RequestMapping(
			value="/desHabilitarUser.html",
			method = RequestMethod.GET
		)
	public String desHabilitarUser(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
	
		User user = userService.obtenerPorId(Long.valueOf(id));
		user.setEnable(false);
		userService.actualizar(user);
		return "redirect:mostrarUser.html";
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}