package com.dividato.security.validadores;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.accesoDatos.interfaz.PersonaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.Persona;
import com.security.recursos.RecursosPassword;
import com.security.recursos.ValidacionEMail;
import com.security.utils.CuitUtils;
/**
 * 
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ClienteValidator implements Validator {	
	private ClienteAspService clienteService;
	private PersonaService personaService;
	private static Logger logger=Logger.getLogger(ClienteValidator.class);
	
	@Autowired
	public void setPersonaService(PersonaService personaService) {
		this.personaService = personaService;
	}
	@Autowired
	public void setClienteService(ClienteAspService clienteService) {
		this.clienteService = clienteService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return ClienteAsp.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
//		binder.setRequiredFields(new String[] {});
		binder.setRequiredFields(new String[] {
				"persona.razonSocial","nombreAbreviado","persona.direccion.calle","persona.direccion.numero","persona.direccion.idBarrio",
				"persona.numeroDoc","contacto.nombre", "contacto.apellido","contacto.mail","user.username"
		});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		ClienteAsp cliente = (ClienteAsp) command;
		
		if(cliente.getAccion().equals("NUEVO")){
			
			try{
				List<ClienteAsp> clientes = clienteService.listarTodosFiltradoPorLista(new CampoComparacion("nombreAbreviado", cliente.getNombreAbreviado()));
				if(clientes != null && !clientes.isEmpty())
					errors.rejectValue("nombreAbreviado", "error.cliente.errornombreAbreviado");
			}catch(NullPointerException e){
				logger.error("Error en la 1 validacion", e);
			}
			
			try{
				if(cliente.getUser().getPassword() != null && !"".equals(cliente.getUser().getPassword())){
					if(!cliente.getUser().getPassword().equals(cliente.getUser().getConfirmarContrasenia()))
						errors.rejectValue("user.password", "error.cliente.errorDistintasContrasenias");
					if(!RecursosPassword.validar(cliente.getUser().getPassword()))
						errors.rejectValue("user.password", "error.cliente.errorPassword");
				}
			}catch(NullPointerException e){
				logger.error("Error en la 2 validacion", e);
			}
			
			try{
				if(!ValidacionEMail.validar(cliente.getContacto().getMail())){
					errors.rejectValue("contacto.mail", "error.cliente.errorFormatoMail");
					return;
				}
			}catch(NullPointerException e){
				logger.error("Error en la 3 validacion", e);
			}
			
			try{
				Persona persona = personaService.obtenerPorMail(cliente.getContacto().getMail());
				if(persona != null)
					errors.rejectValue("contacto.mail", "error.cliente.errorMail");
			}catch(NullPointerException e){
				logger.error("Error en la 4 validacion", e);
			}
			
		}//Aca termina si es Nuevo
		
		try{
			if(cliente.getPersona().getMail()!=null && !"".equals(cliente.getPersona().getMail())){
				if(!ValidacionEMail.validar(cliente.getPersona().getMail())){
					errors.rejectValue("persona.mail", "error.cliente.errorFormatoMail");
					return;
				}	
			}
		}catch(NullPointerException e){
			logger.error("Error en la 5 validacion", e);
		}
		
		try{
			if(cliente.getPersona().getDireccion().getBarrio()!=null){
				if(cliente.getPersona().getDireccion().getIdBarrio() == 0){
					errors.rejectValue("persona.direccion.idBarrio", "error.cliente.errorBarrioCliente");
					return;
				}	
			}
		}catch(NullPointerException e){
			logger.error("Error en la 6 validacion", e);
		}
		
		try{
			if(cliente.getContacto().getDireccion().getIdBarrio()!=null){
				if(cliente.getContacto().getDireccion().getIdBarrio() == 0){
					errors.rejectValue("contacto.direccion.idBarrio", "error.cliente.errorBarrioContacto");
					return;
				}	
			}
		}catch(NullPointerException e){
			logger.error("Error en la 7 validacion", e);
		}
		
		//Valido el CUIT/CUIL del cliente
		try{
			if("CUIT".equals(cliente.getPersona().getTipoDoc().getDescripcion()) || "CUIL".equals(cliente.getPersona().getTipoDoc().getDescripcion()))
					if(!CuitUtils.validar(cliente.getPersona().getNumeroDoc()))
							errors.rejectValue("persona.numeroDoc", "error.cliente.formatoNumDocInvalido");
		}catch(NullPointerException e){
			logger.error("Error en la 7 validacion", e);
		}	
	}		
}