<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioTipoOperacion.titulo.registrar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioTipoOperacion.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioTipoOperacion.titulo.consultar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>		
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_tipo_operacion.js"></script>
    </head>		
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioTipoOperacion.titulo.registrar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioTipoOperacion.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioTipoOperacion.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarTipoOperacion.html" modelAttribute="tipoFormulario" method="post">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>												
						<input type="hidden" id="id" name="id" value="<c:out value="${tipoFormulario.id}" default="" />"/>
						<input type="hidden" id="clienteId" value="<c:out value="${clienteId}" default="" />"/>							
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioTipoOperacion.datos" htmlEscape="true"/> 
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
										<td style="text-align: left;" colspan="2">											
											<table>													
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioTipoOperacion.datos.codigo" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="codigo" name="codigo" maxlength="6" 
															style="width: 70px;" class="requerido upperCase alphaNumeric"
															value="<c:out value="${tipoFormulario.codigo}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>															
													</td>
												</tr>
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioTipoOperacion.datos.descripcion" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="descripcion" name="descripcion" style="width: 370px;" 
															maxlength="60" class="requerido"
															value="<c:out value="${tipoFormulario.descripcion}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
													</td>										
												</tr>													
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioTipoOperacion.datos.tipoRequerimiento" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">														
														<input type="text" id="tipoRequerimientoCod" name="tipoRequerimientoCod" maxlength="6" style="width: 50px;"
															value="<c:out value="${tipoFormulario.tipoRequerimiento.codigo}" default="" />" class="upperCase"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopup('tipoRequerimientoPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
														<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>&nbsp;&nbsp; 
														<label id="tipoRequerimientoCodLabel" for="tipoRequerimientoCod">
															<c:out value="${tipoFormulario.tipoRequerimiento.descripcion}" default="" />
														</label>
													</td>										
												</tr>
												<tr>
													<td class="texto_ti">			
														<input type="checkbox" id="desagregaPorDeposito" name="desagregaPorDeposito"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${tipoFormulario.desagregaPorDeposito == true}">
																checked = "CHECKED"
															</c:if>
														/>
														<spring:message code="formularioTipoOperacion.datos.desagregaPorDeposito" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">			
														<input type="checkbox" id="envio" name="envio"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${tipoFormulario.envio == true}">
																checked = "CHECKED"
															</c:if>
														/>
														<spring:message code="formularioTipoOperacion.datos.envio" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">			
														<input type="checkbox" id="generaMovimiento" name="generaMovimiento"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${tipoFormulario.generaMovimiento == true}">
																checked = "CHECKED"
															</c:if>
														/>
														<spring:message code="formularioTipoOperacion.datos.generaMovimiento" htmlEscape="true"/>
													</td>
												</tr>		
												<tr>
													<td class="texto_ti">			
														<input type="checkbox" id="generaOperacionAlCerrarse" name="generaOperacionAlCerrarse"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${tipoFormulario.generaOperacionAlCerrarse == true}">
																checked = "CHECKED"
															</c:if>
														/>
														<spring:message code="formularioTipoOperacion.datos.generaOperacionAlCerrarse" htmlEscape="true"/>
													</td>
												</tr>												
												<tr>	
													<td class="texto_ti">					
														<div class="generaOperacionAlCerrarse" 
															<c:if test="${tipoFormulario.generaOperacionAlCerrarse != true}">
																style="display: none;"
															</c:if>
														>
															<spring:message code="formularioTipoOperacion.datos.tipoOperacionSiguiente" htmlEscape="true"/><br>									
															<input type="text" id="tipoOperacionSiguienteCod" name="tipoOperacionSiguienteCod" maxlength="6" style="width: 50px;"
																value="<c:out value="${tipoFormulario.tipoOperacionSiguiente.codigo}" default="" />" class="requerido upperCase"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
															<button type="button" onclick="abrirPopup('tipoOperacionPopupMap');"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
															</button>&nbsp;&nbsp; 
															<label id="tipoOperacionSiguienteCodLabel" for="tipoOperacionSiguienteCod">
																<c:out value="${tipoFormulario.tipoOperacionSiguiente.descripcion}" default="" />
															</label>
														</div>
													</td>										
												</tr>												
												<tr>
													<td class="texto_ti">			
														<input type="checkbox" id="imprimeRemito" name="imprimeRemito"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${tipoFormulario.imprimeRemito == true}">
																checked = "CHECKED"
															</c:if>
														/>
														<spring:message code="formularioTipoOperacion.datos.imprimeRemito" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<div class="imprimeRemito"
															<c:if test="${tipoFormulario.imprimeRemito != true}">
																style="display: none;"
															</c:if>>
															<spring:message code="formularioTipoOperacion.datos.serieRemito" htmlEscape="true"/><br>
															<input type="text" id="serieRemitoCod" name="serieRemitoCod" maxlength="6" 
																style="width: 50px;" class="requerido upperCase"
																<c:if test="${tipoFormulario.imprimeRemito == true}">
																	value="<c:out value="${tipoFormulario.serieRemito.codigo}" default="" />"
																</c:if>																	 
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
															<button type="button" onclick="abrirPopup('serieRemitoPopupMap');"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
															</button>&nbsp;&nbsp; 
															<label id="serieRemitoCodLabel" for="serieRemitoCod">
																<c:if test="${tipoFormulario.imprimeRemito == true}">
																	<c:out value="${tipoFormulario.serieRemito.descripcion}" default="" />
																</c:if>																
															</label>
														</div>															
													</td>
												</tr>
												<tr>
													<td class="texto_ti">			
														<input type="checkbox" id="imprimeDocumento" name="imprimeDocumento"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${tipoFormulario.imprimeDocumento == true}">
																checked = "CHECKED"
															</c:if>
														/>
														<spring:message code="formularioTipoOperacion.datos.imprimeDocumento" htmlEscape="true"/>
													</td>
												</tr>	
																								
												<tr>
													<td class="texto_ti">
														<div class="imprimeDocumento"
															<c:if test="${tipoFormulario.imprimeDocumento != true}">
																style="display: none;"
															</c:if>
														>
															<spring:message code="formularioTipoOperacion.datos.tituloDocumento" htmlEscape="true"/><br>
															<input type="text" id="tituloDocumento" name="tituloDocumento" maxlength="30" 
																style="width: 200px;" class="requerido"
																<c:if test="${tipoFormulario.imprimeDocumento == true}">
																	value="<c:out value="${tipoFormulario.tituloDocumento}" default="" />"
																</c:if>																
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/><br>
															<div style="display: none">  
															<spring:message code="formularioTipoOperacion.datos.serieDocumento" htmlEscape="true"/><br>
															<input type="text" id="serieDocumentoCod" name="serieDocumentoCod" maxlength="6" 
																style="width: 50px;" class="requerido upperCase"
																<c:if test="${tipoFormulario.imprimeDocumento == true}">
																	value="<c:out value="${tipoFormulario.serieDocumento.codigo}" default="" />"
																</c:if>																	 
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
															<button type="button" onclick="abrirPopup('seriePopupMap');"
																title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
															</button>&nbsp;&nbsp; 
															<label id="serieDocumentoCodLabel" for="serieDocumentoCod">
																<c:if test="${tipoFormulario.imprimeDocumento == true}">
																	<c:out value="${tipoFormulario.serieDocumento.descripcion}" default="" />
																</c:if>																
															</label>
															</div>
														</div>															
													</td>
												</tr>														
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioTipoOperacion.datos.conceptoFacturable" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">														
														<input type="text" id="conceptoFacturableCod" name="conceptoFacturableCod" maxlength="6" style="width: 50px;"
															value="<c:out value="${tipoFormulario.conceptoFacturable.codigo}" default="" />" class="upperCase"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopup('conceptosPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
														<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>&nbsp;&nbsp; 
														<label id="conceptoFacturableCodLabel" for="conceptoFacturableCod">
															<c:out value="${tipoFormulario.conceptoFacturable.descripcion}" default="" />
														</label>
													</td>										
												</tr>											
												<c:if test="${accion == 'CONSULTA'}">
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioTipoOperacion.datos.fechaRegistro" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<input type="text" id="fechaRegistro" name="fechaRegistro" style="width: 60px;"
																value="<c:out value="${tipoFormulario.fechaRegistroStr}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>										
													</tr>													
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioTipoOperacion.datos.fechaActualizacion" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<input type="text" id="fechaActualizacion" name="fechaActualizacion" style="width: 60px;"
																value="<c:out value="${tipoFormulario.fechaActualizacionStr}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioTipoJerarquia.datos.modifico" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<input type="text" id="modifico" name="modifico" style="width: 150px;" class="capitalizar"
																value="<c:out value="${tipoFormulario.modifico}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>										
													</tr>
												</c:if>													
											</table>											
										</td>																																				
									</tr>								
									<c:if test="${accion != 'CONSULTA'}">
										<tr>
											<td class="texto_ti">
												<button name="restablecer" type="reset" >
													<img src="<%=request.getContextPath()%>/images/restablecer.png" 
														title=<spring:message code="botones.restablecer" htmlEscape="true"/> 
													> 								 
												</button>
											</td>
										</tr>
									</c:if>
						    	</table>
							</div>
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
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 130%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="conceptosPopupMap" /> 
			<jsp:param name="clase" value="conceptosPopupMap" /> 
		</jsp:include>	
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="tipoOperacionPopupMap" /> 
			<jsp:param name="clase" value="tipoOperacionPopupMap" /> 
		</jsp:include>	
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="seriePopupMap" /> 
			<jsp:param name="clase" value="seriePopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="serieRemitoPopupMap" /> 
			<jsp:param name="clase" value="serieRemitoPopupMap" /> 
		</jsp:include>	
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="tipoRequerimientoPopupMap" /> 
			<jsp:param name="clase" value="tipoRequerimientoPopupMap" /> 
		</jsp:include>	
	</body>
</html>