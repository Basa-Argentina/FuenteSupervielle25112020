/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.LicenciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.administracion.Licencia;
import com.security.utils.DateUtil;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class LicenciaServiceImp extends GestorHibernate<Licencia> implements LicenciaService {
	private static Logger logger=Logger.getLogger(LicenciaServiceImp.class);
	
	@Autowired
	public LicenciaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Licencia> getClaseModelo() {
		return Licencia.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Licencia> obtenerLicenciaPorFiltroLicencia(Licencia licencia) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(licencia != null){
        		if(licencia.getFechaDesde() != null)
        			crit.add(Restrictions.ge("fechaDesde", DateUtil.getDateFrom(licencia.getFechaDesde())));
        		if(licencia.getFechaHasta() != null)
        			crit.add(Restrictions.le("fechaHasta", DateUtil.getDateTo(licencia.getFechaHasta())));
        		if(licencia.getEstadoId() != null && !licencia.getEstadoId().equals(0L))
        			crit.createCriteria("estado").add(Restrictions.eq("id", licencia.getEstadoId()));
        	}
        	return (List<Licencia>) crit.list();
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
	public Boolean guardarLicencia(Licencia licencia) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(licencia);
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
	public Boolean actualizarLicencia(Licencia licencia) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(licencia);
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
	public Boolean eliminarLicencia(Licencia licencia) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(licencia);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Licencia> obtenerLicenciaPorFecha(Date fechaDesde, Date fechaHasta, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        		c.add(
        			Restrictions.or(
        				Restrictions.and(
        						Restrictions.le("fechaDesde", DateUtil.getDateFrom(fechaDesde)),
        						Restrictions.ge("fechaHasta", DateUtil.getDateFrom(fechaDesde))
        				), 
        				Restrictions.and(
        						Restrictions.le("fechaDesde", DateUtil.getDateTo(fechaHasta)),
        						Restrictions.ge("fechaHasta", DateUtil.getDateTo(fechaHasta))
        				)
        			)        				
        		);       
        		if(cliente != null)
        			c.add(Restrictions.eq("cliente", cliente));
        	return (List<Licencia>) c.list();
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
	public Licencia obtenerLicenciaPorFechaAccesoAsp(Date fechaActual, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro por fecha
    		c.add(Restrictions.le("fechaDesde", fechaActual));       
    		c.add(Restrictions.ge("fechaHasta", fechaActual));     
    		//Filtro por estado (habilitada)
    		c.createCriteria("estado").add(Restrictions.ilike("nombre", "Habilitada"));
    		//filtro por clienteAsp
    		if(cliente != null)
    			c.add(Restrictions.eq("cliente", cliente));
        	return (Licencia) c.uniqueResult();
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
}
