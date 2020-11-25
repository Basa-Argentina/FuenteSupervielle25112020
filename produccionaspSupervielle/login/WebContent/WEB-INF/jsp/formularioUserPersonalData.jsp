<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>		
		<spring:message code="formularioUserPersonalData.tituloForm" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> <spring:message code="general.ambiente" htmlEscape="true"/>
	</title>		
	<jsp:include page="styles.jsp"/>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>  
	<script language="JavaScript" src="js/mavalos_jquery.tools.min.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/mavalos_formulario_user_personal_data.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/ini.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/httprequest.js"></script>
    <script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
    <script type="text/javascript" src="js/jquery.alerts.js"></script>
	<meta content="MSHTML 8.00.6001.18852" name="GENERATOR">
</head>
<body onload="mostrarErrores(${errores});">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp"/>
		<div class="contenido" align="center">
			<br>
			<fieldset style="border: none;">
				<legend align="left">
		        	<font class="tituloForm" size="5">
		        		<spring:message code="formularioUserPersonalData.tituloForm" htmlEscape="true"/> 
	        		</font>		  
				</legend>
			</fieldset>
			<br style="font-size: xx-small">
			<table>
				<tr>
					<td>
						<form:form action="guardarActualizarUserPersonalData.html" method="post" modelAttribute="userPersonalDataFormulario">
							<input type="hidden" id="persona.id" name="persona.id" value="<c:out value="${userPersonalDataFormulario.persona.id}" default="" />"/>	
	       					<fieldset>
								<table width="100%">
									<thead>
										<tr>
											<th  align="left" id="caractImg">
												<font style="color:#003090"><spring:message code="textos.datosCaracteristicos" htmlEscape="true"/></font>
					       					</th>
				       					</tr>
				       				</thead>
		       					</table>
		       					<div class="seccion3">
									<table>
										<tr>
											<td colspan="3">
												<fieldset>
													<legend>
														<spring:message code="formularioUserPersonalData.datospersonales" htmlEscape="true"/>
													</legend>
													<table>		
														<tr>
															<td class="texto_label" >
																<spring:message code="formularioUserPersonalData.datospersonales.nombre" htmlEscape="true"/>
															</td>
															<td style="width: 20px;"></td>
															<td class="texto_label" >
																<spring:message code="formularioUserPersonalData.datospersonales.apellido" htmlEscape="true"/>
															</td>															
														</tr>	
														<tr>
															<td class="texto_field">
																<input type="text" id="persona.nombre" name="persona.nombre" style="width: 200px;" class="requerido"
																	value="<c:out value="${userPersonalDataFormulario.persona.nombre}" default="" />"/>
															</td>					
															<td style="width: 20px;"></td>																									
															<td class="texto_field">
																<input type="text" id="persona.apellido" name="persona.apellido" style="width: 200px;" class="requerido"
																	value="<c:out value="${userPersonalDataFormulario.persona.apellido}" default="" />"/>
															</td>										
														</tr>
													</table>
												</fieldset>			
											</td>
										</tr>
										<tr>
											<td style="vertical-align: top;">
												<fieldset style="height: 220px;">
													<legend>
														<spring:message code="formularioUserPersonalData.ubicacion" htmlEscape="true"/>
													</legend>
														<table>														
															<tr>
																<td class="texto_label">
																	<spring:message code="formularioUserPersonalData.ubicacion.pais" htmlEscape="true"/>
																</td>
															</tr>														
															<tr>
																<td class="texto_label">
																	<select id="pais" name="pais" size="1" style="width: 190px;">																		
																		<c:forEach items="${paises}" var="p">
																			<option value="${p.id}"
																				<c:if test="${userPersonalDataFormulario.persona.direccion.barrio.localidad.provincia.pais.id == p.id }">
																					selected="selected"
																				</c:if>
																			>
																				<c:out value="${p.nombre}"/>
																			</option>
																		</c:forEach>																																
																	</select>
																</td>
															</tr>														
															<tr>
																<td class="texto_label">
																	<spring:message code="formularioUserPersonalData.ubicacion.provincia" htmlEscape="true"/>
																</td>
															</tr>														
															<tr>
																<td class="texto_label">
																	<select id="provincia" name="provincia" size="1" style="width: 190px;">																		
																		<c:forEach items="${provincias}" var="p">
																			<option value="${p.id}"
																				<c:if test="${userPersonalDataFormulario.persona.direccion.barrio.localidad.provincia.id == p.id}">
																					selected="selected"
																				</c:if>
																			>
																				<c:out value="${p.nombre}"/>
																			</option>
																		</c:forEach>																	
																	</select>
																</td>
															</tr>														
															<tr>
																<td class="texto_label">
																	<spring:message code="formularioUserPersonalData.ubicacion.localidad" htmlEscape="true"/>
																</td>
															</tr>														
															<tr>
																<td class="texto_label">
																	<select id="localidad" name="localidad" size="1" style="width: 190px;">																		
																		<c:forEach items="${localidades}" var="l">
																			<option value="${l.id}"
																				<c:if test="${userPersonalDataFormulario.persona.direccion.barrio.localidad.id == l.id}">
																					selected="selected"
																				</c:if>
																			>
																				<c:out value="${l.nombre}"/>
																			</option>
																		</c:forEach>																		
																	</select>
																</td>
															</tr>														
															<tr>
																<td class="texto_label">
																	<spring:message code="formularioUserPersonalData.ubicacion.barrio" htmlEscape="true"/>
																</td>
															</tr>														
															<tr>
																<td class="texto_label">
																	<select id="persona.direccion.idBarrio" name="persona.direccion.idBarrio" size="1" style="width: 190px;">											
																		<c:forEach items="${barrios}" var="b">
																			<option value="${b.id}"
																				<c:if test="${userPersonalDataFormulario.persona.direccion.barrio.id == b.id}">
																					selected="selected"
																				</c:if>
																			>
																				<c:out value="${b.nombre}"/>
																			</option>
																		</c:forEach>																		
																	</select>
																</td>
															</tr>														
														</table>	
												</fieldset>	
											</td>
											<td style="vertical-align: top;">
												<fieldset style="height: 220px;">
													<legend>
														<spring:message code="formularioUserPersonalData.direccion" htmlEscape="true"/>
													</legend>
													<table>
														<tr>
															<td class="texto_label">
																<spring:message code="formularioUserPersonalData.direccion.calle" htmlEscape="true"/>
															</td>
														</tr>
														<tr>
															<td class="texto_label">
																<input type="text" id="persona.direccion.calle" name="persona.direccion.calle" style="width: 100px;"
																		value="<c:out value="${userPersonalDataFormulario.persona.direccion.calle}" default="" />"/>
															</td>
														</tr>
														<tr>
															<td class="texto_label">
																<spring:message code="formularioUserPersonalData.direccion.numero" htmlEscape="true"/>
															</td>
														</tr>
														<tr>
															<td class="texto_label">
																<input type="text" id="persona.direccion.numero" name="persona.direccion.numero" style="width: 100px;"
																		value="<c:out value="${userPersonalDataFormulario.persona.direccion.numero}" default="" />"/>
															</td>
														</tr>
														<tr>
															<td class="texto_label">
																<spring:message code="formularioUserPersonalData.direccion.piso" htmlEscape="true"/>
															</td>
														</tr>
														<tr>
															<td class="texto_label">
																<input type="text" id="persona.direccion.piso" name="persona.direccion.piso" style="width: 100px;"
																	value="<c:out value="${userPersonalDataFormulario.persona.direccion.piso}" default="" />"/>
															</td>
														</tr>
														<tr>
															<td class="texto_label">
																<spring:message code="formularioUserPersonalData.direccion.dpto" htmlEscape="true"/>
															</td>
														</tr>
														<tr>
															<td class="texto_label">
																<input type="text" id="persona.direccion.dpto" name="persona.direccion.dpto" style="width: 100px;"
																	value="<c:out value="${userPersonalDataFormulario.persona.direccion.dpto}" default="" />"/>
															</td>
														</tr>
													</table>
												</fieldset>
											</td>		
											<td style="vertical-align: top;">
												<fieldset style="height: 220px;">
													<legend>
														<spring:message code="formularioUserPersonalData.contacto" htmlEscape="true"/>
													</legend>
													<table>														
														<tr>
															<td class="texto_label" >
																<spring:message code="formularioUserPersonalData.contacto.mail" htmlEscape="true"/>
															</td>
														</tr>
														<tr>
															<td class="texto_field">
																<input type="text" id="persona.mail" name="persona.mail" style="width: 200px;" class="requerido"
																		value="<c:out value="${userPersonalDataFormulario.persona.mail}" default="" />"/>
															</td>															
														</tr>
														<tr>
															<td class="texto_label" >
																<spring:message code="formularioUserPersonalData.contacto.telefono" htmlEscape="true"/>
															</td>
														</tr>
														<tr>
															<td class="texto_field">
																<input type="text" id="persona.telefono" name="persona.telefono" style="width: 150px;"
																		value="<c:out value="${userPersonalDataFormulario.persona.telefono}" default="" />"/>
															</td>
														</tr>
													</table>
												</fieldset>
											</td>			
										</tr>
										<tr>
											<td colspan="3">
												<fieldset>
													<legend>
														<spring:message code="formularioUserPersonalData.datosPorDefecto" htmlEscape="true"/>
													</legend>
													<table style="width: 100%;">		
														<tr>
															<td class="texto_label" >
																<spring:message code="formularioUserPersonalData.datosPorDefecto.empresa" htmlEscape="true"/>
															</td>															
															<td class="texto_label" >
																<spring:message code="formularioUserPersonalData.datosPorDefecto.sucursal" htmlEscape="true"/>
															</td>															
														</tr>	
														<tr>
															<td class="texto_field"><input type="text" id="codigoEmpresa"
																name="codigoEmpresa" maxlength="6" style="width: 50px;"
																value="<c:out value="${userPersonalDataFormulario.persona.empresaDefecto.codigo}" default="" />"/>
																&nbsp;&nbsp;
																<button type="button"
																	onclick="abrirPopupEmpresa('empresasPopupMap');"
																	title="<spring:message code="textos.buscar" htmlEscape="true"/>">
																	<img src="<%=request.getContextPath()%>/images/buscar.png">
																</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
																for="codigoEmpresa"> <c:out
																		value="${userPersonalDataFormulario.persona.empresaDefecto.descripcion}"
																		default="" /> </label>
															</td>
															<td class="texto_field"><input type="text" id="codigoSucursal"
																name="codigoSucursal" maxlength="6" style="width: 50px;"
																value="<c:out value="${userPersonalDataFormulario.persona.sucursalDefecto.codigo}" default="" />"/>
																&nbsp;&nbsp;&nbsp;&nbsp;
																<button type="button"
																	onclick="abrirPopupSucursal('sucursalesPopupMap', '<spring:message code="notif.seleccionEmpresa" htmlEscape="true"/>'
																								, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
																	title="<spring:message code="textos.buscar" htmlEscape="true"/>">
																	<img src="<%=request.getContextPath()%>/images/buscar.png">
																</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
																for="codigoSucursal"> <c:out
																		value="${userPersonalDataFormulario.persona.sucursalDefecto.descripcion}"
																		default="" /> </label>
															</td>										
														</tr>
													</table>
												</fieldset>			
											</td>
										</tr>										
										<tr>
											<td align="left">
												<button name="restablecer" type="reset" 
													title=<spring:message code="botones.restablecer" htmlEscape="true"/> 
												>
													<img src="<%=request.getContextPath()%>/images/restablecer.png"> 								 
												</button>														
											</td>
										</tr>
									</table>
								</div>
							</fieldset>
						</form:form>
					</td>
				</tr>
			</table>
			<br style="line-height:0;font-size: 15px;">
			<div align="center">
				<button name="guardar" type="button" onclick="guardar()" class="botonCentrado"> <img src="<%=request.getContextPath()%>/images/ok.png"> 
					<spring:message code="botones.guardar" htmlEscape="true"/>  
				</button>
				&nbsp;
				<button name="cancelar" type="button"  onclick="volverCancelar();" class="botonCentrado"><img src="<%=request.getContextPath()%>/images/cancelar.png"> 
					<spring:message code="botones.cancelar" htmlEscape="true"/>  
				</button>						
			</div>
			<br style="font-size: xx-small;"/>
		</div>
		<jsp:include page="footer.jsp"/>	
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="sucursalesPopupMap" /> 
		<jsp:param name="clase" value="sucursalesPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="empresasPopupMap" /> 
		<jsp:param name="clase" value="empresasPopupMap" /> 
	</jsp:include>
</body>
</html>