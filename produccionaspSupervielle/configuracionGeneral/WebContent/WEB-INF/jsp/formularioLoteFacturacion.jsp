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
		<spring:message code="formularioLoteFacturacion.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioLoteFacturacion.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioLoteFacturacion.titulo.modificar"
			htmlEscape="true" />
	</c:if></title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_loteFacturacion.js"></script>
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
	<div class="contextMenu" id="myMenu1">
	    <ul>	 
	      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>
	      <c:if test="${accion != 'CONSULTA'}">	 
	      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
	      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
	      </c:if>
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
							<spring:message code="formularioLoteFacturacion.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioLoteFacturacion.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioLoteFacturacion.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form name="formLoteFacturacion"
					action="guardarActualizarLoteFacturacion.html"
					commandName="loteFacturacionFormulario" method="post"
					modelAttribute="loteFacturacionFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${loteFacturacionFormulario.id}" default="" />" />
				    <input type="hidden" id="clienteId" name="clienteId"
						value="<c:out value="${clienteId}" default="" />" />
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" maxlength="6" style="width: 50px;"
						value="<c:out value="${loteFacturacionFormulario.codigoEmpresa}" default="" />"/>
					<input type="hidden" id="codigoSucursal" name="codigoSucursal" maxlength="6" style="width: 50px;"
						value="<c:out value="${loteFacturacionFormulario.codigoSucursal}" default="" />"/>
					<input type="hidden" id="periodo" name="periodo" maxlength="6" style="width: 50px;"
						value="<c:out value="${loteFacturacionFormulario.periodo}" default="" />"/>
					<input type="hidden" id="activado" name="activado" maxlength="6" style="width: 50px;"
						value="<c:out value="${activado}" default="" />"/>
						
					
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioLoteFacturacion.datosLoteFacturacion" htmlEscape="true" />
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
							<table style="width: 100%;" align="center">
								<tr>
									<td colspan="3">
									<fieldset>
											<legend>
												<spring:message code="formularioLoteFacturacion.nombreLoteFacturacion"
													htmlEscape="true" />
											</legend>
											<table class="busqueda" style="width: 100%; background-color: white;">
											<c:if test="${accion != 'NUEVO'}">					
									<tr>
										<td class="texto_td"><spring:message
												code="formularioLoteFacturacion.datosLoteFacturacion.numero" htmlEscape="true" />
										</td>
		
										<td class="texto_ti">
											<input type="text" id="numeroLote"
												name="numeroLote" maxlength="8" size="8" class="completarZeros"
												value="<c:out value="${loteFacturacionFormulario.numero}" default="" />" 
												<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
										</td>
									</tr>
									</c:if>
									<tr>
										<td class="texto_td">
										<spring:message code="formularioLoteFacturacion.datosLoteFacturacion.fechaRegistro" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<table>
												<tr>
													<td>
														<input type="text" id="fechaRegistro" name="fechaRegistro" class="requerido"
															maxlength="10" 
															value="<c:out value="${loteFacturacionFormulario.fechaRegistroStr}" default="" />"
															<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
															
												<c:if test="${accion == 'NUEVO'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formLoteFacturacion',
																	// input name
																	'controlname': 'fechaRegistro'
																});
															</script></c:if>
													</td>
												</tr>
											</table>
										</td>		
									</tr>
									
									<tr>
										<td class="texto_td">
											<spring:message code="formularioLoteFacturacion.datosLoteFacturacion.periodo" htmlEscape="true"/>
										</td>
										<td>
										<table>
											<tr>
												<td class="texto_ti">
													<select id="añoPeriodo" name="añoPeriodo" size="1" class="requerido"
														<c:if test="${accion != 'NUEVO'}">
																	disabled="disabled"
																</c:if>>
																<option label="Año" value="0">Año</option>
														<c:forEach items="${años}" var="año">
															<option label="${año}" value="${año}" <c:if test="${loteFacturacionFormulario.añoPeriodo == año}">
																	selected="selected"
																</c:if>><c:out value="${año}"></c:out></option>
														</c:forEach>
													</select>
												</td>	
												<td class="texto_ti">
														<select id="selectPeriodo" class="requerido"
														<c:if test="${accion != 'NUEVO'}">
																		disabled="disabled"
																	</c:if>
															name="selectPeriodo" size="1">
																<option label="Mes" value="0">Periodo</option>
																<option label="Enero" value="1" <c:if test="${loteFacturacionFormulario.periodo == '1'}">
																		selected="selected"
																	</c:if>>Enero</option>
																<option label="Febrero" value="2" <c:if test="${loteFacturacionFormulario.periodo == '2'}">
																		selected="selected"
																	</c:if>>Febrero</option>
																<option label="Marzo" value="3" <c:if test="${loteFacturacionFormulario.periodo == '3'}">
																		selected="selected"
																	</c:if>>Marzo</option>	
																<option label="Abril" value="4" <c:if test="${loteFacturacionFormulario.periodo == '4'}">
																		selected="selected"
																	</c:if>>Abril</option>
																	<option label="Mayo" value="5" <c:if test="${loteFacturacionFormulario.periodo == '5'}">
																		selected="selected"
																	</c:if>>Mayo</option>
																	<option label="Junio" value="6" <c:if test="${loteFacturacionFormulario.periodo == '6'}">
																		selected="selected"
																	</c:if>>Junio</option>
																	<option label="Julio" value="7" <c:if test="${loteFacturacionFormulario.periodo == '7'}">
																		selected="selected"
																	</c:if>>Julio</option>
																	<option label="Agosto" value="8" <c:if test="${loteFacturacionFormulario.periodo == '8'}">
																		selected="selected"
																	</c:if>>Agosto</option>
																	<option label="Setiembre" value="9" <c:if test="${loteFacturacionFormulario.periodo == '9'}">
																		selected="selected"
																	</c:if>>Setiembre</option>
																	<option label="Octubre" value="10" <c:if test="${loteFacturacionFormulario.periodo == '10'}">
																		selected="selected"
																	</c:if>>Octubre</option>
																	<option label="Noviembre" value="11" <c:if test="${loteFacturacionFormulario.periodo == '11'}">
																		selected="selected"
																	</c:if>>Noviembre</option>
																	<option label="Diciembre" value="12" <c:if test="${loteFacturacionFormulario.periodo == '12'}">
																		selected="selected"
																	</c:if>>Diciembre</option>															
														</select>
													</td>
												</tr>
											</table>
										</td>							
									</tr>
									<tr>
										<td class="texto_td">
										<spring:message code="formularioLoteFacturacion.datosLoteFacturacion.fechaFacturacion" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<table>
												<tr>
													<td>
														<input type="text" id="fechaFacturacion" name="fechaFacturacion" class="requerido"
															maxlength="10" 
															value="<c:out value="${loteFacturacionFormulario.fechaFacturacionStr}" default="" />"
															<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if>/>
															
												<c:if test="${accion == 'NUEVO'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formLoteFacturacion',
																	// input name
																	'controlname': 'fechaFacturacion'
																});
															</script></c:if>
													</td>
												</tr>															
											</table>
										</td>		
									</tr>
									<tr>
										<td class="texto_td"><spring:message
												code="formularioLoteFacturacion.descripcion" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<table>
												<tr>
													<td>
											<input type="text" id="descripcion"
												name="descripcion" 	value="<c:out value="${loteFacturacionFormulario.descripcion}" default="" />" 
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<c:if test="${accion != 'NUEVO'}">
										<tr>
											<td class="texto_td"><spring:message
													code="formularioLoteFacturacion.datosLoteFacturacion.estado" htmlEscape="true" />
											</td>
											<td class="texto_ti">
												<select id="estado" 
														<c:if test="${accion == 'CONSULTA' || loteFacturacionFormulario.estado == 'Facturado'}">
																disabled="disabled"
															</c:if>
													name="estado" size="1">
														<option label="Pendiente" value="Pendiente" <c:if test="${loteFacturacionFormulario.estado == 'Pendiente'}">
																selected="selected"
															</c:if>>Pendiente</option>
													<c:if test="${loteFacturacionFormulario.estado == 'Facturado'}">		
														<option label="Facturado" value="Facturado" <c:if test="${loteFacturacionFormulario.estado == 'Facturado'}">
																selected="selected"
															</c:if>>Facturado</option>
													</c:if>		
														<option label="Anulado" value="Anulado" <c:if test="${loteFacturacionFormulario.estado == 'Anulado'}">
																selected="selected"
															</c:if>>Anulado</option>
												</select>
											</td>																					
										</tr>
									</c:if>
									<c:if test="${accion == 'CONSULTA'}">
										<tr>
											<td class="texto_td">
												<spring:message code="formularioLoteFacturacion.datosLoteFacturacion.usuarioRegistro" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<input type="text" readonly="readonly" id="usuarioRegistro" name="usuarioRegistro" value="<c:out value="${loteFacturacionFormulario.usuarioRegistro.persona}" default=""/>" />
											</td>
										</tr>
										<tr>
											<td class="texto_td">
												<spring:message code="formularioLoteFacturacion.datosLoteFacturacion.fechaModificacion" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<input type="text" readonly="readonly" id="fechaModificacion" name="fechaModificacion" value="<c:out value="${loteFacturacionFormulario.fechaModificacionStr}" default=""/>" />
											</td>
										</tr>
										<tr>
											<td class="texto_td">
												<spring:message code="formularioLoteFacturacion.datosLoteFacturacion.usuarioModificacion" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<input type="text" readonly="readonly" id="usuarioModificacion" name="usuarioModificacion" value="<c:out value="${loteFacturacionFormulario.usuarioModificacion.persona}" default=""/>" />
											</td>
										</tr>
									</c:if>		
								</table>
							</fieldset>
							</td>
								</tr>
							</table>
						</div>
					</fieldset>
					<br style="font-size: xx-small;"/>						
					<fieldset>
					<legend>
						<spring:message	code="formularioLoteFacturacion.datosLoteFacturacion.detalles" htmlEscape="true" />
					</legend>
							<display:table name="listaPreFacturas" id="preFactura" requestURI="mostrarLoteFacturacion.html" pagesize="20" sort="list" keepStatus="true">
								<display:column class="hidden" headerClass="hidden">
									<input type="hidden" id="hdn_id" value="${preFactura.id}" />
								<display:column class="hidden" headerClass="hidden">
									<input type="hidden" id="hdn_orden" value="${preFactura.orden}" />
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
								<display:column property="empresa.descripcion" titleKey="formularioComprobanteFactura.empresa" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
								<display:column property="sucursal.descripcion" titleKey="formularioComprobanteFactura.sucursal" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
								<display:column property="clienteEmp.razonSocialONombreYApellido" titleKey="formularioComprobanteFactura.cliente" sortable="true" class="celdaAlineadoIzquierda">
								</display:column>
								<display:column titleKey="formularioComprobanteFactura.importe" sortable="true" class="celdaAlineadoIzquierda">
									$ <fmt:formatNumber value="${preFactura.totalFinal}" ></fmt:formatNumber>
								</display:column>
							</display:table>
						<br style="font-size: xx-small;" />
						<c:if test="${accion != 'CONSULTA'}">
							<table>
								<tr>
<!--									<td>-->
<!--										<div style="width: 100%" align="left">-->
<!--											<button id="botonEliminar" name="eliminar" type="button">-->
<!--												<img src="<%=request.getContextPath()%>/images/eliminar.png">-->
<!--												<spring:message code="botones.desasociar" htmlEscape="true" />-->
<!--											</button>-->
<!--										</div></td>-->
<!--									<td>-->
<!--										<div style="width: 100%" align="right">-->
<!--											<button id="botonAgregar" name="agregar" type="button">-->
<!--												<img src="<%=request.getContextPath()%>/images/add.png">-->
<!--												<spring:message code="botones.asociar" htmlEscape="true" />-->
<!--											</button>-->
<!--										</div>-->
<!--									</td>-->
									<td width="10%">
										<spring:message code="formularioConceptoOperacionCliente.vacio" htmlEscape="true" />
									</td>
									<td class="texto_td"><div align="right">
											<button id="botonGenerarConceptos" name="botonGenerarConceptos" 
											type="button">
												<img src="<%=request.getContextPath()%>/images/eliminar.png">
												<spring:message code="formularioLoteFacturacion.boton.generarConceptoMensual" htmlEscape="true" />
											</button>
										</div>
									</td>
								</tr>
							</table>
						</c:if>
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
	<!-- ----------	fancybox ------------------------- -->
	<a id="fancyboxAgregarDetalle" style="visibility:hidden;" href="iniciarPopUpLoteFacturacionDetalle.html"></a>
	<div id="pop">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label >Generando conceptos mensuales. Espere por favor...</label>	     
	</div>	
</body>
</html>