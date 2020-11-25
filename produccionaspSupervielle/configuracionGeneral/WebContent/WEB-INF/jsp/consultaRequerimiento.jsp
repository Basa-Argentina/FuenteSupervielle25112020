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
		code="formularioRequerimiento.titulo" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript"
	src="js/mavalos_consulta_requerimiento.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
<script type="text/javascript" src="js/busquedaHelper.js"></script>
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>
			<li id="consultar" value=""><img src="images/consultar.png" /><font
				size="2"><spring:message code="botones.consultar"
						htmlEscape="true" />
			</font>
			</li>
			<li id="modificar"><img src="images/modificar.png" /> <font
				size="2"><spring:message code="botones.modificar"
						htmlEscape="true" />
			</font>
			</li>
			<li id="eliminar"><img src="images/eliminar.png" /> <font
				size="2"><spring:message code="botones.eliminar"
						htmlEscape="true" />
			</font>
			</li>
			<li id="cancelar"><img src="images/cancelar.png" /> <font
				size="2"><spring:message code="formularioRequerimiento.display.cancelar"
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
							code="formularioRequerimiento.titulo"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarRequerimiento.html" name="formBusqueda"
					commandName="requerimientoBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="mensajeSeleccioneCliente" name="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
					<input type="hidden" name="hdn_imprimir" id="hdn_imprimir" value="false"/>
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
									<td class="texto_ti" colspan="2">
										<spring:message	code="formularioRequerimiento.tipoRequerimiento" htmlEscape="true" />
									</td>
									<td class="texto_ti" colspan="3">
										<spring:message	code="formularioRequerimiento.empleadoSolicitante" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.fechaDesde" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.fechaHasta" htmlEscape="true" />
									</td>
									<td rowspan="8" style="vertical-align: middle;">
										<button name="buscar" class="botonCentrado" type="submit">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="texto_ti" colspan="2">														
										<input type="text" id="tipoRequerimientoCod" name="tipoRequerimientoCod" maxlength="6" style="width: 50px;"
											value="<c:out value="${requerimientoBusqueda.tipoRequerimientoCod}" default="" />" class="upperCase"
											
										/>
										&nbsp;&nbsp;
										<button type="button"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											id="buscaTipoRequerimiento" name="buscaTipoRequerimiento"
										>
										<img src="<%=request.getContextPath()%>/images/buscar.png" > 
										</button>&nbsp;&nbsp; 
										<label id="tipoRequerimientoCodLabel" for="tipoRequerimientoCod" style="width: 150px;">
											<c:out value="${requerimientoBusqueda.tipoRequerimiento.descripcion}" default="" />
										</label>
									</td>
									<td class="texto_ti" colspan="3">
										<input type="text" id="codigoPersonal"
											name="codigoPersonal" maxlength="6" style="width: 50px;"
											value="<c:out value="${requerimientoBusqueda.codigoPersonal}" default="" />"
											 />
											&nbsp;&nbsp;
											<button type="button"
												id="buscaPersonal" name="buscaPersonal"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoPersonalLabel"
											for="codigoPersonal"> <c:out
													value="${requerimientoBusqueda.empleadoSolicitante.nombreYApellido}"
													default="" /> </label>
									</td>
									<td class="texto_ti">
										<input type="text" id="fechaDesde" name="fechaDesde" style="width: 80px;"
											maxlength="10" 
											value="<c:out value="${requerimientoBusqueda.fechaDesdeStr}" default="" />" />
											
								
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
											value="<c:out value="${requerimientoBusqueda.fechaHastaStr}" default="" />" />
								
										<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'formBusqueda',
													// input name
													'controlname': 'fechaHasta'
												});
											</script>
									</td>
								</tr>
									
								<tr>
									<td class="texto_ti" colspan="5"><spring:message
											code="formularioRequerimiento.cliente" htmlEscape="true" />
									</td>
									
									
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.fechaEntregaDesde" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.fechaEntregaHasta" htmlEscape="true" />
									</td>
									
									
								</tr>
								<tr>
									<td class="texto_ti" colspan="3"><input type="text" id="clienteCodigo"
										name="clienteCodigo" maxlength="6" style="width: 50px;"
										value="<c:out value="${requerimientoBusqueda.clienteCodigo}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											id="buscaCliente" name="buscaCliente"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="clienteCodigoLabel"
										for="clienteCodigo"> <c:out
												value="${requerimientoBusqueda.clienteEmp.nombreYApellido}"
												default="" /> </label></td>
												
									<td class="texto_ti" colspan="2">
									</td>
									<td class="texto_ti">
										<input type="text" id="fechaEntregaDesde" name="fechaEntregaDesde" style="width: 80px;"
											maxlength="10" 
											value="<c:out value="${requerimientoBusqueda.fechaEntregaDesdeStr}" default="" />" />
											
								
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
											value="<c:out value="${requerimientoBusqueda.fechaEntregaHastaStr}" default="" />" />
								
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
									<td class="texto_ti" colspan="4">
										<spring:message	code="formularioRequerimiento.direccionEntrega" htmlEscape="true" />
									</td>
								</tr>
								<tr>
									<td class="texto_ti" colspan="4">
										<input type="text" id="codigoDireccion"
											name="codigoDireccion" maxlength="6" style="width: 50px;"
											value="<c:out value="${requerimientoBusqueda.codigoDireccion}" default="" />"
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
											for="codigoDireccion"> <c:out
													value="${requerimientoBusqueda.direccionDefecto.descripcion}"
													default="" />
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.serie" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.serieDesde" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.serieHasta" htmlEscape="true" />
									</td>
									<td class="texto_ti">
										<spring:message	code="formularioRequerimiento.estado" htmlEscape="true" />
									</td>
								</tr>
								<tr>
									<td class="texto_ti" style="width: 150px;">
										<input type="text" id="codigoSerie" name="codigoSerie" maxlength="30" style="width: 50px;"
													value="<c:out value="${requerimientoBusqueda.codigoSerie}" default="" />"
											/>
											&nbsp;&nbsp;
											<button type="button" id="buscaSerie" name="buscaSerie"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											>
											<img src="<%=request.getContextPath()%>/images/buscar.png" > 
											</button>&nbsp;&nbsp;
											
											<label id="codigoSerieLabel" "for="codigoSerie" >
													<c:out value="${requerimientoBusqueda.serie.prefijo}" default="" />			
											</label>
											
										
									</td>
									<td class="texto_ti">
										-
										<input type="text" id="serieDesdeStr" maxlength="8"
											name="serieDesdeStr" style="width: 70px;" class="integer"
											value="<c:out value="${requerimientoBusqueda.serieDesdeStr}" default="" />"
											/>
									</td>
									<td class="texto_ti">
										<input type="text" id="serieHastaStr" maxlength="8"
											name="serieHastaStr" style="width: 70px;" class="integer"
											value="<c:out value="${requerimientoBusqueda.serieHastaStr}" default="" />"
											/>
									</td>
									<td class="texto_ti">
										<select id="estado" 
											name="estado" size="1" style="width: 140px;"
											>
												<option label="Todos" value="Todos" 
													<c:if test="${requerimientoBusqueda.estado == 'Todos'}">
															selected="selected"
													</c:if>>
													Todos
												</option>
												<option label="Pendiente" value="Pendiente" 
													<c:if test="${requerimientoBusqueda.estado == 'Pendiente'}">
															selected="selected"
													</c:if>>
													Pendiente
												</option>
												<option label="En Proceso" value="En Proceso" 
													<c:if test="${requerimientoBusqueda.estado == 'En Proceso'}">
															selected="selected"
													</c:if>>
													En Proceso
												</option>
												<option label="Cancelado" value="Cancelado" 
													<c:if test="${requerimientoBusqueda.estado == 'Cancelado'}">
															selected="selected"
													</c:if>>
													Cancelado
												</option>
												<option label="Finalizado OK" value="Finalizado OK" 
													<c:if test="${requerimientoBusqueda.estado == 'Finalizado OK'}">
															selected="selected"
													</c:if>>
													Finalizado OK
												</option>
												<option label="Finalizado ERROR" value="Finalizado ERROR" 
													<c:if test="${requerimientoBusqueda.estado == 'Finalizado ERROR'}">
															selected="selected"
													</c:if>>
													Finalizado ERROR
												</option>											
										</select>
									</td>
								</tr>
								<tr>
									
								</tr>
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<fieldset>
					<display:table name="sessionScope.requerimientos" id="requerimiento"  pagesize="20" sort="external" requestURI="mostrarRequerimiento.html"
								partialList="true" size="${size}">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${requerimiento.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminarNoPendientes"
								value="<spring:message code="formularioRequerimiento.errorEliminarNoPendientes" htmlEscape="true"/>" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_modificar"
								value="<spring:message code="formularioRequerimiento.display.mensajeErrorModificar" htmlEscape="true"/>" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_cancelar"
								value="<spring:message code="formularioRequerimiento.mensaje.cancelar" htmlEscape="true"/>" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_cancelarMensaje"
								value="<spring:message code="formularioRequerimiento.display.mensajeErrorCancelar" htmlEscape="true"/>" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_estado" value="${requerimiento.estado}" />
						</display:column>
						<display:column property="tipoRequerimiento.descripcion"  sortName="tipoRequerimiento.descripcion"
							titleKey="formularioRequerimiento.display.tipoRequerimiento" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column  sortName="requerimiento.prefijoStr"
							titleKey="formularioRequerimiento.display.numero" sortable="true"
							class="celdaAlineadoIzquierda" >
							<c:out value="${requerimiento.serie.codigo}" default="" />: <c:out value="${requerimiento.prefijoStr}" default="" /> - <c:out value="${requerimiento.numeroStr}" default="" />
						</display:column>
						<display:column property="clienteEmp.razonSocialONombreYApellido"  
							titleKey="formularioRequerimiento.cliente" 
							class="celdaAlineadoIzquierda" />
						<display:column property="empleadoSolicitante.nombreYApellido"  
							titleKey="formularioRequerimiento.display.empleadoSolicitante"
							class="celdaAlineadoIzquierda" />
						<display:column property="fechaHoraAltaStr"  sortName="fechaHoraAltaStr"
							titleKey="formularioRequerimiento.display.fechaAlta" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="fechaHoraEntregaStr"  sortName="fechaHoraEntregaStr"
							titleKey="formularioRequerimiento.display.fechaEntrega" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="estado"  sortName="estado"
							titleKey="formularioRequerimiento.display.estado" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="usuario.persona"
							titleKey="formularioRequerimiento.display.usuarioRegistrante" sortable="false"
							class="celdaAlineadoIzquierda" />
							
						<display:column class="celdaAlineadoCentrado">
							<img id="information"
								src="<%=request.getContextPath()%>/images/information.png"
								title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
						</display:column>
					</display:table>
					<div style="width: 100%" align="right">
						
						<button name="imprimir" id="imprimir" type="button">
							<img src="<%=request.getContextPath()%>/images/impresora.gif">
							<spring:message code="botones.imprimir" htmlEscape="true" />
						</button>
						<button name="agregar" type="button"
							onclick="agregarRequerimiento();">
							<img src="<%=request.getContextPath()%>/images/add.png">
							<spring:message code="botones.agregar" htmlEscape="true" />
						</button>
					</div>
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
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
	<div class="selectorDiv"></div>
</html>