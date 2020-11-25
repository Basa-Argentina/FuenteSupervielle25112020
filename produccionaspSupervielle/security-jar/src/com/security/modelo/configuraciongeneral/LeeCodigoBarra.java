/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author X
 *
 */
@Entity(name="lee_codigo_barra")
public class LeeCodigoBarra implements Comparable<LeeCodigoBarra>{
	private Long id;
	private String nombre;
	private Long desde;
	private Long hasta;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@Column(name = "desde")
	public Long getDesde() {
		return desde;
	}

	public void setDesde(Long desde) {
		this.desde = desde;
	}
	@Column(name = "hasta")
	public Long getHasta() {
		return hasta;
	}

	public void setHasta(Long hasta) {
		this.hasta = hasta;
	}
	@Override
	public int compareTo(LeeCodigoBarra arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}