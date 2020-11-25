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

import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.StockService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class StockValidator implements Validator {	
	private StockService stockService;
	private ConceptoFacturableService conceptoService;
	
	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}
	@Autowired
	public void setConceptoService(ConceptoFacturableService conceptoService) {
		this.conceptoService = conceptoService;
	}


	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Stock.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fecha", new CustomDateEditor(formatoFechaFormularios,true));
		binder.setRequiredFields(new String[] {"codigoConcepto", "nota", "cantidad",
				"hora", "fecha", "tipoMovimiento"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Stock stock = (Stock) command;
		if(stock.getAccion().equals("NUEVO")){
			Stock exists = stockService.verificarExistente(stock, obtenerClienteAspUser());
			ConceptoFacturable concepto = conceptoService.obtenerConceptoFacturablePorCodigo(stock.getCodigoConcepto(), obtenerClienteAspUser());
			if(exists != null)
				errors.rejectValue("fecha", "formularioStock.errorClavePrimaria");
			
			if(concepto == null){
				errors.rejectValue("concepto", "formularioStock.errorNoExisteConcepto");
			}
			
			if(concepto!=null && !concepto.getGeneraStock()){
				errors.rejectValue("concepto", "formularioStock.errorNoStockConcepto");
			}
		}
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}