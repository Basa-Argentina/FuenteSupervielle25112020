<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioCai.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioCai.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioCai.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_cai.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas.js"></script>
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
							<spring:message code="formularioCai.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioCai.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioCai.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formCai" action="guardarActualizarCai.html"
					commandName="caiFormulario" method="post"
					modelAttribute="caiFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="accionSerie" name="accionSerie"
						value="<c:out value="${accionSerie}" default="" />" />	
					<input type="hidden" id="id" name="id"
						value="<c:out value="${caiFormulario.id}" default="" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioCai.datosCai" htmlEscape="true" />
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
												<spring:message code="formularioCai.nombreCai"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioCai.datosCai.numero"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCai.datosCai.vencimiento"
															htmlEscape="true" />
													</td>																				
												</tr>
												<tr>													
													<td class="texto_ti"><input type="text" id="numero" maxlength="14"
														name="numero" style="width: 100px;" class="requerido"
														value="<c:out value="${caiFormulario.numero}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
													</td>													
													<td class="texto_ti">
														<input type="text" id="fechaVencimiento" name="fechaVencimiento" 
															maxlength="10" class="requerido"
															value="<c:out value="${caiFormulario.fechaVencimientoStr}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>/>
															<script type="text/javascript">
																new tcal ({
																	// form name
																	'formname': 'formCai',
																	// input name
																	'controlname': 'fechaVencimiento'
																});
															</script>
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
</body>
</html>