<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="styles.jsp"/>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
	</head>
<body style="width: 400px; ">
	
		<form:form id="medioPagoReciboForm" name="medioPagoReciboForm" 
			 action="guardarActualizarMedioPagoRecibo.html" 
    		 commandName="medioPagoReciboForm" method="POST">
	    		<input type="hidden" id="accion" name="accion" value="${accion}"/>
		    	<input type="hidden" id="id" name="id" value="${id}"/>
		  	<table>    
			    <tr>
				     <td style="text-align: center; vertical-align: middle;">
				      	<fieldset>
						      	<legend>
									<spring:message code="formularioListaFacturaDocto.titulo" htmlEscape="true" />
								</legend>
					      	<display:table name="doctoCtaCteList" id="docto" requestURI="mostrarCuentaCorriente.html" pagesize="20" sort="list" keepStatus="true">
									<display:column 
										titleKey="formularioListaFacturaDocto.tabla.nc_rc"
										class="celdaAlineadoIzquierda" >
										<c:out value="${docto.nc_rc.afipTipoDeComprobante.tipo}"/>
										<c:out value="${docto.nc_rc.prefijoSerie}"/>-
										<c:out value="${docto.nc_rc.numeroComprobante}"/>	
				           		   </display:column>
								   <display:column 
										titleKey="formularioListaFacturaDocto.tabla.fc_nd"
										class="celdaAlineadoIzquierda" >
										<c:out value="${docto.fc_nd.afipTipoDeComprobante.tipo}"/>
										<c:out value="${docto.fc_nd.prefijoSerie}"/>-
										<c:out value="${docto.fc_nd.numeroComprobante}"/>	
				           		   </display:column>
				           		   <display:column 
										titleKey="formularioListaFacturaDocto.tabla.importe"
										class="celdaAlineadoIzquierda" >
										<fmt:formatNumber value="${docto.importe}"></fmt:formatNumber>
								   </display:column>
								</display:table> 
					      	</fieldset>
				     </td>
			    </tr>		  	
		   </table>
	   </form:form>
</body>
</html>
