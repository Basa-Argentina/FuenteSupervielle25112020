package com.dividato.configuracionGeneral.controladores;

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

import com.dividato.configuracionGeneral.validadores.ElementoHistoricoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoHistoricoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaHistoricoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ElementoHistorico;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.ReferenciaHistorico;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
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
				"/iniciarHistorico.html",
				"/mostrarHistorico.html",
				"/filtrarHistoricoElemento.html",
				"/filtrarHistoricoReferencia.html",
				"/mostrarHistoricoSinFiltrar.html"
			}
		)
public class ListaHistoricosController {
	
	private ElementoHistoricoService elementoHistoricoService;
	private ReferenciaHistoricoService referenciaHistoricoService;
	private ElementoHistoricoBusquedaValidator validator;
	
	@Autowired
	public void setElementoHistoricoService(ElementoHistoricoService elementoHistoricoService) {
		this.elementoHistoricoService = elementoHistoricoService;
	}
	@Autowired
	public void setReferenciaHistoricoService(ReferenciaHistoricoService referenciaHistoricoService) {
		this.referenciaHistoricoService = referenciaHistoricoService;
	}
	@Autowired
	public void setValidator(ElementoHistoricoBusquedaValidator validator) {
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
			value="/iniciarHistorico.html",
			method = RequestMethod.GET
		)
	public String iniciarHistorico(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="tipoHistorico") String tipoHistorico){
		
		session.removeAttribute("historicoBusqueda");
		
		atributos.put("tipoHistorico", tipoHistorico);
		return "redirect:mostrarHistoricoSinFiltrar.html";
	}
	
	
	@RequestMapping(
			value="/mostrarHistoricoSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarHistoricoSinFiltrar(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="tipoHistorico") String tipoHistorico){
		
		String rutaHistorico = "";
		
		if(tipoHistorico!=null && !tipoHistorico.equals("")){
			if(tipoHistorico.equalsIgnoreCase("elemento"))
				rutaHistorico = "filtrarHistoricoElemento.html";
			if(tipoHistorico.equalsIgnoreCase("referencia"))
				rutaHistorico = "filtrarHistoricoReferencia.html";
		}
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("rutaHistorico", rutaHistorico);
		atributos.put("tipoHistorico", tipoHistorico);
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("size", Integer.valueOf(0));
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());

		//hacemos el forward
		return "consultaHistorico";
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
			value="/mostrarHistorico.html",
			method = RequestMethod.GET
		)
	public String mostrarHistorico(HttpSession session,
			Map<String,Object> atributos,HttpServletRequest request,
			@RequestParam(value="tipoHistorico") String tipoHistorico){
		
		String rutaHistorico = "";
		
		if(tipoHistorico!=null && !tipoHistorico.equals("")){
			if(tipoHistorico.equalsIgnoreCase("elemento"))
				rutaHistorico = "filtrarHistoricoElemento.html";
			if(tipoHistorico.equalsIgnoreCase("referencia"))
				rutaHistorico = "filtrarHistoricoReferencia.html";
		}
		
		if(tipoHistorico!=null && !tipoHistorico.equals("")){
			if(tipoHistorico.equalsIgnoreCase("elemento"))
				esElementoHistorico(atributos, session, request);
			if(tipoHistorico.equalsIgnoreCase("referencia"))
				esReferenciaHistorico(atributos, session, request);
		}
		
		atributos.put("rutaHistorico", rutaHistorico);
		atributos.put("tipoHistorico", tipoHistorico);
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());
		atributos.put("clienteId", obtenerClienteAspUser().getId());
	
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
	
		//hacemos el forward
		return "consultaHistorico";
	}
	
	@RequestMapping(
			value="/filtrarHistoricoElemento.html",
			method = RequestMethod.POST
		)
	public String filtrarHistoricoElemento(@ModelAttribute("historicoBusqueda") ElementoHistorico elementoHistorico, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request,
			@RequestParam(value="tipoHistorico") String tipoHistorico){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(elementoHistorico, result);
		if(!result.hasErrors()){
			session.setAttribute("historicoBusqueda", elementoHistorico);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarHistorico(session, atributos, request, tipoHistorico);
	}
	
	@RequestMapping(
			value="/filtrarHistoricoReferencia.html",
			method = RequestMethod.POST
		)
	public String filtrarHistoricoReferencia(@ModelAttribute("historicoBusqueda") ReferenciaHistorico referenciaHistorico, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request,
			@RequestParam(value="tipoHistorico") String tipoHistorico){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(referenciaHistorico, result);
		if(!result.hasErrors()){
			session.setAttribute("historicoBusqueda", referenciaHistorico);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarHistorico(session, atributos, request, tipoHistorico);
	}
	
//	/**
//	 * Observar la anotación @RequestMapping de SPRING.
//	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
//	 * 
//	 * Se encarga de eliminar Plantilla.
//	 * 
//	 * @param idRemito el id de Plantilla a eliminar.
//	 * (Observar la anotación @RequestParam)
//	 * @param atributos son los atributos del request
//	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
//	 */
//	@RequestMapping(
//			value="/eliminarPlantillaFacturacion.html",
//			method = RequestMethod.GET
//		)
//	public String eliminarPlantillaFacturacion(HttpSession session,
//			@RequestParam("id") Long id,
//			Map<String,Object> atributos,
//			HttpServletRequest request) {
//		Boolean commit = null;
//		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
//		boolean hayAvisos = false;
//		boolean hayAvisosNeg = false;
//		//Obtenemos la remito para eliminar luego
//		PlantillaFacturacion plantillaFacturacion = plantillaFacturacionService.obtenerPorId(id);
//		
//		//Eliminamos la plantilla
//		commit = plantillaFacturacionService.eliminarPlantillaFacturacion(plantillaFacturacion);
//		ScreenMessage mensaje;
//		//Controlamos su eliminacion.
//		if(commit){
//			mensaje = new ScreenMessageImp("formularioPlantillaFacturacion.notif.plantillaEliminadaExito", null);
//			hayAvisos = true;
//		}else{
//			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
//			hayAvisosNeg = true;
//		}
//		avisos.add(mensaje);
//		
//		atributos.put("hayAvisosNeg", hayAvisosNeg);
//		atributos.put("hayAvisos", hayAvisos);
//		atributos.put("avisos", avisos);
//		return mostrarPlantillaFacturacion(session, atributos, request);
//	}

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
	
	private void esElementoHistorico(Map<String,Object> atributos, HttpSession session, HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List<ElementoHistorico> elementosHistoricos = null;
		 
		ElementoHistorico elementoHistorico = (ElementoHistorico) session.getAttribute("historicoBusqueda");
		 if(elementoHistorico == null)
		 {
			 elementoHistorico = new ElementoHistorico();
		 }
					
		//cuenta la cantidad de resultados
		Integer size = elementoHistoricoService.contarElementoHistoricoFiltrados(elementoHistorico, obtenerClienteAspUser());
		atributos.put("size", size);
		
		//paginacion y orden de resultados de displayTag
		elementoHistorico.setTamañoPagina(20);		
		String nPaginaStr= request.getParameter((new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		if(nPaginaStr==null){
			nPaginaStr = (String)atributos.get((new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		}
		String fieldOrder = request.getParameter( new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_SORT));
		elementoHistorico.setFieldOrder(fieldOrder);
		String sortOrder = request.getParameter(new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
		elementoHistorico.setSortOrder(sortOrder);
		Integer nPagina = 1;		
		if(nPaginaStr!=null){
			nPagina = (Integer.parseInt(nPaginaStr));
		}
		elementoHistorico.setNumeroPagina(nPagina);
		
		//Se busca en la base de datos los plantillasFacturacion con los filtros de paginacion agregados a la plantillaFacturacion
		elementosHistoricos =(List<ElementoHistorico>) elementoHistoricoService.listarElementoHistorico(elementoHistorico, obtenerClienteAspUser());
								
		atributos.put("historicos", elementosHistoricos);
	}
	
	private void esReferenciaHistorico(Map<String,Object> atributos, HttpSession session, HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List<ReferenciaHistorico> referenciasHistoricos = null;
		 
		ReferenciaHistorico referenciaHistorico = (ReferenciaHistorico) session.getAttribute("historicoBusqueda");
		 if(referenciaHistorico == null)
		 {
			 referenciaHistorico = new ReferenciaHistorico();
		 }
					
		//cuenta la cantidad de resultados
		Integer size = referenciaHistoricoService.contarReferenciaHistoricoFiltrados(referenciaHistorico, obtenerClienteAspUser());
		atributos.put("size", size);
		
		//paginacion y orden de resultados de displayTag
		referenciaHistorico.setTamañoPagina(20);		
		String nPaginaStr= request.getParameter((new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		if(nPaginaStr==null){
			nPaginaStr = (String)atributos.get((new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		}
		String fieldOrder = request.getParameter( new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_SORT));
		referenciaHistorico.setFieldOrder(fieldOrder);
		String sortOrder = request.getParameter(new ParamEncoder("historico").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
		referenciaHistorico.setSortOrder(sortOrder);
		Integer nPagina = 1;		
		if(nPaginaStr!=null){
			nPagina = (Integer.parseInt(nPaginaStr));
		}
		referenciaHistorico.setNumeroPagina(nPagina);
		
		//Se busca en la base de datos los plantillasFacturacion con los filtros de paginacion agregados a la plantillaFacturacion
		referenciasHistoricos =(List<ReferenciaHistorico>) referenciaHistoricoService.listarReferenciaHistorico(referenciaHistorico, obtenerClienteAspUser());
								
		atributos.put("historicos", referenciasHistoricos);
	}
	
}