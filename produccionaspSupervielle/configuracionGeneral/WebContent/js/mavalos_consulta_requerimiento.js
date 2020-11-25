var menuAbierto=false;

$(document).ready(function() {
	//tabla user
	$("#requerimiento tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#requerimiento tbody tr").mouseout(function(){
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
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
      $('#requerimiento tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'consultar': function(t) {
            consultar($(t).find('#hdn_id').val());
          },
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val(),$(t).find('#hdn_modificar').val(),$(t).find('#hdn_estado').val());
 
          },
 
          'eliminar': function(t) {
 
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val(),$(t).find('#hdn_eliminarNoPendientes').val(),$(t).find('#hdn_estado').val());
 
          },
 
          'cancelar': function(t) {
 
        	 cancelar($(t).find('#hdn_cancelar').val(),$(t).find('#hdn_id').val(),$(t).find('#hdn_cancelarMensaje').val(),$(t).find('#hdn_estado').val());
 
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#requerimiento tbody tr").removeClass('tr_mouseover');
		}
 
      }); 

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
	$("#codigoDireccion").blur(function(){
		getAjax('direccionesEntregaServlet','codigo','codigoDireccion',getDireccionCallBack);
  	});
	if($("#codigoDireccion").val() != null){
		getAjax('direccionesEntregaServlet','codigo','codigoDireccion',getDireccionCallBack);
	}
	$("#codigoSerie").blur(function(){
		getAjax('serieServletPorCodigoReturnPrefijo','codigo','codigoSerie',getCodigoSerieCallBack);
	});
	if($("#codigoSerie").val() != null){
		getAjax('serieServletPorCodigoReturnPrefijo','codigo','codigoSerie',getCodigoSerieCallBack);
	}
	$("#tipoRequerimientoCod").blur(function(){
		getAjax('tipoRequerimientoServlet','codigo','tipoRequerimientoCod',getTipoRequerimientoCallBack);
	});
	if($("#tipoRequerimientoCod").val() != null){
		getAjax('tipoRequerimientoServlet','codigo','tipoRequerimientoCod',getTipoRequerimientoCallBack);
	}
	$("#codigoPersonal").blur(function(){
		getAjaxEmpleado('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal',getPersonalCallBack);
	});
	if($("#codigoPersonal").val() != null){
		getAjaxEmpleado('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal',getPersonalCallBack);
	}
	
	$("#buscaTipoRequerimiento").click(function(){
		abrirPopupSeleccion("popUpSeleccionTipoRequerimiento.html",null,null, null);
  	});
	
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionClienteEmpresaDefecto.html",null,null, null);
  	});
	
	$("#buscaSerie").click(function(){
		abrirPopupSeleccion("popUpSeleccionSerie.html",null,null, null);
  	});
	
	$("#buscaPersonal").click(function(){
		abrirPopupSeleccionPersonalSolicitanteODireccion("popUpSeleccionEmpleadoSolicitante.html",'clienteCodigo',"personalPopupMap", $("#mensajeSeleccioneCliente").val());
  	});
	$("#buscaDireccion").click(function(){
		abrirPopupSeleccionPersonalSolicitanteODireccion("popUpSeleccionDirecciones.html",'clienteCodigo',"direccionesPopupMap", $("#mensajeSeleccioneCliente").val());
  	});
	
	
	$("#imprimir").click(function(){
		exportarPdf();
  	});
	
});

function exportarPdf(){
	window.open('exportarPdf.html','popupRequerimiento','width=900,height=700,scrollbars=1');
}

function agregarRequerimiento(){
	document.location="precargaFormularioRequerimiento.html";
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioRequerimiento.html?accion=CONSULTA&id="+id;
}
function modificar(id, mensaje, estado){
	if(estado != undefined && estado != null && mensaje!=undefined && estado != 'Pendiente'){
		jAlert(mensaje);
		return;
	}
	if(id!=null)
		document.location="precargaFormularioRequerimiento.html?accion=MODIFICACION&id="+id;
}
function eliminar(mensaje, id, mensajeEliminar, estado){
	if(estado != undefined && estado != null && mensajeEliminar!=undefined && estado != 'Pendiente'){
		jAlert(mensajeEliminar);
		return;
	}
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarRequerimiento.html?id="+id;
		    }
		});
	}
}

function cancelar(mensaje, id, mensajeCancelar, estado){
	
	if(estado != undefined && estado != null && mensajeCancelar!=undefined && estado != 'Pendiente' && estado != 'En Proceso' ){
		jAlert(mensajeCancelar);
		return;
	}
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Cancelar',function(r) {
		    if(r){
		    	document.location="cancelarRequerimiento.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosRequerimiento.html";
}

function volver(){
	document.location="menu.html";
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
function getClienteCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != "")
		$("#clienteCodigoLabel").html(sResponseText);
	else{
		$("#clienteCodigo").val("");
		$("#clienteCodigoLabel").html("");
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
function getCodigoSerieCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		var array = sResponseText.split('-');
		$("#codigoSerieLabel").html(array[0]);
	}
	else{
		$("#codigoSerie").val("");
		$("#codigoSerieLabel").html("");
	}
}

//ajax
function getTipoRequerimientoCallBack(sResponseText){	
	var componentId = "tipoRequerimientoCod";
	setResponce(sResponseText, componentId);
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