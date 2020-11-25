/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoFactDetalleService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.GrupoFactDetalle;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class GrupoFactDetalleServiceImp extends GestorHibernate<GrupoFactDetalle> implements GrupoFactDetalleService {
	private static Logger logger=Logger.getLogger(GrupoFactDetalleServiceImp.class);
	
	@Autowired
	public GrupoFactDetalleServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<GrupoFactDetalle> getClaseModelo() {
		return GrupoFactDetalle.class;
	}

	@Override
	public Boolean guardarGrupoFactDetalle(GrupoFactDetalle grupoFactDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(grupoFactDetalle);
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
	public Boolean actualizarGrupoFactDetalle(GrupoFactDetalle grupoFactDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(grupoFactDetalle);
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
	public Boolean eliminarGrupoFactDetalle(GrupoFactDetalle grupoFactDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(grupoFactDetalle);
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
	
	@Override
	public List<GrupoFactDetalle> listarGrupoFactDetalleFiltradas(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("grupoFacturacion", "grupo");
        	crit.createCriteria("grupo.agrupador", "a");
        	if(grupoFactDetalle!=null){
        		if(grupoFactDetalle.getGrupoFacturacion() !=null)
	        		crit.add(Restrictions.eq("grupo.id", grupoFactDetalle.getGrupoFacturacion().getId()));
        		if(grupoFactDetalle.getEmpleado() !=null)
	        		crit.createCriteria("empleado").add(Restrictions.eq("id", grupoFactDetalle.getEmpleado().getId()));
        		if(grupoFactDetalle.getDireccionEntrega() !=null)
	        		crit.createCriteria("direccionEntrega").add(Restrictions.eq("id", grupoFactDetalle.getDireccionEntrega().getId()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("a.clienteAsp", cliente));
        	}
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
	public List<ClienteDireccion> listarGrupoFactDetalleDirecciones(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente){
		List<GrupoFactDetalle> listDetalle = null;
		List<ClienteDireccion> listDirecciones = new ArrayList<ClienteDireccion>();
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("grupoFacturacion", "grupo");
        	crit.createCriteria("grupo.agrupador", "a");
        	if(grupoFactDetalle!=null){
        		if(grupoFactDetalle.getGrupoFacturacion() !=null && grupoFactDetalle.getGrupoFacturacion().getAgrupador() != null){
	        		crit.add(Restrictions.eq("a.id", grupoFactDetalle.getGrupoFacturacion().getAgrupador().getId()));
        		}
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("a.clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	listDetalle = crit.list();
        	for(GrupoFactDetalle detalle : listDetalle){
        		listDirecciones.add(detalle.getDireccionEntrega());
        	}
        	return listDirecciones;        	
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
	public List<Empleado> listarGrupoFactDetalleEmpleados(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente){
		List<GrupoFactDetalle> listDetalle = null;
		List<Empleado> listEmpleados = new ArrayList<Empleado>();
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("grupoFacturacion", "grupo");
        	crit.createCriteria("grupo.agrupador", "a");
        	if(grupoFactDetalle!=null){
        		if(grupoFactDetalle.getGrupoFacturacion() !=null && grupoFactDetalle.getGrupoFacturacion().getAgrupador() != null){
	        		crit.add(Restrictions.eq("a.id", grupoFactDetalle.getGrupoFacturacion().getAgrupador().getId()));
        		}
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("a.clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	listDetalle = crit.list();
        	for(GrupoFactDetalle detalle : listDetalle){
        		listEmpleados.add(detalle.getEmpleado());
        	}
        	return listEmpleados;        	
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
	public GrupoFactDetalle verificarExistente(GrupoFactDetalle grupoFactDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("grupoFacturacion", "grupo");
        	crit.createCriteria("grupo.agrupador", "a");
        	if(grupoFactDetalle!=null){
        		if(grupoFactDetalle.getGrupoFacturacion() !=null)
	        		crit.add(Restrictions.eq("grupo.id", grupoFactDetalle.getGrupoFacturacion().getId()));
        		if(grupoFactDetalle.getEmpleado() !=null)
	        		crit.createCriteria("empleado").add(Restrictions.eq("id", grupoFactDetalle.getEmpleado().getId()));
        		if(grupoFactDetalle.getDireccionEntrega() !=null)
	        		crit.createCriteria("direccionEntrega").add(Restrictions.eq("id", grupoFactDetalle.getDireccionEntrega().getId()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("a.clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<GrupoFactDetalle> result=crit.list();
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
	
}
