package com.security.accesoDatos.hibernate;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.PasswordHistoryService;
import com.security.modelo.seguridad.PasswordHistory;
import com.security.modelo.seguridad.User;

/**
 * 
 * @author Gabriel Mainero
 * 
 */
@Component
public class PasswordHistoryServiceImp extends GestorHibernate<PasswordHistory> implements PasswordHistoryService {
	private static Logger logger=Logger.getLogger(PasswordHistoryServiceImp.class);

	@Autowired
	public PasswordHistoryServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getClaseModelo() {
		return PasswordHistory.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validarContraseniaAnterior(User user,String contrasenia){
		try {
			//obtenemos una sesión
			Session session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
    		crit.add(Restrictions.eq("user", user));
    		crit.add(Restrictions.eq("oldPassword", contrasenia));
            ArrayList<PasswordHistory> lista = (ArrayList<PasswordHistory>) crit.list();
            if(lista!=null && lista.size() >0)
            	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return false;
        }
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long diasCambioContrasenia(User user){		
		try {
			//obtenemos una sesión
			Session session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("user", user));
        	crit.addOrder(Order.desc("dateChange"));
        	ArrayList<PasswordHistory> salida = (ArrayList<PasswordHistory>) crit.list();
        	if(salida!=null && salida.size()>0){
        		PasswordHistory passwordHistory = salida.get(0);
        		Date fechaDesde= new Date();
        		Date fechaHasta = passwordHistory.getDateChange();
    			long dias =  fechaDesde.getTime() - fechaHasta.getTime();
    			dias = dias / (24 * 60 * 60 * 1000L);
    			return dias;
        	}
        	else{
        		return null;
        	}
        		
		} catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }
	}
}
