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
		<spring:message code="formularioMovimiento.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioMovimiento.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioMovimiento.titulo.modificar"
			htmlEscape="true" />
	</c:if></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_movimiento.js"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
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
							<spring:message code="formularioMovimiento.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioMovimiento.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioMovimiento.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formMovimiento" action="guardarActualizarMovimiento.html" commandName="movimientoFormulario" method="post"
					modelAttribute="movimientoFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="listaTipoTrabajo" name="listaTipoTrabajo"
						value="" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${movimientoFormulario.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" maxlength="6" style="width: 50px;"
						value="<c:out value="${codigoEmpresa}" default="" />"/>
					<input type="hidden" id="codigoSucursalActual" name="codigoSucursalActual" maxlength="6" style="width: 50px;"
						value="<c:out value="${codigoSucursalActual}" default="" />"/>
						
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioMovimiento.datosMovimiento" htmlEscape="true" />
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
												<spring:message	code="formularioMovimiento.claseMovimiento" htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td>
													<input type="radio" id="claseMovimiento" name="claseMovimiento" value="cliente" 
														<c:if test="${(accion != 'NUEVO') && (movimientoFormulario.claseMovimiento == 'interno')}">
															disabled="disabled"
														</c:if>
														<c:if test="${movimientoFormulario.claseMovimiento != 'interno'}">
															checked="checked"
														</c:if>/>
														<spring:message	code="formularioMovimiento.cliente" htmlEscape="true" />
													</td>
													<td>
													<input type="radio" id="claseMovimiento" name="claseMovimiento" value="interno" 
														<c:if test="${(accion != 'NUEVO') && (movimientoFormulario.claseMovimiento == 'cliente')}">
																disabled="disabled"
														</c:if>
														<c:if test="${movimientoFormulario.claseMovimiento == 'interno'}">
															checked="checked"
														</c:if>/>
														<spring:message	code="formularioMovimiento.interno" htmlEscape="true" />
													</td>

													<td class="texto_ti"><spring:message
															code="formularioMovimiento.vacio" htmlEscape="true" /></td>

													<td id="tdEgresoIngreso">
														<fieldset>
															<legend>
																<spring:message code="formularioMovimiento.datosMovimiento.tipoMovimiento"
																	htmlEscape="true" />
															</legend>
															<table>
																<tr>
																<td><input type="radio" id="tipoMovimiento"
																		name="tipoMovimiento" value="EGRESO"
																		<c:if test="${(accion != 'NUEVO') && (movimientoFormulario.tipoMovimiento == 'INGRESO')}">
																disabled="disabled"
														</c:if>
																		<c:if test="${movimientoFormulario.tipoMovimiento != 'INGRESO'}">
															checked="checked"
														</c:if> />
																		<spring:message code="formularioMovimiento.egreso"
																			htmlEscape="true" /></td>
																	<td><input type="radio" id="tipoMovimiento"
																		name="tipoMovimiento" value="INGRESO"
																		<c:if test="${(accion != 'NUEVO') && (movimientoFormulario.tipoMovimiento == 'EGRESO')}">
															disabled="disabled"
														</c:if>
																		<c:if test="${movimientoFormulario.tipoMovimiento == 'INGRESO'}">
															checked="checked"
														</c:if> />
																		<spring:message code="formularioMovimiento.ingreso"
																			htmlEscape="true" /></td>
																	
																</tr>
															</table>
														</fieldset>
													</td>
												
												</tr>
											</table>
											</fieldset>
										<fieldset>
											<legend>
												<spring:message code="formularioMovimiento.nombreMovimiento"
													htmlEscape="true" />
											</legend>
											<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti" id="tdtituloDepositoActual"><spring:message
												code="formularioMovimiento.depositoActual" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloCliente"><spring:message
												code="formularioMovimiento.clienteOrigen" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloClienteDestino"><spring:message
												code="formularioMovimiento.clienteDestino" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloSucursalDestino"><spring:message
												code="formularioMovimiento.datosMovimiento.sucursalDestino" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloDepositoDestino"><spring:message
												code="formularioMovimiento.datosMovimiento.depositoDestino" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloSucursalOrigen"><spring:message
												code="formularioMovimiento.datosMovimiento.sucursalOrigen" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloDepositoOrigen"><spring:message
												code="formularioMovimiento.datosMovimiento.depositoOrigen" htmlEscape="true" />
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti" id="tdcodigoDepositoActual"><input type="text" id="codigoDepositoActual"
											name="codigoDepositoActual" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${movimientoFormulario.codigoDepositoActual}" default="" />"
											<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupDepositoOrigen('depositosOrigenPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion != 'NUEVO'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoDepositoActualLabel"
											for="codigoDepositoActual"> <c:out
													value="${movimientoFormulario.deposito.descripcion}"
													default="" /> </label>
										</td>
										<td class="texto_ti" id="tdcodigoCliente"><input type="text" id="codigoClienteEmp"
											name="codigoClienteEmp" maxlength="6" style="width: 50px;" 
											value="<c:out value="${movimientoFormulario.codigoClienteEmp}" default="" />"
											<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupCliente('clientesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion != 'NUEVO'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoClienteEmpLabel"
											for="codigoClienteEmp"> <c:out
													value="${movimientoFormulario.clienteEmpOrigenDestino.razonSocialONombreYApellido}"
													default="" /> </label>
													</td>
													
											<td class="texto_ti" id="tdcodigoSucursalDestino">
											<input type="text" id="codigoSucursalOrigenDestino"
											name="codigoSucursalOrigenDestino" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${movimientoFormulario.codigoSucursalOrigenDestino}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
											<c:if test="${accion == 'MODIFICACION'}">
															disabled="disabled"
													</c:if>>
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupSucursal('sucursalesPopupMap', '<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
													<c:if test="${accion == 'MODIFICACION'}">
															disabled="disabled"
														</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoSucursalOrigenDestinoLabel"
											for="codigoSucursalOrigenDestino"> <c:out
													value="${movimientoFormulario.depositoOrigenDestino.sucursal.descripcion}"
													default="" /> </label>
											
											</td>
													
											<td class="texto_ti" id="tdcodigoDepositoDestino">
											<input type="text" id="codigoDepositoOrigenDestino"
											name="codigoDepositoOrigenDestino" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${movimientoFormulario.codigoDepositoOrigenDestino}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
											<c:if test="${accion == 'MODIFICACION'}">
															disabled="disabled"
													</c:if>>
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupDepositoDestino('depositosDestinoPopupMap', '<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
													<c:if test="${accion == 'MODIFICACION'}">
															disabled="disabled"
														</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoDepositoOrigenDestinoLabel"
											for="codigoDepositoOrigenDestino"> <c:out
													value="${movimientoFormulario.depositoOrigenDestino.descripcion}"
													default="" /> </label>
										</td>			
										</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioMovimiento.fecha" htmlEscape="true" />
										</td>	
										<td class="texto_ti"><spring:message
												code="formularioMovimiento.responsable" htmlEscape="true" />
										</td>									
									</tr>
									<tr>
									<td class="texto_ti">
														<input type="text" id=fecha name="fecha" 
															maxlength="10" class="requerido"
															value="<c:out value="${movimientoFormulario.fechaStr}" default="" />"
															<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
															
												<c:if test="${accion == 'NUEVO'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formMovimiento',
																	// input name
																	'controlname': 'fecha'
																});
															</script></c:if>
										</td>
										<td class="texto_ti">
								    		<input type="text" id="codigoResponsable"
											name="codigoResponsable" maxlength="6" style="width: 50px;"
											value="<c:out value="${movimientoFormulario.codigoResponsable}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
											<c:if test="${accion == 'MODIFICACION'}">
															disabled="disabled"
													</c:if>>
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupResponsables('responsablesPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
													<c:if test="${accion == 'MODIFICACION'}">
															disabled="disabled"
														</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoResponsableLabel"
											for="codigoResponsable"> </label>
								    	</td>									
								    </tr>
								    <tr>
								    	<td class="texto_ti" colspan="2">
								    		<spring:message code="formularioMovimiento.descripcion" htmlEscape="true" />
								    	</td>
								    </tr>
								    <tr>
								    	<td class="texto_ti" colspan="2">
											<textarea id="descripcion" name="descripcion" rows="2" cols="40">
												<c:out value="${movimientoFormulario.descripcion}" default="" />
											</textarea>
										</td>
								    </tr>									
								</table>
							</fieldset>
							</td>
								</tr>
							</table>
						</div>
					</fieldset>
					<br style="font-size: xx-small;" />
					<fieldset>
					<legend>
						<spring:message code="formularioLectura.lecturaDetalle"
							htmlEscape="true" />
					</legend>
					<display:table name="listaElementos" id="elemento" requestURI="precargaFormularioMovimiento.html"
						pagesize="40" sort="list" keepStatus="true">
						<display:column class="celdaAlineadoCentrado" title="<input type='checkbox' id='checktodos' name='checktodos'/>">
						    <input type="checkbox" class='checklote' value="${elemento.codigo}"/>
		              	</display:column>	
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id"
								value="${elemento.id}" />
						</display:column>
						<display:column property="tipoElemento.descripcionStr"
							titleKey="formularioLectura.lecturaDetalle.tipo" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column
							property="codigo" titleKey="formularioMovimiento.elemento"
							sortable="true" class="celdaAlineadoIzquierda" />
						<display:column
							property="estado" titleKey="formularioMovimiento.datosMovimiento.estado"
							sortable="true" class="celdaAlineadoIzquierda" />
						<display:column
							property="clienteEmp.razonSocialONombreYApellido" titleKey="formularioMovimiento.cliente"
							sortable="true" class="celdaAlineadoIzquierda" />	
						<display:column
							property="depositoActual.descripcion" titleKey="formularioMovimiento.depositoActual"
							sortable="true" class="celdaAlineadoIzquierda" />
						<display:column titleKey="formularioMovimiento.detalles.tipoTrabajo"
							sortable="false" class="trabajo celdaAlineadoIzquierda">
								<c:if test="${elemento.estado == 'En el Cliente'}">
									<select id="tipoTrabajo" name="tipoTrabajo"	size="1" onchange="listadoTipoTrabajo();"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>>
															<option label="No especificado" value="No especificado"
																<c:if test="${elemento.tipoTrabajo == null}">
																				selected="selected"
																			</c:if>>No especificado</option>
															<option label="Digitalización" value="Digitalización"
																<c:if test="${elemento.tipoTrabajo == 'Digitalización'}">
																				selected="selected"
																			</c:if>>Digitalización</option>
															<option label="Referencia en planta" value="Referencia en planta"
																<c:if test="${elemento.tipoTrabajo == 'Referencia en planta'}">
																				selected="selected"
																			</c:if>>Referencia en planta</option>
															<option label="Referencia enviada por correo" value="Referencia enviada por correo"
																<c:if test="${elemento.tipoTrabajo == 'Referencia enviada por correo'}">
																				selected="selected"
																			</c:if>>Referencia en planta</option>
															<option label="Referencia adm. por terceros" value="Referencia adm. por terceros"
																<c:if test="${elemento.tipoTrabajo == 'Referencia adm. por terceros'}">
																				selected="selected"
																			</c:if>>Referencia adm. por terceros</option>
														    <option label="Legajo Carga" value="Legajo Carga"
																<c:if test="${elemento.tipoTrabajo == 'Legajo Carga'}">
																				selected="selected"
																			</c:if>>Referenciado por terceros</option>
															<option label="Referenciado por terceros" value="Referenciado por terceros"
																<c:if test="${elemento.tipoTrabajo == 'Referenciado por terceros'}">
																				selected="selected"
																			</c:if>>Referenciado por terceros</option>
															<option label="Sin referencia" value="Sin referencia"
																<c:if test="${elemento.tipoTrabajo == 'Sin referencia'}">
																				selected="selected"
																			</c:if>>Sin referencia</option>
													</select>
								</c:if>
							</display:column>				
					</display:table>
					<c:if test="${accion != 'CONSULTA'}"><table><tr><td class="texto_ti" width="50%">
					<button type="button" onclick="abrirPopupCargaElementos();" class="botonCentradoGrande">
						<spring:message code="formularioMovimiento.botonAgregar" htmlEscape="true" />
						<img src="<%=request.getContextPath()%>/images/add.png">
					</button>
					<button type="button" id="botonQuitar" name="botonQuitar" class="botonCentradoGrande">
						<spring:message code="formularioMovimiento.botonQuitar" htmlEscape="true" />
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
					</button>
						</td>
						<td class="texto_td" width="50%">
						<spring:message
								code="formularioPosicionLibre.posicionLibreDetalle.lectura"
								htmlEscape="true" />
							<input type="text" id="codigoLectura" name="codigoLectura"
								maxlength="6" style="width: 50px;"
								value="<c:out value="${codigoLectura}" default="" />" />
						&nbsp;&nbsp;
						<button type="button"
								onclick="abrirPopupLectura('lecturasPopupMap');"
								title="<spring:message code="textos.buscar" htmlEscape="true"/>">
								<img src="<%=request.getContextPath()%>/images/buscar.png">
							</button>
						&nbsp;&nbsp; <label id="codigoLecturaLabel" for="codigoLectura">
								<c:out value="${descripcion}" default="" /> </label>
							<input type="button" name="importar" id="importar"
								class="botonCentrado"
								value="<spring:message code="botones.importar" htmlEscape="true"/>" />
								<input type="checkbox" id="anexar" value="true"
													name="anexar" checked="checked"
													<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if> /><spring:message
														code="formularioLectura.lecturaDetalle.anexar"
														htmlEscape="true" />
						</td>
						</tr>
					</table></c:if>
					</fieldset>
					<br style="font-size: xx-small;" />
					<fieldset>
						<table>
							<tr>
								
								<td class="texto_ti" style="width: 25%;"><spring:message
										code="formularioMovimiento.vacio"
										htmlEscape="true" />
								</td>
								<td class="texto_ti"><spring:message
										code="formularioMovimiento.vacio"
										htmlEscape="true" />
								</td>
							</tr>
							<tr>
								
								<td class="texto_ti"><spring:message
										code="formularioMovimiento.vacio"
										htmlEscape="true" />
								</td>
								<td class="texto_ti"><spring:message
										code="formularioMovimiento.cantidad"
										htmlEscape="true" />
								</td>
								<td class="texto_ti">${movimientoFormulario.cantidadElementos}</td>
							</tr>
						</table>
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
	<div class="selectorDiv"></div>
	<div id="pop" style="display:none">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
	</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="clientesPopupMap" /> 
			<jsp:param name="clase" value="clientesPopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="depositosDestinoPopupMap" />
			<jsp:param name="clase" value="depositosDestinoPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="depositosOrigenPopupMap" />
			<jsp:param name="clase" value="depositosOrigenPopupMap" />
		</jsp:include>
		<jsp:include page="popupCargaElementos.jsp">
		<jsp:param name="mapa" value="direccionesPopupMap" />
		<jsp:param name="clase" value="direccionesPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="sucursalesPopupMap" />
		<jsp:param name="clase" value="sucursalesPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="lecturasPopupMap" /> 
			<jsp:param name="clase" value="lecturasPopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="responsablesPopupMap" /> 
			<jsp:param name="clase" value="responsablesPopupMap" /> 
		</jsp:include>

</body>
</html>