package com.security.modelo.configuraciongeneral;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
@Entity
@Table(name="remitos")
public class Remito implements Comparable<Remito>{
	private Long id;	
	private String tipoRemito;
	private String ingresoEgreso;
	private ClienteAsp clienteAsp;
	private Empresa empresa;
	private Sucursal sucursal;
	private ClienteEmp clienteEmp;
	private Empleado empleado;
	private AfipTipoComprobante tipoComprobante;
	private Serie serie;
	private User usuario;
	private Transporte transporte;
	private String letraComprobante;
	private Set<RemitoDetalle> detalles;
	private Deposito depositoOrigen;
	private Deposito depositoDestino;
	private ClienteDireccion direccion;
	private Date fechaEmision;
	private Date fechaEntrega;
	private Date fechaImpresion;
	private String prefijo;
	private String numeroSinPrefijo;
	private Long numero;
	private String estado;
	private String observacion;
	private Integer cantidadElementos;
	private String tipoRequerimiento;
	private String numRequerimiento;
	private String empleadoSolicitante;
	private String fechaSolicitud;
	private Boolean verificaLectura;
	private Long numeroManual;
	
	private transient String accion;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient String codigoTransporte;
	private transient String codigoPersonal;
	private transient String codigoDireccion;
	private transient String codigoDepositoOrigen;
	private transient String codigoDepositoDestino;
	private transient String codigoSerie;
	private transient String codigoCliente;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient String numeroDesde;
	private transient String numeroHasta;
	private transient Set<RemitoDetalle> detallesViejos;
	
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	
	private transient Long nroGyC;
	private transient Long nroDev;
	
	private transient List<Movimiento> movAsociados;
	
	
	public Remito(){
		super();
		this.cantidadElementos = 0;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(columnDefinition = "VARCHAR(8)")
	public String getTipoRemito() {
		return tipoRemito;
	}
	public void setTipoRemito(String tipoRemito) {
		this.tipoRemito = tipoRemito;
	}
	
	@Column(columnDefinition = "VARCHAR(8)")
	public String getIngresoEgreso() {
		return ingresoEgreso;
	}
	public void setIngresoEgreso(String ingresoEgreso) {
		this.ingresoEgreso = ingresoEgreso;
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
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public AfipTipoComprobante getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(AfipTipoComprobante tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Transporte getTransporte() {
		return transporte;
	}
	public void setTransporte(Transporte transporte) {
		this.transporte = transporte;
	}
	public String getLetraComprobante() {
		return letraComprobante;
	}
	public void setLetraComprobante(String letraComprobante) {
		this.letraComprobante = letraComprobante;
	}
	@OneToMany(mappedBy = "remito", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<RemitoDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<RemitoDetalle> detalles) {
		this.detalles = detalles;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Deposito getDepositoOrigen() {
		return depositoOrigen;
	}
	public void setDepositoOrigen(Deposito depositoOrigen) {
		this.depositoOrigen = depositoOrigen;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Deposito getDepositoDestino() {
		return depositoDestino;
	}
	public void setDepositoDestino(Deposito depositoDestino) {
		this.depositoDestino = depositoDestino;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteDireccion getDireccion() {
		return direccion;
	}
	public void setDireccion(ClienteDireccion direccion) {
		this.direccion = direccion;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	@Transient
	public String getFechaEmisionStr() {
		if(fechaEmision==null){
			return "";
		}
		return formatoFechaFormularios.format(fechaEmision);
	}
	@Transient
	public String getFechaEmisionEtiqueta() {
		if(fechaEmision==null){
			return "";
		}
		SimpleDateFormat sd = new SimpleDateFormat("dd   MM   yy");
		return sd.format(fechaEmision);
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	@Transient
	public String getFechaEntregaStr() {
		if(fechaEntrega==null)
			return "";
		return formatoFechaFormularios.format(fechaEntrega);
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
	public Date getFechaImpresion() {
		return fechaImpresion;
	}
	@Transient
	public String getFechaImpresionStr() {
		if(fechaImpresion==null)
			return "";
		return formatoFechaFormularios.format(fechaImpresion);
	}
	public void setFechaImpresion(Date fechaImpresion) {
		this.fechaImpresion = fechaImpresion;
	}

	@Column(columnDefinition = "VARCHAR(4)")
	public String getPrefijo() {
		return prefijo;
	}
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	@Column(columnDefinition = "VARCHAR(8)")
	public String getNumeroSinPrefijo() {
		return numeroSinPrefijo;
	}
	public void setNumeroSinPrefijo(String numeroSinPrefijo) {
		this.numeroSinPrefijo = numeroSinPrefijo;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	@Column(columnDefinition = "VARCHAR(15)")
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Column(columnDefinition = "VARCHAR(500)")
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public Integer getCantidadElementos() {
		return cantidadElementos;
	}

	public void setCantidadElementos(Integer cantidadElementos) {
		this.cantidadElementos = cantidadElementos;
	}

	@Transient
	public String getInfo(){
		return prefijo + numero;
	}
	@Transient
	public String getLetraYNumeroComprobante(){
		String codigoSerie="";
		if(this.serie!=null)
			codigoSerie = this.serie.getCodigo();
		return codigoSerie+": "+prefijo+"-"+numeroSinPrefijo;
	}
	@Transient
	public String getDestino(){
		if(tipoRemito != null && tipoRemito.equals("cliente"))
		{
			return clienteEmp.getRazonSocialONombreYApellido();
		}
		if(tipoRemito != null && tipoRemito.equals("interno"))
		{
			return depositoDestino.getDescripcion();
		}
		else
		{
			return " ";
		}
	}
	
	@Transient
	public String getDestinoRemito(){
		if(tipoRemito != null && tipoRemito.equals("cliente"))
		{
			return clienteEmp.getCodigo()+ " - " + clienteEmp.getRazonSocialONombreYApellido();
		}
		if(tipoRemito != null && tipoRemito.equals("interno"))
		{
			return "Remito interno : "+depositoOrigen.getDescripcion()+" hacia "+depositoDestino.getDescripcion();
		}
		else
		{
			return " ";
		}
	}
	
	@Transient
	public String getDireccionDestino(){
		if(tipoRemito != null && tipoRemito.equals("cliente"))
		{
			return direccion.getDireccion().getCalle()+" - "+
			direccion.getDireccion().getNumero()+" - "+
			direccion.getDireccion().getBarrio().getLocalidad().getNombre()+" - "+
			direccion.getDireccion().getBarrio().getLocalidad().getProvincia().getNombre();
		}
		if(tipoRemito != null && tipoRemito.equals("interno"))
		{
			return " ";
		}
		else
		{
			return " ";
		}
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
	public String getCodigoTransporte() {
		return codigoTransporte;
	}
	@Transient
	public void setCodigoTransporte(String codigoTransporte) {
		this.codigoTransporte = codigoTransporte;
	}@Transient
	public String getCodigoPersonal() {
		return codigoPersonal;
	}
	@Transient
	public void setCodigoPersonal(String codigoPersonal) {
		this.codigoPersonal = codigoPersonal;
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
	public String getCodigoDepositoOrigen() {
		return codigoDepositoOrigen;
	}
	@Transient
	public void setCodigoDepositoOrigen(String codigoDepositoOrigen) {
		this.codigoDepositoOrigen = codigoDepositoOrigen;
	}
	@Transient
	public String getCodigoDepositoDestino() {
		return codigoDepositoDestino;
	}
	@Transient
	public void setCodigoDepositoDestino(String codigoDepositoDestino) {
		this.codigoDepositoDestino = codigoDepositoDestino;
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
	public String getCodigoCliente() {
		return codigoCliente;
	}
	@Transient
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
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
	public String getNumeroDesde() {
		return numeroDesde;
	}
	@Transient
	public void setNumeroDesde(String numeroDesde) {
		this.numeroDesde = numeroDesde;
	}
	@Transient
	public String getNumeroHasta() {
		return numeroHasta;
	}
	@Transient
	public void setNumeroHasta(String numeroHasta) {
		this.numeroHasta = numeroHasta;
	}

	@Override
	public int compareTo(Remito o) {
		int cmp = this.id.compareTo(o.getId());
		if(cmp != 0)return cmp;
		return cmp;
	}

	public String getTipoRequerimiento() {
		return tipoRequerimiento;
	}

	public void setTipoRequerimiento(String tipoRequerimiento) {
		this.tipoRequerimiento = tipoRequerimiento;
	}
	@Column(columnDefinition = "VARCHAR(20)")
	public String getNumRequerimiento() {
		return numRequerimiento;
	}

	public void setNumRequerimiento(String numRequerimiento) {
		this.numRequerimiento = numRequerimiento;
	}
	@Column(columnDefinition = "VARCHAR(50)")
	public String getEmpleadoSolicitante() {
		return empleadoSolicitante;
	}
	public void setEmpleadoSolicitante(String empleadoSolicitante) {
		this.empleadoSolicitante = empleadoSolicitante;
	}
	@Column(columnDefinition = "VARCHAR(10)")
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	@Column(name = "numeroManual")
	public Long getNumeroManual() {
		return numeroManual;
	}
	public void setNumeroManual(Long numeroManual) {
		this.numeroManual = numeroManual;
	}

	@Transient
	public String getIdCodigoBarra()
	{
		String idStr = id.toString();
		int cant = idStr.length();
		int totalCeros = 12 - cant;
		for(int i = 0;i<totalCeros;i++){
			idStr = "0"+idStr;
		}
		return idStr;
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
	public List<Movimiento> getMovAsociados() {
		return movAsociados;
	}
	@Transient
	public void setMovAsociados(List<Movimiento> movAsociados) {
		this.movAsociados = movAsociados;
	}

	public Boolean getVerificaLectura() {
		return verificaLectura;
	}

	public void setVerificaLectura(Boolean verificaLectura) {
		this.verificaLectura = verificaLectura;
	}
	@Transient
	public Set<RemitoDetalle> getDetallesViejos() {
		return detallesViejos;
	}
	@Transient
	public void setDetallesViejos(Set<RemitoDetalle> detallesViejos) {
		this.detallesViejos = detallesViejos;
	}
	@Transient
	public Long getNroGyC() {
		return nroGyC;
	}
	@Transient
	public void setNroGyC(Long nroGyC) {
		this.nroGyC = nroGyC;
	}
	@Transient
	public Long getNroDev() {
		return nroDev;
	}
	@Transient
	public void setNroDev(Long nroDev) {
		this.nroDev = nroDev;
	}
	
}
