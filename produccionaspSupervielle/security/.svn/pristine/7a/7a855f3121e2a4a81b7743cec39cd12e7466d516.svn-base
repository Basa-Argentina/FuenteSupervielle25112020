package com.dividato.security.controladores;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.security.validadores.AuthorityBusquedaValidator;
import com.security.accesoDatos.interfaz.AuthorityService;
import com.security.modelo.seguridad.Authority;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Authority.
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
				"/iniciarAuthority.html",
				"/mostrarAuthority.html",
				"/eliminarAuthority.html",
				"/filtrarAuthority.html",
				"/borrarFiltrosAuthority.html"
			}
		)
public class ListaAuthorityController {
	
	private AuthorityService authorityService;
	private AuthorityBusquedaValidator validator;
	
	@Autowired
	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}
	@Autowired
	public void setValidator(AuthorityBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarAuthority.html",
			method = RequestMethod.GET
		)
	public String iniciarAuthority(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("errorEliminacion");
		return "redirect:mostrarAuthority.html";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Authority y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaAuthority" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarAuthority.html",
			method = RequestMethod.GET
		)
	public String mostrarAuthority(HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Authority> authoritys = null;
		if(session.getAttribute("authorityBusqueda")==null)
//			authoritys=(List<Authority>) authorityService.listarTodos();
			authoritys=(List<Authority>) authorityService.listAuthorityExceptAuthority("ROLE_ASP_ADMIN");
		else{
			Authority authority = (Authority) session.getAttribute("authorityBusqueda");
			authoritys =(List<Authority>) authorityService.listarTodosAuthorityFiltrados(authority);
		}
			
		atributos.put("authoritys", authoritys);
		//hacemos el forward
		return "consultaAuthority";
	}
	
	@RequestMapping(
			value="/filtrarAuthority.html",
			method = RequestMethod.POST
		)
	public String filtrarAuthority(@ModelAttribute("authorityBusqueda") Authority authority, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(authority, result);
		if(!result.hasErrors()){
			session.setAttribute("authorityBusqueda", authority);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
//		return "redirect:iniciarAuthority.html";
		return mostrarAuthority(session,atributos);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosAuthority.html",
			method = RequestMethod.GET
		)
	public String filtrarAuthority(HttpSession session){
		session.removeAttribute("authorityBusqueda");
		session.removeAttribute("errorEliminacion");
		return "redirect:mostrarAuthority.html";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Authority.
	 * 
	 * @param idAuthority el id de authority a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarAuthority.html",
			method = RequestMethod.GET
		)
	public String eliminarAuthority(HttpSession session,
			@RequestParam("authorityname") String authority,
			Map<String,Object> atributos) {
	
		//eliminamos authority.
		if(!authorityService.eliminar(authority))
			session.setAttribute("errorEliminacion", "formularioAuthority.errorEliminacion");
		else
			session.removeAttribute("errorEliminacion");
		return "redirect:mostrarAuthority.html";
		//return mostrarAuthority(session,atributos);
	}
	
}