/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 26/05/2011
 */
package com.security.accesoDatos.interfaz;

import java.util.List;

import com.security.modelo.general.Barrio;
import com.security.modelo.general.Localidad;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface BarrioService extends GeneralServiceInterface<Barrio>{
	public List<Barrio> listarBarriosPorLocalidadId(Long localidadId);
	public List<Barrio> listarBarriosPopup(Long localidadId, String val);
	public Barrio obtenerPorNombreLocalidad(String nombreLocalidad);
	public Boolean guardarBarrio(Barrio barrio);
	public Barrio getBarrioPorNombre(String nombre, Long idLocalidad);
	public Boolean eliminarBarrio(Barrio barrio);
	public List<Barrio> buscarBarrios(Long locId, String nombre, Long id);
}
