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
	<title><spring:message
			code="formularioMovimiento.titulo" htmlEscape="true" />
		- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
			code="general.ambiente" htmlEscape="true" />
	</title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
	<script type="text/javascript" src="js/httprequest.js"></script>
	<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
	<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
	<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
	<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
	<script type="text/javascript" src="js/jquery.alerts.js"></script>
	<script type="text/javascript" src="js/jquery.numeric.js"></script>
	<script type="text/javascript" src="js/busquedaHelper.js"></script>
	<script type="text/javascript" src="js/mavalos_consulta_movimiento.js"></script>
	<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>

</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="anular" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.anular" htmlEscape="true"/></font></li>	 
		    </ul> 	 
	  	</div> 
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp" />
			<div class="contenido" align="left" style="overflow:hidden !important;">
				<br style="font-size: medium;" />
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5"> <spring:message
								code="formularioMovimiento.tituloconsulta"
								htmlEscape="true" /> </font>
					</legend>
					<br />
					<input id="clienteAspId" type="hidden" value="<c:out value="${clienteAspId}" default="" />" />
					<input id="sucursalId" type="hidden" value="<c:out value="${sucursalId}" default="" />" />
					<input id="codigoDepositoRequerido" type="hidden" value="<spring:message code="formularioMovimiento.error.codigoDeposito" htmlEscape="true"/>"/>
					<input id="tituloError" type="hidden" value="<spring:message code="formularioMovimiento.error.titulo" htmlEscape="true"/>" /> 
					<input id="tipoMovimientoSelected" type="hidden" value="<c:out value="${movimientoBusqueda.tipoMovimientoInt}" default="0"/>" />
					<form:form name="movimientoBusqueda" action="filtrarListaMovimiento.html" commandName="movimientoBusqueda" method="post">
					<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>					
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
											<spring:message	code="formularioMovimiento.deposito" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message code="formularioMovimiento.tipoElemento" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message	code="formularioMovimiento.cliente" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message code="formularioMovimiento.elemento" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message code="formularioMovimiento.datosMovimiento.mostrarAnulados" htmlEscape="true" />
										</td>									
										
									</tr>
									<tr>
										<td class="texto_ti">
											<table>
											<tr>
											<td>
												<input type="text" id="codigoDepositoActual" class="completarZeros inputTextNumericPositiveIntegerOnly"
													name="codigoDepositoActual" maxlength="2" style="width: 50px;"
													value="<c:out value="${movimientoBusqueda.codigoDepositoActual}" default="" />"/>
												&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopup('depositosActualesPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											</td>
											<td>
											<label id="codigoDepositoActualLabel"	for="codigoDepositoActual"> 
												
											</label>
											</td>
											</tr>
											</table>
										</td>
										<td class="texto_ti">
											<table>
											<tr>
											<td>
											<input type="text" id="codigoTipoElemento" class="completarZeros inputTextNumericPositiveIntegerOnly"
												name="codigoTipoElemento" maxlength="3" style="width: 50px;"
												value="<c:out value="${movimientoBusqueda.codigoTipoElemento}" default="" />"/>
											&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopup('tiposElementosPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp;
											</td>
											<td>
											<label id="codigoTipoElementoLabel"	for="codigoTipoElemento"> 
												
											</label>
											</td>
											</tr>
											</table>
										</td>
										<td>
											<table>
											<tr>
											<td>
											<input type="text" id="codigoClienteEmp" class="inputTextNumericPositiveIntegerOnly"
												name="codigoClienteEmp" maxlength="6" style="width: 50px;"
												value="<c:out value="${movimientoBusqueda.codigoClienteEmp}" default="" />"/>
											&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopup('clienteEmpPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp;
											</td>
											<td> 
											<label id="codigoClienteEmpLabel" for="codigoClienteEmp"> 
												
											</label>
											</td>
											</tr>
											</table>
										</td>
										<td class="texto_ti">
											<table>
											<tr>
											<td>
											<input type="text" id="codigoElemento" class="completarZeros inputTextNumericPositiveIntegerOnly"
												name="codigoElemento" maxlength="12" style="width: 90px;"
												value="<c:out value="${movimientoBusqueda.codigoElemento}" default="" />"/>
											&nbsp;&nbsp;
											</td>
											<td>
											<button type="button"
												onclick="abrirPopupElemento();"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp;
											</td>
											<td> 
											<label id="codigoElementoLabel"	for="codigoElemento"> 
												
											</label>
											</td>
											</tr>
											</table>
										</td>
										<td class="texto_ti">
												<table align="center">
													<tr>
														<td>
															<input type="checkbox" id="mostrarAnulados" name="mostrarAnulados" 
															value="true" <c:if test="${movimientoBusqueda.mostrarAnulados==true}">checked="checked"</c:if>/>
														</td>
													</tr>
												</table>
										</td>																								
									</tr>							
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioMovimiento.fechaDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message code="formularioMovimiento.fechaHasta" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message	code="formularioMovimiento.tipoMovimiento" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<spring:message	code="formularioMovimiento.claseMovimiento" htmlEscape="true" />
										</td>
										<td rowspan="2" style="vertical-align: bottom;">
											<button type="button" id="botonCargar" name="cargar" class="botonCentrado" >
												<img src="<%=request.getContextPath()%>/images/buscar.png">
												<spring:message code="formularioMovimiento.buscar" htmlEscape="true" />
											</button>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">											
											<input type="text" id="fechaDesdeStr"
												name="fechaDesdeStr" maxlength="10" style="width: 65px;"
												value="<c:out value="${movimientoBusqueda.fechaDesdeStr}" />"/>
											<script type="text/javascript" >
															new tcal ({
																// form name
																'formname': 'movimientoBusqueda',
																// input name
																'controlname': 'fechaDesdeStr'
															});
											</script>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaHastaStr"
												name="fechaHastaStr" maxlength="10" style="width: 65px;"
												value="<c:out value="${movimientoBusqueda.fechaHastaStr}" />"/>
											<script type="text/javascript" >
															new tcal ({
																// form name
																'formname': 'movimientoBusqueda',
																// input name
																'controlname': 'fechaHastaStr'
															});
											</script>
										</td>
										<td class="texto_ti">
											<select id="tipoMovimientoInt" name="tipoMovimientoInt" size="1" style="width: 190px;">
												<option label="<spring:message code="formularioMovimiento.tipoMovimiento.seleccionar" htmlEscape="true"/>" value="0">
													<spring:message code="formularioMovimiento.tipoMovimiento.seleccionar" htmlEscape="true"/>
												</option>
												<option label="<spring:message code="formularioMovimiento.tipoMovimiento.ingreso" htmlEscape="true"/>" value="1">
													<spring:message code="formularioMovimiento.tipoMovimiento.ingreso" htmlEscape="true"/>
												</option>
												<option label="<spring:message code="formularioMovimiento.tipoMovimiento.transferencia" htmlEscape="true"/>" value="2">
													<spring:message code="formularioMovimiento.tipoMovimiento.transferencia" htmlEscape="true"/>
												</option>
												<option label="<spring:message code="formularioMovimiento.tipoMovimiento.egreso" htmlEscape="true"/>" value="3">
													<spring:message code="formularioMovimiento.tipoMovimiento.egreso" htmlEscape="true"/>
												</option>		
											</select>
										</td>
										<td class="texto_ti">
											<select id="claseMovimiento" name="claseMovimiento" size="1" style="width: 190px;">
												<option label="<spring:message code="formularioMovimiento.claseMovimiento.seleccionar" htmlEscape="true"/>" value="">
													<spring:message code="formularioMovimiento.claseMovimiento.seleccionar" htmlEscape="true"/>
												</option>
												<option label="<spring:message code="formularioMovimiento.claseMovimiento.cliente" htmlEscape="true"/>" value="cliente"
												<c:if test="${movimientoBusqueda.claseMovimiento == 'cliente'}">
															selected="selected"
														</c:if>>
													<spring:message code="formularioMovimiento.claseMovimiento.cliente" htmlEscape="true"/>
												</option>
												<option label="<spring:message code="formularioMovimiento.claseMovimiento.interno" htmlEscape="true"/>" value="interno"
												<c:if test="${movimientoBusqueda.claseMovimiento == 'interno'}">
															selected="selected"
														</c:if>>
													<spring:message code="formularioMovimiento.claseMovimiento.interno" htmlEscape="true"/>
												</option>		
											</select>
										</td>		
									</tr>
									<tr>
										<td class="texto_ti">
											Por Remito
										</td>
										<td class="texto_ti">
											Remito
										</td>
										<td class="texto_ti">
											<spring:message code="formularioElemento.lectura" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											
										</td>
										<td>
											
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<select id="tieneRemitoAsoc" name="tieneRemitoAsoc" size="1" style="width: 190px;">
												<option value="" 	<c:if test="${movimientoBusqueda.tieneRemitoAsoc == null}">
															selected="selected"
														</c:if>>
													TODOS
												</option>
												<option value="true" 	<c:if test="${movimientoBusqueda.tieneRemitoAsoc != null && movimientoBusqueda.tieneRemitoAsoc == true}">
															selected="selected"
														</c:if>>
													Con Remito
												</option>
												<option value="false" 	<c:if test="${movimientoBusqueda.tieneRemitoAsoc != null && movimientoBusqueda.tieneRemitoAsoc == false}">
															selected="selected"
														</c:if>>
													Sin Remito
												</option>		
											</select>
										</td>
										<td class="texto_ti">
									   
										<input type="text" id="codigoRemito" name="codigoRemito" 
											style="width: 80px;" class="numericPositiveIntegerOnly"
											value="<c:out value="${movimientoBusqueda.remito.id}" default="" />"
											/>
										
										<button type="button" id="buscaRemito"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"	
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoRemitoLabel" for="codigoRemito"> 
											 <c:out value="${movimientoBusqueda.remito.letraYNumeroComprobante}" default="" />
										</label>
									</td>
									<td class="texto_ti">
										<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="${codigoEmpresa}"/> 
										<input type="text" id="codigoLectura" name="codigoLectura"
											maxlength="6" style="width: 50px;"
											value="<c:out value="${movimientoBusqueda.codigoLectura}" default="" />" />
										&nbsp;&nbsp;
										<button type="button" name="buscaLectura" id="buscaLectura" title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; <label id="codigoLecturaLabel" for="codigoLectura">
											<c:out value="${descripcion}" default="" /> </label>
										</td>	
									</tr>
								</table>
							</div>
						</fieldset>
					</form:form>
					<br style="font-size: xx-small;" />
					<form:form name="movimientos" action="mostrarListaMovimientos.html" commandName="movimientos" method="post">
						<fieldset>
							<display:table name="movimientos" id="movimiento" requestURI="mostrarListaMovimientos.html" pagesize="20" sort="external"
								partialList="true" size="${size}">
								<display:column class="hidden" headerClass="hidden">
									<input type="hidden" id="hdn_id" value="${movimiento.id}" />
								</display:column>
								<display:column class="hidden" headerClass="hidden">
										<input type="hidden" id="hdn_estado" value="${movimiento.estado}" />
									</display:column>
								<c:if test="${movimientoBusqueda.mostrarAnulados==true}">
									<display:column sortName="estado" property="estado"
									titleKey="formularioMovimiento.datosMovimiento.estado" sortable="true"
									class="celdaAlineadoIzquierda" />
								</c:if>
								<display:column sortName="fecha" property="fechaStr"
									titleKey="formularioMovimiento.fecha" sortable="true"
									class="celdaAlineadoIzquierda" />
								<display:column sortName="tipoMovimiento" property="tipoMovimiento"
									titleKey="formularioMovimiento.tipoMovimiento" sortable="true"
									class="celdaAlineadoIzquierda" />
								<display:column sortName="tipoElemento" property="elemento.tipoElemento.descripcion"
									titleKey="formularioMovimiento.tipoElemento" sortable="true"
									class="celdaAlineadoIzquierda" />
								<display:column sortName="codigoElemento" property="elemento.codigo"
									titleKey="formularioMovimiento.elemento" sortable="true"
									class="celdaAlineadoIzquierda" />
								<display:column sortName="deposito" property="descripcionDepositoActual"
									titleKey="formularioMovimiento.depositoActual" sortable="true"
									class="celdaAlineadoCentrado" />
								<display:column property="descripcionOrigenDestino"
									titleKey="formularioMovimiento.origenDestino" sortable="false"
									class="celdaAlineadoCentrado" />						
								<display:column headerClass="hidden" property="lectura"
									titleKey="formularioMovimiento.lectura" sortable="false"
									class="celdaAlineadoCentrado hidden" />
								<display:column
									titleKey="formularioMovimiento.estante" sortable="false"
									class="celdaAlineadoCentrado" >
									<c:if test="${movimiento!=null && movimiento.posicionOrigenDestino!=null}">
										<c:out value="${movimiento.posicionOrigenDestino.estante.codigo}"/>
									</c:if>
								</display:column>
								<display:column sortName="posicion"
									titleKey="formularioMovimiento.posicion" sortable="false"
									class="celdaAlineadoCentrado" >
									<c:if test="${movimiento!=null && movimiento.posicionOrigenDestino!=null}">									
										<c:out value="(${movimiento.posicionOrigenDestino.posVertical};${movimiento.posicionOrigenDestino.posHorizontal})"/>
									</c:if>
								</display:column>
								<display:column property="remito.letraYNumeroComprobante"
									titleKey="formularioMovimiento.remito" sortable="false"
									class="celdaAlineadoCentrado" />
								<display:column property="descripcionRemito"
									titleKey="formularioMovimiento.nroRequerimiento" sortable="false"
									class="celdaAlineadoCentrado" />
								<display:column property="remito.tipoRequerimiento"
									titleKey="formularioMovimiento.tipoRequerimiento" sortable="false"
									class="celdaAlineadoCentrado" />
								<display:column property="usuario.usernameSinCliente"
									titleKey="formularioMovimiento.usuario" sortable="false"
									class="celdaAlineadoCentrado" />
								<display:column property="descripcion"
									titleKey="formularioMovimiento.descripcion" sortable="false"
									class="celdaAlineadoCentrado" />								
							</display:table>
							<div style="width: 100%" align="right">
						<table>
							<tr>
								<td>
									<button name="registrar" id="registrar" type="button">
										<img src="<%=request.getContextPath()%>/images/add.png">
										<spring:message code="formularioMovimiento.botones.registrarMovimientos" htmlEscape="true" />
									</button>
								</td>
							</tr>
						</table>
					</div>				
						</fieldset>				
					</form:form>
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
		<div class="selectorDiv"></div>
		<div id="pop" style="display:none">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
		</div>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="depositosActualesPopupMap" />
			<jsp:param name="clase" value="depositosActualesPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="tiposElementosPopupMap" />
			<jsp:param name="clase" value="tiposElementosPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="elementosPopupMap" />
			<jsp:param name="clase" value="elementosPopupMap" />
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="clienteEmpPopupMap" />
			<jsp:param name="clase" value="clienteEmpPopupMap" />
		</jsp:include>		
		
</body>
</html>