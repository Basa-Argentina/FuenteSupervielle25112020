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
import com.security.modelo.configuraciongeneral.Modulo;

/**
 * @author Gonzalo Noriega
 *
 */
public interface ModuloService extends GeneralServiceInterface<Modulo>{
	public List<Modulo> listarModuloFiltradas(Modulo modulo, ClienteAsp cliente,Boolean impresion);
	public Modulo getByCodigo(String codigo, ClienteAsp cliente);
	public Modulo getModuloByOffset(String codigoEstante, Integer offsetV, Integer offsetH, ClienteAsp clienteAsp);
	public Boolean guardarModulo(Modulo modulo);
	public Boolean actualizarModulo(Modulo modulo);
	public Boolean eliminarModulo(Modulo modulo);
	public Integer contarModulosFiltrados(Modulo modulo, ClienteAsp cliente);
}
