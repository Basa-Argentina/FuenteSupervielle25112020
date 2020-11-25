var codigoLecturaValido = false;

$(document).ready(function() {
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	$('#codigoLectura').numeric({ decimal: false, negative: false});
	
	$('#cargarLectura').click(function(){
		if(codigoLecturaValido){
			$('#cargarLectura').attr('disabled', 'disabled');
			$('#botonGuardar').attr('disabled', 'disabled');
			var liberado = false;
			if($('#libera').attr('checked'))
				{liberado = true;}
			//document.location="mostrarReposicionamiento.html?libera="+liberado;
			$('#formulario').attr('action', 'mostrarReposicionamiento.html');
			document.forms[0].submit();	
		}else{
			jAlert($('#errorCodigoLecturaInvalido').val());
		}
	});
	
	$("#buscaLectura").click(function(){
		abrirPopupLecturaNuevo();
  	});
	
	$("#codigoLectura").blur(function(){
		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
	});
	
	//Validaciones y botones
	$('.completarZeros').blur(function(e){
		valorSinZeros='';
		valorSinZeros=$(this).val();
		zerosIzq='';
		maximo=$(this).attr('maxlength');
		actual=valorSinZeros.length;
		for(i=0;i<(maximo-actual);i++){
			zerosIzq+='0';
		}
		
		$(this).val(zerosIzq+valorSinZeros);
	});
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	$('#botonCancelar').click(function(){
		volver();
	});
	
	$('#botonGuardar').click(function(){
		if(codigoLecturaValido){
			$('#botonGuardar').attr('disabled', 'disabled');
			$('#formulario').attr('action', 'guardarReposicionamiento.html');
			document.forms[0].submit();
		}else{
			jAlert($('#errorCodigoLecturaInvalido').val());
		}
	});
	
	$("#codigoLectura").blur();
});



//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()/*+'&utilizada=false'*/;	
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function abrirPopupLectura(claseNom){
	var url = "mostrarReposicionamiento.html";
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

function abrirPopupLecturaNuevo(){

	var url = "popUpSeleccionLectura.html?codigo="+$("#codigoEmpresa").val()/*+"&utilizada=false"*/;
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
function getLecturaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		
		$("#codigoLecturaLabel").html(sResponseText);
		$('#botonGuardar').removeAttr('disabled');
		$('#cargarLectura').removeAttr('disabled');
		codigoLecturaValido=true;
		
	}else{
		
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
		codigoLecturaValido=false;
	}	
}

//boton volver
function volver(){
	document.location="menu.html";
} 