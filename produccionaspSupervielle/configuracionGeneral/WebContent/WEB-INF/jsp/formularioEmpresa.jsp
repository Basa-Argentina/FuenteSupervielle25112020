<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioEmpresa.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioEmpresa.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioEmpresa.titulo.modificar"
			htmlEscape="true" />
	</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/mavalos_formulario_empresa.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/Utils.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.chromatable.js"></script>
<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
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
							<spring:message code="formularioEmpresa.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioEmpresa.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioEmpresa.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarEmpresa.html"
					commandName="empresaFormulario" method="post"
					modelAttribute="empresaFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${empresaFormulario.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioEmpresa.datosEmpresa" htmlEscape="true" />
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
												<spring:message code="formularioEmpresa.nombreEmpresa"
													htmlEscape="true" />
											</legend>
											<table>
											<tr>
													<td>
														<table>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioEmpresa.datosEmpresa.principal"
																		htmlEscape="true" /></td>
																<td class="texto_ti"><input type="checkBox"
																	id="principal" name="principal" style="width: 10px;"
																	value="true"
																	<c:if test="${empresaFormulario.principal == 'true'}">
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
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.codigo"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.descripcion"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.razonSocial.razonSocial"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.telefono"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="codigo" class="requerido"
														name="codigo" style="width: 200px;" maxlength="4" onblur="formato(4);"
														value="<c:out value="${empresaFormulario.codigo}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="descripcion" name="descripcion" style="width: 200px;" maxlength="255"
														value="<c:out value="${empresaFormulario.descripcion}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="razonSocial.razonSocial"
														name="razonSocial.razonSocial" style="width: 200px;"
														value="<c:out value="${empresaFormulario.razonSocial.razonSocial}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="telefono" maxlength="20"
														name="telefono" style="width: 190px;"
														value="<c:out value="${empresaFormulario.telefono}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.iva"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.tipoDocumento"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.numeroDocumento"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.mail"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><select id="idAfipCondIva"
														name="idAfipCondIva" size="1" style="width: 190px;"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>>
															<c:forEach items="${afipCondIvas}" var="iva">
																<option value="${iva.id}"
																	<c:if test="${iva.id == empresaFormulario.afipCondIva.id}">
																	selected="selected"
																</c:if>>
																	<c:out value="${iva.descripcion}" />
																</option>
															</c:forEach>
													</select></td>
													<td class="texto_ti">
														<select id="idTipoDocSel" class="requerido"
															name="idTipoDocSel" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<c:forEach items="${tiposDocumento}" var="tipo">
																	<option value="${tipo.id}"
																		<c:if test="${tipo.id == empresaFormulario.tipoDoc.id}">
																		selected="selected"
																	</c:if>>
																		<c:out value="${tipo.nombre}" />
																	</option>
																</c:forEach>
														</select>
													</td>
													<td class="texto_ti"><input type="text" id="numeroDoc" class="requerido"
														name="numeroDoc" style="width: 190px;" maxlength="15"
														value="<c:out value="${empresaFormulario.numeroDoc}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="mail"
														name="mail" style="width: 190px;"
														value="<c:out value="${empresaFormulario.mail}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.vacio" htmlEscape="true" /></td>
												</tr>
												
											</table>
										</fieldset></td>
								</tr>
								<tr>
									<td colspan="4">
										<fieldset>
											<legend>
												<spring:message
													code="formularioEmpresa.datosEmpresa.direccion"
													htmlEscape="true" />
											</legend>
											<table>												
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.calle"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.numero"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.piso"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.dpto"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" class="requerido"
														id="direccion.calle" name="direccion.calle" maxlength="30"
														style="width: 190px;"
														value="<c:out value="${empresaFormulario.direccion.calle}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>

													<td class="texto_ti"><input type="text" class="requerido"
														id="direccion.numero" name="direccion.numero" maxlength="6"
														style="width: 190px;"
														value="<c:out value="${empresaFormulario.direccion.numero}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="direccion.piso" name="direccion.piso" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${empresaFormulario.direccion.piso}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="direccion.dpto" name="direccion.dpto" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${empresaFormulario.direccion.dpto}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.pais"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.provincia"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.localidad"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.direccion.barrio"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti">													
														<input type="text" id="pais" name="pais" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${empresaFormulario.direccion.barrio.localidad.provincia.pais.nombre}" default="" />" 
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
																<c:out value="${empresaFormulario.direccion.barrio.localidad.provincia.pais.id}" default="" />
															</label>
														</div>																					
													</td>
													<td class="texto_ti">
														<input type="text" id="provincia" name="provincia" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${empresaFormulario.direccion.barrio.localidad.provincia.nombre}" default="" />" 
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
																<c:out value="${empresaFormulario.direccion.barrio.localidad.provincia.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input type="text" id="localidad" name="localidad" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${empresaFormulario.direccion.barrio.localidad.nombre}" default="" />" 
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
																<c:out value="${empresaFormulario.direccion.barrio.localidad.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input id="idBarrio" name="idBarrio" type="hidden" 
															value="<c:out value="${empresaFormulario.direccion.barrio.id}" default="" />"/>
														<input type="text" id="barrio" name="barrio" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${empresaFormulario.direccion.barrio.nombre}" default="" />" 
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
																<c:out value="${empresaFormulario.direccion.barrio.id}" default="" />
															</label>
														</div>
													</td>
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
													code="formularioEmpresa.datosEmpresa.series"
													htmlEscape="true" />
											</legend>
											<table style="width: 100%;">
											    <tr >
											    
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.series.respInscriptos"
															htmlEscape="true" /></td>
													
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.series.otros"
															htmlEscape="true" /></td>
												
												</tr>
												<tr>
												
													<td class="texto_ti" style="width: 50%">
													
														<input type="text" id="codigoSerie1" name="codigoSerie1" maxlength="30" 
														style="width: 50%;"
																
																	value="<c:out value="${empresaFormulario.serie1.codigo}" default="" />"
																															 
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
															<button type="button" onclick="abrirPopup('seriesPopupMap');"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
															</button>&nbsp;&nbsp;
															
															<label id="codigoSerie1Label" "for="codigoSerie1" style=" width: 50%">
																	<c:out value="${empresaFormulario.serie1.descripcion}" default="" />			
															</label>
															
														
													</td>
													
													<td class="texto_ti"  style="width: 50%">
													
														<input type="text" id="codigoSerie2" name="codigoSerie2" maxlength="30" 
																style="width: 50%" 
																value="<c:out value="${empresaFormulario.serie2.codigo}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
															<button type="button" onclick="abrirPopup('seriesPopupMap2');"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
															</button>&nbsp;&nbsp;
															
															<label id="codigoSerie2Label" for="codigoSerie2">
																	<c:out value="${empresaFormulario.serie2.descripcion}" default="" />			
															</label>
															
												
													</td>
													
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
													code="formularioEmpresa.datosEmpresa.datos"
													htmlEscape="true" />
											</legend>
											<table style="width: 100%;">
											    <tr >
											    
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.ingresosBrutos"
															htmlEscape="true" /></td>
													
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.numeroEstablecimiento"
															htmlEscape="true" /></td>
												
												</tr>
												<tr>
												
													<td class="texto_ti" style="width: 50%">
														<input type="text" id="ingresosBrutos" name="ingresosBrutos" maxlength="30" 
														style="width: 200px;"														
																	value="<c:out value="${empresaFormulario.ingresosBrutos}" default="" />"											 
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>>
													</td>
													<td class="texto_ti"  style="width: 50%">
														<input type="text" id="numeroEstablecimiento" name="numeroEstablecimiento" maxlength="30" 
																style="width: 200px;"
																value="<c:out value="${empresaFormulario.numeroEstablecimiento}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>>	
													</td>	
												</tr>
												<tr >
											    
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.sedeTimbrado"
															htmlEscape="true" /></td>
													
													<td class="texto_ti"><spring:message
															code="formularioEmpresa.datosEmpresa.fechaInicioActividad"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti" style="width: 50%">
														<input type="text" id="sedeTimbrado" name="sedeTimbrado" maxlength="30" 
														style="width: 200px;"
																	value="<c:out value="${empresaFormulario.sedeTimbrado}" default="" />"															 
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>>
													</td>
													<td class="texto_ti"  style="width: 50%">
														<input type="text" id="fechaInicioActividad" name="fechaInicioActividad" 
															maxlength="10" style="width: 200px;"
															value="<c:out value="${empresaFormulario.fechaInicioActividadStr}" default="" />" />
															
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'empresaFormulario',
																	// input name
																	'controlname': 'fechaInicioActividad'
																});
															</script></c:if>
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
		<jsp:param name="mapa" value="seriesPopupMap" /> 
		<jsp:param name="clase" value="seriesPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="seriesPopupMap2" /> 
		<jsp:param name="clase" value="seriesPopupMap2" /> 
	</jsp:include>
</body>
</html>