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
		<spring:message code="formularioTransferenciaContenedores.titulo" htmlEscape="true" />
	</title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
	<script type="text/javascript" src="js/httprequest.js"></script>
	<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
	<script type="text/javascript" src="js/Utils.js"></script>
	<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
	<script type="text/javascript" src="js/jquery.alerts.js"></script>
	<script type="text/javascript" src="js/jquery.chromatable.js"></script>
	<script type="text/javascript" src="js/calendar_us.js"></script>
	<script type="text/javascript" src="js/ini.js"></script>
	<script type="text/javascript" src="js/busquedaHelper.js"></script>
	<script type="text/javascript" src="js/formulario_transferencia_contenedor.js"></script>
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
				<form:form action="ejecutarTransferenciaContenedor.html" method="post"
							 commandName="transferenciaContenedorForm" id="transferenciaContenedorForm"> 
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${transferenciaContenedorForm.clienteId}" default="" />"/>
					<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
					<fieldset>
						<legend>
							<font size="1">
								<spring:message code="formularioTransferenciaContenedores.titulo" htmlEscape="true"/>
							</font>
						</legend>
							
						<div style="background-color: #f1e87d; WIDTH: auto;" align="center">
							<table class="busqueda" style="width: 100%; background-color: white;">
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioTransferenciaContenedores.empresa" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioTransferenciaContenedores.sucursal" htmlEscape="true"/>
									</td>										
									<td class="texto_ti">
										<spring:message code="formularioTransferenciaContenedores.cliente" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigoEmpresa" name="codigoEmpresa" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${transferenciaContenedorForm.codigoEmpresa}" default="" />"
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaEmpresa"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoEmpresaLabel" for="codigoEmpresa"> 										 
										</label>
									</td>										
									<td class="texto_ti">
										<input type="text" id="codigoSucursal" name="codigoSucursal" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${transferenciaContenedorForm.codigoSucursal}" default="" />"
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaSucursal"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoSucursalLabel" for="codigoSucursal"> 
											 
										</label>
									</td>
									<td class="texto_ti">
										<input type="text" id="codigoCliente" name="codigoCliente" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${transferenciaContenedorForm.codigoCliente}" default="" />"
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaCliente" 
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoClienteLabel" for="codigoCliente"> 										 
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti" >
										<spring:message	code="formularioTransferenciaContenedores.contenedorOrigen" htmlEscape="true" />
									</td>	
									<td class="texto_ti" >
										<spring:message	code="formularioTransferenciaContenedores.contenedorDestino" htmlEscape="true" />
									</td>									
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigoContenedorOrigen" class="requerido"
												name="codigoContenedorOrigen" maxlength="12" style="width: 90px;" 
												value="<c:out value="${transferenciaContenedorForm.codigoContenedorOrigen}" default="" />"
										 />
										&nbsp;&nbsp;
										<button type="button" id="buscaContenedorOrigen"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoContenedorOrigenLabel" for="codigoContenedorOrigen">  </label>
									</td>	
									<td class="texto_ti">
										<input type="text" id="codigoContenedorDestino" class="requerido"
											name="codigoContenedorDestino" maxlength="12" style="width: 90px;" 
											value="<c:out value="${transferenciaContenedorForm.codigoContenedorDestino}" default="" />"
										 />
										&nbsp;&nbsp;
										<button type="button" id="buscaContenedorDestino"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoContenedorDestinoLabel" for="codigoContenedorDestino">  </label>
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
				<button name="guardar" type="button" class="botonCentrado" id="btnGuardar">
					<img src="<%=request.getContextPath()%>/images/ok.png">
					<spring:message code="botones.guardar" htmlEscape="true" />
				</button>
				&nbsp;
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