package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoFacturacionService;
import com.security.modelo.configuraciongeneral.GrupoFacturacion;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class GrupoFacturacionValidator implements Validator {	
	private GrupoFacturacionService grupoFacturacionService;
	
	@Autowired
	public void setGrupoFacturacionService(GrupoFacturacionService grupoFacturacionService) {
		this.grupoFacturacionService = grupoFacturacionService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return GrupoFacturacion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {
				"codigo","descripcion"
		});
	}
	
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		GrupoFacturacion grupoFacturacion = (GrupoFacturacion)command;
		if(grupoFacturacion.getAccion().equals("NUEVO")){
			GrupoFacturacion grupoFacturacions = grupoFacturacionService.verificarExistente(grupoFacturacion);
			if(grupoFacturacions != null)
				errors.rejectValue("codigo", "formularioGrupoFacturacion.error");			
			
		}
	}
}