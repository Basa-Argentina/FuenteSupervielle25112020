<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page buffer = "32kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<link rel="stylesheet" href="css/forms.css" type="text/css"/>
		<link rel="stylesheet" href="css/displaytag.css" type="text/css"/>
		<link rel="stylesheet" href="css/calendar.css" type="text/css"/>
		<link rel="stylesheet" href="css/ini.css" type="text/css" />
		<link rel="stylesheet" href="css/jquery.alerts.css" type="text/css" />
		<link rel="stylesheet" href="css/tabs.css" type="text/css" />
		<link rel="stylesheet" href="css/folders.css" type="text/css" />
		<style type="text/css">
			BODY{
				font-family:Trebuchet MS, Arial, Helvetica, sans-serif;
				font-size: 12px;
			}
			.tooltip {
			    background: url("images/mavalos_tooltip/yellow_arrow_small.png") repeat scroll 0 0 transparent;
			    color: #330099;
			    display: none;
			    font-size: 11px;
			    font-weight: bold;
			    height: 25px;
			    padding: 25px;
			    text-align: center;
			    width: 75px;
			}
		</style>
		<title>
			<spring:message code="formularioLoteReferencia.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/ini.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/formulario_referencia.js"></script>
		<script type="text/javascript" src="js/checkEAN.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
			    <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
				<li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<c:if test="${param.accion != 'CONSULTA'}">
						<form:form action="agregarReferencia.html" commandName="referenciaFormulario" method="post" id="referenciaFormulario">
							<input type="hidden" id="idLoteReferencia" name="idLoteReferencia" value=""/>
							<input type="hidden" id="id" name="id" value="<c:out value="${referencia.id}" default="" />"/>
							<input type="hidden" id="obj_hash" name="obj_hash" value="<c:out value="${objectHash}" default="" />"/>
							<input type="hidden" id="indiceIndividual" name="indiceIndividual" value="<c:out value="${referencia.indiceIndividual}" default="" />"/>
							<input type="hidden" id="porRango" name="porRango" value="<c:out value="${porRango}" default="" />"/>
							<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
							<input type="hidden" id="mensajeSeleccioneContenedor" value="<spring:message code="notif.seleccion.seleccioneContenedor" htmlEscape="true"/>"/>
							<input type="hidden" id="mensajeSeleccioneClasificacionDocumental" value="<spring:message code="notif.seleccion.seleccioneClasificacionDocumental" htmlEscape="true"/>"/>
							<input type="hidden" id="codigoCliente" name="codigoCliente" value=""/>
							<input type="hidden" id="clienteCodigoRequerimientoElemento" name="clienteCodigoRequerimientoElemento" value=""/>
							<input type="hidden" id="hacerFocusEn" value="${hacerFocusEn}"/>
							<input type="hidden" id="codigoContenedorComparar" name="codigoContenedorComparar" value="<c:out value="${codigoContenedorComparar}" default="" />"/>
							<input type="hidden" id="bloqueoNumero1Hijo" name="bloqueoNumero1Hijo" value="${bloqueoNumero1Hijo}" />
							<input type="hidden" id="numero1Hijo" name="numero1Hijo" value="${numero1Hijo}" />
							<input type="hidden" id="bloqueoTexto1Hijo" name="bloqueoTexto1Hijo" value="${bloqueoTexto1Hijo}" />
							<input type="hidden" id="texto1Hijo" name="texto1Hijo" value="${texto1Hijo}" />
							<input type="hidden" id="bloqueoNumero2Hijo" name="bloqueoNumero2Hijo" value="${bloqueoNumero2Hijo}" />
							<input type="hidden" id="numero2Hijo" name="numero2Hijo" value="${numero2Hijo}" />
							<input type="hidden" id="bloqueoTexto2Hijo" name="bloqueoTexto2Hijo" value="${bloqueoTexto2Hijo}" />
							<input type="hidden" id="texto2Hijo" name="texto2Hijo" value="${texto2Hijo}" />
							<input type="hidden" id="leeCodigoBarraDesde" name="leeCodigoBarraDesde" value="${referencia.clasificacionDocumental.codigoBarraDesde}" />
							<input type="hidden" id="leeCodigoBarraHasta" name="leeCodigoBarraHasta" value="${referencia.clasificacionDocumental.codigoBarraHasta}" />
							<input type="hidden" id="leeCodigoBarra" name="leeCodigoBarra" value="${referencia.clasificacionDocumental.leeCodigoBarra}" />
							<input type="hidden" id="pathArchivoDigital" name="pathArchivoDigital" value="${referencia.pathArchivoDigital}" />
							<input type="hidden" id="pathLegajo" name="pathLegajo" value="${referencia.pathLegajo}" />
							<input type="hidden" id="mensajeErrorEAN" name="mensajeErrorEAN" value="<spring:message code="formularioOperacionReferencia.error.codigoBarraIncorrecto" htmlEscape="true"/>"/>
							<sec:authorize ifAnyGranted="ROLE_ELEM_HABILITAR_CERRAR">
								<input type="hidden" id="roleCerrarHdn" name="roleCerrarHdn" value="true" />
							</sec:authorize>
							
							<fieldset>
								<legend>
									<font size="1">
										<spring:message code="formularioLoteReferencia.titulo.registarReferencia" htmlEscape="true"/>
									</font>
								</legend>
								
								<div style="background-color: white; WIDTH: auto;" align="center">
									<table class="busqueda" style="width: 100%; background-color: white;">
										<tr>
										<c:if test="${porRango == null || porRango == false || porRango == ''}">
											<td class="texto_ti" colspan="1">
												<spring:message	code="formularioLoteReferencia.referencia.prefijoCodigotipoElemento" htmlEscape="true" />
											</td>
										</c:if>
											<td class="texto_ti" colspan="2">
												<spring:message	code="formularioLoteReferencia.referencia.contenedor" htmlEscape="true" />
											</td>
											<td class="texto_ti" colspan="1">
												<c:if test="${referencia.indiceIndividual}">
													<spring:message code="formularioLoteReferencia.referencia.elemento" htmlEscape="true"/>
												</c:if>
											</td>										
										</tr>				
										<tr>
									<c:if test="${porRango == null || porRango == false || porRango == ''}">
											<td class="texto_ti" colspan="1">
												<c:if test="${bloqueoTipoElementoContenedor}">
													<img id="bloquearTipoElementoContenedor" name="bloquearTipoElementoContenedor"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
													<input type="hidden" id="bloqueoTipoElementoContenedor" name="bloqueoTipoElementoContenedor" value="true" />
												</c:if>
												<c:if test="${!bloqueoTipoElementoContenedor}">
													<img id="bloquearTipoElementoContenedor" name="bloquearTipoElementoContenedor"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
													<input type="hidden" id="bloqueoTipoElementoContenedor" name="bloqueoTipoElementoContenedor" value="false" />
												</c:if>
												<!-- Modifica Arian harcodeo "11"  20/07/2017 -->
												<%-- value="<c:out value="${referencia.prefijoCodigoTipoElemento}" default="" />"  --%>
												<input type="text" id="prefijoCodigoTipoElemento" class="requerido"
														name="prefijoCodigoTipoElemento" maxlength="3" style="width: 50px;"
														value="11" <c:if test="${fijarContenedor}">
															readonly="readonly"
														</c:if>
														
												 />
												&nbsp;&nbsp;
												<button type="button" id="buscaTipoElemento"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${fijarContenedor}">
														disabled="disabled"
													</c:if>
												>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>
												&nbsp;&nbsp; 
												<label id="prefijoCodigoTipoElementoLabel" for="prefijoCodigoTipoElemento">  </label>
											</td>
										</c:if>
											<td class="texto_ti" colspan="2">
												<c:if test="${bloqueoContenedor}">
													<img id="bloquearContenedor" name="bloquearContenedor"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
													<input type="hidden" id="bloqueoContenedor" name="bloqueoContenedor" value="true" />
												</c:if>
												<c:if test="${!bloqueoContenedor}">
													<img id="bloquearContenedor" name="bloquearContenedor"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
													<input type="hidden" id="bloqueoContenedor" name="bloqueoContenedor" value="false" />
												</c:if>
												<input type="text" id="codigoContenedor" class="requerido"
													name="codigoContenedor" maxlength="12" style="width: 90px;" tabindex="1"
													value="<c:out value="${referencia.contenedor.codigo}" default="" />"
													<c:if test="${param.accion == 'CONSULTA' || fijarContenedor}">
														readonly="readonly"
													</c:if>
												 />
												&nbsp;&nbsp;
													<input type="text" id="codigoConteEAN" name="codigoConteEAN" tabindex="2"
														style="width: 10px;" class="requerido"
														value='<c:out value="${referencia.elementoContenedor.digitoControlCodigoEAN13}" default=""/>'
														<c:if test="${accion == 'CONSULTA' && referencia.indiceIndividual}">
															disabled="disabled"
														</c:if>
													/>
													&nbsp;
												<button type="button" id="buscaContenedor"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${param.accion == 'CONSULTA' || fijarContenedor}">
														disabled="disabled"
													</c:if>
												>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>
												&nbsp;&nbsp; 
												<label id="codigoContenedorLabel" for="codigoContenedor">  </label>
											</td>
											<td class="texto_ti" colspan="1">
												<c:if test="${referencia.indiceIndividual}">
													<input type="text" id="codigoElemento" name="codigoElemento" tabindex="2"
														style="width: 90px;" class="requerido"
														value='<c:out value="${referencia.elementoContenido.codigoBarra}" default=""/>'
														<c:if test="${accion == 'CONSULTA' && referencia.indiceIndividual}">
															disabled="disabled"
														</c:if>
													/>
													&nbsp;&nbsp;
													<input type="text" id="codigoEAN" name="codigoEAN" tabindex="2"
														style="width: 10px;" class="requerido"
														value='<c:out value="${referencia.elementoContenido.digitoControlCodigoEAN13}" default=""/>'
														<c:if test="${accion == 'CONSULTA' && referencia.indiceIndividual}">
															disabled="disabled"
														</c:if>
													/>
													&nbsp;
													<button type="button" id="buscaElemento" 
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													>
														<img src="<%=request.getContextPath()%>/images/buscar.png">
													</button>
													<c:if test="${incrementoElemento}">	
														<img id="incrementarElemento" name="incrementarElemento"
															src="images/incrementar.png" align="top" title="<spring:message code="formularioLoteReferencia.referencia.autoincrementarElemento" htmlEscape="true"/>"> 
												    	<input type="hidden" id="incrementoElemento" name="incrementoElemento" value="true"/>
												    </c:if>
													<c:if test="${!incrementoElemento}">
														<img id="incrementarElemento" name="incrementarElemento"
															src="images/noIncrementar.png" align="top" title="<spring:message code="formularioLoteReferencia.referencia.autoincrementarElemento" htmlEscape="true"/>">
												    	<input type="hidden" id="incrementoElemento" name="incrementoElemento" value="false"/>
												    </c:if>
													&nbsp;&nbsp; 
													<label id="codigoElementoLabel" for="codigoElemento"> 													 
													</label>
												</c:if>
											</td>					
										</tr>
										<tr>
											<td class="texto_ti" colspan="4">
												<spring:message code="formularioLoteReferencia.referencia.clasificacionDocumental" htmlEscape="true"/>
											</td>
										</tr>
										<tr>	
											<td class="texto_ti" colspan="3">
												<c:if test="${bloqueoClasificacionDocumental}">
													<img id="bloquearClasificacionDocumental" name="bloquearClasificacionDocumental"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    <input type="hidden" id="bloqueoClasificacionDocumental" name="bloqueoClasificacionDocumental" value="true"/>
											    </c:if>
											    <c:if test="${!bloqueoClasificacionDocumental}">
													<img id="bloquearClasificacionDocumental" name="bloquearClasificacionDocumental"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    <input type="hidden" id="bloqueoClasificacionDocumental" name="bloqueoClasificacionDocumental" value="false" 
												    />
											    </c:if>
												<input type="text" id="codigoClasificacionDocumental" name="codigoClasificacionDocumental" tabindex="3"
													style="width: 80px;" class="requerido" <c:if test="${bloqueoClasificacionDocumental}">readonly="readonly"</c:if>
													value="<c:out value="${referencia.clasificacionDocumental.codigo}" default="" />"/>
												&nbsp;&nbsp;
												<button type="button" id="buscaClasificacionDocumental"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
												>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>
												&nbsp;&nbsp; 
												<label id="codigoClasificacionDocumentalLabel" for="codigoClasificacionDocumental"> 
													 <c:out value="${referencia.clasificacionDocumental.nombre}" default="" />
												</label>
											</td>
											<td class="texto_ti" colspan="3">
												<input type="checkbox" id="cerrarCaja" name="cerrarCaja" value="true">&nbsp;
												<label id="labelCajaCerrada" for="labelCajaCerrada"> 
													 <c:out value="Caja Cerrada" />
												</label>
<%-- 												<spring:message  code="formularioLoteReferencia.referencia.cerrarCaja" htmlEscape="true"/>	 --%>
											</td>
										</tr>
								<c:if test="${porRango != null && porRango == true}">
										<tr>
										<td colspan="6">
										<fieldset>
										<legend>
											<font size="1">
												<spring:message code="formularioLoteReferencia.titulo.rangoReferencia" htmlEscape="true"/>
											</font>	
										</legend>
										<table align="center">
										<tr>
											<td class="texto_ti" colspan="2">
												<spring:message code="formularioLoteReferencia.rangoDesde" htmlEscape="true"/>
											</td>
											<td class="texto_ti" colspan="2">
												<spring:message code="formularioLoteReferencia.rangoHasta" htmlEscape="true"/>
											</td>
										</tr>		
										<tr>
											<td class="texto_ti" colspan="2">
												<input type="text" id="codigoElementoDesde" name="codigoElementoDesde" tabindex="2"
														style="width: 90px;" class="requerido"
														value='<c:out value="${referencia.codigoElementoDesde}" default=""/>'
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													/>
													&nbsp;&nbsp;
													<button type="button" id="buscaElementoDesde" 
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													>
														<img src="<%=request.getContextPath()%>/images/buscar.png">
													</button>
													&nbsp;&nbsp; 
													<label id="codigoElementoDesdeLabel" for="codigoElementoDesde"> 													 
													</label>
												
											</td>
											<td class="texto_ti" colspan="2">
												<input type="text" id="codigoElementoHasta" name="codigoElementoHasta" tabindex="2"
														style="width: 90px;" class="requerido"
														value='<c:out value="${referencia.codigoElementoHasta}" default=""/>'
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													/>
													&nbsp;&nbsp;
													<button type="button" id="buscaElementoHasta" 
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													>
														<img src="<%=request.getContextPath()%>/images/buscar.png">
													</button>
													&nbsp;&nbsp; 
													<label id="codigoElementoHastaLabel" for="codigoElementoHasta"> 													 
													</label>
												
											</td>
											</tr>
											</table>
											</fieldset>
											</td>
										</tr>
									</c:if>	
										<c:if test="${referencia.clasificacionDocumental!=null && porRango == null || porRango == false || porRango == ''}">
									<tr>
												<td class="texto_ti" id="tit_numero1">
				
													<c:if test="${referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.individualNumero1Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.grupalNumero1Titulo}" escapeXml="true"/>
													</c:if>
													
												</td>
												
													<td class="texto_ti" id="tit_texto2" >
											
													<c:if test="${referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.individualTexto2Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.grupalTexto2Titulo}" escapeXml="true"/>
													</c:if>
												</td>
												
											</tr>
											
											<tr>
												<td class="texto_ti">
													<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualNumero1Seleccionado
															   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalNumeroSeleccionado}">
														<c:if test="${bloqueoNumero1}">
														<img id="bloquearNumero1" name="bloquearNumero1"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoNumero1" name="bloqueoNumero1" value="true" />
												    	</c:if>
												    	<c:if test="${!bloqueoNumero1}">
														<img id="bloquearNumero1" name="bloquearNumero1"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoNumero1" name="bloqueoNumero1" value="false" />
												    	</c:if>
														<input type="text" id="numero1" class="requerido" name="numero1" style="width: 100px;" tabindex="4" <c:if test="${bloqueoNumero1}">readonly="readonly"</c:if>
															<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualNumero1Requerido
															  		   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalNumeroRequerido}">
																class="requerido"
															</c:if>
															value="<c:out value="${referencia.numero1}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														/>
													</c:if>
												</td>
												<td class="texto_ti" >
													<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualTexto2Seleccionado
														 || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalTextoSeleccionado}">
														<c:if test="${bloqueoTexto2}">
														<img id="bloquearTexto2" name="bloquearTexto2"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoTexto2" name="bloqueoTexto2" value="true" />
												    	</c:if>
												    	<c:if test="${!bloqueoTexto2}">
														<img id="bloquearTexto2" name="bloquearTexto2"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoTexto2" name="bloqueoTexto2" value="false" />
												    	</c:if>
														<input type="text" id="texto2" class="requerido" name="texto2" style="width: 100px;" tabindex="4" maxlength="1255"
															<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualTexto2Requerido
															  		   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalTextoRequerido}">
																class="requerido"
															</c:if>
															value="<c:out value="${referencia.texto2}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														/>
													</c:if>
											
												</td>
											<%--	</tr> --%>
											<%--	</table> --%>
											<%--	</div> --%>
											<%--	</fieldset> --%>
											</c:if>
								
											<tr>
												<td class="texto_ti" id="tit_texto1" colspan="1">
													<c:if test="${referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.individualTexto1Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.grupalTexto1Titulo}" escapeXml="true"/>
													</c:if>
												</td>
												<td class="texto_ti" id="tit_fecha1" colspan="1">
													<c:if test="${referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.individualFecha1Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.grupalFecha1Titulo}" escapeXml="true"/>
													</c:if>
												</td>
												<td class="texto_ti" id="tit_fecha2" colspan="1">
													<c:if test="${referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.individualFecha2Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.grupalFecha2Titulo}" escapeXml="true"/>
													</c:if>
												</td>
											
											</tr>
											<tr>
												<td class="texto_ti" id="tit_texto1" colspan="1" >
													<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualTexto1Seleccionado
															   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalTextoSeleccionado}">
														<c:if test="${bloqueoTexto1}">
														<img id="bloquearTexto1" name="bloquearTexto1"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoTexto1" name="bloqueoTexto1" value="true" />
												    	</c:if>
												    	<c:if test="${!bloqueoTexto1}">
														<img id="bloquearTexto1" name="bloquearTexto1"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoTexto1" name="bloqueoTexto1" value="false" />
												    	</c:if>
														<input type="text" id="texto1" name="texto1" style="width: 300px;" tabindex="8" maxlength="1255"
															
															value="<c:out value="${referencia.texto1}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														/>
													</c:if>
												</td>
														
												<td class="texto_ti" colspan="1">
													<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualFecha1Seleccionado
															   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalFechaSeleccionado}">
														
														<c:if test="${bloqueoFecha1}">
														<img id="bloquearFecha1" name="bloquearFecha1"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoFecha1" name="bloqueoFecha1" value="true" />
												    	</c:if>
												    	<c:if test="${!bloqueoFecha1}">
														<img id="bloquearFecha1" name="bloquearFecha1"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoFecha1" name="bloqueoFecha1" value="false" />
												    	</c:if>
														<input type="text" id="fecha1" maxlength="10" name="fecha1" style="width: 80px;" tabindex="6"
															<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualFecha1Requerido
															  		   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalFechaRequerido}">
																class="requerido"
															</c:if>
															value="<c:out value="${referencia.fecha1Str}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														/>
														<c:if test="${accion != 'CONSULTA'}">
															<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'referenciaFormulario',
																	// input name
																	'controlname': 'fecha1'
																});
															</script>
														</c:if>
													</c:if>
												</td>
												<td class="texto_ti" colspan="1">
													<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualFecha2Seleccionado
															   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalFechaSeleccionado}">
														<c:if test="${bloqueoFecha2}">
														<img id="bloquearFecha2" name="bloquearFecha2"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoFecha2" name="bloqueoFecha2" value="true" />
												    	</c:if>
												    	<c:if test="${!bloqueoFecha2}">
														<img id="bloquearFecha2" name="bloquearFecha2"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoFecha2" name="bloqueoFecha2" value="false" />
												    	</c:if>
														<input type="text" id="fecha2" maxlength="10"   name="fecha2" style="width: 80px;" tabindex="7"
															<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualFecha2Requerido
															  		   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalFechaRequerido}">
																class="requerido"
															</c:if>
															
															value="<c:out value="${referencia.fecha2Str}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															
														/>
														
														<c:if test="${accion != 'CONSULTA'}">
														
															<script type="text/javascript" >
															
																new tcal ({
																	// form name
																	'formname': 'referenciaFormulario',
																	// input name
																	'controlname': 'fecha2'
																});
															</script>
														</c:if>
													</c:if>
												</td>
												
														<input type="checkbox" id="chkDoubleInput" name="chkDoubleInput"  class="hidden" value="true" title="Ingresar texto por duplicado y verificar"
														<c:if test="${chkDoubleInput!=null && chkDoubleInput}">
															checked="checked"
														</c:if>	>
														
	
												
											
										
											</tr>
											<c:if test="${referencia.clasificacionDocumental.descripcionSeleccionado}">
												<tr>
													<td class="texto_ti" colspan="1">
														<c:if test="${referencia.clasificacionDocumental.descripcionTitulo != null}">
															<c:out value="${referencia.clasificacionDocumental.descripcionTitulo}" escapeXml="true"/>
														</c:if>
														<c:if test="${referencia.clasificacionDocumental.descripcionTitulo == null}">
															<spring:message code="formularioLoteReferencia.referencia.descripcion" htmlEscape="true"/>
														</c:if>
													</td>
													
													<td class="texto_ti" id="tit_numero2">
													<c:if test="${referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.individualNumero2Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!referencia.indiceIndividual}">
														<c:out value="${referencia.clasificacionDocumental.grupalNumero2Titulo}" escapeXml="true"/>
													</c:if>
												</td>
												</tr>
												<tr>
													<td class="texto_ti" colspan="1">
														<textarea id="descripcion" name="descripcion" cols="50" rows="2" tabindex="10"
															<c:if test="${referencia.clasificacionDocumental.descripcionRequerido}">
																class="requerido"
															</c:if>
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															
														>${referencia.descripcion}</textarea>
														
													<%-- 	<button type="button" id="buscaDescripcion"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button> --%>
														
														
													</td>
													
													<td class="texto_ti">
													<c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualNumero2Seleccionado
															   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalNumeroSeleccionado}">
														<c:if test="${bloqueoNumero2}">
														<img id="bloquearNumero1" name="bloquearNumero1"
														src="images/candado_cerrado.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoNumero2" name="bloqueoNumero2" value="true" />
												    	</c:if>
												    	<c:if test="${!bloqueoNumero2}">
														<img id="bloquearNumero2" name="bloquearNumero2"
														src="images/candado_abierto.gif" width="17" height="20" align="top"> 
														&nbsp;&nbsp;
												    	<input type="hidden" id="bloqueoNumero2" name="bloqueoNumero2" value="false" />
												    	</c:if>
														<input type="text" id="numero2" name="numero2" style="width: 100px;" tabindex="5"
															<%-- <c:if test="${referencia.indiceIndividual && referencia.clasificacionDocumental.individualNumero2Requerido
															  		   || !referencia.indiceIndividual && referencia.clasificacionDocumental.grupalNumeroRequerido}">
																class="requerido"
															</c:if> --%>
															value="<c:out value="${referencia.numero2}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														/>
													</c:if>
												</td>
									
												</tr>
											</c:if>										
									<%--	</c:if> --%>
										
												
										
									</table>
										<br style="font-size: xx-small;"/>
						<sec:authorize ifAnyGranted="ROLE_REFERENCIA_ASIGNAR_TAREA">
									<c:if test="${referencia.indiceIndividual}">
									<fieldset>
									
						<legend >
							<font size="1">
								Asignar Tarea
							</font>
						</legend><table><tr>
													<td class="texto_ti">
														Usuario:
													</td>
													<td class="texto_ti" colspan="3">
														Tarea:
													</td>
												</tr>
											<tr>
												<td class="texto_ti" >
											
											<input type="text" id="codigoUsuario"
											name="codigoUsuario" maxlength="6" style="width: 90px;"
											value="<c:out value="${referencia.codigoUsuario}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupUsuario"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoUsuarioLabel"
											for="codigoUsuario"> <c:out
													value=""
													default="" /> </label>
											</td>

												<td class="texto_ti" colspan="3">
												<input type="text"
													id="descripcionTarea" name="descripcionTarea"
													tabindex="2" style="width: 400px;"
													value='<c:out value="${referencia.descripcionTarea}" default=""/>'
													<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if> />
												</td>
											</tr></table></fieldset></c:if></sec:authorize>
								</div>	
								<br style="font-size: xx-small;"/>
								<div align="center">
									<button name="guardar" type="button" class="botonCentrado" id="btnGuardarReferencia" tabindex="11">
										<img src="<%=request.getContextPath()%>/images/ok.png">
										<spring:message code="botones.guardar" htmlEscape="true" />
									</button>
									&nbsp;
									<button name="cancelar" type="button" id="btnCancelar" class="botonCentrado">
										<img src="<%=request.getContextPath()%>/images/cancelar.png">
										<spring:message code="botones.cancelar" htmlEscape="true" />
									</button>
								</div>
								<div align="center">
									<spring:message code="textos.crtlS" htmlEscape="true" />
								</div>
							</fieldset>
						</form:form>	
					</c:if>
					<br style="font-size: xx-small;"/>
					
					<sec:authorize ifAnyGranted="ROLE_CARGA_REFERENCIA_VER_IMAGEN">
					<c:if test="${referencia!=null && referencia.id!=null && referencia.pathLegajo!=null && referencia.pathLegajo!=''}">
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.buscar" htmlEscape="true"/>
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							
								<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"  id="busquedaDiv" align="center">
									<iframe id="iframeVerLegajo" src="verLegajo.html?fileName=${referencia.pathLegajo}" style="width: 100%; height: 400px;"></iframe>
								</div>
							
						</fieldset>
					</c:if>
					</sec:authorize>
					
					<br style="font-size: xx-small;"/>
					<fieldset>
						<legend>
							<font size="1">
								<spring:message code="formularioLoteReferencia.titulo.referenciasRegistadas" htmlEscape="true"/>
							</font>
						</legend>
						<form name="sortForm" id="sortForm" action="seccionReferencias.html">
							<input type="hidden" name="indiceIndividual" value="<c:out value="${referencia.indiceIndividual}" default="" />"/>
							<input type="hidden" name="porRango" value="<c:out value="${porRango}" default="" />"/>
						</form>
						<display:table name="sessionScope.referencias_lote" id="referencias_lote" defaultsort="3" sort="list" keepStatus="false"
						requestURI="seccionReferencias.html">
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_id" value="${referencias_lote.obj_hash}"/>
							</display:column>		
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
							</display:column>
							<display:column property="clasificacionDocumental.nombre" titleKey="formularioLoteReferencia.listaRef.clasificacion" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="indiceIndividualStr" titleKey="formularioLoteReferencia.listaRef.indice" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="elementoContenedor.codigo" titleKey="formularioLoteReferencia.listaRef.contenedor" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="elementoContenido.codigo" titleKey="formularioLoteReferencia.listaRef.elemento" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="numero1" titleKey="formularioLoteReferencia.listaRef.numero1" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="numero2" titleKey="formularioLoteReferencia.listaRef.numero2" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="fecha1Str" titleKey="formularioLoteReferencia.listaRef.fecha1" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="fecha2Str" titleKey="formularioLoteReferencia.listaRef.fecha2" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="texto1" titleKey="formularioLoteReferencia.listaRef.texto1" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="texto2" titleKey="formularioLoteReferencia.listaRef.texto2" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column class="celdaAlineadoCentrado">
								<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="${referencias_lote.descripcion}">
							</display:column>
						</display:table> 
					</fieldset>
					<br style="font-size: xx-small;"/>
				</fieldset>	
			</div>	
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<div class="selectorDiv"></div>
		<div id="pop" style="display:none">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
		</div>
	</body>
</html>