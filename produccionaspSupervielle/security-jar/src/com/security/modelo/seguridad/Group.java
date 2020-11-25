package com.security.modelo.seguridad;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.utils.HashCodeUtil;
/**
 * 
 * @author Federico Muñoz
 * @Modificado Gabriel Mainero
 * @Modificado Ezequiel Beccaria (11/05/2011)
 *
 */
@Entity(name="groups")
public class Group implements Serializable, Comparable<Group>{
	private static final long serialVersionUID = 5275289473903656631L;
	private Long id;
	private String groupName;
	private Set<Authority> authorities;
	private ClienteAsp cliente;
	private Boolean admin;
	private Boolean nombreFijo;
	private transient String accion;
	private transient String groupPrivileges;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@ManyToMany(
	        targetEntity=Authority.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE},
	        fetch=FetchType.EAGER
	    )
	@JoinTable(
        name="x_group_authority",
        joinColumns=@JoinColumn(name="group_id"),
        inverseJoinColumns=@JoinColumn(name="authority_id")
    )
	public Set<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
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
	public String getGroupPrivileges() {
		return groupPrivileges;
	}
	@Transient
	public void setGroupPrivileges(String groupPrivileges) {
		this.groupPrivileges = groupPrivileges;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getCliente() {
		return cliente;
	}
	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
	}	
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}	
	public Boolean getNombreFijo() {
		return nombreFijo;
	}
	public void setNombreFijo(Boolean nombreFijo) {
		this.nombreFijo = nombreFijo;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Group group = (Group) o;
		if (getId() != null ? !getId().equals(group.getId()) :	group.getId() != null) 
			return false;		
		if (getGroupName() != null ? !getGroupName().equals(group.getGroupName()) :	group.getGroupName() != null) 
			return false;		
		return true;
	}	
	@Override
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.id);
		result = HashCodeUtil.hash(result, this.groupName);
		return result;
	}
	@Override
	public int compareTo(Group o) {
		int cmp;
		
		if(o!=null)
			cmp = groupName.compareTo(o.groupName);
		else
			cmp = -1;
		if(cmp != 0) 
			return cmp;
		
		if(o!=null)
			if(id == null && o.id == null) cmp = 0;
			else{
				cmp = id.compareTo(o.id);
			}
		else
			cmp = -1;
		return cmp;
	}
	
}
