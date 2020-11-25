package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.recursos.ValidacionEMail;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class SucursalValidator implements Validator {	
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
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
		return Sucursal.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
//		binder.setRequiredFields(new String[] {});
		binder.setRequiredFields(new String[] {
				"codigo","descripcion","direccion.numero",
				"direccion.calle","idEmpresa"
		});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Sucursal sucursal = (Sucursal)command;
		if(sucursal.getAccion().equals("NUEVO")){
			Empresa empresa = null;
			if(sucursal.getIdEmpresa() != null){
				empresa = empresaService.obtenerPorId(sucursal.getIdEmpresa());
			}else{
				errors.rejectValue("idEmpresa", "formularioSucursal.errorEmpresa");
			}
			Sucursal sucursals = null;
			if(empresa != null && empresa.getId() != null){
				sucursal.setEmpresa(empresa);
				sucursals = sucursalService.verificarSucursal(sucursal);
			}else{
				errors.rejectValue("idEmpresa", "formularioSucursal.errorEmpresa");
			}
			
			if(sucursals != null)
				errors.rejectValue("codigo", "formularioSucursal.errorCodigo");
		}

		if(sucursal.getMail() != null && !"".equals(sucursal.getMail()) && !ValidacionEMail.validar(sucursal.getMail())){
			errors.rejectValue("mail", "formularioSucursal.errorFormatoMail");
			return;
		}
		
		if(sucursal.getIdBarrio() == null || sucursal.getIdBarrio() == 0){	
			errors.rejectValue("idBarrio", "required");
		}
	}		
}