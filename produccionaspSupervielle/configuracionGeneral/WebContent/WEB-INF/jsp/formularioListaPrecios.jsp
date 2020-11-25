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
				<spring:message code="formularioListaPrecios.titulo.registrar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioListaPrecios.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioListaPrecios.titulo.consultar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>		
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_lista_precios.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
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
								<spring:message code="formularioListaPrecios.titulo.registrar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioListaPrecios.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioListaPrecios.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarListaPrecios.html" modelAttribute="listaFormulario" method="post" name="formPrecios">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${listaFormulario.id}" default="" />"/>						
						<input type="hidden" id="opcion" name="opcion"/>						
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioListaPrecios.datos" htmlEscape="true"/> 
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
											<fieldset>
												<legend>
													<spring:message code="formularioListaPrecios.datos" htmlEscape="true"/>
												</legend>
												<table>
													<tr>
														<td class="texto_ti">
															<input type="checkbox" id="habilitada" name="habilitada"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
																<c:if test="${listaFormulario.habilitada == true}">
																	checked = "CHECKED"
																</c:if>
															/>				
															<spring:message code="formularioListaPrecios.datos.habilitada" htmlEscape="true"/>
														</td>
													</tr>
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioListaPrecios.datos.codigo" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>
														<td class="texto_ti">
															<input type="text" id="codigo" name="codigo" maxlength="6" 
																style="width: 70px;" class="requerido upperCase alphaNumeric"
																value="<c:out value="${listaFormulario.codigo}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>															
														</td>
													</tr>
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioConceptoFacturable.datos.descripcion" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<input type="text" id="descripcion" name="descripcion" style="width: 370px;" 
																maxlength="60" class="requerido"
																value="<c:out value="${listaFormulario.descripcion}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioListaPrecios.datos.tipoVariacion" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<select id="tipoVariacionId" name="tipoVariacionId" class="requerido"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>
																<c:forEach items="${tiposVariacion}" var="tipo">
																	<option value="${tipo.id}" 
																		<c:if test="${tipo.id == listaFormulario.tipoVariacion.id}">
																			selected="selected"
																		</c:if>
																	>
																		<c:out value="${tipo.descripcion}"/>
																	</option>
																</c:forEach>															
															</select>															
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioListaPrecios.datos.valor" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>	
														<td class="texto_ti">
															<input type="text" id="valor" name="valor" style="text-align: right; width: 60px;" 
																	value="<c:out value="${listaFormulario.valor}" default="" />" class="requerido"
																	<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if>
															/>
														</td>										
													</tr>
													<tr>
														<td class="texto_ti">
															<input type="checkbox" id="listaFija" name="listaFija" 
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
																<c:if test="${listaFormulario.listaFija == true}">
																	checked = "CHECKED"
																</c:if>
															/>				
															<spring:message code="formularioListaPrecios.datos.listaFija" htmlEscape="true"/>
														</td>
														
													</tr>
													<tr>
													<td class="texto_ti">
															<input type="checkbox" id="usaVencimiento" name="usaVencimiento" onclick="checkSelect();"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
																<c:if test="${listaFormulario.usaVencimiento == true}">
																	checked = "CHECKED"
																</c:if>
															/>				
															<spring:message code="formularioListaPrecios.datos.usaVencimiento" htmlEscape="true"/>
															<br>
														</td>
														
													</tr>
													<tr>
												
													<td class="texto_ti">
													<div id="seccion1" style="display:none">
										
													<spring:message code="formularioListaPrecios.datos.vencimiento" htmlEscape="true"/><br>
													
														<input type="text" id="fechaVencimiento" name="fechaVencimiento" 
															maxlength="10"
															value="<c:out value="${listaFormulario.fechaVencimientoStr}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>/>
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formPrecios',
																	// input name
																	'controlname': 'fechaVencimiento'
																});
															</script></c:if></div>
													</td>
											
													</tr>
													<c:if test="${accion == 'CONSULTA'}">
														<tr>	
															<td class="texto_ti">
																<spring:message code="formularioListaPrecios.datos.fechaRegistro" htmlEscape="true"/>
															</td>										
														</tr>
														<tr>	
															<td class="texto_ti">
																<input type="text" id="fechaRegistro" name="fechaRegistro" style="width: 60px;"
																	value="<c:out value="${listaFormulario.fechaRegistroStr}" default="" />"
																	<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if>
																/>
															</td>										
														</tr>													
														<tr>	
															<td class="texto_ti">
																<spring:message code="formularioListaPrecios.datos.fechaActualizacion" htmlEscape="true"/>
															</td>										
														</tr>
														<tr>	
															<td class="texto_ti">
																<input type="text" id="fechaActualizacion" name="fechaActualizacion" style="width: 60px;"
																	value="<c:out value="${listaFormulario.fechaActualizacionStr}" default="" />"
																	<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if>
																/>
															</td>										
														</tr>
														<tr>	
															<td class="texto_ti">
																<spring:message code="formularioListaPrecios.datos.modifico" htmlEscape="true"/>
															</td>										
														</tr>
														<tr>	
															<td class="texto_ti">
																<input type="text" id="modifico" name="modifico" style="width: 150px;" class="capitalizar"
																	value="<c:out value="${listaFormulario.modifico}" default="" />"
																	<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if>
																/>
															</td>										
														</tr>
													</c:if>													
												</table>
											</fieldset>
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
						<button name="guardar" type="button" onclick="popupOpcionDetalle('${accion}', ${listaVacia});" class="botonCentrado">
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
							<img src="<%=request.getContextPath()%>/images/volver4.png" > 
							<spring:message code="botones.volver" htmlEscape="true"/>  
						</button>
					</div>						
				</c:if>
				<br style="font-size: xx-small;"/>
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 140%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="popupOpcion.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
	</body>
</html>