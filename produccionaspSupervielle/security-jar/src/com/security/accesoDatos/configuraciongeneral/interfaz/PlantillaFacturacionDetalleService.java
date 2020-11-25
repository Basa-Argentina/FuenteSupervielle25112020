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
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;

/**
 * @author Victor Kenis
 *
 */
public interface PlantillaFacturacionDetalleService extends GeneralServiceInterface<PlantillaFacturacionDetalle>{

	public Boolean guardarActualizarPlantillaFacturacionDetalle(
			PlantillaFacturacionDetalle plantillaDetalle);

	public List<PlantillaFacturacionDetalle> listarPlantillaDetallesPorPlantilla(
			Long idPlantilla, ClienteAsp cliente);
	
}
