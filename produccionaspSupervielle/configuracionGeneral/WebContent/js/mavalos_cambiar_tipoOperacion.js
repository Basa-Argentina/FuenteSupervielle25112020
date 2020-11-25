var menuAbierto=false;

$(document).ready(function() {
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	 
  //binding OnBlur() para los campos con popup	
	
	$("#codigoTipoOperacion").blur(function(){
		getAjax('tipoOperacionServlet','codigo','codigoTipoOperacion',getTipoOperacionCallBack);
	});
	
	$("#buscaTipoOperacion").click(function(){
		abrirPopupSeleccion("popUpSeleccionTipoOperacion.html",null,null, null);
  	});
	
	if($("#codigoTipoOperacion").val() != null){
		getAjax('tipoOperacionServlet','codigo','codigoTipoOperacion',getTipoOperacionCallBack);
	}
});

function volver(){
	document.location="mostrarOperacion.html";
}
function volverCancelar(){
	document.location="mostrarOperacion.html";
}
function guardarYSalir(){
	document.location="cambiarTipoOperacion.html?accion=GUARDAR&id="+$('#id').val()+"&codigoTipoOperacion="+$('#codigoTipoOperacion').val();
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

//ajax
function getAjax(url, varName, elementName, callBack){
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
		$("#"+componentId+"Label").html(sResponseText);		
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
	}	
}

//ajax
function getTipoOperacionCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTipoOperacionLabel").html(sResponseText);
	else{
		$("#codigoTipoOperacionLabel").html("");
		$("#codigoTipoOperacion").val("");
	}
}