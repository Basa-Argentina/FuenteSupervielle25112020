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
				<spring:message code="formularioConceptoFacturable.titulo.registrar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioConceptoFacturable.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioConceptoFacturable.titulo.consultar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>		
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_concepto_facturable2.js"></script>
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
								<spring:message code="formularioConceptoFacturable.titulo.registrar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioConceptoFacturable.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioConceptoFacturable.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarConceptoFacturable.html" modelAttribute="conceptoFormulario" method="post">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${conceptoFormulario.id}" default="" />"/>						
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioConceptoFacturable.datos" htmlEscape="true"/> 
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="width: 100%;" id="busquedaDiv" align="center">
								<table style="width: 100%;">
									<tr>
										<td align="center">											
											<table>
												<tr>
													<td class="texto_ti">
														<input type="checkbox" id="habilitado" name="habilitado"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${conceptoFormulario.habilitado == true}">
																checked = "CHECKED"
															</c:if>
														/>				
														<spring:message code="formularioConceptoFacturable.datos.habilitado" htmlEscape="true"/>
													</td>
												</tr>
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioConceptoFacturable.datos.codigo" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>
												<td class="texto_ti"><input type="text" id="codigo"
													name="codigo" maxlength="6" style="width: 50px;"
													value="<c:out value="${conceptoFormulario.codigo}" default="" />"
													class="requerido upperCase alphaNumeric completarZeros"
													<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
												</td>
											</tr>
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioConceptoFacturable.datos.descripcion" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="descripcion" name="descripcion" style="width: 300px;" maxlength="60"
															value="<c:out value="${conceptoFormulario.descripcion}" default="" />" class="requerido"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
													</td>										
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="checkbox" id="generaStock" name="generaStock" 
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
															<c:if test="${conceptoFormulario.generaStock == true}">
																checked = "CHECKED"
															</c:if>
														/>				
														<spring:message code="formularioConceptoFacturable.datos.generaStock" htmlEscape="true"/>
													</td>
												</tr>
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioConceptoFacturable.datos.costo" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="costo" name="costo" style="text-align: right;" 
															value="<c:out value="${conceptoFormulario.costo}" default="" />" class="requerido"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioConceptoFacturable.datos.precioBase" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="precioBase" name="precioBase" style="text-align: right;" 
															value="<c:out value="${conceptoFormulario.precioBase}" default="" />" class="requerido"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioConceptoFacturable.datos.impuesto" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="impuestoCodigo" name="impuestoCodigo" maxlength="3" style="width: 50px;"
															value="<c:out value="${conceptoFormulario.impuesto.codigo}" default="" />" 
															class="requerido upperCase alphaNumeric"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopup('impuestosPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>&nbsp;
														<label id="impuestoCodigoLabel" for="impuestoCodigo">
															<c:out value="${conceptoFormulario.impuesto.descripcion}" default="" />
														</label>															
													</td>										
												</tr>
												<tr>
													<td class="texto_ti">
														<spring:message code="formularioConceptoFacturable.datos.calculoAutomatico" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<select size="1" id="tipoCalculo" name="tipoCalculo" class="requerido"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<option value="Automatíco"
																<c:if test="${conceptoFormulario.tipoCalculo == 'Automatíco'}">
																	selected="selected"
																</c:if>
															>
																<c:out value="Automatíco"/>
															</option>
															<option value="Manual"
																<c:if test="${conceptoFormulario.tipoCalculo == 'Manual'}">
																	selected="selected"
																</c:if>
															>
																<c:out value="Manual"/>
															</option>
															<option value="Unico"
																<c:if test="${conceptoFormulario.tipoCalculo == 'Unico'}">
																	selected="selected"
																</c:if>
															>
																<c:out value="Unico"/>
															</option>
														</select>													
													</td>
												</tr>
												<tr>	
													<td class="texto_ti">
														<spring:message code="formularioConceptoFacturable.datos.tipo" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<select id="tipoId" name="tipoId" class="requerido"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<c:forEach items="${tipos}" var="tipo">
																<option value="${tipo.id}" 
																	<c:if test="${tipo.id == conceptoFormulario.tipo.id}">
																		selected="selected"
																	</c:if>
																>
																	<c:out value="${tipo.descripcion}"/>
																</option>
															</c:forEach>															
														</select>
													</td>										
												</tr>													
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
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 140%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="impuestosPopupMap" /> 
			<jsp:param name="clase" value="impuestosPopupMap" /> 
	</jsp:include>		
	</body>
</html>