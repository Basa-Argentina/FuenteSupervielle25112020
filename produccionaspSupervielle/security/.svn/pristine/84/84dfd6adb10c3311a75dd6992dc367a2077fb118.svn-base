<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page errorPage="/error.html" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>	
		<title>
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioGroup.tituloFormAgregar" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> (<spring:message code="general.ambiente" htmlEscape="true"/>)
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioGroup.tituloFormModificar" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> (<spring:message code="general.ambiente" htmlEscape="true"/>)
			</c:if> 
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioGroup.tituloFormConsultar" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> (<spring:message code="general.ambiente" htmlEscape="true"/>)
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_group.js"></script>
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
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioGroup.tituloFormAgregar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioGroup.tituloFormModificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioGroup.tituloFormConsultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>			
					<form:form action="guardarActualizarGroup.html" commandName="groupForm" method="post">
						<input type="hidden" id="accion" name="accion"
							value="<c:out value="${accion}" default="" />"
						/>
						<input type="hidden" id="id" name="id"
								<c:if test="${accion == 'MODIFICACION'}">
									value="<c:out value="${groupFormulario.id}" default="" />"
								</c:if> 
						/>		
						<input type="hidden" id="privilegiosGrupoSeleccionados" name="privilegiosGrupoSeleccionados"/>
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
								<table style="width: 100%;">
									<tr>
										<td align="center">
											<table>
												<tr>
													<td class="texto_ti">
														<spring:message code="formularioGroup.groupName" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="groupName" name="groupName" 
															style="width: 300px;" class="requerido" maxlength="60"	
															value="<c:out value="${groupFormulario.groupName}" default="" />"
															<c:if test="${accion == 'CONSULTA' || groupFormulario.nombreFijo == true}">
																readonly="readonly"
															</c:if>
														/>
													</td>
												</tr>		
											</table>
										</td>
									</tr>	
									<c:if test="${accion != 'CONSULTA'}">
										<tr>
											<td class="texto_ti">
												<button name="restablecer" type="reset" >
													<img src="<%=request.getContextPath()%>/images/restablecer.png" title=<spring:message code="botones.restablecer" htmlEscape="true"/> > 								 
												</button>											
											</td>
										</tr>
									</c:if>		
									<tr>
										<td>
											<fieldset>
												<table width="100%">
										          	<thead>
										            	<tr>
											            	<th align="left" id="grupoImg">								 
													        	<font style="color:#003090">
													        		<spring:message code="formularioGroup.asignacionPrivilegios" htmlEscape="true"/> 
													        	</font>
													        	<img id="grupoImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
													        	<img id="grupoImgSrc" style="DISPLAY: none" src="images/skip.png" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">								 	  
											              	</th>
													 	</tr>
													</thead>
												</table>
												<div id="grupoDiv" align="center">				         	
													<table>
														<tr>
															<td>
																<spring:message code="formularioGroup.privilegiosDisponibles" htmlEscape="true"/>:
															</td>
															<td>
																&nbsp;
															</td>
															<td>
																<spring:message code="formularioGroup.privilegiosAsignados" htmlEscape="true"/>:
															</td>
														</tr>
														<tr>
													      <td width="40%" align="center">
													        <select id="privilegiosGrupoSeleccionadosIzq" name="privilegiosGrupoSeleccionadosIzq" size="8" style="width:250px" multiple="multiple">
													        	<c:forEach items="${privilegiosIzq}" var="privilegioIzq">
																	<option value="<c:out value="${privilegioIzq.authority}"/>">
																		<c:out value="${privilegioIzq.authority}"/>
																	</option>
																</c:forEach>
													        </select>
													      </td>
													      <td width="20%" align="center">
													      	<sec:authorize ifAnyGranted="ROLE_ABM_GRUPOS_DE_USUARIOS">								          
													          <c:if test="${accion != 'CONSULTA'}">
														          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('privilegiosGrupoSeleccionados')" 
														        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
														          	>
														          <br />
														          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('privilegiosGrupoSeleccionados')" 
														        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
														          >
														          <br />
												        	  </c:if>
												        	</sec:authorize>
													      </td>
													      <td width="40%" align="center">
													        <select id="privilegiosGrupoSeleccionadosDer" name="privilegiosGrupoSeleccionadosDer" size="8" style="width:250px" multiple="multiple">
													            <c:forEach items="${privilegiosDer}" var="privilegioDer">
																	<option value="<c:out value="${privilegioDer.authority}"/>">
																		<c:out value="${privilegioDer.authority}"/>
																	</option>
																</c:forEach>
													        </select>
													      </td>
													    </tr>
													    <tr>
													    	<td align="center" colspan="3" class="error" style="text-align: center;">
																<c:if test="${asignacion != null}">
																	<spring:message code="${asignacion}" htmlEscape="true"/>
																</c:if>
															</td>
													    </tr>
												    </table>
											   	</div> 
										    </fieldset>
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
		<jsp:include page="fieldErrors.jsp"/>
	</body>
</html>