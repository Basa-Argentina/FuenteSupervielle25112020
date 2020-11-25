package com.security.modelo.seguridad;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.GrantedAuthority;

import com.security.utils.HashCodeUtil;

/**
 * 
 * @author Federico Muñoz
 * @Modificado Ezequiel Beccaria
 *
 */
@Entity(name="authorities")
public class Authority implements GrantedAuthority{
	private static final long serialVersionUID = -6041908290379996161L;
	private String authority;
	private String description;
		
	public Authority() {
		super();
	}
	public Authority(String authority) {
		super();
		this.authority = authority;
	}
	@Id
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int compareTo(Object o) {
		return this.authority.compareTo(((Authority)o).getAuthority());
	}	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Authority authority = (Authority) o;
		if (getAuthority() != null ? !getAuthority().equals(authority.getAuthority()):authority.getAuthority() != null) 
			return false;
		if (getDescription() != null ? !getDescription().equals(authority.getDescription()):authority.getDescription() != null) 
			return false;		
		return true;
	}	
	@Override
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.authority);
		result = HashCodeUtil.hash(result, this.description);
		return result;
	}
	
}
