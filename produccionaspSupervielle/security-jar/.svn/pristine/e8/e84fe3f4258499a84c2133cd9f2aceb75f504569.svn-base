package com.security.modelo.configuraciongeneral;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity(name="plantillasFacturacionDetalle")
public class PlantillaFacturacionDetalle {
	private Long id;
	private PlantillaFacturacion plantillaFacturacion;
	private Long orden;
	private String codigoConcepto;
	private ConceptoFacturable conceptoFacturable;
	private String descripcionConcepto;
	private Long cantidadSinCosto;
	
	private transient String accionDetalle;
	private transient String accion;
	private transient Long ordenAnterior;
	private transient Long idPlantilla;
	private transient List<PlantillaFacturacionDetalle> detalles; 
	
	public PlantillaFacturacionDetalle() {
		super();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public PlantillaFacturacion getPlantillaFacturacion() {
		return plantillaFacturacion;
	}

	public void setPlantillaFacturacion(PlantillaFacturacion plantillaFacturacion) {
		this.plantillaFacturacion = plantillaFacturacion;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public String getCodigoConcepto() {
		return codigoConcepto;
	}

	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public ConceptoFacturable getConceptoFacturable() {
		return conceptoFacturable;
	}

	public void setConceptoFacturable(ConceptoFacturable conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
	}

	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}

	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}

	public Long getCantidadSinCosto() {
		return cantidadSinCosto;
	}

	public void setCantidadSinCosto(Long cantidadSinCosto) {
		this.cantidadSinCosto = cantidadSinCosto;
	}
	
	
	@Transient
	public String getAccionDetalle() {
		return accionDetalle;
	}
	@Transient
	public void setAccionDetalle(String accionDetalle) {
		this.accionDetalle = accionDetalle;
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
	public Long getIdPlantilla() {
		return idPlantilla;
	}
	@Transient
	public Long getOrdenAnterior() {
		return ordenAnterior;
	}
	@Transient
	public void setOrdenAnterior(Long ordenAnterior) {
		this.ordenAnterior = ordenAnterior;
	}
	@Transient
	public void setIdPlantilla(Long idPlantilla) {
		this.idPlantilla = idPlantilla;
	}
	@Transient
	public List<PlantillaFacturacionDetalle> getDetalles() {
		return detalles;
	}
	@Transient
	public void setDetalles(List<PlantillaFacturacionDetalle> detalles) {
		this.detalles = detalles;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj == null) {
//			return false;
//		}
//		if (!(obj instanceof PlantillaFacturacionDetalle)) {
//			return false;
//		}
//		PlantillaFacturacionDetalle other = (PlantillaFacturacionDetalle) obj;
//		if (id == null) {
//			if (other.id != null) {
//				return false;
//			}
//		} else if (!id.equals(other.id)) {
//			return false;
//		}
//		return true;
//	}
	

}