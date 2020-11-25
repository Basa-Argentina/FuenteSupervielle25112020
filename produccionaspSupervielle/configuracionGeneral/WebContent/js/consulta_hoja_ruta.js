var menuAbierto=false;

$(document).ready(function() {
	//tabla cliente
	$("#hojaRuta tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#hojaRuta tbody tr").mouseout(function(){
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
	$('#busquedaDiv > table').addClass('seccion');
	
	/**/
	//Tooltips
	$("img[title]").tooltip();
	
		
	//PopUp
      $('#hojaRuta tbody tr').contextMenu('myMenu1', {
 
        bindings: {
 
          'consultar': function(t) {
            consultar($(t).find('#hdn_id').val());
          },
 
          'modificar': function(t) {
            modificar($(t).find('#hdn_id').val(),$(t).find('#hdn_estado').val());
          },
 
          'eliminar': function(t) {
        	 eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());
          },
          'imprimir': function(t) {
              imprimir($(t).find('#hdn_id').val());
          },
          'chequear': function(t) {
        	  chequear($(t).find('#hdn_id').val(),$(t).find('#hdn_estado').val());
          }
 
        },
		onShowMenu: function(e, menu) {
			menuAbierto=true;
			return menu;

		},
		onHide: function(){
			menuAbierto=false;
			$("#hojaRuta tbody tr").removeClass('tr_mouseover');
		}
 
      }); 
      
    //binding OnBlur() para los campos con popup	
	$("#codigoEmpresa").blur(function(){
		searchAjax('seleccionEmpresa.html','codigoEmpresa',null,getEmpresaCallBack);
	});
	$("#codigoSucursal").blur(function(){
		searchAjax('seleccionSucursal.html','codigoSucursal','codigoEmpresa',getSucursalCallBack);
	});
	$("#codigoCliente").blur(function(){
		searchAjax('seleccionCliente.html','codigoCliente','codigoEmpresa',getClienteCallBack);
	});
	
	$("#codigoSerie").blur(function(){
		getAjax('serieServletPorCodigoReturnPrefijo','codigo','codigoSerie',getCodigoSerieCallBack);
	});
	if($("#codigoSerie").val() != null){
		getAjax('serieServletPorCodigoReturnPrefijo','codigo','codigoSerie',getCodigoSerieCallBack);
	}
	
	
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,null,"empresasPopupMap", null);
	});
	$("#buscaSucursal").click(function(){
		abrirPopupSeleccion("popUpSeleccionSucursal.html","codigoEmpresa","sucursalesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	
	$("#buscaTransporte").click(function(){
		abrirPopupSeleccion("popUpTransporte.html","codigoEmpresa","transportesPopupMap",$("#mensajeSeleccioneEmpresa").val());
	});
	
	$("#buscaSerie").click(function(){
		abrirPopupSeleccion("popUpSeleccionSerie.html",null,null, null);
	});
	
	$("#codigoEmpresa").trigger('blur');
	
	$("#nuevaHojaRuta").click(function(){
		document.location="precargaFormularioHojaRuta.html?accion=NUEVO";
	});
	
});
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
function getCodigoSerieCallBack(sResponseText) {
	
	if (sResponseText != 'null' && sResponseText != "") {
		var array = sResponseText.split('-');
		$("#codigoSerieLabel").html(array[0]);
		
	} else {
		$("#codigoSerie").val("");
		$("#codigoSerieLabel").html("");
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
		$("#codigoCliente").val("");
	}
	$("#codigoSucursal").trigger('blur');
	$("#codigoCliente").trigger('blur');
}
function getSucursalCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoSucursalLabel").html(sResponseText);
	else{
		$("#codigoSucursalLabel").html("");
		$("#codigoSucursal").val("");
	}
}
function getClienteCallBack(sResponseText){
	if (sResponseText != 'null' && sResponseText != "")
		$("#codigoClienteLabel").html(sResponseText);
	else{
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
}
function consultar(id){
	if(id!=null)
		document.location="precargaFormularioHojaRuta.html?accion=CONSULTA&id="+id;
}
function modificar(id, estado){
	if(estado=='Pendiente'){
		if(id!=null)
			document.location="precargaFormularioHojaRuta.html?accion=MODIFICACION&id="+id;
	}else{
		jAlert('Seleccione una hoja de ruta en estado pendiente');
	}	
}

function imprimir(id){
	if(id!=null)
		window.open('imprimirHojaRuta.html?id='+id);
}

function chequear(id, estado){
	if(estado=='Pendiente'){
		if(id!=null)
			document.location="chequearHojaRuta.html?id="+id;
	}else{
		jAlert('Seleccione una hoja de ruta en estado pendiente');
	}
	
}

function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
		    	document.location="eliminarHojaRuta.html?id="+id;
		    }
		});		
	}
}
function buscarFiltro(){
	document.forms[0].submit();
}
function borrarFiltros(){
	document.location="borrarFiltrosHojaRuta.html";
}
function volver(){
	document.location="menu.html";
}
