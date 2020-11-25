/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 26/05/2011
 */
package com.security.accesoDatos.interfaz;

import java.util.List;

import com.security.modelo.general.Pais;
import com.security.modelo.general.Provincia;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface ProvinciaService extends GeneralServiceInterface<Provincia>{
	public List<Provincia> listarProvincias();
	public List<Provincia> listarProvinciasPorPaisId(Long paisId);
	public List<Provincia> listarProvinciasPopup(Long paisId, String val);
	public Provincia getProvinciaPorId(Long id);
	public Boolean guardarProvincia(Provincia provincia);
	public Provincia getProvinciaPorNombre(String nombre, Long idPais);
	public Boolean eliminarProvincia(Provincia pcia);
	public List<Provincia> buscarProvincias(Long paisId, String nombre, Long id);
}
