<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<jsp:include page="styles.jsp"/>
	
	<title>Error - <spring:message code="general.empresa" htmlEscape="true"/> <spring:message code="general.ambiente" htmlEscape="true"/></title>
	<script type="text/javascript">
		function volver(){
			document.location="../index.html";
		}
	</script>
	<style type="text/css">
		H1{
			color:blue;
		}
	</style>
</head>
<body>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="center">
			<br />
			<fieldset style="border: none; text-align: left;">
				<legend> </legend>
				<br />
				<br>
			<br>
			</fieldset>
			<table>
				<tr>
					<td valign="top" style="text-align: left;">
						<fieldset>
							<table width="100%">
								<thead>
									<tr>
										<th align="center" id="agenciaImg">
										<font style="color: #003090; font-size: 20px;"><c:if test="${message != null}">
								
									<spring:message code="${message}" htmlEscape="true" />
								
							</c:if>
							<c:out value="${exception}" /> </font>
										</th>
									</tr>
								</thead>
							</table>
							<div align="center" style="overflow: visible;">	
							<br />
							<center>
								<img
									src="<%=request.getContextPath()%>/images/mensajes/stop.png" />
							</center>
							<div align="center">
								<br>
								<button name="volver_atras" type="button" onclick="volver();"
									class="botonCentrado">
									<img src="<%=request.getContextPath()%>/images/volver4.png" />
									<spring:message code="error.volver" htmlEscape="true" />
								</button>
								<br>
								<br>
								</div>
							</div>
						</fieldset>
					</td>
				</tr>
			</table>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
</body>
</html>