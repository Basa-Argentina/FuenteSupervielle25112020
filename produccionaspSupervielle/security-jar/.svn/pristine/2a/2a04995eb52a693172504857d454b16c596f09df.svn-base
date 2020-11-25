/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;

/**
 * @author Gonzalo Noriega
 *
 */
public interface EmpresaService extends GeneralServiceInterface<Empresa>{
	public Empresa getByCodigo(String codigo, ClienteAsp cliente);
	public Empresa verificarExistente(Empresa empresa);
	public List<Empresa> getByDescripcion(String descripcion, ClienteAsp cliente);
    public List<Empresa> listarEmpresaFiltradas(Empresa empresa, ClienteAsp cliente);
	public Boolean guardarEmpresa(Empresa empresa);
	public Boolean actualizarEmpresa(Empresa empresa);
	public Boolean eliminarEmpresa(Empresa empresa);
	public List<Empresa> listarEmpresaPopup(String descripcion, ClienteAsp cliente);
	public Empresa getPrincipal(ClienteAsp cliente);
	public Empresa getByIDConSucursales(Long id);
	public List<Empresa> listarEmpresaFiltradasConSucursales(Empresa empresa,
			ClienteAsp cliente);
	public Empresa getByID(Long id);
	public Empresa getByCodigoConDireccion(String codigo, ClienteAsp cliente);
	public Empresa getByCodigoConCondAfip(String codigo, ClienteAsp cliente);
}
