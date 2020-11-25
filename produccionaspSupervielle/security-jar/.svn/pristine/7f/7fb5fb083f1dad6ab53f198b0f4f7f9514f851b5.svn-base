package com.security.modelo.facturacion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "email_clientes")
public class EmailCliente {

	@Id
	@Column(name="id_cliente")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idCliente;
	
	@Column(name="codigo_cliente", columnDefinition = "VARCHAR(50)")
	private String codigoCliente;
	
	@Column(name="nombre_cliente", columnDefinition = "VARCHAR(150)")
	private String nombreCliente;
	
	@Column(name="emails", columnDefinition = "VARCHAR(255)")
	private String emails;
	
	@Column(name="provincia", columnDefinition = "VARCHAR(50)")
	private String provincia;

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	
	
	
}
