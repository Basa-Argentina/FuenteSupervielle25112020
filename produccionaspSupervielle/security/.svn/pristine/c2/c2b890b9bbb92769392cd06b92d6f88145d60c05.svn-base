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
			<spring:message code="formularioLicencia.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_licencia.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});">
		<div class="contextMenu" id="myMenu1">
		    <ul>		      	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioLicencia.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form id="formLicencia" action="filtrarLicencia.html" commandName="licenciaBusqueda" method="post">
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
										<td class="texto_ti">
											<spring:message code="formularioLicencia.licencia.desde" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<spring:message code="formularioLicencia.licencia.hasta" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioLicencia.licencia.estado" htmlEscape="true"/>
										</td>										
										<td rowspan="2" style="vertical-align: bottom;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png"> 
												<spring:message code="textos.buscar" htmlEscape="true"/>								
											</button>
										</td>
									</tr>
									<tr>	
										<td class="texto_ti">
											<input type="text" id="fechaDesde" name="fechaDesde" maxlength="10"
												value="<c:out value="${licenciaBusqueda.fechaDesdeStr}" default="" />"/>
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
											<input type="text" id="fechaHasta" name="fechaHasta" maxlength="10"
												value="<c:out value="${licenciaBusqueda.fechaHastaStr}" default="" />"/>
												<script type="text/javascript">
													new tcal ({
														// form name
														'formname': 'formLicencia',
														// input name
														'controlname': 'fechaHasta'
													});
												</script>
										</td>
										<td class="texto_ti">	
											<select id="estadoId" name="estadoId" size="1" style="width: 200px;">
												<c:forEach items="${estados}" var="estado">
													<option												
														<c:if test="${estado.asignable == false}">
															value="0"
														</c:if>
														<c:if test="${estado.asignable == true}">
															value="${estado.id}"
														</c:if>
														<c:if test="${estado.id == licenciaBusqueda.estadoId}">
															selected="selected"
														</c:if>
													>
														<spring:message code="formularioLicencia.estado.${estado.nombre}"/>
													</option>
												</c:forEach>												
											</select>
										</td>																			
									</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>						
						<display:table name="licencias" id="licencia" requestURI="" pagesize="7" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${licencia.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>					  	
						  	<display:column property="cliente.clienteStr" titleKey="formularioLicencia.cliente" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="estado.nombre" titleKey="formularioLicencia.licencia.estado" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="fechaDesdeStr" titleKey="formularioLicencia.licencia.desde" sortable="true" class="celdaAlineadoIzquierda" sortProperty="fechaDesde"/>						  	
						  	<display:column property="fechaHastaStr" titleKey="formularioLicencia.licencia.hasta" sortable="true" class="celdaAlineadoIzquierda" sortProperty="fechaHasta"/>						  	
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" onclick="agregar();" class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="botones.agregar" htmlEscape="true"/>  
							</button>
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
		<jsp:include page="fieldAvisos.jsp"/>
	</body>
</html>