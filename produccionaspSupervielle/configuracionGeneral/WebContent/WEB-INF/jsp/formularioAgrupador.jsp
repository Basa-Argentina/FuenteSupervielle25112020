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
		<spring:message code="formularioAgrupador.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioAgrupador.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioAgrupador.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_agrupador.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
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
							<spring:message code="formularioAgrupador.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioAgrupador.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioAgrupador.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarAgrupador.html"
					commandName="agrupadorFormulario" method="post"
					modelAttribute="agrupadorFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${agrupadorFormulario.id}" default="" />" />
					<input type="hidden" id="cantGrupoFacturaciones" name="cantGrupoFacturaciones"
						value="<c:out value="${cantGrupoFacturaciones}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="agrupadorTipo" name="agrupadorTipo" value="<c:out value="${agrupadorFormulario.tipoAgrupador}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioAgrupador.datosAgrupador" htmlEscape="true" />
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
												<spring:message code="formularioAgrupador.nombreAgrupador"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioAgrupador.datosAgrupador.cliente"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="clienteCodigo" class="requerido"
														name="clienteCodigo" maxlength="6" style="width: 50px;"
														value="<c:out value="${agrupadorFormulario.clienteEmp.codigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
														&nbsp;&nbsp;
														<button type="button" id="btnCliente"
															onclick="abrirPopup('clientesPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="clienteCodigoLabel"
														for="clienteCodigo"> <c:out
																value="${agrupadorFormulario.clienteEmp.nombreYApellido}"
																default="" /> </label>
													</td>
												</tr>
											</table>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioAgrupador.datosAgrupador.codigo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioAgrupador.datosAgrupador.descripcion"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioAgrupador.datosAgrupador.tipoAgrupador"
															htmlEscape="true" />
													</td>												
												</tr>
												<tr>													
													<td class="texto_ti"><input type="text" id="codigo" maxlength="4"
														name="codigo" style="width: 190px;" class="requerido"
														value="<c:out value="${agrupadorFormulario.codigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
													</td>
													<td class="texto_ti"><input type="text" id="decripcion" maxlength="30"
														name="descripcion" style="width: 230px;" class="requerido"
														value="<c:out value="${agrupadorFormulario.descripcion}"default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
													</td>
													<td class="texto_ti">
														<select id="tipoAgrupador" class="requerido"
															name="tipoAgrupador" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<option label="Empleado" value="E" 
																	<c:if test="${agrupadorFormulario.tipoAgrupador == 'E'}">
																			selected="selected"
																		</c:if>>Empleado</option>
																<option label="Direcciones de Entrega" value="D"
																	<c:if test="${agrupadorFormulario.tipoAgrupador == 'D'}">
																			selected="selected"
																		</c:if>>Direcciones de Entrega</option>															
														</select>
													</td>													
												</tr>												
											</table>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioAgrupador.datosAgrupador.observacion"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioAgrupador.vacio" htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<textarea id="observacion" 
															name="observacion" style="width: 445px;"
															<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if>>
															<c:out value="${agrupadorFormulario.observacion}" 
																default="" />
														</textarea>
													</td>
													<td>
														<table>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioAgrupador.datosAgrupador.habilitado"
																		htmlEscape="true" />
																</td>
																<td class="texto_ti"><input type="checkBox" 
																	id="habilitado" name="habilitado" style="width: 10px;" 
																	value="true"
																	<c:if test="${agrupadorFormulario.habilitado == 'true'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if>/>
																</td>
															</tr>
														</table>
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
				<br style="font-size: xx-small;"/>
					<fieldset>
						<legend>
							<spring:message code="formularioAgrupador.datosGrupo.grupoFacturacion"	htmlEscape="true" />
						</legend>
						<display:table name="grupoFacturaciones" id="grupoFacturacion" requestURI="" pagesize="10" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${grupoFacturacion.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column style="width:70px;" property="codigo" titleKey="formularioAgrupador.datosGrupo.grupoFacturacion.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="descripcion" titleKey="formularioAgrupador.datosGrupo.grupoFacturacion.descripcion" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="observacion" titleKey="formularioAgrupador.datosGrupo.grupoFacturacion.observacion" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
						  	<display:column class="celdaAlineadoCentrado" style="width:50px;">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" onclick="agregarGrupoFacturacion();" class="botonCentrado"
								<c:if test="${accion != 'MODIFICACION'}">
									disabled="disabled"
								</c:if>>
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="botones.agregar" htmlEscape="true"/>  
							</button>
						</div>
					</fieldset>
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
		<jsp:param name="mapa" value="clientesPopupMap" />
		<jsp:param name="clase" value="clientesPopupMap" />
	</jsp:include>
</body>
</html>