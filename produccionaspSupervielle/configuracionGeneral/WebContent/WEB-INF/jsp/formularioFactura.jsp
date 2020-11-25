<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accionFactura == 'NUEVO'}">
		<spring:message code="formularioFactura.titulo.registrar"
			htmlEscape="true" />
	</c:if> <c:if test="${accionFactura == 'MODIFICACION'}">
		<spring:message code="formularioFactura.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accionFactura == 'CONSULTA'}">
		<spring:message code="formularioFactura.titulo.consultar"
			htmlEscape="true" />
	</c:if></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/popupCargaElementos.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_factura.js"></script>
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
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
	    <ul>	 
	      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>
	      <c:if test="${accionFactura != 'CONSULTA'}">	 
	      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
	      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
	      </c:if>
	    </ul> 	 
	</div> 
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp"/>
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accionFactura == 'NUEVO'}">
							<spring:message code="formularioFactura.titulo.registrar"
								htmlEscape="true" />
						</c:if> <c:if test="${accionFactura == 'MODIFICACION'}">
							<spring:message code="formularioFactura.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accionFactura == 'CONSULTA'}">
							<spring:message code="formularioFactura.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<input type="hidden" id="error" name="error" value="<spring:message code="formularioFactura.error" htmlEscape="true" />" />
				<input type="hidden" id="errorSeleccioneEmpresa" name="errorSeleccioneEmpresa" value="<spring:message code="formularioFactura.error.seleccioneEmpresa" htmlEscape="true" />" />
				<input type="hidden" id="errorSeleccioneSucursal" name="errorSeleccioneSucursal" value="<spring:message code="formularioFactura.error.seleccioneSucursal" htmlEscape="true" />" />
				<input type="hidden" id="errorSeleccioneCliente" name="errorSeleccioneCliente" value="<spring:message code="formularioFactura.error.seleccioneClienteEmp" htmlEscape="true" />" />
				<input type="hidden" id="errorSeleccioneTipoComprobante" name="errorSeleccioneTipoComprobante" value="<spring:message code="formularioFactura.error.seleccioneAfipTipoComprobante" htmlEscape="true" />" />
				<input type="hidden" id="errorSeleccioneFecha" name="errorSeleccioneFecha" value="<spring:message code="formularioFactura.error.seleccioneFecha" htmlEscape="true" />" />
				<input type="hidden" id="headerFacturaNoModificable" name="headerFacturaNoModificable" value="${headerFacturaNoModificable}" />
 				
 				<form:form id="formFactura" name="formFactura" action="guardarActualizarFactura.html"  commandName="facturaAGuardar" method="post"
						modelAttribute="facturaAGuardar">
					<input type="hidden" id="accionFactura" name="accionFactura" value="<c:out value="${accionFactura}" default="" />" />
					<input type="hidden" id="idAfipTipoComprobanteSelected" name="idAfipTipoComprobanteSelected" value="<c:out value="${facturaFormulario.idAfipTipoComprobante}" default="0" />" />
					<input type="hidden" id="id" name="id" value="<c:out value="${facturaFormulario.id}" default="" />" />
					<input type="hidden" id="clienteAspId" name="clienteAspId" value="<c:out value="${clienteAspId}" default="" />" />
					<input type="hidden" id="codigoEmpresaDefault" name="codigoEmpresaDefault" value="<c:out value="${facturaFormulario.codigoEmpresa}" default="" />"/>
					<input type="hidden" id="codigoSucursalDefault" name="codigoSucursalDefault" value="<c:out value="${facturaFormulario.codigoSucursal}" default="" />"/>
					<input type="hidden" id="clienteEmpCodigoCondIva" name="clienteEmpCodigoCondIva" value="<c:out value="${facturaFormulario.clienteEmpCodigoCondIva}" default="" />" /> 
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioFactura.datosFactura" htmlEscape="true" />
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
										<spring:message code="formularioFactura.empresa" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioFactura.tipo" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioFactura.fecha" htmlEscape="true"/>
									</td>									
								</tr>
								<tr>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
										<input type="text" id="codigoEmpresa" class="inputTextNumericPositiveIntegerOnly requerido"
											name="codigoEmpresa" maxlength="4" style="width: 50px;" readonly="readonly"
											value="<c:out value="${facturaFormulario.codigoEmpresa}" default="" />" />
										&nbsp;&nbsp;
										</td>
										<td>
										<button type="button" disabled="disabled"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp;
										</td>
										<td>
										<label id="codigoEmpresaLabel"	for="codigoEmpresa"> 
											<c:if test="${facturaFormulario.empresa != null}">
												<c:out value="${facturaFormulario.empresa.nombreRazonSocial}" escapeXml="true"/> 
											</c:if>
										</label>
										</td>
										</tr>
										</table>
									</td>
									<td class="texto_ti">
										<select id="idAfipTipoComprobante" name="idAfipTipoComprobante" class="requerido"
												size="1" style="width: 190px;" >
											<c:out value="${comboTipoComprobante}" escapeXml="false" ></c:out>
										</select>
									</td>
									<td class="texto_ti">
										<input type="text" id="fechaStr" class="requerido"
												name="fechaStr" maxlength="10" style="width: 65px;"
												value="<c:out value="${facturaFormulario.fechaStr}" />" 
												<c:if test="${accionFactura == 'CONSULTA'}"> 
												readonly="readonly"
												</c:if>/>
										<c:if test="${accionFactura != 'CONSULTA'}">
											<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'formFactura',
													// input name
													'controlname': 'fechaStr'
												});
											</script>
										</c:if>		
									</td>
								</tr>
								
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFactura.sucursal" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioFactura.serie" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioFactura.numero" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
											<input type="text" id="codigoSucursal" class="requerido inputTextNumericPositiveIntegerOnly"
												name="codigoSucursal" maxlength="4" style="width: 50px;" readonly="readonly"
												value="<c:out value="${facturaFormulario.codigoSucursal}" default="" />" >
											&nbsp;&nbsp;
										</td>
										<td>
										<button type="button" disabled="disabled"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										</td>
										<td>
										<label id="codigoSucursalLabel"	for="codigoSucursal"> 
											<c:if test="${facturaFormulario.sucursal != null}">
												<c:out value="${facturaFormulario.sucursal.descripcion}" escapeXml="true"/> 
											</c:if>
										</label>
										</td>
										</tr>
										</table>
									</td>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
											<input type="text" id="codigoSerie" class="requerido"
												name="codigoSerie" maxlength="4" style="width: 50px;"
												value="<c:out value="${facturaFormulario.codigoSerie}" default="" />"
												<c:if test="${headerFacturaNoModificable && facturaFormulario.codigoSerie!=''}" >
													readonly="readonly"
												</c:if> />
											&nbsp;&nbsp;
										</td>
										<td>
										<button type="button"
											onclick="abrirPopupSeries();"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${headerFacturaNoModificable && facturaFormulario.codigoSerie!=''}" >
													disabled="disabled"
											</c:if> >
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										</td>
										<td>
										<label id="codigoSerieLabel" for="codigoSerie"> 
											
										</label>
										</td>
										</tr>
										</table>
									</td>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
											<label id="prefijoSerie" for="numeroComprobante">
												XXXX
											</label> 
										</td>
										<td>
											<input id="numeroComprobanteStr" name="numeroComprobanteStr" type="text" class="requerido inputTextNumericPositiveIntegerOnly completarZeros"
												value="<c:out value="${facturaFormulario.numeroStr}" default="" />" maxlength="8" style="width: 50px;" <c:if test="${accionFactura == 'CONSULTA'}"> 
												readonly="readonly"
												</c:if>/>
										</td>
										<td>
											
										</td>
										</tr>
										</table>
									</td>
								</tr>
								
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioFactura.cliente" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										&nbsp;&nbsp;
									</td>
									<td class="texto_ti">
										&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<table>
										<tr>
										<td>
										<input type="text" id="codigoCliente" class="inputTextNumericPositiveIntegerOnly requerido"
											name="codigoCliente" maxlength="6" style="width: 50px;"
											value="<c:out value="${facturaFormulario.codigoCliente}" default="" />"
											<c:if test="${headerFacturaNoModificable}" >
													readonly="readonly"
											</c:if>/>
										&nbsp;&nbsp;
										</td>
										<td>
										<button type="button"
											onclick="abrirPopupClienteEmp();"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${headerFacturaNoModificable}" >
												disabled="disabled"
											</c:if> >
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp;
										</td>
										<td> 
										<label id="codigoClienteLabel" for="codigoCliente"> 
											
										</label>
										</td>
										</tr>
										</table>
									</td>
									<td class="texto_ti" colspan="5">
										<table>
										<tr>
										<td>
											<spring:message code="formularioFactura.notasFacturacion" htmlEscape="true"/>
										</td>
										<td>
											<textarea  id="notasFacturacion" name="notasFacturacion" rows="3" cols="45" 
												<c:if test="${accionFactura == 'CONSULTA'}">readonly="readonly"</c:if>>
													<c:out value="${facturaFormulario.notasFacturacion}" default="" />
											</textarea>
										</td>
										</tr>
										</table>
									</td>
									<td class="texto_ti">
										&nbsp;&nbsp;
									</td>
								</tr>
								
							</table>
						</div>
					</fieldset>
				</form:form>
					<br style="font-size: xx-small;" />
					<fieldset>
						<legend>
							<spring:message code="formularioFactura.facturaDetalle"
								htmlEscape="true" />
						</legend>
						<display:table name="facturaDetalles" id="detalle" requestURI="precargaformularioFactura.html"
							pagesize="10" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_id" value="${detalle.id}" />
							</display:column>
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_eliminar"
									value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
							</display:column>
							<display:column property="orden"
								titleKey="formularioFactura.tabla.orden" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column property="conceptoFacturable.codigo"
								titleKey="formularioFactura.tabla.codigo" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column	property="conceptoFacturable.descripcion" 
								titleKey="formularioFactura.tabla.concepto"	sortable="true" 
								class="celdaAlineadoIzquierda" />
							<display:column	property="listaprecios.codigo" 
								titleKey="formularioFactura.tabla.lista"	sortable="true" 
								class="celdaAlineadoIzquierda" />
							<display:column
								titleKey="formularioFactura.tabla.precio"	sortable="true" 
								class="celdaAlineadoIzquierda" >
								$ <fmt:formatNumber value="${detalle.netoUnitario}"></fmt:formatNumber>
							</display:column>
							<display:column
								titleKey="formularioFactura.tabla.iva"	sortable="true" 
								class="celdaAlineadoIzquierda" >
								<fmt:formatNumber value="${detalle.IVA}"></fmt:formatNumber> %	
							</display:column>
							<display:column	property="cantidad" 
								titleKey="formularioFactura.tabla.cantidad"	sortable="true" 
								class="celdaAlineadoIzquierda" />								
							<display:column	
								titleKey="formularioFactura.tabla.total"	sortable="true" 
								class="celdaAlineadoIzquierda" >
								$ <fmt:formatNumber value="${detalle.finalTotal}" ></fmt:formatNumber>
							</display:column>
						</display:table>
						<br style="font-size: xx-small;" />
						<div>
							<table width="100%">
								<tr>
									<td>
										<spring:message code="formularioFactura.tabla.totalNeto" htmlEscape="true" />
										$ <fmt:formatNumber value="${facturaFormulario.totalNeto}" ></fmt:formatNumber>
									</td>
									<td>
										<spring:message code="formularioFactura.tabla.totalImpuestos" htmlEscape="true" />
										$ <fmt:formatNumber value="${facturaFormulario.totalIVA}" ></fmt:formatNumber>
									</td>
									<td>
										<spring:message code="formularioFactura.tabla.totalFinal" htmlEscape="true" />
										$ <fmt:formatNumber value="${facturaFormulario.totalFinal}" ></fmt:formatNumber>
									</td>
								</tr>
							</table>
						</div>
						<br style="font-size: xx-small;" />
						<c:if test="${accionFactura != 'CONSULTA'}">
						<div style="width: 100%" align ="right">
							<button id="botonAgregar" name="agregar" type="button" class="botonCentrado" >
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="botones.agregar" htmlEscape="true"/>  
							</button>
						</div>
						</c:if>
						<br style="font-size: xx-small;" />		
					</fieldset>
				
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accionFactura != 'CONSULTA'}">
				<div align="center">
					<button id="botonGuardar" name="botonGuardar" type="button" 
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
					<button id="botonCancelar" name="botonCancelar" type="button" 
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accionFactura == 'CONSULTA'}">
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
	
	<!-- ----------	fancybox ------------------------- -->
<!-- 	<a id="fancyboxAgregarDetalle" style="visibility:hidden;" href="iniciarPrecargaFormularioFacturaDetalle.html"></a>	 -->
	
<div class="selectorDiv"></div>
<div id="pop" class="hidden">
	<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
	<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
</div>
</body>
</html>