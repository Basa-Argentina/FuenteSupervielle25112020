<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="darkMiddleAvisos" class="darkMiddleClass" align="left" style="left: 20px; width: 60px; background-color: white; ">
	<fieldset style="background-color: white;">
		<table width="100%" style="background-color: white;">
			<thead>
				<tr>
					<th align="left" id="loginImg" >		       				
      					<font style="color:#003090"><spring:message code="textos.avisos" htmlEscape="true"/></font>    					
   					</th>
				</tr>
  			</thead>		
		</table>
       	<div class="seccion2">
		
			<table align="center" style="width: 100%; background-color: white;">						
				<tr>
					<td style="text-align: left; vertical-align: center; width: 1%; padding-left: 16px;" rowspan="2">
						<c:if test="${hayAvisos == true}">
							<img src="<%=request.getContextPath()%>/images/mensajes/check.png" style="width: 64px; height: 64px;"/>
						</c:if>
						<c:if test="${hayAvisosNeg == true}">
							<img src="<%=request.getContextPath()%>/images/mensajes/error.png" style="width: 64px; height: 64px;"/>
						</c:if>					
					</td>
					<td style="text-align: center; vertical-align: middle;">
						<!-- todos los avisos -->
						<c:forEach items="${avisos}" var="aviso">
							<font style="font-weight: bold;">
								<spring:message code="${aviso.messageCode}"/>
							</font>	
							<c:if test="${aviso.messageArguments != null}">
								<c:forEach items="${aviso.messageArguments}" var="arg">
									<c:out value="${arg}"/>
								</c:forEach>
							</c:if>							
						</c:forEach>						
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center" style="padding-top: 10px;">
						<button type="button" value="off" onclick="javascript:popupOff('darkLayer','darkMiddleAvisos');" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.aceptar" htmlEscape="true"/>  
						</button>
					</td>
				</tr>				
			</table>
		</div>
	</fieldset>
</div>