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

import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Transporte;

@Component
public class TransporteServiceImp extends GestorHibernate<Transporte> implements TransporteService {
	private static Logger logger=Logger.getLogger(TransporteServiceImp.class);

	@Autowired
	public TransporteServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Transporte> getClaseModelo() {
		return Transporte.class;
	}
	
	
	@Override
	public List<Transporte> listarTransporteFiltradas(Transporte transporte, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.createCriteria("empresa", "emp");
//        	if(transporte!=null){
//	        	if(transporte.getEmpresa() != null && transporte.getEmpresa().getId() !=null )
//	        		crit.add(Restrictions.eq("empresa", transporte.getEmpresa()));
//	        	if(transporte.getCodigoEmpresa() != null && !"".equals(transporte.getCodigoEmpresa()))
//	        		crit.add(Restrictions.eq("emp.codigo", transporte.getCodigoEmpresa()));
//	        	if(transporte.getDescripcion() !=null && !"".equals(transporte.getDescripcion()))
//	        		crit.add(Restrictions.ilike("descripcion", transporte.getDescripcion() + "%"));
//	        	if(transporte.getCodigo() !=null && transporte.getCodigo()!=null)
//	        		crit.add(Restrictions.eq("codigo", transporte.getCodigo()));
//	        	if(transporte!=null && transporte.getCodigoMax()!=null && !transporte.getCodigoMax().equals(0))
//	        		crit.add(Restrictions.le("codigo", transporte.getCodigoMax()));
//	        	if(transporte!=null && transporte.getCodigoMin()!=null && !transporte.getCodigoMin().equals(0))
//	        		crit.add(Restrictions.ge("codigo", transporte.getCodigoMin()));
//        	}
//        	if(cliente != null)
//        		crit.add(Restrictions.eq("clienteAsp", cliente));
//        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			Query query=session.getNamedQuery("Transporte.listarTransporteFiltradasNamed");
			
			if(transporte != null && transporte.getEmpresa()!=null)
				query.setParameter("empresaId", transporte.getEmpresa().getId());
			else
				query.setParameter("empresaId",null);
			
			if(transporte != null)
				query.setParameter("codigoEmpresa", transporte.getCodigoEmpresa());
			else
				query.setParameter("codigoEmpresa",null);
			
            return query.list();
        } catch (HibernateException hibernateException) {
        	//logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		//logger.error("No se pudo cerrar la sesión", e);
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
	public Boolean eliminarTransporte(Transporte transporte) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(transporte);
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
	
	@Override
	public Boolean guardarTransporte(Transporte transporte) {
		Boolean result = true;
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();			
			//guardamos el objeto
			session.save(transporte);
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
	public Boolean actualizarTransporte(Transporte transporte) {
		Boolean result = true;
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(transporte);					
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
	public Transporte getByCodigo(Integer codigo, Empresa empresa) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
        	
        	if(empresa != null){
        		crit.add(Restrictions.eq("empresa", empresa));
        		crit.add(Restrictions.eq("clienteAsp", empresa.getCliente()));
        	}
        	
            return (Transporte) crit.uniqueResult();
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
	public Transporte verificarTransporte(Transporte transporte){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(transporte!=null){
	        	if(transporte.getEmpresa() !=null){
	        		crit.add(Restrictions.eq("empresa", transporte.getEmpresa()));}
        		if(transporte.getClienteAsp() !=null)
	        		crit.createCriteria("clienteAsp").add(Restrictions.eq("id", transporte.getClienteAsp().getId()));
        		if(transporte.getCodigo() !=null && !"".equals(transporte.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", transporte.getCodigo()));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Transporte> result = crit.list();
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
	
	@Override
	public List<Transporte> listarTransportePopup(String val, String codigoEmpresa, ClienteAsp cliente)
	{ return listarTransportePopup(val, codigoEmpresa, cliente, null);}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Transporte> listarTransportePopup(String val, String codigoEmpresa, ClienteAsp cliente, Boolean habilitado) {
		Session session = null;
        try {
        	String valores[] = null;
        	if(val!=null)
        		valores = val.split(" ");
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("empresa", "emp");
        	//filtro value
        	if(valores!=null){
        		for(String filtro : valores){
		        	c.add(Restrictions.or(
		        			Restrictions.ilike("codigo", filtro+"%"), 
		        			Restrictions.ilike("decripcion", filtro+"%")));
        		}
        	}
        	//filtro empresa
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa)){
        		c.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	}
        	//filtro por habilitados
        	if(habilitado != null)
        		c.add(Restrictions.eq("habilitado", habilitado));
        	//filtro cliente
        	c.add(Restrictions.eq("clienteAsp", cliente));
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
	public Transporte obtenerPorCodigo(Integer codigo, ClienteAsp clienteAsp) {
		return obtenerPorCodigo(codigo, null, clienteAsp);
	}
	
	@Override
	public Transporte obtenerPorCodigo(Integer codigo, String codigoEmpresa, ClienteAsp clienteAsp) {
		return obtenerPorCodigo(codigo, codigoEmpresa, null,clienteAsp);
	}
	
	@Override
	public Transporte obtenerPorCodigo(Integer codigo, String codigoEmpresa, Boolean habilitado,ClienteAsp clienteAsp) {
		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());			
			//filtro por codigo
			if(codigo != null && codigo != 0)
				c.add(Restrictions.eq("codigo", codigo));
			//filtro por empresa
			if(codigoEmpresa != null && codigoEmpresa.length()>0)
				c.createCriteria("empresa", "emp").add(Restrictions.eq("emp.codigo", codigoEmpresa));
			//filtro por cliente
			c.add(Restrictions.eq("clienteAsp", clienteAsp));
			//filtro por habilitados
        	if(habilitado != null)
        		c.add(Restrictions.eq("habilitado", habilitado));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (Transporte) c.uniqueResult();		
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
	
}
