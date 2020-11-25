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
import com.security.modelo.configuraciongeneral.Grupo;

/**
 * @author Gonzalo Noriega
 *
 */
public interface GrupoService extends GeneralServiceInterface<Grupo>{
	public Grupo verificarGrupo(Grupo grupo, ClienteAsp cliente);
	public List<Grupo> listarGrupoFiltradas(Grupo grupo, ClienteAsp cliente);
	public Grupo getByCodigo(String codigo);
	public Boolean guardarGrupo(Grupo grupo);
	public Boolean actualizarGrupo(Grupo grupo);
	public Boolean eliminarGrupo(Grupo grupo);
}
