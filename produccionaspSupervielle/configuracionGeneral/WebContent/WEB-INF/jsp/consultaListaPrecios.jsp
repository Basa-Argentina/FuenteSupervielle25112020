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
			<spring:message code="formularioListaPrecios.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_lista_precios.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2-original.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg}); buscarAsociacionesOnBodyLoad('${listaSel.id}');">
		<div class="contextMenu" id="myMenu1">
		    <ul>		      	 
		    	<li id="consultar"><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>
		    	<sec:authorize ifAllGranted="ROLE_AJUSTAR_LISTA_DE_PRECIOS">
			      	<li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
			      	<li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		      	</sec:authorize>
		    </ul> 	 
	  	</div> 
		<div class="contextMenu" id="myMenu2">
		    <ul>
		    	<sec:authorize ifAllGranted="ROLE_AJUSTAR_LISTA_DE_PRECIOS">		      	 		    	
			      	<li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
			      	<li id="quitar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		      	</sec:authorize>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioListaPrecios.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarListaPrecios.html" commandName="listaPreciosBusqueda" method="post">
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.buscar" htmlEscape="true"/>&nbsp;<spring:message code="formularioListaPrecios.titulo" htmlEscape="true"/>
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
										<td class="texto_td">
											<spring:message code="formularioListaPrecios.filtro.codigo" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<input type="text" id="codigo" name="codigo" style="width: 150px;"
												value="<c:out value="${listaPreciosBusqueda.codigo}" default="" />"/>
										</td>
										<td class="texto_td">
											<spring:message code="formularioListaPrecios.filtro.descripcion" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<input type="text" id="descripcion" name="descripcion" style="width: 150px;"
												value="<c:out value="${listaPreciosBusqueda.descripcion}" default="" />"/>
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
					<table style="width: 100%;">
						<tr>
							<td style="width: 25%; min-width: 25%; vertical-align: top;">
								<fieldset style="min-height: 200px;">
									<table width="100%">
						            	<thead>
							            	<tr>
							              		<th align="left">						  
									        		<font style="color:#003090">
									        			<spring:message code="formularioListaPrecios.tabla.listaPrecios" htmlEscape="true"/>
									        		</font>						        						 
							              		</th>
										 	</tr>
										</thead>
									</table>
									<div style="text-align: center;">
										<display:table name="listas" id="lista" requestURI="" pagesize="10" sort="list" keepStatus="true">											
											<display:column class="hidden" headerClass="hidden">
											    <input type="hidden" id="hdn_id" value="${lista.id}"/>
							              	</display:column>		
							              	<display:column class="hidden" headerClass="hidden">
										    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
						           		   	</display:column>					  	
										  	<display:column property="codigo" titleKey="formularioListaPrecios.filtro.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
										  	<display:column property="descripcion" titleKey="formularioListaPrecios.filtro.descripcion" sortable="true" class="celdaAlineadoIzquierda"/>										  	
										  	<display:column class="celdaAlineadoCentrado">
									  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
											</display:column>	
										</display:table> 
									</div>
									<div style="width: 100%" align ="right">
										<sec:authorize ifAllGranted="ROLE_AJUSTAR_LISTA_DE_PRECIOS">
											<button name="agregar" type="button" onclick="agregar();" class="botonCentrado">
												<img src="<%=request.getContextPath()%>/images/add.png" > 
												<spring:message code="botones.agregar" htmlEscape="true"/>  
											</button>										
										</sec:authorize>
									</div>
								</fieldset>
							</td>
							<td style="width: 75%; min-width: 75%; vertical-align: top;">
								<fieldset style="min-height: 200px;">
									<table width="100%;">
						            	<thead>
							            	<tr>
							              		<th align="left">						  
									        		<font style="color:#003090">
									        			<spring:message code="formularioListaPrecios.tablaB.titulo" htmlEscape="true"/>
									        		</font>						        						 
							              		</th>
										 	</tr>
										</thead>
									</table>
									<div class="displayTagDiv" style="text-align: center; height: 80%;">
										<display:table name="listaSel.detalle" id="detalle" requestURI="mostrarListaPrecios.html" pagesize="10" sort="list" keepStatus="true" defaultsort="1">
											<display:column class="hidden" headerClass="hidden">
											    <input type="hidden" id="hdn_id" value="${detalle.id}"/>
							              	</display:column>		
							              	<display:column class="hidden" headerClass="hidden">
										    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="formularioAgregarConcepto.manesaje.deasociarConcepto" htmlEscape="true"/>"/>
						           		   	</display:column>					  	
										  	<display:column property="conceptoFacturable.codigo" titleKey="formularioListaPrecios.tablaB.codigo" sortable="true" class="celdaAlineadoIzquierda" />
										  	<display:column property="conceptoFacturable.descripcion" titleKey="formularioListaPrecios.tablaB.descripcion" sortable="true" class="celdaAlineadoIzquierda"/>
										  	<display:column titleKey="formularioListaPrecios.tablaB.costo" sortable="true" class="celdaAlineadoDerecha">
										  		<fmt:formatNumber value="${detalle.conceptoFacturable.costo}"></fmt:formatNumber>
										  	</display:column>
										  	<display:column titleKey="formularioListaPrecios.tablaB.precioBase" sortable="true" class="celdaAlineadoDerecha">
										  		<fmt:formatNumber value="${detalle.conceptoFacturable.precioBase}"></fmt:formatNumber>
										  	</display:column>
										  	<display:column property="tipoVariacion.descripcion" titleKey="formularioListaPrecios.tablaB.variacion" sortable="true" class="celdaAlineadoIzquierda"/>										  	
										  	<display:column titleKey="formularioListaPrecios.tablaB.valor" sortable="true" class="celdaAlineadoDerecha">
										  		<fmt:formatNumber value="${detalle.valor}"></fmt:formatNumber>
										  	</display:column>										  	
										  	<display:column titleKey="formularioListaPrecios.tablaB.monto" sortable="true" class="celdaAlineadoDerecha">
										  		<fmt:formatNumber value="${detalle.calcularMonto}"></fmt:formatNumber>
										  	</display:column>										  	
										  	<display:column class="celdaAlineadoCentrado">
									  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
											</display:column>	
										</display:table>
									</div>
									<div style="width: 100%;" align ="right">
										<sec:authorize ifAllGranted="ROLE_AJUSTAR_LISTA_DE_PRECIOS">
											<button name="agregar" type="button" onclick="asociar('<spring:message code="notif.listaPrecios.seleccionPreviaAsocioacion" htmlEscape="true"/>');" class="botonCentrado">
												<img src="<%=request.getContextPath()%>/images/add.png" > 
												<spring:message code="botones.asociar" htmlEscape="true"/>  
											</button>
										</sec:authorize>
									</div> 
								</fieldset>	
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
				</fieldset>	
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
	</body>
</html>