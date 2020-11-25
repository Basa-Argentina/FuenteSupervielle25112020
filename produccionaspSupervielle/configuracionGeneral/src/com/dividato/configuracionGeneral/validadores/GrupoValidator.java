package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Grupo;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class GrupoValidator implements Validator {
	
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
		return Grupo.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigoEmpresa","codigoSucursal","codigoDeposito","codigoSeccion","codigo","descripcion", "verticales", "horizontales"
				, "modulosVert", "modulosHor"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Grupo grupo = (Grupo) command;
		if(grupo.getAccion().equals("NUEVO")){	
			Grupo exists= grupoService.verificarGrupo(grupo, obtenerClienteAspUser());
			if(exists != null)
				errors.rejectValue("codigo", "formularioGrupo.errorCodigo");
		}
		
		if(grupo.getVerticales()%grupo.getModulosVert()!=0){
			errors.rejectValue("modulosVert", "formularioGrupo.errorModulosVert");
		}
		if(grupo.getHorizontales()%grupo.getModulosHor()!=0){
			errors.rejectValue("modulosHor", "formularioGrupo.errorModulosHor");
		}
		try{
			Integer.valueOf(grupo.getCodigo());
		}catch (NumberFormatException e) {
			errors.rejectValue("codigo", "formularioGrupo.errorFormat");
			grupo.setCodigo("0");
		}		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}