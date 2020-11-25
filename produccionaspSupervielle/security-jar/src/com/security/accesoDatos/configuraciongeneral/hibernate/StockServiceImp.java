/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.StockService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.configuraciongeneral.StockAcumulado;
import com.security.modelo.configuraciongeneral.StockResumen;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class StockServiceImp extends GestorHibernate<Stock> implements StockService{
	private static Logger logger=Logger.getLogger(StockServiceImp.class);
	
	@Autowired
	public StockServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Stock> getClaseModelo() {
		return Stock.class;
	}

	@Override
	public boolean delete(Stock stock) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(stock);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Eliminar");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}

	@Override
	public boolean save(Stock stock) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(stock);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Guardar");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@Override
	public boolean saveList(List<Stock> listStock) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			for(Stock save : listStock){
				//guardamos el objeto
				session.save(save);
				session.flush();
				session.clear();
			}
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Guardar");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	

	@Override
	public boolean update(Stock stock) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(stock);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Actualizar");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	private void rollback(Transaction tx, Exception e, String mensaje){
		//si ocurre algún error intentamos hacer rollback
		if (tx != null && tx.isActive()) {
			try {
				tx.rollback();
	        } catch (HibernateException e1) {
	        	logger.error("no se pudo hacer rollback "+getClaseModelo().getName(), e1);
	        }
	        logger.error(mensaje+" "+getClaseModelo().getName(), e);
		}
	}
	
	
	@Override
	public List<StockResumen> groupStock(Stock stock, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("deposito", "dep");
        	if(stock != null){
        		if(stock.getCodigoDeposito() != null){
        			crit.add(Restrictions.eq("dep.codigo", stock.getCodigoDeposito()));
        		}
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	crit.setProjection( Projections.projectionList()
        	        .add( Projections.sum("cantidad"), "cantidad")
        	        .add( Projections.groupProperty("deposito"), "deposito")
        	        .add( Projections.groupProperty("clienteAsp"))
        	        .add( Projections.groupProperty("concepto"), "concepto")
        	    );
        	//CREAR UNA CLASE RESUMEN
        	crit.setResultTransformer(new AliasToBeanResultTransformer(StockResumen.class));
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo realizar la agrupacion de Stock ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public List<Stock> listarStockFiltrados(Stock stock, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("concepto", "con");
        	crit.createCriteria("deposito", "dep");
        	if(stock != null){
        		if(stock.getCodigoDeposito() != null)
        			crit.add(Restrictions.eq("dep.codigo", stock.getCodigoDeposito()));
        		if(stock.getCodigoConcepto() != null)
        			crit.add(Restrictions.eq("con.codigo", stock.getCodigoConcepto()));
        	}
        	if(cliente != null){
    			crit.add(Restrictions.eq("clienteAsp", cliente));
    			crit.add(Restrictions.eq("con.clienteAsp", cliente));
    			crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
    		}
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo realizar la agrupacion de Stock ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public List<StockAcumulado> listarStockAcumulado(Stock stock, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			String sql = "SELECT this_.id as id, this_.cantidad as cantidad,Sum(C2.cantidad)as acumulado," +
						"(SELECT deposito.descripcion FROM depositos deposito where deposito.id = this_.deposito_id) AS depositoDescripcion, " +
						"this_.clienteAsp_id    AS clienteAspId, " +
						"(SELECT concepto.descripcion FROM conceptoFacturable concepto where concepto.id = this_.concepto_id) AS conceptoDescripcion," +
						"this_.fecha            AS fecha, " +
						"this_.hora             AS hora, " +
						"this_.tipoMovimiento   AS tipoMovimiento, " +
						"this_.nota             AS nota," +
						"(SELECT origen.descripcion FROM depositos origen where origen.id = this_.origenDestino_id) AS origenDestinoDescripcion " +
						" FROM STOCK this_ " +
						" INNER JOIN STOCK C2 on (this_.id >= C2.id) " +
						" INNER JOIN conceptoFacturable con1_ " +
						" ON this_.concepto_id=con1_.id and C2.concepto_id=con1_.id " +
						" INNER JOIN depositos dep2_ " +
						" ON this_.deposito_id=dep2_.id and C2.deposito_id=dep2_.id " +
						" INNER JOIN sucursales sucursal3_ " +
						" ON dep2_.sucursal_id=sucursal3_.id " +
						" INNER JOIN empresas empresa4_ " +
						" ON sucursal3_.empresa_id=empresa4_.id " +
						" WHERE 1=1 AND ";
        	if(stock != null){
        		if(stock.getCodigoDeposito() != null){
        			sql += " dep2_.codigo= '"+stock.getCodigoDeposito()+"' AND ";
        		}
        		if(stock.getCodigoConcepto() != null){
        			sql += " con1_.codigo='"+stock.getCodigoConcepto()+"' AND ";
        		}
        	}
        	if(cliente != null){
        		sql += " con1_.clienteAsp_id="+cliente.getId()+" AND con1_.clienteAsp_id="+cliente.getId()+" AND empresa4_.cliente_id="+cliente.getId();
    		}        	
        	sql += " GROUP BY  this_.nota, this_.cantidad, this_.id, this_.deposito_id, this_.clienteAsp_id, this_.concepto_id,	this_.fecha," +
        			" this_.hora, this_.tipoMovimiento, this_.origenDestino_id ";
        	
        	sql +="Order by  this_.fecha DESC, this_.hora DESC, this_.id DESC";
        	SQLQuery crit = session.createSQLQuery(sql);
        	crit.addScalar("id", Hibernate.LONG);
        	crit.addScalar("cantidad", Hibernate.INTEGER);
        	crit.addScalar("acumulado", Hibernate.INTEGER);
        	crit.addScalar("depositoDescripcion", Hibernate.STRING);
        	crit.addScalar("origenDestinoDescripcion", Hibernate.STRING);
        	crit.addScalar("conceptoDescripcion", Hibernate.STRING);
        	crit.addScalar("clienteAspId", Hibernate.LONG);
        	crit.addScalar("tipoMovimiento", Hibernate.STRING);
        	crit.addScalar("hora", Hibernate.STRING);
        	crit.addScalar("fecha", Hibernate.DATE);
        	crit.addScalar("nota", Hibernate.STRING);
        	//CREAR UNA CLASE RESUMEN
        	crit.setResultTransformer(Transformers.aliasToBean(StockAcumulado.class));
        	
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo realizar el acumulado del detalle de Stock ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public Stock verificarExistente(Stock stock, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("concepto", "con");
        	crit.createCriteria("deposito", "dep");
        	crit.createCriteria("origenDestino", "od");
        	if(stock != null){
        		if(stock.getDeposito() != null)
        			crit.add(Restrictions.eq("dep", stock.getDeposito()));
        		if(stock.getCodigoConcepto() != null)
        			crit.add(Restrictions.eq("con.codigo", stock.getCodigoConcepto()));
        		if(stock.getCodigoOrigenDestino() != null)
        			crit.add(Restrictions.eq("od.codigo", stock.getCodigoOrigenDestino()));
        		if(stock.getFecha() != null){
        			crit.add(Restrictions.eq("fecha", stock.getFecha()));        			
        		}
        		if(stock.getHora() != null && !"".equals(stock.getHora())){
        			crit.add(Restrictions.eq("hora", stock.getHora()));
        		}
        	}
        	if(cliente != null){
    			crit.add(Restrictions.eq("clienteAsp", cliente));
    			crit.add(Restrictions.eq("con.clienteAsp", cliente));
    			crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
    			crit.createCriteria("od.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
    		}
        	List<Stock> listaStock = crit.list();
        	if(listaStock != null && listaStock.size() == 1){
        		return listaStock.get(0);
        	}
            return null;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo realizar la agrupacion de Stock ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	
	@Override
	public Boolean resumirMovimientosStock(Stock stock, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("deposito", "dep");
        	if(stock != null){
        		if(stock.getCodigoDeposito() != null){
        			crit.add(Restrictions.eq("dep.codigo", stock.getCodigoDeposito()));
        		}
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	crit.setProjection( Projections.projectionList()
        	        .add( Projections.sum("cantidad"), "cantidad")
        	        .add( Projections.groupProperty("deposito"), "deposito")
        	        .add( Projections.groupProperty("clienteAsp"), "cliente")
        	        .add( Projections.groupProperty("concepto"), "concepto")
        	    );
        	//CREAR UNA CLASE RESUMEN
        	crit.setResultTransformer(new AliasToBeanResultTransformer(StockResumen.class));
        	List<StockResumen> listResumen = crit.list();
	        if(listResumen != null && !listResumen.isEmpty()){	
	        	//Creamos un Nuevo Criterio para traer todo el stock y poder eliminarlos
	        	crit = session.createCriteria(getClaseModelo());
	        	crit.createCriteria("deposito", "dep");
	        	if(stock != null){
	        		if(stock.getCodigoDeposito() != null){
	        			crit.add(Restrictions.eq("dep.codigo", stock.getCodigoDeposito()));
	        		}
	        	}
	        	if(cliente != null)
	        		crit.add(Restrictions.eq("clienteAsp", cliente));
	        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	List<Stock> listStock = crit.list();
	        	if(listStock != null && !listStock.isEmpty()){
	        		for(Stock delete  : listStock){
	        			session.delete(delete);
	        			session.flush();
	        			session.clear();
	        		}
	        	}
	        	
	        	//Creamos los nuevos valores para el stock Resumido
	        	Stock saveStock = null;
	        	for(StockResumen save : listResumen){
	        		saveStock = new Stock();
	        		saveStock.setCantidad(save.getCantidad());
	        		saveStock.setDeposito(save.getDeposito());
	        		saveStock.setConcepto(save.getConcepto());
	        		saveStock.setTipoMovimiento("Ingreso");
	        		Date fecha = new Date();
	        		saveStock.setFecha(fecha);
	        		saveStock.setHora(fecha.getHours() + ":" + fecha.getMinutes());
	        		saveStock.setClienteAsp(save.getCliente());
	        		saveStock.setNota("Se ejecuto la Tarea Resumir Movimientos.");
	        		session.save(saveStock);
	        		session.flush();
        			session.clear();
	        	}
	        	
        	}
	        return true;            
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo realizar la agrupacion de Stock ", hibernateException);
	        return false;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }

}
