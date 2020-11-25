<%@page import="java.util.Date"%>
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
	<title><c:if	test="${accion == 'NUEVO'}">
						<spring:message code="formularioConceptoOperacionCliente.titulo.registrar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'MODIFICACION'}">
						<spring:message code="formularioConceptoOperacionCliente.titulo.modificar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'CONSULTA'}">
						<spring:message code="formularioConceptoOperacionCliente.titulo.consultar"
							htmlEscape="true" />
			</c:if>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_conceptoOperacionCliente.js"></script>
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
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
		<fieldset style="border: none; text-align: left; width: 97%;">
			<legend>
				<font class="tituloForm" size="5">
					 <c:if	test="${accion == 'NUEVO'}">
						<spring:message code="formularioConceptoOperacionCliente.titulo.registrar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'MODIFICACION'}">
						<spring:message code="formularioConceptoOperacionCliente.titulo.modificar"
							htmlEscape="true" />
					</c:if> <c:if test="${accion == 'CONSULTA'}">
						<spring:message code="formularioConceptoOperacionCliente.titulo.consultar"
							htmlEscape="true" />
					</c:if> </font>
			</legend>
			<br />
			<form:form name="formConceptoOperacionCliente" action="guardarActualizarConceptoOperacionCliente.html" commandName="conceptoOperacionClienteFormulario" method="post"
					modelAttribute="conceptoOperacionClienteFormulario">
					
				<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />" />
				<input type="hidden" id="id" name="id" value="<c:out value="${conceptoOperacionClienteFormulario.id}" default="" />" />
				<input type="hidden" id="alicuota" value="<c:out value="${conceptoOperacionClienteFormulario.conceptoFacturable.impuesto.alicuota}" escapeXml="true"/>"/>
				<input type="hidden" id="clienteAspId" name="clienteAspId" value="<c:out value="${clienteAspId}" default="" />" />
				<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />" />
				
				
				<fieldset>
					<div style="width: 97%;" id="busquedaDiv" align="center">
						<table >
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioConceptoOperacionCliente.fecha" htmlEscape="true" /></td>
									<td class="texto_ti">
									<table>
											<tr>
												<td>
													<input type="text" id="fechaAlta" name="fechaAlta" class="requerido"
															maxlength="10" 
															value="<c:out value="${conceptoOperacionClienteFormulario.fechaAltaStr}"/>"
															<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
															
												<c:if test="${accion == 'NUEVO'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formConceptoOperacionCliente',
																	// input name
																	'controlname': 'fechaAlta'
																});
															</script></c:if>
												</td>
											</tr>
									</table></td>
								</tr>
								<tr>
								<td class="texto_ti">
											<spring:message
											code="formularioConceptoOperacionCliente.cliente" htmlEscape="true" />
									</td>
									<td class="texto_ti"
									><table>
									<tr>
									<td>
									<input type="text" id="codigoCliente" class="inputTextNumericPositiveIntegerOnly requerido"
										name="codigoCliente" maxlength="6" style="width: 50px;" <c:if test="${accion == 'CONSULTA'}"> readonly="readonly" </c:if>
										value="<c:out value="${conceptoOperacionClienteFormulario.clienteEmp.codigo}" escapeXml="true"/>"/>
									&nbsp;&nbsp;
									</td>
									<td>
									<button type="button"
											onclick="abrirPopupNuevoClienteEmp();"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO'}">
													disabled="disabled"
												</c:if>
											>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
									&nbsp;&nbsp;
									</td>
									<td>
									<label id="codigoClienteLabel"	for="codigoCliente"> 
											<c:out value="${conceptoOperacionClienteFormulario.clienteEmp.razonSocialONombreYApellido}" escapeXml="true"/> 									
									</label>
									</td>
									</tr>
									</table>
									</td>
							</tr>
							<tr>
								<td class="texto_ti">
									<spring:message code="formularioConceptoOperacionCliente.concepto" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
									<table>
									<tr>
									<td>
									<input type="text" id="codigoConcepto" class="completarZeros inputTextNumericPositiveIntegerOnly requerido"
										name="codigoConcepto" maxlength="6" style="width: 50px;" <c:if test="${accion == 'CONSULTA'}"> readonly="readonly" </c:if>
										value="<c:out value="${conceptoOperacionClienteFormulario.conceptoFacturable.codigo}" escapeXml="true"/>"/>
									&nbsp;&nbsp;
									</td>
									<td>
									<button type="button" id="botonBuscarConceptoFacturable"
										title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										<c:if test="${accion != 'NUEVO'}">
													disabled="disabled"
												</c:if>
											>
										<img src="<%=request.getContextPath()%>/images/buscar.png">
									</button>
									&nbsp;&nbsp;
									</td>
									<td>
									<label id="codigoConceptoLabel"	for="codigoConcepto"> 
										<c:if test="${conceptoOperacionClienteFormulario.conceptoFacturable != null}">
											<c:out value="${conceptoOperacionClienteFormulario.conceptoFacturable.descripcion}" escapeXml="true"/> 
										</c:if>											
									</label>
									</td>
									</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
									<spring:message code="formularioConceptoOperacionCliente.listaPrecios" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
									<table>
									<tr>
									<td>
										<input type="text" id="listaPreciosCodigo" class="requerido inputTextNumericPositiveIntegerOnly"
											name="listaPreciosCodigo"  style="width: 50px;"
											value="<c:out value="${conceptoOperacionClienteFormulario.listaPrecios.codigo}" default="" />"/>
										&nbsp;&nbsp;
									</td>
									<td>
									<button type="button" id="botonBuscarListaPrecios"
										title="<spring:message code="textos.buscar" htmlEscape="true"/>"
										<c:if test="${accion != 'NUEVO'}">
													disabled="disabled"
												</c:if>
											>
										<img src="<%=request.getContextPath()%>/images/buscar.png">
									</button>
									&nbsp;&nbsp; 
									</td>
									<td>
									<label id="listaPreciosCodigoLabel"	for="listaPreciosCodigo"> 
										<c:if test="${conceptoOperacionClienteFormulario != null && conceptoOperacionClienteFormulario.listaPrecios != null}">
											<c:out value="${conceptoOperacionClienteFormulario.listaPrecios.descripcion}" escapeXml="true"/> 
										</c:if>
									</label>
									</td>
									</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
									<spring:message code="formularioConceptoOperacionCliente.impuesto" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
									<label id="alicuotaLabel"	for="codigoConcepto"> 
										<c:if test="${conceptoOperacionClienteFormulario.conceptoFacturable != null && conceptoOperacionClienteFormulario.conceptoFacturable.impuesto!=null}">
											<c:out value="${conceptoOperacionClienteFormulario.conceptoFacturable.impuesto.descripcion}" escapeXml="true"/>
											<c:out value=" / " escapeXml="true"/>
											<fmt:formatNumber value="${conceptoOperacionClienteFormulario.conceptoFacturable.impuesto.alicuota}" />
											<c:out value=" % " escapeXml="true"/>
										</c:if>
									</label> 
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
									<spring:message code="formularioConceptoOperacionCliente.precio" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
									<table>
									<tr>
									<td>
								<label id="precioLabel" ></label>
								<input type="text" id="precio" name="precio" class="requerido"
									value="<c:out value="${conceptoOperacionClienteFormulario.finalUnitario}" default="" />"/>
									</td>
									</tr>
									</table>								
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
									
									<spring:message code="formularioConceptoOperacionCliente.cantidad" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
									<table>
									<tr>
									<td>
										<input type="text" id="cantidad" class="requerido inputTextNumericPositiveIntegerOnly"
											name="cantidad" maxlength="10" style="width: 50px;"
											value="<c:out value="${conceptoOperacionClienteFormulario.cantidad}" default="" />"/>
										&nbsp;&nbsp;
									</td>
									</tr>
									</table>	
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
									<spring:message code="formularioConceptoOperacionCliente.netoTotal" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
								<table>
											<tr>
												<td>
									<label id="netoTotal" >
										<c:out value="$ 0,00" escapeXml="true"/>
									</label>
									</td>
									</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
									<spring:message code="formularioConceptoOperacionCliente.impuestos" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
								<table>
											<tr>
												<td>
									<label id="impuestos" >
										<c:out value="$ 0,00" escapeXml="true"/>
									</label>
									</td>
									</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="texto_ti">
									<spring:message code="formularioConceptoOperacionCliente.finalTotal" htmlEscape="true"/>
								</td>
								<td class="texto_ti">
								<table>
											<tr>
												<td>
									<label id="finalTotal">
										<c:out value="$ 0,00" escapeXml="true"/>
									</label>
									</td>
									</tr>
									</table>
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
				<button id="botonGuardar" name="guardar" type="button" onclick="guardarYSalir();"
					class="botonCentrado">
					<img src="<%=request.getContextPath()%>/images/ok.png">
					<spring:message code="botones.guardar" htmlEscape="true" />
				</button>
				&nbsp;
				<button id="botonCancelar" name="cancelar" type="button" class="botonCentrado">
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