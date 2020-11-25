<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="darkMiddleOpcion" class="darkMiddleClass">
	<fieldset>
		<table width="100%">
			<thead>
				<tr>
					<th align="left" id="loginImg" >		       				
      					<font style="color:#003090"><spring:message code="${opcionesTitulo}" htmlEscape="true"/></font>    					
   					</th>
				</tr>
  			</thead>		
		</table>
       	<div class="seccion2">		
			<table align="center" style="width: 100%;">							
				<tr>
					<td style="text-align: left; vertical-align: center; width: 1%; padding-left: 16px;" rowspan="2">						
						<img src="<%=request.getContextPath()%>/images/mensajes/exclamation.png"/>						
					</td>
					<td style="text-align: center; vertical-align: middle;">
						<!-- todos los avisos -->
						<c:forEach items="${opciones}" var="opcion">
							<input type="radio" name="radio_group" value="${opcion.value}"
								<c:if test="${opcion.selected == true}">
									checked="checked"									
								</c:if>
							/>
								<spring:message code="${opcion.message}" htmlEscape="true"/><br>							
						</c:forEach>						
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center" style="padding-top: 10px;">
						<button name="guardar" type="button" onclick="aceptar();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="botones.guardar" htmlEscape="true"/>  
						</button>
						&nbsp;
						<button type="button" value="off" onclick="javascript:popupOff('darkLayer','darkMiddleOpcion');" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
							<spring:message code="botones.cancelar" htmlEscape="true"/>    
						</button>
					</td>
				</tr>				
			</table>
		</div>
	</fieldset>
</div>