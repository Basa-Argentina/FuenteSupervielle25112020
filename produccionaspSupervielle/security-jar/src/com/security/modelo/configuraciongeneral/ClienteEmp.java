/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.security.modelo.general.PersonaJuridica;
import com.security.modelo.general.TipoDocumento;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity
@Table(name="clientesEmp")
public class ClienteEmp implements Comparable<ClienteEmp>{
	private Long id;
	private Empresa empresa;
	private Direccion direccion;	
	private String codigo;
	private String nombre;
	private String apellido;
	private PersonaJuridica razonSocial;
	private TipoDocumento tipoDoc;
	private String numeroDoc;
	private AfipCondIva afipCondIva;	
	private String telefono;
	private String interno;
	private String fax;
	private String email;
	//private Set<Empleado> empleados;
	private Boolean habilitado;
	private Set<ListaPrecios> listasPrecio;
	private ListaPrecios listaPreciosDefecto;
	private String tipoPersona;
	private String observaciones;
	private String mesesFacturables;
	private String notasFacturacion;
	private transient String accion;
	private transient Long idTipoDocSel;
	private transient Long idBarrio;
	private transient Long idDireccion;
	private transient Long idAfipCondIva;
	private transient Long idRazonSocial;
	private transient Long idEmpresa;
	private transient String codigoEmpresa;
	private transient String raSocial;
	
	
	public ClienteEmp() {
		super();
		this.razonSocial = new PersonaJuridica();
		this.direccion = new Direccion();
		this.empresa = new Empresa();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(columnDefinition = "VARCHAR(20)")
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	@Column(length=60)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public TipoDocumento getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(TipoDocumento tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	
	@Column(columnDefinition = "VARCHAR(15)")
	public String getNumeroDoc() {
		return numeroDoc;
	}
	public void setNumeroDoc(String numeroDoc) {
		this.numeroDoc = numeroDoc;
	}
	
	@Column(columnDefinition = "VARCHAR(6)")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getInterno() {
		return interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public AfipCondIva getAfipCondIva() {
		return afipCondIva;
	}
	public void setAfipCondIva(AfipCondIva afipCondIva) {
		this.afipCondIva = afipCondIva;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	public void setRazonSocial(PersonaJuridica razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	@OneToOne
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	public PersonaJuridica getRazonSocial() {
		return razonSocial;
	}
	
	@Transient
	public String getNombreRazonSocial(){
		if(razonSocial!=null && razonSocial.getRazonSocial()!=null)
			return razonSocial.getRazonSocial();
		else
			return "";
	}
	
	@Transient
	public String getNombreYApellido(){
		if(nombre != null && apellido != null)
			return apellido + ", "+nombre;
		return "";
	}
	
	@Transient
	public String getRazonSocialONombreYApellido(){
		if(razonSocial != null && (tipoPersona==null || tipoPersona.equals("Juridica")) )
			return razonSocial.getRazonSocial();
		else if(nombre != null && apellido != null && (tipoPersona==null || tipoPersona.equals("Fisica")) )
			return apellido + ", "+ nombre;
		return "";
	}
	
	@ManyToMany(targetEntity=ListaPrecios.class, cascade={CascadeType.PERSIST, CascadeType.MERGE},
	        fetch=FetchType.LAZY)
	@JoinTable(name="x_clienteEmp_listaPrecio", 
			joinColumns=@JoinColumn(name="clienteEmp_id", referencedColumnName="id"),
        inverseJoinColumns=@JoinColumn(name="listaPrecios_id", referencedColumnName="id"))
    public Set<ListaPrecios> getListasPrecio() {
		return listasPrecio;
	}

	public void setListasPrecio(Set<ListaPrecios> listasPrecio) {
		this.listasPrecio = listasPrecio;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public ListaPrecios getListaPreciosDefecto(){
		return listaPreciosDefecto;
	}
	
	public void setListaPreciosDefecto(ListaPrecios listaPreciosDefecto){
		this.listaPreciosDefecto=listaPreciosDefecto;
	}
	/*@OneToMany(cascade=CascadeType.ALL, mappedBy="clienteEmp", fetch=FetchType.EAGER)
	public Set<Empleado> getEmpleados() {
		return empleados;
	}
	@Transient
	public void setEmpleados(Set<Empleado> empleados) {
		this.empleados = empleados;
	}*/
	@Override
	public int compareTo(ClienteEmp o) {
		int cmp = this.id.compareTo(o.getId());
		if(cmp != 0) return cmp;
			
		cmp = this.codigo.compareTo(o.getCodigo());
		return cmp;
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
	public Long getIdTipoDocSel() {
		return idTipoDocSel;
	}
	@Transient
	public void setIdTipoDocSel(Long idTipoDocSel) {
		this.idTipoDocSel = idTipoDocSel;
	}
	@Transient
	public Long getIdBarrio() {
		return idBarrio;
	}
	@Transient
	public void setIdBarrio(Long idBarrio) {
		this.idBarrio = idBarrio;
	}
	@Transient
	public Long getIdDireccion() {
		return idDireccion;
	}
	@Transient
	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}
	@Transient
	public Long getIdAfipCondIva() {
		return idAfipCondIva;
	}
	@Transient
	public void setIdAfipCondIva(Long idAfipCondIva) {
		this.idAfipCondIva = idAfipCondIva;
	}
	@Transient
	public Long getIdRazonSocial() {
		return idRazonSocial;
	}
	@Transient
	public void setIdRazonSocial(Long idRazonSocial) {
		this.idRazonSocial = idRazonSocial;
	}
	@Transient
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	@Transient
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Column(columnDefinition = "VARCHAR(30)")
	public String getMesesFacturables() {
		return mesesFacturables;
	}

	public void setMesesFacturables(String mesesFacturables) {
		this.mesesFacturables = mesesFacturables;
	}
	@Column(columnDefinition = "VARCHAR(500)")
	public String getNotasFacturacion() {
		return notasFacturacion;
	}

	public void setNotasFacturacion(String notasFacturacion) {
		this.notasFacturacion = notasFacturacion;
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
	public String getRaSocial() {
		return raSocial;
	}
	@Transient
	public void setRaSocial(String raSocial) {
		this.raSocial = raSocial;
	}
	
}
