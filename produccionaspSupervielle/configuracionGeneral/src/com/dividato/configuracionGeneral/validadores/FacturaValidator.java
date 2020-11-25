package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;
import static com.security.recursos.Configuracion.formatoFechaHoraFormularios;

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
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
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
public class FacturaValidator implements Validator {	
	private RemitoService remitoService;
	private TransporteService transporteService;
	private ClienteEmpService clienteEmpService;
	private EmpleadoService empleadoService;
	private ClienteDireccionService clienteDireccionService;
	private SerieService serieService;
	private DepositoService depositoService;
	
	@Autowired
	public void setRemitoService(RemitoService remitoService) {
		this.remitoService = remitoService;
	}
	@Autowired
	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
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
		binder.registerCustomEditor(Date.class, "fechaEmision",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaEntrega",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.setRequiredFields(new String[] {"numeroSinPrefijo","codigoDepositoOrigen","codigoTransporte","codigoSerie",
				"fechaEmision","tipoRemito"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Remito remito = (Remito) command;
		Deposito depositoOrigen = new Deposito();
		remito.setClienteAsp(obtenerClienteAspUser());
		if(remito.getAccion().equals("NUEVO")){
			
			Remito exists = remitoService.verificarExistenteEnSerie(remito.getNumero(), remito.getCodigoSerie(), obtenerClienteAspUser());
			if(exists != null){
				errors.rejectValue("numero", "formularioRemito.errorClavePrimaria");
			}
		}
			if(remito.getCodigoDepositoOrigen() != null && !"".equals(remito.getCodigoDepositoOrigen())){
				depositoOrigen = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoOrigen(), remito.getSucursal().getCodigo(), obtenerClienteAspUser());
				if(depositoOrigen!=null)
				{
					remito.setDepositoOrigen(depositoOrigen);
				}
				else
				{errors.rejectValue("codigoDepositoOrigen","formularioRemito.errorDepositoOrigen");}
			}
			
			//Validar y Setear Transporte
			if (remito.getCodigoTransporte() != null && !"".equals(remito.getCodigoTransporte())) {
				Transporte transporte = new Transporte();
				transporte = transporteService.getByCodigo(Integer.valueOf(remito.getCodigoTransporte()),obtenerEmpresaUser());
				if (transporte != null) {
					remito.setTransporte(transporte);
				} else {
					errors.rejectValue("transporte","formularioRemito.errorTransporteInexistente");
				}
			}
			
			
			if("cliente".equals(remito.getTipoRemito())){
				
				//Validar y Setear Cliente
				if(remito.getCodigoCliente() != null && !"".equals(remito.getCodigoCliente())){
				ClienteEmp cliente = new ClienteEmp();
				cliente = clienteEmpService.getByCodigo(remito.getCodigoCliente(), remito.getCodigoEmpresa(),obtenerClienteAspUser());
					if(cliente != null)
					{
					remito.setClienteEmp(cliente);
					}else
					{
						errors.rejectValue("codigoCliente","formularioRemito.errorCliente");
					}
				}else
				{
					errors.rejectValue("codigoCliente","formularioRemito.errorCodigoCliente");
				}
				
				// Validar y Setear Personal
				if (remito.getCodigoPersonal() != null && !"".equals(remito.getCodigoPersonal())) {
					Empleado empleado = new Empleado();
					empleado = empleadoService.getByCodigo(remito.getCodigoPersonal(), remito.getCodigoCliente(),obtenerClienteAspUser());
						if (empleado != null) {
							remito.setEmpleado(empleado);
						} 
						else 
						{
							errors.rejectValue("codigoPersonal","formularioRemito.errorPersonal");
						}
				}
				else
				{
						errors.rejectValue("codigoPersonal","formularioRemito.errorCodigoPersonal");
				}
				
				
				//Validar y Setear Direccion
				if(remito.getCodigoDireccion() != null && !"".equals(remito.getCodigoDireccion())){
					ClienteDireccion direccion = new ClienteDireccion();
					direccion = clienteDireccionService.obtenerPorCodigo(remito.getCodigoDireccion(), remito.getCodigoCliente(), obtenerClienteAspUser());
						if(direccion != null){
							remito.setDireccion(direccion);
						}
						else
						{
							errors.rejectValue("codigoDireccion","formularioRemito.errorDireccion");
						}
					}
				else
				{
					errors.rejectValue("codigoDireccion","formularioRemito.errorCodigoDireccion");
				}
			}
			else
			{
				if(remito.getCodigoDepositoDestino() != null && !"".equals(remito.getCodigoDepositoDestino())){
					Deposito depositoDestino = new Deposito();
					depositoDestino = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoDestino(), remito.getSucursal().getCodigo(), obtenerClienteAspUser());
					if(depositoDestino!=null)
					{
						if(depositoDestino.getId().longValue() == depositoOrigen.getId().longValue())
						{errors.rejectValue("codigoDepositoDestino","formularioRemito.errorDepositosIguales");}
						else
						{remito.setDepositoDestino(depositoDestino);}
						
					}
					else
					{errors.rejectValue("codigoDepositoDestino","formularioRemito.errorDepositoDestino");}
				}
				else
				{errors.rejectValue("codigoDepositoDestino","formularioRemito.errorCodigoDepositoDestino");}
					
			}
			//Validar y Setear Serie
			if(remito.getCodigoSerie() != null && !"".equals(remito.getCodigoSerie())){
				Serie serie = new Serie();
				serie = serieService.obtenerPorCodigo(remito.getCodigoSerie(), "R", obtenerEmpresaUser().getCodigo(),obtenerClienteAspUser());
				if(serie != null){
					remito.setSerie(serie);
				}
				else
				{
					errors.rejectValue("codigoSerie","formularioRemito.errorSerie");
				}
			}
			
			//Validar detalles
			if(remito.getDetalles() != null && remito.getDetalles().size()> 0)
			{
				for (RemitoDetalle remitoDetalle : remito.getDetalles()) {
					int vecesExiste = 0;
					if(remitoDetalle.getElemento().getDepositoActual().getId().longValue() == depositoOrigen.getId().longValue()){
						if(!Constantes.ELEMENTO_ESTADO_EN_GUARDA.equals(remitoDetalle.getElemento().getEstado()) && remitoDetalle.getElemento().getDepositoActual() != remito.getDepositoOrigen())
						{
							errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosNOenGuarda");
						}
						else
						{
							for (RemitoDetalle remitoDetalleAComparar : remito.getDetalles())
							{
								if(remitoDetalle.getElemento().equals(remitoDetalleAComparar.getElemento()))
								{
									vecesExiste++;
								}
							}
							if(vecesExiste>1)
							{
								errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosRepetidos");
							}
						}
					}
					else
					{
						errors.rejectValue("detalles","formularioRemito.errorDetalles.elementoNoEnPlanta");
					}
					
				}
			}
			else
			{
				errors.rejectValue("detalles","formularioRemito.errorDetallesVacios");
			}
			
//			//Validar existencia de numero en serie
//			Remito existe = remitoService.verificarExistenteEnSerie(remito.getNumero(), remito.getCodigoSerie(), obtenerClienteAspUser());
//			if(existe != null)
//			{
//				errors.rejectValue("numero", "formularioRemito.errorNumeroExistenteEnSerie");
//			}
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