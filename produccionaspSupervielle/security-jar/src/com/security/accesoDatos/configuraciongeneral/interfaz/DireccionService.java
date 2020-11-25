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
import com.security.modelo.configuraciongeneral.Direccion;

/**
 * @author Gonzalo Noriega
 *
 */
public interface DireccionService extends GeneralServiceInterface<Direccion>{
	public List<Direccion> listarTodosDireccionFiltrados(Direccion direccion);
	public Boolean guardarDireccion(Direccion direccion);
	public Boolean actualizarDireccion(Direccion direccion);
	public Boolean eliminarDireccion(Direccion direccion);
}
