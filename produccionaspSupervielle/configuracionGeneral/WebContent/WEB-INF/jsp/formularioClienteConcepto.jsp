<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioClienteConcepto.tituloFormAgregar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> (<spring:message code="general.ambiente"
			htmlEscape="true" />)
			</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioClienteConcepto.tituloFormModificar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> (<spring:message code="general.ambiente"
			htmlEscape="true" />)
			</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioClienteConcepto.tituloFormConsultar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> (<spring:message code="general.ambiente"
			htmlEscape="true" />)
			</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_cliente_concepto.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
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
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							<spring:message code="formularioClienteConcepto.tituloFormAgregar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioClienteConcepto.tituloFormModificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioClienteConcepto.tituloFormConsultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarClienteConcepto.html"
					commandName="clienteConceptoFormulario" method="post"
					modelAttribute="clienteConceptoFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${clienteConceptoFormulario.id}" default="" />" />
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioClienteConcepto.datosClienteConcepto" htmlEscape="true" />
									</font> <img id="busquedaImgSrcDown" src="images/skip_down.png"
										title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
										style="DISPLAY: none"
										title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="width: 100%;" id="busquedaDiv" align="center">
							<table style="width: 100%;">
								<tr>
									<td>
										<table>
											<tr>
												<td class="texto_ti"><spring:message
														code="formularioClienteConcepto.vacio" htmlEscape="true" />
												</td>
											</tr>
											<tr>
												<td class="texto_ti"><spring:message
														code="formularioClienteConcepto.habilitado"
														htmlEscape="true" />
												</td>
												<td class="texto_ti"><input type="checkBox"
													id="habilitado" name="habilitado" style="width: 10px;"
													value="true" checked="checked"
													<c:if test="${clienteConceptoFormulario.habilitado == 'true'}">
														checked="CHECKED"
													</c:if>
													<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if> />
												</td>
											</tr>
										</table>
									</td>
									<td class="texto_ti"><spring:message
											code="formularioClienteConcepto.cliente" htmlEscape="true" />
									</td>
									
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioClienteConcepto.vacio" htmlEscape="true" />
									</td>
									<td class="texto_ti"><input type="text" id="clienteCodigo" class="requerido"
										name="clienteCodigo" maxlength="6" style="width: 50px;"
										value="<c:out value="${clienteConceptoFormulario.cliente.codigo}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										&nbsp;&nbsp;
										<button type="button" 
											onclick="abrirPopup('clientesPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="clienteCodigoLabel"
										for="clienteCodigo"> <c:out
												value="${clienteConceptoFormulario.cliente.nombreYApellido}"
												default="" /> </label>
									</td>									
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioClienteConcepto.listaPrecio"
											htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioClienteConcepto.concepto" htmlEscape="true" />
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="listaPrecioCodigo" class="requerido"
										name="listaPrecioCodigo" maxlength="6" style="width: 50px;"
										value="<c:out value="${clienteConceptoFormulario.listaPrecios.codigo}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupListaPrecio('listaPreciosPopupMap', '<spring:message code="notif.clienteConcepto.seleccionCliente" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="listaPrecioCodigoLabel"
										for="listaPrecioCodigo"> <c:out
												value="${clienteConceptoFormulario.listaPrecios.descripcion}"
												default="" /> </label>
									</td>
									<td class="texto_ti"><input type="text" id="conceptoCodigo" class="requerido"
										name="conceptoCodigo" maxlength="6" style="width: 50px;"
										value="<c:out value="${clienteConceptoFormulario.concepto.codigo}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupConcepto('conceptosPopupMap', '<spring:message code="notif.clienteConcepto.seleccionListaPrecio" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="conceptoCodigoLabel"
										for="conceptoCodigo"> <c:out
												value="${clienteConceptoFormulario.concepto.descripcion}"
												default="" /> </label>
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
					<button name="guardar" type="button" onclick="guardarYSalir();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
					<button name="cancelar" type="button" onclick="volverCancelar();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accion == 'CONSULTA'}">
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="clientesPopupMap" />
		<jsp:param name="clase" value="clientesPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="listaPreciosPopupMap" />
		<jsp:param name="clase" value="listaPreciosPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="conceptosPopupMap" />
		<jsp:param name="clase" value="conceptosPopupMap" />
	</jsp:include>
</body>
</html>