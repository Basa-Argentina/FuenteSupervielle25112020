<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioLectura.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioLectura.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioLectura.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_lectura.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
.error {
	color: #ff0000;
}
 
.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
	    <ul>	 
	      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>
	      <c:if test="${accion == 'MODIFICACION'}">	 
	      	<li id="modificar" ><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
	      	<li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
	      </c:if>
	    </ul> 	 
  	</div> 
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							<spring:message code="formularioLectura.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioLectura.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioLectura.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarLectura.html"
					commandName="lecturaFormulario" method="post"
					name="lecturaFormulario" modelAttribute="lecturaFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${lecturaFormulario.id}" default="" />" />
					<input type="hidden" id="cantLecturaDetalles"
						name="cantLecturaDetalles"
						value="<c:out value="${cantLecturaDetalles}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioLectura.datosLectura" htmlEscape="true" />
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
												<spring:message code="formularioLectura.nombreLectura"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioLectura.datosLectura.descripcion"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioLectura.datosLectura.fecha"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text"
														id="descripcion" maxlength="30" name="descripcion"
														style="width: 230px;" class="requerido"
														value="<c:out value="${lecturaFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="fecha"
														name="fecha" maxlength="10" class="requerido"
														value="<c:out value="${lecturaFormulario.fechaStr}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> /> 
															<c:if test="${accion != 'CONSULTA'}">
															<script type="text/javascript">
																new tcal ({
																	// form name
																	'formname': 'lecturaFormulario',
																	// input name
																	'controlname': 'fecha'
																});
															</script>
														</c:if></td>
														
												</tr>
											</table>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioLectura.datosLectura.observacion"
															htmlEscape="true" /></td>
													
													
												</tr>
												<tr>
													<td class="texto_ti"><textarea id="observacion"
															name="observacion" style="width: 445px;"
															<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if>>
															<c:out value="${lecturaFormulario.observacion}"
																default="" />
														</textarea></td>
												</tr>
												<c:if test="${accion == 'MODIFICACION'}">
												<tr><td>
														<table>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioLectura.datosLectura.utlizada"
																		htmlEscape="true" /></td>
																<td class="texto_ti"><input type="checkbox"
																	name="utilizada" id="utilizada" <c:if test="${lecturaFormulario.utilizada == 'true'}">
																checked="CHECKED"
																</c:if>"/>
																</td>
															</tr>
														</table></td>
													</tr>
												</c:if>
											</table>
										</fieldset></td>
								</tr>

							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<fieldset>
					<legend>
						<spring:message code="formularioLectura.lecturaDetalle"
							htmlEscape="true" />
					</legend>
					<display:table name="lecturaDetalles" id="detalles" requestURI="mostrarLecturaDetalle.html"
						pagesize="10" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${lecturaDetalle.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column property="codigoBarras"
							titleKey="formularioLectura.lecturaDetalle.elemento"
							sortable="true" class="celdaAlineadoIzquierda" />
						<display:column property="observacion"
							titleKey="formularioLectura.datosLectura.observacion" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="elemento.tipoElemento.descripcionStr"
							titleKey="formularioLectura.lecturaDetalle.tipo" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column
							property="elemento.clienteEmp.razonSocialONombreYApellido"
							titleKey="formularioLectura.lecturaDetalle.cliente"
							sortable="true" class="celdaAlineadoIzquierda" />
					</display:table>
				</fieldset>
				<br style="font-size: xx-small;" />
				<c:if test="${accion != 'CONSULTA'}">
				<fieldset>
					<legend>
						<spring:message
							code="formularioLectura.lecturaDetalle.importarArchivo"
							htmlEscape="true" />
					</legend>
					<table>
						<tr>
							<td class="texto_ti"><spring:message
									code="formularioLectura.lecturaDetalle.archivo"
									htmlEscape="true" />
							</td>
						</tr>
						<tr>
							<td><form:form method="POST" commandName="fileUploadForm"
									enctype="multipart/form-data" name="fileUploadForm"
									action="importarLecturaDetalle.html">
									<table>
											<tr>
												<td><input type="hidden" id="accion" name="accion"
													value="<c:out value="${accion}" default="" />" /> <input
													type="hidden" id="descripcion2" name="descripcion2"
													style="width: 190px;" class="requerido"
													value="<c:out value="${lecturaFormulario.descripcion}" default="" />" />
													<input type="hidden" id="observacion2" name="observacion2"
													style="width: 190px;" class="requerido"
													value="<c:out value="${lecturaFormulario.observacion}" default="" />" />
													<input type="hidden" id="fecha2" name="fecha2"
													maxlength="10"
													value="<c:out value="${lecturaFormulario.fechaStr}" default="" />" />
													<form:errors path="*" cssClass="errorblock" element="div" />
												</td>
												<td class="texto_td"><input type="file" name="file"
													class="busqueda" style="height: 22px;" /></td>
												<td class="texto_td"><input type="checkbox" id="anexar"
													name="anexar" checked="checked"
													<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if> />
												</td>
												<td class="texto_td"><spring:message
														code="formularioLectura.lecturaDetalle.anexar"
														htmlEscape="true" />
												</td>
												<td class="texto_td"><input type="button"
													onclick="pasar();pregunta();" name="Importar"
													class="botonCentrado" value="Importar"> <span><form:errors
															path="file" cssClass="error" /> </span>
												</td>
											</tr>
										</table>
								</form:form></td>
						</tr>
					</table>
				</fieldset>
				</c:if>
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
</body>
</html>