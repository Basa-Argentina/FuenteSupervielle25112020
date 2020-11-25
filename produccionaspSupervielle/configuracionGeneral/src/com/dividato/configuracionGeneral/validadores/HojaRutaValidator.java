
package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.HojaRuta;

/**
 * 
 * @author Luis Manzanelli
 *
 */
@Component
public class HojaRutaValidator implements Validator{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class type) {
		return HojaRuta.class.isAssignableFrom(type);
	}

	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"fecha","fechaSalida"});
		binder.registerCustomEditor(Date.class, "fecha",new CustomDateEditor(formatoFechaFormularios, false));
		binder.registerCustomEditor(Date.class, "fechaSalida",new CustomDateEditor(formatoFechaFormularios, false));
	}

	@Override
	public void validate(Object command, Errors errors) {
		HojaRuta hojaRuta = (HojaRuta) command;
		if(hojaRuta.getEmpresa()==null){
			errors.rejectValue("codigoEmpresa", "required");
		}
		if(hojaRuta.getSucursal()==null){
			errors.rejectValue("codigoSucursal", "required");
		}
		if(hojaRuta.getClienteEmp()==null){
			errors.rejectValue("codigoCliente", "required");
		}
		if(hojaRuta.getTransporte()==null){
			errors.rejectValue("codigoTransporte", "required");
		}
		if(hojaRuta.getSerie()==null){
			errors.rejectValue("codigoSerie", "required");
		}
		if(hojaRuta.getNumero()==null){
			errors.rejectValue("numeroStr", "required");
		}
		if(hojaRuta.getFechaSalida()==null){
			errors.rejectValue("fechaSalida", "required");
		}
		if(hojaRuta.getHoraSalida()==null || hojaRuta.getHoraSalida().equals("")){
			errors.rejectValue("horaSalida", "required");
		}
		
		
	}

}
