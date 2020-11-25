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
import com.security.modelo.configuraciongeneral.Seccion;

/**
 * @author Gonzalo Noriega
 *
 */
public interface SeccionService extends GeneralServiceInterface<Seccion>{
	public Seccion verificarSeccion(Seccion seccion, ClienteAsp cliente);
	public List<Seccion> listarSeccionFiltradas(Seccion seccion, ClienteAsp cliente);
	public Seccion getByCodigo(String codigo);
	public Boolean guardarSeccion(Seccion seccion);
	public Boolean actualizarSeccion(Seccion seccion);
	public Boolean eliminarSeccion(Seccion seccion);
	public List<Seccion> listarSeccionPopup(String val, String codigoDeposito, ClienteAsp cliente);
	public Seccion getByCodigo(Seccion seccion, ClienteAsp clienteAsp);
}
