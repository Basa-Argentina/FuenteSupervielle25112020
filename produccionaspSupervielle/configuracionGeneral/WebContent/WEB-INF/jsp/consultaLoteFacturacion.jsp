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
<title><spring:message
		code="formularioLoteFacturacion.tituloConsulta" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/mavalos_consulta_loteFacturacion.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		 <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		      <li id="facturar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.facturar" htmlEscape="true"/></font></li>
	    </ul> 
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioLoteFacturacion.tituloConsulta"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarLoteFacturacion.html"
					commandName="loteFacturacionBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="errorSeleccioneEmpresa" name="errorSeleccioneEmpresa" value="<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>" />
					<input type="hidden" id="mensajeFacturar" name="mensajeFacturar" value="<spring:message code="formularioLoteFacturacion.notificacion.facturarLote" htmlEscape="true"/>" />
						<input type="hidden" id="errorSeleccioneSucursal" name="errorSeleccioneSucursal" value="<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>" />
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
										<td class="texto_ti"><spring:message
												code="formularioLoteFacturacion.empresa" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioLoteFacturacion.sucursal" htmlEscape="true" />
										</td>
									</tr>
									<tr>
										<td class="texto_ti"><input type="text" id="codigoEmpresa"
											name="codigoEmpresa" maxlength="4" style="width: 50px;"
											value="<c:out value="${loteFacturacionBusqueda.codigoEmpresa}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
											for="codigoEmpresa"> <c:out
													value="${loteFacturacionBusqueda.empresa.descripcion}"
													default="" /> </label></td>
										<td class="texto_ti"><input type="text" id="codigoSucursal"
											name="codigoSucursal" maxlength="4" style="width: 50px;"
											value="<c:out value="${loteFacturacionBusqueda.codigoSucursal}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupSucursales"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
											for="codigoSucursal"> <c:out
													value="${loteFacturacionBusqueda.sucursal.descripcion}"
													default="" /> </label></td>
										</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioLoteFacturacion.fechaDesde" htmlEscape="true" />
										
										</td>
										<td class="texto_ti">
										<spring:message
												code="formularioLoteFacturacion.fechaHasta" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message	code="formularioLoteFacturacion.datosLoteFacturacion.estado" htmlEscape="true" />
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
														<input type="text" id="fechaDesde" name="fechaDesde" 
															maxlength="10" 
															value="<c:out value="${loteFacturacionBusqueda.fechaDesdeStr}" default="" />" />
															
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'loteFacturacionBusqueda',
																	// input name
																	'controlname': 'fechaDesde'
																});
															</script></c:if>
													</td>										
													<td class="texto_ti">
														<input type="text" id="fechaHasta" name="fechaHasta" 
															maxlength="10" 
															value="<c:out value="${loteFacturacionBusqueda.fechaHastaStr}" default="" />" />
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'loteFacturacionBusqueda',
																	// input name
																	'controlname': 'fechaHasta'
																});
															</script></c:if>
													</td>
									<td class="texto_ti">
											<select id="estado"
												name="estado" size="1">
													<option label="Todos" value="">Todos</option>
													<option label="Pendiente" value="Pendiente" <c:if test="${loteFacturacionBusqueda.estado == 'Pendiente'}">
															selected="selected"
														</c:if>>Pendiente</option>
													<option label="Facturado" value="Facturado" <c:if test="${loteFacturacionBusqueda.estado == 'Facturado'}">
															selected="selected"
														</c:if>>Facturado</option>
													<option label="Anulado" value="Anulado" <c:if test="${loteFacturacionBusqueda.estado == 'Anulado'}">
															selected="selected"
														</c:if>>Anulado</option>
											</select>
										</td>						
									<td rowspan="2" style="vertical-align: bottom;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
												<spring:message code="textos.buscar" htmlEscape="true" />
											</button>
										</td>							
								<tr>							
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<form:form action="cambiarEstadoLoteFacturaciones.html" commandName="loteFacturacionTable" method="post">
					<fieldset>
						<display:table name="lotesFacturacion" id="loteFacturacion"
							requestURI="mostrarLoteFacturacion.html" pagesize="20" sort="external" partialList="true" size="${size}">
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_id" value="${loteFacturacion.id}" />
							</display:column>						
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_eliminar"
									value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
							</display:column>
							<display:column sortName="estado" property="estado"
								titleKey="formularioLoteFacturacion.datosLoteFacturacion.estado" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column sortName="fechaRegistro" property="fechaRegistroStr"
								titleKey="formularioLoteFacturacion.datosLoteFacturacion.fechaRegistro" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column property="numeroStr" sortName="numero"
								titleKey="formularioLoteFacturacion.datosLoteFacturacion.numero" sortable="true"
								class="celdaAlineadoIzquierda" />					
							<display:column property="descripcion"
								titleKey="formularioLoteFacturacion.descripcion" sortable="false"
								class="celdaAlineadoCentrado" />
							<display:column property="usuarioRegistro.persona"
								titleKey="formularioLoteFacturacion.user" sortable="false"
								class="celdaAlineadoCentrado" />
							<display:column class="celdaAlineadoCentrado">
								<img id="information"
									src="<%=request.getContextPath()%>/images/information.png"
									title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>
						</display:table>
						<div style="width: 100%" align="right">
						<table>
							<tr>
								<td>
									<button name="agregar" id="agregar" type="button">
										<img src="<%=request.getContextPath()%>/images/add.png">
										<spring:message code="botones.agregar" htmlEscape="true" />
									</button>
								</td>
							</tr>
						</table>
						</div>					
					</fieldset>
				</form:form>
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
	<div id="darkLayer" class="darkClassWithHeigth">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
			<div class="selectorDiv"></div>
</html>