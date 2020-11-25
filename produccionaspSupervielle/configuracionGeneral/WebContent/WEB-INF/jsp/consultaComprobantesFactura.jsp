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
		<jsp:include page="styles.jsp"/>
	<title>
		<spring:message	code="formularioComprobanteFactura.titulo" htmlEscape="true" />
	</title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
	<script type="text/javascript" src="js/httprequest.js"></script>
	<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
	<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
	<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
	<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
	<script type="text/javascript" src="js/jquery.alerts.js"></script>
	<script type="text/javascript" src="js/jquery.numeric.js"></script>
	<script type="text/javascript" src="js/mavalos_consulta_comprobantes_factura.js"></script>
	<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>

</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>
		      <li id="modificar" ><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		      <li id="anular" value=""><img src="images/cancelar.png" /><font size="2"><spring:message code="botones.anular" htmlEscape="true"/></font></li>
		    </ul> 	 
  		</div> 
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left" style="overflow:hidden !important;">
				<br style="font-size: medium;" />
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5"> <spring:message
								code="formularioComprobanteFactura.tituloconsulta"
								htmlEscape="true" /> </font>
					</legend>
					<br />
					<input type="hidden" id="clienteAspId" value="<c:out value="${clienteAspId}" default="" />" />
					<input type="hidden" id="clienteEmpCodigoCondIva" value="<c:out value="TODO" default="" />" />
					<input type="hidden" id="sucursalId" value="<c:out value="${sucursalId}" default="" />" />
					<input type="hidden" id="codigoEmpresaRequerido" value="<spring:message code="formularioComprobanteFactura.error.codigoEmpresa" htmlEscape="true"/>"/>
					<input type="hidden" id="tituloError" value="<spring:message code="formularioComprobanteFactura.error.titulo" htmlEscape="true"/>" /> 
					<form:form name="facturaBusqueda" action="filtrarListaComprobantesFactura.html" commandName="facturaBusqueda" method="post">					
						<fieldset>
							<table width="100%">
								<thead>
									<tr>
										<th align="left" id="busquedaImg">
										<font style="color: #003090"> 
											<spring:message	code="textos.buscar" htmlEscape="true" /> 
										</font>
										<img id="busquedaImgSrcDown" src="images/skip_down.png"
											title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png" style="DISPLAY: none"
											title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
										</th>
									</tr>
								</thead>
							</table>	
							<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"
								id="busquedaDiv" align="center">
								<table class="busqueda"	style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti">
											<spring:message	code="formularioComprobanteFactura.empresa" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message code="formularioComprobanteFactura.tipo" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message code="formularioMovimiento.datosMovimiento.mostrarAnulados" htmlEscape="true" />
										</td>
										<td>
											&nbsp;
										</td>
									</tr>									
									<tr>
										<td class="texto_ti">
											<table>
											<tr>
											<td>
											<input type="text" id="codigoEmpresa" class="inputTextNumericPositiveIntegerOnly requerido"
												name="codigoEmpresa" maxlength="4" style="width: 50px;"
												value="<c:out value="${facturaBusqueda.codigoEmpresa}" default="" />"/>
											&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopup('empresasPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp;
											</td>
											<td>
											<label id="codigoEmpresaLabel"	for="codigoEmpresa"> 
												
											</label>
											</td>
											</tr>
											</table>
										</td>										
										<td class="texto_ti">
											<select id="idAfipTipoComprobante" name="idAfipTipoComprobante" 
													size="1" style="width: 190px;" >
												<option value="0"
													<c:if test="${facturaBusqueda.idAfipTipoComprobante == 0}">
													selected="selected"
													</c:if> > 
													<spring:message code="formularioComprobanteFactura.seleccionar" htmlEscape="true" />
												</option>
												<c:forEach items="${afipTipoComprobantes}" var="tipo">
													<option value="${tipo.id}"
														<c:if test="${tipo.id == facturaBusqueda.idAfipTipoComprobante}">
														selected="selected"
														</c:if> >
														<c:out value="${tipo.descripcion}"/>
													</option>
												</c:forEach>
											</select>
										</td>
										<td class="texto_ti">
												<table align="left">
													<tr>
														<td style="padding-left: 35px;">
															<input type="checkbox" id="mostrarAnulados" name="mostrarAnulados" 
															value="true" <c:if test="${facturaBusqueda.mostrarAnulados==true}">checked="checked"</c:if>/>
														</td>
													</tr>
												</table>
										</td>								
																						
									</tr>
																	
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioComprobanteFactura.sucursal" htmlEscape="true" />
										</td>				
										<td class="texto_ti">
											<spring:message	code="formularioComprobanteFactura.serie" htmlEscape="true" />
										</td>
										<td rowspan="3" style="vertical-align: center;">
											<button type="button" id="botonCargar" name="cargar" class="botonCentrado" >
												<img src="<%=request.getContextPath()%>/images/buscar.png">
												<spring:message code="textos.buscar" htmlEscape="true" />
											</button>
										</td>
										<td class="texto_ti">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<table>
											<tr>
											<td>
												<input type="text" id="codigoSucursal" class="inputTextNumericPositiveIntegerOnly"
													name="codigoSucursal" maxlength="4" style="width: 50px;"
													value="<c:out value="${facturaBusqueda.codigoSucursal}" default="" />"/>
												&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopupSucursal();"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											</td>
											<td>
											<label id="codigoSucursalLabel"	for="codigoSucursal"> 
												
											</label>
											</td>
											</tr>
											</table>
										</td>
										<td class="texto_ti">
											<table>
											<tr>
											<td>
												<input type="text" id="codigoSerie" class="inputTextNumericPositiveIntegerOnly"
													name="codigoSerie" maxlength="4" style="width: 50px;"
													value="<c:out value="${facturaBusqueda.codigoSerie}" default="" />"/>
												&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopupSeries();"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											</td>
											<td>
											<label id="codigoSerieLabel"	for="codigoSucursal"> 
												
											</label>
											</td>
											</tr>
											</table>
										</td>										
									</tr>
									
									<tr>
										<td class="texto_ti">
											<spring:message	code="formularioComprobanteFactura.cliente" htmlEscape="true" />
										</td>					
										<td class="texto_ti">										
											<spring:message code="formularioComprobanteFactura.fechaDesde" htmlEscape="true" />
											     
											<spring:message code="formularioComprobanteFactura.fechaHasta" htmlEscape="true" />
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<table>
											<tr>
											<td>
											<input type="text" id="codigoCliente" class="completarZeros inputTextNumericPositiveIntegerOnly"
												name="codigoCliente" maxlength="6" style="width: 50px;"
												value="<c:out value="${facturaBusqueda.codigoCliente}" default="" />"/>
											&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopupClienteEmp();"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
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
										<td class="texto_ti">											
											<input type="text" id="fechaDesdeStr"
												name="fechaDesdeStr" maxlength="10" style="width: 65px;"
												value="<c:out value="${facturaBusqueda.fechaDesdeStr}" />"/>
											<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'facturaBusqueda',
													// input name
													'controlname': 'fechaDesdeStr'
												});
											</script>

											<input type="text" id="fechaHastaStr"
												name="fechaHastaStr" maxlength="10" style="width: 65px;"
												value="<c:out value="${facturaBusqueda.fechaHastaStr}" />"/>
											<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'facturaBusqueda',
													// input name
													'controlname': 'fechaHastaStr'
												});
											</script>
										</td>
									</tr>
								</table>
							</div>
						</fieldset>
					</form:form>
					<br style="font-size: xx-small;" />
					<form:form name="facturas" action="mostrarListaComprobantesFactura.html" commandName="facturas" method="post">
						<fieldset>
							<display:table name="facturas" id="factura" requestURI="mostrarListaComprobantesFactura.html" pagesize="20" sort="list" keepStatus="false">
								<display:column class="hidden" headerClass="hidden">
									<input type="hidden" id="hdn_id" value="${factura.id}" />
								<display:column class="hidden" headerClass="hidden">
									<input type="hidden" id="hdn_estado" value="${factura.estado}" />
								</display:column>
								<display:column class="hidden" headerClass="hidden">
						    		<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		  	 	</display:column>
		           		  	 	<display:column class="hidden" headerClass="hidden">
						    		<input type="hidden" id="hdn_anular" value="<spring:message code="mensaje.anular" htmlEscape="true"/>"/>
		           		  	 	</display:column>
								</display:column>
								<display:column class="celdaAlineadoCentrado" title="<input type='checkbox' id='checktodos' name='checktodos'/>">
							    	<input type="checkbox" class='checklote' value="${factura.id}"/>
			              		</display:column>
			              		<display:column property="estado" titleKey="formularioComprobanteFactura.estado" sortable="false" class="estado celdaAlineadoIzquierda"> 
								</display:column>
								<display:column property="fechaString" titleKey="formularioComprobanteFactura.fecha" sortable="true" class="celdaAlineadoIzquierda"> 
								</display:column>
								<display:column property="codigoSeriePrefijoNumero" titleKey="formularioComprobanteFactura.comprobante" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
								<display:column property="empresa.descripcion" titleKey="formularioComprobanteFactura.empresa" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
								<display:column property="sucursal.descripcion" titleKey="formularioComprobanteFactura.sucursal" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
								<display:column property="clienteEmp.nombreRazonSocial" titleKey="formularioComprobanteFactura.cliente" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
								<display:column property="totalFinal" titleKey="formularioComprobanteFactura.importe" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
							</display:table>
							
							<button name="imprimirFacturas" type="button" id="imprimirFacturas">
										<img src="<%=request.getContextPath()%>/images/skip.png">
										<spring:message code="formularioFactura.botones.imprimir"
											htmlEscape="true" />
									</button>					
						</fieldset>				
					</form:form>
					<div style="width: 100%" align ="right">
						<button id="botonAgregar" name="agregar" type="button" class="botonCentrado" >
							<img src="<%=request.getContextPath()%>/images/add.png" > 
							<spring:message code="botones.agregar" htmlEscape="true"/>  
						</button>
					</div>
					<br style="font-size: xx-small;" />					
				</fieldset>
				<br style="font-size: xx-small;" />
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png" />
						<spring:message code="botones.cerrar" htmlEscape="true" />
					</button>
					<br style="font-size: xx-small;" />		
				</div>
				<jsp:include page="footer.jsp"/>
			</div>
			<jsp:include page="footer.jsp" />	
		</div>
		<div id="darkLayer" class="darkClassWithHeigth">&nbsp;</div>
		<jsp:include page="fieldErrors.jsp" />
		<jsp:include page="fieldAvisos.jsp" />
		
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="empresasPopupMap" />
			<jsp:param name="clase" value="empresasPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="sucursalesPopupMap" />
			<jsp:param name="clase" value="sucursalesPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="clienteEmpPopupMap" />
			<jsp:param name="clase" value="clienteEmpPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="seriesPopupMap" />
			<jsp:param name="clase" value="seriesPopupMap" />
		</jsp:include>
		
				
</body>
</html>