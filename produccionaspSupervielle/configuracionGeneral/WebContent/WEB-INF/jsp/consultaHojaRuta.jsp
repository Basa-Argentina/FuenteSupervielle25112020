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
			<spring:message code="formularioHojaRuta.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		
		<script type="text/javascript" src="js/ini.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/consulta_hoja_ruta.js"></script>
		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		      <li id="imprimir"><img src="images/print.png" /> <font size="2"><spring:message code="botones.imprimir" htmlEscape="true"/></font></li>		 
		      <li id="chequear" value=""><img src="images/chequear.png" /><font size="2"><spring:message code="botones.chequear" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioHojaRuta.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="consultaHojaRuta.html" commandName="busquedaHojaRutaFormulario" method="post">
						<input type="hidden" id="idClienteAsp" name="idClienteAsp"
							value="<c:out value="${busquedaHojaRutaFormulario.idClienteAsp}" default="" />"/>
						<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
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
										<td class="texto_ti" colspan="2">
											<spring:message code="formularioHojaRuta.busqueda.empresa" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.sucursal" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.transporte" htmlEscape="true"/>
										</td>
									</tr>
									<tr>
										<td class="texto_ti" colspan="2">
											<input type="text" id="codigoEmpresa" name="codigoEmpresa" style="width: 50px;"
												value="<c:out value="${busquedaHojaRutaFormulario.codigoEmpresa}" default="" />"/>
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
											<input type="text" id="codigoSucursal" name="codigoSucursal" style="width: 50px;"
												value="<c:out value="${busquedaHojaRutaFormulario.codigoSucursal}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaSucursal"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoSucursalLabel" for="codigoSucursal"> 
												 
											</label>
										</td>
										<td class="texto_ti">
											<input type="text" id="codigoTransporte" name="codigoTransporte" style="width: 50px;"
												value="<c:out value="${busquedaHojaRutaFormulario.codigoTransporte}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaTransporte" 
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoTransporteLabel" for="codigoTransporte"> 
												 
											</label>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.serie" htmlEscape="true"/>
										</td>	
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.numeroDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.numeroHasta" htmlEscape="true"/>
										</td>	
										
									</tr>
									<tr>
										<td class="texto_ti" style="width: 150px;">
											<input type="text" id="codigoSerie" name="codigoSerie" maxlength="30" style="width: 50px;"
												value="<c:out value="${busquedaHojaRutaFormulario.codigoSerie}" default="" />"
												/>
												&nbsp;&nbsp;
												<button type="button" id="buscaSerie" name="buscaSerie"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												>
												<img src="<%=request.getContextPath()%>/images/buscar.png" > 
												</button>&nbsp;&nbsp;
												
												<label id="codigoSerieLabel" "for="codigoSerie" >
														<c:out value="${busquedaHojaRutaFormulario.serie.prefijo}" default="" />			
												</label>
												
											
										</td>
										<td class="texto_ti">
											-
											<input type="text" id="serieDesde" maxlength="8"
												name="serieDesde" style="width: 70px;" class="integer"
												value="<c:out value="${busquedaHojaRutaFormulario.serieDesde}" default="" />"
												/>
										</td>
										<td class="texto_ti">
											<input type="text" id="serieHasta" maxlength="8"
												name="serieHasta" style="width: 70px;" class="integer"
												value="<c:out value="${busquedaHojaRutaFormulario.serieHasta}" default="" />"
												/>
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
											<spring:message code="formularioHojaRuta.busqueda.fechaDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioHojaRuta.busqueda.fechaHasta" htmlEscape="true"/>
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="fechaDesde" name="fechaDesde" style="width: 80px;"
												value="<c:out value="${busquedaHojaRutaFormulario.fechaDesdeStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'busquedaHojaRutaFormulario',
														// input name
														'controlname': 'fechaDesde'
													});
												</script>
											</c:if>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaHasta" name="fechaHasta" style="width: 80px;"
												value="<c:out value="${busquedaHojaRutaFormulario.fechaHastaStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'busquedaHojaRutaFormulario',
														// input name
														'controlname': 'fechaHasta'
													});
												</script>
											</c:if>
										</td>
										<td class="texto_ti" rowspan="2">
												<input type="checkbox" style="width: 70px;" id="estado" name="estado" 
												    <c:if test="${busquedaHojaRutaFormulario.estado==true}">
												    	checked="checked"
												    </c:if>
												  
													 /> 
													Pendiente<br />
										</td>
									</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="hojaRuta" id="hojaRuta"
							requestURI="consultaHojaRuta.html" pagesize="20" sort="external" partialList="true" size="${size}">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${hojaRuta.id}"/>
							    <input type="hidden" id="hdn_estado" value="${hojaRuta.estado}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column sortName="formularioHojaRuta.lista.fecha" property="fechaStr" titleKey="formularioHojaRuta.lista.fecha" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	
		           		   	<display:column titleKey="formularioHojaRuta.lista.numero" sortable="false" class="celdaAlineadoIzquierda">
								<c:out value="${hojaRuta.serie.codigo}" default="" />: <c:out value="${hojaRuta.serie.prefijo}" default="" /> - <c:out value="${hojaRuta.numeroStr}" default="" />
							</display:column>	
		           		   	
							<display:column property="empresa.razonSocial.razonSocial" titleKey="formularioHojaRuta.lista.empresa" sortable="false" class="celdaAlineadoIzquierda"/>
							<display:column property="sucursal.descripcion" titleKey="formularioHojaRuta.lista.sucursal" sortable="false" class="celdaAlineadoIzquierda"/>
							<display:column property="transporte.descripcion" titleKey="formularioHojaRuta.lista.transporte" sortable="false" class="celdaAlineadoIzquierda"/>
							<display:column property="estado" titleKey="formularioHojaRuta.lista.estado" sortable="false" class="celdaAlineadoIzquierda"/>
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" id="nuevaHojaRuta" class="botonCentrado" style="width:200px;">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="formularioHojaRuta.nuevaHojaRuta" htmlEscape="true"/>  
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