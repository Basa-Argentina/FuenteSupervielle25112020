/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.accesoDatos.facturacion.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.facturacion.EmailCliente;

/**
 * @author Gonzalo Noriega
 *
 */
public interface ClienteEmailService extends GeneralServiceInterface<EmailCliente>{
	public List<ClienteEmp> listarClienteFiltradas(ClienteEmp cliente, ClienteAsp clienteAsp);
	public Boolean guardarCliente(EmailCliente cliente);
	public Boolean actualizarCliente(EmailCliente cliente);
	public Boolean eliminarCliente(EmailCliente cliente);
	public ClienteEmp verificarExistente(ClienteEmp cliente, ClienteAsp clienteAsp);
	public List<ClienteEmp> listarClientesPopup(String val, String codigoEmpresa, ClienteAsp cliente);
	public ClienteEmp getByCodigo(ClienteEmp cliente, ClienteAsp clienteAsp);
	public ClienteEmp getByCodigo(String codigo);
	public ClienteEmp getByCodigo(String codigo, String codigoEmpresa,ClienteAsp clienteAsp);
	public EmailCliente getById(Long id);
	public ClienteEmp getByCodigo(String codigo, String codigoEmpresa,
			ClienteAsp cliente, Boolean habilitado);
	public List<ClienteEmp> listarClientesPopup(String val, String codigoEmpresa,
			ClienteAsp cliente, Boolean habilitado);
	public ListaPrecios listaPrecioDefectoPorCliente(String codigoCliente,
			ClienteAsp clienteAsp);
	public EmailCliente getByID(Long id);
	public ClienteEmp getClienteConListaDefectoByCodigo(String codigo, String codigoEmpresa, ClienteAsp cliente);
	public ClienteEmp getByCodigoFactura(ClienteEmp cliente, ClienteAsp clienteAsp);
	public ClienteEmp getByIdConDireccion(Long clienteId);
	List<EmailCliente> listarClienteEmailFiltradas(EmailCliente cliente);
	public List<EmailCliente> listarClienteEmailPopup(String val, ClienteAsp obtenerClienteAspEmpleado);
}
