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
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.CuentaCorrienteCliente;
import com.security.modelo.configuraciongeneral.ListaPrecios;

/**
 * @author X
 *
 */
public interface CuentaCorrienteClienteService extends GeneralServiceInterface<CuentaCorrienteCliente>{
	public List<ClienteEmp> listarClienteFiltradas(ClienteEmp cliente, ClienteAsp clienteAsp);
	public Boolean guardarCliente(ClienteEmp cliente);
	public Boolean actualizarCliente(ClienteEmp cliente);
	public Boolean eliminarCliente(ClienteEmp cliente);
	public ClienteEmp verificarExistente(ClienteEmp cliente, ClienteAsp clienteAsp);
	public List<ClienteEmp> listarClientesPopup(String val, String codigoEmpresa, ClienteAsp cliente);
	public ClienteEmp getByCodigo(ClienteEmp cliente, ClienteAsp clienteAsp);
	public ClienteEmp getByCodigo(String codigo);
	public ClienteEmp getByCodigo(String codigo, String codigoEmpresa,ClienteAsp clienteAsp);
	public ClienteEmp getById(Long id, ClienteAsp clienteAsp);
	public ClienteEmp getByCodigo(String codigo, String codigoEmpresa,
			ClienteAsp cliente, Boolean habilitado);
	public List<ClienteEmp> listarClientesPopup(String val, String codigoEmpresa,
			ClienteAsp cliente, Boolean habilitado);
	public ListaPrecios listaPrecioDefectoPorCliente(String codigoCliente,
			ClienteAsp clienteAsp);
}
