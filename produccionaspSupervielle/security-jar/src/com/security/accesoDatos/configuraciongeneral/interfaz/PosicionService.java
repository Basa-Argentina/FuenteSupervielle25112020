/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 15/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.TipoElemento;

/**
 * @author Gonzalo Noriega
 *
 */
public interface PosicionService extends GeneralServiceInterface<Posicion>{
	public List<Posicion> listarPosicionFiltradas(Posicion posicion, ClienteAsp cliente);
	public Posicion getByCodigo(String codigo, ClienteAsp cliente);
	public Boolean guardarPosicion(Posicion posicion);
	public Boolean actualizarPosicion(Posicion posicion);
	public Boolean eliminarPosicion(Posicion posicion);
	public Boolean actualizarPosicionList(List<Posicion> listPosiciones);
	/**
	 * Devuelve una lista con todas las posiciones del modulo pasado como parametro.<br>
	 * La lista esta ordenada por verticales (V) ascendentemente y luego por horizontales (H) ascendentemente,<br>
	 * por ej, para un modulo de 3 verticales por 2 horizontales y offset = (0,0) la lista se ordena:<br>
	 * 1- (V,H) = (1,1)<br>
	 * 1- (V,H) = (1,2)<br>
	 * 1- (V,H) = (2,1)<br>
	 * 1- (V,H) = (2,2)<br>
	 * 1- (V,H) = (3,1)<br>
	 * 1- (V,H) = (3,2)<br><br>
	 * NOTA:recordar que verticales hace referencia a columnas y horizontales a filas.<br>
	 * 
	 * @param modulo
	 * @param clienteAsp
	 * @return
	 */
	public List<Posicion> getPosicionesPorModuloParaReposicionamiento(Modulo modulo, ClienteAsp clienteAsp);	
	public List<Posicion> listarPosicionFiltradas(Posicion posicion,
			ClienteAsp cliente, String estado);
	public Boolean verificarEstadoPosiciones(List<Posicion> posiciones,
			ClienteAsp cliente, String estado);
	public Integer contarPosicionFiltradas(Posicion posicion, ClienteAsp cliente);
	public List<Posicion> traerPosicionesPorSQL(Posicion posicion,
			ClienteAsp clienteAsp);
	public List<Posicion> traerPosicionesLibresPorSQL(Posicion posicion,
			ClienteAsp clienteAsp, TipoElemento tipo);
	
	
}
