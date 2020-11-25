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

import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.modelo.general.Localidad;
import com.security.modelo.general.Provincia;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class LocalidadServiceImp extends GestorHibernate<Localidad> implements LocalidadService{
	private static Logger logger=Logger.getLogger(LocalidadServiceImp.class);
	
	@Autowired
	public LocalidadServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);	
	}

	@Override
	public Class<Localidad> getClaseModelo() {
		return Localidad.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Localidad> listarLocalidades() {
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
	public List<Localidad> listarLocalidadesPorProcinciaId(Long provinciaId) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("provincia").add(Restrictions.eq("id", provinciaId));
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
	public List<Localidad> listarLocalidadesPopup(Long provinciaId, String val) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("provincia").add(Restrictions.eq("id", provinciaId));
        	if(val != null && !"".equals(val))
        		crit.add(Restrictions.ilike("nombre", val + "%"));
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
	public Localidad getLocalidadPorId(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	//filtro por id
        	if(id != null)
        		crit.add(Restrictions.eq("id",id));
        	
        	//solo listar etidades diferentes
        	crit.setFetchMode("barrios", FetchMode.JOIN);
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
            List<Localidad> salida = crit.list();
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
	public Localidad getLocalidadPorNombre(String nombre,Long idPcia) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por nombre
        	if(nombre != null && !"".equals(nombre))
        		crit.add(Restrictions.eq("nombre",nombre));
        	//filtro por id Provincia
        		crit.createCriteria("provincia").add(Restrictions.eq("id", idPcia));
        	//solo listar etidades diferentes
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<Localidad> salida = crit.list();
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
	public Boolean guardarLocalidad(Localidad localidad) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		String consulta ="";
		
		try {
			
			if(localidad.getId()==null){
				Long id = traerUltId();
				if(id!=null){
					localidad.setId(id+1);
					consulta = "INSERT INTO LOCALIDADES(id,nombre,provincia_id) VALUES ("+localidad.getId()+",'"+localidad.getNombre().toUpperCase()+"','"+localidad.getProvincia().getId()+"')";
				}
			}
			else{
				consulta = "UPDATE LOCALIDADES SET nombre = '"+localidad.getNombre().toUpperCase()+"' WHERE id ="+ localidad.getId() + " ";
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
	public Boolean eliminarLocalidad(Localidad loc) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		
		try {
			
			if(loc.getId()==null){
				Long id = traerUltId();
				if(id!=null)
					loc.setId(id+1);
			}
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			String consulta = "delete from localidades where id =" + loc.getId();
			
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
	public List<Localidad> buscarLocalidades(Long pciaId, String nombre, Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtrar por pais
        	if(pciaId!=null)
        		crit.createCriteria("provincia").add(Restrictions.eq("id", pciaId));
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
