package com.security.accesoDatos.hibernate;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.ParameterService;
import com.security.modelo.seguridad.Parameter;
import com.security.modelo.seguridad.ParameterBean;
import com.security.recursos.RecursosPassword;

/**
 * 
 * @author Gabriel Mainero
 * @Modificado Ezequiel Beccaria
 * 
 */
@Component
public class ParameterServiceImp implements ParameterService {
	private static Logger logger=Logger.getLogger(ParameterServiceImp.class);
	protected HibernateControl hibernateControl;

	@Autowired
	public ParameterServiceImp(HibernateControl hibernateControl) {
		this.hibernateControl = hibernateControl;
	}
	
	@SuppressWarnings("unchecked")
	public Class getClaseModelo(){
		return ParameterBean.class;
	}
	
	public ParameterBean obtenerPorId(String id){
		Session session = null;
	    try {
	    	session = hibernateControl.getSession();
	        return (ParameterBean)session.get(ParameterBean.class,id);
	    } catch (HibernateException e) {
	        logger.error("ocurrió un error al obtener por id", e);
	    }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	    return null;
	} 
	
	@Override
	public Boolean guardar(Parameter parameter){
		Session session = null;
		Transaction tx = null;
		try {
			session = hibernateControl.getSession();
			tx = session.getTransaction();
			tx.begin();		
			guardar(parameter, session);
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No se puedo guardar");
			return false;
		} catch (IllegalAccessException e) {
			rollback(tx, e, "No se puedo guardar");
			return false;
		}finally{
	        	try{
	        		session.close();
	        	}catch(Exception e){
	        		logger.error("No se pudo cerrar la sesión", e);
	        	}
	        }
	}
	
	@SuppressWarnings("unchecked")
	public void guardar(Parameter parameter, Session session) throws IllegalArgumentException, IllegalAccessException{
		Class parameterClass = parameter.getClass();
		for(Field f:parameterClass.getDeclaredFields()){
			ParameterBean param = new ParameterBean();
			param.setName(f.getName());
			f.setAccessible(true);
			if("passwordSMTP".equals(f.getName())){
				String pass = null;
				try {
					String s = (String) f.get(parameter);
					if(s != null && !"".equals(s))
						pass = RecursosPassword.encrypt(s);
					else
						pass = "";
				} catch (Exception e) {
					e.printStackTrace();
				} 
				param.setValue(pass);
			}else					
				param.setValue(String.valueOf(f.get(parameter)));			
			session.save(param);
			f.setAccessible(false);
		}		
	}
	
	@Override
	public Boolean actualizar(Parameter parameter){
		Session session = null;
		Transaction tx = null;
		try {
			session = hibernateControl.getSession();
			tx = session.getTransaction();
			tx.begin();
			
			ParameterBean parameter1 = obtenerPorId("passwordExpirationDays");
			if(parameter1!=null){
				parameter1.setValue(String.valueOf(parameter.getPasswordExpirationDays()));			
				session.update(parameter1);
			}
			else{
				parameter1 = new ParameterBean();
				parameter1.setName("passwordExpirationDays");
				parameter1.setValue(String.valueOf(parameter.getPasswordExpirationDays()));			
				session.save(parameter1);
			}
			
			ParameterBean parameter2 = obtenerPorId("passwordWarningDays");
			if(parameter2!=null){
				parameter2.setValue(String.valueOf(parameter.getPasswordWarningDays()));			
				session.update(parameter2);
			}
			else{
				parameter2 = new ParameterBean();
				parameter2.setName("passwordWarningDays");
				parameter2.setValue(String.valueOf(parameter.getPasswordWarningDays()));			
				session.save(parameter2);
			}
			
			ParameterBean parameter3 = obtenerPorId("failedLoginCounter");
			if(parameter3!=null){
				parameter3.setValue(String.valueOf(parameter.getFailedLoginCounter()));			
				session.update(parameter3);
			}
			else{
				parameter3 = new ParameterBean();
				parameter3.setName("failedLoginCounter");
				parameter3.setValue(String.valueOf(parameter.getFailedLoginCounter()));			
				session.save(parameter3);
			}
			
			ParameterBean parameter4 = obtenerPorId("minutesSanctionLogin");
			if(parameter4!=null){
				parameter4.setValue(String.valueOf(parameter.getMinutesSanctionLogin()));			
				session.update(parameter4);
			}
			else{
				parameter4 = new ParameterBean();
				parameter4.setName("minutesSanctionLogin");
				parameter4.setValue(String.valueOf(parameter.getMinutesSanctionLogin()));			
				session.save(parameter4);
			}
			
			ParameterBean parameter5 = obtenerPorId("enableOldPassword");
			if(parameter5!=null){
				parameter5.setValue(String.valueOf(parameter.getEnableOldPassword()));			
				session.update(parameter5);
			}
			else{
				parameter5 = new ParameterBean();
				parameter5.setName("enableOldPassword");
				parameter5.setValue(String.valueOf(parameter.getEnableOldPassword()));			
				session.save(parameter5);
			}
			
			ParameterBean parameter6 = obtenerPorId("hostSMTP");
			if(parameter6!=null){
				parameter6.setValue(String.valueOf(parameter.getHostSMTP()));			
				session.update(parameter6);
			}
			else{
				parameter6 = new ParameterBean();
				parameter6.setName("hostSMTP");
				parameter6.setValue(String.valueOf(parameter.getHostSMTP()));			
				session.save(parameter6);
			}
			
			ParameterBean parameter7 = obtenerPorId("portSMTP");
			if(parameter7!=null){
				parameter7.setValue(String.valueOf(parameter.getPortSMTP()));				
				session.update(parameter7);
			}
			else{
				parameter7 = new ParameterBean();
				parameter7.setName("portSMTP");
				parameter7.setValue(String.valueOf(parameter.getPortSMTP()));			
				session.save(parameter7);
			}
			
			ParameterBean parameter8 = obtenerPorId("userSMTP");
			if(parameter8!=null){
				parameter8.setValue(String.valueOf(parameter.getUserSMTP()));				
				session.update(parameter8);
			}
			else{
				parameter8 = new ParameterBean();
				parameter8.setName("userSMTP");
				parameter8.setValue(String.valueOf(parameter.getUserSMTP()));			
				session.save(parameter8);
			}
			
			ParameterBean parameter9 = obtenerPorId("passwordSMTP");
			if(parameter9!=null){
				String pass = null;
				try {
					pass = RecursosPassword.encrypt(parameter.getPasswordSMTP());
				} catch (Exception e) {
					e.printStackTrace();
				} 
				parameter9.setValue(pass);						
				session.update(parameter9);
			}
			else{
				parameter9 = new ParameterBean();
				parameter9.setName("passwordSMTP");
				String pass = null;
				try {
					pass = RecursosPassword.encrypt(parameter.getPasswordSMTP());
				} catch (Exception e) {
					e.printStackTrace();
				} 
				parameter9.setValue(pass);					
				session.save(parameter9);
			}

			ParameterBean parameter10 = obtenerPorId("enableSSLSMTP");
			if(parameter10!=null){
				parameter10.setValue(String.valueOf(parameter.getEnableSSLSMTP()));				
				session.update(parameter10);
			}
			else{
				parameter10 = new ParameterBean();
				parameter10.setName("enableSSLSMTP");
				parameter10.setValue(String.valueOf(parameter.getEnableSSLSMTP()));			
				session.save(parameter10);
			}
			
			ParameterBean parameter11 = obtenerPorId("enableSPASMTP");
			if(parameter11!=null){
				parameter11.setValue(String.valueOf(parameter.getEnableSPASMTP()));				
				session.update(parameter11);
			}
			else{
				parameter11 = new ParameterBean();
				parameter11.setName("enableSPASMTP");
				parameter11.setValue(String.valueOf(parameter.getEnableSPASMTP()));			
				session.save(parameter11);
			}
			
			ParameterBean parameter12 = obtenerPorId("eMailUserSMTP");
			if(parameter12!=null){
				parameter12.setValue(String.valueOf(parameter.geteMailUserSMTP()));					
				session.update(parameter12);
			}
			else{
				parameter12 = new ParameterBean();
				parameter12.setName("eMailUserSMTP");
				parameter12.setValue(String.valueOf(parameter.geteMailUserSMTP()));				
				session.save(parameter12);
			}

			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e,"no se pudo actualizar");
			return false;
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
	public Parameter obtenerParametros(){
		Session session = null;
		try{
			session = hibernateControl.getSession();
			
			session.clear();
			List<ParameterBean> parameters = session.createQuery(String.format("from %1$s",ParameterBean.class.getName())).list();
			if(parameters!=null && parameters.size()>0){
				Parameter parameter = new Parameter();
				for(ParameterBean parameterBean:parameters){
					if(parameterBean.getName().equals("passwordExpirationDays"))
						parameter.setPasswordExpirationDays(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("passwordWarningDays"))
						parameter.setPasswordWarningDays(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("failedLoginCounter"))
						parameter.setFailedLoginCounter(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("minutesSanctionLogin"))
						parameter.setMinutesSanctionLogin(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("enableOldPassword"))
						parameter.setEnableOldPassword(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("hostSMTP"))
						parameter.setHostSMTP(parameterBean.getValue());
					if(parameterBean.getName().equals("portSMTP"))
						parameter.setPortSMTP(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("userSMTP"))
						parameter.setUserSMTP(parameterBean.getValue());
					if(parameterBean.getName().equals("passwordSMTP")){
						String pass = null;
						try {
							pass = RecursosPassword.decrypt(parameterBean.getValue());
						} catch (Exception e) {
							e.printStackTrace();
						} 
						parameter.setPasswordSMTP(pass);
					}
					if(parameterBean.getName().equals("enableSSLSMTP"))
						parameter.setEnableSSLSMTP(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("enableSPASMTP"))
						parameter.setEnableSPASMTP(Integer.parseInt(parameterBean.getValue()));
					if(parameterBean.getName().equals("eMailUserSMTP"))
						parameter.seteMailUserSMTP(parameterBean.getValue());
				}
				return parameter;
			}
			return null;
		}catch(Exception e){
			logger.error("no se pudo obtener",e);
			return null;
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
}
