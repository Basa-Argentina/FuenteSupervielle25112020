<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String loc = org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver(request).resolveLocale(request).toString();
pageContext.setAttribute("loc",loc);
%>

<sec:authorize ifAnyGranted="ROLE_ANY_AUTHENTICATED">
	<script language="JavaScript" src="js/jquery.cookie.js" type="text/javascript"></script>
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
		<table style="width: 100%;text-align: ">
			<tr>
				<td style="vertical-align: middle;" class="infoUsuario">
					<div class="login">			
						<sec:authorize ifAnyGranted="ROLE_ANY_AUTHENTICATED">						
							<a href="j_spring_security_logout" style="color: white; text-decoration: underline; font-weight: bold;">
								<spring:message code="menu.salir" htmlEscape="true"/>
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
			<sec:authorize ifAllGranted="ROLE_MOD_CONFIGURACION">
				<sec:authorize ifAnyGranted="ROLE_ABM_TRANSPORTES,ROLE_ABM_UBICACIONES">			
				    <li class="cfgmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_TRANSPORTES">
				        		<li><a href="iniciarTransporte.html"><span><spring:message code="menu.administrar.adminTransportes" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_UBICACIONES">
								<li><a href="iniciarLugares.html"><span><spring:message code="menu.administrar.adminUbicaciones" htmlEscape="true"/></span></a></li>
							</sec:authorize>
				        </ul>  
				    </li>
			    </sec:authorize>
			    <sec:authorize ifAnyGranted="ROLE_ABM_CONCEPTOS_FACTURABLES,ROLE_ABM_IMPUESTOS,ROLE_ABM_SERIES">
				    <li class="cfgmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.facturacion" htmlEscape="true"/></span></a>			    		
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_CONCEPTOS_FACTURABLES">
				        		<li><a href="iniciarConceptoFacturable.html"><span><spring:message code="menu.configuracion.conceptoFacturable" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_IMPUESTOS">
				        		<li><a href="iniciarImpuesto.html"><span><spring:message code="menu.configuracion.impuesto" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_SERIES">
				        		<li><a href="iniciarSerie.html"><span><spring:message code="menu.adminSeries" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_PLANTILLA_FACTURAS">
				        		<li><a href="iniciarPlantillaFacturacion.html"><span><spring:message code="menu.adminPlantillasFacturacion" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
				        </ul>  
				    </li>
			    </sec:authorize>
			    <sec:authorize ifAnyGranted="ROLE_ABM_EMPRESAS,ROLE_ABM_SUCURSALES">
				    <li class="cfgmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.organizacion" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_EMPRESAS">
				        		<li><a href="iniciarEmpresa.html"><span><spring:message code="menu.adminEmpresas" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_SUCURSALES">
				        		<li><a href="iniciarSucursal.html"><span><spring:message code="menu.adminSucursales" htmlEscape="true"/></span></a></li>			
							</sec:authorize>						
				        </ul>  
				    </li>
			    </sec:authorize>
			    <sec:authorize ifAllGranted="ROLE_MOD_CONFIGURACION">	    
				    <li class="cfgmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
				        <ul >
					        <li><a href="manuales/Manual-Configuracion.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
				        </ul>  
				    </li>
				</sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
				<sec:authorize ifAnyGranted="ROLE_ABM_ELEMENTOS,ROLE_ABM_LECTURAS,ROLE_ABM_REFERENCIAS,ROLE_ABM_REARCHIVOS,ROLE_ABM_HOJA_DE_RUTA,ROLE_PRO_EXPORTACION_REFERENCIAS,ROLE_PRO_TRANSFERENCIA_CONTENEDORES,ROLE_BUSQUEDA_REFERENCIAS">
				    <li class="depmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_ELEMENTOS">		
				        		<li><a href="iniciarElemento.html"><span><spring:message code="menu.adminElementos" htmlEscape="true"/></span></a></li>	
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_LECTURAS">
				        		<li><a href="iniciarLectura.html"><span><spring:message code="menu.adminLecturas" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_REFERENCIAS">
				        		<li><a href="consultaLotesReferencia.html"><span><spring:message code="menu.loteReferencia" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_REARCHIVOS">
				        		<li><a href="consultaLotesRearchivo.html"><span><spring:message code="menu.loteRearchivo" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_HOJA_DE_RUTA">		
				        		<li><a href="consultaHojaRuta.html"><span><spring:message code="menu.hojaRuta" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_HOJA_DE_RUTA">		
				        		<li><a href="inicioFormularioImportarDatos.html"><span><spring:message code="menu.importarDatos" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_PRO_EXPORTACION_REFERENCIAS">		
				        		<li><a href="exportarReferencia.html"><span><spring:message code="menu.exportarReferencia" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_PRO_EXPORTACION_REARCHIVOS">
				        		<li><a href="consultaExportacionRearchivo.html"><span><spring:message code="menu.exportarRearchivo" htmlEscape="true"/></span></a></li>			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_BUSQUEDA_REFERENCIAS">		
				        		<li><a href="iniciarReferencia.html"><span><spring:message code="menu.buscarReferencias" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_PRO_TRANSFERENCIA_CONTENEDORES">
								<li><a href="precargaFormularioTransferenciaContenedor.html"><span><spring:message code="menu.transferenciaContenedores" htmlEscape="true"/></span></a></li>
							</sec:authorize>	
						</ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>			
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
				<sec:authorize ifAnyGranted="ROLE_ABM_DEPOSITOS,ROLE_ABM_GRUPOS_ESTANTERIAS,ROLE_ABM_POSICIONES,ROLE_ABM_SECCIONES,ROLE_PRO_IMPRESION_ETIQUETAS">
				    <li class="depmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.estructura" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_DEPOSITOS">
				        		<li><a href="iniciarDeposito.html"><span><spring:message code="menu.adminDepositos" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_GRUPOS_ESTANTERIAS">
				        		<li><a href="iniciarGrupo.html"><span><spring:message code="menu.adminGrupos" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_CON_POSICIONES">
				        		<li><a href="iniciarPosicion.html"><span><spring:message code="menu.adminPosiciones" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_SECCIONES">
				        		<li><a href="iniciarSeccion.html"><span><spring:message code="menu.adminSecciones" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_PRO_IMPRESION_ETIQUETAS">
				        		<li><a href="iniciarImpresionEtiquetaModulo.html"><span><spring:message code="menu.adminImpresionEtiquetasModulo" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
				<sec:authorize ifAnyGranted="ROLE_ABM_JERARQUIAS,ROLE_ABM_TIPOS_DE_ELEMENTOS">
				    <li class="depmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.parametrosAdministrador" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_JERARQUIAS">
				        		<li><a href="iniciarTipoJerarquia.html"><span><spring:message code="menu.adminTipoJerarquia" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_TIPOS_DE_ELEMENTOS">	
				        		<li><a href="iniciarTipoElemento.html"><span><spring:message code="menu.adminTipoElementos" htmlEscape="true"/></span></a></li>	
							</sec:authorize>									        
				        </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	 
				<sec:authorize ifAnyGranted="ROLE_PRO_LOCALIZACION_POSICIONES,ROLE_PRO_REPOSICIONAMIENTO,ROLE_ABM_MOVIMIENTOS,ROLE_PRO_CAMBIO_ETIQUETAS,ROLE_PRO_REARCHIVAR">   
				    <li class="depmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.adminProcesos" htmlEscape="true"/></span></a>
				    	<ul >
				        	<sec:authorize ifAllGranted="ROLE_PRO_LOCALIZACION_POSICIONES">	
				        		<li><a href="iniciarPosicionLibre.html"><span><spring:message code="menu.adminPosicionLibre" htmlEscape="true"/></span></a></li>	
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_PRO_REPOSICIONAMIENTO">
				        		<li><a href="iniciarReposicionamiento.html"><span><spring:message code="menu.adminReposicionamiento" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_MOVIMIENTOS">
				        		<li><a href="iniciarListaMovimientos.html"><span><spring:message code="menu.adminConsultaMovimiento" htmlEscape="true"/></span></a></li>
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_MOVIMIENTOS">
				        		<li><a href="iniciarPrecargaFormularioMovimiento.html"><span><spring:message code="menu.adminRegistroMovimientos" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_PRO_CAMBIO_ETIQUETAS">
				        		<li><a href="iniciarPrecargaFormularioCambioEtiqueta.html"><span><spring:message code="menu.adminCambioEtiqueta" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_PRO_UNIFICAR_ETIQUETAS">
				        		<li><a href="iniciarUnificarElementos.html"><span><spring:message code="menu.adminUnificarEtiquetas" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_PRO_REARCHIVAR">
				        		<li><a href="iniciarUbicacionProvisoria.html"><span><spring:message code="menu.adminUbicacionProvisoria" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
				<sec:authorize ifAnyGranted="ROLE_CON_STOCK_MOVIMIENTOS">
				    <li class="depmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.adminStocks" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_CON_STOCK_MOVIMIENTOS">
				        		<li class="submenu"><a href="iniciarStock.html"><span><spring:message code="menu.consultaStocks" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
				<sec:authorize ifAnyGranted="ROLE_CON_HISTORICO_ELEMENTOS,ROLE_CON_HISTORICO_REFERENCIAS">
				    <li class="depmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.adminHistoricos" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_CON_HISTORICO_ELEMENTOS">
				        		<li class="submenu"><a href="iniciarHistorico.html?tipoHistorico=elemento"><span><spring:message code="menu.consultaHistoricoElementos" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_CON_HISTORICO_REFERENCIAS">
				        		<li class="submenu"><a href="iniciarHistorico.html?tipoHistorico=referencia"><span><spring:message code="menu.consultaHistoricoReferencias" htmlEscape="true"/></span></a></li>		
							</sec:authorize>
				        </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
				<sec:authorize ifAnyGranted="ROLE_PRO_INFORME_REFERENCIAS_USUARIO">
				    <li class="depmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.adminInformes" htmlEscape="true"/></span></a>
				        <ul >
				        	<li class="submenu"><a href="iniciarReferenciasPorUsuario.html"><span><spring:message code="menu.consultaReferenciasPorUsuario" htmlEscape="true"/></span></a></li>
				        	<li class="submenu"><a href="precargaFormularioRequerimiento.html"><span>Informe Cajas Sin Referencias</span></a></li>
				        </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
			   <li class="depmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="manuales/Manual-Depositos.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
			   <li class="depmod hidenLi">  
			    	<a href="#" class="parent"><span>Tareas Asignadas</span></a>
			        <ul >
				        <li><a href="mostrarTarea.html"><span>Buzón de Tareas</span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_CLIENTES">	
				<sec:authorize ifAnyGranted="ROLE_ABM_AGRUPADORES_FACTURACION,ROLE_ABM_CLIENTES_CONSULTA,ROLE_ABM_CONCEPTOS_X_CLIENTE,ROLE_ABM_CONCEPTOS_FACTURACION,ROLE_ABM_DIRECCIONES_X_CLIENTE,ROLE_ABM_ELEMENTOS,ROLE_ABM_LISTAS_DE_PRECIOS,ROLE_ABM_PERSONAL_X_CLIENTE,ROLE_ABM_REFERENCIAS">     
				    <li class="cccmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
				        <ul >
<!--				        	<sec:authorize ifAllGranted="ROLE_ABM_AGRUPADORES_FACTURACION">-->
<!--				        		<li><a href="iniciarAgrupador.html"><span><spring:message code="menu.adminAgrupadoresFacturacion" htmlEscape="true"/></span></a></li>-->
<!--							</sec:authorize>	-->
				        	<sec:authorize ifAllGranted="ROLE_ABM_CLIENTES_CONSULTA">
								<li><a href="iniciarCliente.html"><span><spring:message code="menu.adminClientes" htmlEscape="true"/></span></a></li>
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_CLIENTES_CONSULTA">
								<li><a href="iniciarCuentaCorriente.html"><span><spring:message code="menu.adminCuentaCorriente" htmlEscape="true"/></span></a></li>
							</sec:authorize>
<!--				        	<sec:authorize ifAllGranted="ROLE_ABM_CONCEPTOS_X_CLIENTE">		-->
<!--				        		<li><a href="iniciarClienteConcepto.html"><span><spring:message code="menu.adminClienteConceptos" htmlEscape="true"/></span></a></li>-->
<!--							</sec:authorize>-->
				        	<sec:authorize ifAllGranted="ROLE_ABM_CONCEPTOS_FACTURACION">		
				        		<li><a href="iniciarConceptoOperacionCliente.html"><span><spring:message code="menu.adminConceptoOperacionCliente" htmlEscape="true"/></span></a></li>
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_DIRECCIONES_X_CLIENTE">
				        		<li><a href="iniciarClienteDireccion.html"><span><spring:message code="menu.adminClienteDirecciones" htmlEscape="true"/></span></a></li>        			
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_ELEMENTOS">		
				        		<li><a href="iniciarElemento.html"><span><spring:message code="menu.adminElementos" htmlEscape="true"/></span></a></li>
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_LISTAS_DE_PRECIOS">		
				        		<li><a href="iniciarListaPrecios.html"><span><spring:message code="menu.configuracion.listaPrecios" htmlEscape="true"/></span></a></li>
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_PERSONAL_X_CLIENTE">		
				        		<li><a href="iniciarEmpleado.html"><span><spring:message code="menu.adminEmpleados" htmlEscape="true"/></span></a></li>
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_REFERENCIAS">		
				        		<li><a href="consultaLotesReferencia.html"><span><spring:message code="menu.loteReferencia" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
				         </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_CLIENTES">
				<sec:authorize ifAnyGranted="ROLE_ABM_FACTURAS,ROLE_ABM_REMITOS">	
					<li class="cccmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.comprobantes" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_FACTURAS">		
				        		<li><a href="iniciarListaComprobantesFactura.html"><span><spring:message code="menu.comprobantesFacturacion" htmlEscape="true"/></span></a></li>
				        		<li><a href="iniciarLoteFacturacion.html"><span><spring:message code="menu.comprobantesLotesFacturacion" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_REMITOS">		
				        		<li><a href="iniciarRemito.html"><span><spring:message code="menu.comprobantesRemitos" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
				        </ul>
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_CLIENTES">	    
			   <li class="cccmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="manuales/Manual-Clientes.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">
				<sec:authorize ifAnyGranted="ROLE_ABM_TIPOS_DE_ELEMENTOS,ROLE_CON_STOCK_MOVIMIENTOS">	     
				    <li class="stockmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.gestionStock" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_TIPOS_DE_ELEMENTOS">		
				        		<li><a href="iniciarTipoElemento.html"><span><spring:message code="menu.adminTipoElementos" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_CON_STOCK_MOVIMIENTOS">		
				        		<li><a href="iniciarStock.html"><span><spring:message code="menu.adminStocks" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
				         </ul>  
				    </li>
			    </sec:authorize>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_REQUERIMIENTOS">
				<sec:authorize ifAnyGranted="ROLE_ABM_REQUERIMIENTOS,ROLE_ABM_OPERACIONES,ROLE_ABM_HOJA_DE_RUTA">
					 <li class="reqmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_REQUERIMIENTOS">		
				        		<li><a href="iniciarRequerimiento.html"><span><spring:message code="menu.requerimientos.requerimientos" htmlEscape="true"/></span></a></li>
				        	</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_OPERACIONES">		
				        		<li><a href="iniciarOperacion.html"><span><spring:message code="menu.requerimientos.operaciones" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_HOJA_DE_RUTA">		
				        		<li><a href="consultaHojaRuta.html"><span><spring:message code="menu.hojaRuta" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_ABM_REFERENCIAS">		
				        		<li><a href="exportarReferencia.html"><span><spring:message code="menu.exportarReferencia" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>							
						 </ul>  
				    </li>
			    </sec:authorize>	
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_REQUERIMIENTOS">
				<sec:authorize ifAnyGranted="ROLE_ABM_TIPOS_OPERACION,ROLE_ABM_TIPOS_REQUERIMIENTOS">
					 <li class="reqmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.parametrosAdministrador" htmlEscape="true"/></span></a>
				        <ul >
				        	<sec:authorize ifAllGranted="ROLE_ABM_TIPOS_OPERACION">		
				        		<li><a href="iniciarTipoOperacion.html"><span><spring:message code="menu.requerimientos.tipoOperacion" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>
				        	<sec:authorize ifAllGranted="ROLE_ABM_TIPOS_REQUERIMIENTOS">		
				        		<li><a href="iniciarTipoRequerimiento.html"><span><spring:message code="menu.requerimientos.tipoRequerimiento" htmlEscape="true"/></span></a></li>			        			
							</sec:authorize>	        	
				         </ul>  
				    </li>
			    </sec:authorize>	
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_REQUERIMIENTOS">	    
			   <li class="reqmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="manuales/Manual-Requerimientos.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			
			<sec:authorize ifAllGranted="ROLE_MOD_REQUERIMIENTOS_WEB">
					 <li class="reqwebmod hidenLi">  
				    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
				        <ul >
				      		<li><a href="iniciarRequerimientoWeb.html"><span><spring:message code="menu.requerimientos.requerimientosWeb" htmlEscape="true"/></span></a></li>			        			
				     <%-- 	<li><a href="iniciarOperacionWeb.html"><span><spring:message code="menu.requerimientos.operacionesWeb" htmlEscape="true"/></span></a></li>	        	 --%>
			            </ul>
				    </li>
			</sec:authorize>
			
		</ul> 
	</div>
</c:if>
<c:if test="${compatibilidadIE == 'SI'}">
	<ul class="topnav">  
			<sec:authorize ifAllGranted="ROLE_ANY_AUTHENTICATED">			
			    <li><a href="/index.html"><span>Home</span></a></li>		    
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_CONFIGURACION">			
			    <li class="cfgmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarTransporte.html"><span><spring:message code="menu.administrar.adminTransportes" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarLugares.html"><span><spring:message code="menu.administrar.adminUbicaciones" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			    <li class="cfgmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.facturacion" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarConceptoFacturable.html"><span><spring:message code="menu.configuracion.conceptoFacturable" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarImpuesto.html"><span><spring:message code="menu.configuracion.impuesto" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarSerie.html"><span><spring:message code="menu.adminSeries" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarPlantillaFacturacion.html"><span><spring:message code="menu.adminPlantillasFacturacion" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			    <li class="cfgmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.organizacion" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarEmpresa.html"><span><spring:message code="menu.adminEmpresas" htmlEscape="true"/></span></a></li>
				        <li><a href="iniciarSucursal.html"><span><spring:message code="menu.adminSucursales" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
			    <li class="depmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarElemento.html"><span><spring:message code="menu.adminElementos" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarLectura.html"><span><spring:message code="menu.adminLecturas" htmlEscape="true"/></span></a></li>
			        	<li><a href="consultaLotesReferencia.html"><span><spring:message code="menu.loteReferencia" htmlEscape="true"/></span></a></li>
			        	<li><a href="consultaLotesRearchivo.html"><span><spring:message code="menu.loteRearchivo" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>			
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
			    <li class="depmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.estructura" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarDeposito.html"><span><spring:message code="menu.adminDepositos" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarGrupo.html"><span><spring:message code="menu.adminGrupos" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarPosicion.html"><span><spring:message code="menu.adminPosiciones" htmlEscape="true"/></span></a></li>
				        <li><a href="iniciarSeccion.html"><span><spring:message code="menu.adminSecciones" htmlEscape="true"/></span></a></li>
				        <li><a href="iniciarImpresionEtiquetaModulo.html"><span><spring:message code="menu.adminImpresionEtiquetasModulo" htmlEscape="true"/></span></a></li>	        
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
			    <li class="depmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.parametrosAdministrador" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="iniciarTipoJerarquia.html"><span><spring:message code="menu.adminTipoJerarquia" htmlEscape="true"/></span></a></li>
				        <li><a href="iniciarTipoElemento.html"><span><spring:message code="menu.adminTipoElementos" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
			    <li class="depmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.adminProcesos" htmlEscape="true"/></span></a>
			        <ul >			        	
			        	<li><a href="iniciarPosicionLibre.html"><span><spring:message code="menu.adminPosicionLibre" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarReposicionamiento.html"><span><spring:message code="menu.adminReposicionamiento" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarListaMovimientos.html"><span><spring:message code="menu.adminConsultaMovimiento" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarPrecargaFormularioMovimiento.html"><span><spring:message code="menu.adminRegistroMovimientos" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarPrecargaFormularioCambioEtiqueta.html"><span><spring:message code="menu.adminCambioEtiqueta" htmlEscape="true"/></span></a></li>
			        </ul>
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	    
			    <li class="depmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.adminStocks" htmlEscape="true"/></span></a>
			        <ul >
			        	<li class="submenu"><a href="iniciarStock.html"><span><spring:message code="menu.consultaStocks" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			
			<sec:authorize ifAllGranted="ROLE_MOD_CLIENTES">	     
			    <li class="cccmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
<!--			        	<li><a href="iniciarAgrupador.html"><span><spring:message code="menu.adminAgrupadoresFacturacion" htmlEscape="true"/></span></a></li>-->
			        	<li><a href="iniciarCliente.html"><span><spring:message code="menu.adminClientes" htmlEscape="true"/></span></a></li>
<!--			        	<li><a href="iniciarClienteConcepto.html"><span><spring:message code="menu.adminClienteConceptos" htmlEscape="true"/></span></a></li>-->
			        	<li><a href="iniciarConceptoOperacionCliente.html"><span><spring:message code="menu.adminConceptoOperacionCliente" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarClienteDireccion.html"><span><spring:message code="menu.adminClienteDirecciones" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarElemento.html"><span><spring:message code="menu.adminElementos" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarListaPrecios.html"><span><spring:message code="menu.configuracion.listaPrecios" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarEmpleado.html"><span><spring:message code="menu.adminEmpleados" htmlEscape="true"/></span></a></li>
			        	<li><a href="consultaLotesReferencia.html"><span><spring:message code="menu.loteReferencia" htmlEscape="true"/></span></a></li>
			         </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_CLIENTES">	
				<li class="cccmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.comprobantes" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarListaComprobantesFactura.html"><span><spring:message code="menu.comprobantesFacturacion" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarLoteFacturacion.html"><span><spring:message code="menu.comprobantesLotesFacturacion" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarRemito.html"><span><spring:message code="menu.comprobantesRemitos" htmlEscape="true"/></span></a></li>
			        </ul>
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_CLIENTES">	    
			   <li class="cccmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.ayuda" htmlEscape="true"/></span></a>
			        <ul >
				        <li><a href="manuales/Manual-Clientes.pdf" target="_blank" ><span><spring:message code="menu.manual" htmlEscape="true"/></span></a></li>
			        </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_DEPOSITOS">	     
			    <li class="stockmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.gestionStock" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarTipoElemento.html"><span><spring:message code="menu.adminTipoElementos" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarStock.html"><span><spring:message code="menu.adminStocks" htmlEscape="true"/></span></a></li>
			         </ul>  
			    </li>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_REQUERIMIENTOS">
				 <li class="reqmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.administrar" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarRequerimiento.html"><span><spring:message code="menu.requerimientos.requerimientos" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarRequerimientoWeb.html"><span><spring:message code="menu.requerimientos.requerimientosWeb" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarOperacion.html"><span><spring:message code="menu.requerimientos.operaciones" htmlEscape="true"/></span></a></li>
			         </ul>  
			    </li>	
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_REQUERIMIENTOS">
				 <li class="reqmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.parametrosAdministrador" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarTipoOperacion.html"><span><spring:message code="menu.requerimientos.tipoOperacion" htmlEscape="true"/></span></a></li>
			        	<li><a href="iniciarTipoRequerimiento.html"><span><spring:message code="menu.requerimientos.tipoRequerimiento" htmlEscape="true"/></span></a></li>	        	
			         </ul>  
			    </li>	
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_MOD_REQUERIMIENTOS_WEB">
				 <li class="reqmod hidenLi">  
			    	<a href="#" class="parent"><span><spring:message code="menu.parametrosAdministrador" htmlEscape="true"/></span></a>
			        <ul >
			        	<li><a href="iniciarRequerimientoWeb.html"><span><spring:message code="menu.requerimientos.requerimientosWeb" htmlEscape="true"/></span></a></li>	        	
			        	<li><a href="iniciarOperacionWeb.html"><span><spring:message code="menu.requerimientos.operacionesWeb" htmlEscape="true"/></span></a></li>	        	
			         </ul>
			    </li>	
			</sec:authorize>
			
	</ul>  
</c:if>
