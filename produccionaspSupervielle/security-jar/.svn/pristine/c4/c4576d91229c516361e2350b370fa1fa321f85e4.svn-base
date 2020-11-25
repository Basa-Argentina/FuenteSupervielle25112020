/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;

/**
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (11/08/2011)
 *	
 */
public interface SerieService extends GeneralServiceInterface<Serie>{
	public Serie verificarSerie(Serie serie);
	public List<Serie> listarSerieFiltradas(Serie serie, ClienteAsp cliente);
	public Boolean guardarSerie(Serie serie);
	public Boolean actualizarSerie(Serie serie);
	public Boolean eliminarSerie(Serie serie);
	public Serie obtenerPorCodigo(String codigo ,ClienteAsp clienteAsp);
	public Serie obtenerPorCodigo(String codigo, String tipoSerie ,ClienteAsp clienteAsp);
	public List<Serie> listarSeriePopup(String val, String tipoSerie,
			String condIvaClientes, ClienteAsp clienteAsp, Empresa empresa);
	public Serie obtenerPorCodigo(String codigo, String tipoSerie,
			String codigoEmpresa, ClienteAsp clienteAsp);
	public List<Serie> listarSerieFiltradasPopup(Serie serie,String val, ClienteAsp clienteAsp);
	public Serie obtenerPorCodigo(String codigo, String tipoSerie,
			String codigoEmpresa, Boolean habilitado, ClienteAsp clienteAsp);
	public List<Serie> listarSeriePopup(String val, String tipoSerie,
			String condIvaClientes, ClienteAsp clienteAsp, Empresa empresa,
			Boolean habilitado);
	public List<Serie> listarSerieFiltradasPopup(Serie serie, ClienteAsp cliente);
	public Serie obtenerSerieFiltradaServlet(Serie serie, ClienteAsp clienteAsp);
	public Serie getByID(Long id);
}
