<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
			<spring:message code="formularioImportarDatos.titulo" htmlEscape="true" />
	</title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
	<script type="text/javascript" src="js/httprequest.js"></script>
	<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
	<script type="text/javascript" src="js/Utils.js"></script>
	<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
	<script type="text/javascript" src="js/jquery.alerts.js"></script>
	<script type="text/javascript" src="js/jquery.chromatable.js"></script>
	<script type="text/javascript" src="js/calendar_us.js"></script>
	<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
	<script type="text/javascript" src="js/ini.js"></script>
	<script type="text/javascript" src="js/busquedaHelper.js"></script>
	<script type="text/javascript" src="js/formulario_importar_datos.js"></script>
	
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
				<legend>
					<font class="tituloForm" size="5"> 
						<spring:message code="formularioImportarDatos.titulo"	htmlEscape="true" />
					</font>
				</legend>
				<br />
				<form:form id="importarDatos" action="importarDatos.html"
					commandName="importarDatos" method="post">
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioImportarDatos.importar" htmlEscape="true" />
										</font> 
										<img id="busquedaImgSrcDown" src="images/skip_down.png"
											title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
											style="DISPLAY: none"
											title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="width: 100%;" id="busquedaDiv" align="center">
							
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioImportarDatos.conexion" htmlEscape="true"/>:
										</td>
										<td class="texto_ti">
											<input name="stringConexion" id="stringConexion" type="text" width="100%"/>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<button name="guardar" type="submit" class="botonCentrado" id="btnGuardar" onclick="abrirPopup()">
													<img src="<%=request.getContextPath()%>/images/ok.png">
													<spring:message code="formularioImportarDatos.botones.importar" htmlEscape="true" />
											</button>
										</td>										
										
									</tr>
								</table>
							</div>	
					</fieldset>
				</form:form>
				
				
				<form:form id="renombrarExtencionDatos" action="renombrarExtencionDatos.html"
					commandName="renombrarExtencionDatos" method="post">
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioImportarDatos.modificarExtencion" htmlEscape="true" />
										</font> 
										<img id="busquedaImgSrcDown" src="images/skip_down.png"
											title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
											style="DISPLAY: none"
											title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="width: 100%;" id="busquedaDiv" align="center">
							
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioImportarDatos.path" htmlEscape="true"/>:
										</td>
										<td class="texto_ti">
											<input name="path" id="path" type="text" width="100%"/>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<button name="guardar" type="submit" class="botonCentrado" id="btnGuardar" onclick="abrirPopup()">
													<img src="<%=request.getContextPath()%>/images/ok.png">
													<spring:message code="formularioImportarDatos.botones.modificar" htmlEscape="true" />
											</button>
										</td>										
										
									</tr>
								</table>
							</div>	
					</fieldset>
				</form:form>
			</fieldset>
			
				<br style="font-size: xx-small;" />
				<div align="center">
						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/volver4.png"/> 
							<spring:message code="botones.cerrar" htmlEscape="true"/>  
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
	
</body>
</html>
