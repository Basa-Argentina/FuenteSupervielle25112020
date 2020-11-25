package com.dividato.configuracionGeneral.objectForms;

/**
 * 
 * @author Emiliano
 *
 */
public class DatosParaExportacionForm {
	private Long idClienteAsp;
	private String codigoEmpresa;
	private String codigoSucursal;
	private String codigoCliente;
	private String filtrarPor;
	private Boolean enviarMail;
	private Boolean enviarConCopia;
	private Integer codigoClasificacionDocumental;
	private String codigoPersonal;
	private String[] elementosSeleccionadosDer;
	
	public Long getIdClienteAsp() {
		return idClienteAsp;
	}
	public void setIdClienteAsp(Long idClienteAsp) {
		this.idClienteAsp = idClienteAsp;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public Integer getCodigoClasificacionDocumental() {
		return codigoClasificacionDocumental;
	}
	public void setCodigoClasificacionDocumental(
			Integer codigoClasificacionDocumental) {
		this.codigoClasificacionDocumental = codigoClasificacionDocumental;
	}
	public String getCodigoPersonal() {
		return codigoPersonal;
	}
	public void setCodigoPersonal(String codigoPersonal) {
		this.codigoPersonal = codigoPersonal;
	}
	public String[] getElementosSeleccionadosDer() {
		return elementosSeleccionadosDer;
	}
	public void setElementosSeleccionadosDer(String[] elementosSeleccionadosDer) {
		this.elementosSeleccionadosDer = elementosSeleccionadosDer;
	}
	public String getFiltrarPor() {
		return filtrarPor;
	}
	public void setFiltrarPor(String filtrarPor) {
		this.filtrarPor = filtrarPor;
	}
	public Boolean getEnviarMail() {
		return enviarMail;
	}
	public void setEnviarMail(Boolean enviarMail) {
		this.enviarMail = enviarMail;
	}
	public Boolean getEnviarConCopia() {
		return enviarConCopia;
	}
	public void setEnviarConCopia(Boolean enviarConCopia) {
		this.enviarMail = enviarConCopia;
	}
}