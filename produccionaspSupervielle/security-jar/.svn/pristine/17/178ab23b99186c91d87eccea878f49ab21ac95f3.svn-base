/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.utils.HashCodeUtil;

/**
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (25/08/2011)
 *
 */
@Entity
@Table(name="tipoElementos")
public class TipoElemento {
	private Long id;
	private String codigo;
	private String descripcion;
	private Boolean contenedor;
	private Boolean contenido;
	private Boolean posicionable;
	private ClienteAsp clienteAsp;
	private Boolean generaConceptoVenta;
	private ConceptoFacturable conceptoVenta;
	private Boolean generaConceptoGuarda;
	private ConceptoFacturable conceptoGuarda;
	private Boolean descuentaStock;
	private Boolean precintoSeguridad;
	private Boolean seleccionaTipoTrabajo;
	private ConceptoFacturable conceptoStock;
	private String prefijoCodigo;
	private String tipoEtiqueta;
	private transient String accion;
	private transient String conceptoCodigoVenta;
	private transient String conceptoCodigoGuarda;
	private transient String conceptoCodigoStock;
	private transient Integer tipoEtiquetaInt;
	
	public TipoElemento(){
		super();
		this.contenedor = false;
		this.contenido = false;
		this.posicionable = false;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length=3)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(length=30)
	public String getDescripcion() {
		return descripcion;
	}

	@Transient
	public String getDescripcionStr() {
		if(descripcion==null)
			return "";
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getContenedor() {
		return contenedor;
	}

	public void setContenedor(Boolean contenedor) {
		this.contenedor = contenedor;
	}

	public Boolean getContenido() {
		return contenido;
	}

	public void setContenido(Boolean contenido) {
		this.contenido = contenido;
	}

	public Boolean getPosicionable() {
		return posicionable;
	}

	public void setPosicionable(Boolean posicionable) {
		this.posicionable = posicionable;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}

	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}

	@Override
	/**
	 * genera el codigo hash por id de tipo de elemento
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	/**
	 * Compara por id de tipo de elemento
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TipoElemento)) {
			return false;
		}
		TipoElemento other = (TipoElemento) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Boolean getGeneraConceptoVenta() {
		return generaConceptoVenta;
	}

	public void setGeneraConceptoVenta(Boolean generaConceptoVenta) {
		this.generaConceptoVenta = generaConceptoVenta;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public ConceptoFacturable getConceptoVenta() {
		return conceptoVenta;
	}

	public void setConceptoVenta(ConceptoFacturable conceptoVenta) {
		this.conceptoVenta = conceptoVenta;
	}

	public Boolean getGeneraConceptoGuarda() {
		return generaConceptoGuarda;
	}

	public void setGeneraConceptoGuarda(Boolean generaConceptoGuarda) {
		this.generaConceptoGuarda = generaConceptoGuarda;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public ConceptoFacturable getConceptoGuarda() {
		return conceptoGuarda;
	}

	public void setConceptoGuarda(ConceptoFacturable conceptoGuarda) {
		this.conceptoGuarda = conceptoGuarda;
	}

	public Boolean getDescuentaStock() {
		return descuentaStock;
	}

	public void setDescuentaStock(Boolean descuentaStock) {
		this.descuentaStock = descuentaStock;
	}
	public Boolean getPrecintoSeguridad() {
		return precintoSeguridad;
	}

	public void setPrecintoSeguridad(Boolean precintoSeguridad) {
		this.precintoSeguridad = precintoSeguridad;
	}

	public Boolean getSeleccionaTipoTrabajo() {
		return seleccionaTipoTrabajo;
	}

	public void setSeleccionaTipoTrabajo(Boolean seleccionaTipoTrabajo) {
		this.seleccionaTipoTrabajo = seleccionaTipoTrabajo;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public ConceptoFacturable getConceptoStock() {
		return conceptoStock;
	}

	public void setConceptoStock(ConceptoFacturable conceptoStock) {
		this.conceptoStock = conceptoStock;
	}
	
	@Column(columnDefinition = "VARCHAR(2)")
	public String getPrefijoCodigo() {
		return prefijoCodigo;
	}

	public void setPrefijoCodigo(String prefijoCodigo) {
		this.prefijoCodigo = prefijoCodigo;
	}
	
	public String getTipoEtiqueta() {
		return tipoEtiqueta;
	}

	public void setTipoEtiqueta(String tipoEtiqueta) {
		this.tipoEtiqueta = tipoEtiqueta;
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
	public String getConceptoCodigoVenta() {
		return conceptoCodigoVenta;
	}
	@Transient
	public void setConceptoCodigoVenta(String conceptoCodigoVenta) {
		this.conceptoCodigoVenta = conceptoCodigoVenta;
	}
	@Transient
	public String getConceptoCodigoGuarda() {
		return conceptoCodigoGuarda;
	}
	@Transient
	public void setConceptoCodigoGuarda(String conceptoCodigoGuarda) {
		this.conceptoCodigoGuarda = conceptoCodigoGuarda;
	}
	@Transient
	public String getConceptoCodigoStock() {
		return conceptoCodigoStock;
	}
	@Transient
	public void setConceptoCodigoStock(String conceptoCodigoStock) {
		this.conceptoCodigoStock = conceptoCodigoStock;
	}
	@Transient
	public Integer getTipoEtiquetaInt() {
		return tipoEtiquetaInt;
	}
	@Transient
	public void setTipoEtiquetaInt(Integer tipoEtiquetaInt) {
		this.tipoEtiquetaInt = tipoEtiquetaInt;
	}
	
}
