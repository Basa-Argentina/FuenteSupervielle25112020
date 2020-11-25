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
		<spring:message code="formularioDireccion.tituloFormAgregar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> (<spring:message code="general.ambiente"
			htmlEscape="true" />)
			</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioDireccion.tituloFormModificar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> (<spring:message code="general.ambiente"
			htmlEscape="true" />)
			</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioDireccion.tituloFormConsultar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> (<spring:message code="general.ambiente"
			htmlEscape="true" />)
			</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_direccion.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas.js"></script>
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
							<spring:message code="formularioDireccion.tituloFormAgregar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioDireccion.tituloFormModificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioDireccion.tituloFormConsultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarDireccion.html"
					commandName="direccionFormulario" method="post"
					modelAttribute="direccionFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${direccionFormulario.id}" default="" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioDireccion.datosDireccion" htmlEscape="true" />
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

									<td class="texto_ti"><spring:message
											code="formularioDireccion.calle" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.numero" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.piso" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.dpto" htmlEscape="true" /></td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="calle"
										name="calle" style="width: 200px;" maxlength="30"
										value="<c:out value="${direccionFormulario.calle}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										<font class="error">*</font></td>
									<td class="texto_ti"><input type="text" id="numero"
										name="numero" style="width: 200px;" maxlength="6"
										value="<c:out value="${direccionFormulario.numero}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										<font class="error">*</font></td>
									<td class="texto_ti"><input type="text" id="piso"
										name="piso" style="width: 200px;" maxlength="4"
										value="<c:out value="${direccionFormulario.piso}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
									</td>
									<td class="texto_ti"><input type="text" id="dpto"
										name="dpto" style="width: 200px;" maxlength="6"
										value="<c:out value="${direccionFormulario.dpto}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.pais" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.provincia" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.localidad" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.barrio" htmlEscape="true" /></td>
								</tr>
								<tr>
									<td class="texto_ti"><select id="pais" name="pais"
										size="1" style="width: 200px;"
										<c:if test="${accion == 'CONSULTA'}">
											disabled="disabled"
										</c:if>>
											<c:if test="${accion != 'NUEVO'}">
												<c:forEach items="${paises}" var="p">
													<option value="${p.id}"
														<c:if test="${direccionFormulario.barrio.localidad.provincia.pais.id == p.id }">
														selected="selected"
													</c:if>>
														<c:out value="${p.nombre}" />
													</option>
												</c:forEach>
											</c:if>
											<c:if test="${accion == 'NUEVO'}">
												<c:forEach items="${paises}" var="p">
													<option value="${p.id}">
														<c:out value="${p.nombre}" />
													</option>
												</c:forEach>
											</c:if>
									</select></td>
									<td class="texto_ti"><select id="provincia"
										name="provincia" size="1" style="width: 190px;"
										<c:if test="${accion == 'CONSULTA'}">
											disabled="disabled"
										</c:if>>
											<c:if test="${accion != 'NUEVO'}">
												<c:forEach
													items="${direccionFormulario.barrio.localidad.provincia.pais.provincias}"
													var="p">
													<option value="${p.id}"
														<c:if test="${direccionFormulario.barrio.localidad.provincia.id == p.id}">
														selected="selected"
													</c:if>>
														<c:out value="${p.nombre}" />
													</option>
												</c:forEach>
											</c:if>
									</select></td>
									<td class="texto_ti"><select id="localidad"
										name="localidad" size="1" style="width: 190px;"
										<c:if test="${accion == 'CONSULTA'}">
											disabled="disabled"
										</c:if>>
											<c:if test="${accion != 'NUEVO'}">
												<c:forEach
													items="${direccionFormulario.barrio.localidad.provincia.localidades}"
													var="l">
													<option value="${l.id}"
														<c:if test="${direccionFormulario.barrio.localidad.id == l.id}">
														selected="selected"
													</c:if>>
														<c:out value="${l.nombre}" />
													</option>
												</c:forEach>
											</c:if>
									</select></td>
									<td class="texto_ti"><select id="idBarrio" name="idBarrio"
										size="1" style="width: 200px;"
										<c:if test="${accion == 'CONSULTA'}">
											disabled="disabled"
										</c:if>>
											<c:if test="${accion != 'NUEVO'}">
												<c:forEach
													items="${direccionFormulario.barrio.localidad.barrios}"
													var="b">
													<option value="${b.id}"
														<c:if test="${direccionFormulario.barrio.id == b.id}">
														selected="selected"
													</c:if>>
														<c:out value="${b.nombre}" />
													</option>
												</c:forEach>
											</c:if>
									</select> <font class="error">*</font></td>
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.edificio" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioDireccion.observaciones" htmlEscape="true" />
									</td>
								</tr>
								<tr style="vertical-align: top;">
									<td class="texto_ti"><input type="text" id="edificio"
										name="edificio" style="width: 200px;" maxlength="30"
										value="<c:out value="${direccionFormulario.edificio}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
									</td>
									<td class="texto_ti"><textarea id="observaciones"
											name="observaciones" style="width: 200px;"
											<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if>>
											<c:out value="${direccionFormulario.observaciones}"
												default="" />
										</textarea></td>
								</tr>
								<tr>
									<td colspan="4">
										<fieldset>
											<legend>
												<spring:message code="formularioDireccion.geolocalizacion"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioDireccion.latitud" htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioDireccion.longitud" htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="latitud"
														name="latitud" style="width: 200px;"
														value="<c:out value="${direccionFormulario.latitud}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="longitud"
														name="longitud" style="width: 200px;"
														value="<c:out value="${direccionFormulario.longitud}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
													</td>
												</tr>
											</table>
										</fieldset></td>
								</tr>
								<c:if test="${accion != 'CONSULTA'}">
									<tr>
										<td class="texto_ti">
											<button name="restablecer" type="reset">
												<img
													src="<%=request.getContextPath()%>/images/restablecer.png"
													title=<spring:message code="botones.restablecer" htmlEscape="true"/>>
											</button></td>
										<td align="left" class="error"><spring:message
												code="textos.requerido" htmlEscape="true" /></td>
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
	<div id="darkLayer" class="darkClass">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
</body>
</html>