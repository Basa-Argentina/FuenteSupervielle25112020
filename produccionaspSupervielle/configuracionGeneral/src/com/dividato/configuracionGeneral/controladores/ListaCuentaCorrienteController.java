/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.FacturaBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DoctoCtaCteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MedioPagoReciboService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.DoctoCtaCte;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de clientes.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author X *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarCuentaCorriente.html",
				"/mostrarCuentaCorriente.html",
				"/eliminarCuentaCorriente.html",
				"/filtrarCuentaCorriente.html",
				"/mostrarFacturaDocto.html"
			}
		)
public class ListaCuentaCorrienteController {
	private FacturaService facturaService;
	private DoctoCtaCteService doctoCtaCteService;
	private MedioPagoReciboService medioPagoReciboService;
	private FacturaBusquedaValidator validator;
	
	
	@Autowired
	public void setService(FacturaService facturaService,
			DoctoCtaCteService doctoCtaCteService,
			MedioPagoReciboService medioPagoReciboService) {
		this.facturaService = facturaService;
		this.doctoCtaCteService = doctoCtaCteService;
		this.medioPagoReciboService = medioPagoReciboService;
	}

	@Autowired
	public void setValidator(FacturaBusquedaValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(value="/iniciarCuentaCorriente.html", method = RequestMethod.GET)
	public String iniciarCuentaCorriente(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("facturaBusqueda");
		return "redirect:mostrarCuentaCorriente.html";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/mostrarCuentaCorriente.html", method = RequestMethod.GET)
	public String mostrarCuentaCorriente(
			@ModelAttribute("facturaBusqueda") final Factura facturaForm,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		Double saldoDeudor = 0.0;
		Double saldoAcreedor = 0.0;
		
		Factura factura = (Factura)session.getAttribute("facturaBusqueda");
		if(factura!=null){
			// Validar los datos requeridos crear los mensajes.
			List<String> codigoErrores = new ArrayList<String>();
			if(factura.getCodigoEmpresa()==null || factura.getCodigoEmpresa().trim().length()==0){
				codigoErrores.add("formularioCuentaCorriente.errorCodigoEmpresa");
			}
			if(factura.getCodigoCliente()==null || factura.getCodigoCliente().trim().length()==0){
				codigoErrores.add("formularioCuentaCorriente.errorCodigoCliente");
			}
			result = generateErrors(codigoErrores);
			if(!result.hasErrors()){
				Double saldoTotal=0.0;
				Date fechaDesde = factura.getFechaDesde();
				Date fechaHasta = factura.getFechaHasta();
				factura.setFechaDesde(null);
				factura.setFechaHasta(null);
				List<Factura> facturaList = facturaService.listarFacturasFiltradas(factura, obtenerClienteAspUser());	
				    // Inicio Calculamos el saldo deudor y acreedor
					for (Factura f : facturaList){
						if(f.getAfipTipoDeComprobante()!=null){
							// Debito
							if(f.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("F") || f.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("ND")){
								saldoDeudor+= f.getTotalFinal()!=null ?f.getTotalFinal().doubleValue():0.0;
							}
							// Credito
							if(f.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("X") || f.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("NC")){
								saldoAcreedor+= f.getTotalFinal()!=null ?f.getTotalFinal().doubleValue():0.0;
							}
						}	
					}
				    // Fin Calculamos el saldo deudor y acreedor
					// Inicio Calcular el saldo por fila
					
					Boolean bandera = true;
					for(Factura fx:facturaList){
						if(bandera){
							saldoTotal = fx.getTotalFinal().doubleValue();
							bandera=false;
						}else{
							// Calculo de acuerdo al tipo de factura.
							if(fx.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("F") || fx.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("ND")){
								saldoTotal+= fx.getTotalFinal()!=null ?fx.getTotalFinal().doubleValue():0.0;
							}
							// Credito
							if(fx.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("X") || fx.getAfipTipoDeComprobante().getTipo().equalsIgnoreCase("NC")){
								saldoTotal-= fx.getTotalFinal()!=null ?fx.getTotalFinal().doubleValue():0.0;
							}
						}
						fx.setSaldoCtaCte(saldoTotal);
					}
				    // Fin Calcular el saldo por fila	
					// Inicio Remuevo las facturas que esten fuera del rango de fechas :D
					if(facturaList!=null){
						Iterator it = facturaList.iterator();
						while(it.hasNext()){
							Factura	fr  = (Factura) it.next();
							if(fechaDesde!=null){
								if(fr.getFecha().before(fechaDesde)){
									it.remove();
								}
							}
							if(fechaHasta!=null){
								if(fr.getFecha().after(fechaHasta)){
									it.remove();
								}
							}	
						}
					}	
					// Fin Remuevo las facturas que esten fuera del rango de fechas :D	
					
				atributos.put("facturaList", facturaList);
			}	
		}
		if(result.hasErrors()){
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			//return precargaFormularioCuentaCorriente(session, atributos, accion, facturaFormulario.getId(), null, null, null, null);
		}
		atributos.put("saldoDeudor", saldoDeudor);
		atributos.put("saldoAcreedor", saldoAcreedor);
		atributos.put("saldoTotal", saldoDeudor-saldoAcreedor);
		
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("facturaBusqueda", factura);
		return "consultaCuentaCorriente";
	}
	
	@RequestMapping(value="/filtrarCuentaCorriente.html", method = RequestMethod.POST)
	public String filtrarCuentaCorriente(
			@ModelAttribute("facturaBusqueda") Factura facturaBusqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos
			){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(facturaBusqueda, result);
		if(!result.hasErrors()){
			session.setAttribute("facturaBusqueda", facturaBusqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarCuentaCorriente(facturaBusqueda,result,session, atributos);
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar CuentaCorriente.
	 * 
	 * @param idDireccion el id de CuentaCorriente a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarCuentaCorriente.html",
			method = RequestMethod.GET
		)
	public String eliminarCuentaCorriente(HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos,
			BindingResult result) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		
		//Eliminamos la factura del tipo Recibo
		// Tambien eliminamos las facturas que estan en la tabla doctoCtaCte.
		Factura factura = facturaService.obtenerPorId(id);
		// Eliminar el Medio de Pago
		commit = medioPagoReciboService.eliminarMedioPagoRecibo(factura);
		// Eliminar el doctoCtaCte
		if(commit==true){
			commit = doctoCtaCteService.eliminarDoctoCtaCte(factura);
		}
		if(commit==true){
			commit = facturaService.eliminarFactura(factura);
		}	
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit==true){
			mensaje = new ScreenMessageImp("formularioCuentaCorriente.notificacion.facturaEliminada", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarCuentaCorriente(factura,result,session, atributos);
	}
	
	@RequestMapping(value="/mostrarFacturaDocto.html", method = RequestMethod.GET)
	public String mostrarFacturaDocto(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="id", required=false) Long id){
		//buscamos en la base de datos y lo subimos a request.
		
		Factura f= facturaService.obtenerPorId(id);
		List<DoctoCtaCte> doctoCtaCteList = doctoCtaCteService.getByFactura(f);
		atributos.put("doctoCtaCteList", doctoCtaCteList);
		return "formularioListaFacturaDocto";
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private BindingResult generateErrors(List<String> codigoErrores) {
		BindingResult result = new BeanPropertyBindingResult(new Object(),"");
		if (!codigoErrores.isEmpty()) {
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(	"error.formBookingGroup.general", codigo, null, false, new String[] { codigo }, null, "?"));
			}
		}
		return result;
	}
}
