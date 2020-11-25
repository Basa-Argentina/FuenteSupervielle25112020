package com.security.servicios;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.ui.WebAuthenticationDetails;
import org.springframework.security.vote.AccessDecisionVoter;

import com.security.accesoDatos.interfaz.IpBlockedService;
import com.security.accesoDatos.interfaz.ParameterService;
import com.security.modelo.seguridad.IpBlocked;
import com.security.modelo.seguridad.Parameter;
/**
 * 
 * @author Federico Muñoz
 * @Modificado Gabriel Mainero
 *
 */
public class IpAddressVoter  implements AccessDecisionVoter {
	private IpBlockedService ipBlockedService;
	private ParameterService parameterService;
	
	@Autowired
	public void setIpBlockedService(IpBlockedService ipBlockedService) {
		this.ipBlockedService = ipBlockedService;
	}
	
	@Autowired
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
		int result = ACCESS_GRANTED;
		try{
			if (authentication.getDetails() != null) {
				String remoteIpAddress = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
				Parameter parameter = parameterService.obtenerParametros();
				if(parameter==null)
					parameter = new Parameter();
				long minutosRestar = parameter.getMinutesSanctionLogin() * 60 * 1000L;
				Date actual = new Date();
				long minutos = actual.getTime();
				minutos-=minutosRestar;
				actual.setTime(minutos);
				ArrayList<IpBlocked> ipBlockeds = (ArrayList<IpBlocked>)ipBlockedService.listarIpsBlocked(actual, remoteIpAddress);
				if(ipBlockeds!= null && ipBlockeds.size()>0){//está bloqueada?
					result=ACCESS_DENIED;
				}
			}
		}catch(Exception e){}
		return result;
	}
}
