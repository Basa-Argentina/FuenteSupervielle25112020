<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioElemento.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioElemento.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioElemento.titulo.modificar"
			htmlEscape="true" />
	</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/busquedaHelper.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_elemento.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/Utils.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
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
							<spring:message code="formularioElemento.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioElemento.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioElemento.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formElemento"
					action="guardarActualizarElemento.html"
					commandName="elementoFormulario" method="post"
					modelAttribute="elementoFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${elementoFormulario.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
					<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioElemento.datosElemento" htmlEscape="true" />
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
												<spring:message code="formularioElemento.nombreElemento"
													htmlEscape="true" />
											</legend>
											<table align="center">
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.tipoElemento" htmlEscape="true" />
													</td>
												</tr>
												<tr>
																<td class="texto_ti">
																	<input type="text" id="codigoTipoElemento" maxlength="6"
															name="codigoTipoElemento" style="width: 50px;" class="requerido"
															value="<c:out value="${elementoFormulario.codigoTipoElemento}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
															&nbsp;&nbsp;
															<button type="button" id="buscaTipoElemento"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
																<img
																	src="<%=request.getContextPath()%>/images/buscar.png">
															</button> &nbsp;&nbsp; <label id="codigoTipoElementoLabel"
															for="codigoTipoElemento"><c:out
																value="${elementoFormulario.tipoElemento.descripcion}"
																default="" /> </label>
															</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.datosElemento.codigo"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><label id="codigoTipoElementoLabel2"
														for="codigoTipoElemento"> <c:out
																value="${elementoFormulario.tipoElemento.prefijoCodigo}"
																default="" />
													</label>
													<input type="text" maxlength="10"
														id="codigo" name="codigo" class="requerido completarZeros inputTextNumericPositiveIntegerOnly"
														style="width: 100px;" readonly="readonly"
														value="<c:out value="${elementoFormulario.codigoSinPrefijo}" default="" />" 
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti" id="tdCantidad"><spring:message
															code="formularioElemento.cantidad" htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" maxlength="5" class="inputTextNumericPositiveIntegerOnly"
														id="cantidad" name="cantidad" style="width: 100px;"
														<c:if test="${accion != 'NUEVO'}">
															value="<c:out value="${elementoFormulario.cantidad}" default="" />"
														</c:if>
														<c:if test="${accion == 'NUEVO'}">
															value="1"
														</c:if>
														/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti" id="tdHasta"><spring:message 
															code="formularioElemento.hasta" htmlEscape="true" /></td>
												</tr>
												<c:if test="${accion == 'NUEVO'}">
												<tr>
													<td class="texto_ti"><label id="codigoTipoElementoLabel3"
														for="codigoTipoElemento"> <c:out
																value="${elementoFormulario.tipoElemento.prefijoCodigo}"
																default="" />
													</label>
													<input type="text" maxlength="10" readonly="readonly"
														id="hasta" name="hasta" style="width: 100px;" class="completarZeros inputTextNumericPositiveIntegerOnly"
														value="<c:out value="${elementoFormulario.hasta}" default="" />" />
													</td>
												</tr>
												</c:if>
												<c:if test="${elementoFormulario.tipoElemento.precintoSeguridad == true}">
													<tr>
														<td class="texto_ti"><spring:message
																code="formularioElemento.nroPrecintoSeguridad" htmlEscape="true" /></td>
													</tr>
													<tr>
															<td class="texto_ti">
																<input type="text" id="nroPrecinto" 
																name="nroPrecinto" value="<c:out value="${elementoFormulario.nroPrecinto}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>>	
															</td>
													</tr>
													<tr>
														<td class="texto_ti"><spring:message
																code="formularioElemento.nroPrecintoSeguridad1" htmlEscape="true" /></td>
													</tr>
													<tr>
															<td class="texto_ti">
																<input type="text" id="nroPrecinto1" 
																name="nroPrecinto1" value="<c:out value="${elementoFormulario.nroPrecinto1}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>>	
															</td>
													</tr>
												</c:if>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.empresa" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti">
													<input type="text" id="codigoEmpresa" name="codigoEmpresa" style="width: 50px;" maxlength="6"
												value="<c:out value="${elementoFormulario.codigoEmpresa}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
												</c:if>/>
											&nbsp;&nbsp;
											<button type="button" id="buscaEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoEmpresaLabel" for="codigoEmpresa"><c:out
																value="${elementoFormulario.clienteEmp.empresa.descripcion}"
																default="" /></label>
												</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.cliente" htmlEscape="true" /></td>
												</tr>
												<tr>
																												
																<td class="texto_ti">
											<input type="text" id="codigoCliente" name="codigoCliente" style="width: 50px;" maxlength="6"
												value="<c:out value="${elementoFormulario.codigoCliente}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
											&nbsp;&nbsp;
											<button type="button" id="buscaCliente" 
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoClienteLabel" for="codigoCliente"> 
												 <c:out	value="${elementoFormulario.clienteEmp.nombreYApellido}" default="" />
											</label>
																</td>
												</tr>
												
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.contenedor" htmlEscape="true" />
													</td>
												</tr>
												<tr>
	
													<td class="texto_ti"><input type="text"
														id="codigoContenedor" name="codigoContenedor"
														style="width: 50px;" maxlength="12"
														value="<c:out value="${elementoFormulario.contenedor.codigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if> />

														&nbsp;&nbsp;
														<button type="button" id="buscaContenedor"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
															<img
																src="<%=request.getContextPath()%>/images/buscar.png">
														</button> &nbsp;&nbsp; 
														<label id="codigoContenedorLabel" for="codigoContenedor">
														<c:out
																value="${elementoFormulario.contenedor.tipoElemento.descripcion}"
																default="" />
														</label></td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.vacio" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td>
														<table>
															<tr>																
																<td class="texto_ti"><input type="checkBox"
																	id="generaCanonMensual" name="generaCanonMensual"
																	style="width: 10px;" value="true"
																	<c:if test="${elementoFormulario.generaCanonMensual!=null && elementoFormulario.generaCanonMensual == true}">
																		checked="checked"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
																</td>
																<td class="texto_ti"><spring:message
																		code="formularioElemento.datosElemento.generaCanonMensual"
																		htmlEscape="true" />
																</td>
															</tr>
														</table></td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.datosElemento.estado"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><select id="estado" name="estado" class="requerido"
														size="1"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>>
															<option label="Creado" value="Creado"
																<c:if test="${elementoFormulario.estado == 'Creado'}">
																				selected="selected"
																			</c:if>>Creado</option>
															<option label="En el Cliente" value="En el Cliente"
																<c:if test="${elementoFormulario.estado == 'En el Cliente'}">
																				selected="selected"
																			</c:if>>En el Cliente</option>				
															<option label="En Guarda" value="En Guarda"
																<c:if test="${elementoFormulario.estado == 'En Guarda'}">
																				selected="selected"
																			</c:if>>En Guarda</option>
															<option label="En Transito" value="En Transito"
																<c:if test="${elementoFormulario.estado == 'En Transito'}">
																				selected="selected"
																			</c:if>>En Transito</option>
															<option label="En Consulta" value="En Consulta"
																<c:if test="${elementoFormulario.estado == 'En Consulta'}">
																				selected="selected"
																			</c:if>>En Consulta</option>
															<option label="No Existe" value="No Existe"
															<c:if test="${elementoFormulario.estado == 'No Existe'}">
																			selected="selected"
																		</c:if>>No Existe</option>
															<option label="Unificado" value="Unificado"
															<c:if test="${elementoFormulario.estado == 'Unificado'}">
																			selected="selected"
																		</c:if>>Unificado</option>
													</select></td>
													
												</tr>
												<sec:authorize ifAnyGranted="ROLE_ELEM_HABILITAR_CERRAR">
													<c:if test="${elementoFormulario.tipoElemento.contenedor}">
														<tr>
															<td>
																<table>
																	<tr>																
																		<td class="texto_ti"><input type="checkBox"
																			id="habilitadoCerrar" name="habilitadoCerrar"
																			style="width: 10px;" value="true"
																			<c:if test="${elementoFormulario.habilitadoCerrar !=null && elementoFormulario.habilitadoCerrar == true}">
																				checked="checked"
																			</c:if>
																			<c:if test="${accion == 'CONSULTA'}">
																				disabled="disabled"
																			</c:if> />
																		</td>
																		<td class="texto_ti"><spring:message
																				code="formularioElemento.datosElemento.habilitarCerrar"
																				htmlEscape="true" />
																		</td>
																	</tr>
																</table></td>
														</tr>
													</c:if>	
												<c:if test="${elementoFormulario.tipoElemento.contenedor}">
													<tr>
														<td>
															<table>
																<tr>																
																	<td class="texto_ti"><input type="checkBox"
																		id="cerrado" name="cerrado"
																		style="width: 10px;" value="true"
																		<c:if test="${elementoFormulario.cerrado !=null && elementoFormulario.cerrado == true}">
																			checked="checked"
																		</c:if>
																		<c:if test="${accion == 'CONSULTA'}">
																			disabled="disabled"
																		</c:if> />
																	</td>
																	<td class="texto_ti"><spring:message
																			code="formularioElemento.datosElemento.cajaCerrada"
																			htmlEscape="true" />
																	</td>
																</tr>
															</table></td>
													</tr>
												</c:if>	
												</sec:authorize>
												<sec:authorize ifNotGranted="ROLE_ELEM_HABILITAR_CERRAR">
													<c:if test="${elementoFormulario.tipoElemento.contenedor 
													&& elementoFormulario.habilitadoCerrar !=null && elementoFormulario.habilitadoCerrar == true}">
													<tr>
														<td>
															<table>
																<tr>																
																	<td class="texto_ti"><input type="checkBox"
																		id="cerrado" name="cerrado"
																		style="width: 10px;" value="true"
																		<c:if test="${elementoFormulario.cerrado !=null && elementoFormulario.cerrado == true}">
																			checked="checked"
																		</c:if>
																		<c:if test="${accion == 'CONSULTA'}">
																			disabled="disabled"
																		</c:if> />
																	</td>
																	<td class="texto_ti"><spring:message
																			code="formularioElemento.datosElemento.cajaCerrada"
																			htmlEscape="true" />
																	</td>
																</tr>
															</table></td>
													</tr>
												</c:if>	
												</sec:authorize>
														
												<c:if test="${elementoFormulario.tipoElemento.seleccionaTipoTrabajo == true}">
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioElemento.datosElemento.tipoTrabajo"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><select id="tipoTrabajo" name="tipoTrabajo"														size="1"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>>
															<option label="No especificado" value="No especificado"
																<c:if test="${elementoFormulario.tipoTrabajo == null}">
																				selected="selected"
																			</c:if>>No especificado</option>
															<option label="Digitalización" value="Digitalización"
																<c:if test="${elementoFormulario.tipoTrabajo == 'Digitalización'}">
																				selected="selected"
																			</c:if>>Digitalización</option>
															<option label="Referencia en planta" value="Referencia en planta"
																<c:if test="${elementoFormulario.tipoTrabajo == 'Referencia en planta'}">
																				selected="selected"
																			</c:if>>Referencia en planta</option>
															<option label="Referencia enviada por correo" value="Referencia enviada por correo"
																<c:if test="${elementoFormulario.tipoTrabajo == 'Referencia enviada por correo'}">
																				selected="selected"
																			</c:if>>Referencia en planta</option>
															<option label="Referencia adm. por terceros" value="Referencia adm. por terceros"
																<c:if test="${elementoFormulario.tipoTrabajo == 'Referencia adm. por terceros'}">
																				selected="selected"
																			</c:if>>Referencia adm. por terceros</option>
															<option label="Legajo Carga" value="Legajo Carga"
																<c:if test="${elementoFormulario.tipoTrabajo == 'Legajo Carga'}">
																				selected="selected"
																			</c:if>>Referenciado por terceros</option>
															<option label="Referenciado por terceros" value="Referenciado por terceros"
																<c:if test="${elementoFormulario.tipoTrabajo == 'Referenciado por terceros'}">
																				selected="selected"
																			</c:if>>Referenciado por terceros</option>
															<option label="Sin referencia" value="Sin referencia"
																<c:if test="${elementoFormulario.tipoTrabajo == 'Sin referencia'}">
																				selected="selected"
																			</c:if>>Sin referencia</option>
													</select></td>
												</tr>
												</c:if>
												<c:if test="${accion == 'CONSULTA'}">												
												<tr>
												<td class="text_ti">
												<br style="font-size: xx-small;" />
												<fieldset>
												<legend>
													<spring:message
															code="formularioElemento.datosElemento.ultimaModifPrecintos"
															htmlEscape="true" />
												</legend>
												<table>
												<tr>
													<td class="texto_ti"><strong><spring:message
															code="formularioElemento.datosElemento.usuario"
															htmlEscape="true" /></strong></td>
													<td class="texto_ti"><spring:message
															code="formularioElemento.vacio"
															htmlEscape="true" /></td>
													<td class="texto_ti">
														<label id="usuarioModificacionLabel" for="usuarioModificacion">
														<c:out value="${elementoFormulario.usuarioModificacionPrecinto.persona}"
														default="" /></label>
													</td>
													
												</tr>
												<tr>
													<td class="texto_ti"><strong><spring:message
															code="formularioElemento.datosElemento.fecha"
															htmlEscape="true" /></strong></td>
													<td class="texto_ti"><spring:message
															code="formularioElemento.vacio"
															htmlEscape="true" /></td>
													<td class="texto_ti">
														<label id="fechaModificacionLabel" for="fechaModificacion">
														<c:out value="${elementoFormulario.fechaModificacionPrecinto}"
														default="" /></label>
													</td>
												</tr>
												</table>
												</fieldset>	
												</td>
												</tr>
												<tr>
													<td>
													<fieldset>
												<legend>
													<spring:message
															code="formularioElemento.datosElemento.ultimaModifElemento"
															htmlEscape="true" />
												</legend>
												<table>
												<tr>
													<td class="texto_ti"><strong><spring:message
															code="formularioElemento.datosElemento.usuario"
															htmlEscape="true" /></strong></td>
													<td class="texto_ti"><spring:message
															code="formularioElemento.vacio"
															htmlEscape="true" /></td>
															<td class="texto_ti">
														<label id="usuarioModificacionLabel" for="usuarioModificacion">
														<c:out value="${elementoFormulario.usuarioModificacion.persona}"
														default="" /></label>
													</td>
													
												</tr>
												<tr>
													<td class="texto_ti"><strong><spring:message
															code="formularioElemento.datosElemento.fecha"
															htmlEscape="true" /></strong></td>
													<td class="texto_ti"><spring:message
															code="formularioElemento.vacio"
															htmlEscape="true" /></td>
													<td class="texto_ti">
														<label id="fechaModificacionLabel" for="fechaModificacion">
														<c:out value="${elementoFormulario.fechaModificacion}"
														default="" /></label>
													</td>
												</tr>
												</table>
												</fieldset>
													
													</td>
												</tr>
												</c:if>
											</table>
										</fieldset></td>
								</tr>
							</table>
						</div>
					</fieldset>
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
					<button id="guardar" name="guardar" type="button" onclick="this.disabled=true;guardarYSalir();"
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
					<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png"> 
						<spring:message code="botones.volver" htmlEscape="true"/>  
					</button>
				</div>						
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
	<div class="selectorDiv"></div>
</body>
</html>