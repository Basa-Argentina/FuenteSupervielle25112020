
package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.LoteRearchivo;

/**
 * 
 * @author Gabriel Mainero
 *
 */
@Component
public class LoteRearchivoValidator implements Validator{

	@Override
	public boolean supports(Class type) {
		return LoteRearchivo.class.isAssignableFrom(type);
	}

	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"fechaRegistro"});
		binder.registerCustomEditor(Date.class, "fechaRegistro",new CustomDateEditor(formatoFechaFormularios, true));
	}

	@Override
	public void validate(Object command, Errors errors) {
		LoteRearchivo lote = (LoteRearchivo) command;
		if(lote.getEmpresa()==null){
			errors.rejectValue("codigoEmpresa", "required");
		}
		if(lote.getSucursal()==null){
			errors.rejectValue("codigoSucursal", "required");
		}
		if(lote.getClienteEmp()==null){
			errors.rejectValue("codigoCliente", "required");
		}
		if(lote.getClasificacionDocumental()==null){
			errors.rejectValue("clasificacionDocumental", "required");
			return;
		}
		if(lote.getContenedor()==null){
			errors.rejectValue("contenedor", "required");
		}
	}

}
