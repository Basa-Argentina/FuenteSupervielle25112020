/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.EstanteService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Estante;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class EstanteServiceImp extends GestorHibernate<Estante> implements EstanteService {
	private static Logger logger=Logger.getLogger(EstanteServiceImp.class);
	
	@Resource(mappedName = "java:/comp/env/jdbc/basa")
    protected DataSource dataSource;
	
	@Autowired
	public EstanteServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}


	@Override
	public Class<Estante> getClaseModelo() {
		return Estante.class;
	}

	@Override
	public Boolean guardarEstante(Estante estante) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(estante);
			//Creamos las posiciones, solo se crea el estante si los modulos y posiciones
			//fueron creados correctamente.
			crearPosicionesYModulos(estante, session);
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
	public Boolean actualizarEstante(Estante estante) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(estante);
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
	public Boolean eliminarEstante(Estante estante) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(estante);
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
	public Boolean eliminarEstanteModulosPosiciones(Estante estante){
		Session session = null;
		Transaction tx = null;
		int result = 0;
		try {			
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			//tx = session.getTransaction();
			//tx.begin();
			//borramos la estanteria
			//session.delete(estante);
			
			String consulta = "delete from posiciones where estante_id =" + estante.getId() +" ; " + 
				" delete from modulos where estante_id =" + estante.getId() +" ; " + 
				" delete from estanterias where id =" + estante.getId() +" ; ";
			
			SQLQuery q = session.createSQLQuery(consulta);
			
			result = q.executeUpdate();
			
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			//tx.commit();
			
			if(result > 0)
				return true;
			else
				return false;
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
	public Estante getByCodigo(String codigo) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
            return (Estante) crit.uniqueResult();
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
	public Estante getById(Long id) {
		Session session = null;
		Estante estante = null;
        if(id!=null && id>0){
			try {
	        	//obtenemos una sesión
				session = getSession();
	        	Criteria crit = session.createCriteria(getClaseModelo());
	        	crit.add(Restrictions.eq("id", id));
	            estante= (Estante) crit.uniqueResult();
	        } catch (HibernateException hibernateException) {
	        	logger.error("No se pudo listar ", hibernateException);
		        estante = null;
	        }finally{
	        	try{
	        		session.close();
	        	}catch(Exception e){
	        		logger.error("No se pudo cerrar la sesión", e);
	        	}
	        }
        }
        return estante;
	}
	
	@Override
	public List<Estante> listarEstanteFiltradas(Estante estante, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("grupo", "g");
        	if(estante!=null){
        		if(estante.getIdGrupo() !=null)
	        		crit.add(Restrictions.eq("g.id", estante.getIdGrupo()));	        	
	        	if(estante.getCodigo() !=null && !"".equals(estante.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", estante.getCodigo() + "%"));	      
        	}
        	if(cliente != null){
        		crit.createCriteria("g.seccion").createCriteria("deposito").createCriteria("sucursal").
    			createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
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
	public Boolean estaVacio(Estante estante){
		Boolean vacio = false;
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(Posicion.class);
			crit.setProjection(Projections.rowCount());
			
			if(estante != null && estante.getId() != null && estante.getId().longValue()!=0){
				crit.add(Restrictions.eq("estante", estante));
				crit.add(Restrictions.eq("estado", "OCUPADA"));
			}
			
			Long cant = Long.valueOf(String.valueOf(crit.uniqueResult()));
			
			if(cant.longValue()==0){
				vacio = true;
			}
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        vacio = false;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        		vacio = false;
        	}
        }	
			
		return vacio;
	}
	
	@Override
	public Boolean NoHayElementos(Long estanteId){
		Boolean vacio = false;
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
//			Criteria crit = session.createCriteria(Elemento.class);
//			crit.setProjection(Projections.rowCount());
//			
//			if(posiciones != null && posiciones.size()>0){
//				crit.add(Restrictions.in("posicion", posiciones));
//			}
//			
			Object elemento = new Object();
			
			String consulta = "SELECT TOP 1 * " + 
					"  FROM elementos e " + 
					"  inner join posiciones p on p.id = e.posicion_id " + 
					"  where p.estante_id = "+ estanteId;
			
			SQLQuery q = session.createSQLQuery(consulta);
			elemento = (Object) q.uniqueResult();
			
			if(elemento==null){
				vacio = true;
			}
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        vacio = false;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        		vacio = false;
        	}
        }	
			
		return vacio;
	}
	
	@Override
	public Estante verificarEstante(Estante estante, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("grupo").createCriteria("seccion").createCriteria("deposito", "dep");
        	if(estante!=null){
        		//Se comenta el filtro por deposito ya que en la documentacion se pide distinto 
        		//codigo de estante por clienteASP
//	        	if(estante.getGrupo() !=null)
//	        		crit.add(Restrictions.eq("dep.id", estante.getGrupo().getSeccion().getDeposito().getId()));
        		
	        	if(estante.getCodigo() !=null && !"".equals(estante.getCodigo()))
	        		crit.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) = "+ Integer.parseInt(estante.getCodigo())+""));
	        		//crit.add(Restrictions.eq("codigo", estante.getCodigo()));	      
	        		
        	}
        	if(cliente != null){
        		crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Estante> result = crit.list();
        	if(result.size() >= 1){
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
	public String traerUltCodigoPorDeposito(Long idDeposito){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(idDeposito!=null){
	        	crit.createCriteria("grupo").createCriteria("seccion").createCriteria("deposito").add(Restrictions.eq("id", idDeposito));	        	
	        	crit.setProjection(Projections.max("codigo"));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<String> result = crit.list();
        	if(result.size() == 1){
        		String rta = result.get(0);
        		if(rta == null)
        			return "0";
        		else
        			return rta; 
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
	
	/*
	 * No se realiza en un procedimiento, debido a la portabilidad de la base de datos.
	 * El cliente puede cambiar la base utilizada y se deberia reescribir el codigo
	 * del procedimiento.
	 */
	public void crearPosicionesYModulos(Estante estante, Session session)throws RuntimeException{
//		Session session = null;
		
     	int verticales = 0;
     	int horizontales = 0;
     	int modulosVert = 0;
     	int modulosHor = 0;
     	int iterarCountVert = 0;
     	int iterarCountHor = 0;
     	int modCountVert = 0;
     	int modCountHor = 0;
     	int modCountHorAnt = 0;
     	int codigoModulo = 1;
        
     	//CARGAMOS EN LAS VARIABLES LOS VALORES ENCONTRADOS EN EL REGISTRO
     	verticales = estante.getGrupo().getVerticales();
     	horizontales = estante.getGrupo().getHorizontales();
     	modulosVert = estante.getGrupo().getModulosVert();
     	modulosHor = estante.getGrupo().getModulosHor();
     	//DIVIDIMOS PARA SABER CADA CUANTO CREAR LOS MODULOS.
     	iterarCountVert = verticales/modulosVert;
     	iterarCountHor = horizontales/modulosHor;

		//ITERAMOS LAS HORIZONTALES Y VERTICALES CREANDO LOS MODULOS Y POSICIONES.
 		//obtenemos una sesión
//		session = getSession();
		Modulo[] modulos = null;
		for(int hor=1; hor<horizontales+1; hor++){
			modCountVert = 0;				
			if(hor == iterarCountHor*modCountHor + 1 || hor == 1){
				modulos = new Modulo[modulosVert];
			}
			for(int ver=1; ver<verticales+1; ver++){
				Modulo modulo = null;
				Posicion posicion = null;
				if(hor == 1 && ver == 1){
					//Creamos el primer modulo
					modulo = new Modulo();
					modulo.setCodigo(String.valueOf(codigoModulo));
					modulo.setOffsetHorizontal(hor - 1);
					modulo.setOffsetVertical(ver - 1);
					modulo.setEstante(estante);					
					modCountVert ++;
					modCountHor ++;
					modulo.setPosHorizontal(modCountHor);
					modulo.setPosVertical(modCountVert);
					modulo.setCodigoBarra(getCodigoPosicion(estante, modulo));
					session.save(modulo);
					modCountHorAnt++;
					
					modulos[modCountVert-1] = modulo;
					
					//Creamos la primer posicion
					posicion = new Posicion();
					posicion.setEstado("DISPONIBLE");
					posicion.setEstante(estante);
					posicion.setModulo(modulo);
					posicion.setPosHorizontal(hor);
					posicion.setPosVertical(ver);
					posicion.setCodigo(getCodigoPosicion(estante, posicion));
					session.save(posicion);
					
					posicion = null;
					modulo = null;
				}
				//Para crear los modulos al comienzo del recorrido vertical
				if(ver==1 && hor == iterarCountHor*modCountHor + 1){
					codigoModulo++;
					//Creamos el modulo
					modulo = new Modulo();
					modulo.setCodigo(String.valueOf(codigoModulo));
					modulo.setOffsetHorizontal(hor - 1);
					modulo.setOffsetVertical(ver - 1);
					modulo.setEstante(estante);
					
					modCountVert ++;
					modCountHorAnt = modCountHor;
					modCountHor ++;
					
					modulo.setPosHorizontal(modCountHor);
					modulo.setPosVertical(modCountVert);
					modulo.setCodigoBarra(getCodigoPosicion(estante, modulo));
					
					session.save(modulo);
					
					modulos[modCountVert-1] = modulo;
					
					modulo = null;
				}
				
				//Para crear los modulos Verticales de los lugares intermedios
				if(ver == iterarCountVert*modCountVert + 1 && hor == iterarCountHor*modCountHorAnt + 1){
					codigoModulo++;
					//Creamos el modulo
					modulo = new Modulo();
					modulo.setCodigo(String.valueOf(codigoModulo));
					modulo.setOffsetHorizontal(hor - 1);
					modulo.setOffsetVertical(ver - 1);
					modulo.setEstante(estante);
					
					modCountVert ++;
					
					modulo.setPosHorizontal(modCountHor);
					modulo.setPosVertical(modCountVert);
					modulo.setCodigoBarra(getCodigoPosicion(estante, modulo));
					
					session.save(modulo);
					
					modulos[modCountVert-1] = modulo;
					
					modulo = null;
				}
				
				//Para crear los primeros modulos verticales antes de seguir con las horizontales
				if(ver == iterarCountVert*modCountVert + 1 && hor == 1){
					codigoModulo++;
					//Creamos el modulo
					modulo = new Modulo();
					modulo.setCodigo(String.valueOf(codigoModulo));
					modulo.setOffsetHorizontal(hor - 1);
					modulo.setOffsetVertical(ver - 1);
					modulo.setEstante(estante);
					
					modCountVert ++;
					
					modulo.setPosHorizontal(modCountHor);
					modulo.setPosVertical(modCountVert);
					modulo.setCodigoBarra(getCodigoPosicion(estante, modulo));
					
					session.save(modulo);
					
					modulos[modCountVert-1] = modulo;
					
					modulo = null;
				}
				if(hor != 1 || ver != 1){
					if(hor < iterarCountHor*modCountHor + 1 && hor <= iterarCountHor && hor > 1 &&  ver==1){
						modCountVert++;
					}
					if(hor < iterarCountHor*modCountHor + 1 && hor <= iterarCountHor && hor > 1 &&  ver == iterarCountVert*modCountVert + 1){
						modCountVert++;
					}
					if(hor < iterarCountHor*modCountHor + 1 && hor > iterarCountHor*modCountHorAnt + 1 && hor > iterarCountHor &&  ver==1){
						modCountVert++;
					}
					if(hor < iterarCountHor*modCountHor + 1 && hor > iterarCountHor*modCountHorAnt + 1 && hor > iterarCountHor &&  ver == iterarCountVert*modCountVert + 1){
						modCountVert++;
					}
					posicion = new Posicion();					
					posicion.setEstado("DISPONIBLE");
					posicion.setEstante(estante);
					posicion.setModulo(modulos[modCountVert-1]);
					posicion.setPosHorizontal(hor);
					posicion.setPosVertical(ver);
					posicion.setCodigo(getCodigoPosicion(estante, posicion));
					session.save(posicion);
				}					
				
			}
		}
	}
	
	/*
	 * Devolvemos un codigo formado por los codigos de (Deposito, Seccion, Estante, 
	 * Posicion(PosVertical, PosHorizontal), checksum EAN13) 
	 */
	public static String getCodigoPosicion(Estante estante, Object posMod){
		Posicion posicion = null;
		Modulo modulo = null;
		//Creamos el codigo
		String codigo = "98";//getFormatString(2,estante.getGrupo().getSeccion().getDeposito().getCodigo()); //+ getFormatString(2,estante.getGrupo().getSeccion().getCodigo());
		codigo += getFormatString(4,estante.getCodigo());
		//Si es posicion, casteamos a posicion
		if(posMod instanceof Posicion){
			posicion  = (Posicion)posMod;
			codigo += getFormatString(3, posicion.getPosVertical().toString()) + getFormatString(3,posicion.getPosHorizontal().toString());
		}
		//Si es modulo, casteamos a modulo
		if(posMod instanceof Modulo){
			modulo  = (Modulo)posMod;
			codigo = "99";
			codigo +=  getFormatString(4, estante.getCodigo());
			codigo +=getFormatString(3, modulo.getOffsetVertical().toString()) + getFormatString(3, modulo.getOffsetHorizontal().toString());
		}
		//Obtenemos el checkSum y lo posicionamos al final del codigo.
//		Long a = EAN13.EAN13_CHECKSUM(codigo);
//		codigo += String.valueOf(a.toString());
		return codigo;
	}
	
//	public static void main(String[] args){
//		Modulo modulo = new Modulo();
//		modulo.setPosVertical(1);
//		modulo.setPosHorizontal(1);
//		Estante estante = new Estante();
//		estante.setCodigo("123");
//		System.out.println(getCodigoPosicion(estante, modulo));
//	}
	
	public static String getFormatString(Integer length, String value){
		return String.format("%0"+length.toString()+"d", new Integer(value));
	}
}
