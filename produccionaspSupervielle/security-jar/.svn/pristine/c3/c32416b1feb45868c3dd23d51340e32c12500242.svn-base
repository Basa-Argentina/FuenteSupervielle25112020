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
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.OperacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.ElementoHistorico;
import com.security.modelo.configuraciongeneral.HojaRuta;
import com.security.modelo.configuraciongeneral.HojaRutaOperacionElemento;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.seguridad.User;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class OperacionServiceImp extends GestorHibernate<Operacion> implements OperacionService{
	private static Logger logger=Logger.getLogger(OperacionServiceImp.class);
	
	@Autowired
	public OperacionServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	

	@Override
	public Class<Operacion> getClaseModelo() {
		return Operacion.class;
	}
	
	@Override
	public Integer contarOperacionFiltradas(Operacion operacion, ClienteAsp cliente){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
        	
        	crit.createCriteria("tipoOperacion", "tipo");
        	crit.createCriteria("deposito", "dep");
        	crit.createCriteria("requerimiento", "req");
        	
        	if(operacion!=null){
        		if(operacion != null && operacion.getCodigoRequerimiento() != null && !"".equals(operacion.getCodigoRequerimiento()))
        			crit.add(Restrictions.eq("req.numero", BigInteger.valueOf(Long.valueOf(operacion.getCodigoRequerimiento()))));
        		if(operacion != null && operacion.getCodigoEmpresa() != null && !"".equals(operacion.getCodigoEmpresa())){
        			crit.createCriteria("req.clienteEmp", "cli");
        			crit.createCriteria("cli.empresa", "emp").add(Restrictions.eq("emp.codigo", operacion.getCodigoEmpresa()));}
        		if(operacion != null && operacion.getCodigoSucursal() != null && !"".equals(operacion.getCodigoSucursal())){
        			crit.createCriteria("req.sucursal", "suc").add(Restrictions.eq("suc.codigo", operacion.getCodigoSucursal()));}
        		if(operacion != null && operacion.getClienteCodigo() != null && !"".equals(operacion.getClienteCodigo())){
        			crit.createCriteria("clienteEmp", "cliE").add(Restrictions.eq("cliE.codigo", operacion.getClienteCodigo()));}
        		if(operacion != null && operacion.getCodigoPersonal() != null && !"".equals(operacion.getCodigoPersonal())){
        			crit.createCriteria("req.empleadoSolicitante", "empl").add(Restrictions.eq("empl.codigo", operacion.getCodigoPersonal()));}
        		if(operacion != null && operacion.getCodigoDireccion() != null && !"".equals(operacion.getCodigoDireccion())){
        			crit.createCriteria("req.direccionDefecto", "dir").add(Restrictions.eq("dir.codigo", operacion.getCodigoDireccion()));}
        		if(operacion != null && operacion.getIdDesde() != null)
        			crit.add(Restrictions.ge("id", operacion.getIdDesde()));
        		if(operacion != null && operacion.getIdHasta() != null)
        			crit.add(Restrictions.le("id", operacion.getIdHasta()));
        		if(operacion != null && operacion.getFechaHasta() != null && !"".equals(operacion.getFechaHasta()))
        			crit.add(Restrictions.le("fechaAlta", getDateTo(operacion.getFechaHasta())));
        		if(operacion != null && operacion.getFechaDesde() != null && !"".equals(operacion.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaAlta", getDateFrom(operacion.getFechaDesde())));
        		if(operacion != null && operacion.getFechaHasta() != null && !"".equals(operacion.getFechaHasta()))
        			crit.add(Restrictions.le("fechaAlta", getDateTo(operacion.getFechaHasta())));
        		if(operacion != null && operacion.getFechaEntregaDesde() != null && !"".equals(operacion.getFechaEntregaDesde()))
        			crit.add(Restrictions.ge("fechaEntrega", getDateFrom(operacion.getFechaEntregaDesde())));
        		if(operacion != null && operacion.getFechaEntregaHasta() != null && !"".equals(operacion.getFechaEntregaHasta()))
        			crit.add(Restrictions.le("fechaEntrega", getDateTo(operacion.getFechaEntregaHasta())));
        		if(operacion.getEstado()!=null && !operacion.getEstado().equals("Todos"))
        			crit.add(Restrictions.eq("estado", operacion.getEstado())); 		
        		
//        		if(operacion.getCodigoTipoOperacion() !=null &&  !"".equals(operacion.getCodigoTipoOperacion())){
//	        		crit.add(Restrictions.eq("tipo.codigo", operacion.getCodigoTipoOperacion()));
//        		}
        		// 78	30/12/11	
        		// Pendiente	Operaciones	En el listado de operaciones, 
        		// el filtro "Tipo operación" deberá ser de selección múltiple, es decir, permitir seleccionar varios tipos.
        		// IN
        		if(operacion.getCodigoTipoOperacion() !=null &&  !"".equals(operacion.getCodigoTipoOperacion())){
	        		crit.add(Restrictions.in("tipo.codigo", operacion.getCodigoTipoOperacion().split(",")));
        		}
//        		87	30/12/11	
//        		Pendiente	Operaciones	"Agregar un nuevo filtro "Ocultar operaciones de envío"
//        				que esté activado por defecto y que cuando es seleccionado excluya aquellas 
//        				operaciones configuradas como "Envío".
//        		Las operaciones de envío se trabajan desde la funcionalidad "Hoja de Ruta".
        		if(operacion.getOcultarOpEnvio()!=null){
        			if(operacion.getOcultarOpEnvio()==true){
        				crit.add(Restrictions.or(
        						Restrictions.ne("tipo.envio",true),
    							Restrictions.isNull("tipo.envio")
    											)
    							);
        			}
        		}
        		if(operacion.getCodigoDeposito() !=null &&  !"".equals(operacion.getCodigoDeposito())){
	        		crit.add(Restrictions.eq("dep.codigo", operacion.getCodigoDeposito()));
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
	
	
	private List<Long> obtenerIDsOperacionFiltradas(Operacion operacion, ClienteAsp cliente){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
        	
        	crit.createCriteria("tipoOperacion", "tipo");
        	crit.createCriteria("deposito", "dep");
        	crit.createCriteria("requerimiento", "req");
        	
        	if(operacion!=null){
        		if(operacion != null && operacion.getCodigoRequerimiento() != null && !"".equals(operacion.getCodigoRequerimiento()))
        			crit.add(Restrictions.eq("req.numero", BigInteger.valueOf(Long.valueOf(operacion.getCodigoRequerimiento()))));
        		if(operacion != null && operacion.getCodigoEmpresa() != null && !"".equals(operacion.getCodigoEmpresa())){
        			crit.createCriteria("req.clienteEmp", "cli");
        			crit.createCriteria("cli.empresa", "emp").add(Restrictions.eq("emp.codigo", operacion.getCodigoEmpresa()));}
        		if(operacion != null && operacion.getCodigoSucursal() != null && !"".equals(operacion.getCodigoSucursal())){
        			crit.createCriteria("req.sucursal", "suc").add(Restrictions.eq("suc.codigo", operacion.getCodigoSucursal()));}
        		if(operacion != null && operacion.getClienteCodigo() != null && !"".equals(operacion.getClienteCodigo())){
        			crit.createCriteria("clienteEmp", "cliE").add(Restrictions.eq("cliE.codigo", operacion.getClienteCodigo()));}
        		if(operacion != null && operacion.getCodigoPersonal() != null && !"".equals(operacion.getCodigoPersonal())){
        			crit.createCriteria("req.empleadoSolicitante", "empl").add(Restrictions.eq("empl.codigo", operacion.getCodigoPersonal()));}
        		if(operacion != null && operacion.getCodigoDireccion() != null && !"".equals(operacion.getCodigoDireccion())){
        			crit.createCriteria("req.direccionDefecto", "dir").add(Restrictions.eq("dir.codigo", operacion.getCodigoDireccion()));}
        		if(operacion != null && operacion.getIdDesde() != null)
        			crit.add(Restrictions.ge("id", operacion.getIdDesde()));
        		if(operacion != null && operacion.getIdHasta() != null)
        			crit.add(Restrictions.le("id", operacion.getIdHasta()));
        		if(operacion != null && operacion.getFechaDesde() != null && !"".equals(operacion.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaAlta", getDateFrom(operacion.getFechaDesde())));
        		if(operacion != null && operacion.getFechaHasta() != null && !"".equals(operacion.getFechaHasta()))
        			crit.add(Restrictions.le("fechaAlta", getDateTo(operacion.getFechaHasta())));
        		if(operacion != null && operacion.getFechaEntregaDesde() != null && !"".equals(operacion.getFechaEntregaDesde()))
        			crit.add(Restrictions.ge("fechaEntrega", getDateFrom(operacion.getFechaEntregaDesde())));
        		if(operacion != null && operacion.getFechaEntregaHasta() != null && !"".equals(operacion.getFechaEntregaHasta()))
        			crit.add(Restrictions.le("fechaEntrega", getDateTo(operacion.getFechaEntregaHasta())));
        		
//        		87	30/12/11	
//        		Pendiente	Operaciones	"Agregar un nuevo filtro "Ocultar operaciones de envío"
//        				que esté activado por defecto y que cuando es seleccionado excluya aquellas 
//        				operaciones configuradas como "Envío".
//        		Las operaciones de envío se trabajan desde la funcionalidad "Hoja de Ruta".
        		if(operacion.getOcultarOpEnvio()!=null){
        			if(operacion.getOcultarOpEnvio()==true){
        				crit.add(Restrictions.or(
        						Restrictions.ne("tipo.envio",true),
    							Restrictions.isNull("tipo.envio")
    											)
    							);
        			}
        		}
        		
        		if(operacion.getEstado()!=null && !operacion.getEstado().equals("Todos")){
            			crit.add(Restrictions.eq("estado", operacion.getEstado()));
        		}
        		
        			 		
        		// 78	30/12/11	
        		// Pendiente	Operaciones	En el listado de operaciones, 
        		// el filtro "Tipo operación" deberá ser de selección múltiple, es decir, permitir seleccionar varios tipos.
        		// IN
        		if(operacion.getCodigoTipoOperacion() !=null &&  !"".equals(operacion.getCodigoTipoOperacion())){
	        		crit.add(Restrictions.in("tipo.codigo", operacion.getCodigoTipoOperacion().split(",")));
        		}
        		if(operacion.getCodigoDeposito() !=null &&  !"".equals(operacion.getCodigoDeposito())){
	        		crit.add(Restrictions.eq("dep.codigo", operacion.getCodigoDeposito()));
        		}
        		
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	
//        	//Ordenamos
        	if(operacion.getFieldOrder()!=null && operacion.getSortOrder()!=null){
        		String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		if("tipoOperacion.descripcion".equals(operacion.getFieldOrder())){
        			crit.createCriteria("tipoOperacion", "tipo");
        			fieldOrdenar = "tipo.descripcion";
        		}
        		if("deposito.descripcion".equals(operacion.getFieldOrder())){
        			crit.createCriteria("deposito", "dep");
        			fieldOrdenar = "dep.descripcion";
        		}
        		if("fechaHoraAltaStr".equals(operacion.getFieldOrder())){
        			fieldOrdenar = "fechaAlta";
        			fieldOrdenar2 = "horaAlta";
        		}
        		if("fechaHoraEntregaStr".equals(operacion.getFieldOrder())){
        			fieldOrdenar = "fechaEntrega";
        			fieldOrdenar2 = "horaEntrega";
        		}
        		if("cantidadPendientes".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "cantidadPendientes";
        		if("cantidadProcesados".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "cantidadProcesados";
        		if("cantidadOmitidos".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "cantidadOmitidos";
        		if("estado".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "estado";
        		if("id".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "id";
        		
        		if("1".equals(operacion.getSortOrder())){
        			if(!"".equals(fieldOrdenar))
    					crit.addOrder(Order.asc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.asc(fieldOrdenar2));
    			}else if("2".equals(operacion.getSortOrder())){
    				if(!"".equals(fieldOrdenar))
    					crit.addOrder(Order.desc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.desc(fieldOrdenar2));
    			}
        	}
        	
        	//Paginamos
        	if(operacion.getNumeroPagina()!=null && operacion.getNumeroPagina().longValue()>0 
    				&& operacion.getTamañoPagina()!=null && operacion.getTamañoPagina().longValue()>0){
    			Integer paginaInicial = (operacion.getNumeroPagina() - 1);
    			Integer filaDesde = operacion.getTamañoPagina() * paginaInicial;
    			crit.setFirstResult(filaDesde);
    			
    			crit.setMaxResults(operacion.getTamañoPagina());
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
	public List<Operacion> listarOperacionFiltradas(Operacion operacion, ClienteAsp cliente){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsOperacionFiltradas(operacion, cliente);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Operacion>();
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.in("id", ids));
//        	//Ordenamos
        	if(operacion.getFieldOrder()!=null && operacion.getSortOrder()!=null){
        		String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		if("tipoOperacion.descripcion".equals(operacion.getFieldOrder())){
        			crit.createCriteria("tipoOperacion", "tipo");
        			fieldOrdenar = "tipo.descripcion";
        		}
        		if("deposito.descripcion".equals(operacion.getFieldOrder())){
        			crit.createCriteria("deposito", "dep");
        			fieldOrdenar = "dep.descripcion";
        		}
        		if("fechaHoraAltaStr".equals(operacion.getFieldOrder())){
        			fieldOrdenar = "fechaAlta";
        			fieldOrdenar2 = "horaAlta";
        		}
        		if("fechaHoraEntregaStr".equals(operacion.getFieldOrder())){
        			fieldOrdenar = "fechaEntrega";
        			fieldOrdenar2 = "horaEntrega";
        		}
        		if("cantidadPendientes".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "cantidadPendientes";
        		if("cantidadProcesados".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "cantidadProcesados";
        		if("cantidadOmitidos".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "cantidadOmitidos";
        		if("estado".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "estado";
        		if("id".equals(operacion.getFieldOrder()))
        			fieldOrdenar = "id";
        		
        		if("1".equals(operacion.getSortOrder())){
        			if(!"".equals(fieldOrdenar))
    					crit.addOrder(Order.asc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.asc(fieldOrdenar2));
    			}else if("2".equals(operacion.getSortOrder())){
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
	public List<Operacion> listarOperacionPorRequerimiento(Requerimiento requerimiento, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	
        	if(requerimiento != null){
        		crit.add(Restrictions.eq("requerimiento", requerimiento));
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
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
	public synchronized boolean update(Operacion objeto, List<Operacion> operaciones) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(objeto);
			Hashtable<Operacion, Set<OperacionElemento>> hashOperacion = new Hashtable<Operacion, Set<OperacionElemento>>();
			
			//Recorro las operaciones, las registro y armo una hash de elementos para guardarlos en el segundo commit
			if(operaciones!=null){
				for(Operacion operacion:operaciones){
					if(operacion.getListaElementos()!=null)
						hashOperacion.put(operacion, operacion.getListaElementos());
					operacion.setListaElementos(null);
					session.save(operacion);
				}
			}
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			
			tx.begin();
			if(operaciones!=null){
				for(Operacion operacion:operaciones){
					operacion.setListaElementos(hashOperacion.get(operacion));
					session.update(operacion);
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
	public synchronized boolean update(Operacion objeto, List<Operacion> operaciones, ConceptoOperacionCliente conceptoOperacionCliente, List<ConceptoOperacionCliente> conceptosVentas
			, List<Stock> stocks, List<Rearchivo> listaRearchivosActualizar) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(objeto);
			
			//recorro los elementos de la operacion y los actualizo
			if(objeto.getListaElementos()!=null){
				for(OperacionElemento opE: objeto.getListaElementos()){
					if(opE.getElemento()!=null)
						session.update(opE.getElemento());
					//Lineas agregadas para el historico
					registrarHistoricoElementos("MS015ELE", opE.getElemento(),session);
					////////////////////////////////////
				}
			}
			Hashtable<Operacion, Set<OperacionElemento>> hashOperacion = new Hashtable<Operacion, Set<OperacionElemento>>();
			
			//Recorro las operaciones, las registro y armo una hash de elementos para guardarlos en el segundo commit
			if(operaciones!=null){
				for(Operacion operacion:operaciones){
					if(operacion.getListaElementos()!=null)
						hashOperacion.put(operacion, operacion.getListaElementos());
					operacion.setListaElementos(null);
					session.save(operacion);
					//recorro los elementos de la operacion y los actualizo
					if(operacion.getListaElementos()!=null){
						for(OperacionElemento opE: operacion.getListaElementos()){
							if(opE.getElemento()!=null)
								session.update(opE.getElemento());
							//Lineas agregadas para el historico
							registrarHistoricoElementos("MS015ELE", opE.getElemento(),session);
							////////////////////////////////////
						}
					}
				}
			}
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			
			tx.begin();
			if(operaciones!=null){
				for(Operacion operacion:operaciones){
					operacion.setListaElementos(hashOperacion.get(operacion));
					session.update(operacion);
				}
			}
			if(conceptoOperacionCliente!=null)
				session.save(conceptoOperacionCliente);
			if(conceptosVentas!=null){
				for(ConceptoOperacionCliente con:conceptosVentas){
					session.save(con);
				}
			}
			if(stocks!=null){
				for(Stock stock:stocks){
					session.save(stock);
				}
			}
			if(listaRearchivosActualizar!=null){
				for(Rearchivo rearchivo:listaRearchivosActualizar){
					session.update(rearchivo);
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
	public Integer contarOperacionesPorRequerimientoYEstado(Requerimiento requerimiento, String estado, ClienteAsp cliente){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
        	if(requerimiento!=null)
    			crit.add(Restrictions.eq("requerimiento", requerimiento)); 
    		if(estado!=null)
    			crit.add(Restrictions.eq("estado", estado)); 		
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	
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



	@Override
	public List<Operacion> listarOperacionEstado(String estado, ClienteAsp cliente) {
		Session session = null;
		try {
        	session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(estado != null)
        		crit.add(Restrictions.eq("estado", estado)); 		
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	// Operacion de tipo envio
        	crit.createCriteria("tipoOperacion", "tipoOP");
    		crit.add(Restrictions.eq("tipoOP.envio", true));
    		
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return  crit.list();
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
	public Operacion obtenerPorId(long id){
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
			Operacion operacion = (Operacion)session.get(getClaseModelo(),id);
		    //cargar referencias...
	        for(OperacionElemento ele : operacion.getListaElementos()){
	        	ele.getElemento();
	        }
		    return operacion;
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
	
	////////////////////////////////METODOS AUXILIARES////////////////////////////////////////////

	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void registrarHistoricoElementos(String mensaje, Elemento elemento,Session session){
		ElementoHistorico elementoHis = new ElementoHistorico();
		elementoHis.setCodigoElemento(elemento.getCodigo());
		elementoHis.setAccion(mensaje);
		elementoHis.setFechaHora(new Date());
		elementoHis.setUsuario(obtenerUser());
		elementoHis.setClienteAsp(obtenerClienteAspUser());
		if(elemento.getClienteEmp()!=null){
			elementoHis.setCodigoCliente(elemento.getClienteEmp().getCodigo());
			elementoHis.setNombreCliente(elemento.getClienteEmp().getRazonSocialONombreYApellido());
		}
		elementoHis.setCodigoTipoElemento(elemento.getTipoElemento().getCodigo());
		elementoHis.setNombreTipoElemento(elemento.getTipoElemento().getDescripcion());
		session.save(elementoHis);
	}
	
	@Override
	public Boolean actualizarOperacionList(List<Operacion> listOperaciones)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Operacion actualizar : listOperaciones){
    			session.update(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo actualizar la coleccion de operaciones ", hibernateException);
	        return false;
        }
   }
	
}
