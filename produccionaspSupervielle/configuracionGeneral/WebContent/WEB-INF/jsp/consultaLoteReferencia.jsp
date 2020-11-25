<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioLoteReferencia.titulo" htmlEscape="true"/>
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
		<script type="text/javascript" src="js/consulta_lote_referencia.js"></script>
		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
		      <li id="agregarRango"><img src="images/modificar.png" /> <font size="2"><spring:message code="formularioLoteReferencia.boton.agregarRango" htmlEscape="true"/></font></li>
		      <sec:authorize ifAnyGranted="ROLE_ELIMINAR_LOTE_REFERENCIAS">  
		     	 <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		     </sec:authorize>	 
		      <li id="imprimir"><img src="images/print.png" /> <font size="2"><spring:message code="botones.imprimir" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioLoteReferencia.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="consultaLotesReferencia.html" commandName="busquedaLoteReferenciaFormulario" method="post">
						<input type="hidden" id="clienteId" name="idClienteAsp"
							value="<c:out value="${busquedaLoteReferenciaFormulario.idClienteAsp}" default="" />"/>
						<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
						<input type="hidden" id="mensajeSeleccioneCliente" name="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
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
											<spring:message code="formularioLoteReferencia.busqueda.empresa" htmlEscape="true"/>
										</td>
										<td class="texto_ti"  >
											<spring:message code="formularioLoteReferencia.busqueda.cliente" htmlEscape="true"/>
										</td>										
										<%-- <td class="texto_ti"  >
											<spring:message code="formularioLoteReferencia.busqueda.cliente" htmlEscape="true"/>
										</td> --%>
										<td class="texto_ti">
											<spring:message code="formularioLoteReferencia.busqueda.usuario.carga" htmlEscape="true"/>
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
												value="<c:out value="${busquedaLoteReferenciaFormulario.codigoEmpresa}" default="" />" <c:if test='${empleadoSession!=null }'> readonly="readonly"</c:if>/>
											&nbsp;&nbsp;
											<button type="button" id="buscaEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA' || (empleadoSession!=null)}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoEmpresaLabel" for="codigoEmpresa"> 
												 
											</label>
										</td>										
									<%-- 	<td class="texto_ti">
											<input type="text" id="codigoSucursal" hidden= "true" name="codigoSucursal"  style="width: 50px;" <c:if test='${empleadoSession!=null }'> readonly="readonly"</c:if>
						se saca Adrian						value="<c:out value="${busquedaLoteReferenciaFormulario.codigoSucursal}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaSucursal" hidden= "true"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA' || (empleadoSession!=null)}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoSucursalLabel"  for="codigoSucursal" > 
												 
											</label>
										</td> --%>
										<td class="texto_ti">
											<input type="text" id="codigoCliente" name="codigoCliente" style="width: 50px;" <c:if test='${empleadoSession!=null }'> readonly="readonly"</c:if>
												value="<c:out value="${busquedaLoteReferenciaFormulario.codigoCliente}" default="" />"/>
											&nbsp;&nbsp;
											<button type="button" id="buscaCliente" 
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA' || (empleadoSession!=null)}">
													disabled="disabled"
												</c:if>
											>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>
											&nbsp;&nbsp; 
											<label id="codigoClienteLabel" for="codigoCliente"> 
												 
											</label>
										</td>
										<td class="texto_ti" style="width: 250px;">
										
											<input type="text" id="codigoPersonal" 
												name="codigoPersonal" maxlength="6" style="width: 50px;"
												value="<c:out value="${busquedaLoteReferenciaFormulario.codigoPersonal}" default="" />"
												<sec:authorize ifNotGranted="ROLE_CON_LOTEREF_CAMBIAR_USUARIO"> 
														readonly="readonly"
												</sec:authorize> 
												 />
												
													&nbsp;&nbsp;
													<button type="button"
														name="buscaPersonal" id="buscaPersonal"
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<sec:authorize ifNotGranted="ROLE_CON_LOTEREF_CAMBIAR_USUARIO"> 
															disabled="disabled"
														</sec:authorize> >
														<img src="<%=request.getContextPath()%>/images/buscar.png">
													</button>&nbsp;&nbsp;
													
													<label id="codigoPersonalLabel" for="codigoPersonal"> <c:out
														value="${busquedaLoteReferenciaFormulario.personal.nombreYApellido}" default="" /> 
													</label>
												
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioLoteReferencia.busqueda.codigoDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioLoteReferencia.busqueda.codigoHasta" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<spring:message code="formularioLoteReferencia.busqueda.fechaDesde" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioLoteReferencia.busqueda.fechaHasta" htmlEscape="true"/>
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="codigoDesde" name="codigoDesde" style="width: 50px;"
												value="<c:out value="${busquedaLoteReferenciaFormulario.codigoDesde}" default="" />"/>
										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoHasta" name="codigoHasta" style="width: 50px;"
												value="<c:out value="${busquedaLoteReferenciaFormulario.codigoHasta}" default="" />"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaDesde" name="fechaDesde" style="width: 80px;"
												value="<c:out value="${busquedaLoteReferenciaFormulario.fechaDesdeStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'busquedaLoteReferenciaFormulario',
														// input name
														'controlname': 'fechaDesde'
													});
												</script>
											</c:if>
										</td>
										<td class="texto_ti">
											<input type="text" id="fechaHasta" name="fechaHasta" style="width: 80px;"
												value="<c:out value="${busquedaLoteReferenciaFormulario.fechaHastaStr}" default="" />"/>
											<c:if test="${accion != 'CONSULTA'}">
												<script type="text/javascript" >
													new tcal ({
														// form name
														'formname': 'busquedaLoteReferenciaFormulario',
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
						<display:table name="lotesReferencia" id="loteReferencia"
							requestURI="consultaLotesReferencia.html" pagesize="20" sort="external" partialList="true" size="${size}">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${loteReferencia.id}"/>
			              	</display:column>
			              	<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_porRango" value="${loteReferencia.cargaPorRango}"/>
			              	</display:column>				
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column sortName="formularioLoteReferencia.lista.codigo" property="codigo" titleKey="formularioLoteReferencia.lista.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="formularioLoteReferencia.lista.fecha" property="fechaRegistroStr" titleKey="formularioLoteReferencia.lista.fecha" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="usuarioCarga" title="Usr. Carga" sortable="false" class="celdaAlineadoIzquierda"/>
						  	<display:column property="cantidadRef" title="Cantidad Ref" sortable="false" class="celdaAlineadoIzquierda"/>
							<display:column property="empresaStr" titleKey="formularioLoteReferencia.lista.empresa" sortable="false" class="celdaAlineadoIzquierda"/>
							<display:column property="sucursalStr" titleKey="formularioLoteReferencia.lista.sucursal" sortable="false" class="celdaAlineadoIzquierda"/>
							<display:column property="clienteEmpStr" titleKey="formularioLoteReferencia.lista.cliente" sortable="false" class="celdaAlineadoIzquierda"/>
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" id="cargaIndividual" class="botonCentrado" style="width:200px;">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="formularioLoteReferencia.cargaIndividual" htmlEscape="true"/>  
							</button>
							&nbsp;&nbsp;
							<button name="agregar" type="button" id="cargaGrupal" class="botonCentrado" style="width:200px;">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="formularioLoteReferencia.cargaGrupal" htmlEscape="true"/>  
							</button>
							&nbsp;&nbsp;
							<button name="agregar" type="button" id="cargaRango" class="botonCentrado" style="width:200px;">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="formularioLoteReferencia.cargaPorRango" htmlEscape="true"/>  
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