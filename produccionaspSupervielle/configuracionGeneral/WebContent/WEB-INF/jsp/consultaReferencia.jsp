<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ page buffer="64kb"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><spring:message
		code="formularioRequerimientoElemento.titulo" htmlEscape="true" /> -
	<spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" /></title>

<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/calendar_us.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>

<script type="text/javascript" src="js/ini.js"></script>
<script type="text/javascript" src="js/busquedaHelper.js"></script>
<script type="text/javascript" src="js/consulta_referencia.js"></script>

<script type="text/javascript"
	src="js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
<script type="text/javascript"
	src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css"
	href="js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>
			<li id="verImagenes" value=""><img src="images/open.gif" /><font
				size="2"><spring:message code="botones.verImagenes"
						htmlEscape="true" /></font></li>
		</ul>
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioRequerimientoElemento.titulo" htmlEscape="true" />
					</font>
				</legend>
				<br />
				<form:form action="filtrarReferencia.html" name="formBusqueda"
					id="formBusqueda" commandName="requerimientoElementoBusqueda"
					method="post">
					<input type="hidden" id="isRequerimiento" name="isRequerimiento" value="false">
					<input type="hidden" id="listaPalabras" name="listaPalabras">
					<input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="clienteCodigoRequerimientoElemento"
						name="clienteCodigoRequerimientoElemento"
						value="<c:out value="${clienteCodigoRequerimientoElemento}" default="" />" />
					<input type="checkbox" id="numero1Derecha" name="numero1Derecha"
						style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.numero1Derecha}">
							checked="checked"
						</c:if> />
					<input type="checkbox" id="numero1Izquierda"
						name="numero1Izquierda" style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.numero1Izquierda}">
							checked="checked"
						</c:if> />

					<input type="checkbox" id="numero2Derecha" name="numero2Derecha"
						style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.numero2Derecha}">
							checked="checked"
						</c:if> />
					<input type="checkbox" id="numero2Izquierda"
						name="numero2Izquierda" style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.numero2Izquierda}">
							checked="checked"
						</c:if> />

					<input type="checkbox" id="texto1Derecha" name="texto1Derecha"
						style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.texto1Derecha}">
							checked="checked"
						</c:if> />
					<input type="checkbox" id="texto1Izquierda" name="texto1Izquierda"
						style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.texto1Izquierda}">
							checked="checked"
						</c:if> />

					<input type="checkbox" id="texto2Derecha" name="texto2Derecha"
						style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.texto2Derecha}">
							checked="checked"
						</c:if> />
					<input type="checkbox" id="texto2Izquierda" name="texto2Izquierda"
						style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.texto2Izquierda}">
							checked="checked"
						</c:if> />

					<input type="checkbox" id="descripcionDerecha"
						name="descripcionDerecha" style="visibility: hidden;"
						<c:if test="${requerimientoElementoBusqueda.descripcionDerecha}">
							checked="checked"
						</c:if> />
					<input type="checkbox" id="descripcionIzquierda"
						name="descripcionIzquierda" style="visibility: hidden;" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="textos.buscar" htmlEscape="true" />
									</font> <img id="busquedaImgSrcDown" src="images/skip_down.png"
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
											code="formularioLoteReferencia.busqueda.empresa"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioLoteReferencia.busqueda.sucursal"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioLoteReferencia.busqueda.cliente"
											htmlEscape="true" /></td>

								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="codigoEmpresa"
										name="codigoEmpresa" style="width: 50px;"
										value="<c:out value="${requerimientoElementoBusqueda.codigoEmpresa}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" id="buscaEmpresa"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button> &nbsp;&nbsp; <label id="codigoEmpresaLabel"
										for="codigoEmpresa"> </label></td>
									<td class="texto_ti"><input type="text"
										id="codigoSucursal" name="codigoSucursal" style="width: 50px;"
										value="<c:out value="${requerimientoElementoBusqueda.codigoSucursal}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" id="buscaSucursal"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button> &nbsp;&nbsp; <label id="codigoSucursalLabel"
										for="codigoSucursal"> </label></td>

									<td class="texto_ti"><input type="text" id="codigoCliente"
										name="codigoCliente" style="width: 50px;"
										value="<c:out value="${requerimientoElementoBusqueda.codigoCliente}" default="" />"
										<c:if test="${empleadoSession!=null}">
												readonly="readonly"
											</c:if> />
										&nbsp;&nbsp;
										<button type="button" id="buscaCliente"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA' || (empleadoSession!=null)}">
												disabled="disabled"
											</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button> &nbsp;&nbsp; <label id="codigoClienteLabel"
										for="codigoCliente"> </label></td>
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioRequerimientoElemento.clasificacionDocumental"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioRequerimientoElemento.tipoElemento"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioRequerimientoElemento.elemento"
											htmlEscape="true" /></td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text"
										id="codigoClasificacionDocumental"
										name="codigoClasificacionDocumental" maxlength="6"
										style="width: 90px;"
										value="<c:out value="${requerimientoElementoBusqueda.codigoClasificacionDocumental}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" id="buscaClasificacionDocumental"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoClasificacionDocumentalLabel"
										for="codigoClasificacionDocumental"> <c:out
												value="${requerimientoElementoBusqueda.clasificacionDocumental.nombre}"
												default="" />
									</label></td>
									<td class="texto_ti"><input type="text"
										id="codigoTipoElemento" class="integer"
										name="codigoTipoElemento" style="width: 100px;"
										value="<c:out value="${requerimientoElementoBusqueda.codigoTipoElemento}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" id="buscaTiposElementos"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoTipoElementoLabel"
										for="codigoTipoElemento"> </label></td>
									<td class="texto_ti"><input type="text"
										id="codigoElemento" name="codigoElemento" maxlength="12"
										style="width: 90px;"
										value="<c:out value="${requerimientoElementoBusqueda.codigoElemento}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" id="buscaElemento"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoElementoLabel"
										for="codigoElemento"> </label></td>
								</tr>
								<tr align="right">
									<td align="left"><input type="radio" id="unicaSelector"
										name="seleccion"
										<c:if test="${requerimientoElementoBusqueda.seleccion == 'unico'}"> checked="checked" </c:if>
										value="unico" /> <spring:message
											code="formularioRequerimientoElemento.seleccion.unica"
											htmlEscape="true" /> &nbsp;&nbsp; <input type="radio"
										id="grupoSelector" name="seleccion"
										<c:if test="${requerimientoElementoBusqueda.seleccion == 'grupo'}"> checked="checked" </c:if>
										value="grupo" /> <spring:message
											code="formularioRequerimientoElemento.seleccion.grupo"
											htmlEscape="true" /> &nbsp;&nbsp; <input type="radio"
										id="ambosSelector" name="seleccion"
										<c:if test="${requerimientoElementoBusqueda.seleccion == 'ambos' || requerimientoElementoBusqueda.seleccion ==null}"> checked="checked" </c:if>
										value="ambos" /> <spring:message
											code="formularioRequerimientoElemento.seleccion.ambos"
											htmlEscape="true" /> &nbsp;&nbsp;</td>
									<td class="texto_ti"><spring:message
											code="formularioRequerimientoElemento.contenedor"
											htmlEscape="true" /></td>
									<td class="texto_ti" rowspan="2">
										<button name="buscar" class="botonCentrado" type="submit">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioRequerimientoElemento.vacio"
											htmlEscape="true" /></td>
									<td class="texto_ti"><input type="text"
										id="codigoContenedor" name="codigoContenedor" maxlength="12"
										style="width: 90px;"
										value="<c:out value="${requerimientoElementoBusqueda.codigoContenedor}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" id="buscaContenedor"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoContenedorLabel"
										for="codigoContenedor"> </label></td>
								</tr>
								<tr>
									<td>
										<fieldset style="height: 250px;">
											<table>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualFecha1Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalFechaSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 120px;" class="texto_ti"><c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental!=null}">
															<c:if
																test="${requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.individualFecha1Titulo}"
																	escapeXml="true" />
															</c:if>
															<c:if
																test="${!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.grupalFecha1Titulo}"
																	escapeXml="true" />
															</c:if>
														</c:if> <c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental==null}">
															<spring:message
																code="formularioRequerimientoElemento.fecha1"
																htmlEscape="true" />
														</c:if></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualFecha1Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalFechaSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 110px;" class="texto_ti"><input
														type="text" id="fecha1" name="fecha1" style="width: 80px;"
														maxlength="10"
														value="<c:out value="${requerimientoElementoBusqueda.fecha1Str}" default="" />" />

														<script type="text/javascript">
																new tcal ({
																	// form name
																	'formname': 'formBusqueda',
																	// input name
																	'controlname': 'fecha1'
																});
															</script></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualFecha2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalFechaSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 110px;" class="texto_ti"><spring:message
															code="formularioRequerimientoElemento.fechaEntre"
															htmlEscape="true" /></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualFecha2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalFechaSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 110px;" class="texto_ti"><input
														type="text" id="fechaEntre" name="fechaEntre"
														style="width: 80px;" maxlength="10"
														value="<c:out value="${requerimientoElementoBusqueda.fechaEntreStr}" default="" />" />

														<script type="text/javascript">
																new tcal ({
																	// form name
																	'formname': 'formBusqueda',
																	// input name
																	'controlname': 'fechaEntre'
																});
															</script></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualFecha2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalFechaSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 110px;" class="texto_ti"><c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental!=null}">
															<c:if
																test="${requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.individualFecha2Titulo}"
																	escapeXml="true" />
															</c:if>
															<c:if
																test="${!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.grupalFecha2Titulo}"
																	escapeXml="true" />
															</c:if>
														</c:if> <c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental==null}">
															<spring:message
																code="formularioRequerimientoElemento.fecha2"
																htmlEscape="true" />
														</c:if></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualFecha2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalFechaSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 110px;" class="texto_ti"><input
														type="text" id="fecha2" name="fecha2" style="width: 80px;"
														maxlength="10"
														value="<c:out value="${requerimientoElementoBusqueda.fecha2Str}" default="" />" />

														<script type="text/javascript">
																new tcal ({
																	// form name
																	'formname': 'formBusqueda',
																	// input name
																	'controlname': 'fecha2'
																});
															</script></td>
												</tr>
												<tr>
													<td style="width: 110px;" class="texto_ti"><spring:message
															code="formularioRequerimientoElemento.fechaInicio"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td style="width: 110px;" class="texto_ti"><input
														type="text" id="fechaInicio" name="fechaInicio"
														style="width: 80px;" maxlength="10"
														value="<c:out value="${requerimientoElementoBusqueda.fechaInicioStr}" default="" />" />

														<script type="text/javascript">
																new tcal ({
																	// form name
																	'formname': 'formBusqueda',
																	// input name
																	'controlname': 'fechaInicio'
																});
															</script></td>
												</tr>
												<tr>
													<td style="width: 110px;" class="texto_ti"><spring:message
															code="formularioRequerimientoElemento.fechaFin"
															htmlEscape="true" /></td>
												</tr>
												<tr>
													<td style="width: 110px;" class="texto_ti"><input
														type="text" id="fechaFin" name="fechaFin"
														style="width: 80px;" maxlength="10"
														value="<c:out value="${requerimientoElementoBusqueda.fechaFinStr}" default="" />" />

														<script type="text/javascript">
																new tcal ({
																	// form name
																	'formname': 'formBusqueda',
																	// input name
																	'controlname': 'fechaFin'
																});
															</script></td>
												</tr>


											</table>
										</fieldset>
									</td>
									<td>
										<fieldset style="height: 150px;">
											<table>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualTexto1Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalTextoSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 100px;" class="texto_ti"><c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental!=null}">
															<c:if
																test="${requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.individualTexto1Titulo}"
																	escapeXml="true" />
															</c:if>
															<c:if
																test="${!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.grupalTexto1Titulo}"
																	escapeXml="true" />
															</c:if>
														</c:if> <c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental==null}">
															<spring:message
																code="formularioRequerimientoElemento.texto1"
																htmlEscape="true" />
														</c:if></td>
													<td style="width: 150px;" class="texto_ti"></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualTexto1Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalTextoSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 140px;" class="texto_ti"><input
														type="text" id="texto1" name="texto1"
														style="width: 140px;" maxlength="255"
														value="<c:out value="${requerimientoElementoBusqueda.texto1}" default="" />" />


													</td>
													<td style="width: 150px;" class="texto_ti">
														<button type="button" id="texto1DerechaBtn"
															name="texto1DerechaBtn"
															onclick="activar('texto1Derecha');"
															<c:if test="${requerimientoElementoBusqueda.texto1Derecha}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">...%</font>
														</button>

														<button type="button" id="texto1IzquierdaBtn"
															name="texto1IzquierdaBtn"
															onclick="activar('texto1Izquierda');"
															<c:if test="${requerimientoElementoBusqueda.texto1Izquierda}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">%...</font>
														</button>
													</td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualTexto2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalTextoSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 100px;" class="texto_ti"><c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental!=null}">
															<c:if
																test="${requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && requerimientoElementoBusqueda.clasificacionDocumental.individualTexto2Seleccionado}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.individualTexto2Titulo}"
																	escapeXml="true" />
															</c:if>
															<c:if
																test="${!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && requerimientoElementoBusqueda.clasificacionDocumental.grupalTextoSeleccionado}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.grupalTexto2Titulo}"
																	escapeXml="true" />
															</c:if>
														</c:if> <c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental==null}">
															<spring:message
																code="formularioRequerimientoElemento.texto2"
																htmlEscape="true" />
														</c:if></td>
													<td></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualTexto2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalTextoSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td class="w140 texto_ti"><input type="text"
														id="texto2" name="texto2" style="width: 140px;"
														maxlength="252"
														value="<c:out value="${requerimientoElementoBusqueda.texto2}" default="" />" />


													</td>
													<td style="width: 100px;" class="texto_ti">
														<button type="button" id="texto2DerechaBtn"
															name="texto2DerechaBtn"
															onclick="activar('texto2Derecha');"
															<c:if test="${requerimientoElementoBusqueda.texto2Derecha}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">...%</font>
														</button>


														<button type="button" id="texto2IzquierdaBtn"
															name="texto2IzquierdaBtn"
															onclick="activar('texto2Izquierda');"
															<c:if test="${requerimientoElementoBusqueda.texto2Izquierda}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">%...</font>
														</button>
													</td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& !requerimientoElementoBusqueda.clasificacionDocumental.descripcionSeleccionado}">
																style="display: none;"
															</c:if>>
													<td style="width: 100px;" class="texto_ti"><c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental!=null}">
															<c:if
																test="${requerimientoElementoBusqueda.clasificacionDocumental.descripcionSeleccionado}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.descripcionTitulo}"
																	escapeXml="true" />
															</c:if>
														</c:if> <c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental==null}">
															<spring:message
																code="formularioRequerimientoElemento.descripcion"
																htmlEscape="true" />
														</c:if></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& !requerimientoElementoBusqueda.clasificacionDocumental.descripcionSeleccionado}">
																style="display: none;"
															</c:if>>
													<td style="width: 140px;" class="texto_ti"><input
														type="text" id="descripcion" name="descripcion"
														style="width: 140px;" maxlength="500"
														value="<c:out value="${requerimientoElementoBusqueda.descripcion}" default="" />" />


													</td>
													<td style="width: 100px;" class="texto_ti">
														<button type="button" id="descripcionDerechaBtn"
															name="descripcionDerechaBtn"
															onclick="activar('descripcionDerecha');"
															<c:if test="${requerimientoElementoBusqueda.descripcionDerecha}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">...%</font>
														</button>


														<button type="button" id="descripcionIzquierdaBtn"
															name="descripcionIzquierdaBtn"
															onclick="activar('descripcionIzquierda');"
															<c:if test="${requerimientoElementoBusqueda.descripcionIzquierda}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">%...</font>
														</button>
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
									<td colspan="2">
										<fieldset style="height: 150px;">
											<table width="100%">
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualNumero1Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 100px;" class="texto_ti"><c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental!=null}">
															<c:if
																test="${requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && requerimientoElementoBusqueda.clasificacionDocumental.individualNumero1Seleccionado}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.individualNumero1Titulo}"
																	escapeXml="true" />
															</c:if>
															<c:if
																test="${!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.grupalNumero1Titulo}"
																	escapeXml="true" />
															</c:if>
														</c:if> <c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental==null}">
															<spring:message
																code="formularioRequerimientoElemento.numero1"
																htmlEscape="true" />
														</c:if></td>
													<td style="width: 150px;" class="texto_ti"></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualNumero1Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 140px;" class="texto_ti"><input
														type="text" id="numero1" name="numero1"
														style="width: 140px;" class="integer" maxlength="19"
														value="<c:out value="${requerimientoElementoBusqueda.numero1}" default="" />" />
													</td>
													<td style="width: 150px;" class="texto_ti">
														<button type="button" id="numero1DerechaBtn"
															name="numero1DerechaBtn"
															onclick="activar('numero1Derecha');"
															<c:if test="${requerimientoElementoBusqueda.numero1Derecha}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">...%</font>
														</button>


														<button type="button" id="numero1IzquierdaBtn"
															name="numero1IzquierdaBtn"
															onclick="activar('numero1Izquierda');"
															<c:if test="${requerimientoElementoBusqueda.numero1Izquierda}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">%...</font>
														</button>
													</td>
												</tr>

												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualNumero2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 100px;" class="texto_ti"><spring:message
															code="formularioRequerimientoElemento.numeroEntre"
															htmlEscape="true" /></td>
													<td style="width: 100px;" class="texto_ti"></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualNumero2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 140px;" class="texto_ti"><input
														type="text" id="numeroEntre" name="numeroEntre"
														style="width: 140px;" class="integer" maxlength="19"
														value="<c:out value="${requerimientoElementoBusqueda.numeroEntre}" default="" />" />
													</td>
												</tr>
												<tr>
													<td style="width: 100px;" class="texto_ti"><c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental!=null}">
															<c:if
																test="${requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && requerimientoElementoBusqueda.clasificacionDocumental.individualNumero2Seleccionado}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.individualNumero2Titulo}"
																	escapeXml="true" />
															</c:if>
															<c:if
																test="${!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado}">
																<c:out
																	value="${requerimientoElementoBusqueda.clasificacionDocumental.grupalNumero2Titulo}"
																	escapeXml="true" />
															</c:if>
														</c:if> <c:if
															test="${requerimientoElementoBusqueda.clasificacionDocumental==null}">
															<spring:message
																code="formularioRequerimientoElemento.numero2"
																htmlEscape="true" />
														</c:if></td>
													<td style="width: 100px;" class="texto_ti"></td>
												</tr>
												<tr
													<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualNumero2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado))}">
																style="display: none;"
															</c:if>>
													<td style="width: 140px;" class="texto_ti"><input
														type="text" id="numero2" name="numero2"
														class="w140 integer" maxlength="19"
														value="<c:out value="${requerimientoElementoBusqueda.numero2}" default="" />"
														<c:if test="${requerimientoElementoBusqueda.clasificacionDocumental!=null 
															&& ((requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.individualNumero2Seleccionado) 
															|| (!requerimientoElementoBusqueda.clasificacionDocumental.indiceIndividual && !requerimientoElementoBusqueda.clasificacionDocumental.grupalNumeroSeleccionado))}">
																style="display: none;"
															</c:if> />
													</td>
													<td style="width: 100px;" class="texto_ti">
														<button type="button" id="numero2DerechaBtn"
															name="numero2DerechaBtn"
															onclick="activar('numero2Derecha');"
															<c:if test="${requerimientoElementoBusqueda.numero2Derecha}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">...%</font>
														</button>


														<button type="button" id="numero2IzquierdaBtn"
															name="numero2IzquierdaBtn"
															onclick="activar('numero2Izquierda');"
															<c:if test="${requerimientoElementoBusqueda.numero2Izquierda}">
																class="buttonSelect"
															</c:if>>
															<font style="font-size: xx-small;">%...</font>
														</button>
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

				<br style="font-size: xx-small;" />
				<fieldset>

					<table width="100%">
						<thead>
							<tr>
								<th align="left" id="busquedaDocImg"><font
									style="color: #003090"> <spring:message
											code="textos.buscarDoc" htmlEscape="true" />
								</font> <img id="busquedaDocImgSrcDown" src="images/skip_down.png"
									title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
									<img id="busquedaDocImgSrc" src="images/skip.png"
									style="DISPLAY: none"
									title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
								</th>
							</tr>
						</thead>
					</table>
					<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"
						id="busquedaDocDiv" align="center">
						<table class="busqueda"
							style="width: 100%; background-color: white;">
							<tr>
								<td class="texto_ti"><spring:message
										code="formularioLoteReferencia.busqueda.interna"
										htmlEscape="true" /></td>

							</tr>
							<tr>
								<td class="texto_ti"><input type="text" id="palabraBusqueda"
									name="palabraBusqueda" style="width: 700px;"
									value="<c:out value="" default="" />" /> &nbsp;</td>
								<td class="texto_ti" rowspan="2">
									<button name="buscar" class="botonCentrado" type="submit" id="buscarDocumento">
										<img src="<%=request.getContextPath()%>/images/buscar.png">
										<spring:message code="textos.buscar" htmlEscape="true" />
									</button>
								</td>

							</tr>

						</table>
					</div>
				</fieldset>
				<br style="font-size: xx-small;" />
			<fieldset>
			
			<button name="descargarImagenesZIP" type="button"
					id="descargarImagenesZIP">
					<img src="<%=request.getContextPath()%>/images/skip.png">
					DescargarImagenesZIP
				</button>
				<td>
				<button name="descargarImagenesPDF" type="button"
					id="descargarImagenesPDF">
					<img src="<%=request.getContextPath()%>/images/ico_file_pdf.png">
					DescargarImagenesPDF
				</button>
				</td>
					</fieldset>
				<fieldset>
					<display:table name="requerimientoElementos"
						id="requerimientoElemento"  sort="external"
						requestURI="mostrarReferencia.html" size="{size}"
						
						 export="true">
						<display:setProperty name="export.excel.export_amount"
							value="list" />

						<display:column class="celdaAlineadoCentrado" media="html"
							title="<input type='checkbox' id='checktodos' name='checktodos'/>">
							<c:if
								test="${(requerimientoElemento.pathArchivoDigital!=null && requerimientoElemento.pathArchivoDigital!='') 
							|| (requerimientoElemento.pathLegajo!=null && requerimientoElemento.pathLegajo!='')}">
								<input type="checkbox" class='checklote'
									value="${requerimientoElemento.idReferencia}" />
							</c:if>
						</display:column>

						<display:column media="html" sortName="tipoElementos.descripcion"
							sortable="true" property="descripcionTipoElemento"
							titleKey="formularioRequerimientoElemento.display.tipoElemento"
							class="celdaAlineadoIzquierda" />

						<display:column media="html" sortName="elementos.codigo" sortable="true"
							titleKey="formularioRequerimientoElemento.display.codigoElemento"
							class="celdaAlineadoIzquierda">
							<c:if
								test="${requerimientoElemento.pathLegajo==null || requerimientoElemento.pathLegajo==''}">
									<c:out value="${requerimientoElemento.codigoElemento}"
									default=""></c:out>
							</c:if>
							<c:if
								test="${requerimientoElemento.pathLegajo!=null && requerimientoElemento.pathLegajo!=''}">
								<a
									href="verLegajo.html?fileName=${requerimientoElemento.pathLegajo}"
									target="_blank"><c:out
										value="${requerimientoElemento.codigoElemento}"></c:out></a>
							</c:if>
						</display:column>
						<display:column media="excel" sortName="elementos.codigo" sortable="true"
							property="elemento"
							titleKey="formularioRequerimientoElemento.display.codigoElemento"
							class="celdaAlineadoIzquierda" />
						
							<display:column media="excel" sortName="ee.codigo" sortable="true"
							property="caja"
							titleKey="formularioRequerimientoElemento.display.contenedor"
							class="celdaAlineadoIzquierda" />
						

						<display:column media = "html" sortName="ee.codigo" sortable="true"
							property="codigoContenedor"
							titleKey="formularioRequerimientoElemento.display.contenedor"
							class="celdaAlineadoIzquierda" />
						<display:column media="html" sortName="posicion" sortable="false"
							titleKey="formularioRequerimientoElemento.display.posicion"
							class="celdaAlineadoIzquierda">
							<c:choose>
								<c:when test="${requerimientoElemento.posicionCon != null}">
									<c:out value="${requerimientoElemento.posicionCon}" default="" />
								</c:when>
								<c:otherwise>
									<c:if test="${requerimientoElemento.posicionEle != null}">
										<c:out value="${requerimientoElemento.posicionEle}" default="" />
									</c:if>
								</c:otherwise>
							</c:choose>
						</display:column>
						<display:column property="clasificacionDocumental"
							titleKey="formularioRequerimientoElemento.display.clasificacionDocumental"
							class="celdaAlineadoIzquierda" />
						
						<display:column property="pathLegajo1" media="excel"
							title="Archivo"
							class="celdaAlineadoIzquierda" />
						<display:column media="html" sortName="referencia.fecha1" sortable="true"
							property="fecha1Str"
							titleKey="formularioRequerimientoElemento.display.fecha1"
							class="celdaAlineadoIzquierda" />
						<display:column  sortName="referencia.fecha2" sortable="true"
							property="fecha2Str"
							titleKey="formularioRequerimientoElemento.display.fecha2"
							class="celdaAlineadoIzquierda" />
						<display:column   sortName="referencia.texto1" sortable="true"
							property="texto1"
							titleKey="formularioRequerimientoElemento.display.texto1"
							class="celdaAlineadoIzquierda" />
						<display:column  sortName="referencia.texto2" sortable="true"
							property="texto2"
							titleKey="formularioRequerimientoElemento.display.texto2"
							class="celdaAlineadoIzquierda" />
						<display:column  sortName="referencia.numero1" sortable="true"
							property="numero1"
							titleKey="formularioRequerimientoElemento.display.numero1"
							class="celdaAlineadoDerecha" />
						<display:column   sortName="referencia.numero2" sortable="true"
							property="numero2"
							titleKey="formularioRequerimientoElemento.display.numero2"
							class="celdaAlineadoDerecha" />

						<display:column media="html" sortName="referencia.descripcion" sortable="true"
							property="descripcionReferencia"
							titleKey="formularioRequerimientoElemento.display.descripcion"
							class="celdaAlineadoDerecha" />		
						<display:column sortName="referencia.cImagenes" sortable="true"
							property="cantImagenes"
							titleKey="formularioRequerimientoElemento.display.cImagenes"
							class="celdaAlineadoDerecha" />	
					
						<display:column media="html" sortName="elementos.estado" sortable="true"
							property="estadoElemento"
							titleKey="formularioRequerimientoElemento.display.estado"
							class="celdaAlineadoIzquierda" />
						<display:column media="html" sortName="idLote" sortable="false"
							property="idLoteReferencia"
							titleKey="formularioRequerimientoElemento.display.lote"
							class="celdaAlineadoIzquierda" />
						<display:column media="html" class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_path"
								value="${requerimientoElemento.pathArchivoDigital}" />
							<input type="hidden" id="hdn_idReferencia"
								value="${requerimientoElemento.idReferencia}" />
						</display:column>
						<display:column media="html" sortName="cliente" sortable="false"
							property="cliente"
							titleKey="formularioRequerimientoElemento.display.cliente"
							class="celdaAlineadoIzquierda" />
						
						<display:column media="html" sortName="referencia.texto1" sortable="false"
							property="userAsig" title="Usr.Tarea"
							class="celdaAlineadoIzquierda" />
						<display:column media="html" sortName="referencia.texto1" sortable="false"
							property="descripcionTarea" title="Tarea"
							class="celdaAlineadoIzquierda" />
					</display:table>
				</fieldset>
			
				<br style="font-size: xx-small;" /> <br
					style="font-size: xx-small;" />
			</fieldset>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
	<div class="selectorDiv"></div>
	<div id="pop" style="display: none">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0"
			width="20px" height="20px"> <label><spring:message
				code="textos.espere" htmlEscape="true" /></label>
	</div>
	<!-- ----------	fancybox ------------------------- -->
	<a id="fancyboxImagenes" style="visibility: hidden;"
		href="iniciarPopUpLoteFacturacionDetalle.html"></a>
</html>
