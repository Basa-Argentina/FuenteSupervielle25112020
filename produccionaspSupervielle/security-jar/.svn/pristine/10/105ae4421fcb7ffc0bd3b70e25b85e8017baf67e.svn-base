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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.HojaRutaOperacionElementoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.HojaRutaOperacionElemento;
import com.security.utils.Constantes;

/**
 * 
 * @author FedeMz
 *
 */
@Component
public class HojaRutaOperacionElementoServiceImp extends GestorHibernate<HojaRutaOperacionElemento> implements HojaRutaOperacionElementoService{
	private static Logger logger=Logger.getLogger(HojaRutaOperacionElementoServiceImp.class);
	
	@Autowired
	public HojaRutaOperacionElementoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<HojaRutaOperacionElemento> getClaseModelo() {
		return HojaRutaOperacionElemento.class;
	}
	
	/**
	 * Guarda o actualiza un LoteReferencia en la base de datos y todas sus relaciones
	 * @param objeto
	 */
	public synchronized void guardarActualizar(HojaRutaOperacionElemento hojaRutaOperacionElemento){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			if(hojaRutaOperacionElemento.getId()==null || hojaRutaOperacionElemento.getId()==0){
				hojaRutaOperacionElemento.setId(null);
				//loteReferencia.getReferencias().clear();
				session.save(hojaRutaOperacionElemento);
			}else{
				session.update(hojaRutaOperacionElemento);
			}
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre algún error intentamos hacer rollback
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public void eliminar(long idElemento){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			String hql = "delete from hojaRuta_operacionElemento where operacionElemento_id = :idElemento";
	        Query query = session.createQuery(hql);
	        query.setLong("idElemento",idElemento);
	        query.executeUpdate();
			//hacemos commit a los cambios para que se refresque la base de datos.
			
		} 
		catch (RuntimeException e) {
			//si ocurre algún error, se hace rollback a los cambios.
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
	}
	
	@Override
	public void eliminarPorHojaRuta(long idHojaRuta){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			String hql = "delete from hojaRuta_operacionElemento where hojaRuta_id = :idHojaRuta";
	        Query query = session.createQuery(hql);
	        query.setLong("idHojaRuta",idHojaRuta);
	        query.executeUpdate();
			//hacemos commit a los cambios para que se refresque la base de datos.
			
		} 
		catch (RuntimeException e) {
			//si ocurre algún error, se hace rollback a los cambios.
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
	}

	@Override
	public boolean chequearOperacionElementoPendiente(Long idHojaRuta,Long idOperacionElemento) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria query = session.createCriteria(HojaRutaOperacionElemento.class);
			query.add(Restrictions.or(
					Restrictions.eq("estado", Constantes.ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO),
					Restrictions.eq("estado", Constantes.ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_PROCESADO)
			));
			query.createCriteria("operacionElemento", "op");
			query.add(Restrictions.eq("op.id", idOperacionElemento));
	        if(idHojaRuta!=null){
	        	query.createCriteria("hojaRuta", "hr");
	        	query.add(Restrictions.ne("hr.id", idHojaRuta));
	        }	
	        query.setProjection(Projections.rowCount());
	        List result = query.list();
	        if(result.size()==0)
	        	return true;
	        return ((Number) result.get(0)).intValue()==0;
		} 
		catch (RuntimeException e) {
			logger.error("Ocurrió un error en la consulta", e);
			return true;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
}
