<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
	<title> 
	</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_loteFacturacionDetalle.js"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		
		<script type="text/javascript" src="js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
		<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
		<link rel="stylesheet" type="text/css" href="js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})" style="background-color: #1584D6; width: 700px; padding: 10px 0px 0px 0px;">
	<div id="divContenedorFB" align="center" style="width:700px; min-height:432px;margin:0px;padding:0px;background-color: #FFF;" align="left">
		<br style="font-size: medium;" />
		<fieldset style="border: none; text-align: left; width: 97%;">
			<legend>
				<font class="tituloForm" size="5">
					 <c:if	test="${accion == 'NUEVO'}">
						<spring:message code="formularioFacturaDetalle.titulo.registrar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'MODIFICACION'}">
						<spring:message code="formularioFacturaDetalle.titulo.modificar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'CONSULTA'}">
						<spring:message code="formularioFacturaDetalle.titulo.consultar"
							htmlEscape="true" />
					</c:if> </font>
			</legend>
			<br />
			<form:form name="formFacturaDetalle" action="guardarActualizarFacturaDetalle.html" commandName="facturaDetalleFormulario" method="post"
					modelAttribute="facturaDetalleFormulario">
			
				<input type="hidden" id="accion" name="accion" value="<c:out value="${loteFacturacionFormulario.accion}" default="" />" />
				<input type="hidden" id="id" name="id" value="<c:out value="${loteFacturacionFormulario.id}" default="" />" />
				<input type="hidden" id="clienteAspId" name="clienteAspId" value="<c:out value="${clienteAspId}" default="" />" />
				<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${loteFacturacionFormulario.codigoEmpresa}" default="" />" />
				<input type="hidden" id="codigoSucursal2" name="codigoSucursal" value="<c:out value="${loteFacturacionFormulario.codigoSucursal}" default="" />" />
				<input type="hidden" id="estado" name="estado" value="<c:out value="${loteFacturacionFormulario.estado}" default="" />" />
				<input type="hidden" id="periodo" name="periodo" value="<c:out value="${loteFacturacionFormulario.periodo}" default="" />" />
				<input type="hidden" id="descripcion" name="descripcion" value="<c:out value="${loteFacturacionFormulario.descripcion}" default="" />" />
				<input type="hidden" id="fechaRegistro" name="fechaRegistro" value="<c:out value="${loteFacturacionFormulario.fechaRegistro}" default="" />" />
				<input type="hidden" id="fechaFacturacion" name="fechaFacturacion" value="<c:out value="${loteFacturacionFormulario.fechaFacturacion}" default="" />" />
				
				<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="detalles" id="conceptoOperCliente" requestURI="mostrarLoteFacturacionDetalle.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${conceptoOperCliente.idConcepto}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column class="celdaAlineadoCentrado" title="<input type='checkbox' id='checktodos' name='checktodos'/>">
							    <input type="checkbox" class='checklote' value="${conceptoOperCliente.idConcepto}"/>
			              	</display:column>			
			              	<display:column property="razonCliente" titleKey="formularioConceptoOperacionCliente.cliente" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="fechaAltaStr" titleKey="formularioConceptoOperacionCliente.fecha" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="tipoConcepto" titleKey="formularioConceptoOperacionCliente.tipoConcepto" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="finalUnitario" titleKey="formularioConceptoOperacionCliente.finalUnitario" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="cantidad" titleKey="formularioConceptoOperacionCliente.cantidad" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="finalTotal" titleKey="formularioConceptoOperacionCliente.finalTotal" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="estado" titleKey="formularioConceptoOperacionCliente.estado" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="requerimiento" titleKey="formularioConceptoOperacionCliente.requerimiento" sortable="true" class="celdaAlineadoIzquierda"/>
						</display:table>
					<div style="width: 100%" align="right">
						<table>
							<tr>
								<td>
									<button name="agregar" type="button" id="agregar">
										<img src="<%=request.getContextPath()%>/images/add.png">
										<spring:message code="botones.asociar" htmlEscape="true" />
									</button>
								</td>
							</tr>
						</table>
					</div>
				</fieldset>				
			</form:form>
		</fieldset>
		<br style="font-size: xx-small;" />
		<c:if test="${accion != 'CONSULTA'}">
			<div align="center">
				<button id="botonCancelar" name="cancelar" type="button" class="botonCentrado">
					<img src="<%=request.getContextPath()%>/images/cancelar.png">
					<spring:message code="botones.cerrar" htmlEscape="true" />
				</button>
			</div>
		</c:if>
		<c:if test="${accion == 'CONSULTA'}">
			<div align="center">
				<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
					<img src="<%=request.getContextPath()%>/images/volver4.png"> 
					<spring:message code="botones.volver" htmlEscape="true"/>  
				</button>
			</div>						
		</c:if>
		<br style="font-size: xx-small;" />
	</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
<div class="selectorDiv"></div>
</body>
</html>