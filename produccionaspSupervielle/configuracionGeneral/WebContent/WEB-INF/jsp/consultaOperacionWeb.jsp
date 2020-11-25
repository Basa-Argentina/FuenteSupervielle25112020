<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ page buffer = "64kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><spring:message
		code="formularioOperacion.titulo" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/mavalos_consulta_operacion_web.js"></script>
<script type="text/javascript" src="js/busquedaHelper.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>

<!-- <script type="text/javascript" src="js/jquery.tooltip.js" language="JavaScript"></script> -->
<!-- <script type="text/javascript" src="js/tooltip.js" language="JavaScript"></script> -->

</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>
			<li id="consultar" value=""><img src="images/consultar.png" /><font
				size="2"><spring:message code="formularioOperacion.elementos"
						htmlEscape="true" />
			</font>
			</li>
		</ul>
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioOperacion.titulo"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarOperacionWeb.html" name="formBusqueda"
					commandName="operacionBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="mensajeSeleccioneCliente" name="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
					<input type="hidden" name="preguntaFinalizarOK" id="preguntaFinalizarOK" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarOK" htmlEscape="true"/>"></input>		
					<input type="hidden" name="preguntaFinalizarOKConTraspaso" id="preguntaFinalizarOKConTraspaso" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarOKConTraspaso" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaTraspaso" id="preguntaTraspaso" value="<spring:message code="formularioOperacionReferencia.pregunta.traspaso" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaFinalizarError" id="preguntaFinalizarError" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarError" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaFinalizarErrorConTraspaso" id="preguntaFinalizarErrorConTraspaso" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarErrorConTraspaso" htmlEscape="true"/>"></input>
					<input type="hidden" name="errorFinalizarPendienteOEnProceso" id="errorFinalizarPendienteOEnProceso" value="<spring:message code="formularioOperacion.errorFinalizarPendienteOEnProceso" htmlEscape="true"/>"></input>
					<input type="hidden" name="errorElementosPendientes" id="errorElementosPendientes" value="<spring:message code="formularioOperacion.errorElementosPendientes" htmlEscape="true"/>"></input>
					<input type="hidden" name="errorCancelarPendienteOEnProceso" id="errorCancelarPendienteOEnProceso" value="<spring:message code="formularioOperacion.errorCancelarPendienteOEnProceso" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaCancelar" id="preguntaCancelar" value="<spring:message code="formularioOperacion.pregunta.cancelar" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaFinalizarOKConTraspasoSinElementos" id="preguntaFinalizarOKConTraspasoSinElementos" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarOKConTraspasoSinElementos" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaFinalizarOKSinElementos" id="preguntaFinalizarOKSinElementos" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarOKSinElementos" htmlEscape="true"/>"></input>
					<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
					
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="textos.buscar" htmlEscape="true" /> </font> <img
										id="busquedaImgSrcDown" src="images/skip_down.png"
										title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
										style="DISPLAY: none"
										title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"
							id="busquedaDiv" align="center">
							<table class="busqueda"
								style="width: 100%; background-color: white;">
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioLoteReferencia.busqueda.empresa" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteReferencia.busqueda.sucursal" htmlEscape="true"/>
									</td>		
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.deposito" htmlEscape="true" />
									</td>	
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.tipoOperacion" htmlEscape="true" />
									</td>
									
									<td rowspan="8" style="vertical-align: middle;">
										<button name="buscar" class="botonCentrado" type="submit">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
											<input type="text" id="codigoEmpresa" name="codigoEmpresa" style="width: 50px;" readonly="readonly"
												value="<c:out value="${operacionBusqueda.codigoEmpresa}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												
													disabled="disabled"
												
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoEmpresaLabel" for="codigoEmpresa"> 
												 
											</label>
										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoSucursal" name="codigoSucursal" style="width: 50px;" readonly="readonly"
												value="<c:out value="${operacionBusqueda.codigoSucursal}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaSucursal"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												
													disabled="disabled"
												
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoSucursalLabel" for="codigoSucursal"> 
												 
											</label>
										</td>
									<td class="texto_ti">
										<input type="text" id="codigoDeposito"
											name="codigoDeposito" maxlength="6" style="width: 50px;"
											value="<c:out value="${operacionBusqueda.codigoDeposito}" default="" />"
											 />
											&nbsp;&nbsp;
											<button type="button" id="buscaDeposito"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoDepositoLabel"
											for="codigoDeposito"> <c:out
													value="${operacionBusqueda.deposito.descripcion}"
													default="" /> </label>
									</td>	
									<td class="texto_ti">
											<input type="text" id="codigoTipoOperacion" 
														name="codigoTipoOperacion" maxlength="6" style="width: 50px;" 
														value="<c:out value="${operacionBusqueda.codigoTipoOperacion}" default="" />"
											/>
											<c:out value="${operacionBusqueda.codigoTipoOperacion}" default="" />
											&nbsp;&nbsp;
											<button type="button" id="buscaTipoOperacion"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; 
											<div style="margin-right:50px; float:right;" class="tooltip-wrapper">
												<a href="#" target="blank">
													<label id="codigoTipoOperacionLabel" class="tooltip-trigger"  
															onmouseover="resize(this,30,30);" 
															onmouseout="resize(this,25,25);"
															for="codigoTipoOperacion"> <c:out
														value="${operacionBusqueda.tipoOperacion.descripcion}"
														default="" /> </label>
											    </a>
												<div style="margin-top:20px;  margin-left:-30px; float:left;" class="tooltip">Facebook</div>
											</div>
											
									</td>								
								</tr>
									
								<tr>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.fechaDesde" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.fechaHasta" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.fechaEntregaDesde" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.fechaEntregaHasta" htmlEscape="true" />
									</td>
									
									
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="fechaDesde" name="fechaDesde" style="width: 80px;"
											maxlength="10" 
											value="<c:out value="${operacionBusqueda.fechaDesdeStr}" default="" />" />
											
								
										<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'formBusqueda',
													// input name
													'controlname': 'fechaDesde'
												});
											</script>
									</td>										
									<td class="texto_ti">
										<input type="text" id="fechaHasta" name="fechaHasta" style="width: 80px;"
											maxlength="10" 
											value="<c:out value="${operacionBusqueda.fechaHastaStr}" default="" />" />
								
										<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'formBusqueda',
													// input name
													'controlname': 'fechaHasta'
												});
											</script>
									</td>
									<td class="texto_ti">
										<input type="text" id="fechaEntregaDesde" name="fechaEntregaDesde" style="width: 80px;"
											maxlength="10" 
											value="<c:out value="${operacionBusqueda.fechaEntregaDesdeStr}" default="" />" />
											
								
										<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'formBusqueda',
													// input name
													'controlname': 'fechaEntregaDesde'
												});
											</script>
									</td>										
									<td class="texto_ti">
										<input type="text" id="fechaEntregaHasta" name="fechaEntregaHasta" style="width: 80px;"
											maxlength="10" 
											value="<c:out value="${operacionBusqueda.fechaEntregaHastaStr}" default="" />" />
								
										<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'formBusqueda',
													// input name
													'controlname': 'fechaEntregaHasta'
												});
											</script>
									</td>							
									
									
								</tr>
								
								<tr>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.estado" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.display.requerimiento" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.codigoDesde" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.codigoHasta" htmlEscape="true" />
									</td>
								</tr>
								<tr>
																	
									<td class="texto_ti">
										<select id="estado" 
											name="estado" size="1" style="width: 140px;"
											>
												<option label="Todos" value="Todos" 
													<c:if test="${operacionBusqueda.estado == 'Todos'}">
															selected="selected"
													</c:if>>
													Todos
												</option>
												<option label="Pendiente" value="Pendiente" 
													<c:if test="${operacionBusqueda.estado == 'Pendiente'}">
															selected="selected"
													</c:if>>
													Pendiente
												</option>
												<option label="En Proceso" value="En Proceso" 
													<c:if test="${operacionBusqueda.estado == 'En Proceso'}">
															selected="selected"
													</c:if>>
													En Proceso
												</option>
												<option label="Cancelado" value="Cancelado" 
													<c:if test="${operacionBusqueda.estado == 'Cancelado'}">
															selected="selected"
													</c:if>>
													Cancelado
												</option>
												<option label="Finalizado OK" value="Finalizado OK" 
													<c:if test="${operacionBusqueda.estado == 'Finalizado OK'}">
															selected="selected"
													</c:if>>
													Finalizado OK
												</option>
												<option label="Finalizado ERROR" value="Finalizado ERROR" 
													<c:if test="${operacionBusqueda.estado == 'Finalizado ERROR'}">
															selected="selected"
													</c:if>>
													Finalizado ERROR
												</option>											
										</select>
									</td>
									<td class="texto_ti">
										<input type="text" id="codigoRequerimiento"
											name="codigoRequerimiento" maxlength="6" style="width: 50px;"
											value="<c:out value="${operacionBusqueda.codigoRequerimiento}" default="" />"
											 />
											&nbsp;&nbsp;
											<button type="button" id="buscaRequerimiento"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoRequerimientoLabel"
											for="codigoRequerimiento"> </label>
									</td>	
									<td class="texto_ti">
										<input type="text" id="idDesde" name="idDesde" style="width: 80px;"
											maxlength="6" 
											value="<c:out value="${operacionBusqueda.idDesde}" default="" />" />
									</td>
									<td class="texto_ti">
										<input type="text" id="idHasta" name="idHasta" style="width: 80px;"
											maxlength="6" 
											value="<c:out value="${operacionBusqueda.idHasta}" default="" />" />
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.display.cliente" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.display.solicitante" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.display.entrega" htmlEscape="true" />
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="clienteCodigo"
										name="clienteCodigo" maxlength="6" style="width: 50px;"
										value="<c:out value="${operacionBusqueda.clienteCodigo}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										&nbsp;&nbsp;
										<button type="button" disabled="disabled"
											id="buscaCliente" name="buscaCliente"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="clienteCodigoLabel"
										for="clienteCodigo"> <c:out
												value="${operacionBusqueda.clienteEmp.nombreYApellido}"
												default="" /> </label></td>
									<td class="texto_ti">
										<input type="text" id="codigoPersonal"
											name="codigoPersonal" maxlength="6" style="width: 50px;"
											value="<c:out value="${operacionBusqueda.codigoPersonal}" default="" />"
											 />
											&nbsp;&nbsp;
											<button type="button" disabled="disabled"
												id="buscaPersonal" name="buscaPersonal"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoPersonalLabel"
											for="codigoPersonal"></label>
									</td>
									<td class="texto_ti">
										<input type="text" id="codigoDireccion"
											name="codigoDireccion" maxlength="6" style="width: 50px;"
											value="<c:out value="${operacionBusqueda.codigoDireccion}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												name = "buscaDireccion" id="buscaDireccion"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoDireccionLabel"
											for="codigoDireccion"></label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message	code="formularioOperacion.display.ocultarOpEnvio" htmlEscape="true" />
									</td>
								</tr>
								<tr>	
									<td>
										&nbsp;&nbsp;
										<input type="checkbox" id="ocultarOpEnvio"  name="ocultarOpEnvio" 
											 <c:if test="${operacionBusqueda.ocultarOpEnvio==true}">
															checked="checked"
											</c:if> />
									</td>
								</tr>
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<fieldset>
					<display:table name="operacions" id="operacion"  pagesize="7" sort="external" requestURI="mostrarOperacion.html"
								partialList="true" size="${size}">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${operacion.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_tipoOperacion" value="${operacion.tipoOperacion.imprimeDocumento}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_estado" value="${operacion.estado}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_finalizarOK" value="${operacion.finalizarOK}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_finalizarError" value="${operacion.finalizarError}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_traspasar" value="${operacion.traspasar}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_cantidad_pendientes" value="${operacion.cantidadPendientes}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_genera_operac_siguiente" value="${operacion.tipoOperacion.generaOperacionAlCerrarse}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_tipoCalculo" value="${operacion.tipoOperacion.conceptoFacturable.tipoCalculo}" />
						</display:column>
						<display:column property="id" titleKey="formularioOperacion.display.codigo" sortName="id" 
						sortable="true" class="celdaAlineadoIzquierda"/>
						<display:column property="tipoOperacion.descripcion" sortName="tipoOperacion.descripcion"
							titleKey="formularioOperacion.display.tipoOperacion" sortable="true" 
							class="celdaAlineadoIzquierda"/>
						<display:column property="deposito.descripcion" sortName="deposito.descripcion"
							titleKey="formularioOperacion.display.deposito" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="requerimiento.clienteEmp.razonSocialONombreYApellido" sortName="requerimiento.clienteEmp.razonSocialONombreYApellido"
							titleKey="formularioOperacion.display.cliente"
							class="celdaAlineadoIzquierda" />
						<display:column property="requerimiento.empleadoSolicitante.apellidoYNombre" sortName="requerimiento.clienteEmp.razonSocialONombreYApellido"
							titleKey="formularioOperacion.display.solicitante"
							class="celdaAlineadoIzquierda" />	
						<display:column property="requerimiento.direccionDefecto.calleNumeroPisoDpto" sortName="requerimiento.clienteEmp.razonSocialONombreYApellido"
							titleKey="formularioOperacion.display.entrega"
							class="celdaAlineadoIzquierda" />		
						<display:column 
							titleKey="formularioOperacion.display.requerimiento" 
							class="celdaAlineadoIzquierda" >
							<c:out value="${operacion.requerimiento.serie.codigo}" default="" />: <c:out value="${operacion.requerimiento.prefijoStr}" default="" /> - <c:out value="${operacion.requerimiento.numeroStr}" default="" />
						</display:column>
						<display:column property="fechaHoraAltaStr" sortName="fechaHoraAltaStr"
							titleKey="formularioOperacion.display.fechaAlta" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="fechaHoraEntregaStr" sortName="fechaHoraEntregaStr"
							titleKey="formularioOperacion.display.fechaEntrega" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="cantidadPendientes" sortName="cantidadPendientes"
							titleKey="formularioOperacion.display.cantidadPendientes" sortable="true"
							class="celdaAlineadoDerecha" />
						<display:column property="cantidadProcesados" sortName="cantidadProcesados"
							titleKey="formularioOperacion.display.cantidadProcesados" sortable="true"
							class="celdaAlineadoDerecha" />
						<display:column property="cantidadOmitidos" sortName="cantidadOmitidos"
							titleKey="formularioOperacion.display.cantidadOmitidos" sortable="true"
							class="celdaAlineadoDerecha" />
						<display:column property="estado" sortName="estado"
							titleKey="formularioOperacion.display.estado" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_imprime" value="${operacion.tipoOperacion.imprimeDocumento}" />
						</display:column>		
						<display:column class="celdaAlineadoCentrado">
							<img id="information"
								src="<%=request.getContextPath()%>/images/information.png" alt="<c:out value="${operacion.requerimiento.observaciones}" default="" />"
								title="<c:out value="${operacion.requerimiento.observaciones}" default="" />" />
						</display:column>	
					</display:table>
				</fieldset>
				<br style="font-size: xx-small;" />
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png" />
						<spring:message code="botones.cerrar" htmlEscape="true" />
					</button>
				</div>
				<br style="font-size: xx-small;" />
			</fieldset>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
</html>