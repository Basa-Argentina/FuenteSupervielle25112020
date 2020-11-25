package com.dividato.security.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

import com.dividato.security.validadores.GroupBusquedaValidator;
import com.security.accesoDatos.interfaz.GroupService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.Group;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Group.
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
				"/iniciarGroup.html",
				"/mostrarGroup.html",
				"/eliminarGroup.html",
				"/filtrarGroup.html",
				"/borrarFiltrosGroup.html"
			}
		)
public class ListaGroupController {
	private UserService userService;
	private GroupService groupService;
	private GroupBusquedaValidator validator;
	
	/**
	 * Setea el servicio de Group.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto GroupService.
	 * La clase GroupServiceImp implementa GroupService y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setValidator(GroupBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarGroup.html",
			method = RequestMethod.GET
		)
	public String iniciarGroup(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("groupBusqueda");
		session.removeAttribute("errorEliminacion");
		return "redirect:mostrarGroup.html";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Group y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaGroup" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarGroup.html",
			method = RequestMethod.GET
		)
	public String mostrarGroup(HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		Set<Group> groups = null;	
		Group group = (Group) session.getAttribute("groupBusqueda");
		groups = new TreeSet<Group>(groupService.listarTodosGroupFiltrados(group,obtenerClienteAspUser()));			
		atributos.put("groups", groups);
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
		//hacemos el forward
		return "consultaGroup";
	}
	
	@RequestMapping(
			value="/filtrarGroup.html",
			method = RequestMethod.POST
		)
	public String filtrarGroup(@ModelAttribute("groupBusqueda") Group group, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(group, result);
		if(!result.hasErrors()){
			session.setAttribute("groupBusqueda", group);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarGroup(session,atributos);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosGroup.html",
			method = RequestMethod.GET
		)
	public String filtrarGroup(HttpSession session){
		session.removeAttribute("groupBusqueda");
		session.removeAttribute("errorEliminacion");
		return "redirect:mostrarGroup.html";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Group.
	 * 
	 * @param idGroup el id de group a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/eliminarGroup.html",
			method = RequestMethod.GET
		)
	public String eliminarGroup(HttpSession session,
			@RequestParam("idGroup") Long idGroup,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Busco el grupo
		Group group= groupService.obtenerPorId(idGroup);	
		if(group.getAdmin() != null && !group.getAdmin()){
			//eliminamos group.
			List users = userService.listarPorGrupo(group);
			if(users.isEmpty()){
				commit = groupService.delete(group);
				ScreenMessage mensaje;
				if(commit){
					mensaje = new ScreenMessageImp("notif.group.grupoEliminado", null);
					hayAvisos = true;
				}else{
					mensaje = new ScreenMessageImp("error.commitDataBase", null);
					hayAvisosNeg = true;
				}
				avisos.add(mensaje);
			}else{
				ScreenMessage mensaje = new ScreenMessageImp("error.group.grupoConUsuarios", null);
				avisos.add(mensaje);
				hayAvisosNeg = true;
			}
		}else{
			//Notifico que el grupo no puede ser eliminado
			avisos.add(new ScreenMessageImp("notif.group.deleteAdminGroup", null));
			hayAvisosNeg = true;
		}
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarGroup(session, atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}