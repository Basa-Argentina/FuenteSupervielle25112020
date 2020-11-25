package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ModuloService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.ParseNumberUtils;
import com.security.utils.ReposicionamientoUtil;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados al
 * reposicionamiento.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Luciano de Asteinza
 * 
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarReposicionamiento.html",
				"/mostrarReposicionamiento.html",	
				"/guardarReposicionamiento.html"
			}
		)
public class ListaReposicionamientoController {

	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	private EmpresaService empresaService;
	private ElementoService elementoService;
	private TipoElementoService tipoElementoService;
	private ModuloService moduloService;
	private PosicionService posicionService;
	
	
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	
	@Autowired
	public void setModuloService(ModuloService moduloService) {
		this.moduloService = moduloService;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}	
	
	@Autowired
	public void setLecturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	
	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}
	
	@RequestMapping(value="/iniciarReposicionamiento.html", method = RequestMethod.GET)
	public String iniciarReposicionamiento(HttpSession session, Map<String,Object> atributos){
		
		session.removeAttribute("listaElementosAReposicionarSession");
		session.removeAttribute("listaElementosAnterioresModuloDestinoSession");
		session.removeAttribute("lecturaSession");
		session.removeAttribute("listaPosicionesDestinoSession");
		session.removeAttribute("listaPosicionesOrigenSession");
		session.removeAttribute("lecturaBusqueda");
		
		atributos.remove("elementosReposicionados");

		return "redirect:mostrarReposicionamiento.html";
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/mostrarReposicionamiento.html", method = RequestMethod.GET)
	public String mostrarReposicionamiento(HttpSession session, Map<String,Object> atributos,
			@ModelAttribute(value="lecturaBusqueda") Lectura lecturaBusqueda,
			@RequestParam(value="codigoLectura", required=false) String codigoLecturaStr,
			@RequestParam(value="val", required=false) String valLectura,
			@RequestParam(value="libera", required=false) Boolean libera){
		
		List<Elemento> listaElementosAReposicionar = (List<Elemento>) session.getAttribute("listaElementosAReposicionarSession");
		if(listaElementosAReposicionar == null){
			listaElementosAReposicionar = new ArrayList<Elemento>();
		}
		
		List <Posicion> listaPosicionesDestino = (List<Posicion>) session.getAttribute("listaPosicionesDestinoSession");
		if(listaPosicionesDestino == null){
			listaPosicionesDestino = new ArrayList<Posicion>();
		}
		
		Lectura lectura = (Lectura) session.getAttribute("lecturaSession");
		
		lectura = null;
		if(libera==null)
		{
			libera = false;
		}
		//Lectura lecturaBusqueda = (Lectura)session.getAttribute("lecturaBusqueda");
		Long codigoLectura = parseLongCodigo(codigoLecturaStr);
		if(lecturaBusqueda==null){
			lecturaBusqueda = new Lectura();
		}
		if(codigoLecturaStr!=null){
			lecturaBusqueda.setCodigo(codigoLectura);
		}
		if(atributos.get("elementosReposicionados")==null){
			listaElementosAReposicionar = new ArrayList<Elemento>();
		}
		String prefijo = null;
		Modulo moduloDestino = null;
		ArrayList<String> codigosErrores = new ArrayList<String>();
		List<LecturaDetalle> lecturaDetalles = null;
		ArrayList<String> codigosElementos = new ArrayList<String>();  
		String tipoElementoStr = null;
		TipoElemento tipoElemento = null;
		boolean depActYdepDesIguales = true;
		boolean banderaLecturaCorrecta = true;
		//se carga una lectura
		//si no hay errores
		if(atributos.get("errores") == null){
			atributos.put("errores", false);
		}
		if(lecturaBusqueda.getCodigo()!=null){
			ClienteAsp clienteAsp = obtenerClienteAsp();			
			lectura = lecturaService.obtenerPorCodigo(Long.valueOf(lecturaBusqueda.getCodigo()), Boolean.FALSE,  obtenerEmpresaDefault(), clienteAsp);
			if(lectura==null){
				codigosErrores.add(Constantes.ERROR_NO_EXISTE_LECTURA);
			}else{
				LecturaDetalle lecturaDetalleBusqueda = new LecturaDetalle();
				lecturaDetalleBusqueda.setLectura(lectura);
				lecturaDetalles = lecturaDetalleService.listarLecturaDetalleFiltradas(lecturaDetalleBusqueda, clienteAsp);
				lecturaBusqueda.setDescripcion(lectura.getDescripcion());
				//recorre la coleccion con los codigos de barras, verifica que el modulo exista y 
				//que todos los codigos de elementos pertenezcan a un tipo valido 
				//ademas de que todos los elementos pertenezcan al mismo tipo			
				for(LecturaDetalle detalle: lecturaDetalles){
					prefijo = detalle.getCodigoBarras().substring(0, 2);
					//si el primero es módulo
					if(prefijo.equalsIgnoreCase("99")){					
						moduloDestino = getModuloPorCodigoBarras(detalle.getCodigoBarras().substring(0, 12));
						if(moduloDestino==null){
							codigosErrores.add(Constantes.ERROR_MODULO_NO_EXISTE);
							break;
						}
					}else{
						//la primera ves que ingresa setea el tipo de elemento que debe repetirse para las demas pasadas.
						if(tipoElementoStr == null){
							tipoElementoStr = prefijo;
							tipoElemento = tipoElementoService.getByPrefijo(tipoElementoStr, clienteAsp);
							if(tipoElemento==null){
								codigosErrores.add(Constantes.ERROR_TIPO_ELEMENTO_NO_EXISTE);
								break;
							}else{
								codigosElementos.add(detalle.getCodigoBarras().substring(0, 12));
							}
						}else{
							if(tipoElementoStr.equals(detalle.getCodigoBarras().substring(0,2))){
								codigosElementos.add(detalle.getCodigoBarras().substring(0, 12));							
							}else{
								codigosErrores.add(Constantes.ERROR_LOS_ELEMENTOS_DE_LA_LECTURA_NO_SON_DEL_MISMO_TIPO);
								banderaLecturaCorrecta = false;
								break;
							}
						}
					}
				}//end for
				
				//verifico errores
				if(moduloDestino != null && tipoElemento != null && banderaLecturaCorrecta){
					listaElementosAReposicionar = elementoService.getByCodigos(codigosElementos, clienteAsp);
					if(listaElementosAReposicionar != null && codigosElementos.size() == listaElementosAReposicionar.size()){
						verificarElementos(listaElementosAReposicionar,tipoElemento, moduloDestino, codigosErrores);	
						listaPosicionesDestino = posicionService.getPosicionesPorModuloParaReposicionamiento(moduloDestino, clienteAsp);
						if(libera==false)
						{
							if(verificarPosicionesDestino(listaPosicionesDestino))
							{
								cargarNuevasPosiciones(listaPosicionesDestino, listaElementosAReposicionar);
							}
							else
							{
								codigosErrores.add(Constantes.ERROR_EXISTEN_POSICIONES_OCUPADAS);
							}
						}
						else
						{
							if(verificarPosicionesAnuladas(listaPosicionesDestino,listaElementosAReposicionar))
								cargarNuevasPosiciones(listaPosicionesDestino, listaElementosAReposicionar);
							else
								codigosErrores.add(Constantes.ERROR_EXISTEN_POSICIONES_ANULADAS);
						}
					}else{
						codigosErrores.add(Constantes.ERROR_ALGUNOS_ELEMENTOS_NO_EXISTEN);
					}
					
				}
			}	
		}
		if(listaElementosAReposicionar==null || codigosErrores.size()>0){
			listaElementosAReposicionar = new ArrayList<Elemento>();
		}
		
		session.setAttribute("listaPosicionesDestinoSession", listaPosicionesDestino);
		session.setAttribute("listaElementosAReposicionarSession", listaElementosAReposicionar);
		session.setAttribute("lecturaBusqueda", lecturaBusqueda);
		session.setAttribute("lecturaSession", lectura);
		
		atributos.put("elementosReposicionados", listaElementosAReposicionar);

		atributos.put("codigoEmpresa", obtenerEmpresaDefault().getCodigo());
		generateErrors(codigosErrores, atributos);
		
		//definirPopupLecturas(atributos, valLectura);
		
		return "consultaReposicionamiento";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/guardarReposicionamiento.html", method = RequestMethod.GET)
	public String guardarReposicionamiento(HttpSession session, Map<String,Object> atributos){
		
		List<Elemento> listaElementosAReposicionar = (List<Elemento>) session.getAttribute("listaElementosAReposicionarSession");
		if(listaElementosAReposicionar == null){
			listaElementosAReposicionar = new ArrayList<Elemento>();
		}
		
		List<Elemento> listaElementosAnterioresModuloDestino = (List<Elemento>) session.getAttribute("listaElementosAnterioresModuloDestinoSession");
		if(listaElementosAnterioresModuloDestino == null){
			listaElementosAnterioresModuloDestino = new ArrayList<Elemento>();
		}
		
		List <Posicion> listaPosicionesDestino = (List<Posicion>) session.getAttribute("listaPosicionesDestinoSession");
		if(listaPosicionesDestino == null){
			listaPosicionesDestino = new ArrayList<Posicion>();
		}
		
		List <Posicion> listaPosicionesOrigen = (List<Posicion>) session.getAttribute("listaPosicionesOrigenSession");
		if(listaPosicionesOrigen == null){
			listaPosicionesOrigen = new ArrayList<Posicion>();
		}
		
		Lectura lectura = (Lectura) session.getAttribute("lecturaSession");
		
		ArrayList <String> codigoErrores = new ArrayList<String>();
		if(atributos.get("errores") == null){
			atributos.put("errores", false);
		}
		if(lectura!=null){
			if(listaPosicionesDestino!=null && listaPosicionesDestino.size()>0 ){
				if(listaElementosAReposicionar != null && listaElementosAReposicionar.size() > 0){
					ClienteAsp clienteAsp = obtenerClienteAsp();
					listaPosicionesOrigen = obtenerPosicionesOrigen(listaElementosAReposicionar, clienteAsp);					
					actualizarViejasPosiciones(listaPosicionesOrigen);
						Modulo moduloDestino = listaElementosAReposicionar.get(0).getPosicionFutura().getModulo();
						listaElementosAnterioresModuloDestino = elementoService.buscarElementosAnterioresModuloDestino(moduloDestino, clienteAsp);
						if(elementoService.guardarReposicionamiento(listaPosicionesOrigen, listaPosicionesDestino, listaElementosAReposicionar, listaElementosAnterioresModuloDestino, clienteAsp,obtenerUser(),lectura)){
							
							generateAvisoExito(atributos);
							atributos.remove("elementosReposicionados");
							session.removeAttribute("lecturaBusqueda");
							session.removeAttribute("listaElementosAReposicionarSession");
							session.removeAttribute("listaElementosAnterioresModuloDestinoSession");
							session.removeAttribute("lecturaSession");
							session.removeAttribute("listaPosicionesDestinoSession");
							session.removeAttribute("listaPosicionesOrigenSession");
							
						}else{
							codigoErrores.add(Constantes.ERROR_FALLA_REPOSICIONAMIENTO);					
					}
				}else{
					codigoErrores.add(Constantes.ERROR_NO_HAY_ELEMENTOS_PARA_REPOSICIONAR);
				}
			}else{
				codigoErrores.add(Constantes.ERROR_FALLA_REPOSICIONAMIENTO);
			}
		}else{
			codigoErrores.add(Constantes.ERROR_NO_EXISTE_LECTURA);
		}
		generateErrors(codigoErrores, atributos);
		
		return mostrarReposicionamiento(session, atributos, null, null, null,null);
	}
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	
//	private void definirPopupLecturas(Map<String,Object> atributos, String val){
//		//creo un hashmap para almacenar los parametros del popup
//		Map<String,Object> lecturasPopupMap = new HashMap<String, Object>();
//		//definicion de los campos a mostrar en la tabla
//		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
//		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
//		campos.add(new CampoDisplayTag("codigo","formularioReposicionamiento.datosElemento.codigo",false));
//		campos.add(new CampoDisplayTag("descripcion","formularioReposicionamiento.datosElemento.descripcion",false));		
//		lecturasPopupMap.put("campos", campos);
//		//Coleccion de objetos a utilizar en el popup
//		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
//		ClienteAsp casp= obtenerClienteAsp();
//		Empresa emp = obtenerEmpresaDefault();
//		lecturasPopupMap.put("coleccionPopup", lecturaService.listarLecturaPopup(val, casp, emp ));
//		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
//		lecturasPopupMap.put("referenciaPopup", "codigo");
//		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
//		lecturasPopupMap.put("referenciaPopup2", "descripcion");
//		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
//		lecturasPopupMap.put("referenciaHtml", "codigoLecturaStr"); 		
//		//url que se debe consumir con ajax
//		lecturasPopupMap.put("urlRequest", "mostrarReposicionamiento.html");
//		//se vuelve a setear el texto utilizado para el filtrado
//		lecturasPopupMap.put("textoBusqueda", val);		
//		//codigo de la localización para el titulo del popup
//		lecturasPopupMap.put("tituloPopup", "textos.seleccion");
//		//Agrego el mapa a los atributos
//		atributos.put("lecturasPopupMap", lecturasPopupMap);
//	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresaDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getEmpresaDefecto();
	}
	
	private Integer parseIntegerCodigo(String codigo){
		return ParseNumberUtils.parseIntegerCodigo(codigo);
	}
	
	private Long parseLongCodigo(String codigo){
		return ParseNumberUtils.parseLongCodigo(codigo);
	}
	/**
	 * A partir del codigo de barra obtiene el codigo de estanteria 
	 * y las coordenadas de offset del modulo para realizar una consulta
	 * (filtrando por clienteAsp) en la base de datos y devolver el modulo 
	 * correspondiente. 
	 * @param codigoBarras
	 * @return
	 */
	private Modulo getModuloPorCodigoBarras(String codigoBarras){
		String codEstante = codigoBarras.substring(2, 6);
		String offSetVStr = codigoBarras.substring(6, 9);
		String offSetHStr = codigoBarras.substring(9, 12);
		return moduloService.getModuloByOffset(codEstante, parseIntegerCodigo(offSetVStr), parseIntegerCodigo(offSetHStr), obtenerClienteAsp());
	}
	
	/**
	 * Realiza las siguientes verificaciones y carga sus codigos de error en la lista de codigos de error:<br>
	 *  1 - Que el reposicionamiento sea a modulo completo (numero de elementos multiplo del numero de posiciones horizontales del modulo).
	 * 	2 - Que todos los elementos sean del mismo tipo
	 *  3 - Que todos los elementos a reposicionar tengan el mismo origen (nulo o el mismo modulo).
	 *  4 - Que todos los modulos del grupo al que pertenece el modulo destino contienen el mismo tipo de elemento.
	 *  
	 * @param elementos
	 * @param tipoElemento
	 * @param moduloDestino
	 * @param errores
	 * @return
	 */
	private void verificarElementos(List<Elemento> elementos, TipoElemento tipoElemento, Modulo moduloDestino, List<String> errores) {
		ReposicionamientoUtil util = new ReposicionamientoUtil();
		/*if (!util.verificarReposicionamientoModuloCompleto(elementos, moduloDestino)) {   ///// ESTA VALIDACION SE PIDIO SACAR POR LA GENTE DE BASA - LUIS Y MIGUEL
		errores.add(Constantes.ERROR_NUM_ELEMENTOS_NO_ES_MULTIPLO_POS_HOR);
	}else*/ if(!util.verificarTodosElementosMismoTipo(elementos, tipoElemento)) {
			errores.add(Constantes.ERROR_LOS_ELEMENTOS_DE_LA_LECTURA_NO_SON_DEL_MISMO_TIPO);
		}else if(!util.verificarSuficientesPosicionesModuloDestino(elementos, moduloDestino)) {
			errores.add(Constantes.ERROR_POSICIONES_INSUFICIENTES_MODULO_DESTINO);
		}else if (!util.verificarTodosElementosPosicionables(elementos)){
			errores.add(Constantes.ERROR_ELEMENTOS_NO_POSICIONABLES_EN_LECTURA);
		}else if(!util.verificarTipoValidoParaModuloDestino(tipoElemento, moduloDestino, elementoService, obtenerClienteAsp())){
			errores.add(Constantes.ERROR_EL_GRUPO_DEL_MODULO_DESTINO_CONTIENE_UN_TIPO_ELEMENTO_DIFERENTE);
		}else if(!util.verificarDepositoActualIgualDepositoDestino(moduloDestino,elementos)){
			errores.add(Constantes.ERROR_DEPOSITO_ACTUAL_DE_ELEMENTOS_DISTINTO_DEPOSITO_DESTINO);
		}
	}
	/**
	 * genera el objeto BindingResult para mostrar los errores por pantalla en un popup y lo agrega al map atributos
	 * @param codigoErrores
	 * @param atributos
	 */
	private void generateErrors(List<String> codigoErrores,	Map<String, Object> atributos) {
		if (!codigoErrores.isEmpty()) {
			BindingResult result = new BeanPropertyBindingResult(new Object(),"");
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(	"error.formBookingGroup.general", codigo, null, false, new String[] { codigo }, null, "?"));
			}
			atributos.put("result", result);
			atributos.put("errores", true);
		} else if(atributos.get("result") == null){
			atributos.put("errores", false);
		}
	}
	
	private void generateAvisoExito(Map<String, Object> atributos) {
		//Genero las notificaciones 
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		ScreenMessage mensajeEstanteReg = new ScreenMessageImp(Constantes.EXITO_REPOSICIONAMIENTO_REALIZADO, null);
		avisos.add(mensajeEstanteReg); //agrego el mensaje a la coleccion
		atributos.put("errores", false);
		atributos.remove("result");
		atributos.put("hayAvisos", true);
		atributos.put("hayAvisosNeg", false);
		atributos.put("avisos", avisos);
	}
	
	/**
	 * Agrega las posiciones del modulo de destino a cada elemento del parametro listaElementos
	 * en el campo transcient posicionFutura. Ademas setea las posiciones asignadas como "ocupadas"
	 * y las libres como "temporalmente anuladas"  
	 * @param listaPosiciones
	 * @param listaElementos
	 * @return
	 */
	private boolean cargarNuevasPosiciones(List <Posicion> listaPosiciones, List<Elemento> listaElementos){		
		boolean result = true;
		if(listaElementos!=null && listaPosiciones!=null){
			Iterator<Elemento> itElementos = listaElementos.iterator();
			Iterator<Posicion> itPosicion = listaPosiciones.iterator();
			Elemento elemento = null;
			Posicion posicion = null;
			while(itPosicion.hasNext()){
				posicion = itPosicion.next();
				if(itElementos.hasNext()){
					elemento = itElementos.next();
					elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
					elemento.setPosicionFutura(posicion);
					posicion.setEstado(Constantes.POSICION_ESTADO_OCUPADA);
				}else{
					posicion.setEstado(Constantes.POSICION_ESTADO_TEMPORALMENTE_ANULADA);
				}
			}			
		}else{
			result = false;
		}
		return result;
	}
	
	/**
	 * Quita de la lista de posiciones las anuladas o temporalmente anuladas. Luego pregunta si las restants alcanzan para posicionar los elementos, de ser
	 * asi devuelve true y el proceso continua, si no devuelve false y el proceso se corta."  
	 * @param listaPosiciones
	 * @param listaElementos
	 * @return
	 */
	private boolean verificarPosicionesAnuladas(List <Posicion> listaPosiciones, List<Elemento> listaElementos){		
		List<Posicion> posicionesAnuladas = new ArrayList<Posicion>();
		for(Posicion pos:listaPosiciones)
		{
			if(Constantes.POSICION_ESTADO_ANULADO.equals(pos.getEstado()) || Constantes.POSICION_ESTADO_TEMPORALMENTE_ANULADA.equals(pos.getEstado()))
			{
				posicionesAnuladas.add(pos);
			}
		}
		listaPosiciones.removeAll(posicionesAnuladas);
		if(listaPosiciones.size()<listaElementos.size())
			return false;
		return true;
	}
	
	/**
	 * Actualiza el estado de las posiciones de origen a DISPONIBLE, siempre que la posicion de origen no este ANULADA o TEMPORALMENTE ANULADA.
	 * @param listaPosicionesOrigen
	 */
	private void actualizarViejasPosiciones(List<Posicion> listaPosicionesOrigen){
		
		for(Posicion pos : listaPosicionesOrigen){
					if(!Constantes.POSICION_ESTADO_ANULADO.equals(pos.getEstado()) && !Constantes.POSICION_ESTADO_TEMPORALMENTE_ANULADA.equals(pos.getEstado())){
						pos.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
					}
		}
	}
	
	/**
	 * Verifica que en las posiciones de destino obtenidas no exista ninguna con el estado OCUPADA.
	 * @param listaPosicionesDestino
	 * 
	 */
	private Boolean verificarPosicionesDestino(List<Posicion> listaPosicionesDestino){
		
		for(Posicion pos : listaPosicionesDestino){
			if(!Constantes.POSICION_ESTADO_DISPONIBLE.equals(pos.getEstado())){
				return false;
			}
		}
		return true;
	}
	
	private List<Posicion> obtenerPosicionesPorModulo(Modulo modulo){
		List <Posicion> listaPos = null;
		if(modulo != null){
			listaPos = posicionService.getPosicionesPorModuloParaReposicionamiento(modulo, obtenerClienteAsp());
		}
		return listaPos!=null? listaPos : new ArrayList<Posicion>();
	}
	
	/**
	 * Devuelve los elementos ubicados antes del reposicionamiento en el modulo de destino (si existen)
	 * @param moduloDestino
	 * @param clienteAsp
	 * @return si no encuentra ninguno devuelve una lista vacia.
	 */
	private List<Elemento> obtenerElementosAnterioresModuloDestino(Modulo moduloDestino, ClienteAsp clienteAsp){
		List<Elemento> listaElementosAnteriores = elementoService.buscarElementosAnterioresModuloDestino(moduloDestino, clienteAsp);
		for(Elemento e:listaElementosAnteriores){
			e.setPosicion(null);
		}
		return listaElementosAnteriores;
	}
	
	/**
	 * Devuelve la lista de posiciones anteriores de los elementos a reposicionar.
	 * @param elementosReposicionados Los elementos a reposicionar.
	 * @return Devuelve una lista vacia si los elementos a reposicionar no tienen ninguna posicion anterior.
	 */
	private List<Posicion> obtenerPosicionesOrigen(List<Elemento> elementosReposicionados, ClienteAsp clienteAsp){
		return elementoService.obtenerPosicionesAnterioresPorElementos(elementosReposicionados, clienteAsp);
	}
}
