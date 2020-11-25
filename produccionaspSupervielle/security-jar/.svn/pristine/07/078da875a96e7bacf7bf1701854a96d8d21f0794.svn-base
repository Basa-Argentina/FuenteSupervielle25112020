/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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

import com.security.accesoDatos.configuraciongeneral.interfaz.HojaRutaService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.HojaRuta;
import com.security.modelo.configuraciongeneral.HojaRutaOperacionElemento;
import com.security.modelo.jerarquias.Operacion;
import com.security.utils.DateUtil;

/**
 * 
 * @author FedeMz
 *
 */
@Component
public class HojaRutaServiceImp extends GestorHibernate<HojaRuta> implements HojaRutaService {
	private static Logger logger=Logger.getLogger(HojaRutaServiceImp.class);
	
	@Autowired
	public HojaRutaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<HojaRuta> getClaseModelo() {
		return HojaRuta.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HojaRuta> obtenerPor(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta) {
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.add(Restrictions.eq("clienteAsp", cliente));
	        
        	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
        		crit.createCriteria("empresa", "emp");
        		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	}
        	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
        		crit.createCriteria("sucursal", "suc");
        		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
        	}
        	if(codigoCliente!=null && !codigoCliente.isEmpty()){
        		crit.createCriteria("clienteEmp", "cli");
        		crit.add(Restrictions.eq("cli.codigo", codigoCliente));
        	}
        	if(codigoDesde!=null)
        		crit.add(Restrictions.ge("id", codigoDesde));
        	if(codigoHasta!=null && codigoHasta.intValue()!=0)
        		crit.add(Restrictions.le("id", codigoHasta));
        	if(fechaDesde!=null){
        		crit.add(Restrictions.ge("fechaRegistro", DateUtil.getDateFrom(fechaDesde)));
        	}
        	if(fechaHasta!=null){
        		crit.add(Restrictions.le("fechaRegistro", DateUtil.getDateTo(fechaHasta)));
        	}

	        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        return crit.list();
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
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
	public List<HojaRuta> listarHojaRutaFiltradas(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, 
			Date fechaDesde, Date fechaHasta, String codigoSerie, BigInteger serieDesde,
			BigInteger serieHasta, String estado, Integer codigoTransporte) {
		
		Session session = null;
		
        try {
        	List<Long> ids = obtenerIDsLoteReferenciaFiltradas(cliente, codigoEmpresa,codigoSucursal,
        			fechaDesde,fechaHasta , codigoSerie, serieDesde,
        			serieHasta, estado, codigoTransporte);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<HojaRuta>();
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.in("id", ids));
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
	
	private List<Long> obtenerIDsLoteReferenciaFiltradas(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal,
			Date fechaDesde, Date fechaHasta, String codigoSerie, BigInteger serieDesde,
			BigInteger serieHasta, String estado, Integer codigoTransporte) {
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
	       
	        
	        crit.add(Restrictions.eq("clienteAsp", cliente));
	        
        	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
        		crit.createCriteria("empresa", "emp");
        		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	}
        	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
        		crit.createCriteria("sucursal", "suc");
        		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
        	}
//        	if(codigoCliente!=null && !codigoCliente.isEmpty()){
//        		crit.createCriteria("clienteEmp", "cli");
//        		crit.add(Restrictions.eq("cli.codigo", codigoCliente));
//        	}
        	if(fechaDesde!=null){
        		crit.add(Restrictions.ge("fecha", DateUtil.getDateFrom(fechaDesde)));
        	}
        	if(fechaHasta!=null){
        		crit.add(Restrictions.le("fecha", DateUtil.getDateTo(fechaHasta)));
        	}

        	// Inicio Nuevo parametros.
        	// Como filtro serie??
        	if(codigoSerie!=null && !codigoSerie.isEmpty()){
        		crit.createCriteria("serie", "ser");
        		crit.add(Restrictions.eq("ser.codigo", codigoSerie));
        		crit.add(Restrictions.ge("numero", serieDesde));
        		crit.add(Restrictions.le("numero", serieHasta));
        	}
        	
        	
        	if(estado!=null && !estado.isEmpty()){
        		crit.add(Restrictions.eq("estado", estado));
        	}
        	// Como filtro transporte, falta agregar relacion??
        	if(codigoTransporte!=null){
        		crit.createCriteria("transporte", "trans");
        		crit.add(Restrictions.eq("trans.codigo", codigoTransporte));
        	}
        	
        	// Fin Nuevo parametros.
        	
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
	public Integer contarObtenerPor(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta) {
		
		Session session = null;
		Integer result = null;
		
	    try {
	    	//obtenemos una sesión
	    	session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.setProjection(Projections.rowCount());
	       
	        
	        crit.add(Restrictions.eq("clienteAsp", cliente));
	        
        	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
        		crit.createCriteria("empresa", "emp");
        		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	}
        	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
        		crit.createCriteria("sucursal", "suc");
        		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
        	}
        	if(codigoCliente!=null && !codigoCliente.isEmpty()){
        		crit.createCriteria("clienteEmp", "cli");
        		crit.add(Restrictions.eq("cli.codigo", codigoCliente));
        	}
        	if(codigoDesde!=null)
        		crit.add(Restrictions.ge("id", codigoDesde));
        	if(codigoHasta!=null && codigoHasta.intValue()!=0)
        		crit.add(Restrictions.le("id", codigoHasta));
        	if(fechaDesde!=null){
        		crit.add(Restrictions.ge("fechaRegistro", DateUtil.getDateFrom(fechaDesde)));
        	}
        	if(fechaHasta!=null){
        		crit.add(Restrictions.le("fechaRegistro", DateUtil.getDateTo(fechaHasta)));
        	}

	        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        result = ((Integer)crit.list().get(0));
	        return result;
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
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
	 * Guarda o actualiza un LoteReferencia en la base de datos y todas sus relaciones
	 * @param objeto
	 */
	public synchronized void guardarActualizar(HojaRuta hojaRuta){
		guardarActualizar(hojaRuta, null);
	}
	
	@Override
	public synchronized void guardarActualizar(HojaRuta hojaRuta, List<Operacion> operaciones){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			if(operaciones!=null && operaciones.size()>0){
				for(Operacion op: operaciones){
					session.update(op);
				}
			}
			
			if(hojaRuta.getId()==null || hojaRuta.getId().equals(0L)){
				hojaRuta.setId(null);
				session.save(hojaRuta);
			}else{
				session.update(hojaRuta);
			}
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre algún error intentamos hacer rollback
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public void eliminar(long idHojaRuta){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			tx.begin();
			HojaRuta hojaRuta=(HojaRuta) session.get(HojaRuta.class, idHojaRuta);
			session.delete(hojaRuta);
			//hacemos commit a los cambios para que se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre algún error, se hace rollback a los cambios.
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
	}
	
	@Override
	public HojaRuta obtenerPorId(long id){
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
	        HojaRuta hojaRuta = (HojaRuta)session.get(getClaseModelo(),id);
	        //cargar referencias...
	        for(HojaRutaOperacionElemento hroe : hojaRuta.getOperacionesElementos())
	        	hroe.getOperacionElemento();
	        return hojaRuta;
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
}
