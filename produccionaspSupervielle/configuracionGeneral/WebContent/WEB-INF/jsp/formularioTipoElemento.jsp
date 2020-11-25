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
		<spring:message code="formularioTipoElemento.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioTipoElemento.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioTipoElemento.titulo.modificar"
			htmlEscape="true" />
	</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js">
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_tipo_elemento.js"></script>
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
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							<spring:message code="formularioTipoElemento.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioTipoElemento.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioTipoElemento.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarTipoElemento.html"
					commandName="tipoElementoFormulario" method="post"
					modelAttribute="tipoElementoFormulario">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${tipoElementoFormulario.id}" default="" />" />
						<input id="tipoEtiquetaPreseleccionado" type="hidden" value="${tipoEtiquetaPreseleccionado}" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioTipoElemento.datosTipoElemento" htmlEscape="true" />
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
												<spring:message code="formularioTipoElemento.nombreTipoElemento"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>												
													<td class="texto_ti"><spring:message
															code="formularioTipoElemento.datosTipoElemento.codigo"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioTipoElemento.datosTipoElemento.descripcion"
															htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioTipoElemento.datosTipoElemento.prefijoCodigo"
															htmlEscape="true" /></td>
													<td class="texto_ti">
														<spring:message
															code="formularioTipoElemento.datosTipoElemento.tipoEtiqueta"
															htmlEscape="true" />
													</td>
									
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="codigo" class="requerido completarZeros inputTextNumericPositiveIntegerOnly"
														maxlength="3" name="codigo" style="width: 200px;"
														value="<c:out value="${tipoElementoFormulario.codigo}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="descripcion" name="descripcion" style="width: 200px;"
														maxlength="30"
														value="<c:out value="${tipoElementoFormulario.descripcion}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="prefijoCodigo" class="requerido completarZeros inputTextNumericPositiveIntegerOnly"
														maxlength="2" name="prefijoCodigo" style="width: 100px;"
														value="<c:out value="${tipoElementoFormulario.prefijoCodigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti">
														<select id="tipoEtiquetaInt" name="tipoEtiquetaInt" class="requerido"	size="1" <c:if test="${accion == 'CONSULTA'}">disabled="disabled"</c:if> >
															<option value="1" ><spring:message code="formularioTipoElemento.datosTipoElemento.tipoEtiqueta.etiquetaChica" htmlEscape="true"></spring:message></option>
															<option value="2" ><spring:message code="formularioTipoElemento.datosTipoElemento.tipoEtiqueta.etiquetaMedia" htmlEscape="true"></spring:message></option>
														</select>
													</td>
														
									
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioTipoElemento.vacio"
															htmlEscape="true" /></td>													
													<td class="texto_ti"><spring:message
															code="formularioTipoElemento.vacio" htmlEscape="true" /></td>
												</tr>
												
											</table>
											<table>
												<tr>																						
												<td>
										     	<fieldset>
											     <legend>
														<spring:message code="formularioTipoElemento.datosTipoElemento.caracteristicas"
															htmlEscape="true" />
													</legend>
														 <table>
															 <tr>
															   <td>
																<table>
																	<tr>
																		<td class="texto_ti"><spring:message
																				code="formularioTipoElemento.datosTipoElemento.contenedor"
																				htmlEscape="true" /></td>
																		<td class="texto_ti"><input type="checkBox"
																			id="contenedor" name="contenedor"
																			style="width: 10px;" value="true"
																			<c:if test="${tipoElementoFormulario.contenedor == 'true'}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'CONSULTA'}">
																				disabled="disabled"
																			</c:if> />
																		</td>
																	</tr>
																</table>
																
															</td>
															<td>
																<table>
																	<tr>
																		<td class="texto_ti"><spring:message
																				code="formularioTipoElemento.datosTipoElemento.contenido"
																				htmlEscape="true" /></td>
																		<td class="texto_ti"><input type="checkBox"
																			id="contenido" name="contenido"
																			style="width: 10px;" value="true"
																			<c:if test="${tipoElementoFormulario.contenido == 'true'}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'CONSULTA'}">
																				disabled="disabled"
																			</c:if> />
																		</td>
																	</tr>
																</table>
															</td>
		
															<td>
																<table>
																	<tr>
																		<td class="texto_ti"><spring:message
																				code="formularioTipoElemento.datosTipoElemento.posicionable"
																				htmlEscape="true" /></td>
																		<td class="texto_ti"><input type="checkBox"
																			id="posicionable" name="posicionable"
																			style="width: 10px;" value="true"
																			<c:if test="${tipoElementoFormulario.posicionable == 'true'}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'CONSULTA'}">
																				disabled="disabled"
																			</c:if> />
																		</td>
																	</tr>
																</table>
															</td>
															<td>
																<table>
																	<tr>
																		<td class="texto_ti"><spring:message
																				code="formularioTipoElemento.datosTipoElemento.precintoSeguridad"
																				htmlEscape="true" /></td>
																				<td class="texto_ti"><input type="checkBox"
																					id="precintoSeguridad" name="precintoSeguridad"
																					style="width: 10px;" value="true"
																					<c:if test="${tipoElementoFormulario.precintoSeguridad == 'true'}">
																						checked="CHECKED"
																					</c:if>
																					<c:if test="${accion == 'CONSULTA'}">
																						disabled="disabled"
																					</c:if> />
																				</td>
																			</tr>
																</table>
															</td>
															<td>
																<table>
																	<tr>
																		<td class="texto_ti"><spring:message
																				code="formularioTipoElemento.datosTipoElemento.seleccionaTipoTrabajo"
																				htmlEscape="true" /></td>
																				<td class="texto_ti"><input type="checkBox"
																					id="seleccionaTipoTrabajo" name="seleccionaTipoTrabajo"
																					style="width: 10px;" value="true"
																					<c:if test="${tipoElementoFormulario.seleccionaTipoTrabajo == 'true'}">
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
									 			</fieldset>       
											 </td>
									      </tr>
									   </table>
									   <table>
									   	  <tr>
									   		<td>
									   		<fieldset>
									   		<legend>
												<spring:message code="formularioTipoElemento.datosTipoElemento.conceptosAsociados"
													htmlEscape="true" />
											</legend>
									   				<table>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioTipoElemento.datosTipoElemento.generaConceptoVenta"
																		htmlEscape="true" /></td>
																<td class="texto_ti"><input type="checkBox" onclick="checkSelectVenta();"
												  					id="generaConceptoVenta" name="generaConceptoVenta"
																	style="width: 10px;" value="true"
																	<c:if test="${tipoElementoFormulario.generaConceptoVenta == 'true'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
																</td>
																<td class="texto_ti"><div id="seccion1" style="display:none"><input type="text" id="conceptoCodigoVenta"
																	name="conceptoCodigoVenta" maxlength="6" style="width: 50px;" class="completarZeros inputTextNumericPositiveIntegerOnly"
																	value="<c:out value="${tipoElementoFormulario.conceptoVenta.codigo}" default="" />"
																	<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
																				</c:if> />
																	&nbsp;&nbsp;
																	<button type="button"
																		onclick="abrirPopup('conceptosVentaPopupMap');"
																		title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																		<c:if test="${accion == 'CONSULTA'}">
																				disabled="disabled"
																			</c:if>>
																		<img src="<%=request.getContextPath()%>/images/buscar.png">
																	</button>&nbsp;&nbsp; <label id="conceptoCodigoVentaLabel"
																	for="conceptoCodigoVenta"> <c:out
																			value="${tipoElementoFormulario.conceptoVenta.descripcion}"
																			default="" /> </label>
																</div></td>
															</tr>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioTipoElemento.datosTipoElemento.generaConceptoGuarda"
																		htmlEscape="true" /></td>
																<td class="texto_ti"><input type="checkBox" onclick="checkSelectGuarda();"
																	id="generaConceptoGuarda" name="generaConceptoGuarda"
																	style="width: 10px;" value="true"
																	<c:if test="${tipoElementoFormulario.generaConceptoGuarda == 'true'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
																</td>
																<td class="texto_ti"><div id="seccion2" style="display:none"><input type="text" id="conceptoCodigoGuarda"
																	name="conceptoCodigoGuarda" maxlength="6" style="width: 50px;" class="completarZeros inputTextNumericPositiveIntegerOnly"
																	value="<c:out value="${tipoElementoFormulario.conceptoGuarda.codigo}" default="" />"
																	<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
																				</c:if> />
																	&nbsp;&nbsp;
																	<button type="button"
																		onclick="abrirPopup('conceptosGuardaPopupMap');"
																		title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																		<c:if test="${accion == 'CONSULTA'}">
																				disabled="disabled"
																			</c:if>>
																		<img src="<%=request.getContextPath()%>/images/buscar.png">
																	</button>&nbsp;&nbsp; <label id="conceptoCodigoGuardaLabel"
																	for="conceptoCodigoGuarda"> <c:out
																			value="${tipoElementoFormulario.conceptoGuarda.descripcion}"
																			default="" /> </label>
																</div></td>
															</tr>
															<tr>
																<td class="texto_ti"><spring:message
																		code="formularioTipoElemento.datosTipoElemento.generaConceptoStock"
																		htmlEscape="true" /></td>
																<td class="texto_ti"><input type="checkBox" onclick="checkSelectStock();"
																	id="descuentaStock" name="descuentaStock"
																	style="width: 10px;" value="true"
																	<c:if test="${tipoElementoFormulario.descuentaStock == 'true'}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
																</td>
																<td class="texto_ti"><div id="seccion3" style="display:none"><input type="text" id="conceptoCodigoStock"
																	name="conceptoCodigoStock" maxlength="6" style="width: 50px;" class="completarZeros inputTextNumericPositiveIntegerOnly"
																	value="<c:out value="${tipoElementoFormulario.conceptoStock.codigo}" default="" />"
																	<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
																				</c:if> />
																	&nbsp;&nbsp;
																	<button type="button"
																		onclick="abrirPopup('conceptosStockPopupMap');"
																		title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																		<c:if test="${accion == 'CONSULTA'}">
																				disabled="disabled"
																			</c:if>>
																		<img src="<%=request.getContextPath()%>/images/buscar.png">
																	</button>&nbsp;&nbsp; <label id="conceptoCodigoStockLabel"
																	for="conceptoCodigoStock"> <c:out
																			value="${tipoElementoFormulario.conceptoStock.descripcion}"
																			default="" /> </label>
																</div></td>
															</tr>
														</table>
									   		</fieldset>
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
		<jsp:param name="mapa" value="conceptosVentaPopupMap" /> 
		<jsp:param name="clase" value="conceptosVentaPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="conceptosGuardaPopupMap" /> 
		<jsp:param name="clase" value="conceptosGuardaPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="conceptosStockPopupMap" /> 
		<jsp:param name="clase" value="conceptosStockPopupMap" /> 
	</jsp:include>
</body>
</html>