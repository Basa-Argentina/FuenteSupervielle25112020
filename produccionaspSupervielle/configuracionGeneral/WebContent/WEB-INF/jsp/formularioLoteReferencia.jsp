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
	<title><c:if test="${accion == 'NUEVO'}">
			<spring:message code="formularioLoteReferencia.titulo.registar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'MODIFICACION'}">
			<spring:message code="formularioLoteReferencia.titulo.modificar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'CONSULTA'}">
			<spring:message code="formularioLoteReferencia.titulo.consultar"
				htmlEscape="true" />
		</c:if>
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
	<script type="text/javascript" src="js/formulario_lote_referencia.js"></script>
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
						<c:if test="${accion == 'NUEVO'}">
							<spring:message code="formularioLoteReferencia.titulo.registar"	htmlEscape="true" />
						</c:if> 
						<c:if test="${accion == 'MODIFICACION' || accion == 'MODIFICACIONRANGO'}">
							<spring:message code="formularioLoteReferencia.titulo.modificar" htmlEscape="true" />
						</c:if> 
						<c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioLoteReferencia.titulo.consultar" htmlEscape="true" />
						</c:if> 
						-
						<c:if test="${loteReferencia.indiceIndividual}">
							<spring:message code="formularioLoteReferencia.titulo.indiceIndividual" htmlEscape="true" />
						</c:if>
						<c:if test="${!loteReferencia.indiceIndividual}">
							<c:if test="${porRango !=null && porRango == true}">
								<spring:message code="formularioLoteReferencia.titulo.porRango" htmlEscape="true" />
							</c:if>
							<c:if test="${porRango == null || porRango == '' || porRango == false}">
								<spring:message code="formularioLoteReferencia.titulo.indiceGrupal" htmlEscape="true" />
							</c:if>
						</c:if>
					</font>
				</legend>
				<br />
				<form:form action="guardarActualizarLoteReferencia.html"
					commandName="loteReferencia" method="post"
					modelAttribute="loteReferencia">
					<input type="hidden" id="id" name="id" value="<c:out value="${loteReferencia.id}" default="" />" />
					<input type="hidden" id="codigo" name="codigo" value="<c:out value="${loteReferencia.codigo}" default="" />" />
					<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="indiceIndividual" name="indiceIndividual" value="<c:out value="${loteReferencia.indiceIndividual}" default="" />" />
					<input type="hidden" id="cargaPorRango" name="cargaPorRango" value="<c:out value="${loteReferencia.cargaPorRango}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="guardarContinuar" name="guardarContinuar" value="false" />
					<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
					<input type="hidden" id="codigoClasificacionDocumentalPadre" name="codigoClasificacionDocumentalPadre" value="${codigoClasificacionDocumentalPadre}" />
					<input type="hidden" id="bloqueoClasificacion" name="bloqueoClasificacion" value="${bloqueoClasificacion}" />
					<input type="hidden" id="codigoTipoPadre" name="codigoTipoPadre" value="${codigoTipoPadre}" />
					<input type="hidden" id="bloqueoTipo" name="bloqueoTipo" value="${bloqueoTipo}" />
					<input type="hidden" id="codigoContenedorPadre" name="codigoContenedorPadre" value="${codigoContenedorPadre}" />
					<input type="hidden" id="bloqueoContenedor" name="bloqueoContenedor" value="${bloqueoContenedor}" />
					<input type="hidden" id="codigoElementoPadre" name="codigoElementoPadre" value="${codigoElementoPadre}" />
					<input type="hidden" id="incrementoElemento" name="incrementoElemento" value="${incrementoElemento}" />
					<input type="hidden" id="codigoContenedorComparar" name="codigoContenedorComparar" value="${codigoContenedorComparar}" />
					<input type="hidden" id="bloqueoNumero1" name="bloqueoNumero1" value="${bloqueoNumero1}" />
					<input type="hidden" id="numero1Padre" name="numero1Padre" value="${numero1Padre}" />
					<input type="hidden" id="bloqueoTexto1" name="bloqueoTexto1" value="${bloqueoTexto1}" />
					<input type="hidden" id="texto1Padre" name="texto1Padre" value="${texto1Padre}" />
					<input type="hidden" id="bloqueoNumero2" name="bloqueoNumero2" value="${bloqueoNumero2}" />
					<input type="hidden" id="numero2Padre" name="numero2Padre" value="${numero2Padre}" />
					<input type="hidden" id="bloqueoTexto2" name="bloqueoTexto2" value="${bloqueoTexto2}" />
					<input type="hidden" id="texto2Padre" name="texto2Padre" value="${texto2Padre}" />
					
					<input type="hidden" id="refrescarIframe" name="refrescarIframe" value="" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioLoteReferencia.datosGenerales" htmlEscape="true" />
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
										<spring:message code="formularioLoteReferencia.codigo" htmlEscape="true"/>
									</td>									
									<td class="texto_ti">
										<spring:message code="formularioLoteReferencia.fecha" htmlEscape="true"/>
									</td>	
									<td class="texto_ti">
										&nbsp;
									</td>								
								</tr>
								<tr>
									<td class="texto_ti">
											<input type="text" id="codigo1" name="codigo1" style="width: 50px;" readonly="readonly"
											value="<c:out value="${loteReferencia.codigo}" default="" />"
											<c:if test="${accion == 'NUEVO'}">
												title="<spring:message code="formularioLoteReferencia.codigoInfo" htmlEscape="true"/>"
											</c:if>
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>/>
										
									</td>										
									<td class="texto_ti">
										<input type="text" id="fechaRegistro" name="fechaRegistro" 
											style="width: 80px;" class="requerido"
											value="<c:out value="${loteReferencia.fechaRegistroStr}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO'}">
												readonly="readonly"
											</c:if>
										/>
										<c:if test="${accion == 'NUEVO'}">
											<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'loteReferencia',
													// input name
													'controlname': 'fechaRegistro'
												});
											</script>
										</c:if>
									</td>
									<td class="texto_ti">
									<table>
										<tr>
											<td class="texto_ti">
												<input type="checkbox" id="chkLector" name="chkLector" value="true"> 
												<spring:message code="formularioLoteReferencia.elementoDesdeLector" htmlEscape="true"/>	
											</td>
										</tr>	
										<tr>										
											<td class="texto_ti">
												<input type="checkbox" id="chkLectorContenedor" name="chkLectorContenedor" value="true"> 
												<spring:message code="formularioLoteReferencia.contenedorDesdeLector" htmlEscape="true"/>	
											</td>										

										</tr>
									</table>	
										
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioLoteReferencia.empresa" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteReferencia.sucursal" htmlEscape="true"/>
									</td>										
									<td class="texto_ti">
										<spring:message code="formularioLoteReferencia.cliente" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigoEmpresa" name="codigoEmpresa" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${loteReferencia.empresa.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO' || (bloqueoEmpleado!=null && bloqueoEmpleado)}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaEmpresa"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO' || (bloqueoEmpleado!=null && bloqueoEmpleado)}">
												disabled="disabled"
											</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoEmpresaLabel" for="codigoEmpresa"> 
											 
										</label>
									</td>										
									<td class="texto_ti">
										<input type="text" id="codigoSucursal" name="codigoSucursal" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${loteReferencia.sucursal.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO' || (bloqueoEmpleado!=null && bloqueoEmpleado)}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaSucursal"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO' || (bloqueoEmpleado!=null && bloqueoEmpleado)}">
												disabled="disabled"
											</c:if>
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
											value="<c:out value="${loteReferencia.clienteEmp.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO' || (bloqueoEmpleado!=null && bloqueoEmpleado)}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaCliente" 
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO' || (bloqueoEmpleado!=null && bloqueoEmpleado)}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoClienteLabel" for="codigoCliente"> 
											 
										</label>
									</td>
								</tr>
							</table>
						</div>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="grupoImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioLoteReferencia.referencias" htmlEscape="true" />
										</font> 
										<img id="grupoImgSrcDown" src="images/skip_down.png"
											title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="grupoImgSrc" src="images/skip.png"
											style="DISPLAY: none"
											title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="width: 100%;" id="grupoDiv" align="center">
							<iframe id="seccionReferencias" 
								src="seccionReferencias.html?accion=${accion}&indiceIndividual=${loteReferencia.indiceIndividual}&porRango=${porRango}" 
								width="100%" height="650px" scrolling="si" frameborder="0" style="border: 0px solid #ffffff;">
							</iframe>
						</div>
					</fieldset>
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
					<button name="guardar" type="button" class="botonCentrado" id="btnGuardar">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
					<c:if test="${porRango==null || porRango == '' || porRango == false}">
						<button name="guardar" type="button" class="botonCentrado" id="btnGuardarContinuar" style="width:200px;">
							<img src="<%=request.getContextPath()%>/images/ok.png">
							<spring:message code="botones.guardarContinuar" htmlEscape="true" />
						</button>
						&nbsp;
					</c:if>
					<button name="cancelar" type="button" id="btnCancelar" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accion == 'CONSULTA'}">
				<div align="center">
					<button name="volver_atras" type="button" id="btnVolver" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight" style="height: 200%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>	
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
	<div id="pop" style="display:none">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><font style="font-size: 20px; color: Navy; background-color: white; border-color: Navy; border-width: 2px; border: solid;"><spring:message code="textos.espere" htmlEscape="true"/></font></label>	     
	</div>
</body>
</html>

