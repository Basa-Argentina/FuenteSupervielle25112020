package com.security.accesoDatos.facturacion.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.hibernate.ReferenciaServiceImp;
import com.security.accesoDatos.facturacion.interfaz.RemitosMovimientosService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.Referencia;

@Component
public class RemitosMovimientosServiceImp extends GestorHibernate<Referencia> implements RemitosMovimientosService{
	@Autowired
	public RemitosMovimientosServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<?> listarRemitosMovimientosFiltrados(int mesBusqueda, int anoBusqueda, String codigoEmpresa, String codigoCliente, String tipoRequerimientoCod) {
		Session session = null;
		final Logger logger=Logger.getLogger(ReferenciaServiceImp.class);

		try{
			
	
			//obtenemos una sesión
			session = getSession();
			String consulta = "SELECT TOP 10000  " + 
			"razonSocial, " + 
			"cantidadElementos, " + 
			"tipoRequerimiento, " + 
			"numero, " + 
			"fechaEmision, " + 
			"fechaEntrega " + 
			" " + 
			"FROM [basa].[dbo].[remitos] r " + 
			"inner join clientesEmp ce " + 
			"on r.clienteEmp_id = ce.id " + 
			"inner join personas_juridicas pj " + 
			"on pj.id = ce.razonSocial_id " + 
			"left join tipos_requerimiento tr " + 
			"on tr.descripcion = r.tipoRequerimiento " +
			"WHERE DATEPART(month, fechaEmision) =  " + mesBusqueda + " " +
			"and DATEPART(year, fechaEmision) =  " + anoBusqueda + " ";
			if(null != codigoCliente && !codigoCliente.equals("") ){
				consulta = consulta + " and ce.codigo like '"+ codigoCliente + "' "; 
			}
			if(null != tipoRequerimientoCod && !tipoRequerimientoCod.equals("") ){
				consulta = consulta + " and tr.codigo like '"+ tipoRequerimientoCod + "' "; 
			}			
			consulta = consulta + " order by razonSocial, fechaEmision ";
			
			SQLQuery q = session.createSQLQuery(consulta);
			
			return q.list();
			
		}catch(Exception e){
			logger.error("no se pudo listar",e);
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
	public List<String> obtenerTipoRequerimientoDetalle(String tipoRequerimientoCod) {
		Session session = null;
		final Logger logger=Logger.getLogger(ReferenciaServiceImp.class);

		try{
			
	
			//obtenemos una sesión
			session = getSession();
			String consulta = "SELECT TOP 1 descripcion\r\n" + 
					"  FROM [basa].[dbo].[tipos_requerimiento]\r\n" + 
					"  WHERE codigo like '" + tipoRequerimientoCod + "'";
			
			SQLQuery q = session.createSQLQuery(consulta);
			
			return q.list();
			
		}catch(Exception e){
			logger.error("no se pudo listar",e);
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
	public Class<Referencia> getClaseModelo() {
		// TODO Auto-generated method stub
		return null;
	}
	


}
