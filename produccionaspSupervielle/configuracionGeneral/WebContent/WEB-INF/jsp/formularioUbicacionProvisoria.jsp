<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<jsp:include page="styles.jsp" />
	<title>
		Rearchivar - Ubicacion Provisoria
	</title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
	<script type="text/javascript" src="js/httprequest.js"></script>
	<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
	<script type="text/javascript" src="js/Utils.js"></script>
	<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
	<script type="text/javascript" src="js/jquery.alerts.js"></script>
	<script type="text/javascript" src="js/ini.js"></script>
	<script type="text/javascript" src="js/busquedaHelper.js"></script>
	<script type="text/javascript" src="js/formulario_ubicacion_provisoria.js"></script>
	<style type="text/css">
	.cascade-loading {
		background: transparent
			url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
			center;
	}
	</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<form:form action="guardarUbicacionProvisoria.html" method="post"
							 commandName="ubicacionProvisoriaForm" id="ubicacionProvisoriaForm"> 
					
					<fieldset>
						<legend>
							<font size="1">
								Rearchivar
							</font>
						</legend>
							
						<div style="background-color: #f1e87d; WIDTH: auto;" align="center">
							<table class="busqueda" style="width: 100%; background-color: white;">
								<tr>
									<td class="texto_ti">
										Lectura
									</td>
									<td class="texto_ti">
										Ubicación Provisoria
									</td>										
									<td class="texto_ti">
										 
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigoLectura"
										name="codigoLectura" maxlength="6" style="width: 50px;"
										value="<c:out value="${codigoLectura}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" id="botonPopupLectura"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button> &nbsp;&nbsp; <label id="codigoLecturaLabel"
										for="codigoLectura"> <c:out value="${descripcion}"
												default="" />
									</label>
									</td>
									<td class="texto_ti">
										<input type="text" id="ubicacionProvisoria"
										name="ubicacionProvisoria" maxlength="255" style="width: 100px;"
										value="<c:out value="${ubicacionProvisoria}" default="" />" />
									</td>	
									<td class="texto_ti">
										<button name="guardar" type="button" class="botonCentrado" id="btnGuardar" onclick="document.forms[0].submit()">
											<img src="<%=request.getContextPath()%>/images/ok.png">
											Rearchivar
										</button>
									</td>							
								</tr>
							</table>
						</div>	
					</fieldset>
				</form:form>	
				<br style="font-size: xx-small;"/>
				<br style="font-size: xx-small;"/>
			</fieldset>
			<br style="font-size: xx-small;" />
			
			<div align="center">
				<button name="cancelar" type="button" id="btnCancelar" class="botonCentrado">
					<img src="<%=request.getContextPath()%>/images/cancelar.png">
					<spring:message code="botones.cancelar" htmlEscape="true" />
				</button>
			</div>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight" style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>	
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
	<div id="pop" style="display:none">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
	</div>
	<div style="display:none">
		<input type="text" id="triggerBlurAfterSelectItemInPopUp"/>
		<input type="text" id="contenedor"/>
		<input type="text" id="codigoContenedor"/>
	</div>
</body>
</html>