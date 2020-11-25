/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 14/06/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.utils.EAN13;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="modulos")
public class Modulo{
	private Long id;
	private Estante estante;
	private String codigo;
	private Integer offsetHorizontal;
	private Integer offsetVertical;
	private Integer posHorizontal;//Se agrega este campo para identificar las coordenadas de los estantes
	private Integer posVertical;//Se agrega este campo para identificar las coordenadas de los estantes
	private String codigoBarra;//Formado por Deposito(2)-Seccion(2)-Estante(3)-(posVertical - posHorizontal)(6) - Digito Verificador(1)
	private transient String accion;
	private transient Long idEstante;
	private transient String codigoEstante;
	private transient String codigoDesdeEstante;
	private transient String codigoHastaEstante;
	private transient String codigoGrupo;
	private transient String codigoSeccion;
	private transient String codigoDeposito;
	private transient Integer posDesdeHorModulo;
	private transient Integer posHastaHorModulo;
	private transient Integer posDesdeVertModulo;
	private transient Integer posHastaVertModulo;	
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
		
	public Modulo() {
		super();
		this.estante = new Estante();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(columnDefinition = "VARCHAR(3)")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Estante getEstante() {
		return estante;
	}
	public void setEstante(Estante estante) {
		this.estante = estante;
	}
	
	public Integer getOffsetHorizontal() {
		return offsetHorizontal;
	}

	public void setOffsetHorizontal(Integer offsetHorizontal) {
		this.offsetHorizontal = offsetHorizontal;
	}

	public Integer getOffsetVertical() {
		return offsetVertical;
	}

	public void setOffsetVertical(Integer offsetVertical) {
		this.offsetVertical = offsetVertical;
	}

	public Integer getPosHorizontal() {
		return posHorizontal;
	}

	public void setPosHorizontal(Integer posHorizontal) {
		this.posHorizontal = posHorizontal;
	}

	public Integer getPosVertical() {
		return posVertical;
	}

	public void setPosVertical(Integer posVertical) {
		this.posVertical = posVertical;
	}

	@Column(length=14)
	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
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
	public Long getIdEstante() {
		return idEstante;
	}
	@Transient
	public String getCodigoEstante() {
		return codigoEstante;
	}
	@Transient
	public void setCodigoEstante(String codigoEstante) {
		this.codigoEstante = codigoEstante;
	}

	@Transient
	public void setIdEstante(Long idEstante) {
		this.idEstante = idEstante;
	}
	@Transient
	public String getCodigoDeposito() {
		return codigoDeposito;
	}
	@Transient
	public void setCodigoDeposito(String codigoDeposito) {
		this.codigoDeposito = codigoDeposito;
	}
	@Transient
	public String getCodigoSeccion() {
		return codigoSeccion;
	}
	@Transient
	public String getCodigoGrupo() {
		return codigoGrupo;
	}
	@Transient
	public void setCodigoGrupo(String codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}

	@Transient
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}
	@Transient
	public Integer getPosDesdeHorModulo() {
		return posDesdeHorModulo;
	}
	@Transient
	public void setPosDesdeHorModulo(Integer posDesdeHorModulo) {
		this.posDesdeHorModulo = posDesdeHorModulo;
	}
	@Transient
	public Integer getPosHastaHorModulo() {
		return posHastaHorModulo;
	}
	@Transient
	public void setPosHastaHorModulo(Integer posHastaHorModulo) {
		this.posHastaHorModulo = posHastaHorModulo;
	}
	@Transient
	public Integer getPosDesdeVertModulo() {
		return posDesdeVertModulo;
	}
	@Transient
	public void setPosDesdeVertModulo(Integer posDesdeVertModulo) {
		this.posDesdeVertModulo = posDesdeVertModulo;
	}
	@Transient
	public Integer getPosHastaVertModulo() {
		return posHastaVertModulo;
	}
	@Transient
	public void setPosHastaVertModulo(Integer posHastaVertModulo) {
		this.posHastaVertModulo = posHastaVertModulo;
	}
	@Transient
	public String getCodigoDesdeEstante() {
		return codigoDesdeEstante;
	}
	@Transient
	public void setCodigoDesdeEstante(String codigoDesdeEstante) {
		this.codigoDesdeEstante = codigoDesdeEstante;
	}
	@Transient
	public String getCodigoHastaEstante() {
		return codigoHastaEstante;
	}
	@Transient
	public void setCodigoHastaEstante(String codigoHastaEstante) {
		this.codigoHastaEstante = codigoHastaEstante;
	}
	@Transient
	public String getDigitoControlCodigoEAN13(){
		return String.valueOf(EAN13.EAN13_CHECKSUM(codigo));
	}
	@Transient
	public String getModuloPosicionStr(){
		String salida = "";
		if(codigo!=null)
			salida +=codigo + " ";
		if(posVertical!=null && posHorizontal!=null)
			salida += "(" + posVertical + ";" +posHorizontal + ")";
		return salida;
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

	@Override
	/**
	 * Usa el id del modulo para generar el codigo hash
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	/**
	 * Utiliza el id del modulo para comparar
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Modulo other = (Modulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
