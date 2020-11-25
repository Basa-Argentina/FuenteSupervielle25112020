/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.exceptions.BasaException;
import com.security.exceptions.ClienteAspNullException;
import com.security.exceptions.ClienteEmpNullException;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.seguridad.User;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class EmpleadoServiceImp extends GestorHibernate<Empleado> implements EmpleadoService {
	private static Logger logger=Logger.getLogger(EmpleadoServiceImp.class);
	
	@Autowired
	public EmpleadoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Empleado> getClaseModelo() {
		return Empleado.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<Empleado> listarTodosEmpleadoFiltradosByCliente(User empleado, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro por usuario
        	if(empleado!=null){
	        	if(empleado.getUsername()!=null && !"".equals(empleado.getUsername()))
	        		c.add(Restrictions.ilike("username", empleado.getUsername() + "%"));	
	        	Criteria c2 = null;
	        	if(empleado.getPersona().getNombre()!=null && !"".equals(empleado.getPersona().getNombre())){
	        		if(c2 == null)
	        			c2 = c.createCriteria("persona");
	        		c2.add(Restrictions.ilike("nombre", empleado.getPersona().getNombre()+ "%"));	     
	        	}	
	        	if(empleado.getPersona().getApellido()!=null && !"".equals(empleado.getPersona().getApellido())){
	        		if(c2 == null)
	        			c2 = c.createCriteria("persona");
	        		c2.add(Restrictions.ilike("apellido", empleado.getPersona().getApellido()+ "%"));
	        	}
	        	Empleado e = (Empleado)empleado;
	        	if(e.getCodigoCliente()!=null && !e.getCodigoCliente().trim().equals("")){
	        		c.createCriteria("clienteEmp", "clienteEmp");
	        		c.add(Restrictions.eq("clienteEmp.codigo", e.getCodigoCliente()));
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

	@Override
	public Boolean guardarEmpleado(Empleado empleado) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(empleado.getPersona());
			session.save(empleado);
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
	public Boolean actualizarEmpleado(Empleado empleado) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(empleado.getPersona());
			session.update(empleado);
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
	public Boolean eliminarEmpleado(Empleado empleado) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(empleado.getPersona());
			session.delete(empleado);
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

	/**
	 * Eliminar un empleado y sus grupos
	 * @param clase
	 * @param id
	 */
	public boolean delete(Long id){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			tx.begin();
			Empleado empleado= (Empleado) session.load(getClaseModelo(), id);
			session.delete(empleado.getPersona());
			session.delete(empleado);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Empleado> listarEmpleadoFiltradas(Empleado empleado, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(empleado!=null){
        		if(empleado.getClienteCodigo() !=null)
	        		crit.createCriteria("clienteEmp").add(Restrictions.eq("codigo", empleado.getClienteCodigo()));
	        	if(empleado.getCodigo() !=null && !"".equals(empleado.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", empleado.getCodigo() + "%"));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("cliente", cliente));
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Empleado verificarExistente(Empleado empleado, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(empleado!=null){
        		//Se comento para la funcionalidad que permite a empleados agregar requerimientos de distintos clientesEmp
//        		if(empleado.getClienteCodigo()!=null && !empleado.getCodigoCliente().equals("") && empleado.getBandera()!= null )
//	        		crit.createCriteria("clienteEmp").add(Restrictions.eq("codigo", empleado.getClienteCodigo()));
	        	if(empleado.getCodigo() !=null && !"".equals(empleado.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", empleado.getCodigo()).ignoreCase());
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("cliente", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Empleado> result=crit.list();
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
	
	@Override
	public Empleado obtenerPorUsername(String username) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//session.clear();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.add(Restrictions.eq("username", username)); 			
			return (Empleado) crit.uniqueResult();
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
	public Empleado obtenerPorCodigo(String codigo, ClienteAsp clienteAsp) {
		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por codigo
			if(codigo != null && !"".equals(codigo))
				c.add(Restrictions.eq("codigo", codigo));
			
			//filtro por cliente
			c.add(Restrictions.eq("cliente", clienteAsp));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (Empleado) c.uniqueResult();		
		} catch (HibernateException e) {
			logger.error("no se pudo obtener lista",e);
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
	public Empleado obtenerPorUsernameRoles(String username, String roles) {
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
			return (Empleado) c.uniqueResult();
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
	public Empleado obtenerPorEMail(String eMail){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.createCriteria("persona").add(Restrictions.eq("mail", eMail));
			return (Empleado) crit.uniqueResult();
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
	public List<Empleado> listarPorId(Long[] ids, ClienteAsp cliente) {
		Session session = null; 
		try {
			//obtenemos una sesión
			session = getSession();
			//Filtramos los id's
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.add(Restrictions.in("id", ids));
			//Filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("cliente", cliente));
        	else 
        		crit.add(Restrictions.isNull("cliente"));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return crit.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener los empleados por identificador",e);
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
	public List<Empleado> listarEmpleadosConCarpetasAsignadas(Long[] ids, ClienteAsp cliente) {
		Session session = null; 
		try {
			//obtenemos una sesión
			session = getSession();
			//Filtramos los id's
			
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.add(Restrictions.in("id", ids));
			crit.setFetchMode("clasificacionesDocumentales", FetchMode.JOIN);
			//Filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("cliente", cliente));
        	else 
        		crit.add(Restrictions.isNull("cliente"));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return crit.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener los empleados por identificador",e);
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
	public List<Empleado> listarEmpleadoPopup(String val, ClienteAsp clienteAsp, String codigoCliente)
	{return listarEmpleadoPopup(val, clienteAsp, codigoCliente, null);}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Empleado> listarEmpleadoPopup(String val, ClienteAsp clienteAsp, String codigoCliente,Boolean habilitado) {
		Session session = null;
		if(clienteAsp == null)
			return null;
        try {
        	String valores[] = null;
        	if(val!=null)
        		valores = val.split(" ");
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("clienteEmp", "cli");
        	c.createCriteria("persona", "per");
        	
        	//filtro value
        	if(valores!=null){
        		for(String filtro : valores){
        			if(!filtro.trim().equals(""))
        			{
        				c.add(Restrictions.or(Restrictions.or(
		        			Restrictions.ilike("per.nombre", "%"+filtro+"%"),
		        			Restrictions.ilike("per.apellido", "%"+filtro+"%")),Restrictions.ilike("codigo", "%"+filtro+"%")));
        			}
		        	
		        }
        	}
        	//filtro por ClienteEmp
        	if(codigoCliente != null && !"".equals(codigoCliente))
        		c.add(Restrictions.eq("cli.codigo", codigoCliente));
        	//filtro por habilitado
        	if(habilitado != null)
        		c.add(Restrictions.eq("habilitado", habilitado));
        	c.add(Restrictions.eq("cliente", clienteAsp));
        	c.addOrder(Order.asc("per.nombre"));
        	c.addOrder(Order.asc("per.apellido"));
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
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Empleado> listarEmpleadosDisponiblesParaCarpeta(Set<Empleado> empleadosNoDisponibles, ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		Session session = null;
		List<Empleado> result = null;
        try {
        	if(clienteAsp == null){
    			throw new ClienteAspNullException();
        	}
        	if(clienteEmp == null){
        		throw new ClienteEmpNullException();
        	}
        	session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.add(Restrictions.eq("clienteEmp",clienteEmp));
        	c.createCriteria("clienteEmp").createCriteria("empresa").add(Restrictions.eq("cliente", clienteAsp));
        	Long[] listIdsEmpleadosNoDisponibles = new Long[empleadosNoDisponibles.size()];
        	
        	Conjunction conjunction = Restrictions.conjunction();
        	for(Empleado emp: empleadosNoDisponibles){
        		conjunction.add(Restrictions.ne("id", emp.getId()));
        		
        	}
        	
        	c.add(conjunction);
        	
        	c.createCriteria("persona").addOrder(Order.asc("apellido")).addOrder(Order.asc("nombre"));
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	result = (List<Empleado>) c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar empleados disponibles para carpeta ", hibernateException);
        	result = null;
        }catch (BasaException e){
        	logger.error("No se pudo listar empleados disponibles para carpeta ", e);
        	result = null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
        return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Empleado> listarEmpleadosAsignadosCarpeta(ClasificacionDocumental carpetaSeleccionada, ClienteEmp clienteEmp, ClienteAsp clienteAsp) {
		Session session = null;
		List<Empleado> result = null;
        try {
        	if(clienteAsp == null){
    			throw new ClienteAspNullException();
        	}
        	if(clienteEmp == null){
        		throw new ClienteEmpNullException();
        	}
        	session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.add(Restrictions.eq("clienteEmp",clienteEmp));
        	c.add(Restrictions.eq("cliente",clienteAsp));
        	c.createCriteria("clienteEmp").createCriteria("empresa").add(Restrictions.eq("cliente", clienteAsp));
        	c.createCriteria("clasificacionesDocumentales").add(Restrictions.idEq(carpetaSeleccionada.getId()));
        	c.createCriteria("persona", "per");
        	c.addOrder(Order.asc("per.apellido"));
        	c.addOrder(Order.asc("per.nombre"));
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
         	result = (List<Empleado>) c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar empleados disponibles para carpeta ", hibernateException);
        	result = null;
        }catch (BasaException e){
        	logger.error("No se pudo listar empleados disponibles para carpeta ", e);
        	result = null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
        return result;
	}
	
	@Override
	public Empleado getByCodigo(String codigoPersonal, String codigoCliente, ClienteAsp clienteAsp)
	{return getByCodigo(codigoPersonal, codigoCliente, clienteAsp, null);}
	
	@Override
	public Empleado getByCodigo(String codigoPersonal, String codigoCliente, ClienteAsp clienteAsp,Boolean habilitado) {
		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			
			
//			Criteria c = session.createCriteria(getClaseModelo());
//			//filtro por codigo
//			if(codigoPersonal != null && !"".equals(codigoPersonal))
//				c.add(Restrictions.eq("codigo", codigoPersonal));
//			//filtro por habilitado
//        	if(habilitado != null)
//        		c.add(Restrictions.eq("habilitado", habilitado));
//			//filtro por ClienteEmp
//			if(codigoCliente != null && !"".equals(codigoCliente))
//				c.createCriteria("clienteEmp").add(Restrictions.eq("codigo", codigoCliente));
//			//filtro por clienteAsp
//			c.add(Restrictions.eq("cliente", clienteAsp));
//			//Seteo propiedades de la consulta
//			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			String consulta = "SELECT DISTINCT emp FROM Empleado emp WHERE 1 = 1 ";
								if(codigoPersonal != null && !"".equals(codigoPersonal))
								{
									consulta += "AND emp.codigo = '" + codigoPersonal + "' ";
								}
								if(habilitado != null)
								{
									consulta += "AND emp.habilitado = " + habilitado + " ";
								}
								if(codigoCliente != null && !"".equals(codigoCliente))
								{
									consulta += "AND emp.clienteEmp.codigo = '" + codigoCliente + "' ";
								}
								consulta += "AND emp.cliente.id = " + clienteAsp.getId().longValue() + " ";
			
			Empleado empleado = (Empleado) session.createQuery(consulta).uniqueResult();
			
			return empleado;
			
		} catch (HibernateException e) {
			logger.error("no se pudo obtener lista",e);
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
	public Empleado getByID(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			String consulta = " SELECT DISTINCT em FROM Empleado em WHERE em.id = "+ id.longValue()+"";
			
			Empleado empleado = (Empleado)session.createQuery(consulta).uniqueResult();
			
			if(empleado!=null){
				Hibernate.initialize(empleado.getClienteEmp());
				Hibernate.initialize(empleado.getClienteEmp().getListaPreciosDefecto());
			}
			
			return empleado;
			
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
