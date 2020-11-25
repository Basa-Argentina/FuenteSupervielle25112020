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
			<spring:message code="formularioExportacionReferencia.titulo" htmlEscape="true"/>
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
		<script type="text/javascript" src="js/doblelistas.js"></script>
		<script type="text/javascript" src="js/formulario_exportacion_referencia.js"></script>
		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">

	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioExportacionReferencia.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="generarExcelReferencias.html" commandName="datosParaExportacion" method="post">
						<input type="hidden" id="elementosSeleccionados" name="elementosSeleccionados"/>
						<input type="hidden" id="clienteId" name="idClienteAsp"
							value="<c:out value="${datosParaExportacion.idClienteAsp}" default="" />"/>
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
							<div style="background-color: #f1e87d; WIDTH: auto; "  id="busquedaDiv" align="center">
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioExportacionReferencia.busqueda.empresa" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioExportacionReferencia.busqueda.sucursal" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioExportacionReferencia.busqueda.cliente" htmlEscape="true"/>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="codigoEmpresa" name="codigoEmpresa" style="width: 50px;"
												value="<c:out value="${datosParaExportacion.codigoEmpresa}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoEmpresaLabel" for="codigoEmpresa"> 
												 
											</label>
										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoSucursal" name="codigoSucursal" style="width: 50px;"
												value="<c:out value="${datosParaExportacion.codigoSucursal}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaSucursal"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"												
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoSucursalLabel" for="codigoSucursal"> 
												 
											</label>
										</td>
										<td class="texto_ti">
											<input type="text" id="codigoCliente" name="codigoCliente" style="width: 50px;"
												value="<c:out value="${datosParaExportacion.codigoCliente}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaCliente" 
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"								
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoClienteLabel" for="codigoCliente"> 
												 
											</label>
										</td>
									</tr>
								</table>
							</div>	
							<div align="left"><br>							
								<input type="radio" id="filtrarPor" name="filtrarPor" value="ClasificacionDocumental"
									<c:if test="${datosParaExportacion.filtrarPor != 'Empleado'}">
										checked="checked"
									</c:if> 
								> Clasificacion Documental&nbsp;&nbsp;
								<input type="radio" id="filtrarPor" name="filtrarPor" value="Empleado"
									<c:if test="${datosParaExportacion.filtrarPor == 'Empleado'}">
										checked="checked"
									</c:if> 
								> Empleado&nbsp;&nbsp;			
									<input type="text" id="codigoClasificacionDocumental" name="codigoClasificacionDocumental" tabindex="3"
										style="width: 80px; " class="requerido"
										value="<c:out value="${datosParaExportacion.codigoClasificacionDocumental}" default="" />"/>
									<button type="button" id="buscaClasificacionDocumental"
										title="<spring:message code="textos.buscar" htmlEscape="true"/>">
										<img src="<%=request.getContextPath()%>/images/buscar.png">
									</button>
									&nbsp;&nbsp; 
									<label id="codigoClasificacionDocumentalLabel" for="codigoClasificacionDocumental"> 												 
									</label>
									<input type="text" id="codigoPersonal" name="codigoPersonal" tabindex="3"
										style="width: 80px; display:none;" class="requerido"
										value="<c:out value="${datosParaExportacion.codigoPersonal}" default="" />"/>
									<button type="button" id="buscaPersonal"
										style="display:none;" 
										title="<spring:message code="textos.buscar" htmlEscape="true"/>">
										<img src="<%=request.getContextPath()%>/images/buscar.png">
									</button>
									&nbsp;&nbsp; 
									<label id="codigoPersonalLabel" for="codigoPersonal"> 												 
									</label>

							</div>
							<br style="font-size: xx-small;"/>
							<div align="left">
								<input type="checkbox" id="enviarMail" name="enviarMail" style="display:none;" value="true"
									<c:if test="${datosParaExportacion.enviarMail}">
										checked="checked"
									</c:if> 
								/>
								<label id="envioMailLabel" style="display: none;">
									<spring:message code="formularioExportacionReferencia.envioMail" htmlEscape="true"/> 												 
								</label>&nbsp;&nbsp;	
								<input type="checkbox" id="enviarConCopia" name="enviarConCopia" style="display:none;" value="true"
									<c:if test="${datosParaExportacion.enviarConCopia}">
										checked="checked"
									</c:if> 
								/>
								<label id="envioConCopiaLabel" style="display: none;">
									<spring:message code="formularioExportacionReferencia.envioConCopia" htmlEscape="true"/> 												 
								</label>						
							</div>
							<br style="font-size: xx-small;"/>
							<div align="left">
							<table>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioExportacionReferencia.tiposElementosDisponible" htmlEscape="true"/>
									</td>
									<td>
										&nbsp;
									</td>
									<td class="texto_ti">
										<spring:message code="formularioExportacionReferencia.tiposElementosSeleccionado" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
							      <td width="40%" align="center">
							        <select id="elementosSeleccionadosIzq" name="elementosSeleccionadosIzq" size="8" style="width:250px" multiple="multiple">
							        	<c:forEach items="${elementosDisponibles}" var="elementoIzq">
											<option value="<c:out value="${elementoIzq.id}"/>">
												<c:out value="${elementoIzq.codigo} ${elementoIzq.descripcion}"/>
											</option>
										</c:forEach>
							        </select>
							      </td>
							      <td width="20%" align="center">											          
							          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('elementosSeleccionados')" 
							        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
							          	>
							          <br />
							          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('elementosSeleccionados')" 
							        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
							          >
							          <br />
							      </td>
							      <td width="40%" align="center">
							        <select id="elementosSeleccionadosDer" name="elementosSeleccionadosDer" size="8" style="width:250px" multiple="multiple">
							            <c:forEach items="${elementsSeleccionados}" var="elementoDer">
											<option value="<c:out value="${elementoDer.id}"/>">
												<c:out value="${elementoDer.codigo} ${elementoDer.descripcion}"/>
											</option>
										</c:forEach>
							        </select>
							      </td>
							    </tr>
						    </table>
							</div>
							<br style="font-size: xx-small;"/>
							<div align="center">
								<button id="btnExportar" name="exportar" type="submit" class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/export.png"/> 
								<spring:message code="botones.exportar" htmlEscape="true"/>  
								</button>						
							</div>
						</fieldset>
					</form:form>
					<br style="font-size: xx-small;"/>
					<div align="center">
						<button name="volver_atras" id="volver_atras" type="button" class="botonCentrado">
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
		<div id="pop" style="display:none">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
		</div>
	</body>
</html>