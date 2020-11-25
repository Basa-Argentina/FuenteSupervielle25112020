package com.security.modelo.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.PreFacturaDetalle;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.seguridad.User;

/**
 * 
 * @author Gabriel Mainero
 *
 */
@Entity(name="concepto_operacion_cliente")
public class ConceptoOperacionCliente implements Comparable<ConceptoOperacionCliente>{
	private Long id;
	private ConceptoFacturable conceptoFacturable;
	private String descripcion;
	private ListaPrecios listaPrecios;
	private Operacion operacion;
	private Requerimiento requerimiento;
	private ClienteAsp clienteAsp;
	private ClienteEmp clienteEmp;
	private Empresa empresa;
	private Sucursal sucursal;
	private User usuario;
	private Date fechaAlta;
	private String horaAlta;  //Formato (HH:MM)
	private BigDecimal costo;
	private BigDecimal precioBase;
	private Long cantidad;
	private BigDecimal netoUnitario;
	private BigDecimal impuestos;
	private BigDecimal finalUnitario;
	private BigDecimal netoTotal;
	private BigDecimal finalTotal;
	private String estado;
	private String tipoConcepto;
	private Factura factura;
	private Boolean asignado;
	private PreFacturaDetalle preFacturaDetalle;
	private transient String accion;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient String codigoCliente;
	private transient String codigoConcepto;
	private transient String listaPreciosCodigo;
	private transient BigDecimal precio;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient String idRequerimiento;
	private transient Long cantidadDesde;
	private transient Long cantidadHasta;
	private transient String numeroRequerimientoDesde;
	private transient String numeroRequerimientoHasta;
	private transient String prefijoRequerimientoDesde;
	private transient String prefijoRequerimientoHasta;
	private transient BigDecimal finalUnitarioDesde;
	private transient BigDecimal finalUnitarioHasta;
	private transient BigDecimal finalTotalDesde;
	private transient BigDecimal finalTotalHasta;
	private transient Date fechaPeriodo;
	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Requerimiento getRequerimiento() {
		return requerimiento;
	}
	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Column(length=5)
	public String getHoraAlta() {
		return horaAlta;
	}
	public void setHoraAlta(String horaAlta) {
		this.horaAlta = horaAlta;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ConceptoFacturable getConceptoFacturable() {
		return conceptoFacturable;
	}
	public void setConceptoFacturable(ConceptoFacturable conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ListaPrecios getListaPrecios() {
		return listaPrecios;
	}
	public void setListaPrecios(ListaPrecios listaPrecios) {
		this.listaPrecios = listaPrecios;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Operacion getOperacion() {
		return operacion;
	}
	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}
	public BigDecimal getCosto() {
		return costo;
	}
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}
	public BigDecimal getPrecioBase() {
		return precioBase;
	}
	public void setPrecioBase(BigDecimal precioBase) {
		this.precioBase = precioBase;
	}
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getNetoUnitario() {
		return netoUnitario;
	}
	public void setNetoUnitario(BigDecimal netoUnitario) {
		this.netoUnitario = netoUnitario;
	}
	public BigDecimal getImpuestos() {
		return impuestos;
	}
	public void setImpuestos(BigDecimal impuestos) {
		this.impuestos = impuestos;
	}
	public BigDecimal getFinalUnitario() {
		return finalUnitario;
	}
	public void setFinalUnitario(BigDecimal finalUnitario) {
		this.finalUnitario = finalUnitario;
	}
	public BigDecimal getNetoTotal() {
		return netoTotal;
	}
	public void setNetoTotal(BigDecimal netoTotal) {
		this.netoTotal = netoTotal;
	}
	public BigDecimal getFinalTotal() {
		return finalTotal;
	}
	public void setFinalTotal(BigDecimal finalTotal) {
		this.finalTotal = finalTotal;
	}
	public String getTipoConcepto() {
		return tipoConcepto;
	}
	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.LAZY)
	public Factura getFactura() {
		return factura;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	public Boolean getAsignado() {
		return asignado;
	}
	public void setAsignado(Boolean asignado) {
		this.asignado = asignado;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.LAZY)	
	public PreFacturaDetalle getPreFacturaDetalle() {
		return preFacturaDetalle;
	}
	public void setPreFacturaDetalle(PreFacturaDetalle preFacturaDetalle) {
		this.preFacturaDetalle = preFacturaDetalle;
	}
	
	
	@Transient
	public String getFechaAltaStr() {
		if(fechaAlta==null)
			return "";
		return formatoFechaFormularios.format(fechaAlta);
	}
	
	
	@Override
	public int compareTo(ConceptoOperacionCliente o) {
		int cmp = getId().compareTo(o.getId());
		return cmp;		
	}
	
	@Transient
	public String getFechaHoraAltaStr(){
		String salida = "";
		salida += getFechaAltaStr() + " ";
		if(horaAlta!=null)
			salida += horaAlta;
		return salida;
	
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
	public String getCodigoCliente() {
		return codigoCliente;
	}
	@Transient
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	@Transient
	public String getCodigoConcepto() {
		return codigoConcepto;
	}
	@Transient
	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}
	@Transient
	public String getListaPreciosCodigo() {
		return listaPreciosCodigo;
	}
	@Transient
	public void setListaPreciosCodigo(String listaPreciosCodigo) {
		this.listaPreciosCodigo = listaPreciosCodigo;
	}
	@Transient
	public BigDecimal getPrecio() {
		return precio;
	}
	@Transient
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	@Transient
	public Date getFechaDesde() {
		return fechaDesde;
	}
	@Transient
	public String getFechaDesdeStr() {
		if(fechaDesde==null)
			return "";
		return formatoFechaFormularios.format(fechaDesde);
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
	public String getFechaHastaStr() {
		if(fechaHasta==null)
			return "";
		return formatoFechaFormularios.format(fechaHasta);
	}
	@Transient
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	@Transient
	public String getIdRequerimiento() {
		return idRequerimiento;
	}
	@Transient
	public void setIdRequerimiento(String idRequerimiento) {
		this.idRequerimiento = idRequerimiento;
	}
	@Transient
	public Long getCantidadDesde() {
		return cantidadDesde;
	}
	@Transient
	public void setCantidadDesde(Long cantidadDesde) {
		this.cantidadDesde = cantidadDesde;
	}
	@Transient
	public Long getCantidadHasta() {
		return cantidadHasta;
	}
	@Transient
	public void setCantidadHasta(Long cantidadHasta) {
		this.cantidadHasta = cantidadHasta;
	}
	@Transient
	public BigDecimal getFinalUnitarioDesde() {
		return finalUnitarioDesde;
	}
	@Transient
	public void setFinalUnitarioDesde(BigDecimal finalUnitarioDesde) {
		this.finalUnitarioDesde = finalUnitarioDesde;
	}
	@Transient
	public BigDecimal getFinalUnitarioHasta() {
		return finalUnitarioHasta;
	}
	@Transient
	public void setFinalUnitarioHasta(BigDecimal finalUnitarioHasta) {
		this.finalUnitarioHasta = finalUnitarioHasta;
	}
	@Transient
	public BigDecimal getFinalTotalDesde() {
		return finalTotalDesde;
	}
	@Transient
	public void setFinalTotalDesde(BigDecimal finalTotalDesde) {
		this.finalTotalDesde = finalTotalDesde;
	}
	@Transient
	public BigDecimal getFinalTotalHasta() {
		return finalTotalHasta;
	}
	@Transient
	public void setFinalTotalHasta(BigDecimal finalTotalHasta) {
		this.finalTotalHasta = finalTotalHasta;
	}
	@Transient
	public String getNumeroRequerimientoDesde() {
		return numeroRequerimientoDesde;
	}
	@Transient
	public void setNumeroRequerimientoDesde(String numeroRequerimientoDesde) {
		this.numeroRequerimientoDesde = numeroRequerimientoDesde;
	}
	@Transient
	public String getNumeroRequerimientoHasta() {
		return numeroRequerimientoHasta;
	}
	@Transient
	public void setNumeroRequerimientoHasta(String numeroRequerimientoHasta) {
		this.numeroRequerimientoHasta = numeroRequerimientoHasta;
	}
	@Transient
	public String getPrefijoRequerimientoDesde() {
		return prefijoRequerimientoDesde;
	}
	@Transient
	public void setPrefijoRequerimientoDesde(String prefijoRequerimientoDesde) {
		this.prefijoRequerimientoDesde = prefijoRequerimientoDesde;
	}
	@Transient
	public String getPrefijoRequerimientoHasta() {
		return prefijoRequerimientoHasta;
	}
	@Transient
	public void setPrefijoRequerimientoHasta(String prefijoRequerimientoHasta) {
		this.prefijoRequerimientoHasta = prefijoRequerimientoHasta;
	}
	@Transient
	public String getCodigoSeriePrefijoNumero(){
		if(requerimiento == null)
			return "";
		return requerimiento.getSerie().getCodigo() + ": " + requerimiento.getPrefijoStr()+"-"+requerimiento.getNumeroStr();
	}
	@Transient
	public String getEstadoStr(){
		if(factura == null)
			return estado;
		return "Facturado en " + factura.getCodigoSeriePrefijoNumero();
	}
	@Transient
	public Date getFechaPeriodo() {
		return fechaPeriodo;
	}
	@Transient
	public void setFechaPeriodo(Date fechaPeriodo) {
		this.fechaPeriodo = fechaPeriodo;
	}
}	
