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
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.GrupoFacturacion;

/**
 * @author Gonzalo Noriega
 *
 */
public interface GrupoFacturacionService extends GeneralServiceInterface<GrupoFacturacion>{
	public List<GrupoFacturacion> listarGrupoFacturacionFiltradas(GrupoFacturacion grupoFacturacion);
	public Boolean guardarGrupoFacturacion(GrupoFacturacion grupoFacturacion);
	public Boolean actualizarGrupoFacturacion(GrupoFacturacion grupoFacturacion);
	public Boolean eliminarGrupoFacturacion(GrupoFacturacion grupoFacturacion);
	public GrupoFacturacion verificarExistente(GrupoFacturacion grupoFacturacion);
	public List<ClienteEmp> listarGrupoFacturacionPopup(String val, ClienteAsp cliente);
}
