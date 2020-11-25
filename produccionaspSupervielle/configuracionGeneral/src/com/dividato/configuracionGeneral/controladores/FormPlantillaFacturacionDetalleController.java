package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.dividato.configuracionGeneral.validadores.PlantillaFacturacionDetalleValidator;
import com.dividato.configuracionGeneral.validadores.PlantillaFacturacionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
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
				"/precargaFormularioPlantillaFacturacionDetalle.html",
				"/guardarActualizarPlantillaFacturacionDetalle.html",
				"/eliminarPlantillaFacturacionDetalle.html"
			}
		)
public class FormPlantillaFacturacionDetalleController {
		
	private PlantillaFacturacionDetalleService plantillaFacturacionDetalleService;
	private PlantillaFacturacionDetalleValidator validator;
	private ConceptoFacturableService conceptoFacturableService;

		
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
	public void setPlantillaFacturacionDetalleService(PlantillaFacturacionDetalleService plantillaFacturacionDetalleService) {
		this.plantillaFacturacionDetalleService = plantillaFacturacionDetalleService;
	}
	
	@Autowired
	public void setValidator(PlantillaFacturacionDetalleValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
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
			value="/precargaFormularioPlantillaFacturacionDetalle.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioPlantillaFacturacionDetalle(
			@RequestParam(value="accion",required=false) String accion,	
			@RequestParam(value="accionDetalle",required=false) String accionDetalle,	
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="orden",required=false) Long orden,
			@RequestParam(value="idPlantilla",required=false) Long idPlantilla,
			Map<String,Object> atributos, HttpSession session) {
		
		PlantillaFacturacionDetalle plantillaFacturacionDetalleFormulario = null; 
		
		@SuppressWarnings("unchecked")
		List<PlantillaFacturacionDetalle> plantillaDetalles = (List<PlantillaFacturacionDetalle>) session.getAttribute("detallesSession");
		if(plantillaDetalles == null){
			plantillaDetalles = new ArrayList<PlantillaFacturacionDetalle>();
		}
		
		if(accion==null){
			accion = "NUEVO";
		}
		if(!accion.equals("NUEVO")){
			
			if(plantillaDetalles == null || plantillaDetalles.size()<= 0){
				plantillaDetalles = plantillaFacturacionDetalleService.listarPlantillaDetallesPorPlantilla(idPlantilla, obtenerClienteAspUser());
				if(plantillaDetalles != null && plantillaDetalles.size()>0)
					session.setAttribute("detallesSession", plantillaDetalles);
			}	
		}
		
		if(orden!=null){
			
			if(atributos.get("plantillaFacturacionDetalleFormulario") == null){
				for(PlantillaFacturacionDetalle detalleBuscar:plantillaDetalles){
					if(detalleBuscar.getOrden().longValue()== orden){
						plantillaFacturacionDetalleFormulario = detalleBuscar;
						plantillaFacturacionDetalleFormulario.setOrdenAnterior(orden);
					}
				}
			}
			
			atributos.put("plantillaFacturacionDetalleFormulario", plantillaFacturacionDetalleFormulario);
			
		}
		
		atributos.put("idPlantilla", idPlantilla);
		atributos.put("detalles", plantillaDetalles);
		atributos.put("ordenDefecto", plantillaDetalles.size()+1);
		atributos.put("accion", accion);
		atributos.put("accionDetalle", accionDetalle);
		return "formularioPlantillaFacturacionDetalle";
	}
	
	@RequestMapping(
			value="/guardarActualizarPlantillaFacturacionDetalle.html",
			method = RequestMethod.POST
		)
	public String guardarActualizarPlantillaFacturacionDetalle(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="accionDetalle",required=false) String accionDetalle,
			@ModelAttribute("plantillaFacturacionDetalleFormulario") final PlantillaFacturacionDetalle plantillaFacturacionDetalleFormulario,
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
		
		if (accion == null || accion.equals("") || accion.equals("NUEVO"))
			accion = "NUEVO";
		else {
			accion = "MODIFICACION";
		}
		
		if(!result.hasErrors()){
			plantillaFacturacionDetalleFormulario.setAccion(accion);
			plantillaFacturacionDetalleFormulario.setDetalles(plantillaDetalles);
			//Seteamos el concepto Facturable
			if(plantillaFacturacionDetalleFormulario.getCodigoConcepto()!= null){
				ConceptoFacturable concepto = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(plantillaFacturacionDetalleFormulario.getCodigoConcepto(), obtenerClienteAspUser());
				if(concepto!=null)
					plantillaFacturacionDetalleFormulario.setConceptoFacturable(concepto);
			}
			validator.validate(plantillaFacturacionDetalleFormulario, result);
		}
		
		if(!result.hasErrors()){
					
			
			
			if(accionDetalle.equals("") || accionDetalle.equals("NUEVO")){
				//Agrego el detalle a la lista
				plantillaDetalles.add(plantillaFacturacionDetalleFormulario);
				
			}
			if(accionDetalle.equals("MODIFICACION")){
				for(int i = 0; i<plantillaDetalles.size();i++){
					if(plantillaDetalles.get(i).getOrden().longValue() == plantillaFacturacionDetalleFormulario.getOrdenAnterior().longValue()){
						plantillaDetalles.set(i, plantillaFacturacionDetalleFormulario);
					}
				}
			}
			
			//Guardo nuevamente la lista
			session.setAttribute("detallesSession", plantillaDetalles);
			
		}
			
		if(result.hasErrors()){
			atributos.put("plantillaFacturacionDetalleFormulario", plantillaFacturacionDetalleFormulario);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioPlantillaFacturacionDetalle(
					plantillaFacturacionDetalleFormulario.getAccion(),plantillaFacturacionDetalleFormulario.getAccionDetalle(),
					plantillaFacturacionDetalleFormulario.getId(),plantillaFacturacionDetalleFormulario.getOrdenAnterior(),plantillaFacturacionDetalleFormulario.getIdPlantilla(),atributos, session);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeEscrituracionReg = new ScreenMessageImp("formularioPlantillaFacturacionDetalle.notif.detalleAgregadoExito", null);
			avisos.add(mensajeEscrituracionReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		//hacemos el redirect
		atributos.remove("plantillaFacturacionDetalleFormulario");
		return precargaFormularioPlantillaFacturacionDetalle(
				plantillaFacturacionDetalleFormulario.getAccion(),null,
				null,null,plantillaFacturacionDetalleFormulario.getIdPlantilla(),atributos, session);
	
	}
	
	@RequestMapping(
			value="/eliminarPlantillaFacturacionDetalle.html",
			method = RequestMethod.GET
		)
	public String eliminarPlantillaFacturacionDetalle(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="orden",required=false) Long orden,
			Map<String,Object> atributos, HttpSession session) {
		
		@SuppressWarnings("unchecked")
		List<PlantillaFacturacionDetalle> plantillaDetalles = (List<PlantillaFacturacionDetalle>) session.getAttribute("detallesSession");
		if(plantillaDetalles == null){
			plantillaDetalles = new ArrayList<PlantillaFacturacionDetalle>();
		}
		
		if(orden!=null){
			for(int i = 0; i<plantillaDetalles.size();i++){
				if(plantillaDetalles.get(i).getOrden().longValue() == orden){
					plantillaDetalles.remove(i);
					i--;
				}
			}
			session.setAttribute("detallesSession", plantillaDetalles);
		}
		
		return precargaFormularioPlantillaFacturacionDetalle(accion, null, null, null, null, atributos, session);
	}
	
/////////////////////////////////////////////////////METODOS AUXILIARES/////////////////////////////////////////////////////////////
	
	private void setData(PlantillaFacturacionDetalle plantillaDetalle, PlantillaFacturacionDetalle plantillaFacturacionDetalleFormulario){
		if(plantillaFacturacionDetalleFormulario != null){	
			plantillaDetalle.setId(plantillaFacturacionDetalleFormulario.getId());
			plantillaDetalle.setCantidadSinCosto(plantillaFacturacionDetalleFormulario.getCantidadSinCosto());
			plantillaDetalle.setCodigoConcepto(plantillaFacturacionDetalleFormulario.getCodigoConcepto());
			plantillaDetalle.setConceptoFacturable(plantillaFacturacionDetalleFormulario.getConceptoFacturable());
			plantillaDetalle.setDescripcionConcepto(plantillaFacturacionDetalleFormulario.getDescripcionConcepto());
			plantillaDetalle.setOrden(plantillaFacturacionDetalleFormulario.getOrden());
			
		}
	}

	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}

	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}
