/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.jerarquias.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.jerarquias.TipoRequerimiento;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface TipoOperacionService extends GeneralServiceInterface<TipoOperacion>{
	public boolean save(TipoOperacion objeto);
	public boolean update(TipoOperacion objeto);
	public boolean delete(TipoOperacion objeto);
	public List<TipoOperacion> listarTipoOperacion(String codigo, String descripcion, TipoRequerimiento tipoRequerimiento, ClienteAsp clienteAsp);
	public boolean seRepiteCodigoTipoOperacion(TipoOperacion tipo);
	public TipoOperacion obtenerTipoOperacionPorCodigo(String codigo, ClienteAsp clienteAsp);
	public List<TipoOperacion> listarTipoOperacionPopup(String val, ClienteAsp clienteAsp);
}
