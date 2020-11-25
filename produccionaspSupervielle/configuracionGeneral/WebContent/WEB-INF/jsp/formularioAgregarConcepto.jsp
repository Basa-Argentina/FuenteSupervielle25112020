<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioAgregarConcepto.titulo.registrar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioAgregarConcepto.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioAgregarConcepto.titulo.consultar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>		
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_agregar_concepto.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
    </head>		
	<body onload="mostrarErrores(${errores});">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioAgregarConcepto.titulo.registrar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioAgregarConcepto.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioAgregarConcepto.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarAgregarConcepto.html" modelAttribute="detalle" method="post">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="urlDestino" name="urlDestino" value="<c:out value="${detalle.urlDestino}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${detalle.id}" default="" />"/>	
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>					
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioConceptoFacturable.datos" htmlEscape="true"/> 
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="width: 100%;" id="busquedaDiv" align="center">								
								<fieldset>
									<legend>
										<spring:message code="formularioAgregarConcepto.datos" htmlEscape="true"/>
									</legend>
									<table>													
										<tr>	
											<td class="texto_ti">
												<spring:message code="formularioAgregarConcepto.datos.concepto" htmlEscape="true"/>
											</td>										
										</tr>
										<tr>
											<td class="texto_ti">
												<input type="text" id="conceptoCodigo" name="conceptoCodigo" maxlength="6" style="width: 50px;"
													value="<c:out value="${detalle.conceptoFacturable.codigo}" default="" />" class="requerido upperCase alphaNumeric"
													<c:if test="${accion == 'CONSULTA' || detalle.conceptoFacturable != null}">
														readonly="readonly"
													</c:if>
												/>
												<button type="button" onclick="abrirPopup('conceptosPopupMap');"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${accion == 'CONSULTA' || detalle.conceptoFacturable != null}">
														disabled="disabled"
													</c:if>
												>
													<img src="<%=request.getContextPath()%>/images/buscar.png" > 
												</button>&nbsp;&nbsp; 
												<label id="conceptoCodigoLabel" for="conceptoCodigo">
													<c:out value="${detalle.conceptoFacturable.descripcion}" default="" />
												</label>
											</td>
										</tr>
										<tr>	
											<td class="texto_ti">
												<spring:message code="formularioAgregarConcepto.datos.listaPrecios" htmlEscape="true"/>
											</td>										
										</tr>
										<tr>	
											<td class="texto_ti">
												<input type="text" id="listaPreciosCodigo" name="listaPreciosCodigo" style="width: 50px;" maxlength="6"
													value="<c:out value="${detalle.listaPrecios.codigo}" default="" />" class="requerido upperCase alphaNumeric"
													<c:if test="${accion == 'CONSULTA' || detalle.listaPrecios != null}">
														readonly="readonly"
													</c:if>
												/>
												<button type="button" onclick="abrirPopup('listaPopupMap');"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													<c:if test="${accion == 'CONSULTA' || detalle.listaPrecios != null}">
														disabled="disabled"
													</c:if>
												>
													<img src="<%=request.getContextPath()%>/images/buscar.png" > 
												</button>&nbsp;&nbsp; 
												<label id="listaPreciosCodigoLabel" for="listaPreciosCodigo">
													<c:out value="${detalle.listaPrecios.descripcion}" default="" />
												</label>
											</td>										
										</tr>
										<tr>	
											<td class="texto_ti">
												<spring:message code="formularioAgregarConcepto.datos.tipoVariacion" htmlEscape="true"/>
											</td>										
										</tr>
										<tr>	
											<td class="texto_ti">
												<select id="variacionId" name="variacionId" class="requerido"
													<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>
												>
													<c:forEach items="${tiposVariacion}" var="tipo">
														<option value="${tipo.id}" 
															<c:if test="${tipo.id == detalle.tipoVariacion.id}">
																selected="selected"
															</c:if>
														>
															<c:out value="${tipo.descripcion}"/>
														</option>
													</c:forEach>															
												</select>															
											</td>										
										</tr>												
										<tr>	
											<td class="texto_ti">
												<spring:message code="formularioAgregarConcepto.datos.valor" htmlEscape="true"/>
											</td>										
										</tr>
										<tr>	
											<td class="texto_ti">
												<input type="text" id="valor" name="valor" style="text-align: right;" class="requerido"
													value="<c:out value="${detalle.valor}" default="" />"
													<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if>
												/>
											</td>										
										</tr>
										<tr>	
											<td class="texto_ti">
												<spring:message code="formularioAgregarConcepto.datos.precioFinal" htmlEscape="true"/>
											</td>										
										</tr>
										<tr>	
											<td class="texto_ti">
												<input type="text" id="precioConcepto" style="text-align: right;" class="requerido" 
													readonly="readonly"	value="<c:out value="${detalle.calcularMonto}" default="" />"/>
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
								</fieldset>		
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
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 140%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="conceptosPopupMap" /> 
			<jsp:param name="clase" value="conceptosPopupMap" /> 
		</jsp:include>		
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="listaPopupMap" /> 
			<jsp:param name="clase" value="listaPopupMap" /> 
		</jsp:include>		
	</body>
</html>