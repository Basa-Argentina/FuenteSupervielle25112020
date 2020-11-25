package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.CuentaCorrienteCliente;
import com.security.modelo.general.TipoDocumento;
import com.security.modelo.seguridad.User;
import com.security.recursos.ValidacionEMail;
import com.security.utils.CuitUtils;
/**
 * 
 * @author X
 *
 */
@Component
public class CuentaCorrienteValidator implements Validator {	
	private ClienteEmpService clienteEmpService;
	private TipoDocumentoService tipoDocumentoService;
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return CuentaCorrienteCliente.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
//		binder.setRequiredFields(new String[] {});
		binder.setRequiredFields(new String[] {
				"codigoEmpresa","codigo", "idTipoDocSel", "numeroDoc", "direccion.numero",
				"direccion.calle","idAfipCondIva"
		});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		ClienteEmp clienteEmp = (ClienteEmp)command;
		if(clienteEmp.getAccion().equals("NUEVO")){
			ClienteEmp clienteEmps = clienteEmpService.verificarExistente(clienteEmp, obtenerClienteAspUser());
			if(clienteEmps != null)
				errors.rejectValue("codigo", "formularioCliente.errorCodigo");
		}
		if(clienteEmp.getEmail() != null && !"".equals(clienteEmp.getEmail()) && !ValidacionEMail.validar(clienteEmp.getEmail())){
			errors.rejectValue("email", "formularioCliente.errorFormatoMail");
			return;
		}
		if(clienteEmp.getEmail() != null && !"".equals(clienteEmp.getEmail()) && !ValidacionEMail.validar(clienteEmp.getEmail())){
			errors.rejectValue("email", "formularioCliente.errorFormatoMail");
			return;
		}
		if(clienteEmp.getIdTipoDocSel() != null){
			TipoDocumento tipoDoc = tipoDocumentoService.obtenerPorId(clienteEmp.getIdTipoDocSel());			
			
			if(tipoDoc != null && ("CUIT".equals(tipoDoc.getCodigo()) || "CUIL".equals(tipoDoc.getCodigo()))
					&& (clienteEmp.getNumeroDoc() != null && !CuitUtils.validar(clienteEmp.getNumeroDoc()))){
				errors.rejectValue("numeroDoc", "formularioCliente.errorFormato"+tipoDoc.getCodigo());
				return;
			}
		}
		if(clienteEmp.getIdBarrio() == null || clienteEmp.getIdBarrio() == 0){	
			errors.rejectValue("idBarrio", "required");
		}
		if(clienteEmp.getTipoPersona()!=null && !"".equals(clienteEmp.getTipoPersona())){
			if(clienteEmp.getTipoPersona().equalsIgnoreCase("Juridica") && 
					(clienteEmp.getRazonSocial()==null || clienteEmp.getRazonSocial().getRazonSocial().equalsIgnoreCase("") )){
				errors.rejectValue("razonSocial.razonSocial", "required");
			}
			if(clienteEmp.getTipoPersona().equalsIgnoreCase("Fisica") && clienteEmp.getApellido().equalsIgnoreCase("")){
				errors.rejectValue("apellido", "required");
			}
			if(clienteEmp.getTipoPersona().equalsIgnoreCase("Fisica") && clienteEmp.getNombre().equalsIgnoreCase("")){
				errors.rejectValue("nombre", "required");
			}
		}
		if(clienteEmp.getObservaciones() != null && !clienteEmp.getObservaciones().equalsIgnoreCase("") && clienteEmp.getObservaciones().length()>500){
			errors.rejectValue("observaciones", "formularioCliente.errorObservaciones");
		}
		if(clienteEmp.getNotasFacturacion() != null && !clienteEmp.getNotasFacturacion().equalsIgnoreCase("") && clienteEmp.getNotasFacturacion().length()>500){
			errors.rejectValue("notasFacturacion", "formularioCliente.errorNotasFacturacion");
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}