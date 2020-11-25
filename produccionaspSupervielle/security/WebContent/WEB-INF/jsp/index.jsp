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
   	<script language="JavaScript" src="js/mavalos_index.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/ini.js" type="text/javascript"></script>
</head>
<body onload="mostrarErrores(${errores});">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp"/>
		<div class="contenido" align="center">
			<br/>
			<fieldset style="border: none; text-align: left;">
				<legend>
		        	<font class="tituloForm" size="5">
		        		<sec:authorize ifAnyGranted="ROLE_ASP_ADMIN">
		        			<spring:message code="bienvenidoAsp" htmlEscape="true"/>
		        		</sec:authorize>		        		
		        		<sec:authorize ifAnyGranted="ROLE_MOD_SEGURIDAD">
		        			<spring:message code="bienvenido" htmlEscape="true"/>
		        		</sec:authorize>		        		
	        		</font>		  
				</legend>
				<br/>							
			</fieldset>
			<table style="width: 100%; height: 430px;">
				<tr>
					<td style="text-align: right; vertical-align: bottom;">						
<!-- 						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado"> -->
<%-- 							<img src="<%=request.getContextPath()%>/images/volver4.png">  --%>
<%-- 							<spring:message code="botones.volver" htmlEscape="true"/>   --%>
<!-- 						</button> -->
					</td>
				</tr>
			</table>			
		</div>		
		<jsp:include page="footer.jsp"/>
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="fieldErrors.jsp"/>	
</body>
</html>
