package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
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

import com.dividato.configuracionGeneral.validadores.DireccionBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DireccionService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.modelo.configuraciongeneral.Direccion;
import com.security.modelo.general.Pais;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Direccion.
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
				"/iniciarDireccion.html",
				"/mostrarDireccion.html",
				"/eliminarDireccion.html",
				"/filtrarDireccion.html",
				"/borrarFiltrosDireccion.html",
				"/habilitarDireccion.html",
				"/desHabilitarDireccion.html"
			}
		)
public class ListaDireccionController {
	
	private DireccionService direccionService;
	private DireccionBusquedaValidator validator;
	private PaisService paisService;
	private List<Pais> paises;
	
	@Autowired
	public void setDireccionService(DireccionService direccionService) {
		this.direccionService = direccionService;
	}
	@Autowired
	public void setValidator(DireccionBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	/**
	 * Setea el servicio de Pais.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto DireccionService.
	 * La clase PaisImp implementa Barrio y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param paisService
	 */
	@Autowired
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	@RequestMapping(
			value="/iniciarDireccion.html",
			method = RequestMethod.GET
		)
	public String iniciarDireccion(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("direccionBusqueda");
		return "redirect:mostrarDireccion.html";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Direccion y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaDireccion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarDireccion.html",
			method = RequestMethod.GET
		)
	public String mostrarDireccion(HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Direccion> direcciones = null;	
		Direccion direccion = (Direccion) session.getAttribute("direccionBusqueda");
		direcciones =(List<Direccion>) direccionService.listarTodosDireccionFiltrados(direccion);		
		atributos.put("direcciones", direcciones);
		
		paises = paisService.listarPaises();
		atributos.put("paises", paises); // los paso por get
		
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
		//hacemos el forward
		return "consultaDireccion";
	}
	
	@RequestMapping(
			value="/filtrarDireccion.html",
			method = RequestMethod.POST
		)
	public String filtrarDireccion(@ModelAttribute("direccionBusqueda") Direccion direccion, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(direccion, result);
		if(!result.hasErrors()){
			session.setAttribute("direccionBusqueda", direccion);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarDireccion(session,atributos);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosDireccion.html",
			method = RequestMethod.GET
		)
	public String filtrarDireccion(HttpSession session){
		session.removeAttribute("direccionBusqueda");
		return "redirect:mostrarDireccion.html";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Direccion.
	 * 
	 * @param idDireccion el id de Direccion a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarDireccion.html",
			method = RequestMethod.GET
		)
	public String eliminarDireccion(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la direccion para eliminar luego
		Direccion direccion = direccionService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la direccion
		commit = direccionService.eliminarDireccion(direccion);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.direccion.direccionEliminada", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarDireccion(session, atributos);
	}
	
	
	
}