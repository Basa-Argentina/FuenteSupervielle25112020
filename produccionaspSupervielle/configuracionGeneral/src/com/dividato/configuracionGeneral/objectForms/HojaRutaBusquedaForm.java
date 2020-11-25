package com.dividato.configuracionGeneral.objectForms;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Transient;

import com.security.modelo.configuraciongeneral.Serie;
import com.security.recursos.Configuracion;
/**
 * 
 * @author Luis Manzanelli
 *
 */
public class HojaRutaBusquedaForm {
	private Long idClienteAsp;
	private String descripcionClienteAsp;
	private String codigoEmpresa;
	private String codigoSucursal;
	private String codigoCliente;
	private Integer codigoTransporte;
	private Serie serie;
	private String codigoSerie;
	private BigInteger serieDesde;
	private BigInteger serieHasta;
	private Long numero;
	private Date fechaDesde;
	private Date fechaHasta;
	private Date fechaSalida;
	private Date fecha;
	private boolean estado;
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
	
	public String getFechaStr(){
		if(fecha!=null)
			return Configuracion.formatoFechaFormularios.format(fecha);
		return "";
	}
	public void setFechaStr(String fecha){}
	
	public String getFechaSalidaStr(){
		if(fechaSalida!=null)
			return Configuracion.formatoFechaFormularios.format(fechaSalida);
		return "";
	}
	public void setFechaSalidaStr(String fecha){}
	
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
	public Integer getCodigoTransporte() {
		return codigoTransporte;
	}
	public void setCodigoTransporte(Integer codigoTransporte) {
		this.codigoTransporte = codigoTransporte;
	}
	
	public Date getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getCodigoSerie() {
		return codigoSerie;
	}
	public void setCodigoSerie(String codigoSerie) {
		this.codigoSerie = codigoSerie;
	}
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	public BigInteger getSerieDesde() {
		return serieDesde;
	}
	public void setSerieDesde(BigInteger serieDesde) {
		this.serieDesde = serieDesde;
	}
	public BigInteger getSerieHasta() {
		return serieHasta;
	}
	public void setSerieHasta(BigInteger serieHasta) {
		this.serieHasta = serieHasta;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
