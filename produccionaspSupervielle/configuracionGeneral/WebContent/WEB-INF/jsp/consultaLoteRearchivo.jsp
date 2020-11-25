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
			<spring:message code="formularioLoteRearchivo.titulo" htmlEscape="true"/>
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
		<script type="text/javascript" src="js/consulta_lote_rearchivo.js"></script>
		
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
			        		<spring:message code="formularioLoteRearchivo.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="consultaLotesRearchivo.html" commandName="busquedaLoteRearchivoFormulario" method="post">
						<input type="hidden" id="clienteId" name="clienteId"
							value="<c:out value="${busquedaLoteRearchivoFormulario.idClienteAsp}" default="" />"/>
						<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
						<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
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
											<spring:message code="formularioLoteRearchivo.busqueda.empresa" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.sucursal" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.cliente" htmlEscape="true"/>
										</td>
										<td class="texto_ti" rowspan="2">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png"> 
												<spring:message code="textos.buscar" htmlEscape="true"/>								
											</button>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="codigoEmpresa" name="codigoEmpresa" style="width: 50px;"
												value="<c:out value="${busquedaLoteRearchivoFormulario.codigoEmpresa}" default="" />"/>
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
												value="<c:out value="${busquedaLoteRearchivoFormulario.codigoSucursal}" default="" />"/>
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
											<input type="text" id="codigoCliente" name="codigoCliente" style="width: 50px;"
												value="<c:out value="${busquedaLoteRearchivoFormulario.codigoCliente}" default="" />"/>
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
											<spring:message code="formularioLoteRearchivo.busqueda.tipo" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.clasificacionDocumental" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.contenedor" htmlEscape="true"/>
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti">
											<select id="tipo" 
												name="tipo" size="1" style="width: 140px;"
												>
													<option value="Todos" 
														<c:if test="${busquedaLoteRearchivoFormulario.tipo == 'Todos'}">
																selected="selected"
														</c:if>>
													</option>
													<option label="Fisico" value="Fisico" 
														<c:if test="${busquedaLoteRearchivoFormulario.tipo == 'Fisico'}">
																selected="selected"
														</c:if>>
														<spring:message code="formularioLoteRearchivo.tipo.fisico" htmlEscape="true"/>
													</option>
													<option label="Lote" value="Lote" 
														<c:if test="${busquedaLoteRearchivoFormulario.tipo == 'Lote'}">
																selected="selected"
														</c:if>>
														<spring:message code="formularioLoteRearchivo.tipo.lote" htmlEscape="true"/>
													</option>
													<option label="Digital" value="Digital" 
														<c:if test="${busquedaLoteRearchivoFormulario.tipo == 'Digital'}">
																selected="selected"
														</c:if>>
														<spring:message code="formularioLoteRearchivo.tipo.digital" htmlEscape="true"/>
													</option>						
											</select>
										</td>
										<td class="texto_ti" >
										   
											<input type="text" id="codigoClasificacionDocumental" name="codigoClasificacionDocumental" 
												style="width: 80px;"
												value="<c:out value="${busquedaLoteRearchivoFormulario.codigoClasificacionDocumental}" default="" />"
												
												/>
											&nbsp;&nbsp;
											<button type="button" id="buscaClasificacionDocumental"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoClasificacionDocumentalLabel" for="codigoClasificacionDocumental"> 

											</label>
										</td>
										<td class="texto_ti">
											
											<input type="text" id="codigoContenedor" name="codigoContenedor" 
												style="width: 90px;" 
												value="<c:out value="${busquedaLoteRearchivoFormulario.codigoContenedor}" default="" />"
											/>
											
											&nbsp;&nbsp;
											<button type="button" id="buscaContenedor"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoContenedorLabel" for="codigoContenedor"> 
												 
											</label>
										</td>
									</tr>	
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.codigoDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.codigoHasta" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.fechaDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioLoteRearchivo.busqueda.fechaHasta" htmlEscape="true"/>
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="codigoDesde" name="codigoDesde" style="width: 50px;"
												value="<c:out value="${busquedaLoteRearchivoFormulario.codigoDesde}" default="" />"/>
										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoHasta" name="codigoHasta" style="width: 50px;"
												value="<c:out value="${busquedaLoteRearchivoFormulario.codigoHasta}" default="" />"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaDesde" name="fechaDesde" style="width: 80px;"
												value="<c:out value="${busquedaLoteRearchivoFormulario.fechaDesdeStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'busquedaLoteRearchivoFormulario',
														// input name
														'controlname': 'fechaDesde'
													});
												</script>
											</c:if>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaHasta" name="fechaHasta" style="width: 80px;"
												value="<c:out value="${busquedaLoteRearchivoFormulario.fechaHastaStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'busquedaLoteRearchivoFormulario',
														// input name
														'controlname': 'fechaHasta'
													});
												</script>
											</c:if>
										</td>
									</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="lotesRearchivo" id="loteRearchivo"
							requestURI="consultaLotesRearchivo.html" pagesize="10" sort="external" partialList="true" size="${size}">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${loteRearchivo.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column sortName="id" property="id" titleKey="formularioLoteRearchivo.lista.codigo" sortable="true" sortProperty="id" class="celdaAlineadoIzquierda"/>
		           		   	<display:column sortName="tipo" property="tipo" titleKey="formularioLoteRearchivo.lista.tipo" sortProperty="tipo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="fechaRegistroStr" property="fechaRegistroStr" titleKey="formularioLoteRearchivo.lista.fecha" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="empresa.razonSocial.razonSocial" titleKey="formularioLoteRearchivo.lista.empresa" class="celdaAlineadoIzquierda"/>
							<display:column property="sucursal.descripcion" titleKey="formularioLoteRearchivo.lista.sucursal" class="celdaAlineadoIzquierda"/>
							<display:column property="clienteEmp.razonSocialONombreYApellido" titleKey="formularioLoteRearchivo.lista.cliente" class="celdaAlineadoIzquierda"/>
							
							<display:column property="clasificacionDocumental.nombre" titleKey="formularioLoteRearchivo.lista.clasificacionDocumental" class="celdaAlineadoIzquierda"/>
							<display:column sortName="indiceIndividualStr" property="indiceIndividualStr" titleKey="formularioLoteRearchivo.lista.tipoIndice" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="contenedor.codigo" titleKey="formularioLoteRearchivo.lista.contenedor"  class="celdaAlineadoIzquierda"/>
							<display:column sortName="cantidad" property="cantidad" titleKey="formularioLoteRearchivo.lista.cantidad" sortProperty="cantidad" sortable="true" class="celdaAlineadoDerecha"/>
							<display:column property="remito.letraYNumeroComprobante" titleKey="formularioLoteRearchivo.lista.remito" class="celdaAlineadoIzquierda"/>
							
							
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" onclick="agregar();" class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="botones.agregar" htmlEscape="true"/>  
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