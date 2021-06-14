package com.dividato.configuracionGeneral.validadores.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.RequerimientoReferencia;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;

@Component
public class RequerimientoWebValidator implements Validator{
	private TipoOperacionService tipoOperacionService;
	private ElementoService elementoService;
	
	
	@Autowired
	public void setTipoOperacionService(TipoOperacionService tipoOperacionService) {
		this.tipoOperacionService = tipoOperacionService;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class type) {
		return Requerimiento.class.isAssignableFrom(type);
	}

	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaAlta",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaEntrega",
				new CustomDateEditor(formatoFechaFormularios, true));
	}

	@Override
	public void validate(Object obj, Errors errors) {	
		Requerimiento requerimiento = (Requerimiento) obj;
		if(!requerimiento.isBuscarElemento() && !requerimiento.isBuscarElementoSinReferencia() 
				&& !requerimiento.isInsertarElementoDirecto()){
			
			boolean banderaRequeridos = false;
			
			if(requerimiento.getClienteCodigo() == null || "".equals(requerimiento.getClienteCodigo())){
				errors.rejectValue("clienteCodigo", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getCodigoDireccion() == null || "".equals(requerimiento.getCodigoDireccion())){
				errors.rejectValue("codigoDireccion", "required");
				banderaRequeridos = true;
			}
//			if(requerimiento.getCodigoSerie() == null || "".equals(requerimiento.getCodigoSerie())){
//				errors.rejectValue("codigoSerie", "required");
//				banderaRequeridos = true;
//			}
			if(requerimiento.getTipoRequerimientoCod() == null || "".equals(requerimiento.getTipoRequerimientoCod())){
				errors.rejectValue("tipoRequerimientoCod", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getCodigoPersonal() == null || "".equals(requerimiento.getCodigoPersonal())){
				errors.rejectValue("codigoPersonal", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getCodigoPersonalAutorizante() == null || "".equals(requerimiento.getCodigoPersonalAutorizante())){
				errors.rejectValue("codigoPersonalAutorizante", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getFechaAlta() == null){
				errors.rejectValue("fechaAlta", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getFechaEntrega() == null){
				errors.rejectValue("fechaEntrega", "required");
				banderaRequeridos = true;
			}
//			if(requerimiento.getListaPrecioCodigo() == null || "".equals(requerimiento.getListaPrecioCodigo())){
//				errors.rejectValue("listaPrecioCodigo", "required");
//				banderaRequeridos = true;
//			}
			if(banderaRequeridos)
				return;
			//Valido los formatos de hora
			if(validarHoraIncorrecta(requerimiento.getHoraAlta()))
				errors.rejectValue("horaAlta", "formularioRequerimiento.errorHora");
			if(validarHoraIncorrecta(requerimiento.getHoraEntrega()))
				errors.rejectValue("horaEntrega", "formularioRequerimiento.errorHora");
			
			if(requerimiento.getFechaAlta() != null && requerimiento.getFechaEntrega() != null)
				if(requerimiento.getFechaAlta().after(requerimiento.getFechaEntrega()))
					errors.rejectValue("fechaAlta", "formularioRequerimiento.errorFechaAltaEntrega");
			
			//Valido que se hallan ingresado elementos, sino se igresaron deposito es requerido
//			if((requerimiento.getListaElementos() == null || requerimiento.getListaElementos().size()==0) &&
//					(requerimiento.getCodigoDeposito() == null || "".equals(requerimiento.getCodigoDeposito())))
//				errors.rejectValue("codigoDeposito", "formularioRequerimiento.errorDepositoSinElemento");
			
			//Si el tipo de operacion no tiene seleccionado la division por deposito y la lista de elementos es mayor a uno y son de distinto deposito
			//el deposito es requerido
			
			if(requerimiento.getTipoRequerimiento()!=null && requerimiento.getListaElementos() != null 
					//&& requerimiento.getListaElementos().size()>1
					&& (requerimiento.getCodigoDeposito() == null || "".equals(requerimiento.getCodigoDeposito()))){
				boolean banderaNoDesagregaElementosPorDeposito = false;
				ArrayList<TipoOperacion> listaTipoOperaciones = (ArrayList<TipoOperacion>) tipoOperacionService.listarTipoOperacion(null, null, requerimiento.getTipoRequerimiento(), obtenerClienteAspUser());
				if(listaTipoOperaciones!=null){
					for(TipoOperacion tipoOperacion:listaTipoOperaciones){
						if(!tipoOperacion.getDesagregaPorDeposito()){
							banderaNoDesagregaElementosPorDeposito = true;
							break;
						}
					}
				}
				if(banderaNoDesagregaElementosPorDeposito){
					Deposito deposito = null;
					boolean banderaDepositoDiferente = false;
					for(RequerimientoReferencia requerimientoReferencia:requerimiento.getListaElementos()){
//						Se comenta porque se busca el contenedor
//						if(deposito==null){
//							if(requerimientoReferencia.getReferencia()!=null && requerimientoReferencia.getReferencia().getElemento()!=null 
//									&& requerimientoReferencia.getReferencia().getElemento().getDepositoActual()!=null)
//								deposito = requerimientoReferencia.getReferencia().getElemento().getDepositoActual();
//						}
//						else{
//							if(requerimientoReferencia.getReferencia()!=null && requerimientoReferencia.getReferencia().getElemento()!=null 
//									&& requerimientoReferencia.getReferencia().getElemento().getDepositoActual()!=null 
//									&& requerimientoReferencia.getReferencia().getElemento().getDepositoActual().getId().longValue() != deposito.getId().longValue()){
//								banderaDepositoDiferente = true;
//								break;
//							}
//								
//						}
						
						if(deposito==null){
							if(requerimientoReferencia.getReferencia()!=null && requerimientoReferencia.getReferencia().getElemento()!=null 
									&& (requerimientoReferencia.getReferencia().getElemento().getDepositoActual()!=null || 
											(requerimientoReferencia.getReferencia().getElemento().getContenedor()!= null && requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual()!=null))){
								if(requerimientoReferencia.getReferencia().getElemento().getContenedor()==null || requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual()==null)
									deposito = requerimientoReferencia.getReferencia().getElemento().getDepositoActual();
								else{
									if(requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual()!=null)
										deposito = requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual();
								}
							}
						}
						else{
							Deposito depositoComparar = null;
							if(requerimientoReferencia.getReferencia()!=null && requerimientoReferencia.getReferencia().getElemento()!=null 
									&& (requerimientoReferencia.getReferencia().getElemento().getDepositoActual()!=null || 
											(requerimientoReferencia.getReferencia().getElemento().getContenedor()!= null && requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual()!=null))){
								if(requerimientoReferencia.getReferencia().getElemento().getContenedor()==null || requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual()==null)
									depositoComparar = requerimientoReferencia.getReferencia().getElemento().getDepositoActual();
								else{
									if(requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual()!=null)
										depositoComparar = requerimientoReferencia.getReferencia().getElemento().getContenedor().getDepositoActual();
								}
							}
							if(depositoComparar!=null){
								if(deposito.getId().longValue() != depositoComparar.getId().longValue()){
									banderaDepositoDiferente = true;
									break;
								}
							}
								
						}

					}
					if(banderaDepositoDiferente)
						errors.rejectValue("codigoDeposito", "formularioRequerimiento.errorDepositoElementosDiferentes");
					if(deposito==null)
						errors.rejectValue("codigoDeposito", "formularioRequerimiento.errorElementosYContenedoresSinDeposito");
				}
			}
			
		}
		else{
			
			if(requerimiento.getClienteCodigo() == null || "".equals(requerimiento.getClienteCodigo())){
				errors.rejectValue("clienteCodigo", "required");
				return;
			}
			
			if(requerimiento.isInsertarElementoDirecto())
				
		
			{
				
				if(requerimiento.getTipoRequerimientoCod() == null || "".equals(requerimiento.getClienteCodigo())){
					errors.rejectValue("clienteCodigo", "required");
					return;
				}
				
				Elemento elemento = elementoService.buscarElementosParaRequerimientosPorSQL(requerimiento.getCodigoElemento(), 
						requerimiento.getClienteCodigo(), requerimiento.getTipoRequerimientoCod());
				
				
				if(elemento==null){	
				
					errors.rejectValue("codigoElemento", "formularioRequerimiento.errorInsertarElemento");
					return;
					
				}
				
				
				
				else if(requerimiento.getTipoRequerimiento().getTipoMovimiento().equals("ingreso") && !elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE) && 
						!elemento.getEstado().equalsIgnoreCase("En Consulta")){
					errors.rejectValue("codigoElemento","formularioRequerimiento.errorInsertarElementoEstado",new String[]{elemento.getCodigo(),elemento.getEstado()},"");
					return;
				}
				
				else if(requerimiento.getTipoRequerimiento().getTipoMovimiento().equals("egreso") && !elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_GUARDA) && 
						!elemento.getEstado().equalsIgnoreCase("En Planta")){
					errors.rejectValue("codigoElemento","formularioRequerimiento.errorInsertarElementoEstado",new String[]{elemento.getCodigo(),elemento.getEstado()},"");
					return;
				}
				
				requerimiento.setElemento(elemento);
			
			}
		}
	}
	
	
	private boolean validarHoraIncorrecta(String hora){
		String [] split = hora.split(":");
		try{
			Integer h = Integer.parseInt(split[0]);
			Integer m = Integer.parseInt(split[1]);
		}
		catch (NumberFormatException e) {
			return true;
		}
		return false;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		
		return obtenerUser().getCliente();
	}
	
	private User obtenerUser(){		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
