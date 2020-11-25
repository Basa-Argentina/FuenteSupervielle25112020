/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.configuraciongeneral.interfaz.AfipCondIvaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.accesoDatos.interfaz.ClienteEmpleadosService;
import com.security.accesoDatos.interfaz.GroupService;
import com.security.accesoDatos.interfaz.PersonaFisicaService;
import com.security.accesoDatos.interfaz.PersonaJuridicaService;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ClienteEmpleados;
import com.security.modelo.configuraciongeneral.Direccion;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.general.PersonaJuridica;
import com.security.modelo.general.TipoDocumento;
import com.security.modelo.seguridad.Group;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para Importar los datos TopSpeed 
 * 
 * 
 * @author Luis Manzanelli
 */

@Controller
@RequestMapping(value = { "/inicioFormularioImportarDatos.html",
		"/importarDatos.html", "/renombrarExtencionDatos.html"

})
public class FormImportarDatosController {

	private DireccionService direccionService;
	private TipoDocumentoService tipoDocumentoService;
	private AfipCondIvaService afipCondIvaService;
	private EmpresaService empresaService;
	private ClienteAspService clienteAspService; 
	private TipoElementoService tipoElementoService; 
	private ClienteEmpService clienteEmpService;
	private BarrioService barrioService;
	private GroupService groupService;
	private PersonaJuridicaService personaJuridicaService;
	private UserService userService;
	private ClienteEmpleadosService clienteEmpleadosService;
	private SucursalService sucursalService; 
	private ElementoService elementoService;
	private PersonaFisicaService personaFisicaService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private LoteReferenciaService loteReferenciaService;
	private ReferenciaService referenciaService; 
	
	
	private static Connection con;
	private static Hashtable<String, PreparedStatement> preparedStatements = new Hashtable<String, PreparedStatement>();
	private static Logger logger = Logger
			.getLogger(FormImportarDatosController.class);

	@Autowired
	public void setServices(
			DireccionService direccionService,
			TipoDocumentoService tipoDocumentoService,
			AfipCondIvaService afipCondIvaService,
			EmpresaService empresaService,
			ClienteAspService clienteAspService,
			TipoElementoService tipoElementoService,
			ClienteEmpService clienteEmpService,
			BarrioService barrioService,
			GroupService groupService,
			PersonaJuridicaService personaJuridicaService,
			UserService userService,
			ClienteEmpleadosService clienteEmpleadosService,
			SucursalService sucursalService,
			ElementoService elementoService,
			PersonaFisicaService personaFisicaService,
			ClasificacionDocumentalService clasificacionDocumentalService,
			LoteReferenciaService loteReferenciaService,
			ReferenciaService referenciaService) {
		this.direccionService = direccionService;
		this.tipoDocumentoService = tipoDocumentoService;
		this.afipCondIvaService = afipCondIvaService;
		this.empresaService = empresaService;
		this.clienteAspService = clienteAspService;
		this.tipoElementoService = tipoElementoService;
		this.clienteEmpService = clienteEmpService;
		this.barrioService = barrioService;
		this.groupService = groupService;
		this.personaJuridicaService = personaJuridicaService;
		this.userService = userService;
		this.clienteEmpleadosService = clienteEmpleadosService;
		this.sucursalService = sucursalService;
		this.elementoService = elementoService;
		this.personaFisicaService = personaFisicaService;
		this.clasificacionDocumentalService = clasificacionDocumentalService;
		this.loteReferenciaService = loteReferenciaService;
		this.referenciaService = referenciaService;
	}

	@RequestMapping(value = "/inicioFormularioImportarDatos.html")
	public String inicioFormularioImportarDatos(
			Map<String, Object> atributos,
			HttpServletRequest request) {

		return "formularioImportarDatos";
	}

	@RequestMapping(value = "/renombrarExtencionDatos.html")
	public String renombrarExtencionDatos(HttpSession session,

	@RequestParam(value = "path", required = true) String path,
		Map<String, Object> atributos, HttpServletRequest request) {
		ScreenMessage mensajeClienteReg = null;
		if (!path.equals("")) {
			// String path = "D:/Proyectos/basa/BASA/datas";
			try {

				File folder = new File(path);
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						String name = listOfFiles[i].getName();
						String nombre = name.split("\\.")[0];
						String extencion = name.split("\\.")[1];
						if (extencion.equalsIgnoreCase("tps")) {
							File f = null;
							String file = listOfFiles[i].getPath().replaceAll(
									name,
									nombre + "." + extencion.toUpperCase());
							f = new File(file);
							listOfFiles[i].renameTo(f);
						}
					}
				}
				mensajeClienteReg = new ScreenMessageImp("formularioImportarDatos.notificacion.okModificacion", Arrays.asList(new String[]{}));
			} catch (Exception e) {
				e.getStackTrace();
				logger.error("No se pudo renombrar extencion de Archivos TPS: ", e);
				mensajeClienteReg = new ScreenMessageImp("formularioImportarDatos.notificacion.errorPath", Arrays.asList(new String[]{}));
			}
		}else{
			mensajeClienteReg = new ScreenMessageImp("formularioImportarDatos.notificacion.errorPath", Arrays.asList(new String[]{}));
		}
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
		atributos.put("errores", false);
		atributos.remove("result");
		atributos.put("hayAvisos", true);
		atributos.put("avisos", avisos);
		return inicioFormularioImportarDatos(atributos, request);
	}

	@RequestMapping(value="/importarDatos.html")
	public String importarDatos(HttpSession session,
			@RequestParam(value="stringConexion",required=false) String stringConexion,
			Map<String,Object> atributos,
			HttpServletRequest request) {
			ScreenMessage mensajeClienteReg = null;
		try{
			// Inicio Variables Utiles
			String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
			List<ClienteEmp> clienteEmpList = new ArrayList<ClienteEmp>();
			TipoElemento tipoElemento8 = tipoElementoService.obtenerPorId(8);
			ClienteAsp clienteAsp4 = clienteAspService.obtenerPorId(4);
			ClienteAsp clienteAsp5 = clienteAspService.obtenerPorId(5);
			Sucursal sucursal7 = sucursalService.obtenerPorId(7);
			TipoDocumento tipoDocCliente1 = tipoDocumentoService.obtenerPorId(1);
			TipoDocumento tipoDocCliente8 = tipoDocumentoService.obtenerPorId(8);
			Empresa empresa6 = empresaService.obtenerPorId(6);
			Group group14 = groupService.obtenerPorId(14);
			TipoElemento tipoElemento7 = tipoElementoService.obtenerPorId(7);
			// Fin Variables Utiles
			
			Class.forName(driver);
			con=DriverManager.getConnection("jdbc:odbc:"+stringConexion);	
			
			// Migración de clientes
			PreparedStatement select= getPreparedStatement("SELECT * FROM CLIENTE");
			ResultSet rs=select.executeQuery();
			
			Integer iControl = 0;
			while(rs.next()){
				try{
					String localidad = rs.getString("LOCALIDAD");
					String cuit = rs.getString("CUIT");
					String telefonoenvioinfo = rs.getString("TELEFONOENVIOINFO");
					// D1
					Direccion direccionD1 = new Direccion();
					direccionD1.setCalle(rs.getString("DOMICILIO"));
					direccionD1.setNumero(rs.getString("NUMEROCALLE"));
					
					direccionD1.setObservaciones("PROVINCIA "+ rs.getString("PROVINCIA")+" LOCALIDAD "+localidad+" - Cod.Postal: "+rs.getString("CODIGOPOSTAL"));
					direccionD1.setBarrio(barrioService.obtenerPorNombreLocalidad(localidad));
					direccionService.guardar(direccionD1);
					// D2 
					Direccion direccionD2 = new Direccion();
					direccionD2.setCalle(null);
					direccionD2.setNumero(null);
					direccionD2.setObservaciones(null);
					direccionD2.setBarrio(null);
					direccionService.guardar(direccionD2);
					// P1 
					// PJ1 
					PersonaJuridica persona_juridica = new PersonaJuridica();
					persona_juridica.setNumeroDoc(cuit);
					persona_juridica.setTelefono(telefonoenvioinfo);
					persona_juridica.setDireccion(direccionD2);
					TipoDocumento tipoDoc = !persona_juridica.getNumeroDoc().equals("")?tipoDocCliente1:tipoDocCliente8;
					persona_juridica.setTipoDoc(tipoDoc);
					persona_juridica.setRazonSocial(rs.getString("NOMBRE"));
					personaJuridicaService.guardar(persona_juridica);
					// C1
					ClienteEmp clientesEmp = new ClienteEmp();
					clientesEmp.setCodigo("90"+rs.getInt("IDCLIENTE"));
					clientesEmp.setFax(rs.getString("FAXENVIOINFO"));
					clientesEmp.setHabilitado(true);
					clientesEmp.setNumeroDoc(cuit);
					clientesEmp.setTelefono(telefonoenvioinfo);
					String afipCondIva = rs.getString("IVA");
					long condIva = 5;
					if(afipCondIva.equalsIgnoreCase("INSCRIPTO")){
						condIva = 1;
					}
					if(afipCondIva.equalsIgnoreCase("EXENTO")){
						condIva = 4;
					}
					clientesEmp.setAfipCondIva(afipCondIvaService.obtenerPorId(condIva));
					clientesEmp.setDireccion(direccionD1);
					clientesEmp.setEmpresa(empresa6);
					clientesEmp.setRazonSocial(persona_juridica);
					TipoDocumento tipoDocCliente = !clientesEmp.getNumeroDoc().equals("")?tipoDocCliente1:tipoDocCliente8;
					clientesEmp.setTipoDoc(tipoDocCliente);
					clientesEmp.setTipoPersona("JURIDICA");
					clientesEmp.setMesesFacturables(rs.getString("NUMEROSMESEAFACTURAR"));
					clienteEmpService.guardar(clientesEmp);
					clienteEmpList.add(clientesEmp);
					
				}catch(Exception ex){
					logger.error("Error en la lectura de la tabla CLIENTE - Registro Nro "+iControl+": ", ex);
				}
				iControl ++;
			}

			// Migración de personal de los clientes
			select= getPreparedStatement("SELECT * FROM CONTACTO");
			ResultSet rs2=select.executeQuery();
			iControl = 0;
			while(rs2.next()){
				try{
					String email = rs2.getString("EMAIL");
					int idContacto = rs2.getInt("IDCONTACTO");
					// D3
					Direccion direccion3 = new Direccion();
					direccion3.setCalle(null);
					direccion3.setNumero(null);
					direccion3.setObservaciones(null);
					direccion3.setBarrio(null);
					direccionService.guardar(direccion3);
					// P2
					// PF1
					PersonaFisica persona_fisica = new PersonaFisica();
					String telefono = email.indexOf("@")!=-1?email:null;
					persona_fisica.setTelefono(telefono);
					persona_fisica.setDireccion(direccion3);
					persona_fisica.setTipoDoc(tipoDocCliente8);
					persona_fisica.setNombre(rs2.getString("NOMBRECONTACTO"));
					personaFisicaService.guardar(persona_fisica);
					
					// U1 
					User user = new User();
					user.setAdmin(false);
					user.setEnable(false);
					user.setPassword("b66a33f306fd97b5c2fe9054ed80ae33cc520bb1");
					user.setUsername("CUS"+idContacto);
					user.setCliente(clienteAsp5);
					user.setPersona(persona_fisica);
					// UG1 Generar nx_user_group 
					Group group = group14;
					Set<Group> setGroup = new HashSet<Group>();
					user.setGroups(setGroup);
					user.getGroups().add(group);
					userService.guardar(user);
					
					// CE1 
					ClienteEmpleados clienteEmpleados = new ClienteEmpleados();
					clienteEmpleados.setCodigo(String.valueOf(idContacto));
					clienteEmpleados.setHabilitado(true);
					String codigo = "90"+rs2.getString("IDCLIENTE");
					ClienteEmp clienteEmp = clienteEmpService.getByCodigo(codigo);
					clienteEmpleados.setClienteEmp(clienteEmp);
					clienteEmpleadosService.guardar(clienteEmpleados);
					
				}catch(Exception ex){
					logger.error("Error en la lectura de la tabla CONTACTO - Registro Nro "+iControl+": ", ex);
				}
				iControl++;
			}
			
			// Migración de elementos (Cajas)
			select= getPreparedStatement("SELECT * FROM CJAS");
			ResultSet rs3=select.executeQuery();
			iControl = 0;
			while(rs3.next()){
				try{
					// E1
					String estadoValue = rs3.getString("ESTADO");
					int idCaja = rs3.getInt("IDCAJA");
					int idCliente = rs3.getInt("IDCLIENTE");
					if(idCliente!=0 && estadoValue!=null){
						Elemento elementoE1 = new Elemento();
						elementoE1.setCodigo("07"+String.format("%010d", idCaja));
						String estado = "";
						if(estadoValue.equalsIgnoreCase("OCUPADA")){
							estado = "En Guarda";
						}
						if(estadoValue.equalsIgnoreCase("LIBRE")){
							estado = "Creado";
						}
						if(estadoValue.equalsIgnoreCase("EN TRANSITO")){
							estado = "En Consulta";
						}
						elementoE1.setEstado(estado);
						elementoE1.setGeneraCanonMensual(true);
						elementoE1.setClienteAsp(clienteAsp4);
						long idClienteEmp = Long.valueOf("90"+idCliente);
						elementoE1.setClienteEmp(clienteEmpService.obtenerPorId(idClienteEmp));
						elementoE1.setTipoElemento(tipoElemento7);
						elementoE1.setCodigoAlternativo(String.valueOf(idCaja));
						try{
							elementoE1.setNroPrecinto(Long.valueOf(rs3.getString("NROPRECINTO")));
						}catch(Exception e){}
						elementoE1.setUbicacionProvisoria(rs3.getString("UBICACION"));
						elementoService.guardar(elementoE1);
					}
				}catch(Exception ex){
					logger.error("Error en la lectura de la tabla CJAS - Registro Nro "+iControl+": ", ex);
				}
				iControl++;
			}
			
			// Migración de referencias
			List<ClasificacionDocumental> clasificacionDocumentalList = new ArrayList<ClasificacionDocumental>();
			
			for (ClienteEmp clienteEmp : clienteEmpList){
				String nroTablaStr = null;
				try{
					int nroTabla = Integer.valueOf(clienteEmp.getCodigo().replaceAll("90", ""));
					nroTablaStr = String.format("%04d", nroTabla);
					select= getPreparedStatement("SELECT DISTINCT NOMBRESUCURSAL, NOMBRETIPODOCUMENTO FROM DCTO"+nroTablaStr);
					// Con el resultado del select obtengo el arbol que tengo que armar
					ResultSet rs4=select.executeQuery();
					List<migracionReferencia> migracionReferenciaList = new ArrayList<FormImportarDatosController.migracionReferencia>();
					while(rs4.next()){
						migracionReferencia mr = new migracionReferencia();
						mr.setNombreSucursal(rs4.getString("NOMBRESUCURSAL"));
						mr.setNombreTipoDocumento(rs4.getString("NOMBRETIPODOCUMENTO"));
						migracionReferenciaList.add(mr);
					}
					/*-	Primer nivel: Se deberá crear un nodo para cada cliente, será el nodo raíz.
					-	Segundo nivel: Será cada una de las ocurrencias diferentes del campo “NOMBRESUCURSAL”.
					-	Tercer nivel: Cada una de las diferentes ocurrencias del campo “NOMBRETIPODOCUMENTO” 
						donde el padre será el anteriormente creado con “NOMBRESUCURSAL”.*/
					ClasificacionDocumental clasificacionDocumentalPadre = null;
					Integer codigo = 0;
					List<migracionReferencia> listaValidar = new ArrayList<FormImportarDatosController.migracionReferencia>();
					
					for (migracionReferencia m : migracionReferenciaList){
						// buscar en la collecion el segundo nivel osea NOMBRESUCURSAL
						boolean noExisteNomSuc = true;
						for (migracionReferencia mv2:listaValidar){
							if(mv2.getNombreSucursal().equals(m.getNombreSucursal())){
								noExisteNomSuc = false;
								break;
							}
						}
						if(noExisteNomSuc){
							// Primer Nivel
							clasificacionDocumentalPadre = crearClasificacionDoc(); 
							clasificacionDocumentalPadre.setClienteAsp(clienteAsp4);
							//clasificacionDocumentalPadre.setClienteEmp(clienteEmpService.getByCodigo(clienteEmp.getCodigo()));
							clasificacionDocumentalPadre.setClienteEmp(clienteEmp);
							clasificacionDocumentalPadre.setCodigo(codigo++);
							clasificacionDocumentalPadre.setNombre(clienteEmp.getCodigo());
							clasificacionDocumentalPadre.setDescripcion(clienteEmp.getCodigo());
							clasificacionDocumentalService.guardar(clasificacionDocumentalPadre);
							clasificacionDocumentalList.add(clasificacionDocumentalPadre);
							// Segundo Nivel
							ClasificacionDocumental clasificacionDocumental = crearClasificacionDoc();
							clasificacionDocumental.setClienteAsp(clienteAsp4);
							String codigoClienteEmp = clienteEmp.getCodigo();
							clasificacionDocumental.setClienteEmp(clienteEmpService.getByCodigo(codigoClienteEmp));
							clasificacionDocumental.setCodigo(codigo++);
							clasificacionDocumental.setNombre(m.getNombreSucursal());
							clasificacionDocumental.setDescripcion(m.getNombreSucursal());
							clasificacionDocumental.setNodo("N");
							clasificacionDocumental.setPadre(clasificacionDocumentalPadre);
							clasificacionDocumentalPadre.getNodosHijos().add(clasificacionDocumental);
							clasificacionDocumentalService.guardar(clasificacionDocumental);
							// Tercer Nivel
							List<migracionReferencia> listaValidar3 = new ArrayList<FormImportarDatosController.migracionReferencia>();
							for(migracionReferencia m3 : migracionReferenciaList){
								boolean noExisteNomTipDoc = true;
								for (migracionReferencia mv3:listaValidar3){
									if(mv3.getNombreTipoDocumento().equals(m3.getNombreTipoDocumento())){
										noExisteNomTipDoc = false;
										break;
									}
								}
								if(noExisteNomTipDoc && m.getNombreSucursal().equals(m3.getNombreSucursal())){
									// Valido que no este repetido el 3 nivel y que pertenesca a la sucursal
									ClasificacionDocumental cd = crearClasificacionDoc();
									cd.setClienteAsp(clienteAsp4);
									cd.setClienteEmp(clienteEmpService.getByCodigo(clienteEmp.getCodigo()));
									cd.setCodigo(codigo++);
									cd.setNombre(m3.getNombreTipoDocumento());
									cd.setDescripcion(m3.getNombreTipoDocumento());
									cd.setNodo("I");
									cd.setPadre(clasificacionDocumental);
									clasificacionDocumental.getNodosHijos().add(cd);
									clasificacionDocumentalService.guardar(cd);
								}
								listaValidar3.add(m3);
							}
						}
						listaValidar.add(m);
					}
				}catch(SQLException e){
					while(e!=null){
						e.printStackTrace();
						e=e.getNextException();
						logger.error("Error en la lectura: " , e);
					}
				}	
			
				// Esta lista "clasificacionDocumentalList" la utilizo para buscar el contenedor 
				String documento = "DCTO"+nroTablaStr;
				select= getPreparedStatement("SELECT * FROM "+ documento);
				// Con el resultado del select obtengo el arbol que tengo que armar
				ResultSet rs5=select.executeQuery();
				iControl = 0;
				while(rs5.next()){
					try{
						Long desdeNro = Long.valueOf(rs5.getInt("DESDENUMERO"));
						Long hastaNro = Long.valueOf(rs5.getInt("HASTANUMERO"));
						int idCaja = rs5.getInt("IDCAJA");
						LoteReferencia loteReferencia = null;
						if(desdeNro.longValue()==hastaNro.longValue()){
							// E2
							Elemento elementoE2 = new Elemento();
							// Código
							elementoE2.setEstado("En Guarda");
							elementoE2.setGeneraCanonMensual(false);
							elementoE2.setClienteAsp(clienteAsp4);
							//String codigoEmp = "90"+idCliente;
							//elementoE2.setClienteEmp(clienteEmpService.getByCodigo(codigoEmp));
							elementoE2.setClienteEmp(clienteEmp);
							//Buscar el Id en la tabla elementos donde “elemento.codigo = ‘07’ 
							//+ ceros + IDCAJA“ donde ceros deberá completarse hasta llegar a 12 dígitos.
							String elementoCodigo = "07"+String.format("%010d", idCaja);
							elementoE2.setContenedor(elementoService.getElementoByCodigo(elementoCodigo, null, null, null,null));
							// Contenedor_id
							elementoE2.setTipoElemento(tipoElemento8);
							elementoE2.setCodigoAlternativo(String.valueOf(idCaja));
							elementoE2.setUbicacionProvisoria(rs5.getString("UBICACION"));
							elementoService.guardar(elementoE2);
							
							// LR1
							loteReferencia = new LoteReferencia();
							loteReferencia.setFechaRegistro(new Date());
							loteReferencia.setClienteAsp(clienteAsp5);
							//String codigoEmpLR1 = "90"+idCliente;
							//loteReferencia.setClienteEmp(clienteEmpService.getByCodigo(codigoEmpLR1));
							loteReferencia.setClienteEmp(clienteEmp);
							loteReferencia.setEmpresa(empresa6);
							loteReferencia.setSucursal(sucursal7);
							loteReferenciaService.guardar(loteReferencia);
							
							
							// R1
							Referencia referencia = new Referencia();
							referencia.setDescripcion(rs5.getString("DESCRIPCION"));
							referencia.setFecha1(new Date());
							referencia.setFecha2(new Date());
							referencia.setIndiceIndividual(true);
							referencia.setNumero1(desdeNro);
							referencia.setNumero2(hastaNro);
							// Se deberá localizar el Id creado anteriormente utilizando los campos “NOMBRESUCURSAL” y “NOMBRETIPODOCUMENTO”. 
							// El primero para el segundo nivel y el segundo para el tercer nivel.
							String nombreSucursal = rs5.getString("NOMBRESUCURSAL");
							String nombreTipoDocumento = rs5.getString("NOMBRETIPODOCUMENTO");
							for ( ClasificacionDocumental cd : clasificacionDocumentalList){
								if(cd.getClienteEmp().getId().equals(loteReferencia.getClienteEmp().getId())){
									for(ClasificacionDocumental cdHijos2: cd.getNodosHijos()){
										if(nombreSucursal.equalsIgnoreCase(cdHijos2.getNombre())){
											for (ClasificacionDocumental cdHijos3 : cdHijos2.getNodosHijos()){
												System.out.println("cdHijos3 "+cdHijos3.getNombre()+"nombreTipoDocumento "+nombreTipoDocumento);
												if(nombreTipoDocumento.equalsIgnoreCase(cdHijos3.getNombre())){
													referencia.setClasificacionDocumental(cdHijos3);
													break;
												}
											}
											break;
										}
									}
									break;
								}
							}
							referencia.setElemento(elementoE2);
							referencia.setLoteReferencia(null);
							referenciaService.guardar(referencia);
						}else{
							//Si los campos DESDENUMERO y HASTANUMERO son DISTINTOS, 
							// entonces se deberá crear directamente el lote de referencias y las referencias
							// LR2
							LoteReferencia loteReferencia2 = new LoteReferencia();
							loteReferencia2.setFechaRegistro(new Date());
							loteReferencia2.setClienteAsp(clienteAsp5);
							//String codigoEmpLR2 = "90"+idCliente;
							//loteReferencia2.setClienteEmp(clienteEmpService.getByCodigo(codigoEmpLR2));
							loteReferencia2.setClienteEmp(clienteEmp);
							loteReferencia2.setEmpresa(empresa6);
							loteReferencia2.setSucursal(sucursal7);
							loteReferenciaService.guardar(loteReferencia2);
							
							// R2
							Referencia referencia2 = new Referencia();
							referencia2.setDescripcion(rs5.getString("DESCRIPCION"));
							referencia2.setFecha1(new Date());
							referencia2.setFecha2(new Date());
							referencia2.setIndiceIndividual(false);
							referencia2.setNumero1(desdeNro);
							referencia2.setNumero2(hastaNro);
							// Se deberá localizar el Id creado anteriormente utilizando los campos “NOMBRESUCURSAL” y “NOMBRETIPODOCUMENTO”. 
							// El primero para el segundo nivel y el segundo para el tercer nivel.
							String nombreSucursal = rs5.getString("NOMBRESUCURSAL");
							String nombreTipoDocumento = rs5.getString("NOMBRETIPODOCUMENTO");
							for ( ClasificacionDocumental cd : clasificacionDocumentalList){
								if(cd.getClienteEmp().getId().equals(loteReferencia2.getClienteEmp().getId())){
									for(ClasificacionDocumental cdHijos2: cd.getNodosHijos()){
										if(nombreSucursal.equalsIgnoreCase(cdHijos2.getNombre())){
											for (ClasificacionDocumental cdHijos3 : cdHijos2.getNodosHijos()){
												System.out.println("cdHijos3 "+cdHijos3.getNombre()+"nombreTipoDocumento "+nombreTipoDocumento);
												if(nombreTipoDocumento.equalsIgnoreCase(cdHijos3.getNombre())){
													referencia2.setClasificacionDocumental(cdHijos3);
													break;
												}
											}
											break;
										}
									}
									break;
								}
							}
							String elementoCodigo = "07"+String.format("%010d", idCaja);
							referencia2.setContenedor(elementoService.getElementoByCodigo(elementoCodigo, null, null, null,null));
							referencia2.setLoteReferencia(loteReferencia);
							referenciaService.guardar(referencia2);
						}	
					}catch(Exception ex){
						logger.error("Error en la lectura de la tabla "+documento+" - Registro Nro "+iControl+": ", ex);
					}
					iControl++;
				}	
			}
			
			mensajeClienteReg = new ScreenMessageImp("formularioImportarDatos.notificacion.okImportacion", Arrays.asList(new String[]{}));
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
			logger.error("Error en la lectura: " , ex);
			mensajeClienteReg = new ScreenMessageImp("formularioImportarDatos.notificacion.errorImportacion", Arrays.asList(new String[]{}));
		}catch(SQLException e){
			while(e!=null){
				e.printStackTrace();
				e=e.getNextException();
				logger.error("Error en la lectura: " , e);
			}
			mensajeClienteReg = new ScreenMessageImp("formularioImportarDatos.notificacion.errorImportacion", Arrays.asList(new String[]{}));
		}
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
		atributos.put("errores", false);
		atributos.remove("result");
		atributos.put("hayAvisos", true);
		atributos.put("avisos", avisos);
		return inicioFormularioImportarDatos(atributos, request);
	}
	
	public class migracionReferencia{
		public String nombreSucursal;
		public String nombreTipoDocumento;
		
		public String getNombreSucursal() {
			return nombreSucursal;
		}
		public void setNombreSucursal(String nombreSucursal) {
			this.nombreSucursal = nombreSucursal;
		}
		public String getNombreTipoDocumento() {
			return nombreTipoDocumento;
		}
		public void setNombreTipoDocumento(String nombreTipoDocumento) {
			this.nombreTipoDocumento = nombreTipoDocumento;
		}
	}
	
	private ClasificacionDocumental crearClasificacionDoc(){
		ClasificacionDocumental clasificacionDocumental = new ClasificacionDocumental();
		clasificacionDocumental.setClienteAsp(clienteAspService.obtenerPorId(4));
		clasificacionDocumental.setIndiceIndividual(true);
		clasificacionDocumental.setIndiceGrupal(true);
		clasificacionDocumental.setIndividualNumero1Seleccionado(true);
		clasificacionDocumental.setIndividualNumero2Seleccionado(true);
		clasificacionDocumental.setIndividualFecha1Seleccionado(true);
		clasificacionDocumental.setIndividualFecha2Seleccionado(true);
		clasificacionDocumental.setIndividualNumero1Titulo("NUMERO 1");
		clasificacionDocumental.setIndividualNumero2Titulo("NUMERO 2");
		clasificacionDocumental.setIndividualFecha1Titulo("FECHA 1");
		clasificacionDocumental.setIndividualFecha2Titulo("FECHA 2");
		clasificacionDocumental.setIndividualTexto1Titulo("TEXTO 1");
		clasificacionDocumental.setIndividualTexto2Titulo("TEXTO 2");
		clasificacionDocumental.setGrupalNumeroSeleccionado(true);
		clasificacionDocumental.setGrupalFechaSeleccionado(true);
		clasificacionDocumental.setGrupalTextoSeleccionado(true);
		clasificacionDocumental.setGrupalNumero1Titulo("Nro.Desde");
		clasificacionDocumental.setGrupalNumero2Titulo("Nro.Hasta");
		clasificacionDocumental.setGrupalFecha1Titulo("F.Desde");
		clasificacionDocumental.setGrupalFecha2Titulo("F.Hasta");
		clasificacionDocumental.setGrupalTexto1Titulo("TEXTO 1");
		clasificacionDocumental.setGrupalTexto2Titulo("TEXTO 2");
		clasificacionDocumental.setDescripcionRequerido(false);
		clasificacionDocumental.setDescripcionSeleccionado(true);
		clasificacionDocumental.setGrupalFechaSeleccionado(false);
		clasificacionDocumental.setGrupalNumeroRequerido(false);
		clasificacionDocumental.setGrupalTextoRequerido(false);
		clasificacionDocumental.setIndividualFecha1Requerido(false);
		clasificacionDocumental.setIndividualFecha2Requerido(false);
		clasificacionDocumental.setIndividualNumero1Requerido(false);
		clasificacionDocumental.setIndividualNumero2Requerido(false);
		clasificacionDocumental.setIndividualTexto1Requerido(false);
		clasificacionDocumental.setIndividualTexto2Requerido(false);
		clasificacionDocumental.setNodosHijos(new HashSet<ClasificacionDocumental>());
		return clasificacionDocumental;
	}

	/**
	 * Retorna un preparedStatement para la consulta sql que recibe por
	 * parámetros. Como los PreparedStatements son óptimos para consultas
	 * repetitivas, siempre debería retornar el mismo. Este método retorna el
	 * mismo objeto PreparedStatement para dos consultas sql iguales.
	 * 
	 * @param sql
	 *            La consulta
	 * @return el PreparedStatement para esa consulta, listo para comenzar a
	 *         setear los valores.
	 * @throws SQLException
	 */
	public static PreparedStatement getPreparedStatement(String sql)
			throws SQLException {
		PreparedStatement prep = preparedStatements.get(sql);
		if (prep == null) {
			prep = con.prepareStatement(sql);
			preparedStatements.put(sql, prep);
		} else {
			prep.clearParameters();
		}
		return prep;
	}

	

}
