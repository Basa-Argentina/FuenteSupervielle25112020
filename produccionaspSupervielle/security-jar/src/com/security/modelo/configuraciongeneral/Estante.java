/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 14/06/2011
 */
package com.security.modelo.configuraciongeneral;

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
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.security.modelo.jerarquias.TipoJerarquia;



/**
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (16/08/2011)
 *
 */
@Entity(name="estanterias")
public class Estante{
	private Long id;
	private Grupo grupo;
	private String codigo;
	private String observacion;
	private transient String accion;
	private transient Long idGrupo;
	private Set<Modulo> modulos;
	private Set<Posicion> posiciones;
	private TipoJerarquia tipoJerarquia;
	private transient String codigoTipoJ;
	private transient String codigoEmpresa;
	
	public Estante() {
		super();
		this.grupo = new Grupo();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	public Grupo getGrupo() {
		return grupo;
	}
	
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
	@Column(columnDefinition = "VARCHAR(4)")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="estante", fetch=FetchType.LAZY,orphanRemoval=true)
	public Set<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(Set<Modulo> modulos) {
		this.modulos = modulos;
	}
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="estante",fetch=FetchType.LAZY,orphanRemoval=true)
	public Set<Posicion> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(Set<Posicion> posiciones) {
		this.posiciones = posiciones;
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
	public Long getIdGrupo() {
		return idGrupo;
	}
	@Transient
	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
	}
	@ManyToOne
	public TipoJerarquia getTipoJerarquia() {
		return tipoJerarquia;
	}

	public void setTipoJerarquia(TipoJerarquia tipoJerarquia) {
		this.tipoJerarquia = tipoJerarquia;
	}
	@Transient
	public String getCodigoTipoJ() {
		return codigoTipoJ;
	}
	@Transient
	public void setCodigoTipoJ(String codigoTipoJ) {
		this.codigoTipoJ = codigoTipoJ;
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
