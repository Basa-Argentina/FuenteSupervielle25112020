package com.security.modelo.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.math.BigInteger;
import java.util.Date;
import java.util.Formatter;
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
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.seguridad.User;

/**
 * 
 * @author Gabriel Mainero
 *
 */
@Entity(name="requerimiento")
public class Requerimiento implements Comparable<Requerimiento>{
	private Long id;
	private ClienteAsp clienteAsp;
	private Sucursal sucursal;
	private TipoRequerimiento tipoRequerimiento;
	private ClienteEmp clienteEmp;
	private Empleado empleadoSolicitante;
	private Empleado empleadoAutorizante;
	private Date fechaAlta;
	private Date fechaEntrega;
	private Date fechaCierre;
	private Serie serie;
	private BigInteger prefijo;
	private BigInteger numero;
	private ClienteDireccion direccionDefecto;
	private String estado;
	private String horaAlta;  //Formato (HH:MM)
	private String horaEntrega;  //Formato (HH:MM)
	private String horaCierre;  //Formato (HH:MM)
	private Set<RequerimientoReferencia> listaElementos;
	private User usuario;
	private Deposito depositoDefecto;
	private ListaPrecios listaPrecios;
	private String observaciones;
	private Integer cantidad;
	private Long remitoId;
	
	private transient String accion;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient Date fechaEntregaDesde;
	private transient Date fechaEntregaHasta;
	private transient BigInteger serieDesde;
	private transient String serieDesdeStr;
	private transient BigInteger serieHasta;
	private transient String serieHastaStr;
	private transient String clienteCodigo;
	private transient Long idClienteEmp;
	private transient String codigoDireccion;
	private transient String codigoSerie;
	private transient String tipoRequerimientoCod;
	private transient String codigoPersonal;
	private transient String codigoPersonalAutorizante;
	private transient boolean buscarElemento;
	private transient boolean buscarElementoSinReferencia;
	private transient boolean eliminarElemento;
	private transient boolean insertarElementoDirecto;
	private transient String codigoElemento;
	private transient Elemento elemento;
	private transient Long eliminarElementoId;
	private transient boolean cambioDireccionDefecto;
	private transient String codigoDeposito;
	private transient String listaPrecioCodigo;
	
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	private transient Long orden;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public TipoRequerimiento getTipoRequerimiento() {
		return tipoRequerimiento;
	}
	public void setTipoRequerimiento(TipoRequerimiento tipoRequerimiento) {
		this.tipoRequerimiento = tipoRequerimiento;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Empleado getEmpleadoSolicitante() {
		return empleadoSolicitante;
	}
	public void setEmpleadoSolicitante(Empleado empleadoSolicitante) {
		this.empleadoSolicitante = empleadoSolicitante;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Empleado getEmpleadoAutorizante() {
		return empleadoAutorizante;
	}
	public void setEmpleadoAutorizante(Empleado empleadoAutorizante) {
		this.empleadoAutorizante = empleadoAutorizante;
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
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	public BigInteger getPrefijo() {
		return prefijo;
	}
	public void setPrefijo(BigInteger prefijo) {
		this.prefijo = prefijo;
	}
	public BigInteger getNumero() {
		return numero;
	}
	public void setNumero(BigInteger numero) {
		this.numero = numero;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteDireccion getDireccionDefecto() {
		return direccionDefecto;
	}
	public void setDireccionDefecto(ClienteDireccion direccionDefecto) {
		this.direccionDefecto = direccionDefecto;
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
	@Column(length=5)
	public String getHoraEntrega() {
		return horaEntrega;
	}
	public void setHoraEntrega(String horaEntrega) {
		this.horaEntrega = horaEntrega;
	}
	public String getHoraCierre() {
		return horaCierre;
	}
	public void setHoraCierre(String horaCierre) {
		this.horaCierre = horaCierre;
	}
	@OneToMany(	mappedBy="requerimiento", 
			fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL,
			orphanRemoval = true)
	public Set<RequerimientoReferencia> getListaElementos() {
		return listaElementos;
	}
	public void setListaElementos(Set<RequerimientoReferencia> listaElementos) {
		this.listaElementos = listaElementos;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Deposito getDepositoDefecto() {
		return depositoDefecto;
	}
	public void setDepositoDefecto(Deposito depositoDefecto) {
		this.depositoDefecto = depositoDefecto;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ListaPrecios getListaPrecios() {
		return listaPrecios;
	}
	public void setListaPrecios(ListaPrecios listaPrecios) {
		this.listaPrecios = listaPrecios;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	@Column(name="remito_id")
	public Long getRemitoId() {
		return remitoId;
	}
	public void setRemitoId(Long remitoId) {
		this.remitoId = remitoId;
	}
	@Transient
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
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
	public void setFechaEntregaHasta(Date fechaEntregaHasta) {
		this.fechaEntregaHasta = fechaEntregaHasta;
	}
	@Transient
	public BigInteger getSerieDesde() {
		return serieDesde;
	}
	@Transient
	public void setSerieDesde(BigInteger serieDesde) {
		this.serieDesde = serieDesde;
	}
	@Transient
	public BigInteger getSerieHasta() {
		return serieHasta;
	}
	@Transient
	public void setSerieHasta(BigInteger serieHasta) {
		this.serieHasta = serieHasta;
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
	public Long getIdClienteEmp() {
		return idClienteEmp;
	}
	@Transient
	public void setIdClienteEmp(Long idClienteEmp) {
		this.idClienteEmp = idClienteEmp;
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
	public String getCodigoSerie() {
		return codigoSerie;
	}
	@Transient
	public void setCodigoSerie(String codigoSerie) {
		this.codigoSerie = codigoSerie;
	}
	@Transient
	public String getTipoRequerimientoCod() {
		return tipoRequerimientoCod;
	}
	@Transient
	public void setTipoRequerimientoCod(String tipoRequerimientoCod) {
		this.tipoRequerimientoCod = tipoRequerimientoCod;
	}
	@Transient
	public String getCodigoPersonal() {
		return codigoPersonal;
	}
	@Transient
	public void setCodigoPersonal(String codigoPersonal) {
		this.codigoPersonal = codigoPersonal;
	}
	@Transient
	public String getCodigoPersonalAutorizante() {
		return codigoPersonalAutorizante;
	}
	@Transient
	public void setCodigoPersonalAutorizante(String codigoPersonalAutorizante) {
		this.codigoPersonalAutorizante = codigoPersonalAutorizante;
	}
	@Override
	public int compareTo(Requerimiento o) {
		int cmp = getId().compareTo(o.getId());
		return cmp;		
	}
	@Transient
	public boolean isBuscarElemento() {
		return buscarElemento;
	}
	@Transient
	public void setBuscarElemento(boolean buscarElemento) {
		this.buscarElemento = buscarElemento;
	}
	@Transient
	public boolean isBuscarElementoSinReferencia() {
		return buscarElementoSinReferencia;
	}
	@Transient
	public void setBuscarElementoSinReferencia(boolean buscarElementoSinReferencia) {
		this.buscarElementoSinReferencia = buscarElementoSinReferencia;
	}
	@Transient
	public boolean isEliminarElemento() {
		return eliminarElemento;
	}
	@Transient
	public void setEliminarElemento(boolean eliminarElemento) {
		this.eliminarElemento = eliminarElemento;
	}
	@Transient
	public Long getEliminarElementoId() {
		return eliminarElementoId;
	}
	@Transient
	public void setEliminarElementoId(Long eliminarElementoId) {
		this.eliminarElementoId = eliminarElementoId;
	}
	
	@Transient
	public String getNumeroStr(){
		Formatter fmt = new Formatter();
		if(numero!=null){
			fmt.format("%08d",numero);
			return fmt.toString();
		}
		else
			return "";
	}
	@Transient
	public String getPrefijoStr(){
		Formatter fmt = new Formatter();
		if(prefijo!=null){
			fmt.format("%04d",prefijo);
			return fmt.toString();
		}
		else
			return "";
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
	public boolean isCambioDireccionDefecto() {
		return cambioDireccionDefecto;
	}
	@Transient
	public void setCambioDireccionDefecto(boolean cambioDireccionDefecto) {
		this.cambioDireccionDefecto = cambioDireccionDefecto;
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
	public String getListaPrecioCodigo() {
		return listaPrecioCodigo;
	}
	@Transient
	public void setListaPrecioCodigo(String listaPrecioCodigo) {
		this.listaPrecioCodigo = listaPrecioCodigo;
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
	public String getRequerimientoStr(){
		return serie.getCodigo()+": "+getPrefijoStr()+" - "+getNumeroStr();
	}
	@Transient
	public String getSerieDesdeStr() {
		return serieDesdeStr;
	}
	@Transient
	public void setSerieDesdeStr(String serieDesdeStr) {
		this.serieDesdeStr = serieDesdeStr;
	}
	@Transient
	public String getSerieHastaStr() {
		return serieHastaStr;
	}
	@Transient
	public void setSerieHastaStr(String serieHastaStr) {
		this.serieHastaStr = serieHastaStr;
	}
	@Transient
	public boolean isInsertarElementoDirecto() {
		return insertarElementoDirecto;
	}
	@Transient
	public void setInsertarElementoDirecto(boolean insertarElementoDirecto) {
		this.insertarElementoDirecto = insertarElementoDirecto;
	}
	@Transient
	public String getCodigoElemento() {
		return codigoElemento;
	}
	@Transient
	public void setCodigoElemento(String codigoElemento) {
		this.codigoElemento = codigoElemento;
	}
	@Transient
	public Elemento getElemento() {
		return elemento;
	}
	@Transient
	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
	
}	
