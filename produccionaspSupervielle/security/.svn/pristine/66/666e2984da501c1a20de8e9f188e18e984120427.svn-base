<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>		
			<spring:message code="formularioParameterInsMod.tituloForm" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> (<spring:message code="general.ambiente" htmlEscape="true"/>)
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_parameter.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/doblelistas.js"></script>
	</head>	
	<body onload="mostrarErrores(${errores});">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<spring:message code="formularioParameterInsMod.tituloForm" htmlEscape="true"/>
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarParameter.html" commandName="parameterInsModForm" method="post">							
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.datosCaracteristicos" htmlEscape="true"/> 
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
										<td style="width: 42%; vertical-align: top;">
											<fieldset style="height: 190px;">
						         				<legend>
						         					<spring:message code="formularioParameterInsMod.tituloSeguridad" htmlEscape="true"/>
						       					</legend>
												<table>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.passwordExpirationDays" htmlEscape="true"/>
														</td>															
														<td class="texto_ti">
															<input type="text" id="passwordExpirationDays" name="passwordExpirationDays" 
																style="width: 50px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.passwordExpirationDays}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>															
														</td>							
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.passwordWarningDays" htmlEscape="true"/>
														</td>	
														<td class="texto_ti">
															<input type="text" id="passwordWarningDays" name="passwordWarningDays" 
																style="width: 50px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.passwordWarningDays}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>															
														</td>				
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.failedLoginCounter" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<input type="text" id="failedLoginCounter" name="failedLoginCounter" 
																style="width: 50px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.failedLoginCounter}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>															
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.minutesSanctionLogin" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<input type="text" id="minutesSanctionLogin" name="minutesSanctionLogin" 
																style="width: 50px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.minutesSanctionLogin}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>															
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.enableOldPassword" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<input type="checkbox" id="enable" name="enable" style="width: auto;"
																	<c:if test="${parameterInsModFormulario.enableOldPassword == 1}">
																		checked = "CHECKED"
																	</c:if>
																	value="true"
															/>															
														</td>														
													</tr>
												</table>
											</fieldset>
										</td>
										<td style="width: auto; vertical-align: top;">
											<fieldset style="height: 190px;">
						         				<legend>
						         					<spring:message code="formularioParameterInsMod.tituloSMTP" htmlEscape="true"/>
						       					</legend>
							       				<table>
							       					<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.hostSMTP" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<input type="text" id="hostSMTP" name="hostSMTP" 
																style="width: 200px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.hostSMTP}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.portSMTP" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<input type="text" id="portSMTP" name="portSMTP" 
																style="width: 200px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.portSMTP}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>
													</tr>
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.enableSSLSMTP" htmlEscape="true"/>
														</td>
														<td class="texto_ti">	
															<input type="checkbox" id="enableSSL" name="enableSSL" 
																style="width: auto;" class="requerido"
																<c:if test="${parameterInsModFormulario.enableSSLSMTP == 1}">
																	checked = "CHECKED"
																</c:if>
																value="true"
															/>
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.userSMTP" htmlEscape="true"/>:
														</td>
														<td class="texto_ti">
															<input type="text" id="userSMTP" name="userSMTP" 
																style="width: 200px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.userSMTP}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.eMailUserSMTP" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<input type="text" id="eMailUserSMTP" name="eMailUserSMTP" 
																style="width: 200px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.eMailUserSMTP}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.passwordSMTP" htmlEscape="true"/>
														</td>
														<td class="texto_ti">				
															<input type="password" id="passwordSMTP" name="passwordSMTP" 
																style="width: 200px;" class="requerido"
																value="<c:out value="${parameterInsModFormulario.passwordSMTP}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioParameterInsMod.enableSPASMTP" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<input type="checkbox" id="enableSPA" name="enableSPA" style="width: auto;" 
																<c:if test="${parameterInsModFormulario.enableSPASMTP == 1}">
																	checked = "CHECKED"
																</c:if>
																value="true"
															/>
														</td>
													</tr>
							       				</table>							
						       				</fieldset>
										</td>
									</tr>
									<tr>
										<td>
											<table style="width: 100%;">
												<c:if test="${accion != 'CONSULTA'}">
													<tr>
														<td align="left">
															<button name="restablecer" type="reset" >
																<img src="<%=request.getContextPath()%>/images/restablecer.png" 
																	title=<spring:message code="botones.restablecer" htmlEscape="true"/> > 								 
															</button>															
														</td>
													</tr>
												</c:if>	
											</table>
										</td>
									</tr>
								</table>				       				
							</div>							
						</fieldset>
					</form:form>
				</fieldset>	
				<br style="font-size: xx-small;"/>
			    <c:if test="${accion != 'CONSULTA'}">
					<div align="center">
						<button name="guardar" type="button" onclick="guardar();" class="botonCentrado"> 
							<img src="<%=request.getContextPath()%>/images/ok.png"/> 
							<spring:message code="botones.guardar" htmlEscape="true"/>  
						</button>
						&nbsp;
						<button name="cancelar" type="button"  onclick="volverCancelar();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png"/> 
							<spring:message code="botones.cancelar" htmlEscape="true"/>  
						</button>						
					</div>
				</c:if>
				<c:if test="${accion == 'CONSULTA'}">
					<div align="center">
						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado"><img src="<%=request.getContextPath()%>/images/volver.png"> 
							<spring:message code="botones.volver" htmlEscape="true"/>  
						</button>						
					</div>
				</c:if>
				<br style="font-size: xx-small;"/>
			</div>
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
	</body>
</html>