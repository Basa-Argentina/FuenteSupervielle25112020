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
import com.security.modelo.configuraciongeneral.Deposito;

/**
 * @author Gonzalo Noriega
 *
 */
public interface DepositoService extends GeneralServiceInterface<Deposito>{
	public List<Deposito> listarDepositoFiltradas(Deposito deposito, ClienteAsp cliente);
	public Boolean guardarDeposito(Deposito cai);
	public Boolean actualizarDeposito(Deposito cai);
	public Boolean eliminarDeposito(Deposito cai);
	public Deposito verificarExistente(Deposito deposito, ClienteAsp cliente);
	public List<Deposito> listarDepositoPopup(String val, String codigoSucursal, ClienteAsp cliente);
	public Deposito getByCodigo(Deposito deposito, ClienteAsp clienteAsp);
	public Deposito getByCodigoYSucursal(String codigoDeposito, String codigoSucursal,
			ClienteAsp clienteAsp);
	public Deposito getByCodigo(String codigoDeposito, ClienteAsp clienteAsp);
}
