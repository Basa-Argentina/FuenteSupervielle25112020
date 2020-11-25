package com.dividato.configuracionGeneral.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.TipoElemento;
/**
 * 
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (25/08/2011)
 *
 */
@Component
public class TipoElementoValidator implements Validator {
	
	private TipoElementoService tipoElementoService;
	private ElementoService elementoService;
	
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return TipoElemento.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo","descripcion","prefijoCodigo"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		TipoElemento tipoElemento = (TipoElemento) command;
		if(tipoElemento.getAccion().equals("NUEVO"))
		{	
			
			TipoElemento exists= tipoElementoService.verificarExistente(tipoElemento);
			if(exists != null)
				errors.rejectValue("codigo", "formularioTipoElemento.errorCodigo");					
			
		}
		
		TipoElemento exists= tipoElementoService.verificarExistente(tipoElemento);
		if(exists!=null){
			if(!exists.getPrefijoCodigo().equals(tipoElemento.getPrefijoCodigo()))
			{
				List<Elemento> lista = elementoService.listarElementosRelacionados(tipoElemento);
				if(lista.size() >= 1)
				{
					errors.rejectValue("prefijoCodigo", "formularioTipoElemento.errorPrefijoCodigoRelacionado");
				}
				TipoElemento aux= tipoElementoService.verificarExistentePrefijoCodigo(tipoElemento);
				if(aux != null)
					errors.rejectValue("prefijoCodigo", "formularioTipoElemento.errorPrefijoCodigo");
			}
		}		
	}
}