package com.security.modelo.configuraciongeneral;

import java.math.BigInteger;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.Operacion;
import com.security.recursos.Configuracion;
/**
 * 
 * @author Luis Manzanelli
 *
 */
@Entity(name="hojaRuta")
public class HojaRuta {
	private Long id;//codigo
	private ClienteAsp clienteAsp;
	private Empresa empresa;
	private Sucursal sucursal;
	private ClienteEmp clienteEmp;
	private Transporte transporte;
	private Date fecha;
	private String hora;  //Formato (HH:MM)
	private Date fechaSalida;
	private String horaSalida;  //Formato (HH:MM)
	private Serie serie;
	private BigInteger numero;
	private String estado;
	private String observacion;
	private List<Operacion> operacionPlanificadas;
	private Set<HojaRutaOperacionElemento> operacionesElementos;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient String codigoCliente;
	private transient String codigoTransporte;
	private transient String codigoSerie;
	private transient String numeroStr;
	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="cliente_asp_id")
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="empresa_id")
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="sucursal_id")
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="cliente_emp_id")
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	
	@Column(length=60)
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Column(name="fecha")
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
	@Column(name="fecha_salida")
	public Date getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}	
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	
	@Column(name="numero")
	public BigInteger getNumero() {
		return numero;
	}
	public void setNumero(BigInteger numero) {
		this.numero = numero;
	}
	
	@Column(name="hora")
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	@Column(name="horaSalida")
	public String getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}
	
	@OneToMany(mappedBy="hojaRuta",fetch=FetchType.LAZY,cascade=CascadeType.ALL,orphanRemoval=true)
	public Set<HojaRutaOperacionElemento> getOperacionesElementos() {
		return operacionesElementos;
	}

	public void setOperacionesElementos(Set<HojaRutaOperacionElemento> operacionesElementos) {
		this.operacionesElementos = operacionesElementos;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Transporte getTransporte() {
		return transporte;
	}
	public void setTransporte(Transporte transporte) {
		this.transporte = transporte;
	}
	@Column(name="observacion")
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	//------------- TRANSIENT ------------------
	
	@Transient
	public List<Operacion> getOperacionPlanificadas() {
		return operacionPlanificadas;
	}
	public void setOperacionPlanificadas(List<Operacion> operacionPlanificadas) {
		this.operacionPlanificadas = operacionPlanificadas;
	}
	@Transient
	public String getFechaStr(){
		if(fecha!=null)
			return Configuracion.formatoFechaFormularios.format(fecha);
		return "";
	}
	public void setFechaStr(String fecha){}
	
	@Transient
	public String getFechaSalidaStr(){
		if(fechaSalida!=null)
			return Configuracion.formatoFechaFormularios.format(fechaSalida);
		return "";
	}
	public void setFechaSalidaStr(String fechaSalida){}
	
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
	public void setNumeroStr(String numeroStr){}
	
	@Transient
	public String getFechaHoraStr(){
		String salida = "";
		salida += getFechaStr() + " ";
		if(hora!=null)
			salida += hora;
		return salida;
	
	}
	public void setFechaHoraStr(String fechaHoraStr){}
	
	@Transient
	public String getFechaHoraSalidaStr(){
		String salida = "";
		salida += getFechaSalidaStr() + " ";
		if(horaSalida!=null)
			salida += horaSalida;
		return salida;
	}
	public void setFechaHoraSalidaStr(String fechaHoraSalidaStr){}
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
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getCodigoTransporte() {
		return codigoTransporte;
	}
	public void setCodigoTransporte(String codigoTransporte) {
		this.codigoTransporte = codigoTransporte;
	}
	public String getCodigoSerie() {
		return codigoSerie;
	}
	public void setCodigoSerie(String codigoSerie) {
		this.codigoSerie = codigoSerie;
	}
	
	
}
