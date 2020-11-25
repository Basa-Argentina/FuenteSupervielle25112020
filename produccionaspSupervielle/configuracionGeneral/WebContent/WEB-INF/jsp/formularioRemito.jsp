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
		<spring:message code="formularioRemito.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioRemito.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioRemito.titulo.modificar"
			htmlEscape="true" />
	</c:if></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_remito.js"></script>
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
							<spring:message code="formularioRemito.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioRemito.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioRemito.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formRemito"
					action="guardarActualizarRemito.html"
					commandName="remitoFormulario" method="post"
					modelAttribute="remitoFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${remitoFormulario.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="actualizaNumero" name="actualizaNumero"
						value="<c:out value="${actualizaNumero}" default="" />" />
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" maxlength="6" style="width: 50px;"
						value="<c:out value="${remitoFormulario.codigoEmpresa}" default="" />"/>
					<input type="hidden" id="codigoSucursal" name="codigoSucursal" maxlength="6" style="width: 50px;"
						value="<c:out value="${remitoFormulario.codigoSucursal}" default="" />"/>
					<input type="hidden" id="verificoMovHdn" name="verificoMovHdn"
						value="<c:out value="${verificoMovHdn}" default="" />" 
					/>
						
					
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioRemito.datosRemito" htmlEscape="true" />
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
												<spring:message	code="formularioRemito.tipoRemito" htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td>
													<input type="radio" id="tipoRemito" name="tipoRemito" value="cliente" 
														<c:if test="${(accion != 'NUEVO') && (remitoFormulario.tipoRemito == 'interno')}">
															disabled="disabled"
														</c:if>
														<c:if test="${remitoFormulario.tipoRemito != 'interno'}">
															checked="checked"
														</c:if>/>
														<spring:message	code="formularioRemito.cliente" htmlEscape="true" />
													</td>
													<td>
													<input type="radio" id="tipoRemito" name="tipoRemito" value="interno" 
														<c:if test="${(accion != 'NUEVO') && (remitoFormulario.tipoRemito == 'cliente')}">
																disabled="disabled"
														</c:if>
														<c:if test="${remitoFormulario.tipoRemito == 'interno'}">
															checked="checked"
														</c:if>/>
														<spring:message	code="formularioRemito.interno" htmlEscape="true" />
													</td>

													<td class="texto_ti"><spring:message
															code="formularioRemito.vacio" htmlEscape="true" /></td>

													<td id="tdEgresoIngreso">
														<fieldset>
															<legend>
																<spring:message code="formularioRemito.datosRemito.ingresoEgreso"
																	htmlEscape="true" />
															</legend>
															<table>
																<tr>
																<td><input type="radio" id="ingresoEgreso"
																		name="ingresoEgreso" value="egreso"
																		<c:if test="${(accion != 'NUEVO') && (remitoFormulario.ingresoEgreso == 'ingreso')}">
																disabled="disabled"
														</c:if>
																		<c:if test="${remitoFormulario.ingresoEgreso != 'ingreso'}">
															checked="checked"
														</c:if> />
																		<spring:message code="formularioRemito.egreso"
																			htmlEscape="true" /></td>
																	<td><input type="radio" id="ingresoEgreso"
																		name="ingresoEgreso" value="ingreso"
																		<c:if test="${(accion != 'NUEVO') && (remitoFormulario.ingresoEgreso == 'egreso')}">
															disabled="disabled"
														</c:if>
																		<c:if test="${remitoFormulario.ingresoEgreso == 'ingreso'}">
															checked="checked"
														</c:if> />
																		<spring:message code="formularioRemito.ingreso"
																			htmlEscape="true" /></td>
																	
																</tr>
															</table>
														</fieldset>
														</td>
														<td class="texto_ti"><spring:message
															code="formularioRemito.vacio" htmlEscape="true" /></td>
														<td class="texto_ti"><input type="checkBox"
																	id="verificaLectura" name="verificaLectura"
																	style="width: 10px;" value="true"
																	<c:if test="${remitoFormulario.verificaLectura!=null && remitoFormulario.verificaLectura == true}">
																		checked="checked"
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if> />
																</td>
																<td class="texto_ti">
																		<spring:message
																		code="formularioRemito.verificaLectura"
																		htmlEscape="true" />
																</td>

												</tr>
											</table>
											</fieldset>
											
											
										<fieldset>
											<legend>
												<spring:message code="formularioRemito.nombreRemito"
													htmlEscape="true" />
											</legend>
											<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti" id="tdtituloDepositoOrigen"><spring:message
												code="formularioRemito.datosRemito.depositoOrigen" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloCliente"><spring:message
												code="formularioRemito.cliente" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloDepositoDestino"><spring:message
												code="formularioRemito.datosRemito.depositoDestino" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.serie" htmlEscape="true" />
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti" id="tdcodigoDepositoOrigen"><input type="text" id="codigoDepositoOrigen"
											name="codigoDepositoOrigen" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${remitoFormulario.codigoDepositoOrigen}" default="" />"
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
											</button>&nbsp;&nbsp; <label id="codigoDepositoOrigenLabel"
											for="codigoDepositoOrigen"> <c:out
													value="${remitoFormulario.depositoOrigen.descripcion}"
													default="" /> </label>
										</td>
										<td class="texto_ti" id="tdcodigoCliente"><input type="text" id="codigoCliente"
											name="codigoCliente" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${remitoFormulario.codigoCliente}" default="" />"
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
											</button>&nbsp;&nbsp; <label id="codigoClienteLabel"
											for="codigoCliente"> <c:out
													value="${remitoFormulario.clienteEmp.razonSocialONombreYApellido}"
													default="" /> </label>
													</td>
											<td class="texto_ti" id="tdcodigoDepositoDestino"><input type="text" id="codigoDepositoDestino"
											name="codigoDepositoDestino" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${remitoFormulario.codigoDepositoDestino}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
											<c:if test="${accion == 'MODIFICACION' && remitoFormulario.fechaImpresion != null}">
															disabled="disabled"
													</c:if>>
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupDepositoDestino('depositosDestinoPopupMap', '<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
													<c:if test="${accion == 'MODIFICACION' && remitoFormulario.fechaImpresion != null}">
															disabled="disabled"
														</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoDepositoDestinoLabel"
											for="codigoDepositoDestino"> <c:out
													value="${remitoFormulario.depositoDestino.descripcion}"
													default="" /> </label>
										</td>			
										<td class="texto_ti"><input type="text" id="codigoSerie"
											name="codigoSerie" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${remitoFormulario.codigoSerie}" default="" />"
											<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupSerie('seriesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion != 'NUEVO'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoSerieLabel"
											for="codigoRemito"> <c:out
													value="${remitoFormulario.serie.descripcion}"
													default="" /> </label>
										</td>
										
										</tr>
										<tr>
										<td class="texto_ti"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloPersonal"><spring:message
												code="formularioRemito.personal"
												htmlEscape="true" /></td>
										<td class="texto_ti" id="tdtituloPersonalVacio"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.datosRemito.numero" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" />
										</td>
									</tr>
									<tr>
										
										<td class="texto_ti"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" />
										</td>			
										<td class="texto_ti" id="tdcodigoPersonal"><input type="text" id="codigoPersonal"
											name="codigoPersonal" maxlength="6" style="width: 50px;" class="requerido"
											value="<c:out value="${remitoFormulario.codigoPersonal}" default="" />"
											<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupPersonal('personalPopupMap', '<spring:message code="notif.stock.seleccionCliente" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion != 'NUEVO'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoPersonalLabel"
											for="codigoPersonal"> <c:out
													value="${remitoFormulario.empleado.nombreYApellido}"
													default="" /> </label>
										</td>
										<td class="texto_ti" id="tdcodigoPersonalVacio"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" />
										</td>	
										<td class="texto_ti">
									<input type="text" id="prefijo"
										name="prefijo" maxlength="4" size="4" class="requerido"
										value="<c:out value="${remitoFormulario.prefijo}" default="" />" readonly="readonly"/>
									-
									<input type="text" id="numeroSinPrefijo"
										name="numeroSinPrefijo" maxlength="8" size="8" class="requerido"
										value="<c:out value="${remitoFormulario.numeroSinPrefijo}" default="" />" <c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>/>
									</td>
																																							
									</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioRemito.transporte" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloDireccion"><spring:message
												code="formularioRemito.direccion" htmlEscape="true" />
										</td>
										<td class="texto_ti" id="tdtituloDireccionVacio"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.datosRemito.fechaEmision" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.datosRemito.fechaEntrega" htmlEscape="true" />
										</td>
									</tr>
									<tr>
									<td class="texto_ti"><input type="text"
										id="codigoTransporte" name="codigoTransporte" maxlength="6"
										style="width: 50px;" class="requerido"
										value="<c:out value="${remitoFormulario.codigoTransporte}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>
											<c:if test="${accion == 'MODIFICACION' && remitoFormulario.fechaImpresion != null}">
															readonly="readonly"
														</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupTransporte('transportesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
											<c:if test="${accion == 'MODIFICACION' && remitoFormulario.fechaImpresion != null}">
															disabled="disabled"
													</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoTransporteLabel"
										for="codigoTransporte"> <c:out
												value="${remitoFormulario.transporte.descripcion}" default="" />
									</label>
									</td>
									<td class="texto_ti" id="tdcodigoDireccion"><input type="text"
										id="codigoDireccion" name="codigoDireccion" maxlength="6"
										style="width: 50px;" class="requerido"
										value="<c:out value="${remitoFormulario.codigoDireccion}" default="" />"
										<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupDireccion('direccionesPopupMap', '<spring:message code="notif.stock.seleccionCliente" htmlEscape="true"/>'
																			, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO'}">
														disabled="disabled"
													</c:if>>
											<img
												src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; 
									</td>
									<td class="texto_ti" id="tdcodigoDireccionVacio"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" />
										</td>
									<td class="texto_ti">
														<input type="text" id=fechaEmision name="fechaEmision" 
															maxlength="10" class="requerido"
															value="<c:out value="${remitoFormulario.fechaEmisionStr}" default="" />"
															<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
															
												<c:if test="${accion == 'NUEVO'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formRemito',
																	// input name
																	'controlname': 'fechaEmision'
																});
															</script></c:if>
										</td>										
										<td class="texto_ti">
														<input type="text" id="fechaEntrega" name="fechaEntrega" 
															maxlength="10" 
															value="<c:out value="${remitoFormulario.fechaEntregaStr}" default="" />" 
															<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
														
													<c:if test="${accion == 'NUEVO'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formRemito',
																	// input name
																	'controlname': 'fechaEntrega'
																});
															</script></c:if>
													</td>
								</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioRemito.vacio" htmlEscape="true" /></td>
										<td class="texto_ti"><label id="codigoDireccionLabel"
											for="codigoDireccion"> <c:out
													value="${remitoFormulario.direccion.descripcion}"
													default="" /> </label></td>
													
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
					<table style="width: 100%;" id="tablaUnica">
						<tr style="width: 100%;">
							<td style="width: 100%;">
					<display:table name="remitoDetalles" id="detalles" requestURI="precargaFormularioRemito.html"
						pagesize="40" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${remitoDetalle.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column property="elemento.tipoElemento.descripcionStr"
							titleKey="formularioLectura.lecturaDetalle.tipo" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column
							property="elemento.codigo" titleKey="formularioRemito.elemento"
							sortable="true" class="celdaAlineadoIzquierda" />
						<display:column
							property="elemento.estado" titleKey="formularioRemito.datosRemito.estado"
							sortable="true" class="celdaAlineadoIzquierda" />
						<display:column
							property="elemento.clienteEmp.razonSocialONombreYApellido" titleKey="formularioRemito.cliente"
							sortable="true" class="celdaAlineadoIzquierda" />
					</display:table>
					</td>
					</tr>
					</table>
					<table style="width: 100%;" id="tablaDoble">
						<tr style="width: 100%;">
							<td style="width: 50%;">
								<fieldset>
								<legend>Nuevos GyC</legend>
								<display:table name="sessionScope.listaNuevos" id="movNuevo" requestURI="precargaFormularioRemito.html"
								pagesize="40" sort="list" keepStatus="true">
									<display:column
									property="elemento.codigo" titleKey="formularioRemito.elemento"
									sortable="true" class="celdaAlineadoIzquierda" />
								</display:table>
								</fieldset>
							</td>
							<td style="width: 50%;">
								<fieldset>
								<legend>Devolucion</legend>
								<display:table name="sessionScope.listaDevolucion" id="movDevolucion" requestURI="precargaFormularioRemito.html"
								pagesize="40" sort="list" keepStatus="true">
									<display:column
									property="elemento.codigo" titleKey="formularioRemito.elemento"
									sortable="true" class="celdaAlineadoIzquierda" />
								</display:table>
								</fieldset>
							</td>
						</tr>
					</table>
					<c:if test="${accion != 'CONSULTA'}"><table><tr><td class="texto_ti" width="50%">
					<button type="button" onclick="abrirPopupCargaElementos();" class="botonCentradoGrande">
						<spring:message code="formularioRemito.botonAgregar" htmlEscape="true" />
						<img src="<%=request.getContextPath()%>/images/add.png">
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
								<td class="texto_ti"><spring:message
										code="formularioRemito.datosRemito.observaciones"
										htmlEscape="true" />
								</td>
								<td class="texto_ti" style="width: 25%;"><spring:message
										code="formularioRemito.vacio"
										htmlEscape="true" />
								</td>
								<td class="texto_ti"><spring:message
										code="formularioRemito.vacio"
										htmlEscape="true" />
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
								<textarea id="observacion" name="observacion" style="width: 445px;"
									<c:if test="${accion == 'CONSULTA'}">
												readonly="readonly"
											</c:if>><c:out value="${remitoFormulario.observacion}" default="" /></textarea>
								</td>
								<td class="texto_ti"><spring:message
										code="formularioRemito.vacio"
										htmlEscape="true" />
								</td>
								<td class="texto_ti"><spring:message
										code="formularioRemito.cantidad"
										htmlEscape="true" />
								</td>
								<td class="texto_ti">${remitoFormulario.cantidadElementos}</td>
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
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="seriesPopupMap" /> 
			<jsp:param name="clase" value="seriesPopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="transportesPopupMap" /> 
			<jsp:param name="clase" value="transportesPopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="personalPopupMap" /> 
			<jsp:param name="clase" value="personalPopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="direccionesPopupMap" />
		<jsp:param name="clase" value="direccionesPopupMap" />
		</jsp:include>
		<jsp:include page="popupCargaElementos.jsp">
		<jsp:param name="mapa" value="direccionesPopupMap" />
		<jsp:param name="clase" value="direccionesPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="lecturasPopupMap" /> 
			<jsp:param name="clase" value="lecturasPopupMap" /> 
		</jsp:include>
</body>
</html>