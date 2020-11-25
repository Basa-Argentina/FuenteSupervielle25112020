/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 26/05/2011
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

import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.modelo.general.Pais;
import com.security.modelo.general.Provincia;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ProvinciaServiceImp extends GestorHibernate<Provincia> implements ProvinciaService{
	private static Logger logger=Logger.getLogger(ProvinciaServiceImp.class);
	
	@Autowired
	public ProvinciaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Provincia> getClaseModelo() {
		return Provincia.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Provincia> listarProvincias() {
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
	public List<Provincia> listarProvinciasPorPaisId(Long paisId) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("pais").add(Restrictions.eq("id", paisId));
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
	public List<Provincia> listarProvinciasPopup(Long paisId, String val) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtrar por pais
        	crit.createCriteria("pais").add(Restrictions.eq("id", paisId));
        	//filtro por el val
        	if(val != null && !"".equals(val))
        		crit.add(Restrictions.ilike("nombre", val+"%"));
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
	public Provincia getProvinciaPorId(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	//filtro por id
        	if(id != null)
        		crit.add(Restrictions.eq("id",id));
        	
        	//solo listar etidades diferentes
        	crit.setFetchMode("localidades", FetchMode.JOIN);
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
            List<Provincia> salida = crit.list();
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
	public Provincia getProvinciaPorNombre(String nombre, Long idPais) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por nombre
        	if(nombre != null && !"".equals(nombre))
        		crit.add(Restrictions.eq("nombre",nombre));
        	//filtro por id Pais
    		crit.createCriteria("pais").add(Restrictions.eq("id", idPais));
        	//solo listar etidades diferentes
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<Provincia> salida = crit.list();
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
	public Boolean guardarProvincia(Provincia provincia) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		String consulta ="";
		
		try {
			
			if(provincia.getId()==null){
				Long id = traerUltId();
				if(id!=null){
					provincia.setId(id+1);
					consulta = "INSERT INTO PROVINCIAS(id,nombre,pais_id) VALUES ("+provincia.getId()+",'"+provincia.getNombre()+"','"+provincia.getPais().getId()+"')";
				}
			}
			else{
				consulta = "UPDATE PROVINCIAS SET nombre = '"+provincia.getNombre()+"' WHERE id ="+ provincia.getId() + " ";
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
	
	@Override
	public Boolean eliminarProvincia(Provincia pcia) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		
		try {
			
			if(pcia.getId()==null){
				Long id = traerUltId();
				if(id!=null)
					pcia.setId(id+1);
			}
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			String consulta = "delete from provincias where id =" + pcia.getId();
			
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Provincia> buscarProvincias(Long paisId, String nombre, Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtrar por pais
        	if(paisId!=null)
        		crit.createCriteria("pais").add(Restrictions.eq("id", paisId));
        	//filtro por el nombre
        	if(nombre != null && !"".equals(nombre))
        		crit.add(Restrictions.ilike("nombre", nombre+"%"));
        	//filtro por el id
        	if(id != null)
        		crit.add(Restrictions.eq("id", id));
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
