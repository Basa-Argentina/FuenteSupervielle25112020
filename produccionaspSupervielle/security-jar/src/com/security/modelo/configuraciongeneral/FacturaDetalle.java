package com.security.modelo.configuraciongeneral;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity(name="facturaDetalles")
public class FacturaDetalle {
	private Long id;	
	private ConceptoFacturable conceptoFacturable;
	private ListaPrecios listaprecios;
	private Factura factura;
	private BigDecimal costo;
	private BigDecimal precioBase;
	private BigDecimal netoUnitario;
	private BigDecimal IVA;
	private BigDecimal impuestoUnitario;	
	private BigDecimal finalUnitario;
	private Long cantidad;
	private BigDecimal netoTotal;
	private BigDecimal impuestoTotal;
	private BigDecimal finalTotal;
	private String descripcion;
	private Integer orden;
	
	private transient String codigoConcepto;
	private transient String listaPreciosCodigo;
	private transient Long idEliminar;
	
	public FacturaDetalle(){
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
	public ConceptoFacturable getConceptoFacturable() {
		return conceptoFacturable;
	}
	public void setConceptoFacturable(ConceptoFacturable conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}) 
	public ListaPrecios getListaprecios() {
		return listaprecios;
	}
	public void setListaprecios(ListaPrecios listaprecios) {
		this.listaprecios = listaprecios;
	}

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Factura getFactura() {
		return factura;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public BigDecimal getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(BigDecimal precioBase) {
		this.precioBase = precioBase;
	}

	public BigDecimal getNetoUnitario() {
		return netoUnitario;
	}

	public void setNetoUnitario(BigDecimal netoUnitario) {
		this.netoUnitario = netoUnitario;
	}

	public BigDecimal getIVA() {
		return IVA;
	}

	public void setIVA(BigDecimal iVA) {
		IVA = iVA;
	}

	public BigDecimal getFinalUnitario() {
		return finalUnitario;
	}

	public void setFinalUnitario(BigDecimal finalUnitario) {
		this.finalUnitario = finalUnitario;
	}

	public BigDecimal getNetoTotal() {
		return netoTotal;
	}

	public void setNetoTotal(BigDecimal netoTotal) {
		this.netoTotal = netoTotal;
	}

	public BigDecimal getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(BigDecimal finalTotal) {
		this.finalTotal = finalTotal;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long long1) {
		this.cantidad = long1;
	}

	public BigDecimal getImpuestoUnitario() {
		return impuestoUnitario;
	}

	public void setImpuestoUnitario(BigDecimal impuestoUnitario) {
		this.impuestoUnitario = impuestoUnitario;
	}

	public BigDecimal getImpuestoTotal() {
		return impuestoTotal;
	}

	public void setImpuestoTotal(BigDecimal impuestoTotal) {
		this.impuestoTotal = impuestoTotal;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
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
	public String getListaPreciosCodigo() {
		return listaPreciosCodigo;
	}
	@Transient
	public void setListaPreciosCodigo(String listaPreciosCodigo) {
		this.listaPreciosCodigo = listaPreciosCodigo;
	}
	@Transient
	public Long getIdEliminar() {
		return idEliminar;
	}
	@Transient
	public void setIdEliminar(Long idEliminar) {
		this.idEliminar = idEliminar;
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
//		if (!(obj instanceof FacturaDetalle)) {
//			return false;
//		}
//		FacturaDetalle other = (FacturaDetalle) obj;
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