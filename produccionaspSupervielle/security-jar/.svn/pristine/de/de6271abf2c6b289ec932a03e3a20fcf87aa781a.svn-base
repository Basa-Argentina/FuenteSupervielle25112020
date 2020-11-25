/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.exceptions.BasaException;
import com.security.exceptions.ClienteAspNullException;
import com.security.exceptions.ClienteEmpNullException;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.utils.Constantes;

/**
 * @author Luis Manzanelli
 *
 */
@Component
public class ClasificacionDocumentalServiceImp extends GestorHibernate<ClasificacionDocumental> implements ClasificacionDocumentalService {
	private static Logger logger=Logger.getLogger(ClasificacionDocumentalServiceImp.class);
	
	@Resource(mappedName = "java:/comp/env/jdbc/basa")
    protected DataSource dataSource;
	
	@Autowired
	public ClasificacionDocumentalServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<ClasificacionDocumental> getClaseModelo() {
		return ClasificacionDocumental.class;
	}

	@Override
	public List<ClasificacionDocumental> getNodosRaizPorCliente(ClienteEmp clienteEmp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("clienteEmp", clienteEmp));
        	crit.setFetchMode("nodosHijos", FetchMode.JOIN);
        	crit.addOrder(Order.asc("codigo"));
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	List<ClasificacionDocumental> todos = crit.list();
        	List<ClasificacionDocumental> nodosRaizPorCliente = new ArrayList<ClasificacionDocumental>();
        	for(ClasificacionDocumental candidato : todos){
        		if(candidato.getPadre()==null)
        			nodosRaizPorCliente.add(candidato);
        		candidato.getNodosHijos().size();
        	}
        	return nodosRaizPorCliente;
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
	public List<ClasificacionDocumental> getByClienteCodigo(ClienteEmp clienteEmp, Integer codigo) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("clienteEmp", clienteEmp));
        	crit.add(Restrictions.eq("codigo", codigo));
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
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
	public ClasificacionDocumental getClasificacionByCodigo(Integer codigo, String codigoClienteEmp, ClienteAsp clienteAsp, String nodo) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.createCriteria("clienteEmp", "cli");
//        	crit.add(Restrictions.eq("cli.codigo", codigoClienteEmp));
//        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
//        	//es conveniente hacer los filtros en memoria y no dejar a hibernate que filtre 
//        	//porque cuando intenta llenar los hijos y padre se muere (tarda mucho).
//        	//crit.add(Restrictions.eq("codigo", codigo)); 
//        	//crit.add(Restrictions.eq("nodo", "I"));
//        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        	
        	String consulta = "SELECT DISTINCT cd FROM ClasificacionDocumental cd WHERE cd.clienteEmp.codigo = '" + codigoClienteEmp + "' " +
        						"AND cd.clienteAsp.id = " + clienteAsp.getId().longValue()+ " ";
        	
        	Query query = session.createQuery(consulta);
        	
        	List<ClasificacionDocumental> candidatos = query.list();
        	for(ClasificacionDocumental candidato:candidatos){
        		//ahora el código es único por cliente emp
        		if( (nodo==null || nodo.equalsIgnoreCase(candidato.getNodo())) && candidato.getCodigo().equals(codigo)){
        			return candidato;
        		}
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

	public ClasificacionDocumental getClasificacionByCodigoCargarHijos(Integer codigo, String codigoClienteEmp, ClienteAsp clienteAsp, String nodo) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("clienteEmp", "cli");
        	crit.add(Restrictions.eq("cli.codigo", codigoClienteEmp));
        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	
        	crit.setFetchMode("nodosHijos", FetchMode.EAGER);
        	
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	
        	List<ClasificacionDocumental> candidatos = crit.list();
        	
        	for(ClasificacionDocumental candidato:candidatos){
        		//ahora el código es único por cliente emp
        		if( (nodo==null || nodo.equalsIgnoreCase(candidato.getNodo())) && candidato.getCodigo().equals(codigo)){
        			return candidato;
        		}
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
	public ClasificacionDocumental getByCodigo(Integer codigo, String codigoClienteEmp, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("clienteEmp", "cli");
        	crit.add(Restrictions.eq("cli.codigo", codigoClienteEmp));
        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	crit.add(Restrictions.eq("codigo", codigo)); //ahora el código es único por cliente emp
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	List<ClasificacionDocumental> candidatos = crit.list();
        	if(candidatos.size()>0)return candidatos.get(0);
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
	public List<ClasificacionDocumental> listarClasificacionPopup(String val,String clienteCodigo, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("clienteEmp", "cli");
        	crit.add(Restrictions.eq("cli.codigo", clienteCodigo));
        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	crit.setFetchMode("nodosHijos", FetchMode.JOIN);
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	List<ClasificacionDocumental> todos = crit.list();
        	List<ClasificacionDocumental> nodosRaizPorCliente = new ArrayList<ClasificacionDocumental>();
        	for(ClasificacionDocumental candidato : todos){
        		if(candidato.getPadre()==null)
        			nodosRaizPorCliente.add(candidato);
        		candidato.getNodosHijos().size();
        	}
        	return nodosRaizPorCliente;
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
	public List<ClasificacionDocumental> listarClasificacionPopupEntera(String val,String clienteCodigo, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("clienteEmp", "cli");
        	crit.add(Restrictions.eq("cli.codigo", clienteCodigo));
        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	if(val!=null && !val.equals(""))
        		crit.add(Restrictions.ilike("nombre", val+"%"));
        	crit.addOrder(Order.asc("codigo"));
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
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
	public Integer getProximoCodigoByClienteEmp(ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		Integer proximoCodigo = null;
		Session session = null;
		Integer result = null;
        try {
			//obtenemos una sesión
			session = getSession();
	    	Criteria crit = session.createCriteria(getClaseModelo());
	    	//crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
	    	crit.add(Restrictions.eq("clienteEmp", clienteEmp));
	    	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
	    	crit.setProjection(Projections.max("codigo"));	    	
	    	Integer ultimoCodigo = (Integer)crit.uniqueResult();
	    	if(ultimoCodigo != null && ultimoCodigo.intValue()>0){
	    		proximoCodigo = ultimoCodigo + 1;
	    	}else{
	    		proximoCodigo = 1;
	    	}
			result = proximoCodigo;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo obtener el ultimo codigo ", hibernateException);
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
	@Override
	public Set<Empleado> getPersonalAsignadoPorNodos(Set<ClasificacionDocumental> carpetas, ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		Set<Empleado> empleadosAsignados = new HashSet<Empleado>();
		Session session = null;
        try {
        	HashSet<Long> ids = new HashSet<Long>(); 
        	for(ClasificacionDocumental carpeta : carpetas){
        		if(Constantes.CLASIFICACION_DOCUMENTAL_NODO_CARPETA.equals(carpeta.getNodo())){
        			ids.add(carpeta.getId());
        		}
        	}
        	
        	List<ClasificacionDocumental> carpetasResult = null;
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	crit.add(Restrictions.eq("clienteEmp", clienteEmp));
	    	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
	    	//la consulta trae todos los empleados asignados a la carpeta 
	    	crit.setFetchMode("empleadosClienteEmp", FetchMode.JOIN);
        	crit.add(Restrictions.in("id", ids));
        	carpetasResult = (List<ClasificacionDocumental>)crit.list();
        	
        	for(ClasificacionDocumental carpetaResult:carpetasResult){
        		empleadosAsignados.addAll(carpetaResult.getEmpleadosClienteEmp());
        	}
        	
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo obtener el ultimo codigo ", hibernateException);        	
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		return	empleadosAsignados; 
	}
	
	@Override
	public ClasificacionDocumental traerCarpetaConEmpleadosAsignados(ClasificacionDocumental carpeta, ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		Session session = null;
		ClasificacionDocumental result = null;
		try{
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			crit.add(Restrictions.eq("clienteEmp", clienteEmp));
			crit.add(Restrictions.eq("clienteAsp", clienteAsp));
			// la consulta trae todos los empleados asignados a la carpeta
			crit.setFetchMode("empleadosClienteEmp", FetchMode.JOIN);
			crit.setFetchMode("nodosHijos", FetchMode.SELECT);
			crit.add(Restrictions.eq("id", carpeta.getId()));
			result = (ClasificacionDocumental) crit.uniqueResult();
			
			if(result!=null && result instanceof ClasificacionDocumental)
			{
				buscarYAsignarNodosHijos(result);
			}
			
			
		} catch (HibernateException hibernateException) {
			logger.error("No se pudieron obtener los empleados asignados a la carpeta ",
					hibernateException);
			result = null;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("No se pudo cerrar la sesión", e);
			}
		}
		return result;
	}
	
	@Override
	public boolean guardarPersonarClasificacionDocumental(List<Empleado> empleadosAsignados, ClasificacionDocumental carpetaSeleccionada, 
			ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		boolean result = false;
		
		ClasificacionDocumental	carpetaSeleccionadaConEmpleadosAsignados = traerCarpetaConEmpleadosAsignados(carpetaSeleccionada, clienteEmp, clienteAsp);
		List<ClasificacionDocumental> carpetasHijasModificadas = verificarEmpleadosAsignadosNoPertenezcanACarpetasHijas(carpetaSeleccionadaConEmpleadosAsignados, empleadosAsignados, clienteAsp, clienteEmp);
		List<Empleado> empleadosAsignadosNoPertenecenACarpetasPadre = verificarEmpleadosAsignadosNoPertezcanACarpetasPadre(carpetaSeleccionadaConEmpleadosAsignados, empleadosAsignados, clienteEmp, clienteAsp);
		
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.getTransaction();
			tr.begin();		
			//verifica que carpetaSeleccionada y empleados asignados pertenezcan a clienteAsp
			if(clienteAsp == null){
				throw new ClienteAspNullException();
			}
			if(clienteEmp == null){
				throw new ClienteEmpNullException();
			}			
			

			StringBuilder sql = null;
			StringBuilder ids = null;
			String sqlquery = null;
			SQLQuery q = null;
			//borramos la relacion de los empleados asignados con las carpetas hijas
			for(ClasificacionDocumental carpetaHija: carpetasHijasModificadas){
				sql = new StringBuilder("DELETE FROM x_clasificacionDocumental_clienteEmpleados \n");
				sql.append("WHERE clasificacionDocumental_id=");			
				sql.append(carpetaHija.getId()).append(" ");
				sql.append("AND clienteEmpleados_id IN ");
				ids = new StringBuilder("(");
				for (Empleado emp : carpetaHija.getEmpleadosParaEliminar()){
					ids.append(emp.getId()).append(",");
				}
				ids.setCharAt(ids.length()-1,')');
				sql.append(ids);
				sqlquery = sql.toString();
				q =session.createSQLQuery(sqlquery);
				q.executeUpdate();
			}
			
			//borramos la relacion de la carpeta seleccionada con sus viejos empleados asignados
			if(carpetaSeleccionadaConEmpleadosAsignados.getEmpleadosClienteEmp()!=null 
					&& carpetaSeleccionadaConEmpleadosAsignados.getEmpleadosClienteEmp().size()>0){
				sql = new StringBuilder("DELETE FROM x_clasificacionDocumental_clienteEmpleados \n");
				sql.append("WHERE clasificacionDocumental_id=");			
				sql.append(carpetaSeleccionadaConEmpleadosAsignados.getId()).append(" ");
				sql.append("AND clienteEmpleados_id IN ");
				ids = new StringBuilder("(");
				for (Empleado emp : carpetaSeleccionadaConEmpleadosAsignados.getEmpleadosClienteEmp()){
					ids.append(emp.getId()).append(",");
				}
				ids.setCharAt(ids.length()-1,')');
				sql.append(ids);
				sqlquery = sql.toString();
				q = session.createSQLQuery(sqlquery);
				q.executeUpdate();
			}
			//actualizamos los empleados de la carpeta seleccionada			
			for(Empleado empl:empleadosAsignadosNoPertenecenACarpetasPadre){
				sql = new StringBuilder("INSERT INTO x_clasificacionDocumental_clienteEmpleados \n");
				sql.append("(clasificacionDocumental_id, clienteEmpleados_id)");
				sql.append("VALUES (").append(carpetaSeleccionadaConEmpleadosAsignados.getId()).append(",");
				sql.append(empl.getId()).append(")");
				sqlquery = sql.toString();
				q = session.createSQLQuery(sqlquery);
				q.executeUpdate();
			}
			
			session.flush();
			tr.commit();
			result = true;
		} catch (HibernateException hibernateException) {
			logger.error("No se pudieron obtener los empleados asignados a la carpeta ", hibernateException);
			rollback(tr, hibernateException, "No fue posible actualizar Empleados de carpeta");
			result = false;
		}catch (RuntimeException e) {
			logger.error("No se pudieron obtener los empleados asignados a la carpeta ", e);
			rollback(tr, e, "No fue posible actualizar Empleados de carpeta");
			result = false;
		}catch (BasaException e){
			logger.error("No se pudieron obtener los empleados asignados a la carpeta ", e);
			rollback(tr, e, "No fue posible actualizar Empleados de carpeta");
			result = false;
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
	public Boolean eliminarNodoYNodosHijos(ClasificacionDocumental nodo,
			ClienteAsp clienteAsp) {
		Boolean result = false;
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.getTransaction();
			tr.begin();	
			//verifica que carpetaSeleccionada y empleados asignados pertenezcan a clienteAsp
			if(clienteAsp == null){
				throw new ClienteAspNullException();
			}
			nodo.setPadre(null);
			session.delete(nodo);
			//eliminarNodosHijos(nodo, session);
			tr.commit();
			result = true;
		} catch (HibernateException hibernateException) {
			logger.error("No se pudieron obtener los empleados asignados a la carpeta ", hibernateException);
			rollback(tr, hibernateException, "No fue posible actualizar Empleados de carpeta");
			result = false;
		}catch (RuntimeException e) {
			logger.error("No se pudieron obtener los empleados asignados a la carpeta ", e);
			rollback(tr, e, "No fue posible actualizar Empleados de carpeta");
			result = false;
		}catch (BasaException e){
			logger.error("No se pudieron obtener los empleados asignados a la carpeta ", e);
			rollback(tr, e, "No fue posible actualizar Empleados de carpeta");
			result = false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		return result;
	}
	
	/////////////////////////////////// METODOS AUXILIARES////////////////////////////////////////////////////////////////////
	
	/**
	 * Busca las carpetas hijas que tengan asignados empleados que van a ser asignados a la carpeta seleccionada y devuelve una lista de carpetas que contiene los objetos de las carpetas hijas 
	 * que fueron modificadas para eliminar las relaciones correspondientes 
	 * @param carpetaSeleccionada
	 * @param empleadosAsignados
	 * @param clienteAsp
	 * @param clienteEmp
	 * @return la lista de carpetas hijas modificadas
	 */
	private List<ClasificacionDocumental> verificarEmpleadosAsignadosNoPertenezcanACarpetasHijas(ClasificacionDocumental carpetaSeleccionada, 
			List<Empleado> empleadosAsignados, ClienteAsp clienteAsp, ClienteEmp clienteEmp){
		LinkedList<ClasificacionDocumental> carpetasHijasActualizadas = new LinkedList<ClasificacionDocumental>();
		Set<ClasificacionDocumental> nodosHijos = carpetaSeleccionada.getListaCompletaHijos();
		ClasificacionDocumental carpetaHija = null;
		
		for(ClasificacionDocumental nodo:nodosHijos){
			if(Constantes.CLASIFICACION_DOCUMENTAL_NODO_CARPETA.equals(nodo.getNodo())){
				carpetaHija = traerCarpetaConEmpleadosAsignados(nodo, clienteEmp, clienteAsp);
				if(this.removeEmpleados(carpetaHija,empleadosAsignados)){
					carpetasHijasActualizadas.add(carpetaHija);
				}		
			}
		}
				
		return carpetasHijasActualizadas;
	}
	
	
	/**
	 * Devuelve una lista con los empleados asignados que no pertenezcan a las carpetas padre de la carpeta seleccionada
	 * @param carpetaSeleccionada
	 * @param empleadosAsignados
	 * @param clienteEmp
	 * @param clienteAsp
	 * @return
	 */
	private List<Empleado> verificarEmpleadosAsignadosNoPertezcanACarpetasPadre(ClasificacionDocumental carpetaSeleccionada, List<Empleado> empleadosAsignados, ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		List<Empleado> empleadosAsignadosNoPertenecenCarpetasPadre = new LinkedList<Empleado>(empleadosAsignados);
		ClasificacionDocumental carpetaPadreConEmpleadosAsignados = null;
		
		for(ClasificacionDocumental carpetaPadre: carpetaSeleccionada.getListaCarpetasPadre()){
			carpetaPadreConEmpleadosAsignados = traerCarpetaConEmpleadosAsignados(carpetaPadre, clienteEmp, clienteAsp);
			this.removeEmpleados(empleadosAsignadosNoPertenecenCarpetasPadre,carpetaPadreConEmpleadosAsignados.getEmpleadosClienteEmp());
		}
		
		return empleadosAsignadosNoPertenecenCarpetasPadre;
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
	
	private boolean removeEmpleados(ClasificacionDocumental carpeta, Collection<Empleado> empleadosARemover){
		boolean result = false;
		Collection<Empleado> empleados = carpeta.getEmpleadosClienteEmp();
		if(carpeta.getEmpleadosParaEliminar()==null){
			carpeta.setEmpleadosParaEliminar(new HashSet<Empleado>());
		}
		Iterator<Empleado> it = empleados.iterator();
		Empleado empleado = null;
		while(it.hasNext()){
			empleado = it.next();
			for(Empleado emp:empleadosARemover){
				if(empleado.equals(emp)){
					carpeta.getEmpleadosParaEliminar().add(empleado);
					it.remove();
					result = true;
				}
			}
		}
		return result;
	}
	
	private boolean removeEmpleados(Collection<Empleado> empleados, Collection<Empleado> empleadosARemover){
		boolean result = false;
		Iterator<Empleado> it = empleados.iterator();
		Empleado empleado = null;
		while(it.hasNext()){
			empleado = it.next();
			for(Empleado emp:empleadosARemover){
				if(empleado.equals(emp)){
					it.remove();
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	public Collection<ClasificacionDocumental> getByPersonalAsignado(Empleado empleado) {
		Session session = null;
		try{
			session = getSession();
			empleado = (Empleado) session.get(Empleado.class, empleado.getId());
			Collection<ClasificacionDocumental> clasificaciones = empleado.getClasificacionesDocumentales();
			clasificaciones.iterator();//forzar inicialización
			if(clasificaciones.iterator().hasNext())
				buscarYAsignarNodosHijos(clasificaciones.iterator().next());
			return clasificaciones;
		} catch (HibernateException hibernateException) {
			logger.error("No se pudieron obtener las clasificaciones asignadas al empleado ",hibernateException);
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("No se pudo cerrar la sesión", e);
			}
		}
		return null;
	}
	
	public void buscarYAsignarNodosHijos(ClasificacionDocumental doc)
	{
		try{
			if(doc!=null){
				if(doc.getNodosHijos()!=null && doc.getNodosHijos().size()>0)
				{
					for(ClasificacionDocumental docHija:doc.getNodosHijos()){
						buscarYAsignarNodosHijos(docHija);
					}
				}
			}
		}catch(LazyInitializationException ex){
			ClasificacionDocumental hijo = this.getClasificacionByCodigoCargarHijos(doc.getCodigo(),doc.getClienteEmp().getCodigo(), doc.getClienteAsp(), doc.getNodo());
			buscarYAsignarNodosHijos(hijo);
		}
	}
}
