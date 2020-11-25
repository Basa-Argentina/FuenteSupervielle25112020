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
	// Luis Inicio
	/*if($("#codigoPersonal").val() != null){
		if($("#codigoPersonal").val() == 5077) {
			
			
			$('#buscaPersonal').removeAttr('disabled');
			$('#codigoPersonal').removeAttr('readonly');
			$('#codigoPersonal').val("");
			//value="<c:out value="${requerimientoBusqueda.codigoPersonal}"" +
			
			//		$("#codigoPersonal").test="${permiteCambiarPersonal==null || permiteCambiarPersonal==false}"
		}
		else{
			getAjaxEmpleado('empleadosReturnCodigoDireccionServlet','codigo','codigoPersonal',getPersonalCallBack);
		}
	}
	*/
	//Luis Fin
	
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
	
	
});

function agregarRequerimiento(){
	//if($("#clienteCodigo").val()!=null && $("#clienteCodigo").val()!=''){
	//document.location="precargaFormularioRequerimientoWeb.html?clienteCodigoString="+$("#clienteCodigo").val();
	document.location="precargaFormularioRequerimientoWeb.html";
	//}else{
		//jAlert("Seleccione un cliente");
	//}
}
function consultar(id){
	if(id!=null){
		document.location="precargaFormularioRequerimientoWeb.html?accion=CONSULTA&id="+id+"&clienteCodigoString="+$("#clienteCodigo").val();
	}	
}
function modificar(id, mensaje, estado){
	if(estado != undefined && estado != null && mensaje!=undefined && estado != 'Pendiente'){
		jAlert(mensaje);
		return;
	}
	if(id!=null){
		document.location="precargaFormularioRequerimientoWeb.html?accion=MODIFICACION&id="+id+"&clienteCodigoString="+$("#clienteCodigo").val();
	}	
}
function eliminar(mensaje, id, mensajeEliminar, estado){
	if(estado != undefined && estado != null && mensajeEliminar!=undefined && estado != 'Pendiente'){
		jAlert(mensajeEliminar);
		return;
	}
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarRequerimientoWeb.html?id="+id;
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
		    	document.location="cancelarRequerimientoWeb.html?id="+id;
		    }
		});
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosRequerimientoWeb.html";
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
			+ $("#clienteId").val() ;
	if($("#bandera").val()==null || $("#bandera").val()!="true"){
		url += '&clienteEmpId='+  $("#clienteCodigo").val();
	}
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