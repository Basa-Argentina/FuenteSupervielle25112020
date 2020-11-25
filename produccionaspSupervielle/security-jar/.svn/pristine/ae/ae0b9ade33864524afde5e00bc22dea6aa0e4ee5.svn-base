/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/07/2011
 */
package com.security.accesoDatos.jerarquias.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.TipoRequerimiento;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface TipoRequerimientoService extends GeneralServiceInterface<TipoRequerimiento>{
	public boolean save(TipoRequerimiento objeto);
	public boolean update(TipoRequerimiento objeto);
	public boolean delete(TipoRequerimiento objeto);
	public List<TipoRequerimiento> listarTipoRequerimiento(String codigo, String descripcion, Integer plazo, ClienteAsp clienteAsp);
	public boolean seRepiteCodigoTipoRequerimiento(TipoRequerimiento tipo, ClienteAsp clienteAsp);
	public TipoRequerimiento obtenerPorCodigo(String codigo, ClienteAsp clienteAsp);
	public List<TipoRequerimiento> listarTipoRequerimientoPopup(String val, ClienteAsp clienteAsp);
}
