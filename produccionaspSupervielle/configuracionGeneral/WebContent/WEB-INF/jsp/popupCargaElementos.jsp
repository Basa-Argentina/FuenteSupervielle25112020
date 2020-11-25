<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="darkMiddleCargaElementos" class="darkMiddleClass">
	<fieldset>
		<table width="100%">
			<thead>
				<tr>
					<th align="left" id="loginImg" >		       				
      					<font style="color:#003090">Carga de elementos</font>    					
   					</th>
				</tr>
  			</thead>		
		</table>
       	<div>		
			<table align="center" style="width: 100%;">							
				<tr>
					<td style="text-align: center; vertical-align: middle;">
						<div id="grupoDiv" align="center">							
							<table>
								<tr>
									<td class="texto_ti">
										<spring:message code="formularioRemito.vacio" htmlEscape="true"/>
									</td>
									<td>
										&nbsp;
									</td>
									<td class="texto_ti">
										<spring:message code="formularioRemito.detalles.listadoCodigosACargar" htmlEscape="true"/>
									</td>
								</tr>
								<tr>
							      <td width="40%" align="center">
							      <spring:message code="formularioRemito.detalles.codigoElemento" htmlEscape="true"/>
							      <br/>
							        <input type="text" id="direccionesSeleccionadasIzq" maxlength="13" size="13"  
							        class="inputTextNumericPositiveIntegerOnly" name="direccionesSeleccionadasIzq">
							        <br/>
							        <input type="checkbox" id="anexarCodigos" value="true"
													name="anexarCodigos" checked="checked"
													<c:if test="${accion == 'CONSULTA'}">
																	disabled="disabled"
																</c:if> />
												<spring:message
														code="formularioLectura.lecturaDetalle.anexar"
														htmlEscape="true" />
							      </td>
							      <td width="20%" align="center">											          
							          <c:if test="${accion != 'CONSULTA'}">
								          <img src="<%=request.getContextPath()%>/images/insertar.png" onclick="leftToRight('direccionesSeleccionadas')" 
								        	  title=<spring:message code="botones.insertar" htmlEscape="true"/>
								          	>
								          <br />
								          <img src="<%=request.getContextPath()%>/images/quitar.png" onclick="rightToLeft('direccionesSeleccionadas')" 
								        	  title=<spring:message code="botones.quitar" htmlEscape="true"/>
								          >
								          <br />
						        	  </c:if>
							      </td>
							      <td width="40%" align="center">
							        <select id="direccionesSeleccionadasDer" name="direccionesSeleccionadasDer" size="8" style="width:250px" multiple="multiple">
							            <c:forEach items="${direccionesDer}" var="direccionDer">
											<option value="<c:out value="${direccionDer.id}"/>">
												<c:out value="${direccionDer.codigoDescripcion}"/>
											</option>
										</c:forEach>
							        </select>
							      </td>
							    </tr>
						    </table>
					    </div>
					</td>
				</tr>
			
				<tr>
					<td colspan="2" align="center" style="padding-top: 10px;">
						<button id="cargarCodigosElementos" type="button" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/ok.png"> 
							<spring:message code="formularioRemito.botonCargar" htmlEscape="true"/>  
						</button>
						&nbsp;
						<button type="button" value="off" onclick="javascript:popupOff('darkLayer','darkMiddleCargaElementos');" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
							<spring:message code="botones.cancelar" htmlEscape="true"/>    
						</button>
					</td>
				</tr>				
			</table>
		</div>
	</fieldset>
</div>