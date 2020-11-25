package com.dividato.configuracionGeneral.objectForms;

import java.util.Date;

import javax.persistence.Transient;

import com.security.modelo.configuraciongeneral.Empleado;
import com.security.recursos.Configuracion;
/**
 * 
 * @author FedeMz
 *
 */
public class LoteReferenciaBusquedaForm {
	private Long idClienteAsp;
	private String descripcionClienteAsp;
	private String codigoEmpresa;
	private String codigoSucursal;
	private String codigoCliente;
	private String codigoPersonal;
	private Long codigoDesde;
	private Long codigoHasta;
	private Date fechaDesde;
	private Date fechaHasta;
	private Empleado personal;
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	
	public Long getIdClienteAsp() {
		return idClienteAsp;
	}
	public void setIdClienteAsp(Long idClienteAsp) {
		this.idClienteAsp = idClienteAsp;
	}
	
	public Long getCodigoDesde() {
		return codigoDesde;
	}
	public void setCodigoDesde(Long codigoDesde) {
		this.codigoDesde = codigoDesde;
	}
	public Long getCodigoHasta() {
		return codigoHasta;
	}
	public void setCodigoHasta(Long codigoHasta) {
		this.codigoHasta = codigoHasta;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public String getFechaDesdeStr(){
		if(fechaDesde!=null)
			return Configuracion.formatoFechaFormularios.format(fechaDesde);
		return "";
	}
	public void setFechaDesdeStr(String fecha){}
	
	public String getFechaHastaStr(){
		if(fechaHasta!=null)
			return Configuracion.formatoFechaFormularios.format(fechaHasta);
		return "";
	}
	public void setFechaHastaStr(String fecha){}
	public String getDescripcionClienteAsp() {
		return descripcionClienteAsp;
	}
	public void setDescripcionClienteAsp(String descripcionClienteAsp) {
		this.descripcionClienteAsp = descripcionClienteAsp;
	}
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getCodigoCliente() {
		return codigoCliente;
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
	public String getCodigoPersonal() {
		return codigoPersonal;
	}
	public void setCodigoPersonal(String codigoPersonal) {
		this.codigoPersonal = codigoPersonal;
	}
	public Empleado getPersonal() {
		return personal;
	}
	public void setPersonal(Empleado personal) {
		this.personal = personal;
	}
	
}
