package com.security.accesoDatos.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.general.Persona;
import com.security.modelo.seguridad.Group;
import com.security.modelo.seguridad.PasswordHistory;
import com.security.modelo.seguridad.User;
import com.security.modelo.seguridad.UserLogin;

/**
 * 
 * @author Federico Muñoz
 * @Modificado Gabriel Mainero
 * @Modificado Ezequiel Beccaria
 * 
 */
@Component
public class UserServiceImp extends GestorHibernate<User> implements UserService {
	private static Logger logger=Logger.getLogger(UserServiceImp.class);

	@Autowired
	public UserServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<User> getClaseModelo() {
		return User.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> listarTodosUserFiltradosByCliente(User user, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro por usuario
        	if(user!=null){
	        	if(user.getUsername()!=null && !"".equals(user.getUsername()))
	        		c.add(Restrictions.ilike("username", user.getUsername() + "%"));	
	        	Criteria c2 = null;
	        	if(user.getPersona().getNombre()!=null && !"".equals(user.getPersona().getNombre())){
	        		if(c2 == null)
	        			c2 = c.createCriteria("persona");
	        		c2.add(Restrictions.ilike("nombre", user.getPersona().getNombre()+ "%"));	     
	        	}	
	        	if(user.getPersona().getApellido()!=null && !"".equals(user.getPersona().getApellido())){
	        		if(c2 == null)
	        			c2 = c.createCriteria("persona");
	        		c2.add(Restrictions.ilike("apellido", user.getPersona().getApellido()+ "%"));
	        	}	        			        	     	
        	}
        	//filtro por cliente
        	if(cliente != null)
        		c.add(Restrictions.eq("cliente", cliente));
        	else 
        		c.add(Restrictions.isNull("cliente"));
        	//criterio de ordenacion
        	c.addOrder(Order.asc("username"));
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return c.list();
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
	
	/**
	 * Guarda un nuevo usuario en la base de datos y sus asignaciones de grupo 
	 * @param objeto
	 */
	public boolean save(User user){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(user.getPersona());
			session.save(user);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} catch (RuntimeException e) {
			rollback(tx, e, "no se pudo guardar usuario");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	/**
	 * Guarda un nuevo usuario en la base de datos y sus asignaciones de grupo 
	 * @param objeto
	 */
	public boolean update(User user){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(user.getPersona());
			session.update(user);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "no se pudo actualizar usuario");
			return false;			
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	/**
	 * Eliminar un usuario y sus grupos
	 * @param clase
	 * @param id
	 */
	public boolean delete(User user, List<UserLogin> log, List<PasswordHistory> history){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			tx.begin();
			//elimino el registro de login del usuario
			if(log != null)
				for(UserLogin login:log)
					session.delete(login);
			//elimino el registro de passwords
			if(history != null)
				for(PasswordHistory pass:history)
					session.delete(pass);
			//elimino la persona asociada al usuario
			session.delete(user.getPersona());
			//elimino el usuario
			session.delete(user);
			//hacemos commit a los cambios para que se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "no se pudo eliminar usuario");
			return false;				
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
	}
	
	/**
	 * Eliminar un usuario y sus grupos
	 * @param clase
	 * @param id
	 */
	public boolean delete(Long id, List<UserLogin> log, List<PasswordHistory> history){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			tx.begin();
			//elimino el registro de login del usuario
			if(log != null)
				for(UserLogin login:log)
					session.delete(login);			
			//elimino el registro de passwords
			if(history != null)
				for(PasswordHistory pass:history)
					session.delete(pass);
			User user = (User) session.load(getClaseModelo(), id);
			//elimino la persona asociada al usuario
			session.delete(user.getPersona());
			//elimino el usuario
			session.delete(user);
			//hacemos commit a los cambios para que se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "no se pudo eliminar usuario");
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
	public User obtenerPorUsername(String username) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//session.clear();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.add(Restrictions.eq("username", username)); 			
			return (User) crit.uniqueResult();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener el usuario por nombre",e);
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
	public User obtenerPorUsernameRoles(String username, String roles) {
		if(roles==null || roles.equals(""))
			return obtenerPorUsername(username);
		
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
						
			String rolesArr[] = roles.split(",");			
			Criteria c = session.createCriteria(getClaseModelo());
			if(username != null && !"".equals(username))
				c.add(Restrictions.eq("username", username));
			if(roles != null && !"".equals(roles))
				c.createCriteria("groups").createCriteria("authorities").add(Restrictions.in("authority", rolesArr));
			return (User) c.uniqueResult();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener el usuario",e);
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
	public User obtenerPorEMail(String eMail){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.createCriteria("persona").add(Restrictions.eq("mail", eMail));
			return (User) crit.uniqueResult();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener el usuario por eMail",e);
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
	public User obtenerPorIdNoPersonal(Long id){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			
			crit.add(Restrictions.eq("id", id));
			
			//Subconsulta para filtrar los usuarios que no son personal de un cliente
        	DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Empleado.class, "emple");
    		subCritReferencias.setProjection(Property.forName("id"));    	
    		crit.add(Property.forName("id").notIn(subCritReferencias));     
			
			return (User) crit.uniqueResult();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener el usuario por eMail",e);
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
	public List<User> listarPorEMail(String eMail) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.createCriteria("persona").add(Restrictions.eq("mail", eMail));
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return crit.list();	
		} catch (HibernateException e) {
			logger.error("no se pudo obtener el usuario por eMail",e);
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
	public User listarPorPersona(Persona persona) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(persona!=null){
	        	crit.add(Restrictions.eq("persona", persona));
	        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	return (User) crit.uniqueResult();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listarPorGrupo(Group group) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(group!=null){
	        	crit.createCriteria("groups").add(Restrictions.idEq(group.getId()));
	        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	return crit.list();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> listarPopup(String val, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro value
        	Criteria c2 = c.createCriteria("persona");	
        	if(val!=null){
//	        	c.add(Restrictions.ilike("username", val+"%"));
    
	        	c2.add(Restrictions.or(Restrictions.ilike("nombre", val+"%"), Restrictions.ilike("apellido", val+"%")));
	        	//c2.addOrder(Order.desc("apellido"));
        	}
        	//filtro cliente
        	c.add(Restrictions.eq("cliente", cliente));
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	c2.addOrder(Order.asc("apellido"));
        	c2.addOrder(Order.asc("nombre"));
        	return c.list();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> listarPopupNoPersonal(String val, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro value
        	if(val!=null){
//	        	c.add(Restrictions.ilike("username", val+"%"));
	        	Criteria c2 = c.createCriteria("persona");	    
	        	c2.add(Restrictions.or(Restrictions.ilike("nombre", val+"%"), Restrictions.ilike("apellido", val+"%")));
        	}
        	
        	//Subconsulta para filtrar los usuarios que no son personal de un cliente
        	DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Empleado.class, "emple");
    		subCritReferencias.setProjection(Property.forName("id"));    	
    		c.add(Property.forName("id").notIn(subCritReferencias));       	
        	
        	//filtro cliente
        	c.add(Restrictions.eq("cliente", cliente));
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
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
