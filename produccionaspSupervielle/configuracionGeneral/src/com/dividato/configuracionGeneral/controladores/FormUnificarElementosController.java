/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaHistoricoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.EAN13;
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
				"/iniciarUnificarElementos.html",
				"/mostrarUnificarElementos.html",
				"/procesarUnificarElementos.html"
			}
		)
public class FormUnificarElementosController {
	private ReferenciaService referenciaService;
	private ElementoService elementoService;
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}

	@RequestMapping(value="/iniciarUnificarElementos.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){
		
		
		
		return "redirect:mostrarUnificarElementos.html";
	}
	
	@RequestMapping(value="/mostrarUnificarElementos.html", method = RequestMethod.GET)
	public String mostrar(HttpSession session, Map<String,Object> atributos){
				
		
		return "formularioUnificarElementos";
	}
	
	@RequestMapping(value="/procesarUnificarElementos.html", method = RequestMethod.GET)
	public String procesar(HttpSession session, Map<String,Object> atributos,
			@RequestParam(value="listaCodigos",required=false)String listaCodigos){
		
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		List<Elemento> listaElementos = new ArrayList<Elemento>();
		
		
		if(listaCodigos!=null && listaCodigos.length()>0)
			validarListaCodigos(session, listaCodigos, atributos, avisos, listaElementos);
		
		atributos.put("listaCodigos", listaCodigos);
		
		if(listaElementos!=null && listaElementos.size()>0){
			for(Elemento elemento: listaElementos){
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_UNIFICADO);
			}
			commit = elementoService.actualizarElementoList(listaElementos);
			
			if(commit!=null && commit){
				//Genero las notificaciones 
				ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioUnificarElementos.notificacion.unificacionExito", null);
				avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", true);
				atributos.put("listaCodigos", listaCodigos);
			}else{
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
			}
		}
		
		atributos.put("avisos", avisos);
		
		return mostrar(session, atributos);
	}	
	
	private void validarListaCodigos(HttpSession session, String cadenaCodigosElementos,Map<String,Object> atributos, List<ScreenMessage> avisos, List<Elemento> listaElementos)
	{
		ScreenMessage mensaje = null;
		Boolean hayAvisos = false;
		Referencia ref;
		String[] listaCodigos = cadenaCodigosElementos.split("\\,");
		List<String> listaDescartados = new ArrayList<String>();
		List<String> codigosCorrectos = new ArrayList<String>();
		Boolean banderaModulos = false, banderaInexistentes = false, banderaNoGuarda = false, banderaNoReferenciado = false,banderaRepetido = false, repetido = false, noValido = false;
		String codigo,codigoCorrecto,codigoTomado12;
		
		for (int i = 0; i < listaCodigos.length; i++) 
		{
			ref = null;
			repetido = false;
			noValido = false;
			banderaModulos = false;
			banderaInexistentes = false;
			banderaNoGuarda = false;
			banderaNoReferenciado = false;
			codigo = listaCodigos[i];
			
			
			if(codigo.length() == 13 || codigo.length() == 12)
			{	
				codigoTomado12 = listaCodigos[i].substring(0, 12);
				//codigoCorrecto = codigoTomado12 + String.valueOf(EAN13.EAN13_CHECKSUM(codigoTomado12));
				
				if(!codigoTomado12.startsWith("0")){
					codigoTomado12 = "0" +codigoTomado12;
					codigoTomado12 = codigoTomado12.substring(0,12);
				}
				
				Elemento elemento = null;		
				
				if(!codigo.startsWith("02")) 
				{
					noValido = true;
					break;						
				}
				else
				{
					elemento = elementoService.getByCodigo(codigoTomado12,obtenerClienteAspUser());
					if(elemento==null)
					{
						banderaInexistentes=true;
						break;
					}else
					{
						if(!elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_GUARDA) 
								&& !elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_CREADO)){
							banderaNoGuarda = true;
							break;
						}else{
							ref = referenciaService.obtenerByElemento(elemento);
							if(ref==null){
								banderaNoReferenciado = true;
								break;
							}
						}
					}
				}
				
				if(noValido==false && elemento!=null)
				{
					
					
					if(codigosCorrectos.size()>=1)
					{
						for(int f=0;f<codigosCorrectos.size();f++)
						{
							if(codigoTomado12.equals(codigosCorrectos.get(f)))
							{
								banderaRepetido = true;
								repetido = true;
								break;
							}
						}
					}
					else
					{
						codigosCorrectos.add(codigoTomado12);
					}
					
					if(!repetido)
					{
						listaElementos.add(elemento);
					}
				}
				
			}
			else
			{
				noValido = true;
				break;					
			}
		}
		
		if(noValido)
		{
			//Se avisa que uno o mas etiquetas fueron descartados de la lectura
			mensaje = new ScreenMessageImp("formularioUnificarElementos.notificacion.etiquetaNoValida", null);
			avisos.add(mensaje);
			hayAvisos = true;
			
		}
		if(banderaInexistentes)
		{
			//Se avisa que uno o mas elementos inexistentes fueron descartados de la lectura
			mensaje = new ScreenMessageImp("formularioUnificarElementos.notificacion.elementoInexistente", null);
			avisos.add(mensaje);
			hayAvisos = true;
		}
		if(banderaNoGuarda)
		{
			//Se avisa que uno o mas elementos no estan En Guarda
			mensaje = new ScreenMessageImp("formularioUnificarElementos.notificacion.elementosNoEnGuarda", null);
			avisos.add(mensaje);
			hayAvisos = true;
		}
		if(banderaNoReferenciado)
		{
			//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
			mensaje = new ScreenMessageImp("formularioUnificarElementos.notificacion.elementosNoReferenciados", null);
			avisos.add(mensaje);
			hayAvisos = true;
		}
		if(banderaRepetido)
		{
			//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
			mensaje = new ScreenMessageImp("formularioUnificarElementos.notificacion.elementosRepetidos", null);
			avisos.add(mensaje);
			hayAvisos = true;
		}
		
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
