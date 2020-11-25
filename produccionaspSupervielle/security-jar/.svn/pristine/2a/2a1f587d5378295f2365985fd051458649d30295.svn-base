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
import com.security.modelo.configuraciongeneral.AgrupadorFacturacion;

/**
 * @author Gonzalo Noriega
 *
 */
public interface AgrupadorService extends GeneralServiceInterface<AgrupadorFacturacion>{
	public List<AgrupadorFacturacion> listarAgrupadorFacturacionFiltradas(AgrupadorFacturacion agrupador, ClienteAsp cliente);
	public Boolean guardarAgrupadorFacturacion(AgrupadorFacturacion agrupador);
	public Boolean actualizarAgrupadorFacturacion(AgrupadorFacturacion agrupador);
	public Boolean eliminarAgrupadorFacturacion(AgrupadorFacturacion agrupador);
	public AgrupadorFacturacion verificarExistente(AgrupadorFacturacion agrupador);
}
