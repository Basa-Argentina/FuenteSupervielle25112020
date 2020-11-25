package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class LecturaValidator implements Validator {	
	private LecturaService lecturaService;
	
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Lectura.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {
				"descripcion","fecha"});
		binder.registerCustomEditor(Date.class, "fecha",
				new CustomDateEditor(formatoFechaFormularios, true));
	}
	
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		Lectura lectura = (Lectura) command;
		
		//Validar detalles
		if (lectura.getDetalles() == null || lectura.getDetalles().size() == 0) {
			errors.rejectValue("detalles","formularioLectura.errorDetallesVacios");
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}