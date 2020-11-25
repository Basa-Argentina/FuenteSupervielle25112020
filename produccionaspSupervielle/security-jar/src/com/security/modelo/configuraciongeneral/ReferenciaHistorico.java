package com.security.modelo.configuraciongeneral;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.User;
import com.security.recursos.Configuracion;

/**
 * @author Victor Kenis
 *
 */
@Entity
@Table(name="referencias_historico")
public class ReferenciaHistorico {

	private Long id;
	private Long idReferencia;
	private Long idLoteReferencia;
	private String codigoElemento;
	private String codigoContenedor;
	private String accion;
	private User usuario;
	private Date fechaHora;
	private ClienteAsp clienteAsp;
	private String codigoCliente;
	private String nombreCliente;
	
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient Long codigoUsuario;
	private transient Long codigoUsuarioDia;
	private transient Long codigoReferencia;

	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdReferencia() {
		return idReferencia;
	}

	public void setIdReferencia(Long idReferencia) {
		this.idReferencia = idReferencia;
	}
	
	public Long getIdLoteReferencia() {
		return idLoteReferencia;
	}
	public void setIdLoteReferencia(Long idLoteReferencia) {
		this.idLoteReferencia = idLoteReferencia;
	}
	@Column(columnDefinition = "VARCHAR(18)")
	public String getCodigoElemento() {
		return codigoElemento;
	}
	public void setCodigoElemento(String codigoElemento) {
		this.codigoElemento = codigoElemento;
	}
	@Column(columnDefinition = "VARCHAR(18)")
	public String getCodigoContenedor() {
		return codigoContenedor;
	}
	public void setCodigoContenedor(String codigoContenedor) {
		this.codigoContenedor = codigoContenedor;
	}
	@Column(columnDefinition = "VARCHAR(8)")
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	
	@Column(columnDefinition = "VARCHAR(8)")
	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
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
	@Transient
	public Integer getNumeroPagina() {
		return numeroPagina;
	}
	@Transient
	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}
	@Transient
	public Integer getTamañoPagina() {
		return tamañoPagina;
	}
	@Transient
	public void setTamañoPagina(Integer tamañoPagina) {
		this.tamañoPagina = tamañoPagina;
	}
	@Transient
	public String getFieldOrder() {
		return fieldOrder;
	}
	@Transient
	public void setFieldOrder(String fieldOrder) {
		this.fieldOrder = fieldOrder;
	}
	@Transient
	public String getSortOrder() {
		return sortOrder;
	}
	@Transient
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Transient
	public Date getFechaDesde() {
		return fechaDesde;
	}
	@Transient
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	@Transient
	public Date getFechaHasta() {
		return fechaHasta;
	}
	@Transient
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	@Transient
	public Long getCodigoUsuario() {
		return codigoUsuario;
	}
	@Transient
	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	@Transient
	public Long getCodigoReferencia() {
		return codigoReferencia;
	}
	@Transient
	public void setCodigoReferencia(Long codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}
	@Transient
	public void setFechaHoraStr(String fechaHoraStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			this.fechaHora = df.parse(fechaHoraStr);
		}catch (ParseException pe){
			fechaHora = null;
		}
	}
	@Transient
	public String getFechaDesdeStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaDesde!=null){			
				result = sd.format(fechaDesde);
			}else{
				result = "";
			}
		}catch(Exception e){
			fechaDesde = null;
			result = "";
		}
		return result;
	}
	@Transient
	public String getFechaHoraDesdeStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaDesde!=null){			
				result = sd.format(fechaDesde);
				result = result + " 00:00:00";
			}else{
				result = "";
			}
		}catch(Exception e){
			fechaDesde = null;
			result = "";
		}
		return result;
	}
	@Transient
	public void setFechaDesdeStr(String fechaDesdeStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaDesdeStr += " 00:00:00";
			this.fechaDesde = df.parse(fechaDesdeStr);
		}catch (ParseException pe){
			fechaDesde = null;
		}
	}
	@Transient
	public void setFechaHastaStr(String fechaHastaStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaHastaStr += " 23:59:59";
			fechaHasta = df.parse(fechaHastaStr);
		}catch (ParseException pe){
			fechaHasta=null;
		}
	}
	@Transient
	public String getFechaHastaStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaHasta!=null){
				result = sd.format(fechaHasta);
			}else{
				result = "";
			}
		}catch(Exception e){
			fechaHasta = null;
			result = getFechaHastaStr();
		}
		return result;
	}
	@Transient
	public String getFechaHoraHastaStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaHasta!=null){
				result = sd.format(fechaHasta);
				result = result+" 23:59:59";
			}else{
				result = "";
			}
		}catch(Exception e){
			fechaHasta = null;
			result = getFechaHastaStr();
		}
		return result;
	}
	@Transient
	public Long getCodigoUsuarioDia() {
		return codigoUsuarioDia;
	}
	@Transient
	public void setCodigoUsuarioDia(Long codigoUsuarioDia) {
		this.codigoUsuarioDia = codigoUsuarioDia;
	}	
	
	
}
