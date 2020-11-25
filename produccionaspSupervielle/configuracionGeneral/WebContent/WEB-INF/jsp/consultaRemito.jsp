<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioRemito.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_consulta_remito.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>	
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioRemito.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarRemito.html" commandName="remitoBusqueda" method="post">
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.buscar" htmlEscape="true"/>
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"  id="busquedaDiv" align="center">
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioRemito.empresa" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.cliente" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.fechaDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.fechaHasta" htmlEscape="true" />
										</td>
										<td rowspan="2" style="vertical-align: bottom;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
												<spring:message code="textos.buscar" htmlEscape="true" />
											</button>
										</td>
									</tr>
									<tr>
										<td class="texto_ti"><input type="text" id="codigoEmpresa"
											name="codigoEmpresa" maxlength="6" style="width: 50px;"
											value="<c:out value="${remitoBusqueda.codigoEmpresa}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopup('empresasPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
											for="codigoEmpresa"> <c:out
													value="${remitoBusqueda.empresa.descripcion}"
													default="" /> </label></td>
										<td class="texto_ti"><input type="text" id="codigoCliente"
											name="codigoCliente" maxlength="6" style="width: 50px;"
											value="<c:out value="${remitoBusqueda.codigoCliente}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupCliente('clientesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoClienteLabel"
											for="codigoCliente"> <c:out
													value="${remitoBusqueda.clienteEmp.razonSocialONombreYApellido}"
													default="" /> </label>
										</td>			
										<td class="texto_ti">
														<input type="text" id="fechaDesde" name="fechaDesde" 
															maxlength="10" 
															value="<c:out value="${remitoBusqueda.fechaDesdeStr}" default="" />" />
															
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'remitoBusqueda',
																	// input name
																	'controlname': 'fechaDesde'
																});
															</script></c:if>
													</td>										
													<td class="texto_ti">
														<input type="text" id="fechaHasta" name="fechaHasta" 
															maxlength="10" 
															value="<c:out value="${remitoBusqueda.fechaHastaStr}" default="" />" />
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'remitoBusqueda',
																	// input name
																	'controlname': 'fechaHasta'
																});
															</script></c:if>
													</td>
										
									</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioRemito.sucursal" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.deposito"
												htmlEscape="true" /></td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.serie" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.datosRemito.estado" htmlEscape="true" />
										</td>										
									</tr>
									<tr>
										<td class="texto_ti"><input type="text" id="codigoSucursal"
											name="codigoSucursal" maxlength="6" style="width: 50px;"
											value="<c:out value="${remitoBusqueda.codigoSucursal}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupSucursal('sucursalesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
											for="codigoSucursal"> <c:out
													value="${remitoBusqueda.sucursal.descripcion}"
													default="" /> </label></td>
													
										<td class="texto_ti"><input type="text" id="codigoDepositoOrigen"
											name="codigoDepositoOrigen" maxlength="6" style="width: 50px;"
											value="<c:out value="${remitoFormulario.codigoDepositoOrigen}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupDepositoOrigen('depositosOrigenPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoDepositoOrigenLabel"
											for="codigoDeposito"> <c:out
													value="${remitoFormulario.depositoOrigen.descripcion}"
													default="" /> </label>
										</td>	
												
										<td class="texto_ti"><input type="text" id="codigoSerie"
											name="codigoSerie" maxlength="6" style="width: 50px;"
											value="<c:out value="${remitoBusqueda.codigoSerie}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopupSerie('seriesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoSerieLabel"
											for="codigoRemito"> <c:out
													value="${remitoBusqueda.serie.descripcion}"
													default="" /> </label>
										</td>
										
										<td class="texto_ti">
											<select id="estado"
												name="estado" size="1">
													<option label="Seleccione un Estado" value="">Seleccione un Estado</option>
													<option label="Pendiente" value="Pendiente" <c:if test="${remitoBusqueda.estado == 'Pendiente'}">
															selected="selected"
														</c:if>>Pendiente</option>
													<option label="Cancelado" value="Cancelado" <c:if test="${remitoBusqueda.estado == 'Cancelado'}">
															selected="selected"
														</c:if>>Cancelado</option>
													<option label="En Transito" value="En Transito" <c:if test="${remitoBusqueda.estado == 'En Transito'}">
															selected="selected"
														</c:if>>En Transito</option>
													<option label="Entregado" value="Entregado" <c:if test="${remitoBusqueda.estado == 'Entregado'}">
															selected="selected"
														</c:if>>Entregado</option>
											</select>
										</td>																												
									</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioRemito.transporte" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.tipoRemito" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.numeroDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioRemito.numeroHasta" htmlEscape="true" />
										</td>
									</tr>
									<tr>
									<td class="texto_ti"><input type="text"
										id="codigoTransporte" name="codigoTransporte" maxlength="6"
										style="width: 50px;"
										value="<c:out value="${remitoBusqueda.codigoTransporte}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupTransporte('transportesPopupMap', '<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>', '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoTransporteLabel"
										for="codigoTransporte"> <c:out
												value="${remitoBusqueda.transporte.descripcion}" default="" />
									</label></td>
									<td class="texto_ti"><select id="tipoRemito"
										name="tipoRemito" size="1"
										<c:if test="${accion == 'CONSULTA'}">disabled="disabled"</c:if>>
											<option label="Todos" value="">Todos</option>
													<option label="cliente" value="cliente" <c:if test="${remitoBusqueda.tipoRemito == 'cliente'}">
															selected="selected"
														</c:if>>Cliente</option>
													<option label="interno" value="interno" <c:if test="${remitoBusqueda.tipoRemito == 'interno'}">
															selected="selected"
														</c:if>>Interno</option>
									</select></td>
									<td class="texto_ti"><input type="text" id="numeroDesde"
										name="numeroDesde" maxlength="12" size="12"
										value="<c:out value="${remitoBusqueda.numeroDesde}" default="" />" />
									</td>
									<td class="texto_ti"><input type="text" id="numeroHasta"
										name="numeroHasta" maxlength="12" size="12"
										value="<c:out value="${remitoBusqueda.numeroHasta}" default="" />" />
									</td>
								</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="remitos" id="remito" 
							requestURI="mostrarRemito.html" pagesize="20" sort="external" partialList="true" size="${size}">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${remito.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column class="celdaAlineadoCentrado" title="<input type='checkbox' id='checktodos' name='checktodos'/>">
							    <input type="checkbox" class='checklote' value="${remito.id}"/>
			              	</display:column>			
		           		   	<display:column sortName="fecha" property="fechaEmisionStr" titleKey="formularioRemito.fecha" sortable="true" class="celdaAlineadoIzquierda"/>	           		   
							<display:column sortName="comprobante" property="letraYNumeroComprobante" titleKey="formularioRemito.comprobante" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="tipo" property="tipoRemito" titleKey="formularioRemito.tipo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="ingresoEgreso" property="ingresoEgreso" titleKey="formularioRemito.datosRemito.ingreEgre" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="cantidadElementos" property="cantidadElementos" titleKey="formularioRemito.datosRemito.cant" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column sortName="empresa" property="empresa.descripcion" titleKey="formularioRemito.empresa" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="sucursal" property="sucursal.descripcion" titleKey="formularioRemito.sucursal" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="destino" property="destino" titleKey="formularioRemito.destino" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
						  	<display:column sortName="estado" property="estado" titleKey="formularioRemito.datosRemito.estado" sortable="true" class="celdaAlineadoIzquierda"/>
						</display:table>
						<div style="width: 100%" align="right">
							<table width="100%">
								<tr>
									<td align="left">
										<form:form action="cambiarEstadoRemitos.html" commandName="posicionTable" method="post">
											<input type="hidden" id="selectedSel" name="selectedSel"  />
											<select id="estadoRemito"
													name="estadoRemito" size="1">
														<option label="Seleccione un Estado" value="">Seleccione un Estado</option>
														<option label="Pendiente" value="Pendiente">Pendiente</option>
														<option label="Cancelado" value="Cancelado">Cancelado</option>
														<option label="En Transito" value="En Transito">En Transito</option>
														<option label="Entregado" value="Entregado">Entregado</option>
												</select>						
											<button name="cambiarEstado"  id="cambiarEstado" type="button">
												<img src="<%=request.getContextPath()%>/images/skip.png">							
												<spring:message code="botones.cambiarEstado" htmlEscape="true" />
											</button>
										</form:form>	
									</td>
									<td align="right">
										<button name="imprimirRemitos" type="button" id="imprimirRemitos">
											<img src="<%=request.getContextPath()%>/images/skip.png">
											<spring:message code="botones.imprimirRemitos"
												htmlEscape="true" />
										</button>
										<button name="agregar" type="button" onclick="agregar();"
											>
											<img src="<%=request.getContextPath()%>/images/add.png">
											<spring:message code="botones.agregar" htmlEscape="true" />
										</button>
									</td>
								</tr>
							</table>
							
						</div>
						
				</fieldset>
					<br style="font-size: xx-small;"/>
					<div align="center">
						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/volver4.png"/> 
							<spring:message code="botones.cerrar" htmlEscape="true"/>  
						</button>						
					</div>	
					<br style="font-size: xx-small;"/>
				</fieldset>	
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="depositosOrigenPopupMap" />
			<jsp:param name="clase" value="depositosOrigenPopupMap" />
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
			<jsp:param name="mapa" value="clientesPopupMap" /> 
			<jsp:param name="clase" value="clientesPopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="seriesPopupMap" /> 
			<jsp:param name="clase" value="seriesPopupMap" /> 
		</jsp:include>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="transportesPopupMap" /> 
			<jsp:param name="clase" value="transportesPopupMap" /> 
		</jsp:include>
	</body>
</html>