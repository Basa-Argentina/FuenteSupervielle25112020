var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#operacion tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#operacion tbody tr").mouseout(function(){
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
	
	//PopUp
    $('#operacion tbody tr').contextMenu('myMenu1', {

      bindings: {

        'consultar': function(t) {
          consultar($(t).find('#hdn_id').val());
        },
        'finalizar': function(t) {
            finalizar($(t).find('#hdn_id').val(),$(t).find('#hdn_estado').val(),$(t).find('#hdn_finalizarOK').val()
            		,$(t).find('#hdn_finalizarError').val(),$(t).find('#hdn_traspasar').val(),$(t).find('#hdn_tipoCalculo').val()
            		,$(t).find('#hdn_cantidad_pendientes').val(),$(t).find('#hdn_genera_operac_siguiente').val());
        },
        'cancelar': function(t) {
            cancelar($(t).find('#hdn_id').val(),$(t).find('#hdn_estado').val());
        },
        'imprimir': function(t) {
            imprimir($(t).find('#hdn_id').val(), $(t).find('#hdn_tipoOperacion').val());
        },
        'cambiarTipo': function(t) {
            cambiarTipo($(t).find('#hdn_id').val());
        }
          

      },
		onShowMenu: function(e, menu) {
			var cell = e.currentTarget.children[1].children[0].value;
			var num = 1;
			if(cell == 'false'){
				menuAbierto=true;
				$('#imprimir', menu).remove();
				return menu;
			}else{
				menuAbierto=true;
				return menu;
			}

		},
		onHide: function(){
			menuAbierto=false;
			$("#requerimiento tbody tr").removeClass('tr_mouseover');
		}

    }); 

	
	/**/
	//Tooltips
	$("img[title]").tooltip();

      $(".integer").numeric(false, function() { 
  		this.value = ""; 
  		this.focus(); });
     
      
 
  //binding OnBlur() para los campos con popup
    $("#clienteCodigo").blur(function(){
  		getAjax('clientesServlet','codigo','clienteCodigo',getClienteCallBack);
  	});
  	if($("#clienteCodigo").val() != null){
  		getAjax('clientesServlet','codigo','clienteCodigo',getClienteCallBack);
  	}  
	$("#codigoEmpresa").blur(function(){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		searchAjax('seleccionSucursal.html','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	});
	$("#codigoDeposito").blur(function(){
		searchAjax('seleccionDeposito.html','codigoDeposito','codigoSucursal',getDepositoCallBack);
	});
	$("#codigoRequerimiento").blur(function(){
		searchAjaxReq('seleccionRequerimiento.html','codigoRequerimiento','codigoSucursal','codigoEmpresa',getRequerimientoCallBack);
	});
	$("#codigoTipoOperacion").blur(function(){
		//getAjax('tipoOperacionServlet','codigo','codigoTipoOperacion',getTipoOperacionCallBack);
		getAjax('tipoOperacionesServlet','codigo','codigoTipoOperacion',getTipoOperacionCallBack);
	});
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,null,"empresasPopupMap", null);
	});
	$("#buscaSucursal").click(function(){
		abrirPopupSeleccion("popUpSeleccionSucursal.html","codigoEmpresa","sucursalesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaDeposito").click(function(){
		abrirPopupSeleccion("popUpSeleccionDepositoPorSucursal.html","codigoSucursal","depositosPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaRequerimiento").click(function(){
		abrirPopupSeleccionRequerimiento("popUpSeleccionRequerimiento.html","codigoSucursal","codigoEmpresa","requerimientosPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaTipoOperacion").click(function(){
		//abrirPopupSeleccion("popUpSeleccionTipoOperacion.html",null,null, null);
		abrirPopupTiposOperaciones();
  	});
	if($("#codigoDeposito").val() != null){
		searchAjax('seleccionDeposito.html','codigoDeposito','codigoSucursal',getDepositoCallBack);
	}
	if($("#codigoRequerimiento").val() != null){
		searchAjaxReq('seleccionRequerimiento.html','codigoRequerimiento','codigoSucursal','codigoEmpresa',getRequerimientoCallBack);
	}
	if($("#codigoEmpresa").val() != null){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	}
	if($("#codigoSucursal").val() != null){
		searchAjax('seleccionSucursal.html','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	}
	if($("#codigoTipoOperacion").val() != null){
		getAjax('tipoOperacionesServlet','codigo','codigoTipoOperacion',getTipoOperacionCallBack);
	}
	$("#codigoDireccion").blur(function(){
		getAjax('direccionesEntregaServlet','codigo','codigoDireccion',getDireccionCallBack);
  	});
	if($("#codigoDireccion").val() != null){
		getAjax('direccionesEntregaServlet','codigo','codigoDireccion',getDireccionCallBack);
	}
	$("#codigoPersonal").blur(function(){
		getAjaxEmpleado('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal',getPersonalCallBack);
	});
	if($("#codigoPersonal").val() != null){
		getAjaxEmpleado('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal',getPersonalCallBack);
	}
	$("#buscaPersonal").click(function(){
		abrirPopupSeleccionPersonalSolicitanteODireccion("popUpSeleccionEmpleadoSolicitante.html",'clienteCodigo',"personalPopupMap", $("#mensajeSeleccioneCliente").val());
  	});
	$("#buscaDireccion").click(function(){
		abrirPopupSeleccionPersonalSolicitanteODireccion("popUpSeleccionDirecciones.html",'clienteCodigo',"direccionesPopupMap", $("#mensajeSeleccioneCliente").val());
  	});
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionClienteEmpresaDefecto.html",null,null, null);
  	});
});


function abrirPopupTiposOperaciones(){
	var url = "popUpSeleccionTiposOperaciones.html";
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

function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosOperacion.html";
}

function volver(){
	document.location="menu.html";
}

function consultar(id){
	if(id!=null)
		document.location="iniciarOperacionReferenciaWeb.html?idOperacion="+id;
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
function getDepositoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDepositoLabel").html(sResponseText);
	else{
		$("#codigoDepositoLabel").html("");
		$("#codigoDeposito").val("");
	}
}

//ajax
function getRequerimientoCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoRequerimientoLabel").html(sResponseText);
	else{
		$("#codigoRequerimientoLabel").html("");
		$("#codigoRequerimiento").val("");
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

//ajax
function getEmpresaCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoEmpresaLabel").html(sResponseText);
	else{
		$("#codigoEmpresaLabel").html("");
		$("#codigoEmpresa").val("");
		$("#codigoSucursal").val("");
	}
	$("#codigoSucursal").trigger('blur');
}
function getSucursalCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#codigoSucursalLabel").html("");
		$("#codigoSucursal").val("");
		$("#codigoDeposito").val("");
	}
	$("#codigoDeposito").trigger('blur');
}

function finalizar(id, estado, finOK, finError, traspE, tipoCalculo, cantidadPendientes, operacSiguiente) {
	
	if(estado != 'Pendiente' && estado != 'En Proceso'){
		jAlert($("#errorFinalizarPendienteOEnProceso").val(),"Error");
		return;
	}
	
	var accionGuardar = '';
	
	if(finOK == 'true' && traspE=='false'){
		jConfirm($("#preguntaFinalizarOK").val(), 'Confirmar Finalizar',function(r) {
		    if(r){
		    	accionGuardar='finalizarOK';
		    	verificarTipoCalculo(id, accionGuardar, tipoCalculo);
		    }
		});
	}
	if(finOK=='true' && traspE=='true'){
		jConfirm($("#preguntaFinalizarOKConTraspaso").val(), 'Confirmar Finalizar',function(r) {
		    if(r){
		    	accionGuardar='finalizarOKConTraspaso';
		    	verificarTipoCalculo(id, accionGuardar, tipoCalculo);
		    }
		});
	}
	if(finOK == 'false' && traspE == 'true'){
		jConfirm($("#preguntaTraspaso").val(), 'Confirmar Guardar',function(r) {
		    if(r){
		    	accionGuardar='traspaso';
		    	verificarTipoCalculo(id, accionGuardar, tipoCalculo);
		    }
		});
	}
	if(finOK=='false' && finError=='true'){
		jConfirm($("#preguntaFinalizarError").val(), 'Confirmar Guardar',function(r) {
		    if(r){
		    	accionGuardar='finalizarError';
		    	verificarTipoCalculo(id, accionGuardar, tipoCalculo);
		    }
		});
	}
	if(finOK=='false' && finError=='true' && traspE == 'true'){
		jConfirm($("#preguntaFinalizarErrorConTraspaso").val(), 'Confirmar Guardar',function(r) {
			if(r){
		    	accionGuardar='finalizarErrorConTraspaso';
		    	verificarTipoCalculo(id, accionGuardar, tipoCalculo);
		    }
		});
	}
	
	if(cantidadPendientes!=null && cantidadPendientes=='0'){
		
		if(operacSiguiente!=null){
			if(operacSiguiente=='true'){
				jConfirm($("#preguntaFinalizarOKConTraspasoSinElementos").val(), 'Confirmar Finalizar',function(r) {
				    if(r){
				    	accionGuardar='finalizarOKConTraspaso';
				    	verificarTipoCalculo(id, accionGuardar, tipoCalculo);
				    }
				});
			}
			else{
				jConfirm($("#preguntaFinalizarOKSinElementos").val(), 'Confirmar Finalizar',function(r) {
				    if(r){
				    	accionGuardar='finalizarOK';
				    	verificarTipoCalculo(id, accionGuardar, tipoCalculo);
				    }
				});
			}
				
		}
	}
	else{
		if(finOK == 'false' && traspE=='false' && finError=='false'){
			jAlert($("#errorElementosPendientes").val(),"Error");
			return;
		}	
	}
}

function imprimir(id,envio){
	if(envio=="false")
		$("#imprimir").attr('disabled', 'disabled');
	else if(envio=="true"){
		if(id!=null)
			//document.location="imprimirOperacion.html?id="+id;
			window.open('imprimirOperacion.html?id='+id);
	}
}

function cambiarTipo(id){
	if(id!=null)
		document.location="cambiarTipoOperacion.html?id="+id+"&accion=MOSTRAR";
}

function cancelar(id, estado) {
	
	if(estado != 'Pendiente' && estado != 'En Proceso'){
		jAlert($("#errorCancelarPendienteOEnProceso").val(),"Error");
		return;
	}
	else{
		jConfirm($("#preguntaCancelar").val(), 'Confirmar Cancelar',function(r) {
		    if(r){
		    	document.location="cancelarOperacionReferencia.html?idOperacion="+id;
		    }
		});
	}
	
}

function verificarTipoCalculo(id, accionGuardar, tipoCalculo){
	if(tipoCalculo == 'Manual'){
		jPrompt('Cantidad', '1', 'Ingrese cantidad a facturar', function(r) {
		    if( r ){
		    	if(validarInt(r)){
		    		$("#cantidadTipoCalculo").val(r);
		    		document.location="finalizarOperacionReferencia.html?idOperacion="+id+'&accionGuardar='+accionGuardar+'&tipoCalculo='+tipoCalculo+'&cantidadTipoCalculo='+r;
		    	}
		    	else
		    		verificarTipoCalculo();
		    }
		    else{
		    	verificarTipoCalculo();
		    }
		});
	}
	else{
		document.location="finalizarOperacionReferencia.html?idOperacion="+id+'&accionGuardar='+accionGuardar+'&tipoCalculo='+tipoCalculo;
	}
}

function abrirPopupSeleccionRequerimiento(url,inputValue,inputValue2,nombreClase, mensaje){

		if(url.indexOf("?") != -1)
			url = url+'&codigoSucursal='+$("#"+inputValue).val()+"&codigoEmpresa="+$("#"+inputValue2).val();
		else
			url = url+'?codigoSucursal='+$("#"+inputValue).val()+"&codigoEmpresa="+$("#"+inputValue2).val();
	
	if($('#pop').length>0){
		jQuery.ajax({
	        url: url,
	        success: function(data){
	           $(this).html(data);
	           $(".displayTagDiv").displayTagAjax();
	           popupOffDiv($('#pop'),'darkLayer');
	           popupOnDiv($(this).find('.darkMiddleClass'),'darkLayer');
	        },
	        data: ({"time":new Date().getTime()}),
	        context: $(".selectorDiv"),
	        beforeSend:function(){
	        	popupOnDiv($('#pop'),'darkLayer');
	        },
	        error:function(){
	        	jAlert("Ha ocurrido un error...");
	        	popupOffDiv($('#pop'),'darkLayer');
	        }
	    });
	}else {
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
}

//ajax
function searchAjaxReq(url, elementName,dependence,dependence2, callBack){
	var value = $("#"+elementName).val();
	if(url.indexOf("?") != -1)
		url = url+'&codigo='+value;
	else
		url = url+'?codigo='+value;;
	if(dependence){
		url = url+'&codigoSucursal='+$("#"+dependence).val();
	}
	if(dependence2){
		url = url+'&codigoEmpresa='+$("#"+dependence2).val();
	}
	var request = new HttpRequest(url, callBack);
	request.send();	
}
//ajax
function getAjaxEmpleado(url, varName, elementName, callBack) {
	var input = document.getElementById(elementName);
	if (input == null)
		return;
	var url = url + '?' + varName + '=' + input.value + '&clienteId='
			+ $("#clienteId").val() + '&clienteEmpId='+  $("#clienteCodigo").val() ;
	var request = new HttpRequest(url, callBack);
	request.send();
}

function abrirPopupSeleccionPersonalSolicitanteODireccion(url,inputValue,nombreClase, mensaje){
	if(inputValue!=null){
		if($("#"+inputValue).val()==null || $("#"+inputValue).val()==""){
			jAlert(mensaje);
			return;
		}
		url = url+"?clienteCodigoString="+$("#"+inputValue).val();
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
function getPersonalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split('-');
		$("#codigoPersonalLabel").html(array[0]);
	}
	else{
		$("#codigoPersonal").val("");
		$("#codigoPersonalLabel").html("");
	}
}
//ajax
function getDireccionCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoDireccionLabel").html(sResponseText);
	else{
		$("#codigoDireccion").val("");
		$("#codigoDireccionLabel").html("");
	}
}
//ajax
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#clienteCodigoLabel").html(sResponseText);
	else{
		$("#clienteCodigo").val("");
		$("#clienteCodigoLabel").html("");
	}
}