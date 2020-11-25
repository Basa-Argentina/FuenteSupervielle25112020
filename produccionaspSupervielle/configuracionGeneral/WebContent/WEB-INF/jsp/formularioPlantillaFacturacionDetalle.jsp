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
	<title> 
	</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_plantillaFacturacionDetalle.js"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})" style="background-color: #FFFFFF; width: 930px; padding: 0px 0px 0px 0px;">
	<div class="contextMenu" id="myMenu1">
		    <ul>	 	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	</div> 
	<div id="contenido" align="center" style="width:930px; min-height:400px;margin:0px;padding:0px;background-color: #FFF;" align="center">
		
		<fieldset style="border: none; text-align: left; width: 97%;">
			
			<br />
			<form:form name="formPlantillaFacturacionDetalle" action="guardarActualizarPlantillaFacturacionDetalle.html" 
				commandName="plantillaFacturacionDetalleFormulario" method="post" modelAttribute="plantillaFacturacionDetalleFormulario">
			
				<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />" />
				<input type="hidden" id="accionDetalle" name="accionDetalle" value="<c:out value="${accionDetalle}" default="" />" />
				<input type="hidden" id="id" name="id" value="<c:out value="${plantillaFacturacionDetalleFormulario.id}" default="" />" />
				<input type="hidden" id="idPlantilla" name="idPlantilla" value="<c:out value="${idPlantilla}" default="" />" />
				<input type="hidden" id="ordenAnterior" name="ordenAnterior" value="<c:out value="${plantillaFacturacionDetalleFormulario.ordenAnterior}" default="" />" />
				
				<c:if test="${accion!='CONSULTA'}">
				<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioPlantillaFacturacionDetalle.agregarDetalle"
												htmlEscape="true" /> </font> <img id="busquedaImgSrcDown"
										src="images/skip_down.png"
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
									<td>
										
											<table>
												<tr>
													<td class="texto_ti" style="padding-right: 200px;">
														<spring:message code="formularioPlantillaFacturacionDetalle.concepto" htmlEscape="true"/>
													</td>
													<td class="texto_ti" style="padding-right: 200px;">
														<spring:message code="formularioPlantillaFacturacionDetalle.descripcion" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id=codigoConcepto name="codigoConcepto"
														maxlength="6" style="width: 50px;"
														value="<c:out value="${plantillaFacturacionDetalleFormulario.codigoConcepto}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
														</c:if> /> 
															<button type="button" name="buscaConcepto" id="buscaConcepto"
																	title="<spring:message code="textos.buscar" htmlEscape="true"/>"
																	<c:if test="${accion == 'CONSULTA'}">
																							disabled="disabled"
																	</c:if>>
																	<img src="<%=request.getContextPath()%>/images/buscar.png">
																</button>&nbsp;&nbsp; 
															<label id="codigoConceptoLabel" for="codigoConcepto">
															</label>
													</td>
													<td class="texto_ti">
														<textarea id="descripcionConcepto" name="descripcionConcepto" rows="2" cols="50" class="requerido">
															<c:out value="${plantillaFacturacionDetalleFormulario.descripcionConcepto}" default="" />
														</textarea>
													</td>
												</tr>
												<tr>
													<td class="texto_ti" style="padding-right: 200px;">
														<spring:message code="formularioPlantillaFacturacionDetalle.orden" htmlEscape="true"/>
													</td>
													<td class="texto_ti" style="padding-right: 200px;">
														<spring:message code="formularioPlantillaFacturacionDetalle.cantidadSinCosto" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<input type="text" id="orden" name="orden" class="integer requerido" 
														value="<c:out value="${plantillaFacturacionDetalleFormulario.orden}" default="${ordenDefecto}" />"/>
													</td>
													<td class="texto_ti">
														<input type="text" id="cantidadSinCosto" name="cantidadSinCosto" class="integer requerido"
														value="<c:out value="${plantillaFacturacionDetalleFormulario.cantidadSinCosto}" default="0" />"/>
														
														
															<button name="agregar" type="submit" id="agregar">
																<img src="<%=request.getContextPath()%>/images/add.png">
																
																<c:if test="${accionDetalle!=null && accionDetalle=='MODIFICACION'}">
																	<spring:message code="botones.modificar" htmlEscape="true" />
																</c:if>
																<c:if test="${accionDetalle==null || accionDetalle!='MODIFICACION'}">
																	<spring:message code="botones.guardar" htmlEscape="true" />
																</c:if>
															</button>
															<c:if test="${accionDetalle!=null && accionDetalle=='MODIFICACION'}">
																<button name="cancelarMod" type="button" id="cancelarMod">
																	<img src="<%=request.getContextPath()%>/images/cancelar.png">
																		<spring:message code="botones.cancelar" htmlEscape="true" />
																</button>
															</c:if>
														
													</td>	
												</tr>
											</table>
									
									</td>
								</tr>

							</table>
						</div>
					</fieldset>
				</c:if>	
				<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="detalles" id="plantillaFacturacionDetalle" requestURI="mostrarLoteFacturacionDetalle.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${plantillaFacturacionDetalle.id}"/>
			              	</display:column>
			              	<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_orden" value="${plantillaFacturacionDetalle.orden}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>			
			              	<display:column property="orden" titleKey="formularioPlantillaFacturacionDetalle.orden" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="codigoConcepto" titleKey="formularioPlantillaFacturacionDetalle.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="descripcionConcepto" titleKey="formularioPlantillaFacturacionDetalle.descripcion" sortable="true" class="celdaAlineadoIzquierda" maxLength="50"/>
		           		   	<display:column property="cantidadSinCosto" titleKey="formularioPlantillaFacturacionDetalle.cantidadSinCosto" sortable="true" class="celdaAlineadoIzquierda"/>
						</display:table>
						
				</fieldset>				
			</form:form>
		</fieldset>
		
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 100%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
	<div class="selectorDiv" style="position:absolute; top:-150px;"></div>
</body>
</html>