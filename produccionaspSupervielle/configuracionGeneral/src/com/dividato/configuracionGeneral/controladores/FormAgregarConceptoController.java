/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
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

import com.dividato.configuracionGeneral.validadores.ListaPreciosDetalleValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoVariacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.TipoVariacion;
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
				"/precargaFormularioAgregarConcepto.html",
				"/guardarActualizarAgregarConcepto.html"
			}
		)
public class FormAgregarConceptoController {
	private ListaPreciosService service;
	private ConceptoFacturableService conceptoFacturableService;
	private TipoVariacionService tipoVariacionService;
	private ListaPreciosDetalleValidator validator;
	private ListaListaPreciosController listaListaPreciosController;
		
	@Autowired
	public void setService(ListaPreciosService service) {
		this.service = service;
	}	
	@Autowired
	public void setConceptoFacturableService(
			ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}
	@Autowired	
	public void setTipoVariacionService(TipoVariacionService tipoVariacionService) {
		this.tipoVariacionService = tipoVariacionService;
	}
	@Autowired
	public void setListaListaPreciosController(
			ListaListaPreciosController listaListaPreciosController) {
		this.listaListaPreciosController = listaListaPreciosController;
	}
	@Autowired
	public void setValidator(ListaPreciosDetalleValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(
			value="/precargaFormularioAgregarConcepto.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="urlDestino",required=true) String urlDestino,			
			@RequestParam(value="idLista",required=false) Long idLista,
			@RequestParam(value="idConcepto",required=false) Long idConcepto,
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="val",required=false) String valConcepto,
			@RequestParam(value="val",required=false) String valLista,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		ListaPreciosDetalle detalle;
		if(!accion.equals("NUEVO")){
			detalle = service.obtenerListaPreciosDetallePorId(id);				
		}else{
			detalle = new ListaPreciosDetalle();
			if(idLista != null){
				ListaPrecios lista = service.obtenerPorId(idLista);
				detalle.setListaPrecios(lista);		
				detalle.setTipoVariacion(lista.getTipoVariacion());
				detalle.setValor(lista.getValor());
			}
			if(idConcepto != null){
				ConceptoFacturable concepto = conceptoFacturableService.obtenerPorId(idConcepto);
				detalle.setConceptoFacturable(concepto);
			}
		}
		//seteo la url de destino
		detalle.setUrlDestino(urlDestino);
		//agrego el detalle a los atributos
		atributos.put("detalle", detalle);			
		//Seteo la accion actual
		atributos.put("accion", accion);	
		//Busco los impuesto registrados en la BD
		List<TipoVariacion> tiposVariacion = tipoVariacionService.listarVariaciones(null);
		atributos.put("tiposVariacion", tiposVariacion); //Seteo los impuestos en el responce		
		//Creo el popup de conceptos facturables
		definirPopupConcepto(atributos, valConcepto, accion, urlDestino, idLista, idConcepto, id);
		//Creo el popup de lista de precios
		definirPopupListaPrecios(atributos, valLista, accion, urlDestino, idLista, idConcepto, id);	
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		//Se realiza el redirect
		return "formularioAgregarConcepto";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto User con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return la vista que debe mostrarse
	 */
	@RequestMapping(
			value="/guardarActualizarAgregarConcepto.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("detalle") final ListaPreciosDetalle detalle,
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
			detalle.setAccion(accion);			
			//obtengo el tipo de variacion asignado
			detalle.setTipoVariacion(tipoVariacionService.obtenerPorId(detalle.getVariacionId()));		
			//obtengo la lista de precios a la cual pertenece el detalle
			detalle.setListaPrecios(service.obtenerListaPreciosPorCodigo(
					detalle.getListaPreciosCodigo(), obtenerClienteAspUser()));
			//obtengo el concepto facturable por codigo
			detalle.setConceptoFacturable(conceptoFacturableService.obtenerConceptoFacturablePorCodigo(
					detalle.getConceptoCodigo(), obtenerClienteAspUser()));
			//valido datos
			validator.validate(detalle, result);
		}else{
			//obtengo los elementos para volver a cargar los que no generaron errores
			//obtengo la lista de precios a la cual pertenece el detalle
			if(detalle.getListaPreciosCodigo() != null && !"".equals(detalle.getListaPreciosCodigo()))
				detalle.setListaPrecios(service.obtenerListaPreciosPorCodigo(
					detalle.getListaPreciosCodigo(), obtenerClienteAspUser()));
			//obtengo el concepto facturable por codigo
			if(detalle.getConceptoCodigo() != null && !"".equals(detalle.getConceptoCodigo()))
				detalle.setConceptoFacturable(conceptoFacturableService.obtenerConceptoFacturablePorCodigo(
					detalle.getConceptoCodigo(), obtenerClienteAspUser()));
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		ListaPreciosDetalle lista;
		Long lista_id = null;
		if(!result.hasErrors()){			
			if(accion.equals("NUEVO")){
				lista = new ListaPreciosDetalle();
				setData(lista, detalle);				
				//Asigno el detalle a la lista de precios
				detalle.getListaPrecios().getDetalle().add(detalle);
				//Se guarda el cliente en la BD
				commit = service.update(detalle.getListaPrecios());
			}else{
				lista = service.obtenerListaPreciosDetallePorId(detalle.getId());
				setData(lista, detalle);				
				//Se Actualiza el cliente en la BD
				commit = service.update(detalle);
			}			
			lista_id = detalle.getListaPrecios().getId(); //seteo el id de la lista seleccionada
		}
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		//Ver errores
		if(commit != null && !commit){
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("listaFormulario", detalle);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(
					accion, 
					detalle.getUrlDestino(),
					(detalle.getListaPrecios() != null)? detalle.getListaPrecios().getId() : null,
					(detalle.getConceptoFacturable() != null)? detalle.getConceptoFacturable().getId(): null,
					detalle.getId(),
					null,
					null,
					atributos);
		}
		if(result.hasErrors()){
			atributos.put("impuestoFormulario", detalle);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");	
			return precarga(
					accion, 
					detalle.getUrlDestino(),
					(detalle.getListaPrecios() != null)? detalle.getListaPrecios().getId() : null,
					(detalle.getConceptoFacturable() != null)? detalle.getConceptoFacturable().getId(): null,
					detalle.getId(),
					null,
					null,
					atributos);
		}else{
			//Genero las notificaciones			
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.agregarConcepto.registrar", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.agregarConcepto.modificar", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaListaPreciosController.mostrar(lista_id, session, atributos);
	}
	 
	private void setData(ListaPreciosDetalle obj, ListaPreciosDetalle data){
		if(data != null){			
			obj.setConceptoFacturable(data.getConceptoFacturable());
			obj.setTipoVariacion(data.getTipoVariacion());
			obj.setValor(data.getValor());
			obj.setListaPrecios(data.getListaPrecios());			
		}
	}
	
	private void definirPopupConcepto(Map<String,Object> atributos, String val, String accion, String urlDestino, Long idLista, Long idConcepto, Long id){
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
		map.put("referenciaHtml", "conceptoCodigo"); 		
		//url que se debe consumir con ajax
		String idListaParam = (idLista != null)? "&idLista="+String.valueOf(idLista):"";
		String idConceptoParam = (idConcepto != null)? "&idConcepto="+String.valueOf(idConcepto):"";
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put(
				"urlRequest", 
				"precargaFormularioAgregarConcepto.html?" +
				"accion="+accion +
				"&urlDestino="+urlDestino +
				idListaParam +
				idConceptoParam +
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosPopupMap", map);
	}
	
	private void definirPopupListaPrecios(Map<String,Object> atributos, String val, String accion, String urlDestino, Long idLista, Long idConcepto, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioListaPrecios.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioListaPrecios.datos.descripcion",false));
		campos.add(new CampoDisplayTag("tipoVariacion.descripcion","formularioListaPrecios.datos.tipoVariacion",false));	
		campos.add(new CampoDisplayTag("valor","formularioListaPrecios.datos.valor",false));
		campos.add(new CampoDisplayTag("habilitada","formularioListaPrecios.datos.habilitada",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", service.listarListasPreciosPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "listaPreciosCodigo"); 		
		//url que se debe consumir con ajax
		String idListaParam = (idLista != null)? "&idLista="+String.valueOf(idLista):"";
		String idConceptoParam = (idConcepto != null)? "&idConcepto="+String.valueOf(idConcepto):"";
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put(
				"urlRequest", 
				"precargaFormularioAgregarConcepto.html?" +
				"accion="+accion +
				"&urlDestino="+urlDestino +
				idListaParam +
				idConceptoParam +
				idParam);				
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("listaPopupMap", map);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
