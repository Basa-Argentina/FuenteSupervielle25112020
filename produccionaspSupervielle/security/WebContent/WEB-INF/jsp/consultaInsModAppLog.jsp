<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioInsModAppLog.titulo" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> (<spring:message code="general.ambiente" htmlEscape="true"/>)
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_applog.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
	</head>	
	<body onload="mostrarErrores(${errores});">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
				<div class="contenido" align="left">
					<br style="font-size: medium;"/>
					<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioInsModAppLog.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarInsModAppLog.html" commandName="insErrBusqueda" method="post" name="insErrBusqueda">
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
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td align="center">
											<table>
												<tr>
													<td class="texto_ti" style="padding-left: 50px;">	
														<spring:message code="formularioInsModAppLog.app" htmlEscape="true"/>
													</td>
													<td class="texto_ti" style="padding-left: 50px;">	
														<spring:message code="formularioInsModAppLog.nivel" htmlEscape="true"/>
													</td>
													<td class="texto_ti" style="padding-left: 50px;">	
														<spring:message code="formularioInsModAppLog.fecha" htmlEscape="true"/>
													</td>
													<td rowspan="2" style="vertical-align: bottom; padding-left: 50px;">
														<button name="buscar" class="botonCentrado" type="submit">
															<img src="<%=request.getContextPath()%>/images/buscar.png">
															<spring:message code="textos.buscar" htmlEscape="true"/> 								
														</button>	
													</td>
												</tr>
												<tr>
													<td class="texto_ti" style="padding-left: 50px;">	
														<input type="text" id="app" name="app" style="width: 200px"
															value="<c:out value="${insModAppLogBusqueda.app}" default="" />"/>
													</td>	
													<td class="texto_ti" style="padding-left: 50px;">	
													     <select id="nivel" name="nivel" style="width: 100px;">			        
													        <option  value=""></option>
													        <option <c:if test="${insModAppLogBusqueda.nivel=='ERROR'}"> selected="selected" </c:if>  value="ERROR">ERROR</option>
													        <option <c:if test="${insModAppLogBusqueda.nivel=='FATAL'}"> selected="selected" </c:if>  value="FATAL">FATAL</option>
													        <option <c:if test="${insModAppLogBusqueda.nivel=='WARN'}"> selected="selected" </c:if> value="WARN">WARN</option>
													    </select>
													 </td>
													 <td class="texto_ti" style="padding-left: 50px;">								
														<input type="text" id="fechaHora" name="fechaHora" maxlength="10"
															value="<c:out value="${insModAppLogBusqueda.fechaHoraStrCorta}" default="" />"/>
														<script type="text/javascript">
															new tcal ({
																// form name
																'formname': 'insErrBusqueda',
																// input name
																'controlname': 'fechaHora'
															});
														</script>											
													</td>
												</tr>
											</table>
									</td>
									</tr>
								</table>
							</div>		
						</fieldset>
					</form:form>
					<br style="font-size: xx-small;"/>
					<fieldset>
						<div style="width: 100%; overflow-y: hidden; overflow-x: auto;">
						<display:table name="insModAppLogs" id="insModAppLog" requestURI="mostrarInsModAppLog.html" pagesize="7" sort="list">
							<display:column property="app" titleKey="formularioInsModAppLog.app" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column sortProperty="nivel"
											titleKey="formularioInsModAppLog.nivel"
											sortable="true" class="celdaAlineadoCentrado">
								<a href="#" onclick="consultarError(${insModAppLog.id});">
								<c:if test="${insModAppLog.nivel == 'ERROR'}">
							  	 	<img src="<%=request.getContextPath()%>/images/error.png"
							  	 		title="ERROR">	  	 	
							  	 </c:if>
							  	 <c:if test="${insModAppLog.nivel == 'FATAL'}">
							  	 	<img src="<%=request.getContextPath()%>/images/fatal.png"
							  	 		title="FATAL">	  	 	
							  	 </c:if>
							  	 <c:if test="${insModAppLog.nivel == 'WARN'}">
							  	 	<img src="<%=request.getContextPath()%>/images/warning.png"
							  	 		title="WARN">	  	 	
							  	 </c:if>	
							  	 </a>
							</display:column>	
							<display:column property="fechaHoraStr" titleKey="formularioInsModAppLog.fecha" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="mensaje" titleKey="formularioInsModAppLog.mensaje" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="clase" titleKey="formularioInsModAppLog.clase" sortable="true" class="celdaAlineadoIzquierda"/>
						</display:table>
						</div>
						<div id="eliminarDiv" style="width: auto; display:block;" align="right">
							<table class="tablaEliminar">
								<tr>
									<td style="text-align: right;">
										<button name="eliminarBtn" type="button" class="botonCentrado"
											<c:if test="${habilitarEliminar == 'NO'}">
												disabled="disabled"
											</c:if>
											title="<spring:message code="botones.eliminar" htmlEscape="true"/>"
											onclick="eliminar('<spring:message code="formularioInsModAppLog.mensajeEliminar" htmlEscape="true"/>','eliminarInsModAppLog.html');">
												<img src="<%=request.getContextPath()%>/images/eliminar.png">	
												<spring:message code="botones.eliminar" htmlEscape="true"/>							 
										</button>
									</td>
								</tr>	
							</table>	
						</div>
					</fieldset>
					<br style="font-size: xx-small;"/>
					<div align="center">
						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/volver4.png"/> 
							<spring:message code="botones.cerrar" htmlEscape="true"/>  
						</button>						
					</div>	
					<br style="font-size: xx-small;"/>
				</fieldset>	
			</div>
			<jsp:include page="footer.jsp"/>
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
	</body>
</html>