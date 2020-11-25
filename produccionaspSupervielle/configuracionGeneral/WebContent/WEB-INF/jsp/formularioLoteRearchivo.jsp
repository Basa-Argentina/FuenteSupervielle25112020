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
			<spring:message code="formularioLoteRearchivo.titulo.registar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'MODIFICACION'}">
			<spring:message code="formularioLoteRearchivo.titulo.modificar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'CONSULTA'}">
			<spring:message code="formularioLoteRearchivo.titulo.consultar"
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
	<script type="text/javascript" src="js/formulario_lote_rearchivo.js"></script>
	<style type="text/css">
	.cascade-loading {
		background: transparent
			url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
			center;
	}
	</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});seleccionarCombos();">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							<spring:message code="formularioLoteRearchivo.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioLoteRearchivo.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioLoteRearchivo.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarLoteRearchivo.html"
					commandName="loteRearchivo" method="post"
					modelAttribute="loteRearchivo">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${loteRearchivo.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
					<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
					<fieldset style="height: 1500px;">
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioLoteRearchivo.datosGenerales" htmlEscape="true" />
									</font> <img id="busquedaImgSrcDown" src="images/skip_down.png"
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
										<spring:message code="formularioLoteRearchivo.codigo" htmlEscape="true"/>
									</td>									
									<td class="texto_ti">
										<spring:message code="formularioLoteRearchivo.fecha" htmlEscape="true"/>
									</td>	
									<td class="texto_ti">
										<spring:message code="formularioLoteRearchivo.tipo" htmlEscape="true"/>
									</td>
									<td class="texto_ti" colspan="2">
										<spring:message code="formularioLoteRearchivo.remito" htmlEscape="true"/>
									</td>							
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigo" name="codigo" style="width: 50px;" readonly="readonly"
											value="<c:out value="${loteRearchivo.id}" default="" />"
											<c:if test="${accion == 'NUEVO'}">
												title="<spring:message code="formularioLoteRearchivo.codigoInfo" htmlEscape="true"/>"
											</c:if>
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										/>
										
									</td>										
									<td class="texto_ti">
										<input type="text" id="fechaRegistro" name="fechaRegistro" 
											style="width: 80px;" class="requerido"
											value="<c:out value="${loteRearchivo.fechaRegistroStr}" default="" />"
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
													'formname': 'loteRearchivo',
													// input name
													'controlname': 'fechaRegistro'
												});
											</script>
										</c:if>
									</td>
									<td class="texto_ti">
										<select id="tipo" 
											<c:if test="${accion != 'NUEVO'}">
												class="hiddenInput"
											</c:if>
											name="tipo" size="1"
											>
												<option label="Fisico" value="Fisico" 
													<c:if test="${loteRearchivo.tipo == 'Fisico'}">
															selected="selected"
													</c:if>>
													<spring:message code="formularioLoteRearchivo.tipo.fisico" htmlEscape="true"/>
												</option>
												<option label="Lote" value="Lote" 
													<c:if test="${loteRearchivo.tipo == 'Lote'}">
															selected="selected"
													</c:if>>
													<spring:message code="formularioLoteRearchivo.tipo.lote" htmlEscape="true"/>
												</option>
												<option label="Digital" value="Digital" 
													<c:if test="${loteRearchivo.tipo == 'Digital'}">
															selected="selected"
													</c:if>>
													<spring:message code="formularioLoteRearchivo.tipo.digital" htmlEscape="true"/>
												</option>	
												<option label="Electronico" value="Electronico" 
													<c:if test="${loteRearchivo.tipo == 'Electronico'}">
															selected="selected"
													</c:if>>
													Electronico
												</option>						
										</select>
										<input type="text" id="tipoAux" name="tipoAux" 
											<c:if test="${accion == 'NUEVO'}">
												class="hiddenInput"
											</c:if>
											
											style="width: 80px;border: none;text-align: left;font-weight: bold;" readonly="readonly" 
											value="<c:out value="${loteRearchivo.tipo}" default="" />"
											/>
									</td>
									
									<td class="texto_ti">
									   
										<input type="hidden" id="codigoRemito" name="codigoRemito" 
											style="width: 80px;" class="requerido"
											value="<c:out value="${loteRearchivo.remito.id}" default="" />"
											
											<c:if test="${accion == 'CONSULTA'}">
												readonly="readonly"
											</c:if>
											/>
										
										<button type="button" id="buscaRemito"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoRemitoLabel" for="codigoRemito"> 
											 <c:out value="${loteRearchivo.remito.letraYNumeroComprobante}" default="" />
										</label>
									</td>			
								</tr>
								<tr>
									<td class="texto_ti" colspan="2">
										<spring:message code="formularioLoteRearchivo.empresa" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteRearchivo.sucursal" htmlEscape="true"/>
									</td>										
									<td class="texto_ti">
										<spring:message code="formularioLoteRearchivo.cliente" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti" colspan="2">
										<input type="text" id="codigoEmpresa" name="codigoEmpresa" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${loteRearchivo.empresa.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO'}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaEmpresa"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO'}">
												disabled="disabled"
											</c:if>
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
											value="<c:out value="${loteRearchivo.sucursal.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO'}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaSucursal"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO'}">
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
											value="<c:out value="${loteRearchivo.clienteEmp.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO'}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaCliente" 
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO'}">
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
								<tr>
									<td class="texto_ti" colspan="6">
										<spring:message code="formularioLoteRearchivo.descripcion" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti" colspan="6">
										<textarea id="descripcion" name="descripcion" cols="105" rows="1"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										>${loteRearchivo.descripcion}</textarea>
									</td>
								</tr>
							</table>
						</div>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="grupoImg"><font
										style="color: #003090"> <spring:message
												code="formularioLoteRearchivo.rearchivos" htmlEscape="true" />
									</font> <img id="grupoImgSrcDown" src="images/skip_down.png"
										title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="grupoImgSrc" src="images/skip.png"
										style="DISPLAY: none"
										title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="width: 100%;" id="grupoDiv" align="center">
							<iframe src="seccionRearchivos.html?accion=${accion}" id="seccionRearchivos"
								width="100%" height="1250px" scrolling="no"
								frameborder="0" style="border: 0px solid #ffffff;">
							</iframe>
						</div>
					</fieldset>
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
					<button name="guardar" type="button" class="botonCentrado" id="btnGuardar" 
						
						>
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
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

