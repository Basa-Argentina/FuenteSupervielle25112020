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
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script language="JavaScript" src="js/formulario_medio_cobro.js" type="text/javascript"></script>
	</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})" style="width: 400px; ">
	
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
								<c:if test="${accion=='NUEVO'}">
									<spring:message code="formularioMedioCobro.tituloFormAgregar"
										htmlEscape="true" />
								</c:if>
								<c:if test="${accion=='MODIFICAR'}">
									<spring:message code="formularioMedioCobro.tituloFormModificar"
										htmlEscape="true" />
								</c:if>
								<c:if test="${accion == 'CONSULTAR'}">
									<spring:message code="formularioMedioCobro.tituloFormConsultar" htmlEscape="true" />
								</c:if>
				      	</legend>
			      	<table>
			      		<tr>
			      			<td class="texto_ti">
			      				<fieldset style="border: hidden;">
			      					<table>
			      						<tr>
											<td class="texto_ti">
							      				<spring:message code="formularioMedioCobro.campos.tipo" htmlEscape="true" />:
							      			</td>
			      							<td class="texto_ti">
							      				<select id="idTipoMedioPago" name="idTipoMedioPago" >
													<option value="-1"
														<c:if test="${medioPagoReciboForm.tipoMedioPago==null}">
															selected="selected"
														</c:if>
														<c:if test="${accion == 'CONSULTAR' }" >disabled="disabled"</c:if>
													>
													</option>
													<c:forEach items="${tipoMedioPagoList}" var="tipoMedioPago">
														<option value="${tipoMedioPago.id}"
															<c:if test="${medioPagoReciboForm.tipoMedioPago!=null && medioPagoReciboForm.tipoMedioPago.id==tipoMedioPago.id}">
																selected = "selected"
															</c:if>
															
														>
															<c:out value="${tipoMedioPago.nombreMedio}"/>
														</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										
										<tr id="banco">
											<td class="texto_ti">
							      				<spring:message code="formularioMedioCobro.campos.banco" htmlEscape="true" />:
							      			</td>
			      							<td class="texto_ti">
							      				<select id="idBancoSel" name="idBancoSel" >
													<option value="-1"
														<c:if test="${medioPagoReciboForm.banco==null}">
															selected="selected"
														</c:if>
														<c:if test="${accion == 'CONSULTAR' }" >disabled="disabled"</c:if>
													>
													</option>
													<c:forEach items="${bancoList}" var="banco">
														<option value="${banco.id}"
															<c:if test="${medioPagoReciboForm.banco!=null && medioPagoReciboForm.banco.id==banco.id}">
																selected = "selected"
															</c:if>
															
														>
															<c:out value="${banco.nombreBanco}"/>
														</option>
													</c:forEach>
												</select>
											</td>
										</tr>	
											
							      		<tr id="numero">
											<td class="texto_ti">
							      				<spring:message code="formularioMedioCobro.tabla.numero" htmlEscape="true" />:
							      			</td>
							      			<td class="texto_ti" colspan="3">
							      				<input type="text" maxlength="15" 
							      				id="numero" name="numero" value="${fn:trim(medioPagoReciboForm.numero)}" 
							      				<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>
							      			</td>			      			
							      		</tr>	
							      		<tr id="fechaVencimiento">
											<td class="texto_ti">
							      				<spring:message code="formularioMedioCobro.tabla.vencimiento" htmlEscape="true" />:
							      			</td>
							      			<td class="texto_ti">
												<input type="text" id="fechaVencimientoStr" name="fechaVencimientoStr" style="width: 80px;" readonly="readonly"
													value="<c:out value="${medioPagoReciboForm.fechaVencimientoStr}" default="" />"/>
												<c:if test="${accion != 'CONSULTA'}">
													<script type="text/javascript" >
														new tcal ({
															// form name
															'formname': 'medioPagoReciboForm',
															// input name
															'controlname': 'fechaVencimientoStr'
														});
													</script>
												</c:if>
											</td>			      			
							      		</tr>	
							      		<tr id="titular">
											<td class="texto_ti">
							      				<spring:message code="formularioMedioCobro.tabla.titular" htmlEscape="true" />:
							      			</td>
							      			<td class="texto_ti" colspan="3">
							      				<input type="text" maxlength="40"
							      				id="titular" name="titular" 
							      				value="${medioPagoReciboForm.titular}" 
							      				<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>
							      			</td>			      			
							      		</tr>
							      			
							      		<tr>
											<td class="texto_ti">
							      				<spring:message code="formularioMedioCobro.tabla.importe" htmlEscape="true" />:
							      			</td>
							      			<td class="texto_ti" colspan="3">
							      				<input type="text" maxlength="10" class="numerico"
							      				id="importe" name="importe" 
							      				value="<fmt:formatNumber type="number" groupingUsed="false" value="${medioPagoReciboForm.importe}"/>"
							      				<c:if test="${accion == 'CONSULTAR'}">disabled="disabled"</c:if>>
							      			</td>			      			
							      		</tr>	
							      		<tr>
											<td class="texto_ti">
							      				<spring:message code="formularioMedioCobro.tabla.nota" htmlEscape="true" />:
							      			</td>
							      			<td class="texto_ti" colspan="3">
							      				<textarea id="nota" name="nota" rows="3" cols="40" class="textArea_400" style="resize: none;"
													<c:if test="${accion=='CONSULTAR' }">
														readonly="readonly"
													</c:if>
												>${fn:trim(medioPagoReciboForm.nota)}</textarea>
							      			</td>			      			
							      		</tr>	
							      	</table>
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
		      <button id="cancelar" type="button"  value="off" class="botonCentrado">
		       <img src="<%=request.getContextPath()%>/images/cancelar.png">
		       <spring:message code="botones.cancelar" htmlEscape="true"/>
		      </button>
		     </td>
		    </tr>
		    </c:if>
		 	
		    
<%----------------------------------------- fin de formulario nuevo modificacion y consulta-------------------------------------------------%>   
		   </table>
	   </form:form>
	 
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 50%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp"/>
	
</body>
</html>
