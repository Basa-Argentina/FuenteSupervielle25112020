<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page errorPage="/error.html" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>		
		<spring:message code="formularioRestorePassword.tituloForm" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> <spring:message code="general.ambiente" htmlEscape="true"/>
	</title>
		
	<jsp:include page="styles.jsp"/>

	 <script type="text/javascript" src="js/jquery-1.5.js"></script>  
	 <script language="JavaScript" src="js/mavalos_jquery.tools.min.js" type="text/javascript"></script>
	 <script language="JavaScript" src="js/mavalos_formulario_restore_password.js" type="text/javascript"></script>
	 <script language="JavaScript" src="js/ini.js" type="text/javascript"></script>
	 <meta content="MSHTML 8.00.6001.18852" name="GENERATOR">
</head>
<body onload="mostrarErrores(${errores});">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp"/>
		<div class="contenido" align="center">
			<br>
			<fieldset style="border: none;">
				<legend align="left">
		        	<font class="tituloForm" size="5">
		        		<spring:message code="formularioRestorePassword.tituloForm" htmlEscape="true"/> 
	        		</font>		  
				</legend>
			</fieldset>
			
			<br style="line-height:0;font-size: 15px;">
			<br style="line-height:0;font-size: 15px;">
			<table>
				<tr>
					<td>
						<form:form action="enviarRestorePassword.html" commandName="restorePasswordForm" method="post" name="restorePassForm">
							<fieldset>
								<table width="100%">
									<thead>
										<tr>
											<th  align="left" id="caractImg">
												<font style="color:#003090"><spring:message code="persona.mail" htmlEscape="true"/></font>
					       					</th>
				       					</tr>
				       					</thead>
		       					</table>
		       					<div class="seccion3">
									<table style="width:300px;">									
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
										<tr>
											<td class="texto_label" colspan="2">
												<spring:message code="persona.mail" htmlEscape="true"/>:
											</td>
											
										</tr>	
										<tr>
											<td class="texto_field">
												<input type="text" id="persona.mail" name="persona.mail" style="width: 95%;"
														value="<c:out value="${restorePasswordFormulario.persona.mail}" default="" />"
												/>
											</td>
											<td width="1%" class="error">
												*
											</td>											
										</tr>		
									</table>
								</div>
							</fieldset>
						</form:form>
					</td>
				</tr>
			</table>
			<br style="line-height:0;font-size: 15px;">
			<div align="center">
				<button name="guardar" type="button" onclick="enviar()" class="botonCentrado"> <img src="<%=request.getContextPath()%>/images/ok.png"> 
					<spring:message code="botones.enviar" htmlEscape="true"/>  
				</button>
				&nbsp;
				<button name="cancelar" type="button"  onclick="volverCancelar();" class="botonCentrado"><img src="<%=request.getContextPath()%>/images/cancelar.png"> 
					<spring:message code="botones.cancelar" htmlEscape="true"/>  
				</button>						
			</div>
			<br style="font-size: xx-small;"/>
		</div>
		<jsp:include page="footer.jsp"/>	
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="fieldErrors.jsp"/>
</body>
</html>