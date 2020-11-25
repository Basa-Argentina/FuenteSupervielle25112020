/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.modelo.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Serie;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="tipos_operacion")
public class TipoOperacion implements Comparable<TipoOperacion>{
	private Long id;
	private String codigo;
	private String descripcion;
	private TipoRequerimiento tipoRequerimiento;
	private Boolean desagregaPorDeposito;
	private Boolean generaOperacionAlCerrarse;
	private TipoOperacion tipoOperacionSiguiente;
	private Boolean imprimeDocumento;
	private Boolean imprimeRemito;
	private Boolean generaMovimiento;
	private String tituloDocumento;
	private Serie serieDocumento;
	private Serie serieRemito;
	private ConceptoFacturable conceptoFacturable;
	private ClienteAsp clienteAsp;
	private Date fechaRegistro;
	private Date fechaActualizacion;
	private String modifico;
	private Boolean envio;
	private transient String accion;
	private transient String tipoRequerimientoCod;
	private transient String conceptoFacturableCod;
	private transient String tipoOperacionSiguienteCod;
	private transient String serieDocumentoCod;
	private transient String serieRemitoCod;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public TipoRequerimiento getTipoRequerimiento() {
		return tipoRequerimiento;
	}
	public void setTipoRequerimiento(TipoRequerimiento tipoRequerimiento) {
		this.tipoRequerimiento = tipoRequerimiento;
	}
	public Boolean getDesagregaPorDeposito() {
		if(desagregaPorDeposito == null)
			return false;
		return desagregaPorDeposito;
	}
	public void setDesagregaPorDeposito(Boolean desagregaPorDeposito) {
		this.desagregaPorDeposito = desagregaPorDeposito;
	}
	public Boolean getGeneraOperacionAlCerrarse() {
		if(generaOperacionAlCerrarse == null)
			return false;
		return generaOperacionAlCerrarse;
	}
	public void setGeneraOperacionAlCerrarse(Boolean generaOperacionAlCerrarse) {
		this.generaOperacionAlCerrarse = generaOperacionAlCerrarse;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public TipoOperacion getTipoOperacionSiguiente() {
		return tipoOperacionSiguiente;
	}
	public void setTipoOperacionSiguiente(TipoOperacion tipoOperacionSiguiente) {
		this.tipoOperacionSiguiente = tipoOperacionSiguiente;
	}
	public Boolean getImprimeDocumento() {
		if(imprimeDocumento == null)
			return false;
		return imprimeDocumento;
	}
	public void setImprimeDocumento(Boolean imprimeDocumento) {
		this.imprimeDocumento = imprimeDocumento;
	}
	public Boolean getImprimeRemito() {
		if(imprimeRemito == null)
			return false;
		return imprimeRemito;
	}
	public void setImprimeRemito(Boolean imprimeRemito) {
		this.imprimeRemito = imprimeRemito;
	}
	public Boolean getGeneraMovimiento() {
		if(generaMovimiento == null)
			return false;
		return generaMovimiento;
	}
	public void setGeneraMovimiento(Boolean generaMovimiento) {
		this.generaMovimiento = generaMovimiento;
	}
	public String getTituloDocumento() {
		return tituloDocumento;
	}
	public void setTituloDocumento(String tituloDocumento) {
		this.tituloDocumento = tituloDocumento;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Serie getSerieDocumento() {
		return serieDocumento;
	}
	public void setSerieDocumento(Serie serieDocumento) {
		this.serieDocumento = serieDocumento;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Serie getSerieRemito() {
		return serieRemito;
	}
	public void setSerieRemito(Serie serieRemito) {
		this.serieRemito = serieRemito;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ConceptoFacturable getConceptoFacturable() {
		return conceptoFacturable;
	}
	public void setConceptoFacturable(ConceptoFacturable conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
	}	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	@Transient
	public String getFechaRegistroStr() {
		if(fechaRegistro==null)
			return "";
		return formatoFechaFormularios.format(fechaRegistro);
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	@Transient
	public String getFechaActualizacionStr() {
		if(fechaActualizacion==null)
			return "";
		return formatoFechaFormularios.format(fechaActualizacion);
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}	
	public String getModifico() {
		return modifico;
	}
	public void setModifico(String modifico) {
		this.modifico = modifico;
	}
	@Transient
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	@Transient
	public String getTipoRequerimientoCod() {
		return tipoRequerimientoCod;
	}
	public void setTipoRequerimientoCod(String tipoRequerimientoCod) {
		this.tipoRequerimientoCod = tipoRequerimientoCod;
	}
	@Transient
	public String getConceptoFacturableCod() {
		return conceptoFacturableCod;
	}
	public void setConceptoFacturableCod(String conceptoFacturableCod) {
		this.conceptoFacturableCod = conceptoFacturableCod;
	}
	@Transient
	public String getTipoOperacionSiguienteCod() {
		return tipoOperacionSiguienteCod;
	}
	public void setTipoOperacionSiguienteCod(String tipoOperacionSiguienteCod) {
		this.tipoOperacionSiguienteCod = tipoOperacionSiguienteCod;
	}
	@Transient
	public String getSerieDocumentoCod() {
		return serieDocumentoCod;
	}
	public void setSerieDocumentoCod(String serieDocumentoCod) {
		this.serieDocumentoCod = serieDocumentoCod;
	}
	@Transient
	public String getSerieRemitoCod() {
		return serieRemitoCod;
	}
	public void setSerieRemitoCod(String serieRemitoCod) {
		this.serieRemitoCod = serieRemitoCod;
	}
	@Override
	public int compareTo(TipoOperacion o) {
		int cmp = this.getCodigo().compareTo(o.getCodigo());
		if(cmp != 0) return cmp;
		
		cmp = this.getDescripcion().compareTo(o.getDescripcion());
		if(cmp != 0) return cmp;
		
		cmp = this.getId().compareTo(o.getId());
		return cmp;
	}
	@Column(name="envio")
	public Boolean getEnvio() {
		return envio;
	}
	public void setEnvio(Boolean envio) {
		this.envio = envio;
	}	
}
