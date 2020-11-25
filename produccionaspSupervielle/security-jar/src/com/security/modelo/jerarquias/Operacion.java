package com.security.modelo.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

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
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.seguridad.User;

/**
 * 
 * @author Gabriel Mainero
 *
 */
@Entity(name="operacion")
public class Operacion implements Comparable<Operacion>{
	private Long id;
	private String codigo;
	private Requerimiento requerimiento;
	private TipoOperacion tipoOperacion;
	private Deposito deposito;
	private ClienteAsp clienteAsp;
	private ClienteEmp clienteEmp;
	private User usuario;
	private Set<OperacionElemento> listaElementos;
	private Date fechaAlta;
	private Date fechaEntrega;
	private Date fechaCierre;
	private String estado;
	private String horaAlta;  //Formato (HH:MM)
	private String horaEntrega;  //Formato (HH:MM)
	private String horaCierre;  //Formato (HH:MM)
	private Integer cantidadProcesados;
	private Integer cantidadOmitidos;
	private Integer cantidadPendientes;
	private Integer cantidadProcesadosParaTraspaso;
	private String observaciones;
	private boolean finalizarOK;
	private boolean finalizarError;
	private boolean traspasar;
	private User usuarioAsignado;
	private Integer cantidadImpresiones;
	
	private transient Long idDesde;
	private transient Long idHasta;
	private transient String accion;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient Date fechaEntregaDesde;
	private transient Date fechaEntregaHasta;
	private transient Long idClienteEmp;
	private transient String codigoDeposito;
	private transient String codigoTipoOperacion;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient String codigoRequerimiento;
	private transient String codigoPersonal;
	private transient String codigoDireccion;
	private transient String clienteCodigo;
	private transient Integer cantidadElementos;
	
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	private transient Long orden;
	private transient Boolean ocultarOpEnvio;
	
	public Operacion(){
		this.cantidadProcesados = new Integer(0);
		this.cantidadOmitidos = new Integer(0);
		this.cantidadProcesadosParaTraspaso = new Integer(0);
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Requerimiento getRequerimiento() {
		return requerimiento;
	}
	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Deposito getDeposito() {
		return deposito;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
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
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	public Date getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
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
	public String getHoraEntrega() {
		return horaEntrega;
	}
	public void setHoraEntrega(String horaEntrega) {
		this.horaEntrega = horaEntrega;
	}
	@Column(length=5)
	public String getHoraCierre() {
		return horaCierre;
	}
	public void setHoraCierre(String horaCierre) {
		this.horaCierre = horaCierre;
	}
	public Integer getCantidadProcesados() {
		return cantidadProcesados;
	}
	public void setCantidadProcesados(Integer cantidadProcesados) {
		this.cantidadProcesados = cantidadProcesados;
	}
	public Integer getCantidadOmitidos() {
		return cantidadOmitidos;
	}
	public void setCantidadOmitidos(Integer cantidadOmitidos) {
		this.cantidadOmitidos = cantidadOmitidos;
	}
	public Integer getCantidadPendientes() {
		return cantidadPendientes;
	}
	public void setCantidadPendientes(Integer cantidadPendientes) {
		this.cantidadPendientes = cantidadPendientes;
	}
	public Integer getCantidadProcesadosParaTraspaso() {
		return cantidadProcesadosParaTraspaso;
	}
	public void setCantidadProcesadosParaTraspaso(
			Integer cantidadProcesadosParaTraspaso) {
		this.cantidadProcesadosParaTraspaso = cantidadProcesadosParaTraspaso;
	}
	public boolean isFinalizarOK() {
		return finalizarOK;
	}
	public void setFinalizarOK(boolean finalizarOK) {
		this.finalizarOK = finalizarOK;
	}
	public boolean isFinalizarError() {
		return finalizarError;
	}
	public void setFinalizarError(boolean finalizarError) {
		this.finalizarError = finalizarError;
	}
	public boolean isTraspasar() {
		return traspasar;
	}
	public void setTraspasar(boolean traspasar) {
		this.traspasar = traspasar;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	@OneToMany(	mappedBy="operacion", 
			fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL,
			orphanRemoval = true)
	public Set<OperacionElemento> getListaElementos() {
		return listaElementos;
	}
	public void setListaElementos(
			Set<OperacionElemento> listaElementos) {
		this.listaElementos = listaElementos;
	}
		
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	@Transient
	public String getFechaAltaStr() {
		if(fechaAlta==null)
			return "";
		return formatoFechaFormularios.format(fechaAlta);
	}
	@Transient
	public String getFechaEntregaStr() {
		if(fechaEntrega==null)
			return "";
		return formatoFechaFormularios.format(fechaEntrega);
	}
	@Transient
	public String getFechaCierreStr() {
		if(fechaCierre==null)
			return "";
		return formatoFechaFormularios.format(fechaCierre);
	}
	
	@Override
	public int compareTo(Operacion o) {
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
	public String getFechaHoraEntregaStr(){
		String salida = "";
		salida += getFechaEntregaStr() + " ";
		if(horaEntrega!=null)
			salida += horaEntrega;
		return salida;
	
	}
	@Transient
	public String getFechaHoraCierreStr(){
		String salida = "";
		salida += getFechaCierreStr() + " ";
		if(horaCierre!=null)
			salida += horaCierre;
		return salida;
	
	}
	
	@Transient
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
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
	public Date getFechaEntregaDesde() {
		return fechaEntregaDesde;
	}
	@Transient
	public void setFechaEntregaDesde(Date fechaEntregaDesde) {
		this.fechaEntregaDesde = fechaEntregaDesde;
	}
	@Transient
	public String getFechaEntregaDesdeStr() {
		if(fechaEntregaDesde==null)
			return "";
		return formatoFechaFormularios.format(fechaEntregaDesde);
	}
	@Transient
	public Date getFechaEntregaHasta() {
		return fechaEntregaHasta;
	}
	@Transient
	public String getFechaEntregaHastaStr() {
		if(fechaEntregaHasta==null)
			return "";
		return formatoFechaFormularios.format(fechaEntregaHasta);
	}
	@Transient
	public Long getIdClienteEmp() {
		return idClienteEmp;
	}
	@Transient
	public void setIdClienteEmp(Long idClienteEmp) {
		this.idClienteEmp = idClienteEmp;
	}
	@Transient
	public String getCodigoDeposito() {
		return codigoDeposito;
	}
	@Transient
	public void setCodigoDeposito(String codigoDeposito) {
		this.codigoDeposito = codigoDeposito;
	}
	@Transient
	public String getCodigoTipoOperacion() {
		return codigoTipoOperacion;
	}
	@Transient
	public void setCodigoTipoOperacion(String codigoTipoOperacion) {
		this.codigoTipoOperacion = codigoTipoOperacion;
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
	public Long getOrden() {
		return orden;
	}
	@Transient
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	@Transient
	public Long getIdDesde() {
		return idDesde;
	}
	@Transient
	public void setIdDesde(Long idDesde) {
		this.idDesde = idDesde;
	}
	@Transient
	public Long getIdHasta() {
		return idHasta;
	}
	@Transient
	public void setIdHasta(Long idHasta) {
		this.idHasta = idHasta;
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
	public String getCodigoRequerimiento() {
		return codigoRequerimiento;
	}
	@Transient
	public void setCodigoRequerimiento(String codigoRequerimiento) {
		this.codigoRequerimiento = codigoRequerimiento;
	}
	@Transient
	public String getCodigoPersonal() {
		return codigoPersonal;
	}
	@Transient
	public String getCodigoDireccion() {
		return codigoDireccion;
	}
	@Transient
	public void setCodigoDireccion(String codigoDireccion) {
		this.codigoDireccion = codigoDireccion;
	}
	@Transient
	public void setCodigoPersonal(String codigoPersonal) {
		this.codigoPersonal = codigoPersonal;
	}
	@Transient
	public String getClienteCodigo() {
		return clienteCodigo;
	}
	@Transient
	public void setClienteCodigo(String clienteCodigo) {
		this.clienteCodigo = clienteCodigo;
	}
	@Transient
	public Integer getCantidadElementos() {
		return cantidadElementos;
	}
	@Transient
	public void setCantidadElementos(Integer cantidadElementos) {
		this.cantidadElementos = cantidadElementos;
	}
	@Transient
	public String getObj_hash(){
		return super.toString();
	}
	public void setObj_hash(String hash){}
	@Transient
	public Boolean getOcultarOpEnvio() {
		return ocultarOpEnvio;
	}
	@Transient
	public void setOcultarOpEnvio(Boolean ocultarOpEnvio) {
		this.ocultarOpEnvio = ocultarOpEnvio;
	}

	public Integer getCantidadImpresiones() {
		return cantidadImpresiones;
	}
	public void setCantidadImpresiones(Integer cantidadImpresiones) {
		this.cantidadImpresiones = cantidadImpresiones;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public User getUsuarioAsignado() {
		return usuarioAsignado;
	}
	public void setUsuarioAsignado(User usuarioAsignado) {
		this.usuarioAsignado = usuarioAsignado;
	}
}	
