<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
	<title> 
	</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/mavalos_consulta_localidades.js"></script>
		
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})" style="width: 100%;">
		<div class="contextMenu" id="myMenu1">
		    <ul>		      	 
			    <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
			    <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
		<table style="width: 100%; border-spacing: 0px;">
		<tr>
			<td style="width: 48%; vertical-align: top;">
				<form:form action="filtrarLocalidades.html" commandName="paisBusqueda" method="post">
										<fieldset>
											<table width="100%">
							            	<thead>
								            	<tr>
								              		<th align="left" id="busquedaImg" >						  
										        		<font style="color:#003090">
										        			<spring:message code="formularioLugares.buscarLocalidades" htmlEscape="true"/>
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
														<input type="hidden" id="pciaId" name="pciaId" style="width: 150px;"
															value="<c:out value="${localidadBusqueda.provincia.id}" default="" />"/>
													</td>
												</tr>
												<tr>	
													<td class="texto_ti">
														<input type="text" id="id" name="id" style="width: 150px;"
															value="<c:out value="${localidadBusqueda.id}" default="" />"/>
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
															value="<c:out value="${localidadBusqueda.nombre}" default="" />"/>
													</td>
												</tr>
												<tr>										
													<td style="vertical-align: bottom;" align="center">
														<button name="buscar" class="botonCentrado" type="button" onclick="buscarFiltro();">
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
				<table width="100%;">
		           	<thead>
		            	<tr>
		              		<th align="left">						  
				        		<font style="color:#003090">
				        			<spring:message code="formularioLugares.localidades" htmlEscape="true"/>
				        		</font>						        						 
		              		</th>
					 	</tr>
					</thead>
				</table>
		<div class="displayTagDiv2" style="text-align: left; height: 80%;">
			<display:table name="pciaSel.localidades" id="localidad" requestURI="mostrarLocalidades.html" pagesize="10" sort="list" keepStatus="true" defaultsort="1">
				<display:column class="hidden" headerClass="hidden">
				    <input type="hidden" id="hdn_id" value="${localidad.id}"/>
              	</display:column>		
              	<display:column class="hidden" headerClass="hidden">
			    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="formularioLugares.mensaje.eliminarLocalidad" htmlEscape="true"/>"/>
          		   	</display:column>					  	
			  	<display:column property="id" titleKey="formularioLugares.código" sortable="true" class="celdaAlineadoIzquierda" />
			  	<display:column property="nombre" titleKey="formularioLugares.nombre" sortable="true" class="celdaAlineadoIzquierda"/>				  	
			  	<display:column class="celdaAlineadoCentrado">
					<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
				</display:column>
				<display:column class="hidden" headerClass="hidden">
					<input type="hidden" id="hdn_idPcia" value="${localidad.provincia.id}"/>
		        </display:column>
			</display:table>
		</div>
				<br>
					<div style="width: 100%" align ="center">
							<input type="hidden" id="idPcia" name="idPcia"
								value="<c:out value="${pciaSel.id}" default="" />"/>
							<input type="hidden" id="accion" name="accion"
								value="<c:out value="${accion}" default="" />"/>
							<input type="hidden" id="codigo" name="codigo"
								value="<c:out value="${locFormulario.id}" default="" />"/>
							<input type="text" id="nuevo" name="nuevo" size="32"
								value="<c:out value="${locFormulario.nombre}" default="" />"/>
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
			<td style="width: 52%; vertical-align: top;">
				<iframe src="mostrarBarrios.html" id="iframeBarrios" frameborder="0" height="495px" width="100%"></iframe>
			</td>
		</tr>
	</table>	 
	<div id="darkLayer" class="darkClassWithoutHeight" style="height: 100%;">&nbsp;</div>	
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisosChicos.jsp" />
</body>
</html>