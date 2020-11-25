/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;

/**
 * @author Victor Kenis
 *
 */
public interface PlantillaFacturacionService extends GeneralServiceInterface<PlantillaFacturacion>{

	public Boolean guardarPlantillaFacturacionYDetalles(
			Set<PlantillaFacturacionDetalle> plantillaFacturacionDetalles,
			PlantillaFacturacion plantillaFacturacion);

	public Long contarObtenerPor(ClienteAsp cliente, String clienteCodigo,
			String codigoSerie, String listaPreciosCodigo,
			Long tipoComprobanteId, Boolean habilitado);

	public List<PlantillaFacturacion> obtenerPor(ClienteAsp cliente,
			String clienteCodigo, String codigoSerie,
			String listaPreciosCodigo, Long tipoComprobanteId,
			Boolean habilitado, String fieldOrder, String sortOrder,
			Integer numeroPagina, Integer tamañoPagina);

	public Boolean eliminarPlantillaFacturacion(
			PlantillaFacturacion plantillaFacturacion);

	public List<PlantillaFacturacion> listarPlantillasClientesEnPeriodo(
			String periodo, String codigoEmpresa, Boolean habilitado,
			ClienteAsp cliente);
	
}
