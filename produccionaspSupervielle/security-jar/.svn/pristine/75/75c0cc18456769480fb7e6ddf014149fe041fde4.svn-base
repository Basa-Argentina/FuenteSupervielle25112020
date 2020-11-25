/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/05/2011
 */
package com.security.accesoDatos.interfaz;

import java.util.Date;
import java.util.List;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.administracion.Licencia;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface LicenciaService extends GeneralServiceInterface<Licencia>{
	public List<Licencia> obtenerLicenciaPorFiltroLicencia(Licencia licencia);
	public List<Licencia> obtenerLicenciaPorFecha(Date fechaDesde, Date fechaHasta, ClienteAsp cliente);
	public Licencia obtenerLicenciaPorFechaAccesoAsp(Date fechaActual, ClienteAsp cliente);
	public Boolean guardarLicencia(Licencia licencia);
	public Boolean actualizarLicencia(Licencia licencia);
	public Boolean eliminarLicencia(Licencia licencia);
}
