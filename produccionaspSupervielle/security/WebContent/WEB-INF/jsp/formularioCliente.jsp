<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioCliente.titulo.registar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioCliente.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioCliente.titulo.modificar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_cliente.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/doblelistas.js"></script>
      	<style type="text/css">
	  		.cascade-loading{																		
	    		background: transparent url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat center;
        	}
		</style>
    </head>		
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioCliente.titulo.registar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioCliente.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioCliente.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarCliente.html" commandName="clienteFormulario" method="post" modelAttribute="clienteFormulario">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${clienteFormulario.id}" default="" />"/>						
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioCliente.datosCliente" htmlEscape="true"/> 
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="width: 100%;" id="busquedaDiv" align="center">
								<table style="width: 100%;">
									<tr>
										<td colspan="4" style="text-align: left;">
											<fieldset>
												<legend>
													<spring:message code="formularioCliente.datosCliente.identificacion" htmlEscape="true"/>
												</legend>
												<table>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioCliente.datosCliente.identificacion.razonSocial" htmlEscape="true"/>
														</td>
														<td class="texto_ti">
															<spring:message code="formularioCliente.datosCliente.identificacion.nombreAbreviado" htmlEscape="true"/>
														</td>										
													</tr>
													<tr>
														<td class="texto_ti">
															<input type="text" id="persona.razonSocial" name="persona.razonSocial" style="width: 200px;" maxlength="60"																	
																value="<c:out value="${clienteFormulario.persona.razonSocial}" default="" />" class="requerido"
																<c:if test="${accion != 'NUEVO'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>
														<td class="texto_ti">
															<input type="text" id="nombreAbreviado" name="nombreAbreviado" style="width: 200px;" maxlength="30"			
																value="<c:out value="${clienteFormulario.nombreAbreviado}" default="" />" class="requerido lowerCase"
																<c:if test="${accion != 'NUEVO'}">
																	readonly="readonly"
																</c:if>
															/>

														</td>										
													</tr>
												</table>
											</fieldset>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<fieldset>
												<legend>
													<spring:message code="formularioCliente.datosCliente.direccion" htmlEscape="true"/>
												</legend>
												<table>
													<tr>
														<td>
															<fieldset style="border: none;">
																<table>
																	<tr>
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.pais" htmlEscape="true"/>
																		</td>
																	</tr>
																	<tr>	
																		<td class="texto_ti">
																			<select id="pais" name="pais" size="1" style="width: 190px;"
																				<c:if test="${accion == 'CONSULTA'}">
																					disabled="disabled"
																				</c:if>
																			>
																				<c:if test="${accion != 'NUEVO'}">
																					<c:forEach items="${paises}" var="p">
																						<option value="${p.id}"
																							<c:if test="${clienteFormulario.persona.direccion.barrio.localidad.provincia.pais.id == p.id }">
																								selected="selected"
																							</c:if>
																						>
																							<c:out value="${p.nombre}"/>
																						</option>
																					</c:forEach>
																				</c:if>
																				<c:if test="${accion == 'NUEVO'}">
																					<c:forEach items="${paises}" var="p">
																						<option value="${p.id}">
																							<c:out value="${p.nombre}"/>
																						</option>
																					</c:forEach>
																				</c:if>
																			</select>
																		</td>
																	</tr>
																	<tr>
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.provincia" htmlEscape="true"/>
																		</td>	
																	</tr>
																	<tr>
																		<td class="texto_ti">
																			<select id="provincia" name="provincia" size="1" style="width: 190px;"
																				<c:if test="${accion == 'CONSULTA'}">
																					disabled="disabled"
																				</c:if>
																			>
																				<c:if test="${accion != 'NUEVO'}">
																					<c:forEach items="${provincias}" var="p">
																						<option value="${p.id}"
																							<c:if test="${clienteFormulario.persona.direccion.barrio.localidad.provincia.id == p.id}">
																								selected="selected"
																							</c:if>
																						>
																							<c:out value="${p.nombre}"/>
																						</option>
																					</c:forEach>
																				</c:if>
																			</select>
																		</td>
																	</tr>
																	<tr>
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.localidad" htmlEscape="true"/>
																		</td>
																	</tr>
																	<tr>
																		<td class="texto_ti">
																			<select id="localidad" name="localidad" size="1" style="width: 190px;"
																				<c:if test="${accion == 'CONSULTA'}">
																					disabled="disabled"
																				</c:if>	
																			>
																				<c:if test="${accion != 'NUEVO'}">
																					<c:forEach items="${localidades}" var="l">
																						<option value="${l.id}"
																							<c:if test="${clienteFormulario.persona.direccion.barrio.localidad.id == l.id}">
																								selected="selected"
																							</c:if>
																						>
																							<c:out value="${l.nombre}"/>
																						</option>
																					</c:forEach>
																				</c:if>
																			</select>
																		</td>
																	</tr>
																	<tr>
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.barrio" htmlEscape="true"/>
																		</td>
																	</tr>
																	<tr>
																		<td class="texto_ti">
																			<select id="persona.direccion.idBarrio" name="persona.direccion.idBarrio" size="1" 
																			style="width: 190px;" class="requerido"
																				<c:if test="${accion == 'CONSULTA'}">
																					disabled="disabled"
																				</c:if>
																			>
																				<c:if test="${accion != 'NUEVO'}">
																					<c:forEach items="${barrios}" var="b">
																						<option value="${b.id}"
																							<c:if test="${clienteFormulario.persona.direccion.barrio.id == b.id}">
																								selected="selected"
																							</c:if>
																						>
																							<c:out value="${b.nombre}"/>
																						</option>
																					</c:forEach>
																				</c:if>
																			</select>
																		</td>
																	</tr>	
																</table>																		
															</fieldset>
														</td>
														<td>
															<fieldset style="border: none;">
																<table>
																	<tr>														
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.calle" htmlEscape="true"/>
																		</td>
																	</tr>	
																	<tr>																	
																		<td class="texto_ti">
																			<input type="text" id="persona.direccion.calle" name="persona.direccion.calle" 
																				style="width: 190px;" size="60" class="requerido capitalizar"
																				value="<c:out value="${clienteFormulario.persona.direccion.calle}" default="" />"
																					<c:if test="${accion == 'CONSULTA'}">
																						readonly="readonly"
																					</c:if>
																			/>
																		</td>											
																	</tr>	
																	<tr>															
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.numero" htmlEscape="true"/>
																		</td>	
																	</tr>	
																	<tr>															
																		<td class="texto_ti">
																			<input type="text" id="persona.direccion.numero" name="persona.direccion.numero" 
																				style="width: 50px;" maxlength="6" class="requerido"
																				value="<c:out value="${clienteFormulario.persona.direccion.numero}" default="" />"
																				<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
																				</c:if>
																			/>
																		</td>	
																	</tr>	
																	<tr>														
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.piso" htmlEscape="true"/>
																		</td>
																	</tr>	
																	<tr>																	
																		<td class="texto_ti">
																			<input type="text" id="persona.direccion.piso" name="persona.direccion.piso" 
																				style="width: 50px;" maxlength="3"
																				value="<c:out value="${clienteFormulario.persona.direccion.piso}" default="" />"
																				<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
																				</c:if>
																			/>
																		</td>		
																	</tr>	
																	<tr>														
																		<td class="texto_ti">
																			<spring:message code="formularioCliente.datosCliente.direccion.dpto" htmlEscape="true"/>
																		</td>
																	</tr>	
																	<tr>														
																		<td class="texto_ti">
																			<input type="text" id="persona.direccion.dpto" name="persona.direccion.dpto" 
																				style="width: 50px;" maxlength="1"
																				value="<c:out value="${clienteFormulario.persona.direccion.dpto}" default="" />"
																				<c:if test="${accion == 'CONSULTA'}">
																					readonly="readonly"
																				</c:if>
																			/>
																		</td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
												</table>												
											</fieldset>
										</td>	
										<td style="vertical-align: top;">
											<fieldset style="height: 222px;">
												<legend>
													<spring:message code="formularioCliente.datosCliente.datosAdicionales" htmlEscape="true"/>
												</legend>
												<table>
													<tr>
														<td class="texto_ti">
															<spring:message code="formularioCliente.datosCliente.datosAdicionales.tipoDocumento" htmlEscape="true"/>
														</td>
													</tr>	
													<tr>			
														<td class="texto_ti">
															<select id="idTipoDocSel" name="idTipoDocSel" size="1" style="width: 190px;" class="requerido"
																<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if>
															>
																<c:forEach items="${tiposDocumento}" var="tipo">
																	<option value="${tipo.id}"
																		<c:if test="${tipo.id == clienteFormulario.persona.tipoDoc.id}">
																			selected="selected"
																		</c:if>	
																	>
																		<c:out value="${tipo.nombre}"/>
																	</option>
																</c:forEach>
															</select>
														</td>		
													</tr>	
													<tr>																	
														<td class="texto_ti">
															<spring:message code="formularioCliente.datosCliente.datosAdicionales.numeroDocumento" htmlEscape="true"/>
														</td>	
													</tr>	
													<tr>	
														<td class="texto_ti">
															<input type="text" id="persona.numeroDoc" name="persona.numeroDoc" style="width: 190px;" maxlength="15"
																value="<c:out value="${clienteFormulario.persona.numeroDoc}" default="" />" class="requerido"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>	
													</tr>	
													<tr>																				
														<td class="texto_ti">
															<spring:message code="formularioCliente.datosCliente.datosAdicionales.telefono" htmlEscape="true"/>
														</td>	
													</tr>	
													<tr>	
														<td class="texto_ti">
															<input type="text" id="persona.telefono" name="persona.telefono" 
																style="width: 190px;" class="telefono" maxlength="20"
																value="<c:out value="${clienteFormulario.persona.telefono}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>	
													</tr>	
													<tr>																				
														<td class="texto_ti">
															<spring:message code="formularioCliente.datosCliente.datosAdicionales.mail" htmlEscape="true"/>
														</td>		
													</tr>	
													<tr>	
														<td class="texto_ti">
															<input type="text" id="persona.mail" name="persona.mail" 
																style="width: 190px;" maxlength="60" class="lowerCase"
																value="<c:out value="${clienteFormulario.persona.mail}" default="" />"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															/>
														</td>																	
													</tr>
												</table>
											</fieldset>	
										</td>
										<td style="vertical-align: top;">
											<fieldset style="height: 222px;">
												<legend>
													<spring:message code="formularioCliente.datosCliente.observaciones" htmlEscape="true"/>
												</legend>
												<table>
													<tr>
														<td class="texto_ti" rowspan="8">
															<textarea id="observaciones" name="observaciones" rows="10" 
																style="resize: none;" class="observacion"
																<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if>
															><c:out value="${clienteFormulario.observaciones}"/></textarea>
														</td>																				
													</tr>
												</table>
											</fieldset>
										</td>																			
									</tr>								
									<tr>
										<td colspan="4">
											<br style="font-size: xx-small;"/>																
											<fieldset>
												<table width="100%">
										          <thead>
										            <tr>
										              <th align="left" id="grupoImg">								 
												        	<font style="color:#003090">
												        		<spring:message code="formularioCliente.datosCliente.contacto" htmlEscape="true"/> 
												        	</font>
												        	<img id="grupoImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
												        	<img id="grupoImgSrc" style="DISPLAY: none" src="images/skip.png" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">													 	  
										              </th>
													 </tr>
													</thead>
												</table>
												<div id="grupoDiv" align="center">							
													<table>				
														<tr>
															<td colspan="4" style="text-align: left;">
																<fieldset>
																	<legend>
																		<spring:message code="formularioCliente.datosCliente.contacto.identificacion" htmlEscape="true"/>
																	</legend>
																	<table>
																		<tr>
																			<td class="texto_ti">
																				<spring:message code="formularioCliente.datosCliente.contacto.identificacion.nombre" htmlEscape="true"/>
																			</td>
																			<td class="texto_ti">
																				<spring:message code="formularioCliente.datosCliente.contacto.identificacion.apellido" htmlEscape="true"/>
																			</td>															
																		</tr>
																		<tr>	
																			<td class="texto_ti">
																				<input type="text" id="contacto.nombre" name="contacto.nombre" 
																					style="width: 200px;" maxlength="30" class="requerido capitalizar"
																					value="<c:out value="${clienteFormulario.contacto.nombre}" default="" />"
																					<c:if test="${accion == 'CONSULTA'}">
																						readonly="readonly"
																					</c:if>				
																				/>
																			</td>
																			<td class="texto_ti">
																				<input type="text" id="contacto.apellido" name="contacto.apellido" 
																					style="width: 200px;" maxlength="30" class="requerido capitalizar"
																					value="<c:out value="${clienteFormulario.contacto.apellido}" default="" />"
																					<c:if test="${accion == 'CONSULTA'}">
																						readonly="readonly"
																					</c:if>
																				/>
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
																		<spring:message code="formularioCliente.datosCliente.direccion" htmlEscape="true"/>
																	</legend>																	
																	<table>
																		<tr>
																			<td>
																				<fieldset style="border: none;">
																					<table>														
																						<tr>
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.pais" htmlEscape="true"/>
																							</td>
																						</tr>														
																						<tr>
																							<td class="texto_ti">
																								<select id="paisContacto" name="pais" size="1" style="width: 190px;"
																									<c:if test="${accion == 'CONSULTA'}">
																										disabled="disabled"
																									</c:if>
																								>																		
																									<c:forEach items="${paises}" var="p">
																										<option value="${p.id}"
																											<c:if test="${clienteFormulario.contacto.direccion.barrio.localidad.provincia.pais.id == p.id }">
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
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.provincia" htmlEscape="true"/>
																							</td>
																						</tr>														
																						<tr>
																							<td class="texto_ti">
																								<select id="provinciaContacto" name="provincia" size="1" style="width: 190px;"
																									<c:if test="${accion == 'CONSULTA'}">
																										disabled="disabled"
																									</c:if>
																								>																		
																									<c:forEach items="${provinciasCont}" var="p">
																										<option value="${p.id}"
																											<c:if test="${clienteFormulario.contacto.direccion.barrio.localidad.provincia.id == p.id}">
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
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.localidad" htmlEscape="true"/>
																							</td>
																						</tr>														
																						<tr>
																							<td class="texto_ti">
																								<select id="localidadContacto" name="localidad" size="1" style="width: 190px;"
																									<c:if test="${accion == 'CONSULTA'}">
																										disabled="disabled"
																									</c:if>
																								>																		
																									<c:forEach items="${localidadesCont}" var="l">
																										<option value="${l.id}"
																											<c:if test="${clienteFormulario.contacto.direccion.barrio.localidad.id == l.id}">
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
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.barrio" htmlEscape="true"/>
																							</td>
																						</tr>														
																						<tr>
																							<td class="texto_ti">
																								<select id="contacto.direccion.idBarrio" name="contacto.direccion.idBarrio" size="1" 
																								style="width: 190px;" class="requerido"
																									<c:if test="${accion == 'CONSULTA'}">
																										disabled="disabled"
																									</c:if>
																								>											
																									<c:forEach items="${barriosCont}" var="b">
																										<option value="${b.id}"
																											<c:if test="${clienteFormulario.contacto.direccion.barrio.id == b.id}">
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
																			<td>
																				<fieldset style="border: none;">
																					<table>
																						<tr>
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.calle" htmlEscape="true"/>
																							</td>
																						</tr>
																						<tr>
																							<td class="texto_ti">
																								<input type="text" id="contacto.direccion.calle" name="contacto.direccion.calle" 
																									maxlength="30" class="capitalizar"
																									value="<c:out value="${clienteFormulario.contacto.direccion.calle}" default="" />"
																									<c:if test="${accion == 'CONSULTA'}">
																										readonly="readonly"
																									</c:if>
																								/>
																							</td>
																						</tr>
																						<tr>
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.numero" htmlEscape="true"/>
																							</td>
																						</tr>
																						<tr>
																							<td class="texto_ti">
																								<input type="text" id="contacto.direccion.numero" name="contacto.direccion.numero" 
																									style="width: 50px;" maxlength="6"
																									value="<c:out value="${clienteFormulario.contacto.direccion.numero}" default="" />"
																									<c:if test="${accion == 'CONSULTA'}">
																										readonly="readonly"
																									</c:if>
																								/>
																							</td>
																						</tr>
																						<tr>
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.piso" htmlEscape="true"/>
																							</td>
																						</tr>
																						<tr>
																							<td class="texto_ti">
																								<input type="text" id="contacto.direccion.piso" name="contacto.direccion.piso" 
																									style="width: 50px;" maxlength="3"
																									value="<c:out value="${clienteFormulario.contacto.direccion.piso}" default="" />"
																									<c:if test="${accion == 'CONSULTA'}">
																										readonly="readonly"
																									</c:if>
																								/>
																							</td>
																						</tr>
																						<tr>
																							<td class="texto_ti">
																								<spring:message code="formularioCliente.datosCliente.direccion.dpto" htmlEscape="true"/>
																							</td>
																						</tr>
																						<tr>
																							<td class="texto_ti">
																								<input type="text" id="contacto.direccion.dpto" name="contacto.direccion.dpto" 
																									style="width: 50px;" maxlength="1"
																									value="<c:out value="${clienteFormulario.contacto.direccion.dpto}" default="" />"
																									<c:if test="${accion == 'CONSULTA'}">
																										readonly="readonly"
																									</c:if>
																								/>
																							</td>
																						</tr>
																					</table>
																				</fieldset>	
																			</td>
																		</tr>
																	</table>																																	
																</fieldset>	
															</td>															
															<td style="vertical-align: top;">
																<fieldset style="height: 220px;">
																	<legend>
																		<spring:message code="formularioCliente.datosCliente.datosAdicionales" htmlEscape="true"/>
																	</legend>
																	<table>																	
																		<tr>
																			<td class="texto_ti">
																				<spring:message code="formularioCliente.datosCliente.datosAdicionales.telefono" htmlEscape="true"/>
																			</td>
																		</tr>
																		<tr>
																			<td class="texto_ti">
																				<input type="text" id="contacto.telefono" name="contacto.telefono" 
																					style="width: 200px;" maxlength="20"
																					value="<c:out value="${clienteFormulario.contacto.telefono}" default="" />"
																					<c:if test="${accion == 'CONSULTA'}">
																						readonly="readonly"
																					</c:if>
																				/>
																			</td>
																		</tr>
																		<tr>
																			<td class="texto_ti">
																				<spring:message code="formularioCliente.datosCliente.contacto.mail" htmlEscape="true"/>
																			</td>
																		</tr>
																		<tr>
																			<td class="texto_ti">
																				<input type="text" id="contacto.mail" name="contacto.mail" 
																						style="width: 200px;" maxlength="60" class="requerido lowerCase"
																						value="<c:out value="${clienteFormulario.contacto.mail}" default="" />"
																						<c:if test="${accion == 'CONSULTA'}">
																							readonly="readonly"
																						</c:if>
																				/>
																			</td>
																		</tr>
																	</table>
																</fieldset>
															</td>
															<td style="vertical-align: top;">
																<fieldset style="height: 220px;">
																	<legend>
																		<spring:message code="formularioCliente.datosCliente.datosAdicionales" htmlEscape="true"/>
																	</legend>
																	<table>
																		<tr>																		
																			<td class="texto_ti">
																				<spring:message code="formularioCliente.datosCliente.contacto.username" htmlEscape="true"/>
																			</td>
																		</tr>
																		<tr>
																			<td class="texto_ti">
																				<input type="text" id="user.username" name="user.username" 
																					style="width: 150px;" maxlength="30" class="requerido"
																						<c:if test="${accion == 'NUEVO'}">
																							value="<c:out value="admin" default="" />"
																						</c:if>	
																						<c:if test="${accion != 'NUEVO'}">
																							value="<c:out value="${clienteFormulario.user.usernameSinCliente}" default="" />"																							
																						</c:if>
																						readonly="readonly"
																					/>
																			</td>
																		</tr>	
																		<c:if test="${accion == 'NUEVO'}">
																			<tr>	
																				<td class="texto_ti">
																					<spring:message code="formularioCliente.datosCliente.contacto.password" htmlEscape="true"/>
																				</td>
																			</tr>
																			<tr>
																				<td class="texto_ti">				
																					<input type="password" id="user.password" name="user.password" 
																						style="width: 150px;" maxlength="30"/>																	
																				</td>																
																			</tr>
																			<tr>	
																				<td class="texto_ti">
																					<spring:message code="formularioCliente.datosCliente.contacto.passwordConfirmar" htmlEscape="true"/>
																				</td>
																			</tr>
																			<tr>
																				<td class="texto_ti">				
																					<input type="password" id="user.confirmarContrasenia" name="user.confirmarContrasenia" 
																						style="width: 150px;" maxlength="30"/>
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
									    </td>
									</tr>
									<c:if test="${accion != 'CONSULTA'}">
										<tr>
											<td class="texto_ti">
												<button name="restablecer" type="reset" >
													<img src="<%=request.getContextPath()%>/images/restablecer.png" 
														title=<spring:message code="botones.restablecer" htmlEscape="true"/> 
													> 								 
												</button>
											</td>											
										</tr>
									</c:if>
						    	</table>
							</div>
					    </fieldset>
					</form:form>
				</fieldset>
				<br style="font-size: xx-small;"/>
			    <c:if test="${accion != 'CONSULTA'}">
			    	<div align="center">
						<button name="guardar" type="button" onclick="guardarYSalir();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.guardar" htmlEscape="true"/>  
						</button>
						&nbsp;
						<button name="cancelar" type="button"  onclick="volverCancelar();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
							<spring:message code="botones.cancelar" htmlEscape="true"/>  
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
				<br style="font-size: xx-small;"/>
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 130%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>	
		<jsp:include page="fieldAvisos.jsp"/>	
	</body>
</html>