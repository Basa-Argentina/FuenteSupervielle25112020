/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;


/**
 * @author X
 *
 */
@Entity(name="cuentaCorrienteCliente")
public class CuentaCorrienteCliente implements Comparable<CuentaCorrienteCliente>{
	private Long id;
	private ClienteEmp clienteEmp;
	private ClienteAsp clienteAsp;
	private Double saldoDeudor;	
	private Double saldoAcreedor;	
	private Double saldo;
	private Double limiteSaldo;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	
	
	public CuentaCorrienteCliente() {
		super();
		
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}

	@Override
	public int compareTo(CuentaCorrienteCliente arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Double getSaldoDeudor() {
		return saldoDeudor;
	}

	public void setSaldoDeudor(Double saldoDeudor) {
		this.saldoDeudor = saldoDeudor;
	}

	public Double getSaldoAcreedor() {
		return saldoAcreedor;
	}

	public void setSaldoAcreedor(Double saldoAcreedor) {
		this.saldoAcreedor = saldoAcreedor;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getLimiteSaldo() {
		return limiteSaldo;
	}

	public void setLimiteSaldo(Double limiteSaldo) {
		this.limiteSaldo = limiteSaldo;
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
	public String getFechaDesdeStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaDesde!=null){
				result = sd.format(fechaDesde);
			}else{
				fechaDesde= new Date();
				result = sd.format(fechaDesde);
			}
		}catch(Exception e){
			fechaDesde = null;
		}
		return result;
	}
	@Transient
	public void setFechaDesdeStr(String fechaStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			this.fechaDesde = df.parse(fechaStr);
		}catch (ParseException pe){
			fechaDesde=null;
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
				fechaHasta= new Date();
				result = sd.format(fechaHasta);
			}
		}catch(Exception e){
			fechaHasta = null;
		}
		return result;
	}
	@Transient
	public void setFechaHastaStr(String fechaStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			this.fechaHasta = df.parse(fechaStr);
		}catch (ParseException pe){
			fechaHasta=null;
		}
	}
	
	
}
