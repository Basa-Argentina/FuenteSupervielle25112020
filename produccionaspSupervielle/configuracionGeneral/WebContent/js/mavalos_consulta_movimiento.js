var menuAbierto=false;

$(document).ready(function(){
	//tabla Movimiento
	$("#movimiento tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#movimiento tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});	
	
	//PopUp con Anular movimiento
    $('#movimiento tbody tr').contextMenu('myMenu1', {

      bindings: {

        'anular': function(t) {
          anular($(t).find('#hdn_id').val());
        }

      },
		onShowMenu: function(e, menu) {
			var cell = e.currentTarget.children[1].children[0].value;
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
			$("#movimiento tbody tr").removeClass('tr_mouseover');
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
	
	$('#botonCargar').click(function(){
		$(this).attr('disabled', 'disabled');
		codDepAct = $('#codigoDepositoActual').val();		
		//if(codDepAct != null && codDepAct!=""){
			document.forms[0].submit();		;
//		}else{			
//			var tittle = $('#tituloError').val();
//			var message =$('#codigoDepositoRequerido').val(); 
//			jAlert(message,tittle);
//			$('#botonCargar').removeAttr('disabled');
//		}		
	});
	
	$('#tipoMovimientoInt').val($('#tipoMovimientoSelected').val());
	
	$('#botonCargar').removeAttr('disabled');
	
	$('#registrar').click(function(){
		document.location="iniciarPrecargaFormularioMovimiento.html";
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
	
	$('#codigoLectura').numeric({ decimal: false, negative: false});
	$(".numericPositiveIntegerOnly").numeric({ decimal: false, negative: false});
	
	//llamadas a los servlets
	$('#codigoDepositoActual').blur(function(){		
		getAjaxDepositoActual('depositosServlet','codigo','codigoDepositoActual', getCodigoDepositoActualCallBack);
	});
	
	$('#codigoClienteEmp').blur(function(){
		getAjax('clientesServlet','codigo','codigoClienteEmp',getCodigoClienteEmpCallBack);
	});
	
	$('#codigoTipoElemento').blur(function(){
		getAjax('tipoElementoDescripcionServlet','codigo','codigoTipoElemento',getCodigoTipoElementoCallBack);
	});
	
	$('#codigoElemento').blur(function(){
		getAjaxElemento('elementosServlet', 'codigo', "codigoElemento", getCodigoElementoCallBack);
	});
	
	$("#codigoRemito").blur(function(){
		getAjaxRemito('remitoServlet','codigo','codigoRemito',getRemitoCallBack);
	});
	
	
	$("#buscaRemito").click(function(){
		abrirPopupSeleccion("popUpSeleccionRemito.html",'codigoClienteEmp',"remitoPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	
	$("#buscaLectura").click(function(){
		abrirPopupLecturaNuevo();
  	});
	
	$("#codigoLectura").blur(function(){
		getAjax('lecturasServlet','codigo','codigoLectura',getLecturaCallBack);
	});
	 
	$('#codigoDepositoActual').blur();
	$('#codigoClienteEmp').blur();
	$('#codigoTipoElemento').blur();
	$('#codigoElemento').blur();
	$("#codigoLectura").blur();
	
});

function anular(id){
	if(id!=null)
		document.location="anularMovimiento.html?id="+id;
}

function volver(){
	document.location="menu.html";
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
			+ '&codigoClienteEmp='+$('#codigoClienteEmp').val();
			
	var request = new HttpRequest(url, callBack);
	request.send();	
}

function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}

function abrirPopupElemento(){
	if($("#codigoDepositoActual").val()==""){
		var tittle = $('#tituloError').val();
		var message =$('#codigoDepositoRequerido').val(); 
		jAlert(message,tittle);
		return;
	}
	codigoDepositoActual =  $("#codigoDepositoActual").val();
	codigoTipoElemento = $('#codigoTipoElemento').val();
	codigoClienteEmp = $('#codigoClienteEmp').val();
	var url = "mostrarListaMovimientos.html?"
				+ "codigoDepositoActual=" + codigoDepositoActual 
				+ "&codigoTipoElemento=" + codigoTipoElemento
				+ "&codigoClienteEmp=" + codigoClienteEmp;
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
        context: $(".displayTagDiv."+ "elementosPopupMap")
    });
	popupClaseNombre = 'elementosPopupMap';
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+'elementosPopupMap');
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

function getCodigoDepositoActualCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoActualLabel").html(sResponseText);	
	else{			
		$("#codigoDepositoActual").html("");
		$("#codigoDepositoActualLabel").html("");		
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
		$("#codigoClienteEmpLabel").html(sResponseText);	
	else{			
		$("#codigoClienteEmp").html("");
		$("#codigoClienteEmpLabel").html("");		
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

//ajax
function getRemitoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoRemitoLabel").html(sResponseText);
	}else{
		$("#codigoRemitoLabel").html("");
		$("#codigoRemito").val("");
	}
}

//ajax
function getAjaxRemito(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#codigoClienteEmp").val();	
	var request = new HttpRequest(url, callBack);
	request.send();	
}

//ajax
function getAjax(url, varName, elementName, callBack){
	var input = document.getElementById(elementName);
	if(input == null)
		return;
	var url = url+'?'+varName+'='+input.value+'&clienteId='+$("#clienteId").val()/*+'&utilizada=false'*/;	
	var request = new HttpRequest(url, callBack);
	request.send();	
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
	if (sResponseText != 'null' && sResponseText != "")
	{
		$("#codigoLecturaLabel").html(sResponseText);	
	}
	else
	{
		$("#codigoLecturaLabel").html("");
		$("#codigoLectura").val("");
	}	
}

