<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><c:if test="${accion == 'NUEVO'}">
		<spring:message code="formularioCliente.titulo.registar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'MODIFICACION'}">
		<spring:message code="formularioCliente.titulo.modificar"
			htmlEscape="true" />
	</c:if> <c:if test="${accion == 'CONSULTA'}">
		<spring:message code="formularioCliente.titulo.consultar"
			htmlEscape="true" />
	</c:if>
</title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
<script type="text/javascript" language="JavaScript"
	src="js/mavalos_jquery.tools.min.js"></script>
<script type="text/javascript" src="js/mavalos_formulario_cliente.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/doblelistas_comboDefault.js"></script>
<script type="text/javascript" src="js/Utils.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.chromatable.js"></script>
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});seleccionarCombos();">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <c:if
							test="${accion == 'NUEVO'}">
							<spring:message code="formularioCliente.titulo.registar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioCliente.titulo.modificar"
								htmlEscape="true" />
						</c:if> <c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioCliente.titulo.consultar"
								htmlEscape="true" />
						</c:if> </font>
				</legend>
				<br />
				<form:form action="guardarActualizarCliente.html"
					commandName="clienteFormulario" method="post"
					modelAttribute="clienteFormulario">
					<input type="hidden" id="accion" name="accion"
						value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="id" name="id"
						value="<c:out value="${clienteFormulario.id}" default="" />" />
					<input type="hidden" id="listasSeleccionadas" name="listasSeleccionadas"/>
					<input type="hidden" id="listaDefecto" name="listaDefecto"/>
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="formularioCliente.datosCliente" htmlEscape="true" />
									</font> <img id="busquedaImgSrcDown" src="images/skip_down.png"
										title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
										style="DISPLAY: none"
										title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="width: 100%;" id="busquedaDiv" align="center">
							<table style="width: 100%;">
								<tr>
									<td></td>
									<td class="texto_ti"><spring:message
											code="formularioCliente.datosCliente.empresa"
											htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioCliente.datosCliente.codigo"
											htmlEscape="true" /></td>
								</tr>
								<tr>
									<td>
										<table>
											<tr>
												<td class="texto_ti"><spring:message
														code="formularioCliente.datosCliente.habilitado"
														htmlEscape="true" />
												</td>
												<td class="texto_ti"><input type="checkBox"
													id="habilitado" name="habilitado" style="width: 10px;"
													value="true"
													<c:if test="${clienteFormulario.habilitado == 'true'}">
														checked="CHECKED"
													</c:if>
													<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if> />
												</td>
												
											</tr>
										</table></td>
										<td class="texto_ti">
											<input type="text" id="codigoEmpresa"
												name="codigoEmpresa" maxlength="6" style="width: 50px;"
												value="<c:out value="${clienteFormulario.codigoEmpresa}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
												&nbsp;&nbsp;
											<button type="button"
												onclick="abrirPopup('empresasPopupMap');"
												title="<spring:message code="textos.buscar" htmlEscape="true"/>"
												<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
												<img src="<%=request.getContextPath()%>/images/buscar.png">
											</button>&nbsp;&nbsp; 
											<label id="codigoEmpresaLabel"
												for="codigoEmpresa"> <c:out
														value="${clienteFormulario.empresa.descripcion}"
														default="" /> 
											</label>	
										</td>
										<td class="texto_ti"><input type="text" id="codigo" class="requerido"
											name="codigo" style="width: 200px;" maxlength="6"
											value="<c:out value="${clienteFormulario.codigo}" default="" />"
											<c:if test="${accion != 'NUEVO'}">
												readonly="readonly"
											</c:if> />
										</td>
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message code="formularioCliente.datosCliente.persona"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.tipoPersona"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.razonSocial"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.nombre"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.apellido"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti">
														<select id="tipoPersona" name="tipoPersona" size="1" class="requerido"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
														
															<option value="Juridica" 
																<c:if test="${clienteFormulario.tipoPersona == 'Juridica'}">
																	selected = "selected"
																</c:if>
															>
																<spring:message code="formularioCliente.datosCliente.tipoPersona.juridica" htmlEscape="true"/>
															</option>
														
														
															<option value="Fisica"
																<c:if test="${clienteFormulario.tipoPersona == 'Fisica'}">
																	selected = "selected"
																</c:if>
															>
																<spring:message code="formularioCliente.datosCliente.tipoPersona.fisica" htmlEscape="true"/>
															</option>
														</select>		
													</td>
													<td class="texto_ti"><input type="text" class="requerido"
														id="razonSocial" maxlength="200"
														name="razonSocial.razonSocial" style="width: 190px;"
														value="<c:out value="${clienteFormulario.razonSocial.razonSocial}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="nombre" class="requerido"
														name="nombre" style="width: 200px;" maxlength="60"
														value="<c:out value="${clienteFormulario.nombre}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
															readonly="readonly"
														</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="apellido" class="requerido"
														name="apellido" style="width: 200px;" maxlength="60"
														value="<c:out value="${clienteFormulario.apellido}" default="" />"
														<c:if test="${accion != 'NUEVO'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.tipoDocumento"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.numeroDocumento"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.iva"
															htmlEscape="true" />
													</td>
													<td></td>
												</tr>
												<tr>
													
													<td class="texto_ti"><select id="idTipoDocSel" class="requerido"
														name="idTipoDocSel" size="1" style="width: 190px;"
														<c:if test="${accion == 'CONSULTA'}">
															disabled="disabled"
														</c:if>>
															<c:forEach items="${tiposDocumento}" var="tipo">
																<option value="${tipo.id}"
																	<c:if test="${tipo.id == tipoDocumentoDefecto.id}">
																	selected="selected"
																</c:if>>
																	<c:out value="${tipo.nombre}" />
																</option>
															</c:forEach>
													</select>
													</td>
													<td class="texto_ti"><input type="text" id="numeroDoc" class="requerido"
														name="numeroDoc" style="width: 190px;" maxlength="13"
														value="<c:out value="${clienteFormulario.numeroDoc}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti">
														<select id="idAfipCondIva" class="requerido"
															name="idAfipCondIva" size="1" style="width: 190px;"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
																<c:forEach items="${afipCondIvas}" var="iva">
																	<option value="${iva.id}"
																		<c:if test="${iva.id == clienteFormulario.afipCondIva.id}">
																		selected="selected"
																	</c:if>>
																		<c:out value="${iva.descripcion}" />
																	</option>
																</c:forEach>
														</select>
													</td>
													<td></td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioCliente.datosCliente.contacto"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.telefono"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.interno"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.fax"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.mail"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" id="telefono"
														maxlength="20" name="telefono" style="width: 190px;"
														value="<c:out value="${clienteFormulario.telefono}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="interno"
														maxlength="20" name="interno" style="width: 190px;"
														value="<c:out value="${clienteFormulario.interno}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="fax"
														maxlength="20" name="fax" style="width: 190px;"
														value="<c:out value="${clienteFormulario.fax}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
													<td class="texto_ti"><input type="text" id="email"
														maxlength="60" name="email" style="width: 190px;"
														value="<c:out value="${clienteFormulario.email}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																	readonly="readonly"
																</c:if> />
													</td>
												</tr>
												
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<fieldset>
											<legend>
												<spring:message
													code="formularioCliente.datosCliente.direccion"
													htmlEscape="true" />
											</legend>
											<table>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.calle"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.numero"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.piso"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.dpto"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><input type="text" class="requerido"
														id="direccion.calle" name="direccion.calle" maxlength="30"
														style="width: 190px;"
														value="<c:out value="${clienteFormulario.direccion.calle}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>

													<td class="texto_ti"><input type="text" class="requerido"
														id="direccion.numero" name="direccion.numero"
														maxlength="6" style="width: 190px;"
														value="<c:out value="${clienteFormulario.direccion.numero}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="direccion.piso" name="direccion.piso" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${clienteFormulario.direccion.piso}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
													<td class="texto_ti"><input type="text"
														id="direccion.dpto" name="direccion.dpto" maxlength="4"
														style="width: 190px;"
														value="<c:out value="${clienteFormulario.direccion.dpto}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if> />
													</td>
												</tr>
												<tr>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.pais"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.provincia"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.localidad"
															htmlEscape="true" />
													</td>
													<td class="texto_ti"><spring:message
															code="formularioCliente.datosCliente.direccion.barrio"
															htmlEscape="true" />
													</td>
												</tr>
												<tr>
													<td class="texto_ti">													
														<input type="text" id="pais" name="pais" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${paisDefecto.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopup('paisPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="paisLabel" for="pais">
																<c:out value="${paisDefecto.id}" default="" />
															</label>
														</div>																					
													</td>
													<td class="texto_ti">
														<input type="text" id="provincia" name="provincia" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${clienteFormulario.direccion.barrio.localidad.provincia.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopupProvincia('provinciaPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="provinciaLabel" for="provincia">
																<c:out value="${clienteFormulario.direccion.barrio.localidad.provincia.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input type="text" id="localidad" name="localidad" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${clienteFormulario.direccion.barrio.localidad.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopupLocalidad('localidadPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="localidadLabel" for="localidad">
																<c:out value="${clienteFormulario.direccion.barrio.localidad.id}" default="" />
															</label>
														</div>
													</td>
													<td class="texto_ti">
														<input id="idBarrio" name="idBarrio" type="hidden" 
															value="<c:out value="${clienteFormulario.direccion.barrio.id}" default="" />"/>
														<input type="text" id="barrio" name="barrio" maxlength="30" style="width: 150px;" class="requerido"
															value="<c:out value="${clienteFormulario.direccion.barrio.nombre}" default="" />" 
															<c:if test="${accion == 'CONSULTA'}">
																readonly="readonly"
															</c:if>
														/>
														<button type="button" onclick="abrirPopupBarrio('barrioPopupMap');"
															title="<spring:message code="textos.buscar" htmlEscape="true"/>"
															<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>
														>
															<img src="<%=request.getContextPath()%>/images/buscar.png" > 
														</button>
														<div style="display: none;">
															<label id="barrioLabel" for="barrio">
																<c:out value="${clienteFormulario.direccion.barrio.id}" default="" />
															</label>
														</div>
													</td>
												
													
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<br/>

																					
									<fieldset>
										<table width="100%">
								          <thead>
								            <tr>
								              <th align="left" id="grupoImg">								 
										        	<font style="color:#003090">
										        		<spring:message code="formularioCliente.asignacionListas" htmlEscape="true"/> 
										        	</font>
										        	<img id="grupoImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										        	<img id="grupoImgSrc" style="DISPLAY: none" src="images/skip.png" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
											 	  
								              </th>
											 </tr>
											</thead>
										</table>
										<div id="grupoDiv" align="center">							
											<table>
												<tr>
													<td class="texto_ti">
														<spring:message code="formularioCliente.listasDisponibles" htmlEscape="true"/>
													</td>
													<td>
														&nbsp;
													</td>
													<td class="texto_ti">
														<spring:message code="formularioCliente.listasAsignadas" htmlEscape="true"/>
													</td>
												</tr>
												<tr>
											      <td width="40%" align="center">
											        <select id="listasSeleccionadasIzq" name="listasSeleccionadasIzq" size="8" style="width:250px" multiple="multiple">
											        	<c:forEach items="${listasIzq}" var="listaIzq">
															<option value="<c:out value="${listaIzq.id}"/>">
																<c:out value="${listaIzq.descripcion}"/>
															</option>
														</c:forEach>
											        </select>
											      </td>
											      <td width="20%" align="center">											          
											          <c:if test="${accion != 'CONSULTA'}">
												          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('listasSeleccionadas')" 
												        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
												          	>
												          <br />
												          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('listasSeleccionadas')" 
												        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
												          >
												          <br />
										        	  </c:if>
											      </td>
											      <td width="40%" align="center">
											        <select id="listasSeleccionadasDer" name="listasSeleccionadasDer" size="8" style="width:250px" multiple="multiple">
											            <c:forEach items="${listasDer}" var="listaDer">
															<option value="<c:out value="${listaDer.id}"/>">
																<c:out value="${listaDer.descripcion}"/>
															</option>
														</c:forEach>
											        </select>											        
											      </td>
											      
											    </tr>											    
											    <tr>
													<td class="texto_ti">
														<spring:message code="formularioCliente.listaDefecto" htmlEscape="true"/>
													</td>
													<td>
														&nbsp;
													</td>
												</tr>
											    <tr>
											    <td width="40%" align="center">
											    	<select id="listasSeleccionadasDef" style="width:250px" name="listaDer" >
											    	 <c:forEach items="${listasDer}" var="listaDer">
															<option value="<c:out value="${listaDer.id}"/>">
																<c:out value="${listaDer.descripcion}"/>
															</option>
														</c:forEach>
											        </select>
											     </td>
											    </tr>
										    </table>
									    </div>
								    </fieldset>
								    </td>
							    </tr>
							    <tr>
									<td colspan="2">
										<fieldset>
											<legend>
												<spring:message
													code="formularioCliente.datosCliente.facturacion"
													htmlEscape="true" />
											</legend>
											<table style="width: 100%;">
												<tr>
													<td class="texto_ti" style="width: 30%;"><spring:message
															code="formularioCliente.datosCliente.mesesFacturacion" htmlEscape="true" /></td>
													<td class="texto_ti" style="width: 70%;"><spring:message
															code="formularioCliente.datosCliente.notasFacturacion" htmlEscape="true" /></td>
												</tr>
												
												<tr>
													<td style="width: 30%;">
														<display:table id="mes" name="meses" >	
															<display:column class="celdaAlineadoCentrado">								
																<input type="checkbox" name="mesesFacturables" value="${mes.mes}" 
																	<c:if test="${mes.seleccionado == true}">
																		checked
																	</c:if>
																	<c:if test="${accion == 'CONSULTA'}">
							       										disabled="disabled"
							       									</c:if>
																/>
															</display:column>
															<display:column titleKey="formularioCliente.datosCliente.mes"  class="celdaAlineadoIzquierda">								
																<c:if test="${mes.mes == 1}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.enero"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 2}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.febrero"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 3}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.marzo"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 4}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.abril"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 5}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.mayo"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 6}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.junio"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 7}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.julio"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 8}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.agosto"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 9}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.septiembre"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 10}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.octubre"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 11}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.noviembre"
																		htmlEscape="true" />
																</c:if>
																<c:if test="${mes.mes == 12}">
																	<spring:message
																		code="formularioCliente.datosCliente.mes.diciembre"
																		htmlEscape="true" />
																</c:if>
															</display:column>
														</display:table>
													</td>
													<td style="width: 70%;">
														<textarea id="notasFacturacion" name="notasFacturacion"  rows="10" cols="40" 
							       							<c:if test="${accion == 'CONSULTA'}">
							       								readonly="readonly"
							       							</c:if>
							       						><c:out value="${clienteFormulario.notasFacturacion}"/></textarea>	
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								
									<td>
										<fieldset>
											<legend>
												<spring:message
													code="formularioCliente.datosCliente.adicionales"
													htmlEscape="true" />
											</legend>
											<table style="width: 100%;">
												<tr style="width: 95%;">
													<td class="texto_ti" style="width: 100%;"><spring:message
															code="formularioCliente.datosCliente.observaciones" htmlEscape="true" /></td>
												</tr>
												<tr>
													
													<td style="width: 95%;">
														<textarea id="observaciones" name="observaciones"  rows="10" cols="40" 
							       							<c:if test="${accion == 'CONSULTA'}">
							       								readonly="readonly"
							       							</c:if>
							       						><c:out value="${clienteFormulario.observaciones}"/></textarea>	
													</td>
												</tr>
											</table>
										</fieldset>
									</td>
								</tr>
								<c:if test="${accion != 'CONSULTA'}">
									<tr>
										<td class="texto_ti">
											<button name="restablecer" type="reset">
												<img
													src="<%=request.getContextPath()%>/images/restablecer.png"
													title=<spring:message code="botones.restablecer" htmlEscape="true"/>>
											</button>
										</td>
										
									</tr>
								</c:if>
							</table>
						</div>
					</fieldset>
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
					<button name="guardar" type="button" onclick="guardarYSalir();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
					<button name="cancelar" type="button" onclick="volverCancelar();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accion == 'CONSULTA'}">
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight"
		style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>	
	<jsp:include page="fieldAvisos.jsp"/>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="paisPopupMap" /> 
		<jsp:param name="clase" value="paisPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="provinciaPopupMap" /> 
		<jsp:param name="clase" value="provinciaPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="localidadPopupMap" /> 
		<jsp:param name="clase" value="localidadPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="barrioPopupMap" /> 
		<jsp:param name="clase" value="barrioPopupMap" /> 
	</jsp:include>
	<jsp:include page="popupBusqueda.jsp">
		<jsp:param name="mapa" value="empresasPopupMap" /> 
		<jsp:param name="clase" value="empresasPopupMap" /> 
	</jsp:include>
</body>
</html>