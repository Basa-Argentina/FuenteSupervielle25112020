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
			<spring:message code="formularioConceptoOperacionCliente.titulo.administrar" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/mavalos_consulta_conceptoOperacionCliente.js"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
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
			        		<spring:message code="formularioConceptoOperacionCliente.titulo.administrar" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarConceptoOperacionCliente.html" commandName="conceptoOperacionClienteBusqueda" method="post">
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
						<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />"/>
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
												code="formularioConceptoOperacionCliente.cliente" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.fechaDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.fechaHasta" htmlEscape="true" />
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
											<input type="text" id="codigoCliente" name="codigoCliente" style="width: 50px;"
												value="<c:out value="${busquedaLoteReferenciaFormulario.codigoCliente}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaCliente" 
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoClienteLabel" for="codigoCliente"> 
												 
											</label>
										</td>			
										<td class="texto_ti">
														<input type="text" id="fechaDesde" name="fechaDesde" 
															maxlength="10" 
															value="<c:out value="${conceptoOperacionClienteBusqueda.fechaDesdeStr}" default="" />" />
															
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'conceptoOperacionClienteBusqueda',
																	// input name
																	'controlname': 'fechaDesde'
																});
															</script></c:if>
													</td>										
													<td class="texto_ti">
														<input type="text" id="fechaHasta" name="fechaHasta" 
															maxlength="10" 
															value="<c:out value="${conceptoOperacionClienteBusqueda.fechaHastaStr}" default="" />" />
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'conceptoOperacionClienteBusqueda',
																	// input name
																	'controlname': 'fechaHasta'
																});
															</script></c:if>
													</td>
										
									</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.estado" htmlEscape="true" />
										</td>	
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.tipoConcepto" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.cantidadDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.cantidadHasta" htmlEscape="true" />
										</td>									
									</tr>
									<tr>
										<td class="texto_ti">
											<select id="estado"
												name="estado" size="1">
													<option label="Seleccione un Estado" value="">Seleccione un Estado</option>
													<option label="Pendiente" value="Pendiente" <c:if test="${conceptoOperacionClienteBusqueda.estado == 'Pendiente'}">
															selected="selected"
														</c:if>>Pendiente</option>
													<option label="Facturado" value="Facturado" <c:if test="${conceptoOperacionClienteBusqueda.estado == 'Facturado'}">
															selected="selected"
														</c:if>>Facturado</option>
													<option label="Cancelado" value="Cancelado" <c:if test="${conceptoOperacionClienteBusqueda.estado == 'Cancelado'}">
															selected="selected"
														</c:if>>Cancelado</option>
											</select>
										</td>		
										<td class="texto_ti"><select id="tipoConcepto"
										name="tipoConcepto" size="1"
										<c:if test="${accion == 'CONSULTA'}">disabled="disabled"</c:if>>
											<option label="Todos" value="">Todos</option>
													<option label="Automatico" value="Automatico" <c:if test="${conceptoOperacionClienteBusqueda.tipoConcepto == 'Automatico'}">
															selected="selected"
														</c:if>>Automatico</option>
													<option label="Manual" value="Manual" <c:if test="${conceptoOperacionClienteBusqueda.tipoConcepto == 'Manual'}">
															selected="selected"
														</c:if>>Manual</option>
									</select></td>
									<td class="texto_ti"><input type="text" id="cantidadDesde"
										name="cantidadDesde" maxlength="12" size="12"
										value="<c:out value="${conceptoOperacionClienteBusqueda.cantidadDesde}" default="" />" />
									</td>
									<td class="texto_ti"><input type="text" id="cantidadHasta"
										name="cantidadHasta" maxlength="12" size="12"
										value="<c:out value="${conceptoOperacionClienteBusqueda.cantidadDesde}" default="" />" />
									</td>																									
									</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.numeroRequerimientoDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.numeroRequerimientoHasta" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.finalUnitarioDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.finalUnitarioHasta" htmlEscape="true" />
										</td>	
									</tr>
									<tr>
									
									<td class="texto_ti">
									<table><tr><td><input type="text" id="prefijoRequerimientoDesde"
										name="prefijoRequerimientoDesde" maxlength="4" size="4" class="completarZeros inputTextNumericPositiveIntegerOnly"
										value="<c:out value="${conceptoOperacionClienteBusqueda.prefijoRequerimientoDesde}" default="" />" /> -
										</td>
										<td>
										<input type="text" id="numeroRequerimientoDesde"
										name="numeroRequerimientoDesde" maxlength="8" size="8" class="completarZeros inputTextNumericPositiveIntegerOnly"
										value="<c:out value="${conceptoOperacionClienteBusqueda.numeroRequerimientoDesde}" default="" />" />
										</td>
										</tr>
									</table>
									</td>
									<td class="texto_ti">
									<table><tr><td><input type="text" id="prefijoRequerimientoHasta"
										name="prefijoRequerimientoHasta" maxlength="4" size="4" class="completarZeros inputTextNumericPositiveIntegerOnly"
										value="<c:out value="${conceptoOperacionClienteBusqueda.prefijoRequerimientoHasta}" default="" />" /> -
										</td>
										<td><input type="text" id="numeroRequerimientoHasta"
										name="numeroRequerimientoHasta" maxlength="8" size="8" class="completarZeros inputTextNumericPositiveIntegerOnly"
										value="<c:out value="${conceptoOperacionClienteBusqueda.numeroRequerimientoHasta}" default="" />" />
									</td>
									</tr>
									</table>
									<td class="texto_ti"><input type="text" id="finalUnitarioDesde"
										name="finalUnitarioDesde" maxlength="12" size="12" 
										value="<c:out value="${conceptoOperacionClienteBusqueda.finalUnitarioDesde}" default="" />" />
									</td>
									<td class="texto_ti"><input type="text" id="finalUnitarioHasta"
										name="finalUnitarioHasta" maxlength="12" size="12" 
										value="<c:out value="${conceptoOperacionClienteBusqueda.finalUnitarioHasta}" default="" />" />
									</td>	
								</tr>
								<tr>
									<td>
										<spring:message
												code="formularioConceptoOperacionCliente.vacio" htmlEscape="true" />
									</td>
									<td>
										<spring:message
												code="formularioConceptoOperacionCliente.vacio" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.finalTotalDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioConceptoOperacionCliente.finalTotalHasta" htmlEscape="true" />
										</td>
								</tr>
								<tr>
									<td>
										<spring:message
												code="formularioConceptoOperacionCliente.vacio" htmlEscape="true" />
									</td>
									<td>
										<spring:message
												code="formularioConceptoOperacionCliente.vacio" htmlEscape="true" />
									</td>
									<td class="texto_ti"><input type="text" id="finalTotalDesde"
										name="finalTotalDesde" maxlength="12" size="12"
										value="<c:out value="${conceptoOperacionClienteBusqueda.finalTotalDesde}" default="" />" />
									</td>
									<td class="texto_ti"><input type="text" id="finalTotalHasta"
										name="finalTotalHasta" maxlength="12" size="12"
										value="<c:out value="${conceptoOperacionClienteBusqueda.finalTotalHasta}" default="" />" />
									</td>	
								</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="conceptosOperacionCliente" id="conceptoOperacionCliente" requestURI="mostrarConceptoOperacionCliente.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${conceptoOperacionCliente.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column class="celdaAlineadoCentrado" title="<input type='checkbox' id='checktodos' name='checktodos'/>">
							    <input type="checkbox" class='checklote' value="${conceptoOperacionCliente.id}"/>
			              	</display:column>
			              	<display:column property="conceptoFacturable.codigo" titleKey="formularioConceptoOperacionCliente.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
			              	<display:column property="conceptoFacturable.descripcion" titleKey="formularioConceptoOperacionCliente.concepto" sortable="true" class="celdaAlineadoIzquierda"/>			
			              	<display:column property="clienteEmp.razonSocialONombreYApellido" titleKey="formularioConceptoOperacionCliente.cliente" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="fechaAltaStr" titleKey="formularioConceptoOperacionCliente.fecha" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="tipoConcepto" titleKey="formularioConceptoOperacionCliente.tipoConcepto" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="finalUnitario" titleKey="formularioConceptoOperacionCliente.finalUnitario" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="cantidad" titleKey="formularioConceptoOperacionCliente.cantidad" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="finalTotal" titleKey="formularioConceptoOperacionCliente.finalTotal" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="estadoStr" titleKey="formularioConceptoOperacionCliente.estado" sortable="true" class="celdaAlineadoIzquierda" />
						  	<display:column property="codigoSeriePrefijoNumero" titleKey="formularioConceptoOperacionCliente.requerimiento" sortable="true" class="celdaAlineadoIzquierda"/>
						</display:table>
					<div style="width: 100%" align="right">
						<table>
							<tr>
								<td>
									<button id="imprimir" name="imprimir" type="button" 
										>
										<img src="<%=request.getContextPath()%>/images/impresora.gif">
										<spring:message code="botones.imprimir" htmlEscape="true" />
									</button>
								</td>
								<td>
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
		<div class="selectorDiv"></div>
	</body>
</html>