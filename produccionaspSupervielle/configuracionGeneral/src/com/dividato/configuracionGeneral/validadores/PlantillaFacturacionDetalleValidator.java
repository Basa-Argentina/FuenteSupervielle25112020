package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionDetalleService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class PlantillaFacturacionDetalleValidator implements Validator {	
	private PlantillaFacturacionDetalleService plantillaFacturacionDetalleService;
	
	@Autowired
	public void setPlantillaFacturacionDetalleService(PlantillaFacturacionDetalleService plantillaFacturacionDetalleService) {
		this.plantillaFacturacionDetalleService = plantillaFacturacionDetalleService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return PlantillaFacturacionDetalle.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"descripcionConcepto","orden","cantidadSinCosto"});
	}
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		PlantillaFacturacionDetalle detalle = (PlantillaFacturacionDetalle) command;

		
		if(detalle.getCantidadSinCosto().longValue()<0){
			errors.rejectValue("cantidadSinCosto","formularioPlantillaFacturacionDetalle.error.cantidadNegativa");
		}
		
		if(!detalle.getAccionDetalle().equalsIgnoreCase("MODIFICACION")){
			if(detalle.getConceptoFacturable()!=null){
				if(detalle.getDetalles()!=null && detalle.getDetalles().size()>0){
					for(PlantillaFacturacionDetalle detalleComparar:detalle.getDetalles()){
						if(detalleComparar.getConceptoFacturable()!=null 
								&& detalleComparar.getConceptoFacturable().getId().longValue() 
								== detalle.getConceptoFacturable().getId().longValue())
						{
							errors.rejectValue("conceptoFacturable","formularioPlantillaFacturacionDetalle.error.conceptoRepetido");
							break;
						}
					}
					
				}
			}
		}
		
		
		if(detalle.getOrden().longValue()<1){
			errors.rejectValue("orden","formularioPlantillaFacturacionDetalle.error.ordenInvalido");
		}else{
			if(detalle.getAccionDetalle() == null || detalle.getAccionDetalle().equals("") || detalle.getAccionDetalle().equals("NUEVO")){
				if(detalle.getDetalles()!=null && detalle.getDetalles().size()>0){
					for(PlantillaFacturacionDetalle detalleComparar:detalle.getDetalles()){
						if(detalle.getOrden().longValue() == detalleComparar.getOrden().longValue()){
							errors.rejectValue("orden","formularioPlantillaFacturacionDetalle.error.ordenRepetido");
							break;
						}
					}
				}
			}
			else{
				if(detalle.getOrdenAnterior().longValue() != detalle.getOrden().longValue()){
					if(detalle.getDetalles()!=null && detalle.getDetalles().size()>0){
						for(PlantillaFacturacionDetalle detalleComparar:detalle.getDetalles()){
							if(detalle.getOrden().longValue() == detalleComparar.getOrden().longValue()){
								errors.rejectValue("orden","formularioPlantillaFacturacionDetalle.error.ordenRepetido");
								break;
							}
						}
					}
				}
			}
		}
		

		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}