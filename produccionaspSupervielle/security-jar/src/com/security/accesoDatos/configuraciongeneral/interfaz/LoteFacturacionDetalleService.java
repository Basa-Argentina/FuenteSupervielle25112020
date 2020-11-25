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
import com.security.modelo.configuraciongeneral.LoteFacturacionDetalle;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.LoteFacturacionDetalle;

/**
 * @author Victor Kenis
 *
 */
public interface LoteFacturacionDetalleService extends GeneralServiceInterface<LoteFacturacionDetalle>{
	public List<LoteFacturacionDetalle> listarLoteFacturacionDetalleFiltradas(LoteFacturacionDetalle loteFacturacionDetalle, ClienteAsp cliente);
	public Boolean guardarLoteFacturacionDetalle(LoteFacturacionDetalle loteFacturacionDetalle);
	public Boolean actualizarLoteFacturacionDetalle(LoteFacturacionDetalle loteFacturacionDetalle);
	public Boolean eliminarLoteFacturacionDetalle(LoteFacturacionDetalle loteFacturacionDetalle);
	public LoteFacturacionDetalle verificarExistente(LoteFacturacionDetalle loteFacturacionDetalle, ClienteAsp cliente);
	public List<LoteFacturacionDetalle> listarLoteFacturacionDetallePorLoteFacturacion(LoteFacturacion loteFacturacion,
			ClienteAsp cliente);
	
	
}
