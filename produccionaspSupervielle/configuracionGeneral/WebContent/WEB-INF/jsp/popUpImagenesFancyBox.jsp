<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://www.costapag.org/tags/format"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
	<title> 
	</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/Utils.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>
		<script type="text/javascript" src="js/popUpImagenesFancyBox.js"></script>
		<script type="text/javascript" src="js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
		<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
		<link rel="stylesheet" type="text/css" href="js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
		
<style type="text/css">
.cascade-loading {
	background: transparent
		url("<%=request.getContextPath()%>/images/indicator.gif") no-repeat
		center;
}
</style>
</head>
<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})" style="background-color: #1584D6; width: 830px; padding: 10px 0px 0px 0px;">
	<div id="divContenedorFB" align="center" style="width:830px; min-height:432px;margin:0px;padding:0px;background-color: #FFF;" align="left">
		<fieldset style="border: none; text-align: left; width: 97%;">
			<div id="imageDiv" style="display:none; height: 650px;" >
				
			
				<input type="hidden" id="hdn_nombreArchivoDigital" value="${nombreArchivoDigital}"/>
				<input type="hidden" id="hdn_pathArchivoDigital" value="${pathArchivoDigital}"/>
				<input type="hidden" id="hdn_cantidadImagenes" value="${cantidadImagenes}"/>
				<input type="hidden" id="hdn_pathArchivoJPGDigital" value="${pathArchivoJPGDigital}"/>
								
				<c:if test="${(fn:endsWith(pathArchivoDigital,'.TIF')) || (fn:endsWith(pathArchivoDigital,'.tif'))}">
					<table class="busqueda" style="width: 100%; background-color: white;">
						<tr>
							<td class="texto_ti">
								<spring:message code="formularioLoteRearchivo.rearchivo.pagina" htmlEscape="true"/>: &nbsp;
								<select id="selectCantidadJPG" 
									name="selectCantidadJPG" size="1" style="width: 140px;">
								</select>
							</td>
							<td>
								<input type="hidden" id="zoom" name="zoom" value='<c:out value="${zoom}" default="70" />'/>
								<input type="hidden" id="scrollY" name="scrollY" value='<c:out value="${scrollY}" default="" />'/>
								<input type="hidden" id="scrollX" name="scrollX" value='<c:out value="${scrollX}" default="" />'/>
							</td>
							<td class="texto_ti" style="vertical-align: middle;">
								<spring:message code="formularioLoteRearchivo.rearchivo.zoom" htmlEscape="true"/>: &nbsp;
								<button name="btnZoomOut" id="btnZoomOut" type="button" onclick="zoomOut();"
								title="<spring:message code="formularioLoteRearchivo.rearchivo.zoom.out" htmlEscape="true" />">
									<img src="<%=request.getContextPath()%>/images/zoom-out.png">
								</button>
								<input type="text" id="zoomTxt" name="zoomTxt" value="70 %" readonly="readonly" style="width: 40px;"/>
								<button name="btnZoomIn" id="btnZoomIn" type="button" onclick="zoomIn();" 
									title="<spring:message code="formularioLoteRearchivo.rearchivo.zoom.in" htmlEscape="true" />">
									<img src="<%=request.getContextPath()%>/images/zoom-in.png">
								</button>
							</td>
						</tr>
						<tr>											
							<td class="texto_ti" colspan="6">
								<fieldset>
									<div style="overflow: auto; height:500px; text-align: center;" id="divImagen"> 	
										<div style="background-color: white; height: 380%; width: auto;" align="center">
											<img src="verImagenesRearchivo.html?fileName=${pathArchivoDigital}&pos=1"
												id="jpgDigital" name="jpgDigital" style="display:block; height: 70%; width: 70%;">	
										</div>
									</div>
								</fieldset>
							</td>
						</tr>
					</table>
				</c:if>
				
				<c:if test="${(fn:endsWith(nombreArchivoDigital,'.pdf')) || (fn:endsWith(nombreArchivoDigital,'.PDF'))}">	
					<iframe src="../FlexPaper/common/simple_document.jsp?doc=${nombreArchivoDigital}&ruta=${pathArchivoDigital}"
					id="jpgDigital" name="jpgDigital" style="display:block; height: 700px; width: 100%; border: none;"></iframe>
				</c:if>
				
		</div>
	</fieldset>
	</div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
</body>
</html>