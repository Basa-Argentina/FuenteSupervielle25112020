package com.security.accesoDatos.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.AuthorityService;
import com.security.modelo.seguridad.Authority;

/**
 * 
 * @author Gabriel Mainero
 * 
 */
@Component
public class AuthorityServiceImp extends GestorHibernate<Authority> implements AuthorityService {
	private static Logger logger=Logger.getLogger(AuthorityServiceImp.class);

	@Autowired
	public AuthorityServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class getClaseModelo() {
		return Authority.class;
	}

	
	@Override
	public List<Authority> listarTodosAuthorityFiltrados(Authority authority){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(authority.getAuthority()!=null && authority.getAuthority().equals("")==false)
        		crit.add(Restrictions.ilike("authority", authority.getAuthority() + "%"));	
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
	public boolean eliminar(String authority){
		Transaction tx = null;
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			tx.begin();
			//si queremos utilizar el método delete de session, debemos
			//tener el objeto cargado.
			Object objeto = obtenerPorAuthority(authority);
			//borramos el objeto
			session.delete(objeto);
			//hacemos commit a los cambios para que se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			//si ocurre algún error, se hace rollback a los cambios.
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo eliminar", e);
		        return false;
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		return true;
		
	}
	
	@Override
	public Authority obtenerPorAuthority(String authority) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.add(Restrictions.eq("authority", authority)); 
			List<Authority> result=crit.list();
			if(result.size()==1)
				return result.get(0);
			return null;
		} catch (HibernateException e) {
			logger.error("no se pudo obtener el authority",e);
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
	public List<Authority> listarPorAuthority(String[] authorities) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.add(Restrictions.in("authority", authorities)); 
			return crit.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener el authority",e);
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
	public List<Authority> listAuthorityExceptAuthority(String authority) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(authority != null && !"".equals(authority))
        		crit.add(Restrictions.ne("authority", authority));	
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
