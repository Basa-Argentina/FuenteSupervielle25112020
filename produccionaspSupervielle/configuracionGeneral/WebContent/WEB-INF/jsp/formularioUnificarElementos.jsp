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
			<spring:message code="formularioUnificarElementos.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/formulario_unificar_elementos.js"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>		
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
								<spring:message code="formularioUnificarElementos.titulo" htmlEscape="true"/>
		        		</font>		  
					</legend>
					<br/>
			</fieldset>
			<div>
			<fieldset>
			<table align="center" style="width: 100%;">							
				<tr>
					<td style="text-align: center; vertical-align: middle;">
						<div id="grupoDiv" align="center">							
							<table>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioRemito.vacio" htmlEscape="true"/>
									</td>
									<td>
										&nbsp;
									</td>
									<td class="texto_ti">
										<spring:message code="formularioUnificarElementos.notificacion.listadoEtiquetasAUnificar" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
							      <td width="40%" align="center">
							      <spring:message code="formularioRemito.detalles.codigoElemento" htmlEscape="true"/>
							      <br/>
							      	<input type="hidden" id="listadoCodigos" name="listadoCodigos" value="${listaCodigos}">
							        <input type="text" id="direccionesSeleccionadasIzq" maxlength="13" size="13"  
							        class="inputTextNumericPositiveIntegerOnly" name="direccionesSeleccionadasIzq">
							        <br/>
							      </td>
							      <td width="20%" align="center">											          
							          <c:if test="${accion != 'CONSULTA'}">
								          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('direccionesSeleccionadas')" 
								        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
								          	>
								          <br />
								          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('direccionesSeleccionadas')" 
								        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
								          >
								          <br />
						        	  </c:if>
							      </td>
							      <td width="40%" align="center">
							        <select id="direccionesSeleccionadasDer" name="direccionesSeleccionadasDer" size="8" style="width:250px" multiple="multiple">
							            <c:forEach items="${direccionesDer}" var="direccionDer">
											<option value="<c:out value="${direccionDer.id}"/>">
												<c:out value="${direccionDer.codigoDescripcion}"/>
											</option>
										</c:forEach>
							        </select>
							      </td>
							    </tr>
						    </table>
					    </div>
					</td>
				</tr>
			
				<tr>
					<td colspan="2" align="center" style="padding-top: 10px;">
						<button id="cargarCodigosElementos" type="button" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.unificar" htmlEscape="true"/>  
						</button>						
					</td>
				</tr>				
			</table>
			</fieldset>
		</div>
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<div class="selectorDiv"></div>
</body>
</html>