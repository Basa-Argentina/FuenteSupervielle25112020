<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioCuentaCorriente.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioCuentaCorriente.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioCuentaCorriente.titulo.consultar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas_comboDefault.js"></script>
<script type="text/javascript" src="js/Utils.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.chromatable.js"></script>
<script type="text/javascript" src="js/formulario_cuenta_corriente.js"></script>
<script type="text/javascript" src="js/busquedaHelper.js"></script>
<link rel="stylesheet" href="js/fancybox//jquery.fancybox-1.3.4.css" type="text/css" />
<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.js"></script>
<jsp:include page="styles.jsp"/>
<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});">
		<div class="contextMenu" id="myMenu1">
		    <ul>
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div class="contextMenu" id="myMenu2">
		    <ul>
		      <li id="eliminarComprobante"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							<spring:message code="formularioCuentaCorriente.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioCuentaCorriente.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioCuentaCorriente.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<input type="hidden" id="error" name="error" value="<spring:message code="formularioFactura.error" htmlEscape="true" />" />
				<input type="hidden" id="errorSeleccioneCliente" name="errorSeleccioneCliente" value="<spring:message code="formularioFactura.error.seleccioneClienteEmp" htmlEscape="true" />" />
				<input type="hidden" id="errorSeleccioneFecha" name="errorSeleccioneFecha" value="<spring:message code="formularioFactura.error.seleccioneFecha" htmlEscape="true" />" />
				<input type="hidden" id="headerFacturaNoModificable" name="headerFacturaNoModificable" value="${headerFacturaNoModificable}" />
				<form:form action="guardarActualizarCuentaCorriente.html"
					commandName="facturaFormulario" method="post"
					modelAttribute="facturaFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${facturaFormulario.id}" default="" />" />
					<input type="hidden" id="listasSeleccionadas" name="listasSeleccionadas"/>
					<input type="hidden" id="listaDefecto" name="listaDefecto"/>
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="idAfipTipoComprobante" name="idAfipTipoComprobante" value="${facturaFormulario.afipTipoDeComprobante.id}" />
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioCuentaCorriente.datosCuentaCorriente" htmlEscape="true" />
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
											<input type="text" id="afipTipoDeComprobante" class="requerido inputTextNumericPositiveIntegerOnly"
												name="afipTipoDeComprobante"  style="width: 50px;" readonly="readonly"
												value="<c:out value="${facturaFormulario.afipTipoDeComprobante.tipo}" default="" />" >
											&nbsp;&nbsp;
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
													'formname': 'facturaFormulario',
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
												value="<c:out value="${facturaFormulario.numeroComprobanteStr}" default="" />" maxlength="8" style="width: 50px;" <c:if test="${accionFactura == 'CONSULTA'}"> 
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
					<fieldset>
						<legend>
							<spring:message code="formularioCuentaCorriente.titulo.medioCobro" htmlEscape="true" />
						</legend>
						<display:table name="sessionScope.medioPagoList" id="medioPago"  pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${medioPago.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
<!-- 							COLUMNAS		           		    -->
		           		    
		           		    <display:column property="tipoMedioPago.nombreMedio"  
								titleKey="formularioCuentaCorriente.tabla.medioCobro"
								class="celdaAlineadoIzquierda" />
							
							<display:column property="numero"  
								titleKey="formularioCuentaCorriente.tabla.numero"
								class="celdaAlineadoIzquierda" />	
							<display:column property="fechaVencimientoStr"    
								titleKey="formularioCuentaCorriente.tabla.vencimiento"
								class="celdaAlineadoIzquierda" />		
							<display:column property="titular"    
								titleKey="formularioCuentaCorriente.tabla.titular"
								class="celdaAlineadoIzquierda" />			
							
							<display:column property="nota"    
								titleKey="formularioCuentaCorriente.tabla.nota"
								class="celdaAlineadoIzquierda" />		
							
							<display:column
								titleKey="formularioCuentaCorriente.tabla.importe"
								class="celdaAlineadoIzquierda" >	
								<fmt:formatNumber value="${medioPago.importe}"></fmt:formatNumber>
							</display:column>		
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button id="agregarMedioCobro" name="agregarMedioCobro" type="button" class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="botones.agregar" htmlEscape="true"/>  
							</button>
						</div>
					</fieldset>
					<fieldset>
						<legend>
							<spring:message code="formularioCuentaCorriente.titulo.comprobante" htmlEscape="true" />
						</legend>
						<display:table name="sessionScope.comprobanteList" id="comprobante" requestURI="mostrarCuentaCorriente.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_idComprobante" value="${comprobante.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminarComprobante" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
<!-- 							COLUMNAS		           		    -->
		           		    <display:column property="fechaStr"    
								titleKey="formularioCuentaCorriente.tabla.fecha"
								class="celdaAlineadoIzquierda" />	
							<display:column  property="afipTipoDeComprobante.codigo"
								titleKey="formularioCuentaCorriente.tabla.comprobante"
								class="celdaAlineadoIzquierda" />	
							<display:column 
								titleKey="formularioCuentaCorriente.tabla.saldo"
								class="celdaAlineadoIzquierda">
								<fmt:formatNumber value="${comprobante.saldoDisponible}"></fmt:formatNumber>
							</display:column>			
							<display:column 
								titleKey="formularioCuentaCorriente.tabla.imputado">
									<input maxlength="15"  type="text" class="numerico" id="imputado" name="imputado" 
									value="<fmt:formatNumber value="${comprobante.imputado}"></fmt:formatNumber>" onblur="setImporte('${comprobante.id}',this.value,'${comprobante.saldoDisponible}');"/>
							</display:column>		
									
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button id="agregarComprobante" name="agregarComprobante" type="button"  class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="botones.agregar" htmlEscape="true"/>  
							</button>
						</div>
					</fieldset>
					<fieldset>
						<table>
							<tr>
								<td>
									<spring:message code="formularioCuentaCorriente.titulo.medioCobro" htmlEscape="true"/>  
								</td>
								<td class="texto_ti">
									<input type="text" id="medioCobro" name="medioCobro" style="width: 200px;" readonly="readonly"
										value="<fmt:formatNumber value="${medioPagoTotal}"></fmt:formatNumber>"
									/>
								</td>
								<td>
									<spring:message code="formularioCuentaCorriente.titulo.imputado" htmlEscape="true"/>  
								</td>
								<td class="texto_ti">
									<input type="text" id="imputadoTotal" name="imputadoTotal" style="width: 200px;" readonly="readonly"
										value="<fmt:formatNumber value="${comprobanteTotal}"></fmt:formatNumber>"
									/>
								</td>
								<td>
									<spring:message code="formularioCuentaCorriente.titulo.sinImputar" htmlEscape="true"/>  
								</td>
								<td class="texto_ti">
									<input type="text" id="sinImputar" name="sinImputar" style="width: 200px;" readonly="readonly"
										value="<fmt:formatNumber value="${sinImputar}"></fmt:formatNumber>"
									/>
								</td>	
							</tr>
						</table>
						
					</fieldset>
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
					<button name="guardar" type="button" id="guardar"
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
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>	
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
	<div id="pop" class="hidden">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
	</div>
</body>
</html>