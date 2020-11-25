
package com.dividato.configuracionGeneral.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;

/**
 * 
 * @author FedeMz
 *
 */
@Component
public class ClasificacionDocumentalValidator implements Validator{

	private ClasificacionDocumentalService clasificacionDocumentalService;
	
	@Autowired
	public void setService(ClasificacionDocumentalService service){
		this.clasificacionDocumentalService=service;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean supports(Class type) {
		return ClasificacionDocumental.class.isAssignableFrom(type);
	}

	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"nombre","codigo"});
	}

	@Override
	public void validate(Object command, Errors errors) {
		ClasificacionDocumental clasificacion = (ClasificacionDocumental) command;

		//el código es único por cliente
		List<ClasificacionDocumental> clasificacionesDocumentales = 
			clasificacionDocumentalService.getByClienteCodigo(clasificacion.getClienteEmp(),clasificacion.getCodigo());
		if(clasificacionesDocumentales.size()>=1){
			Long id = clasificacion.getId();
			if(id==null || !clasificacionesDocumentales.get(0).getId().equals(id)){
				errors.rejectValue("codigo", "codigoRepetido");
			}
		}
		
		//si lee codigo de barra, controlamos que se hayan ingresado los parametros desde y hasta
		if(clasificacion.getLeeCodigoBarra()!=null && clasificacion.getLeeCodigoBarra()==true){
			if(clasificacion.getCodigoBarraDesde()==null)
				errors.rejectValue("codigoBarraDesde", "codigoBarraDesde.requerido");
			if(clasificacion.getCodigoBarraHasta()==null)
				errors.rejectValue("codigoBarraHasta", "codigoBarraHasta.requerido");
		}
			
		
		//si es índice, controlamos que los datos hayan sido cargados correctamente
		if(clasificacion.getNodo().equalsIgnoreCase("I")){
			if(!clasificacion.getIndiceIndividual() && !clasificacion.getIndiceGrupal()){
				errors.reject("seleccioneIndice");
			}
			if(clasificacion.getIndiceIndividual()){
				//Validar que tenga al menos 1 campo habilitado para cargar (para individual y para grupal).
				int sel = clasificacion.getIndividualFecha1Seleccionado()?1:0;
				sel += clasificacion.getIndividualFecha2Seleccionado()?1:0;
				sel += clasificacion.getIndividualNumero1Seleccionado()?1:0;
				sel += clasificacion.getIndividualNumero2Seleccionado()?1:0;
				sel += clasificacion.getIndividualTexto1Seleccionado()?1:0;
				sel += clasificacion.getIndividualTexto2Seleccionado()?1:0;
				if(sel==0){
					errors.rejectValue("indiceIndividual","minimoIndices");
				}
				
				if(clasificacion.getIndividualNumero1Seleccionado()){
					if(clasificacion.getIndividualNumero1Titulo()==null || clasificacion.getIndividualNumero1Titulo().equals("")){
						errors.rejectValue("indiceIndividual", "numero1Titulo.requerido");
					}
				}
				if(clasificacion.getIndividualNumero2Seleccionado()){
					if(clasificacion.getIndividualNumero2Titulo()==null || clasificacion.getIndividualNumero2Titulo().equals("")){
						errors.rejectValue("indiceIndividual", "numero2Titulo.requerido");
					}
				}
				if(clasificacion.getIndividualFecha1Seleccionado()){
					if(clasificacion.getIndividualFecha1Titulo()==null || clasificacion.getIndividualFecha1Titulo().equals("")){
						errors.rejectValue("indiceIndividual", "fecha1Titulo.requerido");
					}
				}
				if(clasificacion.getIndividualFecha2Seleccionado()){
					if(clasificacion.getIndividualFecha2Titulo()==null || clasificacion.getIndividualFecha2Titulo().equals("")){
						errors.rejectValue("indiceIndividual", "fecha2Titulo.requerido");
					}
				}
				if(clasificacion.getIndividualTexto1Seleccionado()){
					if(clasificacion.getIndividualTexto1Titulo()==null || clasificacion.getIndividualTexto1Titulo().equals("")){
						errors.rejectValue("indiceIndividual", "texto1Titulo.requerido");
					}
				}
				if(clasificacion.getIndividualTexto2Seleccionado()){
					if(clasificacion.getIndividualTexto2Titulo()==null || clasificacion.getIndividualTexto2Titulo().equals("")){
						errors.rejectValue("indiceIndividual", "texto2Titulo.requerido");
					}
				}
			}
			if(clasificacion.getIndiceGrupal()){
				int sel = clasificacion.getGrupalFechaSeleccionado()?1:0;
				sel += clasificacion.getGrupalNumeroSeleccionado()?1:0;
				sel += clasificacion.getGrupalTextoSeleccionado()?1:0;
				if(sel==0){
					errors.rejectValue("indiceGrupal","minimoIndices");
				}
				
				if(clasificacion.getGrupalNumeroSeleccionado()){
					if(clasificacion.getGrupalNumero1Titulo()==null || clasificacion.getGrupalNumero1Titulo().equals("")){
						errors.rejectValue("indiceGrupal", "numero1Titulo.requerido");
					}
					if(clasificacion.getGrupalNumero2Titulo()==null || clasificacion.getGrupalNumero2Titulo().equals("")){
						errors.rejectValue("indiceGrupal", "numero2Titulo.requerido");
					}
				}
				if(clasificacion.getGrupalFechaSeleccionado()){
					if(clasificacion.getGrupalFecha1Titulo()==null || clasificacion.getGrupalFecha1Titulo().equals("")){
						errors.rejectValue("indiceGrupal", "fecha1Titulo.requerido");
					}
					if(clasificacion.getGrupalFecha2Titulo()==null || clasificacion.getGrupalFecha2Titulo().equals("")){
						errors.rejectValue("indiceGrupal", "fecha2Titulo.requerido");
					}
				}
				if(clasificacion.getGrupalTextoSeleccionado()){
					if(clasificacion.getGrupalTexto1Titulo()==null || clasificacion.getGrupalTexto1Titulo().equals("")){
						errors.rejectValue("indiceGrupal", "texto1Titulo.requerido");
					}
					if(clasificacion.getGrupalTexto2Titulo()==null || clasificacion.getGrupalTexto2Titulo().equals("")){
						errors.rejectValue("indiceGrupal", "texto2Titulo.requerido");
					}
				}
			}
			if(clasificacion.getDescripcionSeleccionado()){
				if(clasificacion.getDescripcionTitulo()==null || clasificacion.getDescripcionTitulo().equals("")){
					errors.rejectValue("descripcionTitulo", "requerido");
				}
			}
		}
	}

}
