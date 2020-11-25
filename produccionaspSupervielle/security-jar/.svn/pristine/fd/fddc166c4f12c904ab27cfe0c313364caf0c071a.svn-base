package com.security.modelo.configuraciongeneral;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.User;
import com.security.recursos.Configuracion;
/**
 * 
 * @author Victor Kenis
 *
 */
@Entity(name="loteFacturacion")
public class LoteFacturacion {
	private Long id;//codigo
	private ClienteAsp clienteAsp;
	private User usuarioRegistro;
	private Empresa empresa;
	private Sucursal sucursal;
	private String periodo;
	private String anoPeriodo;
	private Date fechaFacturacion;
	private Date fechaRegistro;
	private User usuarioModificacion;
	private Date fechaModificacion;
	private Long numero;
	private String descripcion;
	private String estado;
	private Set<PreFactura> detalles;
	private Integer cantidadConceptos;
	
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient String accion;
	private transient Integer numeroPagina;
	private transient Integer tamanoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuarioRegistro() {
		return usuarioRegistro;
	}
	public void setUsuarioRegistro(User usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	@Transient
	public String getUsuarioRegistroStr() {
		return usuarioRegistro.getPersona().getRazonSocial();
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(User usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getAnoPeriodo() {
		return anoPeriodo;
	}
	public void setAnoPeriodo(String anoPeriodo) {
		this.anoPeriodo = anoPeriodo;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Date getFechaFacturacion() {
		return fechaFacturacion;
	}
	public void setFechaFacturacion(Date fechaFacturacion) {
		this.fechaFacturacion = fechaFacturacion;
	}
	@OneToMany(mappedBy="loteFacturacion",fetch=FetchType.LAZY,cascade=CascadeType.ALL,orphanRemoval=true)
	public Set<PreFactura> getDetalles() {
		return detalles;
	}
	public void setDetalles(Set<PreFactura> detalles) {
		this.detalles = detalles;
	}
	@Transient
	public String getFechaRegistroStr(){
		if(fechaRegistro!=null)
			return Configuracion.formatoFechaFormularios.format(fechaRegistro);
		return "";
	}
	@Transient
	public String getFechaModificacionStr(){
		if(fechaModificacion!=null)
			return Configuracion.formatoFechaFormularios.format(fechaModificacion);
		return "";
	}
	@Transient
	public String getFechaFacturacionStr(){
		if(fechaFacturacion!=null)
			return Configuracion.formatoFechaFormularios.format(fechaFacturacion);
		return "";
	}
	@Transient
	public String getPeriodoStr(){
		SimpleDateFormat sd = new SimpleDateFormat("MM");
		if(periodo!=null)
			return sd.format(periodo);
		return "";
	}
	
	public Integer getCantidadConceptos() {
		return cantidadConceptos;
	}
	public void setCantidadConceptos(Integer cantidadConceptos) {
		this.cantidadConceptos = cantidadConceptos;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	@Transient
	public String getNumeroStr() {
		String numero = String.valueOf(getNumero());
		if (numero == null)
			return "";
		int cant = numero.length();
		int largo = 8 - cant;
		for (int i = 0; i < largo; i++) {
			numero = "0" + numero;
		}
		return numero;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Transient
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	@Transient
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	@Transient
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	@Transient
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	@Transient
	public String getAccion() {
		return accion;
	}
	@Transient
	public void setAccion(String accion) {
		this.accion = accion;
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
	public String getFechaDesdeStr(){
		if(fechaDesde!=null)
			return Configuracion.formatoFechaFormularios.format(fechaDesde);
		return "";
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
	public String getFechaHastaStr(){
		if(fechaHasta!=null)
			return Configuracion.formatoFechaFormularios.format(fechaHasta);
		return "";
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
	public Integer getTamanoPagina() {
		return tamanoPagina;
	}
	@Transient
	public void setTamanoPagina(Integer tamanoPagina) {
		this.tamanoPagina = tamanoPagina;
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
	
}
