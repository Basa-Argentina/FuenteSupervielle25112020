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
			<c:if test="${tipoHistorico=='elemento'}">
				<spring:message code="menu.consultaReferenciasPorUsuario" htmlEscape="true"/>
			</c:if>
			<c:if test="${tipoHistorico=='referencia'}">
				<spring:message code="menu.consultaReferenciasPorUsuario" htmlEscape="true"/>
			</c:if>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_historico.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>		
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
								<spring:message code="informeReferenciasPorUsuario.titulo.totales" htmlEscape="true"/>
		        		</font>		  
					</legend>
					<br/>
					<form:form action="imprimirReferenciasPorUsuarioXls.html" commandName="historicoBusqueda" method="post" name="formBusqueda">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="tipoHistorico" name="tipoHistorico" value="<c:out value="${tipoHistorico}" default="" />"/>
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
										<td class="texto_ti">
											<spring:message code="formularioHistoricoElemento.datosHistorico.fechaDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHistoricoElemento.datosHistorico.fechaHasta" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<spring:message code="formularioHistoricoElemento.datosHistorico.usuario" htmlEscape="true"/>
										</td>
										<td class="texto_ti" rowspan="2" style="vertical-align: middle;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png"> 
												<spring:message code="textos.buscar" htmlEscape="true"/>								
											</button>
										</td>
									</tr>
									<tr>
													<td class="texto_ti">
														<input type="text" id="fechaDesdeStr" name="fechaDesdeStr" 
															maxlength="10" style="width: 90px;"
															value="<c:out value="${historicoBusqueda.fechaDesdeStr}" default="" />" />
															
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formBusqueda',
																	// input name
																	'controlname': 'fechaDesdeStr'
																});
															</script></c:if>
													</td>										
													<td class="texto_ti">
														<input type="text" id="fechaHastaStr" name="fechaHastaStr" 
															maxlength="10" style="width: 90px;"
															value="<c:out value="${historicoBusqueda.fechaHastaStr}" default="" />" />
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formBusqueda',
																	// input name
																	'controlname': 'fechaHastaStr'
																});
															</script></c:if>
													</td>
													<td class="texto_ti">
											<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" 
											value="<c:out value="${codigoEmpresa}" default="" />"/>
											
											<input type="text" id="codigoUsuario"
											name="codigoUsuario" maxlength="6" style="width: 90px;"
											value="<c:out value="${historicoBusqueda.codigoUsuario}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupUsuario"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoUsuarioLabel"
											for="codigoUsuario"> <c:out
													value=""
													default="" /> </label>
									</td>
																																										
									</tr>
									<tr>
										<c:if test="${tipoHistorico=='elemento'}">
											<td class="texto_ti">
												<spring:message code="formularioHistoricoElemento.datosHistorico.tipoElemento" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<spring:message code="formularioHistoricoElemento.datosHistorico.elemento" htmlEscape="true"/>
											</td>
										</c:if>
										<c:if test="${tipoHistorico=='referencia'}">
											<td class="texto_ti">
												<spring:message code="formularioHistoricoReferencia.datosHistorico.loteReferencia" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<spring:message code="formularioHistoricoReferencia.datosHistorico.elemento" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<spring:message code="formularioHistoricoReferencia.datosHistorico.contenedor" htmlEscape="true"/>
											</td>
										</c:if>
									</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
				</fieldset>
				
				
				<br/>
				<br/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
								<spring:message code="informeReferenciasPorUsuario.titulo.detallePorDia" htmlEscape="true"/>
		        		</font>		  
					</legend>
					<br/>
					<form:form action="imprimirReferenciasPorUsuarioDetalle.html" commandName="historicoBusqueda" method="post" name="formBusqueda2">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="tipoHistorico" name="tipoHistorico" value="<c:out value="${tipoHistorico}" default="" />"/>
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg2" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.buscar" htmlEscape="true"/>
							        		</font>
							        		<img id="busquedaImgSrcDown2" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc2" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"  id="busquedaDiv2" align="center">
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioHistoricoElemento.datosHistorico.fechaDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHistoricoElemento.datosHistorico.usuario" htmlEscape="true"/>
										</td>
										<td class="texto_ti" rowspan="2" style="vertical-align: middle;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png"> 
												<spring:message code="textos.buscar" htmlEscape="true"/>								
											</button>
										</td>
									</tr>
									<tr>
													<td class="texto_ti">
														<input type="text" id="fechaHoraStr" name="fechaHoraStr" 
															maxlength="10" style="width: 90px;"
															value="<c:out value="${historicoBusqueda.fechaHoraStr}" default="" />" />
															
												<c:if test="${accion != 'CONSULTA'}">
														<script type="text/javascript" >
																new tcal ({
																	// form name
																	'formname': 'formBusqueda2',
																	// input name
																	'controlname': 'fechaHoraStr'
																});
															</script></c:if>
													</td>
													<td class="texto_ti">
											<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" 
											value="<c:out value="${codigoEmpresa}" default="" />"/>
											
											<input type="text" id="codigoUsuarioDia"
											name="codigoUsuarioDia" maxlength="6" style="width: 90px;"
											value="<c:out value="${historicoBusqueda.codigoUsuarioDia}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupUsuarioDia"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoUsuarioDiaLabel"
											for="codigoUsuarioDia"> <c:out
													value=""
													default="" /> </label>
									</td>
																																										
									</tr>
									<tr>
										<c:if test="${tipoHistorico=='elemento'}">
											<td class="texto_ti">
												<spring:message code="formularioHistoricoElemento.datosHistorico.tipoElemento" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<spring:message code="formularioHistoricoElemento.datosHistorico.elemento" htmlEscape="true"/>
											</td>
										</c:if>
										<c:if test="${tipoHistorico=='referencia'}">
											<td class="texto_ti">
												<spring:message code="formularioHistoricoReferencia.datosHistorico.loteReferencia" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<spring:message code="formularioHistoricoReferencia.datosHistorico.elemento" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<spring:message code="formularioHistoricoReferencia.datosHistorico.contenedor" htmlEscape="true"/>
											</td>
										</c:if>
									</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
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