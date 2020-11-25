package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;
import static com.security.recursos.Configuracion.formatoFechaHoraFormularios;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteFacturacionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.Transporte;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class LoteFacturacionValidator implements Validator {	
	private LoteFacturacionService loteFacturacionService;
	
	@Autowired
	public void setLoteFacturacionService(LoteFacturacionService loteFacturacionService) {
		this.loteFacturacionService = loteFacturacionService;
	}
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Remito.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaRegistro",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaFacturacion",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.setRequiredFields(new String[] {"fechaRegistro",
				"fechaFacturacion","periodo"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {

		LoteFacturacion loteFacturacion = (LoteFacturacion) command;
		loteFacturacion.setClienteAsp(obtenerClienteAspUser());
		
		if(loteFacturacion.getAccion().equals("NUEVO")){
			
			//PREGUNTAR SI VA A SER AUTOINCREMENTAL Y SIN PODER MODIFICARSE
//			LoteFacturacion exists = loteFacturacionService.verificarExistente(loteFacturacion);
//			if(exists != null){
//				errors.rejectValue("numero", "formularioRemito.errorClavePrimaria");
//			}
			
			Long resultado = loteFacturacionService.verificarExistentePeriodoPosterior(loteFacturacion, obtenerClienteAspUser());
			if(resultado > 0)
			{
				errors.rejectValue("periodo", "formularioLoteFacturacion.errorPeriodo");
			}
			
			if(loteFacturacion.getFechaRegistro().after(new Date()))
			{
				errors.rejectValue("fechaRegistro", "formularioLoteFacturacion.errorFechaRegistro");
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			SimpleDateFormat sd = new SimpleDateFormat("MM");
			String añoFacturacion = sdf.format(loteFacturacion.getFechaFacturacion());
			String añoActual = sdf.format(new Date());
			String mesFacturacion = sd.format(loteFacturacion.getFechaFacturacion());
			if(añoFacturacion.compareTo(añoActual) <= 0){
				if(mesFacturacion.startsWith("0"))
					mesFacturacion = mesFacturacion.substring(1);
				if(mesFacturacion.compareTo(loteFacturacion.getPeriodo()) < 0)
				{
					errors.rejectValue("fechaFacturacion", "formularioLoteFacturacion.errorFechaFacturacion");
				}
			}
			
		}
		
		if(loteFacturacion.getDetalles()== null || loteFacturacion.getDetalles().size()<=0)
		{
			errors.rejectValue("detalles", "formularioLoteFacturacion.errorDetalles");
		}
						
			
	}
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Empresa obtenerEmpresaUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}