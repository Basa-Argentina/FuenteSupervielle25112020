package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoService;
import com.security.modelo.configuraciongeneral.MedioPagoRecibo;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class MedioPagoReciboValidator implements Validator {
	
	private GrupoService grupoService;
	
	@Autowired
	public void setGrupoService(GrupoService grupoService) {
		this.grupoService = grupoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return MedioPagoRecibo.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigoSeccion","codigo","descripcion", "verticales", "horizontales"
				, "modulosVert", "modulosHor"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		
	}
	
	
}