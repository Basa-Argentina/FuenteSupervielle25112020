package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class PlantillaFacturacionBusquedaValidator implements Validator {	
	private PlantillaFacturacionService plantillaFacturacionService;
	private ClienteEmpService clienteEmpService;
	private SerieService serieService;
	private AfipTipoComprobanteService afipTipoComprobanteService;
	private ListaPreciosService listaPreciosService;
	
	@Autowired
	public void setPlantillaFacturacionService(PlantillaFacturacionService plantillaFacturacionService) {
		this.plantillaFacturacionService = plantillaFacturacionService;
	}
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	@Autowired
	public void setAfipTipoComprobanteService(AfipTipoComprobanteService afipTipoComprobanteService) {
		this.afipTipoComprobanteService = afipTipoComprobanteService;
	}
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return PlantillaFacturacion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		
	}
	
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
				
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}