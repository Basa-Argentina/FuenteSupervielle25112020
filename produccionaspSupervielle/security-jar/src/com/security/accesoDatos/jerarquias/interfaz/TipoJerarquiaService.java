/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/07/2011
 */
package com.security.accesoDatos.jerarquias.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.jerarquias.TipoJerarquia;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface TipoJerarquiaService extends GeneralServiceInterface<TipoJerarquia>{
	public boolean save(TipoJerarquia objeto);
	public boolean update(TipoJerarquia objeto);
	public boolean delete(TipoJerarquia objeto);
	public List<TipoJerarquia> listarTipoJerarquia(String codigo, String descripcion, ClienteAsp clienteAsp);
	public TipoJerarquia obtenerTipoJerarquiaPorCodigo(String codigo,
			ClienteAsp clienteAsp);
	public List<TipoJerarquia> listarTipoJerarquiaPopup(String val, ClienteAsp cliente);
	public TipoJerarquia getByCodigo(String codigo, ClienteAsp cliente);
	public TipoJerarquia getByID(Long id);
}
