<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioLugares.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_lugares.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2-original.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
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
					<table style="width: 100%;">
						<tr>
							<td style="width: 25%; min-width: 25%; vertical-align: top;">
									<form:form action="filtrarLugares.html" commandName="paisBusqueda" method="post">
										<fieldset>
											<table width="100%">
							            	<thead>
								            	<tr>
								              		<th align="left" id="busquedaImg" >						  
										        		<font style="color:#003090">
										        			<spring:message code="formularioLugares.buscarPaises" htmlEscape="true"/>
										        		</font>
										        		<img id="busquedaImgSrcDown" src="images/skip_down.png" style="DISPLAY: none" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										        		<img id="busquedaImgSrc" src="images/skip.png" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
								              		</th>
											 	</tr>
											</thead>
										</table>
										<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: block;"  id="busquedaDiv" align="center">
											<table class="busqueda" style="width: 100%; background-color: white;">
												<tr>					
													<td class="texto_ti">
														<spring:message code="formularioLugares.código" htmlEscape="true"/>
													</td>
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="id" name="id" style="width: 150px;"
															value="<c:out value="${paisBusqueda.id}" default="" />"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<spring:message code="formularioLugares.nombre" htmlEscape="true"/>
													</td>
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="nombre" name="nombre" style="width: 150px;"
															value="<c:out value="${paisBusqueda.nombre}" default="" />"/>
													</td>
												</tr>
												<tr>										
													<td style="vertical-align: bottom;" colspan="2" align="center">
														<button name="buscar" class="botonCentrado" type="submit">
															<img src="<%=request.getContextPath()%>/images/buscar.png"> 
															<spring:message code="textos.buscar" htmlEscape="true"/>								
														</button>
													</td>																																						
												</tr>
											</table>
											</div>	
										</fieldset>
									</form:form>
								<fieldset style="min-height: 200px;">
									<table width="100%">
						            	<thead>
							            	<tr>
							              		<th align="left">						  
									        		<font style="color:#003090">
									        			<spring:message code="formularioLugares.paises" htmlEscape="true"/>
									        		</font>						        						 
							              		</th>
										 	</tr>
										</thead>
									</table>
									<div style="text-align: center;">
										<display:table name="paises" id="pais" requestURI="" pagesize="10" sort="list" keepStatus="true" defaultsort="4">											
											<display:column class="hidden" headerClass="hidden">
											    <input type="hidden" id="hdn_id" value="${pais.id}"/>
							              	</display:column>		
							              	<display:column class="hidden" headerClass="hidden">
										    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="formularioLugares.mensaje.eliminarPais" htmlEscape="true"/>"/>
						           		   	</display:column>					  	
										  	<display:column property="id" titleKey="formularioLugares.código" sortable="true" class="celdaAlineadoIzquierda"/>
										  	<display:column property="nombre" titleKey="formularioLugares.nombre" sortable="true" class="celdaAlineadoIzquierda"/>										  	
										  	<display:column class="celdaAlineadoCentrado">
									  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
											</display:column>	
										</display:table> 
									</div>
									<br>
									<div style="width: 100%" align ="center">
								
											<input type="hidden" id="accion" name="accion"
												value="<c:out value="${accion}" default="" />"/>
											<input type="hidden" id="codigo" name="codigo"
												value="<c:out value="${paisFormulario.id}" default="" />"/>
											<input type="text" id="nuevo" name="nuevo" size="35"
												value="<c:out value="${paisFormulario.nombre}" default="" />"/>
											<c:if test="${accion == 'NUEVO'}">	
												<button name="agregar" type="button" onclick="agregar();" class="botonCentrado">
													<img src="<%=request.getContextPath()%>/images/add.png" > 
													<spring:message code="botones.agregar" htmlEscape="true"/>  
												</button>
											</c:if>
											<c:if test="${accion == 'MODIFICAR'}">	
												<button name="modificar" type="button" onclick="agregar();" class="botonCentrado">
													<img src="<%=request.getContextPath()%>/images/ok.png" > 
													<spring:message code="botones.modificar" htmlEscape="true"/>  
												</button>
												<button name="cancelar" type="button" onclick="cancelar();" class="botonCentrado">
													<img src="<%=request.getContextPath()%>/images/cancelar.png" > 
													<spring:message code="botones.cancelar" htmlEscape="true"/>  
												</button>
											</c:if>
																					
									</div>
								</fieldset>
							</td>
							<td style="width: 75%; vertical-align: top;">
								
								
								<iframe src="mostrarProvincias.html" id="iframePcias" frameborder="0" height="700px" 
								width="100%" align="top" style="border-spacing: 0px; margin: 0px; padding: 0px;"></iframe>
									
								
							</td>
						</tr>
					</table>					
					<br style="font-size: xx-small;"/>
					<div align="center">
						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/volver4.png"/> 
							<spring:message code="botones.cerrar" htmlEscape="true"/>  
						</button>						
					</div>	
					<br style="font-size: xx-small;"/>
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
	</body>
</html>