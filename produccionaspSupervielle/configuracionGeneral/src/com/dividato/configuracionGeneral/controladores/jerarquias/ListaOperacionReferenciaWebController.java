/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import static com.security.utils.Constantes.ESTADO_OPERACION_ELEMENTO_OMITIDO;
import static com.security.utils.Constantes.ESTADO_OPERACION_ELEMENTO_PENDIENTE;
import static com.security.utils.Constantes.ESTADO_OPERACION_ELEMENTO_PROCESADO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

import com.dividato.configuracionGeneral.validadores.jerarquias.OperacionReferenciaBusquedaValidator;
import com.security.accesoDatos.jerarquias.interfaz.OperacionElementoService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionReferenciaService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.seguridad.User;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de operacions.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarOperacionReferenciaWeb.html",
				"/mostrarOperacionReferenciaWeb.html"
				
			}
		)
public class ListaOperacionReferenciaWebController {
	private OperacionReferenciaService operacionReferenciaService;
	private OperacionReferenciaBusquedaValidator validator;
	private OperacionService operacionService;
	private OperacionElementoService operacionElementoService;
	
	
	
	
	@Autowired
	public void setOperacionElementoService(OperacionElementoService operacionElementoService) {
		this.operacionElementoService = operacionElementoService;
	}
	
	@Autowired
	public void setOperacionReferenciaService(OperacionReferenciaService operacionReferenciaService) {
		this.operacionReferenciaService = operacionReferenciaService;
	}
	
	@Autowired
	public void setOperacionService(OperacionService operacionService) {
		this.operacionService = operacionService;
	}
	
	@Autowired
	public void setValidator(OperacionReferenciaBusquedaValidator validator) {
		this.validator = validator;
	}

	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarOperacionReferenciaWeb.html", method = RequestMethod.GET)
	public String iniciarOperacionReferencia(HttpSession session, Map<String,Object> atributos, 
			@RequestParam(value="idOperacion",required=false) Long idOperacion,
			@RequestParam(value="isWeb",required=false) Boolean isWeb){
		session.removeAttribute("operacionReferenciaBusqueda");
		session.removeAttribute("operacionReferenciasSession");
		session.setAttribute("rearchivosPendientesEnOperacion", false);
		return mostrarOperacionReferencia(session, atributos, idOperacion, isWeb);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/mostrarOperacionReferenciaWeb.html", method = RequestMethod.GET)
	public String mostrarOperacionReferencia(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="idOperacion",required=false) Long idOperacion,
			@RequestParam(value="isWeb",required=false) Boolean isWeb){
		//buscamos en la base de datos y lo subimos a request.
		List<OperacionElemento> operacionReferencias = null;
		List<OperacionElemento> lista = (List<OperacionElemento>)session.getAttribute("operacionReferenciasSession");
		OperacionElemento operacionReferencia = (OperacionElemento) session.getAttribute("operacionReferenciaBusqueda");
		Operacion operacion = null;
		if(idOperacion!=null){
			operacion = operacionService.obtenerPorId(idOperacion);
			operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
		}
		if(operacionReferencia != null && operacionReferencia.getIdOperacion()!=null){
			operacion = operacionService.obtenerPorId(operacionReferencia.getIdOperacion());
			operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
		}
		if(lista==null){
			if(operacionReferencia != null){
				operacionReferencias = operacionReferenciaService.listarOperacionReferenciaFiltradas(operacionReferencia, operacion);
			}
			else{
				operacionReferencia = new OperacionElemento();
				//Poner filtros por defecto
				operacionReferencias = operacionReferenciaService.listarOperacionReferenciaFiltradas(operacionReferencia, operacion);
			}
		}
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		atributos.put("idOperacion", obtenerClienteAsp().getId());
		Empresa emp = obtenerEmpresaDefault();
		if(emp!=null)
			atributos.put("codigoEmpresa", emp.getCodigo());
		atributos.put("operacion", operacion);
		
		if(lista==null){
			operacionReferencia.setOperacion(operacion);
			Collections.sort(operacionReferencias);
			atributos.put("operacionReferencias", operacionReferencias);
			session.setAttribute("operacionReferenciasSession", operacionReferencias);
		}
		else{
			Collections.sort(lista);
			atributos.put("operacionReferencias", lista);
		}
		contarElementosProcesados(operacionReferencia.getOperacion(), session, atributos);
		session.setAttribute("operacionReferenciaBusqueda", operacionReferencia);
		
		return "consultaOperacionReferenciaWeb";
	}
	
	
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresaDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getEmpresaDefecto();
	}
	
	
	@SuppressWarnings({ "unchecked" })
	private void contarElementosProcesados(Operacion operacion, HttpSession session, Map<String,Object> atributos){
		ArrayList<OperacionElemento> lista = (ArrayList<OperacionElemento>) session.getAttribute("operacionReferenciasSession");
		if(lista!=null){
			int cantidadProcesados = 0, cantidadPendientes=0, cantidadOmitidos =0, cantidadProcesadosParaTraspaso =0;
			boolean traspaso = false;
			if(operacion!=null && operacion.getTipoOperacion()!=null && operacion.getTipoOperacion().getGeneraOperacionAlCerrarse().booleanValue())
				traspaso = true;
			for(OperacionElemento operacionElemento:lista){
				if(operacionElemento.getEstado()!=null){
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PROCESADO))
						cantidadProcesados++;
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PROCESADO) && !operacionElemento.isTraspasado() && traspaso)
						cantidadProcesadosParaTraspaso++;
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_OMITIDO))
						cantidadOmitidos++;
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PENDIENTE))
						cantidadPendientes++;
				}
			}
			boolean finalizarOK = false, finalizarError = false, traspasar = false, procesando = false;
			if(cantidadProcesados == lista.size())
				finalizarOK = true;
			if(!finalizarOK && (cantidadProcesados + cantidadOmitidos) == lista.size())
				finalizarError = true;
			if(cantidadProcesadosParaTraspaso > 0)
				traspasar = true;
			if(!traspasar && !finalizarOK && !finalizarError && cantidadProcesados > 0)
				procesando = true;
			operacion.setCantidadProcesados(cantidadProcesados);
			operacion.setCantidadProcesadosParaTraspaso(cantidadProcesadosParaTraspaso);
			operacion.setCantidadPendientes(cantidadPendientes);
			operacion.setCantidadOmitidos(cantidadOmitidos);
			operacion.setFinalizarOK(finalizarOK);
			operacion.setFinalizarError(finalizarError);
			operacion.setTraspasar(traspasar);
			
			atributos.put("finalizarOK", finalizarOK);
			atributos.put("finalizarError", finalizarError);
			atributos.put("traspasar", traspasar);
			atributos.put("procesando", procesando);
		}
	}
}
