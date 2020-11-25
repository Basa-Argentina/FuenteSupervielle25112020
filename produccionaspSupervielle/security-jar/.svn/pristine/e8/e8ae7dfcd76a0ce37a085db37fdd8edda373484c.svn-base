/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class SucursalServiceImp extends GestorHibernate<Sucursal> implements SucursalService {
	private static Logger logger=Logger.getLogger(SucursalServiceImp.class);
	
	@Resource(mappedName = "java:/comp/env/jdbc/basa")
    protected DataSource dataSource;
	
	@Autowired
	public SucursalServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Sucursal> getClaseModelo() {
		return Sucursal.class;
	}

	@Override
	public Boolean guardarSucursal(Sucursal sucursal) {
		Boolean result = true;
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();			
			//Seteamos todas las Sucursales del cliente como no principal 
			if(sucursal.getPrincipal())
				result = setSucursalPrincipal(sucursal, session);
			
			session.save(sucursal.getDireccion());
			//guardamos el objeto
			session.save(sucursal);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return result;
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
	public Boolean actualizarSucursal(Sucursal sucursal) {
		Boolean result = true;
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//Seteamos todas las Sucursales del cliente como no principal 
			if(sucursal.getPrincipal()){
				result = setSucursalPrincipal(sucursal, session);
			}
			session.update(sucursal.getDireccion());			
			//guardamos el objeto
			session.update(sucursal);					
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return result;
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
	public Boolean eliminarSucursal(Sucursal sucursal) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(sucursal);
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
	public Sucursal getByCodigo(String codigo, Empresa empresa) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
        	if(empresa != null){
        		crit.add(Restrictions.eq("empresa", empresa));
        	}
            return (Sucursal) crit.uniqueResult();
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
	
	public Sucursal getById(Long id){
		Session session = null;
        if(id!=null){
			try {
	        	//obtenemos una sesión
				session = getSession();
				
//	        	Criteria crit = session.createCriteria(getClaseModelo());
//	        	crit.add(Restrictions.eq("id", id));
				
				String consulta = "SELECT suc FROM Sucursal suc WHERE suc.id = " + id.longValue() + " ";
	        	
	            Sucursal sucursal = (Sucursal) session.createQuery(consulta).uniqueResult();
	            
	            return sucursal;
	            
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
        }else{
        	return null;
        }
	}
	
	@Override
	public List<Sucursal> listarSucursalFiltradas(Sucursal sucursal, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("empresa", "emp");
        	if(sucursal!=null){
	        	if(sucursal.getIdEmpresa() !=null)
	        		crit.add(Restrictions.eq("emp.id", sucursal.getIdEmpresa()));	        	
	        	if(sucursal.getDescripcion() !=null && !"".equals(sucursal.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", sucursal.getDescripcion() + "%"));
	        	if(sucursal.getCodigo() !=null && !"".equals(sucursal.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", sucursal.getCodigo() + "%"));	      
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("emp.cliente", cliente));
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
	public Sucursal verificarSucursal(Sucursal sucursal){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("empresa", "emp");
        	crit.createCriteria("emp.cliente", "cli");
        	if(sucursal!=null){
	        	if(sucursal.getEmpresa() !=null){
	        		crit.add(Restrictions.eq("emp.id", sucursal.getEmpresa().getId()));
	        		crit.add(Restrictions.eq("cli.id", sucursal.getEmpresa().getCliente().getId()));
	        	}
	        	if(sucursal.getCodigo() !=null && !"".equals(sucursal.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", sucursal.getCodigo()));	      
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Sucursal> result = crit.list();
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
	
	
	public Boolean setSucursalPrincipal(Sucursal sucursal, Session session)throws RuntimeException{
		List<Sucursal> listSucursal = new ArrayList<Sucursal>();
		 try {
	        	Criteria crit = session.createCriteria(getClaseModelo());
	        	crit.createCriteria("empresa", "emp");
	        	if(sucursal!=null && sucursal.getEmpresa() != null && sucursal.getEmpresa().getCliente() != null){
	        		crit.add(Restrictions.eq("emp.cliente", sucursal.getEmpresa().getCliente()));
	        		crit.add(Restrictions.eq("emp.id", sucursal.getEmpresa().getId()));
	        	}
	        	if(sucursal.getId() != null)
	        		crit.add(Restrictions.ne("id", sucursal.getId()));
	        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	listSucursal =  crit.list();
	        	for(Sucursal actualizar : listSucursal){
	        		actualizar.setPrincipal(false);
	        		if(sucursal.getId() != null && !sucursal.getId().equals(actualizar.getId())){
	        			session.update(actualizar);
	        			session.flush();
	        			session.clear();
	        		}
	        	}
	        	return true;
	        } catch (HibernateException hibernateException) {
	        	logger.error("No se pudo listar ", hibernateException);
		        return false;
	        }
    }
	
	@Override
	public List<Sucursal> listarSucursalPopup(String val, String codigoEmpresa, ClienteAsp cliente){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("empresa", "emp");
        	//filtro value
        	if(val!=null && !"".equals(val)){        		
        		c.add(Restrictions.ilike("descripcion", val+"%"));
        	}
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa)){
        		c.add(Restrictions.eq("emp.codigo", codigoEmpresa));
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
	public Sucursal getByCodigo(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
        	if(cliente != null)
        		crit.createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
            return (Sucursal) crit.uniqueResult();
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
	public Sucursal getPrincipal(String codigoEmpresa,ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("empresa", "emp");
        	crit.add(Restrictions.eq("principal", true));
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa)){
        		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("emp.cliente", cliente));
            return (Sucursal) crit.uniqueResult();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo encontrar la empresa principal", hibernateException);
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
	public Sucursal getByCodigo(String codigo, String codigoEmpresa,ClienteAsp cliente) {
		Session session = null;
        try {
        	
        	//obtenemos una sesión
			session = getSession();
			
//        	Criteria c = session.createCriteria(getClaseModelo());
//        	//filtro codigo
//        	c.add(Restrictions.eq("codigo", codigo));
//        	//filtro codigo empresa
//        	c.createCriteria("empresa", "emp");
//        	c.add(Restrictions.eq("emp.codigo", codigoEmpresa));
//        	//filtro cliente
//        	c.add(Restrictions.eq("emp.cliente", cliente));
//        	//distinct
//        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        	//obtener el primero
			
			String consulta = "SELECT DISTINCT suc FROM Sucursal suc WHERE suc.codigo = '" + codigo + "' " +
								"AND suc.empresa.codigo = '" + codigoEmpresa + "' " +
								"AND suc.empresa.cliente.id = " + cliente.getId().longValue() + " ";
			
			Query query = session.createQuery(consulta);
			
        	List<Sucursal> salida = query.list();
            if(salida != null && salida.size()>0)
            	return salida.get(0);
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
