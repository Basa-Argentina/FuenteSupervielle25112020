package com.security.servicios;

import org.apache.log4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.security.modelo.seguridad.User;
import com.security.recursos.RecursosPassword;
/**
 * 
 * @author Federico Muñoz
 *
 */
public class CustomAuthenticationManager implements AuthenticationManager {
	private static Logger logger = Logger.getLogger(CustomAuthenticationManager.class);
	
	private UserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication auth)	throws AuthenticationException {
		try{
			String securityString = String.valueOf(auth.getPrincipal());
			securityString = RecursosPassword.decrypt(securityString);
			if(System.currentTimeMillis()-Long.parseLong(securityString.substring(0,securityString.indexOf("|")))>10000)
				throw new BadCredentialsException("security string lapsed");
			String username = securityString.substring(securityString.indexOf("|")+1);
			User user = (User) userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, "",user.getAuthorities());
		}catch(BadCredentialsException e){
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new BadCredentialsException("bad security string");
		}
	}
	
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

}
