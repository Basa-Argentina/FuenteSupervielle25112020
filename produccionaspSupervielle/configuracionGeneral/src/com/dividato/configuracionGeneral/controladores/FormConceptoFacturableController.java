/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.dividato.configuracionGeneral.validadores.ConceptoFacturableValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ImpuestoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.TipoConceptoFacturable;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * @author Ezequiel Beccaria
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioConceptoFacturable.html",
				"/guardarActualizarConceptoFacturable.html"
			}
		)
public class FormConceptoFacturableController {
	private ConceptoFacturableService service;
	private ConceptoFacturableValidator validator;	
	private ListaConceptoFacturableController listaConceptoFacturablesController;
	private ImpuestoService impuestoService;
	private ListaPreciosService listaPreciosService;
	
	/**
	 * Setea el servicio de User.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase UserImp implementa User y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */

	@Autowired
	public void setService(ConceptoFacturableService impuestoService) {
		this.service = impuestoService;
	}
	@Autowired
	public void setListaConceptoFacturablesController(
			ListaConceptoFacturableController listaConceptoFacturablesController) {
		this.listaConceptoFacturablesController = listaConceptoFacturablesController;
	}
	@Autowired
	public void setValidator(ConceptoFacturableValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	@Autowired
	public void setImpuestoService(ImpuestoService impuestoService) {
		this.impuestoService = impuestoService;
	}
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioUser.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de User, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioConceptoFacturable.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,		
			@RequestParam(value="val", required=false) String val,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			ConceptoFacturable concepto;
			concepto = service.obtenerPorId(Long.valueOf(id));			
			atributos.put("conceptoFormulario", concepto);			
			//busco las listas de precios asociadas al concepto
			List<ListaPrecios> listas = listaPreciosService.listarPorConceptoFacturable(concepto, null, obtenerClienteAspUser());
			atributos.put("listas", listas);
		}	
		//Seteo la accion actual
		atributos.put("accion", accion);	
		//Busco los impuesto registrados en la BD
		List<Impuesto> impuestos = impuestoService.listarImpuestos(null, null, obtenerClienteAspUser());
		atributos.put("impuestos", impuestos); //Seteo los impuestos en el responce
		//Busco los tipos de conceptos facturables
		List<TipoConceptoFacturable> tipos = service.listarTiposConceptosFacturables();
		atributos.put("tipos", tipos); //Seteo los impuestos en el responce
		//Preparo el popup de impuestos	
		definirPopupImpuestos(atributos, val);
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		//Se realiza el redirect
		return "formularioConceptoFacturable";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param User user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto User con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarConceptoFacturable.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("conceptoFormulario") final ConceptoFacturable conceptoFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			//seteo la accion
			conceptoFormulario.setAccion(accion);
			//obtengo el cliente
			conceptoFormulario.setClienteAsp(obtenerClienteAspUser());
			//obtengo el impuesto asignado
			conceptoFormulario.getImpuestos().add(impuestoService.obtenerPorCodigo(conceptoFormulario.getImpuestoCodigo(), obtenerClienteAspUser()));
			//obtengo el tipo de Concepto Facturable asignado
			conceptoFormulario.setTipo(service.obtenerTipoPorId(conceptoFormulario.getTipoId()));
			//valido datos
			validator.validateRegMod(conceptoFormulario, result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		ConceptoFacturable concepto;
		
		if(!result.hasErrors()){	
			
			if(accion.equals("NUEVO")){
				concepto = new ConceptoFacturable();
				setData(concepto, conceptoFormulario);
				concepto.setFechaRegistro(new Date());	
				concepto.setFechaActualizacion(concepto.getFechaRegistro()); //fecha de actualizacion
				//Se guarda el cliente en la BD
				commit = service.save(concepto);
			}else{
				concepto = service.obtenerPorId(conceptoFormulario.getId());
				setData(concepto, conceptoFormulario);
				concepto.setFechaActualizacion(new Date()); //fecha de actualizacion
				//Se Actualiza el cliente en la BD
				commit = service.update(concepto);
			}
			if(commit == null || !commit)
				conceptoFormulario.setId(concepto.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("impuestoFormulario", conceptoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(accion, String.valueOf(conceptoFormulario.getId()), null, atributos);
		}
		
		if(result.hasErrors()){
			atributos.put("impuestoFormulario", conceptoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			return precarga(accion, String.valueOf(conceptoFormulario.getId()), null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.conceptoFacturable.registrado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.conceptoFacturable.modificado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaConceptoFacturablesController.mostrar(session, atributos);
	}
	
	private void setData(ConceptoFacturable obj, ConceptoFacturable data){
		if(data != null){			
			obj.setClienteAsp(data.getClienteAsp());
			if(data.getHabilitado() != null)
				obj.setHabilitado(data.getHabilitado());
			else
				obj.setHabilitado(false);
			obj.setCodigo(data.getCodigo());
			obj.setDescripcion(data.getDescripcion());
			obj.setTipo(data.getTipo());
			if(data.getGeneraStock() != null)
				obj.setGeneraStock(data.getGeneraStock());
			else
				obj.setGeneraStock(false);
			obj.setCosto(data.getCosto());
			obj.setPrecioBase(data.getPrecioBase());
			obj.setImpuestos(data.getImpuestos());
			obj.setTipoCalculo(data.getTipoCalculo());
		}
	}
	
	private void definirPopupImpuestos(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> impuestosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioImpuesto.filtro.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioImpuesto.filtro.descripcion",false));
		campos.add(new CampoDisplayTag("tipo","formularioImpuesto.tabla.tipo",false));
		campos.add(new CampoDisplayTag("alicuota","formularioImpuesto.tabla.alicuota",false));
		impuestosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		impuestosPopupMap.put("coleccionPopup", impuestoService.listarImpuestosPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		impuestosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		impuestosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		impuestosPopupMap.put("referenciaHtml", "impuestoCodigo"); 		
		//url que se debe consumir con ajax
		impuestosPopupMap.put("urlRequest", "precargaFormularioConceptoFacturable.html");
		//se vuelve a setear el texto utilizado para el filtrado
		impuestosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		impuestosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("impuestosPopupMap", impuestosPopupMap);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
