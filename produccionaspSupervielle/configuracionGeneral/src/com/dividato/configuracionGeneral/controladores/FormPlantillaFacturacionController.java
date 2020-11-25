package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.PlantillaFacturacionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Lectura.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarPrecargaFormularioPlantillaFacturacion.html",
				"/precargaFormularioPlantillaFacturacion.html",
				"/guardarActualizarPlantillaFacturacion.html"
			}
		)
public class FormPlantillaFacturacionController {
		
	private PlantillaFacturacionService plantillaFacturacionService;
	private PlantillaFacturacionDetalleService plantillaFacturacionDetalleService;
	private PlantillaFacturacionValidator validator;
	private AfipTipoComprobanteService afipTipoComprobanteService;
	private ListaPlantillaFacturacionController listaPlantillaFacturacionController;

		
	/**
	 * Setea el servicio de Lectura.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase LecturaImp implementa Lectura y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param lecturaService
	 */
	
	@Autowired
	public void setPlantillaFacturacionService(PlantillaFacturacionService plantillaFacturacionService) {
		this.plantillaFacturacionService = plantillaFacturacionService;
	}
	@Autowired
	public void setPlantillaFacturacionDetalleService(PlantillaFacturacionDetalleService plantillaFacturacionDetalleService) {
		this.plantillaFacturacionDetalleService = plantillaFacturacionDetalleService;
	}
	
	@Autowired
	public void setAfipTipoComprobanteService(AfipTipoComprobanteService afipTipoComprobanteService) {
		this.afipTipoComprobanteService = afipTipoComprobanteService;
	}
	
	@Autowired
	public void setValidator(PlantillaFacturacionValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	@Autowired
	public void setListaPlantillaFacturacionController(ListaPlantillaFacturacionController listaPlantillaFacturacionController) {
		this.listaPlantillaFacturacionController = listaPlantillaFacturacionController;
	}
	
	
	@RequestMapping(
			value="/iniciarPrecargaFormularioPlantillaFacturacion.html",
			method = RequestMethod.GET
		)
	public String iniciarPrecargaFormularioPlantillaFacturacion(
			@RequestParam(value="accion",required=false) String accion,	
			@RequestParam(value="id",required=false) Long id,			
			Map<String,Object> atributos, HttpSession session) {
		
		session.removeAttribute("detallesSession");
		return precargaFormularioPlantillaFacturacion(accion, id, atributos, session);
		
	}
	
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioLectura.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Lectura, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioLectura" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioPlantillaFacturacion.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioPlantillaFacturacion(
			@RequestParam(value="accion",required=false) String accion,	
			@RequestParam(value="id",required=false) Long id,			
			Map<String,Object> atributos, HttpSession session) {
		
		PlantillaFacturacion plantillaFacturacionFormulario = null; 
		
	
		if(accion==null){
			accion = "NUEVO";
		}
		if(!accion.equals("NUEVO")){
			if(atributos.get("plantillaFacturacionFormulario") == null){
				plantillaFacturacionFormulario = plantillaFacturacionService.obtenerPorId(id);
				if(plantillaFacturacionFormulario!=null){
					plantillaFacturacionFormulario.setClienteCodigo(plantillaFacturacionFormulario.getClienteEmp().getCodigo());
					plantillaFacturacionFormulario.setCodigoSerie(plantillaFacturacionFormulario.getSerie().getCodigo());
					plantillaFacturacionFormulario.setTipoComprobanteId(plantillaFacturacionFormulario.getAfipTipoComprobante().getId());
					plantillaFacturacionFormulario.setListaPreciosCodigo(plantillaFacturacionFormulario.getListaPrecios().getCodigo());
				}
			}
			
			atributos.put("plantillaFacturacionFormulario", plantillaFacturacionFormulario);
		}
			
		
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());
		atributos.put("accion", accion);
				
		return "formularioPlantillaFacturacion";
	}
	
	@RequestMapping(
			value="/guardarActualizarPlantillaFacturacion.html",
			method = RequestMethod.POST
		)
	public String guardarActualizarPlantillaFacturacion(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("plantillaFacturacionFormulario") final PlantillaFacturacion plantillaFacturacionFormulario,
			BindingResult result,
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Map<String,Object> atributos) {
		
		
		@SuppressWarnings("unchecked")
		List<PlantillaFacturacionDetalle> plantillaDetalles = (List<PlantillaFacturacionDetalle>) session.getAttribute("detallesSession");
		if(plantillaDetalles == null){
			plantillaDetalles = new ArrayList<PlantillaFacturacionDetalle>();
		}
		
		Boolean commit = null;
		if (accion == null || accion.equals("") || accion.equals("NUEVO"))
			accion = "NUEVO";
		else {
			accion = "MODIFICACION";
		}
		
		if(!result.hasErrors()){
			plantillaFacturacionFormulario.setAccion(accion);
			plantillaFacturacionFormulario.setDetalles(plantillaDetalles);
			validator.validate(plantillaFacturacionFormulario, result);
		}
		
		if(!result.hasErrors()){
			//empezamos a setear los datos nuevos,
			PlantillaFacturacion plantilla = new PlantillaFacturacion();
			
			setData(plantilla, plantillaFacturacionFormulario);
			
			List<PlantillaFacturacionDetalle> detallesViejos = plantillaFacturacionDetalleService.listarPlantillaDetallesPorPlantilla(plantillaFacturacionFormulario.getId(), obtenerClienteAspUser());
			Set<PlantillaFacturacionDetalle> detallesNuevos = new HashSet<PlantillaFacturacionDetalle>(plantillaDetalles);
			plantilla.setDetalles(detallesViejos);
			commit = plantillaFacturacionService.guardarPlantillaFacturacionYDetalles(detallesNuevos, plantilla);
		}
			
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("plantillaFacturacionFormulario", plantillaFacturacionFormulario);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioPlantillaFacturacion(plantillaFacturacionFormulario.getAccion(), plantillaFacturacionFormulario.getId(), atributos, session);
		}	
		
		if(result.hasErrors()){
			atributos.put("plantillaFacturacionFormulario", plantillaFacturacionFormulario);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioPlantillaFacturacion(plantillaFacturacionFormulario.getAccion(), plantillaFacturacionFormulario.getId(), atributos, session);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeEscrituracionReg = new ScreenMessageImp("formularioPlantillaFacturacion.notif.plantillaAgregadaExito", null);
			avisos.add(mensajeEscrituracionReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		//hacemos el redirect
		atributos.remove("plantillaFacturacionFormulario");
		session.removeAttribute("detallesSession");
		//return precargaFormularioPlantillaFacturacion(plantillaFacturacionFormulario.getAccion(), plantillaFacturacionFormulario.getId(), atributos, session);
		return listaPlantillaFacturacionController.mostrarPlantillaFacturacion(session, atributos, request);
	
	}

	
	/////////////////////////////////////////////////////////////METODOS AUXILIARES/////////////////////////////////////////////////////////////////////

	
	private void setData(PlantillaFacturacion plantilla, PlantillaFacturacion plantillaFacturacionFormulario){
		if(plantillaFacturacionFormulario != null){	
			plantilla.setId(plantillaFacturacionFormulario.getId());
			plantilla.setAccion(plantillaFacturacionFormulario.getAccion());
			plantilla.setAfipTipoComprobante(plantillaFacturacionFormulario.getAfipTipoComprobante());
			plantilla.setClienteAsp(obtenerClienteAspUser());
			plantilla.setClienteEmp(plantillaFacturacionFormulario.getClienteEmp());
			plantilla.setHabilitado(plantillaFacturacionFormulario.getHabilitado());
			plantilla.setListaPrecios(plantillaFacturacionFormulario.getListaPrecios());
			plantilla.setSerie(plantillaFacturacionFormulario.getSerie());
			plantilla.setTipoComprobanteId(plantillaFacturacionFormulario.getTipoComprobanteId());
			
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}

	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private Empresa obtenerEmpresaUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
	}
}
