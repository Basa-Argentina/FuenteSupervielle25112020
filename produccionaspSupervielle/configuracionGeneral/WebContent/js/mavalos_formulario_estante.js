$(document).ready(function() {		
	var urlParams = {};
	(function () {
	    var e,
	        a = /\+/g,  // Regex for replacing addition symbol with a space
	        r = /([^&=]+)=?([^&]*)/g,
	        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
	        q = window.location.search.substring(1);

	    while (e = r.exec(q))
	       urlParams[d(e[1])] = d(e[2]);
	})();
	$("#observacion").val($.trim($('#observacion').val()));

	//Tooltips
	$("img[title]").tooltip();
	
	//Slide 1
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//Slide 1
	$('#grupoDiv').attr({'style':'display:block'});
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
	//binding OnBlur() para los campos con popup
	$("#codigoTipoJ").blur(function(){
		getAjax('tipoJerarquiaServlet','codigo','codigoTipoJ',getCodigoTipoJCallBack);
  	});

});

//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	var accion = document.getElementById("accion");
	var accionGrupo = document.getElementById("accionGrupo");
	document.location="volverFormularioGrupo.html?accionGrupo="+accionGrupo.value+"&accion="+accion.value;
}
function volverCancelar(){
	var accion = document.getElementById("accion");
	var accionGrupo = document.getElementById("accionGrupo");
	document.location="volverFormularioGrupo.html?accionGrupo="+accionGrupo.value+"&accion="+accion.value;
}
function guardarYSalir(){
 	document.forms[0].submit();
}

function abrirPopupGeneral(claseNom, url){
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
        context: $(".displayTagDiv."+ claseNom)
    });
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}
//ajax
function getAjaxCliente(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function setResponce(sResponseText, componentId){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");
		$("#"+componentId).val(array[0]);		
		$("#"+componentId+"Label").html(array[1]);		
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
	}	
}

//ajax
function getCodigoTipoJCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTipoJLabel").html(sResponseText);
	else{
		$("#codigoTipoJLabel").html("");
	}
}

