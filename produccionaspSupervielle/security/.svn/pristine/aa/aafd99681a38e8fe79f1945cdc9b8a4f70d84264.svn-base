<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page errorPage="/error.html" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioAuthority.tituloConsulta" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> (<spring:message code="general.ambiente" htmlEscape="true"/>)
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_authority.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script src="js/eliminar.js" language="JavaScript"></script>
	</head>	
	<body onload="mostrarErrores(${errores});">		
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioAuthority.tituloConsulta" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>	
					<form:form action="filtrarAuthority.html" commandName="authorityBusqueda" method="post">	
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
										<td  align="center">
											<table>
												<tr>									
													<td class="texto_ti">
														<spring:message code="formularioAuthority.authority" htmlEscape="true"/>
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
														<input type="text" id="authority" name="authority" style="width: 200px;"
															value="<c:out value="${authorityBusqueda.authority}" default="" />"/>												
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
						<display:table name="authoritys" id="authority" requestURI="mostrarAuthority.html" pagesize="7" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${authority.authority}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
							<display:column property="authority" titleKey="formularioAuthority.authority" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="description" titleKey="formularioAuthority.description" sortable="true" class="celdaAlineadoIzquierda"/>						  	
						</display:table>						
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