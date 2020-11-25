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

import com.security.accesoDatos.configuraciongeneral.interfaz.CaiService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Cai;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class CaiValidator implements Validator {	
	private CaiService caiService;
	
	@Autowired
	public void setCaiService(CaiService caiService) {
		this.caiService = caiService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Cai.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		//try{
			binder.registerCustomEditor(Date.class, "fechaVencimiento", new CustomDateEditor(formatoFechaFormularios,true));
		/*}catch (Exception e) {
			MapBindingResult br = new MapBindingResult(new HashMap(),"fechaVencimiento");
			br.rejectValue("fechaVencimiento", "formularioCai.errorFechaVencimiento");
			DefaultBindingErrorProcessor error = new DefaultBindingErrorProcessor();
			error.processMissingFieldError("fechaVencimiento", br);
			binder.setBindingErrorProcessor(error);
		}*/
		binder.setRequiredFields(new String[] {
				"numero","fechaVencimiento"
		});
	}
	
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Cai cai = (Cai)command;
		if(cai.getAccion().equals("NUEVO")){
			Cai cais = caiService.verificarExistente(cai, obtenerClienteAspUser());
			if(cais != null)
				errors.rejectValue("numero", "formularioCai.error");			
			
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}