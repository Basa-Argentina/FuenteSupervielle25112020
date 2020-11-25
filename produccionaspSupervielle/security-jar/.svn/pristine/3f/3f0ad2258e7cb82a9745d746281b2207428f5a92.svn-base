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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Grupo;

/**
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (16/08/2011)
 *
 */
@Component
public class GrupoServiceImp extends GestorHibernate<Grupo> implements GrupoService {
	private static Logger logger=Logger.getLogger(GrupoServiceImp.class);
	
	@Autowired
	public GrupoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Grupo> getClaseModelo() {
		return Grupo.class;
	}

	@Override
	public Boolean guardarGrupo(Grupo grupo) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(grupo);
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
	public Boolean actualizarGrupo(Grupo grupo) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(grupo);
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
	public Boolean eliminarGrupo(Grupo grupo) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(grupo);
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
	public Grupo getByCodigo(String codigo) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
            return (Grupo) crit.uniqueResult();
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
	public List<Grupo> listarGrupoFiltradas(Grupo grupo, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("seccion", "sec");
        	if(grupo!=null){
        		if(grupo.getIdSeccion() !=null && grupo.getIdSeccion() != 0)
	        		crit.add(Restrictions.eq("sec.id", grupo.getIdSeccion()));
        		if(grupo.getCodigoSeccion() !=null && !"".equals(grupo.getDescripcion()))
	        		crit.add(Restrictions.eq("sec.codigo", grupo.getCodigoSeccion()));	   
	        	if(grupo.getDescripcion() !=null && !"".equals(grupo.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", grupo.getDescripcion() + "%"));
	        	if(grupo.getCodigo() !=null && !"".equals(grupo.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", grupo.getCodigo() + "%"));	      
        	}
        	
        	if(cliente != null)
        		crit.createCriteria("sec.deposito").createCriteria("sucursal").
        			createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	
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
	public Grupo verificarGrupo(Grupo grupo, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("seccion", "sec");
        	if(grupo!=null){
        		if(grupo.getIdSeccion() !=null && grupo.getIdSeccion() != 0)
	        		crit.add(Restrictions.eq("seccion_id", grupo.getIdSeccion()));
        		if(grupo.getCodigo() !=null && !"".equals(grupo.getCodigo()))
        			crit.add(Restrictions.eq("codigo", grupo.getCodigo()));
        	}
        	if(cliente != null)
        		crit.createCriteria("sec.deposito").createCriteria("sucursal").
    			createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Grupo> result = crit.list();
        	if(result.size() == 1){
        		return result.get(0);
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
