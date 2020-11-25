package com.dividato.configuracionGeneral.controladores;

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

import com.dividato.configuracionGeneral.validadores.TipoElementoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de TipoElemento.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarTipoElemento.html",
				"/mostrarTipoElemento.html",
				"/eliminarTipoElemento.html",
				"/filtrarTipoElemento.html",
				"/borrarFiltrosTipoElemento.html"
			}
		)
public class ListaTipoElementosController {
	
	private TipoElementoService tipoElementoService;
	private TipoElementoBusquedaValidator validator;
	
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	@Autowired
	public void setValidator(TipoElementoBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarTipoElemento.html",
			method = RequestMethod.GET
		)
	public String iniciarTipoElemento(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("tipoElementoBusqueda");
		return "redirect:mostrarTipoElemento.html";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de TipoElemento y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaTipoElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarTipoElemento.html",
			method = RequestMethod.GET
		)
	public String mostrarTipoElemento(HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<TipoElemento> tipoElementos = null;	
		TipoElemento tipoElemento = (TipoElemento) session.getAttribute("tipoElementoBusqueda");
		tipoElementos =(List<TipoElemento>) tipoElementoService.listarTipoElementoFiltrados(tipoElemento, obtenerClienteAspUser());		
		atributos.put("tipoElementos", tipoElementos);
	
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
		//hacemos el forward
		return "consultaTipoElemento";
	}
	
	@RequestMapping(
			value="/filtrarTipoElemento.html",
			method = RequestMethod.POST
		)
	public String filtrarTipoElemento(@ModelAttribute("tipoElementoBusqueda") TipoElemento tipoElemento, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(tipoElemento, result);
		if(!result.hasErrors()){
			session.setAttribute("tipoElementoBusqueda", tipoElemento);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarTipoElemento(session,atributos);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosTipoElemento.html",
			method = RequestMethod.GET
		)
	public String filtrarTipoElemento(HttpSession session){
		session.removeAttribute("tipoElementoBusqueda");
		return "redirect:mostrarTipoElemento.html";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar TipoElemento.
	 * 
	 * @param idTipoElemento el id de TipoElemento a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarTipoElemento.html",
			method = RequestMethod.GET
		)
	public String eliminarTipoElemento(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la tipoElemento para eliminar luego
		TipoElemento tipoElemento = tipoElementoService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la tipoElemento
		commit = tipoElementoService.delete(tipoElemento);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.tipoElemento.tipoElementoEliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarTipoElemento(session, atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}