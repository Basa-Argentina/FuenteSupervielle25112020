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
		<spring:message code="formularioTransporte.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioTransporte.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioTransporte.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_transporte.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
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
							<spring:message code="formularioTransporte.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioTransporte.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioTransporte.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarTransporte.html"
					commandName="transporteFormulario" method="post"
					modelAttribute="transporteFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${transporteFormulario.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioTransporte.datosTransporte" htmlEscape="true" />
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
								<td></td>								
									<td class="texto_ti"><spring:message
															code="formularioTransporte.datosTransporte.codigo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioTransporte.datosTransporte.descripcion"
															htmlEscape="true" />
													</td>
								</tr>
								<tr>
									<td>
										<table>
											<tr>
											<td class="texto_ti" rowspan="1"><input type="checkBox" 
																	id="habilitado" name="habilitado" style="width: 10px;" 
																	value="true"
																	<c:if test="${transporteFormulario.habilitado == 'true' || accion=='NUEVO'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
											</td>
											<td class="texto_ti"><spring:message
																		code="formularioTransporte.datosTransporte.habilitado"
																		htmlEscape="true" />
																</td>
																
																
											</tr>
											</table>
											</td>
											<td class="texto_ti"><input type="text" id="codigo" class="requerido"
														name="codigo" style="width: 200px;" maxlength="3"
														value="<c:out value="${transporteFormulario.codigo}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
											</td>
											<td class="texto_ti"><input type="text" class="requerido" 
														id="descripcion" name="descripcion" style="width: 200px;" maxlength="100"
														value="<c:out value="${transporteFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
										    </td>
											<tr>
													
													<td class="texto_ti"><spring:message
															code="formularioTransporte.datosTransporte.empresa"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioTransporte.datosTransporte.patente"
															htmlEscape="true" />
													</td>													
													<td class="texto_ti"><spring:message
															code="formularioTransporte.datosTransporte.capacidad"
															htmlEscape="true" />
													</td>
												</tr>
											<tr>
												<td class="texto_ti">
													<input type="text" id="codigoEmpresa" class="requerido" 
														name="codigoEmpresa" maxlength="6" style="width: 50px;"
														value="<c:out value="${transporteFormulario.codigoEmpresa}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
														&nbsp;&nbsp;
													<button type="button"
														onclick="abrirPopup('empresasPopupMap');"
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
														<img src="<%=request.getContextPath()%>/images/buscar.png">
													</button>&nbsp;&nbsp; 
													<label id="codigoEmpresaLabel"
														for="codigoEmpresa"> <c:out
																value="${transporteFormulario.empresa.descripcion}"
																default="" /> 
													</label>	
												</td>																					
													<td class="texto_ti"><input type="text" id="patente"
														name="patente" style="width: 190px;"
														value="<c:out value="${transporteFormulario.patente}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="capacidad"
														name="capacidad" style="width: 190px;" maxlength="20"
														value="<c:out value="${transporteFormulario.capacidad}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>	
													</tr>											
											</table>
										</div></fieldset>
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
		<jsp:param name="mapa" value="empresasPopupMap" /> 
		<jsp:param name="clase" value="empresasPopupMap" /> 
	</jsp:include>
</body>
</html>