package com.dividato.configuracionGeneral.objectForms;

import java.util.Date;

import com.security.recursos.Configuracion;
/**
 * 
 * @author Federico Mz
 *
 */
public class LoteExportacionRearchivoBusquedaForm {
	private Long idClienteAsp;
	private String codigoEmpresa;
	private String codigoSucursal;
	private String codigoCliente;
	private Long codigoDesde;
	private Long codigoHasta;
	private Date fechaDesde;
	private Date fechaHasta;
	private Integer codigoClasificacionDocumental;
	
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
	public Integer getCodigoClasificacionDocumental() {
		return codigoClasificacionDocumental;
	}
	public void setCodigoClasificacionDocumental(
			Integer codigoClasificacionDocumental) {
		this.codigoClasificacionDocumental = codigoClasificacionDocumental;
	}
}
