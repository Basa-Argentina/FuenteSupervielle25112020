/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/07/2011
 */
package com.security.accesoDatos.jerarquias.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.RequerimientoReferencia;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class RequerimientoServiceImp extends GestorHibernate<Requerimiento> implements RequerimientoService{
	private static Logger logger=Logger.getLogger(RequerimientoServiceImp.class);
	
	@Autowired
	public RequerimientoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	

	@Override
	public Class<Requerimiento> getClaseModelo() {
		return Requerimiento.class;
	}

	@Override
	public synchronized boolean delete(Requerimiento objeto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(objeto);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Eliminar");
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
	public boolean delete(Requerimiento objeto, List<Operacion> operaciones) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			if(operaciones!=null){
				for(Operacion operacion:operaciones)
					session.delete(operacion);
			}
			//eliminamos el objeto
			session.delete(objeto);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Eliminar");
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
	public synchronized boolean save(Requerimiento objeto, Serie serie, Set<RequerimientoReferencia> detalle, List<Operacion> operaciones) {
		Session session = null;
		Transaction tx = null;
		try {
			Hashtable<Operacion, Set<OperacionElemento>> hashOperacion = new Hashtable<Operacion, Set<OperacionElemento>>();
			objeto.setListaElementos(null);
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(objeto);
			//Actualizo la serie
			session.update(serie);
			//Si se selecciono q cambie la direccion de defecto del personal actualizo el personal
			if(objeto.isCambioDireccionDefecto()){
				Empleado empleado = objeto.getEmpleadoSolicitante();
				if(empleado!=null){
					empleado.setDireccionDefecto(objeto.getDireccionDefecto());
					session.update(empleado);
				}
			}
			//Recorro las operaciones, las registro y armo una hash de elementos para guardarlos en el segundo commit
			for(Operacion operacion:operaciones){
				if(operacion.getListaElementos()!=null)
					hashOperacion.put(operacion, operacion.getListaElementos());
				operacion.setListaElementos(null);
				session.save(operacion);
			}
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			//guardamos los detalles
			
			tx.begin();
			if(detalle!=null){
				objeto.setListaElementos(new HashSet<RequerimientoReferencia>());
				for(RequerimientoReferencia requerimientoReferencia:detalle){
					requerimientoReferencia.setRequerimiento(objeto);
					objeto.getListaElementos().add(requerimientoReferencia);
					//Agregado para poder cargar requerimientos de cajas vacias sin ClienteEmp
					if(requerimientoReferencia.getElemento().getClienteEmp()==null){
						requerimientoReferencia.getElemento().setClienteEmp(objeto.getClienteEmp());
						session.update(requerimientoReferencia.getElemento());
					}
				}
			}
			for(Operacion operacion:operaciones){
				operacion.setListaElementos(hashOperacion.get(operacion));
				session.update(operacion);
			}
			session.update(objeto);
			tx.commit();
			
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Guardar");
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
	public synchronized boolean update(Requerimiento objeto, Set<RequerimientoReferencia> detalle, Set<RequerimientoReferencia> detalleOld) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			if(detalleOld!=null){
				for (RequerimientoReferencia req : detalleOld) {
					try {
						session.delete(req);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
				objeto.setListaElementos(new HashSet<RequerimientoReferencia>());
			}
			session.update(objeto);
			tx.commit();
			//guardamos los detalles
			
			tx.begin();
			if(detalle!=null){
				for(RequerimientoReferencia requerimientoReferencia:detalle){
					requerimientoReferencia.setId(null);
					requerimientoReferencia.setRequerimiento(objeto);
					objeto.getListaElementos().add(requerimientoReferencia);
					//Agregado para poder cargar requerimientos de cajas vacias sin ClienteEmp
					if(requerimientoReferencia.getElemento().getClienteEmp()==null){
						requerimientoReferencia.getElemento().setClienteEmp(objeto.getClienteEmp());
						session.update(requerimientoReferencia.getElemento());
					}
				}
				session.update(objeto);
			}
			//Si se selecciono q cambie la direccion de defecto del personal actualizo el personal
			if(objeto.isCambioDireccionDefecto()){
				Empleado empleado = objeto.getEmpleadoSolicitante();
				if(empleado!=null){
					empleado.setDireccionDefecto(objeto.getDireccionDefecto());
					session.update(empleado);
				}
			}
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
	public synchronized boolean update(Requerimiento objeto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			session.update(objeto);
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
	public synchronized boolean update(Requerimiento objeto, List<Operacion> operaciones) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			session.update(objeto);
			if(operaciones!=null){
				for(Operacion operacion:operaciones)
					session.update(operacion);
			}
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Requerimiento> listarRequerimientoFiltradas(Requerimiento requerimiento, ClienteAsp cliente){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsRequerimientoFiltradas(requerimiento, cliente);
        	
        	//obtenemos una sesión
			session = getSession();
			
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Requerimiento>();
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.in("id", ids));
        	//Ordenamos
        	if(requerimiento.getFieldOrder()!=null && requerimiento.getSortOrder()!=null){
        		String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		if("tipoRequerimiento.descripcion".equals(requerimiento.getFieldOrder())){
        			crit.createCriteria("tipoRequerimiento", "tipo");
        			fieldOrdenar = "tipo.descripcion";
        		}
        		if("requerimiento.prefijoStr".equals(requerimiento.getFieldOrder())){
        			fieldOrdenar = "prefijo";
        			fieldOrdenar2 = "numero";
        		}
        
        		if("fechaHoraAltaStr".equals(requerimiento.getFieldOrder())){
        			fieldOrdenar = "fechaAlta";
        			fieldOrdenar2 = "horaAlta";
        		}
        		if("fechaHoraEntregaStr".equals(requerimiento.getFieldOrder())){
        			fieldOrdenar = "fechaEntrega";
        			fieldOrdenar2 = "horaEntrega";
        		}
        		
        		if("estado".equals(requerimiento.getFieldOrder()))
        			fieldOrdenar = "estado";
        		
        		if("1".equals(requerimiento.getSortOrder())){
        			if(!"".equals(fieldOrdenar))
    					crit.addOrder(Order.asc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.asc(fieldOrdenar2));
    			}else if("2".equals(requerimiento.getSortOrder())){
    				if(!"".equals(fieldOrdenar))
    					crit.addOrder(Order.desc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.desc(fieldOrdenar2));
    			}
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
	public Integer contarRequerimientoFiltradas(Requerimiento requerimiento, ClienteAsp cliente){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
        	
        	crit.createCriteria("clienteEmp", "cli");
        	crit.createCriteria("tipoRequerimiento", "tipo");
        	crit.createCriteria("direccionDefecto", "dir");
        	crit.createCriteria("empleadoSolicitante", "emp");
        	crit.createCriteria("serie", "serie");
        	
        	if(requerimiento!=null){
        		if(requerimiento != null && requerimiento.getFechaDesde() != null && !"".equals(requerimiento.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaAlta", getDateFrom(requerimiento.getFechaDesde())));
        		if(requerimiento != null && requerimiento.getFechaHasta() != null && !"".equals(requerimiento.getFechaHasta()))
        			crit.add(Restrictions.le("fechaAlta", getDateTo(requerimiento.getFechaHasta())));
        		if(requerimiento != null && requerimiento.getFechaEntregaDesde() != null && !"".equals(requerimiento.getFechaEntregaDesde()))
        			crit.add(Restrictions.ge("fechaEntrega", getDateFrom(requerimiento.getFechaEntregaDesde())));
        		if(requerimiento != null && requerimiento.getFechaEntregaHasta() != null && !"".equals(requerimiento.getFechaEntregaHasta()))
        			crit.add(Restrictions.le("fechaEntrega", getDateTo(requerimiento.getFechaEntregaHasta())));
        		if(requerimiento != null && requerimiento.getSerieDesde() != null && !"".equals(requerimiento.getSerieDesde()))
        			crit.add(Restrictions.ge("numero", requerimiento.getSerieDesde()));
        		if(requerimiento != null && requerimiento.getSerieHasta() != null && !"".equals(requerimiento.getSerieHasta()))
        			crit.add(Restrictions.le("numero", requerimiento.getSerieHasta()));
        		if(requerimiento.getEstado()!=null && !requerimiento.getEstado().equals("Todos"))
        			crit.add(Restrictions.eq("estado", requerimiento.getEstado()));
        		
        		if(requerimiento.getClienteCodigo() !=null &&  !"".equals(requerimiento.getClienteCodigo())){
	        		crit.add(Restrictions.eq("cli.codigo", requerimiento.getClienteCodigo()));
        		}
        		if(requerimiento.getTipoRequerimientoCod() !=null &&  !"".equals(requerimiento.getTipoRequerimientoCod())){
	        		crit.add(Restrictions.eq("tipo.codigo", requerimiento.getTipoRequerimientoCod()));
        		}
        		if(requerimiento.getCodigoDireccion() !=null &&  !"".equals(requerimiento.getCodigoDireccion())){
	        		crit.add(Restrictions.eq("dir.codigo", requerimiento.getCodigoDireccion()));
        		}
        		if(requerimiento.getCodigoPersonal() !=null &&  !"".equals(requerimiento.getCodigoPersonal())){
	        		crit.add(Restrictions.eq("emp.codigo", requerimiento.getCodigoPersonal()));
        		}
        		if(requerimiento.getCodigoSerie() !=null &&  !"".equals(requerimiento.getCodigoSerie())){
	        		crit.add(Restrictions.eq("serie.codigo", requerimiento.getCodigoSerie()));
        		}
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	result = ((Integer)crit.list().get(0));
        	return  result;
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
	
	
	private List<Long> obtenerIDsRequerimientoFiltradas(Requerimiento requerimiento, ClienteAsp cliente){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
        	
        	crit.createCriteria("clienteEmp", "cli");
        	crit.createCriteria("tipoRequerimiento", "tipo");
        	crit.createCriteria("direccionDefecto", "dir");
        	crit.createCriteria("empleadoSolicitante", "emp");
        	crit.createCriteria("serie", "serie");
        	
        	if(requerimiento!=null){
        		if(requerimiento != null && requerimiento.getFechaDesde() != null && !"".equals(requerimiento.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaAlta", getDateFrom(requerimiento.getFechaDesde())));
        		if(requerimiento != null && requerimiento.getFechaHasta() != null && !"".equals(requerimiento.getFechaHasta()))
        			crit.add(Restrictions.le("fechaAlta", getDateTo(requerimiento.getFechaHasta())));
        		if(requerimiento != null && requerimiento.getFechaEntregaDesde() != null && !"".equals(requerimiento.getFechaEntregaDesde()))
        			crit.add(Restrictions.ge("fechaEntrega", getDateFrom(requerimiento.getFechaEntregaDesde())));
        		if(requerimiento != null && requerimiento.getFechaEntregaHasta() != null && !"".equals(requerimiento.getFechaEntregaHasta()))
        			crit.add(Restrictions.le("fechaEntrega", getDateTo(requerimiento.getFechaEntregaHasta())));
        		if(requerimiento != null && requerimiento.getSerieDesde() != null && !"".equals(requerimiento.getSerieDesde()))
        			crit.add(Restrictions.ge("numero", requerimiento.getSerieDesde()));
        		if(requerimiento != null && requerimiento.getSerieHasta() != null && !"".equals(requerimiento.getSerieHasta()))
        			crit.add(Restrictions.le("numero", requerimiento.getSerieHasta()));
        		if(requerimiento.getEstado()!=null && !requerimiento.getEstado().equals("Todos"))
        			crit.add(Restrictions.eq("estado", requerimiento.getEstado()));
        		
        		if(requerimiento.getClienteCodigo() !=null &&  !"".equals(requerimiento.getClienteCodigo())){
	        		crit.add(Restrictions.eq("cli.codigo", requerimiento.getClienteCodigo()));
        		}
        		if(requerimiento.getTipoRequerimientoCod() !=null &&  !"".equals(requerimiento.getTipoRequerimientoCod())){
	        		crit.add(Restrictions.eq("tipo.codigo", requerimiento.getTipoRequerimientoCod()));
        		}
        		if(requerimiento.getCodigoDireccion() !=null &&  !"".equals(requerimiento.getCodigoDireccion())){
	        		crit.add(Restrictions.eq("dir.codigo", requerimiento.getCodigoDireccion()));
        		}
        		if(requerimiento.getCodigoPersonal() !=null &&  !"".equals(requerimiento.getCodigoPersonal())){
	        		crit.add(Restrictions.eq("emp.codigo", requerimiento.getCodigoPersonal()));
        		}
        		if(requerimiento.getCodigoSerie() !=null &&  !"".equals(requerimiento.getCodigoSerie())){
	        		crit.add(Restrictions.eq("serie.codigo", requerimiento.getCodigoSerie()));
        		}
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	//Ordenamos
        	if(requerimiento.getFieldOrder()!=null && requerimiento.getSortOrder()!=null){
        		String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		if("tipoRequerimiento.descripcion".equals(requerimiento.getFieldOrder())){
        			//crit.createCriteria("tipoRequerimiento", "tipo");
        			fieldOrdenar = "tipo.descripcion";
        		}
        		if("requerimiento.prefijoStr".equals(requerimiento.getFieldOrder())){
        			fieldOrdenar = "prefijo";
        			fieldOrdenar2 = "numero";
        		}
        
        		if("fechaHoraAltaStr".equals(requerimiento.getFieldOrder())){
        			fieldOrdenar = "fechaAlta";
        			fieldOrdenar2 = "horaAlta";
        		}
        		if("fechaHoraEntregaStr".equals(requerimiento.getFieldOrder())){
        			fieldOrdenar = "fechaEntrega";
        			fieldOrdenar2 = "horaEntrega";
        		}
        		
        		if("estado".equals(requerimiento.getFieldOrder()))
        			fieldOrdenar = "estado";
        		
        		if("1".equals(requerimiento.getSortOrder())){
        			if(!"".equals(fieldOrdenar))
    					crit.addOrder(Order.asc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.asc(fieldOrdenar2));
    			}else if("2".equals(requerimiento.getSortOrder())){
    				if(!"".equals(fieldOrdenar))			
    					crit.addOrder(Order.desc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.desc(fieldOrdenar2));
    			}
        	}

        	//Paginamos
        	if(requerimiento.getNumeroPagina()!=null && requerimiento.getNumeroPagina().longValue()>0 
    				&& requerimiento.getTamañoPagina()!=null && requerimiento.getTamañoPagina().longValue()>0){
    			Integer paginaInicial = (requerimiento.getNumeroPagina() - 1);
    			Integer filaDesde = requerimiento.getTamañoPagina() * paginaInicial;
    			crit.setFirstResult(filaDesde);
    			
    			crit.setMaxResults(requerimiento.getTamañoPagina());
    		}
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
	public List<Requerimiento> listarRequerimientoPopup(String val, ClienteAsp clienteAsp, String codigoCliente, String codigoEmpresa, String codigoSucursal) {
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
        	c.createCriteria("sucursal", "suc");
        	//filtro value
        	if(valores!=null){
        		for(String filtro : valores){
		        	c.add(Restrictions.or(
		        			Restrictions.ilike("codigo", filtro+"%"),
		        			Restrictions.ilike("descripcion", filtro+"%")));
        		}
        	}
        	//filtro por Sucursal
        	if(codigoSucursal != null && !"".equals(codigoSucursal))
        		c.add(Restrictions.eq("suc.codigo", codigoSucursal));
        	//filtro por Empresa
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa))
        		c.createCriteria("suc.empresa", "emp").add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	//filtro por ClienteEmp
        	if(codigoCliente != null && !"".equals(codigoCliente))
        		c.add(Restrictions.eq("cli.codigo", codigoCliente));
        	//filtro por ClienteAsp
        	c.add(Restrictions.eq("clienteAsp", clienteAsp));
        	
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
	public Requerimiento obtenerPorId(Long id, String codigoClienteEmp,String codigoEmpresa, String codigoSucursal, ClienteAsp clienteAsp) {

		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			c.createCriteria("sucursal", "suc");
			//filtro por id
				c.add(Restrictions.eq("id", id));
			//filtro por Sucursal
        	if(codigoSucursal != null && !"".equals(codigoSucursal))
        		c.add(Restrictions.eq("suc.codigo", codigoSucursal));
			//filtro por empresa
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa))
        		c.createCriteria("suc.empresa", "emp").add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	//filtro por ClienteEmp
        	if(codigoClienteEmp != null && !"".equals(codigoClienteEmp))
        		c.createCriteria("clienteEmp", "cli").add(Restrictions.eq("cli.codigo", codigoClienteEmp));
			//filtro por ClienteAsp
			c.add(Restrictions.eq("clienteAsp", clienteAsp));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			if(c.list().size() == 1)
				return (Requerimiento) c.list().get(0);
			return null;
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
	public Requerimiento obtenerPorNumero(Long numero, String codigoClienteEmp,String codigoEmpresa, String codigoSucursal, ClienteAsp clienteAsp) {

		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			c.createCriteria("sucursal", "suc");
			//filtro por id
			c.add(Restrictions.eq("numero", BigInteger.valueOf(numero)));
			//filtro por Sucursal
        	if(codigoSucursal != null && !"".equals(codigoSucursal))
        		c.add(Restrictions.eq("suc.codigo", codigoSucursal));
			//filtro por empresa
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa))
        		c.createCriteria("suc.empresa", "emp").add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	//filtro por ClienteEmp
        	if(codigoClienteEmp != null && !"".equals(codigoClienteEmp))
        		c.createCriteria("clienteEmp", "cli").add(Restrictions.eq("cli.codigo", codigoClienteEmp));
			//filtro por ClienteAsp
			c.add(Restrictions.eq("clienteAsp", clienteAsp));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			if(c.list().size() == 1)
				return (Requerimiento) c.list().get(0);
			return null;
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
	
	private Date getDateFrom(Date from) {
		Calendar calendarInicio = Calendar.getInstance();
		calendarInicio.setTime(from);
		calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
		calendarInicio.set(Calendar.MINUTE, 0);
		calendarInicio.set(Calendar.SECOND, 0);
		calendarInicio.set(Calendar.MILLISECOND, 0);
		return calendarInicio.getTime();
	}

	private Date getDateTo(Date to) {
		Calendar calendarFin = Calendar.getInstance();
		calendarFin.setTime(to);
		calendarFin.set(Calendar.HOUR_OF_DAY, 23);
		calendarFin.set(Calendar.MINUTE, 59);
		calendarFin.set(Calendar.SECOND, 59);
		calendarFin.set(Calendar.MILLISECOND, 999);
		return calendarFin.getTime();
	}
	
}
