package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.Transporte;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.recursos.ValidacionEMail;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class ConceptoOperacionClienteValidator implements Validator {	
	private ConceptoOperacionClienteService conceptoOperacionClienteService;
	private ClienteEmpService clienteEmpService;
	private ConceptoFacturableService conceptoFacturableService;
	private ListaPreciosService listaPreciosService;
	
	@Autowired
	public void setConceptoOperacionClienteService(ConceptoOperacionClienteService conceptoOperacionClienteService) {
		this.conceptoOperacionClienteService = conceptoOperacionClienteService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return ConceptoOperacionClienteService.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaAlta",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.setRequiredFields(new String[] {
				"codigoCliente","codigoConcepto","listaPreciosCodigo","cantidad","precio"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		ConceptoOperacionCliente conceptoOperacionCliente = (ConceptoOperacionCliente) command;
		ClienteEmp cliente = new ClienteEmp();
		
		//Validar y Setear Cliente
		if(conceptoOperacionCliente.getCodigoCliente() != null && !"".equals(conceptoOperacionCliente.getCodigoCliente())){
		cliente = clienteEmpService.getByCodigo(conceptoOperacionCliente.getCodigoCliente(), conceptoOperacionCliente.getCodigoEmpresa(),conceptoOperacionCliente.getClienteAsp());
			if(cliente != null)
			{
				conceptoOperacionCliente.setClienteEmp(cliente);
			}else
			{
				errors.rejectValue("codigoCliente","formularioConceptoOperacionCliente.error.clienteNoExiste");
			}
		}else
		{
			errors.rejectValue("codigoCliente","formularioConceptoOperacionCliente.error.codigoClienteVacio");
		}
		
		//Validar y Setear Concepto
		if(conceptoOperacionCliente.getCodigoConcepto() != null && !"".equals(conceptoOperacionCliente.getCodigoConcepto())){
			ConceptoFacturable conceptoFacturable = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(conceptoOperacionCliente.getCodigoConcepto(), conceptoOperacionCliente.getClienteAsp());
			if(conceptoFacturable != null)
			{
				conceptoOperacionCliente.setConceptoFacturable(conceptoFacturable);
			}else
			{
				errors.rejectValue("codigoCliente","formularioConceptoOperacionCliente.error.conceptoFacturableNoExiste");
			}
		}else
		{
			errors.rejectValue("codigoCliente","formularioConceptoOperacionCliente.error.codigoConceptoFacturableVacio");
		}
		
		//Validar y Setear ListaPrecios
		if(conceptoOperacionCliente.getListaPreciosCodigo() != null && !"".equals(conceptoOperacionCliente.getListaPreciosCodigo())){
			ListaPrecios listaPrecios = listaPreciosService.obtenerListaPreciosPorCodigo(conceptoOperacionCliente.getListaPreciosCodigo(), conceptoOperacionCliente.getClienteAsp(), conceptoOperacionCliente.getCodigoConcepto(), cliente, true);
			if(listaPrecios != null)
			{
				conceptoOperacionCliente.setListaPrecios(listaPrecios);
			}else
			{
				errors.rejectValue("codigoCliente","formularioConceptoOperacionCliente.error.listaPreciosNoExiste");
			}
		}else
		{
			errors.rejectValue("codigoCliente","formularioConceptoOperacionCliente.error.listaPreciosVacia");
		}
	}		
}