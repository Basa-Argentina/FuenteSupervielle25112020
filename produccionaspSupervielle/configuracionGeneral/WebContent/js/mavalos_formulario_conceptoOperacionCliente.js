var menuAbierto=true;

$(document).ready(function(){
		
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
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	//Validaciones sobre formato de codigos
	$('.completarZeros').blur(function(e){
		valorSinZeros='';
		valorSinZeros=$(this).val();
		zerosIzq='';
		if(valorSinZeros!=null && valorSinZeros!=''){
			maximo=$(this).attr('maxlength');
			actual=valorSinZeros.length;
			for(i=0;i<(maximo-actual);i++){
				zerosIzq+='0';
			}
		}
		$(this).val(zerosIzq+valorSinZeros);		
	});
	
	$('#botonCancelar').click(function(){
		document.location="mostrarConceptoOperacionClienteSinFiltrar.html";
	});
	
		
	$('#botonGuardar').click(function(){
		document.forms[0].submit();		
	});
	
	//servlets blur
	$("#codigoConcepto").blur(function(){
		if($('#accion').val() == "NUEVO"){
			getAjaxConceptoFacturableAlicuotaYDescripcionServlet();
		}
	});
	
	$('#codigoCliente').blur(function(){
		getAjaxCodigoClienteEmp();
	});
	
	$('#precio').blur(function(){
		if($('#cantidad').val() != null && $('#cantidad').val()!="" && $('#cantidad').val().length > 0)
		{
			calcularTotales();
		}
	});
	
	$("#listaPreciosCodigo").blur(function(){
		codigoConceptoText =  $('#codigoConcepto').val();
		codigoClienteText = $('#codigoCliente').val();
//		if(codigoClienteText!=null && codigoClienteText!=undefined && codigoClienteText.length>0){
//			if(codigoConceptoText!=null && codigoConceptoText!=undefined && codigoConceptoText.length>0){
				if($('#accion').val() == "NUEVO"){
					getAjaxListaPrecios();
				}
//			}else{
//				jAlert("Debe ingresar previamente un concepto facturable.");
//			}
//		}else{
//			jAlert("Debe ingresar previamente un cliente.");
//		}
	});
	
	$('#cantidad').blur(function(){
		calcularTotales();
	});
	//botones busqueda popups
	$('#botonBuscarConceptoFacturable').click(function(){ abrirpopupConceptoFacturable(); });
	
	
	$('#botonBuscarListaPrecios').click(function(){
		codigoConceptoText = $('#codigoConcepto').val();
		codigoClienteText = $('#codigoCliente').val();
		if(codigoClienteText!=null && codigoClienteText!=undefined && codigoClienteText.length>0){
			if(codigoConceptoText!=null && codigoConceptoText!=undefined && codigoConceptoText.length>0){ 
				abrirpopupListaPrecios();
			}else{
				jAlert("Debe ingresar previamente un concepto facturable.");
			}
		}else{
			jAlert("Debe ingresar previamente un cliente.");
		}
	});
	
	calcularTotales();
		
	
});

function abrirpopupConceptoFacturable(){
	
	if($('#codigoCliente').val()!=null && $('#codigoCliente').val()!=undefined && $('#codigoCliente').val().length>0){ 
		
		var url = "popUpSeleccionConceptoFacturable.html?habilitado=true";
		
		jQuery.ajax({
	        url: url,
	        success: function(data){
	           $(this).html(data);
	           $(".displayTagDiv").displayTagAjax();
	           var div = $(this).find('.darkMiddleClass');
	           div.css('position','absolute');
	           
	           popupOnDivInsideFancybox(div);
	        },
	        data: ({"time":new Date().getTime()}),
	        context: $(".selectorDiv")
	    });
	}else{
		jAlert("Debe ingresar previamente un cliente.");
	}
	
	
}

function abrirpopupListaPrecios(){
	
	var url = "popUpSeleccionListaPreciosPorConceptoFacturable.html?habilitado=true&codigoClienteEmp="+$('#codigoCliente').val()
		+ "&codigoEmpresa="+$('#codigoEmpresa').val() + "&codigoConceptoFacturable="+$('#codigoConcepto').val();
	
	jQuery.ajax({
        url: url,
        success: function(data){
           $(this).html(data);
           $(".displayTagDiv").displayTagAjax();
           var div = $(this).find('.darkMiddleClass');
           div.css('position','absolute');
           
           popupOnDivInsideFancybox(div);
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}

function popupOnDivInsideFancybox(divOpen){
	var left=($(window).width()-$(divOpen).width())/2;
	var top = ($(window).height()-$(divOpen).height())/2;
	$(divOpen).css("left",left);
	$(divOpen).css("top",top);
	$(divOpen).show("slow");
}

//ajax
function getAjaxConceptoFacturableAlicuotaYDescripcionServlet(){
	var input = document.getElementById('codigoConcepto');
	if(input == null)
		return;
	var clienteId = $('#clienteAspId').val();
	var url = 'conceptoFacturableAlicuotaYDescripcionServlet'+'?mode=0&tipo=0&habilitado=true&codigo='+input.value+'&clienteId='+clienteId;	
	var request = new HttpRequest(url, getConceptoFacturableAlicuotaCallBack);
	request.send();	
}

function getAjaxListaPrecios(url, varName, elementName, callBack){
	var input = document.getElementById('listaPreciosCodigo');
	if(input == null)
		return;
	var url = 'listasPreciosPrecioYDescripcionServlet?habilitado=true&codigoConceptoFacturable='+$('#codigoConcepto').val()
		+'&codigo='+$('#listaPreciosCodigo').val()
		+'&clienteId='+$("#clienteAspId").val()
		+'&codigoClienteEmp='+$('#codigoClienteEmp').val()
		+'&codigoEmpresa='+$('#codigoEmpresa').val();
	var request = new HttpRequest(url, getCodigoListaPreciosCodigoCallBack);
	request.send();	
}


function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteAspId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

//llamadas a servlets
function getAjaxCodigoClienteEmp(){
	var input = document.getElementById('codigoCliente');
	var codigoEmpresa = $('#codigoEmpresa').val();
	if(input == null || codigoEmpresa == null)
		return;
	
	var url = 'clientesPorEmpresaServlet'+'?codigo='+input.value+'&clienteId='+$("#clienteAspId").val()+'&codigoEmpresa='+codigoEmpresa+'&habilitado=true';	
	var request = new HttpRequest(url, getCodigoClienteEmpCallBack);
	request.send();	
}

function getCodigoListaPreciosCodigoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		var vec = sResponseText.split(';*;');
		var descripcion = vec[1];
		var precio = vec[0];
		$("#listaPreciosCodigoLabel").html(descripcion);
		$('#precioLabel').html("$");
		$('#precio').val(precio);
		calcularTotales();
	}else{	
		$("#listaPreciosCodigoLabel").html("");
		$("#listaPreciosCodigo").val("");
	}
}

function getConceptoFacturableAlicuotaCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		var vec = sResponseText.split(";*;");
		$("#codigoConceptoLabel").html(vec[0]);	
		$("#listaPreciosCodigoLabel").html('');
		$("#listaPreciosCodigo").val("");
		
		$("#alicuotaLabel").html(vec[1] + ' / ' + vec[3] + " %");	
		$("#alicuota").val(vec[3]);
		calcularTotales();
	}else{
		$("#listaPreciosCodigoLabel").html("");
		$("#listaPreciosCodigo").val("");
		$("#codigoConceptoLabel").html('');
		$("#codigoConcepto").val("");
		$("#alicuotaLabel").html('');	
		$("#alicuota").val('0');
	}
}

function calcularTotales(){
	alicuota = $('#alicuota').val();
	precio = $('#precio').val();
	cantidad = $('#cantidad').val();
	if(alicuota!=null && alicuota!=undefined && alicuota.length>0 &&
			precio!=null && precio!=undefined && precio.length>0 &&
			cantidad!=null && cantidad!=undefined && cantidad.length>0 ){
		
		//METODO DE PRECIOS CON IMPUESTOS
//		var netoUnitario = (precio) / (1+(alicuota/100));
//		var netoTotal = (precio) / (1+(alicuota/100)) * cantidad;
//		var impuestoUnitario = precio - netoUnitario;
//		var impuestos = impuestoUnitario*cantidad;//(precio) * (1/alicuota) * cantidad;
//		var finalTotal = netoTotal + impuestos;
		
		//METODO DE PRECIOS SIN IMPUESTOS
		var finalUnitario = (precio) * (1+(alicuota/100));
		var finalTotal = (precio) * (1+(alicuota/100)) * cantidad;
		var impuestoUnitario = precio - finalUnitario;
		var impuestos = impuestoUnitario*cantidad;
		var netoTotal = finalTotal + impuestos;
		var impuestosPositivos = impuestos*-1;
		
		$('#netoTotal').html("$ "+netoTotal.toFixed(2));
		$('#impuestos').html("$ "+impuestosPositivos.toFixed(2));
		$('#finalTotal').html("$ "+finalTotal.toFixed(2));
	}
} 

function abrirPopupNuevoClienteEmp(){
	if($("#codigoEmpresa").val()==""){
		var tittle = $('#tituloError').val();
		var message =$('#codigoEmpresaRequerido').val(); 
		jAlert(message,tittle);
		return false;
	}
	codigoEmpresa =  $("#codigoEmpresa").val();
	var url = "popUpSeleccionCliente.html?"
		+ "&codigo=" + codigoEmpresa;
	
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

function getCodigoClienteEmpCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClienteLabel").html(sResponseText);
	}else{			
		$("#codigoCliente").val("");
		$("#codigoClienteLabel").html("");	
		$("#codigoConcepto").val("");
		$("#codigoConcepto").blur();
	}
}

function volver(){
	document.location="mostrarConceptoOperacionClienteSinFiltrar.html";
}
