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
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.GrupoFactDetalle;

/**
 * @author Gonzalo Noriega
 *
 */
public interface GrupoFactDetalleService extends GeneralServiceInterface<GrupoFactDetalle>{
	public List<GrupoFactDetalle> listarGrupoFactDetalleFiltradas(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente);
	public Boolean guardarGrupoFactDetalle(GrupoFactDetalle grupoFactDetalle);
	public Boolean actualizarGrupoFactDetalle(GrupoFactDetalle grupoFactDetalle);
	public Boolean eliminarGrupoFactDetalle(GrupoFactDetalle grupoFactDetalle);
	public GrupoFactDetalle verificarExistente(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente);
	public List<ClienteDireccion> listarGrupoFactDetalleDirecciones(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente);
	public List<Empleado> listarGrupoFactDetalleEmpleados(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente);
}
