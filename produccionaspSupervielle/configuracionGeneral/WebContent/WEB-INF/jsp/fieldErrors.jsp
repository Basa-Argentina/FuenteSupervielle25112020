<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div id="darkMiddle" class="darkMiddleClass">
	<fieldset>
		<table width="100%">
			<thead>
				<tr>
					<th align="left" id="loginImg" >
		       				
      					<font style="color:#003090"><spring:message code="textos.validacion" htmlEscape="true"/></font>
    					
   					</th>
				</tr>
  			</thead>		
		</table>
       	<div class="seccion2">		
			<table align="center" style="width: 100%;">						
				<tr>
					<td style="text-align: left; vertical-align: center; width: 1%; padding-left: 16px;" rowspan="2">
						<img src="<%=request.getContextPath()%>/images/mensajes/error.png" style="width: 64px; height: 64px;"/>
					</td>
					<td style="text-align: center; vertical-align: middle;">
						<!-- todos los errores -->
						<c:forEach items="${result.fieldErrors}" var="error">
							<font style="font-weight: bold;">* <spring:message code="${error.field}" arguments="${error.arguments}"/> </font>
							<c:if test="${error.code != ''&& error.field != error.code}">
								: <spring:message code="${error.code}" arguments="${error.arguments}"/>
							</c:if>
							<br>
						</c:forEach>
						<c:forEach items="${result.globalErrors}" var="error">
							<spring:message code="${error.code}" arguments="${arguments}"/>
							<br>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center" style="padding-top: 10px;">
						<button type="button" value="off" onclick="javascript:popupOff('darkLayer','darkMiddle');" class="botonCentrado"><img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.aceptar" htmlEscape="true"/>  
						</button>
					</td>
				</tr>
			</table>
		</div>
	</fieldset>
</div>