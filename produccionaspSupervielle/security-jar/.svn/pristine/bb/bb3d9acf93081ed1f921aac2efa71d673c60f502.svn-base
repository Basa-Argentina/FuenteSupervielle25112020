/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.interfaz;

import java.util.List;

import com.security.modelo.administracion.ClienteAsp;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface ClienteAspService extends GeneralServiceInterface<ClienteAsp>{
	public List<ClienteAsp> getByPersona(String persona, String contacto);
	public boolean guardarNuevoCliente(ClienteAsp cliente);
	public boolean actualuzarCliente(ClienteAsp cliente);
	public ClienteAsp getByNombreAbreviado(String nombreAbrev);
}
