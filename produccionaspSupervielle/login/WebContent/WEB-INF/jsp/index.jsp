<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
String loc = org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver(request).resolveLocale(request).toString();
pageContext.setAttribute("loc",loc);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><spring:message code="bienvenido" htmlEscape="true"/> - <spring:message code="general.empresa" htmlEscape="true"/> <spring:message code="general.ambiente" htmlEscape="true"/></title>
	
	<jsp:include page="styles.jsp"/>
	<link rel="stylesheet" href="css/inFieldLabels.css" type="text/css" />
	<script type="text/javascript" src="js/jquery-1.5.js"></script>  
	<script type="text/javascript" src="js/jquery.infieldlabel.js"></script> 	
    <script language="JavaScript" src="js/mavalos_jquery.tools.min.js" type="text/javascript"></script>
   	<script language="JavaScript" src="js/mavalos_index.js" type="text/javascript"></script>
   	<script language="JavaScript" src="js/jquery.cookie.js" type="text/javascript"></script>
    <script language="JavaScript" src="js/ini.js" type="text/javascript"></script>
	<meta content="MSHTML 8.00.6001.18852" name="GENERATOR">
	
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg}); setSeparador('<c:out value="${separador}"/>')">
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp"/>
		<c:if test="${action!=null}">
			<form name="f" action="${action}" method="post"
				<c:if test="${newPage==true}">	
					target="_blank"
				</c:if>
			>
				<input type="hidden" name="sessionString" value="${securityString}">
			</form>
			<script type="text/javascript">
				document.forms[0].submit();
			</script>
		</c:if>
		<div class="contenido" align="center">
			
			<br>
			<fieldset style="border: none;">
				<legend>
		        	<font class="tituloForm" size="5">
		        		<spring:message code="bienvenido" htmlEscape="true"/>
	        		</font>		  
				</legend>
			</fieldset>
			<br>			
			<sec:authorize ifNotGranted="ROLE_ANY_AUTHENTICATED">				
				<div >
					<table>						
						<tr>							
							<td valign="top" style="text-align: left;">
								<fieldset>
									<table width="100%">
										<thead>
											<tr>
												<th align="left" id="agenciaImg" >							         				
							         				<font style="color:#003090"><spring:message code="index.loggin" htmlEscape="true"/></font>							       					
						       					</th>
					       					</tr>
					       				</thead>
			       					</table>
			       					<div align="center" style="overflow: visible;">										
									   	<form id="f" name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
											<fieldset style="border: none;">
												<table width="100%">
													<tr>
														<td align="left">														
															<p>
													       		<label for="j_empresa"><spring:message code="login.empresa" htmlEscape="true"/></label>
																  <input type='text' id='j_empresa' style="width: 145px;" class="lowerCase"/>
															</p>
															<p>
													       		<label for="j_username"><spring:message code="login.usuario" htmlEscape="true"/></label>
																  <input type='text' name='j_username' id='j_username' style="width: 145px;" class="lowerCase"/>
															</p>
															<p>
																<label for="j_password"><spring:message code="login.pass" htmlEscape="true"/></label>
																<input type='password' name='j_password' id='j_password' style="width: 145px;">
															</p>															
														</td>
														<td>
															<c:if test="${not empty param.login_error}">
															     <font style="font-size: 11px; color: red; position: absolute; text-align: center; padding-left: 50px;">
															       <spring:message code="login.error" htmlEscape="true"/>
															     </font>
													   		</c:if>
														</td>
													</tr>
													<tr>	
														<td style="text-align: center;">
															<button name="guardar" type="button" class="botonLoggin" onclick="login()"> 
																<img src="<%=request.getContextPath()%>/images/ok3.png" border="0"/> 	
													       		<spring:message code="botones.ingresar" htmlEscape="true"/>											       										 
															</button>								
														</td>
													</tr>
													<tr>
											       		<td style="text-align: center;">									       			
															<a href="precargaFormularioRestorePassword.html" style="color: #003090; font-size: 11px;"><spring:message code="login.restorePassword" htmlEscape="true"/></a>
											       		</td>								       		
											       </tr>
												</table>
											</fieldset>												
										</form>
									</div>
									
								</fieldset>
							</td>
						</tr>
					</table>
				</div>
				<br>				
			</sec:authorize>
			<sec:authorize ifAnyGranted="ROLE_NO_LICENSE">	
				<font style="font-size: large; color: #8A0808;">
					<spring:message code="notif.licencia.licenciaNoValida" htmlEscape="true"/>	
				</font>
				<br/><br/>
				<table>
					<tr>
						<td>
							<fieldset>
								<legend>
									<spring:message code="mail.envio" htmlEscape="true"/>
								</legend>
								<form:form action="enviarMailAdministrador.html" commandName="mail" method="post" modelAttribute="mail">
									<table>												
										<tr>
											<td style="vertical-align: top;">
												<spring:message code="mail.asunto" htmlEscape="true"/>
											</td>
											<td style="vertical-align: top;">
												<input type="text" id="asunto" name="asunto" style="width: 414px;" maxlength="100"/>
											</td>
										</tr>							
										<tr>
											<td style="vertical-align: top;">
												<spring:message code="mail.mensaje" htmlEscape="true"/>
											</td>
											<td style="vertical-align: top;">
												<textarea id="mensaje" name="mensaje" rows="10" cols="50" style="resize: none;"><c:out value="${clienteFormulario.observaciones}"/></textarea>
											</td>
										</tr>
										<tr>
											<td style="text-align: right;" colspan="2">
												<button type="submit" class="botonCentrado">
													<img src="<%=request.getContextPath()%>/images/mail_forward.png"/>
													<spring:message code="botones.enviar" htmlEscape="true"/>
												</button>
											</td>
										</tr>							
									</table>
								</form:form>
							</fieldset>	
						</td>
					</tr>
				</table>
										
			</sec:authorize>
			<sec:authorize ifAnyGranted="ROLE_ANY_AUTHENTICATED">
				<c:if test="${setEmpresa == null}">					
						<font style="font-size: large; color: #8A0808;">
							<spring:message code="notif.empresaSucursal.noConfigurada" htmlEscape="true"/>	
						</font>
						<br/><br/>
						<table>
							<tr>
								<td>
									<fieldset>
										<legend>
											<spring:message code="notif.empresaSucursal.infomarcion" htmlEscape="true"/>
										</legend>
										<form:form id="continuar" name="continuar" action="noConfigurarEmpresaSucursal.html" method="POST">
											<table>												
												<tr>
													<td style="vertical-align: top;">
														<spring:message code="notif.empresaSucursal.mensaje" htmlEscape="true"/>
													</td>											
												</tr>										
												<tr>
													<td style="text-align: center;">
														<button name="aceptar" type="button"  onclick="configurarEmpresaSucursal();" 
																class="botonCentrado">
															<img src="<%=request.getContextPath()%>/images/match.png" width="13px" height="13px"> 
															<spring:message code="botones.aceptar" htmlEscape="true"/>  
														</button>
														<button name="aceptar" type="submit" class="botonCentrado">
															<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
															<spring:message code="botones.cancelar" htmlEscape="true"/>  
														</button>
													</td>
												</tr>							
											</table>
										</form:form>
									</fieldset>	
								</td>
							</tr>
						</table>		
				</c:if>
			</sec:authorize>
			<c:if test="${setEmpresa == true}">
				<sec:authorize ifNotGranted="ROLE_NO_LICENSE" ifAnyGranted="ROLE_ANY_AUTHENTICATED">
					<p style="color: #003090;">
						<spring:message code="index.modulo" htmlEscape="true"/>
					</p>
				</sec:authorize>
				<table>
					<sec:authorize ifAnyGranted="ROLE_ASP_ADMIN">
						<tr>
							<td class="texto_ti">						
								<form action="redirect.html" method="post" id="secmodForm">
									<input type="hidden" name="modulo" value="security">
									<input type="hidden" name="submodulo" value="secmod">
									<a href="#" onclick="javascript:document.getElementById('secmodForm').submit();">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.aspmod" htmlEscape="true"/>	
									</a>
								</form>						
							</td>
						</tr>
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_MOD_CLIENTES">
						<tr>	
							<td class="texto_ti">				
								<form action="redirect.html" method="post" id="cccmodForm">
									<input type="hidden" name="modulo" value="configuracionGeneral">
									<a href="#" onclick="ingresarModulo('cccmodForm','cccmod');">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.cccmod" htmlEscape="true"/>
									</a>
								</form>							
							</td>
						</tr>
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_MOD_CONFIGURACION">	
						<tr>	
							<td class="texto_ti">					
								<form action="redirect.html" method="post" id="cfgmodForm">
									<input type="hidden" name="modulo" value="configuracionGeneral">
									<a href="#" onclick="ingresarModulo('cfgmodForm','cfgmod');">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.cfgmod" htmlEscape="true"/>
									</a>
								</form>						
							</td>
						</tr>
					</sec:authorize>
						<sec:authorize ifAnyGranted="ROLE_MOD_DEPOSITOS1">
						<tr>	
							<td class="texto_ti">					
								<form action="redirect.html" method="post" id="depmodForm">
									<input type="hidden" name="modulo" value="configuracionGeneral">								
									<a href="#" onclick="ingresarModulo('depmodForm','depmod');">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.depmod" htmlEscape="true"/>
									</a>
								</form>							
							</td>
						</tr>
					</sec:authorize>

					<sec:authorize ifAnyGranted="ROLE_MOD_DEPOSITOS">
						
						<tr>	
							<td class="texto_ti">					
				<form action="redirectProxy.html" method="post" id="cargaRefForm" name="cargaRefForm">
    	     <input type="hidden" name="modulo" value="configuracionGeneral">
     	    <input type="hidden" name="submodulo" value="consultaLotesReferencia">
      	   <a href="#" onclick="javascript:document.getElementById('cargaRefForm').submit();">
     	     <img src="<%=request.getContextPath()%>/images/c_gris.png"/>
       	   &nbsp;<spring:message code="index.modulo.cargaDoc" htmlEscape="true"/>
      	   </a>
     		   </form>	
							</td>
						</tr>
						
						<tr>	
							<td class="texto_ti">					
				<form action="redirectProxy.html" method="post" id="consultaRefForm" name="consultaRefForm">
    	     <input type="hidden" name="modulo" value="configuracionGeneral">
     	    <input type="hidden" name="submodulo" value="iniciarReferencia">
      	   <a href="#" onclick="javascript:document.getElementById('consultaRefForm').submit();">
     	     <img src="<%=request.getContextPath()%>/images/c_gris.png"/>
       	   &nbsp;<spring:message code="index.modulo.busquedaDoc" htmlEscape="true"/>
      	   </a>
     		   </form>	
							</td>
						</tr>

					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_MOD_REQUERIMIENTOS">
						<tr>	
							<td class="texto_ti">			
								<form action="redirect.html" method="post" id="reqmodForm">
									<input type="hidden" name="modulo" value="configuracionGeneral">
									<a href="#" onclick="ingresarModulo('reqmodForm','reqmod');">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="menu.pedidosweb" htmlEscape="true"/>
									</a>
								</form>							
							</td>
						</tr>
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_MOD_SEGURIDAD">
						<tr>	
							<td class="texto_ti">					
								<form action="redirect.html" method="post" id="secmodForm">
									<input type="hidden" name="modulo" value="security">
									<input type="hidden" name="submodulo" value="secmod">
									<a href="#" onclick="javascript:document.getElementById('secmodForm').submit();">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.secmod" htmlEscape="true"/>
									</a>
								</form>						
							</td>
						</tr>
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_MOD_REQUERIMIENTOS_WEB">	
					<%-- 	<tr>	
							<td class="texto_ti">					
								<form action="redirect.html" method="post" id="reqwebmodForm" name="reqwebmodForm">
									<input type="hidden" name="modulo" value="configuracionGeneral">
									<input type="hidden" name="submodulo" value="reqwebmod">
									<a href="#" onclick="ingresarModulo('reqwebmodForm','reqwebmod');">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.reqwebmod" htmlEscape="true"/>
									</a>
								</form>						
							</td>
						</tr> --%>
						<tr>	
							<td class="texto_ti">					
								<form action="redirectProxy.html" method="post" id="reqwebProxyForm" name="reqwebProxyForm">
									<input type="hidden" name="modulo" value="configuracionGeneral">
									<input type="hidden" name="submodulo" value="mostrarRequerimientoWeb">
									<a href="#" onclick="javascript:document.getElementById('reqwebProxyForm').submit();">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.pedidoWeb" htmlEscape="true"/>
									</a>
								</form>						
							</td>
						</tr>
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_ABM_FACTURAS">	
						<tr>	
							<td class="texto_ti">					
								<form action="redirect.html" method="post" id="fctmodForm" name="fctmodForm">
									<input type="hidden" name="modulo" value="facturacion">
									<input type="hidden" name="submodulo" value="fctmod">
									<a href="#" onclick="ingresarModulo('fctmodForm','fctmod');">
										<img src="<%=request.getContextPath()%>/images/c_gris.png"/>
										&nbsp;<spring:message code="index.modulo.fctmod" htmlEscape="true"/>
									</a>
								</form>						
							</td>
						</tr>
					</sec:authorize>					
				</table>
			</c:if>
		</div>		
		<jsp:include page="footer.jsp"/>		
	</div>	
	<div id="darkLayer" class="darkClass">&nbsp;</div>		
	<jsp:include page="fieldErrors.jsp"/>
	<jsp:include page="fieldAvisos.jsp"/>
</body>
</html>
