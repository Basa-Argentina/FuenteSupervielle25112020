package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dividato.configuracionGeneral.objectForms.TransferenciaContenedorForm;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la administración
 * de la ClasificacionDocumental.
 *
 * @author Emiliano Maldonado
 */

@Controller
@RequestMapping(
		value=
			{					
				"/precargaFormularioTransferenciaContenedor.html",
				"/ejecutarTransferenciaContenedor.html"
			}
		)
public class FormTransferenciaContenedores {
private static Logger logger=Logger.getLogger(FormTransferenciaContenedores.class);
	
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	private ClienteEmpService clienteEmpService;
	private ElementoService elementoService;
	private ReferenciaService referenciaService;
	
	@Autowired
	public void setServices(EmpresaService empresaService,
			SucursalService sucursalService,
			ClienteEmpService clienteEmpService,
			ElementoService elementoService,
			ReferenciaService referenciaService
			) {
		this.empresaService = empresaService;
		this.sucursalService = sucursalService;
		this.clienteEmpService = clienteEmpService;
		this.elementoService = elementoService;
		this.referenciaService = referenciaService;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[]{"clienteId","codigoEmpresa","codigoSucursal",
				"codigoCliente","codigoContenedorOrigen","codigoContenedorDestino"});	
	}
	
	@RequestMapping(value="/precargaFormularioTransferenciaContenedor.html")
	public String precargaFormularioLotesReferencia(Map<String,Object> atributos) {
		
		TransferenciaContenedorForm form = new TransferenciaContenedorForm();
		form.setClienteId(obtenerClienteAspUser().getId());
		form.setCodigoEmpresa(obtenerCodigoEmpresaUser());
		form.setCodigoSucursal(obtenerCodigoSucursalUser());
		
		atributos.put("transferenciaContenedorForm", form);
		
		return "formularioTransferenciaContenedores";
	}
	@RequestMapping(value="/ejecutarTransferenciaContenedor.html")
	public String ejecutarTransferenciaContenedor(Map<String,Object> atributos,
			@ModelAttribute("transferenciaContenedorForm") TransferenciaContenedorForm form,
			BindingResult result) {
		
		Elemento contenedorOrigen = elementoService.getContenedorByCodigo(form.getCodigoContenedorOrigen(),
				form.getCodigoCliente(), false, obtenerClienteAspUser(), null);
		if(contenedorOrigen==null){
			result.rejectValue("codigoContenedorOrigen","required");
		}
		Elemento contenedorDestino = elementoService.getContenedorByCodigo(form.getCodigoContenedorDestino(),
				form.getCodigoCliente(), false, obtenerClienteAspUser(), null);
		if(contenedorDestino==null){
			result.rejectValue("codigoContenedorDestino","required");
		}
		
		List<Referencia> referenciasContenidas = null;
		if(contenedorOrigen!=null){
			referenciasContenidas = referenciaService.obtenerByElementoContenedor(contenedorOrigen);
			if(referenciasContenidas.size()==0){
				result.reject("errors.origenSinReferencias");
			}
		}
		
		if(result.hasErrors()){
			atributos.put("transferenciaContenedorForm", form);
			atributos.put("errores", true);
			atributos.put("result", result);
			return "formularioTransferenciaContenedores";
		}else{
			//actualizar
			
			
			contenedorDestino.setClienteEmp(clienteEmpService.getByCodigo(form.getCodigoCliente(), form.getCodigoEmpresa(), obtenerClienteAspUser()));
			elementoService.actualizar(contenedorDestino);
			
			for(Referencia ref : referenciasContenidas){
				if(ref.getElemento().getCodigo().equals(form.getCodigoContenedorOrigen())){
					ref.setElemento(contenedorDestino);
					referenciaService.actualizar(ref);
				}else{
					ref.getElemento().setContenedor(contenedorDestino);
					elementoService.actualizar(ref.getElemento());
				}
			}
			
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioTransferenciaContenedores.notificacion.transferenciaRegistrada", null);
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
			return precargaFormularioLotesReferencia(atributos);
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private String obtenerCodigoEmpresaUser(){
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if(user.getPersona()!=null && user.getPersona() instanceof PersonaFisica){
			return ((PersonaFisica)user.getPersona()).getEmpresaDefecto().getCodigo();
		}
		return null;
	}
	private String obtenerCodigoSucursalUser(){
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if(user.getPersona()!=null && user.getPersona() instanceof PersonaFisica){
			return ((PersonaFisica)user.getPersona()).getSucursalDefecto().getCodigo();
		}
		return null;
	}	
}
