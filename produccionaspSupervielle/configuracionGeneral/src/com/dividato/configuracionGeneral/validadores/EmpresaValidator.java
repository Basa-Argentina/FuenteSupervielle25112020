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

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.general.TipoDocumento;
import com.security.modelo.seguridad.User;
import com.security.recursos.ValidacionEMail;
import com.security.utils.CuitUtils;
/**
 * 
 * @author Gonzalo Noriega
 * @modificado Víctor Kenis
 *
 */
@Component
public class EmpresaValidator implements Validator {	
	private EmpresaService empresaService;
	private TipoDocumentoService tipoDocumentoService;
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Empresa.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaInicioActividad",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.setRequiredFields(new String[] {
				"codigo","descripcion", "razonSocial.razonSocial","numeroDoc","direccion.numero",
				"direccion.calle","idTipoDocSel"
		});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Empresa empresa = (Empresa)command;
		empresa.setCliente(obtenerClienteAspUser());
		Empresa principal = empresaService.getPrincipal(obtenerClienteAspUser());
		if(empresa.getAccion().equals("NUEVO")){
			
			Empresa empresas = empresaService.verificarExistente(empresa);
			if(empresas != null)
			{	
				errors.rejectValue("codigo", "formularioEmpresa.errorCodigo");
				return;
			}
			if(empresa.getPrincipal()== true && principal != null)
			{
				errors.rejectValue("principal", "formularioEmpresa.errorPrincipal");
				return;
			}
		}
	
		if(empresa.getPrincipal()== true && principal != null)
		{
			if(!empresa.getId().equals(principal.getId()))
			{
			errors.rejectValue("principal", "formularioEmpresa.errorPrincipal");
			return;
			}
		}
		if(empresa.getMail()!= null && !"".equals(empresa.getMail()) && !ValidacionEMail.validar(empresa.getMail())){
			errors.rejectValue("mail", "formularioEmpresa.errorFormatoMail");
			return;
		}
		
		if(empresa.getIdTipoDocSel() != null){
			TipoDocumento tipoDoc = tipoDocumentoService.obtenerPorId(empresa.getIdTipoDocSel());			
			
			if(tipoDoc != null && ("CUIT".equals(tipoDoc.getCodigo()) || "CUIL".equals(tipoDoc.getCodigo()))
					&& (empresa.getNumeroDoc() != null && !CuitUtils.validar(empresa.getNumeroDoc()))){
				errors.rejectValue("numeroDoc", "formularioEmpresa.errorFormato"+tipoDoc.getCodigo());
				return;
			}
		}
		if(empresa.getIdBarrio() == null || empresa.getIdBarrio() == 0){
			errors.rejectValue("idBarrio", "required");
			return;
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}