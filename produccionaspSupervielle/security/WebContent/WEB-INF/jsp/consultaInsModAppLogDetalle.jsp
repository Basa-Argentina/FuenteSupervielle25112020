<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page errorPage="/error.html" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<jsp:include page="styles.jsp"/>
	<style type="text/css">
		input[type="text"] {
			width: 500px;
		}
	</style>
	<link rel="stylesheet" href="css/popup.css" type="text/css"/>
	<title>
		<spring:message code="consultaInsModAppLogDetalle.titulo" htmlEscape="true" /> - <spring:message code="general.empresa" htmlEscape="true"/> (<spring:message code="general.ambiente" htmlEscape="true"/>)
	</title>	
		<script>
			 function volver(){
				close();
			 }
		 </script>	
	</head>	
	<body style="border: none;">
		<div>
			<br style="font-size: xx-small;"/>
			<fieldset>
				<table width="100%">
	            	<thead>
		            	<tr>
		              		<th align="left" id="busquedaImg" >						  
				        		<font style="color:#003090">
				        			<spring:message code="consultaInsModAppLogDetalle.titulo" htmlEscape="true" />
				        		</font>				        							 
		              		</th>
					 	</tr>
					</thead>
				</table>
				<table class="seccion">
					<tr>
						<td>
						<table style="width: 600px;">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td class="texto_td"><spring:message
									code="consultaInsModAppLogDetalle.nivel" htmlEscape="true" />:</td>
								<td><input type="text"
									value="<c:out value="${insModLog.nivel}" default="" />"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="texto_td"><spring:message
									code="consultaInsModAppLogDetalle.fecha" htmlEscape="true" />:</td>
								<td><input type="text"
									value="<c:out value="${insModLog.fechaHoraStr}" default="" />"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="texto_td"><spring:message
									code="consultaInsModAppLogDetalle.mensaje" htmlEscape="true" />:</td>
								<td><input type="text"
									value="<c:out value="${insModLog.mensaje}" default="" />"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="texto_td"><spring:message
									code="consultaInsModAppLogDetalle.clase" htmlEscape="true" />:</td>
								<td><input type="text"
									value="<c:out value="${insModLog.clase}" default="" />"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="texto_td"><spring:message
									code="consultaInsModAppLogDetalle.lineaReferencia"
									htmlEscape="true" />:</td>
								<td><input type="text"
									value="<c:out value="${insModLog.lineaReferencia}" default="" />"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="texto_td"><spring:message
									code="consultaInsModAppLogDetalle.excepcion" htmlEscape="true" />:
								</td>
								<td><textarea cols="80" rows="10" readonly="readonly"><c:out
									value="${insModLog.excepcion}" default="" /></textarea></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				<br />
				<div align="center">
				<button name="volver_atras" type="button" onclick="volver();"
					class="botonCentrado"><img
					src="<%=request.getContextPath()%>/images/volver4.png" /> <spring:message
					code="botones.cerrar" htmlEscape="true" /></button>
				</div>
			</fieldset>
		</div>
	</body>
</html>
