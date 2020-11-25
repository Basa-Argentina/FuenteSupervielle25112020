<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><spring:message
		code="formularioClienteDireccion.tituloConsulta" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript"
	src="js/mavalos_consulta_cliente_direccion.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>
			<li id="consultar" value=""><img src="images/consultar.png" /><font
				size="2"><spring:message code="botones.consultar"
						htmlEscape="true" />
			</font>
			</li>
			<li id="modificar"><img src="images/modificar.png" /> <font
				size="2"><spring:message code="botones.modificar"
						htmlEscape="true" />
			</font>
			</li>
			<li id="eliminar"><img src="images/eliminar.png" /> <font
				size="2"><spring:message code="botones.eliminar"
						htmlEscape="true" />
			</font>
			</li>
		</ul>
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioClienteDireccion.tituloConsulta"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarClienteDireccion.html"
					commandName="clienteDireccionBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="textos.buscar" htmlEscape="true" /> </font> <img
										id="busquedaImgSrcDown" src="images/skip_down.png"
										title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
										style="DISPLAY: none"
										title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"
							id="busquedaDiv" align="center">
							<table class="busqueda"
								style="width: 100%; background-color: white;">
								<tr>
									<td class="texto_ti" colspan="2"><spring:message
											code="formularioClienteDireccion.cliente" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioClienteDireccion.codigo" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioClienteDireccion.descripcion"
											htmlEscape="true" /></td>
									
									<td rowspan="4" style="vertical-align: middle;">
										<button name="buscar" class="botonCentrado" onclick="buscarFiltro()">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="texto_ti" colspan="2"><input type="text" id="clienteCodigo"
										name="clienteCodigo" maxlength="6" style="width: 50px;"
										value="<c:out value="${clienteDireccionBusqueda.cliente.codigo}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
														readonly="readonly"
													</c:if> />
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopup('clientesPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="clienteCodigoLabel"
										for="clienteCodigo"> <c:out
												value="${clienteDireccionBusqueda.cliente.razonSocialONombreYApellido}"
												default="" /> </label></td>
									<td class="texto_ti"><input type="text" id="codigo"
										name="codigo" style="width: 150px;" maxlength="3"
										value="<c:out value="${clienteDireccionBusqueda.codigo}" default="" />" />
									</td>
									<td class="texto_ti"><input type="text" id="descripcion"
										name="descripcion" style="width: 150px;" maxlength="100"
										value="<c:out value="${clienteDireccionBusqueda.descripcion}" default="" />" />
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioClienteDireccion.pais" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioClienteDireccion.provincia" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioClienteDireccion.localidad" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioClienteDireccion.barrio" htmlEscape="true" />
									</td>									
								</tr>
								<tr>
									<td class="texto_ti">	
										<input id="idPaisAux" name="idPaisAux" type="hidden" 
											value="<c:out value="${clienteDireccionBusqueda.paisAux.id}" default="" />"/>												
										<input type="text" id="pais" name="pais" maxlength="30" style="width: 150px;"
											value="<c:out value="${paisDefecto.nombre}" default="" />" 
											<c:if test="${accion == 'CONSULTA'}">
												readonly="readonly"
											</c:if>
										/>
										<button type="button" onclick="abrirPopup('paisPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png" > 
										</button>
										<div style="display: none;">
											<label id="paisLabel" for="pais">
												<c:out value="${paisDefecto.id}" default="" />
											</label>
										</div>																					
									</td>
									<td class="texto_ti">
										<input id="idProvinciaAux" name="idProvinciaAux" type="hidden" 
											value="<c:out value="${clienteDireccionBusqueda.provinciaAux.id}" default="" />"/>
										<input type="text" id="provincia" name="provincia" maxlength="30" style="width: 150px;"
											value="<c:out value="${clienteDireccionBusqueda.provinciaAux.nombre}" default="" />" 
											<c:if test="${accion == 'CONSULTA'}">
												readonly="readonly"
											</c:if>
										/>
										<button type="button" onclick="abrirPopupProvincia('provinciaPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png" > 
										</button>
										<div style="display: none;">
											<label id="provinciaLabel" for="provincia">
												<c:out value="${clienteDireccionBusqueda.provinciaAux.id}" default="" />
											</label>
										</div>
									</td>
									<td class="texto_ti">
										<input id="idLocalidadAux" name="idLocalidadAux" type="hidden" 
											value="<c:out value="${clienteDireccionBusqueda.localidadAux.id}" default="" />"/>
										<input type="text" id="localidad" name="localidad" maxlength="30" style="width: 150px;"
											value="<c:out value="${clienteDireccionBusqueda.localidadAux.nombre}" default="" />" 
											<c:if test="${accion == 'CONSULTA'}">
												readonly="readonly"
											</c:if>
										/>
										<button type="button" onclick="abrirPopupLocalidad('localidadPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png" > 
										</button>
										<div style="display: none;">
											<label id="localidadLabel" for="localidad">
												<c:out value="${clienteDireccionBusqueda.localidadAux.id}" default="" />
											</label>
										</div>
									</td>
									<td class="texto_ti">
										<input id="idBarrio" name="idBarrio" type="hidden" 
											value="<c:out value="${clienteDireccionBusqueda.barrioAux.id}" default="" />"/>
										<input type="text" id="barrio" name="barrio" maxlength="30" style="width: 150px;"
											value="<c:out value="${clienteDireccionBusqueda.barrioAux.nombre}" default="" />" 
											<c:if test="${accion == 'CONSULTA'}">
												readonly="readonly"
											</c:if>
										/>
										<button type="button" onclick="abrirPopupBarrio('barrioPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png" > 
										</button>
										<div style="display: none;">
											<label id="barrioLabel" for="barrio">
												<c:out value="${clienteDireccionBusqueda.barrioAux.id}" default="" />
											</label>
										</div>
									</td>
								</tr>
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<fieldset>
					<display:table name="clienteDirecciones" id="clienteDireccion"
						requestURI="mostrarClienteDireccion.html" pagesize="7" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${clienteDireccion.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column property="cliente.razonSocialONombreYApellido"
							titleKey="formularioClienteDireccion.cliente" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="direccion.barrio.localidad.provincia.nombre"
							titleKey="formularioClienteDireccion.provincia" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="direccion.barrio.localidad.nombre"
							titleKey="formularioClienteDireccion.localidad" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="direccion.barrio.nombre"
							titleKey="formularioClienteDireccion.barrio" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="direccion.calle"
							titleKey="formularioClienteDireccion.calle" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="direccion.numero"
							titleKey="formularioClienteDireccion.numero" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column class="celdaAlineadoCentrado">
							<img id="information"
								src="<%=request.getContextPath()%>/images/information.png"
								title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
						</display:column>
					</display:table>
					<div style="width: 100%" align="right">
						<button name="agregar" type="button"
							onclick="agregarClienteDireccion();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/add.png">
							<spring:message code="botones.agregar" htmlEscape="true" />
						</button>
					</div>
				</fieldset>
				<br style="font-size: xx-small;" />
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png" />
						<spring:message code="botones.cerrar" htmlEscape="true" />
					</button>
				</div>
				<br style="font-size: xx-small;" />
			</fieldset>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="clientesPopupMap" />
		<jsp:param name="clase" value="clientesPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="paisPopupMap" /> 
		<jsp:param name="clase" value="paisPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="provinciaPopupMap" /> 
		<jsp:param name="clase" value="provinciaPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="localidadPopupMap" /> 
		<jsp:param name="clase" value="localidadPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="barrioPopupMap" /> 
		<jsp:param name="clase" value="barrioPopupMap" /> 
	</jsp:include>
</body>
</html>