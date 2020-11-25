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
			<spring:message code="formularioLoteExportacionRearchivo.titulo.registar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'MODIFICACION'}">
			<spring:message code="formularioLoteExportacionRearchivo.titulo.modificar"
				htmlEscape="true" />
		</c:if> <c:if test="${accion == 'CONSULTA'}">
			<spring:message code="formularioLoteExportacionRearchivo.titulo.consultar"
				htmlEscape="true" />
		</c:if>
	</title>
	<script type="text/javascript" src="js/jquery-1.5.js"></script>
	<script type="text/javascript" src="js/httprequest.js"></script>
	<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>
	<script type="text/javascript" src="js/Utils.js"></script>
	<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
	<script type="text/javascript" src="js/jquery.alerts.js"></script>
	<script type="text/javascript" src="js/jquery.chromatable.js"></script>
	<script type="text/javascript" src="js/calendar_us.js"></script>
	<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
	
	<script type="text/javascript" src="js/ini.js"></script>
	<script type="text/javascript" src="js/busquedaHelper.js"></script>
	<script type="text/javascript" src="js/formulario_exportacion_rearchivo.js"></script>
	<style type="text/css">
	.cascade-loading {
		background: transparent
			url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
			center;
	}
	</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg});">
	<div class="contextMenu" id="myMenu1">
	    <ul>	 
	      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
	    </ul> 	 
  	</div> 
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> 
						<c:if test="${accion == 'NUEVO'}">
							<spring:message code="formularioLoteExportacionRearchivo.titulo.registar"	htmlEscape="true" />
						</c:if> 
						<c:if test="${accion == 'MODIFICACION'}">
							<spring:message code="formularioLoteExportacionRearchivo.titulo.modificar" htmlEscape="true" />
						</c:if> 
						<c:if test="${accion == 'CONSULTA'}">
							<spring:message code="formularioLoteExportacionRearchivo.titulo.consultar" htmlEscape="true" />
						</c:if> 
					</font>
				</legend>
				<br />
				<form:form action="guardarActualizarLoteExportacionRearchivo.html"
					commandName="loteExportacionRearchivo" method="post"
					modelAttribute="loteExportacionRearchivo">
					<!-- atributos del lote de rearchivo que deben mantenerse -->
					<input type="hidden" id="id" name="id" value="<c:out value="${loteExportacionRearchivo.id}" default="" />" />
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
					<input type="hidden" id="sizeMaxArchivo" name="sizeMaxArchivo" value="<c:out value="${loteExportacionRearchivo.sizeMaxArchivo}" default="" />" />
					<input type="hidden" id="cantPartesGeneradas" name="cantPartesGeneradas" value="<c:out value="${loteExportacionRearchivo.cantPartesGeneradas}" default="" />" />
					<input type="hidden" id="pathArchivoBase" name="pathArchivoBase" value="<c:out value="${loteExportacionRearchivo.pathArchivoBase}" default="" />" />
					
					<!-- para las acciones -->
					<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />" />
					<input type="hidden" id="codigoLoteRearchivoSeleccionado" name="codigoLoteRearchivoSeleccionado" value=""/>
					<input type="hidden" id="codigoLoteRearchivoEliminar" name="codigoLoteRearchivoEliminar" value=""/>
					<!-- para los mensajes -->
					<input type="hidden" id="mensajeSeleccioneEmpresa" value="<spring:message code="notif.seleccion.seleccionEmpresa" htmlEscape="true"/>"/>
					<input type="hidden" id="mensajeSeleccioneCliente" value="<spring:message code="notif.seleccion.seleccionCliente" htmlEscape="true"/>"/>
					<input type="hidden" id="mensajeSeleccioneClasificacionDocumental" value="<spring:message code="notif.seleccion.seleccioneClasificacionDocumental" htmlEscape="true"/>"/>
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioLoteExportacionRearchivo.datosGenerales" htmlEscape="true" />
										</font> 
										<img id="busquedaImgSrcDown" src="images/skip_down.png"
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
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.codigo" htmlEscape="true"/>
									</td>									
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.fecha" htmlEscape="true"/>
									</td>	
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.empresa" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.sucursal" htmlEscape="true"/>
									</td>									
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigo" name="codigo" style="width: 50px;" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.id}" default="" />"
											<c:if test="${accion == 'NUEVO'}">
												title="<spring:message code="formularioLoteExportacionRearchivo.codigoInfo" htmlEscape="true"/>"
											</c:if>
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
										/>
										
									</td>										
									<td class="texto_ti">
										<input type="text" id="fechaRegistro" name="fechaRegistro" 
											style="width: 80px;" class="requerido"
											value="<c:out value="${loteExportacionRearchivo.fechaRegistroStr}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO'}">
												readonly="readonly"
											</c:if>
										/>
										<c:if test="${accion == 'NUEVO'}">
											<script type="text/javascript" >
												new tcal ({
													// form name
													'formname': 'loteExportacionRearchivo',
													// input name
													'controlname': 'fechaRegistro'
												});
											</script>
										</c:if>
									</td>
									<td class="texto_ti">
										<input type="text" id="codigoEmpresa" name="codigoEmpresa" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${loteExportacionRearchivo.empresa.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaEmpresa"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
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
										<input type="text" id="codigoSucursal" name="codigoSucursal" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${loteExportacionRearchivo.sucursal.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaSucursal"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoSucursalLabel" for="codigoSucursal"> 
											 
										</label>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.cliente" htmlEscape="true"/>
									</td>
									<td class="texto_ti" colspan="2">
										<spring:message code="formularioLoteExportacionRearchivo.clasificacionDocumental" htmlEscape="true"/>
									</td>
									<td class="texto_ti">
										<spring:message code="formularioLoteExportacionRearchivo.estado" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti">
										<input type="text" id="codigoCliente" name="codigoCliente" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${loteExportacionRearchivo.clienteEmp.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaCliente" 
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoClienteLabel" for="codigoCliente"> 
											 
										</label>
									</td>
									<td class="texto_ti" colspan="2">
										<input type="text" id="codigoClasificacionDocumental" name="codigoClasificacionDocumental" 
											style="width: 50px;" class="requerido"
											value="<c:out value="${loteExportacionRearchivo.clasificacionDocumental.codigo}" default="" />"
											<c:if test="${accion == 'CONSULTA'}">
												disabled="disabled"
											</c:if>
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
												readonly="readonly"
											</c:if>
										/>
										&nbsp;&nbsp;
										<button type="button" id="buscaClasificacionDocumental" 
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion != 'NUEVO' || bloquearCampos}">
												disabled="disabled"
											</c:if>
										>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>
										&nbsp;&nbsp; 
										<label id="codigoClasificacionDocumentalLabel" for="codigoClasificacionDocumental"> 
											 
										</label>
									</td>
									<td class="texto_ti">
										<input type="text" id="estado" name="estado" 
											style="width: 250px;" class="requerido" readonly="readonly"
											value="<c:out value="${loteExportacionRearchivo.estado}" default="" />"
										/>
									</td>
								</tr>
								<tr>
									<td class="texto_ti" colspan="4">
										<spring:message code="formularioLoteExportacionRearchivo.descripcion" htmlEscape="true"/>
									</td>									
								</tr>
								<tr>
									<td class="texto_ti" colspan="4">
										<textarea id="descripcion" name="descripcion" cols="90" rows="3">${loteExportacionRearchivo.descripcion}</textarea>
									</td>									
								</tr>
							</table>
						</div>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="grupoImg">
										<font style="color: #003090"> 
											<spring:message	code="formularioLoteExportacionRearchivo.lotesRearchivo" htmlEscape="true" />
										</font> 
										<img id="grupoImgSrcDown" src="images/skip_down.png"
											title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="grupoImgSrc" src="images/skip.png"
											style="DISPLAY: none"
											title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>								
							</thead>
							<tbody>
								<tr>
									<td>
										<div style="width: 100%" id="grupoDiv" align="center">
											<fieldset>
												<display:table name="sessionScope.lotesRearchivo" id="loteRearchivo" >
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
												<c:if test="${accion != 'CONSULTA' }">
													<div style="width: 100%" align ="right">
														<button name="agregar" type="button" onclick="agregarLoteRearchivo();" class="botonCentrado">
															<img src="<%=request.getContextPath()%>/images/add.png" > 
															<spring:message code="botones.agregar" htmlEscape="true"/>   
														</button>
													</div>
												</c:if>
											</fieldset>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form:form>
			</fieldset>
			<br style="font-size: xx-small;" />
			<c:if test="${accion != 'CONSULTA'}">
				<div align="center">
					<button name="guardar" type="button" class="botonCentrado" id="btnGuardar">
						<img src="<%=request.getContextPath()%>/images/ok.png">
						<spring:message code="botones.guardar" htmlEscape="true" />
					</button>
					&nbsp;
					<button name="cancelar" type="button" id="btnCancelar" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/cancelar.png">
						<spring:message code="botones.cancelar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<c:if test="${accion == 'CONSULTA'}">
				<div align="center">
					<button name="volver_atras" type="button" id="btnVolver" class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png">
						<spring:message code="botones.volver" htmlEscape="true" />
					</button>
					&nbsp;
					<button name="exportar" type="button" class="botonCentrado" id="btnExportar" style="width:200px;">
						<img src="<%=request.getContextPath()%>/images/print.png">
						<spring:message code="botones.exportar" htmlEscape="true" />
					</button>
				</div>
			</c:if>
			<br style="font-size: xx-small;" />
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClassWithoutHeight" style="height: 130%;">&nbsp;</div>
	<jsp:include page="fieldErrors.jsp"/>	
	<jsp:include page="fieldAvisos.jsp"/>
	<div class="selectorDiv"></div>
	<div id="pop" style="display:none">
		<img src="<%=request.getContextPath()%>/images/wait.gif" border="0" width="20px" height="20px">
		<label><spring:message code="textos.espere" htmlEscape="true"/></label>	     
	</div>
</body>
</html>

