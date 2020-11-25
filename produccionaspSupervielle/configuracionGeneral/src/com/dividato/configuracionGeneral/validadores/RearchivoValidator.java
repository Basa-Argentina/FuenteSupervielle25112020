
package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.Rearchivo;

/**
 * 
 * @author Gabriel Mainero
 *
 */
@Component
public class RearchivoValidator implements Validator{

	@Override
	public boolean supports(Class type) {
		return ClasificacionDocumental.class.isAssignableFrom(type);
	}

	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fecha1",new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fecha2",new CustomDateEditor(formatoFechaFormularios, true));
	}

	@Override
	public void validate(Object command, Errors errors) {
		Rearchivo rearchivo = (Rearchivo)command;
		
		if(rearchivo.getLoteRearchivo() != null && rearchivo.getLoteRearchivo().getIndiceIndividual()){
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndividualNumero1Requerido()){
				if(rearchivo.getNumero1()==null){
					errors.rejectValue("numero1","required");
				}
			}
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndividualNumero2Requerido()){
				if(rearchivo.getNumero2()==null){
					errors.rejectValue("numero2", "required");
				}
			}
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndividualFecha1Requerido()){
				if(rearchivo.getFecha1()==null){
					errors.rejectValue("fecha1", "required");
				}
			}
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndividualFecha2Requerido()){
				if(rearchivo.getFecha2()==null){
					errors.rejectValue("fecha2", "required");
				}
			}
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndividualTexto1Requerido()){
				if(rearchivo.getTexto1()==null || rearchivo.getTexto1().trim().isEmpty()){
					errors.rejectValue("texto1", "required");
				}
			}
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndividualTexto2Requerido()){
				if(rearchivo.getTexto2()==null || rearchivo.getTexto2().trim().isEmpty()){
					errors.rejectValue("texto2", "required");
				}
			}
		}else{
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getGrupalNumeroRequerido()){
				if(rearchivo.getNumero1()==null){
					errors.rejectValue("numero1", "required");
				}
				if(rearchivo.getNumero2()==null){
					errors.rejectValue("numero2", "required");
				}
			}
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getGrupalFechaRequerido()){
				if(rearchivo.getFecha1()==null){
					errors.rejectValue("fecha1", "required");
				}
				if(rearchivo.getFecha2()==null){
					errors.rejectValue("fecha2", "required");
				}
			}
			if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getGrupalTextoRequerido()){
				if(rearchivo.getTexto1()==null || rearchivo.getTexto1().trim().isEmpty()){
					errors.rejectValue("texto1", "required");
				}
				if(rearchivo.getTexto2()==null || rearchivo.getTexto2().trim().isEmpty()){
					errors.rejectValue("texto2", "required");
				}
			}
		}
		if(rearchivo.getLoteRearchivo().getClasificacionDocumental().getDescripcionRequerido()){
			if(rearchivo.getDescripcion()==null || rearchivo.getDescripcion().trim().isEmpty()){
				errors.rejectValue("descripcion", "required");
			}
		}
		
	}

}
