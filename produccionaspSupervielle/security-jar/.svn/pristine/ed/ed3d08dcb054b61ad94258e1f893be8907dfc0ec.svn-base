/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.seguridad;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
/**
 * 
 * @author Gabriel Mainero
 * @modified Ezequiel Beccaria 11/05/2011
 *
 */
@Entity(name="passwordhistory")
public class PasswordHistory {
	private Long id;
	private Date dateChange;
	private String oldPassword;
	private User user;
	private String nuevaContrasenia;
	private String confirmarContrasenia;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDateChange() {
		return dateChange;
	}
	public void setDateChange(Date dateChange) {
		this.dateChange = dateChange;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	@ManyToOne( cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Transient
	public String getNuevaContrasenia() {
		return nuevaContrasenia;
	}
	public void setNuevaContrasenia(String nuevaContrasenia) {
		this.nuevaContrasenia = nuevaContrasenia;
	}
	@Transient
	public String getConfirmarContrasenia() {
		return confirmarContrasenia;
	}
	public void setConfirmarContrasenia(String confirmarContrasenia) {
		this.confirmarContrasenia = confirmarContrasenia;
	}	
}
