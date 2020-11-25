package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
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

import com.dividato.configuracionGeneral.validadores.PlantillaFacturacionBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de PlantillaFacturacion.
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
				"/iniciarPlantillaFacturacion.html",
				"/mostrarPlantillaFacturacion.html",
				"/filtrarPlantillaFacturacion.html",
				"/eliminarPlantillaFacturacion.html",
				"/mostrarPlantillaFacturacionSinFiltrar.html"
			}
		)
public class ListaPlantillaFacturacionController {
	
	private PlantillaFacturacionService plantillaFacturacionService;
	private PlantillaFacturacionBusquedaValidator validator;
	
	@Autowired
	public void setPlantillaFacturacionService(PlantillaFacturacionService plantillaFacturacionService) {
		this.plantillaFacturacionService = plantillaFacturacionService;
	}
	@Autowired
	public void setValidator(PlantillaFacturacionBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	/**
	 * Setea el servicio de Depositos.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto PlantillaFacturacionService.
	 * La clase DepositoServiceImp implementa Deposito y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param depositoService
	 */
		
	
	@RequestMapping(
			value="/iniciarPlantillaFacturacion.html",
			method = RequestMethod.GET
		)
	public String iniciarPlantillaFacturacion(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("plantillaFacturacionBusqueda");
		session.removeAttribute("plantillaFacturacionSession");
		atributos.remove("plantillaFacturacion");
		session.removeAttribute("tablaPaginada");
		return "redirect:mostrarPlantillaFacturacionSinFiltrar.html";
	}
	
	
	@RequestMapping(
			value="/mostrarPlantillaFacturacionSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarPlantillaFacturacionSinFiltrar(HttpSession session,
			Map<String,Object> atributos){
		
		//buscamos en la base de datos y lo subimos a request.
		@SuppressWarnings("unchecked")
		List<PlantillaFacturacion> plantillasFacturacion = (List<PlantillaFacturacion>)session.getAttribute("plantillasFacturacionSession"); 
		if(plantillasFacturacion==null){
			plantillasFacturacion = new ArrayList<PlantillaFacturacion>();
		}
		
		PlantillaFacturacion plantillaFacturacion = (PlantillaFacturacion) session.getAttribute("plantillaFacturacionBusqueda");
		if(plantillaFacturacion == null){
			plantillaFacturacion = new PlantillaFacturacion();
		}
		
		session.setAttribute("plantillasFacturacionSession", plantillasFacturacion);
		atributos.put("plantillasFacturacion",plantillasFacturacion);
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("size", Integer.valueOf(0));
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());

		//hacemos el forward
		return "consultaPlantillaFacturacion";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de PlantillaFacturacion y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaPlantillaFacturacion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarPlantillaFacturacion.html",
			method = RequestMethod.GET
		)
	public String mostrarPlantillaFacturacion(HttpSession session,
			Map<String,Object> atributos,HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List<PlantillaFacturacion> plantillasFacturacion = null;
		
		 
		 PlantillaFacturacion plantillaFacturacion = (PlantillaFacturacion) session.getAttribute("plantillaFacturacionBusqueda");
		 if(plantillaFacturacion == null)
		 {
			 plantillaFacturacion = new PlantillaFacturacion();
		 }
					
			//cuenta la cantidad de resultados
			Long size = plantillaFacturacionService.contarObtenerPor(
					obtenerClienteAspUser(), plantillaFacturacion.getClienteCodigo(), plantillaFacturacion.getCodigoSerie(), 
					plantillaFacturacion.getListaPreciosCodigo(),plantillaFacturacion.getTipoComprobanteId(), plantillaFacturacion.getHabilitado());
			atributos.put("size", size.intValue());
			
			//paginacion y orden de resultados de displayTag
			plantillaFacturacion.setTamañoPagina(20);		
			String nPaginaStr= request.getParameter((new ParamEncoder("plantillaFacturacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("plantillaFacturacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("plantillaFacturacion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			plantillaFacturacion.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("plantillaFacturacion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			plantillaFacturacion.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			plantillaFacturacion.setNumeroPagina(nPagina);
			
			//Se busca en la base de datos los plantillasFacturacion con los filtros de paginacion agregados a la plantillaFacturacion
			plantillasFacturacion =(List<PlantillaFacturacion>) plantillaFacturacionService.obtenerPor(obtenerClienteAspUser(),
					plantillaFacturacion.getClienteCodigo(),plantillaFacturacion.getCodigoSerie(), plantillaFacturacion.getListaPreciosCodigo(),
					plantillaFacturacion.getTipoComprobanteId(),plantillaFacturacion.getHabilitado(),plantillaFacturacion.getFieldOrder(),
					plantillaFacturacion.getSortOrder(),plantillaFacturacion.getNumeroPagina(),plantillaFacturacion.getTamañoPagina());
								
		
		session.setAttribute("plantillasFacturacionSession", plantillasFacturacion);
		atributos.put("plantillasFacturacion", plantillasFacturacion);
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());
		atributos.put("clienteId", obtenerClienteAspUser().getId());
	
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
	
	
		//hacemos el forward
		return "consultaPlantillaFacturacion";
	}
	
	@RequestMapping(
			value="/filtrarPlantillaFacturacion.html",
			method = RequestMethod.POST
		)
	public String filtrarPlantillaFacturacion(@ModelAttribute("plantillaFacturacionBusqueda") PlantillaFacturacion plantillaFacturacion, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(plantillaFacturacion, result);
		if(!result.hasErrors()){
			session.setAttribute("plantillaFacturacionBusqueda", plantillaFacturacion);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarPlantillaFacturacion(session,atributos,request);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Plantilla.
	 * 
	 * @param idRemito el id de Plantilla a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarPlantillaFacturacion.html",
			method = RequestMethod.GET
		)
	public String eliminarPlantillaFacturacion(HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la remito para eliminar luego
		PlantillaFacturacion plantillaFacturacion = plantillaFacturacionService.obtenerPorId(id);
		
		//Eliminamos la plantilla
		commit = plantillaFacturacionService.eliminarPlantillaFacturacion(plantillaFacturacion);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("formularioPlantillaFacturacion.notif.plantillaEliminadaExito", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarPlantillaFacturacion(session, atributos, request);
	}

	/////////////////////METODOS DE SOPORTE/////////////////////////////
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Empresa obtenerEmpresaUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}