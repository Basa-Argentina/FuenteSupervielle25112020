/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.modelo.configuraciongeneral;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.recursos.Configuracion;
import com.security.utils.HashCodeUtil;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="stock")
public class Stock {
	private Long id;
	private ConceptoFacturable concepto;
	private Date fecha;
	private String hora;  //Formato (HH:MM)
	private String tipoMovimiento; //Combo ('Ingreso', 'Egreso')
	private String nota;
	private Integer cantidad;
	private Deposito deposito;
	private Deposito origenDestino;
	private ClienteAsp clienteAsp;
	private transient String accion;
	private transient String codigoDeposito;
	private transient String codigoSucursal;
	private transient String codigoEmpresa;
	private transient String codigoOrigenDestino;
	private transient String codigoConcepto;
	
	public Stock(){
		super();
		this.deposito = new Deposito();
		Date aux = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
		this.hora = sd.format(aux);
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ConceptoFacturable getConcepto() {
		return concepto;
	}

	public void setConcepto(ConceptoFacturable concepto) {
		this.concepto = concepto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	@Transient
	public String getFechaStr() {
		if(fecha==null)
			return "";
		return Configuracion.formatoFechaFormularios.format(fecha);
	}

	@Column(length=5)
	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	@Column(length=7)
	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	@Column(length=60)
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Deposito getDeposito() {
		return deposito;
	}

	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Deposito getOrigenDestino() {
		return origenDestino;
	}

	public void setOrigenDestino(Deposito origenDestino) {
		this.origenDestino = origenDestino;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}

	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Stock tipo = (Stock) o;		
		if (getId() != null ? !getId().equals(tipo.getId()) : tipo.getId() != null)		 
			return false;
		return true;
	}	
	
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.id);
		return result;
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
	public String getCodigoDeposito() {
		return codigoDeposito;
	}

	@Transient
	public void setCodigoDeposito(String codigoDeposito) {
		this.codigoDeposito = codigoDeposito;
	}

	@Transient
	public String getCodigoOrigenDestino() {
		return codigoOrigenDestino;
	}

	@Transient
	public void setCodigoOrigenDestino(String codigoOrigenDestino) {
		this.codigoOrigenDestino = codigoOrigenDestino;
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
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	@Transient
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
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
