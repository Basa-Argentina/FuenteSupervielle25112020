<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ page buffer = "64kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><spring:message
		code="formularioOperacionReferencia.titulo" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/mavalos_consulta_operacion_referencia.js"></script>
<script type="text/javascript" src="js/busquedaHelper.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
<script type="text/javascript" src="js/checkEAN.js"></script>

<script type="text/javascript" src="js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg}); setFocusInicial();">
	<c:if test="${operacion.estado == 'Pendiente' || operacion.estado == 'En Proceso'}">
		<div class="contextMenu" id="myMenu1">
			<ul>
				<li id="eliminar"><img src="images/eliminar.png" /> <font
					size="2"><spring:message code="botones.eliminar"
							htmlEscape="true" />
				</font>
				</li>
				<li id="verImagenes" value=""><img src="images/open.gif" /><font size="2"><spring:message code="botones.verImagenes" htmlEscape="true"/></font></li>
			</ul>
		</div>
	</c:if>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioOperacionReferencia.titulo"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarOperacionReferencia.html" name="formBusqueda" id="formBusqueda"
					commandName="operacionReferenciaBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="idOperacion" name="idOperacion" value="<c:out value="${idOperacion}" default="" />"/>
					<input type="hidden" name="buscarElemento" id="buscarElemento" value=true></input>	
					<input type="hidden" name="buscarElementoReferencia" id="buscarElementoReferencia" value=true></input>	
					
					<input type="hidden" name="finalizarOK" id="finalizarOK" value="${finalizarOK}"></input>	
					<input type="hidden" name="finalizarError" id="finalizarError" value="${finalizarError}"></input>	
					<input type="hidden" name="traspasar" id="traspasar" value="${traspasar}"></input>
					<input type="hidden" name="procesando" id="procesando" value="${procesando}"></input>
					
					<input type="hidden" name="accionGuardar" id="accionGuardar" value=""></input>
					
					<input type="hidden" name="tipoCalculo" id="tipoCalculo" value="${operacion.tipoOperacion.conceptoFacturable.tipoCalculo}"></input>
					<input type="hidden" name="cantidadTipoCalculo" id="cantidadTipoCalculo" value=""></input>
					
					<input type="hidden" name="preguntaFinalizarOK" id="preguntaFinalizarOK" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarOK" htmlEscape="true"/>"></input>		
					<input type="hidden" name="preguntaFinalizarOKConTraspaso" id="preguntaFinalizarOKConTraspaso" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarOKConTraspaso" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaTraspaso" id="preguntaTraspaso" value="<spring:message code="formularioOperacionReferencia.pregunta.traspaso" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaFinalizarError" id="preguntaFinalizarError" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarError" htmlEscape="true"/>"></input>
					<input type="hidden" name="preguntaFinalizarErrorConTraspaso" id="preguntaFinalizarErrorConTraspaso" value="<spring:message code="formularioOperacionReferencia.pregunta.finalizarErrorConTraspaso" htmlEscape="true"/>"></input>
					<input type="hidden" name="observacionesHdn" id="observacionesHdn" value=""></input>
					
				</form:form>
				
				<fieldset>
					<table class="busqueda"
						style="width: 100%; background-color: white;">
						<tr>
							<td class="texto_ti">
								<spring:message	code="formularioOperacionReferencia.operacion" htmlEscape="true" />
							</td>
						</tr>
						<tr>
							<td class="texto_ti">
								<font style="font-weight:bold;">
									<c:out value="${operacion.tipoOperacion.descripcion}"></c:out>
								</font>
							</td>							
						</tr>
					</table>
					<br style="font-size: xx-small;" />
					<form:form action="filtrarElementoOperacionReferencia.html" name="formBusquedaElemento" id="formBusquedaElemento"
							commandName="operacionElementoReferenciaBusqueda" method="post">
						<input type="hidden" id="seleccionBusqueda" name="seleccionBusqueda"/>
						<input type="hidden" id="mensajeErrorEAN" name="mensajeErrorEAN" value="<spring:message code="formularioOperacionReferencia.error.codigoBarraIncorrecto" htmlEscape="true"/>"/>
						<input type="hidden" id="mensajeErrorSeleccion" name="mensajeErrorSeleccion" value="<spring:message code="formularioOperacionReferencia.error.seleccionElemento" htmlEscape="true"/>"/>
						<input type="hidden" id="cantidadElementosSeleccionados" name="cantidadElementosSeleccionados" value="0"/>
						<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />"/>
						<input type="hidden" id="rearchivosPendientes" name="rearchivosPendientes" value="<c:out value="${rearchivosPendientesEnOperacion}" default="false" />"/>
						<input type="hidden" id="mensajeRearchivosPendientes" name="mensajeRearchivosPendientes" value="<spring:message code="formularioOperacionReferencia.error.rearchivosPendientes" htmlEscape="true"/>"/>
						<display:table name="operacionReferencias" id="operacionReferencia"
							requestURI="">
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_id" value="${operacionReferencia.elemento.id}" />
							</display:column>
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_eliminar"
									value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
							</display:column>
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_traspasado" value="${operacionReferencia.traspasado}" />
							</display:column>
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_proviene_lectura" value="${operacionReferencia.provieneLectura}" />
							</display:column>
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_traspasado_mensaje"
									value="<spring:message code="formularioOperacionReferencia.display.mensaje.traspasado" htmlEscape="true"/>" />
							</display:column>
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_proviene_lectura_mensaje"
									value="<spring:message code="formularioOperacionReferencia.display.mensaje.provieneLectura" htmlEscape="true"/>" />
							</display:column>
							<display:column class="celdaAlineadoCentrado" title="<input type='checkbox' name='selectall' 
								onClick='selectAll(this, \"selectableCheckbox\")' />" media="html">
								<c:if test="${operacionReferencia.traspasado == false && (operacion.estado == 'Pendiente' || operacion.estado == 'En Proceso')}">						
									<input type="checkbox" name="elementosSel" value="${operacionReferencia.elemento.id}"
											class="selectableCheckbox" onclick="sumarElemento(this.checked)"
										/>
								</c:if>
							</display:column>
							<display:column property="elemento.tipoElemento.descripcion" 
								titleKey="formularioOperacionReferencia.display.tipoElemento" 
								class="celdaAlineadoIzquierda"/>
							<display:column property="elemento.codigo" 
								titleKey="formularioOperacionReferencia.display.codigo"
								class="celdaAlineadoIzquierda"/>
							<display:column property="operacion.deposito.descripcion"
								titleKey="formularioOperacionReferencia.display.deposito" 
								class="celdaAlineadoIzquierda" />
							<c:if test="${operacionReferencia.elemento.contenedor == null}">
								<display:column property="elemento.posicion.modulo.moduloPosicionStr"
									titleKey="formularioOperacionReferencia.display.modulo" 
									class="celdaAlineadoIzquierda" />
							  	<display:column property="elemento.posicion.estante.grupo.seccion.descripcion"   titleKey="formularioElemento.seccion" class="celdaAlineadoIzquierda"/>
							  	<display:column titleKey="formularioElemento.datosElemento.posicion" class="celdaAlineadoIzquierda">
							  	<c:if test="${operacionReferencia.elemento.posicion.posVertical != null}">
									<c:out value="E:${operacionReferencia.elemento.posicion.estante.codigo} (${operacionReferencia.elemento.posicion.posVertical};${operacionReferencia.elemento.posicion.posHorizontal})"/>
								</c:if> 
			           		   	</display:column>
		           		   	</c:if>
		           		   	<c:if test="${operacionReferencia.elemento.contenedor != null}">
		           		   		<display:column property="elemento.contenedor.posicion.modulo.moduloPosicionStr"
									titleKey="formularioOperacionReferencia.display.modulo" 
									class="celdaAlineadoIzquierda" />
							  	<display:column property="elemento.contenedor.posicion.estante.grupo.seccion.descripcion"   titleKey="formularioElemento.seccion" class="celdaAlineadoIzquierda"/>
							  	<display:column titleKey="formularioElemento.datosElemento.posicion" class="celdaAlineadoIzquierda">
								  	<c:if test="${operacionReferencia.elemento.contenedor.posicion.posVertical != null}">
										<c:out value="E:${operacionReferencia.elemento.contenedor.posicion.estante.codigo} (${operacionReferencia.elemento.contenedor.posicion.posVertical};${operacionReferencia.elemento.contenedor.posicion.posHorizontal})"/>
									</c:if> 
			           		   	</display:column>
		           		   	</c:if> 	
							
							
							
							<display:column property="rearchivoDe.codigo" 
								titleKey="formularioOperacionReferencia.display.rearchivo"
								class="celdaAlineadoIzquierda"/>
							<display:column titleKey="formularioOperacionReferencia.display.origen" class="celdaAlineadoIzquierda">
							  	<c:if test="${operacionReferencia.provieneLectura == true}">
									<spring:message code="formularioOperacionReferencia.display.origen.lectura" htmlEscape="true"/>
								</c:if>
								<c:if test="${operacionReferencia.provieneLectura == false}">
									<spring:message code="formularioOperacionReferencia.display.origen.requerimiento" htmlEscape="true"/>
								</c:if> 
		           		   	</display:column>
							<display:column property="estado"
								titleKey="formularioOperacionReferencia.display.estado"
								class="celdaAlineadoIzquierda" />
							
							<c:if test="${operacion.estado == 'Pendiente' || operacion.estado == 'En Proceso'}">
								<display:column class="celdaAlineadoCentrado">
									<img id="information"
										src="<%=request.getContextPath()%>/images/information.png"
										title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
								</display:column>
							</c:if>
							<display:column class="hidden" headerClass="hidden">
								<input type="hidden" id="hdn_path" value="${operacionReferencia.pathArchivoDigital}" />
							</display:column>
						</display:table>
						<c:if test="${operacion.estado == 'Pendiente' || operacion.estado == 'En Proceso'}">
							<div style="width: 100%" align="right">
								<spring:message code="formularioOperacionReferencia.agregarPorLectura" htmlEscape="true"/>
								<input type="text" id="codigoLectura" class="integer"
												name="codigoLectura" maxlength="6" style="width: 50px;"
												value=""
												 />
												&nbsp;&nbsp;
												<button type="button" id="buscaLectura"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>&nbsp;&nbsp; <label id="codigoLecturaLabel"
												for="codigoLectura">  </label>&nbsp;&nbsp;
												<button type="button" id="cargaLectura" 
													title="<spring:message code="botones.cargar" htmlEscape="true"/>" class="botonCentrado">
													<spring:message code="botones.cargar" htmlEscape="true"/>
												</button>
							</div>
						
						<sec:authorize ifAnyGranted="ROLE_OPERACIONES_ELEMENTO_ESTADO">
							<fieldset>
								<legend>
									<spring:message code="formularioOperacionReferencia.actualizarEstado" htmlEscape="true"/>
								</legend>
								
									<table class="busqueda"
										style="width: 100%; background-color: white;">
										
										<tr>
											<td class="texto_ti">
												<spring:message code="formularioOperacionReferencia.actualizarEstado.codigoBarra" htmlEscape="true"/>
											</td>
											<td class="texto_ti">
												<spring:message code="formularioOperacionReferencia.actualizarEstado.seleccion" htmlEscape="true"/>
											</td>
										</tr>
										<tr>
											<td class="texto_ti">
												<input type="text" id="codigoElemento"
													name="codigoElemento" maxlength="13" style="width: 100px;">
												&nbsp;&nbsp;
												<button type="button" onclick="buscarCodigoElemento();"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
													<img src="<%=request.getContextPath()%>/images/skip.png">
												</button>
											</td>
											<td>
												<select id="estadoSeleccion" 
													name="estadoSeleccion" size="1" style="width: 140px;"
													>
														<option label="Pendiente" value="Pendiente" 
															>
															<spring:message code="formularioOperacionReferencia.estado.pendiente" htmlEscape="true"/>
														</option>
														<option label="Procesado" value="Procesado" 
															selected="selected" >
															<spring:message code="formularioOperacionReferencia.estado.procesado" htmlEscape="true"/>
														</option>
														<option label="Omitido" value="Omitido" 
														>
															<spring:message code="formularioOperacionReferencia.estado.omitido" htmlEscape="true"/>
														</option>									
												</select>
												&nbsp;&nbsp;
												<button type="button" onclick="cambiarEstadoSeleccion();"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
													<img src="<%=request.getContextPath()%>/images/skip.png">
												</button>
											</td>
										</tr>
									</table>
								</fieldset>
							</sec:authorize>
							<fieldset>
								<table>												
									<tr>
										<td>
											<spring:message code="formularioRequerimiento.datos.observaciones" htmlEscape="true"/>
										</td>
										<c:if test="${operacion.requerimiento.cantidad!=null}">
											<td>
												Cantidad
											</td>
										</c:if>
									</tr>
									<tr>
										<td>
											<textarea id="observaciones" name="observaciones" rows="10" cols="50" 
												<c:if test="${accion == 'CONSULTA'}">readonly="readonly"</c:if>>
												<c:out value="${operacion.observaciones}" />
											</textarea>
										</td>
										<c:if test="${operacion.requerimiento.cantidad!=null}">
											<td style="vertical-align: top;">
												<input type="text" id="cant" name="cant" 
												value="<c:out value="${operacion.requerimiento.cantidad}" default="" />" size="4" maxlength="4" 
												readonly="readonly"/>
											</td>
										</c:if>
									</tr>
								</table>
							</fieldset>
						</c:if>
					</form:form>
				</fieldset>
				<br style="font-size: xx-small;"/>
			    
		    	<div align="center">
		    		<c:if test="${operacion.estado == 'Pendiente' || operacion.estado == 'En Proceso'}">
						<button name="guardar" type="button" onclick="guardarYSalir();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.guardar" htmlEscape="true"/>  
						</button>
						&nbsp;
						<button name="cancelar" type="button"  onclick="volverCancelar();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
							<spring:message code="botones.cancelar" htmlEscape="true"/>  
						</button>
					</c:if>
					<c:if test="${operacion.estado != 'Pendiente' && operacion.estado != 'En Proceso'}">
						<button name="volver" type="button"  onclick="volverCancelar();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/volver4.png"> 
							<spring:message code="botones.cerrar" htmlEscape="true"/>  
						</button>
					</c:if>
				</div>				
				
				<br style="font-size: xx-small;" />
			</fieldset>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
	<div id="pop">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
	</div>
	<!-- ----------	fancybox ------------------------- -->
	<a id="fancyboxImagenes" style="visibility:hidden;" href="iniciarPopUpLoteFacturacionDetalle.html"></a>
</html>