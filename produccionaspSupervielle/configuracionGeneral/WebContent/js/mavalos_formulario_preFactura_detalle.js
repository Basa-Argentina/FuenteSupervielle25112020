var menuAbierto=true;

$(document).ready(function(){
		
	$("#descripcion").val($.trim($('#descripcion').val()));
	
	$('#descripcion').keydown(function() {
	    var longitud = $(this).val().length;
	    var resto = 255 - longitud;
	    if(resto <= 0){
	        $('#descripcion').attr("maxlength", 255);
	    }
	});
	
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
		window.location='precargaFormularioPreFactura.html?accionPreFactura='+$('#accionPreFactura').val();
	});
	if($('#accionPreFacturaDetalle').val()!="CONSULTAR"){
		$('#botonGuardar').click(function(){
			document.forms[0].submit();		
		});
		
		//servlets blur
		$("#codigoConcepto").blur(function(){
			getAjaxConceptoFacturableAlicuotaYDescripcionServlet();
		});
		$("#listaPreciosCodigo").blur(function(){
			codigoConceptoText =  $('#codigoConcepto').val();
			if(codigoConceptoText!=null && codigoConceptoText!=undefined && codigoConceptoText.length>0){
				getAjaxListaPrecios();
			}else{
				jAlert("TODO: implementar mensaje ingrece un concepto valido");
			}
		});
		
		$('#cantidad').blur(function(){
			calcularTotales();
		});
		//botones busqueda popups
		$('#botonBuscarConceptoFacturable').click(function(){ abrirpopupConceptoFacturable(); });
		
		$('#botonBuscarListaPrecios').click(function(){
			codigoConceptoText = $('#codigoConcepto').val();
			if(codigoConceptoText!=null && codigoConceptoText!=undefined && codigoConceptoText.length>0){ 
				abrirpopupListaPrecios();
			}else{
				jAlert("TODO: implementar mensaje ingrece un concepto valido");
			}
		});
	}
	
	if($('#descripcion').val()==null || $('#descripcion').val().length==0 || $('#descripcion').val()==""){
		$('#descripcion').val("Descripción:");
	}
	
	$('#descripcion').focus(function(){
		if($('#descripcion').val()=="Descripción:"){
			$('#descripcion').val('');
		}
	});
	
	
});

function abrirpopupConceptoFacturable(){
	
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
}

function abrirpopupListaPrecios(){
	
	var url = "popUpSeleccionListaPreciosPorConceptoFacturable.html?habilitado=true&codigoClienteEmp="+$('#codigoClienteEmp').val()
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

function getCodigoListaPreciosCodigoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		var vec = sResponseText.split(';*;');
		var descripcion = vec[1];
		var precio = vec[0];
		var precioDec = precio *1 / 1;
		$("#listaPreciosCodigoLabel").html(descripcion);
		$('#precioLabel').html("$ "+precioDec.toFixed(2));
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
		$("#descripcion").val(vec[0]);	
		$("#listaPreciosCodigoLabel").html('');
		$("#listaPreciosCodigo").val("");
		
		$("#alicuotaLabel").html(vec[1] + ' / ' + vec[3] + " %");	
		$("#alicuota").val(vec[3]);
		calcularTotales();
	}else{
		$("#listaPreciosCodigoLabel").html("");
		$("#listaPreciosCodigo").val("");
		$("#descripcion").val('');
		$("#codigoConcepto").html('');
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
		
		if($('#idAfipTipoComprobante').val() == '3'){
			cantidad = -cantidad;	
		}
		
		
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
		var impuestosPositivos = impuestos*-1
		$('#netoTotal').html("$ "+netoTotal.toFixed(2));
		$('#impuestos').html("$ "+impuestosPositivos.toFixed(2));
		$('#finalTotal').html("$ "+finalTotal.toFixed(2));
	}
} 
