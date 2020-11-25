<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String loc = org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver(request).resolveLocale(request).toString();
pageContext.setAttribute("loc",loc);
%>

<sec:authorize ifAnyGranted="ROLE_ANY_AUTHENTICATED">
	<script language="JavaScript" src="js/menu.js" type="text/javascript"></script>
	<%-- no borrar este link,
 evita que la libreria de los menu's inserte un link verde pidiendo agregar dicho link --%>
<div id="copyright" style="display:none;">Copyright &copy; 2011 <a href="http://apycom.com/">Apycom jQuery Menus</a></div>
</sec:authorize>

<%@page import="java.util.Date"%>
<div align="left" class="header">
	<sec:authorize ifAnyGranted="ROLE_ANY_AUTHENTICATED">
		<img alt="" src="images/img_top_modificada_es.jpg"/>
	</sec:authorize>
	<sec:authorize ifNotGranted="ROLE_ANY_AUTHENTICATED">
		<img alt="" src="images/img_top_es.jpg"/>
	</sec:authorize>
</div>
<sec:authorize ifAnyGranted="ROLE_ANY_AUTHENTICATED">
	<div align="center" class="headerDatos" >
		<div>
			<table>
				<tr>
					<td class="tdNombre">
						<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
							<spring:message code="empresa" htmlEscape="true"/>
						</sec:authorize>
						&nbsp;
					</td>
					<td class="tdDato">
						<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
							<c:out value="${empresa}"/>
						</sec:authorize>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="tdNombre">
						<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
							<spring:message code="sucursal" htmlEscape="true"/>
						</sec:authorize>
						&nbsp;
					</td>
					<td class="tdDato">
						<sec:authorize ifNotGranted="ROLE_ASP_ADMIN">
							<c:out value="${sucursal}"/>
						</sec:authorize>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="tdNombre">
						<spring:message code="usuarioRegistrado" htmlEscape="true"/>
						&nbsp;
					</td>
					<td class="tdDato">
						<sec:authentication property="principal.usernameSinCliente"/>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="tdNombre">
						<spring:message code="menu.usuarioUltimaVisita" htmlEscape="true"/>
						&nbsp;
					</td>
					<td class="tdDato">
						<c:out value="${lastLoginUsr}"/>
						&nbsp;
					</td>
				</tr>
			</table>
		</div>
	</div>
</sec:authorize>
<div style="width: 100%">
	<div class="userinfoconteiner">
		<table style="width: 100%;">
			<tr>
				<td style="vertical-align: middle;" class="infoUsuario">
					<div class="login">			
						<sec:authorize ifAnyGranted="ROLE_ANY_AUTHENTICATED">							
							<a href="j_spring_security_logout" style="color: white; text-decoration: underline; font-weight: bold;">
								<span><spring:message code="menu.salir" htmlEscape="true"/></span>
							</a>				
						</sec:authorize>				
					</div>
				</td>
			</tr>
		</table>
				
	</div>
</div>
<c:if test="${compatibilidadIE == 'NO'}">
	<div id="menu">
		<ul class="menu">  
			<sec:authorize ifAllGranted="ROLE_ANY_AUTHENTICATED">			
			    <li><a href="/index.html"><span>Home</span></a></li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_SEGURIDAD">
				<li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
			        	<sec:authorize ifAnyGranted="ROLE_ABM_GRUPOS_DE_USUARIOS">
			        		<li><a href="iniciarGroup.html"><spring:message code="menu.adminGrupos" htmlEscape="true"/></a></li>
			        	</sec:authorize>
			        	<li><a href="iniciarAuthority.html"><spring:message code="menu.adminPrivilegios" htmlEscape="true"/></a></li>
			        	<sec:authorize ifAllGranted="ROLE_ABM_USUARIOS">
			        		<li><a href="iniciarUser.html"><spring:message code="menu.adminUsuarios" htmlEscape="true"/></a></li>
			        	</sec:authorize>			        	        			        	
	<!--			        <li><a href="iniciarInsModAppLog.html"><spring:message code="menu.informeErrorAplicacion" htmlEscape="true"/></a></li>        			 -->
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_SEGURIDAD">	    
			   <li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="../configuracionGeneral/manuales/Manual-Seguridad.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_ASP_ADMIN">
				<li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarCliente.html"><spring:message code="menu.administracion.adminClientes" htmlEscape="true"/></a></li>			        	
				        <li><a href="iniciarLicencia.html"><spring:message code="menu.administracion.adminLicencias" htmlEscape="true"/></a></li>
				        <li><a href="precargaFormularioParameter.html"><spring:message code="menu.parametrosAdministrador" htmlEscape="true"/></a></li>		     			 
			        </ul>  
			    </li>			    
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_ASP_ADMIN">	    
			   <li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="../configuracionGeneral/manuales/Manual-Seguridad.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<li class="last" style="display:none;"></li>
		</ul>
	</div> 
</c:if>
<c:if test="${compatibilidadIE == 'SI'}">
	<div id="menu">
		<ul class="menu">  
			<sec:authorize ifAllGranted="ROLE_ANY_AUTHENTICATED">			
			    <li><a href="/index.html"><span>Home</span></a></li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_SEGURIDAD">
				<li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
			        	<sec:authorize ifAnyGranted="ROLE_ABM_GRUPOS_DE_USUARIOS">
			        		<li><a href="iniciarGroup.html"><spring:message code="menu.adminGrupos" htmlEscape="true"/></a></li>
			        	</sec:authorize>
			        	<li><a href="iniciarAuthority.html"><spring:message code="menu.adminPrivilegios" htmlEscape="true"/></a></li>
			        	<sec:authorize ifAllGranted="ROLE_ABM_USUARIOS">
			        		<li><a href="iniciarUser.html"><spring:message code="menu.adminUsuarios" htmlEscape="true"/></a></li>
			        	</sec:authorize>
	        			        	
	<!--			        <li><a href="iniciarInsModAppLog.html"><spring:message code="menu.informeErrorAplicacion" htmlEscape="true"/></a></li>        			 -->
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_SEGURIDAD">	    
			   <li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="../configuracionGeneral/manuales/Manual-Seguridad.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_ASP_ADMIN">
				<li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarCliente.html"><spring:message code="menu.administracion.adminClientes" htmlEscape="true"/></a></li>			        	
				        <li><a href="iniciarLicencia.html"><spring:message code="menu.administracion.adminLicencias" htmlEscape="true"/></a></li>
				        <li><a href="precargaFormularioParameter.html"><spring:message code="menu.parametrosAdministrador" htmlEscape="true"/></a></li>		     			 
			        </ul>  
			    </li>			    
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_ASP_ADMIN">	    
			   <li>  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="../configuracionGeneral/manuales/Manual-Seguridad.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<li class="last" style="display:none;"></li>	
		</ul> 
	</div>
</c:if>
