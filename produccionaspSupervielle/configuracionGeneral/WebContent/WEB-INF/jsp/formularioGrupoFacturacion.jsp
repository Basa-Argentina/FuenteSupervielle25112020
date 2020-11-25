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
		<spring:message code="formularioGrupoFacturacion.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioGrupoFacturacion.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioGrupoFacturacion.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_grupo_facturacion.js"></script>
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
							<spring:message code="formularioGrupoFacturacion.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioGrupoFacturacion.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioGrupoFacturacion.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formGrupoFacturacion" action="guardarActualizarGrupoFacturacion.html"
					commandName="grupoFacturacionFormulario" method="post"
					modelAttribute="grupoFacturacionFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="accionAgrupador" name="accionAgrupador"
						value="<c:out value="${accionAgrupador}" default="" />" />	
					<input type="hidden" id="id" name="id"
						value="<c:out value="${grupoFacturacionFormulario.id}" default="" />" />
					<input type="hidden" id="empleadosSeleccionados" name="empleadosSeleccionados"/>
					<input type="hidden" id="direccionesSeleccionadas" name="direccionesSeleccionadas"/>
					<input type="hidden" id="tipoAgrupador" name="tipoAgrupador" 
						value="<c:out value="${tipoAgrupador}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioGrupoFacturacion.datosGrupoFacturacion" htmlEscape="true" />
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
												<spring:message code="formularioGrupoFacturacion.nombreGrupoFacturacion"
													htmlEscape="true" />
											</legend>
											<table align="center">
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioGrupoFacturacion.datosGrupoFacturacion.codigo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioGrupoFacturacion.datosGrupoFacturacion.descripcion"
															htmlEscape="true" />
													</td>													
												</tr>
												<tr>													
													<td class="texto_ti"><input type="text" id="codigo" maxlength="4"
														name="codigo" style="width: 100px;" class="requerido"
														value="<c:out value="${grupoFacturacionFormulario.codigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
													</td>
													<td class="texto_ti"><input type="text" id="descripcion" maxlength="30"
														name="descripcion" style="width: 230px;" class="requerido"
														value="<c:out value="${grupoFacturacionFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
													</td>
												</tr>
											</table>
											<table align="center">
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioGrupoFacturacion.datosGrupoFacturacion.observacion"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>													
													<td class="texto_ti">
														<textarea id="observacion" 
															name="observacion" style="width: 350px;"
															<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if>>
															<c:out value="${grupoFacturacionFormulario.observacion}" 
																default="" />
														</textarea>
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
										<td align="left" class="error">
										</td>
									</tr>
								</c:if>
							</table>
							<table style="width: 100%;">
								<tr>
									<td>
										<fieldset id="direcciones" >
											<table width="100%">
									          <thead>
									            <tr>
									              <th align="left" id="grupoImg">								 
											        	<font style="color:#003090">
											        		<spring:message code="formularioGrupoFacturacion.asignacionDirecciones" htmlEscape="true"/> 
											        	</font>
											        	<img id="grupoImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
											        	<img id="grupoImgSrc" style="DISPLAY: none" src="images/skip.png" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
												 	  
									              </th>
												 </tr>
												</thead>
											</table>
											<div id="grupoDiv" align="center">							
												<table>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioGrupoFacturacion.direccionesDisponibles" htmlEscape="true"/>
														</td>
														<td>
															&nbsp;
														</td>
														<td class="texto_ti">
															<spring:message code="formularioGrupoFacturacion.direccionesAsignadas" htmlEscape="true"/>
														</td>
													</tr>
													<tr>
												      <td width="40%" align="center">
												        <select id="direccionesSeleccionadasIzq" name="direccionesSeleccionadasIzq" size="8" style="width:250px" multiple="multiple">
												        	<c:forEach items="${direccionesIzq}" var="direccionIzq">
																<option value="<c:out value="${direccionIzq.id}"/>">
																	<c:out value="${direccionIzq.codigoDescripcion}"/>
																</option>
															</c:forEach>
												        </select>
												      </td>
												      <td width="20%" align="center">											          
												          <c:if test="${accion != 'CONSULTA'}">
													          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('direccionesSeleccionadas')" 
													        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
													          	>
													          <br />
													          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('direccionesSeleccionadas')" 
													        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
													          >
													          <br />
											        	  </c:if>
												      </td>
												      <td width="40%" align="center">
												        <select id="direccionesSeleccionadasDer" name="direccionesSeleccionadasDer" size="8" style="width:250px" multiple="multiple">
												            <c:forEach items="${direccionesDer}" var="direccionDer">
																<option value="<c:out value="${direccionDer.id}"/>">
																	<c:out value="${direccionDer.codigoDescripcion}"/>
																</option>
															</c:forEach>
												        </select>
												      </td>
												    </tr>
											    </table>
										    </div>
									    </fieldset>
									    <fieldset id="empleados">
											<table width="100%">
									          <thead>
									            <tr>
									              <th align="left" id="grupoImg">								 
											        	<font style="color:#003090">
											        		<spring:message code="formularioGrupoFacturacion.asignacionEmpleados" htmlEscape="true"/> 
											        	</font>
											        	<img id="grupoImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
											        	<img id="grupoImgSrc" style="DISPLAY: none" src="images/skip.png" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
												 	  
									              </th>
												 </tr>
												</thead>
											</table>
											<div id="grupoDiv" align="center">							
												<table>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioGrupoFacturacion.empleadosDisponibles" htmlEscape="true"/>
														</td>
														<td>
															&nbsp;
														</td>
														<td class="texto_ti">
															<spring:message code="formularioGrupoFacturacion.empleadosAsignados" htmlEscape="true"/>
														</td>
													</tr>
													<tr>
												      <td width="40%" align="center">
												        <select id="empleadosSeleccionadosIzq" name="empleadosSeleccionadosIzq" size="8" style="width:250px" multiple="multiple">
												        	<c:forEach items="${empleadosIzq}" var="empleadoIzq">
																<option value="<c:out value="${empleadoIzq.id}"/>">
																	<c:out value="${empleadoIzq.apellidoYNombre}"/>
																</option>
															</c:forEach>
												        </select>
												      </td>
												      <td width="20%" align="center">											          
												          <c:if test="${accion != 'CONSULTA'}">
													          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('empleadosSeleccionados')" 
													        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
													          	>
													          <br />
													          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('empleadosSeleccionados')" 
													        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
													          >
													          <br />
											        	  </c:if>
												      </td>
												      <td width="40%" align="center">
												        <select id="empleadosSeleccionadosDer" name="empleadosSeleccionadosDer" size="8" style="width:250px" multiple="multiple">
												            <c:forEach items="${empleadosDer}" var="empleadoDer">
																<option value="<c:out value="${empleadoDer.id}"/>">
																	<c:out value="${empleadoDer.apellidoYNombre}"/>
																</option>
															</c:forEach>
												        </select>
												      </td>
												    </tr>
											    </table>
										    </div>
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
</body>
</html>