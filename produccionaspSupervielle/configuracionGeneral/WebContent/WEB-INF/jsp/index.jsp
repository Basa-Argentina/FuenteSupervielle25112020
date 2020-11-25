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
	<title><spring:message code="general.empresa" htmlEscape="true"/> <spring:message code="general.ambiente" htmlEscape="true"/></title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
    <script language="JavaScript" src="js/mavalos_jquery.tools.min.js" type="text/javascript"></script>
   	<script language="JavaScript" src="js/mavalos_index.js" type="text/javascript"></script>   	
    <script language="JavaScript" src="js/ini.js" type="text/javascript"></script>
</head>
<script>
	
</script>
<body onload="mostrarErrores(${errores});">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp"/>
		<div class="contenido" align="center">
			<br/>
			<fieldset style="border: none; text-align: left;">
				<legend>
		        	<font id="fontTituloForm" class="tituloForm" size="5">
		        		<input type="hidden" id="cfgmod" name="cfgmod"
								value="<spring:message code="configuracionGeneral" htmlEscape="true"/>"/>
		        		<input type="hidden" id="depmod" name="depmod"
								value="<spring:message code="gestionDeposito" htmlEscape="true"/>"/>
		        		<input type="hidden" id="cccmod" name="cccmod"
								value="<spring:message code="clienteCuentasPorCobrar" htmlEscape="true"/>"/>
						<input type="hidden" id="reqmod" name="reqmod"
								value="<spring:message code="gestionRequerimientos" htmlEscape="true"/>"/>		        		
		        		<input type="hidden" id="stockmod" name="stockmod"
								value="<spring:message code="gestionStock" htmlEscape="true"/>"/>		        		
	        		</font>		  
				</legend>
				<br/>							
			</fieldset>
		</div>
		<jsp:include page="footer.jsp"/>
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="fieldErrors.jsp"/>	
</body>
</html>
