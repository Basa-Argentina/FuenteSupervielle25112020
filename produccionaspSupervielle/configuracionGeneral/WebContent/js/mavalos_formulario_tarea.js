	
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
	
	//Slide 1
	$('#busquedaDiv2').attr({'style':'display:block'});
	$('#busquedaImg2').click(function() {
		$('#busquedaDiv2').slideToggle('slideUp');
		$('#busquedaImgSrcDown2').slideToggle('slideUp');
		$('#busquedaImgSrc2').slideToggle('slideUp');
	});
	
	//Popups Busqueda
    $('#botonPopupUsuario').click(function(){
  	  abrirPopupUsuario();
    });
	
	$("#codigoUsuario").blur(function(){
		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
	});
	if($("#codigoUsuario").val() != null){
		getAjax('seleccionUsuario.html','codigo','codigoUsuario',getUsuarioCallBack);
	}
	
});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	document.location="mostrarTarea.html";
}
function volverCancelar(){
	document.location="mostrarTarea.html";
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
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresaLabel").html("");
	}
}

function abrirPopupUsuario(){
	var url = "popUpSeleccionUsuario.html?codigo="+$("#codigoEmpresa").val();
	jQuery.ajax({
        url: url,
        success: function(data){
           $(this).html(data);
           $(".displayTagDiv").displayTagAjax();
           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

//ajax
function getUsuarioCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoUsuarioLabel").html(sResponseText);
	else{
		$("#codigoUsuario").val("");
		$("#codigoUsuarioLabel").html("");
	}
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#codigoCliente").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
