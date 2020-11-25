/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 26/05/2011
 */
package com.security.accesoDatos.interfaz;

import java.util.List;

import com.security.modelo.general.Localidad;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface LocalidadService extends GeneralServiceInterface<Localidad>{
	public List<Localidad> listarLocalidades();
	public List<Localidad> listarLocalidadesPorProcinciaId(Long provinciaId);
	public List<Localidad> listarLocalidadesPopup(Long provinciaId, String val);
	public Localidad getLocalidadPorId(Long id);
	public Boolean guardarLocalidad(Localidad localidad);
	public Localidad getLocalidadPorNombre(String nombre, Long idPcia);
	public Boolean eliminarLocalidad(Localidad loc);
	public List<Localidad> buscarLocalidades(Long pciaId, String nombre, Long id);
}
