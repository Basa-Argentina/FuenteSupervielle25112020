<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page errorPage="/error.html"%>
<%@ page buffer = "16kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioEmpleado.tituloFormAgregar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> <spring:message code="general.ambiente"
			htmlEscape="true" />
			</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioEmpleado.tituloFormModificar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> <spring:message code="general.ambiente"
			htmlEscape="true" />
			</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioEmpleado.tituloFormConsultar"
			htmlEscape="true" /> - <spring:message code="general.empresa"
			htmlEscape="true" /> <spring:message code="general.ambiente"
			htmlEscape="true" />
			</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_empleado.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas.js"></script>
<script type="text/javascript" src="js/Utils.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>

</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							<spring:message code="formularioEmpleado.tituloFormAgregar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioEmpleado.tituloFormModificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioEmpleado.tituloFormConsultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarEmpleado.html"
					commandName="empleadoForm" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="rolesUsuarioSeleccionados"
						name="rolesUsuarioSeleccionados" />
					<c:if test="${accion != 'NUEVO'}">
						<input type="hidden" id="id" name="id"
							value="<c:out value="${empleadoFormulario.id}" default=""/>" />
					</c:if>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="textos.datosCaracteristicos" htmlEscape="true" /> </font> <img
										id="busquedaImgSrcDown" src="images/skip_down.png"
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
											code="formularioEmpleado.datosEmpleado.cliente"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioEmpleado.datosEmpleado.codigo"
											htmlEscape="true" /></td>
									
																		
								</tr>
								<tr>
									<td>
										<table>
											<tr>
												<td class="texto_ti"><spring:message
														code="formularioEmpleado.datosEmpleado.habilitado"
														htmlEscape="true" />
												</td>
												<td class="texto_ti"><input type="checkBox"
													id="habilitado" name="habilitado" style="width: 10px;"
													value="true"
													<c:if test="${empleadoFormulario.habilitado == 'true'}">
															checked="CHECKED"
														</c:if>
													<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if> />
												</td>
											</tr>
										</table></td>
									<td class="texto_ti"><input type="text" id="clienteCodigo" class="requerido"
										name="clienteCodigo" maxlength="6" style="width: 50px;"
										value="<c:out value="${empleadoFormulario.clienteEmp.codigo}" default="" />"
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
												value="${empleadoFormulario.clienteEmp.nombreYApellido}"
												default="" /> </label>
									</td>
									<td class="texto_ti"><input type="text" id="codigo" class="requerido"
										name="codigo" maxlength="6" style="width: 200px;"
										class="descripcionCorta"
										value="<c:out value="${empleadoFormulario.codigo}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if> />
									</td>
									
															
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioEmpleado.datosEmpleado.persona"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.firstName" htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.surName" htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.tipoDocumento" htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.nroDocumento" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" class="requerido"
														id="persona.nombre" name="persona.nombre" maxlength="100"
														style="width: 200px;" class="descripcionCorta"
														value="<c:out value="${empleadoFormulario.persona.nombre}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" maxlength="60" class="requerido"
														id="persona.apellido" name="persona.apellido"
														style="width: 200px;" class="descripcionCorta"
														value="<c:out value="${empleadoFormulario.persona.apellido}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti">
														<select id="idTipoDoc" class="requerido"
															name="idTipoDoc" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if>>
															<c:forEach items="${tiposDocumento}" var="tipo">
																<option value="${tipo.id}"
																	<c:if test="${tipo.id == tipoDocumentoDefecto.id}">
																			selected="selected"
																		</c:if>>
																	<c:out value="${tipo.nombre}" />
																</option>
															</c:forEach>
														</select>
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="persona.numeroDoc" name="persona.numeroDoc" maxlength="15"
														style="width: 200px;" class="descripcionLarga"
														value="<c:out value="${empleadoFormulario.persona.numeroDoc}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioEmpleado.datosEmpleado.contacto"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.telefono" htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.interno"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.celular"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.fax" htmlEscape="true" />
													</td>
												</tr>
												<tr>
													
													<td class="texto_ti"><input type="text"
														id="persona.telefono" name="persona.telefono" maxlength="30"
														style="width: 200px;" class="descripcionLarga"
														value="<c:out value="${empleadoFormulario.persona.telefono}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="interno"
														name="interno" maxlength="5" style="width: 200px;"
														class="descripcionLarga"
														value="<c:out value="${empleadoFormulario.interno}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> /></td>
													<td class="texto_ti"><input type="text" id="celular"
														name="celular" maxlength="30" style="width: 200px;"
														class="descripcionLarga"
														value="<c:out value="${empleadoFormulario.celular}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="fax"
														name="fax" maxlength="30" style="width: 200px;"
														class="descripcionLarga"
														value="<c:out value="${empleadoFormulario.fax}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioEmpleado.datosEmpleado.usuario"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.username" htmlEscape="true" /></td>
													<c:if test="${accion != 'CONSULTA'}">
														<td class="texto_ti"><spring:message
																code="formularioEmpleado.password" htmlEscape="true" /></td>
														<td class="texto_ti"><spring:message
																code="formularioEmpleado.confirmarPassword"
																htmlEscape="true" /></td>
													</c:if>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.eMail" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="username" maxlength="60" class="requerido"
														name="username" style="width: 200px;" class="descripcionCorta"
														<c:if test="${accion != 'NUEVO'}">
															value="<c:out value="${empleadoFormulario.usernameSinCliente}" default="" />"
															readonly="readonly"
														</c:if> />
													</td>
													<c:if test="${accion != 'CONSULTA'}">
														<td class="texto_ti"><input type="password" id="password" 
															name="password" style="width: 200px;"
															<c:if test="${accion == 'MODIFICACION'}">
																			value=""
																		</c:if>
															<c:if test="${accion == 'NUEVO'}">
																class="descripcionCorta, requerido"
															</c:if>/>
														</td>
														<td class="texto_ti"><input type="password"
															id="confirmarContrasenia" name="confirmarContrasenia"
															style="width: 200px;"
															<c:if test="${accion == 'NUEVO'}">
																class="descripcionCorta, requerido"
															</c:if>/>
														</td>
														
													</c:if>
													<td class="texto_ti"><input type="text" id="persona.mail"
														name="persona.mail" maxlength="60" style="width: 200px;"
														class="requerido" class="descripcionLarga"  
														value="<c:out value="${empleadoFormulario.persona.mail}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
							
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioEmpleado.datosEmpleado.direccion"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.calle"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.numero"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.piso"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.dpto"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" class="requerido"
														id="persona.direccion.calle" name="persona.direccion.calle" maxlength="30"
														style="width: 190px;"
														value="<c:out value="${empleadoFormulario.persona.direccion.calle}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>

													<td class="texto_ti"><input type="text" class="requerido"
														id="persona.direccion.numero" name="persona.direccion.numero"
														maxlength="6" style="width: 190px;"
														value="<c:out value="${empleadoFormulario.persona.direccion.numero}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="persona.direccion.piso" name="persona.direccion.piso" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${empleadoFormulario.persona.direccion.piso}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="persona.direccion.dpto" name="persona.direccion.dpto" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${empleadoFormulario.persona.direccion.dpto}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.pais"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.provincia"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.localidad"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioEmpleado.datosEmpleado.direccion.barrio"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti">													
														<input type="text" id="pais" name="pais" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${paisDefecto.nombre}" default="" />" 
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
																<c:out value="${paisDefecto.id}" default="" />
															</label>
														</div>																					
													</td>
													<td class="texto_ti">
														<input type="text" id="provincia" name="provincia" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${empleadoFormulario.persona.direccion.barrio.localidad.provincia.nombre}" default="" />" 
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
																<c:out value="${empleadoFormulario.persona.direccion.barrio.localidad.provincia.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input type="text" id="localidad" name="localidad" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${empleadoFormulario.persona.direccion.barrio.localidad.nombre}" default="" />" 
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
																<c:out value="${empleadoFormulario.persona.direccion.barrio.localidad.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input id="idBarrio" name="idBarrio" type="hidden" 
															value="<c:out value="${empleadoFormulario.persona.direccion.barrio.id}" default="" />"/>
														<input type="text" id="barrio" name="barrio" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${empleadoFormulario.persona.direccion.barrio.nombre}" default="" />" 
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
																<c:out value="${empleadoFormulario.persona.direccion.barrio.id}" default="" />
															</label>
														</div>
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioEmpleado.datosEmpleado.direccionEntregaDefecto"
													htmlEscape="true" />
											</legend>
											<table style="width: 100%;">
												<tr style="width: 95%;">
													<td class="texto_ti" style="width: 100%;"><spring:message
															code="formularioEmpleado.datosEmpleado.direccionDefecto" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="codigoDireccion"
															name="codigoDireccion" maxlength="6" style="width: 50px;"
															value="<c:out value="${empleadoFormulario.direccionDefecto.codigo}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																			readonly="readonly"
																		</c:if> />
															&nbsp;&nbsp;
															<button type="button"
																onclick="abrirPopupDireccion('direccionesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>'
																							, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if>>
																<img src="<%=request.getContextPath()%>/images/buscar.png">
															</button>&nbsp;&nbsp; <label id="codigoDireccionLabel"
															for="codigoDireccion"> <c:out
																	value="${empleadoFormulario.direccionDefecto.descripcion}"
																	default="" />
														</label>
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioEmpleado.datosEmpleado.adicionales"
													htmlEscape="true" />
											</legend>
											<table style="width: 100%;">
												<tr style="width: 95%;">
													<td class="texto_ti" style="width: 100%;"><spring:message
															code="formularioEmpleado.datosEmpleado.observaciones" htmlEscape="true" /></td>
												</tr>
												<tr>
													
													<td style="width: 95%;">
														<textarea id="observaciones" name="observaciones"  rows="5" cols="110" 
							       							<c:if test="${accion == 'CONSULTA'}">
							       								readonly="readonly"
							       							</c:if>
							       						><c:out value="${empleadoFormulario.observaciones}"/></textarea>	
													</td>
												</tr>
											</table>
										</fieldset>
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
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
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
		<jsp:param name="mapa" value="clientesPopupMap" />
		<jsp:param name="clase" value="clientesPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="direccionesPopupMap" />
		<jsp:param name="clase" value="direccionesPopupMap" />
	</jsp:include>
	
</body>
</html>