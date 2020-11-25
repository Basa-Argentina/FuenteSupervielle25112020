package com.dividato.configuracionGeneral.validadores;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;


import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.modelo.administracion.ClienteAsp;

import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (15/08/2011)
 *
 */
@Component
public class SerieValidator implements Validator {	
	private SerieService serieService;
	private EmpresaService empresaService;
	
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Serie.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {
				"codigo","descripcion","tipoSerie","idAfipTipoComprobante",
				"idEmpresa","idSucursal","prefijo"
		});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Serie serie = (Serie)command;
		serie.setCliente(obtenerClienteAspUser());
		
		if(serie.getAccion().equals("NUEVO")){
			Serie series = serieService.verificarSerie(serie);
			if(series != null)
				errors.rejectValue("codigo", "formularioSerie.errorCodigo");			
			
		}
		else
		{
		Serie serieAux = serieService.obtenerPorId(serie.getId());
		Empresa empresa = empresaService.obtenerPorId(serie.getEmpresa().getId());
			if(empresa.getSerie1() != null || empresa.getSerie2() != null)
			{
				if(empresa.getSerie1().getId().equals(serie.getId()) || empresa.getSerie2().getId().equals(serie.getId()))
				{
					if(!serie.getTipoSerie().equals(serieAux.getTipoSerie()))
					{
						errors.rejectValue("tipoSerie", "formularioSerie.errorModificarSerie1PorDefecto");
					}
					if(!serie.getIdAfipTipoComprobante().equals(serieAux.getAfipTipoComprobante().getId()))
					{
						errors.rejectValue("idAfipTipoComprobante", "formularioSerie.errorModificarSerie1PorDefecto");
					}
					if(!serie.getCondIvaClientes().equals(serieAux.getCondIvaClientes()))
					{
						errors.rejectValue("condIvaClientes", "formularioSerie.errorModificarSerie1PorDefecto");
					}
				}
			}
		}
		
		try{
			Integer.valueOf(serie.getPrefijo());
		}catch (NumberFormatException e) {
			errors.rejectValue("prefijo", "formularioSerie.errorFormat");
			serie.setPrefijo("0");
		}
		try{
			Integer.valueOf(serie.getUltNroImpreso());
		}catch (NumberFormatException e) {
			errors.rejectValue("ultNroImpreso", "formularioSerie.errorFormat");
			serie.setUltNroImpreso("0");
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}