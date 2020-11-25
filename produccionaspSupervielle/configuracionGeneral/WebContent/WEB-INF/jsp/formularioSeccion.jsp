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
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioSeccion.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioSeccion.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioSeccion.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_seccion.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
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
							<spring:message code="formularioSeccion.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioSeccion.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioSeccion.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarSeccion.html"
					commandName="seccionFormulario" method="post"
					modelAttribute="seccionFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
						<input id="clienteId" type="hidden" value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${seccionFormulario.id}" default="" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioSeccion.datosSeccion" htmlEscape="true" />
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
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message code="formularioSeccion.nombreSeccion"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioStock.empresa"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioStock.sucursal"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSeccion.datosSeccion.deposito"
															htmlEscape="true" />
													</td>
													
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="codigoEmpresa"
														name="codigoEmpresa" maxlength="6" style="width: 50px;"
														value="<c:out value="${seccionFormulario.deposito.sucursal.empresa.codigo}" default="" />"/>
														&nbsp;&nbsp;
														<button type="button"
															onclick="abrirPopupEmpresa('empresasPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>">
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
														for="codigoEmpresa"> <c:out
																value="${seccionFormulario.deposito.sucursal.empresa.descripcion}"
																default="" /> </label>
													</td>
													<td class="texto_ti"><input type="text" id="codigoSucursal"
														name="codigoSucursal" maxlength="6" style="width: 50px;"
														value="<c:out value="${seccionFormulario.deposito.sucursal.codigo}" default="" />"/>
														&nbsp;&nbsp;
														<button type="button"
															onclick="abrirPopupSucursal('sucursalesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>'
																						, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>">
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
														for="codigoSucursal"> <c:out
																value="${seccionFormulario.deposito.sucursal.descripcion}"
																default="" /> </label>
													</td>
													<td class="texto_ti"><input type="text" id="codigoDeposito"
														name="codigoDeposito" maxlength="6" style="width: 50px;"
														value="<c:out value="${seccionFormulario.deposito.codigo}" default="" />"/>
														&nbsp;&nbsp;
														<button type="button"
															onclick="abrirPopupDeposito('depositosPopupMap', '<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>'
																						, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>">
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoDepositoLabel"
														for="codigoDeposito"> <c:out
																value="${seccionFormulario.deposito.descripcion}"
																default="" /> </label>
													</td>													
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioSeccion.datosSeccion.codigo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSeccion.datosSeccion.descripcion"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="codigo" class="requerido"
														maxlength="2" name="codigo" style="width: 200px;"
														value="<fmt:numberComplete value="${seccionFormulario.codigo}" 
														length="2" valorDefualt="0"/>"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="descripcion" name="descripcion" style="width: 200px;"
														maxlength="100"
														value="<c:out value="${seccionFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<c:if test="${accion != 'CONSULTA'}">
									<tr>
										<td class="texto_ti">
											<button name="restablecer" type="reset">
												<img
													src="<%=request.getContextPath()%>/images/restablecer.png"
													title=<spring:message code="botones.restablecer" htmlEscape="true"/>>
											</button>
										</td>										
									</tr>
								</c:if>
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
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="depositosPopupMap" />
		<jsp:param name="clase" value="depositosPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="sucursalesPopupMap" /> 
		<jsp:param name="clase" value="sucursalesPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="empresasPopupMap" /> 
		<jsp:param name="clase" value="empresasPopupMap" /> 
	</jsp:include>
</body>
</html>