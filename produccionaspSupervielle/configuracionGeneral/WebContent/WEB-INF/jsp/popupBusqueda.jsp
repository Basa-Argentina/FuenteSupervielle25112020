<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<% 	
	String key = request.getParameter("mapa");
	java.util.Map<String,Object> mapaPopup = (java.util.Map<String,Object>) request.getAttribute(key);
	request.setAttribute("tituloPopup",mapaPopup.get("tituloPopup"));
	request.setAttribute("textoBusqueda",mapaPopup.get("textoBusqueda")); 
	request.setAttribute("urlRequest",(String)mapaPopup.get("urlRequest")); 
	request.setAttribute("coleccionPopup",mapaPopup.get("coleccionPopup"));
	request.setAttribute("campos",mapaPopup.get("campos"));
	request.setAttribute("referenciaHtml",mapaPopup.get("referenciaHtml"));
	request.setAttribute("referenciaPopup",mapaPopup.get("referenciaPopup"));
	request.setAttribute("referenciaPopup2",mapaPopup.get("referenciaPopup2"));
	request.setAttribute("filterPopUp",mapaPopup.get("filterPopUp"));
%>

<script type="text/javascript" language="JavaScript">
var popupClaseNombre;
$(document).ready(function() {	
	popupClaseNombre = $(".clase").val();
});
function buscarPopup(url, clase, filterPopUp) {
	var texto = $(".inputSearch."+clase).val();
	//agrego el valor de busqueda a la url	
	if(url.indexOf("?") != -1)
		url = url+'&val='+texto;
	else
		url = url+'?val='+texto;
	//Agrego para filtrar por un codigo.(Ejemplo Cliente)
	if(filterPopUp != null && filterPopUp != "")
		url = url+'&clienteCodigo='+filterPopUp;
		
	//realizo el request
	jQuery.ajax({
                url: url,
                success: function(data){
                   filteredResponse =  $(data).find(this.selector);
                   if(filteredResponse.size() == 1){
                        $(this).html(filteredResponse);                            
                   }else{
                        $(this).html(data);
                   }
                   $(this).displayTagAjax();
                } ,
                data: ({"time":new Date().getTime()}),
                context: $(".displayTagDiv."+clase)
            });
}
function cancelarPopup() {
	$(".urlBusqueda."+popupClaseNombre).val("");
	popupOffDiv($('.'+popupClaseNombre),'darkLayer');
}
function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}
</script>

<div class="darkMiddleClass <%=request.getParameter("clase")%>">
	<fieldset>
		<table width="100%">
			<thead>
				<tr>
					<th align="left" id="loginImg" >		       				
      					<font style="color:#003090"><spring:message code="${tituloPopup}" htmlEscape="true"/></font>    					
   					</th>
				</tr>
  			</thead>		
		</table>
       	<div class="displayTagDiv <%=request.getParameter("clase")%>" style="text-align: center;">     
       		<spring:message code="textos.buscar" htmlEscape="true"/>
       		<input type="hidden" class="clase" style="width: 300px;" value="<%=request.getParameter("clase")%>">
       		<input type="hidden" class="urlBusqueda <%=request.getParameter("clase")%>" style="width: 300px;" value="${urlRequest}">
       		<input type="text" class="inputSearch <%=request.getParameter("clase")%>" style="width: 300px;" value="${textoBusqueda}">&nbsp;&nbsp;
       		<button name="buscar" class="botonCentrado buttonSearch <%=request.getParameter("clase")%>" type="button" onclick="buscarPopup('${urlRequest}','<%=request.getParameter("clase")%>', '${filterPopUp}');">
				<img src="<%=request.getContextPath()%>/images/buscar.png"> 
				<spring:message code="textos.buscar" htmlEscape="true"/>								
			</button>
       		<br style="font-size: xx-small;"/>  	
       		<br style="font-size: xx-small;"/>  	
       		<div style="overflow: scroll; height: 200px;"> 	
			<display:table name="${coleccionPopup}" requestURI="${urlRequest}">
				<display:column class="hidden" headerClass="hidden">
					    <input type="hidden" id="hdn_referenceHtml" value="${referenciaHtml}"/>
	            </display:column>
				<c:forEach items="${campos}" var="campoDisplayTag">
					<c:if test="${campoDisplayTag.property == referenciaPopup}">
						<display:column property="${campoDisplayTag.property}" class="hidden hdn_reference" headerClass="hidden"/>
					</c:if>
					<c:if test="${campoDisplayTag.property == referenciaPopup2}">
						<display:column property="${campoDisplayTag.property}" class="hidden hdn_reference2" headerClass="hidden"/>
					</c:if>
					<c:if test="${campoDisplayTag.hidden == false}">
						<display:column property="${campoDisplayTag.property}" titleKey="${campoDisplayTag.titleKey}"/>
					</c:if>					
					<c:if test="${campoDisplayTag.hidden == true}">
						<display:column property="${campoDisplayTag.property}" sortable="true" headerClass="hidden" class="hidden"/>
					</c:if>					
				</c:forEach>  
			</display:table>
			</div>
			<br style="font-size: xx-small;"/>  	 	
			<button name="cancelar" type="button"  onclick="cancelarPopup();" class="botonCentrado">
				<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
				<spring:message code="botones.cancelar" htmlEscape="true"/>  
			</button>
		</div>
	</fieldset>
</div>