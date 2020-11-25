package com.security.modelo.jerarquias;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Elemento;

@Entity(name="x_operacion_elemento")
public class OperacionElemento implements Comparable<OperacionElemento>{
	private Long id;
	private Operacion operacion;
	private Elemento elemento;
	private String estado;
	private Elemento rearchivoDe;
	private String pathArchivoDigital;
	private boolean traspasado;
	private boolean facturado;
	private boolean provieneLectura;
	private transient Long idOperacion;
	private transient boolean buscarElemento;
	private transient boolean buscarElementoReferencia;
	private transient boolean lectura;
	private transient boolean finalizarOK;
	private transient boolean finalizarError;
	private transient boolean traspasar;
	private transient boolean procesando;
	private transient String accionGuardar;
	private transient String tipoCalculo;
	private transient Long cantidadTipoCalculo;
	private transient ConceptoFacturable conceptoVenta;
	private transient ConceptoFacturable conceptoStock;
	private transient String observaciones;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Operacion getOperacion() {
		return operacion;
	}
	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Elemento getElemento() {
		return elemento;
	}
	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
	@Column(length=60)
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Elemento getRearchivoDe() {
		return rearchivoDe;
	}
	public void setRearchivoDe(Elemento rearchivoDe) {
		this.rearchivoDe = rearchivoDe;
	}
	public boolean isTraspasado() {
		return traspasado;
	}
	public void setTraspasado(boolean traspasado) {
		this.traspasado = traspasado;
	}
	public boolean isFacturado() {
		return facturado;
	}
	public void setFacturado(boolean facturado) {
		this.facturado = facturado;
	}
	public boolean isProvieneLectura() {
		return provieneLectura;
	}
	public void setProvieneLectura(boolean provieneLectura) {
		this.provieneLectura = provieneLectura;
	}
	public String getPathArchivoDigital() {
		return pathArchivoDigital;
	}
	public void setPathArchivoDigital(String pathArchivoDigital) {
		this.pathArchivoDigital = pathArchivoDigital;
	}
	@Transient
	public Long getIdOperacion() {
		return idOperacion;
	}
	@Transient
	public void setIdOperacion(Long idOperacion) {
		this.idOperacion = idOperacion;
	}
	@Transient
	public boolean isBuscarElemento() {
		return buscarElemento;
	}
	@Transient
	public void setBuscarElemento(boolean buscarElemento) {
		this.buscarElemento = buscarElemento;
	}
	@Transient
	public boolean isBuscarElementoReferencia() {
		return buscarElementoReferencia;
	}
	@Transient
	public void setBuscarElementoReferencia(boolean buscarElementoReferencia) {
		this.buscarElementoReferencia = buscarElementoReferencia;
	}
	@Transient
	public boolean isLectura() {
		return lectura;
	}
	@Transient
	public void setLectura(boolean lectura) {
		this.lectura = lectura;
	}
	@Transient
	public boolean isFinalizarOK() {
		return finalizarOK;
	}
	@Transient
	public void setFinalizarOK(boolean finalizarOK) {
		this.finalizarOK = finalizarOK;
	}
	@Transient
	public boolean isFinalizarError() {
		return finalizarError;
	}
	@Transient
	public void setFinalizarError(boolean finalizarError) {
		this.finalizarError = finalizarError;
	}
	@Transient
	public boolean isTraspasar() {
		return traspasar;
	}
	@Transient
	public void setTraspasar(boolean traspasar) {
		this.traspasar = traspasar;
	}
	@Transient
	public boolean isProcesando() {
		return procesando;
	}
	@Transient
	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	@Transient
	public String getAccionGuardar() {
		return accionGuardar;
	}
	@Transient
	public void setAccionGuardar(String accionGuardar) {
		this.accionGuardar = accionGuardar;
	}
	@Transient
	public String getTipoCalculo() {
		return tipoCalculo;
	}
	@Transient
	public void setTipoCalculo(String tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}
	@Transient
	public Long getCantidadTipoCalculo() {
		return cantidadTipoCalculo;
	}
	@Transient
	public void setCantidadTipoCalculo(Long cantidadTipoCalculo) {
		this.cantidadTipoCalculo = cantidadTipoCalculo;
	}
	@Transient
	public ConceptoFacturable getConceptoVenta() {
		return conceptoVenta;
	}
	@Transient
	public void setConceptoVenta(ConceptoFacturable conceptoVenta) {
		this.conceptoVenta = conceptoVenta;
	}
	@Transient
	public ConceptoFacturable getConceptoStock() {
		return conceptoStock;
	}
	@Transient
	public void setConceptoStock(ConceptoFacturable conceptoStock) {
		this.conceptoStock = conceptoStock;
	}
	
	@Transient
	public String getObj_hash(){
		return super.toString();
	}
	public void setObj_hash(String hash){}
	
	@Override
	public int compareTo(OperacionElemento o) {
		int cmp = 0;
		
		if(this.elemento != null && o.elemento !=null){
			cmp = this.elemento.getTipoElemento().getDescripcion().compareTo(o.elemento.getTipoElemento().getDescripcion());
			if(cmp != 0) return cmp;
		
			cmp = this.elemento.getCodigo().compareTo(o.elemento.getCodigo());
		}
		else
			return cmp;
		
		return cmp;
	}
	@Transient
	public String getObservaciones() {
		return observaciones;
	}
	@Transient
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
}	
