package com.security.accesoDatos.hibernate;



import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.UserLoginService;
import com.security.modelo.seguridad.User;
import com.security.modelo.seguridad.UserLogin;

/**
 * 
 * @author Gabriel Mainero
 * @modificado Ezequiel Beccaria
 * 
 */
@Component
public class UserLoginServiceImp extends GestorHibernate<UserLogin> implements UserLoginService {
	private static Logger logger = Logger.getLogger(UserLoginServiceImp.class);

	@Autowired
	public UserLoginServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getClaseModelo() {
		return UserLogin.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserLogin> listarPorIps(String ip){
		Session session = null;
		try{
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
	    	if(ip!=null)
	        	crit.add(Restrictions.eq("ip", ip));
	    	crit.addOrder(Order.desc("dateLogin"));
	    	return crit.list();
		}catch(Exception e){
			logger.error("no se pudo listar",e);
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
	public List<UserLogin> listarPorUser(User user) {
		Session session = null;
		try{
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
	    	if(user!=null)
	        	crit.add(Restrictions.eq("user", user));	    	
	    	return crit.list();
		}catch(Exception e){
			logger.error("no se pudo listar",e);
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
