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
		<spring:message code="formularioSerie.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioSerie.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioSerie.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_serie.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas.js"></script>
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
							<spring:message code="formularioSerie.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioSerie.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioSerie.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarSerie.html"
					commandName="serieFormulario" method="post"
					modelAttribute="serieFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${serieFormulario.id}" default="" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioSerie.datosSerie" htmlEscape="true" />
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
												<spring:message code="formularioSerie.nombreSerie"
													htmlEscape="true" />
											</legend>
											<table>
											<tr>													
													<td>
														<table>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioSerie.datosSerie.habilitado"
																		htmlEscape="true" />
																</td>
																<td class="texto_ti"><input type="checkBox" checked="checked"
																	id="habilitado" name="habilitado" style="width: 10px;" 
																	value="true"
																	<c:if test="${serieFormulario.habilitado == 'true'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if>/>
																</td>
															</tr>
														</table></td>													
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.empresa"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.sucursal"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.codigo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.descripcion"
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
																<c:if test="${accion != 'NUEVO'}">
																	<c:forEach items="${empresas}" var="empresa">
																		<option value="${empresa.id}"
																			<c:if test="${serieFormulario.empresa.id == empresa.id }">
																			selected="selected"
																		</c:if>>
																			<c:out value="${empresa.descripcion}" />
																		</option>
																	</c:forEach>
																</c:if>
																<c:if test="${accion == 'NUEVO'}">
																	<c:forEach items="${empresas}" var="empresa">
																		<option value="${empresa.id}">
																			<c:out value="${empresa.descripcion}" />
																		</option>
																	</c:forEach>
																</c:if>
														</select>
													</td>
													<td class="texto_ti">
														<select id="idSucursal" class="requerido"
															name="idSucursal" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<c:if test="${accion != 'NUEVO'}">
																	<c:forEach
																		items="${serieFormulario.empresa.sucursales}"
																		var="sucursal">
																		<option value="${sucursal.id}"
																			<c:if test="${serieFormulario.sucursal.id == sucursal.id}">
																			selected="selected"
																		</c:if>>
																			<c:out value="${sucursal.descripcion}" />
																		</option>
																	</c:forEach>
																</c:if>
														</select>
													</td>
													<td class="texto_ti"><input type="text" id="codigo" maxlength="3"
														name="codigo" style="width: 200px;" class="requerido"
														value="<c:out value="${serieFormulario.codigo}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="descripcion" name="descripcion" style="width: 200px;"
														value="<c:out value="${serieFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.tipoSerie"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.afipTipoComprobante"
															htmlEscape="true" />
													</td>													
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.prefijo"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioSerie.datosSerie.ultNroImpreso"
															htmlEscape="true" />
													</td>													
												</tr>
												<tr>
													<td class="texto_ti">
														<select id="tipoSerie" class="requerido"
															name="tipoSerie" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<option label="Factura" value="F" 
																	<c:if test="${serieFormulario.tipoSerie == 'F'}">
																			selected="selected"
																		</c:if>>Factura</option>
																<option label="Nota de Crédito" value="NC" 
																	<c:if test="${serieFormulario.tipoSerie == 'NC'}">
																			selected="selected"
																		</c:if>>Nota de Crédito</option>
																<option label="Nota de Débito" value="ND" 
																	<c:if test="${serieFormulario.tipoSerie == 'ND'}">
																			selected="selected"
																		</c:if>>Nota de Débito</option>		
																<option label="Remito" value="R"
																	<c:if test="${serieFormulario.tipoSerie == 'R'}">
																			selected="selected"
																		</c:if>>Remito</option>
																<option label="Recibo" value="X"
																	<c:if test="${serieFormulario.tipoSerie == 'X'}">
																			selected="selected"
																		</c:if>>Recibo</option>
																<option label="Documento Interno" value="I" 
																	<c:if test="${serieFormulario.tipoSerie == 'I'}">
																			selected="selected"
																		</c:if>>Documento Interno</option>															
														</select>
													</td>
													<td class="texto_ti">
														<select id="idAfipTipoComprobante" class="requerido" 
															name="idAfipTipoComprobante" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<c:forEach items="${afipTipoComprobantes}" var="tipo">
																	<option value="${tipo.id}"
																		<c:if test="${tipo.id == serieFormulario.afipTipoComprobante.id}">
																		selected="selected"
																	</c:if>>
																		<c:out value="${tipo.descripcion}"/>
																	</option>
																</c:forEach>
														</select>
													</td>													
													<td class="texto_ti"><input type="text" id="prefijo" maxlength="4"
														name="prefijo" style="width: 190px;" class="requerido"
														value="<fmt:numberComplete value="${serieFormulario.prefijo}" 
														length="4" valorDefualt="1"/>"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
													</td>
													<td class="texto_ti"><input type="text" id="ultNroImpreso" maxlength="8"
														name="ultNroImpreso" style="width: 190px;"
														value="<fmt:numberComplete value="${serieFormulario.ultNroImpreso}" 
														length="8" valorDefualt="1"/>"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
													</td>
												</tr>
												<tr>													
													<td class="texto_ti"><spring:message
															code="formularioSerie.vacio" htmlEscape="true" />
													</td>													
												</tr>
												<tr>													
													<td class="texto_ti"><spring:message code="formularioSerie.condicionIvaClientes"
														htmlEscape="true" />
													</td>													
												</tr>
												<tr>
														<td class="texto_ti">
														<select id="condIvaClientes" class="requerido" 
															name="condIvaClientes" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<option label="RI" value="RI" 
																	<c:if test="${serieFormulario.condIvaClientes == 'RI'}">
																			selected="selected"
																		</c:if>>Responsable Inscripto</option>
																<option label="Otros" value="Otros" 
																	<c:if test="${serieFormulario.condIvaClientes == 'Otros'}">
																			selected="selected"
																		</c:if>>Otros</option>												
														</select>
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
				<br style="font-size: xx-small;"/>
					<fieldset>
					<legend>
						<spring:message code="formularioSerie.nombreTalonCai" htmlEscape="true" />
					</legend>
						<display:table name="cais" id="cai" requestURI="precargaFormularioSerie.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${cai.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column property="numero" titleKey="formularioSerie.datosCai.cai.numero" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="fechaVencimientoStr" titleKey="formularioSerie.datosCai.cai.vencimiento" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" onclick="agregarCai();" class="botonCentrado"
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
</body>
</html>