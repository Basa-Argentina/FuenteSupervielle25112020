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
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.jerarquias.Jerarquia;
import com.security.modelo.jerarquias.TipoJerarquia;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface JerarquiaService extends GeneralServiceInterface<Jerarquia>{
	public boolean save(Jerarquia objeto);
	public boolean update(Jerarquia objeto);
	public boolean delete(Jerarquia objeto);
	public List<Jerarquia> listarJerarquiaPorTipoJerarquia(TipoJerarquia tipo, ClienteAsp clienteAsp);
	public boolean seSuperPonenJerarquias(Jerarquia j, TipoJerarquia tipo, ClienteAsp clienteAsp);
	public boolean seRepiteValoracion(Jerarquia j, TipoJerarquia tipo, ClienteAsp clienteAsp);
	public boolean seRepiteDescripcion(Jerarquia j, TipoJerarquia tipo, ClienteAsp clienteAsp);
	public Jerarquia traerValoracionDePosicion(Posicion posicionLibre,
			TipoJerarquia tipo, ClienteAsp clienteAsp);
	public List<Jerarquia> listarJerarquiasPopup(String val,
			String codigoTipoJerarquia, ClienteAsp cliente);
	public Jerarquia obtenerJerarquiaPorCodigo(String codigo,
			String codigoTipoJerarquia, ClienteAsp clienteAsp);
}
