package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;
import static com.security.recursos.Configuracion.formatoFechaHoraFormularios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.SetUtils;
import org.hibernate.mapping.Array;
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
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Movimiento;
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
public class RemitoValidator implements Validator {	
	private RemitoService remitoService;
	private TransporteService transporteService;
	private ClienteEmpService clienteEmpService;
	private EmpleadoService empleadoService;
	private ClienteDireccionService clienteDireccionService;
	private SerieService serieService;
	private DepositoService depositoService;
	private MovimientoService movimientoService;
		
	
	@Autowired
	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}
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
		binder.setRequiredFields(new String[] {"numeroSinPrefijo", "codigoTransporte","codigoSerie",
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
		Remito remitoBase = null;
		
		if(remito.getAccion().equals("MODIFICACION")){
			remitoBase = remitoService.obtenerPorId(remito.getId());
		}
		
		if(remito.getAccion().equals("NUEVO") || (remito.getAccion().equals("MODIFICACION") && remito.getNumero().longValue()!=remitoBase.getNumero().longValue()))
		{
			Remito exists = remitoService.verificarExistenteEnSerie(remito.getNumero(), remito.getCodigoSerie(), obtenerClienteAspUser());
			if(exists != null){
				errors.rejectValue("numero", "formularioRemito.errorClavePrimaria");
				return;
			}
		}
		
		if(remito.getAccion().equals("MODIFICACION")){
			Set<RemitoDetalle> detalles = remito.getDetalles();;
			
			if(remitoBase!=null && remitoBase.getVerificaLectura()!=null && remitoBase.getVerificaLectura())
			{
				//detalles.removeAll(remito.getDetallesViejos());
				
				Iterator it = remito.getDetalles().iterator();
				while(it.hasNext()){
					if(remito.getDetallesViejos()!=null && remito.getDetallesViejos().size()>0)
					{
						RemitoDetalle viejo = (RemitoDetalle) it.next();
						for(RemitoDetalle det:remito.getDetallesViejos())
						{
							if(det.getElemento().getId().longValue()==viejo.getElemento().getId().longValue())
							{
								it.remove();
							}
						}
					}
				}
			}
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
			
			//VERIFICAMOS LOS DETALLES CONTRA LA TABLA MOVIMIENTOS
			if(remito.getVerificaLectura()!=null && remito.getVerificaLectura())
			{
				Movimiento movVerificar = new Movimiento();
				movVerificar.setClaseMovimiento(remito.getTipoRemito());
				movVerificar.setTipoMovimiento(remito.getIngresoEgreso());
				movVerificar.setTieneRemitoAsoc(false);
				
				
				if(remito.getIngresoEgreso().equalsIgnoreCase("EGRESO"))
				{
					if(remito.getCodigoDepositoOrigen() != null && !"".equals(remito.getCodigoDepositoOrigen())){
						Deposito deposito = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoOrigen(), null, obtenerClienteAspUser());
						if(deposito!=null)
							movVerificar.setDeposito(deposito);
					}
				}
				if(remito.getIngresoEgreso().equalsIgnoreCase("INGRESO"))
				{
					if(remito.getCodigoDepositoDestino() != null && !"".equals(remito.getCodigoDepositoDestino())){
						Deposito deposito = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoDestino(), null, obtenerClienteAspUser());
						if(deposito!=null)
							movVerificar.setDeposito(deposito);
					}
				}
				movVerificar.setMostrarAnulados(Boolean.FALSE);
				
				List movimientos = movimientoService.verificarMovimientosEnRemito(movVerificar, obtenerClienteAspUser());
				List<Movimiento> movimientosAsociados = new ArrayList<Movimiento>();
				
				
				if(movimientos==null || movimientos.size()==0)
				{
					errors.rejectValue("detalles","formularioRemito.errorDetalles.movimientosNoEncontrados");
				}
				else
				{
					boolean existe = false;
					String args = "";
					
					for(RemitoDetalle remitoDetalle:remito.getDetalles())
					{
						existe = false;
						for(Object obj:movimientos)
						{
							Object[] moviElem = (Object[]) obj;
							if( ( (BigDecimal)moviElem[1] ).longValue()==remitoDetalle.getElemento().getId().longValue())
							{
								existe=true;
								Movimiento movi = movimientoService.obtenerPorId( ((BigDecimal)moviElem[0]).longValue() );
								movimientosAsociados.add(movi);
								break;								
							}	
						}
						if(!existe)
							args+=remitoDetalle.getElemento().getCodigo()+"-";
					}
					if(args.length()>0)
						errors.rejectValue("detalles", "formularioRemito.errorDetalles.elementosRemitovNoEnMov", new Object[]{args}, "formularioRemito.errorDetalles.elementosRemitovNoEnMov");
					
				}
				
				remito.setMovAsociados(movimientosAsociados);
			}
			
			
			
			//Si el remito es de tipo Cliente
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
				
				//Si es de tipo cliente y de ingreso
				if("ingreso".equals(remito.getIngresoEgreso()))
				{
					//Validar y Setear Deposito Destino
					if(remito.getCodigoDepositoDestino() != null && !"".equals(remito.getCodigoDepositoDestino()))
					{
						Deposito depositoDestino = new Deposito();
						depositoDestino = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoDestino(), null, obtenerClienteAspUser());
						if(depositoDestino!=null)
						{
							remito.setDepositoDestino(depositoDestino);
						}
						else
						{errors.rejectValue("codigoDepositoDestino","formularioRemito.errorDepositoDestino");}
					}
					else
					{errors.rejectValue("codigoDepositoDestino","formularioRemito.errorCodigoDepositoDestino");}
					
					//Validar detalles
					if (remito.getDetalles() != null && remito.getDetalles().size() > 0) 
					{
						for (RemitoDetalle remitoDetalle : remito.getDetalles()) 
						{
							
							if(remitoDetalle.getElemento().getClienteEmp()!=null 
									&& remitoDetalle.getElemento().getClienteEmp().getId().longValue() != remito.getClienteEmp().getId().longValue())
							{
								errors.rejectValue("detalles","formularioRemito.errorDetalles.elementoDistintoCliente");
								break;
							}
							
							int vecesExiste = 0;
							
							//if(remitoDetalle.getElemento().getDepositoActual()!= null){

								if(!Constantes.ELEMENTO_ESTADO_EN_CONSULTA.equalsIgnoreCase(remitoDetalle.getElemento().getEstado()) 
										&& !Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE.equalsIgnoreCase(remitoDetalle.getElemento().getEstado()))
								{
									//errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosNOenGuarda");
									errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosNOenELClienteNiEnConsulta");
								}
								else
								{
									for (RemitoDetalle remitoDetalleAComparar : remito.getDetalles()) {
										if (remitoDetalle.getElemento().equals(remitoDetalleAComparar.getElemento())) {
											vecesExiste++;
										}
									}
									if (vecesExiste > 1) {
										errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosRepetidos");
										break;
									}
								}
							//}
							//else
							//{
								//errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosSinDepositoActual");
								//break;
							//}
						}	
					} else {
						if(!remito.getAccion().equals("MODIFICACION"))
							errors.rejectValue("detalles","formularioRemito.errorDetallesVacios");
					}
				}
				//Entonces es de tipo cliente y de egreso
				else
				{
					//Validar y setear Deposito Origen
					if(remito.getCodigoDepositoOrigen() != null && !"".equals(remito.getCodigoDepositoOrigen()))
					{
						depositoOrigen = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoOrigen(), remito.getSucursal().getCodigo(), obtenerClienteAspUser());
						if(depositoOrigen!=null)
						{
							remito.setDepositoOrigen(depositoOrigen);
						}
						else
						{errors.rejectValue("codigoDepositoOrigen","formularioRemito.errorDepositoOrigen");}
					}else
					{
						errors.rejectValue("codigoDepositoOrigen","formularioRemito.errorCodigoDepositoOrigen");
					}
					
					//Validar detalles
					if(remito.getDetalles() != null && remito.getDetalles().size()> 0)
					{
						for (RemitoDetalle remitoDetalle : remito.getDetalles()) {
							
							if(remitoDetalle.getElemento().getClienteEmp()!=null 
									&& remitoDetalle.getElemento().getClienteEmp().getId().longValue() != remito.getClienteEmp().getId().longValue()){
								errors.rejectValue("detalles","formularioRemito.errorDetalles.elementoDistintoCliente");
								break;
							}
							
							int vecesExiste = 0;
						
							if(Constantes.ELEMENTO_ESTADO_EN_GUARDA.equalsIgnoreCase(remitoDetalle.getElemento().getEstado()))
							{
								
								if(remitoDetalle.getElemento().getDepositoActual()!= null)
								{
									
									if(remitoDetalle.getElemento().getDepositoActual().getId().longValue() != remito.getDepositoOrigen().getId().longValue()){
										errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosDepositoOrigenDistinto");
										break;
									}
								}
								else
								{
									errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosSinDepositoActual");
									break;
								}
							}
							else if(!Constantes.ELEMENTO_ESTADO_CREADO.equals(remitoDetalle.getElemento().getEstado()))
							{
								errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosNOenGuardaNiCreado");
							}
							
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
								break;
							}
						}
					}
					else
					{
						if(!remito.getAccion().equals("MODIFICACION"))
							errors.rejectValue("detalles","formularioRemito.errorDetallesVacios");
					}
				}

				//Validaciones para los dos tipos de remito cliente
				
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
						if(direccion != null)
						{
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
			//Entonces el Remito es de tipo Interno
			else
			{
				//Validar y setear Deposito Origen
				if(remito.getCodigoDepositoOrigen() != null && !"".equals(remito.getCodigoDepositoOrigen())){
					depositoOrigen = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoOrigen(), remito.getSucursal().getCodigo(), obtenerClienteAspUser());
					if(depositoOrigen!=null)
					{
						remito.setDepositoOrigen(depositoOrigen);
					}
					else
					{errors.rejectValue("codigoDepositoOrigen","formularioRemito.errorDepositoOrigen");}
				}else
				{
					errors.rejectValue("codigoDepositoOrigen","formularioRemito.errorCodigoDepositoOrigen");
				}
				
				//Validar y Setear el Deposito Destino
				if(remito.getCodigoDepositoDestino() != null && !"".equals(remito.getCodigoDepositoDestino())){
					Deposito depositoDestino = new Deposito();
					//depositoDestino = depositoService.getByCodigoYSucursal(remito.getCodigoDepositoDestino(), remito.getSucursal().getCodigo(), obtenerClienteAspUser());
					depositoDestino = depositoService.getByCodigo(remito.getCodigoDepositoDestino(), obtenerClienteAspUser());
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
				
				//SI el remito es de Ingreso Interno
				if("ingreso".equalsIgnoreCase(remito.getIngresoEgreso())){
					
					//Validar detalles
					if(remito.getDetalles() != null && remito.getDetalles().size()> 0)
					{
						for (RemitoDetalle remitoDetalle : remito.getDetalles()) {
							int vecesExiste = 0;
							
							if(!Constantes.ELEMENTO_ESTADO_EN_TRANSITO.equals(remitoDetalle.getElemento().getEstado()))
							{
								errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosNOenTransito");
							}
							else
							{
								
								//Se buscan movimientos de tipo Cliente e Ingreso existentes para ese elemento
								Movimiento mov = new Movimiento();
								mov.setTipoMovimiento("INGRESO");
								mov.setClaseMovimiento("cliente");
								mov.setElemento(remitoDetalle.getElemento());
								Integer movIngCliAnterior = movimientoService.contarMovimientosFiltrados(mov, obtenerClienteAspUser());
								
								if(movIngCliAnterior!=null && movIngCliAnterior>0){
									
									if(remitoDetalle.getElemento().getDepositoActual().getId().longValue() 
											!= remito.getDepositoOrigen().getId().longValue())
									{
										errors.rejectValue("detalles","formularioMovimiento.errorDetalles.elementosDepositoOrigenDistinto");
										break;										
									}
									
								}
								
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
									break;
								}
							}
						}
					}
					else
					{
						if(!remito.getAccion().equals("MODIFICACION"))
							errors.rejectValue("detalles","formularioRemito.errorDetallesVacios");
					}
				}
				
				//Entonces el remito es de Egreso Interno
				else
				{
					//Validar detalles
					if(remito.getDetalles() != null && remito.getDetalles().size()> 0)
					{
						for (RemitoDetalle remitoDetalle : remito.getDetalles()) {
							int vecesExiste = 0;
							
							if(Constantes.ELEMENTO_ESTADO_EN_GUARDA.equalsIgnoreCase(remitoDetalle.getElemento().getEstado())){
								
								if(remitoDetalle.getElemento().getDepositoActual()!= null){
									if(remitoDetalle.getElemento().getDepositoActual().getId().longValue() 
											!= remito.getDepositoDestino().getId().longValue())
									{
										errors.rejectValue("detalles","formularioRemito.errorDetalles.elementoNoEnPlanta");
										break;
									}
								}
								else
								{
									errors.rejectValue("codigoDepositoOrigen","formularioRemito.errorDetalles.elementosSinDepositoActual");
									break;
								}
							}
							else if(!Constantes.ELEMENTO_ESTADO_CREADO.equals(remitoDetalle.getElemento().getEstado()))
							{
								errors.rejectValue("detalles","formularioRemito.errorDetalles.elementosNOenGuardaNiCreado");
							}
							
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
								break;
							}
						}
						
					}
					else
					{
						if(!remito.getAccion().equals("MODIFICACION"))
							errors.rejectValue("detalles","formularioRemito.errorDetallesVacios");
					}
				}
			}
			
			//Validaciones para todos los tipos
			
			
			
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