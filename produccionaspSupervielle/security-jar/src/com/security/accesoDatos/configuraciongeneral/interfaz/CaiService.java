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
import com.security.modelo.configuraciongeneral.Cai;

/**
 * @author Gonzalo Noriega
 *
 */
public interface CaiService extends GeneralServiceInterface<Cai>{
	public List<Cai> listarCaiFiltradas(Cai cai, ClienteAsp cliente);
	public Boolean guardarCai(Cai cai);
	public Boolean actualizarCai(Cai cai);
	public Boolean eliminarCai(Cai cai);
	public Cai verificarExistente(Cai cai, ClienteAsp cliente);
	public List<Cai> listarCaiNoVencidas(Cai cai, ClienteAsp cliente);
}
