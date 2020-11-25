<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="styles.jsp"/>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script language="JavaScript" src="js/formulario_comprobante.js" type="text/javascript"></script>
	</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})" style="width: 300px;">
	<div>
		<form:form id="comprobanteForm" name="comprobanteForm" action="guardarActualizarComprobante.html"
    		 method="POST">
    		<input type="hidden" id="accion" name="accion" value="${accion}"/>
	    	<input type="hidden" id="id" name="id" value="${id}"/>
	    	<input type="hidden" id="codigoCliente" name="codigoCliente" value="${codigoCliente}"/>
			<table>
			  	<tr>
			     <td style="text-align: center; vertical-align: middle;">
			      	<fieldset>
					      	<legend>
									<c:if test="${accion=='NUEVO'}">
										<spring:message code="formularioComprobante.tituloFormAgregar"
											htmlEscape="true" />
									</c:if>
									<c:if test="${accion=='MODIFICAR'}">
										<spring:message code="formularioComprobante.tituloFormModificar"
											htmlEscape="true" />
									</c:if>
									<c:if test="${accion == 'CONSULTAR'}">
										<spring:message code="formularioComprobante.tituloFormConsultar" htmlEscape="true" />
									</c:if>
					      	</legend>
				      	<table>
				      		<tr>
				      			<td class="texto_ti">
				      				<fieldset style="border: hidden;">
				      					<display:table name="comprobanteList" id="comprobante" requestURI="mostrarCuentaCorriente.html" pagesize="20" sort="list" keepStatus="true">
											<display:column class="hidden" headerClass="hidden">
											    <input type="hidden" id="hdn_id" value="${cliente.id}"/>
							              	</display:column>		
											
											<display:column property="fechaStr"  
												titleKey="formularioComprobante.tabla.fecha"
												class="celdaAlineadoIzquierda" />	
						           		    
						           		    <display:column property="codigoSeriePrefijoNumero"  
												titleKey="formularioComprobante.tabla.codigo"
												class="celdaAlineadoIzquierda" />	
						           		   
											 <display:column property="totalFinal"  
												titleKey="formularioComprobante.tabla.saldo"
												class="celdaAlineadoIzquierda" />
											<display:column property="saldoDisponible"  
												titleKey="formularioComprobante.tabla.saldoDisponible"
												class="celdaAlineadoIzquierda" />	
											
											<display:column class="celdaAlineadoCentrado" media="html" sortable="false">
												<input type="checkbox" id="com_${comprobante.id}" 
													name="comprobanteSeleccionada" value="${comprobante.id}" 
													class="selectableCheckbox" 
												/>
											</display:column>		
										</display:table> 
					      			</fieldset>
					      		</td>
					      	</tr>
					      </table>
				      	</fieldset>
			     </td>
			    </tr>		  	
			    <c:if test="${accion != 'CONSULTAR'}">
				    <tr>
				     <td colspan="2" align="center" style="padding-top: 10px;">
				      <button id="guardar" type="button" title="" class="botonCentrado">
				       	<img src="<%=request.getContextPath()%>/images/guardar.png">
				       	<spring:message code="botones.guardar" htmlEscape="true"/>
				      </button>
				      &nbsp;
				      <button id="cancelar" type="button" value="off" class="botonCentrado">
				       <img src="<%=request.getContextPath()%>/images/cancelar.png">
				       <spring:message code="botones.cancelar" htmlEscape="true"/>
				      </button>
				     </td>
				    </tr>
		        </c:if>
		   </table>
	   </form:form>
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 200%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp"/>
</body>
</html>
