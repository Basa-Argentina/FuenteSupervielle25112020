<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ page buffer = "32kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioGrupo.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioGrupo.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioGrupo.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_grupo.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
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
							<spring:message code="formularioGrupo.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioGrupo.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioGrupo.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarGrupo.html"
					commandName="grupoFormulario" method="post"
					modelAttribute="grupoFormulario">
					<input type="hidden" id="confirmarEliminarEstante" value="<spring:message code="formularioGrupo.confirmarEliminarEstante"
								htmlEscape="true" />"/> 
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${grupoFormulario.id}" default="" />" />
					<input type="hidden" id="seccionId" name="seccionId"
						value="<c:out value="${grupoFormulario.idSeccion}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioGrupo.datosGrupo" htmlEscape="true" />
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
												<spring:message code="formularioGrupo.nombreGrupo"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
														code="formularioStock.empresa" htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioStock.sucursal" htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.deposito"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="codigoEmpresa"
														name="codigoEmpresa" maxlength="6" style="width: 50px;"
														value="<c:out value="${grupoFormulario.codigoEmpresa}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
														&nbsp;&nbsp;
														<button type="button"
															onclick="abrirPopup('empresasPopupMap');borrar(); "
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
														for="codigoEmpresa"> <c:out
																value="${grupoFormulario.seccion.deposito.sucursal.empresa.descripcion}"
																default="" /> </label></td>
													<td class="texto_ti"><input type="text" id="codigoSucursal"
														name="codigoSucursal" maxlength="6" style="width: 50px;"
														value="<c:out value="${grupoFormulario.codigoSucursal}" default="" />"
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
																value="${grupoFormulario.seccion.deposito.sucursal.descripcion}"
																default="" /> </label></td>
													<td class="texto_ti"><input type="text" id="codigoDeposito"
														name="codigoDeposito" maxlength="6" style="width: 50px;"
														value="<c:out value="${grupoFormulario.codigoDeposito}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
														&nbsp;&nbsp;
														<button type="button"
															onclick="abrirPopupDeposito('depositosPopupMap', '<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>'
																						, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoDepositoLabel"
														for="codigoDeposito"> <c:out
																value="${grupoFormulario.seccion.deposito.descripcion}"
																default="" /> </label>
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.seccion"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.codigo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.descripcion"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="codigoSeccion"
														name="codigoSeccion" maxlength="6" style="width: 50px;"
														value="<c:out value="${grupoFormulario.codigoSeccion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
														&nbsp;&nbsp;
														<button type="button"
															onclick="abrirPopupSeccion('seccionesPopupMap', '<spring:message code="notif.stock.seleccionDeposito" htmlEscape="true"/>'
																						, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoSeccionLabel"
														for="codigoSeccion"> <c:out
																value="${grupoFormulario.seccion.descripcion}"
																default="" /> </label>
													</td>
													<td class="texto_ti"><input type="text" id="codigo" class="requerido" onblur="formato(2)"
														maxlength="2" name="codigo" style="width: 200px;"
														value="<fmt:numberComplete value="${grupoFormulario.codigo}" 
														length="2" valorDefualt="0"/>"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="descripcion" name="descripcion" style="width: 200px;"
														maxlength="100"
														value="<c:out value="${grupoFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.verticales"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.horizontales"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.modulosVert"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioGrupo.datosGrupo.modulosHor"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="verticales" class="requerido"
														maxlength="3" name="verticales" style="width: 200px;"
														value="<c:out value="${grupoFormulario.verticales}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="horizontales" name="horizontales" style="width: 200px;"
														maxlength="3"
														value="<c:out value="${grupoFormulario.horizontales}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
													</td>
													<td class="texto_ti"><input type="text" id="modulosVert" class="requerido"
														maxlength="2" name="modulosVert" style="width: 200px;"
														value="<c:out value="${grupoFormulario.modulosVert}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="modulosHor" name="modulosHor" style="width: 200px;"
														maxlength="2"
														value="<c:out value="${grupoFormulario.modulosHor}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
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
						<spring:message code="formularioGrupo.estanterias"
							htmlEscape="true" />
					</legend>
					<display:table name="estantes" id="estante" requestURI="" pagesize="15" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
						    <input type="hidden" id="hdn_id" value="${estante.id}"/>
		              	</display:column>		
		              	<display:column class="hidden" headerClass="hidden">
					    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
	           		   	</display:column>
	           		   	<display:column maxLength="4" property="codigo" titleKey="formularioGrupo.datosGrupo.estante.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
						<display:column property="observacion" titleKey="formularioGrupo.datosGrupo.estante.observaciones" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
					  	<display:column class="celdaAlineadoCentrado">
				  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
						</display:column>	
					</display:table> 
					<div style="width: 100%" align ="right">
						<button name="agregar" type="button" onclick="agregarEstante();" class="botonCentrado"
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
	<div id="pop" style="display:none">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
	</div>
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="depositosPopupMap" />
		<jsp:param name="clase" value="depositosPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="sucursalesPopupMap" /> 
		<jsp:param name="clase" value="sucursalesPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="empresasPopupMap" /> 
		<jsp:param name="clase" value="empresasPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="seccionesPopupMap" /> 
		<jsp:param name="clase" value="seccionesPopupMap" /> 
	</jsp:include>
</body>
</html>