package com.security.modelo.configuraciongeneral;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.User;
import com.security.recursos.Configuracion;
/**
 * 
 * @author FedeMz
 *
 */
@Entity
@Table(name="lotereferencia")
public class LoteReferencia {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name="codigo")
	private Long codigo;
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="cliente_asp_id")
	private ClienteAsp clienteAsp;
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="empresa_id")
	private Empresa empresa;
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="sucursal_id")
	private Sucursal sucursal;
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="cliente_emp_id")
	private ClienteEmp clienteEmp;
	@Column(name="fecha_registro")
	private Date fechaRegistro;
	@Column(name="habilitado")
	private Boolean habilitado;
	@OneToMany(mappedBy="loteReferencia",fetch=FetchType.LAZY,cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Referencia> referencias;
	@Column(name="cargaPorRango")
	private Boolean cargaPorRango;
	@Column(name="nombreArchivoPlanilla")
	private String nombreArchivoPlanilla;
	@Transient
	@Column(name="cantidadRef")
	private transient Integer cantidadRef;
	@Transient
	@Column(name="usuarioCarga")
	private transient String usuarioCarga;
	private transient List<Referencia> modificadas;
	private transient List<Referencia> eliminadas;
	private transient Boolean indiceIndividual=true;
	private transient User usuario;
	@Transient
	@Column(name="clienteEmpStr")
	private transient String clienteEmpStr;
	@Transient
	@Column(name="empresaStr")
	private transient String empresaStr;
	@Transient
	@Column(name="sucursalStr")
	private transient String sucursalStr;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo.longValue();
	}
	
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	
	public List<Referencia> getReferencias() {
		return referencias;
	}
	public void setReferencias(List<Referencia> referencias) {
		this.referencias = referencias;
	}
	
	@Transient
	public String getFechaRegistroStr(){
		if(fechaRegistro!=null)
			return Configuracion.formatoFechaFormularios.format(fechaRegistro);
		return "";
	}
	public void setFechaRegistroStr(String fecha){}
	@Transient
	public Boolean getIndiceIndividual() {
		return indiceIndividual;
	}
	public void setIndiceIndividual(Boolean indiceIndividual) {
		this.indiceIndividual = indiceIndividual;
	}
	
	public Boolean getCargaPorRango() {
		return cargaPorRango;
	}
	public void setCargaPorRango(Boolean cargaPorRango) {
		this.cargaPorRango = cargaPorRango;
	}
	
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
	@Transient
	public List<Referencia> getModificadas() {
		return modificadas;
	}
	@Transient
	public void setModificadas(List<Referencia> modificadas) {
		this.modificadas = modificadas;
	}
	@Transient
	public List<Referencia> getEliminadas() {
		return eliminadas;
	}
	@Transient
	public void setEliminadas(List<Referencia> eliminadas) {
		this.eliminadas = eliminadas;
	}
	
	public String getNombreArchivoPlanilla() {
		return nombreArchivoPlanilla;
	}
	public void setNombreArchivoPlanilla(String nombreArchivoPlanilla) {
		this.nombreArchivoPlanilla = nombreArchivoPlanilla;
	}
	@Transient
	public User getUsuario() {
		return usuario;
	}
	@Transient
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	@Transient
	public Integer getCantidadRef() {
		return cantidadRef;
	}
	@Transient
	public void setCantidadRef(Integer cantidadRef) {
		this.cantidadRef = cantidadRef;
	}
	@Transient
	public void setCantidadRef(BigDecimal cantidadRef) {
		this.cantidadRef = cantidadRef.intValue();
	}
	@Transient
	public String getUsuarioCarga() {
		return usuarioCarga;
	}
	@Transient
	public void setUsuarioCarga(String usuarioCarga) {
		this.usuarioCarga = usuarioCarga;
	}
	@Transient
	public String getClienteEmpStr() {
		return clienteEmpStr;
	}
	@Transient
	public void setClienteEmpStr(String clienteEmpStr) {
		this.clienteEmpStr = clienteEmpStr;
	}
	@Transient
	public String getEmpresaStr() {
		return empresaStr;
	}
	@Transient
	public void setEmpresaStr(String empresaStr) {
		this.empresaStr = empresaStr;
	}
	@Transient
	public String getSucursalStr() {
		return sucursalStr;
	}
	@Transient
	public void setSucursalStr(String sucursalStr) {
		this.sucursalStr = sucursalStr;
	}
	
	
}
