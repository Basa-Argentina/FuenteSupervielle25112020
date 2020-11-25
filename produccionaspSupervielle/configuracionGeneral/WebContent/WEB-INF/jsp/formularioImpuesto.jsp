<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>	
		<title>
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioImpuesto.titulo.registrar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioImpuesto.titulo.modificar" htmlEscape="true"/>
			</c:if>  
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" language="JavaScript" src="js/mavalos_jquery.tools.min.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_impuesto.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/doblelistas.js"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>		
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioImpuesto.titulo.registrar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioImpuesto.titulo.modificar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarImpuesto.html" method="post" modelAttribute="impuestoFormulario">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${impuestoFormulario.id}" default="" />"/>						
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioImpuesto.datos" htmlEscape="true"/> 
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="width: 100%;" id="busquedaDiv" align="center">
								<table style="width: 100%;">
									<tr>
										<td class="texto_td" style="width: 50%;">
											<spring:message code="formularioImpuesto.filtro.codigo" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="codigo" name="codigo" style="width: 30px;" class="requerido upperCase alphaNumeric"
												maxlength="3" value="<c:out value="${impuestoFormulario.codigo}" default="" />"/>											
										</td>
									</tr>
									<tr>	
										<td class="texto_td">
											<spring:message code="formularioImpuesto.filtro.descripcion" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="descripcion" name="descripcion" style="width: 200px;" class="requerido"
												value="<c:out value="${impuestoFormulario.descripcion}" default="" />" maxlength="30"/> 											
										</td>
									</tr>
									<tr>										
										<td class="texto_td">
											<spring:message code="formularioImpuesto.tabla.tipo" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<select id="tipo" name="tipo" size="1" class="requerido">												
													<option value="IVA">
														I.V.A.
													</option>
											</select>											
										</td>
									</tr>
									<tr>											
										<td class="texto_td">
											<spring:message code="formularioImpuesto.tabla.alicuota" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<input type="text" id="alicuota" name="alicuota" maxlength="6" style="width: 50px;" class="requerido"
												value="<c:out value="${impuestoFormulario.alicuota}" default="" />"/> 
										</td>										
									</tr>									
									<c:if test="${accion != 'CONSULTA'}">
										<tr>
											<td class="texto_ti">
												<button name="restablecer" type="reset" >
													<img src="<%=request.getContextPath()%>/images/restablecer.png" 
														title=<spring:message code="botones.restablecer" htmlEscape="true"/> 
													> 								 
												</button>
											</td>											
										</tr>
									</c:if>
						    	</table>
							</div>
					    </fieldset>
					</form:form>
				</fieldset>
				<br style="font-size: xx-small;"/>
			    <c:if test="${accion != 'CONSULTA'}">
			    	<div align="center">
						<button name="guardar" type="button" onclick="guardarYSalir();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.guardar" htmlEscape="true"/>  
						</button>
						&nbsp;
						<button name="cancelar" type="button"  onclick="volverCancelar();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
							<spring:message code="botones.cancelar" htmlEscape="true"/>  
						</button>		
					</div>				
				</c:if>
				<c:if test="${accion == 'CONSULTA'}">
					<div align="center">
						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/volver4.png"> 
							<spring:message code="botones.volver" htmlEscape="true"/>  
						</button>
					</div>						
				</c:if>
				<br style="font-size: xx-small;"/>
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 130%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
	</body>
</html>