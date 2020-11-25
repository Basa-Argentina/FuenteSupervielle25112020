/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.dividato.configuracionGeneral.objectForms.RequerimientoElementoBusquedaForm;
import com.dividato.configuracionGeneral.validadores.jerarquias.RequerimientoElementoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.jerarquias.hibernate.RequerimientoServiceImp;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoReferenciaService;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.TipoRequerimiento;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de requerimientoElementos.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarRequerimientoElementoSinReferencia.html",
				"/mostrarRequerimientoElementoSinReferencia.html",
				"/filtrarRequerimientoElementoSinReferencia.html",
				"/mostrarRequerimientoElementoSinReferenciaSinFiltrar.html"
			}
		)
public class ListaRequerimientoElementoSinReferenciaController {
	
	private ElementoService elementoService;
	private TipoElementoService tipoElementoService;
	private ReferenciaService referenciaService;
	private ClienteEmpService clienteEmpService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private RequerimientoReferenciaService requerimientoReferenciaService;
	private RequerimientoElementoBusquedaValidator validator;
	private TipoRequerimientoService tipoRequerimientoService;
	
	@Autowired
	public void setClasificacionDocumentalService(ClasificacionDocumentalService clasificacionDocumentalService) {
		this.clasificacionDocumentalService = clasificacionDocumentalService;
	}

	@Autowired
	public void setRequerimientoReferenciaService(RequerimientoReferenciaService requerimientoReferenciaService) {
		this.requerimientoReferenciaService = requerimientoReferenciaService;
	}
	
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setTipoRequerimientoService(TipoRequerimientoService tipoRequerimientoService) {
		this.tipoRequerimientoService = tipoRequerimientoService;
	}
	
	@Autowired
	public void setValidator(RequerimientoElementoBusquedaValidator validator) {
		this.validator = validator;
	}
	
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarRequerimientoElementoSinReferencia.html", method = RequestMethod.GET)
	public String iniciarRequerimientoElementoSinReferencia(HttpSession session, Map<String,Object> atributos,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			@RequestParam(value="idRequerimiento",required=false) Long idRequerimiento,
			@RequestParam(value="destinoURL",required=false) String destinoURL,
			@RequestParam(value="tipoReqCodigoString",required=false) String tipoReqCodigo){
		session.removeAttribute("elementoBusqueda");
		session.setAttribute("clienteCodigoRequerimientoElemento", clienteCodigo);
		session.setAttribute("tipoReqCodigoRequerimientoElemento", tipoReqCodigo);
		session.setAttribute("idRequerimientoElemento", idRequerimiento);
		session.setAttribute("destinoURLRequerimientoElemento", destinoURL);
		return "redirect:mostrarRequerimientoElementoSinReferenciaSinFiltrar.html";
	}
	
	@RequestMapping(
			value="/mostrarRequerimientoElementoSinReferenciaSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarRequerimientoElementoSinReferenciaSinFiltrar(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valElemento,
			@RequestParam(value="val", required=false) String valLectura,
			@RequestParam(value="val", required=false) String valTipoElemento,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo){
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
		
		Elemento elemento = (Elemento) session.getAttribute("elementoBusqueda");
		if(elemento == null)
		{
			elemento = new Elemento();
		}
		
		elemento.setCodigoEmpresa(obtenerEmpresa().getCodigo());
		elemento.setCodigoSucursal(obtenerSucursal().getCodigo());
		elemento.setCodigoCliente(String.valueOf(session.getAttribute("clienteCodigoRequerimientoElemento")));
		
		TipoRequerimiento tipoReq = tipoRequerimientoService.obtenerPorCodigo((String)session.getAttribute("tipoReqCodigoRequerimientoElemento"),obtenerClienteAsp());
		if(tipoReq!=null)
			elemento.setTipoRequerimiento(tipoReq);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		atributos.put("size", Integer.valueOf(0));
		atributos.put("pagesize", "40");
		
		session.setAttribute("elementoBusqueda", elemento);
		//hacemos el forward
		return "consultaRequerimientoElementoSinReferencia";
	}
	
	@RequestMapping(value="/mostrarRequerimientoElementoSinReferencia.html", method = RequestMethod.GET)
	public String mostrarRequerimientoElementoSinReferencia(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valElemento,
			@RequestParam(value="val", required=false) String valTipoElemento,
			@RequestParam(value="val", required=false) String valClasificacionDocumental,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			HttpServletRequest request){
			
		//buscamos en la base de datos y lo subimos a request.
		List <Elemento> elementos = null;	
		
		Elemento elemento = (Elemento) session.getAttribute("elementoBusqueda");
		
		if(elemento!=null && elemento.getCodigoContenedor()!=null){
			elemento.setCodigoElemento(elemento.getCodigoContenedor());
		}
		if(elemento == null)
		{
			elemento = new Elemento();
		}
		
		elemento.setSinReferencia(true);
		elemento.setCodigoEmpresa(obtenerEmpresa().getCodigo());
		elemento.setCodigoSucursal(obtenerSucursal().getCodigo());
		elemento.setCodigoCliente(String.valueOf(session.getAttribute("clienteCodigoRequerimientoElemento")));
		
		ClienteEmp clienteEmp=null;
		//consulto en la base de datos
		if(elemento.getCodigoCliente()!=null && !"".equals(elemento.getCodigoCliente())){
			clienteEmp = new ClienteEmp();
			clienteEmp.setCodigo(elemento.getCodigoCliente());
			clienteEmp = clienteEmpService.getByCodigo(clienteEmp, obtenerClienteAsp());
		}
		elemento.setClienteEmp(clienteEmp);
		
		TipoRequerimiento tipoReq = tipoRequerimientoService.obtenerPorCodigo((String)session.getAttribute("tipoReqCodigoRequerimientoElemento"),obtenerClienteAsp());
		if(tipoReq!=null)
			elemento.setTipoRequerimiento(tipoReq);
		
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		if(empleado==null)
			elemento.setCajasVacias(Boolean.TRUE);
		
		if(elemento.getCodigoClasificacionDocumental()!=null && !"".equals(elemento.getCodigoClasificacionDocumental()))
			elemento.setClasificacionDocumental(clasificacionDocumentalService.getClasificacionByCodigoCargarHijos(Integer.parseInt(elemento.getCodigoClasificacionDocumental()), elemento.getCodigoCliente(), obtenerClienteAsp(),null));
		
		//Armamos la lista de nodos para hacer in
		String clasificaciones = null;
		if(elemento.getClasificacionDocumental()!=null){
			clasificaciones = "";
			int i = 0;
			Set<ClasificacionDocumental> clasificacionDocumentals = elemento.getClasificacionDocumental().getListaCompletaHijos();
			clasificacionDocumentals.add(elemento.getClasificacionDocumental());
			int total = clasificacionDocumentals.size();
			for(ClasificacionDocumental clasificacionDocumental:clasificacionDocumentals){
				clasificaciones += ""+clasificacionDocumental.getId();
				i++;
				if(i<total)
					clasificaciones += ",";
			}
			
		}
		else if(empleado!=null)
		{
			List<ClasificacionDocumental> porEmpleado = new ArrayList<ClasificacionDocumental>(clasificacionDocumentalService.getByPersonalAsignado(empleado));
			clasificaciones = "";
			int i = 0;
			if(porEmpleado!=null && porEmpleado.size()>0)
			{
				Set<ClasificacionDocumental> clasificacionDocumentals = porEmpleado.get(0).getListaCompletaHijos();
				clasificacionDocumentals.add(porEmpleado.get(0));
				int total = clasificacionDocumentals.size();
				for(ClasificacionDocumental clasificacionDocumental:clasificacionDocumentals){
					clasificaciones += ""+clasificacionDocumental.getId();
					i++;
					if(i<total)
						clasificaciones += ",";
				}
			}
			else
			{
				//Genero las notificaciones 
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.empleadoSinClasifAsignada", null);
				avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				session.removeAttribute("elementoBusqueda");
				return mostrarRequerimientoElementoSinReferenciaSinFiltrar(session, atributos, null, null, null, null, null, null, null, null, null, null);
			}
			
		}
		
		//cuenta la cantidad de resultados
		Integer size = 0;
		
		if(tipoReq!=null && tipoReq.getRetiro())
			size = elementoService.contarElementosRetiroPorSQL(elemento, obtenerClienteAsp(),clasificaciones);
		else
			size = elementoService.contarElementoFiltradas(elemento, obtenerClienteAsp());
		
		atributos.put("size", size);
		
		Integer pagesize = null;
		String pagesizeStr = "";
		try{
			 pagesizeStr = (String) session.getAttribute("pagesize");
		}catch(ClassCastException e){
			 pagesizeStr = session.getAttribute("pagesize").toString();
		}
		if(pagesizeStr==null)
			pagesizeStr="40";
		if(pagesizeStr.equalsIgnoreCase("Todos"))
		{
			pagesize = size;
			
		}else{
			pagesize = Integer.valueOf(pagesizeStr);
		}
		if(pagesize == null)
			pagesize = 40;
		
		atributos.put("pagesize", pagesize);
		session.setAttribute("pagesize", pagesize);
		
		
		
		//paginacion y orden de resultados de displayTag
		elemento.setTamañoPagina(pagesize);

		String nPaginaStr= request.getParameter((new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		if(nPaginaStr==null){
			nPaginaStr = (String)atributos.get((new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		}
		String fieldOrder = request.getParameter( new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_SORT));
		elemento.setFieldOrder(fieldOrder);
		String sortOrder = request.getParameter(new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
		elemento.setSortOrder(sortOrder);
		Integer nPagina = 1;		
		if(nPaginaStr!=null){
			nPagina = (Integer.parseInt(nPaginaStr));
		}
		elemento.setNumeroPagina(nPagina);
		
		if(tipoReq!=null && tipoReq.getRetiro())
			elementos = (List<Elemento>) elementoService.traerElementosRetiroPorSQL(elemento, obtenerClienteAsp(), clasificaciones);
		else 
			elementos =(List<Elemento>) elementoService.listarElementoFiltradas(elemento, obtenerClienteAsp());
		
		session.setAttribute("elementosSession", elementos);
		atributos.put("elementos", elementos);
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		session.setAttribute("elementoBusqueda", elemento);
		//hacemos el forward
		return "consultaRequerimientoElementoSinReferencia";
	}
	
	@RequestMapping(value="/filtrarRequerimientoElementoSinReferencia.html", method = RequestMethod.POST)
	public String filtrarRequerimientoElementoSinReferencia(
			@ModelAttribute("elementoBusqueda") Elemento elemento, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos, HttpServletRequest request,
			@RequestParam(value="pagesize",required=false)String pagesize){
		//buscamos en la base de datos y lo subimos a request.
		session.setAttribute("pagesize",pagesize);
		atributos.put("pagesize",pagesize);
		this.validator.validate(elemento, result);
		if(!result.hasErrors()){
			session.setAttribute("elementoBusqueda", elemento);
			atributos.put("errores", false);
			atributos.remove("result");			
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarRequerimientoElementoSinReferencia(session, atributos, null, null, null, null, null,request);
	}
	
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresa(){
		return ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
	}
	private Sucursal obtenerSucursal(){
		return ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
	}
}
