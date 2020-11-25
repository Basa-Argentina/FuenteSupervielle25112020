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
		<spring:message code="formularioTransporte.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioTransporte.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioTransporte.titulo.modificar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_tarea.js"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							Agregar Tarea
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							Modificar Tarea
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							Consultar Tarea
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarTarea.html"
					commandName="tareaFormulario" method="post"
					modelAttribute="tareaFormulario">
					
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${tareaFormulario.id}" default="" />" />
						
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> Datos Tarea
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
							<table style="width: 100%;">
								<tr>
													
													<td class="texto_ti">
														Etiqueta
													</td>
													<td class="texto_ti">
														Contenedor
													</td>													
													<td class="texto_ti">
														Lote
													</td>
													<td class="texto_ti">
														Cliente
													</td>
												</tr>
											<tr>
												<td class="texto_ti">
														<input type="text" readonly="readonly" id="etiqueta" name="etiqueta" value="${tareaFormulario.elemento.codigo}" />
												</td>																					
													<td class="texto_ti">
														<input type="text" readonly="readonly" id="codConte" name="codConte" value="${tareaFormulario.elemento.contenedor.codigo}" />
													</td>
													<td class="texto_ti">
														<input type="text" readonly="readonly" id="codLote" name="codLote" value="${tareaFormulario.loteReferencia.codigo}" />
													</td>	
													<td class="texto_ti">
														<input type="text" readonly="readonly" id="codCliente" name="codCliente" value="${tareaFormulario.elemento.clienteEmp.razonSocialONombreYApellido}" />
													</td>	
											</tr>
											<tr>
												<td class="texto_ti">
													<spring:message code="formularioLoteReferencia.referencia.clasificacionDocumental" htmlEscape="true"/>
												</td>
												<td class="texto_ti">
													<c:if test="${tareaFormulario.indiceIndividual}">
														<c:out value="${tareaFormulario.clasificacionDocumental.individualTexto1Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!tareaFormulario.indiceIndividual}">
														<c:out value="${tareaFormulario.clasificacionDocumental.grupalTexto1Titulo}" escapeXml="true"/>
													</c:if>
												</td>													
												<td class="texto_ti">
													<c:if test="${tareaFormulario.indiceIndividual}">
														<c:out value="${tareaFormulario.clasificacionDocumental.individualFecha1Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!tareaFormulario.indiceIndividual}">
														<c:out value="${tareaFormulario.clasificacionDocumental.grupalFecha1Titulo}" escapeXml="true"/>
													</c:if>
												</td>
												<td class="texto_ti">
													<c:if test="${tareaFormulario.indiceIndividual}">
														<c:out value="${tareaFormulario.clasificacionDocumental.individualNumero1Titulo}" escapeXml="true"/>
													</c:if>
													<c:if test="${!tareaFormulario.indiceIndividual}">
														<c:out value="${tareaFormulario.clasificacionDocumental.grupalNumero1Titulo}" escapeXml="true"/>
													</c:if>
												</td>
											</tr>
											<tr>
												<td class="texto_ti">
													<input type="text" readonly="readonly" id="indDoc" name="indDoc" value="${tareaFormulario.clasificacionDocumental.nombre}" />
												</td>																					
												<td class="texto_ti">
													<input type="text" readonly="readonly" id="texto1" name="texto1" value="${tareaFormulario.texto1}" />
												</td>
												<td class="texto_ti">
													<input type="text" readonly="readonly" id="fecha1" name="fecha1" value="${tareaFormulario.fecha1Str}" />
												</td>	
												<td class="texto_ti">
													<input type="text" readonly="readonly" id="numero1" name="numero1" value="${tareaFormulario.numero1}" />
												</td>
											</tr>
								<tr>
								<tr>
								
															
									<td class="texto_ti">Usuario Asignado
													</td>
													<td class="texto_ti" colspan="2">Descripcion Tarea
													</td>
								</tr>
								<tr>
									
											<td class="texto_ti">
											
											<input type="text" id="codigoUsuario"
											name="codigoUsuario" maxlength="6" style="width: 90px;"
											value="<c:out value="${tareaFormulario.codigoUsuario}" default="" />"
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

												<td class="texto_ti" colspan="2">
												<input type="text"
													id="descripcionTarea" name="descripcionTarea"
													tabindex="2" style="width: 455px;"
													value='<c:out value="${tareaFormulario.descripcionTarea}" default=""/>'
													<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if> />
												</td>
																						
											</table>
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
										</div></fieldset>
							</form:form>
							<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg2" >						  
							        		<font style="color:#003090">
							        			Archivo Digital
							        		</font>
							        		<img id="busquedaImgSrcDown2" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc2" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<c:if test="${tareaFormulario.pathLegajo!=null && tareaFormulario.pathLegajo!=''}" >
								<div style="background-color: #f1e87d; WIDTH: auto;"  id="busquedaDiv2" align="center">
									<iframe id="iframeVerLegajo" src="verLegajo.html?fileName=${tareaFormulario.pathLegajo}" style="width: 100%; height: 400px;"></iframe>
								</div>
							</c:if>
						</fieldset>
					</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion == 'CONSULTA'}">
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>	
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
		<div id="pop" style="display:none">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
		</div>
</body>
</html>