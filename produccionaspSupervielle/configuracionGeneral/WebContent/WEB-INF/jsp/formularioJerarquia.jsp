<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<c:if test="${accion == 'NUEVO'}">
				<spring:message code="formularioJerarquia.titulo.registrar" htmlEscape="true"/>
			</c:if> 
			<c:if test="${accion == 'MODIFICACION'}">
				<spring:message code="formularioJerarquia.titulo.modificar" htmlEscape="true"/>
			</c:if>  
			<c:if test="${accion == 'CONSULTA'}">
				<spring:message code="formularioJerarquia.titulo.consultar" htmlEscape="true"/>
			</c:if> 
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"></script>		
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/mavalos_formulario_jerarquia.js"></script>
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
    </head>		
	<body onload="mostrarErrores(${errores});">
		<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
				<br style="font-size: medium;"/>
				<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
						<font class="tituloForm" size="5">
			        		<c:if test="${accion == 'NUEVO'}">
								<spring:message code="formularioJerarquia.titulo.registrar" htmlEscape="true"/> 
							</c:if> 
							<c:if test="${accion == 'MODIFICACION'}">
								<spring:message code="formularioJerarquia.titulo.modificar" htmlEscape="true"/>
							</c:if> 
							<c:if test="${accion == 'CONSULTA'}">
								<spring:message code="formularioJerarquia.titulo.consultar" htmlEscape="true"/>
							</c:if> 
						</font>		  
					</legend>
					<br/>
					<form:form action="guardarActualizarJerarquia.html" modelAttribute="jerarquia" method="post">
						<input type="hidden" id="accion" name="accion" value="<c:out value="${accion}" default="" />"/>						
						<input type="hidden" id="id" name="id" value="<c:out value="${jerarquia.id}" default="" />"/>
						<input type="hidden" id="tipoId" name="tipoId" value="<c:out value="${jerarquia.tipo.id}" default="" />"/>													
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="formularioJerarquia.datos" htmlEscape="true"/> 
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="width: 100%;" id="busquedaDiv" align="center">								
								<table>								
									<tr>	
										<td class="texto_ti">
											<spring:message code="formularioJerarquia.datos.descripcion" htmlEscape="true"/>
										</td>										
									</tr>
									<tr>	
										<td class="texto_ti">
											<input type="text" id="descripcion" name="descripcion" style="text-align: left; width: 250px;" class="requerido"
												value="<c:out value="${jerarquia.descripcion}" default="" />" maxlength="60"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
											/>
										</td>										
									</tr>
									<tr>	
										<td class="texto_ti">
											<spring:message code="formularioJerarquia.datos.valoracion" htmlEscape="true"/>
										</td>										
									</tr>
									<tr>	
										<td class="texto_ti">
											<input type="text" id="valoracion" name="valoracion" style="text-align: right; width: 50px;" maxlength="3"
												class="requerido numeric" value="<c:out value="${jerarquia.valoracion}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
											/>	
										</td>										
									</tr>
									<tr>	
										<td class="texto_ti">
											<spring:message code="formularioJerarquia.datos.vertical" htmlEscape="true"/>
										</td>										
									</tr>
									<tr>									
										<td class="texto_ti">
											<spring:message code="formularioJerarquia.datos.desde" htmlEscape="true"/> 
											<input type="text" id="verticalDesde" name="verticalDesde" style="text-align: right; width: 50px;" 
												maxlength="3" class="requerido numeric" value="<c:out value="${jerarquia.verticalDesde}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
											/>
											&nbsp;
											<spring:message code="formularioJerarquia.datos.hasta" htmlEscape="true"/>
											<input type="text" id="verticalHasta" name="verticalHasta" style="text-align: right; width: 50px;" 
												maxlength="3" class="requerido numeric" value="<c:out value="${jerarquia.verticalHasta}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
											/>	
										</td>										
									</tr>
									<tr>	
										<td class="texto_ti">
											<spring:message code="formularioJerarquia.datos.horizontal" htmlEscape="true"/>
										</td>										
									</tr>
									<tr>									
										<td class="texto_ti">
											<spring:message code="formularioJerarquia.datos.desde" htmlEscape="true"/>
											<input type="text" id="horizontalDesde" name="horizontalDesde" style="text-align: right; width: 50px;" 
												maxlength="3" class="requerido numeric" value="<c:out value="${jerarquia.horizontalDesde}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
											/>
											&nbsp;
											<spring:message code="formularioJerarquia.datos.hasta" htmlEscape="true"/>
											<input type="text" id="horizontalHasta" name="horizontalHasta" style="text-align: right; width: 50px;" 
												maxlength="3" class="requerido numeric" value="<c:out value="${jerarquia.horizontalHasta}" default="" />"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
											/>	
										</td>										
									</tr>
									<tr>	
										<td class="texto_ti">
											<spring:message code="formularioJerarquia.datos.observacion" htmlEscape="true"/>
										</td>										
									</tr>
									<tr>	
										<td class="texto_ti">
											<textarea id="observacion" name="observacion" rows="6" cols="30"
												style="resize: none;" class="observacion"
												<c:if test="${accion == 'CONSULTA'}">
													readonly="readonly"
												</c:if>
											><c:out value="${jerarquia.observacion}"/></textarea>												
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
		<div id="darkLayer" class="darkClassWithoutHeight" style="height: 140%;">&nbsp;</div>  
		<jsp:include page="fieldErrors.jsp"/>	
	</body>
</html>