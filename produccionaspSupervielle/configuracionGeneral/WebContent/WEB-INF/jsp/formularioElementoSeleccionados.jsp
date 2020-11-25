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
		<script type="text/javascript" src="js/busquedaHelper.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_elemento_seleccionados.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>	
		<style type="text/css">
		.cascade-loading {
			background: transparent
				url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
				center;
		}
		</style>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioElemento.titulo.elementoSeleccionados" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<br style="font-size: xx-small;"/>
								<form:form action="guardarActualizarElementoSeleccionados.html" commandName="elementoBusqueda" method="post">
									
									<fieldset>
										<display:table name="sessionScope.elementos" id="elemento" >
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
												<input type="checkbox" id="elemento_${elemento.id}" 
													name="elementoSeleccionados" value="${elemento.id}" 
													class="selectableCheckbox" 
												/>
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
											
						</fieldset>		
						<input type="hidden" id="codigo" name="codigo" value="000000000000"/>
						<input type="hidden" id="codigoTipoElemento" name="codigoTipoElemento" value="001"/>
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
												htmlEscape="true" />
										</td>

									</tr>
									<tr>
										<td class="texto_ti"><input type="text" id="codigoEmpresa"
											name="codigoEmpresa" maxlength="4" style="width: 50px;"
											value="<c:out value="${elementoModificar.codigoEmpresa}" default="" />"
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
													value="${elementoModificar.clienteEmp.empresa.descripcion}"
													default="" /> </label></td>
										<td class="texto_ti"><input type="text" id="codigoSucursal"
											name="codigoSucursal" maxlength="4" style="width: 50px;"
											value="<c:out value="${elementoModificar.codigoSucursal}" default="" />"
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
													value="${elementoModificar.posicion.estante.grupo.seccion.deposito.sucursal.descripcion}"
													default="" /> </label></td>
										<td class="texto_ti"><input type="text" id="codigoDeposito"
											name="codigoDeposito" maxlength="2" style="width: 50px;"
											value="<c:out value="${elementoModificar.codigoDeposito}" default="" />"
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
													value="${elementoModificar.posicion.estante.grupo.seccion.deposito.descripcion}"
													default="" /> </label>
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
										
																				
									</tr>
									<tr>
										<td class="texto_ti"><input type="text" id="codigoCliente"
											name="codigoCliente" maxlength="6" style="width: 50px;"
											value="<c:out value="${elementoModificar.codigoCliente}" default="" />"
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
													value="${elementoModificar.clienteEmp.nombreYApellido}"
													default="" /> </label>
										</td>
										<td class="texto_ti"><input type="text" id="codigoContenedor"
											name="codigoContenedor" maxlength="12" style="width: 90px;"
											value="<c:out value="${elementoModificar.codigoElemento}" default="" />"
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
													value="${elementoModificar.tipoElemento.descripcion}"
													default="" /> </label>
										</td>
										<td class="texto_ti">
											<select id="estado"
												name="estado" size="1">
													<option label="Seleccione un Estado" value="">Seleccione un Estado</option>
													<option label="Creado" value="Creado" <c:if test="${elementoModificar.estado == 'Creado'}">
															selected="selected"
														</c:if>>Creado</option>
													<option label="En el Cliente" value="En el Cliente" <c:if test="${elementoModificar.estado == 'En el Cliente'}">
															selected="selected"
														</c:if>>En el Cliente</option>			
													<option label="En Guarda" value="En Guarda" <c:if test="${elementoModificar.estado == 'En Guarda'}">
															selected="selected"
														</c:if>>En Guarda</option>
													<option label="En Transito" value="En Transito" <c:if test="${elementoModificar.estado == 'En Transito'}">
															selected="selected"
														</c:if>>En Transito</option>
													<option label="En Consulta" value="En Consulta" <c:if test="${elementoModificar.estado == 'En Consulta'}">
															selected="selected"
														</c:if>>En Consulta</option>
													<option label="No Existe" value="No Existe" <c:if test="${elementoModificar.estado == 'No Existe'}">
															selected="selected"
														</c:if>>No Existe</option>
													<option label="Unificado" value="Unificado" <c:if test="${elementoModificar.estado == 'Unificado'}">
															selected="selected"
														</c:if>>Unificado</option>
											</select>
										</td>
																																					
									</tr>
								<sec:authorize ifAnyGranted="ROLE_ELEM_HABILITAR_CERRAR">
									
										<tr>
											<td colspan="3">
												<table>
													<tr>
														<td class="texto_ti"><input type="checkBox"
															id="habilitadoCerrar" name="habilitadoCerrar"
															style="width: 10px;" value="true"
															/>
														</td>
														<td class="texto_ti"><spring:message
																code="formularioElemento.datosElemento.habilitarCerrar"
																htmlEscape="true" /></td>
																
														<td class="texto_ti"><input type="checkBox"
															id="cerrado" name="cerrado" style="width: 10px;"
															value="true"
															 />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioElemento.datosElemento.cajaCerrada"
																htmlEscape="true" /></td>
													</tr>
												</table>
											</td>
										</tr>	
								</sec:authorize>
								<sec:authorize ifNotGranted="ROLE_ELEM_HABILITAR_CERRAR">
										<tr>
											<td colspan="3">
												<table>
													<tr>
														<td class="texto_ti"><input type="checkBox"
															id="cerrado" name="cerrado" style="width: 10px;"
															value="true" />
														</td>
														<td class="texto_ti"><spring:message
																code="formularioElemento.datosElemento.cajaCerrada"
																htmlEscape="true" /></td>
													</tr>
												</table>
											</td>
										</tr>
								</sec:authorize>
									<tr>
										<td class="texto_ti"><spring:message
												code="formularioElemento.datosElemento.tipoTrabajo"
												htmlEscape="true" /></td>
									</tr>
									<tr>
										<td class="texto_ti"><select id="tipoTrabajo" name="tipoTrabajo"														size="1"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>>
															<option label="Seleccione un Tipo de Trabajo" value="">Seleccione un Tipo de Trabajo</option>
															<option label="No especificado" value="No especificado"
																<c:if test="${elementoModificar.tipoTrabajo == null}">
																				selected="selected"
																			</c:if>>No especificado</option>
															<option label="Digitalización" value="Digitalización"
																<c:if test="${elementoModificar.tipoTrabajo == 'Digitalización'}">
																				selected="selected"
																			</c:if>>Digitalización</option>
															<option label="Referencia en planta" value="Referencia en planta"
																<c:if test="${elementoModificar.tipoTrabajo == 'Referencia en planta'}">
																				selected="selected"
																			</c:if>>Referencia en planta</option>
														    <option label="Referencia enviada por correo" value="Referencia enviada por correo"
																<c:if test="${elementoModificar.tipoTrabajo == 'Referencia enviada por correo'}">
																				selected="selected"
																			</c:if>>Referencia en planta</option>
															<option label="Referencia adm. por terceros" value="Referencia adm. por terceros"
																<c:if test="${elementoModificar.tipoTrabajo == 'Referencia adm. por terceros'}">
																				selected="selected"
																			</c:if>>Referencia adm. por terceros</option>
															<option label="Referenciado por terceros" value="Referenciado por terceros"
																<c:if test="${elementoModificar.tipoTrabajo == 'Referenciado por terceros'}">
																				selected="selected"
																			</c:if>>Referenciado por terceros</option>
															<option label="Legajo Carga" value="Legajo Carga"
																<c:if test="${elementoModificar.tipoTrabajo == 'Legajo Carga'}">
																				selected="selected"
																			</c:if>>Referenciado por terceros</option>
															<option label="Sin referencia" value="Sin referencia"
																<c:if test="${elementoModificar.tipoTrabajo == 'Sin referencia'}">
																				selected="selected"
																			</c:if>>Sin referencia</option>
													</select></td>
									</tr>
								<tr>
								
								<td colspan="3">
								<br></br>
								<div align="center">

											  <input type="hidden" id="hdn_guardar" name="hdn_guardar" value="false" />
											  
											  <button id="guardarYcontinuar" type="button" >
										       	<img src="<%=request.getContextPath()%>/images/guardar.png">
										       	<spring:message code="botones.guardarContinuar" htmlEscape="true"/>
										      </button>
										      
										      &nbsp;	
										      <button id="guardar" type="button" title="" class="botonCentrado">
										       	<img src="<%=request.getContextPath()%>/images/guardar.png">
										       	<spring:message code="botones.guardar" htmlEscape="true"/>
										      </button>
										      
										      &nbsp;
										      <button id="cancelar" type="button" value="off" class="botonCentrado">
										       <img src="<%=request.getContextPath()%>/images/cancelar.png">
										       <spring:message code="botones.cancelar" htmlEscape="true"/>
										      </button>
										</div>
								</td>
								</tr>
							</table>
						</div>
					</fieldset>
					
							</form:form>
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