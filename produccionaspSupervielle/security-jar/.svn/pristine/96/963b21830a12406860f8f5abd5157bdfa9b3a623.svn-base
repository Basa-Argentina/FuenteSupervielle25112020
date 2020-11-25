/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Date;

import javax.persistence.Entity;

import com.security.recursos.Configuracion;
import com.security.utils.HashCodeUtil;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="stock")
public class StockAcumulado {
	private Long id;
	private String conceptoDescripcion;
	private Date fecha;
	private String hora;  //Formato (HH:MM)
	private String tipoMovimiento; //Combo ('Ingreso', 'Egreso')
	private String nota;
	private Integer cantidad;
	private String depositoDescripcion;
	private String origenDestinoDescripcion;
	private Long clienteAspId;
	private Integer acumulado;
	
	
	public StockAcumulado(){
		super();		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
		

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
	public String getFechaStr() {
		if(fecha==null)
			return "";
		return Configuracion.formatoFechaFormularios.format(fecha);
	}

	
	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

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

	public String getConceptoDescripcion() {
		return conceptoDescripcion;
	}

	public void setConceptoDescripcion(String conceptoDescripcion) {
		this.conceptoDescripcion = conceptoDescripcion;
	}

	public String getDepositoDescripcion() {
		return depositoDescripcion;
	}

	public void setDepositoDescripcion(String depositoDescripcion) {
		this.depositoDescripcion = depositoDescripcion;
	}

	public String getOrigenDestinoDescripcion() {
		return origenDestinoDescripcion;
	}

	public void setOrigenDestinoDescripcion(String origenDestinoDescripcion) {
		this.origenDestinoDescripcion = origenDestinoDescripcion;
	}

	public Long getClienteAspId() {
		return clienteAspId;
	}

	public void setClienteAspId(Long clienteAspId) {
		this.clienteAspId = clienteAspId;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StockAcumulado tipo = (StockAcumulado) o;		
		if (getId() != null ? !getId().equals(tipo.getId()) : tipo.getId() != null)		 
			return false;
		return true;
	}	
	
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.id);
		return result;
	}
	

	public Integer getAcumulado() {
		return acumulado;
	}
	public void setAcumulado(Integer acumulado) {
		this.acumulado = acumulado;
	}
	
}
