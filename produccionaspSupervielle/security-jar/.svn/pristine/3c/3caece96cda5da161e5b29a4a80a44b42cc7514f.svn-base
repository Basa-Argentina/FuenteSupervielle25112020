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
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.BarrioService;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Localidad;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class BarrioServiceImp extends GestorHibernate<Barrio> implements BarrioService{
	private static Logger logger=Logger.getLogger(BarrioServiceImp.class);
	
	@Autowired
	public BarrioServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Barrio> getClaseModelo() {
		return Barrio.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Barrio> listarBarriosPorLocalidadId(Long localidadId) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("localidad").add(Restrictions.eq("id", localidadId));
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
	public List<Barrio> listarBarriosPopup(Long localidadId, String val) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("localidad").add(Restrictions.eq("id", localidadId));
        	//filtro por val
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
	
	@Override
	public Barrio obtenerPorNombreLocalidad(String nombreLocalidad) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("localidad", "loc");
    		crit.add(Restrictions.eq("loc.nombre", nombreLocalidad));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	if(crit.list().isEmpty()){
        		return null;
        	}else{
        		return (Barrio) crit.list().get(0);
        	}
            
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
	public Barrio getBarrioPorNombre(String nombre, Long idLocalidad) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por nombre
        	if(nombre != null && !"".equals(nombre))
        		crit.add(Restrictions.eq("nombre",nombre));
        	//filtro por id localidad
    		crit.createCriteria("localidad").add(Restrictions.eq("id", idLocalidad));
        	//solo listar etidades diferentes
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<Barrio> salida = crit.list();
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
	public Boolean guardarBarrio(Barrio barrio) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		String consulta ="";
		
		try {
			
			if(barrio.getId()==null){
				Long id = traerUltId();
				if(id!=null){
					barrio.setId(id+1);
					consulta = "INSERT INTO BARRIOS(nombre,localidad_id) VALUES ('"+barrio.getNombre().toUpperCase()+"',"+barrio.getLocalidad().getId()+")";
				}
			}
			else{
				consulta = "UPDATE BARRIOS SET nombre = '"+barrio.getNombre().toUpperCase()+"' WHERE id ="+ barrio.getId() + " ";
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
	public Boolean eliminarBarrio(Barrio barrio) {
		Session session = null;
		Transaction tx = null;
		int result = 0;
		
		try {
			
			if(barrio.getId()==null){
				Long id = traerUltId();
				if(id!=null)
					barrio.setId(id+1);
			}
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			String consulta = "delete from barrios where id =" + barrio.getId();
			
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
	public List<Barrio> buscarBarrios(Long locId, String nombre, Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtrar por pais
        	if(locId!=null)
        		crit.createCriteria("localidad").add(Restrictions.eq("id", locId));
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
