package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.security.modelo.jerarquias.ConceptoOperacionCliente;
/**
 * 
 * @author Victor Kenis
 *
 */
@Entity(name="loteFacturacionDetalle")
public class LoteFacturacionDetalle {
	private Long id;//codigo
	private LoteFacturacion loteFacturacion;
	private ConceptoOperacionCliente conceptoOperacionCliente;
		
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public LoteFacturacion getLoteFacturacion() {
		return loteFacturacion;
	}
	public void setLoteFacturacion(LoteFacturacion loteFacturacion) {
		this.loteFacturacion = loteFacturacion;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ConceptoOperacionCliente getConceptoOperacionCliente() {
		return conceptoOperacionCliente;
	}
	public void setConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionCliente) {
		this.conceptoOperacionCliente = conceptoOperacionCliente;
	}
	
	
}
