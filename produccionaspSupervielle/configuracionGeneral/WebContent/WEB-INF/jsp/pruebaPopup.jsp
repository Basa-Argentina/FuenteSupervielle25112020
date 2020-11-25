<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
	<jsp:include page="styles.jsp"/>
	<title>SECMOD - <spring:message code="general.empresa" htmlEscape="true"/> <spring:message code="general.ambiente" htmlEscape="true"/></title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
    <script language="JavaScript" src="js/mavalos_jquery.tools.min.js" type="text/javascript"></script>
   	<script language="JavaScript" src="js/mavalos_prueba_popup.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/ini.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
</head>
<body>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp"/>
		<div class="contenido" align="center">
			<br/>
			<fieldset style="border: none; text-align: left;">
				<legend>
		        	<font class="tituloForm" size="5">
		        		<spring:message code="bienvenido" htmlEscape="true"/>		        		
	        		</font>		  
				</legend>
				<input id="usuario" style="width: 150px;">&nbsp;&nbsp; 
				<button onclick="abrirPopup('userPopupMap');">abrir popup</button>&nbsp;&nbsp; 
				<label id="usuarioLabel" for="usuario"></label>
				<br/>							
				<input id="usuario2" style="width: 150px;">&nbsp;&nbsp; 
				<button onclick="abrirPopup('userPopupMap2');">abrir popup</button>&nbsp;&nbsp; 
				<label id="usuario2Label" for="usuario2"></label>
				<br/>							
			</fieldset>
		</div>
		<jsp:include page="footer.jsp"/>
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="userPopupMap" /> 
		<jsp:param name="clase" value="userPopupMap" /> 
	</jsp:include>	
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="userPopupMap2" /> 
		<jsp:param name="clase" value="userPopupMap2" /> 
	</jsp:include>	
</body>
</html>
