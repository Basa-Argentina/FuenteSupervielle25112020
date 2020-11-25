package com.security.modelo.configuraciongeneral;

import java.util.Date;
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
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.recursos.Configuracion;

@Entity(name="lote_exportacion_rearchivo")
public class LoteExportacionRearchivo {
	private Long id;//codigo
	private ClienteAsp clienteAsp;
	private Empresa empresa;
	private Sucursal sucursal;
	private ClienteEmp clienteEmp;
	private Date fechaRegistro;
	private ClasificacionDocumental clasificacionDocumental;
	private String descripcion;
	private String estado;//NO_GENERADO, GENERADO, GENERADO_INCONSISTENTE (hubo cambios después de generarlo).
	private Integer sizeMaxArchivo = 0;//MB
	private Set<LoteRearchivo> lotesRearchivo;
	private Integer cantPartesGeneradas = 0;
	private String pathArchivoBase;//path del disco duro del server
	//luego se generan los nombres de las distintas partes de acuerdo a la cantidad de archivos. ejemplo: 
	//zip generados: exportacion_rearchivo_digital_parte_{x}.zip
	//excel:
	//lote_imagenes_{codigo_clasificacion_documental}_parte_{x}.xls
	//rearchivos:
	//{codigo_contenedor}/{codigo_lote_rearchivo}/{numero1}_{texto1}_{descripcion}_{id_referencia}.tiff
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	@Column(name="fecha_registro")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	@Transient
	public String getFechaRegistroStr(){
		if(fechaRegistro!=null)
			return Configuracion.formatoFechaFormularios.format(fechaRegistro);
		return "";
	}
	public void setFechaRegistroStr(String fecha){}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClasificacionDocumental getClasificacionDocumental() {
		return clasificacionDocumental;
	}
	public void setClasificacionDocumental(
			ClasificacionDocumental clasificacionDocumental) {
		this.clasificacionDocumental = clasificacionDocumental;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Column(name="size_max_archivo")
	public Integer getSizeMaxArchivo() {
		return sizeMaxArchivo;
	}
	public void setSizeMaxArchivo(Integer sizeMaxArchivo) {
		this.sizeMaxArchivo = sizeMaxArchivo;
	}
	@Column(name="cantidad_partes_generadas")
	public Integer getCantPartesGeneradas() {
		return cantPartesGeneradas;
	}
	public void setCantPartesGeneradas(Integer partesGeneradas) {
		this.cantPartesGeneradas = partesGeneradas;
	}
	@Column(name="path_archivo_base")
	public String getPathArchivoBase() {
		return pathArchivoBase;
	}
	public void setPathArchivoBase(String pathArchivoBase) {
		this.pathArchivoBase = pathArchivoBase;
	}
	
	@ManyToMany(
			targetEntity=LoteRearchivo.class,
			cascade={CascadeType.PERSIST, CascadeType.MERGE},
			fetch=FetchType.LAZY 
	)
	@JoinTable(
			name="x_loteExportacion_loteRearchivo",
			joinColumns=@JoinColumn(name="loteExportacion_id",referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="loteRearchivo_id",referencedColumnName="id")
	)
	public Set<LoteRearchivo> getLotesRearchivo() {
		return lotesRearchivo;
	}
	public void setLotesRearchivo(Set<LoteRearchivo> lotesRearchivo) {
		this.lotesRearchivo = lotesRearchivo;
	}
}
