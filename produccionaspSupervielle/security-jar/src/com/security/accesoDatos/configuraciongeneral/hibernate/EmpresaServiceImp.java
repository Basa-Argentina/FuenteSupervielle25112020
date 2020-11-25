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
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class EmpresaServiceImp extends GestorHibernate<Empresa> implements EmpresaService {
	private static Logger logger=Logger.getLogger(EmpresaServiceImp.class);
	
	@Resource(mappedName = "java:/comp/env/jdbc/basa")
    protected DataSource dataSource;
	
	@Autowired
	public EmpresaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Empresa> getClaseModelo() {
		return Empresa.class;
	}

	@Override
	public Boolean guardarEmpresa(Empresa empresa) {
		Boolean result = true;
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//Seteamos el resto de la empresa como no principales
			if(empresa.getPrincipal())
				result = setEmpresaPrincipal(empresa, session);
			session.save(empresa.getDireccion());
			session.save(empresa.getRazonSocial());
			//guardamos el objeto
			session.save(empresa);
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
	public Boolean actualizarEmpresa(Empresa empresa) {
		Boolean result = true;
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//Seteamos el resto de la empresa como no principales
			if(empresa.getPrincipal())
				result = setEmpresaPrincipal(empresa, session);
			session.update(empresa.getDireccion());
			session.update(empresa.getRazonSocial());
			//guardamos el objeto
			session.update(empresa);			
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
	public Boolean eliminarEmpresa(Empresa empresa) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(empresa);
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
	public Empresa getByCodigo(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.add(Restrictions.eq("codigo", codigo));
//        	if(cliente != null)
//        		crit.add(Restrictions.eq("cliente", cliente));
        	
        	String consulta = "SELECT emp FROM Empresa emp WHERE emp.codigo = '" + codigo + "' ";
        		if(cliente != null)
        			consulta += "AND emp.cliente.id = " + cliente.getId().longValue() + " ";
        		
        	Query query = session.createQuery(consulta);
        	
            return (Empresa) query.uniqueResult();
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
	public Empresa getByCodigoConCondAfip(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
        	if(cliente != null)
        		crit.add(Restrictions.eq("cliente", cliente));
        	
           	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
           	crit.setFetchMode("afipCondIva", FetchMode.JOIN);
        	
//        	String consulta = "SELECT emp FROM Empresa emp JOIN FETCH emp.afipCondIva WHERE emp.codigo = '" + codigo + "' ";
//        		if(cliente != null)
//        			consulta += "AND emp.cliente.id = " + cliente.getId().longValue() + " ";
//        		
//        	Query query = session.createQuery(consulta);
        	
            return (Empresa) crit.uniqueResult();
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
	public Empresa getByCodigoConDireccion(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.add(Restrictions.eq("codigo", codigo));
//        	if(cliente != null)
//        		crit.add(Restrictions.eq("cliente", cliente));
        	
        	String consulta = "SELECT emp FROM Empresa emp WHERE emp.codigo = '" + codigo + "' ";
        		if(cliente != null)
        			consulta += "AND emp.cliente.id = " + cliente.getId().longValue() + " ";
        			
        	Empresa empresa = (Empresa)session.createQuery(consulta).uniqueResult();
        	Hibernate.initialize(empresa.getDireccion());
        	
            return empresa;
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
	public List<Empresa> getByDescripcion(String descripcion, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.like("descripcion", descripcion));
        	if(cliente != null)
        		crit.add(Restrictions.eq("cliente", cliente));
            return (List<Empresa>) crit.list();
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
	public List<Empresa> listarEmpresaFiltradas(Empresa empresa, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(empresa!=null){
	        	if(empresa.getRazonSocial() !=null)
	        		crit.createCriteria("razonSocial").add(Restrictions.ilike("razonSocial", empresa.getRazonSocial().getRazonSocial() + "%"));	        	
	        	if(empresa.getDescripcion() !=null && !"".equals(empresa.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", empresa.getDescripcion() + "%"));
	        	if(empresa.getCodigo() !=null && !"".equals(empresa.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", empresa.getCodigo() + "%"));	      
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("cliente", cliente));
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
	public List<Empresa> listarEmpresaFiltradasConSucursales(Empresa empresa, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			String consulta = "SELECT DISTINCT emp FROM Empresa emp join fetch emp.sucursales WHERE 1 = 1 ";
			if(empresa!=null){
				if(empresa.getRazonSocial() !=null)
					consulta+= " AND emp.razonSocial.razonSocial LIKE '"+ empresa.getRazonSocial().getRazonSocial() + "%'";
				if(empresa.getDescripcion() !=null && !"".equals(empresa.getDescripcion()))
					consulta+= " AND emp.descripcion LIKE '"+ empresa.getDescripcion() + "%'";
				if(empresa.getCodigo() !=null && !"".equals(empresa.getCodigo()))
					consulta+= " AND emp.codigo LIKE '"+ empresa.getCodigo()+ "%'";
			}
			if(cliente != null)
        		consulta+= " AND emp.cliente.id = "+ cliente.getId().longValue()+"";
			
			List<Empresa> empresas = (List<Empresa>)session.createQuery(consulta).list();
						
			return empresas;
			
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
	public Empresa verificarExistente(Empresa empresa){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(empresa!=null){
	        	if(empresa.getCliente() !=null)
	        		crit.createCriteria("cliente").add(Restrictions.eq("id", empresa.getCliente().getId()));	        	
	        	if(empresa.getCodigo() !=null && !"".equals(empresa.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", empresa.getCodigo()));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Empresa> result = crit.list(); 
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
	
	
	public Boolean setEmpresaPrincipal(Empresa empresa, Session session)throws RuntimeException{
		List<Empresa> listEmpresa = new ArrayList<Empresa>();
		 try {
	        	Criteria crit = session.createCriteria(getClaseModelo());
	        	if(empresa.getCliente() != null)
	        		crit.add(Restrictions.eq("cliente", empresa.getCliente()));
	        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	listEmpresa =  crit.list();
	        	for(Empresa actualizar : listEmpresa){
	        		actualizar.setPrincipal(false);
	        		if(empresa.getId() != null && !empresa.getId().equals(actualizar.getId())){
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
	public List<Empresa> listarEmpresaPopup(String descripcion, ClienteAsp cliente){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro value
        	if(descripcion!=null){        		
        		c.add(Restrictions.ilike("descripcion", descripcion+"%"));
        	}
        	if(cliente != null){
	        	//filtro cliente
	        	c.add(Restrictions.eq("cliente", cliente));
        	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar las empresas.", hibernateException);
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
	public Empresa getPrincipal(ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(cliente != null){
        		crit.add(Restrictions.eq("cliente", cliente));
        		crit.add(Restrictions.eq("principal", true));
        	}
            return (Empresa) crit.uniqueResult();
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
	public Empresa getByIDConSucursales(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			String consulta = " SELECT DISTINCT emp FROM Empresa emp WHERE emp.id = "+ id.longValue()+"";
			
			Empresa empresa = (Empresa)session.createQuery(consulta).uniqueResult();
			Hibernate.initialize(empresa.getSucursales());
			
			return empresa;
			
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
	public Empresa getByID(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			String consulta = " SELECT DISTINCT emp FROM Empresa emp WHERE emp.id = "+ id.longValue()+"";
			
			Empresa empresa = (Empresa)session.createQuery(consulta).uniqueResult();
			Hibernate.initialize(empresa.getDireccion());
			
			return empresa;
			
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
