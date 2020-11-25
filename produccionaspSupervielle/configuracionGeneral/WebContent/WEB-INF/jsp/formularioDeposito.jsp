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
		<spring:message code="formularioDeposito.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioDeposito.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioDeposito.titulo.modificar"
			htmlEscape="true" />
	</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_deposito.js"></script>
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
							<spring:message code="formularioDeposito.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioDeposito.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioDeposito.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarDeposito.html"
					commandName="depositoFormulario" method="post"
					modelAttribute="depositoFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${depositoFormulario.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioDeposito.datosDeposito" htmlEscape="true" />
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
												<spring:message code="formularioDeposito.nombreDeposito"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioDeposito.datosDeposito.empresa"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioDeposito.datosDeposito.sucursal"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioDeposito.datosDeposito.codigo"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioDeposito.datosDeposito.descripcion"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="codigoEmpresa"
														name="codigoEmpresa" maxlength="6" style="width: 50px;"
														value="<c:out value="${depositoFormulario.codigoEmpresa}" default="" />"
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
														</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
														for="codigoEmpresa"> <c:out
																value="${depositoFormulario.sucursal.empresa.descripcion}"
																default="" /> </label></td>
													<td class="texto_ti"><input type="text" id="codigoSucursal"
														name="codigoSucursal" maxlength="6" style="width: 50px;"
														value="<c:out value="${depositoFormulario.codigoSucursal}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
														&nbsp;&nbsp;
														<button type="button"
															onclick="abrirPopupSucursal('sucursalesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>'
																						, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
														for="codigoSucursal"> <c:out
																value="${depositoFormulario.sucursal.descripcion}"
																default="" /> </label></td>
													<td class="texto_ti"><input type="text" id="codigo" class="requerido"
														maxlength="2" name="codigo" style="width: 200px;"
														value="<fmt:numberComplete value="${depositoFormulario.codigo}" 
														length="2" valorDefualt="0"/>"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="descripcion" name="descripcion" style="width: 200px;"
														maxlength="100"
														value="<c:out value="${depositoFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioDeposito.datosDeposito.subTotal"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioDeposito.datosDeposito.subDisponible"
															htmlEscape="true" /></td>													
													<td class="texto_ti"><spring:message
															code="formularioDeposito.vacio" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="subTotal" 
														maxlength="8" name="subTotal" style="width: 170px;"
														value="<fmt:formatNumber value="${depositoFormulario.subTotal}" tipoValor="f"/>"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="subDisponible" maxlength="8" name="subDisponible"
														style="width: 170px;"
														value="<fmt:formatNumber value="${depositoFormulario.subDisponible}" tipoValor="f"/>" 
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>												
													<td>
														<table>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioDeposito.datosDeposito.depositoPropio"
																		htmlEscape="true" /></td>
																<td class="texto_ti"><input type="checkBox"
																	id="depositoPropio" name="depositoPropio"
																	style="width: 10px;" value="true"
																	<c:if test="${depositoFormulario.depositoPropio == 'true'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</table>
											<table style="width: 100%;">
												<tr>
													<td colspan="4" class="texto_ti"><spring:message
														code="formularioDeposito.datosDeposito.observaciones"
														htmlEscape="true" /></td>
												</tr>
												<tr>
													<td  colspan="4" class="texto_ti"><textarea id="observacion"
														name="observacion" style="width: 600px;" rows="3"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>>
														<c:out value="${depositoFormulario.observacion}"
																default="" />
													</textarea>
													</td>
												</tr>
											</table>
											<fieldset>
												<legend>
													<spring:message code="formularioDeposito.datosDeposito.direccion"
														htmlEscape="true" />
												</legend>
												<table>
													<tr>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.calle"
																htmlEscape="true" />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.numero"
																htmlEscape="true" />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.piso"
																htmlEscape="true" />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.dpto"
																htmlEscape="true" />
														</td>
													</tr>
													<tr>
														<td class="texto_ti"><input type="text" class="requerido"
															id="direccion.calle" name="direccion.calle"
															maxlength="30" style="width: 190px;"
															value="<c:out value="${depositoFormulario.direccion.calle}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
														</td>

														<td class="texto_ti"><input type="text" class="requerido"
															id="direccion.numero" name="direccion.numero"
															maxlength="6" style="width: 190px;"
															value="<c:out value="${depositoFormulario.direccion.numero}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
														</td>
														<td class="texto_ti"><input type="text"
															id="direccion.piso" name="direccion.piso" maxlength="4"
															style="width: 190px;"
															value="<c:out value="${depositoFormulario.direccion.piso}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
														</td>
														<td class="texto_ti"><input type="text"
															id="direccion.dpto" name="direccion.dpto" maxlength="4"
															style="width: 190px;"
															value="<c:out value="${depositoFormulario.direccion.dpto}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
														</td>
													</tr>
													<tr>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.pais"
																htmlEscape="true" />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.provincia"
																htmlEscape="true" />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.localidad"
																htmlEscape="true" />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioDeposito.datosDeposito.direccion.barrio"
																htmlEscape="true" />
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<input type="text" id="pais" name="pais" maxlength="30" style="width: 150px;" class="requerido"
																value="<c:out value="${depositoFormulario.direccion.barrio.localidad.provincia.pais.nombre}" default="" />" 
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
																	<c:out value="${depositoFormulario.direccion.barrio.localidad.provincia.pais.id}" default="" />
																</label>
															</div>
														</td>
														<td class="texto_ti">
															<input type="text" id="provincia" name="provincia" maxlength="30" style="width: 150px;" class="requerido"
																value="<c:out value="${depositoFormulario.direccion.barrio.localidad.provincia.nombre}" default="" />" 
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
																	<c:out value="${depositoFormulario.direccion.barrio.localidad.provincia.id}" default="" />
																</label>
															</div>
														</td>
														<td class="texto_ti">
															<input type="text" id="localidad" name="localidad" maxlength="30" style="width: 150px;" class="requerido"
																value="<c:out value="${depositoFormulario.direccion.barrio.localidad.nombre}" default="" />" 
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
																	<c:out value="${depositoFormulario.direccion.barrio.localidad.id}" default="" />
																</label>
															</div>
														</td>
														<td class="texto_ti">
															<input id="idBarrio" name="idBarrio" type="hidden" 
																value="<c:out value="${depositoFormulario.direccion.barrio.id}" default="" />"/>
															<input type="text" id="barrio" name="barrio" maxlength="30" style="width: 150px;" class="requerido"
																value="<c:out value="${depositoFormulario.direccion.barrio.nombre}" default="" />" 
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
																	<c:out value="${depositoFormulario.direccion.barrio.id}" default="" />
																</label>
															</div>
														</td>
													</tr>
												</table>
											</fieldset>
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