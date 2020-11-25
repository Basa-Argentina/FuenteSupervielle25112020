<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioConceptoFacturable.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_concepto_facturable.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>
		    	<li id="consultar"><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>			      	 
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
			        		<spring:message code="formularioConceptoFacturable.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarConceptoFacturable.html" modelAttribute="conceptoFacturableBusqueda" method="post">
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
											<spring:message code="formularioConceptoFacturable.filtro.codigo" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="codigo" name="codigo" style="width: 150px;"
												value="<c:out value="${conceptoFacturableBusqueda.codigo}" default="" />"/>
										</td>	
										<td class="texto_ti">
											<spring:message code="formularioConceptoFacturable.filtro.descripcion" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="descripcion" name="descripcion" style="width: 150px;"
												value="<c:out value="${conceptoFacturableBusqueda.descripcion}" default="" />"/>
										</td>			
										<td class="texto_ti">
											<spring:message code="formularioConceptoFacturable.filtro.tipo" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<select id="tipoId" name="tipoId">
												<option value="0">
													<c:out value="Todos"/>
												</option>
												<c:forEach items="${tipos}" var="tipo">
													<option value="${tipo.id}"
														<c:if test="${tipo.id == conceptoFacturableBusqueda.tipo.id}">
															selected="selected"
														</c:if>
													>
														<c:out value="${tipo.descripcion}"/>
													</option>
												</c:forEach>															
											</select>
										</td>								
										<td style="vertical-align: bottom;">
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
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="conceptos" id="concepto" requestURI="mostrarConceptoFacturable.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${concepto.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>					  	
						  	<display:column property="codigo" titleKey="formularioConceptoFacturable.filtro.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="descripcion" titleKey="formularioConceptoFacturable.filtro.descripcion" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="tipo.descripcion" titleKey="formularioConceptoFacturable.tabla.tipo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="tipoCalculo" titleKey="formularioConceptoFacturable.tabla.tipoCalculo" sortable="true" class="celdaAlineadoIzquierda"/>					  	 	
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