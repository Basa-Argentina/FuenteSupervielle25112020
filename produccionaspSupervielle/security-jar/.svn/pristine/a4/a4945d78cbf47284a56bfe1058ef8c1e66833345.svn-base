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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class DepositoServiceImp extends GestorHibernate<Deposito> implements DepositoService {
	private static Logger logger=Logger.getLogger(DepositoServiceImp.class);
	
	@Autowired
	public DepositoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Deposito> getClaseModelo() {
		return Deposito.class;
	}

	@Override
	public Boolean guardarDeposito(Deposito deposito) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//Guardamos la direccion
			session.save(deposito.getDireccion());
			
			//guardamos el objeto
			session.save(deposito);
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
	public Boolean actualizarDeposito(Deposito deposito) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//Guardamos la direccion
			session.update(deposito.getDireccion());
			//guardamos el objeto
			session.update(deposito);
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
	public Boolean eliminarDeposito(Deposito deposito) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(deposito);
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
	public List<Deposito> listarDepositoFiltradas(Deposito deposito, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("sucursal", "suc");
        	crit.createCriteria("suc.empresa", "emp");
        	if(deposito!=null){
	        	if(deposito.getIdSucursal() !=null)
	        		crit.add(Restrictions.eq("suc.id", deposito.getIdSucursal()));
	        	if(deposito.getCodigoSucursal() !=null)
	        		crit.add(Restrictions.eq("suc.codigo", deposito.getCodigoSucursal()));
	        	if(deposito.getCodigo() !=null && !"".equals(deposito.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", deposito.getCodigo() + "%"));
	        	if(deposito.getDescripcion() !=null && !"".equals(deposito.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", deposito.getDescripcion() + "%"));
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
	public Deposito verificarExistente(Deposito deposito, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("sucursal", "suc");
        	crit.createCriteria("suc.empresa", "emp");
        	if(deposito!=null){
        		if(deposito.getIdSucursal() !=null)
	        		crit.add(Restrictions.eq("suc.id", deposito.getIdSucursal()));
	        	if(deposito.getCodigo() !=null && !"".equals(deposito.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", deposito.getCodigo()));
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("emp.cliente", cliente));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Deposito> result=crit.list();
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
	public List<Deposito> listarDepositoPopup(String val, String codigoSucursal, ClienteAsp cliente){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("sucursal", "suc");
        	//filtro value
        	if(val!=null){        		
        		c.add(Restrictions.ilike("descripcion", val+"%"));        	
        	}
        	if(codigoSucursal != null && !"".equals(codigoSucursal)){
        		c.add(Restrictions.eq("suc.codigo", codigoSucursal));
        	}
        	if(cliente != null){
	        	//filtro cliente
	        	c.createCriteria("suc.empresa").add(Restrictions.eq("cliente", cliente));
        	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar los depositos.", hibernateException);
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
	public Deposito getByCodigo(Deposito deposito, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.createCriteria("sucursal", "suc");
//        	crit.createCriteria("suc.empresa", "emp");
//        	if(deposito!=null){
//	        	if(deposito.getCodigo() !=null && deposito.getCodigo().length()>0){
//	        		crit.add(Restrictions.eq("codigo", deposito.getCodigo()));
//	        	}
//	        	if(deposito.getSucursal()!=null && deposito.getSucursal().getCodigo()!=null && deposito.getSucursal().getCodigo().length()>0){
//	        		crit.add(Restrictions.eq("suc.codigo", deposito.getSucursal().getCodigo()));
//	        	}else if(deposito.getCodigoSucursal()!=null){
//	        		crit.add(Restrictions.eq("suc.codigo", deposito.getCodigoSucursal()));
//	        	}
//        	}        	
//        	if(clienteAsp != null){        		
//        		crit.add(Restrictions.eq("emp.cliente", clienteAsp));
//        	}
//        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
			String consulta = "SELECT DISTINCT dep FROM Deposito dep WHERE 1 = 1 ";
							if(deposito != null) {
								if(deposito.getCodigo() != null && deposito.getCodigo().length() > 0) {
									consulta += "AND dep.codigo = '" + deposito.getCodigo() + "' ";
								}
								if(deposito.getSucursal() != null && deposito.getSucursal().getCodigo() != null && deposito.getSucursal().getCodigo().length() > 0) {
									consulta += "AND dep.sucursal.codigo = '" + deposito.getSucursal().getCodigo() + "' ";
								}
								else if(deposito.getCodigoSucursal() != null) {
									consulta += "AND dep.sucursal.codigo = '" + deposito.getCodigoSucursal() + "' ";
								}
							}
							if(clienteAsp != null) {
								consulta += "AND dep.sucursal.empresa.cliente.id = " + clienteAsp.getId().longValue() + " ";
							}
        	
            Deposito depo = (Deposito) session.createQuery(consulta).uniqueResult();
            
            return depo;
            
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
	public Deposito getByCodigoYSucursal(String codigoDeposito,String codigoSucursal, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.createCriteria("sucursal", "suc");
//        	crit.createCriteria("suc.empresa", "emp");
//        	
//        	crit.add(Restrictions.eq("codigo", codigoDeposito));
//        	crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
//        	crit.add(Restrictions.eq("emp.cliente", clienteAsp));
//        	
//        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	String consulta = "SELECT dep FROM Deposito dep WHERE dep.codigo = '" + codigoDeposito + "' ";
        	
        	if(codigoSucursal!=null && !codigoSucursal.trim().equalsIgnoreCase("") && !codigoSucursal.equalsIgnoreCase("null"))
        		consulta += "AND dep.sucursal.codigo = '" + codigoSucursal + "' ";
        						
        	consulta += "AND dep.sucursal.empresa.cliente.id = " + clienteAsp.getId().longValue() + " ";
        	
        	Query query = session.createQuery(consulta);
        	
            return (Deposito)query.uniqueResult();
        } 
        catch (HibernateException hibernateException) 
        {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }
        finally
        {
        	try
        	{
        		session.close();
        	}
        	catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public Deposito getByCodigo(String codigoDeposito, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("sucursal", "suc");
        	crit.createCriteria("suc.empresa", "emp");
	        
	        crit.add(Restrictions.eq("codigo", codigoDeposito));      		
        	crit.add(Restrictions.eq("emp.cliente", clienteAsp));

        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return (Deposito)crit.uniqueResult();
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

