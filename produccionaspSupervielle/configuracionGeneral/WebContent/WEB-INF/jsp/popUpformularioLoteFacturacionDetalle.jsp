<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
	<title> 
	</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/popUp_loteFacturacionDetalle.js"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		
		<script type="text/javascript" src="js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
		<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
		<link rel="stylesheet" type="text/css" href="js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})" style="background-color: #1584D6; width: 700px; padding: 10px 0px 0px 0px;">
	<div id="divContenedorFB" align="center" style="width:700px; min-height:432px;margin:0px;padding:0px;background-color: #FFF;" align="left">
		<br style="font-size: medium;" />
		<fieldset style="border: none; text-align: left; width: 97%;">
			<legend>
				<font class="tituloForm" size="5">
					 <c:if	test="${accion == 'NUEVO'}">
						<spring:message code="formularioFacturaDetalle.titulo.registrar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'MODIFICACION'}">
						<spring:message code="formularioFacturaDetalle.titulo.modificar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'CONSULTA'}">
						<spring:message code="formularioFacturaDetalle.titulo.consultar"
							htmlEscape="true" />
					</c:if> </font>
			</legend>
			<br />
			<form:form name="formFacturaDetalle" action="guardarActualizarFacturaDetalle.html" commandName="facturaDetalleFormulario" method="post"
					modelAttribute="facturaDetalleFormulario">
				<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />" />
				<input type="hidden" id="id" name="id" value="<c:out value="${id}" default="" />" />
				<input type="hidden" id="clienteAspId" name="clienteAspId" value="<c:out value="${clienteAspId}" default="" />" />
				<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />" />
				<input type="hidden" id="codigoSucursal" name="codigoSucursal" value="<c:out value="${codigoSucursal}" default="" />" />
				<input type="hidden" id="periodo" name="periodo" value="<c:out value="${periodo}" default="" />" />
			</form:form>
		</fieldset>
	</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
<div class="selectorDiv"></div>
</body>
</html>