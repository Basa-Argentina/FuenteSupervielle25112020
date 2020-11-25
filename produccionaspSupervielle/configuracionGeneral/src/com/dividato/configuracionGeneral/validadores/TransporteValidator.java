package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Transporte;
import com.security.recursos.ValidacionEMail;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class TransporteValidator implements Validator {	
	private TransporteService transporteService;
	private EmpresaService empresaService;
	
	@Autowired
	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}

	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Transporte.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {
				"codigo","descripcion","codigoEmpresa"
		});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Transporte transporte = (Transporte)command;
		if(transporte.getAccion().equals("NUEVO")){
			Transporte transportes = null;
			transportes = transporteService.verificarTransporte(transporte);
			
			if(transportes != null)
				errors.rejectValue("codigo", "formularioTransporte.errorCodigo");
		}
	}		
}