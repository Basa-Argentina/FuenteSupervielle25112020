/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoFacturacionService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.GrupoFactDetalle;
import com.security.modelo.configuraciongeneral.GrupoFacturacion;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class GrupoFacturacionServiceImp extends GestorHibernate<GrupoFacturacion> implements GrupoFacturacionService {
	private static Logger logger=Logger.getLogger(GrupoFacturacionServiceImp.class);
	
	@Autowired
	public GrupoFacturacionServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<GrupoFacturacion> getClaseModelo() {
		return GrupoFacturacion.class;
	}

	@Override
	public Boolean guardarGrupoFacturacion(GrupoFacturacion grupoFacturacion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(grupoFacturacion);
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
	public Boolean actualizarGrupoFacturacion(GrupoFacturacion grupoFacturacion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			actualizarOrphan(grupoFacturacion, session);
			//guardamos el objeto
			session.update(grupoFacturacion);
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
	public Boolean eliminarGrupoFacturacion(GrupoFacturacion grupoFacturacion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(grupoFacturacion);
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
	public List<GrupoFacturacion> listarGrupoFacturacionFiltradas(GrupoFacturacion grupoFacturacion){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(grupoFacturacion!=null){
        		if(grupoFacturacion.getAgrupador() !=null)
	        		crit.add(Restrictions.eq("agrupador", grupoFacturacion.getAgrupador()));
	        	if(grupoFacturacion.getDescripcion() !=null && !"".equals(grupoFacturacion.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", grupoFacturacion.getDescripcion() + "%"));
	        	if(grupoFacturacion.getCodigo() !=null && !"".equals(grupoFacturacion.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", grupoFacturacion.getCodigo() + "%"));
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
	public GrupoFacturacion verificarExistente(GrupoFacturacion grupoFacturacion){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(grupoFacturacion!=null){
        		if(grupoFacturacion.getAgrupador() !=null)
	        		crit.createCriteria("agrupador").add(Restrictions.eq("id", grupoFacturacion.getAgrupador().getId()));
	        	if(grupoFacturacion.getCodigo() !=null && !"".equals(grupoFacturacion.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", grupoFacturacion.getCodigo()));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<GrupoFacturacion> result=crit.list();
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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClienteEmp> listarGrupoFacturacionPopup(String val, ClienteAsp cliente) {
		Session session = null;
        try {
        	String valores[] = null;
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro value
        	if(val!=null){
		        	c.add(Restrictions.ilike("codigo", val+"%"));
        	}
        	//filtro cliente
        	c.createCriteria("grupoFacturacion").createCriteria("agrupador").add(Restrictions.eq("clienteAsp", cliente));
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
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
	
	/*
	 * El orphan de Persistence y la Opcion REMOVAL_ORPHAN de hibernate no funciona.
	 * Se implementa este metodo que realiza la eliminacion de los hijos, para luego
	 * insertar los nuevos hijos.
	 */
	private void actualizarOrphan(GrupoFacturacion grupo, Session session){
		
		GrupoFacturacion grupoLoad = (GrupoFacturacion)session.get(GrupoFacturacion.class,grupo.getId());
		session.clear();
		Set<GrupoFactDetalle> detalles = grupoLoad.getDetalles();
		for(GrupoFactDetalle detalle : detalles){
			session.delete(detalle);
			session.flush();
			session.clear();
		}
		
	}
}
