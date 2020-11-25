<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cstmTag" tagdir="/WEB-INF/tags" %>
<%@ page buffer = "32kb" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioClasificacionDocumental.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/simpletreemenu.js"></script>
		<script type="text/javascript" src="js/clasificacion_documental.js"></script>
		<script type="text/javascript" src="js/doblelistas.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<input type="hidden" name="auxScrollX" id="auxScrollX" value="${arbolScrollX}" />
		<input type="hidden" name="auxScrollY" id="auxScrollY" value="${arbolScrollY}" />
		<form id="cargar" name="seleccion" action="mostrarClasificacionDocumental.html" method="post">
			<input type="hidden" id="id_cliente_emp" name="id_cliente_emp" value="${cliente.id}"/>
			<input type="hidden" id="clienteAspId" name="clienteAspId" value="${clienteAsp.id}"/>
			<input type="hidden" id="id_seleccionado" name="id_seleccionado"/>
			<input type="hidden" id="accion" name="accion"/>
			<input type="hidden" name="cArbolScrollX" id="cArbolScrollX" value="${arbolScrollX}" />
			<input type="hidden" name="cArbolScrollY" id="cArbolScrollY" value="${arbolScrollY}" />
		</form>
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li>
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		      <li id="crearSubCarpeta"><img src="images/open.gif" /> <font size="2"><spring:message code="botones.crearSubCarpeta" htmlEscape="true"/></font></li>
		      <li id="crearIndice"><img src="images/list.gif" /> <font size="2"><spring:message code="botones.crearIndice" htmlEscape="true"/></font></li>
		      <sec:authorize ifAllGranted="ROLE_ABM_CD_PERSONAL">
		      	<li id="personal"><img src="images/personal.png" /> <font size="2"><spring:message code="botones.personal" htmlEscape="true"/></font></li>
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
		        			<spring:message code="formularioClasificacionDocumental.titulo" htmlEscape="true"/>		        		
	        			</font>	
	        		</legend>	  
					<fieldset >
						<legend>
							<font size="1">
								<spring:message code="formularioClasificacionDocumental.layer" htmlEscape="true"/>
								<c:out value="${cliente.razonSocial.razonSocial}"/>
							</font>
			        	</legend>
						<table>
							<tr>
								<td style="vertical-align: top;">
									<fieldset>
										<legend>
											<font size="1">
												<spring:message code="formularioClasificacionDocumental.layerArbol" htmlEscape="true"/>
											</font>
										</legend>
										<spring:message code="formularioClasificacionDocumental.busquedaClasificacion" htmlEscape="true"/> 
										<input type="text" id="bsqArbol" 
											value="${clasificacionSeleccionada.nombre}"
											title="<spring:message code="formularioClasificacionDocumental.busquedaClasificacionTooltip" htmlEscape="true"/>"
										/>
										<div style="overflow: auto;"  id="tdArbol">
											<ul id="treemenu" class="treeview">
												<cstmTag:clasificacionDocumentalNodo listaClasificacionDocumental="${clasificacionesDocumentales}"/>
									 		</ul>
								 		</div>
							 		</fieldset>
							 		<button name="imprimir" id="imprimir" type="button">
										<img src="<%=request.getContextPath()%>/images/impresora.gif">
										<spring:message code="botones.imprimir" htmlEscape="true" />
									</button>
							 		<button name="agregar" type="button" onclick="ejecutar('crearCarpeta',0);" class="botonCentrado" title="<spring:message code="botones.agregarNodo" htmlEscape="true"/>">
										<img src="<%=request.getContextPath()%>/images/add.png" > 
										<spring:message code="botones.agregar" htmlEscape="true"/>  
									</button>
								</td>
								<td style="vertical-align: top;">
									<c:if test="${clasificacionSeleccionada!=null}">
										<form id="clasifDocForm" name="clasificacionDocumentoFormulario" action="guardarClasificacionDocumental.html" method="post">
											<input type="hidden" id="empleadosSeleccionados" name="empleadosSeleccionados"/>
											<input type="hidden" id="accion" name="accion" value="${accion}"/>
											<input type="hidden" name="id" value="${clasificacionSeleccionada.id}"/>
											<input type="hidden" name="id_cliente_emp" value="${cliente.id}"/>
											<input type="hidden" name="clienteAspId" id="clienteAspId" value="${clienteAsp.id}" />
											<input type="hidden" name="nodo" value="${clasificacionSeleccionada.nodo}"/>
											<input type="hidden" name="gArbolScrollX" id="gArbolScrollX" value="${arbolScrollX}" />
											<input type="hidden" name="gArbolScrollY" id="gArbolScrollY" value="${arbolScrollY}" />
											<c:if test="${clasificacionSeleccionada.padre!=null}">
												<input type="hidden" name="id_padre" value="${clasificacionSeleccionada.padre.id}"/>
											</c:if>
											<c:if test="${clasificacionSeleccionada.padre==null}">
												<input type="hidden" name="id_padre" value="0"/>
											</c:if>
											
											<fieldset>
												<legend>
													<font size="1">
														<c:if test="${clasificacionSeleccionada.nodo=='N'}">
															<spring:message code="formularioClasificacionDocumental.nodo" htmlEscape="true"/>
														</c:if>
														<c:if test="${clasificacionSeleccionada.nodo=='I'}">
															<spring:message code="formularioClasificacionDocumental.indice" htmlEscape="true"/>
														</c:if>
													</font>
									        	</legend>
								        		<table style="width: 100%;">		
													<c:if test="${clasificacionSeleccionada.padre!=null}">						
													<tr>
														<td class="texto_ti">
															<table>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.carpetaPadre" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<c:out value="${clasificacionSeleccionada.padre.nombre}"/>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</c:if>
													<tr>
														<td class="texto_ti">
															<table>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.nombre" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.codigo" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="text" name="nombre" id="nombre"
																			class="requerido" style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.nombre}"/>"
																			<c:if test="${(accion == 'consultar' && clasificacionSeleccionada.padre.id != 0) || accion == 'personal'}">
																				readonly="readonly"
																			</c:if>
																		/>
																	</td>
																	<td class="texto_ti">
																		<input id="codigo" type="text" maxlength="10" name="codigo" 
																			class="requerido" style="width: 50px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.codigo}"/>"

			
																		/>
																		<c:if test="${accion != 'consultar' && accion != 'personal' && (clasificacionSeleccionada.id == null || clasificacionSeleccionada.id < 1 )}">
																			<button id="btn_actualizarCodigo" type="button"  name="btn_actualizarCodigo" >
																				<spring:message code="formularioClasificacionDocumental.botonActualizar" htmlEscape="true"/>
																			</button>
																		</c:if>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td class="texto_ti">
															<table>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.descripcion" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<textarea name="descripcion" cols="40" rows="4"
																			onblur="formato(4);"
																			<c:if test="${accion == 'consultar' || accion == 'personal'}">
																				readonly="readonly"
																			</c:if>
																			><c:out value="${clasificacionSeleccionada.descripcion}"/></textarea>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
												</table>
												<%-- -----------INICIO PERSONAL------------- --%>
												<c:if test="${accion == 'personal'}" >
													<table style="width: 100%;">
														<tr>
															<td>
																<div align="center">							
																	<table>
																		<tr>
																			<td class="texto_ti">
																				<spring:message code="formularioClasificacionDocumental.personalDisponible" htmlEscape="true"/>
																			</td>
																			<td>
																				&nbsp;
																			</td>
																			<td class="texto_ti">
																				<spring:message code="formularioClasificacionDocumental.personalAsignado" htmlEscape="true"/>
																			</td>
																		</tr>
																		<tr>
																	      <td width="40%" align="center">
																	        <select id="empleadosSeleccionadosIzq" name="empleadosSeleccionadosIzq" size="8" style="width:250px" multiple="multiple">
																	        	<c:forEach items="${empleadosDisponibles}" var="empleadoIzq">
																					<option value="<c:out value="${empleadoIzq.id}"/>">
																						<c:out value="${empleadoIzq.apellidoYNombre}"/>
																					</option>
																				</c:forEach>
																	        </select>
																	      </td>
																	      <td width="20%" align="center">											          
																	          <c:if test="${accion != 'CONSULTA'}">
																		          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('empleadosSeleccionados')" 
																		        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
																		          	>
																		          <br />
																		          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('empleadosSeleccionados')" 
																		        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
																		          >
																		          <br />
																        	  </c:if>
																	      </td>
																	      <td width="40%" align="center">
																	        <select id="empleadosSeleccionadosDer" name="empleadosSeleccionadosDer" size="8" style="width:250px" multiple="multiple">
																	            <c:forEach items="${empleadosSeleccionados}" var="empleadoDer">
																					<option value="<c:out value="${empleadoDer.id}"/>">
																						<c:out value="${empleadoDer.apellidoYNombre}"/>
																					</option>
																				</c:forEach>
																	        </select>
																	      </td>
																	    </tr>
																    </table>
															    </div>
															    
															</td>								
														</tr>
													</table>
												</c:if> 
												<%-- -----------FIN PERSONAL------------- --%>
												<c:if test="${clasificacionSeleccionada.nodo=='I'}">
													<div id="tab_header">
														<ul>
															<li id="tab_header_ind"	class="selected">
																<spring:message code="formularioClasificacionDocumental.indiceInd" htmlEscape="true"/>
															</li>
															<li id="tab_header_grp">
																<spring:message code="formularioClasificacionDocumental.indiceGrp" htmlEscape="true"/>
															</li>
														</ul>
													</div>
													<div id="tab_content">
													
														<div id="tab_content_ind" class="selected">
															<table style="width: 100%;">		
																<tr>
																	<td class="texto_ti" colspan="2">
																		<input type="checkBox" id="indiceIndividual" style="width: 10px;"
																			name="indiceIndividual" value="true"
																			<c:if test="${clasificacionSeleccionada.indiceIndividual}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="indiceIndividual" value="${clasificacionSeleccionada.indiceIndividual}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.indiceInd" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualFecha1Seleccionado" style="width: 10px;"
																			name="individualFecha1Seleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.individualFecha1Seleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="individualFecha1Seleccionado" value="${clasificacionSeleccionada.individualFecha1Seleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaFecha1" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualFecha2Seleccionado" style="width: 10px;"
																			name="individualFecha2Seleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.individualFecha2Seleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="individualFecha2Seleccionado" value="${clasificacionSeleccionada.individualFecha2Seleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaFecha2" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualFecha1Requerido" style="width: 10px;"
																			name="individualFecha1Requerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.individualFecha1Requerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.individualFecha1Requerido" value="${clasificacionSeleccionada.individualFecha1Requerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualFecha2Requerido" style="width: 10px;"
																			name="individualFecha2Requerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.individualFecha2Requerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.individualFecha2Requerido" value="${clasificacionSeleccionada.individualFecha2Requerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.fecha1Titulo" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.fecha2Titulo" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="text" id="individualFecha1Titulo" name="individualFecha1Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.individualFecha1Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.individualFecha1Seleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.individualFecha1Seleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																	<td class="texto_ti">
																		<input type="text" id="individualFecha2Titulo" name="individualFecha2Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.individualFecha2Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.individualFecha2Seleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.individualFecha2Seleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualNumero1Seleccionado" style="width: 10px;"
																			name="individualNumero1Seleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.individualNumero1Seleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																		<input type="hidden" name="individualNumero1Seleccionado" value="${clasificacionSeleccionada.individualNumero1Seleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaNumero1" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualNumero2Seleccionado" style="width: 10px;"
																			name="individualNumero2Seleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.individualNumero2Seleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="individualNumero2Seleccionado" value="${clasificacionSeleccionada.individualNumero2Seleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaNumero2" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualNumero1Requerido" style="width: 10px;"
																			name="individualNumero1Requerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.individualNumero1Requerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.individualNumero1Requerido" value="${clasificacionSeleccionada.individualNumero1Requerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualNumero2Requerido" style="width: 10px;"
																			name="individualNumero2Requerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.individualNumero2Requerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.individualNumero2Requerido" value="${clasificacionSeleccionada.individualNumero2Requerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.numero1Titulo" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.numero2Titulo" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="text" id="individualNumero1Titulo" name="individualNumero1Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.individualNumero1Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.individualNumero1Seleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.individualNumero1Seleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																	<td class="texto_ti">
																		<input type="text" id="individualNumero2Titulo" name="individualNumero2Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.individualNumero2Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.individualNumero2Seleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.individualNumero2Seleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualTexto1Seleccionado" style="width: 10px;"
																			name="individualTexto1Seleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.individualTexto1Seleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="individualTexto1Seleccionado" value="${clasificacionSeleccionada.individualTexto1Seleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaTexto1" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualTexto2Seleccionado" style="width: 10px;"
																			name="individualTexto2Seleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.individualTexto2Seleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="individualTexto2Seleccionado" value="${clasificacionSeleccionada.individualTexto2Seleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaTexto2" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualTexto1Requerido" style="width: 10px;"
																			name="individualTexto1Requerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.individualTexto1Requerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.individualTexto1Requerido" value="${clasificacionSeleccionada.individualTexto1Requerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="individualTexto2Requerido" style="width: 10px;"
																			name="individualTexto2Requerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.individualTexto2Requerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.individualTexto2Requerido" value="${clasificacionSeleccionada.individualTexto2Requerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.texto1Titulo" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.texto2Titulo" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="text" id="individualTexto1Titulo" name="individualTexto1Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.individualTexto1Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.individualTexto1Seleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.individualTexto1Seleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																	<td class="texto_ti">
																		<input type="text" id="individualTexto2Titulo" name="individualTexto2Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.individualTexto2Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.individualTexto2Seleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.individualTexto2Seleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																</tr>
															</table>
														</div>
														
														<div id="tab_content_grp">
															<table style="width: 100%;">		
																<tr>
																	<td class="texto_ti" colspan="2">
																		<input type="checkBox" id="indiceGrupal" style="width: 10px;"
																			name="indiceGrupal" value="true"
																			<c:if test="${clasificacionSeleccionada.indiceGrupal}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="indiceGrupal" value="${clasificacionSeleccionada.indiceGrupal}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.indiceGrp" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="grupalFechaSeleccionado" style="width: 10px;"
																			name="grupalFechaSeleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.grupalFechaSeleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																		<input type="hidden" name="grupalFechaSeleccionado" value="${clasificacionSeleccionada.grupalFechaSeleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaFecha" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="grupalFechaRequerido" style="width: 10px;"
																			name="grupalFechaRequerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.grupalFechaRequerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.grupalFechaRequerido" value="${clasificacionSeleccionada.grupalFechaRequerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.fecha1Titulo" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.fecha2Titulo" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="text" id="grupalFecha1Titulo" name="grupalFecha1Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.grupalFecha1Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.grupalFechaSeleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.grupalFechaSeleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																	<td class="texto_ti">
																		<input type="text" id="grupalFecha2Titulo" name="grupalFecha2Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.grupalFecha2Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.grupalFechaSeleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.grupalFechaSeleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="grupalNumeroSeleccionado" style="width: 10px;"
																			name="grupalNumeroSeleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.grupalNumeroSeleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="grupalNumeroSeleccionado" value="${clasificacionSeleccionada.grupalNumeroSeleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaNumero" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="grupalNumeroRequerido" style="width: 10px;"
																			name="grupalNumeroRequerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.grupalNumeroRequerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.grupalNumeroRequerido" value="${clasificacionSeleccionada.grupalNumeroRequerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.numero1Titulo" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.numero2Titulo" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="text" id="grupalNumero1Titulo" name="grupalNumero1Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.grupalNumero1Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.grupalNumeroSeleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.grupalNumeroSeleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																	<td class="texto_ti">
																		<input type="text" id="grupalNumero2Titulo" name="grupalNumero2Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.grupalNumero2Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.grupalNumeroSeleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.grupalNumeroSeleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="checkBox" id="grupalTextoSeleccionado" style="width: 10px;"
																			name="grupalTextoSeleccionado" value="true"
																			<c:if test="${clasificacionSeleccionada.grupalTextoSeleccionado}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																		<input type="hidden" name="grupalTextoSeleccionado" value="${clasificacionSeleccionada.grupalTextoSeleccionado}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.seleccionaTexto" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<input type="checkBox" id="grupalTextoRequerido" style="width: 10px;"
																			name="grupalTextoRequerido" value="true" 
																			<c:if test="${clasificacionSeleccionada.grupalTextoRequerido}">
																				checked="CHECKED"
																			</c:if>
																			<c:if test="${accion == 'consultar'}">
																				disabled="disabled"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																				disabled="disabled"
																			</c:if>
																		/>
																		<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																			<input type="hidden" name="clasificacionSeleccionada.grupalTextoRequerido" value="${clasificacionSeleccionada.grupalTextoRequerido}"/>
																		</c:if>
																		<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.texto1Titulo" htmlEscape="true"/>
																	</td>
																	<td class="texto_ti">
																		<spring:message code="formularioClasificacionDocumental.texto2Titulo" htmlEscape="true"/>
																	</td>
																</tr>
																<tr>
																	<td class="texto_ti">
																		<input type="text" id="grupalTexto1Titulo" name="grupalTexto1Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.grupalTexto1Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.grupalTextoSeleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.grupalTextoSeleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																	<td class="texto_ti">
																		<input type="text" id="grupalTexto2Titulo" name="grupalTexto2Titulo" 
																			style="width: 200px;" onblur="formato(4);"
																			value="<c:out value="${clasificacionSeleccionada.grupalTexto2Titulo}"/>"
																			<c:if test="${accion == 'consultar' || !clasificacionSeleccionada.grupalTextoSeleccionado}">
																				readonly="readonly"
																			</c:if>
																			<c:if test="${clasificacionSeleccionada.grupalTextoSeleccionado}">
																				class="requerido"
																			</c:if>
																		/>
																	</td>
																</tr>
															</table>
														</div>
													</div>
													<table style="width: 100%;">		
														<tr>
															<td class="texto_ti">
																<input type="checkBox" id="descripcionSeleccionado" style="width: 10px;"
																	name="descripcionSeleccionado" value="true"
																	<c:if test="${clasificacionSeleccionada.descripcionSeleccionado}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'consultar'}">
																		disabled="disabled"
																	</c:if>
																	<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																		disabled="disabled"
																	</c:if>
																/>
																<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																<input type="hidden" name="descripcionSeleccionado" value="${clasificacionSeleccionada.descripcionSeleccionado}"/>
																</c:if>
																<spring:message code="formularioClasificacionDocumental.seleccionaDescripcion" htmlEscape="true"/>
															</td>
															<td class="texto_ti">
																<input type="checkBox" id="descripcionRequerido" style="width: 10px;"
																	name="descripcionRequerido" value="true" 
																	<c:if test="${clasificacionSeleccionada.descripcionRequerido}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'consultar'}">
																		disabled="disabled"
																	</c:if>
																	<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																		disabled="disabled"
																	</c:if>
																/>
																<c:if test="${clasificacionSeleccionada.id gt 0 && accion != 'modificar'}">
																	<input type="hidden" name="clasificacionSeleccionada.descripcionRequerido" value="${clasificacionSeleccionada.descripcionRequerido}"/>
																</c:if>
																<spring:message code="formularioClasificacionDocumental.requerido" htmlEscape="true"/>
															</td>
															<td class="texto_ti">
																<spring:message code="formularioClasificacionDocumental.descripcionTitulo" htmlEscape="true"/>
																<input type="text" id="descripcionTitulo" name="descripcionTitulo" 
																		style="width: 200px;" onblur="formato(4);"
																		value="<c:out value="${clasificacionSeleccionada.descripcionTitulo}"/>"
																		<c:if test="${accion == 'consultar'}">
																			readonly="readonly"
																		</c:if>
																		<c:if test="${clasificacionSeleccionada.descripcionSeleccionado}">
																			class="requerido"
																		</c:if>
																	/>
															</td>
														</tr>
														<tr>
															<td class="texto_ti" colspan="3">
																<input type="checkbox" id="leeCodigoBarra" name="leeCodigoBarra" value="true" style="width: 10px;"
																<c:if test="${clasificacionSeleccionada.leeCodigoBarra}">
																		checked="CHECKED"
																	</c:if>
																	<c:if test="${accion == 'consultar'}">
																		disabled="disabled"
																	</c:if>> 
																<spring:message code="formularioClasificacionDocumental.leeCodigoBarra" htmlEscape="true"/>	
															</td>
														</tr>
														<tr>
															<td class="texto_ti"><div class="trLeeCodigo">
																<table>
																	<tr>
																		<td class="texto_ti">
																			<spring:message code="formularioClasificacionDocumental.codigoBarraDesde" htmlEscape="true" />
																		</td>
																		<td class="texto_ti">
																			<spring:message code="formularioClasificacionDocumental.codigoBarraHasta" htmlEscape="true" />	
																		</td>
																	</tr>
																	<tr>
																		<td class="texto_ti">
																			<input type="text" id="codigoBarraDesde" name="codigoBarraDesde" size="1" class="requerido" 
																			value='<c:out value="${clasificacionSeleccionada.codigoBarraDesde}"/>'>
																		</td>
																		<td class="texto_ti">
																			<input type="text" id="codigoBarraHasta" name="codigoBarraHasta" size="1" class="requerido"
																			value='<c:out value="${clasificacionSeleccionada.codigoBarraHasta}"/>'>	
																		</td>
																	</tr>	
																</table>
															</div></td>	
														</tr>
													</table>
												</c:if>
												<div align="center">
													<c:if test="${accion != 'consultar'}">
														<button id="btn_guardar" name="guardar" type="button" class="botonCentrado" title="<spring:message code="botones.guardar" htmlEscape="true"/>">
															<img src="<%=request.getContextPath()%>/images/ok.png" > 
															<spring:message code="botones.guardar" htmlEscape="true"/>  
														</button>
														&nbsp;
													</c:if>
													<button id="btn_cancelar" name="cancelar" type="button" class="botonCentrado" title="<spring:message code="botones.cancelar" htmlEscape="true"/>">
														<img src="<%=request.getContextPath()%>/images/cancelar.png" > 
														<spring:message code="botones.cancelar" htmlEscape="true"/>  
													</button>
												</div>
								        	</fieldset>
										</form>
									</c:if>
								</td>
							</tr>
						</table>						
					</fieldset>
					
					<br style="font-size: xx-small;" />
							<div align="center">
								<button name="volver_atras" type="button" onclick="volver();"
									class="botonCentrado">
									<img src="<%=request.getContextPath()%>/images/volver4.png" />
									<spring:message code="botones.cerrar" htmlEscape="true" />
								</button>
							</div>
					<br style="font-size: xx-small;" />	
				</fieldset>
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
	</body>
</html>