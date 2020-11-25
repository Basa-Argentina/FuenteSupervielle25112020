/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;

/**
 * @author Gonzalo Noriega
 *
 */
public interface SucursalService extends GeneralServiceInterface<Sucursal>{
	public Sucursal verificarSucursal(Sucursal sucursal);
	public List<Sucursal> listarSucursalFiltradas(Sucursal sucursal, ClienteAsp cliente);
	public Sucursal getByCodigo(String codigo, Empresa empresa);
	public Sucursal getById(Long id);
	public Boolean guardarSucursal(Sucursal sucursal);
	public Boolean actualizarSucursal(Sucursal sucursal);
	public Boolean eliminarSucursal(Sucursal sucursal);
	public List<Sucursal> listarSucursalPopup(String val, String codigoSucursal, ClienteAsp cliente);
	public Sucursal getByCodigo(String codigo, ClienteAsp cliente);
	public Sucursal getPrincipal(String codigoEmpresa,ClienteAsp cliente);
	public Sucursal getByCodigo(String codigo, String codigoEmpresa,ClienteAsp clienteAsp);
}
