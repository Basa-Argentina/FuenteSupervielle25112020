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
import com.security.modelo.configuraciongeneral.ClienteConcepto;

/**
 * @author Gonzalo Noriega
 *
 */
public interface ClienteConceptoService extends GeneralServiceInterface<ClienteConcepto>{
	public List<ClienteConcepto> listarClienteConceptoesFiltradasPorCliente(ClienteConcepto clienteConcepto, ClienteAsp cliente);
	public Boolean guardarClienteConcepto(ClienteConcepto clienteConcepto);
	public Boolean actualizarClienteConcepto(ClienteConcepto clienteConcepto);
	public Boolean eliminarClienteConcepto(ClienteConcepto clienteConcepto);
	public ClienteConcepto verificarExistente(ClienteConcepto clienteConcepto, ClienteAsp cliente);
	public List obtenerClienteConceptosEnPeriodo(String periodo, String codigoEmpresa,
			Boolean habilitado, ClienteAsp cliente);
}
