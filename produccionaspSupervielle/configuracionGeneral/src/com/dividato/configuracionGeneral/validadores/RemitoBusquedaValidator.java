package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.Remito;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class RemitoBusquedaValidator implements Validator {
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Remito.class.isAssignableFrom(type);
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
		Remito remito = (Remito) command;
		if (remito.getNumeroDesde() != null && "".equals(remito.getNumeroDesde().trim())
				&& remito.getNumeroHasta() != null && "".equals(remito.getNumeroHasta().trim())) {
			if (parseLongCodigo(remito.getNumeroDesde()) > parseLongCodigo(remito.getNumeroHasta())) {
				errors.rejectValue("numeroDesde",
						"formularioRemito.errorNumeroDesde");

			}
		}
		
		if (remito.getFechaDesde() != null	&& remito.getFechaHasta() != null) {
			if (remito.getFechaDesde().after(remito.getFechaHasta())) {
				errors.rejectValue("fechaDesde","formularioRemito.errorFechaDesde");

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