<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page buffer = "32kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			Consulta Tareas Asignadas
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_tarea.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>		
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>
		      <sec:authorize ifAnyGranted="ROLE_REFERENCIA_ASIGNAR_TAREA"> 	 
		      	<li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
		      </sec:authorize> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="formularioOperacion.finalizar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		Consulta Tareas Asignadas		        		
		        		</font>		  
					</legend>
					<br/>
						
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="tareas" id="tarea" requestURI="mostrarTarea.html" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${tarea.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.tarea.finalizar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column property="elemento.codigo" title="Etiqueta" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="elemento.contenedor.codigo" title="Caja" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="loteReferencia.codigo" title="Lote" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="elemento.clienteEmp.razonSocialONombreYApellido" title="Municipio" class="celdaAlineadoIzquierda" />
							<display:column property="clasificacionDocumental.nombre" titleKey="formularioLoteReferencia.listaRef.clasificacion" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="numero1" titleKey="formularioLoteReferencia.listaRef.numero1" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="fecha1Str" titleKey="formularioLoteReferencia.listaRef.fecha1" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="texto1" titleKey="formularioLoteReferencia.listaRef.texto1" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="descripcionTarea" maxLength="30" title="Desc. Tarea" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="estadoTarea" maxLength="30" title="Estado" sortable="true" class="celdaAlineadoIzquierda"/>						  							  							  	
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
<!-- 							<button name="agregar" type="button" onclick="agregar();" class="botonCentrado"> -->
<%-- 								<img src="<%=request.getContextPath()%>/images/add.png" >  --%>
<%-- 								<spring:message code="botones.agregar" htmlEscape="true"/>   --%>
<!-- 							</button> -->
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