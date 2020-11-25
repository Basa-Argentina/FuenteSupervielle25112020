<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><spring:message
		code="formularioStock.tituloConsulta" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/mavalos_consulta_stock.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>
			<li id="consultar" value=""><img src="images/consultar.png" /><font
				size="2"><spring:message code="botones.detalle"
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
							code="formularioStock.tituloConsulta"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarStock.html"
					commandName="stockBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="errorCodigoEmpresa" value="<spring:message code="formularioStock.error.codigoEmpresaReq" htmlEscape="true"/>"/>
					<input type="hidden" id="errorCodigoSucursal" value="<spring:message code="formularioStock.error.codigoSucursalReq" htmlEscape="true"/>"/>
					<input type="hidden" id="errorCodigoDeposito" value="<spring:message code="formularioStock.error.codigoDepositoReq" htmlEscape="true"/>"/>
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
											code="formularioStock.empresa" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioStock.sucursal" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioStock.deposito"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioStock.vacio" htmlEscape="true" />
									</td>
									<td rowspan="2" style="vertical-align: bottom;">
										<button id="botonBuscar" name="buscar" class="botonCentrado" type="submit">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="codigoEmpresa" class="requerido"
										name="codigoEmpresa" maxlength="6" style="width: 50px;"
										value="<c:out value="${stockBusqueda.codigoEmpresa}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopup('empresasPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
										for="codigoEmpresa"> <c:out
												value="${stockBusqueda.deposito.sucursal.empresa.descripcion}"
												default="" /> </label></td>
									<td class="texto_ti"><input type="text" id="codigoSucursal" class="requerido"
										name="codigoSucursal" maxlength="6" style="width: 50px;"
										value="<c:out value="${stockBusqueda.codigoSucursal}" default="" />"
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
												value="${stockBusqueda.deposito.sucursal.descripcion}"
												default="" /> </label></td>
									<td class="texto_ti"><input type="text" id="codigoDeposito" class="requerido"
										name="codigoDeposito" maxlength="6" style="width: 50px;"
										value="<c:out value="${stockBusqueda.codigoDeposito}" default="" />"
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
												value="${stockBusqueda.deposito.descripcion}"
												default="" /> </label>
									</td>
								</tr>
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<fieldset>
					<display:table name="stocks" id="stock"
						requestURI="" pagesize="7" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_cc" value="${stock.concepto.codigo}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_cd" value="${stock.deposito.codigo}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column style="width:100px;" property="concepto.codigo"
							titleKey="formularioStock.stock.concepto" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="concepto.descripcion"
							titleKey="formularioStock.stock.descripcion" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column style="width:100px;"  property="cantidad"
							titleKey="formularioStock.stock" sortable="true"
							class="celdaAlineadoDerecha" />						
						<display:column class="celdaAlineadoCentrado">
							<img id="information"
								src="<%=request.getContextPath()%>/images/information.png"
								title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
						</display:column>
					</display:table>
					<sec:authorize ifAllGranted="ROLE_PRO_AJUSTE_STOCK">
						<div style="width: 100%" align="left">
							<button name="agregar" type="button"
								onclick="generarAjuste('<spring:message code="notif.stock.seleccionDeposito" htmlEscape="true"/>'
														, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');">
								<img src="<%=request.getContextPath()%>/images/add.png">
								<spring:message code="botones.generarAjuste" htmlEscape="true" />
							</button>
							
							<input type="hidden" id="notifSeleccionDeposito" value="<spring:message code="notif.stock.seleccionDeposito" htmlEscape="true"/>" />
							<input type="hidden" id="mensajeInformacion" value="<spring:message code="mensaje.informacion" htmlEscape="true"/>" />
							<input type="hidden" id="mensajeConfirmacion" value="<spring:message code="formularioStock.notificacion.preguntaConfirmacionAjuste" htmlEscape="true"/>" />
							<input type="hidden" id="mensajeConfirmacionTitulo" value="<spring:message code="formularioStock.notificacion.preguntaConfirmacionAjusteTitulo" htmlEscape="true"/>" />
							
							<button name="resumir" type="button" id="buttonResumir"	>
								<img src="<%=request.getContextPath()%>/images/skip.png">							
								<spring:message code="botones.resumirMovimientos" htmlEscape="true" />
							</button>
						</div>
					</sec:authorize>
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
	
	<div id="pop">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label>Resumiendo movimientos de Stock. Espere por favor...</label>	     
	</div>
</html>