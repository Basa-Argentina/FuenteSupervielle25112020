/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 24/05/2011
 */
package com.security.accesoDatos.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.PaisService;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Estante;
import com.security.modelo.general.Pais;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class PaisServiceImp extends GestorHibernate<Pais> implements PaisService{
	private static Logger logger=Logger.getLogger(PaisServiceImp.class);
	
	@Autowired
	public PaisServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Pais> getClaseModelo() {
		return Pais.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pais> listarPaises() {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Pais> listarPaisesPopup(String val) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por nombre
        	if(val != null && !"".equals(val))
        		crit.add(Restrictions.ilike("nombre", val + "%"));
        	//solo listar etidades diferentes
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Pais getPaisPorNombre(String nombre) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por nombre
        	if(nombre != null && !"".equals(nombre))
        		crit.add(Restrictions.eq("nombre",nombre));
        	//solo listar etidades diferentes
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<Pais> salida = crit.list();
            if(salida!=null && salida.size()>0)
            	return salida.get(0);
            else
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Pais getPaisPorId(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	//filtro por id
        	if(id != null)
        		crit.add(Restrictions.eq("id",id));
        	
        	//solo listar etidades diferentes
        	crit.setFetchMode("provincias", FetchMode.JOIN);
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
            List<Pais> salida = crit.list();
            if(salida!=null && salida.size()>0)
            	return salida.get(0);
            else
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
	public Boolean guardarPais(Pais pais) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		String consulta ="";
		
		try {
			
			if(pais.getId()==null){
				Long id = traerUltId();
				if(id!=null){
					pais.setId(id+1);
					consulta = "INSERT INTO PAISES(id,nombre) VALUES ("+pais.getId()+",'"+pais.getNombre().toUpperCase()+"')";
				}
			}
			else{
				consulta = "UPDATE PAISES  SET nombre = '"+pais.getNombre().toUpperCase()+"' WHERE id ="+ pais.getId() + " ";
			}
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			
			
			SQLQuery q = session.createSQLQuery(consulta);
			
			result = q.executeUpdate();
	
			if(result > 0)
				return true;
			else
				return false;
			
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
	public Boolean eliminarPais(Pais pais) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		
		try {
			
			if(pais.getId()==null){
				Long id = traerUltId();
				if(id!=null)
					pais.setId(id+1);
			}
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			String consulta = "delete from paises where id =" + pais.getId();
			
			SQLQuery q = session.createSQLQuery(consulta);
			
			result = q.executeUpdate();
	
			if(result > 0)
				return true;
			else
				return false;
			
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
	
	public Long traerUltId(){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());	        	
	        crit.setProjection(Projections.max("id"));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	List<Long> result = crit.list();
        	if(result.size() == 1){
        		Long rta = result.get(0);
        		return rta; 
        	}
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

}
