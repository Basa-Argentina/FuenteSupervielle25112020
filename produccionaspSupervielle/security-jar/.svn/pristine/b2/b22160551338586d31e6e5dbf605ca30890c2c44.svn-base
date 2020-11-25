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
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;

/**
 * @author Victor Kenis
 *
 */
@Component
public class LecturaDetalleServiceImp extends GestorHibernate<LecturaDetalle> implements LecturaDetalleService {
	private static Logger logger=Logger.getLogger(LecturaDetalleServiceImp.class);
	
	@Autowired
	public LecturaDetalleServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<LecturaDetalle> getClaseModelo() {
		return LecturaDetalle.class;
	}

	@Override
	public Boolean guardarLecturaDetalle(LecturaDetalle lecturaDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(lecturaDetalle);
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
	public Boolean actualizarLecturaDetalle(LecturaDetalle lecturaDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(lecturaDetalle);
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
	public Boolean eliminarLecturaDetalle(LecturaDetalle lecturaDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(lecturaDetalle);
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
	public List<LecturaDetalle> listarLecturaDetalleFiltradas(LecturaDetalle lecturaDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("lectura", "lectura");
        	if(lecturaDetalle!=null){
        		if(lecturaDetalle.getLectura() !=null)
	        		crit.add(Restrictions.eq("lectura.id", lecturaDetalle.getLectura().getId()));
        		if(lecturaDetalle.getElemento() !=null)
	        		crit.createCriteria("elemento").add(Restrictions.eq("codigo", lecturaDetalle.getElemento().getCodigo()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("lectura.clienteAsp", cliente));
        	}
        	crit.addOrder(Order.asc("orden"));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar lecturas detalles", hibernateException);
	        return null;
        } catch (Exception exc) {
        	logger.error("No se pudo listar lecturas detalles", exc);
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
	public LecturaDetalle verificarExistente(LecturaDetalle lecturaDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("lectura", "lectura");
        	if(lecturaDetalle!=null){
        		if(lecturaDetalle.getLectura() !=null)
	        		crit.add(Restrictions.eq("lectura.id", lecturaDetalle.getLectura().getId()));
        		if(lecturaDetalle.getElemento() !=null)
	        		crit.createCriteria("elemento").add(Restrictions.eq("codigo", lecturaDetalle.getElemento().getCodigo()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("lectura.clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<LecturaDetalle> result=crit.list();
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
	public List<LecturaDetalle> listarLecturaDetallePorLectura(Lectura lectura, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("lectura", "lectura");
        	if(lectura!=null){
        		crit.add(Restrictions.eq("lectura", lectura));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("lectura.clienteAsp", cliente));
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
	
	@Override
	public List<LecturaDetalle> listarOrderByOrdenLecturaDetallePorLectura(Lectura lectura, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("lectura", "lectura");
        	if(lectura!=null){
        		crit.add(Restrictions.eq("lectura", lectura));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("lectura.clienteAsp", cliente));
        	}
        	crit.addOrder(Order.asc("orden"));
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
	
	@Override
	public List<LecturaDetalle> listarLecturaDetalleEnListaElementos(List<Elemento> elementos, Lectura lectura, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("lectura", "lectura");
        	crit.createCriteria("elemento", "elem");
        	if(lectura!=null){
        		crit.add(Restrictions.eq("lectura", lectura));
        	}
        	
			if(elementos!=null && elementos.size()>0)
			{
				Disjunction disjunction = Restrictions.disjunction();
	        	for(Elemento elemento : elementos){
	        			disjunction.add(Restrictions.eq("elem.id", elemento.getId()));
	        	}
	        	crit.add(disjunction);
			}
        	        	
        	if(cliente != null){
        		crit.add(Restrictions.eq("lectura.clienteAsp", cliente));
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
