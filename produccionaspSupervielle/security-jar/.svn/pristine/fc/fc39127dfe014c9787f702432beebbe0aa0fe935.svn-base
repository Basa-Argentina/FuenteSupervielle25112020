/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.configuraciongeneral;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;


/**
 * @author X
 *
 */
@Entity(name="medio_pago_recibo")
public class MedioPagoRecibo{
	private Long id;
	private Factura factura;
	private TipoMedioPago tipoMedioPago;
	private Double importe;
	private Date fechaVencimiento;
	private String numero;
	private String titular;
	private Banco banco;
	private String nota;
	private transient Long idTipoMedioPago;
	private transient Long idBancoSel;
	
	public MedioPagoRecibo() {
		super();
	
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public TipoMedioPago getTipoMedioPago() {
		return tipoMedioPago;
	}

	public void setTipoMedioPago(TipoMedioPago tipoMedioPago) {
		this.tipoMedioPago = tipoMedioPago;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}
	
	@Transient
	public String getFechaVencimientoStr(){
		if(fechaVencimiento!=null){
			return formatoFechaFormularios.format(fechaVencimiento);
		}else{
			return "";
		}
	}
	@Transient
	public void setFechaVencimientoStr(String fechaVencimientoStr){
		if(fechaVencimientoStr!=null){
			try{
				fechaVencimiento = formatoFechaFormularios.parse(fechaVencimientoStr);
			}catch(ParseException e ){
				fechaVencimiento = null;
			}
		}else{
			fechaVencimiento = null;
		}
	}
	@Transient
	public Long getIdTipoMedioPago() {
		return idTipoMedioPago;
	}
	@Transient
	public void setIdTipoMedioPago(Long idTipoMedioPago) {
		this.idTipoMedioPago = idTipoMedioPago;
	}
	@Transient
	public Long getIdBancoSel() {
		return idBancoSel;
	}

	public void setIdBancoSel(Long idBancoSel) {
		this.idBancoSel = idBancoSel;
	}
	
}
