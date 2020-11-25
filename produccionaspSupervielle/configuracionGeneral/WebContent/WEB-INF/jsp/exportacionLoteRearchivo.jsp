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
		<spring:message code="formularioLoteExportacionRearchivo.titulo.exportar" htmlEscape="true" />
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
	<script type="text/javascript" src="js/exportacion_lote_rearchivo.js"></script>
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
						<spring:message code="formularioLoteExportacionRearchivo.titulo.exportar"	htmlEscape="true" />
					</font>
				</legend>
				<br />
				<form:form action="exportarLoteExportacionRearchivo.html" method="post" id="exportarLoteExportacionRearchivo">
					<input type="hidden" id="id" name="id" value="<c:out value="${loteExportacionRearchivo.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="accion" name="accion" value=""/>
					
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioLoteExportacionRearchivo.datosGenerales" htmlEscape="true" />
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
							<table style="width: 100%;">
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.codigo" htmlEscape="true"/>
									</td>									
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.fecha" htmlEscape="true"/>
									</td>	
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.empresa" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.sucursal" htmlEscape="true"/>
									</td>									
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigo" name="codigo" style="width: 50px;" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.id}" default="" />"
										/>
									</td>										
									<td class="texto_ti">
										<input type="text" id="fechaRegistro" name="fechaRegistro" 
											style="width: 80px;" class="requerido" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.fechaRegistroStr}" default="" />"
										/>
									</td>
									<td class="texto_ti">
										<input type="text" id="codigoEmpresa" name="codigoEmpresa" 
											style="width: 50px;" class="requerido" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.empresa.codigo}" default="" />"
										/>
										&nbsp;&nbsp;
										<label id="codigoEmpresaLabel" for="codigoEmpresa"> 
											<c:out value="${loteExportacionRearchivo.empresa.descripcion}" default=""/>										 
										</label>
									</td>										
									<td class="texto_ti">
										<input type="text" id="codigoSucursal" name="codigoSucursal" 
											style="width: 50px;" class="requerido" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.sucursal.codigo}" default="" />"
										/>
										&nbsp;&nbsp;
										<label id="codigoSucursalLabel" for="codigoSucursal"> 
											 <c:out value="${loteExportacionRearchivo.sucursal.descripcion}" default="" />
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.cliente" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.clasificacionDocumental" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.estado" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.sizeMaxArchivo" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigoCliente" name="codigoCliente" 
											style="width: 50px;" class="requerido" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.clienteEmp.codigo}" default="" />"
										/>
										&nbsp;&nbsp; 
										<label id="codigoClienteLabel" for="codigoCliente"> 
											 <c:out value="${loteExportacionRearchivo.clienteEmp.razonSocialONombreYApellido}" default="" />
										</label>
									</td>
									<td class="texto_ti">
										<input type="text" id="codigoClasificacionDocumental" name="codigoClasificacionDocumental" 
											style="width: 50px;" class="requerido" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.clasificacionDocumental.codigo}" default="" />"
										/>
										&nbsp;&nbsp; 
										<label id="codigoClasificacionDocumentalLabel" for="codigoClasificacionDocumental"> 
											 <c:out value="${loteExportacionRearchivo.clasificacionDocumental.nombre}" default="" />
										</label>
									</td>
									<td class="texto_ti">
										<input type="text" id="estado" name="estado" 
											style="width: 250px;" class="requerido" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.estado}" default="" />"
										/>
									</td>
									<td class="texto_ti">
										<input type="text" id="sizeMaxArchivo" name="sizeMaxArchivo" 
											style="width: 50px;" class="requerido" 
											title='<spring:message code="formularioLoteExportacionRearchivo.sizeMaxArchivoTitle" htmlEscape="true"/>'
											value="<c:out value="${loteExportacionRearchivo.sizeMaxArchivo}" default="" />"
										/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti" colspan="4">
										<spring:message code="formularioLoteExportacionRearchivo.descripcion" htmlEscape="true"/>
									</td>									
								</tr>
								<tr>
									<td class="texto_ti" colspan="4">
										<textarea id="descripcion" name="descripcion" readonly="readonly" cols="90" rows="3">${loteExportacionRearchivo.descripcion}</textarea>
									</td>									
								</tr>
							</table>
						</div>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="grupoImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioLoteExportacionRearchivo.lotesRearchivo" htmlEscape="true" />
										</font> 
										<img id="grupoImgSrcDown" src="images/skip_down.png"
											title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="grupoImgSrc" src="images/skip.png"
											style="DISPLAY: none"
											title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>								
							</thead>
							<tbody>
								<tr>
									<td>
										<div style="width: 100%" id="grupoDiv" align="center">
											<fieldset>
												<display:table name="links" id="link">
								           		   	<display:column titleKey="formularioLoteExportacionRearchivo.archivoExportar" class="celdaAlineadoIzquierda">
								           		   		<a href="${link.link}">${link.label}</a>
								           		   	</display:column>
												</display:table> 
											</fieldset>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<div align="center">
				<button name="exportar" type="button" class="botonCentrado" id="btnExportar" style="width:200px;">
					<img src="<%=request.getContextPath()%>/images/print.png">
					<spring:message code="botones.exportar" htmlEscape="true" />
				</button>
				&nbsp;
				<button name="volver_atras" type="button" id="btnVolver" class="botonCentrado">
					<img src="<%=request.getContextPath()%>/images/volver4.png">
					<spring:message code="botones.volver" htmlEscape="true" />
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

