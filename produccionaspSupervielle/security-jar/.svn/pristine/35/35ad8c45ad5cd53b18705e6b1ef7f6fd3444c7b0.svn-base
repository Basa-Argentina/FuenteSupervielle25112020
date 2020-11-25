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
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;

/**
 * @author Gonzalo Noriega
 *
 */
public interface ClienteDireccionService extends GeneralServiceInterface<ClienteDireccion>{
	public List<ClienteDireccion> listarClienteDireccionesFiltradasPorCliente(ClienteDireccion clienteDireccion, ClienteAsp cliente);
	public Boolean guardarClienteDireccion(ClienteDireccion clienteDireccion);
	public Boolean actualizarClienteDireccion(ClienteDireccion clienteDireccion);
	public Boolean eliminarClienteDireccion(ClienteDireccion clienteDireccion);
	public ClienteDireccion verificarExistente(ClienteDireccion clienteDireccion, ClienteAsp cliente);
	public List<ClienteDireccion> listarPorId(Long[] ids, ClienteAsp cliente);
	//public List<ClienteDireccion> listarDireccionesPopup(String val, String clienteCodigo, ClienteAsp cliente);
	public ClienteDireccion getByCodigo(String codigo, ClienteAsp cliente);
	public List<ClienteDireccion> listarDireccionesPopup(String val, ClienteEmp clienteEmp, ClienteAsp cliente);
	public ClienteDireccion obtenerPorCodigo(String codigo, String codigoCliente,
			ClienteAsp cliente);
	public ClienteDireccion obtenerPorCodigo(String codigo, ClienteEmp clienteEmp,
			ClienteAsp cliente);
	public ClienteDireccion getClienteDireccionById(Long id);

}
