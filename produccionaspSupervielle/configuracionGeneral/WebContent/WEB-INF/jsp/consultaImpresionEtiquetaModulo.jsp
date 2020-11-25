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
<title><spring:message
		code="formularioPosicion.tituloConsulta" htmlEscape="true" />
	- <spring:message code="general.empresa" htmlEscape="true" /> <spring:message
		code="general.ambiente" htmlEscape="true" />
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.numeric.js"></script>
<script type="text/javascript" src="js/mavalos_consulta_impresion_etiqueta_modulo.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>

</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>			
		</ul>
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioImpresionEtiquetaModulo.tituloconsulta"
							htmlEscape="true" /> </font>
				</legend>
				<br />
				<form:form action="filtrarImpresionEtiquetaModulo.html"
					commandName="moduloBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="errorCodEstMayor" name="errorCodEstMayor" value="<spring:message code="formularioImpresionEtiquetaModulo.error.codigoEstanteriaMayor" htmlEscape="true" />" />
					<input type="hidden" id="errorCodDeposito" name="errorCodDeposito" value="<spring:message code="formularioImpresionEtiquetaModulo.error.codigoDeposito" htmlEscape="true" />" />
					<input type="hidden" id="errorCodSeccion" name="errorCodSeccion" value="<spring:message code="formularioImpresionEtiquetaModulo.error.codigoSeccion" htmlEscape="true" />" />
					<input type="hidden" id="errorTitulo" name="errorTitulo" value="<spring:message code="formularioImpresionEtiquetaModulo.error.titulo" htmlEscape="true" />" />
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
									<td class="texto_ti"><spring:message
											code="formularioImpresionEtiquetaModulo.deposito" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioImpresionEtiquetaModulo.seccion" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioImpresionEtiquetaModulo.estanteriaDesde" htmlEscape="true" />
									</td>
									<td class="texto_ti"><spring:message
											code="formularioImpresionEtiquetaModulo.estanteriaHasta" htmlEscape="true" />
									</td>
									<td rowspan="2" style="vertical-align: bottom;">
										<button id="botonBuscar" name="buscar" class="botonCentrado" type="submit">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<td class="texto_ti"><input type="text" id="codigoDeposito"
										name="codigoDeposito" maxlength="6" style="width: 50px;"
										value="<c:out value="${moduloBusqueda.codigoDeposito}" default="" />"/>
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopup('depositosPopupMap');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoDepositoLabel"
										for="codigoDeposito"> <c:out
												value="${moduloBusqueda.estante.grupo.seccion.deposito.descripcion}"
												default="" /> </label>
									</td>
									<td class="texto_ti"><input type="text" id="codigoSeccion"
										name="codigoSeccion" maxlength="6" style="width: 50px;"
										value="<c:out value="${moduloBusqueda.codigoSeccion}" default="" />"/>
										&nbsp;&nbsp;
										<button type="button"
											onclick="abrirPopupSeccion('seccionesPopupMap', '<spring:message code="notif.stock.seleccionDeposito" htmlEscape="true"/>'
																		, '<spring:message code="mensaje.informacion" htmlEscape="true"/>');"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoSeccionLabel"
										for="codigoSeccion"> <c:out
												value="${moduloBusqueda.estante.grupo.seccion.descripcion}"
												default="" /> </label>
									<td class="texto_ti"><input type="text" id="codigoDesdeEstante" class="inputTextNumericPositiveIntegerOnly"
										name="codigoDesdeEstante" maxlength="4" style="width: 50px;"
										value='<c:out value="${moduloBusqueda.codigoDesdeEstante}" default="" />'/>
									</td>
									<td class="texto_ti"><input type="text" id="codigoHastaEstante" class="inputTextNumericPositiveIntegerOnly"
										name="codigoHastaEstante" maxlength="4" style="width: 50px;"
										value="<c:out value="${moduloBusqueda.codigoHastaEstante}" default=""/>"/>
									</td>
								</tr>
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<form:form action="cambiarEstadoPosiciones.html"
					commandName="posicionTable" method="post">
				<fieldset>
						<display:table name="modulos" id="modulo" requestURI="mostrarImpresionEtiquetaModulo.html" pagesize="20" sort="external"
								partialList="true" size="${size}">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${modulo.id}" />
						</display:column>
						<display:column sortProperty="deposito" property="estante.grupo.seccion.deposito.codigo"
							titleKey="formularioImpresionEtiquetaModulo.deposito" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column sortName="seccion" property="estante.grupo.seccion.codigo"
							titleKey="formularioImpresionEtiquetaModulo.seccion" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column sortName="estante" property="estante.codigo"
							titleKey="formularioImpresionEtiquetaModulo.posicion.estante" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column sortName="modulo" titleKey="formularioImpresionEtiquetaModulo.modulo" sortable="true"
							class="celdaAlineadoCentrado" >
							<c:out value="(${modulo.posVertical};${modulo.posHorizontal})"/>
						</display:column>
					</display:table>					
				</fieldset>
				<br style="font-size: xx-small;" />
				<div style="width: 100%">
						<fieldset >
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="imprimirImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.imprimir" htmlEscape="true"/>
							        		</font>
							        		<img id="imprimirImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="imprimirImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"  id="imprimirDiv" align="left">
								<table class="imprimir" style="width: 100%; background-color: white;">
								<tr>
								<td>
									<select id="cantImprimir" name="cantImprimir" size="1" style="width: 190px;">
										<option label="Solo página actual" value="1">Solo página actual</option>
										<option label="Todas las páginas" value="2">Todas las páginas</option>
									</select>
								</td>
								<td>
									<button id="imprimirModulos" name="imprimirModulos"
										type="button">
										<img src="<%=request.getContextPath()%>/images/impresora.gif">
										<spring:message code="botones.imprimirReporte" htmlEscape="true" />
									</button></td>
									<td>
									<button id="descargarImprimirModulos" name="descargarImprimirModulos"
										type="button">
										<img src="<%=request.getContextPath()%>/images/diskette.gif">
										<spring:message code="botones.descargarReporte" htmlEscape="true" />
									</button>
									</td>
							</tr>
						</table>
					</div>
					</fieldset>
					</div>			
				</form:form>
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
	<div id="darkLayer" class="darkClassWithHeigth">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="depositosPopupMap" />
		<jsp:param name="clase" value="depositosPopupMap" />
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="seccionesPopupMap" /> 
		<jsp:param name="clase" value="seccionesPopupMap" /> 
	</jsp:include>
	
	<div id="pop">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message ></spring:message></label>	     
	</div>
</html>