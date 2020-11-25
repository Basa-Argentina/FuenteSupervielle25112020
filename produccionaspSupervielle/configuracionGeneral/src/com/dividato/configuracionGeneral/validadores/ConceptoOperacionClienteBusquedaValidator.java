package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.jerarquias.ConceptoOperacionCliente;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class ConceptoOperacionClienteBusquedaValidator implements Validator {
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return ConceptoOperacionCliente.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaDesde",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaHasta",
				new CustomDateEditor(formatoFechaFormularios, true));
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		ConceptoOperacionCliente conceptoOperacionCliente = (ConceptoOperacionCliente) command;

		if (conceptoOperacionCliente.getCantidadDesde() != null && conceptoOperacionCliente.getCantidadHasta() != null) {
			if (conceptoOperacionCliente.getCantidadDesde() > conceptoOperacionCliente.getCantidadHasta()) {
				errors.rejectValue("cantidadDesde",
						"formularioConceptoOperacionCliente.error.cantidadDesdeMayorHasta");

			}
		}
		
		if (conceptoOperacionCliente.getFechaDesde() != null	&& conceptoOperacionCliente.getFechaHasta() != null) {
			if (conceptoOperacionCliente.getFechaDesde().after(conceptoOperacionCliente.getFechaHasta())) {
				errors.rejectValue("fechaDesde","formularioConceptoOperacionCliente.error.fechaDesdeMayorHasta");

			}
		}
		
		if (conceptoOperacionCliente.getFinalTotalDesde() != null && conceptoOperacionCliente.getFinalTotalHasta() != null) {
			int resultado = conceptoOperacionCliente.getFinalTotalDesde().compareTo(conceptoOperacionCliente.getFinalTotalHasta());
			if (resultado == 1) 
			{
				errors.rejectValue("finalTotalDesde","formularioConceptoOperacionCliente.error.finalTotalDesdeMayorHasta");
			}
		}
		
		if (conceptoOperacionCliente.getFinalUnitarioDesde() != null && conceptoOperacionCliente.getFinalUnitarioHasta() != null) {
			int resultado = conceptoOperacionCliente.getFinalUnitarioDesde().compareTo(conceptoOperacionCliente.getFinalUnitarioHasta());
			if (resultado == 1) 
			{
				errors.rejectValue("finalUnitarioDesde","formularioConceptoOperacionCliente.error.finalUnitarioDesdeMayorHasta");
			}
		}
		
		if (conceptoOperacionCliente.getPrefijoRequerimientoDesde() != null && !conceptoOperacionCliente.getPrefijoRequerimientoDesde().equals("")
				&& conceptoOperacionCliente.getPrefijoRequerimientoHasta() != null && !conceptoOperacionCliente.getPrefijoRequerimientoHasta().equals("")) {
			if (parseLongCodigo(conceptoOperacionCliente.getPrefijoRequerimientoDesde()) > parseLongCodigo(conceptoOperacionCliente.getPrefijoRequerimientoHasta())) 
			{
				errors.rejectValue("prefijoRequerimientoDesde","formularioConceptoOperacionCliente.error.prefijoRequerimientoDesdeMayorHasta");
			}
			else if(conceptoOperacionCliente.getPrefijoRequerimientoDesde() == conceptoOperacionCliente.getPrefijoRequerimientoHasta())
			{
				if(conceptoOperacionCliente.getNumeroRequerimientoDesde() != null && !("").equals(conceptoOperacionCliente.getNumeroRequerimientoDesde())
						&& conceptoOperacionCliente.getNumeroRequerimientoHasta() != null && !("").equals(conceptoOperacionCliente.getNumeroRequerimientoHasta()))
				{
					if (parseLongCodigo(conceptoOperacionCliente.getNumeroRequerimientoDesde()) > parseLongCodigo(conceptoOperacionCliente.getNumeroRequerimientoHasta())) 
					{
						errors.rejectValue("numeroRequerimientoDesde","formularioConceptoOperacionCliente.error.numeroRequerimientoDesdeMayorHasta");
					}
					
				}
			}
		}
	}	
	
	private Long parseLongCodigo(String codigo){
		Long result= null;
		//si el codigo es distinto de vacio o null
		if(codigo!=null && codigo.length()>0){
			//cuenta el primer digito diferente de 0
			int cont = 0;
			while(codigo.substring( cont, cont).equals("0")){
				cont++;
			}
			//si el codigo esta formado solo por 0
			if(cont == codigo.length()-1){
				result = new Long(0);
			}else{
				//devuelve el Integer formado por el substring desde el cont hasta el final del codigo
				result = Long.parseLong(codigo.substring(cont));
			}
		}else{
			result = new Long(0);
		}
		return result;
	}
}