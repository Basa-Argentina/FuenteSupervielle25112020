var menuAbierto=false;

$(document).ready(function(){
	
	//tabla Facturas
	$("#factura tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#factura tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});	
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
	//PopUp click derecho
    $('#factura tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_id').val());
        },

        'modificar': function(t) {
          modificar($(t).find('#hdn_id').val());

        },

        'eliminar': function(t) {

      	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());

        },
        
        'anular': function(t) {
            anular($(t).find('#hdn_anular').val(),$(t).find('#hdn_id').val());
          }

      },
		onShowMenu: function(e, menu) {
			var cell = e.currentTarget.children[0].children[0].value;
			if(cell == 'ANULADO'){
				menuAbierto=true;
				$('#anular', menu).remove();
				return menu;
			}else{
				menuAbierto=true;
				return menu;
			}

		},
		onHide: function(){
			menuAbierto=false;
			$("#factura tbody tr").removeClass('tr_mouseover');
		}

    }); 
  //Busqueda
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	//Checkbox
	$("input[name=checktodos]").change(function(){
		$('input[type=checkbox]').each( function() {			
			if($("input[name=checktodos]:checked").length == 1){
				this.checked = true;
			} else {
				this.checked = false;
			}
		});
	});
	
	$('#imprimirFacturas').click(function(){
		
	 	var allVals = [];
	    $("input[class='checklote']:checked").each(function() {
	    	allVals.push($(this).val());
	    });
	    if(allVals.length == 0)
	    {
	    	jAlert('Debe seleccionar al menos una factura para imprimir.');
			return;
		}
	    else
		{
	    	window.open('imprimirFactura.html?&seleccionados='+allVals,'');
	    }
	});
	
	//botones
	$('#botonCargar').click(function(){
		$(this).attr('disabled', 'disabled');
		codEmpresa = $('#codigoEmpresa').val();		
		if(codEmpresa != null && codEmpresa!=""){
			document.forms[0].submit();		;
		}else{			
			var tittle = $('#tituloError').val();
			var message =$('#codigoEmpresaRequerido').val(); 
			jAlert(message,tittle);
			$('#botonCargar').removeAttr('disabled');
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
	//Fin menu
	
	
	$('#botonCargar').removeAttr('disabled');
	
	$('#tipoMovimientoInt').val($('#tipoMovimientoSelected').val());
	
	$('#botonAgregar').click(function(){
		$(this).attr('disabled', 'disabled');
		document.location="iniciarPrecargaFormularioFactura.html?accion=NUEVO";
	});
	
	$('#botonAgregar').removeAttr('disabled');
	
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
	
	//llamadas a los servlets
	$("#codigoEmpresa").blur(function(){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	});
	if($("#codigoEmpresa").val() != null){
		getAjax('empresasServlet','codigo','codigoEmpresa',getEmpresaCallBack);
	}
	$("#codigoSucursal").blur(function(){
		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
	});
	if($("#codigoSucursal").val() != null){
		getAjax('sucursalesServlet','codigo','codigoSucursal',getSucursalCallBack);
	}
	$("#codigoSerie").blur(function(){
		getAjax('serieServletPorCodigo','codigo','codigoSerie',getSerieCallBack);
	});
	if($("#codigoSerie").val() != null){
		getAjax('serieServletPorCodigo','codigo','codigoSerie',getSerieCallBack);
	}
	
	$('#codigoCliente').blur(function(){
		getAjax('clientesServlet','codigo','codigoCliente',getCodigoClienteEmpCallBack);
	});
	if($("#codigoCliente").val() != null){
		getAjax('clientesServlet','codigo','codigoCliente',getCodigoClienteEmpCallBack);
	}
	
	$('#codigoTipoElemento').blur(function(){
		getAjax('tipoElementoDescripcionServlet','codigo','codigoTipoElemento',getCodigoTipoElementoCallBack);
	});
	
	$('#codigoElemento').blur(function(){
		getAjaxElemento('elementosServlet', 'codigo', "codigoElemento", getCodigoElementoCallBack);
	});
	
	if($('#mostrarAnulados').is(':checked')){
		$("#factura thead th:nth-child(6)").css('display','');
		$('.estado').css('display','');
	}else{
		$("#factura thead th:nth-child(6)").css('display','none');
		$('.estado').css('display','none');
	}
	
});

function volver(){
	document.location="menu.html";
}

function anular(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Anular',function(r) {
		    if(r){
		    	document.location="anularFactura.html?id="+id;
		    }
		});
	}
}

function consultar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioFactura.html?accionFactura=CONSULTA&idFactura="+id;
}
function modificar(id){
	if(id!=null)
		document.location="iniciarPrecargaFormularioFactura.html?accionFactura=MODIFICACION&idFactura="+id;
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarFactura.html?id="+id;
		    }
		});
	}
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteAspId").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

//ajax Deposito Actual
function getAjaxDepositoActual(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteAspId").val()+'&sucursalId='+$('#sucursalId').val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

//ajax Deposito Actual
function getAjaxElemento(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value
			+ '&clienteId='+$("#clienteAspId").val()
			+ '&codigoDepositoActual'+$('#codigoDepositoActual').val()
			+ '&codigoTipoElemento='+$('#codigoTipoElemento').val()
			+ '&codigoClienteEmp='+$('#codigoCliente').val();
			
	var request = new HttpRequest(url, callBack);
	request.send();	
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupSucursal(){
	if($("#codigoEmpresa").val()==""){
		var tittle = $('#tituloError').val();
		var message =$('#codigoEmpresaRequerido').val(); 
		jAlert(message,tittle);
		return false;
	}
	codigoEmpresa =  $("#codigoEmpresa").val();
	
	var url = "mostrarListaComprobantesFactura.html?"
				+ "&codigoEmpresa=" + codigoEmpresa;
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
        context: $(".displayTagDiv."+ "sucursalesPopupMap")
    });
	popupClaseNombre = 'sucursalesPopupMap';
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+'sucursalesPopupMap');
	popupOnDiv(div,'darkLayer');
}

function abrirPopupClienteEmp(){
	if($("#codigoEmpresa").val()==""){
		var tittle = $('#tituloError').val();
		var message =$('#codigoEmpresaRequerido').val(); 
		jAlert(message,tittle);
		return false;
	}
	codigoEmpresa =  $("#codigoEmpresa").val();
	
	var url = "mostrarListaComprobantesFactura.html?"
				+ "&codigoEmpresa=" + codigoEmpresa;
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
        context: $(".displayTagDiv."+ "clienteEmpPopupMap")
    });
	popupClaseNombre = 'clienteEmpPopupMap';
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+'clienteEmpPopupMap');
	popupOnDiv(div,'darkLayer');
}
function abrirPopupSeries(){
	if($("#codigoEmpresa").val()==""){
		var tittle = $('#tituloError').val();
		var message =$('#codigoEmpresaRequerido').val(); 
		jAlert(message,tittle);
		return false;
	}
	codigoEmpresa =  $("#codigoEmpresa").val();
	codigoSucursal = $('#codigoSucursal').val();
	idAfipTipoComprobante = $('#idAfipTipoComprobante').val();
	var url = "mostrarListaComprobantesFactura.html?"
				+ "&codigoEmpresa=" + codigoEmpresa
				+"&codigoSucursal=" + codigoSucursal
				+"&idAfipTipoComprobante=" + idAfipTipoComprobante;
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
        context: $(".displayTagDiv."+ "seriesPopupMap")
    });
	popupClaseNombre = 'seriesPopupMap';
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+'seriesPopupMap');
	popupOnDiv(div,'darkLayer');
}


function callBackAbrirPopupElemento(data){
	filteredResponse =  $(data).find(this.selector);
    if(filteredResponse.size() == 1){
         $(this).html(filteredResponse);                            
    }else{
         $(this).html(data);
    }
    $(this).displayTagAjax();
}

//ajax
function getSucursalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#codigoSucursalLabel").html("");
		$("#codigoSucursal").val();
	}
}

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresaLabel").html("");
		$("#codigoSucursalLabel").html("");
		$("#codigoSerieLabel").html("");
		$("#codigoSucursal").val("");
		$("#codigoSerie").val("");
	}
}

//ajax
function getSerieCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSerieLabel").html(sResponseText);
	else{
		$("#codigoSerie").val("");
		$("#codigoSerieLabel").html("");
	}
}

function getCodigoTipoElementoCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoTipoElementoLabel").html(sResponseText);	
	else{			
		$("#codigoTipoElemento").html("");
		$("#codigoTipoElementoLabel").html("");		
	}
}

function getCodigoClienteEmpCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);	
	else{			
		$("#codigoCliente").html("");
		$("#codigoClienteLabel").html("");		
	}
}

function getCodigoElementoCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoElementoLabel").html(sResponseText);	
	else{			
		$("#codigoElemento").html("");
		$("#codigoElementoLabel").html("");		
	}
}



