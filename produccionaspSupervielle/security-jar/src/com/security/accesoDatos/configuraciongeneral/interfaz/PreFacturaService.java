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
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;
import com.security.modelo.configuraciongeneral.PreFactura;

/**
 * @author Victor Kenis
 *
 */
public interface PreFacturaService extends GeneralServiceInterface<PreFactura>{

	public Boolean guardarPlantillaFacturacionYDetalles(
			Set<PlantillaFacturacionDetalle> plantillaFacturacionDetalles,
			PlantillaFacturacion plantillaFacturacion);

	public Integer contarObtenerPor(ClienteAsp cliente, String clienteCodigo,
			String codigoSerie, String listaPreciosCodigo,
			Long tipoComprobanteId, Boolean habilitado);

	public List<PlantillaFacturacion> obtenerPor(ClienteAsp cliente,
			String clienteCodigo, String codigoSerie,
			String listaPreciosCodigo, Long tipoComprobanteId,
			Boolean habilitado, String fieldOrder, String sortOrder,
			Integer numeroPagina, Integer tamañoPagina);

	public List<PreFactura> listarPreFacturasPorLoteFacturacion(
			LoteFacturacion loteFacturacion, ClienteAsp cliente);

	public Boolean eliminarPreFactura(PreFactura preFactura);
	
}
