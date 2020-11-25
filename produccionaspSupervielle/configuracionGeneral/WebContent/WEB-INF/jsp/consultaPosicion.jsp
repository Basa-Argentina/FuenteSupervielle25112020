<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
		code="formularioPosicion.tituloConsulta" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/mavalos_consulta_posicion.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>			
		</ul>
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioPosicion.tituloConsulta"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarPosicion.html"
					commandName="posicionBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
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
											code="formularioPosicion.empresa" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.sucursal" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.deposito"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.seccion" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
												code="formularioElemento.tamañoPagina" htmlEscape="true" />
										</td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="codigoEmpresa"
										name="codigoEmpresa" maxlength="6" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.codigoEmpresa}" default="" />"/>
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopup('empresasPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
										for="codigoEmpresa"> <c:out
												value="${posicionBusqueda.estante.grupo.seccion.deposito.sucursal.empresa.descripcion}"
												default="" /> </label></td>
									<td class="texto_ti"><input type="text" id="codigoSucursal"
										name="codigoSucursal" maxlength="6" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.codigoSucursal}" default="" />"/>
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupSucursal('sucursalesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>'
																		, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
										for="codigoSucursal"> <c:out
												value="${posicionBusqueda.estante.grupo.seccion.deposito.sucursal.descripcion}"
												default="" /> </label></td>
									<td class="texto_ti"><input type="text" id="codigoDeposito"
										name="codigoDeposito" maxlength="6" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.codigoDeposito}" default="" />"/>
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupDeposito('depositosPopupMap', '<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>'
																		, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoDepositoLabel"
										for="codigoDeposito"> <c:out
												value="${posicionBusqueda.estante.grupo.seccion.deposito.descripcion}"
												default="" /> </label>
									</td>
									<td class="texto_ti"><input type="text" id="codigoSeccion"
										name="codigoSeccion" maxlength="6" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.codigoSeccion}" default="" />"/>
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupSeccion('seccionesPopupMap', '<spring:message code="notif.stock.seleccionDeposito" htmlEscape="true"/>'
																		, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoSeccionLabel"
										for="codigoSeccion"> <c:out
												value="${posicionBusqueda.estante.grupo.seccion.descripcion}"
												default="" /> </label>
									</td>
									<td class="texto_ti">
											<select id="pagesize" name="pagesize" size="1">
												<option label="20" value="20" <c:if test="${pagesize == '20'}">
														selected="selected"
													</c:if>>20</option>
												<option label="40" value="40" <c:if test="${pagesize == '40'}">
														selected="selected"
													</c:if>>40</option>
												<option label="60" value="60" <c:if test="${pagesize == '60'}">
														selected="selected"
													</c:if>>60</option>
												<option label="80" value="80" <c:if test="${pagesize == '80'}">
														selected="selected"
													</c:if>>80</option>
												<option label="100" value="100" <c:if test="${pagesize == '100'}">
														selected="selected"
													</c:if>>100</option>
												<option label="200" value="200" <c:if test="${pagesize == '200'}">
														selected="selected"
													</c:if>>200</option>
												<option label="500" value="500" <c:if test="${pagesize == '500'}">
														selected="selected"
													</c:if>>500</option>
												<option label="2000" value="2000" <c:if test="${pagesize == '2000'}">
														selected="selected"
													</c:if>>2000</option>
												<option label="Todos" value="Todos" <c:if test="${pagesize == size}">
														selected="selected"
													</c:if>>Todos</option>
											</select>
										</td>
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.codigoDesdeEstante" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.codigoHastaEstante" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.posDesdeModulo" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.posHastaModulo" htmlEscape="true" />
									</td>
									<td class="texto_ti" rowspan="2" style="vertical-align: bottom;">
										<button id="botonBuscar" name="buscar" class="botonCentrado" type="submit">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="codigoDesdeEstante" class="inputTextNumericPositiveIntegerOnly"
										name="codigoDesdeEstante" maxlength="4" style="width: 50px;"
										value="${posicionBusqueda.codigoDesdeEstante}"/>
									</td>
									<td class="texto_ti"><input type="text" id="codigoHastaEstante" class="inputTextNumericPositiveIntegerOnly"
										name="codigoHastaEstante" maxlength="4" style="width: 50px;"
										value="${posicionBusqueda.codigoHastaEstante}"/>
									</td>
									<td class="texto_ti">
										<spring:message
											code="formularioPosicion.posVerModulo" htmlEscape="true" />
										<input type="text" id="posDesdeVertModulo"
										name="posDesdeVertModulo" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posDesdeVertModulo}" default="" />"/>
										<spring:message
											code="formularioPosicion.posHorModulo" htmlEscape="true" />
										<input type="text" id="posDesdeHorModulo"
										name="posDesdeHorModulo" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posDesdeHorModulo}" default="" />"/>
									</td>
									<td class="texto_ti">
										<spring:message
											code="formularioPosicion.posVerModulo" htmlEscape="true" />
										<input type="text" id="posHastaVertModulo"
										name="posHastaVertModulo" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posHastaVertModulo}" default="" />"/>
										<spring:message
											code="formularioPosicion.posHorModulo" htmlEscape="true" />
										<input type="text" id="posHastaHorModulo"
										name="posHastaHorModulo" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posHastaHorModulo}" default="" />"/>
									</td>
								</tr>
								<tr>
										<td class="texto_ti"><spring:message
												code="formularioPosicion.codigoDesde" htmlEscape="true" />
										
										<label>      </label>
										<spring:message
												code="formularioPosicion.codigoHasta" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
											code="formularioPosicion.vacio" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
											code="formularioPosicion.posDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
											code="formularioPosicion.posHasta" htmlEscape="true" />
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="codigoDesde" name="codigoDesde" maxlength="13" size="13" 
											class="inputTextNumericPositiveIntegerOnly completarZeros"
												value="<c:out value="${posicionBusqueda.codigoDesde}" default="" />"/>
											<label> a </label>
											<input type="text" id="codigoHasta" name="codigoHasta" maxlength="13" size="13" 
											class="inputTextNumericPositiveIntegerOnly completarZeros"
												value="<c:out value="${posicionBusqueda.codigoHasta}" default="" />"/>
									</td>
									<td class="texto_ti"><spring:message
											code="formularioPosicion.vacio" htmlEscape="true" />
										</td>
									<td class="texto_ti">
										<spring:message
											code="formularioPosicion.posVer" htmlEscape="true" />
										<input type="text" id="posDesdeVert"
										name="posDesdeVert" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posDesdeVert}" default="" />"/>
										<spring:message
											code="formularioPosicion.posHor" htmlEscape="true" />
										<input type="text" id="posDesdeHor"
										name="posDesdeHor" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posDesdeHor}" default="" />"/>
									</td>
									<td class="texto_ti">
										<spring:message
											code="formularioPosicion.posVer" htmlEscape="true" />
										<input type="text" id="posHastaVert"
										name="posHastaVert" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posHastaVert}" default="" />"/>
										<spring:message
											code="formularioPosicion.posHor" htmlEscape="true" />
										<input type="text" id="posHastaHor"
										name="posHastaHor" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionBusqueda.posHastaHor}" default="" />"/>
									</td>
									<td class="texto_ti" rowspan="1">
										<button id="botonExportar" name="botonExportar" class="botonCentrado" type="button" onclick="exportar();" >
											<img src="<%=request.getContextPath()%>/images/impresora.gif">
											<spring:message code="botones.exportar" htmlEscape="true" />
										</button>
									</td>	
								<tr>
								<tr>
								<td class="texto_ti">
										<spring:message	code="formularioPosicion.posicion.estado" htmlEscape="true" />
								</td>
								</tr>
								<tr>
								<td class="texto_ti">
											<select id="estado"
												name="estado" size="1">
													<option label="Seleccione un Estado" value="">Seleccione un Estado</option>
													<option label="DISPONIBLE" value="DISPONIBLE" <c:if test="${posicionBusqueda.estado == 'DISPONIBLE'}">
															selected="selected"
														</c:if>>DISPONIBLE</option>
													<option label="OCUPADA" value="OCUPADA" <c:if test="${posicionBusqueda.estado == 'OCUPADA'}">
															selected="selected"
														</c:if>>OCUPADA</option>
													<option label="ANULADO" value="ANULADO" <c:if test="${posicionBusqueda.estado == 'ANULADO'}">
															selected="selected"
														</c:if>>ANULADO</option>
													<option label="TEMPORALMENTE ANULADA" value="TEMPORALMENTE ANULADA" <c:if test="${posicionBusqueda.estado == 'TEMPORALMENTE ANULADA'}">
															selected="selected"
														</c:if>>TEMPORALMENTE ANULADA</option>
											</select>
										</td>																												
									</tr>							
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />

				<form:form action="cambiarEstadoPosiciones.html" commandName="posicionTable" method="post">
					<fieldset>
						<display:table name="posiciones" id="posicion" requestURI="mostrarPosicion.html" pagesize="${pagesize}" 
						sort="external" partialList="true" size="${size}" export="true">
							<display:column class="hidden" headerClass="hidden" media="html">
								<input type="hidden" id="hdn_id" value="${posicion.id}" />
							</display:column>						
							<display:column class="hidden" headerClass="hidden" media="html">
								<input type="hidden" id="hdn_eliminar"
									value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
							</display:column>
							<display:column style="width:100px;" titleKey="formularioPosicion.selected" sortable="false"
								class="celdaAlineadoCentrado" media="html">
								<input type="checkBox" 
									id="selectedSel" name="selectedSel" style="width: 10px;"
									value="${posicion.id}"
									<c:if test="${posicion.selected == 'true'}">
										checked="CHECKED"
									</c:if>
									<c:if test="${posicion.disabled == 'true'}">
										disabled="disabled"
									</c:if> />
							</display:column>
							<display:column sortName="deposito" property="estante.grupo.seccion.deposito.codigo"
								titleKey="formularioPosicion.deposito" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column sortName="seccion" property="estante.grupo.seccion.codigo"
								titleKey="formularioPosicion.seccion" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column sortName="estante" property="estante.codigo"
								titleKey="formularioPosicion.posicion.estante" sortable="true"
								class="celdaAlineadoIzquierda" />
							<display:column sortName="modulo" titleKey="formularioPosicion.posicion.modulo" sortable="true" class="celdaAlineadoCentrado" >
								<c:out value="(${posicion.modulo.posVertical};${posicion.modulo.posHorizontal})"/>
							</display:column>	
							<display:column sortName="posicion" titleKey="formularioPosicion.posicion" sortable="true" class="celdaAlineadoCentrado" >
								<c:out value="(${posicion.posVertical};${posicion.posHorizontal})"/>
							</display:column>						
							<display:column sortName="estado" property="estado"
								titleKey="formularioPosicion.posicion.estado" sortable="true"
								class="celdaAlineadoCentrado" />
							<display:column property="tipoYElementoAsignado"
								titleKey="formularioPosicion.elemento" sortable="false"
								class="celdaAlineadoCentrado" />
							<display:column class="celdaAlineadoCentrado" media="html" >
								<img id="information"
									src="<%=request.getContextPath()%>/images/information.png"
									title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>
						</display:table>					
					</fieldset>
					<sec:authorize ifAllGranted="ROLE_PRO_POSICIONES_ESTADO">
						<fieldset>
							<div style="width: 100%" align="left">
								<select id="estado" name="estado" size="1" style="width: 190px;">
										<option label="ANULADO" value="ANULADO">ANULADO</option>
										<option label="DISPONIBLE" value="DISPONIBLE">DISPONIBLE</option>															
								</select>							
								<button name="cambiarEstadoPosiciones" type="button"						
									onclick="cambiarEstado('<spring:message code="notif.posicion.seleccionPosicion" htmlEscape="true"/>'
															, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');">
									<img src="<%=request.getContextPath()%>/images/skip.png">							
									<spring:message code="botones.cambiarEstado" htmlEscape="true" />
								</button>
							</div>
						</fieldset>
					</sec:authorize>
				</form:form>
				
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
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="depositosPopupMap" />
		<jsp:param name="clase" value="depositosPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="sucursalesPopupMap" /> 
		<jsp:param name="clase" value="sucursalesPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="empresasPopupMap" /> 
		<jsp:param name="clase" value="empresasPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="seccionesPopupMap" /> 
		<jsp:param name="clase" value="seccionesPopupMap" /> 
	</jsp:include>
	
	<div id="pop">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label>Cambiando estado de las Posiciones seleccionadas. Espere por favor...</label>	     
	</div>
</html>