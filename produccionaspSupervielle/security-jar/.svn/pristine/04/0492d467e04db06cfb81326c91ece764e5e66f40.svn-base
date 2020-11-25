/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 * @author X
 *
 */
@Entity(name="docto_cta_cte")
public class DoctoCtaCte{
	private Long id;
	private Factura nc_rc;
	private Factura fc_nd;
	private Double importe;
	private Long pagoACuenta;
	
	public DoctoCtaCte() {
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
	public Factura getNc_rc() {
		return nc_rc;
	}

	public void setNc_rc(Factura nc_rc) {
		this.nc_rc = nc_rc;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Factura getFc_nd() {
		return fc_nd;
	}

	public void setFc_nd(Factura fc_nd) {
		this.fc_nd = fc_nd;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Long getPagoACuenta() {
		return pagoACuenta;
	}

	public void setPagoACuenta(Long pagoACuenta) {
		this.pagoACuenta = pagoACuenta;
	}
	
	
}
