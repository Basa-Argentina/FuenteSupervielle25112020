<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<jsp:include page="styles.jsp"/>
<div>
	<fieldset style="border: none; text-align: left;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="consultaHistorialCambioEtiqueta.titulo.historial" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<fieldset>
						<legend>
							<spring:message code="consultaHistorialCambioEtiqueta.datosPosicionLibre.elementos"
								htmlEscape="true" />
						</legend>
							
							<br style="font-size: xx-small;"/>
							<display:table name="listaCambioEtiqueta" id="lce" >
							  	<display:column property="etiquetaOriginal" titleKey="consultaHistorialCambioEtiqueta.datosElemento.codigoAnterior" sortable="true" class="celdaAlineadoIzquierda"/>
							  	<display:column property="etiquetaNueva" titleKey="consultaHistorialCambioEtiqueta.datosElemento.codigoPosterior" sortable="true" class="celdaAlineadoIzquierda"/>
							  	<display:column property="usuarioModificacion.username"   titleKey="consultaHistorialCambioEtiqueta.usuario" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
							  	<display:column property="idLectura"   titleKey="consultaHistorialCambioEtiqueta.lectura" sortable="true" class="celdaAlineadoIzquierda"/>
							</display:table>
					</fieldset>
	</fieldset>	
</div>