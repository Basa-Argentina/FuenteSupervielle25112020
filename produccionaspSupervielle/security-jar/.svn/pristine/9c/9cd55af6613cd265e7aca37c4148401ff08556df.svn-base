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
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class ClienteDireccionServiceImp extends GestorHibernate<ClienteDireccion> implements ClienteDireccionService {
	private static Logger logger=Logger.getLogger(ClienteDireccionServiceImp.class);
	
	@Autowired
	public ClienteDireccionServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<ClienteDireccion> getClaseModelo() {
		return ClienteDireccion.class;
	}

	@Override
	public Boolean guardarClienteDireccion(ClienteDireccion clienteDireccion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(clienteDireccion);
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
	public Boolean actualizarClienteDireccion(ClienteDireccion clienteDireccion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(clienteDireccion);
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
	public Boolean eliminarClienteDireccion(ClienteDireccion clienteDireccion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(clienteDireccion);
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
	public List<ClienteDireccion> listarClienteDireccionesFiltradasPorCliente(ClienteDireccion clienteDireccion, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("cliente", "cli");
        	crit.createCriteria("cli.empresa", "emp");
        	if(clienteDireccion!=null){
        		if(clienteDireccion.getClienteCodigo() !=null && !"".equals(clienteDireccion.getClienteCodigo()))
	        		crit.add(Restrictions.eq("cli.codigo", clienteDireccion.getClienteCodigo()));
	        	if(clienteDireccion.getCodigo() !=null && !"".equals(clienteDireccion.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", clienteDireccion.getCodigo() + "%"));
	        	if(clienteDireccion.getDescripcion() !=null && !"".equals(clienteDireccion.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", clienteDireccion.getDescripcion() + "%"));
	        	if(clienteDireccion.getIdBarrio() !=null && clienteDireccion.getIdBarrio() !=0)
	        		crit.createCriteria("direccion").createCriteria("barrio").add(Restrictions.eq("id", clienteDireccion.getDireccion().getIdBarrio()));
	        	if(clienteDireccion.getIdProvinciaAux() !=null && clienteDireccion.getIdProvinciaAux() !=0)
	        		crit.createCriteria("provinciaAux").add(Restrictions.eq("id", clienteDireccion.getIdProvinciaAux()));
	        	if(clienteDireccion.getIdPaisAux() !=null && clienteDireccion.getIdPaisAux() !=0)
	        		crit.createCriteria("paisAux").add(Restrictions.eq("id", clienteDireccion.getIdPaisAux()));
	        	if(clienteDireccion.getIdLocalidadAux() !=null && clienteDireccion.getIdLocalidadAux() !=0)
	        		crit.createCriteria("localidadAux").add(Restrictions.eq("id", clienteDireccion.getIdLocalidadAux()));
        	}
        	//filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("emp.cliente", cliente));
        	else 
        		crit.add(Restrictions.isNull("emp.cliente"));
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
	public ClienteDireccion verificarExistente(ClienteDireccion clienteDireccion, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("cliente", "cli");
        	crit.createCriteria("cli.empresa", "emp");
        	if(clienteDireccion!=null){
//        		if(clienteDireccion.getClienteCodigo() !=null)
//	        		crit.add(Restrictions.eq("cli.codigo", clienteDireccion.getClienteCodigo()));
	        	if(clienteDireccion.getCodigo() !=null && !"".equals(clienteDireccion.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", clienteDireccion.getCodigo()));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("emp.cliente", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<ClienteDireccion> result=crit.list();
        	if(result.size()>=1)
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClienteDireccion> listarPorId(Long[] ids, ClienteAsp cliente) {
		Session session = null; 
		try {
			//obtenemos una sesión
			session = getSession();
			//Filtramos los id's
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.createCriteria("cliente", "cli");
        	crit.createCriteria("cli.empresa", "emp");
			crit.createCriteria("emp.cliente", "asp");
			crit.add(Restrictions.in("id", ids));
			//Filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("emp.cliente", cliente));
        	else 
        		crit.add(Restrictions.isNull("emp.cliente"));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return crit.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener las direcciones del cliente por identificador",e);
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
	public List<ClienteDireccion> listarDireccionesPopup(String val, ClienteEmp clienteEmp, ClienteAsp cliente){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("cliente", "cli");
        	c.createCriteria("cli.empresa", "emp");
        	//filtro value
        	if(val!=null && !"".equals(val)){        		
        		c.add(Restrictions.ilike("descripcion", val+"%"));
        	}
        	if(clienteEmp!= null){
        		c.add(Restrictions.eq("cli.id", clienteEmp.getId()));
        	}
        	if(cliente != null){
	        	//filtro cliente
	        	c.add(Restrictions.eq("emp.cliente", cliente));
        	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar las sucursales.", hibernateException);
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
	public ClienteDireccion getByCodigo(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.createCriteria("cliente", "cli");
//        	crit.createCriteria("cli.empresa", "emp");
//        	
//        	crit.add(Restrictions.eq("codigo", codigo));
//        	
//        	if(cliente != null)
//        		crit.add(Restrictions.eq("emp.cliente", cliente));
			
			String consulta = "SELECT cd FROM ClienteDireccion cd " +
							  "WHERE cd.codigo = '" + codigo + "' ";
							  if(cliente != null) {
								  consulta += "AND cd.cliente.empresa.cliente.id = " + cliente.getId().longValue() + " ";
							  }
							  
            ClienteDireccion cliDir = (ClienteDireccion) session.createQuery(consulta).uniqueResult();
            
            return cliDir;
            
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
	public ClienteDireccion obtenerPorCodigo(String codigo, String codigoCliente, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("cliente", "cli");
        	crit.createCriteria("cli.empresa", "emp");
        	//Filtro por codigo
        	crit.add(Restrictions.eq("codigo", codigo));
        	//Filtro por ClienteEmp
        	crit.add(Restrictions.eq("cli.codigo", codigoCliente));
        	//Filtro por ClienteAsp
        	if(cliente != null)
        	crit.add(Restrictions.eq("emp.cliente", cliente));
        	
            return (ClienteDireccion) crit.uniqueResult();
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
	public ClienteDireccion obtenerPorCodigo(String codigo, ClienteEmp clienteEmp, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("cliente", "cli");
        	crit.createCriteria("cli.empresa", "emp");
        	//Filtro por codigo
        	crit.add(Restrictions.eq("codigo", codigo));
        	//Filtro por ClienteEmp
        	if(clienteEmp!= null){
        		crit.add(Restrictions.eq("cli.id", clienteEmp.getId()));
        	}
        	//Filtro por ClienteAsp
        	if(cliente != null){
	        	//filtro cliente
        		crit.add(Restrictions.eq("emp.cliente", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<ClienteDireccion> salida = crit.list();
        	if(salida != null && salida.size()>0)
        		return salida.get(0);
        	else
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
	public ClienteDireccion getClienteDireccionById(Long id) {
		
		Session session = null;
		
		try
		{
			session = getSession();
			
			String consulta = "SELECT cd FROM ClienteDireccion cd WHERE cd.id = " + id.longValue() + " ";
			
			ClienteDireccion clienteDir = (ClienteDireccion) session.createQuery(consulta).uniqueResult();
			Hibernate.initialize(clienteDir.getCliente());
			
			return clienteDir;
			
		}catch (HibernateException hibernateException) {
			
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
