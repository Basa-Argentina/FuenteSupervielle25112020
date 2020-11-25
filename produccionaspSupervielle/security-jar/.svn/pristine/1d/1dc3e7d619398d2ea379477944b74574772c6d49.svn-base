/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;

/**
 * @author Victor Kenis
 *
 */
public interface LecturaService extends GeneralServiceInterface<Lectura>{
	public Lectura verificarLectura(Lectura lectura);
	public List<Lectura> listarLecturaFiltradas(Lectura lectura, ClienteAsp cliente);
	public Boolean guardarLectura(Lectura lectura);
	public Boolean actualizarLectura(Lectura lectura);
	public Lectura obtenerPorCodigo(Long codigo, ClienteAsp clienteAsp);
	public List<Lectura> listarLecturaPopup(String val, ClienteAsp clienteAsp, Empresa empresa);
	public Boolean guardarLecturaYDetalles(Set<LecturaDetalle> lecturaDetalles,
			Lectura lectura);
	public Lectura obtenerPorCodigo(Long codigo, Boolean utilizada, Empresa empresa, ClienteAsp clienteAsp);
	public Long traerUltCodigoPorClienteAsp(ClienteAsp cliente);
	public Boolean actualizarLecturaYDetalles(Boolean noAnexar,
			Set<LecturaDetalle> lecturaDetalles, Lectura lectura);
	public List<Lectura> listarLecturaPopup(String val, ClienteAsp clienteAsp,
			Empresa empresa, Boolean utilizada);
	public Boolean eliminarLectura(Lectura lectura, List<LecturaDetalle> detalles);
	public List<Lectura> listarLecturaFiltradasPorSQL(Lectura lectura,
			ClienteAsp cliente);
}
