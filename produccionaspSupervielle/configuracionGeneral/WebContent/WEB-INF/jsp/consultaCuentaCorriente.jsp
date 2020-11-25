<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioCuentaCorriente.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/consulta_cuenta_corriente.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<link rel="stylesheet" href="js/fancybox//jquery.fancybox-1.3.4.css" type="text/css" />
		<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
			<sec:authorize ifAnyGranted="ROLE_ABM_CLIENTES,ROLE_ABM_CLASIFICACION_DOCUMENTAL">
			    <ul>
			      <sec:authorize ifAllGranted="ROLE_ABM_CLIENTES">	 
			      	<li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
			      	<li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
	   			  </sec:authorize>
	   			</ul>
		    </sec:authorize> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioCuentaCorriente.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarCuentaCorriente.html" commandName="facturaBusqueda" method="post">
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
						<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.buscar" htmlEscape="true"/>
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"  id="busquedaDiv" align="center">
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										
										<td class="texto_ti">
											<spring:message code="formularioCuentaCorriente.datosCuentaCorriente.fechaDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaDesdeStr" name="fechaDesdeStr" style="width: 80px;"
												value="<c:out value="${facturaBusqueda.fechaDesdeStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'facturaBusqueda',
														// input name
														'controlname': 'fechaDesdeStr'
													});
												</script>
											</c:if>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioCuentaCorriente.datosCuentaCorriente.fechaHasta" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaHastaStr" name="fechaHastaStr" style="width: 80px;"
												value="<c:out value="${facturaBusqueda.fechaHastaStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'facturaBusqueda',
														// input name
														'controlname': 'fechaHastaStr'
													});
												</script>
											</c:if>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioCuentaCorriente.datosCuentaCorriente.empresa" htmlEscape="true"/>
										</td>
										
										<td class="texto_ti">
											<input type="text" id="codigoEmpresa" name="codigoEmpresa" style="width: 50px;" class="requerido"
												value="<c:out value="${facturaBusqueda.codigoEmpresa}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoEmpresaLabel" for="codigoEmpresa"> 
												 
											</label>
										</td>	
										
										<td class="texto_ti">
											<spring:message code="formularioCuentaCorriente.datosCuentaCorriente.cliente" htmlEscape="true"/>
										</td>
										
										<td class="texto_ti">
											<input type="text" id="codigoCliente" name="codigoCliente" style="width: 50px;" class="requerido"
												value="<c:out value="${facturaBusqueda.codigoCliente}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaCliente" 
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoClienteLabel" for="codigoCliente"> 
												 
											</label>
										</td>
									</tr>
										
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioCuentaCorriente.datosCuentaCorriente.saldoAcreedor" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="saldoAcreedor" name="saldoAcreedor" style="width: 200px;" maxlength="6" readonly="readonly"
												value="<fmt:formatNumber value="${saldoAcreedor}"></fmt:formatNumber>"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioCuentaCorriente.datosCuentaCorriente.saldoDeudor" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="saldoDeudor" name="saldoDeudor" style="width: 200px;" readonly="readonly"
												value="<fmt:formatNumber value="${saldoDeudor}"></fmt:formatNumber>"/>
										</td>	
										<td class="texto_ti">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png"> 
												<spring:message code="textos.buscar" htmlEscape="true"/>								
											</button>
										</td>	
														
									</tr>
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioCuentaCorriente.datosCuentaCorriente.saldoTotal" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="saldoTotal" name="saldoTotal" style="width: 200px;" maxlength="6" readonly="readonly"
												value="<fmt:formatNumber value="${saldoTotal}"></fmt:formatNumber>"/>
										</td>
									</tr>	
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="facturaList" id="factura" requestURI="mostrarCuentaCorriente.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${factura.id}"/>
							    <input type="hidden" id="hdn_tipo" value="${factura.afipTipoDeComprobante.tipo}"/>
			                	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		    <display:column property="fechaStr"  
								titleKey="formularioCuentaCorriente.datosCuentaCorriente.tabla.fecha"
								class="celdaAlineadoIzquierda" />	
		           		   
							<display:column 
								titleKey="formularioCuentaCorriente.datosCuentaCorriente.tabla.nota"
								class="celdaAlineadoIzquierda">
								<c:out value="${factura.afipTipoDeComprobante.tipo}"/>
								<c:out value="${factura.prefijoSerie}"/>-
								<c:out value="${factura.numeroComprobante}"/>
							</display:column>	
								
							<display:column   
								titleKey="formularioCuentaCorriente.datosCuentaCorriente.tabla.debito"
								class="celdaAlineadoIzquierda">
								<c:if test="${factura.afipTipoDeComprobante.tipo == 'F' || factura.afipTipoDeComprobante.tipo=='ND' }">
									<fmt:formatNumber value="${factura.totalFinal}"></fmt:formatNumber>
								</c:if>	
							</display:column>					           		   
		           		    
		           		    <display:column 
								titleKey="formularioCuentaCorriente.datosCuentaCorriente.tabla.credito"
								class="celdaAlineadoIzquierda">
								<c:if test="${factura.afipTipoDeComprobante.tipo == 'X' || factura.afipTipoDeComprobante.tipo=='NC' }">
									<fmt:formatNumber value="${factura.totalFinal}"></fmt:formatNumber>
								</c:if>	
							</display:column>	
								
							<display:column 
								titleKey="formularioCuentaCorriente.datosCuentaCorriente.tabla.saldoCtaCte"
								class="celdaAlineadoIzquierda">
								<fmt:formatNumber value="${factura.saldoCtaCte}"></fmt:formatNumber>
							</display:column>		
		           		    
		           		    <display:column property="estado"  
								titleKey="formularioCuentaCorriente.datosCuentaCorriente.tabla.estado"
								class="celdaAlineadoIzquierda" />
								
<%-- 							<display:column  --%>
<!-- 								titleKey="formularioCuentaCorriente.datosCuentaCorriente.tabla.saldoComprobante" -->
<!-- 								class="celdaAlineadoIzquierda"> -->
<%-- 								<fmt:formatNumber value="${factura.totalFinal}"></fmt:formatNumber> --%>
<%-- 							</display:column>			 --%>
		           		    
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" onclick="agregar();" >
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="formularioCuentaCorriente.boton.registrarCobro" htmlEscape="true"/>  
							</button>
						</div>
					</fieldset>
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
	</body>
</html>