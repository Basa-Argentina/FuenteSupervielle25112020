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

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class MovimientoValidator implements Validator {	
	private MovimientoService movimientoService;
	private TransporteService transporteService;
	private ClienteEmpService clienteEmpService;
	private EmpleadoService empleadoService;
	private ClienteDireccionService clienteDireccionService;
	private SerieService serieService;
	private DepositoService depositoService;
	
	@Autowired
	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
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
		return Movimiento.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fecha",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.setRequiredFields(new String[] {"fecha","claseMovimiento","codigoDepositoActual"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Movimiento movimiento = (Movimiento) command;
		
		Deposito depositoActual = new Deposito();
		movimiento.setClienteAsp(obtenerClienteAspUser());
		
		//Validar y setear Deposito Actual
		if(movimiento.getCodigoDepositoActual() != null && !"".equals(movimiento.getCodigoDepositoActual()))
		{
			depositoActual = depositoService.getByCodigoYSucursal(movimiento.getCodigoDepositoActual(), movimiento.getCodigoSucursalActual(), obtenerClienteAspUser());
			if(depositoActual!=null)
			{
				movimiento.setDeposito(depositoActual);
			}
			else
			{errors.rejectValue("codigoDepositoOrigen","formularioMovimiento.errorDepositoOrigen");}
		}
		else
		{
			errors.rejectValue("codigoDepositoOrigen","formularioMovimiento.errorCodigoDepositoOrigen");
		}			
			
			
			//Si el movimiento es de tipo Cliente
			if("cliente".equals(movimiento.getClaseMovimiento()))
			{

				//Validar y Setear Cliente
				if(movimiento.getCodigoClienteEmp() != null && !"".equals(movimiento.getCodigoClienteEmp()))
				{
					ClienteEmp cliente = new ClienteEmp();
					cliente = clienteEmpService.getByCodigo(movimiento.getCodigoClienteEmp(), movimiento.getCodigoEmpresa(),obtenerClienteAspUser());
					if(cliente != null)
					{
						movimiento.setClienteEmpOrigenDestino(cliente);
					}else
					{
						errors.rejectValue("codigoCliente","formularioMovimiento.errorCliente");
					}
				}
//				else
//				{
//					errors.rejectValue("codigoCliente","formularioMovimiento.errorCodigoCliente");
//				}
				
				//Si es de tipo cliente y de ingreso
				if("INGRESO".equals(movimiento.getTipoMovimiento()))
				{
									
					//Validar detalles
					if(movimiento.getListaElementos() != null && movimiento.getListaElementos().size()> 0)
					{
						for (Elemento elemento : movimiento.getListaElementos()) {
							
							if(elemento.getClienteEmp()!=null && movimiento.getClienteEmpOrigenDestino()!=null
									&& elemento.getClienteEmp().getId().longValue() != movimiento.getClienteEmpOrigenDestino().getId().longValue()){
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementoDistintoCliente");
								break;
							}
							
							int vecesExiste = 0;
							
							if(Constantes.ELEMENTO_ESTADO_EN_CONSULTA.equalsIgnoreCase(elemento.getEstado()))
							{	
								//if(elemento.getDepositoActual()!= null){
									
//									if(elemento.getDepositoActual().getId().longValue() != movimiento.getDeposito().getId().longValue()){
//										errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosDepositoActualDistinto");
//										break;
//									}
//								}
//								else
//								{
//									errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosSinDepositoActual");
//									break;
//								}
							}
							else if(!Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE.equals(elemento.getEstado()))
							{
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosNOenConsultaNiEnElCliente");
								break;
							}
							
							for (Elemento elementoAComparar : movimiento.getListaElementos())
							{
								if(elemento.equals(elementoAComparar))
								{
									vecesExiste++;
								}
							}
							if(vecesExiste>1)
							{
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosRepetidos");
								break;
							}
						}
					}
					else
					{
						errors.rejectValue("listaElementos","formularioMovimiento.errorDetallesVacios");
					}
				}
				//Entonces es de tipo cliente y de egreso
				else
				{
					//Validar detalles
					if(movimiento.getListaElementos() != null && movimiento.getListaElementos().size()> 0)
					{
						for (Elemento elemento : movimiento.getListaElementos()) 
						{
							if(elemento.getClienteEmp()!=null && movimiento.getClienteEmpOrigenDestino()!=null 
									&& elemento.getClienteEmp().getId().longValue() != movimiento.getClienteEmpOrigenDestino().getId().longValue()){
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementoDistintoCliente");
								break;
							}
							
							int vecesExiste = 0;
							if(Constantes.ELEMENTO_ESTADO_EN_GUARDA.equalsIgnoreCase(elemento.getEstado()))
							{		
								if(elemento.getDepositoActual()!= null)
								{
									if(elemento.getDepositoActual().getId().longValue() != movimiento.getDeposito().getId().longValue()){
										errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementoNoEnPlanta");
										break;
									}
								}
//								else
//								{
//									errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosSinDepositoActual");
//									break;
//								}
							}
							else if(!Constantes.ELEMENTO_ESTADO_CREADO.equals(elemento.getEstado()))
							{
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosNOenGuarda");
								break;
							}
							
							for (Elemento elementoAComparar : movimiento.getListaElementos())
							{
								if(elemento.equals(elementoAComparar))
								{
									vecesExiste++;
								}
							}
							if(vecesExiste>1)
							{
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosRepetidos");
								break;
							}
						}
					}
					else
					{
						errors.rejectValue("listaElementos","formularioMovimiento.errorDetallesVacios");
					}
				}

			}
			//Entonces el Movimiento es de tipo Interno
			else
			{
				//Validar y Setear el Deposito Origen o Destino
				if(movimiento.getCodigoDepositoOrigenDestino()!= null && !"".equals(movimiento.getCodigoDepositoOrigenDestino()))
				{
					Deposito depositoOrigenDestino = new Deposito();
					depositoOrigenDestino = depositoService.getByCodigoYSucursal(movimiento.getCodigoDepositoOrigenDestino(), movimiento.getCodigoSucursalOrigenDestino(), obtenerClienteAspUser());
					if(depositoOrigenDestino!=null)
					{
						if(depositoOrigenDestino.getId().longValue() == depositoActual.getId().longValue())
						{errors.rejectValue("codigoDepositoOrigenDestino","formularioMovimiento.errorDepositosIguales");}
						else
						{
							movimiento.setDepositoOrigenDestino(depositoOrigenDestino);
						}
						
					}
					else
					{errors.rejectValue("codigoDepositoOrigenDestino","formularioMovimiento.errorDepositoDestino");}
				}
				else
				{errors.rejectValue("codigoDepositoOrigenDestino","formularioMovimiento.errorCodigoDepositoDestino");}
								
				//Si es de tipo interno y de egreso
				if("EGRESO".equalsIgnoreCase(movimiento.getTipoMovimiento()))
				{
											
					//Validar detalles
					if(movimiento.getListaElementos() != null && movimiento.getListaElementos().size()> 0)
					{
						for (Elemento elemento : movimiento.getListaElementos()) {
							int vecesExiste = 0;
							if(Constantes.ELEMENTO_ESTADO_EN_GUARDA.equalsIgnoreCase(elemento.getEstado()))
							{
								if(elemento.getDepositoActual()!= null)
								{
									if(elemento.getDepositoActual().getId().longValue() != movimiento.getDeposito().getId().longValue()){
										errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementoNoEnPlanta");
										break;
									}
								}
//								else
//								{
//									errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosSinDepositoActual");
//									break;
//								}
							}
							else if(!Constantes.ELEMENTO_ESTADO_CREADO.equals(elemento.getEstado()))
							{
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosNOenGuarda");
								break;
							}
							
							for (Elemento elementoAComparar : movimiento.getListaElementos())
							{
								if(elemento.equals(elementoAComparar))
								{
									vecesExiste++;
								}
							}
							if(vecesExiste>1)
							{
								errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosRepetidos");
								break;
							}
							
						}
					}
					else
					{
						errors.rejectValue("listaElementos","formularioMovimiento.errorDetallesVacios");
					}
				}
				//Entonces el movimiento es interno y de ingreso
				else
				{
					//Validar detalles
					if(movimiento.getListaElementos() != null && movimiento.getListaElementos().size()> 0)
					{
						for (Elemento elemento : movimiento.getListaElementos()) {
							int vecesExiste = 0;
							
								if(!Constantes.ELEMENTO_ESTADO_EN_TRANSITO.equalsIgnoreCase(elemento.getEstado()))
								{
									errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosNOenTransito");
									break;
								}
								else
								{
									//Se buscan movimientos de tipo Cliente e Ingreso existentes para ese elemento
									Movimiento mov = new Movimiento();
									mov.setTipoMovimiento("INGRESO");
									mov.setClaseMovimiento("cliente");
									mov.setElemento(elemento);
									Integer movIngCliAnterior = movimientoService.contarMovimientosFiltrados(mov, obtenerClienteAspUser());
									
									if(movIngCliAnterior!=null && movIngCliAnterior>0)
									{
										
										if(elemento.getDepositoActual()!=null && elemento.getDepositoActual().getId().longValue() != movimiento.getDepositoOrigenDestino().getId().longValue())
										{
											errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosDepositoOrigenDistinto");
											break;										
										}
										
									}
										
									for (Elemento elementoAComparar : movimiento.getListaElementos())
									{
										if(elemento.equals(elementoAComparar))
										{
											vecesExiste++;
										}
									}
									if(vecesExiste>1)
									{
										errors.rejectValue("listaElementos","formularioMovimiento.errorDetalles.elementosRepetidos");
										break;
									}
									
								}
							
						}
					}
					else
					{
						errors.rejectValue("listaElementos","formularioMovimiento.errorDetallesVacios");
					}
				}
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