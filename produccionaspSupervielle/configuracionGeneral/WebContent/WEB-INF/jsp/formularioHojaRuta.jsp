<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<jsp:include page="styles.jsp" />
	<title><c:if test="${accion == 'NUEVO'}">
			<spring:message code="formularioHojaRuta.titulo.registar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'MODIFICACION'}">
			<spring:message code="formularioHojaRuta.titulo.modificar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'CONSULTA'}">
			<spring:message code="formularioHojaRuta.titulo.consultar"
				htmlEscape="true" />
		</c:if>
	</title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
	<script type="text/javascript" src="js/httprequest.js"></script>
	<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
	<script type="text/javascript" src="js/Utils.js"></script>
	<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
	<script type="text/javascript" src="js/jquery.alerts.js"></script>
	<script type="text/javascript" src="js/jquery.chromatable.js"></script>
	<script type="text/javascript" src="js/calendar_us.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
	<script type="text/javascript" src="js/ini.js"></script>
	<script type="text/javascript" src="js/busquedaHelper.js"></script>
	<script type="text/javascript" src="js/formulario_hoja_ruta.js"></script>
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
		      <li id="agregar"><img src="images/agregar.png" /> <font size="2"><spring:message code="botones.agregar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	<div class="contextMenu" id="myMenu2">
	    <ul>	 
	      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li> 
	    </ul> 	 
  	</div>   	
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> 
						<c:if test="${accion == 'NUEVO'}">
							<spring:message code="formularioHojaRuta.titulo.registar"	htmlEscape="true" />
						</c:if> 
						<c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioHojaRuta.titulo.modificar" htmlEscape="true" />
						</c:if> 
						<c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioHojaRuta.titulo.consultar" htmlEscape="true" />
						</c:if> 
					</font>
				</legend>
				<br />
				<form:form id="formularioHojaRuta" action="guardarActualizarHojaRuta.html"
					commandName="hojaRuta" method="post"
					modelAttribute="hojaRuta">
					<input type="hidden" id="id" name="id" value="<c:out value="${hojaRuta.id}" default="" />" />
					<input type="hidden" id="idOperacion" name="idOperacion" />
					<input type="hidden" id="idOperacionPlanificada" name="idOperacionPlanificada" />
					<input type="hidden" id="obj_hash" name="obj_hash" value="<c:out value="${objectHash}" default="" />"/>
					<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="guardarContinuar" name="guardarContinuar" value="false" />
					<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioLoteReferencia.datosGenerales" htmlEscape="true" />
										</font> 
										<img id="busquedaImgSrcDown" src="images/skip_down.png"
											title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
											style="DISPLAY: none"
											title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="width: 100%;" id="busquedaDiv" align="center">
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.empresa" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.sucursal" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.transporte" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.serie" htmlEscape="true"/>
										</td>	
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="codigoEmpresa" name="codigoEmpresa" style="width: 50px;"
												readonly="readonly"
												value="<c:out value="${hojaRuta.empresa.codigo}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												disabled="disabled">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 

										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoSucursal" name="codigoSucursal" style="width: 50px;"
												readonly="readonly"
												value="<c:out value="${hojaRuta.sucursal.codigo}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaSucursal"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												disabled="disabled">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 

										</td>
										<td class="texto_ti">
											<input 
											    class="requerido" 
												type="text" id="codigoTransporte" name="codigoTransporte" style="width: 50px;"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
												value="<c:out value="${hojaRuta.transporte.codigo}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaTransporte" 
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											
										</td>
										<td class="texto_ti" style="width: 250px;" colspan="2">
											<input type="text" id="codigoSerie" name="codigoSerie" maxlength="30" 
														style="width: 50px;" class="requerido upperCase"
														value="<c:out value="${hojaRuta.serie.codigo}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if>
												/>
													&nbsp;&nbsp;
													<button type="button" 
														name="buscaSerie" id="buscaSerie"
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>
													>
													<img src="<%=request.getContextPath()%>/images/buscar.png" > 
													</button>&nbsp;&nbsp;
													
													<label id="codigoSerieLabel" "for="codigoSerie" >
															<c:out value="${hojaRuta.serie.prefijo}" default="" />			
													</label>-
													<input type="text" maxlength="8" 
															id="numeroStr" name="numeroStr" class="completarZeros"
															style="margin-top: 3px; width: 70px; position: absolute;" readonly="readonly"
															value="<c:out value="${hojaRuta.numeroStr}" default="" />" 
															/>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<label id="codigoEmpresaLabel" for="codigoEmpresa"> 
												 
											</label>
										</td>
										<td class="texto_ti">
											<label id="codigoSucursalLabel" for="codigoSucursal"> 
												 
											</label>
										</td>										
										<td class="texto_ti">
											<label id="codigoTransporteLabel" for="codigoTransporte"> 
												 <c:out value="${hojaRuta.transporte.descripcion}" default="" />	
											</label>
										</td>
										<td class="texto_ti">
											
										</td>	
									</tr>
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.fecha" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.hora" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.fechaSalida" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.horaSalida" htmlEscape="true"/>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="fecha" name="fecha" style="width: 80px;"
												readonly="readonly"
												value="<c:out value="${hojaRuta.fechaStr}" default="" />"/>
										</td>
										<td class="texto_ti" >
											<input type="text" 
												maxlength="5" id="hora" name="hora"
												style="width: 60px;" onblur="CheckTime(this)"
												value="<c:out value="${hojaRuta.hora}" default="" />" 
												readonly="readonly"
												/>
										</td>	
										<td class="texto_ti">
											<input type="text" class="requerido"  
												id="fechaSalida" name="fechaSalida" style="width: 80px;"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
												value="<c:out value="${hojaRuta.fechaSalidaStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'formularioHojaRuta',
														// input name
														'controlname': 'fechaSalida'
													});
												</script>
											</c:if>
										</td>
										<td class="texto_ti" >
											<input type="text" class="requerido" maxlength="5" id="horaSalida" name="horaSalida"
												style="width: 60px;" onblur="CheckTime(this)"
												value="<c:out value="${hojaRuta.horaSalida}" default="" />" 
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
												/>
										</td>	
									</tr>
								</table>
							</div>	
							<div id="tab_header" style="width: 100%;">
								<ul>
									<li id="tab_header_planificado"	class="selected">
										<spring:message code="formularioHojaRuta.planificado" htmlEscape="true"/>
									</li>
									<li id="tab_header_pendiente">
										<spring:message code="formularioHojaRuta.pendiente" htmlEscape="true"/>
									</li>
								</ul>
							</div>
							<div id="tab_content" style="width: 97%;">
								<div id="tab_content_planificado" class="selected">
									<table style="width: 100%;">		
										<tr>
											<td>
												<fieldset style="border: none; text-align: left; width: 97%;">
													<legend>
											        	<font class="tituloForm">
											        		<spring:message code="formularioHojaRuta.titulo.operaciones" htmlEscape="true"/>		        		
										        		</font>		  
													</legend>
													<table style="width: 100%;">		
														<tr>
															<td class="texto_ti" colspan="2">
																<display:table name="sessionScope.operacionesSeleccionadasHojaRuta" id="operacionPlanificadas">
																	<display:column class="hidden" headerClass="hidden">
																	    <input type="hidden" id="hdn_id_operacion" value="${operacionPlanificadas.id}"/>
													              	</display:column>	
																	<display:column property="fechaAltaStr" 
																		titleKey="formularioHojaRuta.listaPlanificado.fechaEntrega" sortable="false" class="celdaAlineadoIzquierda"/>
																		
																	<display:column property="tipoOperacion.descripcion" 
																		titleKey="formularioHojaRuta.listaPlanificado.operacion" sortable="false" class="celdaAlineadoIzquierda"/>	
																	
																	<display:column property="requerimiento.direccionDefecto.direccion.calle" 
																		titleKey="formularioHojaRuta.listaPlanificado.direccion" sortable="false" class="celdaAlineadoIzquierda"/>
																		
																	<display:column titleKey="formularioHojaRuta.listaPlanificado.personal" sortable="false" class="celdaAlineadoIzquierda">
																		<c:out value="${operacionPlanificadas.requerimiento.empleadoSolicitante.nombreYApellido}" default="" />
																	</display:column>
																		
																	<display:column titleKey="formularioHojaRuta.listaPlanificado.requerimiento" sortable="false" class="celdaAlineadoIzquierda">
																		<c:out value="${operacionPlanificadas.requerimiento.serie.codigo}" default="" />: <c:out value="${operacionPlanificadas.requerimiento.prefijoStr}" default="" /> - <c:out value="${operacionPlanificadas.requerimiento.numeroStr}" default="" />
																	</display:column>	
																  	<c:if test="${accion != 'CONSULTA'}">
																	<display:column class="celdaAlineadoCentrado">
														  	 			<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
																	</display:column>	
																	</c:if>
																  	
																</display:table> 
															</td>
														</tr>
													</table>
												</fieldset>	
											</td>
											<td>
												<fieldset style="border: none; text-align: left; width: 97%;">
													<legend>
											        	<font class="tituloForm">
											        		<spring:message code="formularioHojaRuta.titulo.elementos" htmlEscape="true"/>		        		
										        		</font>		  
													</legend>
													<table style="width: 100%;">		
														<tr>
															<td class="texto_ti" colspan="2">
																<div style="overflow-y:scroll;max-height: 200px;">
																<display:table name="sessionScope.operacionesElementosSeleccionadasHojaRuta" id="listaElementos">
																	<display:column class="hidden" headerClass="hidden">
																	    <input type="hidden" id="seleccionado" value="${listaElementos.estado}"/>
													              	</display:column>	
																	<display:column class="celdaAlineadoCentrado" media="html" sortable="false">
																		<input type="hidden" class="operacionId" value="${listaElementos.operacionElemento.operacion.id}"/>
																		<input type="checkbox" name="elementosSel" value="${listaElementos.obj_hash}" 
																			class="selectableCheckbox" onclick="marcarElemento(this.checked, '${listaElementos.obj_hash}')"
																				<c:if test="${listaElementos.estado!='NoSeleccionado'}">
																					checked="false"
																				</c:if>
																				<c:if test="${listaElementos.estado=='Seleccionado'}">
																					checked="true"
																				</c:if>
																				<c:if test="${listaElementos.seleccionable==false || accion=='CONSULTA'}">
																					disabled="disabled"
																				</c:if>
																		/>
																	</display:column>
 																	<display:column titleKey="formularioHojaRuta.listaPlanificadoElemento.tipo" sortable="false" class="celdaAlineadoIzquierda">
																			<c:if test="${listaElementos.operacionElemento.elemento!=null}">
																					<c:out value="${listaElementos.operacionElemento.elemento.tipoElemento.descripcion}"></c:out>
																			</c:if>
																			<c:if test="${listaElementos.operacionElemento.elemento==null}">
																				<spring:message code="formularioHojaRuta.listaPlanificadoElemento.sinElemento.tipo" htmlEscape="true"/>			
																			</c:if>
																	</display:column>	
																	<display:column property="operacionElemento.elemento.codigo" 
																		titleKey="formularioHojaRuta.listaPlanificadoElemento.elemento" sortable="false" class="celdaAlineadoIzquierda"/>
																</display:table> 
																</div>
															</td>
														</tr>
													</table>
												</fieldset>	
											</td>
										</tr>
								</table>		
								</div>
								<div id="tab_content_pendiente">
									<table style="width: 100%;">		
										<tr>
											<td>
												<table style="width: 100%;">		
													<tr>
														<td class="texto_ti" colspan="2">
															<fieldset style="border: none; text-align: left; width: 97%;">
																<legend>
														        	<font class="tituloForm">
														        		<spring:message code="formularioHojaRuta.titulo.operaciones" htmlEscape="true"/>		        		
													        		</font>		  
																</legend>
																
																<display:table name="listaOperacion" id="listaOperacion">
																	<display:column class="hidden" headerClass="hidden">
																	    <input type="hidden" id="hdn_id" value="${listaOperacion.id}"/>
													              	</display:column>
													              	<c:if test="${accion != 'CONSULTA'}">
													              	<display:column class="celdaAlineadoCentrado" title="<input type='checkbox' id='checktodos' name='checktodos'/>">
																	    <input type="checkbox" class='checklote' value="${listaOperacion.id}"/>
													              	</display:column>
													              	</c:if>	
																	<display:column property="fechaAltaStr" 
																		titleKey="formularioHojaRuta.listaPlanificado.fechaEntrega" sortable="false" class="celdaAlineadoIzquierda"/>
																		
																	<display:column property="tipoOperacion.descripcion" 
																		titleKey="formularioHojaRuta.listaPlanificado.operacion" sortable="false" class="celdaAlineadoIzquierda"/>	
																	
																	<display:column property="clienteEmp.razonSocialONombreYApellido" 
																		titleKey="formularioHojaRuta.listaPlanificado.cliente" sortable="false" class="celdaAlineadoIzquierda"/>
																	
																	<display:column property="requerimiento.direccionDefecto.direccion.calle" 
																		titleKey="formularioHojaRuta.listaPlanificado.direccion" sortable="false" class="celdaAlineadoIzquierda"/>
																		
																	<display:column titleKey="formularioHojaRuta.listaPlanificado.personal" sortable="false" class="celdaAlineadoIzquierda">
																		<c:out value="${listaOperacion.requerimiento.empleadoSolicitante.nombreYApellido}" default="" />
																	</display:column>
																	
																	<display:column titleKey="formularioHojaRuta.listaPlanificado.totalElementos" sortable="false" class="celdaAlineadoIzquierda">
																		<c:out value="${listaOperacion.cantidadElementos}" default="" />
																	</display:column>
																		
																	<display:column titleKey="formularioHojaRuta.listaPlanificado.requerimiento" sortable="false" class="celdaAlineadoIzquierda">
																		<c:out value="${listaOperacion.requerimiento.serie.codigo}" default="" />: <c:out value="${listaOperacion.requerimiento.prefijoStr}" default="" /> - <c:out value="${listaOperacion.requerimiento.numeroStr}" default="" />
																	</display:column>		
																		
																	<c:if test="${accion != 'CONSULTA'}">
																	  	<display:column class="celdaAlineadoCentrado">
																  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<c:out value="${listaOperacion.requerimiento.observaciones}" default="" />">
																		</display:column>	
																	</c:if>
																</display:table>
																<c:if test="${accion != 'CONSULTA'}">
																<button name="agregar" id="botonAgregar" name="botonAgregar" type="button">
																	<img src="<%=request.getContextPath()%>/images/add.png">
																	<spring:message code="botones.agregar" htmlEscape="true" />
																</button>
																</c:if>
															</fieldset>	
														</td>
													</tr>		
												</table>
											</td>
											<td>&nbsp;</td>
										</tr>
											
								</table>
								</div>
								<table>
									<tr>
										<td>
											<spring:message code="formularioHojaRuta.listaPlanificadoElemento.elementos" htmlEscape="true"/>	
										</td>
										<td>
											<input id="cantidadElementosSeleccionados" name="cantidadElementosSeleccionados" type="text" value="${elementoSelec}"
											<c:if test="${accion == 'CONSULTA'}">readonly="readonly"</c:if>/>
										</td>
										<td>
											<spring:message code="formularioHojaRuta.listaPlanificadoElemento.direcciones" htmlEscape="true"/>	
										</td>
										<td>
											<input type="text" id="direccionesSelec" name="direccionesSelec" value="${direccionesSelec}"
											<c:if test="${accion == 'CONSULTA'}">readonly="readonly"</c:if>/>
										</td>
										<td>
											<spring:message code="formularioHojaRuta.listaPlanificadoElemento.requerimientos" htmlEscape="true"/>	
										</td>
										<td>
											<input id="totalRequerimientos" name="totalRequerimientos" type="text" value="${totalRequerimientos}"
											<c:if test="${accion == 'CONSULTA'}">readonly="readonly"</c:if>/>
										</td>
										
									</tr>	
								</table>
							</div>
							<table>
								<tr>
									<td>
										<spring:message code="formularioHojaRuta.listaPlanificado.observaciones" htmlEscape="true"/>
									</td>
									<td>
										<textarea name="observacion" id="observacion" rows="3" cols="80" <c:if test="${accion == 'CONSULTA'}">readonly="readonly"</c:if>><c:out value="${hojaRuta.observacion}" default="" /></textarea>
									</td>
								</tr>
							</table>
								
							
									
					</fieldset>
				</form:form>
			</fieldset>
			
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
					<button name="guardar" type="button" class="botonCentrado" id="btnGuardar">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>

					<button name="cancelar" type="button" id="btnCancelar" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accion == 'CONSULTA'}">
				<div align="center">
					<button name="volver_atras" type="button" id="btnVolver" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight" style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>	
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
	<div id="pop" style="display:none">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
	</div>
	
</body>
</html>
