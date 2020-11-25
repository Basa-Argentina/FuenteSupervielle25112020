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
import com.security.modelo.configuraciongeneral.PreFacturaDetalle;

/**
 * @author Victor Kenis
 *
 */
public interface PreFacturaDetalleService extends GeneralServiceInterface<PreFacturaDetalle>{

	public Boolean guardarActualizarPlantillaFacturacionDetalle(
			PlantillaFacturacionDetalle plantillaDetalle);

	public List<PreFacturaDetalle> listarPreFacturaDetallesPorPreFactura(
			Long idPreFactura, ClienteAsp cliente);
	
}
