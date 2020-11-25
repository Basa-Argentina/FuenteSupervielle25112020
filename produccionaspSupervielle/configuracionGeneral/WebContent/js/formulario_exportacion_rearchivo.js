var menuAbierto=false;
$(document).ready(function() {
	
	//Tabla Lotes de Rearchivo
	$("#loteRearchivo tbody tr").mouseover(function(){
		if(!menuAbierto)
			$(this).addClass('tr_mouseover');
			
	});
	$("#loteRearchivo tbody tr").mouseout(function(){
		if(!menuAbierto)
			$(this).removeClass('tr_mouseover');
	});
	//PopUp
	if( $('#accion').val() != "CONSULTA" ){
	    $('#loteRearchivo tbody tr').contextMenu('myMenu1', {
	    	bindings: {
	    		'eliminar': function(t) {
	    			eliminar($(t).find('#hdn_eliminar').val(),$(t).find('#hdn_id').val());
	    		},
	    	},
	    	onShowMenu: function(e, menu) {
	    		menuAbierto=true;
	    		return menu;
	    	},
	    	onHide: function(){
	    		menuAbierto=false;
	    		$("#loteRearchivo tbody tr").removeClass('tr_mouseover');
	    	}
	    }); 
	}
	
	$("#btnGuardar").click(function(){
		document.forms[0].submit();
	});
	$("#btnCancelar").click(function(){
		document.location="consultaExportacionRearchivo.html";
	});
	$("#btnVolver").click(function(){
		document.location="consultaExportacionRearchivo.html";
	});
	
	
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});
	$('#busquedaDiv > table').addClass('seccion');
	
	//Slide 1
	$('#grupoImg').click(function() {
		$('#grupoDiv').slideToggle('slideUp');
		$('#grupoImgSrcDown').slideToggle('slideUp');
		$('#grupoImgSrc').slideToggle('slideUp');
	});
	
	//Tooltips
	$("img[title]").tooltip();
	
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
	$("#codigoClasificacionDocumental").blur(function(){
		searchAjax('seleccionClasificacionDocumental.html','codigoClasificacionDocumental','codigoCliente',getClasificacionDocumentalCallBack);
	});
	
	$("#buscaEmpresa").click(function(){
		abrirPopupSeleccion("popUpSeleccionEmpresa.html",null,"empresasPopupMap", null);
	});
	$("#buscaSucursal").click(function(){
		abrirPopupSeleccion("popUpSeleccionSucursal.html","codigoEmpresa","sucursalesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaCliente").click(function(){
		abrirPopupSeleccion("popUpSeleccionCliente.html","codigoEmpresa","clientesPopupMap", $("#mensajeSeleccioneEmpresa").val());
	});
	$("#buscaClasificacionDocumental").click(function(){
		abrirPopupSeleccion("popUpSeleccionClasificacionDocumental.html",'codigoCliente',"clasificacionDocumentalPopupMap", $("#mensajeSeleccioneCliente").val());
	});
	
	$("#codigoEmpresa").trigger('blur');
	$("#codigoClasificacionDocumental").trigger('blur');
	
});

function agregarLoteRearchivo(){
	abrirPopupSeleccion("popUpSeleccionLoteRearchivoDigital.html?codigo2="+$("#codigoCliente").val(), 'codigoClasificacionDocumental', null, $("#mensajeSeleccioneClasificacionDocumental").val());
}
function eliminar(mensaje, id){
	if(id!=null && id!=undefined && mensaje!=undefined){
		jConfirm(mensaje, 'Confirmar Eliminar',function(r) {
		    if(r){
				$("#codigoLoteRearchivoEliminar").val(id);
				document.forms[0].submit();
		    }
		});
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
	$("#codigoClasificacionDocumental").trigger('blur');
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
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClienteLabel").html(sResponseText);
	} else {
		$("#codigoClienteLabel").html("");
		$("#codigoCliente").val("");
	}
	$("#codigoClasificacionDocumental").trigger('blur');
}
function getClasificacionDocumentalCallBack(sResponseText){	
	if (sResponseText != 'null' && sResponseText != ""){
		$("#codigoClasificacionDocumentalLabel").html(sResponseText);
	}else{
		$("#codigoClasificacionDocumentalLabel").html("");
		$("#codigoClasificacionDocumental").val("");
	}
}

