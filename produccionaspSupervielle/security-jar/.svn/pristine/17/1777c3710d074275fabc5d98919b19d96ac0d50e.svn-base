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

import com.security.modelo.administracion.ClienteAsp;

@Entity(name="plantillasFacturacion")
public class PlantillaFacturacion {
	private Long id;
	private ClienteAsp clienteAsp;
	private ClienteEmp clienteEmp;
	private Serie serie;
	private AfipTipoComprobante afipTipoComprobante;
	private ListaPrecios listaPrecios;
	private Boolean habilitado;
	
	private transient String accion;
	private transient String clienteCodigo;
	private transient String codigoSerie;
	private transient Long tipoComprobanteId;
	private transient String listaPreciosCodigo;
	private transient List<PlantillaFacturacionDetalle> detalles;
	
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	
	public PlantillaFacturacion() {
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
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public AfipTipoComprobante getAfipTipoComprobante() {
		return afipTipoComprobante;
	}
	public void setAfipTipoComprobante(AfipTipoComprobante afipTipoComprobante) {
		this.afipTipoComprobante = afipTipoComprobante;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ListaPrecios getListaPrecios() {
		return listaPrecios;
	}

	public void setListaPrecios(ListaPrecios listaPrecios) {
		this.listaPrecios = listaPrecios;
	}

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
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
	public String getClienteCodigo() {
		return clienteCodigo;
	}
	@Transient
	public void setClienteCodigo(String clienteCodigo) {
		this.clienteCodigo = clienteCodigo;
	}

	@Transient
	public String getCodigoSerie() {
		return codigoSerie;
	}
	@Transient
	public void setCodigoSerie(String codigoSerie) {
		this.codigoSerie = codigoSerie;
	}
	@Transient
	public Long getTipoComprobanteId() {
		return tipoComprobanteId;
	}
	@Transient
	public void setTipoComprobanteId(Long tipoComprobanteId) {
		this.tipoComprobanteId = tipoComprobanteId;
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
	public List<PlantillaFacturacionDetalle> getDetalles() {
		return detalles;
	}
	@Transient
	public void setDetalles(List<PlantillaFacturacionDetalle> detalles) {
		this.detalles = detalles;
	}
	
	@Transient
	public String getHabilitadoStr(){
		if(habilitado == null)
			return "";
		else if(habilitado == true)
			return "SI";
		else
			return "NO";
	}
	@Transient
	public Integer getNumeroPagina() {
		return numeroPagina;
	}
	@Transient
	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}
	@Transient
	public Integer getTamañoPagina() {
		return tamañoPagina;
	}
	@Transient
	public void setTamañoPagina(Integer tamañoPagina) {
		this.tamañoPagina = tamañoPagina;
	}
	@Transient
	public String getFieldOrder() {
		return fieldOrder;
	}
	@Transient
	public void setFieldOrder(String fieldOrder) {
		this.fieldOrder = fieldOrder;
	}
	@Transient
	public String getSortOrder() {
		return sortOrder;
	}
	@Transient
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
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
//		if (!(obj instanceof PlantillaFacturacion)) {
//			return false;
//		}
//		PlantillaFacturacion other = (PlantillaFacturacion) obj;
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