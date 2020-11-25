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
			<spring:message code="formularioPosicionLibre.titulo" htmlEscape="true"/>
		</title>
		
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>		
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/mavalos_consulta_posicionLibre.js"></script>
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
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
			        		<spring:message code="formularioPosicionLibre.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarPosicionLibre.html" commandName="posicionLibreBusqueda" method="post" name="formBusqueda">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="codigoEmpresa" name="codigoEmpresa" value="<c:out value="${codigoEmpresa}" default="" />"/>
					<input type="hidden" id="mensajeSeleccioneTipoJerarquia" value="<spring:message code="notif.seleccion.seleccionTipoJerarquia" htmlEscape="true"/>"/>
						<br style="font-size: xx-small;" />
				<fieldset>
					<legend>
						<spring:message code="formularioPosicionLibre.datosPosicionLibre.elementos"
							htmlEscape="true" />
					</legend>
						<c:if test="${accion != 'GUARDAR'}">
							<spring:message
								code="formularioPosicionLibre.posicionLibreDetalle.lectura"
								htmlEscape="true" />
								
															
							<input type="text" id="codigoLectura" class="integer"
								name="codigoLectura" maxlength="6" style="width: 50px;"
								value="<c:out value="${codigoLectura}" default="" />"
								 />
								&nbsp;&nbsp;
								<button type="button" id="buscaLectura"
									title="<spring:message code="textos.buscar" htmlEscape="true"/>"
									>
									<img src="<%=request.getContextPath()%>/images/buscar.png">
								</button>&nbsp;&nbsp; <label id="codigoLecturaLabel"
								for="codigoLectura" ><c:out value="${descripcion}" default="" /></label>
												
																					
							<input type="button" name="importar" id="importar"
								class="botonCentrado"
								value="<spring:message code="botones.importar" htmlEscape="true"/>" />
						</c:if>
						<br style="font-size: xx-small;"/>
						<br style="font-size: xx-small;"/>
						<display:table name="listaElementos" id="lecturaDetalle" requestURI="mostrarPosicionLibre.html"
						pagesize="10" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${lecturaDetalle.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						  	<display:column property="elemento.tipoElemento.descripcion" titleKey="formularioElemento.nombreElemento" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="elemento.codigo" titleKey="formularioElemento.datosElemento.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="elemento.posicion.estante.grupo.seccion.deposito.descripcion"   titleKey="formularioElemento.deposito" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="elemento.posicion.estante.grupo.seccion.descripcion"   titleKey="formularioElemento.seccion" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
						  	<display:column property="elemento.posicion.codigo"   titleKey="formularioElemento.datosElemento.posicion" sortable="true" class="celdaAlineadoIzquierda"/>
					</display:table>
				</fieldset>
				<br style="font-size: xx-small;"/>
				<c:if test="${accion != 'GUARDAR'}">	
				<br style="font-size: xx-small;"/>	
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
									<td class="texto_td"><spring:message
											code="formularioPosicion.posDesde" htmlEscape="true" />
										</td>
										<td class="texto_ti">
										<spring:message
											code="formularioPosicion.posVer" htmlEscape="true" />
										<input type="text" id="posDesdeVert" class="requerido"
										name="posDesdeVert" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionLibreBusqueda.posDesdeVert}" default="" />"/>
										<spring:message
											code="formularioPosicion.posHor" htmlEscape="true" />
										<input type="text" id="posDesdeHor" class="requerido"
										name="posDesdeHor" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionLibreBusqueda.posDesdeHor}" default="" />"/>
									</td>
										<td class="texto_td"><spring:message
											code="formularioPosicion.posHasta" htmlEscape="true" />
										</td>
										<td class="texto_ti">
										<spring:message
											code="formularioPosicion.posVer" htmlEscape="true" />
										<input type="text" id="posHastaVert" class="requerido"
										name="posHastaVert" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionLibreBusqueda.posHastaVert}" default="" />"/>
										<spring:message
											code="formularioPosicion.posHor" htmlEscape="true" />
										<input type="text" id="posHastaHor" class="requerido"
										name="posHastaHor" maxlength="3" style="width: 50px;"
										value="<c:out value="${posicionLibreBusqueda.posHastaHor}" default="" />"/>
									</td>	
										
									</tr>
									<tr>
										<td class="texto_td">
											<spring:message code="formularioPosicionLibre.datosPosicionLibre.codigoDesdeEstante" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="codigoDesdeEstante" maxlength="4" name="codigoDesdeEstante" class="inputTextNumericPositiveIntegerOnly requerido"
												value="<c:out value="${posicionLibreBusqueda.codigoDesdeEstante}" default="" />"/>
										</td>									
										<td class="texto_td">
											<spring:message code="formularioPosicionLibre.datosPosicionLibre.codigoHastaEstante" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoHastaEstante" maxlength="4" name="codigoHastaEstante" class="inputTextNumericPositiveIntegerOnly requerido"
												value="<c:out value="${posicionLibreBusqueda.codigoHastaEstante}" default="" />"/>
										</td>
									</tr>
									<tr>
										<td class="texto_td">
											<spring:message code="formularioPosicionLibre.datosPosicionLibre.codigoDesdeModulo" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="codigoDesdeModulo" maxlength="12" name="codigoDesdeModulo" class="inputTextNumericPositiveIntegerOnly"
												value="<c:out value="${posicionLibreBusqueda.codigoDesdeModulo}" default="" />"/>
										</td>									
										<td class="texto_td">
											<spring:message code="formularioPosicionLibre.datosPosicionLibre.codigoHastaModulo" htmlEscape="true"/>
										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoHastaModulo" maxlength="12" name="codigoHastaModulo" class="inputTextNumericPositiveIntegerOnly"
												value="<c:out value="${posicionLibreBusqueda.codigoHastaModulo}" default="" />"/>
										</td>										
										<td rowspan="2" style="vertical-align: bottom;">
											<button name="buscar" id="buscar" class="botonCentrado" type="button">
												<img src="<%=request.getContextPath()%>/images/buscar.png"> 
												<spring:message code="textos.buscar" htmlEscape="true"/>								
											</button>
										</td>																								
									</tr>
									<tr>
										<td class="texto_td">
											<spring:message code="formularioTipoJerarquia.tabla.tipoJerarquia" htmlEscape="true"/>
										</td>
											<td class="texto_ti">
											<input type="text" id="codigoTipoJerarquia" class="integer"
												name="codigoTipoJerarquia" maxlength="6" style="width: 50px;"
												value="<c:out value="${posicionLibreBusqueda.codigoTipoJerarquia}" default="" />"
												 />
												&nbsp;&nbsp;
												<button type="button" id="buscaTipoJerarquia"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>&nbsp;&nbsp; <label id="codigoTipoJerarquiaLabel"
												for="codigoTipoJerarquia" ></label>
										</td>
										<td class="texto_td">
											<spring:message code="formularioJerarquia.datos.jerarquia" htmlEscape="true"/>
										</td>
											<td class="texto_ti">
											<input type="text" id="codigoJerarquia" class="integer"
												name="codigoJerarquia" maxlength="6" style="width: 50px;"
												value="<c:out value="${posicionLibreBusqueda.codigoJerarquia}" default="" />"/>
												&nbsp;&nbsp;
												<button type="button" id="buscaJerarquia"
													title="<spring:message code="textos.buscar" htmlEscape="true"/>"
													>
													<img src="<%=request.getContextPath()%>/images/buscar.png">
												</button>&nbsp;&nbsp; <label id="codigoJerarquiaLabel"
												for="codigoJerarquia" ></label>
										</td>		
									</tr>
								</table>
							</div>	
						</fieldset>
						</c:if>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="posicionesLibres" id="posicionLibre" requestURI="mostrarPosicionLibre.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${posicionLibre.pos.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column property="pos.estante.grupo.seccion.deposito.descripcion" titleKey="formularioPosicionLibre.datosPosicionLibre.desposito" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="pos.estante.grupo.seccion.descripcion" titleKey="formularioPosicionLibre.datosPosicionLibre.seccion" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   	<display:column property="pos.estante.codigo" titleKey="formularioPosicionLibre.datosPosicionLibre.estante" sortable="true" class="celdaAlineadoIzquierda"/>
		           		   <display:column titleKey="formularioPosicionLibre.datosPosicionLibre.codigo" sortable="true" class="celdaAlineadoIzquierda">
		           		   		<c:out value="(${posicionLibre.pos.posVertical};${posicionLibre.pos.posHorizontal})"/>
		           		   	</display:column>
							<display:column property="valoracion" titleKey="formularioPosicionLibre.datosPosicionLibre.valoracion" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="pos.elementoAsignado.codigo" titleKey="formularioPosicionLibre.datosPosicionLibre.elemento" sortable="true" class="celdaAlineadoIzquierda"/>
						</display:table>
					<c:if test="${accion == 'ASIGNAR'}">
					<br style="font-size: xx-small;"/>
						<div style="width: 100%" align="center">
							<button name="asignar" id="asignar" type="button"
								class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/add.png">
								<spring:message code="formularioPosicionLibre.boton.asignar" htmlEscape="true" />
							</button>
						</div>
					</c:if>
					<c:if test="${accion == 'GUARDAR'}">
					<br style="font-size: xx-small;"/>
						<div style="width: 100%" align="center">
							<table>
								<tr>
									<td>
										<button name="guardar" id="guardar" type="button"
											class="botonCentrado">
											<img src="<%=request.getContextPath()%>/images/add.png">
											<spring:message code="formularioPosicionLibre.boton.guardar"
												htmlEscape="true" />
										</button></td>
									<td>
									<button name="cancelar" id="cancelar" type="button"
											class="botonCentrado">
											<img src="<%=request.getContextPath()%>/images/cancelar.png">
											<spring:message code="botones.cancelar"
												htmlEscape="true" />
									</button>
									</td>
								</tr>
							</table>
						</div>
					</c:if>
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
		<div id="pop">
			<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
			<label>Buscando Posiciones Libres. Espere por favor...</label>	     
		</div>
	</body>
</html>