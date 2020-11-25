package com.security.accesoDatos.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.AppLogService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.AppLog;
import com.security.utils.DateUtil;
/**
 * 
 * @author Federico Muñoz
 * @Modificado Gabriel Mainero
 * @Modificado Ezequiel Beccaria
 *
 */
@Component
public class AppLogServiceImp extends GestorHibernate<AppLog> implements AppLogService{
	private static Logger logger = Logger.getLogger(GestorHibernate.class);
	
	@Autowired
	public AppLogServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<AppLog> getClaseModelo() {
		return AppLog.class;
	}	
	
	@Override
	public void guardar(AppLog appLog){
		Transaction tx = null;
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			tx = session.getTransaction();
			tx.begin();
			session.save(appLog);
			tx.commit();
			session.flush(); //TODO: verificar si esto es necesario...
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "no se pudo guardar");
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
	}

	@Override
	public void eliminarInsModAppLog(List<AppLog> lista) {
		Session session = null;
		Transaction tx = null;
		try {
        	session = getSession(); //obtenemos una sesión
        	tx = session.getTransaction(); //creamos la transacción
			tx.begin(); //iniciaos la transacción
			
			for(AppLog appLog:lista){
				session.delete(appLog);
			}
			
			tx.commit(); //finalizamos la transacción y actualiza la base de datos
		}catch (RuntimeException e){
			rollback(tx, e, "no se pudo guardar");			
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
	public List<AppLog> listarTodosAppLogFiltrados(String nivel, Date fecha, String aplicacion, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por nivel
        	if(nivel!=null && !nivel.equals(""))
            	crit.add(Restrictions.eq("nivel", nivel));    
        	//Filtro por fecha
        	if(fecha!=null)
            	crit.add(Restrictions.between("fechaHora", DateUtil.getDateFrom(fecha), DateUtil.getDateTo(fecha)));
        	//filtro por aplicació
            if(aplicacion != null && !"".equals(aplicacion))
            	crit.add(Restrictions.ilike("app", aplicacion + "%"));   
            //filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("cliente", cliente));
        	else 
        		crit.add(Restrictions.isNull("cliente")); 
        	//criterio de ordenación
            crit.addOrder(Order.asc("fechaHora"));
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
