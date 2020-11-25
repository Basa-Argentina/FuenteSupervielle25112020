/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/07/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.security.modelo.seguridad.User;


/**
 * @author X
 *
 */
@Entity(name="cambio_etiqueta")
public class CambioEtiqueta implements Cloneable{
	private Long id;
	private String etiquetaOriginal;
	private String etiquetaNueva;
	private Date fechaModificacion;
	private User usuarioModificacion;
	private Long idLectura;
	
	public CambioEtiqueta() {
		
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	

	

	

	

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(User usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	public CambioEtiqueta clone(){
        CambioEtiqueta obj=null;
        try{
            obj=(CambioEtiqueta)super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

	@Override
	/**
	 * Genera el codigo hash por id de elemento
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	/**
	 * Compara por id de elemento
	 */
	public boolean equals(Object obj) {
		int i=1;
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CambioEtiqueta)) {
			return false;
		}
		
		CambioEtiqueta other = (CambioEtiqueta) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		
		
		
		
		return true;
		

	}
	
	private Long parseLongCodigo(String codigo){
		Long result= null;
		//si el codigo es distinto de vacio o null
		if(codigo!=null && codigo.length()>0){
			//cuenta el primer digito diferente de 0
			int cont = 0;
			while(codigo.substring( cont, cont).equals("0")){
				cont++;
			}
			//si el codigo esta formado solo por 0
			if(cont == codigo.length()-1){
				result = new Long(0);
			}else{
				//devuelve el Integer formado por el substring desde el cont hasta el final del codigo
				result = Long.parseLong(codigo.substring(cont));
			}
		}else{
			result = new Long(0);
		}
		return result;
	}

	public String getEtiquetaOriginal() {
		return etiquetaOriginal;
	}

	public void setEtiquetaOriginal(String etiquetaOriginal) {
		this.etiquetaOriginal = etiquetaOriginal;
	}

	public String getEtiquetaNueva() {
		return etiquetaNueva;
	}

	public void setEtiquetaNueva(String etiquetaNueva) {
		this.etiquetaNueva = etiquetaNueva;
	}

	public Long getIdLectura() {
		return idLectura;
	}

	public void setIdLectura(Long idLectura) {
		this.idLectura = idLectura;
	}
}
