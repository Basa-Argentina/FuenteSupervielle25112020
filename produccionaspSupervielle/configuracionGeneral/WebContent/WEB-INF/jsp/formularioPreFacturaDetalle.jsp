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
<title><c:if test="${accionPreFacturaDetalle == 'NUEVO'}">
		<spring:message code="formularioPreFacturaDetalle.titulo.registrar"
			htmlEscape="true" />
	</c:if> <c:if test="${accionPreFacturaDetalle == 'MODIFICACION'}">
		<spring:message code="formularioPreFacturaDetalle.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
		<spring:message code="formularioPreFacturaDetalle.titulo.consultar"
			htmlEscape="true" />
	</c:if></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_preFactura_detalle.js"></script>
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
						 <c:if	test="${accionPreFacturaDetalle == 'NUEVO'}">
							<spring:message code="formularioPreFacturaDetalle.titulo.registrar"
								htmlEscape="true" />
						</c:if> <c:if test="${accionPreFacturaDetalle == 'MODIFICACION'}">
							<spring:message code="formularioPreFacturaDetalle.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
							<spring:message code="formularioPreFacturaDetalle.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formPreFacturaDetalle" action="guardarActualizarPreFacturaDetalle.html" commandName="preFacturaDetalleFormulario" method="post"
						modelAttribute="preFacturaDetalleFormulario">
					<input type="hidden" id="codigoClienteEmp" name="codigoClienteEmp" value="<c:out value="${codigoClienteEmp}" default="" />" />
					<input type="hidden" id="accionPreFactura" name="accionPreFactura" value="<c:out value="${accionPreFactura}" default="" />" />
					<input type="hidden" id="accionPreFacturaDetalle" name="accionPreFacturaDetalle" value="<c:out value="${accionPreFacturaDetalle}" default="" />" />
					<input type="hidden" id="ordenDetalle" name="ordenDetalle" value="<c:out value="${ordenDetalle}" default="" />" />
					<input type="hidden" id="alicuota" value="<c:out value="${preFacturaDetalleFormulario.conceptoFacturable.impuesto.alicuota}" escapeXml="true"/>"/>
					<input type="hidden" id="clienteAspId" name="clienteAspId" value="<c:out value="${clienteAspId}" default="" />" />
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />" />
					<input type="hidden" id="precio" name="precio" value="<c:out value="${preFacturaDetalleFormulario.finalUnitario}" default="" />" />
					<input type="hidden" id="idAfipTipoComprobante" name="idAfipTipoComprobante" value="<c:out value="${idAfipTipoComprobante}" default="0" />" />
					
					<fieldset>
						<div style="width: 97%;" id="busquedaDiv" align="center">
							<table >
								
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioPreFacturaDetalle.concepto" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
										<input type="text" id="codigoConcepto" class="completarZeros inputTextNumericPositiveIntegerOnly requerido"
											name="codigoConcepto" maxlength="6" style="width: 50px;" <c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}"> readonly="readonly" </c:if>
											value="<c:out value="${preFacturaDetalleFormulario.codigoConcepto}" escapeXml="true"/>"
											<c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
												readonly="readonly"
											</c:if> />
										&nbsp;&nbsp;
										</td>
										<td>
										<button type="button" id="botonBuscarConceptoFacturable"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
												disabled="disabled"
											</c:if> >
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp;
										</td>
										<td>
										<textarea id="descripcion" name="descripcion" rows="2" cols="50" class="requerido">
											<c:out value="${preFacturaDetalleFormulario.descripcion}" escapeXml="true"/> 						
										</textarea>
										</td>
										</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioPreFacturaDetalle.impuesto" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="alicuotaLabel"	for="codigoConcepto"> 
											<c:if test="${preFacturaDetalleFormulario.conceptoFacturable != null && preFacturaDetalleFormulario.conceptoFacturable.impuesto!=null}">
												<c:out value="${preFacturaDetalleFormulario.conceptoFacturable.impuesto.descripcion}" escapeXml="true"/>
												<fmt:formatNumber value="${preFacturaDetalleFormulario.conceptoFacturable.impuesto.alicuota}" />
											</c:if>
										</label> 
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioPreFacturaDetalle.listaPrecios" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
											<input type="text" id="listaPreciosCodigo" class="requerido inputTextNumericPositiveIntegerOnly"
												name="listaPreciosCodigo"  style="width: 50px;" readonly="readonly"
												value="<c:out value="${preFacturaDetalleFormulario.listaPreciosCodigo}" default="" />"
												<c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
													readonly="readonly"
												</c:if> />
											&nbsp;&nbsp;
										</td>
										<td>
										<button type="button" id="botonBuscarListaPrecios"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
												disabled="disabled"
											</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										</td>
										<td>
										<label id="listaPreciosCodigoLabel"	for="listaPreciosCodigo"> 
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.listaprecios != null}">
												<c:out value="${preFacturaDetalleFormulario.listaprecios.descripcion}" escapeXml="true"/> <c:out value="/" escapeXml="true"/> <c:out value="${preFacturaDetalleFormulario.listaprecios.valor}" escapeXml="true"/>% 
											</c:if>
										</label>
										</td>
										</tr>
										</table>
									</td>
								</tr>
								
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioPreFacturaDetalle.precio" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="precioLabel" >
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.netoUnitario != null}">
												$<fmt:formatNumber value="${preFacturaDetalleFormulario.netoUnitario}" />
											</c:if>
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.netoUnitario == null}">
												<c:out value="$ 0,00" escapeXml="true"/>
											</c:if>
										</label>									
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										
										<spring:message code="formularioPreFacturaDetalle.cantidad" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<table><tr><td>
												<input type="text" id="cantidad" class="requerido inputTextNumericPositiveIntegerOnly"
													name="cantidad" maxlength="10" style="width: 50px;"
													value="<c:out value="${preFacturaDetalleFormulario.cantidad}" default="" />"
												<c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
													readonly="readonly"
												</c:if>/>
												&nbsp;&nbsp;
										</td></tr></table>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioPreFacturaDetalle.netoTotal" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="netoTotal" >
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.netoTotal != null}">
												$<fmt:formatNumber value="${preFacturaDetalleFormulario.netoTotal}" />
											</c:if>
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.netoTotal == null}">
												<c:out value="$ 0,00" escapeXml="true"/>
											</c:if>
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioPreFacturaDetalle.impuestos" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="impuestos" >
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.impuestoTotal != null}">
												$<fmt:formatNumber value="${preFacturaDetalleFormulario.impuestoTotal}" />
											</c:if>
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.impuestoTotal == null}">
												<c:out value="$ 0,00" escapeXml="true"/>
											</c:if>
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioPreFacturaDetalle.finalTotal" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<label id="finalTotal">
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.finalTotal != null}">
												$<fmt:formatNumber value="${preFacturaDetalleFormulario.finalTotal}" />
											</c:if>
											<c:if test="${preFacturaDetalleFormulario != null && preFacturaDetalleFormulario.finalTotal == null}">
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
			<c:if test="${accionPreFacturaDetalle != 'CONSULTAR'}">
				<div align="center">
					<button id="botonGuardar" name="guardar" type="button" onclick="guardarYSalir();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
					<button id="botonCancelar" name="cancelar" type="button" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accionPreFacturaDetalle == 'CONSULTAR'}">
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