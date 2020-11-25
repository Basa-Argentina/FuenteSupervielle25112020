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
<title><c:if test="${accionFacturaDetalle == 'NUEVO'}">
		<spring:message code="formularioFacturaDetalle.titulo.registrar"
			htmlEscape="true" />
	</c:if> <c:if test="${accionFacturaDetalle == 'MODIFICACION'}">
		<spring:message code="formularioFacturaDetalle.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
		<spring:message code="formularioFacturaDetalle.titulo.consultar"
			htmlEscape="true" />
	</c:if></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_factura_detalle.js"></script>
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
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5">
						 <c:if	test="${accionFacturaDetalle == 'NUEVO'}">
							<spring:message code="formularioFacturaDetalle.titulo.registrar"
								htmlEscape="true" />
						</c:if> <c:if test="${accionFacturaDetalle == 'MODIFICACION'}">
							<spring:message code="formularioFacturaDetalle.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
							<spring:message code="formularioFacturaDetalle.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formFacturaDetalle" action="guardarActualizarFacturaDetalle.html" commandName="facturaDetalleFormulario" method="post"
						modelAttribute="facturaDetalleFormulario">
					<input type="hidden" id="codigoClienteEmp" name="codigoClienteEmp" value="<c:out value="${codigoClienteEmp}" default="" />" />
					<input type="hidden" id="accionFactura" name="accionFactura" value="<c:out value="${accionFactura}" default="" />" />
					<input type="hidden" id="accionFacturaDetalle" name="accionFacturaDetalle" value="<c:out value="${accionFacturaDetalle}" default="" />" />
					<input type="hidden" id="id" name="id" value="<c:out value="${id}" default="" />" />
					<input type="hidden" id="alicuota" value="<c:out value="${facturaDetalleFormulario.conceptoFacturable.impuesto.alicuota}" escapeXml="true"/>"/>
					<input type="hidden" id="clienteAspId" name="clienteAspId" value="<c:out value="${clienteAspId}" default="" />" />
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />" />
					<input type="hidden" id="precio" name="precio" value="<c:out value="${facturaDetalleFormulario.finalUnitario}" default="" />" />
					<input type="hidden" id="idAfipTipoComprobante" name="idAfipTipoComprobante" value="<c:out value="${idAfipTipoComprobante}" default="0" />" />
					<input type="hidden" id="codigoListaDefecto" name="codigoListaDefecto" value="<c:out value="${codigoListaDefecto}" default="" />" />
					<input type="hidden" id="salir" name="salir" value=false />
					
					<fieldset>
						<div style="width: 97%;" id="busquedaDiv" align="center">
							<table >
								
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFacturaDetalle.concepto" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
										<input type="text" id="codigoConcepto" class="completarZeros inputTextNumericPositiveIntegerOnly requerido"
											name="codigoConcepto" maxlength="6" style="width: 50px;" <c:if test="${accionFacturaDetalle == 'CONSULTAR'}"> readonly="readonly" </c:if>
											value="<c:out value="${facturaDetalleFormulario.codigoConcepto}" escapeXml="true"/>"
											<c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
												readonly="readonly"
											</c:if> />
										&nbsp;&nbsp;
										</td>
										<td>
										<button type="button" id="botonBuscarConceptoFacturable"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
												disabled="disabled"
											</c:if> >
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp;
										</td>
										<td>
										<textarea id="descripcion" name="descripcion" rows="2" cols="50" class="requerido">
											<c:out value="${facturaDetalleFormulario.descripcion}" escapeXml="true"/> 						
										</textarea>
										</td>
										</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFacturaDetalle.impuesto" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="alicuotaLabel"	for="codigoConcepto"> 
											<c:if test="${facturaDetalleFormulario.conceptoFacturable != null && facturaDetalleFormulario.conceptoFacturable.impuesto!=null}">
												<c:out value="${facturaDetalleFormulario.conceptoFacturable.impuesto.descripcion}" escapeXml="true"/>
												<fmt:formatNumber value="${facturaDetalleFormulario.conceptoFacturable.impuesto.alicuota}" />
											</c:if>
										</label> 
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFacturaDetalle.listaPrecios" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
											<input type="text" id="listaPreciosCodigo" class="requerido inputTextNumericPositiveIntegerOnly"
												name="listaPreciosCodigo"  style="width: 50px;"
												value="<c:out value="${facturaDetalleFormulario.listaPreciosCodigo}" default="" />"
												<c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
													readonly="readonly"
												</c:if> />
											&nbsp;&nbsp;
										</td>
										<td>
										<button type="button" id="botonBuscarListaPrecios"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
												disabled="disabled"
											</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										</td>
										<td>
										<label id="listaPreciosCodigoLabel"	for="listaPreciosCodigo"> 
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.listaprecios != null}">
												<c:out value="${facturaDetalleFormulario.listaprecios.descripcion}" escapeXml="true"/> <c:out value="/" escapeXml="true"/> <c:out value="${facturaDetalleFormulario.listaprecios.valor}" escapeXml="true"/>% 
											</c:if>
										</label>
										</td>
										</tr>
										</table>
									</td>
								</tr>
								
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFacturaDetalle.precio" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="precioLabel" >
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.netoUnitario != null}">
												$<fmt:formatNumber value="${facturaDetalleFormulario.netoUnitario}" />
											</c:if>
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.netoUnitario == null}">
												<c:out value="$ 0,00" escapeXml="true"/>
											</c:if>
										</label>									
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										
										<spring:message code="formularioFacturaDetalle.cantidad" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<table><tr><td>
												<input type="text" id="cantidad" class="requerido inputTextNumericPositiveIntegerOnly"
													name="cantidad" maxlength="10" style="width: 50px;"
													value="<c:out value="${facturaDetalleFormulario.cantidad}" default="" />"
												<c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
													readonly="readonly"
												</c:if>/>
												&nbsp;&nbsp;
										</td></tr></table>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFacturaDetalle.netoTotal" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="netoTotal" >
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.netoTotal != null}">
												$<fmt:formatNumber value="${facturaDetalleFormulario.netoTotal}" />
											</c:if>
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.netoTotal == null}">
												<c:out value="$ 0,00" escapeXml="true"/>
											</c:if>
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFacturaDetalle.impuestos" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="impuestos" >
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.impuestoTotal != null}">
												$<fmt:formatNumber value="${facturaDetalleFormulario.impuestoTotal}" />
											</c:if>
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.impuestoTotal == null}">
												<c:out value="$ 0,00" escapeXml="true"/>
											</c:if>
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFacturaDetalle.finalTotal" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="finalTotal">
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.finalTotal != null}">
												$<fmt:formatNumber value="${facturaDetalleFormulario.finalTotal}" />
											</c:if>
											<c:if test="${facturaDetalleFormulario != null && facturaDetalleFormulario.finalTotal == null}">
												<c:out value="$ 0,00" escapeXml="true"/>
											</c:if>
										</label>
									</td>
								</tr>
							</table>
						</div>
					</fieldset>					
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accionFacturaDetalle != 'CONSULTAR'}">
				<div align="center">
					<button id="botonGuardar" name="guardar" type="button" onclick="guardarYSalir();"
						class="botonCentradoGrande">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardarContinuar" htmlEscape="true" />
					</button>
					&nbsp;
					<button id="botonGuardarSalir" name="guardarSalir" type="button" onclick="guardarYSalir();"
						class="botonCentradoGrande">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardarSalir" htmlEscape="true" />
					</button>
					&nbsp;
					<button id="botonCancelar" name="cancelar" type="button" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accionFacturaDetalle == 'CONSULTAR'}">
				<div align="center">
					<button id="botonCancelar" name="cancelar" type="button" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>					
				</div>						
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 130%;">&nbsp;
	</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
<div class="selectorDiv"></div>
</body>
</html>