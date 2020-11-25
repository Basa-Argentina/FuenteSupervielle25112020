package com.security.modelo.seguridad;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.recursos.Configuracion;


/**
 * 
 * @author Federico Muñoz
 * 
 */
@Entity(name="applog")
public class AppLog {
	private Long id;
	private String app;
	private String nivel;
	private String mensaje;
	private String clase;
	private String lineaReferencia;
	private Date fechaHora;
	private String excepcion;
	private ClienteAsp cliente;

	@Id	@GeneratedValue(strategy=GenerationType.AUTO)       
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public String getLineaReferencia() {
		return lineaReferencia;
	}
	public void setLineaReferencia(String lineaReferencia) {
		this.lineaReferencia = lineaReferencia;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getExcepcion() {
		return excepcion;
	}
	public void setExcepcion(String excepcion) {
		this.excepcion = excepcion;
	}
	@Transient
	public boolean isLogListenerException() {
		if (clase != null && (
				clase.indexOf("LogListener")!=-1 || 
				clase.indexOf("JDBCException")!=-1 ||
				clase.indexOf("SQLException")!=-1
				))
			return true;
		if (excepcion != null && (
				excepcion.indexOf("LogListener")!=-1 || 
				excepcion.indexOf("JDBCException")!=-1 ||
				excepcion.indexOf("SQLException")!=-1
				))
			return true;
		return false;
	}
	@Transient
	public String getFechaHoraStr() {
		if(fechaHora==null)
			return "";
		return Configuracion.formatoFechaHoraFormularios.format(fechaHora);
	}	
	@Transient
	public String getFechaHoraStrCorta() {
		if(fechaHora==null)
			return "";
		return Configuracion.formatoFechaFormularios.format(fechaHora);
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getCliente() {
		return cliente;
	}
	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
	}
}
