/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 15/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Serie;

/**
 * @author Victor Kenis
 *
 */
public interface RemitoService extends GeneralServiceInterface<Remito>{
	//public List<Remito> listarRemitoFiltradas(Remito remito, ClienteAsp cliente);
	public Boolean guardarRemito(Remito remito);
	public Boolean actualizarRemito(Remito remito);
	public Boolean eliminarRemito(Remito remito);
	public Boolean actualizarRemitoList(List<Remito> listRemitos);
	public Remito verificarExistente(Remito remito);
	public Boolean guardarRemitoList(List<Remito> listRemitos);
	public Remito getByNumero(Long numero, ClienteAsp cliente);
	//public Remito busquedaServlet(Remito remitoBusqueda, ClienteAsp clienteAsp);
	public List<Remito> getByNumeros(List<Long> numeros, ClienteAsp cliente);
	public Remito verificarExistenteEnSerie(Long numero, String codigoSerie,
			ClienteAsp clienteAsp);
	public String traerUltNumeroPorSerie(Serie serie, ClienteAsp cliente);
	public Boolean guardarRemitoYDetalles(Set<RemitoDetalle> remitoDetalles,
			Remito remito);
	public Boolean actualizarRemitoYDetalles(Boolean noAnexar,
			Set<RemitoDetalle> remitoDetalles, Remito remito);
	public List<Remito> listarRemitoFiltradas(Remito remito, ClienteAsp clienteAsp);
	public List<Remito> listarRemitosPorId(List<Remito> remitos, ClienteAsp clienteAsp);
	public List<Remito> listarRemitoPopup(String val, String clienteCodigo,
			ClienteAsp clienteAsp);

	public Remito getByCodigo(Long codigo, ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public List<Remito> getByIds(List<Long> ids, ClienteAsp cliente);
	public Integer contarObtenerPor(Remito remito, ClienteAsp clienteAsp);
	public List<Remito> obtenerPor(Remito remito, ClienteAsp clienteAsp,
			String fieldOrder, String sortOrder, Integer numeroPagina,
			Integer tamañoPagina);
	public List<Remito> listarRemitosPorId(Long[] listaIds, ClienteAsp clienteAsp);
	
}
