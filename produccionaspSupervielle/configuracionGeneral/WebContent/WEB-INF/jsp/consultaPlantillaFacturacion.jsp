<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ page buffer = "32kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><spring:message
		code="formularioPlantillaFacturacion.titulo.consultar" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/busquedaHelper.js"></script>
<script type="text/javascript" src="js/mavalos_consulta_plantillaFacturacion.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		 <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
	    </ul> 
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioPlantillaFacturacion.titulo.consultar"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarPlantillaFacturacion.html"
					commandName="plantillaFacturacionBusqueda" method="post">
					
						<input type="hidden" id="codigoEmpresa" name="codigoEmpresa"
						value="<c:out value="${codigoEmpresa}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="errorSeleccioneTipoComprobante" 
					name="errorSeleccioneTipoComprobante" 
					value="<spring:message code="formularioFactura.error.seleccioneAfipTipoComprobante" htmlEscape="true" />" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="textos.buscar" htmlEscape="true" /> </font> <img
										id="busquedaImgSrcDown" src="images/skip_down.png"
										title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
										style="DISPLAY: none"
										title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"
							id="busquedaDiv" align="center">
							<table class="busqueda"
								style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioPlantillaFacturacion.cliente" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioPlantillaFacturacion.serie" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message	code="formularioPlantillaFacturacion.estado" htmlEscape="true" />
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="clienteCodigo" name="clienteCodigo"
												maxlength="6" style="width: 50px;"
												value="<c:out value="${plantillaFacturacionBusqueda.clienteCodigo}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
												</c:if> /> 
													<c:if test="${accion != 'MODIFICACION'}">
													<button type="button" name="buscaCliente" id="buscaCliente"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																					disabled="disabled"
															</c:if>>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; 
													<label id="clienteCodigoLabel" for="clienteCodigo">
														
													</label>
												</c:if>
										</td>
													
										<td class="texto_ti">
											<input type="text" id="codigoSerie" name="codigoSerie"
												maxlength="6" style="width: 50px;"
												value="<c:out value="${plantillaFacturacionBusqueda.codigoSerie}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
												</c:if> /> 
													<c:if test="${accion != 'MODIFICACION'}">
													<button type="button" name="buscaSerie" id="buscaSerie"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																					disabled="disabled"
															</c:if>>
															<img src="<%=request.getContextPath()%>/images/buscar.png">
														</button>&nbsp;&nbsp; 
													<label id="codigoSerieLabel" for="codigoSerie">
														
													</label>
												</c:if>
										</td>
										<td class="texto_ti">
											<select id="habilitado" name="habilitado">		
												<option value=""  
													<c:if test="${plantillaFacturacionBusqueda.habilitado == null}">selected="selected"</c:if>
													<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>TODOS</option>
													<option value=true  
													<c:if test="${plantillaFacturacionBusqueda.habilitado == true}">selected="selected"</c:if>
													<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>SI</option>
													<option value=false  
													<c:if test="${plantillaFacturacionBusqueda.habilitado == false}">selected="selected"</c:if>
													<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>NO</option>
											</select>
										</td>			
										</tr>
									<tr>
										<td class="texto_ti">
											<spring:message
												code="formularioPlantillaFacturacion.tipoComprobante" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioPlantillaFacturacion.listaPrecio" htmlEscape="true" />
										
										</td>
										<td rowspan="2" style="vertical-align: bottom;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
												<spring:message code="textos.buscar" htmlEscape="true" />
											</button>
										</td>	
									</tr>
									<tr>
										<td class="texto_ti">
											<select id="tipoComprobanteId" name="tipoComprobanteId">
												<option value="0"
													<c:if test="${0 == plantillaFacturacionBusqueda.tipoComprobanteId}">selected="selected"</c:if>
													<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>TODOS</option>
												<option value="1" 
													<c:if test="${1 == plantillaFacturacionBusqueda.tipoComprobanteId}">selected="selected"</c:if>
													<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>Facturas A</option>
												<option value="5" 
													<c:if test="${5 == plantillaFacturacionBusqueda.tipoComprobanteId}">selected="selected"</c:if>
													<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>Facturas B</option>
											</select>
													</td>										
													<td class="texto_ti">
														<input type="text" id="listaPreciosCodigo" name="listaPreciosCodigo"
														maxlength="6" style="width: 50px;"
														value="<c:out value="${plantillaFacturacionBusqueda.listaPreciosCodigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
														</c:if> /> 
															<c:if test="${accion != 'MODIFICACION'}">
															<button type="button" name="buscaListaPrecio" id="buscaListaPrecio"
																	title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																	<c:if test="${accion == 'CONSULTA'}">
																							disabled="disabled"
																	</c:if>>
																	<img src="<%=request.getContextPath()%>/images/buscar.png">
																</button>&nbsp;&nbsp; 
															<label id="listaPreciosCodigoLabel" for="listaPreciosCodigo">
																
															</label>
														</c:if>
													</td>
																
								<tr>							
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				
					<fieldset>
						<display:table name="plantillasFacturacion" id="plantillaFacturacion"
							requestURI="mostrarPlantillaFacturacion.html" pagesize="20" sort="external" partialList="true" size="${size}">
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_id" value="${plantillaFacturacion.id}" />
							</display:column>						
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_eliminar"
									value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
							</display:column>
							<display:column sortName="clienteEmp" property="clienteEmp.razonSocialONombreYApellido"
								titleKey="formularioPlantillaFacturacion.cliente" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column sortName="serie" property="serie.descripcion"
								titleKey="formularioPlantillaFacturacion.serie" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column property="afipTipoComprobante.descripcion" sortName="afipTipoComprobante"
								titleKey="formularioPlantillaFacturacion.tipoComprobante" sortable="true"
								class="celdaAlineadoIzquierda" />					
							<display:column property="listaPrecios.descripcion" sortName="listaPrecios"
								titleKey="formularioPlantillaFacturacion.listaPrecio" sortable="true"
								class="celdaAlineadoCentrado" />
							<display:column property="habilitadoStr" sortName="habilitado"
								titleKey="formularioPlantillaFacturacion.estado" sortable="true"
								class="celdaAlineadoCentrado" />
						</display:table>
						<div style="width: 100%" align="right">
						<table>
							<tr>
								<td>
									<button name="agregar" id="agregar" type="button">
										<img src="<%=request.getContextPath()%>/images/add.png">
										<spring:message code="botones.agregar" htmlEscape="true" />
									</button>
								</td>
							</tr>
						</table>
						</div>					
					</fieldset>
				<br style="font-size: xx-small;" />
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png" />
						<spring:message code="botones.cerrar" htmlEscape="true" />
					</button>
				</div>
				<br style="font-size: xx-small;" />				
			</fieldset>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithHeigth">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
			<div class="selectorDiv"></div>
</html>