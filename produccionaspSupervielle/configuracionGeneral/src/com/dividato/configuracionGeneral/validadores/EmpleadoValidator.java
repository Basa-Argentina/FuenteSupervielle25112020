package com.dividato.configuracionGeneral.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.general.TipoDocumento;
import com.security.recursos.ValidacionEMail;
import com.security.recursos.ValidacionUsername;
import com.security.utils.CuitUtils;
/**
 * 
 * @author Noriega Gonzalo
 *
 */
@Component
public class EmpleadoValidator implements Validator {
	
	private EmpleadoService empleadoService;
	private TipoDocumentoService tipoDocumentoService;
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return Empleado.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"clienteCodigo","codigo","username","persona.nombre","persona.apellido",
				"idTipoDoc","persona.numeroDoc","persona.direccion.calle","persona.direccion.numero","persona.mail"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Empleado empleado = (Empleado) command;
		if(empleado.getAccion().equals("NUEVO")){
			Empleado exists = empleadoService.verificarExistente(empleado, empleado.getCliente());
			if(exists != null){
				errors.rejectValue("codigo", "error.empleado.errorCodigo");
				return;
			}
			List<Empleado> listaSalida = empleadoService.listarTodosFiltradoPorLista(new CampoComparacion("username",empleado.getUsername()));
			if(listaSalida.size()>0){	
				errors.rejectValue("username", "error.empleado.errorUsername");
			}
			if(empleado.getPassword()==null || empleado.getPassword().equals(""))
				errors.rejectValue("password", "error.empleado.errorPassword");
			
			if(!empleado.getPassword().equals(empleado.getConfirmarContrasenia()))
				errors.rejectValue("confirmarContrasenia", "error.empleado.errorDistintasContrasenias");
		}
		if(empleado.getUsernameSinCliente() != null && !"".equals(empleado.getUsernameSinCliente())){
			if(!ValidacionUsername.validar(empleado.getUsernameSinCliente()))
				errors.rejectValue("username", "error.empleado.errorFormatoUsername");
		}
		if(empleado.getPassword() != null && !"".equals(empleado.getPassword())){			
			if(!empleado.getPassword().equals(empleado.getConfirmarContrasenia()))
				errors.rejectValue("confirmarContrasenia", "error.empleado.errorDistintasContrasenias");
		}
		if(empleado.getPersona().getMail()!=null && !"".equals(empleado.getPersona().getMail())){
			if(!ValidacionEMail.validar(empleado.getPersona().getMail())){
				errors.rejectValue("eMail", "error.empleado.errorFormatoEMail");
				return;
			}	
			Empleado empleado2 = empleadoService.obtenerPorEMail(empleado.getPersona().getMail());
			if(empleado.getId()!=null && empleado2!=null && !empleado.getId().equals(empleado2.getId()))
				errors.rejectValue("eMail", "error.empleado.errorEMail");
		}
		if(empleado.getIdTipoDoc() != null){
			TipoDocumento tipoDoc = tipoDocumentoService.obtenerPorId(empleado.getIdTipoDoc());			
			
			if(tipoDoc != null && ("CUIT".equals(tipoDoc.getCodigo()) || "CUIL".equals(tipoDoc.getCodigo()))
					&& (empleado.getPersona().getNumeroDoc() != null && !CuitUtils.validar(empleado.getPersona().getNumeroDoc()))){
				errors.rejectValue("persona.numeroDoc", "formularioEmpleado.errorFormato"+tipoDoc.getCodigo());
				return;
			}
		}
		if(empleado.getIdBarrio() == null || empleado.getIdBarrio() == 0){	
			errors.rejectValue("idBarrio", "required");
		}
		if(empleado.getObservaciones() != null && !empleado.getObservaciones().equalsIgnoreCase("") && empleado.getObservaciones().length()>500){
			errors.rejectValue("observaciones", "error.empleado.errorObservaciones");
		}
	}		
}