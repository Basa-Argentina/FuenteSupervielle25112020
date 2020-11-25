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
				<spring:message code="formularioTipoJerarquia.titulo.registrar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioTipoJerarquia.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioTipoJerarquia.titulo.consultar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>		
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_tipo_jerarquia.js"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
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
								<spring:message code="formularioTipoJerarquia.titulo.registrar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioTipoJerarquia.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioTipoJerarquia.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarTipoJerarquia.html" modelAttribute="tipoFormulario" method="post">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${tipoFormulario.id}" default="" />"/>						
						<input type="hidden" id="opcion" name="opcion"/>						
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioTipoJerarquia.datos" htmlEscape="true"/> 
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
														<spring:message code="formularioTipoJerarquia.datos.codigo" htmlEscape="true"/>
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
														<spring:message code="formularioConceptoFacturable.datos.descripcion" htmlEscape="true"/>
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
														<spring:message code="formularioTipoJerarquia.datos.observacion" htmlEscape="true"/>
													</td>										
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="observacion" name="observacion" style="width: 370px;" 
															maxlength="60"
															value="<c:out value="${tipoFormulario.descripcion}" default="" />"
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
													</td>										
												</tr>																							
												<c:if test="${accion == 'CONSULTA'}">
													<tr>	
														<td class="texto_ti">
															<spring:message code="formularioTipoJerarquia.datos.fechaRegistro" htmlEscape="true"/>
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
															<spring:message code="formularioTipoJerarquia.datos.fechaActualizacion" htmlEscape="true"/>
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
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 100%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
	</body>
</html>