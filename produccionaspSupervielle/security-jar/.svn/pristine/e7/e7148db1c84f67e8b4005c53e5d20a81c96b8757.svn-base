/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.seguridad;


import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.Persona;
import com.security.modelo.general.PersonaFisica;
import com.security.recursos.Configuracion;
import com.security.utils.HashCodeUtil;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity
@Table(name="users")
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements UserDetails, Comparable<User>{
	private static final long serialVersionUID = 1L;	
	private Long id;
	private ClienteAsp cliente;
	private String username;
	private String password;
	private Boolean enable;
	private Set<Group> groups;
	private Persona persona;	
	private Date passwordChangeDate;
	private Date lastLogin;
	private Boolean admin;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient GrantedAuthority[] grantedAuthorities;
	private transient String confirmarContrasenia;
	private transient String accion;
	private transient String assignedRoles;	
		
	public User() {
		//un user solo debe tener asociada una persona fisica
		this.persona = new PersonaFisica(); 
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(
				cascade={CascadeType.PERSIST, CascadeType.MERGE},
				fetch=FetchType.EAGER
			)
	public ClienteAsp getCliente() {
		return cliente;
	}
	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
	}
	@Column(unique=true)
	public String getUsername() {
		return username;
	}	
	public void setUsername(String username) {
		this.username = username;
	}
	@Transient
	public String getUsernameSinCliente(){
		if(getCliente() != null)
			return username.replaceAll(Constants.SEPARADOR_CLIENTE_USUARIO+getCliente().getNombreAbreviado(), "");
		else
			return username;
	}
	@Transient
	public void setUsernameSinCliente(String username){
		if(getCliente() != null)
			this.username = username+Constants.SEPARADOR_CLIENTE_USUARIO+getCliente().getNombreAbreviado();
		else
			this.username = username;
	}
	@ManyToMany(
	        targetEntity=Group.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE},
	        fetch=FetchType.EAGER
	    )
	@JoinTable(
        name="x_user_group",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="group_id")
    )
	public Set<Group> getGroups() {
		return groups;
	}
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	@Override
	@Transient
	public GrantedAuthority[] getAuthorities() {
		return grantedAuthorities;
	}
	public void setGrantedAuthorities(GrantedAuthority[] grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}
	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return enable;
	}
	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return enable;
	}
	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return enable;
	}	
	@Override
	@Transient
	public boolean isEnabled() {
		return enable;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	public Date getPasswordChangeDate() {
		return passwordChangeDate;
	}
	public void setPasswordChangeDate(Date passwordChangeDate) {
		this.passwordChangeDate = passwordChangeDate;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}	
	@Transient
	public String getLastLoginStr(){
		if(lastLogin==null)
			return "";
		return Configuracion.formatoFechaHoraFormularios.format(lastLogin);
	}
	@Transient
	public String getConfirmarContrasenia() {
		return confirmarContrasenia;
	}
	@Transient
	public void setConfirmarContrasenia(String confirmarContrasenia) {
		this.confirmarContrasenia = confirmarContrasenia;
	}
	@Transient
	public String getAccion() {
		return accion;
	}
	@Transient
	public void setAccion(String accion) {
		this.accion = accion;
	}	
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}	
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	@Transient
	public String getAssignedRoles() {
		return assignedRoles;
	}
	@Transient
	public void setAssignedRoles(String assignedRoles) {
		this.assignedRoles = assignedRoles;
	}
	@Override
	public String toString(){
		return username;
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
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	@Transient
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}	
	public Set<Authority> obtenerAutorizaciones(){
		Set<Authority> coleccion = new TreeSet<Authority>();
		for(Group g:groups){
			for(Authority a:g.getAuthorities()){
				coleccion.add(a);
			}
		}
		return coleccion;
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		if (getId() != null ? !getId().equals(user.getId()) : user.getId() != null) 
			return false;
		if (getCliente() != null ? !getCliente().equals(user.getCliente()) : user.getCliente() != null) 
			return false;
		if (getUsername() != null ? !getUsername().equals(user.getUsername()) : user.getUsername() != null) 
			return false;
		if (getPersona() != null ? !getPersona().equals(user.getPersona()) : user.getPersona() != null) 
			return false;
		return true;
	}	
	
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.id);
		result = HashCodeUtil.hash(result, this.cliente);
		result = HashCodeUtil.hash(result, this.username);
		result = HashCodeUtil.hash(result, this.persona);
		return result;
	}
	@Override
	public int compareTo(User o) {
		int cmp = getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = getUsername().compareTo(o.getUsername());
		if(cmp != 0) return cmp;

		if(getPersona() != null && o.getPersona() != null){
			cmp = getUsername().compareTo(o.getUsername());
			if(cmp != 0) return cmp;
		}else if(getPersona() != null || o.getPersona() != null){
			if(getPersona() != null) return -1;
			else return 1;
		}

		if(getCliente() != null && o.getCliente() != null){
			cmp = getCliente().compareTo(o.getCliente());
			if(cmp != 0) return cmp;
		}else if(getCliente() != null || o.getCliente() != null){
			if(getCliente() != null) return -1;
			else return 1;
		}		
		return 0;
	}
}
