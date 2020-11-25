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
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;

/**
 * @author Victor Kenis
 *
 */
public interface RemitoDetalleService extends GeneralServiceInterface<RemitoDetalle>{
	public List<RemitoDetalle> listarRemitoDetalleFiltradas(RemitoDetalle remitoDetalle, ClienteAsp cliente);
	public Boolean guardarRemitoDetalle(RemitoDetalle remitoDetalle);
	public Boolean actualizarRemitoDetalle(RemitoDetalle remitoDetalle);
	public Boolean eliminarRemitoDetalle(RemitoDetalle remitoDetalle);
	public RemitoDetalle verificarExistente(RemitoDetalle remitoDetalle, ClienteAsp cliente);
	public List<RemitoDetalle> listarRemitoDetallePorRemito(Remito remito,
			ClienteAsp cliente);
	
	
}
