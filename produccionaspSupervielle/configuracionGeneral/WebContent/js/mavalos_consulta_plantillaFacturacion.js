var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#plantillaFacturacion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#plantillaFacturacion tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	
	//Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	
	$('#botonPopupEmpresa').click(function(){
  	  abrirPopupEmpresa();
    });
    
    $('#botonPopupSucursales').click(function(){
  	  abrirPopupSucursal();
    });
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
    $('#plantillaFacturacion tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_id').val());
        },

        'modificar': function(t) {
          modificar($(t).find('#hdn_id').val());

        },

        'eliminar': function(t) {

      	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());

        }

      },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#plantillaFacturacion tbody tr").removeClass('tr_mouseover');
		}

    }); 
    //Inicio menu
  	$("ul.topnav li").hover(
  		function() {
  			$(this).addClass("hover");
  			$(this).find("ul.subnav").slideDown('fast').show(); //Drop down the subnav on click  
  		}, 
  		function(){  
  			$(this).removeClass("hover");
  			$(this).find("ul.subnav").slideUp('fast'); //When the mouse hovers out of the subnav, move it back up  
  		}
      );
  	
  	$('#tipoComprobanteId').change(function(){
		$('#codigoSerie').blur();
	});
  	
  //Fin menu
  	//binding OnBlur() para los campos con popup	
  	$("#clienteCodigo").blur(function() {
		getAjax('clientesServlet', 'codigo', 'clienteCodigo',getClienteCallBack);
	});
	if ($("#clienteCodigo").val() != null) {
		getAjax('clientesServlet', 'codigo','clienteCodigo',getClienteCallBack);
	}
	$("#codigoSerie").blur(function() {
		getAjaxCodigoSerie();
	});
	if ($("#codigoSerie").val() != null && $("#codigoSerie").val() != "") {
		getAjaxCodigoSerie();
	}
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionClienteEmpresaDefecto.html",null,null, null);
  	});
	
	$("#buscaListaPrecio").click(function(){
		abrirPopupSeleccion("popUpSeleccionListaPrecios.html",null,null, null);
  	});
	if ($("#listaPreciosCodigo").val() != null && $("#listaPreciosCodigo").val() != "") {
		getAjaxConParent('listasPreciosServlet','codigo','listaPreciosCodigo','listaPreciosCodigo',getListaPreciosCallBack);
	}
	$("#listaPreciosCodigo").blur(function() {
		getAjaxConParent('listasPreciosServlet','codigo','listaPreciosCodigo','listaPreciosCodigo',getListaPreciosCallBack);
	});
	$("#buscaSerie").click(function(){
		error = $('#error').val();
		if($('#tipoComprobanteId').val() == null || $('#tipoComprobanteId').val() == "" || $('#tipoComprobanteId').val()=="0"){
			jAlert($('#errorSeleccioneTipoComprobante').val(),error);
			return;
		}
		abrirPopupPorSerie("popUpSerie.html",null,'tipoComprobanteId', null);
  	});
  		
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
	
	$(".inputTextNumericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	
	$('#agregar').click(function(){
	    	document.location='iniciarPrecargaFormularioPlantillaFacturacion.html';
	});
	
});

function buscarFiltro(){
	document.forms[0].submit();
}

function consultar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioPlantillaFacturacion.html?accion=CONSULTA&id="+id;
}
function modificar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioPlantillaFacturacion.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarPlantillaFacturacion.html?id="+id;
		    }
		});
	}
}

function volver(){
	document.location="menu.html";
}

function abrirPopupPorCliente(url,inputValue,nombreClase, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		url = url+"?clienteCodigoString="+$("#"+inputValue).val();
	}
	if(nombreClase){
		if(url.indexOf("?") != -1)
			url = url+'&codigoEmpresa='+$("#"+nombreClase).val();
		else
			url = url+'?codigoEmpresa='+$("#"+nombreClase).val();
	}
	
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
function getAjax(url, varName, elementName, callBack) {
	var input = document.getElementById(elementName);
	if (input == null)
		return;
	var url = url + '?' + varName + '=' + input.value + '&clienteId='
			+ $("#clienteId").val();
	var request = new HttpRequest(url, callBack);
	request.send();
}

//ajax
function getAjaxConParent(url, varName, elementName, parentId,callBack){
	if($("#"+parentId+"").val()!=null && $("#"+parentId+"").val()!=""){
		var input = document.getElementById(elementName);
		if(input == null)
			return;
		var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()+'&sucursalId='+$("#"+parentId+"").val()+'&clienteEmpId='+$("#"+parentId+"").val()+'&'+parentId+'='+$("#"+parentId+"").val()+'&tipoSerie=R'+'&habilitado=true';	
		var request = new HttpRequest(url, callBack);
		request.send();
	}
}

//ajax
function getClienteCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#clienteCodigoLabel").html(sResponseText);
	} else {
		$("#clienteCodigo").val("");
		$("#clienteCodigoLabel").html("");
	}
}

//ajax
function getListaPreciosCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#listaPreciosCodigoLabel").html(sResponseText);
	} else {
		$("#listaPreciosCodigo").val("");
		$("#listaPreciosCodigoLabel").html("");
	}
}

// ajax
function getCodigoSerieCallBack(sResponseText) {
	if (sResponseText != 'null' && sResponseText != "") {
			var array = sResponseText.split(";*;");
			$("#codigoSerieLabel").html(array[0]);
		} else {
			$("#codigoSerie").val("");
			$("#codigoSerieLabel").html("");
		}
}

//ajax
function setResponce(sResponseText, componentId) {
	if (sResponseText != 'null' && sResponseText != "") {
		$("#" + componentId + "Label").html(sResponseText);
	} else {
		$("#" + componentId).val("");
		$("#" + componentId + "Label").html("");
	}
}

function abrirPopupPorSerie(url,inputValue,nombreClase, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		if(url.indexOf("?") != -1)
			url = url+'&codigo='+$("#"+inputValue).val();
		else
			url = url+'?codigo='+$("#"+inputValue).val();
		
	}
	if(nombreClase){
		if(url.indexOf("?") != -1)
			url = url+'&idAfipTipoComprobante='+$("#"+nombreClase).val();
		else
			url = url+'?idAfipTipoComprobante='+$("#"+nombreClase).val();
	}
	
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

function getAjaxCodigoSerie(){
	codigoEmpresa =  $("#codigoEmpresa").val();
	idAfipTipoComprobante = $('#tipoComprobanteId').val();
	codigoClienteEmp = $('#codigoCliente').val();
	codigoSerie = $('#codigoSerie').val();
	error = $('#error').val();
	
	if(codigoEmpresa ==null || codigoEmpresa == ""){
		jAlert($('#errorSeleccioneEmpresa').val(),error);
		return false;
	}
	if(idAfipTipoComprobante == null || idAfipTipoComprobante == ""){
		jAlert($('#errorSeleccioneTipoComprobante').val(),error);
		return;
	}

	var url = "serieServletParaFactura2";
	jQuery.ajax({
        url: url,
        success: function(data){
           getCodigoSerieCallBack(data);
           popupOffDiv($('#pop'),'darkLayer');            
        },
        type: "GET",
        timeout:120000,
        data: "&codigoEmpresa=" + codigoEmpresa
			+"&idAfipTipoComprobante=" + idAfipTipoComprobante
			+"&codigoCliente=" + codigoClienteEmp
			+"&codigo=" + codigoSerie,
        context: $(".selectorDiv"),
        dataType: "html",
        beforeSend:function(){
        	popupOnDiv($('#pop'),'');
        },
        error:function(){
        	jAlert("Ha ocurrido un error...");
        	popupOffDiv($('#pop'),'');
        }
    });
}