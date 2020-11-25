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
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.FacturaDetalle;

public interface FacturaDetalleService extends GeneralServiceInterface<FacturaDetalle>{
	public List<FacturaDetalle> listarFacturaDetalleFiltradas(FacturaDetalle facturaDetalle, ClienteAsp cliente);
	public Boolean guardarFacturaDetalle(FacturaDetalle facturaDetalle);
	public Boolean eliminarFacturaDetalle(FacturaDetalle facturaDetalle);
	public Boolean actualizarFacturaDetalle(FacturaDetalle facturaDetalle);
	public List<FacturaDetalle> listarFacturaDetallePorFactura(Factura factura,
			ClienteAsp cliente);
	
	
}
