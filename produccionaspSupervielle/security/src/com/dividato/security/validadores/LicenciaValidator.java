package com.dividato.security.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.interfaz.LicenciaService;
import com.security.modelo.administracion.Licencia;
import com.security.utils.DateUtil;
/**
 * 
 * @author Ezequiel Beccaria
 *
 */
@Component
public class LicenciaValidator implements Validator {	
	private LicenciaService licenciaService;
		
	@Autowired
	public void setLicenciaService(LicenciaService licenciaService) {
		this.licenciaService = licenciaService;
	}

	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return Licencia.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"fechaDesde", "fechaHasta"});
		binder.registerCustomEditor(Date.class, "fechaDesde", new CustomDateEditor(formatoFechaFormularios,true));
		binder.registerCustomEditor(Date.class, "fechaHasta", new CustomDateEditor(formatoFechaFormularios,true));
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Licencia licencia = (Licencia) command;
		//valido rango de fechas
		if(licencia.getFechaDesde() != null && licencia.getFechaHasta() != null)
			if(licencia.getFechaHasta().before(licencia.getFechaDesde()))
				errors.rejectValue("fechaDesde", "formularioLicencia.error.rangoFechasS");
	}	
	
	public void validateNewLicencia(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Licencia licencia = (Licencia) command;
		//valido rango de fechas
		if(licencia.getFechaDesde() != null && licencia.getFechaHasta() != null){
			if(licencia.getFechaHasta().before(licencia.getFechaDesde())){
				errors.rejectValue("fechaDesde", "error.licencia.rangoFechas");				
			}else{
				if(licencia.getFechaDesde().before(DateUtil.getDateFrom(new Date())) && !"MODIFICACION".equals(licencia.getAccion()))
					errors.rejectValue("fechaDesde", "error.licencia.fechaDesdeAnteriorActual");
				else{
					List<Licencia> licencias = licenciaService.obtenerLicenciaPorFecha(
							licencia.getFechaDesde(), licencia.getFechaHasta(), licencia.getCliente());
					if(!licencias.isEmpty() && (licencias.size() > 1 || !licencias.get(0).getId().equals(licencia.getId())))
						errors.rejectValue("fechaDesde", "error.licencia.rangoFechasSuperpuesto");
				}
			}
		}			
	}
}