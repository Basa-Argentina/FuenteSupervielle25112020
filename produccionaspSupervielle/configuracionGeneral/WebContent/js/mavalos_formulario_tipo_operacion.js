$(document).ready(function() {	

	//Tooltips
	$("img[title]").tooltip();
	
	//Slide 1
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	//binding para ocultar datos opcionales
	$('#generaOperacionAlCerrarse').click(function(){
		if($('#generaOperacionAlCerrarse:checked').val() == 'on')
			$('.generaOperacionAlCerrarse').slideDown('slow').show();
		else{
			$('.generaOperacionAlCerrarse').slideUp('slow');
			$('#tipoOperacionSiguienteCod').val('');
			$('#tipoOperacionSiguienteCod').blur();
		}	
			
	});	
	//binding para ocultar datos opcionales
	$('#imprimeDocumento').click(function(){
		if($('#imprimeDocumento:checked').val() == 'on')
			$('.imprimeDocumento').slideDown('slow').show();
		else{
			$('.imprimeDocumento').slideUp('slow');
			$('#tituloDocumento').val('');
			$('#serieDocumentoCod').val('');
			$('#serieDocumentoCod').blur();
		}	
	});
	//binding para ocultar datos opcionales
	$('#imprimeRemito').click(function(){
		if($('#imprimeRemito:checked').val() == 'on')
			$('.imprimeRemito').slideDown('slow').show();
		else{
			$('.imprimeRemito').slideUp('slow');
			$('#serieRemitoCod').val('');
			$('#serieRemitoCod').blur();
		}	
	});	
	//binding OnBlur() para los campos con popup
	$("#conceptoFacturableCod").blur(function(){
		getAjax('conceptoFacturableServlet','codigo','conceptoFacturableCod',getConceptoCallBack);
	});
	$("#tipoRequerimientoCod").blur(function(){
		getAjax('tipoRequerimientoServlet','codigo','tipoRequerimientoCod',getTipoRequerimientoCallBack);
	});
	$("#tipoOperacionSiguienteCod").blur(function(){
		getAjax('tipoOperacionServlet','codigo','tipoOperacionSiguienteCod',getTipoOperacionCallBack);
	});
	$("#serieDocumentoCod").blur(function(){
		getAjaxSerie('serieDescripcionPrefijoServlet','codigo','serieDocumentoCod','I',getSerieCallBack);
	});
	$("#serieRemitoCod").blur(function(){
		getAjaxSerie('serieDescripcionPrefijoServlet','codigo','serieRemitoCod','R',getSerieRemitoCallBack);
	});
});

//ajax 
function getAjax(url, varName, elementId, callBack){
	var input = document.getElementById(elementId);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax 
function getAjaxSerie(url, varName, elementId, tipoSerie,callBack){
	var input = document.getElementById(elementId);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()+'&tipoSerie='+tipoSerie+'&habilitado=true';	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getConceptoCallBack(sResponseText){	
	var componentId = "conceptoFacturableCod";
	setResponce(sResponseText, componentId);
}
//ajax
function getTipoRequerimientoCallBack(sResponseText){	
	var componentId = "tipoRequerimientoCod";
	setResponce(sResponseText, componentId);
}
//ajax
function getTipoOperacionCallBack(sResponseText){	
	var componentId = "tipoOperacionSiguienteCod";
	setResponce(sResponseText, componentId);
}
//ajax
function getSerieCallBack(sResponseText){	
	var componentId = "serieDocumentoCod";
	setResponce(sResponseText, componentId);
}
//ajax
function getSerieRemitoCallBack(sResponseText){	
	var componentId = "serieRemitoCod";
	setResponce(sResponseText, componentId);
}
//ajax
function setResponce(sResponseText, componentId){
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split("-");
		$("#"+componentId+"Label").html(array[0]);		
	}else{
		$("#"+componentId).val("");
		$("#"+componentId+"Label").html("");
	}	
}

//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarTipoOperacion.html";
}
function volverCancelar(){
	document.location="mostrarTipoOperacion.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
}