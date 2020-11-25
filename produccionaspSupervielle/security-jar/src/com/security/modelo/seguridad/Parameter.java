package com.security.modelo.seguridad;


/**
 * 
 * @author Gabriel Mainero
 *
 */
public class Parameter{
	private Integer passwordExpirationDays;
	private Integer failedLoginCounter;
	private Integer minutesSanctionLogin;
	private Integer enableOldPassword;
	private Integer passwordWarningDays;
	private boolean enable;
	private String hostSMTP;
	private Integer portSMTP;
	private String userSMTP;
	private String passwordSMTP;
	private Integer enableSSLSMTP;
	private Integer enableSPASMTP;
	private String eMailUserSMTP;
	private boolean enableSSL;
	private boolean enableSPA;
	
	public Parameter(){
		passwordExpirationDays = new Integer(30);
		failedLoginCounter = new Integer(3);
		minutesSanctionLogin = new Integer(60);
		enableOldPassword = new Integer(0);
		passwordWarningDays = new Integer(3);
		enable=false;
	}

	public Integer getPasswordExpirationDays() {
		return passwordExpirationDays;
	}

	public void setPasswordExpirationDays(Integer passwordExpirationDays) {
		this.passwordExpirationDays = passwordExpirationDays;
	}

	public Integer getFailedLoginCounter() {
		return failedLoginCounter;
	}

	public void setFailedLoginCounter(Integer failedLoginCounter) {
		this.failedLoginCounter = failedLoginCounter;
	}

	public Integer getMinutesSanctionLogin() {
		return minutesSanctionLogin;
	}

	public void setMinutesSanctionLogin(Integer minutesSanctionLogin) {
		this.minutesSanctionLogin = minutesSanctionLogin;
	}

	public Integer getEnableOldPassword() {
		return enableOldPassword;
	}

	public void setEnableOldPassword(Integer enableOldPassword) {
		this.enableOldPassword = enableOldPassword;
	}
	
	public Integer getPasswordWarningDays() {
		return passwordWarningDays;
	}

	public void setPasswordWarningDays(Integer passwordWarningDays) {
		this.passwordWarningDays = passwordWarningDays;
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
		if(this.enable)
			this.enableOldPassword = new Integer(1);
		else
			this.enableOldPassword = new Integer(0);
	}

	public String getHostSMTP() {
		return hostSMTP;
	}

	public void setHostSMTP(String hostSMTP) {
		this.hostSMTP = hostSMTP;
	}

	public Integer getPortSMTP() {
		return portSMTP;
	}

	public void setPortSMTP(Integer portSMTP) {
		this.portSMTP = portSMTP;
	}

	public String getUserSMTP() {
		return userSMTP;
	}

	public void setUserSMTP(String userSMTP) {
		this.userSMTP = userSMTP;
	}

	public String getPasswordSMTP() {
		return passwordSMTP;
	}

	public void setPasswordSMTP(String passwordSMTP) {
		this.passwordSMTP = passwordSMTP;
	}

	public Integer getEnableSSLSMTP() {
		return enableSSLSMTP;
	}

	public void setEnableSSLSMTP(Integer enableSSLSMTP) {
		this.enableSSLSMTP = enableSSLSMTP;
	}

	public Integer getEnableSPASMTP() {
		return enableSPASMTP;
	}

	public void setEnableSPASMTP(Integer enableSPASMTP) {
		this.enableSPASMTP = enableSPASMTP;
	}

	public boolean isEnableSSL() {
		return enableSSL;
	}

	public void setEnableSSL(boolean enableSSL) {
		this.enableSSL = enableSSL;
		if(this.enableSSL)
			this.enableSSLSMTP = new Integer(1);
		else
			this.enableSSLSMTP = new Integer(0);
	}

	public boolean isEnableSPA() {
		return enableSPA;
	}

	public void setEnableSPA(boolean enableSPA) {
		this.enableSPA = enableSPA;
		if(this.enableSPA)
			this.enableSPASMTP = new Integer(1);
		else
			this.enableSPASMTP = new Integer(0);
	}

	public String geteMailUserSMTP() {
		return eMailUserSMTP;
	}

	public void seteMailUserSMTP(String eMailUserSMTP) {
		this.eMailUserSMTP = eMailUserSMTP;
	}
	
}
