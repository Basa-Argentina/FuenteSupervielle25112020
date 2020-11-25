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
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class PlantillaFacturacionValidator implements Validator {	
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
		binder.setRequiredFields(new String[] {"clienteCodigo","codigoSerie","tipoComprobanteId","listaPreciosCodigo"});
	}
	
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		PlantillaFacturacion plantilla = (PlantillaFacturacion) command;
		
		//Validar y setear estado
		if(plantilla.getHabilitado()==null)
			plantilla.setHabilitado(false);
		
		//Validar y Setear Cliente
		ClienteEmp cliente = new ClienteEmp();
		cliente.setCodigo(plantilla.getClienteCodigo());
		cliente = clienteEmpService.getByCodigo(cliente, obtenerClienteAspUser());
		if(cliente != null)
		{
			plantilla.setClienteEmp(cliente);
		}else
		{
			errors.rejectValue("codigoCliente","formularioPlantillaFacturacion.error.codigoClienteInvalido");
		}
		
		//Validar y Setear Serie
		Serie serie = new Serie();
		serie = serieService.obtenerPorCodigo(plantilla.getCodigoSerie(), obtenerClienteAspUser());
		if(serie != null)
		{
			plantilla.setSerie(serie);
		}else
		{
			errors.rejectValue("codigoSerie","formularioPlantillaFacturacion.error.codigoSerieInvalido");
		}
		
		//Validar y Setear Tipo de Comprobante
		AfipTipoComprobante afipTipoComprobante = new AfipTipoComprobante();
		afipTipoComprobante = afipTipoComprobanteService.obtenerPorId(plantilla.getTipoComprobanteId());
		if(afipTipoComprobante!= null){
			plantilla.setAfipTipoComprobante(afipTipoComprobante);}
		else{
			errors.rejectValue("tipoComprobanteId", "formularioPlantillaFacturacion.error.codigoTipoComprobanteInvalido");}
		
		
		//Validar y Setear Tipo de Comprobante
		ListaPrecios listaPrecios = new ListaPrecios();
		listaPrecios = listaPreciosService.obtenerListaPreciosPorCodigo(plantilla.getListaPreciosCodigo(), obtenerClienteAspUser(), true);
		if(listaPrecios!= null){
			plantilla.setListaPrecios(listaPrecios);}
		else{
			errors.rejectValue("listaPreciosCodigo", "formularioPlantillaFacturacion.error.codigoListaPreciosInvalido");}
		
		//Validar Detalles
		if(plantilla.getDetalles()== null || plantilla.getDetalles().size()<=0)
			errors.rejectValue("detalles", "formularioPlantillaFacturacion.error.detallesVacios");
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}