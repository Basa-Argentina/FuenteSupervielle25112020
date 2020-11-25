package com.dividato.configuracionGeneral.controladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dividato.configuracionGeneral.validadores.LecturaValidator;
import com.security.accesoDatos.configuraciongeneral.hibernate.LecturaDetalleServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.EAN13;
import com.security.utils.LecturaCodigoBarraUtil;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Lectura.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioLectura.html",
				"/guardarActualizarLectura.html",
				"/importarLecturaDetalle.html",
				"/mostrarLecturaDetalle.html"
			}
		)
public class FormLecturaController {
	private LecturaService lecturaService;
	private LecturaValidator validator;
	private ListaLecturaController listaLecturaController;
	private LecturaDetalleService lecturaDetalleService;
	private ClienteEmpService clienteEmpService;
	private ElementoService elementoService;
	
	
	//private List<LecturaDetalle> lecturaDetalles = new ArrayList<LecturaDetalle>();
	
		
	/**
	 * Setea el servicio de Lectura.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase LecturaImp implementa Lectura y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param lecturaService
	 */
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	
	@Autowired
	public void setLecturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	
	@Autowired
	public void setListaLecturaController(ListaLecturaController listaLecturaController) {
		this.listaLecturaController = listaLecturaController;
	}
	
	@Autowired
	public void setValidator(LecturaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioLectura.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Lectura, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioLectura" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioLectura.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioLectura(
			@RequestParam(value="accion",required=false) String accion,	
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="val", required=false) String val) {
		
		Lectura lecturaFormulario = null;
		
		@SuppressWarnings("unchecked")
		List<LecturaDetalle> lecturaDetalles = (List<LecturaDetalle>) session.getAttribute("lecturaDetallesSession");
		if(lecturaDetalles == null){
			lecturaDetalles = new ArrayList<LecturaDetalle>();
		}
		
		if(session != null){
			session.setAttribute("idLectura", id);
		}
		if(accion==null){
			accion="NUEVO";//acción por defecto: nuevo
			lecturaDetalles.removeAll(lecturaDetalles);
			lecturaFormulario = new Lectura();
			lecturaFormulario.setFecha(new Date());
			atributos.put("lecturaFormulario", lecturaFormulario);
		}
		if(!accion.equals("NUEVO")){
			
			lecturaFormulario = lecturaService.obtenerPorId(Long.valueOf(id));
			atributos.put("lecturaFormulario", lecturaFormulario);
			session.setAttribute("lectura", lecturaFormulario);
		}
		
		LecturaDetalle lecturaDetalleFil = new LecturaDetalle();

		if(id != null){
			lecturaDetalleFil.setLectura(lecturaFormulario);
			lecturaDetalles = lecturaDetalleService.listarLecturaDetalleFiltradas(lecturaDetalleFil, obtenerClienteAspUser());
		}
		int cantLecturaDetalles = 0;
		if(lecturaDetalles != null){
			cantLecturaDetalles = lecturaDetalles.size();
		}
		atributos.put("cantLecturaDetalles", cantLecturaDetalles);
		atributos.put("lecturaDetalles", lecturaDetalles);
		session.setAttribute("lecturaDetallesSession", lecturaDetalles);
	
		//Seteo la accion actual
		atributos.put("accion", accion);
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		definirPopupClientes(atributos, val);
	
		//Se realiza el redirect
		return "formularioLectura";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param Lectura user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Lectura con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioLectura" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarLectura.html",
			method= RequestMethod.POST
		)
	public String guardarLectura(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="lecturaTipo",required=false) String lecturaTipo,
			@ModelAttribute("lecturaFormulario") final Lectura lecturaFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		
		@SuppressWarnings("unchecked")
		List<LecturaDetalle> lecturaDetalles = (List<LecturaDetalle>) session.getAttribute("lecturaDetallesSession");
		if(lecturaDetalles == null){
			lecturaDetalles = new ArrayList<LecturaDetalle>();
		}
		
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
				accion="NUEVO";
				
			
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){			
			lecturaFormulario.setAccion(accion);
			lecturaFormulario.setClienteAsp(obtenerClienteAspUser());
			Empresa empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
			Sucursal sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
			User usuario = obtenerUser();
			lecturaFormulario.setEmpresa(empresaDefecto);
			lecturaFormulario.setSucursal(sucursalDefecto);
			lecturaFormulario.setUsuario(usuario);
			Set<LecturaDetalle> detalles = new HashSet<LecturaDetalle>(lecturaDetalles);
			lecturaFormulario.setDetalles(detalles);
			validator.validate(lecturaFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Lectura lectura;
		if(!result.hasErrors()){
			
			if(accion.equals("NUEVO")){
				lectura = lecturaFormulario;
				Set<LecturaDetalle> setDetalles = new HashSet<LecturaDetalle>(lecturaDetalles);
				Long cantElementos = (long)setDetalles.size();
				lectura.setElementos(cantElementos);
				Long prevCodigo = lecturaService.traerUltCodigoPorClienteAsp(obtenerClienteAspUser());
				Long codigo = prevCodigo + 1;
				lecturaFormulario.setCodigo(codigo);
				//Se guarda la lectura en la BD
				commit = lecturaService.guardarLecturaYDetalles(setDetalles,lectura);
			}else{
				lectura = lecturaService.obtenerPorId(lecturaFormulario.getId());
				Set<LecturaDetalle> setDetalles = new HashSet<LecturaDetalle>(lecturaDetalles);
				Long cantElementos = (long)setDetalles.size();
				lectura.setElementos(cantElementos);
				setData(lectura, lecturaFormulario);
				//Se Actualiza el cliente en la BD
				Boolean noAnexar = (Boolean) session.getAttribute("noAnexar");
				List<LecturaDetalle> detalles = lecturaDetalleService.listarLecturaDetallePorLectura(lectura, obtenerClienteAspUser());
				lectura.setDetalles(new HashSet<LecturaDetalle>(detalles));
				commit = lecturaService.actualizarLecturaYDetalles(noAnexar,setDetalles,lectura);
			}			
			if(commit == null || !commit)
				lecturaFormulario.setId(lectura.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("lecturaFormulario", lecturaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioLectura(accion, lecturaFormulario.getId() !=null ? lecturaFormulario.getId().toString() : null, atributos, session, null);
		}
		
		if(result.hasErrors()){
			atributos.put("lecturaFormulario", lecturaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioLectura(accion, lecturaFormulario.getId() !=null ? lecturaFormulario.getId().toString() : null, atributos, session, null);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeLecturaReg = new ScreenMessageImp("formularioLectura.notificacion.lecturaRegistrada", null);
			avisos.add(mensajeLecturaReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		lecturaDetalles.removeAll(lecturaDetalles);
		session.removeAttribute("lecturaDetallesSession");
		session.removeAttribute("lectura");
		session.removeAttribute("noAnexar");
		return listaLecturaController.mostrarLectura(null, session, atributos);
	}
	
	
	@RequestMapping(
			value="/importarLecturaDetalle.html",
			method = RequestMethod.POST
		)
	public String importarLecturaDetalle
	(HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="anexar",required=false) Boolean anexar,
			@RequestParam(value="descripcion2",required=false) String descripcionLectura,
			@RequestParam(value="fecha2",required=false) String fechaLectura,
			@RequestParam(value="observacion2",required=false) String observacionLectura,
			@RequestParam(value="file",required=false) MultipartFile file,
			Map<String,Object> atributos){
		
		@SuppressWarnings("unchecked")
		List<LecturaDetalle> lecturaDetalles = (List<LecturaDetalle>) session.getAttribute("lecturaDetallesSession");
		if(lecturaDetalles == null){
			lecturaDetalles = new ArrayList<LecturaDetalle>();
		}
		
		Lectura lecturaFormulario;
		
		LecturaDetalle lecturaDetalle = null;
		Lectura lectura = (Lectura) session.getAttribute("lectura");
		if(lectura != null)
		{
			lecturaFormulario = lectura;
		}
		else
		{
			lecturaFormulario = new Lectura();
		}
			lecturaFormulario.setDescripcion(descripcionLectura);
			String fecha= fechaLectura;
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date date=sdf.parse(fecha);
				lecturaFormulario.setFecha(date);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			lecturaFormulario.setObservacion(observacionLectura);
		
		
		try {
			String dir = "c://Archivos_de_Lecturas//archivos//";
			new File(dir).mkdirs();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
			File archivo = new File(dir, "Lectura " + sd.format(new Date())
					+ " " + file.getOriginalFilename());
			file.transferTo(archivo);
			LecturaCodigoBarraUtil lector = new LecturaCodigoBarraUtil();
			List<String> lista = new ArrayList<String>();
			lista = lector.decodificarLectura(archivo);
			Long orden = (long)0;

			if (lista != null) {
				if (anexar == null) {
					lecturaDetalles.removeAll(lecturaDetalles);
					session.setAttribute("noAnexar", true);
				} else {
					session.setAttribute("noAnexar", false);
					for (LecturaDetalle lecturaDetalleOrden : lecturaDetalles) {
						if(lecturaDetalleOrden.getOrden()>orden)
						{
							orden = lecturaDetalleOrden.getOrden();
						}
					}
				}
				List<String> listaDescartados = new ArrayList<String>();
				String codigo,codigoCorrecto,codigoTomado12;
				Boolean repetido;
				for (int i = 0; i < lista.size(); i++) {
					repetido = false;
					codigo = lista.get(i);
					if(codigo.length() >= 12){
					codigoTomado12 = lista.get(i).substring(0, 12);
					codigoCorrecto = codigoTomado12 + String.valueOf(EAN13.EAN13_CHECKSUM(codigoTomado12));
						//if(codigo.equals(codigoCorrecto)){
							lecturaDetalle = new LecturaDetalle();
							if (codigo.startsWith("99")) {
								lecturaDetalle.setCodigoBarras(codigoTomado12);
								lecturaDetalle
										.setObservacion("Este código de barras pertenece a un módulo");
							} else {
								lecturaDetalle.setCodigoBarras(codigoTomado12);
								Elemento elemento = elementoService.getByCodigo(codigoTomado12,
										obtenerClienteAspUser());
								if (elemento != null) {
									lecturaDetalle.setElemento(elemento);
									lecturaDetalle.setObservacion("Elemento "
											+ elemento.getCodigo());
								} else {
									lecturaDetalle
											.setObservacion("Elemento no existente.");
								}
							}
							if(lecturaDetalles.size()>0)
							{
								for(int f = (lecturaDetalles.size()-1);f>=0;f--)
								{
									if(codigoTomado12.equals(lecturaDetalles.get(f).getCodigoBarras()))
									{
										listaDescartados.add(codigoTomado12+"(Repetido)\n");
										repetido = true;
										break;
									}
								}
							}
								if(!repetido)
								{
									orden++;
									lecturaDetalle.setOrden(orden);
									lecturaDetalles.add(lecturaDetalle);
								}
							//}
//							else
//							{
//								listaDescartados.add(codigo+"(Dígito de control inválido)\n");					
//							}
					}
					else
					{
						listaDescartados.add(codigo+" ----  (Linea errónea)\n");					
					}
				}	
				if(listaDescartados.size() > 0)
				{
					String dirDescar = "c://Archivos_de_Lecturas//descartados//";
					new File(dirDescar).mkdirs();
					SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
					File archivoDescartados = new File(dirDescar, "Descartados en" + sd2.format(new Date())
							+ " de " + file.getOriginalFilename());
					BufferedWriter bw = new BufferedWriter(new FileWriter(archivoDescartados));
					bw.write(listaDescartados.toString());
					bw.close();
				}
			}
			archivo.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		atributos.put("accion", accion);
		atributos.put("lecturaFormulario", lecturaFormulario);
		atributos.put("lecturaDetalles", lecturaDetalles);
		session.setAttribute("lecturaDetallesSession", lecturaDetalles);
		return "formularioLectura";
	}
		
	
	@RequestMapping(
			value="/mostrarLecturaDetalle.html",
			method = RequestMethod.GET
		)
	public String mostarLecturaDetalle(HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="anexar",required=false) Boolean anexar,
			@RequestParam(value="descripcion2",required=false) String descripcionLectura,
			@RequestParam(value="fecha2",required=false) String fechaLectura,
			@RequestParam(value="observacion2",required=false) String observacionLectura,
			@RequestParam(value="file",required=false) MultipartFile file,
			Map<String,Object> atributos){
		
		@SuppressWarnings("unchecked")
		List<LecturaDetalle> lecturaDetalles = (List<LecturaDetalle>) session.getAttribute("lecturaDetallesSession");
		if(lecturaDetalles == null){
			lecturaDetalles = new ArrayList<LecturaDetalle>();
		}
		
		Lectura lecturaFormulario;
		LecturaDetalle lecturaDetalle = null;
		Lectura lectura = (Lectura) session.getAttribute("lectura");
		if(lectura != null)
		{
			LecturaDetalle lecturaDetalleFil = new LecturaDetalle();
			lecturaDetalleFil.setLectura(lectura);
			lecturaFormulario = lectura;
		}
		else
		{
			lecturaFormulario = new Lectura();
			lecturaFormulario.setDescripcion(descripcionLectura);
			String fecha= fechaLectura;
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date date=sdf.parse(fecha);
				lecturaFormulario.setFecha(date);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			lecturaFormulario.setObservacion(observacionLectura);
		}
		atributos.put("accion", accion);
		atributos.put("lecturaFormulario", lecturaFormulario);
		atributos.put("lecturaDetalles", lecturaDetalles);
		session.setAttribute("lecturaDetallesSession", lecturaDetalles);
		return "formularioLectura";
	}
	
	
/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupClientes(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioClienteDireccion.cliente.razonSocial",false));
		campos.add(new CampoDisplayTag("nombre","formularioClienteDireccion.cliente.nombre",false));
		campos.add(new CampoDisplayTag("apellido","formularioClienteDireccion.cliente.apellido",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteDireccion.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteDireccion.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, null, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "clienteCodigo"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "precargaFormularioClienteDireccion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	

	private void setData(Lectura lectura, Lectura data){
		if(data != null){			
			lectura.setDescripcion(data.getDescripcion());
			lectura.setFecha(data.getFecha());
			lectura.setObservacion(data.getObservacion());
			lectura.setFecha(data.getFecha());
			lectura.setEmpresa(data.getEmpresa());
			lectura.setSucursal(data.getSucursal());
			lectura.setUsuario(data.getUsuario());
			lectura.setClienteAsp(data.getClienteAsp());
			if(data.getUtilizada()!=null){
			lectura.setUtilizada(data.getUtilizada());}
		}
	
	}
	

	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}

	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}
