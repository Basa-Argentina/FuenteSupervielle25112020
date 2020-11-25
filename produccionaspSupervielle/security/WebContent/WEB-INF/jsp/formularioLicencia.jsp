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
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioLicencia.titulo.registar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioLicencia.titulo.modificar" htmlEscape="true"/>
			</c:if>  
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_licencia.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/doblelistas.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioLicencia.titulo.registar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioLicencia.titulo.modificar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form name="formLicencia" action="guardarActualizarLicencia.html" commandName="licenciaFormulario" method="post" modelAttribute="licenciaFormulario">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${licenciaFormulario.id}" default="" />"/>						
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioCliente.datosCliente" htmlEscape="true"/> 
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="width: 100%;" id="busquedaDiv" align="center">
								<table style="width: 100%;" >
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioLicencia.cliente" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioLicencia.estado" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioLicencia.desde" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioLicencia.hasta" htmlEscape="true"/>
										</td>										
									</tr>
									<tr>
										<td class="texto_ti">
											<select id="idCliente" name="idCliente" size="1" style="width: 200px;" class="requerido">
												<c:forEach items="${clientes}" var="cli">
													<option value="${cli.id}"
														<c:if test="${cli.id == licenciaFormulario.cliente.id}">
															selected="selected"
														</c:if>
													>
														<c:out value="${cli.clienteStr}"/>
													</option>
												</c:forEach>
											</select>											
										</td>
										<td class="texto_ti">
											<select id="estadoId" name="estadoId" size="1" style="width: 200px;" class="requerido">
												<c:forEach items="${estados}" var="estado">
													<option value="${estado.id}"
														<c:if test="${estado.id == licenciaFormulario.estado.id}">
															selected="selected"
														</c:if>
													>
														<spring:message code="formularioLicencia.estado.${estado.nombre}"/>
													</option>
												</c:forEach>												
											</select>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaDesde" name="fechaDesde" maxlength="10" class="requerido"
												value="<c:out value="${licenciaFormulario.fechaDesdeStr}" default="" />"/>
												<script type="text/javascript">
													new tcal ({
														// form name
														'formname': 'formLicencia',
														// input name
														'controlname': 'fechaDesde'
													});
												</script>								
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaHasta" name="fechaHasta" maxlength="10" class="requerido"
												value="<c:out value="${licenciaFormulario.fechaHastaStr}" default="" />"/>
												<script type="text/javascript">
													new tcal ({
														// form name
														'formname': 'formLicencia',
														// input name
														'controlname': 'fechaHasta'
													});
												</script>
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
	</body>
</html>