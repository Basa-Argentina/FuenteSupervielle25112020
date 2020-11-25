/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/07/2011
 */
package com.security.modelo.configuraciongeneral;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.User;


/**
 * @author Luciano de Asteinza
 *
 */
@Entity(name="movimientos")
public class Movimiento implements Cloneable{
	private Long id;
	private Elemento elemento;
	private Date fecha;
	private User usuario;
	private Posicion posicionOrigenDestino;
	private Remito remito;
	private String tipoMovimiento; //Posibles valores: INGRESO - EGRESO - TRANSFERENCIA
	private String claseMovimiento; //Posibles valores: CLIENTE - INTERNO
	private Deposito deposito;
	private Deposito depositoOrigenDestino;
	private ClienteEmp clienteEmpOrigenDestino;
	private Lectura lectura;	
	private ClienteAsp clienteAsp;
	private String estado; //Posibles valores: PENDIENTE - ANULADO
	private String descripcionRemito; //----> Este campo se usa para guarda el nro de requerimiento que genero el movimiento y no remitos
	private String estadoElemento;
	private String descripcion;
	private User responsable;
	private Long codigoCarga;
	
	private transient String accion;
	private transient String codigoDepositoActual;
	private transient String codigoTipoElemento;
	private transient String codigoClienteEmp;
	private transient String codigoElemento;
	private transient String codigoEmpresa;
	private transient String codigoSucursalActual;
	private transient String codigoSucursalOrigenDestino;
	private transient String codigoDepositoOrigenDestino;
	private transient String codigoLectura;
	private transient Long codigoRemito;
	private transient Long codigoResponsable;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient Integer tipoMovimientoInt;
	private transient Integer cantidadElementos;
	private transient List<Elemento> listaElementos;
	private transient Boolean mostrarAnulados; 
	private transient Boolean tieneRemitoAsoc;
	
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
		
	public Movimiento() {
		super();
		fechaDesde = null;
		fechaHasta = null;
		tieneRemitoAsoc = false;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Elemento getElemento() {
		return elemento;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Posicion getPosicionOrigenDestino() {
		return posicionOrigenDestino;
	}
	
	public void setPosicionOrigenDestino(Posicion posicionOrigenDestino) {
		this.posicionOrigenDestino = posicionOrigenDestino;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Remito getRemito() {
		return remito;
	}

	public void setRemito(Remito remito) {
		this.remito = remito;
	}
	
	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Deposito getDepositoOrigenDestino() {
		return depositoOrigenDestino;
	}

	public void setDepositoOrigenDestino(Deposito depositoOrigenDestino) {
		this.depositoOrigenDestino = depositoOrigenDestino;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteEmp getClienteEmpOrigenDestino() {
		return clienteEmpOrigenDestino;
	}
	@Transient
	public String getDescripcionDepositoActual()
	{
		return deposito.getDescripcion();
	}

	public void setClienteEmpOrigenDestino(ClienteEmp clienteEmpOrigenDestino) {
		this.clienteEmpOrigenDestino = clienteEmpOrigenDestino;
	}

	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Lectura getLectura() {
		return lectura;
	}

	public void setLectura(Lectura lectura) {
		this.lectura = lectura;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}

	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcionRemito() {
		return descripcionRemito;
	}

	public void setDescripcionRemito(String descripcionRemito) {
		this.descripcionRemito = descripcionRemito;
	}
	public String getEstadoElemento() {
		return estadoElemento;
	}

	public void setEstadoElemento(String estadoElemento) {
		this.estadoElemento = estadoElemento;
	}
	@Column(columnDefinition="VARCHAR(60)")
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getResponsable() {
		return responsable;
	}

	public void setResponsable(User responsable) {
		this.responsable = responsable;
	}

	@Transient
	public String getFechaStr(){
		String result = "";
		if(fecha != null){
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			result = sd.format(fecha);
		}
		return result;
	}
	@Transient
	public String getDescripcionOrigenDestino(){
		StringBuilder res=new StringBuilder("");
		if(depositoOrigenDestino!=null){
			res.append("Deposito: ");
			res.append(depositoOrigenDestino.getDescripcion());
		}else if(clienteEmpOrigenDestino!=null){
			res.append("Cliente: ");
			res.append(clienteEmpOrigenDestino.getRazonSocialONombreYApellido());
		}
		return res.toString();
	}	
	@Transient
	public String getCodigoDepositoActual() {
		return codigoDepositoActual;
	}
	@Transient
	public void setCodigoDepositoActual(String codigoDepositoActual) {
		this.codigoDepositoActual = codigoDepositoActual;
	}
	@Transient
	public String getCodigoTipoElemento() {
		return codigoTipoElemento;
	}
	@Transient
	public void setCodigoTipoElemento(String codigoTipoElemento) {
		this.codigoTipoElemento = codigoTipoElemento;
	}
	@Transient
	public String getCodigoClienteEmp() {
		return codigoClienteEmp;
	}
	@Transient
	public void setCodigoClienteEmp(String codigoClienteEmp) {
		this.codigoClienteEmp = codigoClienteEmp;
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
	public String getFechaDesdeStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaDesde!=null){			
				result = sd.format(fechaDesde);
			}else{
				result = "";
			}
		}catch(Exception e){
			fechaDesde = null;
			result = "";
		}
		return result;
	}
	@Transient
	public void setFechaDesdeStr(String fechaDesdeStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaDesdeStr += " 00:00:00";
			this.fechaDesde = df.parse(fechaDesdeStr);
		}catch (ParseException pe){
			fechaDesde = null;
		}
	}
	@Transient
	public String getFechaHastaStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaHasta!=null){
				result = sd.format(fechaHasta);
			}else{
				fechaHasta = new Date();
				result = sd.format(fechaHasta);
			}
		}catch(Exception e){
			fechaHasta = null;
			result = getFechaHastaStr();
		}
		return result;
	}
	@Transient
	public void setFechaHastaStr(String fechaHastaStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaHastaStr += " 23:59:59";
			this.fechaHasta = df.parse(fechaHastaStr);
		}catch (ParseException pe){
			fechaHasta=null;
		}
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
	public Date getFechaHasta() {
		return fechaHasta;
	}
	@Transient
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	@Transient
	public Integer getTipoMovimientoInt() {
		return tipoMovimientoInt;
	}
	@Transient
	public void setTipoMovimientoInt(Integer tipoMovimientoInt) {
		this.tipoMovimientoInt = tipoMovimientoInt;
	}
	@Transient
	public String getCodigoDepositoOrigenDestino() {
		return codigoDepositoOrigenDestino;
	}
	@Transient
	public void setCodigoDepositoOrigenDestino(String codigoDepositoOrigenDestino) {
		this.codigoDepositoOrigenDestino = codigoDepositoOrigenDestino;
	}
	@Transient
	public String getCodigoLectura() {
		return codigoLectura;
	}
	@Transient
	public void setCodigoLectura(String codigoLectura) {
		this.codigoLectura = codigoLectura;
	}

	public String getClaseMovimiento() {
		return claseMovimiento;
	}

	public void setClaseMovimiento(String claseMovimiento) {
		this.claseMovimiento = claseMovimiento;
	}
	
	public Long getCodigoCarga() {
		return codigoCarga;
	}

	public void setCodigoCarga(Long codigoCarga) {
		this.codigoCarga = codigoCarga;
	}

	@Transient
	public String getCodigoSucursalOrigenDestino() {
		return codigoSucursalOrigenDestino;
	}
	@Transient
	public void setCodigoSucursalOrigenDestino(String codigoSucursalOrigenDestino) {
		this.codigoSucursalOrigenDestino = codigoSucursalOrigenDestino;
	}
	@Transient
	public String getCodigoSucursalActual() {
		return codigoSucursalActual;
	}
	@Transient
	public void setCodigoSucursalActual(String codigoSucursalActual) {
		this.codigoSucursalActual = codigoSucursalActual;
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
	public Long getCodigoResponsable() {
		return codigoResponsable;
	}
	@Transient
	public void setCodigoResponsable(Long codigoResponsable) {
		this.codigoResponsable = codigoResponsable;
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
	public List<Elemento> getListaElementos() {
		return listaElementos;
	}
	@Transient
	public void setListaElementos(List<Elemento> listaElementos) {
		this.listaElementos = listaElementos;
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
	public Boolean getMostrarAnulados() {
		return mostrarAnulados;
	}
	@Transient
	public Boolean getTieneRemitoAsoc() {
		return tieneRemitoAsoc;
	}
	@Transient
	public void setTieneRemitoAsoc(Boolean tieneRemitoAsoc) {
		this.tieneRemitoAsoc = tieneRemitoAsoc;
	}

	@Transient
	public void setMostrarAnulados(Boolean mostrarAnulados) {
		this.mostrarAnulados = mostrarAnulados;
	}
	@Transient
	public Long getCodigoRemito() {
		return codigoRemito;
	}
	@Transient
	public void setCodigoRemito(Long codigoRemito) {
		this.codigoRemito = codigoRemito;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Elemento obj=null;
        try{
            obj=(Elemento)super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Movimiento)) {
			return false;
		}
		Movimiento other = (Movimiento) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
}
