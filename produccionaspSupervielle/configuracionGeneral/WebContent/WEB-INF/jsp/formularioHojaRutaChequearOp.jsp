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
			<spring:message code="formularioHojaRutaChequearOp.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		
		<script type="text/javascript" src="js/ini.js"></script>
<!-- 		<script type="text/javascript" src="js/busquedaHelper.js"></script> -->
		<script type="text/javascript" src="js/formulario_hoja_ruta_chequear_op.js"></script>
		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	
		      <li id="imprimir"><img src="images/print.png" /> <font size="2"><spring:message code="botones.imprimir" htmlEscape="true"/></font></li>		 
		      <li id="chequear" value=""><img src="images/chequear.png" /><font size="2"><spring:message code="botones.chequear" htmlEscape="true"/></font></li>	 
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
			        		<spring:message code="formularioHojaRutaChequearOp.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<fieldset>
						<table width="97%">
							<tr>
								<td>
									<font size="3">
										<spring:message code="formularioHojaRutaChequearOp.fechaHojaRuta" htmlEscape="true"/>
									</font>	
									
								</td>
								<td>
									<font size="3">
										<spring:message code="formularioHojaRutaChequearOp.numero" htmlEscape="true"/>
									</font>
								</td>
								
								<td>
									<font size="3">
										<spring:message code="formularioHojaRutaChequearOp.responsable" htmlEscape="true"/>
									</font>
								</td>
								<td>
									<font size="3">
										<spring:message code="formularioHojaRutaChequearOp.scanear" htmlEscape="true"/>
									</font>
								</td>
							</tr>
							<tr>
								<td>
									<label>${fechaHoraSalida}</label>
								</td>
								<td>
									<label>${nroSerieHr}</label>
								</td>
								<td>
									<label>${responsable}</label>
								</td>
								<td>
									<input type="text" id="txtScanear" name="txtScanear" onkeydown="scanearOperacion(event)">
								</td>
							</tr>
						</table>
					</fieldset>
					<form:form action="guardarChequearHojaRuta.html" method="post">
						<input type="hidden" id="hd_idHojaRuta" name="hd_idHojaRuta" value="${idHojaRuta}"/>
						<fieldset>
								<display:table name="listaOperaciones" id="listaOperaciones"
									requestURI="consultaHojaRuta.html">
									<display:column class="hidden" headerClass="hidden">
									    <input type="hidden" id="hdn_id" value="${hojaRuta.id}"/>
					              	</display:column>		
					              	<display:column class="hidden" headerClass="hidden">
								    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
				           		   	</display:column>
				           		   	<display:column sortName="formularioHojaRutaChequearOp.lista.fecha" property="serie" 
				           		   		titleKey="formularioHojaRutaChequearOp.lista.req" sortable="true" class="celdaAlineadoIzquierda"/>
									<display:column property="fechaEntrega" 
										titleKey="formularioHojaRutaChequearOp.lista.fecha" sortable="false" class="celdaAlineadoIzquierda"/>
									<display:column property="cliente" 
										titleKey="formularioHojaRutaChequearOp.lista.cliente" sortable="false" class="celdaAlineadoIzquierda"/>
									<display:column property="tipoRequerimiento" 
										titleKey="formularioHojaRutaChequearOp.lista.tipo" sortable="false" class="celdaAlineadoIzquierda"/>
									<display:column property="direccionEntrega" 
										titleKey="formularioHojaRutaChequearOp.lista.sector" sortable="false" class="celdaAlineadoIzquierda"/>
									<display:column property="cantidadElemento" 
										titleKey="formularioHojaRutaChequearOp.lista.cantidad" sortable="false" class="celdaAlineadoIzquierda"/>	
									<display:column property="solicitante" 
										titleKey="formularioHojaRutaChequearOp.lista.solicitante" sortable="false" class="celdaAlineadoIzquierda"/>	
								 	<display:column class="celdaAlineadoCentrado" media="html" sortable="false">
										<input type="checkbox" id="op_${listaOperaciones.idHojaRutaOpElemnt}" 
											name="operacionSeleccionada" value="${listaOperaciones.idHojaRutaOpElemnt}" 
											class="selectableCheckbox" 
										/>
									</display:column>
								</display:table> 
								
						</fieldset>
					</form:form>
					<fieldset>
						<br style="font-size: xx-small;"/>
						<div align="center">
							<button name="guardar" type="button" class="botonCentrado" id="btnGuardar">
								<img src="<%=request.getContextPath()%>/images/ok.png">
								<spring:message code="botones.guardar" htmlEscape="true" />
							</button>
		
							<button name="cancelar" type="button" id="btnCancelar" class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/cancelar.png">
								<spring:message code="botones.cancelar" htmlEscape="true" />
							</button>
						</div>
						<br style="font-size: xx-small;"/>
					</fieldset>
				</fieldset>		
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<div class="selectorDiv"></div>
		<div id="pop" style="display:none">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
		</div>
	</body>
</html>