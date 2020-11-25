package com.security.modelo.configuraciongeneral;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.recursos.Configuracion;
/**
 * 
 * @author Gabriel Mainero
 *
 */
@Entity(name="rearchivo")
public class Rearchivo implements Comparable<Rearchivo>{
	private Long id;
	private LoteRearchivo loteRearchivo;
	private Referencia referencia;
	private Integer orden;
	private String estado;
	private Long numero1;
	private Long numero2;
	private Date fecha1;
	private Date fecha2;
	private String texto1;
	private String texto2;
	private String descripcion;
	private String nombreArchivoDigital;
	private String pathArchivoDigital;
	private String pathArchivoJPGDigital;
	private Integer cantidadImagenes;
	private ClasificacionDocumental clasifDoc;
	
	private transient Long codigoUsuario;
	private transient String descripcionTarea;
	private transient String tipoImg;
	private transient String numero1Str;
	private transient String numero2Str;
	private transient String codigoClasifDoc;
	private transient Elemento elemento;
	
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof Rearchivo)){
			return super.equals(other);
		}
		Rearchivo otra = (Rearchivo)other;
		if(this.getId()==null || otra.getId()==null)
			return super.equals(other);
		return this.getId().equals(otra.getId());
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public LoteRearchivo getLoteRearchivo() {
		return loteRearchivo;
	}
	public void setLoteRearchivo(LoteRearchivo loteRearchivo) {
		this.loteRearchivo = loteRearchivo;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Referencia getReferencia() {
		return referencia;
	}
	public void setReferencia(Referencia referencia) {
		this.referencia = referencia;
	}
	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name="numero1")
	public Long getNumero1() {
		return numero1;
	}
	public void setNumero1(Long numero1) {
		this.numero1 = numero1;
	}
	@Column(name="numero2")
	public Long getNumero2() {
		return numero2;
	}
	public void setNumero2(Long numero2) {
		this.numero2 = numero2;
	}
	@Column(name="fecha1")
	public Date getFecha1() {
		return fecha1;
	}
	public void setFecha1(Date fecha1) {
		this.fecha1 = fecha1;
	}
	@Column(name="fecha2")
	public Date getFecha2() {
		return fecha2;
	}
	public void setFecha2(Date fecha2) {
		this.fecha2 = fecha2;
	}
	@Column(name="texto1")
	public String getTexto1() {
		return texto1;
	}
	public void setTexto1(String texto1) {
		this.texto1 = texto1;
	}
	@Column(name="texto2")
	public String getTexto2() {
		return texto2;
	}
	public void setTexto2(String texto2) {
		this.texto2 = texto2;
	}
	@Column(name="descripcion")
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getNombreArchivoDigital() {
		return nombreArchivoDigital;
	}

	public void setNombreArchivoDigital(String nombreArchivoDigital) {
		this.nombreArchivoDigital = nombreArchivoDigital;
	}
	@Column(length=1000)
	public String getPathArchivoDigital() {
		return pathArchivoDigital;
	}

	public void setPathArchivoDigital(String pathArchivoDigital) {
		this.pathArchivoDigital = pathArchivoDigital;
	}
	@Column(length=1000)
	public String getPathArchivoJPGDigital() {
		return pathArchivoJPGDigital;
	}

	public void setPathArchivoJPGDigital(String pathArchivoJPGDigital) {
		this.pathArchivoJPGDigital = pathArchivoJPGDigital;
	}

	public Integer getCantidadImagenes() {
		return cantidadImagenes;
	}

	public void setCantidadImagenes(Integer cantidadImagenes) {
		this.cantidadImagenes = cantidadImagenes;
	}

	@Transient
	public String getFecha1Str(){
		if(fecha1!=null)
			return Configuracion.formatoFechaFormularios.format(fecha1);
		return "";
	}
	public void setFecha1Str(String fecha){}
	
	@Transient
	public String getFecha2Str(){
		if(fecha2!=null)
			return Configuracion.formatoFechaFormularios.format(fecha2);
		return "";
	}
		
	
	@Transient
	public String getObj_hash(){
		return super.toString();
	}
	public void setObj_hash(String hash){}

	@Override
	public int compareTo(Rearchivo o) {
		return this.orden.compareTo(o.getOrden());
	}
	
	@Transient
	public String getNumero1Str() {
		return numero1Str;
	}
	@Transient
	public void setNumero1Str(String numero1Str) {
		this.numero1Str = numero1Str;
	}
	@Transient
	public String getNumero2Str() {
		return numero2Str;
	}
	@Transient
	public void setNumero2Str(String numero2Str) {
		this.numero2Str = numero2Str;
	}
	@Transient
	public String getTipoImg() {
		return tipoImg;
	}
	@Transient
	public void setTipoImg(String tipoImg) {
		this.tipoImg = tipoImg;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="clasificacionDocumental_id")
	public ClasificacionDocumental getClasifDoc() {
		return clasifDoc;
	}
	public void setClasifDoc(
			ClasificacionDocumental clasifDoc) {
		this.clasifDoc = clasifDoc;
	}
	@Transient
	public String getCodigoClasifDoc() {
		if(codigoClasifDoc==null)
			if(clasifDoc!=null)
				return clasifDoc.getCodigo().toString();
		return codigoClasifDoc;
	}
	@Transient
	public void setCodigoClasifDoc(String codigoClasifDoc) {
		this.codigoClasifDoc = codigoClasifDoc;
	}
	@Transient
	public Elemento getElemento() {
		return elemento;
	}
	@Transient
	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
	@Transient
	public Long getCodigoUsuario() {
		return codigoUsuario;
	}
	@Transient
	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	@Transient
	public String getDescripcionTarea() {
		return descripcionTarea;
	}
	@Transient
	public void setDescripcionTarea(String descripcionTarea) {
		this.descripcionTarea = descripcionTarea;
	}
	
}
