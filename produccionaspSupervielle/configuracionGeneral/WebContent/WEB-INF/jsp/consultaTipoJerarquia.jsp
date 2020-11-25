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
			<spring:message code="formularioTipoJerarquia.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_tipo_jerarquia.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2-original.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg}); buscarAsociacionesOnBodyLoad('${tipoSel.id}');" >
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
			        		<spring:message code="formularioTipoJerarquia.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarTipoJerarquia.html" commandName="tipoJerarquiaBusqueda" method="post">
						 <input type="hidden" id="hdn_idTipoJerarquia" name="hdn_idTipoJerarquia"
						 	value="<c:out value="${hdn_idTipoJerarquia}" default="" />" />
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.buscar" htmlEscape="true"/>&nbsp;<spring:message code="formularioTipoJerarquia.titulo" htmlEscape="true"/>
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
											<spring:message code="formularioTipoJerarquia.filtro.codigo" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<input type="text" id="codigo" name="codigo" style="width: 150px;"
												value="<c:out value="${tipoJerarquiaBusqueda.codigo}" default="" />"/>
										</td>
										<td class="texto_td">
											<spring:message code="formularioTipoJerarquia.filtro.descripcion" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<input type="text" id="descripcion" name="descripcion" style="width: 150px;"
												value="<c:out value="${tipoJerarquiaBusqueda.descripcion}" default="" />"/>
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
							<td style="width: 40%; min-width: 40%; vertical-align: top;">
								<fieldset style="min-height: 200px;">
									<table width="100%">
						            	<thead>
							            	<tr>
							              		<th align="left">						  
									        		<font style="color:#003090">
									        			<spring:message code="formularioTipoJerarquia.tabla.tipoJerarquia" htmlEscape="true"/>
									        		</font>						        						 
							              		</th>
										 	</tr>
										</thead>
									</table>
									<div style="text-align: center;">
										<display:table name="tipos" id="tipo" requestURI="mostrarTipoJerarquia.html" pagesize="10" sort="list" keepStatus="true">											
											<display:column class="hidden" headerClass="hidden">
											    <input type="hidden" id="hdn_id" value="${tipo.id}"/>
							              	</display:column>		
							              	<display:column class="hidden" headerClass="hidden">
										    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
						           		   	</display:column>					  	
										  	<display:column property="codigo" titleKey="formularioTipoJerarquia.filtro.codigo" sortable="true" class="celdaAlineadoIzquierda upperCase"/>
										  	<display:column property="descripcion" titleKey="formularioTipoJerarquia.filtro.descripcion" sortable="true" class="celdaAlineadoIzquierda"/>										  	
										  	<display:column property="observacion" titleKey="formularioTipoJerarquia.tabla.observacion" sortable="true" class="celdaAlineadoIzquierda"/>										  	
										  	<display:column class="celdaAlineadoCentrado">
									  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
											</display:column>	
										</display:table> 
									</div>
									<div style="width: 100%" align ="right">
										<button name="agregar" type="button" onclick="agregar();" class="botonCentrado">
											<img src="<%=request.getContextPath()%>/images/add.png" > 
											<spring:message code="botones.agregar" htmlEscape="true"/>  
										</button>
									</div>
								</fieldset>
							</td>
							<td style="width: 60%; min-width: 60%; vertical-align: top;">
								<fieldset style="min-height: 200px;">
									<div class="displayTagDiv" style="text-align: center; height: 80%;">
										<table width="100%;">
							            	<thead>
								            	<tr>
								              		<th align="left">						  
										        		<font style="color:#003090">
										        			<spring:message code="formularioTipoJerarquia.tablaB.titulo" htmlEscape="true"/>&nbsp;<c:out value="${tipoSel.descripcion}"/>										        			
										        		</font>						        						 
								              		</th>
											 	</tr>
											</thead>
										</table>									
										<display:table name="tipoSel.jerarquias" id="jerarquia" requestURI="mostrarTipoJerarquia.html" pagesize="10" sort="list" keepStatus="true">
											<display:column class="hidden" headerClass="hidden">
											    <input type="hidden" id="hdn_id" value="${jerarquia.id}"/>
							              	</display:column>		
							              	<display:column class="hidden" headerClass="hidden">
										    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="formularioJerarquia.confirmacion.eliminarJerarquia" htmlEscape="true"/>"/>
						           		   	</display:column>										  	
										  	<display:column property="descripcion" titleKey="formularioListaPrecios.tablaB.descripcion" sortable="true" class="celdaAlineadoIzquierda"/>										  							  	
										  	<display:column property="valoracion" titleKey="formularioTipoJerarquia.tablaB.valoracion" sortable="true" class="celdaAlineadoDerecha"/>										  							  	
										  	<display:column titleKey="formularioJerarquia.datos.desde" sortable="true" class="celdaAlineadoDerecha">
										  		<c:out value="${jerarquia.coordenadasDesde}"/>
										  	</display:column>										  							  	
										  	<display:column  titleKey="formularioJerarquia.datos.hasta" sortable="true" class="celdaAlineadoDerecha">
										  		<c:out value="${jerarquia.coordenadasHasta}"/>
										  	</display:column>										  							  	
										  	<display:column property="observacion" titleKey="formularioTipoJerarquia.tablaB.observacion" sortable="true" class="celdaAlineadoIzquierda"/>										  							  	
										  	<display:column class="celdaAlineadoCentrado">
									  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
											</display:column>	
										</display:table>
									</div>
									<div style="width: 100%;" align ="right">
										<button name="agregar" type="button"  class="botonCentrado"
											onclick="agregarJerarquia('<spring:message code="notif.tipoJerarquia.seleccionPreviaAsocioacion" htmlEscape="true"/>'
																	, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');">
											<img src="<%=request.getContextPath()%>/images/add.png" > 
											<spring:message code="botones.agregar" htmlEscape="true"/>  
										</button>
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