package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.jerarquias.OperacionElemento;
import com.security.utils.Constantes;

@Entity(name="hojaRuta_operacionElemento")
public class HojaRutaOperacionElemento {
	
	private Long id;//codigo
	private HojaRuta hojaRuta;
	private OperacionElemento operacionElemento;
	private String estado;
	
	private transient Boolean seleccionable = true;
	
	public HojaRutaOperacionElemento(){}
	
	public HojaRutaOperacionElemento(OperacionElemento operacionElemento) {
		this.operacionElemento = operacionElemento;
		this.estado = Constantes.ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_NO_SELECCIONADO;
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public HojaRuta getHojaRuta() {
		return hojaRuta;
	}
	public void setHojaRuta(HojaRuta hojaRuta) {
		this.hojaRuta = hojaRuta;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public OperacionElemento getOperacionElemento() {
		return operacionElemento;
	}
	public void setOperacionElemento(OperacionElemento operacionElemento) {
		this.operacionElemento = operacionElemento;
	}
	
	@Column(length=60)
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Transient
	public String getObj_hash(){
		return super.toString();
	}
	public void setObj_hash(String hash){}
	
	@Transient
	public Boolean getSeleccionable() {
		return seleccionable;
	}

	public void setSeleccionable(Boolean seleccionable) {
		this.seleccionable = seleccionable;
	}
}
