/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.LoteFacturacionDetalleService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.LoteFacturacionDetalle;

/**
 * @author Victor Kenis
 *
 */
@Component
public class LoteFacturacionDetalleServiceImp extends GestorHibernate<LoteFacturacionDetalle> implements LoteFacturacionDetalleService {
	private static Logger logger=Logger.getLogger(LoteFacturacionDetalleServiceImp.class);
	
	@Autowired
	public LoteFacturacionDetalleServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<LoteFacturacionDetalle> getClaseModelo() {
		return LoteFacturacionDetalle.class;
	}

	@Override
	public Boolean guardarLoteFacturacionDetalle(LoteFacturacionDetalle loteFacturacionDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(loteFacturacionDetalle);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible guardar");
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
	public Boolean actualizarLoteFacturacionDetalle(LoteFacturacionDetalle loteFacturacionDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(loteFacturacionDetalle);
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
	
	@Override
	public Boolean eliminarLoteFacturacionDetalle(LoteFacturacionDetalle loteFacturacionDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(loteFacturacionDetalle);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible eliminar");
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LoteFacturacionDetalle> listarLoteFacturacionDetalleFiltradas(LoteFacturacionDetalle loteFacturacionDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("loteFacturacion", "loteFacturacion");
        	if(loteFacturacionDetalle!=null){
        		if(loteFacturacionDetalle.getLoteFacturacion() !=null)
	        		crit.add(Restrictions.eq("loteFacturacion.id", loteFacturacionDetalle.getLoteFacturacion().getId()));
//        		if(loteFacturacionDetalle.getElemento() !=null)
//	        		crit.createCriteria("elemento").add(Restrictions.eq("codigo", loteFacturacionDetalle.getElemento().getCodigo()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("loteFacturacion.clienteAsp", cliente));
        	}
        	crit.addOrder(Order.asc("orden"));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar loteFacturacions detalles", hibernateException);
	        return null;
        } catch (Exception exc) {
        	logger.error("No se pudo listar loteFacturacions detalles", exc);
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
	public LoteFacturacionDetalle verificarExistente(LoteFacturacionDetalle loteFacturacionDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("loteFacturacion", "loteFacturacion");
        	if(loteFacturacionDetalle!=null){
        		if(loteFacturacionDetalle.getLoteFacturacion() !=null)
	        		crit.add(Restrictions.eq("loteFacturacion.id", loteFacturacionDetalle.getLoteFacturacion().getId()));
//        		if(loteFacturacionDetalle.getElemento() !=null)
//	        		crit.createCriteria("elemento").add(Restrictions.eq("codigo", loteFacturacionDetalle.getElemento().getCodigo()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("loteFacturacion.clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<LoteFacturacionDetalle> result=crit.list();
        	if(result.size()==1)
				return result.get(0);
			return null;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
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
	public List<LoteFacturacionDetalle> listarLoteFacturacionDetallePorLoteFacturacion(LoteFacturacion loteFacturacion, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("loteFacturacion", "loteFacturacion");
        	if(loteFacturacion!=null){
        		crit.add(Restrictions.eq("loteFacturacion", loteFacturacion));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("loteFacturacion.clienteAsp", cliente));
        	}
        	crit.addOrder(Order.asc("id"));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
}
