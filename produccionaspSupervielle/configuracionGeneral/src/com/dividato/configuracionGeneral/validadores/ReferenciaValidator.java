
package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.text.NumberFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.Referencia;

/**
 * 
 * @author FedeMz
 *
 */
@Component
public class ReferenciaValidator implements Validator{

	@Override
	public boolean supports(Class type) {
		return ClasificacionDocumental.class.isAssignableFrom(type);
	}

	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fecha1",new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fecha2",new CustomDateEditor(formatoFechaFormularios, true));
		NumberFormat nf = NumberFormat.getNumberInstance();
		binder.registerCustomEditor(Long.class, "numero1",new CustomNumberEditor(Long.class, nf, true));
	}

	@Override
	public void validate(Object command, Errors errors) {
		Referencia referencia = (Referencia)command;
		
		if(referencia.getClasificacionDocumental()==null){
			errors.rejectValue("clasificacionDocumental", "required");
			return;
		}
		if(referencia.getContenedor()==null){
			errors.rejectValue("contenedor", "required");
		}
		if(referencia.getIndiceIndividual()!=null && referencia.getIndiceIndividual()){
			if(referencia.getElemento()==null || !referencia.getElemento().getTipoElemento().getContenido()){
				errors.rejectValue("elemento", "required");
			}
			if(referencia.getClasificacionDocumental().getIndividualNumero1Requerido()){
				if(referencia.getNumero1()==null){
					errors.rejectValue("numero1","required");
				}
			}
			if(referencia.getClasificacionDocumental().getIndividualNumero2Requerido()){
				if(referencia.getNumero2()==null){
					errors.rejectValue("numero2", "required");
				}
			}
			if(referencia.getClasificacionDocumental().getIndividualFecha1Requerido()){
				if(referencia.getFecha1()==null){
					errors.rejectValue("fecha1", "required");
				}
			}
			if(referencia.getClasificacionDocumental().getIndividualFecha2Requerido()){
				if(referencia.getFecha2()==null){
					errors.rejectValue("fecha2", "required");
				}
			}
			if(referencia.getClasificacionDocumental().getIndividualTexto1Requerido()){
				if(referencia.getTexto1()==null || referencia.getTexto1().trim().isEmpty()){
					errors.rejectValue("texto1", "required");
				}
			}
			if(referencia.getClasificacionDocumental().getIndividualTexto2Requerido()){
				if(referencia.getTexto2()==null || referencia.getTexto2().trim().isEmpty()){
					errors.rejectValue("texto2", "required");
				}
			}
		}
		else if(referencia.getPorRango()!=null && referencia.getPorRango()){
			
			if(referencia.getCodigoElementoDesde()==null || referencia.getCodigoElementoDesde().trim().isEmpty()){
				errors.rejectValue("codigoElementoDesde", "required");
			}
			if(referencia.getCodigoElementoHasta()==null || referencia.getCodigoElementoHasta().trim().isEmpty()){
				errors.rejectValue("codigoElementoHasta", "required");
			}
			
			if(referencia.getCodigoElementoDesde()!=null && !referencia.getCodigoElementoDesde().trim().isEmpty()
					&& referencia.getCodigoElementoHasta()!=null && !referencia.getCodigoElementoHasta().trim().isEmpty()){
				
				String preFijoTipo = referencia.getCodigoElementoDesde().substring(0, 2);
				if(!referencia.getCodigoElementoHasta().startsWith(preFijoTipo)){
					errors.rejectValue("codigoElementoDesde", "formularioLoteReferencia.error.rangoDesdeHastaDistintoTipo");
				}else{
					if(referencia.getCodigoElementoDesde().compareTo(referencia.getCodigoElementoHasta())>0){
						errors.rejectValue("codigoElementoDesde", "formularioLoteReferencia.error.rangoDesdeMayorHasta");
					}
				}
			}
			
				
		}else{
			if(referencia.getClasificacionDocumental().getGrupalNumeroRequerido()){
				if(referencia.getNumero1()==null){
					errors.rejectValue("numero1", "required");
				}
				if(referencia.getNumero2()==null){
					errors.rejectValue("numero2", "required");
				}
			}
			if(referencia.getClasificacionDocumental().getGrupalFechaRequerido()){
				if(referencia.getFecha1()==null){
					errors.rejectValue("fecha1", "required");
				}
				if(referencia.getFecha2()==null){
					errors.rejectValue("fecha2", "required");
				}
			}
			if(referencia.getClasificacionDocumental().getGrupalTextoRequerido()){
				if(referencia.getTexto1()==null || referencia.getTexto1().trim().isEmpty()){
					errors.rejectValue("texto1", "required");
				}
				if(referencia.getTexto2()==null || referencia.getTexto2().trim().isEmpty()){
					errors.rejectValue("texto2", "required");
				}
			}
		}
		if(referencia.getClasificacionDocumental().getDescripcionRequerido()){
			if(referencia.getDescripcion()==null || referencia.getDescripcion().trim().isEmpty()){
				errors.rejectValue("descripcion", "required");
			}
		}
		
		if(referencia.getFecha1()!=null && !referencia.getFecha1Str().equals(""))
		{
			int año = Integer.parseInt(referencia.getFecha1Str().substring(6, referencia.getFecha1Str().length()));
			if(año < 1800)
				errors.rejectValue("fecha1", "formularioLoteReferencia.error.fechaMenor1800");
		}
		
		if(referencia.getFecha2()!=null && !referencia.getFecha2Str().equals(""))
		{
			int año = Integer.parseInt(referencia.getFecha2Str().substring(6, referencia.getFecha2Str().length()));
			if(año < 1800)
				errors.rejectValue("fecha2", "formularioLoteReferencia.error.fechaMenor1800");
		}
		
		if( referencia.getFecha1()!= null && referencia.getFecha2() != null && referencia.getFecha1().after(referencia.getFecha2())){
			errors.rejectValue("fecha1", "formularioLoteReferencia.error.fecha1MayorFecha2");
		}
		
	}

}
