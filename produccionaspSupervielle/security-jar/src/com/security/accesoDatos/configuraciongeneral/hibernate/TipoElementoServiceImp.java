/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.TipoElemento;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class TipoElementoServiceImp extends GestorHibernate<TipoElemento> implements TipoElementoService{
	private static Logger logger=Logger.getLogger(TipoElementoServiceImp.class);
	
	@Autowired
	public TipoElementoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<TipoElemento> getClaseModelo() {
		return TipoElemento.class;
	}

	@Override
	public boolean delete(TipoElemento elemento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(elemento);
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
	public boolean save(TipoElemento elemento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(elemento);
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
	public boolean update(TipoElemento elemento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(elemento);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoElemento> listarTipoElementoFiltrados(TipoElemento tipoElemento, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			if(tipoElemento != null){
				//filtro por codigo
				if(tipoElemento.getCodigo() != null && !"".equals(tipoElemento.getCodigo()))
					c.add(Restrictions.ilike("codigo", tipoElemento.getCodigo()+"%"));
				//filtro por descripcion
				if(tipoElemento.getDescripcion() != null && !"".equals(tipoElemento.getDescripcion()))
					c.add(Restrictions.ilike("descripcion", tipoElemento.getDescripcion()+"%"));
			}
			if(clienteAsp != null){
				c.add(Restrictions.eq("clienteAsp", clienteAsp));
			}
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return c.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener los tipos de elementos filtrados",e);
			return null;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public TipoElemento verificarExistente(TipoElemento tipoElemento){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			if(tipoElemento != null){
				//filtro por codigo
				if(tipoElemento.getCodigo() != null && !"".equals(tipoElemento.getCodigo()))
					c.add(Restrictions.ilike("codigo", tipoElemento.getCodigo()+"%"));
				if(tipoElemento.getClienteAsp() != null){
					c.add(Restrictions.eq("clienteAsp", tipoElemento.getClienteAsp()));
				}
			}
			
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<TipoElemento> result = c.list();
        	if(result.size() == 1){
        		return result.get(0);
        	}
        	return null;
		} catch (HibernateException e) {
			logger.error("no se pudo obtener los tipos de elementos filtrados",e);
			return null;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TipoElemento verificarExistentePrefijoCodigo(TipoElemento tipoElemento){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			if(tipoElemento != null){
				//filtro por codigo
				if(tipoElemento.getPrefijoCodigo() != null && !"".equals(tipoElemento.getPrefijoCodigo()))
					c.add(Restrictions.ilike("prefijoCodigo", tipoElemento.getPrefijoCodigo()+"%"));
				if(tipoElemento.getClienteAsp() != null){
					c.add(Restrictions.eq("clienteAsp", tipoElemento.getClienteAsp()));
				}
			}
			
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<TipoElemento> result = c.list();
        	if(result.size() == 1){
        		return result.get(0);
        	}
        	return null;
		} catch (HibernateException e) {
			logger.error("no se pudo obtener los tipos de elementos filtrados",e);
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
	public List<TipoElemento> listarTipoElementoPopup(String val, ClienteAsp cliente){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro value        	
        	if(val!=null){        		
        		c.add(Restrictions.ilike("descripcion", val+"%"));        	
        	}
        	if(cliente != null){
	        	//filtro cliente
	        	c.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar los tipos de elementos.", hibernateException);
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
	public TipoElemento getByCodigo(String codigo, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	
            return (TipoElemento) crit.uniqueResult();
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
	public TipoElemento traerUltCodigoTipoElemento(ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	int i = (crit.list().size())-1;
        	return (TipoElemento) crit.list().get(i);
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar los tipos de elementos.", hibernateException);
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
	public TipoElemento getByPrefijo(String prefijo, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.add(Restrictions.eq("prefijoCodigo", prefijo));
//        	if(cliente != null){
//        		crit.add(Restrictions.eq("clienteAsp", cliente));
//        	}
        	
        	String consulta = "SELECT te FROM TipoElemento te WHERE te.prefijoCodigo = '" + prefijo + "' ";
        						if(cliente != null)
        							consulta += "AND te.clienteAsp.id = " + cliente.getId().longValue() + " ";
        	
        	Query query = session.createQuery(consulta);
        	
            return (TipoElemento) query.uniqueResult();
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
	public TipoElemento getByID(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			String consulta = " SELECT DISTINCT te FROM TipoElemento te WHERE te.id = "+ id.longValue()+"";
			
			TipoElemento tipoElemento = (TipoElemento)session.createQuery(consulta).uniqueResult();
			Hibernate.initialize(tipoElemento.getConceptoGuarda());
			Hibernate.initialize(tipoElemento.getConceptoStock());
			Hibernate.initialize(tipoElemento.getConceptoVenta());
			
			return tipoElemento;
			
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
