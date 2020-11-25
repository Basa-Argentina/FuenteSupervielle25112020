package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.ElementoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Elemento.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarUbicacionProvisoria.html",
				"/guardarUbicacionProvisoria.html"
			}
		)
public class FormUbicacionProvisoriaController {
	
	private ElementoService elementoService;
	private ElementoBusquedaValidator validator;
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setLeturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	@Autowired
	public void setLeturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	@Autowired
	public void setValidator(ElementoBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarUbicacionProvisoria.html",
			method = RequestMethod.GET
		)
	public String iniciarUbicacionProvisoria(HttpSession session,
			Map<String,Object> atributos){
		
	
		return "formularioUbicacionProvisoria";
	}
	
	@RequestMapping(
			value="/guardarUbicacionProvisoria.html",
			method = RequestMethod.POST
		)
	public String ejecutarUbicacionProvisoria(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="codigoLectura", required=false) Long codigoLectura,
			@RequestParam(value="ubicacionProvisoria", required=false) String ubicacionProvisoria){
		
		List<LecturaDetalle> detalles = null;
		Lectura lectura = lecturaService.obtenerPorCodigo(codigoLectura, obtenerClienteAspUser());
		List<Elemento> elementosUbicar = new ArrayList<Elemento>();
		
		if(lectura!=null){
			detalles = lecturaDetalleService.listarLecturaDetallePorLectura(lectura, obtenerClienteAspUser());
			
			if(detalles!=null && detalles.size()>=0){
				for(LecturaDetalle detalle: detalles)
				{
					Elemento elemento = detalle.getElemento();
					if(elemento!=null){
						elemento.setUbicacionProvisoria(ubicacionProvisoria);
						elementosUbicar.add(elemento);
					}
				}
			}
		}
		
		Boolean commit = elementoService.actualizarElementoList(elementosUbicar);
		
		if(commit!=null && commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioElemento.notificacion.elementosRegistrados", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		else
		{
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("codigoLectura", codigoLectura);
			atributos.put("ubicacionProvisoria", ubicacionProvisoria);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}
			
		
		//hacemos el forward
		return "formularioUbicacionProvisoria";
	}
	
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}