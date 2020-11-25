package com.security.modelo.configuraciongeneral;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.recursos.Configuracion;
/**
 * 
 * @author FedeMz
 *
 */
@Entity
@Table(name="referencia")
public class Referencia implements Comparable<Referencia>{
	private Long id;
	private LoteReferencia loteReferencia;
	private ClasificacionDocumental clasificacionDocumental;
	private Elemento elemento;
	private Boolean indiceIndividual;
	private Long numero1;
	private Long numero2;
	private Date fecha1;
	private Date fecha2;
	private String texto1;
	private String texto2;
	private String descripcion;
	private Referencia referenciaRearchivo;
	private String descripcionRearchivo;
	private String prefijoCodigoTipoElemento;
	private Elemento contenedor;
	private Integer ordenRearchivo;
	private String pathArchivoDigital;
	private String pathLegajo;
	private Long  cImagenes;
	private Date fechaHora;
	
	//INICIO - Agregado BUZON DE TAREAS
	private Long codigoUsuario;
	private String descripcionTarea;
	private String estadoTarea;
	//FIN - Agregado BUZON DE TAREAS
	
	private transient String codigoElementoDesde;
	private transient String codigoElementoHasta;
	private transient Boolean porRango;
	
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof Referencia)){
			return super.equals(other);
		}
		Referencia otra = (Referencia)other;
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
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="elemento_id")
	public Elemento getElemento() {
		return elemento;
	}
	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="lote_referencia_id")
	public LoteReferencia getLoteReferencia() {
		return loteReferencia;
	}
	public void setLoteReferencia(LoteReferencia loteReferencia) {
		this.loteReferencia = loteReferencia;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="clasificacion_documental_id")
	public ClasificacionDocumental getClasificacionDocumental() {
		return clasificacionDocumental;
	}
	public void setClasificacionDocumental(
			ClasificacionDocumental clasificacionDocumental) {
		this.clasificacionDocumental = clasificacionDocumental;
	}
	
	@Column(name="indice_individual")
	public Boolean getIndiceIndividual() {
		return indiceIndividual;
	}
	public void setIndiceIndividual(Boolean indiceIndividual) {
		this.indiceIndividual = indiceIndividual;
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
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="referencia_rearchivo_id")
	public Referencia getReferenciaRearchivo() {
		return referenciaRearchivo;
	}

	public void setReferenciaRearchivo(Referencia referenciaRearchivo) {
		this.referenciaRearchivo = referenciaRearchivo;
	}
	@Column(name="descripcion_rearchivo")
	public String getDescripcionRearchivo() {
		return descripcionRearchivo;
	}

	public void setDescripcionRearchivo(String descripcionRearchivo) {
		this.descripcionRearchivo = descripcionRearchivo;
	}
	
	public Integer getOrdenRearchivo() {
		return ordenRearchivo;
	}

	public void setOrdenRearchivo(Integer ordenRearchivo) {
		this.ordenRearchivo = ordenRearchivo;
	}
	
	public String getPathArchivoDigital() {
		return pathArchivoDigital;
	}

	public void setPathArchivoDigital(String pathArchivoDigital) {
		this.pathArchivoDigital = pathArchivoDigital;
	}
	
	public String getPathLegajo() {
		return pathLegajo;
	}

	public void setPathLegajo(String pathLegajo) {
		this.pathLegajo = pathLegajo;
	}
	
	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getDescripcionTarea() {
		return descripcionTarea;
	}

	public void setDescripcionTarea(String descripcionTarea) {
		this.descripcionTarea = descripcionTarea;
	}

	@Transient
	public String getCodigoElementoDesde() {
		return codigoElementoDesde;
	}
	@Transient
	public void setCodigoElementoDesde(String codigoElementoDesde) {
		this.codigoElementoDesde = codigoElementoDesde;
	}
	@Transient
	public String getCodigoElementoHasta() {
		return codigoElementoHasta;
	}
	@Transient
	public void setCodigoElementoHasta(String codigoElementoHasta) {
		this.codigoElementoHasta = codigoElementoHasta;
	}
	@Transient
	public Boolean getPorRango() {
		return porRango;
	}
	@Transient
	public void setPorRango(Boolean porRango) {
		this.porRango = porRango;
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
	public void setFecha2Str(String fecha){}
	@Transient
	public String getIndiceIndividualStr(){
		return indiceIndividual?"Individual":"Grupal";
	}
	public void setIndiceIndividualStr(String indice){}
	
	@Transient
	public Elemento getContenedor(){
		return contenedor;
	}
	public void setContenedor(Elemento contenedor){
		this.contenedor = contenedor;
		if(this.elemento==null)
			this.elemento=contenedor;
		else
			this.elemento.setContenedor(contenedor);
	}
	
	@Transient
	public void setPrefijoCodigoTipoElemento(String prefijoCodigoTipoElemento) {
		this.prefijoCodigoTipoElemento = prefijoCodigoTipoElemento;
	}

	public String getPrefijoCodigoTipoElemento() {
		if(prefijoCodigoTipoElemento==null)
			if(getElementoContenedor()!=null){
				return getElementoContenedor().getTipoElemento().getPrefijoCodigo();
			}else{
				return prefijoCodigoTipoElemento; 
			}
		return prefijoCodigoTipoElemento;
	}

	@Transient
	public Elemento getElementoContenedor(){
		return !indiceIndividual?elemento:elemento!=null && elemento.getContenedor()!=null?elemento.getContenedor():contenedor;
	}
	public void setElementoContenedor(Elemento elemento){}
	
	@Transient
	public Elemento getElementoContenido(){
		return !indiceIndividual?null: elemento!=null && elemento.getTipoElemento()!=null && elemento.getTipoElemento().getContenido()?elemento:null;
	}
	public void setElementoContenido(Elemento elemento){}
		
	
	@Transient
	public String getObj_hash(){
		return super.toString();
	}
	public void setObj_hash(String hash){}

	@Override
	public int compareTo(Referencia o) {
		if(this.id!=null && o.getId()!=null)
			return this.id.compareTo(o.getId());
		else
			return 0;
	}
	@Transient
	public String getNumero1Str() {
		if(numero1!=null)
			return numero1.toString();
		return "";
	}
	@Transient
	public String getNumero2Str() {
		if(numero2!=null)
			return numero2.toString();
		return "";
	}

	public String getEstadoTarea() {
		return estadoTarea;
	}

	public void setEstadoTarea(String estadoTarea) {
		this.estadoTarea = estadoTarea;
	}
	@Column(name="cImagenes")
	public Long  getcImagenes() {
		return cImagenes;
	}

	public void setcImagenes(Long  cImagenes) {
		this.cImagenes = cImagenes;
	}
	
}
