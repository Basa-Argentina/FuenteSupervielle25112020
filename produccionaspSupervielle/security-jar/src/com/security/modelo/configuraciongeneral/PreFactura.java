package com.security.modelo.configuraciongeneral;


import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;

@Entity(name="preFacturas")
public class PreFactura {
	private Long id;
	private ClienteAsp clienteAsp;
	private Empresa empresa;
	private Sucursal sucursal;
	private ClienteEmp clienteEmp;
	private Serie serie;
	private AfipTipoComprobante afipTipoDeComprobante;
	private Date fecha;
	private String prefijoSerie;
	private String letraComprobante;
	private BigDecimal totalNeto;
	private BigDecimal totalIVA;
	private BigDecimal totalFinal;
	private String observacion;
	private String estado;
	private LoteFacturacion loteFacturacion;
	private Integer orden;
	private String notasFacturacion;
	
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient String codigoCliente;
	private transient Long idAfipTipoComprobante;
	private transient String codigoSerie;
	private transient String clienteEmpCodigoCondIva;
	private transient Boolean mostrarAnulados;
	private transient List<PreFacturaDetalle> detalles; 
	
	public PreFactura() {
		super();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}
	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public AfipTipoComprobante getAfipTipoDeComprobante() {
		return afipTipoDeComprobante;
	}
	public void setAfipTipoDeComprobante(AfipTipoComprobante afipTipoDeComprobante) {
		this.afipTipoDeComprobante = afipTipoDeComprobante;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getPrefijoSerie() {
		return prefijoSerie;
	}

	public void setPrefijoSerie(String prefijoSerie) {
		this.prefijoSerie = prefijoSerie;
	}
	
	@Transient
	public String getFechaDesdeStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaDesde!=null){			
				result = sd.format(fechaDesde);
			}else{
				result = "";
			}
		}catch(Exception e){
			fechaDesde = null;
			result = "";
		}
		return result;
	}
		
	public String getLetraComprobante() {
		return letraComprobante;
	}

	public void setLetraComprobante(String letraComprobante) {
		this.letraComprobante = letraComprobante;
	}

	public BigDecimal getTotalNeto() {
		return totalNeto;
	}

	public void setTotalNeto(BigDecimal totalNeto) {
		this.totalNeto = totalNeto;
	}

	public BigDecimal getTotalIVA() {
		return totalIVA;
	}

	public void setTotalIVA(BigDecimal totalIVA) {
		this.totalIVA = totalIVA;
	}

	public BigDecimal getTotalFinal() {
		return totalFinal;
	}

	public void setTotalFinal(BigDecimal totalFinal) {
		this.totalFinal = totalFinal;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public LoteFacturacion getLoteFacturacion() {
		return loteFacturacion;
	}

	public void setLoteFacturacion(LoteFacturacion loteFacturacion) {
		this.loteFacturacion = loteFacturacion;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	public String getNotasFacturacion() {
		return notasFacturacion;
	}

	public void setNotasFacturacion(String notasFacturacion) {
		this.notasFacturacion = notasFacturacion;
	}

	@Transient
	public void setFechaDesdeStr(String fechaDesdeStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaDesdeStr += " 00:00:00";
			this.fechaDesde = df.parse(fechaDesdeStr);
		}catch (ParseException pe){
			fechaDesde = null;
		}
	}
	@Transient
	public String getFechaStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fecha!=null){
				result = sd.format(fecha);
			}else{
				fecha= new Date();
				result = sd.format(fecha);
			}
		}catch(Exception e){
			fecha = null;
			result = getFechaHastaStr();
		}
		return result;
	}
	@Transient
	public void setFechaStr(String fechaStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			this.fecha = df.parse(fechaStr);
		}catch (ParseException pe){
			fecha=null;
		}
	}
	
	@Transient
	public String getFechaString() {
		if(fecha==null)
			return "";
		return formatoFechaFormularios.format(fecha);
	}
	@Transient
	public String getFechaHastaStr() {
		String result = "";
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try{
			if(fechaHasta!=null){
				result = sd.format(fechaHasta);
			}else{
				fechaHasta = new Date();
				result = sd.format(fechaHasta);
			}
		}catch(Exception e){
			fechaHasta = null;
			result = getFechaStr();
		}
		return result;
	}
	@Transient
	public void setFechaHastaStr(String fechaHastaStr) {
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fechaHastaStr += " 23:59:59";
			this.fechaHasta = df.parse(fechaHastaStr);
		}catch (ParseException pe){
			fechaHasta=null;
		}
	}
	@Transient
	public Date getFechaDesde() {
		return fechaDesde;
	}
	@Transient
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	@Transient
	public Date getFechaHasta() {
		return fechaHasta;
	}
	@Transient
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
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
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	@Transient
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	@Transient
	public String getCodigoCliente() {
		return codigoCliente;
	}
	@Transient
	public void setCodigoCliente(String codigoClienteEmp) {
		this.codigoCliente = codigoClienteEmp;
	}
	@Transient
	public String getCodigoSerie() {
		return codigoSerie;
	}
	@Transient
	public void setCodigoSerie(String codigoSerie) {
		this.codigoSerie = codigoSerie;
	}
	
	@Transient
	public Long getIdAfipTipoComprobante() {
		return idAfipTipoComprobante;
	}
	@Transient
	public void setIdAfipTipoComprobante(Long idAfipTipoComprobante) {
		this.idAfipTipoComprobante = idAfipTipoComprobante;
	}
	@Transient
	public String getClienteEmpCodigoCondIva() {
		return clienteEmpCodigoCondIva;
	}
	@Transient
	public void setClienteEmpCodigoCondIva(String clienteEmpCodigoCondIva) {
		this.clienteEmpCodigoCondIva = clienteEmpCodigoCondIva;
	}
	@Transient
	public Boolean getMostrarAnulados() {
		return mostrarAnulados;
	}
	@Transient
	public void setMostrarAnulados(Boolean mostrarAnulados) {
		this.mostrarAnulados = mostrarAnulados;
	}
	@Transient
	public List<PreFacturaDetalle> getDetalles() {
		return detalles;
	}
	@Transient
	public void setDetalles(List<PreFacturaDetalle> detalles) {
		this.detalles = detalles;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj == null) {
//			return false;
//		}
//		if (!(obj instanceof PreFactura)) {
//			return false;
//		}
//		PreFactura other = (PreFactura) obj;
//		if (id == null) {
//			if (other.id != null) {
//				return false;
//			}
//		} else if (!id.equals(other.id)) {
//			return false;
//		}
//		return true;
//	}
	

}