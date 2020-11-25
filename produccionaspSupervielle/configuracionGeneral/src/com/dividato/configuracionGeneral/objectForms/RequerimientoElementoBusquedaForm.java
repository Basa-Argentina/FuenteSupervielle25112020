package com.dividato.configuracionGeneral.objectForms;

import java.util.Date;

import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.jerarquias.TipoRequerimiento;
import com.security.recursos.Configuracion;
/**
 * 
 * @author Gabriel Mainero
 *
 */
public class RequerimientoElementoBusquedaForm {
	private String codigoClasificacionDocumental;
	private String codigoEmpresa;
	private String codigoSucursal;
	private String codigoCliente;
	private String codigoTipoElemento;
	private String codigoContenedor;
	private String codigoElemento;
	private String codigoLectura;
	private String seleccion;
	private Date fecha1;
	private Date fechaEntre;
	private Date fecha2;
	private Date fechaInicio;
	private Date fechaFin;
	private String texto1;
	private String texto2;
	private String descripcion;
	private Long numero1;
	private Long numeroEntre;
	private Long numero2;
	private Elemento elemento;
	private TipoElemento tipoElemento;
	private ClasificacionDocumental clasificacionDocumental;
	private Empresa empresa;
	private Sucursal sucursal;
	private TipoRequerimiento tipoReq;
	private boolean numero1Derecha;
	private boolean numero1Izquierda;
	private boolean numero2Derecha;
	private boolean numero2Izquierda;
	private boolean texto1Derecha;
	private boolean texto1Izquierda;
	private boolean texto2Derecha;
	private boolean texto2Izquierda;
	private boolean descripcionDerecha;
	private boolean descripcionIzquierda;
	private String destinoURL;
	private String cImagenes;
	private boolean isRequerimiento;
	
	public String getCodigoCliente() {
		return codigoCliente;
	}



	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}



	public Empresa getEmpresa() {
		return empresa;
	}



	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}



	public Sucursal getSucursal() {
		return sucursal;
	}



	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
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



	public String getCodigoClasificacionDocumental() {
		return codigoClasificacionDocumental;
	}



	public void setCodigoClasificacionDocumental(
			String codigoClasificacionDocumental) {
		this.codigoClasificacionDocumental = codigoClasificacionDocumental;
	}



	public String getCodigoContenedor() {
		return codigoContenedor;
	}



	public void setCodigoContenedor(String codigoContenedor) {
		this.codigoContenedor = codigoContenedor;
	}


	public String getCodigoElemento() {
		return codigoElemento;
	}



	public void setCodigoElemento(String codigoElemento) {
		this.codigoElemento = codigoElemento;
	}



	public String getCodigoTipoElemento() {
		return codigoTipoElemento;
	}



	public void setCodigoTipoElemento(String codigoTipoElemento) {
		this.codigoTipoElemento = codigoTipoElemento;
	}


	public String getSeleccion() {
		return seleccion;
	}



	public void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
	}



	public Date getFecha1() {
		return fecha1;
	}



	public void setFecha1(Date fecha1) {
		this.fecha1 = fecha1;
	}



	public Date getFechaEntre() {
		return fechaEntre;
	}



	public void setFechaEntre(Date fechaEntre) {
		this.fechaEntre = fechaEntre;
	}



	public Date getFecha2() {
		return fecha2;
	}



	public void setFecha2(Date fecha2) {
		this.fecha2 = fecha2;
	}

	
	public Date getFechaInicio() {
		return fechaInicio;
	}


	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}


	public String getTexto1() {
		return texto1;
	}



	public void setTexto1(String texto1) {
		this.texto1 = texto1;
	}



	public String getTexto2() {
		return texto2;
	}



	public void setTexto2(String texto2) {
		this.texto2 = texto2;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public Long getNumero1() {
		return numero1;
	}



	public void setNumero1(Long numero1) {
		this.numero1 = numero1;
	}



	public Long getNumeroEntre() {
		return numeroEntre;
	}



	public void setNumeroEntre(Long numeroEntre) {
		this.numeroEntre = numeroEntre;
	}



	public Long getNumero2() {
		return numero2;
	}



	public void setNumero2(Long numero2) {
		this.numero2 = numero2;
	}



	public Elemento getElemento() {
		return elemento;
	}



	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}



	public TipoElemento getTipoElemento() {
		return tipoElemento;
	}



	public void setTipoElemento(TipoElemento tipoElemento) {
		this.tipoElemento = tipoElemento;
	}



	public ClasificacionDocumental getClasificacionDocumental() {
		return clasificacionDocumental;
	}



	public void setClasificacionDocumental(
			ClasificacionDocumental clasificacionDocumental) {
		this.clasificacionDocumental = clasificacionDocumental;
	}



	public boolean isNumero1Derecha() {
		return numero1Derecha;
	}



	public void setNumero1Derecha(boolean numero1Derecha) {
		this.numero1Derecha = numero1Derecha;
	}



	public boolean isNumero1Izquierda() {
		return numero1Izquierda;
	}



	public void setNumero1Izquierda(boolean numero1Izquierda) {
		this.numero1Izquierda = numero1Izquierda;
	}



	public boolean isNumero2Derecha() {
		return numero2Derecha;
	}



	public void setNumero2Derecha(boolean numero2Derecha) {
		this.numero2Derecha = numero2Derecha;
	}



	public boolean isNumero2Izquierda() {
		return numero2Izquierda;
	}



	public void setNumero2Izquierda(boolean numero2Izquierda) {
		this.numero2Izquierda = numero2Izquierda;
	}



	public boolean isTexto1Derecha() {
		return texto1Derecha;
	}



	public void setTexto1Derecha(boolean texto1Derecha) {
		this.texto1Derecha = texto1Derecha;
	}



	public boolean isTexto1Izquierda() {
		return texto1Izquierda;
	}



	public void setTexto1Izquierda(boolean texto1Izquierda) {
		this.texto1Izquierda = texto1Izquierda;
	}



	public boolean isTexto2Derecha() {
		return texto2Derecha;
	}



	public void setTexto2Derecha(boolean texto2Derecha) {
		this.texto2Derecha = texto2Derecha;
	}



	public boolean isTexto2Izquierda() {
		return texto2Izquierda;
	}



	public void setTexto2Izquierda(boolean texto2Izquierda) {
		this.texto2Izquierda = texto2Izquierda;
	}



	public boolean isDescripcionDerecha() {
		return descripcionDerecha;
	}



	public void setDescripcionDerecha(boolean descripcionDerecha) {
		this.descripcionDerecha = descripcionDerecha;
	}



	public boolean isDescripcionIzquierda() {
		return descripcionIzquierda;
	}



	public void setDescripcionIzquierda(boolean descripcionIzquierda) {
		this.descripcionIzquierda = descripcionIzquierda;
	}



	public String getFecha1Str(){
		if(fecha1!=null)
			return Configuracion.formatoFechaFormularios.format(fecha1);
		return "";
	}
	
	public String getFechaEntreStr(){
		if(fechaEntre!=null)
			return Configuracion.formatoFechaFormularios.format(fechaEntre);
		return "";
	}
	
	public String getFecha2Str(){
		if(fecha2!=null)
			return Configuracion.formatoFechaFormularios.format(fecha2);
		return "";
	}

	public String getFechaInicioStr(){
		if(fechaInicio!=null)
			return Configuracion.formatoFechaFormularios.format(fechaInicio);
		return "";
	}

	public String getFechaFinStr(){
		if(fechaFin!=null)
			return Configuracion.formatoFechaFormularios.format(fechaFin);
		return "";
	}
	
	public String getDestinoURL() {
		return destinoURL;
	}

	public void setDestinoURL(String destinoURL) {
		this.destinoURL = destinoURL;
	}


	public String getCodigoLectura() {
		return codigoLectura;
	}

	public void setCodigoLectura(String codigoLectura) {
		this.codigoLectura = codigoLectura;
	}

	public boolean isRequerimiento() {
		return isRequerimiento;
	}

	public void setRequerimiento(boolean isRequerimiento) {
		this.isRequerimiento = isRequerimiento;
	}

	public TipoRequerimiento getTipoReq() {
		return tipoReq;
	}

	public void setTipoReq(TipoRequerimiento tipoReq) {
		this.tipoReq = tipoReq;
	}



	public String getcImagenes() {
		return cImagenes;
	}



	public void setcImagenes(String cImagenes) {
		this.cImagenes = cImagenes;
	}

}
