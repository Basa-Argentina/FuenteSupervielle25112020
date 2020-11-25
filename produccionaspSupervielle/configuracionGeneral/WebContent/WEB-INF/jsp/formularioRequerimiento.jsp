<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioRequerimiento.titulo.registar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioRequerimiento.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioRequerimiento.titulo.consultar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript"
			src="js/mavalos_formulario_requerimiento.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/Utils.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
    </head>		
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<c:if test="${accion == 'NUEVO'}">
			<div class="contextMenu" id="myMenu1">
				<ul>
					<li id="eliminar"><img src="images/eliminar.png" /> <font
						size="2"><spring:message code="botones.eliminar"
								htmlEscape="true" />
					</font>
					</li>
				</ul>
			</div>
		</c:if>
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioRequerimiento.titulo.registar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioRequerimiento.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioRequerimiento.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarRequerimiento.html" modelAttribute="requerimientoFormulario" method="post">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>												
						<input type="hidden" id="id" name="id" value="<c:out value="${requerimientoFormulario.id}" default="" />"/>
						<input type="hidden" id="clienteId" value="<c:out value="${clienteId}" default="" />"/>
						<input type="hidden" id="bandera" value="<c:out value="${bandera}" default="" />"/>
						
						<input type="hidden" id="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />"/>
						<input type="hidden" name="buscarElemento" id="buscarElemento" value=false></input>
						<input type="hidden" name="buscarElementoSinReferencia" id="buscarElementoSinReferencia" value=false></input>		
						<input type="hidden" name="eliminarElemento" id="eliminarElemento" value=false></input>
						<input type="hidden" name="eliminarElementoId" id="eliminarElementoId"></input>
						<input type="hidden" name="cambioDireccionDefecto" id="cambioDireccionDefecto" value="false"></input>
						<input type="hidden" name="idDireccionDefectoAnterior" id="idDireccionDefectoAnterior" 
							value="<c:out value="${requerimientoFormulario.empleadoSolicitante.direccionDefecto.codigo}" default="" />"></input>
						<input type="hidden" name="cambioDireccionDefectoMensaje" id="cambioDireccionDefectoMensaje" 
							value="<spring:message code="formularioRequerimiento.mensaje.cambiarDireccion" htmlEscape="true"/>"></input>
						<input type="hidden" id="mensajeSeleccioneCliente" name="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioRequerimiento.datos" htmlEscape="true"/> 
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="width: 100%;" id="busquedaDiv" align="center">
								<table>
									<tr>	
										<td class="tdNombre">
											<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
												<spring:message code="empresa" htmlEscape="true"/>
											</sec:authorize>
											&nbsp;
										</td>
										<td class="tdDato">
											<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
												<c:out value="${empresa}"/>
											</sec:authorize>
											&nbsp;
										</td>
										<td class="texto_ti" colspan="2">
											
												<spring:message code="formularioRequerimiento.datos.tipoRequerimiento" htmlEscape="true"/>
											
										</td>
										<td class="texto_ti" colspan="2">
											
												<spring:message code="formularioRequerimiento.datos.serie" htmlEscape="true"/>
											
										</td>
										
									</tr>
									<tr>	
										<td class="tdNombre">
											<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
												<spring:message code="sucursal" htmlEscape="true"/>
											</sec:authorize>
											&nbsp;
										</td>
										<td class="tdDato">
											<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
												<c:out value="${sucursal}"/>
											</sec:authorize> 
											&nbsp;
										</td>
										<td class="texto_ti"  style="width: 250px;" colspan="2">														
											<input type="text" id="tipoRequerimientoCod" name="tipoRequerimientoCod" maxlength="6" style="width: 50px;"
												value="<c:out value="${requerimientoFormulario.tipoRequerimiento.codigo}" default="" />" class="requerido upperCase"
												<c:if test="${accion != 'NUEVO'}">
													readonly="readonly"
												</c:if>
											/>
											<c:if test="${accion != 'MODIFICACION'}">
												&nbsp;&nbsp;
												<button type="button"
													id="buscaTipoRequerimiento" name = "buscaTipoRequerimiento"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
													
												>
												<img src="<%=request.getContextPath()%>/images/buscar.png" > 
												</button>&nbsp;&nbsp; </c:if>
												<label id="tipoRequerimientoCodLabel" for="tipoRequerimientoCod" style="width: 150px;">
													<c:out value="${requerimientoFormulario.tipoRequerimiento.descripcion}" default="" />
												</label>
											
										</td>
										<td class="texto_ti" style="width: 250px;" colspan="2">
											<input type="text" id="codigoSerie" name="codigoSerie" maxlength="30" style="width: 50px;" class="requerido upperCase"
														value="<c:out value="${requerimientoFormulario.serie.codigo}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>
												/>
												<c:if test="${accion != 'MODIFICACION'}">
													&nbsp;&nbsp;
													<button type="button" 
														name="buscaSerie" id="buscaSerie"
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													>
													<img src="<%=request.getContextPath()%>/images/buscar.png" > 
													</button>&nbsp;&nbsp;</c:if>
													
													<label id="codigoSerieLabel" "for="codigoSerie" >
															<c:out value="${requerimientoFormulario.serie.prefijo}" default="" />			
													</label>-
													<input type="text" maxlength="8"
															id="numeroStr" name="numeroStr" class="completarZeros"
															style="width: 70px;" readonly="readonly"
															value="<c:out value="${requerimientoFormulario.numeroStr}" default="" />" 
															/>
												
										</td>
										
									</tr>
									<tr>
										<td colspan="2">
										</td>
										<td class="texto_ti" colspan="4">
											
												<spring:message code="formularioRequerimiento.datos.deposito" htmlEscape="true"/>
											
										</td>
									</tr>
									<tr>
										<td colspan="2">
										</td>
										<td class="texto_ti"  style="width: 250px;" colspan="4">
											<input id="codigoDeposito" type="text" name="codigoDeposito" maxlength="6" style="width: 50px;"
											value="<c:out value="${requerimientoFormulario.codigoDeposito}" default="" />"
											<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
											<c:if test="${accion != 'MODIFICACION'}">
												&nbsp;&nbsp;
												<button type="button"
													name="buscaDeposito" id="buscaDeposito"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>&nbsp;&nbsp; </c:if><label id="codigoDepositoLabel"
												for="codigoDeposito"> <c:out
														value="${requerimientoFormulario.depositoDefecto.descripcion}"
														default="" /> </label>
											
										</td>
									</tr>
									<tr>
										<td colspan="2">
										</td>
										<td class="texto_ti">
											
												<spring:message code="formularioRequerimiento.datos.fechaAlta" htmlEscape="true"/>
											
										</td>
										<td class="texto_ti">
											
												<spring:message code="formularioRequerimiento.datos.horaAlta" htmlEscape="true"/>
											
										</td>
										<td class="texto_ti">
											<spring:message code="formularioRequerimiento.datos.fechaEntrega" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioRequerimiento.datos.horaEntrega" htmlEscape="true"/>
										</td>
									</tr>
									<tr>
										<td colspan="2">
										</td>
										<td class="texto_ti"><input type="text" id="fechaAlta"
											name="fechaAlta" maxlength="10" class="requerido" style="width: 80px;"
											value="<c:out value="${requerimientoFormulario.fechaAltaStr}" default="" />"
											<c:if test="${accion != 'NUEVO'}">
													readonly="readonly"
												</c:if> /> 
												<c:if test="${accion != 'CONSULTA' && accion != 'MODIFICACION'}">
												<script type="text/javascript">
													new tcal ({
														// form name
														'formname': 'requerimientoFormulario',
														// input name
														'controlname': 'fechaAlta'
													});
												</script>
											</c:if>
											
										</td>		
										<td class="texto_ti" >
											<input type="text" class="requerido" maxlength="5" id="horaAlta" name="horaAlta"
												style="width: 60px;" onblur="CheckTime(this)"
												value="<c:out value="${requerimientoFormulario.horaAlta}" default="" />" 
												<c:if test="${accion != 'NUEVO'}">
													readonly="readonly"
												</c:if>
												/>
										</td>				
										<td class="texto_ti" ><input type="text" id="fechaEntrega"
											name="fechaEntrega" maxlength="10" class="requerido" style="width: 80px;"
											value="<c:out value="${requerimientoFormulario.fechaEntregaStr}" default="" />"
											<c:if test="${accion == 'CONSULTA' || 
												(requerimientoFormulario.estado!=null && requerimientoFormulario.estado != 'Pendiente')}">
													readonly="readonly"
												</c:if> /> 
												<c:if test="${accion != 'CONSULTA' && (requerimientoFormulario.estado == null || requerimientoFormulario.estado == 'Pendiente')}">
												<script type="text/javascript">
													new tcal ({
														// form name
														'formname': 'requerimientoFormulario',
														// input name
														'controlname': 'fechaEntrega'
													});
												</script>
											</c:if>
										</td>
										<td class="texto_ti" >
											<input type="text"
												class="requerido" maxlength="5" id="horaEntrega" name="horaEntrega"
												style="width: 60px;" onblur="CheckTime(this)"
												value="<c:out value="${requerimientoFormulario.horaEntrega}" default="" />" 
												<c:if test="${accion == 'CONSULTA' || 
												(requerimientoFormulario.estado!=null && requerimientoFormulario.estado != 'Pendiente')}">
													readonly="readonly"
												</c:if>
												/>
										</td>		
									</tr>
									<tr>
										<td style="text-align: left;" colspan="8">											
											<table style="width: 100%;">													
												
												<tr>
													<td colspan="2" style="width: 100%;">
														<fieldset>
														<legend>
															<spring:message code="formularioRequerimiento.datos.datosCliente" htmlEscape="true"/>
														</legend>
															<table>	
																											
																<tr>
																	<td class="texto_ti" >
																		
																			<spring:message code="formularioRequerimiento.datos.cliente" htmlEscape="true"/>
																		
																	</td>
																	<td class="texto_ti" >
																		<spring:message code="formularioRequerimiento.datos.direccionEntrega" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti" style="width: 350px;">
																		<input 
																			<c:if test="${accion != 'MODIFICACION' && (listaConElementos == null || listaConElementos == 'NO')}">
																				type="text" 
																			</c:if>
																			<c:if test="${accion == 'MODIFICACION' || listaConElementos == 'SI'}">
																				type="hidden"
																			</c:if>
																			id="clienteCodigo"
																			name="clienteCodigo" maxlength="6" style="width: 50px;" class="requerido"
																			value="<c:out value="${requerimientoFormulario.clienteEmp.codigo}" default="" />"
																			<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
																						</c:if> />
																		<c:if test="${accion != 'MODIFICACION'}">
																			<c:if test="${listaConElementos == null || listaConElementos == 'NO'}">
																				&nbsp;&nbsp;
																				<button type="button"
																					name="buscaCliente" id="buscaCliente"
																					title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																					<c:if test="${accion == 'CONSULTA'}">
																							disabled="disabled"
																						</c:if>>
																					<img src="<%=request.getContextPath()%>/images/buscar.png">
																				</button>&nbsp;&nbsp; 
																			</c:if>
																			</c:if>
																			<label id="clienteCodigoLabel"	for="clienteCodigo"> 
																				<c:out value="${requerimientoFormulario.clienteEmp.nombreYApellido}" default="" /> 
																			</label>
																		
																	</td>
																	<td class="texto_ti" style="width: 500px;">
																		<input type="text" id="codigoDireccion" class="requerido"
																			name="codigoDireccion" maxlength="6" style="width: 50px;"
																			value="<c:out value="${requerimientoFormulario.direccionDefecto.codigo}" default="" />"
																			<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
																						</c:if> />
																			&nbsp;&nbsp;
																			<button type="button"
																				name="buscaDireccion" id="buscaDireccion"
																				title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																				<c:if test="${accion == 'CONSULTA'}">
																						disabled="disabled"
																					</c:if>>
																				<img src="<%=request.getContextPath()%>/images/buscar.png">
																			</button>&nbsp;&nbsp; <label id="codigoDireccionLabel" style="width: 100%;"
																			for="codigoDireccion"> <c:out
																					value="${requerimientoFormulario.direccionDefecto.descripcion}"
																					default="" />
																		</label>
																	</td>		
																</tr>
																<tr>
																	<td class="texto_ti" >
																		
																			<spring:message code="formularioRequerimiento.datos.empleadoSolicitante" htmlEscape="true"/>
																		
																	</td>
																	<td class="texto_ti" >
																		
																			<spring:message code="formularioRequerimiento.datos.empleadoAutorizante" htmlEscape="true"/>
																		
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti" style="width: 250px;">
																		<input type="text" id="codigoPersonal" class="requerido"
																			name="codigoPersonal" maxlength="6" style="width: 50px;"
																			value="<c:out value="${requerimientoFormulario.codigoPersonal}" default="" />"
																			<c:if test="${accion != 'NUEVO'}">
																					readonly="readonly"
																				</c:if>
																			 />
																			 <c:if test="${accion != 'MODIFICACION'}">
																				&nbsp;&nbsp;
																				<button type="button"
																					name="buscaPersonal" id="buscaPersonal"
																					title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																					<c:if test="${accion == 'CONSULTA'}">
																						disabled="disabled"
																					</c:if>>
																					<img src="<%=request.getContextPath()%>/images/buscar.png">
																				</button>&nbsp;&nbsp; </c:if>
																				
																				<label id="codigoPersonalLabel"
																				for="codigoPersonal"> <c:out
																						value="${requerimientoFormulario.empleadoSolicitante.nombreYApellido}"
																						default="" /> </label>
																			
																	</td>
																	<td class="texto_ti" style="width: 500px;">
																		<input type="text" id="codigoPersonalAutorizante" class="requerido"
																			name="codigoPersonalAutorizante" maxlength="6" style="width: 50px;"
																			value="<c:out value="${requerimientoFormulario.codigoPersonalAutorizante}" default="" />"
																			<c:if test="${accion != 'NUEVO'}">
																					readonly="readonly"
																				</c:if>
																			 />
																			<c:if test="${accion != 'MODIFICACION'}">
																				&nbsp;&nbsp;
																				<button type="button"
																					name="buscaPersonalAutorizante" id="buscaPersonalAutorizante"
																					title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																					<c:if test="${accion != 'NUEVO'}">
																						disabled="disabled"
																					</c:if>>
																					<img src="<%=request.getContextPath()%>/images/buscar.png">
																				</button>&nbsp;&nbsp;</c:if> <label id="codigoPersonalAutorizanteLabel"
																				for="codigoPersonalAutorizante"> <c:out
																						value="${requerimientoFormulario.empleadoAutorizante.nombreYApellido}"
																						default="" /> </label>
																			
																	</td>
																</tr>
																
																
															</table>
														</fieldset>
														
															<td colspan="5" style="width: 150%;">
															
															<table>												
																<tr>
																	<td>
																		<spring:message code="formularioRequerimiento.datos.observaciones" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td>
																		<textarea id="observaciones" name="observaciones" rows="4" cols="50" 
																		<c:if test="${accion == 'CONSULTA'}">readonly="readonly"</c:if>>
																			<c:out value="${requerimientoFormulario.observaciones}" />
																		</textarea>
																	</td>
																</tr>
																</table>
																
																
													</td>
												</tr>							
												<tr>
													<td colspan="6" style="width: 100%;">
														<fieldset>
															<display:table id="requerimientoElemento" name="requerimientoFormulario.listaElementos" requestURI="mostrarRequerimientoElemento.html">
																<display:column class="hidden" headerClass="hidden">
																	<input type="hidden" id="hdn_id" value="${requerimientoElemento.referencia.id}" />
																</display:column>
																<display:column class="hidden" headerClass="hidden">
																	<input type="hidden" id="hdn_eliminar"
																		value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
																</display:column>
																<display:column property="elemento.tipoElemento.descripcion" titleKey="formularioRequerimiento.datos.display.tipoElemento" class="celdaAlineadoIzquierda"/>
																<display:column property="elemento.codigo" titleKey="formularioRequerimiento.datos.display.codigoElemento" class="celdaAlineadoIzquierda"/>
																<display:column property="referencia.descripcion" titleKey="formularioRequerimiento.datos.display.descripcion" class="celdaAlineadoIzquierda"/>
																<c:if test="${requerimientoElemento.elemento.contenedor == null}">
																	<display:column class="celdaAlineadoCentrado" titleKey="formularioElemento.deposito">
																		<c:out value="${requerimientoElemento.elemento.depositoActual.descripcion}"/>
																		<c:if test="${requerimientoElemento.elemento.depositoActual == null}">
																			<img id="information"
																				src="<%=request.getContextPath()%>/images/warning.png"
																				title="<spring:message code="formularioRequerimiento.error.sinDeposito" htmlEscape="true"/>">
																		</c:if>
																	</display:column>
																  	<display:column property="elemento.posicion.estante.grupo.seccion.descripcion"   titleKey="formularioElemento.seccion" class="celdaAlineadoIzquierda"/>
																  	<display:column titleKey="formularioElemento.datosElemento.posicion" class="celdaAlineadoIzquierda">
																  	<c:if test="${requerimientoElemento.elemento.posicion.posVertical != null}">
																		<c:out value="E:${requerimientoElemento.elemento.posicion.estante.codigo} (${requerimientoElemento.elemento.posicion.posVertical};${requerimientoElemento.elemento.posicion.posHorizontal})"/>
																	</c:if> 
												           		   	</display:column>
											           		   	</c:if>
											           		   	<c:if test="${requerimientoElemento.elemento.contenedor != null}">
																	<display:column class="celdaAlineadoCentrado" titleKey="formularioElemento.deposito">
																		<c:out value="${requerimientoElemento.elemento.contenedor.depositoActual.descripcion}"/>
																		<c:if test="${requerimientoElemento.elemento.contenedor.depositoActual == null}">
																			<img id="information"
																				src="<%=request.getContextPath()%>/images/warning.png"
																				title="<spring:message code="formularioRequerimiento.error.sinDeposito" htmlEscape="true"/>">
																		</c:if>
																	</display:column>
																  	<display:column property="elemento.contenedor.posicion.estante.grupo.seccion.descripcion"   titleKey="formularioElemento.seccion" class="celdaAlineadoIzquierda"/>
																  	<display:column titleKey="formularioElemento.datosElemento.posicion" class="celdaAlineadoIzquierda">
																  	<c:if test="${requerimientoElemento.elemento.contenedor.posicion.posVertical != null}">
																		<c:out value="E:${requerimientoElemento.elemento.contenedor.posicion.estante.codigo} (${requerimientoElemento.elemento.contenedor.posicion.posVertical};${requerimientoElemento.elemento.contenedor.posicion.posHorizontal})"/>
																	</c:if> 
												           		   	</display:column>
											           		   	</c:if>
											           		   	<display:column property="elemento.estado" titleKey="formularioRequerimiento.datos.display.estado" class="celdaAlineadoIzquierda"/>					  							  	
																<display:column property="busqueda" titleKey="formularioRequerimiento.datos.display.busqueda" class="celdaAlineadoIzquierda"/>
																<c:if test="${accion == 'NUEVO'}">
																	<display:column class="celdaAlineadoCentrado">
																		<img id="information"
																			src="<%=request.getContextPath()%>/images/information.png"
																			title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
																	</display:column>
																</c:if>
															</display:table>
														</fieldset>
													</td>
												</tr>							
											</table>											
										</td>																																				
									</tr>								
									
						    	</table>
							</div>
							<c:if test="${accion == 'NUEVO'}">
								<div style="width: 100%" align="right">
									<table>
										<tr>
											<td>
												<button id="buscarElementos" name="buscarElementos" visility = hiden onclick="buscarElementosSinReferencia('<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>')"
													type="button">
													<img src="<%=request.getContextPath()%>/images/buscar.png">
													<spring:message code="formularioRequerimiento.buscarElementos" htmlEscape="true" />
												</button>
											</td>
											<td>
											<!–Luis –>
												<button id="buscarElementosReferencias" name="buscarElementosReferencias" visility = hiden onclick="buscarElementosReferencia('<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>')"
													type="button">
													<img src="<%=request.getContextPath()%>/images/buscar.png">
													<spring:message code="formularioRequerimiento.buscarPorReferencia" htmlEscape="true" />
												</button>
											</td>
										</tr>
									</table>
								</div>
							</c:if>		
					    </fieldset>
					</form:form>
				</fieldset>
				<br style="font-size: xx-small;"/>
			    <c:if test="${accion != 'CONSULTA'}">
			    	<div align="center">
						<button name="guardar" type="button" onclick="guardarYSalir();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.guardar" htmlEscape="true"/>  
						</button>
						&nbsp;
						<button name="cancelar" type="button"  onclick="volverCancelar();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
							<spring:message code="botones.cancelar" htmlEscape="true"/>  
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
				<br style="font-size: xx-small;"/>
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>
		<jsp:include page="fieldErrors.jsp" />
		<jsp:include page="fieldAvisos.jsp" />
		
		<div class="selectorDiv"></div>
	</body>
</html>