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

import com.security.accesoDatos.configuraciongeneral.interfaz.AgrupadorService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AgrupadorFacturacion;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class AgrupadorServiceImp extends GestorHibernate<AgrupadorFacturacion> implements AgrupadorService {
	private static Logger logger=Logger.getLogger(AgrupadorServiceImp.class);
	
	@Autowired
	public AgrupadorServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<AgrupadorFacturacion> getClaseModelo() {
		return AgrupadorFacturacion.class;
	}

	@Override
	public Boolean guardarAgrupadorFacturacion(AgrupadorFacturacion agrupador) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			if(agrupador.getHabilitado())
				setHabilitado(agrupador, session);
			//guardamos el objeto
			session.save(agrupador);
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
	public Boolean actualizarAgrupadorFacturacion(AgrupadorFacturacion agrupador) {
		Boolean result = true;
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			if(agrupador.getHabilitado())
				result = setHabilitado(agrupador, session);
			//guardamos el objeto
			session.update(agrupador);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return result;
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
	public Boolean eliminarAgrupadorFacturacion(AgrupadorFacturacion agrupador) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(agrupador);
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
	public List<AgrupadorFacturacion> listarAgrupadorFacturacionFiltradas(AgrupadorFacturacion agrupador, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(agrupador!=null){
	        	if(agrupador.getDescripcion() !=null && !"".equals(agrupador.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", agrupador.getDescripcion() + "%"));
	        	if(agrupador.getCodigo() !=null && !"".equals(agrupador.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", agrupador.getCodigo() + "%"));
	        	if(agrupador.getTipoAgrupador() !=null && !"".equals(agrupador.getTipoAgrupador()))
	        		crit.add(Restrictions.ilike("tipoAgrupador", agrupador.getTipoAgrupador() + "%"));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
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
	public AgrupadorFacturacion verificarExistente(AgrupadorFacturacion agrupador){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(agrupador!=null){
	        	if(agrupador.getCodigo() !=null && !"".equals(agrupador.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", agrupador.getCodigo()));
	        	if(agrupador.getClienteEmp() !=null)
	        		crit.add(Restrictions.eq("clienteEmp", agrupador.getClienteEmp()));
	        	if(agrupador.getTipoAgrupador() !=null && !"".equals(agrupador.getTipoAgrupador()))
	        		crit.add(Restrictions.eq("tipoAgrupador", agrupador.getTipoAgrupador()));
        	}
        	if(agrupador.getClienteAsp() != null){
        		crit.add(Restrictions.eq("clienteAsp", agrupador.getClienteAsp()));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<AgrupadorFacturacion> result=crit.list();
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
	
	public Boolean setHabilitado(AgrupadorFacturacion agrupador, Session session)throws RuntimeException{
		List<AgrupadorFacturacion> listAgrupador = new ArrayList<AgrupadorFacturacion>();
		 try {
	        	Criteria crit = session.createCriteria(getClaseModelo());
	        	if(agrupador.getClienteAsp() != null)
	        		crit.add(Restrictions.eq("clienteAsp", agrupador.getClienteAsp()));
	        	if(agrupador.getClienteEmp() != null)
	        		crit.add(Restrictions.eq("clienteEmp", agrupador.getClienteEmp()));
	        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	listAgrupador =  crit.list();
	        	for(AgrupadorFacturacion actualizar : listAgrupador){
	        		actualizar.setHabilitado(false);
	        		if(agrupador.getId() != null && !agrupador.getId().equals(actualizar.getId())){
	        			session.update(actualizar);
	        			session.flush();
	        			session.clear();
	        		}
	        	}
	        	return true;
	        } catch (HibernateException hibernateException) {
	        	logger.error("No se pudo listar ", hibernateException);
		        return false;
	        }
   }
}
