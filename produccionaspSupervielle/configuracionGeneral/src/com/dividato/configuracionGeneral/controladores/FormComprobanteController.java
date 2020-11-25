package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.dividato.configuracionGeneral.validadores.MedioPagoReciboValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DoctoCtaCteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.DoctoCtaCte;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.MedioPagoRecibo;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de CuentaCorriente.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author X
 *
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/mostrarFormularioComprobante.html",
				"/guardarActualizarComprobante.html",
				"/eliminarComprobante.html"
			}
		)
public class FormComprobanteController {
	private FacturaService facturaService;
	private DoctoCtaCteService doctoCtaCteService; 
	@SuppressWarnings("unused")
	private MedioPagoReciboValidator validator;
	
	@Autowired
	public void setService(FacturaService facturaService,
			DoctoCtaCteService doctoCtaCteService) {
		this.facturaService = facturaService;
		this.doctoCtaCteService = doctoCtaCteService;
		
	}
	
	@Autowired
	public void setValidator(MedioPagoReciboValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		//validator.initDataBinder(binder);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/eliminarComprobante.html")
	public String eliminarComprobante(
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletRequest request){
		
		
			List<Factura> facturaList = (List<Factura>)session.getAttribute("comprobanteList");
			for(Factura f:facturaList){
				if(f.getId()==id){
					facturaList.remove(f);
				}
			}
		return "formularioCuentaCorriente";
	}
	/*
	 * Medio de Cobro
	 * 
	 * */	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(
			value="/mostrarFormularioComprobante.html",
			method = RequestMethod.GET
		)
	public String mostrarFormularioComprobante(
			HttpSession session,
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			Map<String,Object> atributos) {
		
		// Listar los comprobantes, Facturas de tipo.
	
		String tipoFacturas [] = {"F","ND"};
		List<Factura> comprobanteList = facturaService.listarFacturasFiltradasPorAfipTipoComprobante(tipoFacturas, codigoCliente, obtenerClienteAspUser());
		List<Factura> comprobanteFormularioList = (List<Factura>)session.getAttribute("comprobanteList");
		if(comprobanteFormularioList!=null){
			for (Factura f: comprobanteFormularioList){
				if(comprobanteList!=null){
					Iterator it = comprobanteList.iterator();
					while(it.hasNext()){
						Factura	fr  = (Factura) it.next();
						if(fr.getId().equals(f.getId())){
							it.remove();
						}
					}
				}
			}
		}
		
		atributos.put("codigoCliente",codigoCliente);
		atributos.put("comprobanteList", calculoSaldoDisponible(comprobanteList));
		atributos.put("accion", accion);
		return "formularioComprobante";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/guardarActualizarComprobante.html",
			method= RequestMethod.POST
		)
	public String guardarActualizarComprobante(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="comprobanteSeleccionada",required=false) String comprobanteSeleccionada,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@ModelAttribute("medioPagoReciboForm") final MedioPagoRecibo medioPagoReciboForm,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
			
		    //session.removeAttribute("comprobanteList");
		    // Cargo la Lista en session con las cuentas seleccionadas
			String cuentas[] = comprobanteSeleccionada.split(",");
			List<Factura> comprobanteList = (List<Factura>)session.getAttribute("comprobanteList");
			if(comprobanteList==null)
				comprobanteList = new ArrayList<Factura>();
			for (String i:cuentas){
				comprobanteList.add(facturaService.obtenerPorId(Long.valueOf(i)));
			}
			// Calculo el saldo Disponible para ser mostrado en la 
			// grilla Comprobante del Formulario Cuenta Corriente.
			session.setAttribute("comprobanteList", calculoSaldoDisponible(comprobanteList));
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeCuentaCorrienteReg = new ScreenMessageImp("formularioComprobante.notificacion.comprobanteRegistroExito", null);
			avisos.add(mensajeCuentaCorrienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		
		return mostrarFormularioComprobante(session, accion, medioPagoReciboForm.getId(), codigoCliente, atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private List<Factura> calculoSaldoDisponible(List<Factura> lista){
		for(Factura f:lista){
				List<DoctoCtaCte> doctoCtaCtes = doctoCtaCteService.getByFactura(f);
				Double importe = 0.0;
				for(DoctoCtaCte d : doctoCtaCtes){ 
					importe+=d.getImporte();
				}
				BigDecimal saldoDisponible = f.getTotalFinal().subtract(BigDecimal.valueOf(importe)); 
				f.setSaldoDisponible(saldoDisponible);
		}
		return lista;
	}
}	
