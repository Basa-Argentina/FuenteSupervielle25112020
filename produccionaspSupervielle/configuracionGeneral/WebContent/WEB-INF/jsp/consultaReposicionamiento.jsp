<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>
<%@ page buffer = "32kb" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioReposicionamiento.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/mavalos_consulta_reposicionamiento.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>	
		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<input id="errorCodigoLecturaInvalido" type="hidden" value="<spring:message code="formularioReposicionamiento.error.codigoLecturaInvalido" htmlEscape="true" />"/> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
		        	<font class="tituloForm" size="5">
		        		<spring:message code="formularioReposicionamiento.titulo" htmlEscape="true"/>		        		
	        		</font>	
	        	</legend>	
				<form:form name="formulario" id="formulario" action="mostrarReposicionamiento.html" method="get">
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
									<td>
										<label for="codigo" title="<spring:message code="formularioReposicionamiento.lecturaNoUtilizada"></spring:message>">
											<spring:message code="formularioReposicionamiento.lectura"></spring:message>
										</label>&nbsp;&nbsp;
									</td>
									<td>
									
									</td>
								</tr>
								<tr>
									<td>
									<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="${codigoEmpresa}"/> 
										<input type="text" id="codigoLectura" name="codigoLectura" class="inputTextNumericPositiveIntegerOnly"
											maxlength="8" style="width: 50px;"
											value="<fmt:numberComplete value="${lecturaBusqueda.codigo}" 
																length="8" valorDefualt="0"/>" /> &nbsp;&nbsp;
										<button type="button" name="buscaLectura" id="buscaLectura"

											title="<spring:message code="textos.buscar" htmlEscape="true"/>" >
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp;						
										<label id="codigoLecturaLabel" for="codigoLectura"> 
											<c:out value="${lecturaBusqueda.descripcion}"	default="" /> 
										</label>&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
									<td>
										<fieldset>
										<input type="checkbox" id="libera" name="libera" value="true" 
										 <sec:authorize ifNotGranted="ROLE_PRO_REPOSICIONAMIENTO_LIBERAR_POSICIONES">
										 	 readonly="readonly" disabled="disabled" 	
										 </sec:authorize>
										 />
										<spring:message code="formularioReposicionamiento.liberaPosicionesAuto" htmlEscape="true"/>
										</fieldset>
									</td>
									<td style="text-align: right;">
										<button id="cargarLectura" name="cargarLectura" type="button" onclick="" class="botonCentrado">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="formularioReposicionamiento.boton.cargar" htmlEscape="true"/>  
										</button>
									</td>
								</tr>
							</table>
						</div>	
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<fieldset>	
					<br style="font-size: xx-small;"/>
					<display:table name="elementosReposicionados" id="elementoReposicionado" requestURI="mostrarReposicionamiento.html" pagesize="20" sort="list" keepStatus="true">
						<display:caption>
							<spring:message code="formularioReposicionamiento.nuevaPosicion" htmlEscape="true"/>
						</display:caption>
						<display:column class="hidden" headerClass="hidden">
						    <input type="hidden" id="hdn_id" value="${elemento.id}"/>
		              	</display:column>	   
						<display:column property="codigo" titleKey="formularioReposicionamiento.datosElemento.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
					  	<display:column property="posicionFutura.estante.grupo.seccion.deposito.descripcion"   titleKey="formularioReposicionamiento.deposito" sortable="true" class="celdaAlineadoIzquierda"/>
					  	<display:column property="posicionFutura.estante.grupo.seccion.descripcion"   titleKey="formularioReposicionamiento.seccion" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
					  	<display:column property="posicionFutura.estante.codigo"   titleKey="formularioReposicionamiento.estante" sortable="true" class="celdaAlineadoIzquierda"/>
					  	<display:column titleKey="formularioReposicionamiento.datosElemento.posicion" sortable="true" class="celdaAlineadoCentrado">
					  		<c:out value="(${elementoReposicionado.posicionFutura.posVertical};${elementoReposicionado.posicionFutura.posHorizontal})"/>
					  	</display:column>						  	
					</display:table> &nbsp;&nbsp;						
				</fieldset>
				<br style="font-size: xx-small;"/>
				<fieldset>
					<div style="width: 100%" align="right">
						<button id="botonCancelar" name="cancelar" type="button"
							onclick="" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png">
							<spring:message code="formularioReposicionamiento.boton.cancelar"
								htmlEscape="true" />
						</button>
						<button id="botonGuardar" name="guardar" type="button" onclick=""
							class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png">
							<spring:message code="formularioReposicionamiento.boton.guardar"
								htmlEscape="true" />
						</button>
					</div>
				</fieldset>
			</fieldset>
				<br style="font-size: xx-small;"/>
				<div align="center">
					<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png"/> 
						<spring:message code="botones.cerrar" htmlEscape="true"/>  
					</button>						
				</div>	
				<br style="font-size: xx-small;"/>			
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<div class="selectorDiv"></div>
	</body>
</html>