<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
			<spring:message code="formularioLoteRearchivo.titulo" htmlEscape="true"/>
		</title>
		
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/formulario_rearchivo.js"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/jquery.chromatable.js"></script>		
		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg}); seleccionarItemTabla(${seleccionarItemTabla}); cargarComboJPGDigital(${rearchivoSeleccionado.cantidadImagenes})">
	  	<div id="contenedorGeneral">
			<div class="contenido" align="left">
				<fieldset style="border: none; text-align: left; width: 97%;">
						<c:if test="${loteRearchivoSession.clasificacionDocumental==null}">
							<div id="fileDiv" style="display:none">
								<fieldset>
									<legend>
										<spring:message code="formularioLoteRearchivo.tipo.digital" htmlEscape="true"/>
									</legend>
									<form:form method="POST" commandName="fileUploadForm"
										enctype="multipart/form-data" name="fileUploadForm" id="fileUploadForm"
										action="importarArchivoLoteRearchivo.html">
										<input type="hidden" id="mensajeSeleccionarFile" value="<spring:message code="formularioLoteRearchivo.error.seleccionarFile" htmlEscape="true"/>"/>
										<input type="hidden" id="mensajeSeleccionarFileTIFF" value="<spring:message code="formularioLoteRearchivo.error.seleccionarFileTIFF" htmlEscape="true"/>"/>
										<input type="hidden" id="mensajeSeleccionarFilePDF" value="<spring:message code="formularioLoteRearchivo.error.seleccionarFilePDF" htmlEscape="true"/>"/>
										
										<table>
											<tr>
												<td>
												</td>
												<td>
													<spring:message code="formularioLoteRearchivo.archivo" htmlEscape="true"/>
												</td>
											</tr>
											<tr>
												<td>
													<input	type="hidden" id="codigoClasificacionDocumental2" name="codigoClasificacionDocumental2"
													/>
													<input type="hidden" id="codigoContenedor2" name="codigoContenedor2"
													/>
													<input type="hidden" id="codigoCliente2" name="codigoCliente2"
													/>
													<input type="hidden" id="indiceIndividual2" name="indiceIndividual2"
													/>
													<input type="hidden" id="tipo2" name="tipo2"
													/>
													<input type="hidden" id="codigoEmpresa2" name="codigoEmpresa2"
													/>
													<input type="hidden" id="codigoSucursal2" name="codigoSucursal2"
													/>
													<sec:authorize ifAnyGranted="ROLE_MOD_FORMATO_DIGITAL_REARCHIVO"> 
														<spring:message code="formularioLoteRearchivo.tipo.formato" htmlEscape="true"/>  
														<select id="tipoImg" name="tipoImg">
															<option value="TIFF">TIFF</option>
															<option value="PDF">PDF</option>
															<option value="JPG">JPG</option>
														</select>
													</sec:authorize>
												</td>
												<td class="texto_td">
													<input type="file" name="file" id="file" accept="application/zip"
														class="requerido" style="height: 22px;" />
												</td>
												
												<td class="texto_td">
													<input type="button"
														onclick="pasar();" name="Importar"
														class="botonCentrado" value="Importar"> 
														<span>
															<form:errors
																path="file" cssClass="error" />
														</span>
												</td>
											</tr>
										</table>
									</form:form>
								</fieldset>
							</div>
						</c:if>
						
						<form:form action="agregarRearchivo.html" commandName="rearchivoFormulario" method="post" id="rearchivoFormulario">
							<input type="hidden" id="id" name="id" value="<c:out value="${rearchivo.id}" default="" />"/>
							<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
							<input type="hidden" id="mensajeSeleccioneContenedor" value="<spring:message code="notif.seleccion.seleccioneContenedor" htmlEscape="true"/>"/>
							<input type="hidden" id="codigoCliente" name="codigoCliente" value=""/>
							<input type="hidden" id="hacerFocusEn" value="${hacerFocusEn}"/>
							
							<input type="hidden" id="mensajeIngresarCantidad" value="<spring:message code="formularioLoteRearchivo.error.ingresarCantidad" htmlEscape="true"/>"/>
							<input type="hidden" id="mensajeSeleccionarClasificacion" value="<spring:message code="formularioLoteRearchivo.error.seleccionarClasificacion" htmlEscape="true"/>"/>
							
							<input type="hidden" id="objetoSeleccionado" name="objetoSeleccionado" value="${id_hash}"/>
							
							<input type="hidden" id="pathArchivoJPGDigitalAux" name="pathArchivoJPGDigitalAux" value="<c:out value="${rearchivoSeleccionado.pathArchivoJPGDigital}" default="" />"/>
							
							
							
							<div style="background-color: white; WIDTH: auto;" align="center">
								<fieldset>
									<table class="busqueda" style="width: 100%; background-color: white;">
										<tr>
											
											<td class="texto_ti" colspan="2">
												<spring:message code="formularioLoteRearchivo.cantidad" htmlEscape="true"/>
											</td>
											<td class="texto_ti" colspan="2" id="tdContenedorTitulo" 
											<c:if test="${loteRearchivoSession.tipo!=null && loteRearchivoSession.tipo == 'Electronico'}">
												style="display:none;"
											</c:if>>
												<spring:message code="formularioLoteRearchivo.rearchivo.contenedor" htmlEscape="true"/>
											</td>
										</tr>
											
										<tr>
											<td class="texto_ti" colspan="2" >
												<input type="text" id="cantidad" name="cantidad" maxlength="10"
													style="width: 90px; text-align: right;" class="requerido"
													value="<c:out value="${loteRearchivoSession.cantidad}" default="" />"
													<c:if test="${accion == 'CONSULTA' || loteRearchivoSession.clasificacionDocumental!=null}">
														readonly="readonly"
													</c:if>
												/>
											</td>
											<td class="texto_ti" colspan="2" id="tdContenedor" <c:if test="${loteRearchivoSession.tipo!=null && loteRearchivoSession.tipo == 'Electronico'}">
												style="display:none;"
											</c:if>>
												<input type="hidden" id="bloqueoContenedor" name="bloqueoContenedor" value="true" />
												
												<input type="text" id="codigoContenedor" name="codigoContenedor" 
													style="width: 90px;" class="requerido"
													value="<c:out value="${loteRearchivoSession.contenedor.codigo}" default="" />"
													<c:if test="${accion == 'CONSULTA' || loteRearchivoSession.clasificacionDocumental!=null}">
														readonly="readonly"
													</c:if>
												/>
												
												&nbsp;&nbsp;
												<button type="button" id="buscaContenedor"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${accion == 'CONSULTA' || loteRearchivoSession.clasificacionDocumental!=null}">
														disabled="disabled"
													</c:if>
												>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>
												&nbsp;&nbsp; 
												<label id="codigoContenedorLabel" for="codigoContenedor"> 
													 
												</label>
											</td>
										</tr>
										<tr>
											<td class="texto_ti" colspan="2">
												<spring:message code="formularioLoteRearchivo.rearchivo.tipoIndice" htmlEscape="true"/>
											</td>
											<td class="texto_ti" colspan="2">
												<spring:message code="formularioLoteRearchivo.rearchivo.clasificacionDocumental" htmlEscape="true"/>
											</td>										
										</tr>
										<tr>
											<td class="texto_ti" colspan="2">
												<c:if test="${loteRearchivoSession.clasificacionDocumental==null}">
													<input type="radio" id="indiceIndividual" name="indiceIndividual" value="true" 
														<c:if test="${loteRearchivoSession.indiceIndividual}">
															checked="checked"
														</c:if>
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													/><spring:message code="formularioLoteRearchivo.rearchivo.tipoIndice.individual" htmlEscape="true"/>
													&nbsp;&nbsp;
													<input type="radio" id="indiceGrupal" name="indiceIndividual" value="false" 
														<c:if test="${!loteRearchivoSession.indiceIndividual}">
															checked="checked"
														</c:if>
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													/><spring:message code="formularioLoteRearchivo.rearchivo.tipoIndice.grupal" htmlEscape="true"/>
												</c:if>
												<c:if test="${loteRearchivoSession.clasificacionDocumental!=null}">
													<c:if test="${loteRearchivoSession.indiceIndividual}">
														<spring:message code="formularioLoteRearchivo.rearchivo.tipoIndice.individual" htmlEscape="true"/>
													</c:if>
													<c:if test="${!loteRearchivoSession.indiceIndividual}">
														<spring:message code="formularioLoteRearchivo.rearchivo.tipoIndice.grupal" htmlEscape="true"/>
													</c:if>
												</c:if>
											</td>					
											<td class="texto_ti" colspan="2">
												<input type="hidden" id="bloqueoClasificacionDocumental" name="bloqueoClasificacionDocumental" value="true"/>
												<input type="text" id="codigoClasificacionDocumental" name="codigoClasificacionDocumental" 
													style="width: 80px;" class="requerido"
													value="<c:out value="${loteRearchivoSession.clasificacionDocumental.codigo}" default="" />"
													
													<c:if test="${accion == 'CONSULTA' || loteRearchivoSession.clasificacionDocumental!=null}">
														readonly="readonly"
													</c:if>
													/>
												&nbsp;&nbsp;
												<button type="button" id="buscaClasificacionDocumental"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${accion == 'CONSULTA' || loteRearchivoSession.clasificacionDocumental!=null}">
														disabled="disabled"
													</c:if>
												>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>
												&nbsp;&nbsp; 
												<label id="codigoClasificacionDocumentalLabel" for="codigoClasificacionDocumental"> 
													 <c:out value="${loteRearchivoSession.clasificacionDocumental.nombre}" default="" />
												</label>
											</td>					
										</tr>
										<c:if test="${accion != 'CONSULTA' && loteRearchivoSession.clasificacionDocumental==null}">
											<tr>
												<td colspan="4">
													
												</td>
											</tr>
											<tr>
												<td colspan="4" style="text-align: center;">
													<div id="confirmarDiv" style="display:block">
														<br style="font-size: xx-small;"/>
														<button name="btnConfirmarLote" id="btnConfirmarLote" type="button" class="botonCentrado">
															<img src="<%=request.getContextPath()%>/images/ok.png">
															<spring:message code="botones.confirmar" htmlEscape="true" />
														</button>
													</div>
												</td>
											</tr>
										</c:if>
									</table>
								</fieldset>
							</div>
							<c:if test="${loteRearchivoSession.clasificacionDocumental!=null}">
							<br style="font-size: xx-small;"/>
							<fieldset>
								<legend>
									<font size="1">
										<spring:message code="formularioLoteRearchivo.titulo.rearchivosRegistadas" htmlEscape="true"/>
									</font>
								</legend>
								<div style="overflow: scroll; height: 140px;"> 	
									<display:table name="sessionScope.rearchivos_lote" id="rearchivos_lote" sort="list">
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_id" value="${rearchivos_lote.obj_hash}"/>
										</display:column>		
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_orden" value="${rearchivos_lote.orden}" class="selectableOrden"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_numero1" value="${rearchivos_lote.numero1}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_numero2" value="${rearchivos_lote.numero2}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_fecha1" value="${rearchivos_lote.fecha1Str}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_fecha2" value="${rearchivos_lote.fecha2Str}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_texto1" value="${rearchivos_lote.texto1}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_texto2" value="${rearchivos_lote.texto2}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_descripcion" value="${rearchivos_lote.descripcion}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_estado" value="${rearchivos_lote.estado}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_nombreArchivoDigital" value="${rearchivos_lote.nombreArchivoDigital}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_pathArchivoDigital" value="${rearchivos_lote.pathArchivoDigital}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_cantidadImagenes" value="${rearchivos_lote.cantidadImagenes}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_pathArchivoJPGDigital" value="${rearchivos_lote.pathArchivoJPGDigital}"/>
										</display:column>
										<display:column class="hidden" headerClass="hidden">
											<input type="hidden" id="hdn_codigoClasifDoc" value="${rearchivos_lote.codigoClasifDoc}"/>
										</display:column>
										
										<display:column property="orden" titleKey="formularioLoteRearchivo.listaRef.orden" class="celdaAlineadoCentrado"/>
										<display:column property="numero1" titleKey="formularioLoteRearchivo.listaRef.numero1" class="celdaAlineadoIzquierda"/>
										<display:column property="numero2" titleKey="formularioLoteRearchivo.listaRef.numero2" class="celdaAlineadoIzquierda"/>
										<display:column property="fecha1Str" titleKey="formularioLoteRearchivo.listaRef.fecha1" class="celdaAlineadoIzquierda"/>
										<display:column property="fecha2Str" titleKey="formularioLoteRearchivo.listaRef.fecha2" class="celdaAlineadoIzquierda"/>
										<display:column property="texto1" titleKey="formularioLoteRearchivo.listaRef.texto1" class="celdaAlineadoIzquierda"/>
										<display:column property="texto2" titleKey="formularioLoteRearchivo.listaRef.texto2" class="celdaAlineadoIzquierda"/>
										<display:column property="descripcion" titleKey="formularioLoteRearchivo.rearchivo.descripcion" class="celdaAlineadoIzquierda"/>
										<c:if test="${loteRearchivoSession.tipo!=null && loteRearchivoSession.tipo == 'Digital'}">
											<display:column titleKey="formularioLoteRearchivo.listaRef.nombreArchivoDigital" class="celdaAlineadoIzquierda">
												<c:out value="${rearchivos_lote.nombreArchivoDigital}"></c:out>
												<img src="<%=request.getContextPath()%>/images/skip_down.png" title="<spring:message code="formularioLoteRearchivo.listaRef.descargar" htmlEscape="true"/>"
	 	 												onclick="downloadFile('<c:out value="${rearchivos_lote.pathArchivoDigital}"/>');">
											</display:column>
											<display:column property="cantidadImagenes" titleKey="formularioLoteRearchivo.listaRef.cantidadImagenes" class="celdaAlineadoDerecha"/>
										</c:if>
										<display:column property="estado" titleKey="formularioLoteRearchivo.listaRef.estado" class="celdaAlineadoCentrado"/>
									</display:table>
								</div>
								
							</fieldset>
							<br style="font-size: xx-small;"/>
						</c:if>
							<c:if test="${param.accion != 'CONSULTA' && loteRearchivoSession.clasificacionDocumental!=null}">
								<fieldset>
									<legend>
										<font size="1">
											<spring:message code="formularioLoteRearchivo.titulo.registarRearchivo" htmlEscape="true"/>
										</font>
									</legend>
									
									<div style="background-color: white; WIDTH: auto;" align="center">
										<input type="hidden" id="accion" name="accion" value="${param.accion}">
										<input type="hidden" id="nombreArchivoDigital" name="nombreArchivoDigital" value="<c:out value="${rearchivoSeleccionado.nombreArchivoDigital}" default="" />"/>
										<input type="hidden" id="pathArchivoDigital" name="pathArchivoDigital" value="<c:out value="${rearchivoSeleccionado.pathArchivoDigital}" default="" />"/>
										<input type="hidden" id="cantidadImagenes" name="cantidadImagenes" value="<c:out value="${rearchivoSeleccionado.cantidadImagenes}" default="" />"/>
										<input type="hidden" id="pathArchivoJPGDigital" name="pathArchivoJPGDigital" value="<c:out value="${rearchivoSeleccionado.pathArchivoJPGDigital}" default="" />"/>
										<table class="busqueda" style="width: 100%; background-color: white;">
											
												<tr>
													<td class="texto_ti">
														<spring:message code="formularioLoteRearchivo.listaRef.orden" htmlEscape="true"/>
													</td>
													<td class="texto_ti">
														<spring:message code="formularioLoteRearchivo.rearchivo.estado" htmlEscape="true"/>
													</td>
													
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="orden" name="orden"
															style="width: 90px; font-weight: bold; text-align: right;"
															value="<c:out value="${rearchivoSeleccionado.orden}" default="" />"
															readonly="readonly"
														/>
													</td>
													<td class="texto_ti">
														<select id="estado" 
															name="estado" size="1" style="width: 140px;"
															>
																<option label="Pendiente" value="Pendiente" 
																	<c:if test="${rearchivoSeleccionado.estado == 'Pendiente'}">
																			selected="selected"
																	</c:if>>
																	<spring:message code="formularioLoteRearchivo.rearchivo.estado.pendiente" htmlEscape="true"/>
																</option>
																<option label="Procesado" value="Procesado" 
																	<c:if test="${rearchivoSeleccionado.estado == 'Procesado'}">
																			selected="selected"
																	</c:if>>
																	<spring:message code="formularioLoteRearchivo.rearchivo.estado.procesado" htmlEscape="true"/>
																</option>					
														</select>
													</td>
													
												</tr>
												
												<c:if test="${accion != 'CONSULTA' && accion != 'NUEVO' && loteRearchivoSession.tipo == 'Digital'}">
												<sec:authorize ifAnyGranted="ROLE_MOD_CLASIFDOC_REARCHIVO_DIGITAL"> 
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioLoteRearchivo.rearchivo.clasificacionDocumental" htmlEscape="true"/>
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<input
															type="text" id="codigoClasifDoc"
															name="codigoClasifDoc" style="width: 80px;"
															value="<c:out value="${rearchivoSeleccionado.clasifDoc.codigo}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
															&nbsp;&nbsp;
															<button type="button" id="buscaClasifDoc"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
																		disabled="disabled"
																	</c:if>>
																<img src="<%=request.getContextPath()%>/images/buscar.png">
															</button> &nbsp;&nbsp; 
															<label id="codigoClasifDocLabel"
																for="codigoClasifDoc"> <c:out
																		value="${rearchivoSeleccionado.clasifDoc.nombre}"
																		default="" />
															</label>
														</td>
													</tr>
													</sec:authorize>
												</c:if>
												<tr>
													
													<td class="texto_ti" id="tit_numero1">
														<c:if test="${loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.individualNumero1Titulo}" escapeXml="true"/>
														</c:if>
														<c:if test="${!loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.grupalNumero1Titulo}" escapeXml="true"/>
														</c:if>
													</td>
													<td class="texto_ti" id="tit_numero2">
														<c:if test="${loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.individualNumero2Titulo}" escapeXml="true"/>
														</c:if>
														<c:if test="${!loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.grupalNumero2Titulo}" escapeXml="true"/>
														</c:if>
													</td>
													<td class="texto_ti" id="tit_fecha1">
														<c:if test="${loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.individualFecha1Titulo}" escapeXml="true"/>
														</c:if>
														<c:if test="${!loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.grupalFecha1Titulo}" escapeXml="true"/>
														</c:if>
													</td>
													<td class="texto_ti" id="tit_fecha2">
														<c:if test="${loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.individualFecha2Titulo}" escapeXml="true"/>
														</c:if>
														<c:if test="${!loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.grupalFecha2Titulo}" escapeXml="true"/>
														</c:if>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualNumero1Seleccionado
																   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalNumeroSeleccionado}">
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
															<input type="text" id="numero1Str" name="numero1Str" style="width: 80px;" 
																<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualNumero1Requerido
																  		   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalNumeroRequerido}">
																	class="requerido"
																</c:if>
																value="<c:out value="${rearchivoSeleccionado.numero1}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															/>
														</c:if>
													</td>
													<td class="texto_ti">
														<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualNumero2Seleccionado
																   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalNumeroSeleccionado}">
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
															<input type="text" id="numero2Str" name="numero2Str" style="width: 80px;" 
																<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualNumero2Requerido
																  		   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalNumeroRequerido}">
																	class="requerido"
																</c:if>
																value="<c:out value="${rearchivoSeleccionado.numero2}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															/>
														</c:if>
													</td>
													<td class="texto_ti">
														<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualFecha1Seleccionado
																   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalFechaSeleccionado}">
															
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
															<input type="text" id="fecha1" name="fecha1" style="width: 80px;" 
																<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualFecha1Requerido
																  		   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalFechaRequerido}">
																	class="requerido"
																</c:if>
																value="<c:out value="${rearchivoSeleccionado.fecha1Str}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															/>
															<c:if test="${accion != 'CONSULTA'}">
																<script type="text/javascript" >
																	new tcal ({
																		// form name
																		'formname': 'rearchivoFormulario',
																		// input name
																		'controlname': 'fecha1'
																	});
																</script>
															</c:if>
														</c:if>
													</td>
													<td class="texto_ti">
														<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualFecha2Seleccionado
																   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalFechaSeleccionado}">
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
															<input type="text" id="fecha2" name="fecha2" style="width: 80px;" 
																<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualFecha2Requerido
																  		   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalFechaRequerido}">
																	class="requerido"
																</c:if>
																value="<c:out value="${rearchivoSeleccionado.fecha2Str}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															/>
															<c:if test="${accion != 'CONSULTA'}">
																<script type="text/javascript" >
																	new tcal ({
																		// form name
																		'formname': 'rearchivoFormulario',
																		// input name
																		'controlname': 'fecha2'
																	});
																</script>
															</c:if>
														</c:if>
													</td>
												</tr>
												<tr>
													<td class="texto_ti" id="tit_texto1" colspan="2">
														<c:if test="${loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.individualTexto1Titulo}" escapeXml="true"/>
														</c:if>
														<c:if test="${!loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.grupalTexto1Titulo}" escapeXml="true"/>
														</c:if>
													</td>
													<td class="texto_ti" id="tit_texto2" colspan="2">
														<c:if test="${loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.individualTexto2Titulo}" escapeXml="true"/>
														</c:if>
														<c:if test="${!loteRearchivoSession.indiceIndividual}">
															<c:out value="${loteRearchivoSession.clasificacionDocumental.grupalTexto2Titulo}" escapeXml="true"/>
														</c:if>
													</td>
												</tr>
												<tr>
													<td class="texto_ti" id="tit_texto1" colspan="2">
														<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualTexto1Seleccionado
																   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalTextoSeleccionado}">
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
															<input type="text" id="texto1" name="texto1" style="width: 80px;" 
																<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualTexto1Requerido
																  		   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalTextoRequerido}">
																	class="requerido"
																</c:if>
																value="<c:out value="${rearchivoSeleccionado.texto1}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															/>
														</c:if>
													</td>
													<td class="texto_ti" id="tit_texto2" colspan="2">
														<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualTexto2Seleccionado
																   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalTextoSeleccionado}">
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
															<input type="text" id="texto2" name="texto2" style="width: 80px;"
																<c:if test="${loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.individualTexto2Requerido
																  		   || !loteRearchivoSession.indiceIndividual && loteRearchivoSession.clasificacionDocumental.grupalTextoRequerido}">
																	class="requerido"
																</c:if>
																value="<c:out value="${rearchivoSeleccionado.texto2}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															/>
														</c:if>
													</td>
												</tr>
												<c:if test="${loteRearchivoSession.clasificacionDocumental.descripcionSeleccionado}">
													<tr>
														<td class="texto_ti" colspan="4">
															<spring:message code="formularioLoteRearchivo.rearchivo.descripcion" htmlEscape="true"/>
														</td>
													</tr>
													<tr>
														<td class="texto_ti" colspan="4">
															<textarea id="descripcion" name="descripcion" cols="90" rows="3"
																<c:if test="${loteRearchivoSession.clasificacionDocumental.descripcionRequerido}">
																	class="requerido"
																</c:if>
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>${rearchivoSeleccionado.descripcion}</textarea>
														</td>
													</tr>
												</c:if>
											
										</table>
									<br style="font-size: xx-small;"/>
									<sec:authorize ifAnyGranted="ROLE_REFERENCIA_ASIGNAR_TAREA">
									<c:if test="${loteRearchivoSession.indiceIndividual}">
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
											value="<c:out value="${rearchivoSeleccionado.codigoUsuario}" default="" />"
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
													value='<c:out value="${rearchivoSeleccionado.descripcionTarea}" default=""/>'
													<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if> />
												</td>
											</tr></table></fieldset></c:if></sec:authorize>
								</div>	
								<br style="font-size: xx-small;"/>
									<div align="center">
										<button name="guardar" type="button" class="botonCentrado" id="btnGuardar">
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
										<spring:message code="formularioLoteRearchivo.textos.crtlS" htmlEscape="true" />
									</div>
								</fieldset>
								
							</c:if>
							<c:if test="${loteRearchivoSession.clasificacionDocumental!=null}">
							
							
								<div id="imageDiv" >
									<fieldset>
									
									<c:if test="${(fn:endsWith(rearchivoSeleccionado.nombreArchivoDigital,'.TIF')) || (fn:endsWith(rearchivoSeleccionado.nombreArchivoDigital,'.tif')) || (fn:endsWith(rearchivoSeleccionado.nombreArchivoDigital,'.tiff'))|| (fn:endsWith(rearchivoSeleccionado.nombreArchivoDigital,'.jpg'))}">												
										<table class="busqueda" style="width: 100%; background-color: white;">
											<tr>
												<td class="texto_ti">
													<spring:message code="formularioLoteRearchivo.rearchivo.pagina" htmlEscape="true"/>: &nbsp;
													<select id="selectCantidadJPG" 
														name="selectCantidadJPG" size="1" style="width: 140px;"
														>
													</select>
												</td>
												<td>
													<input type="hidden" id="zoom" name="zoom" value='<c:out value="${zoom}" default="70" />'
													/>
													<input type="hidden" id="scrollY" name="scrollY" value='<c:out value="${scrollY}" default="" />'
													/>
													<input type="hidden" id="scrollX" name="scrollX" value='<c:out value="${scrollX}" default="" />'
													/>
												</td>
												<td class="texto_ti" style="vertical-align: middle;">
													<spring:message code="formularioLoteRearchivo.rearchivo.zoom" htmlEscape="true"/>: &nbsp;
													<button name="btnZoomOut" id="btnZoomOut" type="button" onclick="zoomOut();"
													title="<spring:message code="formularioLoteRearchivo.rearchivo.zoom.out" htmlEscape="true" />">
														<img src="<%=request.getContextPath()%>/images/zoom-out.png">
													</button>
													<input type="text" id="zoomTxt" name="zoomTxt" value="70 %" readonly="readonly" style="width: 40px;"/>
													<button name="btnZoomIn" id="btnZoomIn" type="button" onclick="zoomIn();" 
														title="<spring:message code="formularioLoteRearchivo.rearchivo.zoom.in" htmlEscape="true" />">
														<img src="<%=request.getContextPath()%>/images/zoom-in.png">
													</button>
												</td>
											</tr>
											<tr>											
												<td class="texto_ti" colspan="6">
													<fieldset>
														<div style="overflow: auto; height:auto; text-align: center;" id="divImagen"> 	
															<div style="background-color: white; height: auto; width: auto;" align="center">

																<img src="viewFileJPGLoteRearchivo.html?fileName=${rearchivoSeleccionado.pathArchivoJPGDigital}1"
																	id="jpgDigital" name="jpgDigital" style="display:block; height: 70%; width: 70%;">

															</div>
														</div>
													</fieldset>
												</td>
											</tr>
										</table>
										</c:if>
										
										<c:if test="${(fn:endsWith(rearchivoSeleccionado.nombreArchivoDigital,'.pdf')) || (fn:endsWith(rearchivoSeleccionado.nombreArchivoDigital,'.PDF'))}">	
											<iframe src="../FlexPaper/common/simple_document.jsp?doc=${rearchivoSeleccionado.nombreArchivoDigital}&ruta=${rearchivoSeleccionado.pathArchivoDigital}"
											id="jpgDigital" name="jpgDigital" style="display:block; height: 700px; width: 100%; border: none;"></iframe>
										</c:if>
										
									</fieldset>
								</div>
							</c:if>
						</form:form>	
					
					
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