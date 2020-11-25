package com.dividato.configuracionGeneral.validadores.jerarquias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.modelo.jerarquias.TipoOperacion;

@Component
public class TipoOperacionValidator implements Validator{
	private TipoOperacionService service;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return TipoOperacion.class.isAssignableFrom(type);
	}
	
	@Autowired	
	public void setService(TipoOperacionService service) {
		this.service = service;
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(
				new String[] {"codigo","descripcion"/*,"conceptoFacturableCod"*/});
	}

	@Override
	public void validate(Object obj, Errors errors) {	
		TipoOperacion tipo = (TipoOperacion) obj;
		//valido que no se repita el codigo
		if(service.seRepiteCodigoTipoOperacion(tipo))
			errors.rejectValue("codigo", "error.tipoOperacion.codigoDuplicado");
		//si genera operación al cerrarse, debe tener un tipoOperacionSiguiente asociada
		if(tipo.getGeneraOperacionAlCerrarse() && tipo.getTipoOperacionSiguiente() == null)
			errors.rejectValue("tipoOperacionSiguienteCod", "error.tipoOperacion.noSeleccionTipoOperacionSig");
		//si imprime documento, debe tener seleccionada una serie y cargado un titulo para el documento
		if(tipo.getImprimeDocumento()){
			if(tipo.getTituloDocumento() == null || "".equals(tipo.getTituloDocumento()))
					errors.rejectValue("tituloDocumento", "error.tipoOperacion.noIngresoTituloDocumento");
//			if(tipo.getSerieDocumento() == null)
//				errors.rejectValue("serieDocumentoCod", "error.tipoOperacion.noSeleccionSerieDocumento");
		}		
	}
}
