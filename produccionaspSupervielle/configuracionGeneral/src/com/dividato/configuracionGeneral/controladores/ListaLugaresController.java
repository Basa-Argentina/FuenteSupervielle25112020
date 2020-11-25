/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.ListaPreciosValidator;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Localidad;
import com.security.modelo.general.Pais;
import com.security.modelo.general.Provincia;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * @author Victor Kenis
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarLugares.html",
				"/mostrarLugares.html",
				"/mostrarProvincias.html",
				"/mostrarLocalidades.html",
				"/mostrarBarrios.html",
				"/eliminarLugares.html",
				"/eliminarPais.html",
				"/eliminarProvincia.html",
				"/eliminarLocalidad.html",
				"/eliminarBarrio.html",
				"/filtrarLugares.html",
				"/filtrarProvincias.html",
				"/filtrarLocalidades.html",
				"/filtrarBarrios.html",
				"/guardarPais.html",
				"/guardarProvincia.html",
				"/guardarLocalidad.html",
				"/guardarBarrio.html"
			}
		)
public class ListaLugaresController {
	
	private PaisService paisService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private ListaPreciosValidator validator;
	private BarrioService barrioService;
	
	
	@Autowired
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	@Autowired
	public void setProvinciaService(ProvinciaService provinciaService) {
		this.provinciaService = provinciaService;
	}
	@Autowired
	public void setLocalidadService(LocalidadService localidadService) {
		this.localidadService = localidadService;
	}
	@Autowired
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
	}
	@Autowired
	public void setValidator(ListaPreciosValidator validator) {
		this.validator = validator;
	}
	

	@RequestMapping(value="/iniciarLugares.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){
		atributos.remove("paisBusqueda");
		atributos.remove("provinciaBusqueda");
		atributos.remove("localidadBusqueda");
		atributos.remove("barrioBusqueda");
		return "redirect:mostrarLugares.html";
	}
	
	@RequestMapping(value="/mostrarLugares.html", method = RequestMethod.GET)
	public String mostrar(
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		//buscamos en la base de datos los paises filtrados (o no) y lo cargamos a request.
		List<Pais> paises = null;	
		Pais pais = (Pais) atributos.get("paisBusqueda");
		if(pais != null)
			paises = paisService.listarPaisesPopup(pais.getNombre());
		else
			paises = paisService.listarPaises();
		atributos.put("paises", paises);
		
		if(accion== null)
			accion = "NUEVO";
		else if(accion.equalsIgnoreCase("MODIFICAR")){
			if(id!=null){
				Pais paisFormulario = paisService.obtenerPorId(id);
				atributos.put("paisFormulario", paisFormulario);
			}
		}
			
		atributos.put("accion", accion);		
		//hacemos la redireccion a la ventana
		return "consultaLugares";
	}
	
	@RequestMapping(value="/guardarPais.html", method = RequestMethod.GET)
	public String guardarPais(
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="nombre",required=false) String nombre,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean commit = null;
		
		if(accion!= null){
			if(accion.equalsIgnoreCase("NUEVO")){
				if(nombre !=null && !nombre.trim().equals("")){
					Pais existe = paisService.getPaisPorNombre(nombre);
					if(existe==null){
						Pais nuevoPais = new Pais();
						nuevoPais.setNombre(nombre);
						commit = paisService.guardarPais(nuevoPais);
					}
					else{
						ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.paisExistente", null);
						avisos.add(mensaje); //agrego el mensaje a la coleccion
						atributos.put("hayAvisosNeg", true);
					}
				}
			}else if(accion.equalsIgnoreCase("MODIFICAR")){
				if(id!=null && nombre !=null && !nombre.trim().equals("")){
					Pais pais = paisService.obtenerPorId(id);
					if(pais!=null){
						Pais existe = paisService.getPaisPorNombre(nombre);
						if(existe==null){
							pais.setNombre(nombre);
							commit = paisService.guardarPais(pais);
						}else{
							ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.paisExistente", null);
							avisos.add(mensaje); //agrego el mensaje a la coleccion
							atributos.put("hayAvisosNeg", true);
						}
					}
				}
			}
		}
		if(commit != null){
			if(commit){
				ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.avisos.paisRegistradoExito", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("hayAvisos", true);
			}else{
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("hayAvisosNeg", true);
			}
		}
		
		atributos.put("avisos", avisos);
		//hacemos la redireccion a la ventana
		return mostrar(null, null, session, atributos);
	}
	
	@RequestMapping(value="/mostrarProvincias.html", method = RequestMethod.GET)
	public String mostrarProvincias(
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="idPcia",required=false) Long idPcia,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		Pais paisSel;
		//buscamos en la base de datos las provincias filtrados (o no) y lo cargamos a request.
		Set<Provincia> pcias = null;	
		Provincia pciaBusqueda = (Provincia) atributos.get("provinciaBusqueda");
		if(pciaBusqueda != null){
			pcias = new HashSet<Provincia>(provinciaService.buscarProvincias(id, pciaBusqueda.getNombre(), pciaBusqueda.getId()));			
		}
		
		//busco el pais seleccionado
		if(id != null){
			paisSel = paisService.getPaisPorId(id);
			if(paisSel != null){
				if(pciaBusqueda!=null)
					paisSel.setProvincias(pcias);	
			}
		}else{
			paisSel = new Pais();
			if(pcias != null)
				paisSel.setProvincias(pcias);
		}
		atributos.put("paisSel", paisSel);
		
		
		if(accion== null)
			accion = "NUEVO";
		else if(accion.equalsIgnoreCase("MODIFICAR")){
			if(idPcia!=null){
				Provincia pciaFormulario = provinciaService.obtenerPorId(idPcia);
				atributos.put("pciaFormulario", pciaFormulario);
			}
		}
			
		atributos.put("accion", accion);
		
		//hacemos la redireccion a la ventana
		return "consultaProvincias";
	}
	
	@RequestMapping(value="/guardarProvincia.html", method = RequestMethod.GET)
	public String guardarProvincia(
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="nombre",required=false) String nombre,
			@RequestParam(value="idPais",required=false) Long idPais,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean commit = null;
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		if(accion!= null){
			if(accion.equalsIgnoreCase("NUEVO")){
				if(idPais!=null && nombre !=null && !nombre.trim().equals("")){
					Provincia existe = provinciaService.getProvinciaPorNombre(nombre,idPais);
					if(existe==null){
						Provincia nuevoProvincia = new Provincia();
						nuevoProvincia.setNombre(nombre);
						Pais pais = new Pais();
						pais.setId(idPais);
						nuevoProvincia.setPais(pais);
						commit = provinciaService.guardarProvincia(nuevoProvincia);
					}else{
						ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.provinciaExistente", null);
						avisos.add(mensaje); //agrego el mensaje a la coleccion
						hayAvisosNeg = true;
					}
				}
			}else if(accion.equalsIgnoreCase("MODIFICAR")){
				if(id!=null && nombre !=null && !nombre.trim().equals("")){
					Provincia provincia = provinciaService.obtenerPorId(id);
					if(provincia!=null){
						Provincia existe = provinciaService.getProvinciaPorNombre(nombre, idPais);
						if(existe==null){
							provincia.setNombre(nombre);
							commit = provinciaService.guardarProvincia(provincia);
						}else{
							ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.provinciaExistente", null);
							avisos.add(mensaje); //agrego el mensaje a la coleccion
							hayAvisosNeg = true;
						}
					}
				}
			}
		}
		if(commit != null){
			if(commit){
				ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.avisos.provinciaRegistradaExito", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				hayAvisos = true;
			}else{
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				hayAvisosNeg = true;
			}
		}
		
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("avisos", avisos);
		//hacemos la redireccion a la ventana
		return mostrarProvincias(idPais, null, null, session, atributos);
	}
	
	@RequestMapping(value="/mostrarLocalidades.html", method = RequestMethod.GET)
	public String mostrarLocalidades(
			@RequestParam(value="id",required=false) Long idPcia,
			@RequestParam(value="idLoc",required=false) Long idLoc,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		Provincia pciaSel;
		//buscamos en la base de datos las localidades filtrados (o no) y lo cargamos a request.
		Set<Localidad> localidades = null;	
		Localidad locBusqueda = (Localidad) atributos.get("localidadBusqueda");
		if(locBusqueda != null){
			localidades = new HashSet<Localidad>(localidadService.buscarLocalidades(idPcia, locBusqueda.getNombre(), locBusqueda.getId()));
		}
		
		//busco la pcia seleccionada
		if(idPcia != null){
			pciaSel = provinciaService.getProvinciaPorId(idPcia);
			if(pciaSel != null){
				if(locBusqueda!=null)
					pciaSel.setLocalidades(localidades);
			}
		}else{
			pciaSel = new Provincia();
			if(localidades != null)
				pciaSel.setLocalidades(localidades);
		}
		atributos.put("pciaSel", pciaSel);

		
		if(accion== null)
			accion = "NUEVO";
		else if(accion.equalsIgnoreCase("MODIFICAR")){
			if(idLoc!=null){
				Localidad locFormulario = localidadService.obtenerPorId(idLoc);
				atributos.put("locFormulario", locFormulario);
			}
		}
			
		atributos.put("accion", accion);
		
		//hacemos la redireccion a la ventana
		return "consultaLocalidades";
	}
	
	@RequestMapping(value="/guardarLocalidad.html", method = RequestMethod.GET)
	public String guardarLocalidad(
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="nombre",required=false) String nombre,
			@RequestParam(value="idPcia",required=false) Long idPcia,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean commit = null;
		
		if(accion!= null){
			if(accion.equalsIgnoreCase("NUEVO")){
				if(idPcia!=null && nombre !=null && !nombre.trim().equals("")){
					Localidad existe = localidadService.getLocalidadPorNombre(nombre,idPcia);
					if(existe==null){
						Localidad nuevoLocalidad = new Localidad();
						nuevoLocalidad.setNombre(nombre);
						Provincia pcia = new Provincia();
						pcia.setId(idPcia);
						nuevoLocalidad.setProvincia(pcia);
						commit = localidadService.guardarLocalidad(nuevoLocalidad);
					}else{
						ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.localidadExistente", null);
						avisos.add(mensaje); //agrego el mensaje a la coleccion
						atributos.put("hayAvisosNeg", true);
					}
				}
			}else if(accion.equalsIgnoreCase("MODIFICAR")){
				if(id!=null && nombre !=null && !nombre.trim().equals("")){
					Localidad localidad = localidadService.obtenerPorId(id);
					if(localidad!=null){
						Localidad existe = localidadService.getLocalidadPorNombre(nombre, idPcia);
						if(existe==null){
							localidad.setNombre(nombre);
							localidadService.guardarLocalidad(localidad);
						}else{
							ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.localidadExistente", null);
							avisos.add(mensaje); //agrego el mensaje a la coleccion
							atributos.put("hayAvisosNeg", true);
						}
					}
				}
			}
		}
		if(commit != null){
			if(commit){
				ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.avisos.localidadRegistradaExito", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("hayAvisos", true);
			}else{
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("hayAvisosNeg", true);
			}
		}
		atributos.put("avisos", avisos);
		//hacemos la redireccion a la ventana
		return mostrarLocalidades(idPcia, null, null, session, atributos);
	}
	
	@RequestMapping(value="/mostrarBarrios.html", method = RequestMethod.GET)
	public String mostrarBarrios(
			@RequestParam(value="id",required=false) Long idLocalidad,
			@RequestParam(value="idBarrio",required=false) Long idBarrio,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		
		Localidad localidadSel;
		//buscamos en la base de datos las localidades filtrados (o no) y lo cargamos a request.
		Set<Barrio> barrios = null;	
		Barrio barrioBusqueda = (Barrio) atributos.get("barrioBusqueda");
		if(barrioBusqueda != null){
			barrios = new HashSet<Barrio>(barrioService.buscarBarrios(idLocalidad, barrioBusqueda.getNombre(), barrioBusqueda.getId()));
		}
		
		//busco la pcia seleccionada
		if(idLocalidad != null){
			localidadSel = localidadService.getLocalidadPorId(idLocalidad);
			if(localidadSel != null){
				if(barrioBusqueda!=null)
					localidadSel.setBarrios(barrios);
			}
		}else{
			localidadSel = new Localidad();
			if(barrios != null)
				localidadSel.setBarrios(barrios);
		}
		atributos.put("localidadSel", localidadSel);
		
		//busco la pcia seleccionado
		
			

		
		
		if(accion== null)
			accion = "NUEVO";
		else if(accion.equalsIgnoreCase("MODIFICAR")){
			if(idBarrio!=null){
				Barrio barrioFormulario = barrioService.obtenerPorId(idBarrio);
				atributos.put("barrioFormulario", barrioFormulario);
			}
		}
			
		atributos.put("accion", accion);
		
		//hacemos la redireccion a la ventana
		return "consultaBarrios";
	}
	
	@RequestMapping(value="/guardarBarrio.html", method = RequestMethod.GET)
	public String guardarBarrio(
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="nombre",required=false) String nombre,
			@RequestParam(value="idLoc",required=false) Long idLoc,
			@RequestParam(value="accion",required=false) String accion,
			HttpSession session, Map<String,Object> atributos){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean commit = null;
		
		if(accion!= null){
			if(accion.equalsIgnoreCase("NUEVO")){
				if(idLoc!=null && nombre !=null && !nombre.trim().equals("")){
					Barrio existe = barrioService.getBarrioPorNombre(nombre,idLoc);
					if(existe==null){
						Barrio nuevoBarrio = new Barrio();
						nuevoBarrio.setNombre(nombre);
						Localidad loc = new Localidad();
						loc.setId(idLoc);
						nuevoBarrio.setLocalidad(loc);
						commit = barrioService.guardarBarrio(nuevoBarrio);
					}else{
						ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.barrioExistente", null);
						avisos.add(mensaje); //agrego el mensaje a la coleccion
						atributos.put("hayAvisosNeg", true);
					}
				}
			}else if(accion.equalsIgnoreCase("MODIFICAR")){
				if(id!=null && nombre !=null && !nombre.trim().equals("")){
					Barrio barrio = barrioService.obtenerPorId(id);
					if(barrio!=null){
						Barrio existe = barrioService.getBarrioPorNombre(nombre, idLoc);
						if(existe==null){
							barrio.setNombre(nombre);
							commit = barrioService.guardarBarrio(barrio);
						}else{
							ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.errores.barrioExistente", null);
							avisos.add(mensaje); //agrego el mensaje a la coleccion
							atributos.put("hayAvisosNeg", true);
						}
					}
				}
			}
		}
		if(commit != null){
			if(commit){
				ScreenMessage mensaje = new ScreenMessageImp("formularioLugares.avisos.barrioRegistradoExito", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("hayAvisos", true);
			}else{
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("hayAvisosNeg", true);
			}
		}
		atributos.put("avisos", avisos);
		//hacemos la redireccion a la ventana
		return mostrarBarrios(idLoc, null, null, session, atributos);
	}
	
	@RequestMapping(value="/filtrarLugares.html", method = RequestMethod.POST)
	public String filtrar(
			@ModelAttribute("paisBusqueda") Pais busqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);		
		if(!result.hasErrors()){
			atributos.put("paisBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(null,null, session, atributos);
	}
	
	@RequestMapping(value="/eliminarPais.html", method = RequestMethod.GET)
	public String eliminarPais(
			HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		Pais pais = paisService.obtenerPorId(id);
		//eliminamos
		commit =paisService.eliminarPais(pais);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("formularioLugares.avisos.paisEliminadoExito", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(null, null, session, atributos);
	}
	
	@RequestMapping(value="/eliminarProvincia.html", method = RequestMethod.GET)
	public String eliminarProvincia(
			HttpSession session,
			@RequestParam("id") Long id,
			@RequestParam("idPais") Long idPais,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		Provincia pcia = provinciaService.obtenerPorId(id);
		//eliminamos
		commit = provinciaService.eliminarProvincia(pcia);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("formularioLugares.avisos.provinciaEliminadaExito", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrarProvincias(idPais, null, null, session, atributos);
	}
	
	@RequestMapping(value="/eliminarLocalidad.html", method = RequestMethod.GET)
	public String eliminarLocalidad(
			HttpSession session,
			@RequestParam("id") Long id,
			@RequestParam("idPcia") Long idPcia,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		Localidad loc = localidadService.obtenerPorId(id);
		//eliminamos
		commit = localidadService.eliminarLocalidad(loc);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("formularioLugares.avisos.localidadEliminadoExito", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrarLocalidades(idPcia, null, null, session, atributos);
	}
	
	@RequestMapping(value="/eliminarBarrio.html", method = RequestMethod.GET)
	public String eliminarBarrio(
			HttpSession session,
			@RequestParam("id") Long id,
			@RequestParam("idLoc") Long idLoc,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		Barrio barrio = barrioService.obtenerPorId(id);
		//eliminamos
		commit = barrioService.eliminarBarrio(barrio);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("formularioLugares.avisos.barrioEliminadoExito", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrarBarrios(idLoc, null, null, session, atributos);
	}
	
	@RequestMapping(value="/filtrarProvincias.html", method = RequestMethod.POST)
	public String filtrarProvincias(
			@ModelAttribute("provinciaBusqueda") Provincia busqueda,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam("paisId") Long idPais){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);		
		if(!result.hasErrors()){
			atributos.put("provinciaBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarProvincias(idPais, null, null, session, atributos);
	}
	
	@RequestMapping(value="/filtrarLocalidades.html", method = RequestMethod.POST)
	public String filtrarLocalidades(
			@ModelAttribute("localidadBusqueda") Localidad busqueda,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam("pciaId") Long idPcia){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);		
		if(!result.hasErrors()){
			atributos.put("localidadBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarLocalidades(idPcia, null, null, session, atributos);
	}
	
	@RequestMapping(value="/filtrarBarrios.html", method = RequestMethod.POST)
	public String filtrarBarrios(
			@ModelAttribute("barrioBusqueda") Barrio busqueda,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam("locId") Long idLoc){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);		
		if(!result.hasErrors()){
			atributos.put("barrioBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarBarrios(idLoc, null, null, session, atributos);
	}
}
