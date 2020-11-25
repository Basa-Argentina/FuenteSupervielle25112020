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
			<spring:message code="formularioCambioEtiqueta.titulo" htmlEscape="true"/>
		</title>
		
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>		
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/consulta_cambio_etiqueta.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<link rel="stylesheet" href="js/fancybox//jquery.fancybox-1.3.4.css" type="text/css" />
		<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioCambioEtiqueta.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarPosicionLibre.html" commandName="posicionLibreBusqueda" method="post" name="formBusqueda">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />"/>
					<input type="hidden" id="mensajeSeleccioneTipoJerarquia" value="<spring:message code="notif.seleccion.seleccionTipoJerarquia" htmlEscape="true"/>"/>
						<br style="font-size: xx-small;" />
				<fieldset>
					<legend>
						<spring:message code="formularioCambioEtiqueta.datosPosicionLibre.elementos"
							htmlEscape="true" />
					</legend>
						<c:if test="${accion != 'GUARDAR'}">
							<spring:message
								code="formularioCambioEtiqueta.posicionLibreDetalle.lectura"
								htmlEscape="true" />
								
															
							<input type="text" id="codigoLectura" class="integer"
								name="codigoLectura" maxlength="6" style="width: 50px;"
								value="<c:out value="${codigoLectura}" default="" />"
								 />
								&nbsp;&nbsp;
								<button type="button" id="buscaLectura"
									title="<spring:message code="textos.buscar" htmlEscape="true"/>"
									>
									<img src="<%=request.getContextPath()%>/images/buscar.png">
								</button>&nbsp;&nbsp; <label id="codigoLecturaLabel"
								for="codigoLectura" ><c:out value="${descripcion}" default="" /></label>
												
																					
							<input type="button" name="importar" id="importar"
								class="botonCentrado" 
								value="<spring:message code="botones.importar" htmlEscape="true"/>" />
								
							<input type="button" name="historial" id="historial" 
								class="botonCentrado"
								value="<spring:message code="botones.historial" htmlEscape="true"/>" />	
						</c:if>
						<br style="font-size: xx-small;"/>
						<br style="font-size: xx-small;"/>
						<display:table name="listaElementos" id="lecturaDetalle" requestURI="mostrarPosicionLibre.html"
						pagesize="10" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${lecturaDetalle.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_elemento" class="elementoClass"
								value="${lecturaDetalle.elemento.tipoElemento.id}" />
						</display:column>
						  	<display:column property="elemento.tipoElemento.descripcion" titleKey="formularioElemento.nombreElemento" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="elemento.codigo" titleKey="formularioElemento.datosElemento.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="elemento.posicion.estante.grupo.seccion.deposito.descripcion"   titleKey="formularioElemento.deposito" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="elemento.posicion.estante.grupo.seccion.descripcion"   titleKey="formularioElemento.seccion" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
						  	<display:column property="elemento.posicion.codigo"   titleKey="formularioElemento.datosElemento.posicion" sortable="true" class="celdaAlineadoIzquierda"/>
					</display:table>
					<input type="button" name="generarEtiqueta" id="generarEtiqueta"
								class="botonCentradoGrande"
								value="<spring:message code="formularioCambioEtiqueta.boton.generarEtiqueta" htmlEscape="true"/>&nbsp;  " />
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
		<div id="pop">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label>Buscando Lecturas. Espere por favor...</label>	     
		</div>
	</body>
</html>