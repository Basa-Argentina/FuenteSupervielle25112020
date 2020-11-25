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
		<spring:message code="formularioStock.titulo.registarAjuste"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'VER_DETALLE'}">
		<spring:message code="formularioStock.titulo.verDetalle"
			htmlEscape="true" />
	</c:if></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_stock.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/calendar_us.js"
	language="JavaScript"></script>
<script type="text/javascript" src="js/Utils.js"></script>
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
							<spring:message code="formularioStock.titulo.registarAjuste"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'VER_DETALLE'}">
							<spring:message code="formularioStock.titulo.verDetalle"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<input type="hidden" id="errorCodDepIgualDestino" value="<spring:message code="formularioStock.errorCodDepIgualDestino" htmlEscape="true"/>" >
				<input type="hidden" id="errorCodigoEmpresaReq" value="<spring:message code="formularioStock.error.codigoEmpresaReq" htmlEscape="true"/>" >
				<input type="hidden" id="errorCodigoSucursalReq" value="<spring:message code="formularioStock.error.codigoSucursalReq" htmlEscape="true"/>" >
				<input type="hidden" id="errorCodigoDepositoReq" value="<spring:message code="formularioStock.error.codigoDepositoReq" htmlEscape="true"/>" >
				<input type="hidden" id="errorTitulo" value="<spring:message code="formularioStock.errorTitulo" htmlEscape="true"/>" >
				<form:form id="formADS" name="formStock"
					action="guardarActualizarStock.html" commandName="stockFormulario"
					method="post" modelAttribute="stockFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="codigoDeposito" name="codigoDeposito"
						value="<c:out value="${stockFormulario.codigoDeposito}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="fechaAux" name="fechaAux"
						value="<c:out value="${fechaAux}" default="" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioStock.datosStock" htmlEscape="true" /> </font> <img
										id="busquedaImgSrcDown" src="images/skip_down.png"
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
												<spring:message code="formularioStock.nombreStock"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioStock.stock.fecha" htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioStock.stock.hora" htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioStock.stock.tipo" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="fecha"
														maxlength="10" name="fecha" style="width: 100px;"
														class="requerido"
														value="<c:out value="${stockFormulario.fecha}" default="" />" />
														<script type="text/javascript">
															new tcal(
																	{
																		// form name
																		'formname' : 'formStock',
																		// input name
																		'controlname' : 'fecha'
																	});
														</script></td>
													<td class="texto_ti"><input type="text"
														class="requerido" maxlength="5" id="hora" name="hora"
														style="width: 100px;" onblur="CheckTime(this)"
														value="<c:out value="${stockFormulario.hora}" default="" />" />
													</td>
													<td class="texto_ti"><select id="tipoMovimiento"
														class="requerido" name="tipoMovimiento">
															<option label="Ingreso" value="Ingreso"
																<c:if test="${stockFormulario.tipoMovimiento == 'Ingreso'}">
																			selected="selected"
																		</c:if>>Ingreso</option>
															<option label="Egreso" value="Egreso"
																<c:if test="${stockFormulario.tipoMovimiento == 'Egreso'}">
																		selected="selected"
																	</c:if>>Egreso</option>
													</select></td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioStock.stock.concepto" htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioStock.stock.cantidad" htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioStock.stock.nota" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="codigoConcepto"
																	class="requerido" name="codigoConcepto" maxlength="6"
																	style="width: 50px;"
																	value="<c:out value="${stockFormulario.concepto.codigo}" default="" />" />&nbsp;&nbsp;
														<button type="button"
																onclick="abrirPopup('conceptosPopupMap');"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>">
																<img
																	src="<%=request.getContextPath()%>/images/buscar.png">
															</button>&nbsp;&nbsp;
														<label id="codigoConceptoLabel"
															for="codigoConcepto">
															 <c:out	value="${stockFormulario.concepto.descripcion}"
																	default="" />
														 </label>&nbsp;&nbsp;
													</td>
													<td class="texto_ti"><input type="text"
														class="requerido" maxlength="9" id="cantidad"
														name="cantidad" style="width: 80px;"
														value="<c:out value="${stockFormulario.cantidad}" default="" />" />
													</td>
													<td class="texto_ti"><input type="text"
														class="requerido" maxlength="60" id="nota" name="nota"
														style="width: 350px;"
														value="<c:out value="${stockFormulario.nota}" default="" />" />
													</td>
												</tr>
											</table>

											
										</fieldset></td>
								</tr>
								<tr>
									<td>
										<fieldset>
											<legend><spring:message code="formularioStock.destino" htmlEscape="true"/></legend>
											<table >
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioStock.empresa" htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioStock.sucursal" htmlEscape="true" /></td>
													<td class="texto_ti"><spring:message
															code="formularioStock.deposito" htmlEscape="true" /></td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text"
														id="codigoEmpresa" name="codigoEmpresa" maxlength="6"
														style="width: 50px;"
														value="<c:out value="${stockFormulario.origenDestino.sucursal.empresa.codigo}" default="" />" />
														&nbsp;&nbsp;
														<button type="button" id="buttonCodigoEmpresa"
															onclick="abrirPopupEmpresa('empresasPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>">
															<img
																src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
														for="codigoEmpresa"> <c:out
																value="${stockFormulario.origenDestino.sucursal.empresa.descripcion}"
																default="" /> </label></td>
													<td class="texto_ti"><input type="text"
														id="codigoSucursal" name="codigoSucursal" maxlength="6"
														style="width: 50px;"
														value="<c:out value="${stockFormulario.origenDestino.sucursal.codigo}" default="" />" />
														&nbsp;&nbsp;
														<button type="button" id="buttonCodigoSucursal"
															onclick="abrirPopupSucursal('sucursalesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>'
																						, '<spring:message code="formularioStock.errorTitulo" htmlEscape="true"/>');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>">
															<img
																src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
														for="codigoSucursal"> <c:out
																value="${stockFormulario.origenDestino.sucursal.descripcion}"
																default="" /> </label></td>
													<td class="texto_ti"><input type="text"
														id="codigoOrigenDestino" name="codigoOrigenDestino"
														maxlength="6" style="width: 50px;"
														value="<c:out value="${stockFormulario.origenDestino.codigo}" default="" />" />
														&nbsp;&nbsp;
														<button type="button" id="buttonCodigoOrigenDestino"
															onclick="abrirPopupDeposito('depositosPopupMap', '<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>'
																						, '<spring:message code="formularioStock.errorTitulo" htmlEscape="true"/>');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>">
															<img
																src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; <label id="codigoOrigenDestinoLabel"
														for="codigoOrigenDestino"> <c:out
																value="${stockFormulario.origenDestino.descripcion}"
																default="" /> </label></td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<c:if test="${accion == 'NUEVO'}">
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
				<br style="font-size: xx-small;" />
				<fieldset id="detalle">
					<display:table name="stocks" id="stock" requestURI="" pagesize="20"
						sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${stock.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column style="width:100px;" property="tipoMovimiento"
							titleKey="formularioStock.stock.tipo" sortable="true"
							class="celdaAlineadoCentrado" />
						<display:column style="width:100px;" property="fechaStr"
							titleKey="formularioStock.stock.fecha" sortable="true"
							class="celdaAlineadoCentrado" />
						<display:column property="hora"
							titleKey="formularioStock.stock.hora" sortable="true"
							class="celdaAlineadoCentrado" />
						<display:column property="nota"
							titleKey="formularioStock.stock.nota" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="depositoDescripcion"
							titleKey="formularioStock.deposito" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column
							value="${stock.origenDestinoDescripcion == null && stock.tipoMovimiento == 'Egreso' ? 'Venta' : (stock.origenDestinoDescripcion == null && stock.tipoMovimiento == 'Ingreso' ? 'Compra' : stock.origenDestinoDescripcion ) }"
							titleKey="formularioStock.stock.origenDestino" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column style="width:80px;" property="cantidad"
							titleKey="formularioStock.stock.cantidad" sortable="true"
							class="celdaAlineadoDerecha" />
						<display:column style="width:80px;" property="acumulado"
							titleKey="formularioStock.stock.acumulado" sortable="true"
							class="celdaAlineadoDerecha" />
						<display:column class="celdaAlineadoCentrado">
							<img id="information"
								src="<%=request.getContextPath()%>/images/information.png"
								title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
						</display:column>
					</display:table>
				</fieldset>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion == 'NUEVO'}">
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
			<c:if test="${accion == 'VER_DETALLE'}">
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
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
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
		<jsp:param name="mapa" value="conceptosPopupMap" />
		<jsp:param name="clase" value="conceptosPopupMap" />
	</jsp:include>
</body>
</html>