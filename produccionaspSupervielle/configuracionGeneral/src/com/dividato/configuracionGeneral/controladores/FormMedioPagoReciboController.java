package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.MedioPagoReciboValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.BancoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MedioPagoReciboService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoMedioPagoService;
import com.security.modelo.configuraciongeneral.Banco;
import com.security.modelo.configuraciongeneral.MedioPagoRecibo;
import com.security.modelo.configuraciongeneral.TipoMedioPago;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de CuentaCorriente.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author X
 *
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/mostrarFormularioMedioCobro.html",
				"/guardarActualizarMedioPagoRecibo.html",
				"/eliminarMedioCobro.html"
			}
		)
public class FormMedioPagoReciboController {
	@SuppressWarnings("unused")
	private ClienteEmpService clienteEmpService;
	private TipoMedioPagoService tipoMedioPagoService;
	private MedioPagoReciboService medioPagoReciboService;
	private BancoService bancoService;
	private MedioPagoReciboValidator validator;
	
	@Autowired
	public void setService(ClienteEmpService clienteEmpService,
			TipoMedioPagoService tipoMedioPagoService,
			MedioPagoReciboService medioPagoReciboService,
			BancoService bancoService) {
		this.clienteEmpService = clienteEmpService;
		this.tipoMedioPagoService = tipoMedioPagoService;
		this.medioPagoReciboService = medioPagoReciboService;
		this.bancoService = bancoService;
	}
	
	@Autowired
	public void setValidator(MedioPagoReciboValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		//validator.initDataBinder(binder);
	}
	
	
	/*
	 * Medio de Cobro
	 * 
	 * */	
	@RequestMapping(
			value="/mostrarFormularioMedioCobro.html",
			method = RequestMethod.GET
		)
	public String mostrarFormularioMedioCobro(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) Long id,	
			HttpSession session,
			Map<String,Object> atributos) {
		
		List<TipoMedioPago> tipoMedioPagoList = tipoMedioPagoService.listarTodos();
		List<Banco> bancoList = bancoService.listarTodos();
		atributos.put("tipoMedioPagoList", tipoMedioPagoList);
		atributos.put("bancoList", bancoList);
		
		if(id!=null){
			//MedioPagoRecibo medioPagoReciboForm = medioPagoReciboService.obtenerPorId(id);
			List<MedioPagoRecibo> medioPagoList = (List<MedioPagoRecibo>)session.getAttribute("medioPagoList");
			for (MedioPagoRecibo mpr:medioPagoList){
				if(mpr.getId()==id){
					atributos.put("medioPagoReciboForm", mpr);
					break;
				}
			}
			
		}
		atributos.put("id", id);
		atributos.put("accion", accion);
		return "formularioMedioCobro";
	}
	
	
	@RequestMapping(
			value="/guardarActualizarMedioPagoRecibo.html",
			method= RequestMethod.POST
		)
	public String guardarActualizarMedioPagoRecibo(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long id,
			@ModelAttribute("medioPagoReciboForm") final MedioPagoRecibo medioPagoReciboForm,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		
		Boolean commit = null;
		List<String> codigoErrores = new ArrayList<String>();
		if(medioPagoReciboForm.getIdTipoMedioPago()==null || medioPagoReciboForm.getIdTipoMedioPago()==-1)
		{
			codigoErrores.add("formularioMedioCobro.error.tipoMedio");
		}
		// Inicio Selecciono Cheque
		if(medioPagoReciboForm.getIdTipoMedioPago()!=null && medioPagoReciboForm.getIdTipoMedioPago()==2)
		{
			if(medioPagoReciboForm.getIdBancoSel()==null || medioPagoReciboForm.getIdBancoSel()==-1){
				codigoErrores.add("formularioMedioCobro.error.banco");
			}
			if(medioPagoReciboForm.getNumero()==null || medioPagoReciboForm.getNumero().trim().length()==0){
				codigoErrores.add("formularioMedioCobro.error.numero");
			}	
		}
		// Fin Selecciono Cheque
		if(medioPagoReciboForm.getIdTipoMedioPago()!=null && (medioPagoReciboForm.getIdTipoMedioPago()==2 || medioPagoReciboForm.getIdTipoMedioPago()==3))
		{
			if(medioPagoReciboForm.getFechaVencimiento()==null){
				codigoErrores.add("formularioMedioCobro.error.fechaVencimiento");
			}
			if(medioPagoReciboForm.getTitular()==null || medioPagoReciboForm.getTitular().trim().length()==0){
				codigoErrores.add("formularioMedioCobro.error.titular");
			}
		}
		
		
		if(medioPagoReciboForm.getImporte()==null){
			codigoErrores.add("formularioMedioCobro.error.importe");
		}
		
		result = generateErrors(codigoErrores);		
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		medioPagoReciboForm.setTipoMedioPago(tipoMedioPagoService.obtenerPorId(medioPagoReciboForm.getIdTipoMedioPago()));
		medioPagoReciboForm.setBanco(bancoService.obtenerPorId(medioPagoReciboForm.getIdBancoSel()));
		if(!result.hasErrors()){
			List<MedioPagoRecibo> medioPagoList = (List<MedioPagoRecibo>)session.getAttribute("medioPagoList");
			if(id==null){
				
				if(medioPagoList==null){
					medioPagoList = new ArrayList<MedioPagoRecibo>();
					session.setAttribute("medioPagoList", medioPagoList);
				}
				Long i=0L;
				if(medioPagoReciboForm.getId()==null){
					if(!medioPagoList.isEmpty()){
						
						for(MedioPagoRecibo mp:medioPagoList){
							if(mp.getId()>=i){
								i=mp.getId()+1;
							}
						}
					}
					medioPagoReciboForm.setId(i);
				}
				medioPagoList.add(medioPagoReciboForm);
			}else{
				int x=0;
				for(MedioPagoRecibo mp:medioPagoList){
					if(mp.getId()==id){
						medioPagoList.set(x, medioPagoReciboForm);
						break;
					}
					x++;
				}
			}
			commit = true;
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.remove("medioPagoReciboForm");
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}	
		
		if(result.hasErrors()){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeCuentaCorrienteReg = new ScreenMessageImp("formularioMedioCobro.notificacion.medioCobroDeleteExito", null);
			avisos.add(mensajeCuentaCorrienteReg); //agrego el mensaje a la coleccion
			atributos.remove("medioPagoReciboForm");
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeCuentaCorrienteReg = new ScreenMessageImp("formularioMedioCobro.notificacion.medioCobroRegistroExito", null);
			avisos.add(mensajeCuentaCorrienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
			atributos.remove("medioPagoReciboForm");
		}
		return mostrarFormularioMedioCobro(accion,medioPagoReciboForm.getId(),session,atributos);
	}
	
	@RequestMapping(value="/eliminarMedioCobro.html")
	public String eliminarMedioCobro(
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletRequest request){
		
		
			List<MedioPagoRecibo> medioPagoList = (List<MedioPagoRecibo>)session.getAttribute("medioPagoList");
			for(MedioPagoRecibo mpr:medioPagoList){
				if(mpr.getId()==id){
					medioPagoList.remove(mpr);
				}
			}
		return "formularioCuentaCorriente";
	}
	
	
	
	
	private BindingResult generateErrors(List<String> codigoErrores) {
		BindingResult result = new BeanPropertyBindingResult(new Object(),"");
		if (!codigoErrores.isEmpty()) {
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(	"error.formBookingGroup.general", codigo, null, false, new String[] { codigo }, null, "?"));
			}
		}
		return result;
	}
	
}
