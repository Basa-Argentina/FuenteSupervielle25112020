/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.CuentaCorrienteClienteService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.CuentaCorrienteCliente;
import com.security.modelo.configuraciongeneral.ListaPrecios;

/**
 * @author X
 *
 */
@Component
public class CuentaCorrienteClienteServiceImp extends GestorHibernate<CuentaCorrienteCliente> implements CuentaCorrienteClienteService{
	private static Logger logger=Logger.getLogger(CuentaCorrienteClienteServiceImp.class);
	
	@Autowired
	public CuentaCorrienteClienteServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<CuentaCorrienteCliente> getClaseModelo() {
		return CuentaCorrienteCliente.class;
	}

	@Override
	public Boolean guardarCliente(ClienteEmp cliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(cliente);
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
	public Boolean actualizarCliente(ClienteEmp cliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(cliente);
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
	public Boolean eliminarCliente(ClienteEmp cliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(cliente);
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
	public ClienteEmp getByCodigo(ClienteEmp cliente, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("empresa", "emp");
        	if(cliente!=null){
	        	if(cliente.getCodigo() !=null && !"".equals(cliente.getCodigo())){
	        		crit.add(Restrictions.eq("codigo", cliente.getCodigo()));
	        	}
	        	if(cliente.getCodigoEmpresa() != null && cliente.getCodigoEmpresa().length()>0){
	        		crit.add(Restrictions.eq("emp.codigo", cliente.getCodigoEmpresa()));
	        	}
	        	if(cliente.getHabilitado() != null ){
	        		crit.add(Restrictions.eq("habilitado", cliente.getHabilitado()));
	        	}
        	}
        	if(clienteAsp != null){        		
        		crit.add(Restrictions.eq("emp.cliente", clienteAsp));
        	}
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return (ClienteEmp)crit.uniqueResult();
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
	public List<ClienteEmp> listarClienteFiltradas(ClienteEmp cliente, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("empresa", "emp");
        	crit.createCriteria("razonSocial", "ra");
        	if(cliente!=null){
	        	if(cliente.getIdEmpresa() !=null)
	        		crit.add(Restrictions.eq("emp.id", cliente.getIdEmpresa()));
	        	if(cliente.getCodigoEmpresa() !=null && !"".equals(cliente.getCodigoEmpresa()))
	        		crit.add(Restrictions.eq("emp.codigo", cliente.getCodigoEmpresa()));
	        	if(cliente.getCodigo() !=null && !"".equals(cliente.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", cliente.getCodigo() + "%"));
	        	if(cliente.getNombre() !=null && !"".equals(cliente.getNombre()))
	        		crit.add(Restrictions.ilike("nombre", cliente.getNombre() + "%"));
	        	if(cliente.getApellido() !=null && !"".equals(cliente.getApellido()))
	        		crit.add(Restrictions.ilike("apellido", cliente.getApellido() + "%"));
	        	if(cliente.getRaSocial() !=null && !"".equals(cliente.getRaSocial()))
	        		crit.add(Restrictions.ilike("ra.razonSocial", cliente.getRaSocial() + "%"));
        	}
        	if(clienteAsp != null){        		
        		crit.add(Restrictions.eq("emp.cliente", clienteAsp));
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
	
	@Override
	public ClienteEmp verificarExistente(ClienteEmp cliente, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("empresa", "emp");
        	if(cliente!=null){
        		if(cliente.getIdEmpresa() !=null)
        			crit.add(Restrictions.eq("emp.id", cliente.getIdEmpresa()));
	        	if(cliente.getCodigo() !=null && !"".equals(cliente.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", cliente.getCodigo()));
        	}
        	if(clienteAsp != null){        		
        		crit.add(Restrictions.eq("emp.cliente", clienteAsp));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<ClienteEmp> result=crit.list();
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
	public List<ClienteEmp> listarClientesPopup(String val, String codigoEmpresa, ClienteAsp cliente)
	{ return listarClientesPopup(val, codigoEmpresa, cliente, null);}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClienteEmp> listarClientesPopup(String val, String codigoEmpresa, ClienteAsp cliente,Boolean habilitado) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("empresa", "emp");
        	
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa)){
        		c.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	}
        	//filtro cliente
        	c.add(Restrictions.eq("emp.cliente", cliente));
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<ClienteEmp> candidatos = c.list();
        	//filtro por habilitado
        	if(habilitado != null)
        		c.add(Restrictions.eq("habilitado", habilitado));
        	//filtro por razon social o nombre y apellido
        	if(val!=null && candidatos.size()>0){
        		String valores[] = val.split(" ");
        		Iterator<ClienteEmp> it = candidatos.iterator();
        		while(it.hasNext()){
        			ClienteEmp candidato = it.next();
        			boolean matchAll = true;
	        		for(String filtro : valores){
	        			if(candidato.getRazonSocialONombreYApellido().toLowerCase().indexOf(filtro.toLowerCase())==-1){
	        				matchAll=false;
	        				break;
	        			}
	        		}
	        		if(!matchAll)
	        			it.remove();
        		}
        	}
        	return candidatos;
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
	@SuppressWarnings("unchecked")
	public ClienteEmp getByCodigo(String codigo){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(codigo!=null && !"".equals(codigo))
        		crit.add(Restrictions.eq("codigo", codigo));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<ClienteEmp> salida = crit.list();
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
	@Override
	public ClienteEmp getByCodigo(String codigo, String codigoEmpresa,ClienteAsp cliente){
		return getByCodigo(codigo, codigoEmpresa, cliente,null);
	}

	@Override
	public ClienteEmp getByCodigo(String codigo, String codigoEmpresa,ClienteAsp cliente,Boolean habilitado) {
		Session session = null;
        try {
        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	//filtro codigo
        	c.add(Restrictions.eq("codigo", codigo));
        	//filtro codigo empresa
        	c.createCriteria("empresa", "emp");
        	c.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	//filtro cliente
        	c.add(Restrictions.eq("emp.cliente", cliente));
        	//filtro por habilitado
        	if(habilitado != null)
        		c.add(Restrictions.eq("habilitado", habilitado));
        	//distinct
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	//obtener el primero
        	@SuppressWarnings("unchecked")
			List<ClienteEmp> salida = c.list();
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
	
	public ClienteEmp getById(Long id, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(id!=null && id.longValue()!=0){
        		crit.add(Restrictions.eq("id", id));
        	}
        	crit.createCriteria("empresa").add(Restrictions.eq("cliente", clienteAsp));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            ClienteEmp salida = (ClienteEmp)crit.uniqueResult();
            if(salida != null){
            	return salida;
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
	
	@Override
	public ListaPrecios listaPrecioDefectoPorCliente(String codigoCliente, ClienteAsp clienteAsp){

		Session session = null;
		ListaPrecios result = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(clienteAsp!=null){
        		crit.createCriteria("empresa").add(Restrictions.eq("cliente", clienteAsp));
	        	crit.add(Restrictions.eq("codigo", codigoCliente));
	        	crit.setProjection(Projections.max("listaPreciosDefecto"));
        	}
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	result = (ListaPrecios)crit.uniqueResult();
        	
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo obtener el ultimo numero ", hibernateException);
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
}
