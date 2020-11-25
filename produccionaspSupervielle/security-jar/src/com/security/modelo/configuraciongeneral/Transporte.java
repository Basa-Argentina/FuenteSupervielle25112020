package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.security.modelo.administracion.ClienteAsp;

@Entity
@NamedQueries({
        @NamedQuery(name = "Transporte.listarTransporteFiltradasNamed", query = "SELECT t FROM Transporte t where " +
        		"(:empresaId is null or t.empresa.id=:empresaId)" +
        		" and (:codigoEmpresa is null or t.empresa.codigo=:codigoEmpresa)")
})
@Table(name = "transporte")
public class Transporte {
	
	private Long id;
	private Empresa empresa;
	private ClienteAsp clienteAsp;
	private Integer codigo;
	private String descripcion;
	private boolean habilitado;
	private String patente;
	private Integer capacidad;
	private transient String accion;
	private transient Long idEmpresa;
	private transient Integer codigoMin;
	private transient Integer codigoMax;
	private transient String codigoEmpresa;
	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isHabilitado() {
		return habilitado;
	}
	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	@Column(columnDefinition = "VARCHAR(6)")
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public Integer getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@Transient
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	@Transient
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
	public Integer getCodigoMin() {
		return codigoMin;
	}
	@Transient
	public void setCodigoMin(Integer codigoMin) {
		this.codigoMin = codigoMin;
	}
	@Transient
	public Integer getCodigoMax() {
		return codigoMax;
	}
	@Transient
	public void setCodigoMax(Integer codigoMax) {
		this.codigoMax = codigoMax;
	}
	@Transient
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	@Transient
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	
	

}
