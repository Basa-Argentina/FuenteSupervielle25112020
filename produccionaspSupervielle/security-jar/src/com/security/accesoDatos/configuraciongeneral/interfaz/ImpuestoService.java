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
import com.security.modelo.configuraciongeneral.Impuesto;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface ImpuestoService extends GeneralServiceInterface<Impuesto>{
	public boolean save(Impuesto impuesto);
	public boolean update(Impuesto impuesto);
	public boolean delete(Impuesto impuesto);
	public List<Impuesto> listarImpuestos(String codigo, String descripcion, ClienteAsp cliente);
	public List<Impuesto> listarImpuestosPopup(String val, ClienteAsp cliente);
	public Impuesto obtenerPorCodigo(String codigo, ClienteAsp cliente);
}
