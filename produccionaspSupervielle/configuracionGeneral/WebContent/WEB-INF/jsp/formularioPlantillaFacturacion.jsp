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
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioPlantillaFacturacion.titulo.registrar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioPlantillaFacturacion.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioPlantillaFacturacion.titulo.consultar"
			htmlEscape="true" />
	</c:if></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_plantillaFacturacion.js"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div id="contenedorGeneral">
	
		<jsp:include page="innerMenu.jsp" />
		
		<div class="contenido" align="left">
		
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO' || accion == null}">
							<spring:message code="formularioPlantillaFacturacion.titulo.registrar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioPlantillaFacturacion.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioPlantillaFacturacion.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />

				<form:form name="formPlantillaFacturacion"
					action="guardarActualizarPlantillaFacturacion.html"
					commandName="plantillaFacturacionFormulario" method="post"
					modelAttribute="plantillaFacturacionFormulario">

					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${plantillaFacturacionFormulario.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa"
						value="<c:out value="${codigoEmpresa}" default="" />" />
					<input type="hidden" id="errorSeleccioneTipoComprobante" 
					name="errorSeleccioneTipoComprobante" 
					value="<spring:message code="formularioFactura.error.seleccioneAfipTipoComprobante" htmlEscape="true" />" />
							

					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioPlantillaFacturacion.datosPlantillaFacturacion"
												htmlEscape="true" /> </font> <img id="busquedaImgSrcDown"
										src="images/skip_down.png"
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
									<td>
										<fieldset>
											<table>
												<tr>
													<td class="texto_ti" style="padding-right: 180px;">
														<spring:message code="formularioPlantillaFacturacion.estado" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="checkbox" id="habilitado" name="habilitado" value="true"  
														<c:if test='${plantillaFacturacionFormulario.habilitado == true}'> checked="checked"</c:if>
														<c:if test="${accion == 'CONSULTA'}">disabled="disabled"</c:if>
														/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti" style="padding-right: 180px;">
														<spring:message code="formularioPlantillaFacturacion.cliente" htmlEscape="true"/>
													</td>
													<td class="texto_ti" style="padding-right: 180px;">
														<spring:message code="formularioPlantillaFacturacion.listaPrecio" htmlEscape="true"/>
													</td>
													<td class="texto_ti" style="padding-right: 180px;">
														<spring:message code="formularioPlantillaFacturacion.serie" htmlEscape="true"/>
													</td>
													<td class="texto_ti">
														<spring:message code="formularioPlantillaFacturacion.tipoComprobante" htmlEscape="true"/>
													</td>
													
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="clienteCodigo" name="clienteCodigo"
														maxlength="6" style="width: 50px;" class="requerido"
														value="<c:out value="${plantillaFacturacionFormulario.clienteCodigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
														</c:if> /> 
															
															<button type="button" name="buscaCliente" id="buscaCliente"
																	title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																	<c:if test="${accion == 'CONSULTA'}">
																							disabled="disabled"
																	</c:if>>
																	<img src="<%=request.getContextPath()%>/images/buscar.png">
																</button>&nbsp;&nbsp; 
															<label id="clienteCodigoLabel" for="clienteCodigo">
																
															</label>
														
													</td>
													<td class="texto_ti">
														<input type="text" id="listaPreciosCodigo" name="listaPreciosCodigo"
														maxlength="6" style="width: 50px;" class="requerido"
														value="<c:out value="${plantillaFacturacionFormulario.listaPreciosCodigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
														</c:if> /> 
															
															<button type="button" name="buscaListaPrecio" id="buscaListaPrecio"
																	title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																	<c:if test="${accion == 'CONSULTA'}">
																							disabled="disabled"
																	</c:if>>
																	<img src="<%=request.getContextPath()%>/images/buscar.png">
																</button>&nbsp;&nbsp; 
															<label id="listaPreciosCodigoLabel" for="listaPreciosCodigo">
																
															</label>
														
													</td>
													<td class="texto_ti">
														<input type="text" id="codigoSerie" name="codigoSerie"
														maxlength="6" style="width: 50px;" class="requerido"
														value="<c:out value="${plantillaFacturacionFormulario.codigoSerie}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
														</c:if> /> 
															
															<button type="button" name="buscaSerie" id="buscaSerie"
																	title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																	<c:if test="${accion == 'CONSULTA'}">
																							disabled="disabled"
																	</c:if>>
																	<img src="<%=request.getContextPath()%>/images/buscar.png">
																</button>&nbsp;&nbsp; 
															<label id="codigoSerieLabel" for="codigoSerie">
																
															</label>
														
													</td>
													<td class="texto_ti">
														<select id="tipoComprobanteId" name="tipoComprobanteId" <c:if test="${accion == 'CONSULTA'}">disabled="disabled"</c:if>>
															<option value="1" 
																<c:if test="${1 == plantillaFacturacionFormulario.tipoComprobanteId}">selected="selected"</c:if>
																<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>Facturas A</option>
															<option value="5" 
																<c:if test="${5 == plantillaFacturacionFormulario.tipoComprobanteId}">selected="selected"</c:if>
																<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>Facturas B</option>
														</select>
													</td>
													
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>

							</table>
						</div>
					</fieldset>
					<br style="font-size: xx-small;" />
					<fieldset>
						<legend>
								<spring:message code="formularioPlantillaFacturacion.detalles"
								htmlEscape="true" />
						</legend>
						
						<div style="width: 100%;" id="grupoDiv" align="center">
							<table style="width: 100%;">
								<tr>
									<td>
									<iframe id="frame" name="frame" 
										src="precargaFormularioPlantillaFacturacionDetalle.html?accion=${accion}&idPlantilla=${plantillaFacturacionFormulario.id}" 
										style="width: 100%; height: 400px; border-color: transparent;" ></iframe>
									</td>
								</tr>
							</table>
						</div>
					</fieldset>
					
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
						
					<button name="guardar" type="button" onclick="guardarYSalir();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
					<button name="cancelar" type="button" onclick="volverCancelar();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accion == 'CONSULTA'}">
				<div align="center">
					<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png"> 
						<spring:message code="botones.volver" htmlEscape="true"/>  
					</button>
				</div>						
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
	<div class="selectorDiv"></div>
</body>
</html>