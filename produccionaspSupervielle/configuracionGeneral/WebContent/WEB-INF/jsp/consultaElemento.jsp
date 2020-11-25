<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>




<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioElemento.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_consulta_elemento.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>	
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
		      <li id="modificar_seleccionados"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar.seleccionados" htmlEscape="true"/></font></li>
		      <sec:authorize ifAnyGranted="ROLE_ABM_ELIMINAR_ELEMENTO"> 
		      	<li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		      </sec:authorize>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioElemento.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarElemento.html" commandName="elementoBusqueda" method="post">
						<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
						<input type="hidden" id="errorSeleccioneEmpresa" name="errorSeleccioneEmpresa" value="<spring:message code="notif.stock.seleccionEmpresa" htmlEscape="true"/>" />
						<input type="hidden" id="errorSeleccioneSucursal" name="errorSeleccioneSucursal" value="<spring:message code="notif.stock.seleccionSucursal" htmlEscape="true"/>" />
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
										<td class="texto_ti"><spring:message
												code="formularioElemento.empresa" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.sucursal" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.deposito"
												htmlEscape="true" /></td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.vacio" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.tamañoPagina" htmlEscape="true" />
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti"><input type="text" id="codigoEmpresa"
											name="codigoEmpresa" maxlength="4" style="width: 50px;"
											value="<c:out value="${elementoBusqueda.codigoEmpresa}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupEmpresa"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoEmpresaLabel"
											for="codigoEmpresa"> <c:out
													value="${elementoBusqueda.clienteEmp.empresa.descripcion}"
													default="" /> </label></td>
										<td class="texto_ti"><input type="text" id="codigoSucursal"
											name="codigoSucursal" maxlength="4" style="width: 50px;"
											value="<c:out value="${elementoBusqueda.codigoSucursal}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupSucursales"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoSucursalLabel"
											for="codigoSucursal"> <c:out
													value="${elementoBusqueda.posicion.estante.grupo.seccion.deposito.sucursal.descripcion}"
													default="" /> </label></td>
										<td class="texto_ti"><input type="text" id="codigoDeposito"
											name="codigoDeposito" maxlength="2" style="width: 50px;"
											value="<c:out value="${elementoBusqueda.codigoDeposito}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupDeposito"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoDepositoLabel"
											for="codigoDeposito"> <c:out
													value="${elementoBusqueda.posicion.estante.grupo.seccion.deposito.descripcion}"
													default="" /> </label>
										</td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.vacio" htmlEscape="true" />
										</td>
										<td class="texto_ti">
											<select id="pagesize" name="pagesize" size="1">
												<option label="20" value="20" <c:if test="${pagesize == '20'}">
														selected="selected"
													</c:if>>20</option>
												<option label="40" value="40" <c:if test="${pagesize == '40'}">
														selected="selected"
													</c:if>>40</option>
												<option label="60" value="60" <c:if test="${pagesize == '60'}">
														selected="selected"
													</c:if>>60</option>
												<option label="80" value="80" <c:if test="${pagesize == '80'}">
														selected="selected"
													</c:if>>80</option>
												<option label="100" value="100" <c:if test="${pagesize == '100'}">
														selected="selected"
													</c:if>>100</option>
												<option label="200" value="200" <c:if test="${pagesize == '200'}">
														selected="selected"
													</c:if>>200</option>
												<option label="500" value="500" <c:if test="${pagesize == '500'}">
														selected="selected"
													</c:if>>500</option>
												<option label="Todos" value="Todos" <c:if test="${pagesize == size}">
														selected="selected"
													</c:if>>Todos</option>
											</select>
										</td>
										
									</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioElemento.cliente" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.contenedor" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.datosElemento.estado" htmlEscape="true" />
										</td>
										<td class="texto_ti"><spring:message
												code="formularioElemento.vacio" htmlEscape="true" />
										</td>
										<td rowspan="2" style="vertical-align: bottom;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png">
												<spring:message code="textos.buscar" htmlEscape="true" />
											</button>
										</td>											
									</tr>
									<tr>
										<td class="texto_ti"><input type="text" id="codigoCliente"
											name="codigoCliente" maxlength="6" style="width: 50px;"
											value="<c:out value="${elementoBusqueda.codigoCliente}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupCliente"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoClienteLabel"
											for="codigoCliente"> <c:out
													value="${elementoBusqueda.clienteEmp.nombreYApellido}"
													default="" /> </label>
										</td>
										<td class="texto_ti"><input type="text" id="codigoContenedor"
											name="codigoContenedor" maxlength="12" style="width: 90px;"
											value="<c:out value="${elementoBusqueda.codigoElemento}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
											&nbsp;&nbsp;
											<button type="button" id="botonPopupContenedor"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; <label id="codigoContenedorLabel"
											for="codigoContenedor"> <c:out
													value="${elementoBusqueda.tipoElemento.descripcion}"
													default="" /> </label>
										</td>
										<td class="texto_ti">
											<select id="estado"
												name="estado" size="1">
												
													<option label="Seleccione un Estado" value="">Seleccione un Estado</option>
													<option label="Creado" value="Creado" <c:if test="${elementoBusqueda.estado == 'Creado'}">
															selected="selected"
														</c:if>>Creado</option>
															
													<option label="En Guarda" value="En Guarda" <c:if test="${elementoBusqueda.estado == 'En Guarda'}">
															selected="selected"
														</c:if>>En Guarda</option>
													<option label="En Transito" value="En Transito" <c:if test="${elementoBusqueda.estado == 'En Transito'}">
															selected="selected"
														</c:if>>En Transito</option>
													<option label="En Consulta" value="En Consulta" <c:if test="${elementoBusqueda.estado == 'En Consulta'}">
															selected="selected"
														</c:if>>En Consulta</option>
													<option label="No Existe" value="No Existe" <c:if test="${elementoBusqueda.estado == 'No Existe'}">
															selected="selected"
														</c:if>>No Existe</option>
													<option label="Unificado" value="Unificado" <c:if test="${elementoBusqueda.estado == 'Unificado'}">
															selected="selected"
														</c:if>>Unificado</option>
											</select>
										</td>
																																					
									</tr>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioElemento.desde" htmlEscape="true" />
										
										<label>      </label>
										<spring:message
												code="formularioElemento.hasta" htmlEscape="true" />
										</td>
										<td class="texto_ti">
										<spring:message
												code="formularioElemento.lectura" htmlEscape="true" />
										</td>
										<td class="texto_ti">
										<spring:message
												code="formularioElemento.tipoElemento" htmlEscape="true" />
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
											<input type="text" id="codigoDesde" name="codigoDesde" maxlength="12" size="12"
												value="<c:out value="${elementoBusqueda.codigoDesde}" default="" />"/>
											<label> a </label>
											<input type="text" id="codigoHasta" name="codigoHasta" maxlength="12" size="12"
												value="<c:out value="${elementoBusqueda.codigoHasta}" default="" />"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="codigoLectura" name="codigoLectura"
								maxlength="6" style="width: 50px;"
								value="<c:out value="${elementoBusqueda.codigoLectura}" default="" />" />
						&nbsp;&nbsp;
						<button type="button" id="botonPopupLectura"
								title="<spring:message code="textos.buscar" htmlEscape="true"/>">
								<img src="<%=request.getContextPath()%>/images/buscar.png">
							</button>
						&nbsp;&nbsp; <label id="codigoLecturaLabel" for="codigoLectura">
								<c:out value="${descripcion}" default="" /> </label>
										</td>
						<td class="texto_ti"><input type="text"
							id="codigoTipoElemento"	name="codigoTipoElemento" 
							maxlength="6" style="width: 50px;"
							value="<c:out value="${elementoBusqueda.codigoTipoElemento}" default="" />"/>
							&nbsp;&nbsp;
							<button type="button" id="botonPopupTiposElementos"
								title="<spring:message code="textos.buscar" htmlEscape="true"/>">
								<img src="<%=request.getContextPath()%>/images/buscar.png">
							</button>&nbsp;&nbsp; <label id="codigoTipoElementoLabel"
							for="codigoTipoElemento"> <c:out
									value="${elementoBusqueda.tipoElemento.descripcion}"
									default="" /> </label>
						</td>
								</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					
					<fieldset>
						<display:table name="elementos" id="elemento"  pagesize="${pagesize}" sort="external" requestURI="mostrarElemento.html"
								partialList="true" size="${size}" export="true" defaultsort="4" >
							<display:column class="hidden" headerClass="hidden" media="html">
							    <input type="hidden" id="hdn_id" value="${elemento.id}"/>
			              	</display:column>	
			              	<display:column class="hidden" headerClass="hidden">
							    <c:out value="${elemento.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden" media="html">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="false"/>"/>
		           		   	</display:column>
		           		   	<display:column class="celdaAlineadoCentrado" media="html" sortable="false" title="<input type='checkbox' id='checktodos' name='checktodos'/>">
<%-- 		           		   		<c:if test="${!elemento.tipoElemento.contenedor}"> --%>
									<input type="checkbox" id="elemento_${elemento.id}" 
										name="elementoSeleccionados" value="${elemento.id}" class="selectableCheckbox" />
<%-- 								</c:if>	 --%>
							</display:column>		
							<display:column sortName="codigo" property="codigo" titleKey="formularioElemento.datosElemento.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="tipoElemento.descripcion" property="tipoElemento.descripcion" titleKey="formularioElemento.nombreElemento" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="clienteEmp.razonSocialONombreYApellido" property="clienteEmp.razonSocialONombreYApellido" titleKey="formularioElemento.cliente" sortable="false" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="depositoActual.descripcion" property="depositoActual.descripcion"   titleKey="formularioElemento.deposito" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column sortName="posicion.estante.grupo.seccion.descripcion" titleKey="formularioElemento.seccion" sortable="false" class="celdaAlineadoIzquierda">
						  	<c:choose>
								<c:when test="${elemento.contenedor != null}">
									<c:out value="${elemento.contenedor.posicion.estante.grupo.seccion.descripcion}"/>
								</c:when>
								<c:otherwise>
									<c:if test="${elemento.posicion != null}">
						  				<c:out value="${elemento.posicion.estante.grupo.seccion.descripcion}"/>
						  			</c:if>
								</c:otherwise>
							</c:choose>
						  	</display:column>
						  	<display:column sortName="elemento.posicion.posVerticalHorizontal" titleKey="formularioElemento.datosElemento.posicion" sortable="false" class="celdaAlineadoIzquierda">
							<c:choose>
								<c:when test="${elemento.contenedor != null}">
									<c:out value="E:${elemento.contenedor.posicion.estante.codigo} (${elemento.contenedor.posicion.posVertical};${elemento.contenedor.posicion.posHorizontal})"/>
								</c:when>
								<c:otherwise>
									<c:if test="${elemento.posicion.posVertical != null}">
										<c:out value="E:${elemento.posicion.estante.codigo} (${elemento.posicion.posVertical};${elemento.posicion.posHorizontal})"/>
									</c:if>
								</c:otherwise>
							</c:choose>
		           		   	</display:column>
		           		   	<display:column sortName="ubicacionProvisoria" property="ubicacionProvisoria" titleKey="formularioElemento.ubicacionProvisoria" sortable="true" class="celdaAlineadoIzquierda"/>						  							  	
						  	<display:column sortName="contenedor.codigo" property="contenedor.codigo"   titleKey="formularioElemento.contenedor" sortable="false" class="contenedor celdaAlineadoIzquierda">
						  		<input type="text" id="hdn_conten" value="${elemento.contenedor.codigo}" />
						  	</display:column>
						  	<display:column sortName="estado" property="estado"   titleKey="formularioElemento.datosElemento.estado" sortable="true" class="celdaAlineadoCentrado"/>
						  	<display:column sortName="cerrado" titleKey="formularioElemento.datosElemento.cerrado" sortable="false" class="celdaAlineadoCentrado">
						  		<c:if test="${elemento.tipoElemento.contenedor}">
						  			<c:if test="${elemento.cerrado!=null && elemento.cerrado}">
						  				Cerrado
						  			</c:if>
						  			<c:if test="${elemento.cerrado==null || !elemento.cerrado}">
						  				Abierto
						  			</c:if>
						  		</c:if>
						  	</display:column>
						  <display:column sortName="tipoTrabajo" property="tipoTrabajo"  titleKey="formularioElemento.datosElemento.tipoTrabajo" sortable="true" class="celdaAlineadoCentrado"/>	
						</display:table>
						<br>
						<div align="right">
						<button name="agregar" type="button" onclick="agregar();"
							class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/add.png">
							<spring:message code="botones.agregar" htmlEscape="true" />
						</button>
						</div>
				</fieldset>
				<br style="font-size: xx-small;"/>
						<div style="width: 100%">
						<fieldset >
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="imprimirImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.imprimir" htmlEscape="true"/>
							        		</font>
							        		<img id="imprimirImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="imprimirImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"  id="imprimirDiv" align="left">
								<table class="imprimir" style="width: 100%; background-color: white;">
								<tr>
								<td>
									<select id="cantImprimir" name="cantImprimir" size="1" style="width: 190px;">
										<option label="Solo página actual" value="1">Solo página actual</option>
										<option label="Todas las páginas" value="2">Todas las páginas</option>
									</select>
								</td>
								<td>
									<button id="imprimirModulos" name="imprimirModulos"
										type="button">
										<img src="<%=request.getContextPath()%>/images/impresora.gif">
										<spring:message code="botones.imprimirReporte" htmlEscape="true" />
									</button></td>
									<td>
									<button id="descargarImprimirModulos" name="descargarImprimirModulos"
										type="button">
										<img src="<%=request.getContextPath()%>/images/diskette.gif">
										<spring:message code="botones.descargarReporte" htmlEscape="true" />
									</button>
									</td>
									<td>
								</td>
							</tr>
						</table>
					</div>
					</fieldset>
					</div>			
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
			<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
		</div>
	</body>
</html>