package com.security.modelo.configuraciongeneral;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.User;
import com.security.recursos.Configuracion;
/**
 * 
 * @author Gabriel Mainero
 *
 */
@Entity(name="lote_rearchivo")
public class LoteRearchivo {
	private Long id;//codigo
	private ClienteAsp clienteAsp;
	private Empresa empresa;
	private Sucursal sucursal;
	private ClienteEmp clienteEmp;
	private Date fechaRegistro;
	private ClasificacionDocumental clasificacionDocumental;
	private Elemento contenedor; //Contenedor
	private Remito remito;
	private String descripcion;
	private String tipo;
	private Boolean referenciado;
	private Set<Rearchivo> rearchivos;
	private Boolean indiceIndividual;
	private Integer cantidad;
	private LoteReferencia loteReferencia;
	private User usuario_resp1;
	private User usuario_resp2;
	private User usuario_resp3;
	private Date fecha_resp1;
	private Date fecha_resp2;
	private Date fecha_resp3;
	private transient String codigoCliente;
	private String nombreArchivoPlanilla;
	
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
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	@Column(name="fecha_registro")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClasificacionDocumental getClasificacionDocumental() {
		return clasificacionDocumental;
	}
	public void setClasificacionDocumental(
			ClasificacionDocumental clasificacionDocumental) {
		this.clasificacionDocumental = clasificacionDocumental;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Elemento getContenedor() {
		return contenedor;
	}
	public void setContenedor(Elemento contenedor) {
		this.contenedor = contenedor;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Remito getRemito() {
		return remito;
	}
	public void setRemito(Remito remito) {
		this.remito = remito;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Boolean getReferenciado() {
		return referenciado;
	}
	public void setReferenciado(Boolean referenciado) {
		this.referenciado = referenciado;
	}
	@OneToMany(mappedBy="loteRearchivo",fetch=FetchType.LAZY,cascade=CascadeType.ALL,orphanRemoval=true)
	public Set<Rearchivo> getRearchivos() {
		return rearchivos;
	}
	public void setRearchivos(Set<Rearchivo> rearchivos) {
		this.rearchivos = rearchivos;
	}
	@Column(name="indice_individual")
	public Boolean getIndiceIndividual() {
		return indiceIndividual;
	}
	public void setIndiceIndividual(Boolean indiceIndividual) {
		this.indiceIndividual = indiceIndividual;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public LoteReferencia getLoteReferencia() {
		return loteReferencia;
	}
	public void setLoteReferencia(LoteReferencia loteReferencia) {
		this.loteReferencia = loteReferencia;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuario_resp1() {
		return usuario_resp1;
	}
	public void setUsuario_resp1(User usuario_resp1) {
		this.usuario_resp1 = usuario_resp1;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuario_resp2() {
		return usuario_resp2;
	}
	public void setUsuario_resp2(User usuario_resp2) {
		this.usuario_resp2 = usuario_resp2;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuario_resp3() {
		return usuario_resp3;
	}
	public void setUsuario_resp3(User usuario_resp3) {
		this.usuario_resp3 = usuario_resp3;
	}
	public Date getFecha_resp1() {
		return fecha_resp1;
	}
	@Transient
	public String getFecha_resp1Str(){
		if(fecha_resp1!=null)
			return Configuracion.formatoFechaFormularios.format(fecha_resp1);
		return "";
	}
	public void setFecha_resp1(Date fecha_resp1) {
		this.fecha_resp1 = fecha_resp1;
	}
	public Date getFecha_resp2() {
		return fecha_resp2;
	}
	@Transient
	public String getFecha_resp2Str(){
		if(fecha_resp2!=null)
			return Configuracion.formatoFechaFormularios.format(fecha_resp2);
		return "";
	}
	public void setFecha_resp2(Date fecha_resp2) {
		this.fecha_resp2 = fecha_resp2;
	}
	public Date getFecha_resp3() {
		return fecha_resp3;
	}
	@Transient
	public String getFecha_resp3Str(){
		if(fecha_resp3!=null)
			return Configuracion.formatoFechaFormularios.format(fecha_resp3);
		return "";
	}
	public void setFecha_resp3(Date fecha_resp3) {
		this.fecha_resp3 = fecha_resp3;
	}
	@Transient
	public String getCodigoCliente() {
		return codigoCliente;
	}
	@Transient
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	@Transient
	public String getIndiceIndividualStr(){
		return indiceIndividual?"Individual":"Grupal";
	}
	public void setIndiceIndividualStr(String indice){}
	
	@Transient
	public String getFechaRegistroStr(){
		if(fechaRegistro!=null)
			return Configuracion.formatoFechaFormularios.format(fechaRegistro);
		return "";
	}

	public String getNombreArchivoPlanilla() {
		return nombreArchivoPlanilla;
	}

	public void setNombreArchivoPlanilla(String nombreArchivoPlanilla) {
		this.nombreArchivoPlanilla = nombreArchivoPlanilla;
	}

	
	
}
