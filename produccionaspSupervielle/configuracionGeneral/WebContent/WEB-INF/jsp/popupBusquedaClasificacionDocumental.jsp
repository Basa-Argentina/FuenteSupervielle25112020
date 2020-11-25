<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="cstmTag" tagdir="/WEB-INF/tags" %>

<% 	
	String key = (String)request.getAttribute("mapa");
	String clase = (String)request.getAttribute("clase");
	java.util.Map<String,Object> mapaPopup = (java.util.Map<String,Object>) request.getAttribute(key);
	request.setAttribute("tituloPopup",mapaPopup.get("tituloPopup"));
	request.setAttribute("coleccionPopup",mapaPopup.get("coleccionPopup"));
	request.setAttribute("referenciaHtml",mapaPopup.get("referenciaHtml"));//id del input del padre
%>
<script type="text/javascript" src="js/simpletreemenu.js"></script>
<script type="text/javascript" language="JavaScript">
var popupClaseNombre;
$(document).ready(function() {	
	
	popupClaseNombre = $("#hdn_clase").val();
	ddtreemenu.createTree("treemenu", false);
	$('#bsqArbol').keyup(function(){
		$('.txtArbol').removeClass('nodoArbolSeleccionado');
		if($('#bsqArbol').val()!=''){
			ddtreemenu.flatten('treemenu', 'contact');//'expand');
			$('.txtArbol:contains("'+$('#bsqArbol').val()+'")').addClass('nodoArbolSeleccionado');
			$('.nodoArbolSeleccionado').closest('ul').not("#treemenu").each(function(ind,nodo){
				ddtreemenu.expandSubTree('treemenu', nodo );
			});
		}
	});
	
	var idCompHtml = $('#hdn_referenceHtml').val();
	$('.txtArbol').dblclick(function(){
		var nodo   = $(this).siblings('#hdn_nodo').val();
		//if(nodo=='I'){
	    	var codigo = $(this).siblings('#hdn_codigo').val();
	    	var nombre = $(this).siblings('#hdn_nombre').val();
	    	$("#"+idCompHtml).val(codigo);   
	    	//$("#"+idCompHtml+"Label").html(nombre); //dejar que se refresque por el blur del código
	    	popupOffDiv($('.'+popupClaseNombre),'darkLayer');
	    	$("#"+idCompHtml).blur();
    	//}
    });	
    
	$(document).keydown(function(event){
		if (event.keyCode == '27') {
			cancelarPopup();
		}	  
	});

});

function cancelarPopup() {
	popupOffDiv($('.'+popupClaseNombre),'darkLayer');
	var idCompHtml = $('#hdn_referenceHtml').val();
	$("#"+idCompHtml).focus();
}
</script>
<div class="darkMiddleClass ${clase}">
	<fieldset>
		<input type="hidden" id="hdn_referenceHtml" value="${referenciaHtml}"/>
		<input type="hidden" id="hdn_clase" value="${clase}"/>
		<table width="100%">
			<thead>
				<tr>
					<th align="left" id="loginImg" >		       				
      					<font style="color:#003090"><spring:message code="${tituloPopup}" htmlEscape="true"/></font>    					
   					</th>
				</tr>
  			</thead>		
		</table>
       	<div class="displayTagDiv ${clase}" style="text-align: center;">     
       		<spring:message code="formularioClasificacionDocumental.busquedaClasificacion" htmlEscape="true"/> 
			<input type="text" 
				id="bsqArbol" 
				title="<spring:message code="formularioClasificacionDocumental.busquedaClasificacionTooltip" htmlEscape="true"/>"
			/>
       		&nbsp;&nbsp;
       		<br style="font-size: xx-small;"/><br style="font-size: xx-small;"/>  	
       		<div style="overflow: auto;height: 200px;text-align: left;" align="left" id="tdArbol">
				<ul id="treemenu" class="treeview" style="width: 500px;">
					<cstmTag:clasificacionDocumentalNodo listaClasificacionDocumental="${coleccionPopup}"/>
		 		</ul>
	 		</div>
			<br style="font-size: xx-small;"/>  	 	
			<button name="cancelar" type="button"  onclick="cancelarPopup();" class="botonCentrado">
				<img src="<%=request.getContextPath()%>/images/cancelar.png"> 
				<spring:message code="botones.cancelar" htmlEscape="true"/>  
			</button>
		</div>
	</fieldset>
</div>