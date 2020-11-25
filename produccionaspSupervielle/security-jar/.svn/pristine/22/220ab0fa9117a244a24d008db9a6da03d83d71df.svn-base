/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.configuraciongeneral.StockAcumulado;
import com.security.modelo.configuraciongeneral.StockResumen;

/**
 * @author Gonzalo Noriega
 *
 */
public interface StockService extends GeneralServiceInterface<Stock>{
	public boolean saveList(List<Stock> listStock);
	public boolean save(Stock stock);
	public boolean update(Stock stock);
	public boolean delete(Stock stock);
	public List<StockResumen> groupStock(Stock stock, ClienteAsp cliente);
	public List<Stock> listarStockFiltrados(Stock stock, ClienteAsp cliente);
	public Stock verificarExistente(Stock stock, ClienteAsp cliente);
	public Boolean resumirMovimientosStock(Stock stock, ClienteAsp cliente);
	public List<StockAcumulado> listarStockAcumulado(Stock stock, ClienteAsp cliente);
}
