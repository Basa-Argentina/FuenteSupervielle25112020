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
		<spring:message code="formularioSucursal.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioSucursal.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioSucursal.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_sucursal.js"></script>
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
							<spring:message code="formularioSucursal.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioSucursal.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioSucursal.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarSucursal.html"
					commandName="sucursalFormulario" method="post"
					modelAttribute="sucursalFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${sucursalFormulario.id}" default="" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioSucursal.datosSucursal" htmlEscape="true" />
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
												<spring:message code="formularioSucursal.nombreSucursal"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.empresa"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.codigo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.descripcion"
															htmlEscape="true" />
													</td>													
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.telefono"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<select id="idEmpresa" class="requerido"
															name="idEmpresa" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<c:forEach items="${empresas}" var="empresa">
																	<option value="${empresa.id}"
																		<c:if test="${empresa.id == sucursalFormulario.empresa.id}">
																		selected="selected"
																	</c:if>>
																		<c:out value="${empresa.descripcion}" />
																	</option>
																</c:forEach>
														</select>
													</td>
													<td class="texto_ti"><input type="text" id="codigo" class="requerido"
														name="codigo" style="width: 200px;" maxlength="4"
														value="<c:out value="${sucursalFormulario.codigo}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido" 
														id="descripcion" name="descripcion" style="width: 200px;" maxlength="100"
														value="<c:out value="${sucursalFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>													
													<td class="texto_ti"><input type="text" id="telefono"
														name="telefono" style="width: 190px;" maxlength="20"
														value="<c:out value="${sucursalFormulario.telefono}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
												<tr>													
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.mail"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.vacio" htmlEscape="true" />
													</td>
												</tr>
												<tr>													
													<td class="texto_ti"><input type="text" id="mail"
														name="mail" style="width: 190px;"
														value="<c:out value="${sucursalFormulario.mail}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td>
														<table>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioSucursal.datosSucursal.principal"
																		htmlEscape="true" />
																</td>
																<td class="texto_ti"><input type="checkBox"
																	id="principal" name="principal" style="width: 10px;" 
																	value="true"
																	<c:if test="${sucursalFormulario.principal == 'true'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
																</td>
															</tr>
														</table></td>
												</tr>												
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="4">
										<fieldset>
											<legend>
												<spring:message
													code="formularioSucursal.datosSucursal.direccion"
													htmlEscape="true" />
											</legend>
											<table>												
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.direccion.calle"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.direccion.numero"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.direccion.piso"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
														code="formularioSucursal.datosSucursal.direccion.dpto"
														htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" class="requerido"
														id="direccion.calle" name="direccion.calle" maxlength="30"
														style="width: 190px;"
														value="<c:out value="${sucursalFormulario.direccion.calle}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													
													<td class="texto_ti"><input type="text" class="requerido"
														id="direccion.numero" name="direccion.numero" maxlength="6"
														style="width: 190px;"
														value="<c:out value="${sucursalFormulario.direccion.numero}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="direccion.piso" name="direccion.piso" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${sucursalFormulario.direccion.piso}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="direccion.dpto" name="direccion.dpto" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${sucursalFormulario.direccion.dpto}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.direccion.pais"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.direccion.provincia"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.direccion.localidad"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSucursal.datosSucursal.direccion.barrio"
															htmlEscape="true" />
													</td>													
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="pais" name="pais" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${sucursalFormulario.direccion.barrio.localidad.provincia.pais.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopup('paisPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="paisLabel" for="pais">
																<c:out value="${sucursalFormulario.direccion.barrio.localidad.provincia.pais.id}" default="" />
															</label>
														</div>		
													</td>
													<td class="texto_ti">
														<input type="text" id="provincia" name="provincia" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${sucursalFormulario.direccion.barrio.localidad.provincia.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopupProvincia('provinciaPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="provinciaLabel" for="provincia">
																<c:out value="${sucursalFormulario.direccion.barrio.localidad.provincia.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input type="text" id="localidad" name="localidad" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${sucursalFormulario.direccion.barrio.localidad.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopupLocalidad('localidadPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="localidadLabel" for="localidad">
																<c:out value="${sucursalFormulario.direccion.barrio.localidad.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input id="idBarrio" name="idBarrio" type="hidden" 
															value="<c:out value="${sucursalFormulario.direccion.barrio.id}" default="" />"/>
														<input type="text" id="barrio" name="barrio" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${sucursalFormulario.direccion.barrio.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopupBarrio('barrioPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="barrioLabel" for="barrio">
																<c:out value="${sucursalFormulario.direccion.barrio.id}" default="" />
															</label>
														</div>
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
	<div id="darkLayer" class="darkClassWithoutHeight" style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="paisPopupMap" /> 
		<jsp:param name="clase" value="paisPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="provinciaPopupMap" /> 
		<jsp:param name="clase" value="provinciaPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="localidadPopupMap" /> 
		<jsp:param name="clase" value="localidadPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="barrioPopupMap" /> 
		<jsp:param name="clase" value="barrioPopupMap" /> 
	</jsp:include>
</body>
</html>