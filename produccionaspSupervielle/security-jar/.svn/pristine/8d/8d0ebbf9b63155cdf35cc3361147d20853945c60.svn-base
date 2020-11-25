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
import javax.persistence.OneToOne;
import javax.persistence.Transient;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="posiciones")
public class Posicion implements Comparable{
	private Long id;
	private Estante estante;
	private String codigo;//Formado por Deposito(2)-Seccion(2)-Estante(3)-(posVertical - posHorizontal)(6) - Digito Verificador(1)
	private Integer posHorizontal;
	private Integer posVertical;
	private String estado; //LIBRE, OCUPADA
	private Modulo modulo;
	private transient String accion;
	private transient String codigoSucursal;
	private transient String codigoEmpresa;
	private transient String codigoDeposito;
	private transient String codigoSeccion;
	private transient String codigoDesde;
	private transient String codigoHasta;
	private transient String codigoDesdeEstante;
	private transient String codigoHastaEstante;
	private transient String codigoDesdeModulo;
	private transient String codigoHastaModulo;
	private transient Integer posDesdeHorModulo;
	private transient Integer posHastaHorModulo;
	private transient Integer posDesdeVertModulo;
	private transient Integer posHastaVertModulo;
	private transient Integer posDesdeHor;
	private transient Integer posHastaHor;
	private transient Integer posDesdeVert;
	private transient Integer posHastaVert;
	private transient Boolean selected = false;
	private transient String selectedSel;
	private transient Boolean disabled = false;
	private transient Elemento elementoAsignado;
	private transient String valoracionJerarquia;
	private transient Integer valoracionNumericaJerarquia;
	private transient String codigoEstante;
	private transient String descripDeposito;
	private transient String descripSeccion;
	private transient String codigoTipoJerarquia;
	private transient String codigoJerarquia;
	
	private transient Integer numeroPagina;
	private transient Integer tamañoPagina;
	private transient String fieldOrder;
	private transient String sortOrder;
	
	public Posicion() {
		super();
		this.estante = new Estante();
		this.modulo = new Modulo();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(columnDefinition = "VARCHAR(14)")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Estante getEstante() {
		return estante;
	}
	public void setEstante(Estante estante) {
		this.estante = estante;
	}	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}
	@Transient
	public String getCodigoDesde() {
		return codigoDesde;
	}
	@Transient
	public void setCodigoDesde(String codigoDesde) {
		this.codigoDesde = codigoDesde;
	}
	@Transient
	public String getCodigoHasta() {
		return codigoHasta;
	}
	@Transient
	public void setCodigoHasta(String codigoHasta) {
		this.codigoHasta = codigoHasta;
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
	public String getCodigoDesdeModulo() {
		return codigoDesdeModulo;
	}
	@Transient
	public void setCodigoDesdeModulo(String codigoDesdeModulo) {
		this.codigoDesdeModulo = codigoDesdeModulo;
	}
	@Transient
	public String getCodigoHastaModulo() {
		return codigoHastaModulo;
	}
	@Transient
	public void setCodigoHastaModulo(String codigoHastaModulo) {
		this.codigoHastaModulo = codigoHastaModulo;
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
	public Integer getPosDesdeHor() {
		return posDesdeHor;
	}
	@Transient
	public void setPosDesdeHor(Integer posDesdeHor) {
		this.posDesdeHor = posDesdeHor;
	}
	@Transient
	public Integer getPosHastaHor() {
		return posHastaHor;
	}
	@Transient
	public void setPosHastaHor(Integer posHastaHor) {
		this.posHastaHor = posHastaHor;
	}
	@Transient
	public Integer getPosDesdeVert() {
		return posDesdeVert;
	}
	@Transient
	public void setPosDesdeVert(Integer posDesdeVert) {
		this.posDesdeVert = posDesdeVert;
	}
	@Transient
	public Integer getPosHastaVert() {
		return posHastaVert;
	}
	@Transient
	public void setPosHastaVert(Integer posHastaVert) {
		this.posHastaVert = posHastaVert;
	}
	@Transient
	public Boolean getSelected() {
		return selected;
	}
	@Transient
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	@Transient
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	@Transient
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	@Transient
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	@Transient
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	@Transient
	public String getSelectedSel() {
		return selectedSel;
	}
	@Transient
	public void setSelectedSel(String selectedSel) {
		this.selectedSel = selectedSel;
	}
	@Transient
	public Boolean getDisabled() {
		return disabled;
	}
	@Transient
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	@Transient
	public Elemento getElementoAsignado() {
		return elementoAsignado;
	}
	@Transient
	public String getTipoYElementoAsignado()
	{
		if(elementoAsignado==null)
			return"";
		return elementoAsignado.getTipoElemento().getDescripcion() +" "+elementoAsignado.getCodigo(); 
	}
	
	@Transient
	public void setElementoAsignado(Elemento elementoAsignado) {
		this.elementoAsignado = elementoAsignado;
	}
	@Transient
	public String getValoracionJerarquia() {
		return valoracionJerarquia;
	}
	@Transient
	public void setValoracionJerarquia(String valoracionJerarquia) {
		this.valoracionJerarquia = valoracionJerarquia;
	}
	@Transient
	public Integer getValoracionNumericaJerarquia() {
		return valoracionNumericaJerarquia;
	}
	@Transient
	public void setValoracionNumericaJerarquia(Integer valoracionNumericaJerarquia) {
		this.valoracionNumericaJerarquia = valoracionNumericaJerarquia;
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
	public String getDescripDeposito() {
		return descripDeposito;
	}
	@Transient
	public void setDescripDeposito(String descripDeposito) {
		this.descripDeposito = descripDeposito;
	}
	@Transient
	public String getDescripSeccion() {
		return descripSeccion;
	}
	@Transient
	public void setDescripSeccion(String descripSeccion) {
		this.descripSeccion = descripSeccion;
	}
	@Transient
	public String getPosicionStr(){
		String salida = "";
		if(posVertical!=null && posHorizontal!=null)
			salida += "(" + posVertical + ";" +posHorizontal + ")";
		return salida;
	}
	@Transient
	public String getEstanteYPosicionStr(){
		String salida = "";
		if(posVertical!=null && posHorizontal!=null)
			salida += "E:"+estante.getCodigo()+" (" + posVertical + ";" +posHorizontal + ")";
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
	@Transient
	public String getCodigoTipoJerarquia() {
		return codigoTipoJerarquia;
	}
	@Transient
	public void setCodigoTipoJerarquia(String codigoTipoJerarquia) {
		this.codigoTipoJerarquia = codigoTipoJerarquia;
	}
	@Transient
	public String getCodigoJerarquia() {
		return codigoJerarquia;
	}
	@Transient
	public void setCodigoJerarquia(String codigoJerarquia) {
		this.codigoJerarquia = codigoJerarquia;
	}

	@Override
	public int compareTo(Object o) {
		Posicion posicion = (Posicion) o;

		if (this.getEstante().getGrupo().getSeccion().getDeposito().getDescripcion().compareTo(
						posicion.getEstante().getGrupo().getSeccion().getDeposito().getDescripcion()) == 0) 
		{
			if (this.getEstante().getGrupo().getSeccion().getDescripcion().compareTo(
							posicion.getEstante().getGrupo().getSeccion().getDescripcion()) == 0) 
			{
				if (this.getEstante().getCodigo().compareTo(
						posicion.getEstante().getCodigo()) == 0) 
				{
					if (this.getValoracionNumericaJerarquia().compareTo(
							posicion.getValoracionNumericaJerarquia()) == 0) 
					{
						if (this.getCodigo().compareTo(
								posicion.getCodigo()) == 0) 
						{
							return this.getId().compareTo(
									posicion.getId());
						} 
						else 
						{
							return this.getCodigo().compareTo(
									posicion.getCodigo());
						}
					} 
					else 
					{
						return posicion.getValoracionNumericaJerarquia().compareTo(
								this.getValoracionNumericaJerarquia());
					}
				} 
				else 
				{
					return this.getEstante().getCodigo()
							.compareTo(posicion.getEstante().getCodigo());
				}
			} 
			else 
			{
				return this.getEstante().getGrupo().getSeccion().getDescripcion().compareTo(
								posicion.getEstante().getGrupo().getSeccion().getDescripcion());
			}
		} 
		else 
		{
			return this.getEstante().getGrupo().getSeccion().getDeposito().getDescripcion().compareTo(
							posicion.getEstante().getGrupo().getSeccion().getDeposito().getDescripcion());
		}

	}
		
}
