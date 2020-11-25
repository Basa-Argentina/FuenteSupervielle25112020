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

import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoDetalleService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;

/**
 * @author Victor Kenis
 *
 */
@Component
public class RemitoDetalleServiceImp extends GestorHibernate<RemitoDetalle> implements RemitoDetalleService {
	private static Logger logger=Logger.getLogger(RemitoDetalleServiceImp.class);
	
	@Autowired
	public RemitoDetalleServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<RemitoDetalle> getClaseModelo() {
		return RemitoDetalle.class;
	}

	@Override
	public Boolean guardarRemitoDetalle(RemitoDetalle remitoDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(remitoDetalle);
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
	public Boolean actualizarRemitoDetalle(RemitoDetalle remitoDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(remitoDetalle);
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
	public Boolean eliminarRemitoDetalle(RemitoDetalle remitoDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(remitoDetalle);
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
	public List<RemitoDetalle> listarRemitoDetalleFiltradas(RemitoDetalle remitoDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("remito", "remito");
        	if(remitoDetalle!=null){
        		if(remitoDetalle.getRemito() !=null)
	        		crit.add(Restrictions.eq("remito.id", remitoDetalle.getRemito().getId()));
        		if(remitoDetalle.getElemento() !=null)
	        		crit.createCriteria("elemento").add(Restrictions.eq("codigo", remitoDetalle.getElemento().getCodigo()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("remito.clienteAsp", cliente));
        	}
        	crit.addOrder(Order.asc("orden"));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar remitos detalles", hibernateException);
	        return null;
        } catch (Exception exc) {
        	logger.error("No se pudo listar remitos detalles", exc);
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
	public RemitoDetalle verificarExistente(RemitoDetalle remitoDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("remito", "remito");
        	if(remitoDetalle!=null){
        		if(remitoDetalle.getRemito() !=null)
	        		crit.add(Restrictions.eq("remito.id", remitoDetalle.getRemito().getId()));
        		if(remitoDetalle.getElemento() !=null)
	        		crit.createCriteria("elemento").add(Restrictions.eq("codigo", remitoDetalle.getElemento().getCodigo()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("remito.clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<RemitoDetalle> result=crit.list();
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
	public List<RemitoDetalle> listarRemitoDetallePorRemito(Remito remito, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("remito", "remito");
        	if(remito!=null){
        		crit.add(Restrictions.eq("remito", remito));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("remito.clienteAsp", cliente));
        	}
        	crit.addOrder(Order.asc("elemento"));
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
