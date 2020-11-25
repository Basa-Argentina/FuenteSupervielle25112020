/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.AgrupadorFacturacion;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class AfipTipoComprobanteServiceImp extends GestorHibernate<AfipTipoComprobante> implements AfipTipoComprobanteService {
	private static Logger logger=Logger.getLogger(AfipTipoComprobanteServiceImp.class);
	
	@Autowired
	public AfipTipoComprobanteServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<AfipTipoComprobante> getClaseModelo() {
		return AfipTipoComprobante.class;
	}

	@Override
	public Boolean guardarAfipTipoComprobante(AfipTipoComprobante afipTipoComprobante) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(afipTipoComprobante);
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
	public Boolean actualizarAfipTipoComprobante(AfipTipoComprobante afipTipoComprobante) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(afipTipoComprobante);
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
	public Boolean eliminarAfipTipoComprobante(AfipTipoComprobante afipTipoComprobante) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(afipTipoComprobante);
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
	public List<AfipTipoComprobante> listarTipoComprobanteFiltrados(AfipTipoComprobante afipTipoComprobante){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(afipTipoComprobante!=null){
	        	if(afipTipoComprobante.getDescripcion() !=null && !"".equals(afipTipoComprobante.getDescripcion())){
	        		crit.add(Restrictions.ilike("descripcion", afipTipoComprobante.getDescripcion() + "%"));
	        	}
	        	if(afipTipoComprobante.getCodigo() !=null && !"".equals(afipTipoComprobante.getCodigo())){
	        		crit.add(Restrictions.ilike("codigo", afipTipoComprobante.getCodigo() + "%"));
	        	}
	        	if(afipTipoComprobante.getTipo() !=null && !"".equals(afipTipoComprobante.getTipo())){
	        		crit.add(Restrictions.eq("tipo", afipTipoComprobante.getTipo()));
	        	}
	        	if(afipTipoComprobante.getLetra()!=null && afipTipoComprobante.getLetra().length()>0){
	        		crit.add(Restrictions.eq("tipo", afipTipoComprobante.getLetra()));
	        	}
	        	if(afipTipoComprobante.getCodigos()!=null && afipTipoComprobante.getCodigos().size()>0){
	        		Disjunction disjunction = Restrictions.disjunction();
	        		for(String cod : afipTipoComprobante.getCodigos()){
	        			disjunction.add(Restrictions.eq("codigo", cod));
	        		}
	        		crit.add(disjunction);
	        	}
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
}
