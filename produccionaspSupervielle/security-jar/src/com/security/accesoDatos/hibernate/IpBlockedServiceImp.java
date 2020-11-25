package com.security.accesoDatos.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.IpBlockedService;
import com.security.modelo.seguridad.IpBlocked;

/**
 * 
 * @author Gabriel Mainero
 * 
 */
@Component
public class IpBlockedServiceImp extends GestorHibernate<IpBlocked> implements IpBlockedService {
	private static Logger logger = Logger.getLogger(IpBlockedServiceImp.class); 
	@Autowired
	public IpBlockedServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class getClaseModelo() {
		return IpBlocked.class;
	}
	
	@Override
	public List<IpBlocked> listarIpsBlocked(Date timeBlocked, String ip){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			if(timeBlocked!=null){
	        	crit.add(Restrictions.ge("timeBlocked", timeBlocked));
	        }
			if(ip!=null){
	        	crit.add(Restrictions.eq("ip", ip));
	        }
			return crit.list();
		}catch(Exception e){
			logger.error("No se pudo listar ", e);
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
