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
		<title><spring:message code="formularioDireccion.tituloConsulta" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> <spring:message code="general.ambiente" htmlEscape="true"/></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_direccion.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
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
			        		<spring:message code="formularioDireccion.tituloConsulta" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarDireccion.html" commandName="direccionBusqueda" method="post">
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
											<spring:message code="formularioDireccion.pais" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioDireccion.provincia" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioDireccion.localidad" htmlEscape="true"/>
										</td>										
									</tr>
									<tr>
										<td class="texto_ti">	
											<select id="pais" name="pais" size="1" style="width: 200px;">
												<c:forEach items="${paises}" var="p">
													<option value="${p.id}">
														<c:out value="${p.nombre}" />
													</option>
												</c:forEach>												
											</select>
										</td>
										<td class="texto_ti"><select id="provincia"
											name="provincia" size="1" style="width: 190px;">												
												<c:forEach
													items="${direccionBusqueda.barrio.localidad.provincia.pais.provincias}"
													var="p">
													<option value="${p.id}"
														<c:if test="${direccionBusqueda.barrio.localidad.provincia.id == p.id}">
														selected="selected"
													</c:if>>
														<c:out value="${p.nombre}" />
													</option>
												</c:forEach>
										</select>
										</td>
										<td class="texto_ti"><select id="localidad"
											name="localidad" size="1" style="width: 190px;">												
												<c:forEach
													items="${direccionBusqueda.barrio.localidad.provincia.localidades}"
													var="l">
													<option value="${l.id}"
														<c:if test="${direccionBusqueda.barrio.localidad.id == l.id}">
														selected="selected"
													</c:if>>
														<c:out value="${l.nombre}" />
													</option>
												</c:forEach>												
										</select>
										</td>										
									</tr>
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioDireccion.barrio" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioDireccion.calle" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<spring:message code="formularioDireccion.numero" htmlEscape="true"/>
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
											<select id="idBarrio" name="idBarrio" size="1" style="width: 200px;">
												<c:forEach items="${direccionBusqueda.barrio.localidad.barrios}" var="b">
													<option														
														<c:if test="${direccionBusqueda.barrio.id == b.id}">
															selected="selected"
														</c:if>
													>
														<c:out value="${b.nombre}"/>
													</option>
												</c:forEach>												
											</select>
										</td>
										<td class="texto_ti">
											<input type="text" id="calle" name="calle" style="width: 150px;"
												value="<c:out value="${direccionBusqueda.calle}" default="" />"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="numero" name="numero" style="width: 150px;"
												value="<c:out value="${direccionBusqueda.numero}" default="" />"/>
										</td>
									</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="direcciones" id="direccion" requestURI="mostrarDireccion.html" pagesize="7" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${direccion.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>					  	
						  	<display:column property="barrio.nombre" titleKey="formularioDireccion.barrio" sortable="true" class="celdaAlineadoIzquierda"/>						  	
						  	<display:column property="calle" titleKey="formularioDireccion.calle" sortable="true" class="celdaAlineadoIzquierda"/>						  	
						  	<display:column property="numero" titleKey="formularioDireccion.numero" sortable="true" class="celdaAlineadoIzquierda"/>						  	
	  					  	<display:column property="observaciones" titleKey="formularioDireccion.observaciones" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" onclick="agregarDireccion();" class="botonCentrado">
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