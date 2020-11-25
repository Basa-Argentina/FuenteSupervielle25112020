/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;

/**
 * @author Victor Kenis
 *
 */
public interface LecturaDetalleService extends GeneralServiceInterface<LecturaDetalle>{
	public List<LecturaDetalle> listarLecturaDetalleFiltradas(LecturaDetalle lecturaDetalle, ClienteAsp cliente);
	public Boolean guardarLecturaDetalle(LecturaDetalle lecturaDetalle);
	public Boolean actualizarLecturaDetalle(LecturaDetalle lecturaDetalle);
	public Boolean eliminarLecturaDetalle(LecturaDetalle lecturaDetalle);
	public LecturaDetalle verificarExistente(LecturaDetalle lecturaDetalle, ClienteAsp cliente);
	public List<LecturaDetalle> listarLecturaDetallePorLectura(Lectura lectura,
			ClienteAsp cliente);
	public List<LecturaDetalle> listarLecturaDetalleEnListaElementos(
			List<Elemento> elementos, Lectura lectura, ClienteAsp cliente);
	List<LecturaDetalle> listarOrderByOrdenLecturaDetallePorLectura(
			Lectura lectura, ClienteAsp cliente);
	
	
}
