/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

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

import com.dividato.configuracionGeneral.validadores.jerarquias.TipoOperacionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.TipoOperacion;
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
				"/precargaFormularioTipoOperacion.html",
				"/guardarActualizarTipoOperacion.html"
			}
		)
public class FormTipoOperacionController {
	private TipoOperacionService service;
	private TipoOperacionValidator validator;	
	private ListaTipoOperacionController listaTipoOperacionsController;
	private TipoRequerimientoService tipoRequerimientoService;
	private SerieService serieService;
	private ConceptoFacturableService conceptoFacturableService;
	
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
	public void setService(TipoOperacionService impuestoService) {
		this.service = impuestoService;
	}
	@Autowired
	public void setListaTipoOperacionsController(ListaTipoOperacionController listaTipoOperacionsController) {
		this.listaTipoOperacionsController = listaTipoOperacionsController;
	}
	@Autowired
	public void setValidator(TipoOperacionValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	@Autowired
	public void setTipoRequerimientoService(TipoRequerimientoService tipoRequerimientoService) {
		this.tipoRequerimientoService = tipoRequerimientoService;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
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
			value="/precargaFormularioTipoOperacion.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="val",required=false) String val,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			TipoOperacion tipo;
			tipo = service.obtenerPorId(id);			
			atributos.put("tipoFormulario", tipo);			
		}	
		//TODO preparar los popups
		//defino el popup de concepto facturable
		definirPopupConcepto(atributos, val, accion, id);
		//defino el popup de tipo de requerimiento
		definirPopupTipoRequerimiento(atributos, val, accion, id);
		//defino el popup de tipo de Operacion
		definirPopupTipoOperacion(atributos, val, accion, id);
		//defino el popup de serie
		definirPopupSerie(atributos, val, accion, id);
		//defino el popup de serie remito
		definirPopupSerieRemito(atributos, val, accion, id);
		//Seteo la accion actual
		atributos.put("accion", accion);		
		//seteo el id del clienteAsp en la pantalla
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		//Se realiza el redirect
		return "formularioTipoOperacion";
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
			value="/guardarActualizarTipoOperacion.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("tipoFormulario") final TipoOperacion tipoFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		//cago las asociaciones y la accion
		setAsociaciones(tipoFormulario, accion);
		if(!result.hasErrors())		
			validator.validate(tipoFormulario, result); //valido datos		
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		TipoOperacion tipo;
		
		if(!result.hasErrors()){	
			
			if(accion.equals("NUEVO")){
				tipo = new TipoOperacion();
				setData(tipo, tipoFormulario);
				tipo.setFechaRegistro(new Date());	
				tipo.setFechaActualizacion(tipo.getFechaRegistro()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				tipo.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido());
				//Se guarda el cliente en la BD
				commit = service.save(tipo);
			}else{
				tipo = service.obtenerPorId(tipoFormulario.getId());
				setData(tipo, tipoFormulario);
				tipo.setFechaActualizacion(new Date()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				tipo.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido());
				//Se Actualiza el cliente en la BD
				commit = service.update(tipo);
			}
			if(commit == null || !commit)
				tipoFormulario.setId(tipo.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("tipoFormulario", tipoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(accion, tipoFormulario.getId(), null, atributos);			
		}
		
		if(result.hasErrors()){
			atributos.put("tipoFormulario", tipoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			return precarga(accion, tipoFormulario.getId(), null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.tipoOperacion.registrado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.tipoOperacion.modificado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaTipoOperacionsController.mostrar(session, atributos);
	}
	
	private void setData(TipoOperacion o, TipoOperacion d){
		if(d != null){			
			o.setClienteAsp(d.getClienteAsp());
			o.setCodigo(d.getCodigo());
			o.setDescripcion(d.getDescripcion());
			o.setTipoRequerimiento(d.getTipoRequerimiento());
			o.setDesagregaPorDeposito(d.getDesagregaPorDeposito());
			o.setGeneraOperacionAlCerrarse(d.getGeneraOperacionAlCerrarse());
			o.setTipoOperacionSiguiente(d.getTipoOperacionSiguiente());
			o.setImprimeDocumento(d.getImprimeDocumento());
			o.setImprimeRemito(d.getImprimeRemito());
			o.setGeneraMovimiento(d.getGeneraMovimiento());
			o.setTituloDocumento(d.getTituloDocumento());
			o.setSerieDocumento(d.getSerieDocumento());
			o.setSerieRemito(d.getSerieRemito());
			o.setConceptoFacturable(d.getConceptoFacturable());
			o.setEnvio(d.getEnvio());
		}
	}
	
	private void setAsociaciones(TipoOperacion d, String accion){
		//seteo la accion
		d.setAccion(accion);
		//obtengo el cliente
		d.setClienteAsp(obtenerClienteAspUser());
		//obtengo tipo de requerimiento
		if(d.getTipoRequerimientoCod()!=null && !d.getTipoRequerimientoCod().equals(""))
			d.setTipoRequerimiento(tipoRequerimientoService.obtenerPorCodigo(
				d.getTipoRequerimientoCod(), d.getClienteAsp()));
		//obtengo conceptoFacturable
		if(d.getConceptoFacturableCod() != null && !"".equals(d.getConceptoFacturableCod()))
			d.setConceptoFacturable(conceptoFacturableService.obtenerConceptoFacturablePorCodigo(
					d.getConceptoFacturableCod(), d.getClienteAsp()));
		//obtengo tipoOperacion
		if(d.getTipoOperacionSiguienteCod() != null && !"".equals(d.getTipoOperacionSiguienteCod()))
			d.setTipoOperacionSiguiente(service.obtenerTipoOperacionPorCodigo(
					d.getTipoOperacionSiguienteCod(), d.getClienteAsp()));
		//obtengo serie
		if(d.getSerieDocumentoCod() != null && !"".equals(d.getSerieDocumentoCod()))
			d.setSerieDocumento(serieService.obtenerPorCodigo(
					d.getSerieDocumentoCod(), "I", d.getClienteAsp()));
		//obtengo serie para remito
		if(d.getSerieRemitoCod() != null && !"".equals(d.getSerieRemitoCod()))
			d.setSerieRemito(serieService.obtenerPorCodigo(
					d.getSerieRemitoCod(), "R", d.getClienteAsp()));
		
	}
	
	private void definirPopupConcepto(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioConceptoFacturable.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioConceptoFacturable.datos.descripcion",false));
		campos.add(new CampoDisplayTag("tipo.descripcion","formularioConceptoFacturable.datos.tipo",false));
		campos.add(new CampoDisplayTag("habilitado","formularioConceptoFacturable.datos.habilitado",false));
		campos.add(new CampoDisplayTag("generaStock","formularioConceptoFacturable.datos.generaStock",false));
		campos.add(new CampoDisplayTag("costo","formularioConceptoFacturable.datos.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioConceptoFacturable.datos.precioBase",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "conceptoFacturableCod"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioTipoOperacion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosPopupMap", map);
	}
	
	private void definirPopupTipoRequerimiento(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioTipoRequerimiento.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioTipoRequerimiento.datos.descripcion",false));
		campos.add(new CampoDisplayTag("plazo","formularioTipoRequerimiento.datos.plazo",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", tipoRequerimientoService.listarTipoRequerimientoPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "tipoRequerimientoCod"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioTipoOperacion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tipoRequerimientoPopupMap", map);
	}
	
	private void definirPopupTipoOperacion(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioTipoOperacion.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioTipoOperacion.datos.descripcion",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", service.listarTipoOperacionPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "tipoOperacionSiguienteCod"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioTipoOperacion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tipoOperacionPopupMap", map);
	}
	
	private void definirPopupSerie(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioSerie.datosSerie.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioSerie.datosSerie.descripcion",false));
		campos.add(new CampoDisplayTag("tipoSerie","formularioSerie.datosSerie.tipoSerie",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", serieService.listarSeriePopup(val, "I", null, obtenerClienteAspUser(),null));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "serieDocumentoCod"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioTipoOperacion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriePopupMap", map);
	}
	
	private void definirPopupSerieRemito(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioSerie.datosSerie.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioSerie.datosSerie.descripcion",false));
		campos.add(new CampoDisplayTag("tipoSerie","formularioSerie.datosSerie.tipoSerie",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", serieService.listarSeriePopup(val, "R", null, obtenerClienteAspUser(),null,true));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "serieRemitoCod"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioTipoOperacion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("serieRemitoPopupMap", map);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return obtenerUser().getCliente();
	}
	
	private User obtenerUser(){		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
