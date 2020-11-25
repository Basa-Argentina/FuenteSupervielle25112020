var menuAbierto=false;

$(document).ready(function() {
	
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	
      
      $('#botonPopupLectura').click(function(){
    	 abrirPopupLectura(); 
      });
    
            
    
      $("#codigoLectura").blur(function(){
  		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
  		});
    	
    	if($("#codigoLectura").val() != null){
    		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
    	}
    	
    	$('#pop').hide();

    	
});




function guardar(){
	//alert($('#pagesize').val());
//	jAlert($('#pagesize').val());
//	if($('#pagesize').val() == 'Todos'){
//		jConfirm(mensaje, 'Ha seleccionado "Todos" y esto puede demorar mucho tiempo, está seguro de continuar??',function(r) {
//		    if(r){
//		    	document.forms[0].submit();
//		    }
//		});
//	}else{
		document.forms[0].submit();
	//}
}
function borrarFiltros(){
	document.location="borrarFiltrosElemento.html";
}
function volver(){
	document.location="menu.html";
}




function abrirPopupLectura(){
	var url = "popUpSeleccionLectura.html?codigo="+$("#codigoEmpresa").val();
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
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getAjaxConParent(url, varName, elementName, parentId,callBack){

		var input = document.getElementById(elementName);
		if(input == null)
			return;
		var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()+'&empresaId='+$("#"+parentId+"").val();	
		var request = new HttpRequest(url, callBack);
		request.send();
	
}

//ajax
function getLecturaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoLecturaLabel").html(sResponseText);
	else{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}
}

function getValidadorElemntoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		jConfirm('El elemento contiene referencias, desea eliminarlo?', 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarElemento.html?id="+sResponseText;
		    }
		});
	
	}
}