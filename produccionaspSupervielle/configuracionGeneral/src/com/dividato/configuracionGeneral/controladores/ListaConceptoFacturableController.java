/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.ConceptoFacturableValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.TipoConceptoFacturable;
import com.security.modelo.seguridad.User;
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
				"/iniciarConceptoFacturable.html",
				"/mostrarConceptoFacturable.html",
				"/eliminarConceptoFacturable.html",
				"/filtrarConceptoFacturable.html"
			}
		)
public class ListaConceptoFacturableController {
	private ConceptoFacturableService conceptoFacturableService;
	private ConceptoFacturableValidator validator;
	private ListaPreciosService listaPreciosService;

	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}

	@Autowired
	public void setValidator(ConceptoFacturableValidator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}

	@RequestMapping(value="/iniciarConceptoFacturable.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){		
		atributos.remove("conceptoFacturableBusqueda");
		return "redirect:mostrarConceptoFacturable.html";
	}
	
	@RequestMapping(value="/mostrarConceptoFacturable.html", method = RequestMethod.GET)
	public String mostrar(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos los conceptos filtrados (o no) y lo cargamos a request.
		List<ConceptoFacturable> conceptos = null;	
		ConceptoFacturable busqueda = (ConceptoFacturable) atributos.get("conceptoFacturableBusqueda");
		if(busqueda != null)
			conceptos = conceptoFacturableService.listarPorFiltro(
					busqueda.getHabilitado(), busqueda.getCodigo(), busqueda.getDescripcion(), 
					busqueda.getGeneraStock(), busqueda.getTipoCalculo(), busqueda.getTipo(), 
					obtenerClienteAspUser());
		else
			conceptos = conceptoFacturableService.listarPorFiltro(null, null, null, null,
					null, null, obtenerClienteAspUser());
		atributos.put("conceptos", conceptos);
		//busco los tipos de conceptos y los cargamos en el request
		List<TipoConceptoFacturable> tipoConceptos = conceptoFacturableService.listarTiposConceptosFacturables();
		atributos.put("tipos", tipoConceptos);
		//hacemos la redireccion a la ventana
		return "consultaConceptoFacturable";
	}
	
	@RequestMapping(value="/filtrarConceptoFacturable.html", method = RequestMethod.POST)
	public String filtrar(
			@ModelAttribute("impuestoBusqueda") ConceptoFacturable busqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);
		if(busqueda.getTipoId() != 0L){
			TipoConceptoFacturable tipo = conceptoFacturableService.obtenerTipoPorId(busqueda.getTipoId());
			busqueda.setTipo(tipo);
		}	
		if(!result.hasErrors()){
			atributos.put("conceptoFacturableBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(session, atributos);
	}
	
	@RequestMapping(value="/eliminarConceptoFacturable.html", method = RequestMethod.GET)
	public String eliminar(
			HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos el impuesto
		ConceptoFacturable concepto = conceptoFacturableService.obtenerPorId(id);
		
		//verificamos si esta asociado a alguna lista de precios
		List<ListaPreciosDetalle> detalles = listaPreciosService.listarDetallesPorListaPreciosConceptoFacturable(null, concepto);
		if(!detalles.isEmpty()){
			avisos.add(new ScreenMessageImp("error.conceptoFacturable.conceptoAsociado", null));
			hayAvisosNeg = true;
		}else{
			//eliminamos user
			commit = conceptoFacturableService.delete(concepto);
			
			ScreenMessage mensaje;
			if(commit){
				mensaje = new ScreenMessageImp("notif.conceptoFacturable.eliminado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.commitDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
		}	
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(session,atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
