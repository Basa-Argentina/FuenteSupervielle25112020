package com.dividato.configuracionGeneral.objectForms;

/**
 * 
 * @author FedeMz
 *
 */
public class TransferenciaContenedorForm {
	private Long clienteId;
	private String codigoEmpresa;
	private String codigoSucursal;
	private String codigoCliente;
	private String codigoContenedorOrigen;
	private String codigoContenedorDestino;
	
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
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getCodigoContenedorOrigen() {
		return codigoContenedorOrigen;
	}
	public void setCodigoContenedorOrigen(String codigoContenedorOrigen) {
		this.codigoContenedorOrigen = codigoContenedorOrigen;
	}
	public String getCodigoContenedorDestino() {
		return codigoContenedorDestino;
	}
	public void setCodigoContenedorDestino(String codigoContenedorDestino) {
		this.codigoContenedorDestino = codigoContenedorDestino;
	}
	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
	public Long getClienteId() {
		return clienteId;
	}
}
